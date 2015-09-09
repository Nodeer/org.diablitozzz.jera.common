package org.diablitozzz.jera.console.cli;

public class CliParseException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public CliParseException(final String message) {
        super(message);
    }
    
    public CliParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}