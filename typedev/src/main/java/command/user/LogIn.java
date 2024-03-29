package command.user;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import manager.KeyMan;
import translator.Message;
import standard.JsonStd;
import user.User;

public class LogIn extends Command {

    public LogIn(String key, String[] args, ConcurrentHashMap<String, User> users) {
        super(key, args, users);
    }

    @Override
    public JsonObject execute() {
        JsonObject jsonResp = new JsonObject();
        String encodedKey = "";
        String message = null;
        if (users.get(key) == null) {
            // user is not logged in already.
            if (args.length >= 3) {
                // received name and password.
                String name = args[1];
                String password = args[2];
                if (userExsists(name, users)) {
                    message = Message.OTHER_USER_LOGGED_IN_ALREADY.toLine(name);
                } else {
                    // no conflicts with another user.
                    JsonObject authResp = new Authenticate(args).execute();
                    if (authResp.get(JsonStd.MSG) == null) {
                        // no error occured during authentication.
                        try {
                            encodedKey = KeyMan.getKey();
                            if (users.get(encodedKey) == null) {
                                User user = new User(name, password);
                                users.put(encodedKey, user);
                                message = Message.LOGIN_SUCCESS.toLine(name);
                                jsonResp.addProperty(JsonStd.KEY, encodedKey);
                            } else {
                                encodedKey = null;
                                message = Message.UNLUCKY.toLine();
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            message = Message.KEYGEN_FAILED.toLine();
                        }
                    } else {
                        message = authResp.get(JsonStd.MSG).getAsString();
                    }
                }
            } else {
                message = Message.ARGS_NOT_RECEIVED.toLine();
            }
        } else {
            message = Message.LOGGED_IN_ALREADY.toLine(users.get(key).getName());
        }
        jsonResp.addProperty(JsonStd.MSG, message);
        return jsonResp;

    }

    private boolean userExsists(String name, ConcurrentHashMap<String, User> users) {
        if (name == null || users == null) {
            return false;
        }
        for (Map.Entry<String, User> user : users.entrySet()) {
            if (name.equals(user.getValue().getName())) {
                return true;
            }
        }
        return false;
    }
}