package org.diablitozzz.jera.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UtilString {

    public static String[] explodeI(final String str, final String seperator) {

        if (str == null || str.isEmpty()) {
            return new String[] {};
        }
        final List<String> out = new ArrayList<>();
        final String seperatorLow = seperator.toLowerCase();
        final String strLow = str.toLowerCase();
        final int sepLength = seperatorLow.length();
        final int strLength = str.length();
        int fromIndex = 0;
        int index;
        while (true) {
            index = strLow.indexOf(seperatorLow, fromIndex);
            if (index == -1) {
                out.add(str.substring(fromIndex, strLength));
                break;
            }
            //prefix
            if (index > fromIndex) {
                out.add(str.substring(fromIndex, index));
            }
            //sep
            out.add(str.substring(index, index + sepLength));
            fromIndex = index + sepLength;
            if (fromIndex >= strLength) {
                break;
            }
        }
        return out.toArray(new String[out.size()]);
    }

    public static String[] split(final String str, final String separatorChars, final boolean ignoreCase, final int max) {
        if (str == null) {
            return new String[] {};
        } else if (str.isEmpty()) {
            return new String[] {};
        }
        if (ignoreCase) {
            return Pattern.compile(Pattern.quote(separatorChars), Pattern.CASE_INSENSITIVE).split(str, max);
        }
        return Pattern.compile(Pattern.quote(separatorChars)).split(str, max);
    }

    public static String subString(final String value, final int length) {
        if (value.length() <= length) {
            return value;
        }
        return value.substring(0, length);
    }

    public static String subString(final String value, final int length, final String end) {
        if (value.length() <= length) {
            return value;
        }
        return value.substring(0, length) + end;
    }

}
