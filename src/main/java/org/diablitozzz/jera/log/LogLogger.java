package org.diablitozzz.jera.log;

public interface LogLogger {
    
    void debug(final Object message);

    void debug(final String message, final Object... params);

    void error(final Object message);
    
    void error(final String message, final Object... params);
    
    LogLevel getLevel();
    
    String getName();
    
    void info(final Object message);
    
    void info(final String message, final Object... params);

    void setLevel(LogLevel level);
    
    void trace(final Object message);
    
    void trace(final String message, final Object... params);

    void warn(final Object message);

    void warn(final String message, final Object... params);
}
