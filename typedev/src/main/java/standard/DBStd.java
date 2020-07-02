package standard;

public class DBStd {

        public static final String NAME = "username", PASSWORD = "password", REQUEST = "request",
                        REQUEST_REGISTER = "register", REQUEST_AUTHENTICATE = "authentificate",
                        REQUEST_DELETE = "delete", REQUEST_UPDATE = "update", REQUEST_SPY = "request",
                        USER_TARGET = "requested_user", UPDATE_GAMES_WON = "games_won",
                        UPDATE_GAMES_PLAYED = "games_played", UPDATE_SPEED = "wpm", CODE = "statuscode",
                        LEADERBORD_SPEED = "wpm_score", LEADERBORD_WINS = "games_won_score",
                        BOARD_PROPERTY = "highscore", UPDATE_LANGUAGE = "language",
                        REQUEST_LANGUAGE = "languages_score", LANGUAGE_NAME_PROPERTY = "lang",
                        LANGUAGE_PLAYED_PROPERTY = "played", NULL_LANGUAGE = "null", CATEGORY_VICTORIES = "victories",
                        CATEGORY_TOPSPEED = "topspeed";

        public static final int CODE_SUCCESS = 200, CODE_WRITE_SUCCESS = 201, CODE_AUTHENTICATE_SUCCESS = 202,
                        CODE_REGISTERED_DUPLICATE = 406, CODE_WRONGPASSWORD = 401, CODE_USER_NOT_FOUND = 404,
                        CODE_REQUESTED_USER_NOT_FOUND = 500, CODE_REMOVED_USER = 410;

}