function resetSessionValues() {
    codeArray = null;
    codeIndex = 0;
    mistakes = 0;
    progress = 0;
}

function renderMOTD() {
    textBuffer = logo + "\nWelcome! To list all commands, type \"help\".\n";
    renderText();
}

function renderText() {
    let renderBuffer;
    if (password_mode) {
        renderBuffer = textBuffer + getCensoredLine() + cursor;
    } else {
        renderBuffer = textBuffer + prompt + line + cursor;
    }
    $(output).text(renderBuffer);
}

function getCensoredLine() {
    let passwordLine = "";
    for (let i = 0; i < line.length; i++) {
        passwordLine += '*';
    }
    return passwordLine;
}

function fuseLine(anyLine) {
    textBuffer += anyLine + '\n';
    line = "";
}

function fuseStandardLine() {
    fuseLine(prompt + line);
}

function renderStatus() {
    $("#status").text(state + " " + authKey + " "
    + (codeArray === null ? null : codeArray.toString().replace(/,|\n/g, "").substring(0, 16)) + " " 
    + manual_leave + " " + progress + " " + mistakes);
}

function buildCode(buffer) {
    for (let i = codeIndex; i < codeArray.length; i++) {
        const char = codeArray[i];
        buffer += char;
    }
    return buffer;
}

function buildProgressBar() {
    let buffer = "";
    let barLength = 64;
    if (sessionProgress != null) {
        for (let i = 0; i < sessionProgress.length; i++) {
            const user = sessionProgress[i];
            let bar = "";
            let percentage = parseFloat(user.progress) / codeArray.length;
            let char = "#";
            for (let i = 0; i < barLength; i++) {
                if (i == parseInt(barLength * percentage)) {
                    char = " ";
                }
                bar += char;
            }
            buffer += user.name + " |" + bar + "| " + user.progress + " " + user.mistakes + "\n";
        }
    }
    return buffer;
}

function renderLiveSession(output) {
    var buffer = buildProgressBar("") + "\n";
    buffer = buildCode(buffer);
    $(output).html(PR.prettyPrintOne(buffer));
}

function defaultKeyHandler(e) {
    switch (e.code) {
        case "Delete":
        case "Backspace":
            line = line.substring(0, line.length - 1);
            break;
        case "Space":
            line += e.key;
            break;
        case "Enter":
            if (password_mode) {
                if (password_entered) {
                    password_mode = false;
                    if (line === password || password == null) {
                        var registration = command + " " + name + " " + line;
                        fuseLine(getCensoredLine());
                        $.post(comServlet, { command: registration, key: authKey }, function (resp) {
                            resp = JSON.parse(resp);
                            if (commands.login.includes(command)) {
                                updateAuthKey(resp);
                            }
                            textBuffer += resp.message;
                            renderText();
                        });
                    } else {
                        fuseLine(getCensoredLine() + "\nPassword did not match confirmation, try again.");
                    }
                } else {
                    if (line.length === 0) {
                        fuseLine("\nPassword can not be empty, try again.");
                        password_mode = false;
                    } else {
                        password = line;
                        password_entered = true;
                        fuseLine(getCensoredLine());
                    }
                }
            } else {
                var argv = line.split(' ');
                let feedback = "";
                switch (argv[0]) {
                    case "cl":
                    case "clear":
                        textBuffer = logo + '\n';
                        line = "";
                        break;
                    case "lt":
                    case "themes":
                        listThemes();
                        break;
                    case "th":
                    case "theme":
                        $('#theme').each(function () {
                            if (themes.includes(argv[1])) {
                                this.href = "theme/" + argv[1] + ".css";
                                feedback = "Set theme to " + argv[1] + ".";
                            } else {
                                feedback = "\"" + argv[1] + "\" is not a valid theme.";
                            }
                        });
                        textBuffer += prompt + line + '\n' + feedback + '\n';
                        line = "";
                        break;
                    case "li":
                    case "login":
                        passwordMode(argv, false);
                        break;
                    case "rg":
                    case "register":
                        passwordMode(argv, true);
                        break;
                    default:
                        let lineCopy = line;
                        fuseLine(prompt + line);
                        renderText();
                        $.post(comServlet, { command: lineCopy, key: authKey }, function (resp) {
                            resp = JSON.parse(resp);
                            textBuffer += resp.message;
                            renderText();
                        });
                        break;
                }
                break;
            }
        default:
            break;
    }
    renderText();
}

function listThemes() {
    fuseStandardLine();
    themes.forEach(theme => {
        textBuffer += theme + " ";
    });
    textBuffer += "\n";
}

function updateAuthKey(resp) {
    if (authKey === "" && typeof resp.key !== "undefined") {
        // try to get a key if you dont have one yet
        authKey = resp.key;
    } else {
        // alert("refused key");
    }
}

function passwordMode(argv, confirmation) {
    if (argv.length === 2 && argv[1].length > 0) {
        command = argv[0];
        name = argv[1];
        password_mode = true;
        password_entered = !confirmation;
        if (confirmation) {
            password = "";
            feedback = "Enter and confirm password:";
        } else {
            password = null;
            feedback = "Enter password:";
        }
    } else {
        feedback = "Usage: " + command + " [NAME]";
    }
    textBuffer += prompt + line + '\n' + feedback + '\n';
    line = "";
}

function sessionKeyHandler(e) {
    var typedKey = e.key;
    var actualKey = codeArray[codeIndex];
    if (ctrl && typedKey == 'c') {
        manual_leave = "MANUAL_LEAVE";
        resetSessionValues();
        $.post(comServlet, { command: "leave", key: authKey }, function (resp) {
            updateState();
            resp = JSON.parse(resp);
            textBuffer += resp.message;
            renderText();
        });
        return;
    }
    if (ezMode) {
        typedKey = typedKey.toLowerCase();
        actualKey = actualKey.toLowerCase();
    }
    if (typedKey === actualKey) {
        if (typedKey !== ' ') {
            progress++;
        }
        codeIndex++;
    } else if (typedKey.length === 1) {
        mistakes++;
    }
    if (e.key === ' ' || codeArray[codeIndex] === '\n' || codeArray[codeIndex] === '\t') {
        while (codeArray[codeIndex] === ' ' || codeArray[codeIndex] === '\n' || codeArray[codeIndex] === '\t') {
            codeIndex++;
        }
    }
    renderLiveSession(output);
}

function buildResult(result) {
    var resultBuffer = "";
    for (let place in result) {
        const entry = result[place];
        let prefix = place + ". " + entry.name + " | ";
        if (Object.keys(result).length === 1) {
            prefix = "";
        }
        resultBuffer += prefix + entry.progress + " characters " + entry.mistakes + " mistakes " + entry.cpm + " cpm\n";
    }
    return resultBuffer;
}

// get code if you are in a session lobby and don't have any code yet.
function getCode() {
    if (codeArray === null) {
        $.post(codServlet, { key: authKey }, function (resp) {
            resp = JSON.parse(resp);
            codeArray = resp.code.split("");
        });
    }
}

function changeState(nextState) {
    var prevState = state;
    state = nextState;
    switch (state) {
        case states.default:
            // TODO: make reset work here
            if (prevState === states.session) {
                resetSessionValues();
            }
            renderText();
            break;
        case states.session:
            resetSessionValues();
            getCode();
            break;
        case states.live_session:
            // prevent hyperactive users from screwing themselves
            if (prevState === states.default) {
                getCode();
            }
            break;
        case states.finished:
            resetSessionValues();
            $.post(resServlet, { key: authKey }, function (resp) {
                resp = JSON.parse(resp);
                if (!jQuery.isEmptyObject(resp)) {
                    textBuffer += "Finished session.\n";
                    textBuffer += buildResult(resp.result);
                } else {
                    alert("finishing session failed.");
                    changeState(states.default);
                }
            });
            break;
        default:
            // prevent undefined states.
            state = prevState;
            break;
    }
}

function keepAlive() {
    if (authKey !== "") {
        updateState();
    }
}

function updateState() {
    $.post(conServlet, { key: authKey }, function (resp) {
        resp = JSON.parse(resp);
        if (!jQuery.isEmptyObject(resp)) {
            // update to state from server.
            if (state !== resp.state) {
                changeState(resp.state);
            }
        } else {
            authKey = "";
            // alert("connection failed.");
            changeState(states.default);
            textBuffer += "You have been disconnected.\n";
        }
    });
}

function syncProgress() {
    if (state === states.live_session) {
        $.post(synServlet, { key: authKey, progress: progress, mistakes: mistakes }, function (resp) {
            resp = JSON.parse(resp);
            if (!jQuery.isEmptyObject(resp)) {
                sessionProgress = resp.sessionProgress;
                renderLiveSession(output);
            } else {
                alert("sync failed.");
                changeState(states.default);
            }
        });
    }
}