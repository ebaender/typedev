package session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import user.User;
import user.UserState;

public class Session {

    private StringBuilder code = new StringBuilder();
    private Set<User> users = new HashSet<>();
    private int totalChars = 0;
    private String language = null;
    private boolean live = false;
    private boolean stopped = false;
    private JsonObject result = null;

    public Session(String language) throws EmptyLanguageException, IOException {
        this.language = language;
        File dirPath = new File("resource/language");
        File[] contents = dirPath.listFiles();
        List<File> filePaths = Arrays.asList(contents);
        // for (int i = 0; i < contents.length; i++) {
        //     System.out.println(contents[i]);
        // }
        filePaths = filePaths.stream().filter(e -> e.getName().split("\\.")[1].equals(language))
                .collect(Collectors.toCollection(ArrayList::new));
        if (filePaths.size() == 0) {
            throw new EmptyLanguageException(language);
        }
        int randomIndex = (int) (Math.random() * filePaths.size());
        File randomFilePath = filePaths.get(randomIndex);
        List<String> codeLines = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(randomFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                codeLines.add(line);
            }
        }
        for (String line : codeLines) {
            totalChars += line.replaceAll("\\s+", "").length();
            code.append(line).append("\n");
        }
        // System.out.println(code.toString());
    }

    public JsonObject getResult() {
        return result;
    }

    private void buildResult() {
        AtomicInteger place = new AtomicInteger(1);
        result = new JsonObject();
        users.stream().sorted((a, b) -> Integer.compare(b.getProgress(), a.getProgress())).forEach(user -> {
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
        if (!stopped) {
            stopped = true;
            for (User user : users) {
                user.setState(UserState.FINISHED);
            }
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
        user.setProgress(0);
        user.setMistakes(0);
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
