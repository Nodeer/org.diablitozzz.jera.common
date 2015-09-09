package org.diablitozzz.jera.console;

/**
 * @see http://en.wikipedia.org/wiki/ANSI_escape_code
 */
public enum ConsoleColor {

	BLACK("\u001B[30m"),
	RED("\u001B[31m"),
	GREEN("\u001B[32m"),
	YELLOW("\u001B[33m"),
	BLUE("\u001B[34m"),
	PURPLE("\u001B[35m"),
	CYAN("\u001B[36m"),
	WHITE("\u001B[37m");

	private final String command;

	ConsoleColor(final String command) {
		this.command = command;
	}

	public String getCommand() {
		return this.command;
	}

}
