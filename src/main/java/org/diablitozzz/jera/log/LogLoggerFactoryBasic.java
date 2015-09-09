package org.diablitozzz.jera.log;

public class LogLoggerFactoryBasic implements LogLoggerFactory {
    
    @Override
    public LogLogger createLogger(final Class<?> clazz) {
        return new LogLoggerBasic(clazz.getName());
    }
    
}
