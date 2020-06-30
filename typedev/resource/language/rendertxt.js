function renderText() {
    let renderBuffer;
    if (password_mode) {
        renderBuffer = textBuffer + getCensoredLine() + cursor;
    } else {
        renderBuffer = textBuffer + prompt + line + cursor;
    }
    $(output).text(renderBuffer);
    scrollDown();
}