package extra;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import user.User;
import user.UserTimeout;

@WebListener
public class ContextConfiguration implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private int timeout = 2;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HashMap<String, User> users = new HashMap<>();
        sce.getServletContext().setAttribute("usersByKey", users);
        sce.getServletContext().setAttribute("usersByName", new HashMap<String, User>());
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // scheduler.scheduleAtFixedRate(new UserTimeout(users.values(), timeout), 0, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("usersByKey");
        sce.getServletContext().removeAttribute("usersByName");
        scheduler.shutdownNow();
    }

}