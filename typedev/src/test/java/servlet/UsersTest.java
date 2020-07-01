package servlet;

import static matcher.JsonPropertyMatcher.hasJsonProperty;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import extra.Message;
import extra.Standard;
import extra.TestUser;

public class UsersTest extends CommandTest {

    private TestUser testUser;

    @Before
    @Override
    public void setUp() throws Exception {
        testUser = TestUser.getInstance();
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "users";
    }

    @Test
    public void usersNobodyOnline() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.NOBODY_HERE.toLine());
    }

    @Test 
    public void usersSomeoneOnline() throws Exception {
        JsonObject loginResp = testUser.logIn(getUsers());
        assertThat(loginResp, hasJsonProperty(Standard.MSG, Message.LOGIN_SUCCESS.toLine(testUser.getName())));
        final String EXPECTED_RESPONSE_SOMEONE_ONLINE = testUser.getName() + System.lineSeparator();
        assertCommand(testUser.getKey(), getBaseCommand(), EXPECTED_RESPONSE_SOMEONE_ONLINE);
    }

}