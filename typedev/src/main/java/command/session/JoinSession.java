package command.session;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import standard.JsonStd;
import translator.Message;
import session.Session;
import user.User;

public class JoinSession extends Command {

    public JoinSession(String key, String[] args, ConcurrentHashMap<String, User> users) {
        super(key, args, users);
    }

    @Override
    public JsonObject execute() {
        String message = null;
        User client = users.get(key);
        if (client != null) {
            // requesting user is logged in.
            if (args.length > 1) {
                // received target user argument.
                String hostName = args[1];
                User host = null;
                for (User user : users.values()) {
                    if (user.getName().equals(hostName)) {
                        host = user;
                        break;
                    }
                }
                if (host != null) {
                    // target user is logged in.
                    if (client.getSession() == null) {
                        // client has no other session yet.
                        Session session = host.getSession();
                        if (session != null) {
                            // target user has a session.
                            if (!session.isLive()) {
                                // session has not been started yet.
                                session.join(client);
                                client.setSession(session);
                                message = Message.JOINED_SESSION.toLine(hostName, session.getLanguage().toUpperCase());
                            } else {
                                message = Message.SESSION_ALREADY_LIVE.toLine(hostName);
                            }
                        } else {
                            message = Message.USER_HAS_NO_SESSION.toLine(hostName);
                        }
                    } else {
                        message = Message.IN_SESSION_ALREADY.toLine();
                    }
                } else {
                    message = Message.USER_NOT_LOGGED_IN.toLine(hostName);
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