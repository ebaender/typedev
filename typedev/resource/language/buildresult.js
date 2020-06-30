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