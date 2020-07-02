package servlet;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import common.TestUser;
import manager.KeyMan;
import session.Session;
import translator.Message;

public class SessionJoinTest extends CommandTest {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "join";
    }

    @Test
    public void joinSessionNotLoggedIn() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.NEED_LOGIN.toLine());
    }

    @Test
    public void joinSessionNoTargetUserSpecified() throws Exception {
        final TestUser USER = TestUser.getAndAssertLoggedInInstance(0);
        assertCommand(USER.getKey(), getBaseCommand(), Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void joinSessionInvalidTargetUser() throws Exception {
        final TestUser USER = TestUser.getAndAssertLoggedInInstance(0);
        final String INVALID_NAME = KeyMan.getShortKey();
        final String COMMAND = buildCommand(INVALID_NAME);
        assertCommand(USER.getKey(), COMMAND, Message.USER_NOT_LOGGED_IN.toLine(INVALID_NAME));
    }

    @Test
    public void joinSessionInSessionAlready() throws Exception {
        final List<TestUser> USERS = TestUser.getAndAssertLoggedInInstancePool(2);
        for (final TestUser USER : USERS) {
            getUsers().get(USER.getKey()).setSession(new Session(getValidLanguage()));
        }
        final String JOINED_USER_NAME = USERS.get(0).getName();
        final String JOINING_USER_KEY = USERS.get(1).getKey();
        final String COMMAND = buildCommand(JOINED_USER_NAME);
        assertCommand(JOINING_USER_KEY, COMMAND, Message.IN_SESSION_ALREADY.toLine());
    }

    @Test
    public void joinSessionNoTargetSession() throws Exception {
        final List<TestUser> USERS = TestUser.getAndAssertLoggedInInstancePool(2);
        final String JOINED_USER_NAME = USERS.get(0).getName();
        final String JOINING_USER_KEY = USERS.get(1).getKey();
        final String COMMAND = buildCommand(JOINED_USER_NAME);
        assertCommand(JOINING_USER_KEY, COMMAND, Message.USER_HAS_NO_SESSION.toLine(JOINED_USER_NAME));
    }

    @Test
    public void joinSessionIsAlreadyLive() throws Exception {
        final List<TestUser> USERS = TestUser.getAndAssertLoggedInInstancePool(2);
        final TestUser JOINED_USER = USERS.get(0);
        JOINED_USER.getUser().setSession(new Session(getValidLanguage()));
        JOINED_USER.getUser().getSession().start();
        final TestUser JOINING_USER = USERS.get(1);
        final String COMMAND = buildCommand(JOINED_USER.getName());
        assertCommand(JOINING_USER.getKey(), COMMAND, Message.SESSION_ALREADY_LIVE.toLine(JOINED_USER.getName()));
    }

    @Test
    public void joinLobbySession() throws Exception {
        final List<TestUser> USERS = TestUser.getAndAssertLoggedInInstancePool(2);
        final TestUser JOINED_USER = USERS.get(0);
        final String LANGUAGE = getValidLanguage();
        JOINED_USER.getUser().setSession(new Session(LANGUAGE));
        final String JOINING_USER_KEY = USERS.get(1).getKey();
        final String COMMAND = buildCommand(JOINED_USER.getName());
        assertCommand(JOINING_USER_KEY, COMMAND,
                Message.JOINED_SESSION.toLine(JOINED_USER.getName(), LANGUAGE.toUpperCase()));
    }

}