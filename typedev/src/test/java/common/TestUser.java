package common;

import static matcher.JsonPropertyMatcher.hasJsonProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import command.user.LogIn;
import manager.KeyMan;
import standard.DBStd;
import standard.JsonStd;
import standard.UserStd;
import translator.Message;
import user.User;

public class TestUser extends User {

    private static List<TestUser> instancePool = new ArrayList<>();
    public static int INSTANCE_LIMIT = 4;
    public static int insertionIndexPointer = 0;
    private static ConcurrentHashMap<String, User> users = null;
    private String key;
    private User user;

    private TestUser() throws DBException, NoSuchAlgorithmException {
        super(KeyMan.getShortKey().replaceAll(UserStd.FORBIDDEN_CHAR_PATTERN, ""), KeyMan.getShortKey());
        setKey("");
        final JsonObject registerResp = getManager().register();
        if (registerResp != null) {
            final int code = registerResp.get(DBStd.CODE).getAsInt();
            if (code != DBStd.CODE_WRITE_SUCCESS && code != DBStd.CODE_REGISTERED_DUPLICATE) {
                throw new DBException(code);
            }
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User USER) {
        this.user = USER;
    }

    public static ConcurrentHashMap<String, User> getUsers() {
        return users;
    }

    public static void setUsers(final ConcurrentHashMap<String, User> USER) {
        TestUser.users = USER;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String KEY) {
        this.key = KEY;
    }

    public static TestUser getInstance(final int INSTANCE_INDEX) {
        TestUser instance = null;
        if (instancePool.size() < INSTANCE_LIMIT) {
            try {
                instance = new TestUser();
                System.out.println(Message.STATEMENT.toString(TestUser.class, "created", instance.getName()));
                instancePool.add(INSTANCE_INDEX, instance);
                if (instancePool.size() < INSTANCE_LIMIT) {
                    insertionIndexPointer++;
                }
            } catch (final NoSuchAlgorithmException e) {
                System.err.println(Message.CONCAT.toString(TestUser.class, Message.KEYGEN_FAILED.toString()));
            } catch (final DBException e) {
                System.err.println(
                        Message.CONCAT.toString(TestUser.class, Message.UNKNOWN_ERROR.toString(e.getErrorCode())));
            }
        } else {
            if (INSTANCE_INDEX < INSTANCE_LIMIT) {
                instance = instancePool.get(INSTANCE_INDEX);
            } else {
                System.err.println(
                        Message.CONCAT.toString(TestUser.class, Message.INDEX_OUT_OF_BOUNDS.toString(INSTANCE_INDEX)));
            }
        }
        return instance;
    }

    public static TestUser getInstance() {
        return getInstance(insertionIndexPointer);
    }

    public static final List<TestUser> getInstancePool() {
        return instancePool;
    }

    public JsonObject logIn() {
        final String[] ARGUMENTS = { "login", getName(), getPassword() };
        final JsonObject LOGIN_RESP = new LogIn(getKey(), ARGUMENTS, users).execute();
        final JsonElement RECEIVED_KEY = LOGIN_RESP.get(JsonStd.KEY);
        if (RECEIVED_KEY != null) {
            setKey(RECEIVED_KEY.getAsString());
            setUser(users.get(getKey()));
        }
        return LOGIN_RESP;
    }

    public static TestUser getAndAssertLoggedInInstance(final int INSTANCE_INDEX) {
        final TestUser USER = TestUser.getInstance(INSTANCE_INDEX);
        assertThat(USER.logIn(), hasJsonProperty(JsonStd.MSG, Message.LOGIN_SUCCESS.toLine(USER.getName())));
        return USER;
    }

    public static List<TestUser> getAndAssertLoggedInInstancePool(int poolSize) {
        if (poolSize > TestUser.INSTANCE_LIMIT) {
            poolSize = TestUser.INSTANCE_LIMIT;
        }
        final List<TestUser> userPool = new ArrayList<>();
        for (int i = 0; i < poolSize; i++) {
            userPool.add(TestUser.getAndAssertLoggedInInstance(i));
        }
        assertTrue(userPool.size() == poolSize);
        return userPool;
    }

    public static void reset() {
        for (final TestUser USER : instancePool) {
            USER.getManager().delete();
                System.out.println(Message.STATEMENT.toString(TestUser.class, "deleted", USER.getName()));
        }
        instancePool = new ArrayList<>();
        insertionIndexPointer = 0;
    }

}