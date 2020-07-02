package manager;

import java.io.File;
import java.util.ArrayList;

import translator.Message;

public class ResourceMan {

    public static ArrayList<String> getLanguages() {
        File dirPath = new File("resource/language");
        File[] files = dirPath.listFiles();
        ArrayList<String> languages = new ArrayList<>();
        for (File file : files) {
            languages.add(file.getName().split("\\.")[1]);
        }
        return languages;
    }

    public static String getRandomLanguage() {
        ArrayList<String> languages = getLanguages();
        String randomLanguage = languages.get((int) (Math.random() * languages .size()));
        System.out.println(Message.STATEMENT.toString(ResourceMan.class, "generated", randomLanguage));
        return randomLanguage;
    }
    
}