package servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
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
import user.User;

@WebServlet(name = "CommandServlet", urlPatterns = { "command" }, loadOnStartup = 1)
public class CommandServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private HashMap<String, User> users;

    @Override
    public void init() throws ServletException {
        users = (HashMap<String, User>) getServletContext().getAttribute("users");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
        String command = req.getParameter("command");
        // remove duplicate whitespace before splitting into arguments.
        String[] args = command.replaceAll("\\s+", " ").split(" ");
        JsonObject jsonResp = null;
        switch (args[0]) {
            case "login":
                if (key.equals("")) {
                    jsonResp = login(args);
                } else {
                    jsonResp = new JsonObject();
                    jsonResp.addProperty(Standard.MSG, "You are logged in already.\n");
                }
                break;
            case "logout":
                jsonResp = logout(key);
                break;
            case "list":
                jsonResp = getUsers();
                break;
            case "whoami":
                jsonResp = getName(key);
                break;
            default:
                jsonResp = new JsonObject();
                jsonResp.addProperty(Standard.MSG, "");
                break;
        }
        resp.getWriter().print(jsonResp);
        System.out.println(this + " responded with " + jsonResp);
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
            String name = users.get(key).getName();
            users.remove(key);
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
                            users.put(encodedKey, new User(name));
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