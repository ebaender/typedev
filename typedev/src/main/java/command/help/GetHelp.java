package command.help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;

import command.Command;
import extra.Standard;

public class GetHelp extends Command {

    public GetHelp(String[] args) {
        super(args);
    }

    public JsonObject execute() {
        // Pattern grep = args.length > 1 ? Pattern.compile(args[1]) : null;
        String grep = args.length > 1 ? args[1] : null;
        StringBuilder message = new StringBuilder();
        JsonObject jsonResp = new JsonObject();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("resource/help/help.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (grep == null || line.toLowerCase().contains(grep.toLowerCase())) {
                    message.append(line + '\n');
                }
            }
        } catch (IOException e) {
            message.append("There is no help. May god have mercy on your soul.\n");
        }
        jsonResp.addProperty(Standard.MSG, message.toString());
        return jsonResp;
    }

}