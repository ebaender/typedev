package servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import extra.Standard;
import session.EmptyLanguageException;
import session.Session;
import user.User;

@WebServlet(name = "CommandServlet", urlPatterns = { "command" }, loadOnStartup = 1)
public class CommandServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private HashMap<String, User> users;
    private HashMap<String, User> usersName;

    @Override
    public void init() throws ServletException {
        users = (HashMap<String, User>) getServletContext().getAttribute("usersByKey");
        usersName = (HashMap<String, User>) getServletContext().getAttribute("usersByName");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
        String command = req.getParameter("command");
        // remove duplicate whitespace before splitting into arguments.
        String[] args = command.replaceAll("\\s+", " ").split(" ");
        JsonObject jsonResp = null;
        switch (args[0]) {
            case "li":
            case "login":
                if (key.equals("")) {
                    jsonResp = login(args);
                } else {
                    jsonResp = new JsonObject();
                    jsonResp.addProperty(Standard.MSG, "You are logged in already.\n");
                }
                break;
            case "lo":
            case "logout":
                jsonResp = logout(key);
                break;
            case "ls":
            case "sessions":
                jsonResp = getSessions();
                break;
            case "lu":
            case "users":
                jsonResp = getUsers();
                break;
            case "wh":
            case "whoami":
                jsonResp = getName(key);
                break;
            case "cr":
            case "create":
                jsonResp = createSession(args, key);
                break;
            case "jn":
            case "join":
                jsonResp = joinSession(args, key);
                break;
            case "lv":
            case "leave":
                jsonResp = leaveSession(key);
                break;
            case "st":
            case "start":
                jsonResp = startSession(key);
                break;
            default:
                jsonResp = new JsonObject();
                jsonResp.addProperty(Standard.MSG, "");
                break;
        }
        resp.getWriter().print(jsonResp);
        System.out.println(getClass() + " responded with " + jsonResp);
    }

    private JsonObject startSession(String key) {
        String message = null;
        User user = users.get(key);
        if (user != null) {
            Session session = user.getSession();
            if (session != null) {
                session.start();
                message = "Starting session...\n";
            } else {
                message = "You need to have a session to start it.\n";
            }
        } else {
            message = "You need to be logged in to start a session.\n";
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message);
        return jsonResp;
    }

    private JsonObject leaveSession(String key) {
        String message = null;
        User user = null;
        if (key != null && (user = users.get(key)) != null) {
            Session session = null;
            if ((session = user.getSession()) != null) {
                String sessionLanguage = session.getLanguage();
                user.setSession(null);
                session.leave(user);
                message = "Left " + sessionLanguage.toUpperCase() + " session.\n";
            } else {
                message = "You already have no session.\n";
            }
        } else {
            message = "You need to be logged in to leave a session.\n";
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message);
        return jsonResp;
    }

    private JsonObject joinSession(String[] args, String key) {
        String message = null;
        User client = null;
        if (key != null && (client = users.get(key)) != null) {
            if (args.length > 1) {
                User host = null;
                if ((host = usersName.get(args[1])) != null) {
                    if (client.getSession() == null) {
                        Session session = null;
                        if ((session = host.getSession()) != null) {
                            if (!session.isLive()) {
                                session.join(client);
                                client.setSession(session);
                                message = "Joined " + host.getName() + "'s " + session.getLanguage().toUpperCase()
                                        + " session.\n";
                            } else {
                                message = host.getName() +"'s session is already live.\n";
                            }
                        } else {
                            message = host.getName() + " has not joined any session.\n";
                        }
                    } else {
                        message = "You are in a session already.\n";
                    }
                } else {
                    message = args[1] + " is not logged in.\n";
                }
            } else {
                message = "No host specified, try again.\n";
            }
        } else {
            message = "You need to be logged in to join a session.\n";
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message);
        return jsonResp;
    }

    private JsonObject createSession(String[] args, String key) {
        String message = null;
        User host = null;
        if (key != null && (host = users.get(key)) != null) {
            if (args.length > 1) {
                if (host.getSession() == null) {
                    Session session;
                    try {
                        session = new Session(args[1].toLowerCase());
                        host.setSession(session);
                        session.join(host);
                        message = "Created " + args[1].toUpperCase() + " session.\n";
                    } catch (EmptyLanguageException e) {
                        message = "Language " + e.getLanguage().toUpperCase() + " is not supported.\n";
                    } catch (IOException e) {
                        message = "Could not find the language directory.\n";
                    }
                } else {
                    message = "You are in a session already.\n";
                }
            } else {
                message = "No language specified, try again.\n";
            }
        } else {
            message = "You need to be logged in to host a session.\n";
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message);
        return jsonResp;
    }

    private JsonObject getSessions() {
        List<Session> knownSessions = new LinkedList<>();
        StringBuilder messageBuilder = new StringBuilder();
        users.entrySet().forEach(user -> {
            Session session = user.getValue().getSession();
            if (session != null && !knownSessions.contains(session)) {
                String live = session.isLive() ? "live " : "";
                messageBuilder.append(live + session.getLanguage().toUpperCase() + " session (");
                session.getUsers().forEach(sessionUser -> messageBuilder.append(sessionUser.getName() + ", "));
                messageBuilder.setLength(messageBuilder.length() - 2);
                messageBuilder.append(")\n");
                knownSessions.add(session);
            }
        });
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, messageBuilder.toString());
        return jsonResp;
    }

    private JsonObject getUsers() {
        StringBuilder messageBuilder = new StringBuilder();
        users.entrySet().forEach(user -> {
            messageBuilder.append(user.getValue().getName() + "\n");
        });
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, messageBuilder.toString());
        return jsonResp;
    }

    private JsonObject getName(String key) {
        String message = null;
        if (key == null || key.equals("") || users.get(key) == null) {
            message = "You are nobody.\n";
        } else {
            message = "You are " + users.get(key).getName() + ".\n";
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message);
        return jsonResp;
    }

    private JsonObject logout(String key) {
        String message = null;
        if (key == null || key.equals("") || users.get(key) == null) {
            message = "You are logged out already.\n";
        } else if (users.get(key) != null) {
            leaveSession(key);
            String name = users.get(key).getName();
            users.remove(key);
            usersName.remove(name);
            message = "Logged out as " + name + ".\n";
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message);
        jsonResp.addProperty(Standard.KEY, "");
        return jsonResp;
    }

    private JsonObject login(String[] args) {
        String encodedKey = "";
        String message = null;
        if (args.length > 1) {
            String name = args[1];
            if (name != null) {
                if (userExsists(name, users)) {
                    message = "User " + name + " exists already, try again. \n";
                } else {
                    try {
                        encodedKey = generateKey();
                        if (users.get(encodedKey) == null) {
                            User user = new User(name);
                            users.put(encodedKey, user);
                            usersName.put(name, user);
                            message = "Logged in as " + name + ".\n";
                        } else {
                            encodedKey = null;
                            message = "You are one unlucky bastard, try again.\n";
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        message = "Your key could not be generated. Sorry about that.\n";
                    }
                }
            } else {
                message = "Could not read name, try again.\n";
            }
        } else {
            message = "Usage: login [name]\n";
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message);
        jsonResp.addProperty(Standard.KEY, encodedKey);
        return jsonResp;
    }

    private String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator aesGen = KeyGenerator.getInstance("AES");
        aesGen.init(256);
        SecretKey key = aesGen.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private boolean userExsists(String name, HashMap<String, User> users) {
        if (name == null || users == null) {
            return false;
        }
        for (Map.Entry<String, User> user : users.entrySet()) {
            if (name.equals(user.getValue().getName())) {
                return true;
            }
        }
        return false;
    }

}