package servlet;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import common.TestUser;
import manager.KeyMan;
import session.Session;
import translator.Message;
import user.User;

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
        final String USER_KEY = TestUser.getAndAssertLoggedInInstance(0).getKey();
        assertCommand(USER_KEY, getBaseCommand(), Message.YOU_HAVE_NO_SESSION.toLine());
    }

    @Test
    public void leaveSessionHasSession() throws Exception {
        final String USER_KEY = TestUser.getAndAssertLoggedInInstance(0).getKey();
        final String LANGUAGE = getValidLanguage();
        getUsers().get(USER_KEY).setSession(new Session(LANGUAGE));
        assertCommand(USER_KEY, getBaseCommand(), Message.LEFT_SESSION.toLine(LANGUAGE.toUpperCase()));
    }

   }