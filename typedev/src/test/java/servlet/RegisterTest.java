package servlet;

import org.junit.Before;
import org.junit.Test;

import extra.Message;
import extra.TestUser;

public class RegisterTest extends CommandTest {

    private TestUser testUser;
    private final String COMMAND = "register";

    @Before
    @Override
    public void setUp() throws Exception {
        testUser = TestUser.getInstance();
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "register";
    }

    @Test
    public void registerNoUserNameSpecified() throws Exception {
        assertCommand("key", COMMAND, Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void registerNoPasswordSpecified() throws Exception {
    }

}