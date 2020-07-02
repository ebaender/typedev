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
import extra.JsonStan;

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
        String key = req.getParameter(JsonStan.KEY);
        String commandString = req.getParameter(JsonStan.COM);
        JsonObject jsonResp = null;
        if (key != null && commandString != null) {
            // incoming request is usable.
            Command command = commandFactory.create(commandString, key);
            if (command != null) {
                // requested command exists.
                jsonResp = command.execute();
            } else {
                jsonResp = new JsonObject();
                jsonResp.addProperty(JsonStan.MSG, Message.COMMAND_NOT_FOUND.toLine());
            }
        } else {
            jsonResp = new JsonObject();
            jsonResp.addProperty(JsonStan.MSG, Message.ARGS_NOT_RECEIVED.toLine());
        }
        resp.getWriter().print(jsonResp);
        System.out.println(Message.STATEMENT.toString(getClass(), "responded", jsonResp));
    }

    protected void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

}