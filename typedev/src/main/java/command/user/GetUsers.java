package command.user;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import extra.Standard;
import user.User;

public class GetUsers extends Command {

    public GetUsers(ConcurrentHashMap<String, User> users) {
        super(users);
    }

    @Override
    public JsonObject execute() {
        StringBuilder messageBuilder = new StringBuilder();
        users.entrySet().forEach(user -> {
            messageBuilder.append(user.getValue().getName() + "\n");
        });
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, messageBuilder.toString());
        return jsonResp;
 
    }
    
}