package command.session;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import extra.JsonStan;
import session.Session;
import user.User;

public class JoinSession extends Command {

    public JoinSession(String key, String[] args, ConcurrentHashMap<String, User> users) {
        super(key, args, users);
    }

    @Override
    public JsonObject execute() {
        String message = null;
        User client = null;
        if (key != null && (client = users.get(key)) != null) {
            if (args.length > 1) {
                User host = null;
                for (User user : users.values()) {
                    if (user.getName().equals(args[1])) {
                        host = user;
                        break;
                    }
                }
                if (host != null) {
                    if (client.getSession() == null) {
                        Session session = null;
                        if ((session = host.getSession()) != null) {
                            if (!session.isLive()) {
                                session.join(client);
                                client.setSession(session);
                                message = "Joined " + host.getName() + "'s " + session.getLanguage().toUpperCase()
                                        + " session.\n";
                            } else {
                                message = host.getName() + "'s session is already live.\n";
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
        jsonResp.addProperty(JsonStan.MSG, message);
        return jsonResp;
    }
    
}