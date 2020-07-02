package config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import command.CommandFactory;
import standard.SystemStd;
import translator.ContextAttribute;
import user.User;
import user.UserTimeout;

@WebListener
public class ContextConfiguration implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
        sce.getServletContext().setAttribute(ContextAttribute.USERS.name(), users);
        CommandFactory commandFactory = CommandFactory.getInstance(users);
        sce.getServletContext().setAttribute(ContextAttribute.COMMAND_FACTORY.name(), commandFactory);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new UserTimeout(users.values(), SystemStd.USER_TIMEOUT), 0, SystemStd.USER_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute(ContextAttribute.USERS.name());
        sce.getServletContext().removeAttribute(ContextAttribute.COMMAND_FACTORY.name());
        scheduler.shutdownNow();
    }

}