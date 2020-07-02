package command.session;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import standard.JsonStd;
import translator.Message;
import session.Session;
import user.User;

public class LeaveSession extends Command {

    public LeaveSession(String key, ConcurrentHashMap<String, User> users) {
        super(key, users);
    }

    @Override
    public JsonObject execute() {
        String message = null;
        User user = users.get(key);
        if (user != null) {
            // user is logged in.
            Session session = user.getSession();
            if (session != null) {
                // user has a session.
                String sessionLanguage = session.getLanguage();
                user.setSession(null);
                session.leave(user);
                if (session.getUsers().size() > 1) {
                    // user wants to leave a multi player session.
                    user.getDB().updateLeftSession();
                }
                message = Message.LEFT_SESSION.toLine(sessionLanguage.toUpperCase());
            } else {
                message = Message.YOU_HAVE_NO_SESSION.toLine();
            }
        } else {
            message = Message.NEED_LOGIN.toLine();
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStd.MSG, message);
        return jsonResp;
    }
    
}