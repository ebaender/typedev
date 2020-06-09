package user;

import java.time.Instant;
import java.util.Collection;

public class UserTimeout implements Runnable {

    private Collection<User> users;
    private int timeout;

    public UserTimeout(Collection<User> users, int timeout) {
        this.users = users;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        checkUsers();
    }

    private synchronized void checkUsers() {
        for (User user : users) {
            if (user.getLastContact().getEpochSecond() + timeout < Instant.now().getEpochSecond()) {
                System.out.println(getClass() + " removing " + user.getName() + " after " + timeout + " seconds of contact loss");
                if (user.getSession() != null) {
                    user.getSession().getUsers().remove(user);
                }
                users.remove(user);
            }
        }
    }

}