package command.language;

import java.io.File;
import java.util.HashSet;

import com.google.gson.JsonObject;

import command.Command;
import extra.JsonStan;

public class GetLanguages extends Command {

    public GetLanguages() {
        super();
    }

    @Override
    public JsonObject execute() {
        StringBuilder message = new StringBuilder();
        File dirPath = new File("resource/language");
        File[] files = dirPath.listFiles();
        HashSet<String> languages = new HashSet<>();
        for (File file : files) {
            languages.add(file.getName().split("\\.")[1]);
        }
        for (String language : languages) {
            message.append(language.toUpperCase() + ' ');
        }
        message.append('\n');
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty(JsonStan.MSG, message.toString());
        return jsonResp;
    }
    
}