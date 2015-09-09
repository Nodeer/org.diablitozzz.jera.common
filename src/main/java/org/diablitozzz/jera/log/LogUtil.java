package org.diablitozzz.jera.log;

import java.util.ArrayList;
import java.util.List;

public class LogUtil {

    public static String exceptionToString(final Throwable e) {
        
        final StringBuilder buffer = new StringBuilder(64);
        LogUtil.writeException(buffer, e);
        return buffer.toString();
    }

    private static Object[] makeArgs(final Object[] args) {
        if (args == null) {
            return new Object[] {};
        }
        final List<Object> out = new ArrayList<>(args.length);
        for (final Object arg : args) {
            if (arg instanceof Throwable) {
                out.add(LogUtil.exceptionToString((Throwable) arg));
            } else {
                out.add(arg);
            }
        }
        return out.toArray();
    }

    public static String makeMessage(final String message, final Object[] params) {
        if (message == null) {
            return "";
        }
        return String.format(message, LogUtil.makeArgs(params));
    }
    
    public static String stackTraceToString(final StackTraceElement[] stackTraceList) {
        final StringBuilder builder = new StringBuilder();
        LogUtil.writeStackTrace(builder, stackTraceList);
        return builder.toString();
    }
    
    private static void writeException(final StringBuilder builder, final Throwable error) {
        
        // сама ошибка
        builder.append(error.getClass().getCanonicalName());
        builder.append(": ");
        builder.append(error.getLocalizedMessage());
        builder.append('\n');
        
        // stack trace
        LogUtil.writeStackTrace(builder, error.getStackTrace());
        
        // cause
        final Throwable cause = error.getCause();
        if (cause != null) {
            builder.append('\n');
            builder.append('\n');
            LogUtil.writeException(builder, cause);
        }
    }
    
    public static void writeStackTrace(final StringBuilder builder, final StackTraceElement[] stackTraceList) {
        
        // stack trace
        for (final StackTraceElement stackTrace : stackTraceList) {
            
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
    }
    
}
