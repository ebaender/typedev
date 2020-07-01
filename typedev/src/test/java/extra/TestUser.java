package extra;

import com.google.gson.JsonObject;

import user.User;

public class TestUser extends User {

    private static TestUser instance = null;

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

    public static TestUser getInstance(final String NAME, final String PASSWORD) throws DBException {
        if (instance == null) {
            instance = new TestUser(NAME, PASSWORD);
        }
        return instance;
    }

}