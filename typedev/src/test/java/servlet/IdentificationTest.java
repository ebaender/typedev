package servlet;

import static matcher.JsonPropertyMatcher.hasJsonProperty;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import extra.Message;
import extra.Standard;
import extra.TestUser;

public class IdentificationTest extends CommandTest {

    private TestUser testUser;

    @Before
    @Override
    public void setUp() throws Exception {
        testUser = TestUser.getInstance();
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "whoami";
    }

    @Test
    public void identifyLoggedOut() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.YOU_ARE_NOBODY.toLine());
    }

    @Test 
    public void identifyLoggedIn() throws Exception {
        JsonObject loginResp = testUser.logIn(getUsers());
        assertThat(loginResp, hasJsonProperty(Standard.MSG, Message.LOGIN_SUCCESS.toLine(testUser.getName())));
        assertCommand(testUser.getKey(), getBaseCommand(), Message.YOU_ARE.toLine(testUser.getName()));
    }

}