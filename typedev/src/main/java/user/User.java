package user;

import java.time.Instant;

import manager.DBMan;
import session.Session;

public class User {

    private String name;
    private String password;
    private Session session = null;
    private UserState state = UserState.DEFAULT;
    private Integer progress = 0;
    private Integer mistakes = 0;
    private Instant lastContact;
    private DBMan manager;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        lastContact = Instant.now();
        manager = new DBMan(this);
    }

    public DBMan getManager() {
        return manager;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getLastContact() {
        return lastContact;
    }

    public void setLastContact(Instant lastContact) {
        this.lastContact = lastContact;
    }

    public void setMistakes(Integer mistakes) {
        this.mistakes = mistakes;
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

	public Integer getMistakes() {
		return mistakes;
    }

}