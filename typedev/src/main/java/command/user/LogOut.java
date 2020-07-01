package command.user;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import command.session.LeaveSession;
import extra.Message;
import extra.Standard;
import user.User;

public class LogOut extends Command {

    public LogOut(String key, ConcurrentHashMap<String, User> users) {
        super(key, users);
    }
    
    @Override
    public JsonObject execute() {
        JsonObject jsonResp = new JsonObject();
        String message = null;
        if (key == null || key.equals("") || users.get(key) == null) {
            message = Message.LOGGED_OUT_ALREADY.toLine();
        } else if (users.get(key) != null) {
            new LeaveSession(key, users).execute();
            String name = users.get(key).getName();
            users.remove(key);
            message = Message.LOGGED_OUT.toLine(name);
        }
        jsonResp.addProperty(Standard.MSG, message);
        return jsonResp;
   }

}