package org.diablitozzz.jera.db.log;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.diablitozzz.jera.db.DbArray;
import org.diablitozzz.jera.log.LogLogger;

public class DbLogHandlerSqlite implements DbLogHandler {
    
    private final static NumberFormat timeFormat = new DecimalFormat("0.000000");
    
    private final static Pattern patternParams = Pattern.compile("\\{\\}");
    private final static Pattern patternSqlParam = Pattern.compile("\\?");
    
    private static String replaceEach(final String text, final String[] searchList, final String[] replacementList) {
        
        if (text == null || text.length() == 0 || searchList == null || searchList.length == 0 || replacementList == null || replacementList.length == 0) {
            return text;
        }
        final int searchLength = searchList.length;
        final int replacementLength = replacementList.length;
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
        }
        final boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0 || replacementList[i] == null) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i]);
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else if (textIndex == -1 || tempIndex < textIndex) {
                textIndex = tempIndex;
                replaceIndex = i;
            }
        }
        if (textIndex == -1) {
            return text;
        }
        int start = 0;
        int increase = 0;
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i] == null || replacementList[i] == null) {
                continue;
            }
            final int greater = replacementList[i].length() - searchList[i].length();
            if (greater > 0) {
                increase += 3 * greater; // assume 3 matches
            }
        }
        increase = Math.min(increase, text.length() / 5);
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (textIndex != -1) {
            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacementList[replaceIndex]);
            start = textIndex + searchList[replaceIndex].length();
            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0 || replacementList[i] == null) {
                    continue;
                }
                tempIndex = text.indexOf(searchList[i], start);
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        final int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        final String result = buf.toString();
        return result;
    }
    
    private final LogLogger logger;
    
    public DbLogHandlerSqlite(final LogLogger logger) {
        this.logger = logger;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void log(final long start, final String message, final Object[] params, final Throwable exception) {
        
        final long durationNano = System.nanoTime() - start;
        
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final StackTraceElement current = stackTrace[3];
        
        final String resourceName = current.getClassName() + "." + current.getMethodName() + ":" + current.getLineNumber();
        final String methodName = current.getMethodName();
        String log;
        
        switch (methodName) {
            case "execute":
            case "select":
            case "update":
                if (params.length == 1) {
                    log = this.makeLog(durationNano, resourceName, message, params, exception);
                } else { //parse sql
                    log = this.makeLog(durationNano, resourceName, message, new Object[] { this.makeSql((String) params[0], (Object[]) params[1]) }, exception);
                }
                break;
            case "batch":
                if (params[0].getClass().isArray()) { //simple sql array
                    final StringBuilder builder = new StringBuilder();
                    for (final String sql : (String[]) params[0]) {
                        builder.append("\n\t");
                        builder.append(sql);
                    }
                    log = this.makeLog(durationNano, resourceName, message, new Object[] { builder.toString() }, exception);
                } else { //one sql, many args
                    final StringBuilder builder = new StringBuilder();
                    for (final Object[] batchParams : (Collection<Object[]>) params[1]) {
                        builder.append("\n\t");
                        builder.append(this.makeSql((String) params[0], batchParams));
                    }
                    log = this.makeLog(durationNano, resourceName, message, new Object[] { builder.toString() }, exception);
                }
                break;
            default:
                log = this.makeLog(durationNano, resourceName, message, params, exception);
                break;
        }
        if (exception == null) {
            this.writeDebug(log.toString());
        } else {
            this.writeError(log.toString());
        }
    }
    
    private String makeException(final Throwable error) {
        
        final StringBuilder builder = new StringBuilder();
        // сама ошибка
        builder.append(error.getClass().getCanonicalName());
        builder.append(": ");
        builder.append(error.getLocalizedMessage());
        builder.append('\n');
        
        // stack trace
        for (final StackTraceElement stackTrace : error.getStackTrace()) {
            if (stackTrace.isNativeMethod()) {
                continue;
            }
            builder.append('\t');
            builder.append(stackTrace.getClassName());
            builder.append('.');
            builder.append(stackTrace.getMethodName());
            builder.append(": ");
            builder.append(stackTrace.getLineNumber());
            builder.append('\n');
        }
        
        // cause
        final Throwable cause = error.getCause();
        if (cause != null) {
            builder.append('\n');
            builder.append('\n');
            builder.append(this.makeException(cause));
        }
        return builder.toString();
    }
    
    private String makeLog(final long durationNano, final String resourceName, final String template, final Object[] params, final Throwable exception) {
        
        final String message = this.makeMessageTemplate(template, params);
        final double durationSec = durationNano / 1000000000.0d;
        
        final StringBuilder log = new StringBuilder();
        log.append(DbLogHandlerSqlite.timeFormat.format(durationSec));
        log.append("s");
        log.append(" - ");
        log.append(message);
        if (exception != null) {
            log.append("\n\n\t");
            log.append(this.makeException(exception));
        }
        return log.toString();
    }
    
    private String makeMessageTemplate(final String template, final Object[] params) {
        
        if (template == null) {
            return "";
        }
        String content = template;
        
        if (params != null) {
            int questionMarkCount = 1;
            final Matcher m = DbLogHandlerSqlite.patternParams.matcher(content);
            final StringBuffer stringBuffer = new StringBuffer();
            while (m.find()) {
                final Object paramValue = params[questionMarkCount - 1];
                final String value = Matcher.quoteReplacement(this.makeMessageTemplateParam(paramValue));
                m.appendReplacement(stringBuffer, value);
                questionMarkCount++;
            }
            content = String.valueOf(m.appendTail(stringBuffer));
        }
        
        content = content.trim();
        return content;
    }
    
    private String makeMessageTemplateParam(final Object object) {
        
        if (object == null) {
            return "null";
        }
        return object.toString();
    }
    
    private String makeSql(final String sql, final Object[] params) {
        
        if (sql == null) {
            return "";
        }
        String content = sql;
        
        if (params != null) {
            try {
                int questionMarkCount = 1;
                final Matcher m = DbLogHandlerSqlite.patternSqlParam.matcher(content);
                final StringBuffer stringBuffer = new StringBuffer();
                while (m.find()) {
                    final Object paramValue = params[questionMarkCount - 1];
                    m.appendReplacement(stringBuffer, this.makeSqlParam(paramValue));
                    questionMarkCount++;
                }
                content = String.valueOf(m.appendTail(stringBuffer));
            } catch (final IllegalArgumentException e) {
                throw new IllegalArgumentException(sql, e);
            }
        }
        
        final StringBuilder out = new StringBuilder(content.length() + 5);
        out.append(content.trim());
        if (!content.endsWith(";")) {
            out.append(";");
        }
        return out.toString();
    }
    
    private String makeSqlParam(final Object object) {
        
        if (object == null) {
            return "null";
        } else if (object instanceof String) {
            
            final String text = DbLogHandlerSqlite.replaceEach(
                    (String) object,
                    new String[] { "\\", "$", "'", "\"", "\r", "\n", "\t" },
                    new String[] { "\\\\\\\\", "\\$", "\\\\'", "\\\\\"", "\\\\r", "\\\\n", "\\\\t" });
            return "'" + text + "'";
        }
        //else if (object instanceof Timestamp) {
        //	return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object) + "'";
        //}
        //else if (object instanceof Time) {
        //	return "'" + new SimpleDateFormat("HH:mm:ss").format(object) + "'";
        //}
        else if (object instanceof java.util.Date) {
            return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object) + "'";
        } else if (object instanceof UUID) {
            return "'" + object.toString() + "'";
        } else if (object instanceof Boolean) {
            return ((Boolean) object).booleanValue() ? "TRUE" : "FALSE";
        } else if (object instanceof java.sql.Array) {
            final java.sql.Array array = (java.sql.Array) object;
            try {
                final Object arrObj = array.getArray();
                final int size = Array.getLength(arrObj);
                final StringBuilder arrLog = new StringBuilder();
                arrLog.append("ARRAY[ ");
                for (int i = 0; i < size; i++) {
                    arrLog.append(this.makeMessageTemplateParam(Array.get(arrObj, i)));
                    if (i < size - 1) {
                        arrLog.append(", ");
                    }
                }
                arrLog.append("]");
                return arrLog.toString();
            } catch (final SQLException e) {
                throw new RuntimeException("Can't get array", e);
            }
        } else if (object instanceof DbArray) {
            final DbArray array = (DbArray) object;
            
            final Object[] arrayElements = array.getElements();
            final int size = arrayElements.length;
            final StringBuilder arrLog = new StringBuilder();
            arrLog.append("ARRAY[");
            for (int i = 0; i < size; i++) {
                arrLog.append(this.makeMessageTemplateParam(arrayElements[i]));
                if (i < size - 1) {
                    arrLog.append(", ");
                }
            }
            arrLog.append("]");
            return arrLog.toString();
        } else {
            return object.toString();
        }
    }
    
    private void writeDebug(final String message) {
        this.logger.debug(message);
    }
    
    private void writeError(final String error) {
        this.logger.error(error);
    }
}
