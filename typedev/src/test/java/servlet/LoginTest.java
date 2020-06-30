package servlet;

import org.junit.Test;

import extra.Message;

public class LoginTest extends CommandTest {

    @Test
    public void loginNoUserSpecifiedTest() throws Exception {
        assertCommand("", "login", Message.ARGS_NOT_RECEIVED);
    }

}
