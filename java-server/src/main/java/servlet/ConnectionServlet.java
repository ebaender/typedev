package servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import extra.Standard;
import user.User;
import user.UserState;

@WebServlet(name = "ConnectionServlet", urlPatterns = { "connection" }, loadOnStartup = 1)
public class ConnectionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private HashMap<String, User> users;
    private HashMap<String, User> usersName;
    
    @Override
    public void init() throws ServletException {
        users = (HashMap<String, User>) getServletContext().getAttribute("usersByKey");
        usersName = (HashMap<String, User>) getServletContext().getAttribute("usersByName");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
        User user = users.get(key);
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty("code", 200);
        if (user != null) {
            jsonResp.addProperty(Standard.STA, user.getState().name().toLowerCase());
        }
        resp.getWriter().print(jsonResp);
        // System.out.println(getClass() + " responded with " + jsonResp);
    }

}