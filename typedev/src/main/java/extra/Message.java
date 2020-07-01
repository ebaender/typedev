package extra;

import java.text.MessageFormat;

public enum Message {

    COMMAND_NOT_FOUND("Command not found."), ARGS_NOT_RECEIVED("Did not receive all arguments."),
    RESPONDED_WITH("{0} responded with {1}"), LOGGED_IN_ALREADY("You are logged in as {0} already."),
    OTHER_USER_LOGGED_IN_ALREADY("{0} is logged in already."), LOGIN_SUCCESS("Logged in as {0}."),
    REGISTERED_SUCCESS("Registered user {0}."), REGISTERED_DUPLICATE("User {0} is registered already."),
    USER_NOT_FOUND("User \"{0}\" does not exist."), USER_NOT_LOGGED_IN("\"{0}\" is not logged in."),
    UNKNOWN_ERROR("Unknown error {0} occured."), DB_UNREACHABLE("User database could not be reached."),
    UPDATE_SUCESS("Updated {0}."), KEYGEN_FAILED("Your key could not be generated."),
    WRONG_PASSWORD("Wrong password, try again."), CONCAT("{0} {1}"), STATEMENT("{0}: {1} {2}"),
    NO_LANGUAGES_YET("No languages have been used yet."),
    PASSWORD_TOO_SHORT("Password must be at least {0} characters long, try again."),
    NAME_TOO_SHORT("Name must be at least {0} characters long, try again."),
    NAME_ILLEGAL_CHARS("\"{0}\" contains invalid special characters, try again."),
    LOGGED_OUT_ALREADY("You are logged out already."), LOGGED_OUT("Logged out as {0}."),
    YOU_ARE_NOBODY("You are nobody."), YOU_ARE("You are {0}."),
    LOG_IN_TO_VIEW_STATS("You need to be logged in to view stats."), UNLUCKY("You are one unlucky bastard, try again.");

    private final String MESSAGE;

    private Message(final String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    @Override
    public String toString() {
        return MESSAGE;
    }

    public String toLine() {
        return toString() + System.lineSeparator();
    }

    public String toString(Object... arguments) {
        return MessageFormat.format(MESSAGE, arguments);
    }

    public String toLine(Object... arguments) {
        return toString(arguments) + System.lineSeparator();
    }

}