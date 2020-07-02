package servlet;

import org.junit.Test;

import common.TestUser;
import manager.KeyMan;
import standard.UserStd;
import translator.Message;
import user.User;

public class RegistrationTest extends CommandTest {

    private User registrationUser = null;

    @Override
    public String getBaseCommand() {
        return "register";
    }

    @Test
    public void registerNoUserNameSpecified() throws Exception {
        assertCommand(getValidKey(), getBaseCommand(), Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void registerNoPasswordSpecified() throws Exception {
        final String COMMAND = buildCommand(getValidName());
        assertCommand(getValidKey(), COMMAND, Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void registerNameTooShort() throws Exception {
        final String SHORT_NAME = new String(new char[UserStd.MIN_NAME_LENGTH - 1]).replace("\0", "a");
        final String COMMAND = buildCommand(SHORT_NAME, getValidPassword());
        assertCommand(getValidKey(), COMMAND, Message.NAME_TOO_SHORT.toLine(UserStd.MIN_NAME_LENGTH));
    }

    @Test
    public void registerIllegalName() throws Exception {
        final String ILLEGAL_NAME = "8!#$@}{|";
        final String COMMAND = buildCommand(ILLEGAL_NAME, getValidPassword());
        assertCommand(getValidKey(), COMMAND, Message.NAME_ILLEGAL_CHARS.toLine(ILLEGAL_NAME));
    }

    @Test
    public void registerPasswordTooShort() throws Exception {
        final String SHORT_PASSWORD = new String(new char[UserStd.MIN_PASSWORD_LENGTH - 1]).replace("\0", "*");
        final String COMMAND = buildCommand(getValidName(), SHORT_PASSWORD);
        assertCommand(getValidKey(), COMMAND, Message.PASSWORD_TOO_SHORT.toLine(UserStd.MIN_PASSWORD_LENGTH));
    }

    @Test
    public void registerDuplicate() throws Exception {
        final TestUser USER = TestUser.getInstance(0);
        final String COMMAND = buildCommand(USER.getName(), USER.getPassword());
        assertCommand(getValidKey(), COMMAND, Message.REGISTERED_DUPLICATE.toLine(USER.getName()));
    }

    @Test
    public void registerValidUser() throws Exception {
        registrationUser = new User(KeyMan.getShortName(), KeyMan.getShortKey());
        final String COMMAND = buildCommand(registrationUser.getName(), registrationUser.getPassword());
        assertCommand(getValidKey(), COMMAND, Message.REGISTERED_SUCCESS.toLine(registrationUser.getName()));
    }

    @Override
    public void tearDown() throws Exception {
        if (registrationUser != null) {
            registrationUser.getDB().delete();
        }
        super.tearDown();
    }

}