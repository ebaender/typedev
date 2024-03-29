package command.stats;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import command.Command;
import standard.DBStd;
import standard.JsonStd;
import user.User;

public class GetStats extends Command {

    public GetStats(String key, String[] args, ConcurrentHashMap<String, User> users) {
        super(key, args, users);
    }

    @Override
    public JsonObject execute() {
        StringBuilder message = new StringBuilder();
        User user = users.get(key);
        if (user != null) {
            // user is logged in
            JsonObject jsonStats = null;
            if (args.length > 1) {
                // received target argument
                String targetUser = args[1];
                jsonStats = user.getDB().spy(targetUser);
                if (jsonStats != null) {
                    int code = jsonStats.get(DBStd.CODE).getAsInt();
                    switch (code) {
                        case DBStd.CODE_SUCCESS:
                            message = buildStats(jsonStats, message);
                            break;
                        case DBStd.CODE_WRONGPASSWORD:
                            message.append("Your password was changed, try logging in again.\n");
                            break;
                        case DBStd.CODE_REQUESTED_USER_NOT_FOUND:
                            message.append("User \"" + targetUser + "\" does not exist.\n");
                            break;
                        default:
                            message.append("Unknown error " + code + " occured.\n");
                            break;
                    }
                } else {
                    message.append("Could not reach user database.\n");
                }
            } else {
                // no argument
                jsonStats = user.getDB().authenticate();
                if (jsonStats != null) {
                    int code = jsonStats.get(DBStd.CODE).getAsInt();
                    switch (code) {
                        case DBStd.CODE_AUTHENTICATE_SUCCESS:
                            message = buildStats(jsonStats, message);
                            break;
                        case DBStd.CODE_WRONGPASSWORD:
                            message.append("Your password was changed, try logging in again.\n");
                            break;
                        case DBStd.CODE_USER_NOT_FOUND:
                            message.append("Your user was removed from the database.\n");
                            break;
                        default:
                            message.append("Unknown error " + code + " occured.\n");
                            break;
                    }
                } else {
                    message.append("Could not reach user database.\n");
                }
            }
        } else {
            message.append("You need to be logged in to view stats.\n");
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStd.MSG, message.toString());
        return jsonResp;
    }

    private StringBuilder buildStats(JsonObject jsonStats, StringBuilder message) {
        int gamesPlayed = jsonStats.get(DBStd.UPDATE_GAMES_PLAYED).getAsInt();
        int gamesWon = jsonStats.get(DBStd.UPDATE_GAMES_WON).getAsInt();
        int speed = jsonStats.get(DBStd.UPDATE_SPEED).getAsInt();
        message.append("Games played: " + gamesPlayed);
        message.append("\nGames won: " + gamesWon);
        message.append("\nWin ratio: " + (int) ((gamesWon / (double) gamesPlayed) * 100) + " %");
        message.append("\nTop speed: " + speed + " cpm\n");
        return message;
    }

}