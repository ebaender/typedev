package servlet;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import extra.ContextAttribute;
import extra.Message;
import extra.JsonStan;
import user.User;

@WebServlet(name = "SyncServlet", urlPatterns = { "sync" }, loadOnStartup = 1)
public class SyncServlet extends HttpServlet {

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
        if (user != null && user.getSession() != null && user.getSession().isLive()) {
            String progress = req.getParameter("progress");
            String mistakes = req.getParameter("mistakes");
            if (progress != null) {
                try {
                    user.setProgress(Integer.parseInt(progress));
                    user.setMistakes(Integer.parseInt(mistakes));
                    JsonArray sessionProgress = new JsonArray();
                    for (User sessionUser : user.getSession().getUsers()) {
                        JsonObject userProgress = new JsonObject();
                        userProgress.addProperty("name", sessionUser.getName());
                        userProgress.addProperty("progress", sessionUser.getProgress());
                        userProgress.addProperty("mistakes", sessionUser.getMistakes());
                        sessionProgress.add(userProgress);
                    }
                    jsonResp.add(JsonStan.PRG, sessionProgress);
                    user.getSession().think();
                } catch (NumberFormatException e) {
                    System.out.println(getClass() + " could not parse progress string " + progress);
                }
            }
        }
        resp.getWriter().print(jsonResp);
        System.out.println(Message.STATEMENT.toString(getClass(), "responded", jsonResp));
    }

}