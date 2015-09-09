package org.diablitozzz.jera.cron;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

class CronUtils {
    
    public static String[] commaDelimitedListToStringArray(final String str) {
        return CronUtils.delimitedListToStringArray(str, ",");
    }
    
    private static String deleteAny(final String inString, final String charsToDelete) {
        if (!CronUtils.hasLength(inString) || !CronUtils.hasLength(charsToDelete)) {
            return inString;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inString.length(); i++) {
            final char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static String[] delimitedListToStringArray(final String str, final String delimiter) {
        return CronUtils.delimitedListToStringArray(str, delimiter, null);
    }
    
    private static String[] delimitedListToStringArray(final String str, final String delimiter, final String charsToDelete) {
        if (str == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[] { str };
        }
        final List<String> result = new ArrayList<String>();
        if ("".equals(delimiter)) {
            for (int i = 0; i < str.length(); i++) {
                result.add(CronUtils.deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        } else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(CronUtils.deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(CronUtils.deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return CronUtils.toStringArray(result);
    }
    
    private static boolean hasLength(final CharSequence str) {
        return (str != null && str.length() > 0);
    }
    
    private static boolean hasLength(final String str) {
        return CronUtils.hasLength((CharSequence) str);
    }
    
    public static String replace(final String inString, final String oldPattern, final String newPattern) {
        if (!CronUtils.hasLength(inString) || !CronUtils.hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        final StringBuilder sb = new StringBuilder();
        int pos = 0; // our position in the old string
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        final int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sb.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }
    
    public static String[] tokenizeToStringArray(final String str, final String delimiters) {
        return CronUtils.tokenizeToStringArray(str, delimiters, true, true);
    }
    
    private static String[] tokenizeToStringArray(
            final String str, final String delimiters, final boolean trimTokens, final boolean ignoreEmptyTokens) {
            
        if (str == null) {
            return null;
        }
        final StringTokenizer st = new StringTokenizer(str, delimiters);
        final List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return CronUtils.toStringArray(tokens);
    }
    
    private static String[] toStringArray(final Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }
}
