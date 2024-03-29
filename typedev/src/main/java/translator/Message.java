package translator;

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
    YOU_ARE_NOBODY("You are nobody."), YOU_ARE("You are {0}."), NOBODY_HERE("Nobody is logged in."),
    NO_SESSIONS("There are no sessions."), INDEX_OUT_OF_BOUNDS("Index {0} is out of bounds."),
    NEED_LOGIN("You need to be logged in to do that."), IN_SESSION_ALREADY("You are in a session already."),
    LANGUAGE_DIR_MISSING("Could not find the language directory."),
    LANGUAGE_UNSUPPORTED("Language \"{0}\" is not supported."), CREATED_SESSION("Created {0} session."),
    STARTING_SESSION("Starting Session..."), YOU_HAVE_NO_SESSION("You don't have a session."),
    JOINED_SESSION("Joined {0}''s {1} session."), SESSION_ALREADY_LIVE("{0}''s session is already live."),
    USER_HAS_NO_SESSION("{0} is not a member of any session."), LEFT_SESSION("Left {0} session."),
    LEADERBOARD_INVALID_CATEGORY("There are no leadearboards for \"{0}\""),
    UNLUCKY("You are one unlucky bastard, try again."), NO_HELP("There is no help. May god have mercy on your soul.");

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