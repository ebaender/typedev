package servlet;

import org.junit.Before;
import org.junit.Test;

import common.TestUser;
import translator.Message;

public class IdentificationTest extends CommandTest {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "whoami";
    }

    @Test
    public void identifyLoggedOut() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.YOU_ARE_NOBODY.toLine());
    }

    @Test 
    public void identifyLoggedIn() throws Exception {
        TestUser user = TestUser.getAndAssertLoggedInInstance(0);
        assertCommand(user.getKey(), getBaseCommand(), Message.YOU_ARE.toLine(user.getName()));
    }

}