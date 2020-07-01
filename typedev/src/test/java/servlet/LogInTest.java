package servlet;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import extra.Message;
import extra.TestUser;
import user.User;

public class LogInTest extends CommandTest {

    private TestUser testUser;

    @Before
    @Override
    public void setUp() throws Exception {
        testUser = TestUser.getInstance();
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "login";
    }

    @Test
    public void loginNoUserSpecified() throws Exception {
        final String COMMAND_LOG_IN_NO_ARGUMENTS = getBaseCommand();
        assertCommand(getValidKey(), COMMAND_LOG_IN_NO_ARGUMENTS, Message.ARGS_NOT_RECEIVED.toLine());
    }

    public void assertLoginNameConflict(final String NEW_USER_KEY, final String LOGGED_IN_USER_KEY,
            final User LOGGED_IN_USER, final String EXPECTED_RESPONSE) throws Exception {
        final String COMMAND_LOG_IN_AS_LOGGED_IN_USER = buildCommand(LOGGED_IN_USER.getName(),
                LOGGED_IN_USER.getPassword());
        getUsers().put(LOGGED_IN_USER_KEY, LOGGED_IN_USER);
        assertCommand(NEW_USER_KEY, COMMAND_LOG_IN_AS_LOGGED_IN_USER, EXPECTED_RESPONSE);
    }

    @Test
    public void loginLoggedInAlready() throws Exception {
        final String KEY = getValidKey();
        final String NAME = getValidName();
        assertLoginNameConflict(KEY, KEY, new User(NAME, getValidPassword()), Message.LOGGED_IN_ALREADY.toLine(NAME));
    }

    @Test
    public void loginOtherUserLoggedInAlready() throws Exception {
        final String NAME = getValidName();
        assertLoginNameConflict("firstKey", "secondKey", new User(NAME, getValidPassword()),
                Message.OTHER_USER_LOGGED_IN_ALREADY.toLine(NAME));
    }

    @Test
    public void loginUserDoesNotExist() throws Exception {
        final String ILLEGAL_USER_NAME = "adDFJ(*433?/Z|==4Dsd=0``<fdDF{}}#f:kj";
        final String COMMAND_LOG_IN_ILLEGAL_NAME = buildCommand(ILLEGAL_USER_NAME, getValidPassword());
        assertCommand(getValidKey(), COMMAND_LOG_IN_ILLEGAL_NAME, Message.USER_NOT_FOUND.toLine(ILLEGAL_USER_NAME));
    }

    public void assertLoginPasswordConflict(final User REGISTERED_USER, final String PASSWORD,
            final String EXPECTED_RESPONSE) throws Exception {
        final String COMMAND_LOG_IN = buildCommand(REGISTERED_USER.getName(), PASSWORD);
        assertCommand(getValidKey(), COMMAND_LOG_IN, EXPECTED_RESPONSE);
    }

    @Test
    public void loginWrongPassword() throws Exception {
        assertFalse(testUser == null);
        assertLoginPasswordConflict(testUser, testUser.getPassword() + '~', Message.WRONG_PASSWORD.toLine());
    }

    @Test
    public void loginCorrectPassword() throws Exception {
        assertFalse(testUser == null);
        assertLoginPasswordConflict(testUser, testUser.getPassword(), Message.LOGIN_SUCCESS.toLine(testUser.getName()));
    }

}