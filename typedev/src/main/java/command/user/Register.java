package command.user;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;

import command.Command;
import extra.DBStandard;
import extra.HttpEndpoint;
import extra.HttpHost;
import extra.HttpMan;
import extra.Message;
import extra.Standard;

public class Register extends Command {

    private final int MIN_NAME_LENGTH = 2;
    private final int MIN_PASSWORD_LENGTH = 2;
    private final String ALLOWED_PATTERN = "[a-zA-Z0-9_]*";

    public Register(String[] args) {
        super(args);
    }

    @Override
    public JsonObject execute() {
        String message = null;
        String name = null;
        String password = null;
        if (args.length >= 3) {
            // received both arguments arguments.
            if ((name = args[1]) != null && name.length() >= MIN_NAME_LENGTH) {
                // name is long enough
                Pattern alphaNumeric = Pattern.compile(ALLOWED_PATTERN);
                if (alphaNumeric.matcher(name).matches()) {
                    // name is sane
                    if ((password = args[2]) != null && password.length() >= MIN_PASSWORD_LENGTH) {
                        // password is long enough
                        LinkedHashMap<String, String> params = new LinkedHashMap<>();
                        params.put(DBStandard.NAME, name);
                        params.put(DBStandard.PASSWORD, password);
                        params.put(DBStandard.REQUEST, DBStandard.REQUEST_REGISTER);
                        JsonObject databaseResp = HttpMan.post(HttpHost.PI, HttpEndpoint.USER_DB, params);
                        if (databaseResp != null) {
                            // database request succeeded
                            int code = databaseResp.get(DBStandard.CODE.toString()).getAsInt();
                            switch (code) {
                                case DBStandard.CODE_WRITE_SUCCESS:
                                    message = "Registered user " + name + ".\n";
                                    break;
                                case DBStandard.CODE_REGISTERED_DUPLICATE:
                                    message = "User \"" + name + "\" is registered already.\n";
                                    break;
                                default:
                                    message = "Unknown error " + code + " occured.\n";
                                    break;
                            }
                        } else {
                            message = "Could not reach user database.\n";
                        }
                    } else {
                        message = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long, try again.\n";
                    }
                } else {
                    message = "\"" + name + "\" contains invalid special characters, try again.\n";
                }
            } else {
                message = "User name must be at least " + MIN_NAME_LENGTH + " characters long, try again.\n";
            }
        } else {
            message = Message.ARGS_NOT_RECEIVED.toLine();;
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message);
        return jsonResp;
    }

}