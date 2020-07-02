package manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import translator.Message;

public class ResourceMan {

    public static Set<String> getLanguages() {
        File dirPath = new File("resource/language");
        File[] files = dirPath.listFiles();
        HashSet<String> languages = new HashSet<>();
        for (File file : files) {
            languages.add(file.getName().split("\\.")[1]);
        }
        return languages;
    }

    public static String getRandomLanguage() {
        ArrayList<String> languages = new ArrayList<>(getLanguages());
        String randomLanguage = languages.get((int) (Math.random() * languages .size()));
        System.out.println(Message.STATEMENT.toString(ResourceMan.class, "generated", randomLanguage));
        return randomLanguage;
    }

    public static List<String> getHelp() {
        List<String> helpLines = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("resource/help/help.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                helpLines.add(line);
            }
        } catch (IOException e) {
            helpLines.add(Message.NO_HELP.toString());
        }
        return helpLines;
    }

    public static List<String> getSpecificHelp(String query) {
        List<String> helpLines = getHelp();
        List<String>  specificHelpLines = new LinkedList<>();
        Pattern queryPattern = Pattern.compile(".*\\b" + query + "\\b.*");
        boolean include = false;
        for (String line : helpLines) {
            if (line.charAt(0) != ' ' && queryPattern.matcher(line).matches()) {
                // line is not indented and contains search query.
                include = true;
            } else if (line.charAt(0) != ' ') {
                /// is not indented and does not contain search query.
                include = false;
            }
            if (include) {
                specificHelpLines.add(line);
            }
        }
        return specificHelpLines;
    }
    
}