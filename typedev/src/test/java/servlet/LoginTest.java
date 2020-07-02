package servlet;

import org.junit.Test;

import common.TestUser;
import translator.Message;
import user.User;

public class LoginTest extends CommandTest {

    @Override
    public String getBaseCommand() {
        return "login";
    }

    @Test
    public void loginNoUserSpecified() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.ARGS_NOT_RECEIVED.toLine());
    }

    public void assertLoginNameConflict(final String NEW_USER_KEY, final String LOGGED_IN_USER_KEY,
            final User LOGGED_IN_USER, final String EXPECTED_RESPONSE) throws Exception {
        final String COMMAND = buildCommand(LOGGED_IN_USER.getName(),
                LOGGED_IN_USER.getPassword());
        getUsers().put(LOGGED_IN_USER_KEY, LOGGED_IN_USER);
        assertCommand(NEW_USER_KEY, COMMAND, EXPECTED_RESPONSE);
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
        final String COMMAND = buildCommand(ILLEGAL_USER_NAME, getValidPassword());
        assertCommand(getValidKey(), COMMAND, Message.USER_NOT_FOUND.toLine(ILLEGAL_USER_NAME));
    }

    public void assertLoginPasswordConflict(final User REGISTERED_USER, final String PASSWORD,
            final String EXPECTED_RESPONSE) throws Exception {
        final String COMMAND = buildCommand(REGISTERED_USER.getName(), PASSWORD);
        assertCommand(getValidKey(), COMMAND, EXPECTED_RESPONSE);
    }

    @Test
    public void loginWrongPassword() throws Exception {
        final TestUser USER = TestUser.getInstance(0);
        assertLoginPasswordConflict(USER, USER.getPassword() + '~', Message.WRONG_PASSWORD.toLine());
    }

    @Test
    public void loginCorrectPassword() throws Exception {
        final TestUser USER = TestUser.getInstance(0);
        assertLoginPasswordConflict(USER, USER.getPassword(), Message.LOGIN_SUCCESS.toLine(USER.getName()));
    }

}