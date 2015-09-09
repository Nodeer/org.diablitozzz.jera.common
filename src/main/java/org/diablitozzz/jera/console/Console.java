package org.diablitozzz.jera.console;

public class Console {

	private static final String ANSI_RESET = "\u001B[0m";;

	public synchronized Console out(final ConsoleColor color, final String text) {
		final StringBuilder out = new StringBuilder();
		out.append(color.getCommand());
		out.append(text);
		out.append(Console.ANSI_RESET);
		System.out.print(out.toString());
		return this;
	};

	public synchronized Console out(final String text) {
		System.out.print(text);
		return this;
	}
}
