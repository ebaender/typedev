package servlet;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import extra.ContextAttribute;
import extra.Message;
import extra.JsonStan;
import user.User;
import user.UserState;

@WebServlet(name = "ResultServlet", urlPatterns = { "result" }, loadOnStartup = 1)
public class ResultServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ConcurrentHashMap<String, User> users;

    @Override
    public void init() throws ServletException {
        users = (ConcurrentHashMap<String, User>) getServletContext().getAttribute(ContextAttribute.USERS.name());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter(JsonStan.KEY);
        User user = users.get(key);
        JsonObject jsonResp = new JsonObject();
        if (user != null && user.getState() == UserState.FINISHED) {
            jsonResp.add(JsonStan.RES, user.getSession().getResult());
            user.setSession(null);
            user.setState(UserState.DEFAULT);
        }
        resp.getWriter().print(jsonResp);
        System.out.println(Message.STATEMENT.toString(getClass(), "responded", jsonResp));
    }

}