package extra;

public class DBStandard {

        public static final String NAME = "username", PASSWORD = "password", REQUEST = "request",
                        REQUEST_REGISTER = "register", REQUEST_AUTHENTICATE = "authentificate",
                        REQUEST_UPDATE = "update", REQUEST_SPY = "request", SPY_TARGET = "requested_user",
                        UPDATE_GAMES_WON = "games_won", UPDATE_GAMES_PLAYED = "games_played", UPDATE_SPEED = "wpm",
                        CODE = "statuscode", LEADERBORD_SPEED = "wpm_score", LEADERBORD_WINS = "games_won_score",
                        BOARD_PROPERTY = "highscore", UPDATE_LANGUAGE = "language",
                        REQUEST_LANGUAGE = "languages_score", LANGUAGE_NAME_PROPERTY = "lang",
                        LANGUAGE_PLAYED_PROPERTY = "played";

        public static final int CODE_SUCCESS = 200, CODE_WRITE_SUCCESS = 201, CODE_AUTHENTICATE_SUCCESS = 202,
                        CODE_REGISTERED_DUPLICATE = 406, CODE_WRONGPASSWORD = 401, CODE_NOTFOUND = 404,
                        CODE_SPY_NOTFOUND = 500;

}