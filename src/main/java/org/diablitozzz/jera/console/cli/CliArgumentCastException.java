package org.diablitozzz.jera.console.cli;

public class CliArgumentCastException extends CliParseException {
    
    private static final long serialVersionUID = 1L;
    private final CliArgument argument;
    
    public CliArgumentCastException(final CliArgument argument, final Throwable cause) {
        super("Can't cast argument:" + argument.name(), cause);
        this.argument = argument;
    }
    
    public CliArgument getArgument() {
        return this.argument;
    }
    
}