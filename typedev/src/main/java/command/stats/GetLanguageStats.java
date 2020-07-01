package command.stats;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import command.Command;
import extra.DBStandard;
import extra.Message;
import extra.Standard;
import user.User;

public class GetLanguageStats extends Command {

    private StringBuilder message;

    public GetLanguageStats(String key, String[] args, ConcurrentHashMap<String, User> users) {
        super(key, args, users);
        message = new StringBuilder();
    }

    @Override
    public JsonObject execute() {
        User requestingUser = users.get(key);
        if (requestingUser != null) {
            // user is logged in.
            if (args.length > 1) {
                // use requested somone else's stats.
                String requestedName = args[1];
                User requestedUser = null;
                for (User user : users.values()) {
                    if (user.getName().equals(requestedName)) {
                        requestedUser = user;
                        break;
                    }
                }
                if (requestedUser != null) {
                    // requested user exists.
                    appendTargetUserMessage(requestedUser);
                } else {
                    message.append(Message.USER_NOT_LOGGED_IN.toLine(requestedName));
                }
            } else {
                // user requested his own stats.
                appendTargetUserMessage(requestingUser);
            }
        } else {
            message.append(Message.LOG_IN_TO_VIEW_STATS.toLine());
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(Standard.MSG, message.toString());
        return jsonResp;
    }

    private void appendTargetUserMessage(User targetUser) {
        JsonObject jsonLanguageStats = null;
        jsonLanguageStats = targetUser.getManager().language();
        if (jsonLanguageStats != null) {
            // received response from user db.
            int code = jsonLanguageStats.get(DBStandard.CODE).getAsInt();
            switch (code) {
                case DBStandard.CODE_SUCCESS:
                    System.out.println(jsonLanguageStats);
                    message = buildLanguageBoard(jsonLanguageStats);
                    break;
                case DBStandard.CODE_NOTFOUND:
                    message.append(Message.USER_NOT_FOUND.toLine(targetUser.getName()));
                    break;
                case DBStandard.CODE_WRONGPASSWORD:
                    message.append(Message.WRONG_PASSWORD.toLine());
                default:
                    message.append(Message.UNKNOWN_ERROR.toLine(code));
                    break;
            }
        } else {
            message.append(Message.DB_UNREACHABLE.toLine());
        }
    }

    private StringBuilder buildLanguageBoard(JsonObject jsonLanguageStats) {
        JsonArray languageArray = jsonLanguageStats.get(DBStandard.BOARD_PROPERTY).getAsJsonArray();
        int place = 1;
        if (languageArray.size() == 0) {
            // user has no language stats yet.
            message.append(Message.NO_LANGUAGES_YET.toLine());
        }
        for (JsonElement entry : languageArray) {
            JsonObject entryObject = entry.getAsJsonObject();
            message.append(place++ + ". ");
            message.append(entryObject.get(DBStandard.LANGUAGE_NAME_PROPERTY).getAsString() + " | ");
            message.append(entryObject.get(DBStandard.LANGUAGE_PLAYED_PROPERTY).getAsString() + "\n");
        }
        return message;
    }

}