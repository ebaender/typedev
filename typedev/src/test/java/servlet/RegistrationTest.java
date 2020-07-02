package servlet;

import org.junit.Before;
import org.junit.Test;

import extra.Message;
import extra.TestUser;
import extra.UserStan;

public class RegistrationTest extends CommandTest {

    private TestUser testUser;

    @Before
    @Override
    public void setUp() throws Exception {
        testUser = TestUser.getInstance();
        super.setUp();
    }

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
        final String COMMAND_REGISTER_NO_PASSWORD = buildCommand(getValidName());
        assertCommand(getValidKey(), COMMAND_REGISTER_NO_PASSWORD, Message.ARGS_NOT_RECEIVED.toLine());
    }

    @Test
    public void registerNameTooShort() throws Exception {
        final String SHORT_NAME = new String(new char[UserStan.MIN_NAME_LENGTH - 1]).replace("\0", "a");
        final String COMMAND_SHORT_PASSWORD = buildCommand(SHORT_NAME, getValidPassword());
        assertCommand(getValidKey(), COMMAND_SHORT_PASSWORD,
                Message.NAME_TOO_SHORT.toLine(UserStan.MIN_NAME_LENGTH));
    }

    @Test
    public void registerIllegalName() throws Exception {
        final String ILLEGAL_NAME = "8!#$@}{|";
        final String COMMMAND_ILLEGAL_NAME = buildCommand(ILLEGAL_NAME, getValidPassword());
        assertCommand(getValidKey(), COMMMAND_ILLEGAL_NAME, Message.NAME_ILLEGAL_CHARS.toLine(ILLEGAL_NAME));
    }

    @Test
    public void registerPasswordTooShort() throws Exception {
        final String SHORT_PASSWORD = new String(new char[UserStan.MIN_PASSWORD_LENGTH - 1]).replace("\0", "*");
        final String COMMAND_SHORT_PASSWORD = buildCommand(getValidName(), SHORT_PASSWORD);
        assertCommand(getValidKey(), COMMAND_SHORT_PASSWORD,
                Message.PASSWORD_TOO_SHORT.toLine(UserStan.MIN_PASSWORD_LENGTH));
    }

    @Test
    public void registerDuplicate() throws Exception {
        final String COMMAND_REGISTER_DUPLICATE = buildCommand(testUser.getName(), testUser.getPassword());
        assertCommand(getValidKey(), COMMAND_REGISTER_DUPLICATE, Message.REGISTERED_DUPLICATE.toLine(testUser.getName()));
    }

    @Test
    public void registerValidUser() throws Exception {
        // TODO
    }
    
}