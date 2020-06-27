package command.user;

import java.util.HashMap;

import com.google.gson.JsonObject;

import command.Command;
import extra.DatabaseStandard;
import extra.HttpEndpoint;
import extra.HttpHost;
import extra.HttpMan;
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
            // received name and password
            String name = args[1];
            String password = args[2];
            HashMap<String, String> params = new HashMap<>();
            params.put(DatabaseStandard.NAME, name);
            params.put(DatabaseStandard.PASSWORD, password);
            params.put(DatabaseStandard.REQUEST, DatabaseStandard.REQUEST_AUTHENTICATE);
            JsonObject databaseResp = HttpMan.post(HttpHost.PI, HttpEndpoint.USER_DB, params);
            if (databaseResp != null) {
                // received response from database
                int code = databaseResp.get(DatabaseStandard.CODE).getAsInt();
                switch (code) {
                    case DatabaseStandard.CODE_AUTHENTICATE_SUCCESS:
                        jsonResp = databaseResp;
                        break;
                    case DatabaseStandard.CODE_WRONGPASSWORD:
                        message = "Wrong password, try again.\n";
                        break;
                    case DatabaseStandard.CODE_NOTFOUND:
                        message = "User \"" + name + "\" does not exist.\n";
                        break;
                    default:
                        message = "Unknown error " + code + " occured.\n";
                        break;
                }
            } else {
                message = "Could not reach user database.\n";
            }
        }
        if (jsonResp == null) {
            jsonResp = new JsonObject();
            jsonResp.addProperty(Standard.MSG, message);
        }
        return jsonResp;
    }

}