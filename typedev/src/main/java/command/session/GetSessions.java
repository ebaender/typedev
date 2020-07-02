package command.session;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.JsonObject;

import command.Command;
import standard.JsonStd;
import translator.Message;
import session.Session;
import user.User;

public class GetSessions extends Command {
    
    public GetSessions(ConcurrentHashMap<String, User> users) {
        super(users);
    }
    
    @Override
    public JsonObject execute() {
        List<Session> knownSessions = new LinkedList<>();
        StringBuilder message = new StringBuilder();
        AtomicBoolean noSessions = new AtomicBoolean(true);
        users.entrySet().forEach(user -> {
            Session session = user.getValue().getSession();
            if (session != null && !knownSessions.contains(session)) {
                noSessions.set(false);;
                String live = session.isLive() ? "live " : "";
                message.append(live + session.getLanguage().toUpperCase() + " session (");
                session.getUsers().forEach(sessionUser -> message.append(sessionUser.getName() + ", "));
                message.setLength(message.length() - 2);
                message.append(")\n");
                knownSessions.add(session);
            }
        });
        if (noSessions.get()) {
            message.append(Message.NO_SESSIONS.toLine());
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStd.MSG, message.toString());
        return jsonResp;
    }
}