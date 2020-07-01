package command.user;

import java.util.HashMap;

import com.google.gson.JsonObject;

import command.Command;
import extra.DBStandard;
import extra.HttpEndpoint;
import extra.HttpHost;
import extra.HttpMan;
import extra.Message;
import extra.Standard;

public class Authenticate extends Command {

    public Authenticate(String[] args) {
        super(args);
    }

    @Override
    public JsonObject execute() {
        JsonObject jsonResp = null;
        String message = null;
        if (args.length == 3) {
            // received name and password.
            String name = args[1];
            String password = args[2];
            HashMap<String, String> params = new HashMap<>();
            params.put(DBStandard.NAME, name);
            params.put(DBStandard.PASSWORD, password);
            params.put(DBStandard.REQUEST, DBStandard.REQUEST_AUTHENTICATE);
            JsonObject databaseResp = HttpMan.post(HttpHost.PI, HttpEndpoint.USER_DB, params);
            if (databaseResp != null) {
                // received response from database.
                int code = databaseResp.get(DBStandard.CODE).getAsInt();
                switch (code) {
                    case DBStandard.CODE_AUTHENTICATE_SUCCESS:
                        jsonResp = databaseResp;
                        break;
                    case DBStandard.CODE_WRONGPASSWORD:
                        message = Message.WRONG_PASSWORD.toLine();
                        break;
                    case DBStandard.CODE_USER_NOT_FOUND:
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
            jsonResp.addProperty(Standard.MSG, message);
        }
        return jsonResp;
    }

}