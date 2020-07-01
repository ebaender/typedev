package servlet;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import extra.ContextAttribute;
import extra.Message;
import extra.Standard;
import user.User;

@WebServlet(name = "ConnectionServlet", urlPatterns = { "connection" }, loadOnStartup = 1)
public class ConnectionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ConcurrentHashMap<String, User> users;

    @Override
    public void init() throws ServletException {
        users = (ConcurrentHashMap<String, User>) getServletContext().getAttribute(ContextAttribute.USERS.name());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonResp = new JsonObject();
        String key = req.getParameter(Standard.KEY);
        User user = users.get(key);
        if (user != null) {
            user.setLastContact(Instant.now());
            jsonResp.addProperty(Standard.STA, user.getState().name().toLowerCase());
        }
        resp.getWriter().print(jsonResp);
        // System.out.println(Message.STATEMENT.toString(getClass(), "responded", jsonResp));
    }

}