package manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    
}