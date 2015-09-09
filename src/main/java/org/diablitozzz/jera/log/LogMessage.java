package org.diablitozzz.jera.log;

import java.time.LocalDateTime;

public class LogMessage {
    
    private final LocalDateTime dateTime;
    private final LogLevel type;
    private final String loggerName;
    private final StackTraceElement location;
    private final String message;

    public LogMessage(final LogLevel type, final String loggerName, final StackTraceElement location, final String message) {
        this.dateTime = LocalDateTime.now();
        this.type = type;
        this.loggerName = loggerName;
        this.location = location;
        this.message = message;
    }
    
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }
    
    public StackTraceElement getLocation() {
        return this.location;
    }
    
    public String getLoggerName() {
        return this.loggerName;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public LogLevel getType() {
        return this.type;
    }
    
}
