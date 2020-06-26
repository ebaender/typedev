package command.session;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import extra.Standard;
import session.Session;
import user.User;

public class GetSessions extends Command {
    
    public GetSessions(ConcurrentHashMap<String, User> users) {
        super(users);
    }
    
    @Override
    public JsonObject execute() {
        List<Session> knownSessions = new LinkedList<>();
        StringBuilder messageBuilder = new StringBuilder();
        users.entrySet().forEach(user -> {
            Session session = user.getValue().getSession();
            if (session != null && !knownSessions.contains(session)) {
                String live = session.isLive() ? "live " : "";
                messageBuilder.append(live + session.getLanguage().toUpperCase() + " session (");
                session.getUsers().forEach(sessionUser -> messageBuilder.append(sessionUser.getName() + ", "));
                messageBuilder.setLength(messageBuilder.length() - 2);
                messageBuilder.append(")\n");
                knownSessions.add(session);
            }
        });
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, messageBuilder.toString());
        return jsonResp;
    }
}