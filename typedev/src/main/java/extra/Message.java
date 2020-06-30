package extra;

import java.text.MessageFormat;

public enum Message {
    
    COMMAND_NOT_FOUND("Command not found."),
    ARGS_NOT_RECEIVED("Did not receive all arguments."),
    RESPONDED_WITH("{0} responded with {1}"),
    LOGGED_IN_ALREADY("{0} is logged in already."),
    KEYGEN_FAILED("Your key could not be generated."),
    UNLUCKY("You are one unlucky bastard, try again.");
    
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