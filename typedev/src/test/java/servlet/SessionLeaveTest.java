package servlet;

import org.junit.Before;
import org.junit.Test;

import common.TestUser;
import session.Session;
import translator.Message;

public class SessionLeaveTest extends CommandTest {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "leave";
    }

    @Test
    public void leaveSessionNotLoggedIn() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.NEED_LOGIN.toLine());
    }

    @Test
    public void leaveSessionNoSession() throws Exception {
        final TestUser USER = TestUser.getAndAssertLoggedInInstance(0);
        assertCommand(USER.getKey(), getBaseCommand(), Message.YOU_HAVE_NO_SESSION.toLine());
    }

    @Test
    public void leaveSessionHasSession() throws Exception {
        final TestUser USER = TestUser.getAndAssertLoggedInInstance(0);
        final String LANGUAGE = getValidLanguage();
        USER.getUser().setSession(new Session(LANGUAGE));
        assertCommand(USER.getKey(), getBaseCommand(), Message.LEFT_SESSION.toLine(LANGUAGE.toUpperCase()));
    }

   }