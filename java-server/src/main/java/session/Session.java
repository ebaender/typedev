package session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

import user.User;
import user.UserState;

public class Session {

    private StringBuilder code = new StringBuilder();
    private Set<User> users = new HashSet<>();
    private int totalChars = 0;
    private String language = null;
    private boolean live = false;
    private JsonObject result = null;

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
            totalChars += line.replaceAll("\\s+","").length();
            code.append(line).append("\n");
        }
    }

    public JsonObject getResult() {
        return result;
    }

    private void buildResult() {
        AtomicInteger place = new AtomicInteger(1);
        result = new JsonObject();
        users.stream().sorted((a,b) -> Integer.compare(b.getProgress(), a.getProgress())).forEach(user -> {
            JsonObject userResult = new JsonObject();
            userResult.addProperty("name", user.getName());
            userResult.addProperty("progress", user.getProgress());
            userResult.addProperty("mistakes", user.getMistakes());
            result.add(String.valueOf(place.getAndIncrement()), userResult);
        });
    }

    public int getTotalChars() {
        return totalChars;
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
        // System.out.println(getClass() + " started");
    }

    public void stop() {
        for (User user : users) {
            user.setState(UserState.FINISHED);
        }
        // System.out.println(getClass() + " stopped");
    }

    public void think() {
        for (User user : users) {
            if (user.getProgress() == totalChars) {
                buildResult();
                stop();
            }
        }
        // System.out.println(getClass() + " thinking about the number " + totalChars);
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
