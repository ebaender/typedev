package command.stats;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import command.Command;
import standard.DBStd;
import standard.JsonStd;
import translator.Message;
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
            // user is logged in.
            if (args.length > 1) {
                // received category argument.
                String leaderbordType = null;
                String requestedCategory = args[1];
                if (requestedCategory.equals(DBStd.CATEGORY_VICTORIES)) {
                    leaderbordType = DBStd.LEADERBORD_WINS;
                } else if (requestedCategory.equals(DBStd.CATEGORY_TOPSPEED)) {
                    leaderbordType = DBStd.LEADERBORD_SPEED;
                }
                if (leaderbordType != null) {
                    // recognized category.
                    JsonObject jsonLeaderbord = user.getDB().leaderbord(leaderbordType);
                    if (jsonLeaderbord != null) {
                        // received response from database.
                        int code = jsonLeaderbord.get(DBStd.CODE).getAsInt();
                        switch (code) {
                            case DBStd.CODE_SUCCESS:
                                message = buildLeaderbord(jsonLeaderbord, message, leaderbordType);
                                break;
                            case DBStd.CODE_WRONGPASSWORD:
                                message.append(Message.WRONG_PASSWORD.toLine());
                                break;
                            case DBStd.CODE_USER_NOT_FOUND:
                                message.append(Message.USER_NOT_FOUND.toLine(user.getName()));
                                break;
                            default:
                                message.append(Message.UNKNOWN_ERROR.toLine(code));
                                break;
                        }
                    } else {
                        message.append(Message.DB_UNREACHABLE);
                    }
                } else {
                    message.append(Message.LEADERBOARD_INVALID_CATEGORY.toLine(requestedCategory));
                }
            } else {
                message.append(Message.ARGS_NOT_RECEIVED.toLine());
            }
        } else {
            message.append(Message.NEED_LOGIN.toLine());
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStd.MSG, message.toString());
        return jsonResp;
    }

    private StringBuilder buildLeaderbord(JsonObject jsonLeaderbord, StringBuilder message, String leaderbordType) {
        JsonArray leaderbordArray = jsonLeaderbord.get(DBStd.BOARD_PROPERTY).getAsJsonArray();
        int place = 1;
        String category = null;
        String unit = "";
        switch (leaderbordType) {
            case DBStd.LEADERBORD_SPEED:
                category = DBStd.UPDATE_SPEED;
                unit = "cpm";
                break;
            case DBStd.LEADERBORD_WINS:
                category = DBStd.UPDATE_GAMES_WON;
                break;
            default:
                return null;
        }
        for (JsonElement entry : leaderbordArray) {
            JsonObject entryObject = entry.getAsJsonObject();
            message.append(place++ + ". ");
            message.append(entryObject.get(DBStd.NAME).getAsString() + " | ");
            message.append(entryObject.get(category).getAsString() + " " + unit + "\n");
        }
        return message;
    }

}