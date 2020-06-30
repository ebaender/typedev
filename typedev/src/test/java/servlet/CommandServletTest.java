package servlet;

import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import command.CommandFactory;
import extra.Message;
import extra.Standard;
import user.User;

import static matcher.JsonPropertyMatcher.hasJsonProperty;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommandServletTest {

    @Mock private HttpServletRequest req;
    @Mock private HttpServletResponse resp;
    @Mock private RequestDispatcher reqDispatcher;

    private CommandFactory commandFactory;
    private ConcurrentHashMap<String, User> users;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        users = new ConcurrentHashMap<>();
        commandFactory = CommandFactory.getInstance(users);
    }

    private void base(final String KEY, final String COMMAND, Message expectedResponse) throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(req.getParameter(Standard.KEY)).thenReturn(KEY);
        when(req.getParameter(Standard.COM)).thenReturn(COMMAND);
        when(resp.getWriter()).thenReturn(printWriter);

        CommandServlet commandServlet = new CommandServlet();
        commandServlet.setCommandFactory(commandFactory);
        commandServlet.doPost(req, resp);

        JsonElement jsonResp = JsonParser.parseString(stringWriter.toString());
        assertThat(jsonResp, hasJsonProperty(Standard.MSG, expectedResponse.toLine()));
    }

    @Test
    public void missingKeyTest() throws Exception {
        base(null, "", Message.ARGS_NOT_RECEIVED);
    }

    @Test
    public void missingCommandTest() throws Exception {
        base("", null, Message.ARGS_NOT_RECEIVED);
    }

    @Test
    public void unknownCommandTest() throws Exception {
        base("", "", Message.COMMAND_NOT_FOUND);
    }

}
