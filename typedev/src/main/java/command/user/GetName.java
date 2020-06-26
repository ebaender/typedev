package command.user;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
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
            message = "You are nobody.\n";
        } else {
            message = "You are " + users.get(key).getName() + ".\n";
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message);
        return jsonResp;
    }

}