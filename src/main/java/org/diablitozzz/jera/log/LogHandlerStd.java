package org.diablitozzz.jera.log;

import java.time.format.DateTimeFormatter;

public class LogHandlerStd implements LogHandler {
    
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy / HH:mm:ss.SSS");
    
    private String makeMessage(final LogMessage logMessage) {

        final StringBuilder message = new StringBuilder();
        message.append("[");
        message.append(logMessage.getType().name());
        message.append("]");
        message.append("\t");
        message.append(LogHandlerStd.dateTimeFormatter.format(logMessage.getDateTime()));
        message.append(" - ");
        message.append(logMessage.getLocation().getClassName());
        message.append(".");
        message.append(logMessage.getLocation().getMethodName());
        message.append(":");
        message.append(logMessage.getLocation().getLineNumber());
        message.append(" - ");
        message.append(logMessage.getMessage());
        return message.toString();
    }

    @Override
    public void onLog(final LogMessage logMessage) {
        synchronized (this) {
            final String message = this.makeMessage(logMessage);
            switch (logMessage.getType()) {
                case DEBUG:
                    System.out.println(message);
                    System.out.flush();
                    break;
                case ERROR:
                    System.err.println(message);
                    System.out.flush();
                    break;
                case INFO:
                    System.out.println(message);
                    System.out.flush();
                    break;
                case TRACE:
                    System.out.println(message);
                    System.out.flush();
                    break;
                case WARN:
                default:
                    System.err.println(message);
                    System.out.flush();
                    break;
            }
        }
    }
}
