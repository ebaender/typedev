package extra;

public class DatabaseStandard {

    public static final String NAME = "username", PASSWORD = "password", REQUEST = "request",
            REQUEST_REGISTER = "register", REQUEST_AUTHENTICATE = "authentificate", CODE = "statuscode";

    public static final int CODE_REGISTER_SUCCESS = 201, CODE_AUTHENTICATE_SUCCESS = 202,
            CODE_REGISTERED_DUPLICATE = 406, CODE_WRONGPASSWORD = 401, CODE_NOTFOUND = 404;

}