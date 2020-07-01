package extra;

public class CommandBuilder {

    private final String BASE_COMMAND;

    public CommandBuilder(final String BASE_COMMAND) {
        this.BASE_COMMAND = BASE_COMMAND;
    }

    public final String buildSpecific(String command, String... arguments) {
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append(command);
        for (String argument : arguments) {
            commandBuilder.append(' ').append(argument);
        }
        System.out.println(Message.STATEMENT.toString(getClass(), "built", commandBuilder.toString()));
        return commandBuilder.toString();
    }

    public final String build(String... arguments) {
        return buildSpecific(BASE_COMMAND, arguments);
    }

}