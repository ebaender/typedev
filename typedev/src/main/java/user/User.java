package user;

import java.time.Instant;

import session.Session;

public class User {

    private String name;
    private Session session = null;
    private UserState state = UserState.DEFAULT;
    private Integer progress = 0;
    private Integer mistakes = 0;
    private Instant lastContact;

    public User(String name) {
        this.name = name;
        lastContact = Instant.now();
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