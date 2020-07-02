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
        TestUser user = TestUser.getAndAssertLoggedInInstance(0);
        assertCommand(user.getKey(), getBaseCommand(), Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void joinSessionInvalidTargetUser() throws Exception {
        TestUser user = TestUser.getAndAssertLoggedInInstance(0);
        final String INVALID_NAME = KeyMan.getShortKey();
        final String COMMAND = buildCommand(INVALID_NAME);
        assertCommand(user.getKey(), COMMAND, Message.USER_NOT_LOGGED_IN.toLine(INVALID_NAME));
    }

    @Test
    public void joinSessionInSessionAlready() throws Exception {
        List<TestUser> users = TestUser.getAndAssertLoggedInInstancePool(2);
        for (TestUser user : users) {
            getUsers().get(user.getKey()).setSession(new Session(getValidLanguage()));
        }
        final String JOINED_USER_NAME = users.get(0).getName();
        final String JOINING_USER_KEY = users.get(1).getKey();
        final String COMMAND = buildCommand(JOINED_USER_NAME);
        assertCommand(JOINING_USER_KEY, COMMAND, Message.IN_SESSION_ALREADY.toLine());
    }

    @Test
    public void joinSessionNoTargetSession() throws Exception {
        List<TestUser> users = TestUser.getAndAssertLoggedInInstancePool(2);
        final String JOINED_USER_NAME = users.get(0).getName();
        final String JOINING_USER_KEY = users.get(1).getKey();
        final String COMMAND = buildCommand(JOINED_USER_NAME);
        assertCommand(JOINING_USER_KEY, COMMAND, Message.USER_HAS_NO_SESSION.toLine(JOINED_USER_NAME));
    }

    @Test
    public void joinSessionIsAlreadyLive() throws Exception {
        List<TestUser> users = TestUser.getAndAssertLoggedInInstancePool(2);
        final User JOINED_USER = getUsers().get(users.get(0).getKey());
        JOINED_USER.setSession(new Session(getValidLanguage()));
        JOINED_USER.getSession().start();
        final String JOINING_USER_KEY = users.get(1).getKey();
        final String COMMAND = buildCommand(JOINED_USER.getName());
        assertCommand(JOINING_USER_KEY, COMMAND, Message.SESSION_ALREADY_LIVE.toLine(JOINED_USER.getName()));
    }

    @Test
    public void joinLobbySession() throws Exception {
        List<TestUser> users = TestUser.getAndAssertLoggedInInstancePool(2);
        final User JOINED_USER = getUsers().get(users.get(0).getKey());
        final String LANGUAGE = getValidLanguage();
        JOINED_USER.setSession(new Session(LANGUAGE));
        final String JOINING_USER_KEY = users.get(1).getKey();
        final String COMMAND = buildCommand(JOINED_USER.getName());
        assertCommand(JOINING_USER_KEY, COMMAND,
                Message.JOINED_SESSION.toLine(JOINED_USER.getName(), LANGUAGE.toUpperCase()));
    }

}