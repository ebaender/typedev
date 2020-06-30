function calculateCodeLength() {
    if (codeArray != null) {
        codeLength = 0;
        for (let i = 0; i < codeArray.length; i++) {
            const character = codeArray[i];
            if (!(character === ' ' || character === '\n' || character == '\t')) {
                codeLength++;
            }
        }
    }
}