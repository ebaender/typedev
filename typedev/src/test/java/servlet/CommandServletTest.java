package servlet;

import org.junit.Test;

import extra.Message;

public class CommandServletTest extends CommandTest {

    @Test
    public void missingKeyTest() throws Exception {
        assertCommand(null, "", Message.ARGS_NOT_RECEIVED);
    }

    @Test
    public void missingCommandTest() throws Exception {
        assertCommand("", null, Message.ARGS_NOT_RECEIVED);
    }

    @Test
    public void unknownCommandTest() throws Exception {
        assertCommand("", "", Message.COMMAND_NOT_FOUND);
    }

}
