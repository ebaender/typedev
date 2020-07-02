package command.stats;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import command.Command;
import extra.DBStan;
import extra.JsonStan;
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
                    leaderbordType = DBStan.LEADERBORD_WINS;
                } else if (args[1].equals("topspeed")) {
                    leaderbordType = DBStan.LEADERBORD_SPEED;
                }
                if (leaderbordType != null) {
                    // recognized category
                    JsonObject jsonLeaderbord = user.getManager().leaderbord(leaderbordType);
                    if (jsonLeaderbord != null) {
                        // received response from database
                        int code = jsonLeaderbord.get(DBStan.CODE).getAsInt();
                        switch (code) {
                            case DBStan.CODE_SUCCESS:
                                message = buildLeaderbord(jsonLeaderbord, message, leaderbordType);
                                break;
                            case DBStan.CODE_WRONGPASSWORD:
                                message.append("Your password was changed, try logging in again.\n");
                                break;
                            case DBStan.CODE_USER_NOT_FOUND:
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
        jsonResp.addProperty(JsonStan.MSG, message.toString());
        return jsonResp;
    }

    private StringBuilder buildLeaderbord(JsonObject jsonLeaderbord, StringBuilder message, String leaderbordType) {
        JsonArray leaderbordArray = jsonLeaderbord.get(DBStan.BOARD_PROPERTY).getAsJsonArray();
        int place = 1;
        String category = null;
        String unit = "";
        switch (leaderbordType) {
            case DBStan.LEADERBORD_SPEED:
                category = DBStan.UPDATE_SPEED;
                unit = "cpm";
                break;
            case DBStan.LEADERBORD_WINS:
                category = DBStan.UPDATE_GAMES_WON;
                break;
            default:
                return null;
        }
        for (JsonElement entry : leaderbordArray) {
            JsonObject entryObject = entry.getAsJsonObject();
            message.append(place++ + ". ");
            message.append(entryObject.get(DBStan.NAME).getAsString() + " | ");
            message.append(entryObject.get(category).getAsString() + " " + unit + "\n");
        }
        return message;
    }
}