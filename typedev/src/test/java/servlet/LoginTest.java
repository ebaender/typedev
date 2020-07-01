package servlet;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import extra.Message;
import extra.TestUser;
import user.User;

public class LoginTest extends CommandTest {

    private TestUser testUser;

    // private void registerTestUser() {
    //     testUser = null;
    //     try {
    //         testUser = TestUser.getInstance("junit", "@Test");
    //     } catch (DBException e) {
    //         System.out.println(getClass() + "Can not guarantee existence of test user. "
    //                 + Message.UNKNOWN_ERROR.toString(e.getErrorCode()));
    //     }
    // }

    @Before
    @Override
    public void setUp() throws Exception {
        testUser = TestUser.getInstance();
        super.setUp();
    }

    @Test
    public void loginNoUserSpecifiedTest() throws Exception {
        final String COMMAND_LOG_IN_NO_ARGUMENTS = "login";
        assertCommand("KEY", COMMAND_LOG_IN_NO_ARGUMENTS, Message.ARGS_NOT_RECEIVED.toLine());
    }

    public void assertLoginNameConflict(final String NEW_USER_KEY, final String LOGGED_IN_USER_KEY,
            final User LOGGED_IN_USER, final String EXPECTED_RESPONSE) throws Exception {
        final String COMMAND_LOG_IN_AS_LOGGED_IN_USER = "login " + LOGGED_IN_USER.getName() + " "
                + LOGGED_IN_USER.getPassword();
        getUsers().put(LOGGED_IN_USER_KEY, LOGGED_IN_USER);
        assertCommand(NEW_USER_KEY, COMMAND_LOG_IN_AS_LOGGED_IN_USER, EXPECTED_RESPONSE);
    }

    @Test
    public void loginLoggedInAlready() throws Exception {
        assertLoginNameConflict("key", "key", new User("name", "password"), Message.LOGGED_IN_ALREADY.toLine("name"));
    }

    @Test
    public void loginOtherUserLoggedInAlready() throws Exception {
        assertLoginNameConflict("firstKey", "secondKey", new User("name", "password"),
                Message.OTHER_USER_LOGGED_IN_ALREADY.toLine("name"));
    }

    public void assertLoginPasswordConflict(final User REGISTERED_USER, final String PASSWORD,
            final String EXPECTED_RESPONSE) throws Exception {
        final String COMMAND_LOG_IN = "login " + REGISTERED_USER.getName() + " " + PASSWORD;
        assertCommand("key", COMMAND_LOG_IN, EXPECTED_RESPONSE);
    }

    @Test 
    public void loginUserDoesNotExist() throws Exception {
        final String ILLEGAL_USER_NAME = "adDFJ(*433?/Z|==4Dsd=0``<fdDF{}}#f:kj";
        final String COMMAND_LOG_IN_ILLEGAL_NAME = "login " + ILLEGAL_USER_NAME + " password";
        assertCommand("key", COMMAND_LOG_IN_ILLEGAL_NAME, Message.USER_NOT_FOUND.toLine(ILLEGAL_USER_NAME));
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