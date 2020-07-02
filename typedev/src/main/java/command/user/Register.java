package command.user;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;

import command.Command;
import extra.DBStan;
import extra.HttpEndpoint;
import extra.HttpHost;
import extra.HttpMan;
import extra.Message;
import extra.JsonStan;
import extra.UserStan;

public class Register extends Command {

    public Register(String[] args) {
        super(args);
    }

    @Override
    public JsonObject execute() {
        String message = null;
        String name = null;
        String password = null;
        if (args.length >= 3) {
            // received name and password.
            if ((name = args[1]) != null && name.length() >= UserStan.MIN_NAME_LENGTH) {
                // name is long enough.
                Pattern alphaNumeric = Pattern.compile(UserStan.ALLOWED_NAME_PATTERN);
                if (alphaNumeric.matcher(name).matches()) {
                    // name is sane.
                    if ((password = args[2]) != null && password.length() >= UserStan.MIN_PASSWORD_LENGTH) {
                        // password is long enough.
                        LinkedHashMap<String, String> params = new LinkedHashMap<>();
                        params.put(DBStan.NAME, name);
                        params.put(DBStan.PASSWORD, password);
                        params.put(DBStan.REQUEST, DBStan.REQUEST_REGISTER);
                        JsonObject databaseResp = HttpMan.post(HttpHost.PI, HttpEndpoint.USER_DB, params);
                        if (databaseResp != null) {
                            // database request succeeded.
                            int code = databaseResp.get(DBStan.CODE.toString()).getAsInt();
                            switch (code) {
                                case DBStan.CODE_WRITE_SUCCESS:
                                    message = Message.REGISTERED_SUCCESS.toLine(name);
                                    break;
                                case DBStan.CODE_REGISTERED_DUPLICATE:
                                    message = Message.REGISTERED_DUPLICATE.toLine(name);
                                    break;
                                default:
                                    message = Message.UNKNOWN_ERROR.toLine(code);
                                    break;
                            }
                        } else {
                            message = Message.DB_UNREACHABLE.toLine();
                        }
                    } else {
                        message = Message.PASSWORD_TOO_SHORT.toLine(UserStan.MIN_PASSWORD_LENGTH);
                    }
                } else {
                    message = Message.NAME_ILLEGAL_CHARS.toLine(name);
                }
            } else {
                message = Message.NAME_TOO_SHORT.toLine(UserStan.MIN_NAME_LENGTH);
            }
        } else {
            message = Message.ARGS_NOT_RECEIVED.toLine();;
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStan.MSG, message);
        return jsonResp;
    }

}