package extra;

public class CommandBuilder {

    private final String BASE_COMMAND;

    public CommandBuilder(final String BASE_COMMAND) {
        this.BASE_COMMAND = BASE_COMMAND;
    }

    public final String build(String... arguments) {
        StringBuilder command = new StringBuilder();
        command.append(BASE_COMMAND);
        for (String argument : arguments) {
            command.append(' ').append(argument);
        }
        System.out.println(Message.STATEMENT.toString(getClass(), "built", command.toString()));
        return command.toString();
    }

}