package command.session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import extra.Standard;
import session.EmptyLanguageException;
import session.Session;
import user.User;

public class CreateSession extends Command {

    public CreateSession(String key, String args[], ConcurrentHashMap<String, User> users) {
        super(key, args, users);
    }

    @Override
    public JsonObject execute() {
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
    
}