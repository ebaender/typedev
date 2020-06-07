package session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import user.User;

public class Session {

    private List<String> file;
    private HashMap<User, Integer> progressByUser;
    private int total = 0;
    private String language;

    public Session(String language) throws EmptyLanguageException, IOException {
        this.language = language;
        progressByUser = new HashMap<>();
        Stream<Path> paths = Files.walk(Paths.get("resource/language"));
        List<String> filePaths = paths.filter(Files::isRegularFile).map(e -> e.toString())
        .filter(e -> e.split("\\.")[1].equals(language)).collect(Collectors.toCollection(ArrayList::new));
        paths.close();
        if (filePaths.size() == 0) {
            throw new EmptyLanguageException(language);
        }
        int randomIndex = (int) (Math.random() * filePaths.size());
        String randomFilePath = filePaths.get(randomIndex);
        file = Files.readAllLines(Paths.get(randomFilePath));
        for (String string : file) {
            total += string.length();
        }
    }

    public void join(User user) {
        progressByUser.put(user, 0);
    }

    public String getLanguage() {
        return language;
    }

    public Set<User> getUsers() {
        return progressByUser.keySet();
    }

    public void removeUser(User user) {
        progressByUser.remove(user);
    }

}
