package servlet;

import org.junit.Before;
import org.junit.Test;

import common.TestUser;
import manager.KeyMan;
import session.Session;
import translator.Message;

public class SessionCreationTest extends CommandTest {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "create";
    }

    @Test
    public void createSessionNotLoggedIn() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.NEED_LOGIN.toLine());
    }

    @Test
    public void createSessionNoLanguageSpecified() throws Exception {
        TestUser user = TestUser.getAndAssertLoggedInInstance(0);
        assertCommand(user.getKey(), getBaseCommand(), Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void createSessionUnsupportedLanguage() throws Exception {
        TestUser user = TestUser.getAndAssertLoggedInInstance(0);
        final String UNSUPPORTED_LANGUAGE = KeyMan.getShortKey();
        final String COMMAND = buildCommand(UNSUPPORTED_LANGUAGE);
        assertCommand(user.getKey(), COMMAND, Message.LANGUAGE_UNSUPPORTED.toLine(UNSUPPORTED_LANGUAGE.toUpperCase()));
    }

    @Test
    public void createSessionAlreadyInSession() throws Exception {
        TestUser user = TestUser.getAndAssertLoggedInInstance(0);
        getUsers().get(user.getKey()).setSession(new Session(getValidLanguage()));
        final String COMMAND  = buildCommand(getValidLanguage());
        assertCommand(user.getKey(), COMMAND, Message.IN_SESSION_ALREADY.toLine());
    }

    @Test
    public void createSessionNotInSession() throws Exception {
        TestUser user = TestUser.getAndAssertLoggedInInstance(0);
        final String LANGUAGE = getValidLanguage();
        final String COMMAND  = buildCommand(LANGUAGE);
        assertCommand(user.getKey(), COMMAND, Message.CREATED_SESSION.toLine(LANGUAGE.toUpperCase()));
    }

}