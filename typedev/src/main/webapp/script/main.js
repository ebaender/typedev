// servlets.
var comServlet = "command";
var conServlet = "connection";
var codServlet = "code";
var synServlet = "sync";
var resServlet = "result";

// general.
var output = "#terminal";
var states = { default: "default", session: "session", live_session: "live_session", finished: "finished" };
var state = states.default;
var commands = { login: ["login", "li"], register: ["register", "rg"], clear: ["clear", "cl"], theme: ["theme", "th"] };
var command = null;
var authKey = "";
var ctrl = false;

// default state.
var line = "";
var renderLine = "";
var textBuffer = "";
var cursor = '\u2588';
var prompt = "> ";
var keyPressExclude = ["Delete", "Enter"];
var logo = "                        _________\n ____  ____  ____  ____ ______  /_____ ___   __\n||t ||||y ||||p ||||e ||_  __  / _  _ \\__ | / /\n||__||||__||||__||||__||/ /_/ /  /  __/__ |/ /\n|/__\\||/__\\||/__\\||/__\\|\\__,_/   \\___/ _____/\n"

// live session state.
var ezMode = true;
var sessionProgress = null;
var codeArray = null;
var codeIndex = 0;
var mistakes = 0;
var progress = 0;

// theme.
var themes = ["monokai", "bluloco", "desert", "solarized"];

// authentication.
var password_mode = false;
var password_entered = false;
var password = "";
var name = "";

// debug.
var manual_leave = "no_manual_leave";

$.when(
    $.getScript("script/functions.js"),
    $.Deferred(function (deferred) {
        $(deferred.resolve);
    })
).done(function () {

    $(document).ready(function () {

        $(document).keypress(function (e) {
            if (state !== states.live_session) {
                for (let i = 0; i < keyPressExclude.length; i++) {
                    if (e.code === keyPressExclude[i]) {
                        return;
                    }
                }
                line += e.key;
                renderText();
            }
        });

        $(document).keydown(function (e) {
            if (state === states.live_session) {
                sessionKeyHandler(e);
            } else {
                defaultKeyHandler(e);
            }
            if (e.keyCode == 32) {
                return false;
            } else if (e.keyCode == 17) {
                ctrl = true;
            }
        });

        $(document).keyup(function (e) {
            if (e.keyCode == 17) {
                ctrl = false;
            }
        });

        setInterval(() => {
            keepAlive();
            syncProgress();
            renderStatus();
        }, 1000);

        renderMOTD();
        renderText();

    });

});

