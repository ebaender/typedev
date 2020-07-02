package servlet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import common.TestUser;
import session.Session;
import standard.JsonStd;
import translator.Message;

public class SessionsTest extends CommandTest {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public String getBaseCommand() {
        return "sessions";
    }

    @Test
    public void sessionsNoSessionsAvailable() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.NO_SESSIONS.toLine());
    }

    @Test
    public void sessionsSingleSessionAvailable() throws Exception {
        final TestUser USER = TestUser.getAndAssertLoggedInInstance(0);
        final String LANGUAGE = getValidLanguage();
        USER.getUser().setSession(new Session(LANGUAGE));
        final JsonObject SESSIONS_RESP = executeCommand(getValidKey(), getBaseCommand());
        final String SESSIONS_LIST = SESSIONS_RESP.get(JsonStd.MSG).getAsString();
        assertThat(SESSIONS_LIST, containsString(LANGUAGE.toUpperCase()));
    }

    @Test
    public void sessionsMultipleSessionsAvailable() throws Exception {
        TestUser.getAndAssertLoggedInInstancePool(TestUser.INSTANCE_LIMIT);
        for (TestUser USER : TestUser.getInstancePool()) {
            USER.getUser().setSession(new Session(getValidLanguage()));
        }
        final JsonObject SESSIONS_RESP = executeCommand(getValidKey(), getBaseCommand());
        assertThat(SESSIONS_RESP.get(JsonStd.MSG), notNullValue());
        final String SESSIONS_LIST = SESSIONS_RESP.get(JsonStd.MSG).getAsString();
        for (final TestUser USER : TestUser.getInstancePool()) {
            assertThat(SESSIONS_LIST, containsString(USER.getUser().getSession().getLanguage().toUpperCase()));
        }
    }

}