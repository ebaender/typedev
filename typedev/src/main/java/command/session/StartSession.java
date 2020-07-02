package command.session;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import standard.JsonStd;
import session.Session;
import user.User;

    public class StartSession extends Command {

	public StartSession(String key, ConcurrentHashMap<String, User> users) {
		super(key, users);
	}

	@Override
	public JsonObject execute() {
        String message = null;
        User user = getUsers().get(key);
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
        jsonResp.addProperty(JsonStd.MSG, message);
        return jsonResp;
    }
}