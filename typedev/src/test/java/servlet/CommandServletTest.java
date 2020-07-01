package servlet;

import org.junit.Test;

import extra.Message;

public class CommandServletTest extends CommandTest {

    @Override
    public String getBaseCommand() {
        return "";
    }

    @Test
    public void missingKey() throws Exception {
        assertCommand(null, getBaseCommand(), Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void missingCommand() throws Exception {
        assertCommand(getValidKey(), null, Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void unknownCommand() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.COMMAND_NOT_FOUND.toLine());
    }

}
