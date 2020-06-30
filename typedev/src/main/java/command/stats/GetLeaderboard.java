package command.stats;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import command.Command;
import extra.DBStandard;
import extra.Standard;
import user.User;

public class GetLeaderboard extends Command {

    public GetLeaderboard(String key, String[] args, ConcurrentHashMap<String, User> users) {
        super(key, args, users);
    }

    @Override
    public JsonObject execute() {
        StringBuilder message = new StringBuilder();
        User user = users.get(key);
        if (user != null) {
            // user is logged in
            if (args.length > 1) {
                // received category
                String leaderbordType = null;
                if (args[1].equals("victories")) {
                    leaderbordType = DBStandard.LEADERBORD_WINS;
                } else if (args[1].equals("topspeed")) {
                    leaderbordType = DBStandard.LEADERBORD_SPEED;
                }
                if (leaderbordType != null) {
                    // recognized category
                    JsonObject jsonLeaderbord = user.getManager().leaderbord(leaderbordType);
                    if (jsonLeaderbord != null) {
                        // received response from database
                        int code = jsonLeaderbord.get(DBStandard.CODE).getAsInt();
                        switch (code) {
                            case DBStandard.CODE_SUCCESS:
                                message = buildLeaderbord(jsonLeaderbord, message, leaderbordType);
                                break;
                            case DBStandard.CODE_WRONGPASSWORD:
                                message.append("Your password was changed, try logging in again.\n");
                                break;
                            case DBStandard.CODE_NOTFOUND:
                                message.append("Your user was removed from the database.\n");
                                break;
                            default:
                                message.append("Unknown error" + code + " occured.\n");
                                break;
                        }
                    } else {
                        message.append("Could not reach user database.\n");
                    }
                } else {
                    message.append("\"" + args[1] + "\" is not a valid category.\n");
                }
            } else {
                message.append("Usage: " + args[0] + " ['victories' | 'topspeed']\n");
            }
        } else {
            message.append("You need to be logged in to view leaderbords.\n");
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message.toString());
        return jsonResp;
    }

    private StringBuilder buildLeaderbord(JsonObject jsonLeaderbord, StringBuilder message, String leaderbordType) {
        JsonArray leaderbordArray = jsonLeaderbord.get(DBStandard.LEADERBORD_PROPERTY).getAsJsonArray();
        int place = 1;
        String category = null;
        String unit = "";
        switch (leaderbordType) {
            case DBStandard.LEADERBORD_SPEED:
                category = DBStandard.UPDATE_SPEED;
                unit = "cpm";
                break;
            case DBStandard.LEADERBORD_WINS:
                category = DBStandard.UPDATE_GAMES_WON;
                break;
            default:
                return null;
        }
        for (JsonElement entry : leaderbordArray) {
            JsonObject entryObject = entry.getAsJsonObject();
            message.append(place++ + ". ");
            message.append(entryObject.get(DBStandard.NAME).getAsString() + " | ");
            message.append(entryObject.get(category).getAsString() + " " + unit + "\n");
        }
        return message;
    }
}