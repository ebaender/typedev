package servlet;

import org.junit.Test;

import extra.Message;

public class CommandServletTest extends CommandTest {

    @Override
    public String getBaseCommand() {
        return "";
    }

    @Test
    public void missingKeyTest() throws Exception {
        assertCommand(null, getBaseCommand(), Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void missingCommandTest() throws Exception {
        assertCommand("key", null, Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void unknownCommandTest() throws Exception {
        assertCommand("key", getBaseCommand(), Message.COMMAND_NOT_FOUND.toLine());
    }

}
