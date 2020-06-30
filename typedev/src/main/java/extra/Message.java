package extra;

public enum Message {

    COMMAND_NOT_FOUND("Command not found."),
    ARGS_NOT_RECEIVED("Did not receive all arguments."),
    RESPONDED_WITH(" responded with ");

    private final String MESSAGE;

    private Message(final String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    @Override
    public String toString() {
        return MESSAGE;
    }

    public String toLine() {
        return MESSAGE + '\n';
    }
    
}