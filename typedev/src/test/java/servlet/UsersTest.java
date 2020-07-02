package servlet;

import static matcher.JsonPropertyMatcher.hasJsonProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import command.user.GetUsers;
import common.TestUser;
import translator.Message;
import standard.JsonStd;

public class UsersTest extends CommandTest {

    @Before
    @Override
    public void setUp() throws Exception {
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
    public void usersSingleUserOnline() throws Exception {
        TestUser testUser = TestUser.getInstance(0);
        JsonObject loginResp = testUser.logIn(getUsers());
        assertThat(loginResp, hasJsonProperty(JsonStd.MSG, Message.LOGIN_SUCCESS.toLine(testUser.getName())));
        final String EXPECTED_RESPONSE = testUser.getName() + System.lineSeparator();
        assertCommand(getValidKey(), getBaseCommand(), EXPECTED_RESPONSE);
    }

    @Test
    public void usersMultipleUsersOnline() throws Exception {
        TestUser.instantiateAll();
        for (TestUser testUser : TestUser.getInstancePool()) {
            JsonObject loginResp = testUser.logIn(getUsers());
            assertThat(loginResp, hasJsonProperty(JsonStd.MSG, Message.LOGIN_SUCCESS.toLine(testUser.getName())));
        }
        JsonObject usersResp = new GetUsers(getUsers()).execute();
        assertTrue(usersResp != null);
        assertTrue(usersResp.get(JsonStd.MSG) != null);
        String userList = usersResp.get(JsonStd.MSG).getAsString();
        for (TestUser testUser : TestUser.getInstancePool()) {
            assertTrue(userList.contains(testUser.getName()));
        }
    }

}