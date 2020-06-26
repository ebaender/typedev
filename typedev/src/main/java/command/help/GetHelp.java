package command.help;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.JsonObject;

import command.Command;
import extra.Standard;

public class GetHelp extends Command {

    public GetHelp() {
        super();
    }

    public JsonObject execute() {
        StringBuilder message = new StringBuilder();
        JsonObject jsonResp = new JsonObject();
        List<String> helpLines;
        try {
            helpLines = Files.readAllLines(Paths.get("resource/help/help.txt"));
            for (String line : helpLines) {
                message.append(line).append("\n");
            }
        } catch (IOException e) {
            message.append("There is no help. May god have mercy on your soul.\n");
        }
        jsonResp.addProperty(Standard.MSG, message.toString());
        return jsonResp;
    }

}