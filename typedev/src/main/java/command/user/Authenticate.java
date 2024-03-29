package command.user;

import java.util.HashMap;

import com.google.gson.JsonObject;

import command.Command;
import standard.DBStd;
import translator.HttpEndpoint;
import translator.HttpHost;
import manager.HttpMan;
import translator.Message;
import standard.JsonStd;

public class Authenticate extends Command {

    public Authenticate(String[] args) {
        super(args);
    }

    @Override
    public JsonObject execute() {
        JsonObject jsonResp = null;
        String message = null;
        if (args.length > 2) {
            // received name and password.
            String name = args[1];
            String password = args[2];
            HashMap<String, String> params = new HashMap<>();
            params.put(DBStd.NAME, name);
            params.put(DBStd.PASSWORD, password);
            params.put(DBStd.REQUEST, DBStd.REQUEST_AUTHENTICATE);
            JsonObject authResp = HttpMan.post(HttpHost.PI, HttpEndpoint.USER_DB, params);
            if (authResp != null) {
                // received response from database.
                int code = authResp.get(DBStd.CODE).getAsInt();
                switch (code) {
                    case DBStd.CODE_AUTHENTICATE_SUCCESS:
                        jsonResp = authResp;
                        break;
                    case DBStd.CODE_WRONGPASSWORD:
                        message = Message.WRONG_PASSWORD.toLine();
                        break;
                    case DBStd.CODE_USER_NOT_FOUND:
                        message = Message.USER_NOT_FOUND.toLine(name);
                        break;
                    default:
                        message = Message.UNKNOWN_ERROR.toLine(code);
                        break;
                }
            } else {
                message = Message.DB_UNREACHABLE.toLine();
            }
        }
        if (jsonResp == null) {
            jsonResp = new JsonObject();
            jsonResp.addProperty(JsonStd.MSG, message);
        }
        return jsonResp;
    }

}