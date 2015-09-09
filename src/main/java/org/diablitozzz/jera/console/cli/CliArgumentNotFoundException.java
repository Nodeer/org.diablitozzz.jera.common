package org.diablitozzz.jera.console.cli;

public class CliArgumentNotFoundException extends CliParseException {
    
    private static final long serialVersionUID = 1L;
    private final CliArgument argument;
    
    public CliArgumentNotFoundException(final CliArgument argument) {
        super("Can't find argument:" + argument.name());
        this.argument = argument;
    }
    
    public CliArgument getArgument() {
        return this.argument;
    }
    
}
