package session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import user.User;
import user.UserState;

public class Session {

    private StringBuilder code = new StringBuilder();
    private Set<User> users = new HashSet<>();
    private int totalChars = 0;
    private String language = null;
    private boolean live = false;

    public Session(String language) throws EmptyLanguageException, IOException {
        this.language = language;
        Stream<Path> paths = Files.walk(Paths.get("resource/language"));
        List<String> filePaths = paths.filter(Files::isRegularFile).map(e -> e.toString())
                .filter(e -> e.split("\\.")[1].equals(language)).collect(Collectors.toCollection(ArrayList::new));
        paths.close();
        if (filePaths.size() == 0) {
            throw new EmptyLanguageException(language);
        }
        int randomIndex = (int) (Math.random() * filePaths.size());
        String randomFilePath = filePaths.get(randomIndex);
        List<String> codeLines = Files.readAllLines(Paths.get(randomFilePath));
        for (String line : codeLines) {
            totalChars += line.length();
            code.append(line).append("\n");
        }
    }

    public boolean isLive() {
        return live;
    }

    public void start() {
        if (!live) {
            live = true;
            for (User user : users) {
                user.setState(UserState.LIVE_SESSION);
            }
        }
    }

    public void join(User user) {
        users.add(user);
    }

    public String getLanguage() {
        return language;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void leave(User user) {
        users.remove(user);
    }

    public String getCode() {
        return code.toString();
    }

}
