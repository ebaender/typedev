package command;

import command.session.*;
import command.user.*;
import user.User;

import java.util.concurrent.ConcurrentHashMap;

import command.help.*;

public class CommandFactory {

    private ConcurrentHashMap<String, User> users;

    public CommandFactory(ConcurrentHashMap<String, User> users) {
        this.users = users;
    }

    public Command create(String commandString, String key) {
        Command command = null;
        String[] args = commandString.replaceAll("\\s+", " ").split(" ");
        switch (args[0]) {
            case "li":
            case "login":
                command = new LogIn(key, args, users);
                break;
            case "lo":
            case "logout":
                command = new LogOut(key, users);
                break;
            case "ls":
            case "sessions":
                command = new GetSessions(users);
                break;
            case "lu":
            case "users":
                command = new GetUsers(users);
                break;
            case "wh":
            case "whoami":
                command = new GetName(key, users);
                break;
            case "cr":
            case "create":
                command = new CreateSession(key, args, users);
                break;
            case "jn":
            case "join":
                command = new JoinSession(key, args, users);
                break;
            case "lv":
            case "leave":
                command = new LeaveSession(key, users);
                break;
            case "st":
            case "start":
                command = new StartSession(key, users);
                break;
            case "?":
            case "help":
                command = new GetHelp();
                break;
            default:
                break;
        }
        return command;
    }

    public Command create(Class<? extends Command> commandType) {
        Command command = null;
        try {
            command = commandType.newInstance();
        } catch (InstantiationException e) {
            System.err.println(getClass() + "Could not instantiate command of type " + commandType + ".");
        } catch (IllegalAccessException e) {
            System.err.println(getClass() + "IllegalAccessException occured during instantiation of command of type "
                    + commandType + ".");
        }
        return command;
    }

}