package extra;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import command.user.LogIn;
import user.User;

public class TestUser extends User {

    private static TestUser instance = null;
    private static final String NAME = "junit3";
    private static final String PASSWORD = "@Test";
    private String key;

    private TestUser(final String NAME, final String PASSWORD) throws DBException {
        super(NAME, PASSWORD);
        setKey("");
        JsonObject registerResp = getManager().register();
        if (registerResp != null) {
            int code = registerResp.get(DBStandard.CODE).getAsInt();
            if (code != DBStandard.CODE_WRITE_SUCCESS && code != DBStandard.CODE_REGISTERED_DUPLICATE) {
                throw new DBException(code);
            }
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private static TestUser getInternalInstance(final String NAME, final String PASSWORD) throws DBException {
        if (instance == null) {
            instance = new TestUser(NAME, PASSWORD);
        }
        return instance;
    }

    public static TestUser getInstance() {
        try {
            return TestUser.getInternalInstance(NAME, PASSWORD);
        } catch (DBException e) {
            System.out.println(TestUser.class + "Can not guarantee existence of test user. "
                    + Message.UNKNOWN_ERROR.toString(e.getErrorCode()));
            return null;
        }
    }

    public JsonObject logIn(final ConcurrentHashMap<String, User> USERS) {
        final String[] ARGUMENTS = {"login", getName(), getPassword()};
        JsonObject loginResp = new LogIn(getKey(), ARGUMENTS, USERS).execute();
        JsonElement receivedKey = loginResp.get(Standard.KEY);
        if (receivedKey != null) {
            setKey(receivedKey.getAsString());
        }
        return loginResp;
    }

}