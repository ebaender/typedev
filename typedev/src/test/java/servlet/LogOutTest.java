package servlet;

import static matcher.JsonPropertyMatcher.hasJsonProperty;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import extra.Message;
import extra.Standard;
import extra.TestUser;

public class LogOutTest extends CommandTest {

    private TestUser testUser;

    @Before
    @Override
    public void setUp() throws Exception {
        testUser = TestUser.getInstance();
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "logout";
    }

    @Test
    public void logoutLoggedOutAlready() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.LOGGED_OUT_ALREADY.toLine());
    }

    @Test 
    public void logoutLoggedIn() throws Exception {
        JsonObject loginResp = testUser.logIn(getUsers());
        assertThat(loginResp, hasJsonProperty(Standard.MSG, Message.LOGIN_SUCCESS.toLine(testUser.getName())));
        assertCommand(testUser.getKey(), getBaseCommand(), Message.LOGGED_OUT.toLine(testUser.getName()));
    }

}