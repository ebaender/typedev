package servlet;

import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import command.CommandFactory;
import common.CommandBuilder;
import common.TestUser;
import manager.ResourceMan;
import standard.JsonStd;
import user.User;

import static matcher.JsonPropertyMatcher.hasJsonProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public abstract class CommandTest {

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private RequestDispatcher reqDispatcher;

    private CommandFactory commandFactory = null;
    private ConcurrentHashMap<String, User> users = null;
    private CommandBuilder commandBuilder = null;

    @Before
    public void setUp() throws Exception {
        users = new ConcurrentHashMap<>();
        commandFactory = CommandFactory.getInstance(users);
        commandFactory.setUsers(users);
        commandBuilder = new CommandBuilder(getBaseCommand());
        TestUser.setUsers(users);
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        TestUser.reset();
    }

    public abstract String getBaseCommand();

    public String buildCommand(String... arguments) {
        return commandBuilder.build(arguments);
    }

    public String buildSpecificCommand(String command, String... arguments) {
        return commandBuilder.buildSpecific(command, arguments);
    }

    protected String getValidKey() {
        return "key";
    }

    protected String getValidName() {
        return "name";
    }

    protected String getValidPassword() {
        return "password";
    }

    protected String getValidLanguage() {
        return ResourceMan.getRandomLanguage();
    }

    protected void assertCommand(final String KEY, final String COMMAND, final String EXPECTED_RESPONSE)
            throws Exception {
        JsonObject commandResp = executeCommand(KEY, COMMAND);
        assertThat(commandResp, hasJsonProperty(JsonStd.MSG, EXPECTED_RESPONSE));
    }

    protected JsonObject executeCommand(final String KEY, final String COMMAND) throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(req.getParameter(JsonStd.KEY)).thenReturn(KEY);
        when(req.getParameter(JsonStd.COM)).thenReturn(COMMAND);
        when(resp.getWriter()).thenReturn(printWriter);

        CommandServlet commandServlet = new CommandServlet();
        commandServlet.setCommandFactory(commandFactory);
        commandServlet.doPost(req, resp);

        JsonElement commandResp = JsonParser.parseString(stringWriter.toString());
        assertFalse(commandResp == null);
        return commandResp.getAsJsonObject();
    }

    protected CommandFactory getCommandFactory() {
        return commandFactory;
    }

    protected ConcurrentHashMap<String, User> getUsers() {
        return users;
    }

}
