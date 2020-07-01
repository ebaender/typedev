package extra;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import command.user.LogIn;
import user.User;

public class TestUser extends User {

    private static List<TestUser> instancePool = new ArrayList<>();
    public static int INSTANCE_LIMIT = 4;
    public static int InsertionIndexPointer = 0;
    private String key;

    private TestUser() throws DBException, NoSuchAlgorithmException {
        super(KeyMan.getShortKey().replaceAll(UserStandard.FORBIDDEN_CHAR_PATTERN, ""), KeyMan.getShortKey());
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

    public static TestUser getInstance(int instanceIndex) {
        TestUser instance = null;
        if (instancePool.size() < INSTANCE_LIMIT) {
            try {
                instance = new TestUser();
                System.out.println(Message.STATEMENT.toString(TestUser.class, "created", instance.getName()));
                instancePool.add(instanceIndex, instance);
                if (instancePool.size() < INSTANCE_LIMIT) {
                    InsertionIndexPointer++;
                }
            } catch (NoSuchAlgorithmException e) {
                System.err.println(Message.CONCAT.toString(TestUser.class, Message.KEYGEN_FAILED.toString()));
            } catch (DBException e) {
                System.err.println(
                        Message.CONCAT.toString(TestUser.class, Message.UNKNOWN_ERROR.toString(e.getErrorCode())));
            }
        } else {
            if (instanceIndex < INSTANCE_LIMIT) {
                instance = instancePool.get(instanceIndex);
            } else {
                System.err.println(
                        Message.CONCAT.toString(TestUser.class, Message.INDEX_OUT_OF_BOUNDS.toString(instanceIndex)));
            }
        }
        return instance;
    }

    public static TestUser getInstance() {
        return getInstance(InsertionIndexPointer);
    }

    public static void instantiateAll() {
        for (int i = 0; i < INSTANCE_LIMIT; i++) {
            getInstance();
        }
    }

    public static final List<TestUser> getInstancePool() {
        return instancePool;
    }

    public JsonObject logIn(final ConcurrentHashMap<String, User> USERS) {
        final String[] ARGUMENTS = { "login", getName(), getPassword() };
        JsonObject loginResp = new LogIn(getKey(), ARGUMENTS, USERS).execute();
        JsonElement receivedKey = loginResp.get(Standard.KEY);
        if (receivedKey != null) {
            setKey(receivedKey.getAsString());
        }
        return loginResp;
    }

}