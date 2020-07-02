package servlet;

import static org.junit.Assert.assertTrue;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import command.user.GetUsers;
import common.TestUser;
import standard.JsonStd;
import translator.Message;

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
        final TestUser USER = TestUser.getAndAssertLoggedInInstance(0);
        final String EXPECTED_RESPONSE = USER.getName() + System.lineSeparator();
        assertCommand(getValidKey(), getBaseCommand(), EXPECTED_RESPONSE);
    }

    @Test
    public void usersMultipleUsersOnline() throws Exception {
        TestUser.getAndAssertLoggedInInstancePool(TestUser.INSTANCE_LIMIT);
        final JsonObject USERS_RESP = new GetUsers(getUsers()).execute();
        assertTrue(USERS_RESP != null);
        assertTrue(USERS_RESP.get(JsonStd.MSG) != null);
        final String USER_LIST = USERS_RESP.get(JsonStd.MSG).getAsString();
        for (final TestUser USER : TestUser.getInstancePool()) {
            assertTrue(USER_LIST.contains(USER.getName()));
        }
    }

}