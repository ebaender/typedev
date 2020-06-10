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
            $.post(comServlet, {
                command: line,
                key: authKey
            }, function (resp) {
                textBuffer += prompt + line + '\n';
                line = "";
                resp = JSON.parse(resp);
                if (authKey === "" && typeof resp.key !== "undefined") {
                    // try to get a key if you dont have one yet
                    authKey = resp.key;
                } else if (resp.key === "") {
                    // drop the key when receiving logout signal
                    authKey = "";
                }
                textBuffer += resp.message;
                renderText();
            });
            break;
        default:
            break;
    }
    renderText();
}