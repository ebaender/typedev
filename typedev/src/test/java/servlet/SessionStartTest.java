package servlet;

import org.junit.Before;
import org.junit.Test;

import common.TestUser;
import session.Session;
import translator.Message;

public class SessionStartTest extends CommandTest {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "start";
    }

    @Test
    public void startSessionNotLoggedIn() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.NEED_LOGIN.toLine());
    }

    @Test
    public void startSessionNoSession() throws Exception {
        final TestUser USER = TestUser.getAndAssertLoggedInInstance(0);
        assertCommand(USER.getKey(), getBaseCommand(), Message.YOU_HAVE_NO_SESSION.toLine());
    }

    @Test
    public void startSessionHasSession() throws Exception {
        final TestUser USER = TestUser.getAndAssertLoggedInInstance(0);
        USER.getUser().setSession(new Session(getValidLanguage()));
        assertCommand(USER.getKey(), getBaseCommand(), Message.STARTING_SESSION.toLine());
    }
    
}