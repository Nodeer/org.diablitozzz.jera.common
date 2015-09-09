package org.diablitozzz.jera.log;

public class LogLoggerBasic implements LogLogger {
    
    private final String name;
    private LogLevel level = LogLevel.DEBUG;

    public LogLoggerBasic(final String name) {
        this.name = name;
    }

    private LogMessage createLogMessage(final LogLevel type, final StackTraceElement[] stackTrace, final Object message) {
        final StackTraceElement location = stackTrace.length > 2 ? stackTrace[2] : stackTrace[0];
        final String mes;
        if (message == null) {
            mes = "null";
        } else if (message instanceof Throwable) {
            mes = LogUtil.exceptionToString((Throwable) message);
        } else {
            mes = message.toString();
        }
        return new LogMessage(type, this.name, location, mes);
    }

    private LogMessage createLogMessage(final LogLevel type, final StackTraceElement[] stackTrace, final String message, final Object[] params) {
        final StackTraceElement location = stackTrace.length > 2 ? stackTrace[2] : stackTrace[0];
        final String mes = LogUtil.makeMessage(message, params);
        return new LogMessage(type, this.name, location, mes);
    }
    
    @Override
    public void debug(final Object message) {
        if (this.isDebugEnable()) {
            LogService.getLogHandler().onLog(this.createLogMessage(LogLevel.DEBUG, Thread.currentThread().getStackTrace(), message));
        }
    }
    
    @Override
    public void debug(final String message, final Object... params) {
        if (this.isDebugEnable()) {
            LogService.getLogHandler().onLog(this.createLogMessage(LogLevel.DEBUG, Thread.currentThread().getStackTrace(), message, params));
        }
    }

    @Override
    public void error(final Object message) {
        if (this.isErrorEnable()) {
            LogService.getLogHandler().onLog(this.createLogMessage(LogLevel.ERROR, Thread.currentThread().getStackTrace(), message));
        }
    }

    @Override
    public void error(final String message, final Object... params) {
        if (this.isErrorEnable()) {
            LogService.getLogHandler().onLog(this.createLogMessage(LogLevel.ERROR, Thread.currentThread().getStackTrace(), message, params));
        }
    }
    
    @Override
    public LogLevel getLevel() {
        return this.level;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void info(final Object message) {
        if (this.isInfoEnable()) {
            LogService.getLogHandler().onLog(this.createLogMessage(LogLevel.INFO, Thread.currentThread().getStackTrace(), message));
        }
    }
    
    @Override
    public void info(final String message, final Object... params) {
        if (this.isInfoEnable()) {
            LogService.getLogHandler().onLog(this.createLogMessage(LogLevel.INFO, Thread.currentThread().getStackTrace(), message, params));
        }
    }
    
    private boolean isDebugEnable() {
        switch (this.level) {
            case TRACE:
                return true;
            case DEBUG:
                return true;
            case INFO:
                return false;
            case WARN:
                return false;
            default:
            case ERROR:
                return false;
        }
    }
    
    private boolean isErrorEnable() {
        switch (this.level) {
            case TRACE:
                return true;
            case DEBUG:
                return true;
            case INFO:
                return true;
            case WARN:
                return true;
            default:
            case ERROR:
                return true;
        }
    }
    
    private boolean isInfoEnable() {
        switch (this.level) {
            case TRACE:
                return true;
            case DEBUG:
                return true;
            case INFO:
                return true;
            case WARN:
                return false;
            default:
            case ERROR:
                return false;
        }
    }
    
    private boolean isTraceEnable() {
        switch (this.level) {
            case TRACE:
                return true;
            case DEBUG:
                return false;
            case INFO:
                return false;
            case WARN:
                return false;
            default:
            case ERROR:
                return false;
        }
    }
    
    private boolean isWarnEnable() {
        switch (this.level) {
            case TRACE:
                return true;
            case DEBUG:
                return true;
            case INFO:
                return true;
            case WARN:
                return true;
            default:
            case ERROR:
                return false;
        }
    }
    
    @Override
    public void setLevel(final LogLevel level) {
        this.level = level;
    }
    
    @Override
    public void trace(final Object message) {
        if (this.isTraceEnable()) {
            LogService.getLogHandler().onLog(this.createLogMessage(LogLevel.TRACE, Thread.currentThread().getStackTrace(), message));
        }
    }

    @Override
    public void trace(final String message, final Object... params) {
        if (this.isTraceEnable()) {
            LogService.getLogHandler().onLog(this.createLogMessage(LogLevel.TRACE, Thread.currentThread().getStackTrace(), message, params));
        }
    }

    @Override
    public void warn(final Object message) {
        if (this.isWarnEnable()) {
            LogService.getLogHandler().onLog(this.createLogMessage(LogLevel.WARN, Thread.currentThread().getStackTrace(), message));
        }
    }

    @Override
    public void warn(final String message, final Object... params) {
        if (this.isWarnEnable()) {
            LogService.getLogHandler().onLog(this.createLogMessage(LogLevel.WARN, Thread.currentThread().getStackTrace(), message, params));
        }
    }
}
