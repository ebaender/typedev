package user;

import session.Session;

public class User {

    private String name;
    private Session session;
    private UserState state;
    private Integer progress;

    public User(String name) {
        this.setName(name);
        this.setSession(null);
        this.setState(UserState.DEFAULT);
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
        if (session == null) {
            state = UserState.DEFAULT;
        } else {
            state = UserState.SESSION;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}