package org.diablitozzz.jera.log;

public class LogService {
    
    private static volatile LogHandler logHandler = new LogHandlerStd();
    private static volatile LogLoggerFactory loggerFactory = new LogLoggerFactoryBasic();

    public static LogLogger createLogger(final Class<?> clazz) {
        return LogService.getLoggerFactory().createLogger(clazz);
    }
    
    public static LogLoggerFactory getLoggerFactory() {
        return LogService.loggerFactory;
    }

    public static LogHandler getLogHandler() {
        return LogService.logHandler;
    }

    public static void setLoggerFactory(final LogLoggerFactory loggerFactory) {
        LogService.loggerFactory = loggerFactory;

    }
    
    public static void setLogHandler(final LogHandler logHandler) {
        LogService.logHandler = logHandler;
    }
    
}
