package org.diablitozzz.jera.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class JsonWriter {

    private final Writer writer;
    private final boolean htmlSafe;
    private boolean longAsString = false;

    public JsonWriter(final Writer writer) {
        this(writer, false);
    }

    public JsonWriter(final Writer writer, final boolean htmlSafe) {
        this.writer = writer;
        this.htmlSafe = htmlSafe;
    }

    public boolean isLongAsString() {
        return this.longAsString;
    }

    private void makeBoolean(final boolean value) throws IOException {
        this.writer.write(Boolean.valueOf(value).toString());
    }

    private void makeBooleanArray(final boolean[] value) throws IOException {
        this.writer.append('[');
        for (int i = 0; i < value.length; i++) {
            this.makeBoolean(value[i]);
            if (i < (value.length - 1)) {
                this.writer.append(',');
            }
        }
        this.writer.append(']');
    }

    private void makeChar(final char value) throws IOException {
        this.makeString(String.valueOf(value));
    }

    private void makeCharArray(final char[] value) throws IOException {
        this.writer.append('[');
        for (int i = 0; i < value.length; i++) {
            this.makeChar(value[i]);
            if (i < (value.length - 1)) {
                this.writer.append(',');
            }
        }
        this.writer.append(']');
    }

    private void makeDouble(final double value) throws IOException {
        this.writer.write(String.format("%f", value));
    }

    private void makeDoubleArray(final double[] value) throws IOException {
        this.writer.append('[');
        for (int i = 0; i < value.length; i++) {
            this.makeDouble(value[i]);
            if (i < (value.length - 1)) {
                this.writer.append(',');
            }
        }
        this.writer.append(']');
    }

    private void makeFloat(final float value) throws IOException {
        this.writer.write(Float.toString(value));
    }

    private void makeFloatArray(final float[] value) throws IOException {
        this.writer.append('[');
        for (int i = 0; i < value.length; i++) {
            this.makeFloat(value[i]);
            if (i < (value.length - 1)) {
                this.writer.append(',');
            }
        }
        this.writer.append(']');
    }

    private void makeInt(final int value) throws IOException {
        this.writer.write(Integer.toString(value));
    }

    private void makeIntArray(final int[] value) throws IOException {
        this.writer.append('[');
        for (int i = 0; i < value.length; i++) {
            this.makeInt(value[i]);
            if (i < (value.length - 1)) {
                this.writer.append(',');
            }
        }
        this.writer.append(']');
    }

    private void makeIterable(final Iterable<?> value) throws IOException {
        final Iterator<?> iterator = value.iterator();

        Object current = null;
        Object next = null;

        this.writer.append('[');
        while (true) {

            if (!iterator.hasNext()) {
                if (next != null) {
                    this.makeObject(next);
                }
                break;
            }
            if (next != null) {
                current = next;
                next = iterator.next();
            } else {
                next = iterator.next();
                continue;
            }
            this.makeObject(current);
            this.writer.append(',');
        }
        this.writer.append(']');
    }

    private void makeLong(final long value) throws IOException {
        if (!this.longAsString) {
            this.writer.write(Long.toString(value));
        } else {
            this.makeString(Long.toString(value));
        }

    }

    private void makeLongArray(final long[] value) throws IOException {
        this.writer.append('[');
        for (int i = 0; i < value.length; i++) {
            this.makeLong(value[i]);
            if (i < (value.length - 1)) {
                this.writer.append(',');
            }
        }
        this.writer.append(']');
    }

    private void makeMap(final Map<?, ?> value) throws IOException {
        this.writer.append('{');
        boolean start = true;
        for (final Map.Entry<?, ?> entry : value.entrySet()) {
            // Если элемент пустой, пропускаем
            if (entry.getValue() == null) {
                continue;
            }

            if (!start) {
                // если элемент не первый, добавляем разделитель
                this.writer.append(',');
            } else {
                start = false;
            }
            this.makeString(entry.getKey().toString());
            this.writer.append(':');

            // value
            this.makeObject(entry.getValue());
        }

        this.writer.append('}');
    }

    private void makeNull() throws IOException {
        this.writer.append("null");
    }

    private void makeObject(final Object value) throws IOException {
        // null
        if (value == null) {
            this.makeNull();
        }
        // MixedMap
        else if (value instanceof JsonObject) {
            final JsonObject viewMap = (JsonObject) value;
            if (!viewMap.isArray()) {
                this.makeMap(viewMap.toMap());
            } else {
                this.makeObjectArray(viewMap.toObjectArray());
            }
        }
        // map
        else if (value instanceof Map) {
            this.makeMap((Map<?, ?>) value);
        }
        // iterable
        else if (value instanceof Iterable) {

            this.makeIterable((Iterable<?>) value);
        }
        // int
        else if (value instanceof Integer) {
            this.makeInt((int) value);
        }
        // int []
        else if (value instanceof int[]) {
            this.makeIntArray((int[]) value);
        }
        // short
        else if (value instanceof Short) {
            this.makeShort((short) value);
        }
        // short []
        else if (value instanceof short[]) {
            this.makeShortArray((short[]) value);
        }
        // long
        else if (value instanceof Long) {
            this.makeLong((long) value);
        }
        // long []
        else if (value instanceof long[]) {
            this.makeLongArray((long[]) value);
        }
        // float
        else if (value instanceof Float) {
            this.makeFloat((float) value);
        }
        // float []
        else if (value instanceof float[]) {
            this.makeFloatArray((float[]) value);
        }
        // boolean
        else if (value instanceof Boolean) {
            this.makeBoolean((boolean) value);
        }
        // boolean []
        else if (value instanceof boolean[]) {
            this.makeBooleanArray((boolean[]) value);
        }
        // double
        else if (value instanceof Double) {
            this.makeDouble((double) value);
        }
        // double[]
        else if (value instanceof double[]) {
            this.makeDoubleArray((double[]) value);
        }
        //Calendar
        else if (value instanceof Calendar) {
            this.makeString(JsonUtilDate.toISO8601((Calendar) value));
        }
        //Date
        else if (value instanceof Date) {
            this.makeString(JsonUtilDate.toISO8601((Date) value));
        }
        // char
        else if (value instanceof Character) {
            this.makeChar((char) value);
        }
        // char[]
        else if (value instanceof char[]) {
            this.makeCharArray((char[]) value);
        }
        // byte[]
        // else if (value instanceof byte[]) {
        // this.makeByteArray((byte[]) value);
        // }
        // object array
        else if (value instanceof Object[]) {
            this.makeObjectArray((Object[]) value);
        }
        // other objects
        else {
            this.makeString(value.toString());
        }
    }

    private void makeObjectArray(final Object[] value) throws IOException {
        this.writer.append('[');
        for (int i = 0; i < value.length; i++) {
            this.makeObject(value[i]);
            if (i < (value.length - 1)) {
                this.writer.append(',');
            }
        }
        this.writer.append(']');
    }

    private void makeShort(final short value) throws IOException {
        this.writer.append(Short.toString(value));
    }

    private void makeShortArray(final short[] value) throws IOException {
        this.writer.append('[');
        for (int i = 0; i < value.length; i++) {
            this.makeShort(value[i]);
            if (i < (value.length - 1)) {
                this.writer.append(',');
            }
        }
        this.writer.append(']');
    }

    private void makeString(final String value) throws IOException {
        this.writer.append('"');
        final char[] chars = value.toCharArray();
        for (final char c : chars) {
            switch (c) {
                case '"':
                case '\\':
                    this.writer.append('\\');
                    this.writer.append(c);
                    break;
                case '\t':
                    this.writer.append('\\');
                    this.writer.append('t');
                    break;
                case '\b':
                    this.writer.append('\\');
                    this.writer.append('b');
                    break;
                case '\n':
                    this.writer.append('\\');
                    this.writer.append('n');
                    break;
                case '\r':
                    this.writer.append('\\');
                    this.writer.append('r');
                    break;
                case '\f':
                    this.writer.append('\\');
                    this.writer.append('f');
                    break;
                case '<':
                case '>':
                case '&':
                case '=':
                case '\'':
                    if (this.htmlSafe) {
                        this.writeHex(c);
                    } else {
                        this.writer.append(c);
                    }
                    break;
                case '\u2028':
                case '\u2029':
                    this.writeHex(c);
                    break;
                default:
                    if (c <= 0x1F) {
                        this.writeHex(c);
                    } else {
                        this.writer.append(c);
                    }
                    break;
            }
        }
        this.writer.append('"');
    }

    public void setLongAsString(final boolean longAsString) {
        this.longAsString = longAsString;
    }

    public void write(final Object data) throws IOException {
        this.makeObject(data);
    }

    private void writeHex(final char c) throws IOException {
        this.writer.write(String.format("\\u%04x", (int) c));
    }

}
