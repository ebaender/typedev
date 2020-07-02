package command.help;

import java.util.List;

import com.google.gson.JsonObject;

import command.Command;
import manager.ResourceMan;
import standard.JsonStd;

public class GetHelp extends Command {

    public GetHelp(String[] args) {
        super(args);
    }

    public JsonObject execute() {
        StringBuilder message = new StringBuilder();
        List<String> helpLines = null;
        if (args.length > 1) {
            // user requested specific help query.
            String query = args[1];
            helpLines = ResourceMan.getSpecificHelp(query);
        } else {
            helpLines = ResourceMan.getHelp();
        }
        for (String line : helpLines) {
            message.append(line).append(System.lineSeparator());
        }
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStd.MSG, message.toString());
        return jsonResp;
    }

}