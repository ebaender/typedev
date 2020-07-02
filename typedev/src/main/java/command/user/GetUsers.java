package command.user;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import extra.Message;
import extra.JsonStan;
import user.User;

public class GetUsers extends Command {

    public GetUsers(ConcurrentHashMap<String, User> users) {
        super(users);
    }

    @Override
    public JsonObject execute() {
        StringBuilder message = new StringBuilder();
        if (users.size() == 0) {
            // no users are logged in.
            message.append(Message.NOBODY_HERE.toLine());
        }
        users.entrySet().forEach(user -> {
            message.append(user.getValue().getName() + System.lineSeparator());
        });
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStan.MSG, message.toString());
        return jsonResp;
 
    }
    
}