package command.session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import translator.Message;
import standard.JsonStd;
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
        User host = users.get(key);
        if (host != null) {
            // user is logged in.
            if (args.length > 1) {
                // received language argument.
                if (host.getSession() == null) {
                    // user does not have a session yet.
                    Session session;
                    try {
                        String language = args[1];
                        session = new Session(language.toLowerCase());
                        host.setSession(session);
                        session.join(host);
                        message = Message.CREATED_SESSION.toLine(language.toUpperCase());
                    } catch (EmptyLanguageException e) {
                        message = Message.LANGUAGE_UNSUPPORTED.toLine(e.getLanguage().toUpperCase());
                    } catch (IOException e) {
                        message = Message.LANGUAGE_DIR_MISSING.toLine();
                    }
                } else {
                    message = Message.IN_SESSION_ALREADY.toLine();
                }
            } else {
                message = Message.ARGS_NOT_RECEIVED.toLine();
            }
        } else {
            message = Message.NEED_LOGIN.toLine();
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStd.MSG, message);
        return jsonResp;
    }
    
}