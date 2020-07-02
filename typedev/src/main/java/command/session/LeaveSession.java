package command.session;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import standard.JsonStd;
import session.Session;
import user.User;

public class LeaveSession extends Command {

    public LeaveSession(String key, ConcurrentHashMap<String, User> users) {
        super(key, users);
    }

    @Override
    public JsonObject execute() {
        String message = null;
        User user = null;
        if (key != null && (user = users.get(key)) != null) {
            // user exists.
            Session session = null;
            if ((session = user.getSession()) != null) {
                // user has a session.
                String sessionLanguage = session.getLanguage();
                user.setSession(null);
                session.leave(user);
                if (session.getUsers().size() > 1) {
                    // user wants to leave a multi player session.
                    user.getManager().updateLeftSession();
                }
                message = "Left " + sessionLanguage.toUpperCase() + " session.\n";
            } else {
                message = "You already have no session.\n";
            }
        } else {
            message = "You need to be logged in to leave a session.\n";
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStd.MSG, message);
        return jsonResp;
    }
    
}