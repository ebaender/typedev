package extra;

import com.google.gson.JsonObject;

import user.User;

public class TestUser extends User {

    private static TestUser instance = null;
    private static final String NAME = "junit3";
    private static final String PASSWORD = "@Test";

    private TestUser(final String NAME, final String PASSWORD) throws DBException {
        super(NAME, PASSWORD);
        JsonObject registerResp = getManager().register();
        if (registerResp != null) {
            int code = registerResp.get(DBStandard.CODE).getAsInt();
            if (code != DBStandard.CODE_WRITE_SUCCESS && code != DBStandard.CODE_REGISTERED_DUPLICATE) {
                throw new DBException(code);
            }
        }
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

}