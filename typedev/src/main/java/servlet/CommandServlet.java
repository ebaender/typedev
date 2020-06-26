package servlet;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import command.help.GetHelp;
import command.session.CreateSession;
import command.session.GetSessions;
import command.session.JoinSession;
import command.session.LeaveSession;
import command.session.StartSession;
import command.user.GetName;
import command.user.GetUsers;
import command.user.LogIn;
import command.user.LogOut;
import extra.Standard;
import user.User;

@WebServlet(name = "CommandServlet", urlPatterns = { "command" }, loadOnStartup = 1)
public class CommandServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ConcurrentHashMap<String, User> users;

    @Override
    public void init() throws ServletException {
        users = (ConcurrentHashMap<String, User>) getServletContext().getAttribute("usersByKey");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
        String command = req.getParameter("command");
        // remove duplicate whitespace before splitting into arguments.
        String[] args = command.replaceAll("\\s+", " ").split(" ");
        JsonObject jsonResp = null;
        switch (args[0]) {
            case "li":
            case "login":
                jsonResp = new LogIn(key, args, users).execute();
                break;
            case "lo":
            case "logout":
                jsonResp = new LogOut(key, users).execute();
                break;
            case "ls":
            case "sessions":
                jsonResp = new GetSessions(users).execute();
                break;
            case "lu":
            case "users":
                jsonResp = new GetUsers(users).execute();
                break;
            case "wh":
            case "whoami":
                jsonResp = new GetName(key, users).execute();
                break;
            case "cr":
            case "create":
                jsonResp = new CreateSession(key, args, users).execute();
                break;
            case "jn":
            case "join":
                jsonResp = new JoinSession(key, args, users).execute();
                break;
            case "lv":
            case "leave":
                jsonResp = new LeaveSession(key, users).execute();
                break;
            case "st":
            case "start":
                jsonResp = new StartSession(key, users).execute();
                break;
            case "?":
            case "help":
                jsonResp = new GetHelp().execute();
                break;
            default:
                jsonResp = new JsonObject();
                jsonResp.addProperty(Standard.MSG, "");
                break;
        }
        resp.getWriter().print(jsonResp);
        System.out.println(getClass() + " responded with " + jsonResp);
    }

}