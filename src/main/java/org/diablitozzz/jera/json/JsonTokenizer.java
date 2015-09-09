package org.diablitozzz.jera.json;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class JsonTokenizer implements Closeable {

    final private static class JsonScope {

        static final int EMPTY_ARRAY = 1;
        static final int NONEMPTY_ARRAY = 2;
        static final int EMPTY_OBJECT = 3;
        static final int DANGLING_NAME = 4;
        static final int NONEMPTY_OBJECT = 5;
        static final int EMPTY_DOCUMENT = 6;
        static final int NONEMPTY_DOCUMENT = 7;
        static final int CLOSED = 8;
    }

    final private static class StringPool {

        private final String[] pool = new String[512];

        public StringPool() {
        }

        public String get(final char[] array, final int start, final int length) {
            int hashCode = 0;
            for (int i = start; i < (start + length); i++) {
                hashCode = (hashCode * 31) + array[i];
            }

            hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
            hashCode ^= (hashCode >>> 7) ^ (hashCode >>> 4);
            final int index = hashCode & (this.pool.length - 1);

            final String pooled = this.pool[index];
            if ((pooled == null) || (pooled.length() != length)) {
                final String result = new String(array, start, length);
                this.pool[index] = result;
                return result;
            }

            for (int i = 0; i < length; i++) {
                if (pooled.charAt(i) != array[start + i]) {
                    final String result = new String(array, start, length);
                    this.pool[index] = result;
                    return result;
                }
            }

            return pooled;
        }
    }

    public static enum Token {

        BEGIN_ARRAY, END_ARRAY, BEGIN_OBJECT, END_OBJECT, NAME, STRING, NUMBER, BOOLEAN, NULL, END_DOCUMENT
    }

    private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private final StringPool stringPool = new StringPool();
    private final Reader in;

    /** True to accept non-spec compliant JSON */
    private boolean lenient = false;

    /**
     * Use a manual buffer to easily read and unread upcoming characters, and
     * also so we can create strings without an intermediate StringBuilder.
     * We decode literals directly out of this buffer, so it must be at least as
     * long as the longest token that can be reported as a number.
     */
    private final char[] buffer = new char[1024];
    private int pos = 0;
    private int limit = 0;

    /*
     * The offset of the first character in the buffer.
     */
    private int bufferStartLine = 1;
    private int bufferStartColumn = 1;

    /*
     * The nesting stack. Using a manual array rather than an ArrayList saves 20%.
     */
    private int[] stack = new int[32];
    private int stackSize = 0;

    {
        this.push(JsonScope.EMPTY_DOCUMENT);
    }

    private Token token;
    private String name;
    private String value;
    private int valuePos;
    private int valueLength;
    private boolean skipping = false;

    public JsonTokenizer(final Reader in) {
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.in = in;
    }

    private Token advance() throws JsonException, IOException {
        this.peek();

        final Token result = this.token;
        this.token = null;
        this.value = null;
        this.name = null;
        return result;
    }

    public void beginArray() throws JsonException, IOException {
        this.expect(Token.BEGIN_ARRAY);
    }

    public void beginObject() throws JsonException, IOException {
        this.expect(Token.BEGIN_OBJECT);
    }

    private void checkLenient() throws JsonException {
        if (!this.lenient) {
            throw new JsonException("Use JsonReader.setLenient(true) to accept malformed JSON" + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }
    }

    @Override
    public void close() throws IOException {
        this.value = null;
        this.token = null;
        this.stack[0] = JsonScope.CLOSED;
        this.stackSize = 1;
        this.in.close();
    }

    private void consumeNonExecutePrefix() throws JsonException, IOException {
        this.nextNonWhitespace(true);
        this.pos--;

        if (((this.pos + JsonTokenizer.NON_EXECUTE_PREFIX.length) > this.limit) && !this.fillBuffer(JsonTokenizer.NON_EXECUTE_PREFIX.length)) {
            return;
        }

        for (int i = 0; i < JsonTokenizer.NON_EXECUTE_PREFIX.length; i++) {
            if (this.buffer[this.pos + i] != JsonTokenizer.NON_EXECUTE_PREFIX[i]) {
                return; // not a security token!
            }
        }

        // we consumed a security token!
        this.pos += JsonTokenizer.NON_EXECUTE_PREFIX.length;
    }

    private Token decodeLiteral() {
        if (this.valuePos == -1) {
            // it was too long to fit in the buffer so it can only be a string
            return Token.STRING;
        } else if ((this.valueLength == 4)
                && (('n' == this.buffer[this.valuePos]) || ('N' == this.buffer[this.valuePos]))
                && (('u' == this.buffer[this.valuePos + 1]) || ('U' == this.buffer[this.valuePos + 1]))
                && (('l' == this.buffer[this.valuePos + 2]) || ('L' == this.buffer[this.valuePos + 2]))
                && (('l' == this.buffer[this.valuePos + 3]) || ('L' == this.buffer[this.valuePos + 3]))) {
            this.value = "null";
            return Token.NULL;
        } else if ((this.valueLength == 4)
                && (('t' == this.buffer[this.valuePos]) || ('T' == this.buffer[this.valuePos]))
                && (('r' == this.buffer[this.valuePos + 1]) || ('R' == this.buffer[this.valuePos + 1]))
                && (('u' == this.buffer[this.valuePos + 2]) || ('U' == this.buffer[this.valuePos + 2]))
                && (('e' == this.buffer[this.valuePos + 3]) || ('E' == this.buffer[this.valuePos + 3]))) {
            this.value = JsonTokenizer.TRUE;
            return Token.BOOLEAN;
        } else if ((this.valueLength == 5)
                && (('f' == this.buffer[this.valuePos]) || ('F' == this.buffer[this.valuePos]))
                && (('a' == this.buffer[this.valuePos + 1]) || ('A' == this.buffer[this.valuePos + 1]))
                && (('l' == this.buffer[this.valuePos + 2]) || ('L' == this.buffer[this.valuePos + 2]))
                && (('s' == this.buffer[this.valuePos + 3]) || ('S' == this.buffer[this.valuePos + 3]))
                && (('e' == this.buffer[this.valuePos + 4]) || ('E' == this.buffer[this.valuePos + 4]))) {
            this.value = JsonTokenizer.FALSE;
            return Token.BOOLEAN;
        } else {
            this.value = this.stringPool.get(this.buffer, this.valuePos, this.valueLength);
            return this.decodeNumber(this.buffer, this.valuePos, this.valueLength);
        }
    }

    private Token decodeNumber(final char[] chars, final int offset, final int length) {
        int i = offset;
        int c = chars[i];

        if (c == '-') {
            c = chars[++i];
        }

        if (c == '0') {
            c = chars[++i];
        } else if ((c >= '1') && (c <= '9')) {
            c = chars[++i];
            while ((c >= '0') && (c <= '9')) {
                c = chars[++i];
            }
        } else {
            return Token.STRING;
        }

        if (c == '.') {
            c = chars[++i];
            while ((c >= '0') && (c <= '9')) {
                c = chars[++i];
            }
        }

        if ((c == 'e') || (c == 'E')) {
            c = chars[++i];
            if ((c == '+') || (c == '-')) {
                c = chars[++i];
            }
            if ((c >= '0') && (c <= '9')) {
                c = chars[++i];
                while ((c >= '0') && (c <= '9')) {
                    c = chars[++i];
                }
            } else {
                return Token.STRING;
            }
        }

        if (i == (offset + length)) {
            return Token.NUMBER;
        }
        return Token.STRING;
    }

    public void endArray() throws JsonException, IOException {
        this.expect(Token.END_ARRAY);
    }

    public void endObject() throws JsonException, IOException {
        this.expect(Token.END_OBJECT);
    }

    private void expect(final Token expected) throws JsonException, IOException {
        this.peek();
        if (this.token != expected) {
            throw new IllegalStateException("Expected " + expected + " but was " + this.peek() + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }
        this.advance();
    }

    private boolean fillBuffer(final int minimum) throws IOException {
        final char[] buffer = this.buffer;

        // Before clobbering the old characters, update where buffer starts
        // Using locals here saves ~2%.
        int line = this.bufferStartLine;
        int column = this.bufferStartColumn;
        for (int i = 0, p = this.pos; i < p; i++) {
            if (buffer[i] == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        this.bufferStartLine = line;
        this.bufferStartColumn = column;

        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(buffer, this.pos, buffer, 0, this.limit);
        } else {
            this.limit = 0;
        }

        this.pos = 0;
        int total;
        while ((total = this.in.read(buffer, this.limit, buffer.length - this.limit)) != -1) {
            this.limit += total;

            // if this is the first read, consume an optional byte order mark (BOM) if it exists
            if ((this.bufferStartLine == 1) && (this.bufferStartColumn == 1) && (this.limit > 0) && (buffer[0] == '\ufeff')) {
                this.pos++;
                this.bufferStartColumn--;
            }

            if (this.limit >= minimum) {
                return true;
            }
        }
        return false;
    }

    private int getColumnNumber() {
        int result = this.bufferStartColumn;
        for (int i = 0; i < this.pos; i++) {
            if (this.buffer[i] == '\n') {
                result = 1;
            } else {
                result++;
            }
        }
        return result;
    }

    private int getLineNumber() {
        int result = this.bufferStartLine;
        for (int i = 0; i < this.pos; i++) {
            if (this.buffer[i] == '\n') {
                result++;
            }
        }
        return result;
    }

    public boolean hasNext() throws JsonException, IOException {
        this.peek();
        return (this.token != Token.END_OBJECT) && (this.token != Token.END_ARRAY);
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public boolean nextBoolean() throws JsonException, IOException {
        this.peek();
        if (this.token != Token.BOOLEAN) {
            throw new IllegalStateException("Expected a boolean but was " + this.token + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }

        final boolean result = (this.value == JsonTokenizer.TRUE);
        this.advance();
        return result;
    }

    public double nextDouble() throws JsonException, IOException {
        this.peek();
        if ((this.token != Token.STRING) && (this.token != Token.NUMBER)) {
            throw new IllegalStateException("Expected a double but was " + this.token + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }

        final double result = Double.parseDouble(this.value);

        if (((result >= 1.0d) && this.value.startsWith("0"))) {
            throw new JsonException("JSON forbids octal prefixes: " + this.value + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }
        if (!this.lenient && (Double.isNaN(result) || Double.isInfinite(result))) {
            throw new JsonException("JSON forbids NaN and infinities: " + this.value + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }

        this.advance();
        return result;
    }

    @SuppressWarnings("fallthrough")
    private Token nextInArray(final boolean firstElement) throws JsonException, IOException {
        if (firstElement) {
            this.stack[this.stackSize - 1] = JsonScope.NONEMPTY_ARRAY;
        } else {
            /* Look for a comma before each element after the first element. */
            switch (this.nextNonWhitespace(true)) {
                case ']':
                    this.stackSize--;
                    return this.token = Token.END_ARRAY;
                case ';':
                    this.checkLenient(); // fall-through
                case ',':
                    break;
                default:
                    throw new JsonException("Unterminated array" + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
        }

        switch (this.nextNonWhitespace(true)) {
            case ']':
                if (firstElement) {
                    this.stackSize--;
                    return this.token = Token.END_ARRAY;
                }
                // fall-through to handle ",]"
            case ';':
            case ',':
                /* In lenient mode, a 0-length literal means 'null' */
                this.checkLenient();
                this.pos--;
                this.value = "null";
                return this.token = Token.NULL;
            default:
                this.pos--;
                return this.nextValue();
        }
    }

    @SuppressWarnings("fallthrough")
    private Token nextInObject(final boolean firstElement) throws JsonException, IOException {
        /*
         * Read delimiters. Either a comma/semicolon separating this and the
         * previous name-value pair, or a close brace to denote the end of the
         * object.
         */
        if (firstElement) {
            /* Peek to see if this is the empty object. */
            switch (this.nextNonWhitespace(true)) {
                case '}':
                    this.stackSize--;
                    return this.token = Token.END_OBJECT;
                default:
                    this.pos--;
            }
        } else {
            switch (this.nextNonWhitespace(true)) {
                case '}':
                    this.stackSize--;
                    return this.token = Token.END_OBJECT;
                case ';':
                case ',':
                    break;
                default:
                    throw new JsonException("Unterminated object" + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
        }

        /* Read the name. */
        final int quote = this.nextNonWhitespace(true);
        switch (quote) {
            case '\'':
                this.checkLenient(); // fall-through
            case '"':
                this.name = this.nextString((char) quote);
                break;
            default:
                this.checkLenient();
                this.pos--;
                this.name = this.nextLiteral(false);
                if (this.name.length() == 0) {
                    throw new JsonException("Expected name" + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
                }
        }

        this.stack[this.stackSize - 1] = JsonScope.DANGLING_NAME;
        return this.token = Token.NAME;
    }

    public int nextInt() throws JsonException, IOException {
        this.peek();
        if ((this.token != Token.STRING) && (this.token != Token.NUMBER)) {
            throw new IllegalStateException("Expected an int but was " + this.token + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }

        int result;
        try {
            result = Integer.parseInt(this.value);
        } catch (final NumberFormatException ignored) {
            final double asDouble = Double.parseDouble(this.value); // don't catch this NumberFormatException
            result = (int) asDouble;
            if (result != asDouble) {
                throw new NumberFormatException("Expected an int but was " + this.value + " at line " + this.getLineNumber() + " column "
                        + this.getColumnNumber());
            }
        }

        if ((result >= 1L) && this.value.startsWith("0")) {
            throw new JsonException("JSON forbids octal prefixes: " + this.value + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }

        this.advance();
        return result;
    }

    @SuppressWarnings("fallthrough")
    private String nextLiteral(final boolean assignOffsetsOnly) throws JsonException, IOException {
        StringBuilder builder = null;
        this.valuePos = -1;
        this.valueLength = 0;
        int i = 0;

        findNonLiteralCharacter: while (true) {
            for (; (this.pos + i) < this.limit; i++) {
                switch (this.buffer[this.pos + i]) {
                    case '/':
                    case '\\':
                    case ';':
                    case '#':
                    case '=':
                        this.checkLenient(); // fall-through
                    case '{':
                    case '}':
                    case '[':
                    case ']':
                    case ':':
                    case ',':
                    case ' ':
                    case '\t':
                    case '\f':
                    case '\r':
                    case '\n':
                        break findNonLiteralCharacter;
                    default:
                        break;
                }
            }

            /*
             * Attempt to load the entire literal into the buffer at once. If
             * we run out of input, add a non-literal character at the end so
             * that decoding doesn't need to do bounds checks.
             */
            if (i < this.buffer.length) {
                if (this.fillBuffer(i + 1)) {
                    continue;
                }
                this.buffer[this.limit] = '\0';
                break;
            }

            // use a StringBuilder when the value is too long. It must be an unquoted string.
            if (builder == null) {
                builder = new StringBuilder();
            }
            builder.append(this.buffer, this.pos, i);
            this.valueLength += i;
            this.pos += i;
            i = 0;
            if (!this.fillBuffer(1)) {
                break;
            }
        }

        String result;
        if (assignOffsetsOnly && (builder == null)) {
            this.valuePos = this.pos;
            result = null;
        } else if (this.skipping) {
            result = "skipped!";
        } else if (builder == null) {
            result = this.stringPool.get(this.buffer, this.pos, i);
        } else {
            builder.append(this.buffer, this.pos, i);
            result = builder.toString();
        }
        this.valueLength += i;
        this.pos += i;
        return result;
    }

    public long nextLong() throws JsonException, IOException {
        this.peek();
        if ((this.token != Token.STRING) && (this.token != Token.NUMBER)) {
            throw new IllegalStateException("Expected a long but was " + this.token + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }

        long result;
        try {
            result = Long.parseLong(this.value);
        } catch (final NumberFormatException ignored) {
            final double asDouble = Double.parseDouble(this.value); // don't catch this NumberFormatException
            result = (long) asDouble;
            if (result != asDouble) {
                throw new NumberFormatException("Expected a long but was " + this.value + " at line " + this.getLineNumber() + " column "
                        + this.getColumnNumber());
            }
        }

        if ((result >= 1L) && this.value.startsWith("0")) {
            throw new JsonException("JSON forbids octal prefixes: " + this.value + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }

        this.advance();
        return result;
    }

    public String nextName() throws JsonException, IOException {
        this.peek();
        if (this.token != Token.NAME) {
            throw new IllegalStateException("Expected a name but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }
        final String result = this.name;
        this.advance();
        return result;
    }

    private int nextNonWhitespace(final boolean throwOnEof) throws JsonException, IOException {
        final char[] buffer = this.buffer;
        int p = this.pos;
        int l = this.limit;
        while (true) {
            if (p == l) {
                this.pos = p;
                if (!this.fillBuffer(1)) {
                    break;
                }
                p = this.pos;
                l = this.limit;
            }

            final int c = buffer[p++];
            switch (c) {
                case '\t':
                case ' ':
                case '\n':
                case '\r':
                    continue;

                case '/':
                    this.pos = p;
                    if (p == l) {
                        this.pos--; // push back '/' so it's still in the buffer when this method returns
                        final boolean charsLoaded = this.fillBuffer(2);
                        this.pos++; // consume the '/' again
                        if (!charsLoaded) {
                            return c;
                        }
                    }

                    this.checkLenient();
                    final char peek = buffer[this.pos];
                    switch (peek) {
                        case '*':
                            // skip a /* c-style comment */
                            this.pos++;
                            if (!this.skipTo("*/")) {
                                throw new JsonException("Unterminated comment" + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
                            }
                            p = this.pos + 2;
                            l = this.limit;
                            continue;

                        case '/':
                            // skip a // end-of-line comment
                            this.pos++;
                            this.skipToEndOfLine();
                            p = this.pos;
                            l = this.limit;
                            continue;

                        default:
                            return c;
                    }

                case '#':
                    this.pos = p;
                    /*
                     * Skip a # hash end-of-line comment. The JSON RFC doesn't
                     * specify this behaviour, but it's required to parse
                     * existing documents. See http://b/2571423.
                     */
                    this.checkLenient();
                    this.skipToEndOfLine();
                    p = this.pos;
                    l = this.limit;
                    continue;

                default:
                    this.pos = p;
                    return c;
            }
        }
        if (throwOnEof) {
            throw new EOFException("End of input"
                    + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }
        return -1;
    }

    public Object nextNull() throws JsonException, IOException {
        this.peek();
        if (this.token != Token.NULL) {
            throw new IllegalStateException("Expected null but was " + this.token + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }

        this.advance();
        return null;
    }

    public Object nextNumber() throws JsonException, IOException {
        this.peek();
        if ((this.token != Token.STRING) && (this.token != Token.NUMBER)) {
            throw new IllegalStateException("Expected a double but was " + this.token + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }

        //integer
        if (this.value.indexOf('.') == -1) {
            final long result = Double.valueOf(this.value).longValue();
            this.advance();
            return result;
        }

        //double
        final double result = Double.parseDouble(this.value);
        if (((result >= 1.0d) && this.value.startsWith("0"))) {
            throw new JsonException("JSON forbids octal prefixes: " + this.value + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }
        if (!this.lenient && (Double.isNaN(result) || Double.isInfinite(result))) {
            throw new JsonException("JSON forbids NaN and infinities: " + this.value + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }

        this.advance();
        return result;
    }

    public String nextString() throws JsonException, IOException {

        this.peek();
        if ((this.token != Token.STRING) && (this.token != Token.NUMBER)) {
            throw new IllegalStateException("Expected a string but was " + this.peek() + " at line " + this.getLineNumber() + " column "
                    + this.getColumnNumber());
        }

        final String result = this.value;
        this.advance();
        return result;
    }

    private String nextString(final char quote) throws JsonException, IOException {
        // Like nextNonWhitespace, this uses locals 'p' and 'l' to save inner-loop field access.
        final char[] buffer = this.buffer;
        StringBuilder builder = null;
        while (true) {
            int p = this.pos;
            int l = this.limit;
            /* the index of the first character not yet appended to the builder. */
            int start = p;
            while (p < l) {
                final int c = buffer[p++];

                if (c == quote) {
                    this.pos = p;
                    if (this.skipping) {
                        return "skipped!";
                    } else if (builder == null) {
                        return this.stringPool.get(buffer, start, p - start - 1);
                    } else {
                        builder.append(buffer, start, p - start - 1);
                        return builder.toString();
                    }

                } else if (c == '\\') {
                    this.pos = p;
                    if (builder == null) {
                        builder = new StringBuilder();
                    }
                    builder.append(buffer, start, p - start - 1);
                    builder.append(this.readEscapeCharacter());
                    p = this.pos;
                    l = this.limit;
                    start = p;
                }
            }

            if (builder == null) {
                builder = new StringBuilder();
            }
            builder.append(buffer, start, p - start);
            this.pos = p;
            if (!this.fillBuffer(1)) {
                throw new JsonException("Unterminated string" + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
        }
    }

    @SuppressWarnings("fallthrough")
    private Token nextValue() throws JsonException, IOException {
        final int c = this.nextNonWhitespace(true);
        switch (c) {
            case '{':
                this.push(JsonScope.EMPTY_OBJECT);
                return this.token = Token.BEGIN_OBJECT;

            case '[':
                this.push(JsonScope.EMPTY_ARRAY);
                return this.token = Token.BEGIN_ARRAY;

            case '\'':
                this.checkLenient(); // fall-through
            case '"':
                this.value = this.nextString((char) c);
                return this.token = Token.STRING;

            default:
                this.pos--;
                return this.readLiteral();
        }
    }

    private Token objectValue() throws JsonException, IOException {
        switch (this.nextNonWhitespace(true)) {
            case ':':
                break;
            case '=':
                this.checkLenient();
                if (((this.pos < this.limit) || this.fillBuffer(1)) && (this.buffer[this.pos] == '>')) {
                    this.pos++;
                }
                break;
            default:
                throw new JsonException("Expected ':'" + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }

        this.stack[this.stackSize - 1] = JsonScope.NONEMPTY_OBJECT;
        return this.nextValue();
    }

    public Token peek() throws JsonException, IOException {
        if (this.token != null) {
            return this.token;
        }

        switch (this.stack[this.stackSize - 1]) {
            case JsonScope.EMPTY_DOCUMENT:
                if (this.lenient) {
                    this.consumeNonExecutePrefix();
                }
                this.stack[this.stackSize - 1] = JsonScope.NONEMPTY_DOCUMENT;
                final Token firstToken = this.nextValue();
                if (!this.lenient && (this.token != Token.BEGIN_ARRAY) && (this.token != Token.BEGIN_OBJECT)) {
                    throw new JsonException(
                            "Expected JSON document to start with '[' or '{' but was " + this.token
                                    + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
                }
                return firstToken;
            case JsonScope.EMPTY_ARRAY:
                return this.nextInArray(true);
            case JsonScope.NONEMPTY_ARRAY:
                return this.nextInArray(false);
            case JsonScope.EMPTY_OBJECT:
                return this.nextInObject(true);
            case JsonScope.DANGLING_NAME:
                return this.objectValue();
            case JsonScope.NONEMPTY_OBJECT:
                return this.nextInObject(false);
            case JsonScope.NONEMPTY_DOCUMENT:
                final int c = this.nextNonWhitespace(false);
                if (c == -1) {
                    return Token.END_DOCUMENT;
                }
                this.pos--;
                if (!this.lenient) {
                    throw new JsonException("Expected EOF at line " + this.getLineNumber() + " column " + this.getColumnNumber());
                }
                return this.nextValue();
            case JsonScope.CLOSED:
                throw new IllegalStateException("JsonReader is closed");
            default:
                throw new AssertionError();
        }
    }

    private void push(final int newTop) {
        if (this.stackSize == this.stack.length) {
            final int[] newStack = new int[this.stackSize * 2];
            System.arraycopy(this.stack, 0, newStack, 0, this.stackSize);
            this.stack = newStack;
        }
        this.stack[this.stackSize++] = newTop;
    }

    private char readEscapeCharacter() throws JsonException, IOException {
        if ((this.pos == this.limit) && !this.fillBuffer(1)) {
            throw new JsonException("Unterminated escape sequence at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }

        final char escaped = this.buffer[this.pos++];
        switch (escaped) {
            case 'u':
                if (((this.pos + 4) > this.limit) && !this.fillBuffer(4)) {
                    throw new JsonException("Unterminated escape sequence at line " + this.getLineNumber() + " column " + this.getColumnNumber());
                }
                // Equivalent to Integer.parseInt(stringPool.get(buffer, pos, 4), 16);
                char result = 0;
                for (int i = this.pos, end = i + 4; i < end; i++) {
                    final char c = this.buffer[i];
                    result <<= 4;
                    if ((c >= '0') && (c <= '9')) {
                        result += (c - '0');
                    } else if ((c >= 'a') && (c <= 'f')) {
                        result += ((c - 'a') + 10);
                    } else if ((c >= 'A') && (c <= 'F')) {
                        result += ((c - 'A') + 10);
                    } else {
                        throw new NumberFormatException("\\u" + this.stringPool.get(this.buffer, this.pos, 4));
                    }
                }
                this.pos += 4;
                return result;

            case 't':
                return '\t';

            case 'b':
                return '\b';

            case 'n':
                return '\n';

            case 'r':
                return '\r';

            case 'f':
                return '\f';

            case '\'':
            case '"':
            case '\\':
            default:
                return escaped;
        }
    }

    private Token readLiteral() throws JsonException, IOException {
        this.value = this.nextLiteral(true);
        if (this.valueLength == 0) {
            throw new JsonException("Expected literal value at line " + this.getLineNumber() + " column " + this.getColumnNumber());

        }
        this.token = this.decodeLiteral();
        if (this.token == Token.STRING) {
            this.checkLenient();
        }
        return this.token;
    }

    public void setLenient(final boolean lenient) {
        this.lenient = lenient;
    }

    private boolean skipTo(final String toFind) throws IOException {
        outer: for (; ((this.pos + toFind.length()) <= this.limit) || this.fillBuffer(toFind.length()); this.pos++) {
            for (int c = 0; c < toFind.length(); c++) {
                if (this.buffer[this.pos + c] != toFind.charAt(c)) {
                    continue outer;
                }
            }
            return true;
        }
        return false;
    }

    private void skipToEndOfLine() throws IOException {
        while ((this.pos < this.limit) || this.fillBuffer(1)) {
            final char c = this.buffer[this.pos++];
            if ((c == '\r') || (c == '\n')) {
                break;
            }
        }
    }

    public void skipValue() throws JsonException, IOException {
        this.skipping = true;
        try {
            int count = 0;
            do {
                final Token token = this.advance();
                if ((token == Token.BEGIN_ARRAY) || (token == Token.BEGIN_OBJECT)) {
                    count++;
                } else if ((token == Token.END_ARRAY) || (token == Token.END_OBJECT)) {
                    count--;
                }
            } while (count != 0);
        } finally {
            this.skipping = false;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber();
    }

}
