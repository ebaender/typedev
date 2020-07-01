package servlet;

import static matcher.JsonPropertyMatcher.hasJsonProperty;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import extra.KeyMan;
import extra.Message;
import extra.Standard;
import extra.TestUser;
import session.Session;

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
        assertCommand(getValidKey(), getBaseCommand(), Message.CREATE_SESSION_NO_LOGIN.toLine());
    }

    public TestUser getAndAssertLoggedInTestUser(int instanceIndex) {
        TestUser user = TestUser.getInstance(instanceIndex);
        assertThat(user.logIn(getUsers()), hasJsonProperty(Standard.MSG, Message.LOGIN_SUCCESS.toLine(user.getName())));
        return user;
    }

    @Test
    public void createSessionNoLanguageSpecified() throws Exception {
        TestUser user = getAndAssertLoggedInTestUser(0);
        assertCommand(user.getKey(), getBaseCommand(), Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void createSessionUnsupportedLanguage() throws Exception {
        TestUser user = getAndAssertLoggedInTestUser(0);
        final String UNSUPPORTED_LANGUAGE = KeyMan.getShortKey();
        final String COMMAND = buildCommand(UNSUPPORTED_LANGUAGE);
        assertCommand(user.getKey(), COMMAND, Message.LANGUAGE_UNSUPPORTED.toLine(UNSUPPORTED_LANGUAGE.toUpperCase()));
    }

    @Test
    public void createSessionAlreadyInSession() throws Exception {
        TestUser user = getAndAssertLoggedInTestUser(0);
        getUsers().get(user.getKey()).setSession(new Session(getValidLanguage()));
        final String COMMAND  = buildCommand(getValidLanguage());
        assertCommand(user.getKey(), COMMAND, Message.IN_SESSION_ALREADY.toLine());
    }

    @Test
    public void createSessionNotInSession() throws Exception {
        TestUser user = getAndAssertLoggedInTestUser(0);
        final String LANGUAGE = getValidLanguage();
        final String COMMAND  = buildCommand(LANGUAGE);
        assertCommand(user.getKey(), COMMAND, Message.CREATED_SESSION.toLine(LANGUAGE.toUpperCase()));
    }

}