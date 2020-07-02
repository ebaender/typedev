package common;

public class DBException extends Exception {

    private static final long serialVersionUID = 1L;

    private final int ERROR_CODE;

    public DBException(final int ERROR_CODE) {
        this.ERROR_CODE = ERROR_CODE;
    }

    public int getErrorCode() {
        return ERROR_CODE;
    }

}