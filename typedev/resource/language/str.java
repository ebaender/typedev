public String(int[] codePoints, int offset, int count) {
    if (offset < 0) {
        throw new StringIndexOutOfBoundsException(offset);
    }
    if (count <= 0) {
        if (count < 0) {
            throw new StringIndexOutOfBoundsException(count);
        }
        if (offset <= codePoints.length) {
            this.value = "".value;
            return;
        }
    }
    // Note: offset or count might be near -1>>>1.
    if (offset > codePoints.length - count) {
        throw new StringIndexOutOfBoundsException(offset + count);
    }
    
    final int end = offset + count;
    
    // Pass 1: Compute precise size of char[]
    int n = count;
    for (int i = offset; i < end; i++) {
        int c = codePoints[i];
        if (Character.isBmpCodePoint(c))
        continue;
        else if (Character.isValidCodePoint(c))
        n++;
        else throw new IllegalArgumentException(Integer.toString(c));
    }
    
    // Pass 2: Allocate and fill in char[]
    final char[] v = new char[n];
    
    for (int i = offset, j = 0; i < end; i++, j++) {
        int c = codePoints[i];
        if (Character.isBmpCodePoint(c))
        v[j] = (char)c;
        else
        Character.toSurrogates(c, v, j++);
    }
    
    this.value = v;
}