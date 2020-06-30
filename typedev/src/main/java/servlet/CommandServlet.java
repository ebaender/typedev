package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import command.Command;
import command.CommandFactory;
import extra.ContextAttribute;
import extra.Message;
import extra.Standard;

@WebServlet(name = "CommandServlet", urlPatterns = { "command" }, loadOnStartup = 1)
public class CommandServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CommandFactory commandFactory;

    @Override
    public void init() throws ServletException {
        commandFactory = (CommandFactory) getServletContext().getAttribute(ContextAttribute.COMMAND_FACTORY.name());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter(Standard.KEY);
        String commandString = req.getParameter(Standard.COM);
        JsonObject jsonResp = null;
        if (key != null && commandString != null) {
            Command command = commandFactory.create(commandString, key);
            if (command != null) {
                jsonResp = command.execute();
            } else {
                jsonResp = new JsonObject();
                jsonResp.addProperty(Standard.MSG, Message.COMMAND_NOT_FOUND.toLine());
            }
        } else {
            jsonResp = new JsonObject();
            jsonResp.addProperty(Standard.MSG, Message.ARGS_NOT_RECEIVED.toLine());
        }
        resp.getWriter().print(jsonResp);
        System.out.println(getClass() + Message.RESPONDED_WITH.toString() + jsonResp);
    }

    protected void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

}