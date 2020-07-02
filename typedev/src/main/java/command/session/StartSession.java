package command.session;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import standard.JsonStd;
import translator.Message;
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
            // user is logged in.
            Session session = user.getSession();
            if (session != null) {
                // user is part of a session.
                session.start();
                message = Message.STARTING_SESSION.toLine();
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