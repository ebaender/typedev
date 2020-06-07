package extra;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import session.Session;
import user.User;

@WebListener
public class ContextConfiguration implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("usersByKey", new HashMap<String, User>());
        sce.getServletContext().setAttribute("usersByName", new HashMap<String, User>());
        // sce.getServletContext().setAttribute("sessions", new HashMap<User, Session>());
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // scheduler.scheduleAtFixedRate(new TimeoutTest(), 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("usersByKey");
        sce.getServletContext().removeAttribute("userByName");
        // sce.getServletContext().removeAttribute("sessions");
        scheduler.shutdownNow();
    }

}