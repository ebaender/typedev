package servlet;

import org.junit.Before;
import org.junit.Test;

import common.TestUser;
import translator.Message;

public class LogoutTest extends CommandTest {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "logout";
    }

    @Test
    public void logoutLoggedOutAlready() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.LOGGED_OUT_ALREADY.toLine());
    }

    @Test 
    public void logoutLoggedIn() throws Exception {
        TestUser user = TestUser.getAndAssertLoggedInInstance(0);
        assertCommand(user.getKey(), getBaseCommand(), Message.LOGGED_OUT.toLine(user.getName()));
    }

}