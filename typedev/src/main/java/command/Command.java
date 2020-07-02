package command;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

import user.User;

public abstract class Command {

    protected String[] args;
    protected String key;
    protected ConcurrentHashMap<String, User> users;

    public Command(String key, String[] args, ConcurrentHashMap<String, User> users) {
        this.args = args;
        this.key = key;
        this.users = users;
    };

    // public abstract List<String> getHandles();

    public Command(String key, String[] args) {
        this(key, args, null);
    }

    public Command(String key, ConcurrentHashMap<String, User> users) {
        this(key, null, users);
    }

    public Command(String key) {
        this(key, null, null);
    }

    public Command(String[] args) {
        this(null, args, null);
    }

    public Command(ConcurrentHashMap<String, User> users) {
        this(null, null, users);
    }

    public Command() {
        this(null, null, null);
    }

    public JsonObject execute() {
        return null;
    };

    public String[] getArgs() {
        return args;
    }

    public String geyKey() {
        return key;
    }

    public ConcurrentHashMap<String, User> getUsers() {
        return users;
    }

}