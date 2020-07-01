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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import extra.DBStandard;
import extra.Message;
import user.User;
import user.UserState;

public class Session {

    private StringBuilder code = new StringBuilder();
    private Set<User> users = new HashSet<>();
    private int totalChars = 0;
    private String language = null;
    private AtomicBoolean live = null;
    private AtomicBoolean stopped = null;
    private JsonObject result = null;
    private Long startTime = null;
    private Long duration = null;

    public Session(String language) throws EmptyLanguageException, IOException {
        stopped = new AtomicBoolean(false);
        live = new AtomicBoolean(false);
        this.language = language;
        File dirPath = new File("resource/language");
        File[] contents = dirPath.listFiles();
        List<File> filePaths = Arrays.asList(contents);
        filePaths = filePaths.stream().filter(e -> {
            String[] fileAndExtension = e.getName().split("\\.");
            if (fileAndExtension.length > 1 && fileAndExtension[1].equals(language)) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
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
    }

    public JsonObject getResult() {
        return result;
    }

    private void updateDB(User user, int place, int cpm) {
        JsonObject updateResp = user.getManager().update(hasMultipleUsers(), hasMultipleUsers() && place == 1, cpm,
                language);
        String message = null;
        if (updateResp != null) {
            int code = updateResp.get(DBStandard.CODE).getAsInt();
            switch (code) {
                case DBStandard.CODE_WRITE_SUCCESS:
                    message = Message.UPDATE_SUCESS.toString(user.getName());
                    break;
                case DBStandard.CODE_WRONGPASSWORD:
                    message = Message.WRONG_PASSWORD.toString();
                    break;
                case DBStandard.CODE_NOTFOUND:
                    message = Message.USER_NOT_FOUND.toString(user.getName());
                    break;
                default:
                    message = Message.UNKNOWN_ERROR.toString(code);
                    break;
            }
        } else {
            message = Message.DB_UNREACHABLE.toString();
        }
        System.out.println(Message.CONCAT.toString(getClass(), message));
    }

    private void finish() {
        AtomicInteger place = new AtomicInteger(1);
        result = new JsonObject();
        users.stream().sorted((a, b) -> Integer.compare(b.getProgress(), a.getProgress())).forEach(user -> {
            int cpm = (int) (user.getProgress() * 60D / duration);
            JsonObject userResult = new JsonObject();
            userResult.addProperty("name", user.getName());
            userResult.addProperty("progress", user.getProgress());
            userResult.addProperty("mistakes", user.getMistakes());
            userResult.addProperty("cpm", cpm);
            updateDB(user, place.intValue(), cpm);
            result.add(String.valueOf(place.getAndIncrement()), userResult);
        });
    }

    private boolean hasMultipleUsers() {
        return users.size() > 1 ? true : false;
    }

    public int getTotalChars() {
        return totalChars;
    }

    public boolean isLive() {
        return live.get();
    }

    public synchronized void start() {
        if (!live.get()) {
            startTime = System.nanoTime();
            live.set(true);
            ;
            for (User user : users) {
                user.setState(UserState.LIVE_SESSION);
            }
        }
        // System.out.println(getClass() + " started");
    }

    public synchronized void stop() {
        if (!stopped.get()) {
            System.out.println(getClass() + " took " + duration + " seconds.");
            stopped.set(true);
            for (User user : users) {
                user.setState(UserState.FINISHED);
            }
        }
        // System.out.println(getClass() + " stopped");
    }

    public synchronized void think() {
        for (User user : users) {
            if (user.getProgress() == totalChars && !stopped.get()) {
                duration = (System.nanoTime() - startTime) / 1000000000L;
                finish();
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
