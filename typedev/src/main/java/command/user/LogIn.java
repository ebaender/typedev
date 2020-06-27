package command.user;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.google.gson.JsonObject;

import command.Command;
import extra.Standard;
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
            // user is not logged in already
            if (args.length >= 3) {
                // received name and password
                String name = args[1];
                String password = args[2];
                if (userExsists(name, users)) {
                    message = name + " is logged in already.\n";
                } else {
                    // no conflict with other user
                    JsonObject authResp = new Authenticate(args).execute();
                    if (authResp.get(Standard.MSG) == null) {
                        // no error during authentication
                        try {
                            encodedKey = generateKey();
                            if (users.get(encodedKey) == null) {
                                User user = new User(name, password);
                                users.put(encodedKey, user);
                                message = "Logged in as " + name + ".\n";
                                jsonResp.addProperty(Standard.KEY, encodedKey);
                            } else {
                                encodedKey = null;
                                message = "You are one unlucky bastard, try again.\n";
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            message = "Your key could not be generated. Sorry about that.\n";
                        }
                    } else {
                        message = authResp.get(Standard.MSG).getAsString();
                    }
                }
            } else {
                message = "Did not receive name and password.\n";
            }
        } else {
            message = "You are logged in as " + users.get(key).getName() + " already.\n";
        }
        jsonResp.addProperty(Standard.MSG, message);
        return jsonResp;

    }

    private String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator aesGen = KeyGenerator.getInstance("AES");
        aesGen.init(256);
        SecretKey key = aesGen.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
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