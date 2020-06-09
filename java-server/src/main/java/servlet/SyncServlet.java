package servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import extra.Standard;
import user.User;

@WebServlet(name = "SyncServlet", urlPatterns = { "sync" }, loadOnStartup = 1)
public class SyncServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private HashMap<String, User> users;
    
    @Override
    public void init() throws ServletException {
        users = (HashMap<String, User>) getServletContext().getAttribute("usersByKey");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
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
                    jsonResp.add(Standard.PRG, sessionProgress);
                    user.getSession().think();
                } catch (NumberFormatException e) {
                    System.out.println(getClass() + " could not parse progress string " + progress);
                }
            }
        }
        resp.getWriter().print(jsonResp);
        System.out.println(getClass() + " responded with " + jsonResp);
    }

}