package command.stats;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import command.Command;
import standard.DBStd;
import translator.Message;
import standard.JsonStd;
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
                // user requested somone else's stats.
                String requestedName = args[1];
                appendRequestedUserMessage(requestingUser, requestedName);
            } else {
                // user requested his own stats.
                appendRequestedUserMessage(requestingUser, requestingUser.getName());
            }
        } else {
            message.append(Message.NEED_LOGIN.toLine());
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStd.MSG, message.toString());
        return jsonResp;
    }

    private void appendRequestedUserMessage(User requestingUser, String requestedUserName) {
        JsonObject jsonLanguageStats = requestingUser.getManager().language(requestedUserName);
        if (jsonLanguageStats != null) {
            // received response from user db.
            int code = jsonLanguageStats.get(DBStd.CODE).getAsInt();
            switch (code) {
                case DBStd.CODE_SUCCESS:
                    System.out.println(jsonLanguageStats);
                    message = buildLanguageBoard(jsonLanguageStats);
                    break;
                case DBStd.CODE_USER_NOT_FOUND:
                    message.append(Message.USER_NOT_FOUND.toLine(requestingUser.getName()));
                    break;
                case DBStd.CODE_REQUESTED_USER_NOT_FOUND:
                    message.append(Message.USER_NOT_FOUND.toLine(requestedUserName));
                    break;
                case DBStd.CODE_WRONGPASSWORD:
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
        JsonArray languageArray = jsonLanguageStats.get(DBStd.BOARD_PROPERTY).getAsJsonArray();
        int place = 1;
        if (languageArray.size() == 0) {
            // user has no language stats yet.
            message.append(Message.NO_LANGUAGES_YET.toLine());
        }
        for (JsonElement entry : languageArray) {
            JsonObject entryObject = entry.getAsJsonObject();
            message.append(place++ + ". ");
            message.append(entryObject.get(DBStd.LANGUAGE_NAME_PROPERTY).getAsString() + " | ");
            message.append(entryObject.get(DBStd.LANGUAGE_PLAYED_PROPERTY).getAsString() + "\n");
        }
        return message;
    }

}