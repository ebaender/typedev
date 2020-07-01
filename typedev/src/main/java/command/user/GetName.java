package command.user;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import extra.Message;
import extra.Standard;
import user.User;

public class GetName extends Command {

    public GetName(String key, ConcurrentHashMap<String, User> users) {
        super(key, users);
    }
    
    @Override
    public JsonObject execute() {
        String message = null;
        if (key == null || key.equals("") || users.get(key) == null) {
            // user is unrecognizable.
            message = Message.YOU_ARE_NOBODY.toLine();
        } else {
            message = Message.YOU_ARE.toLine(users.get(key).getName());
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message);
        return jsonResp;
    }

}