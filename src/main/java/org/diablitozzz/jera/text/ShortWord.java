package org.diablitozzz.jera.text;

public class ShortWord {

	private static enum State {
		BEGIN,
		LETTER,
		SPACE
	}

	public static String make(String str, int size, String sep) {

		if (str == null) {
			return null;
		}
		if (size < 1) {
			return "";
		}

		if (sep == null) {
			sep = "";
		}
		int length = str.length();
		if (length <= size) {
			return str;
		}
		int wordSize = 16;
		int pos = 0;
		StringBuilder buffer = new StringBuilder(wordSize);
		StringBuilder out = new StringBuilder(size + sep.length());
		State state = State.BEGIN;

		while (pos < size) {

			char chr = str.charAt(pos);
			boolean isSpace = (chr <= 47) || (chr >= 58 && chr <= 64) || (chr >= 91 && chr <= 96) || (chr >= 123 && chr <= 127);

			switch (state) {

				case BEGIN:
					state = isSpace ? State.SPACE : State.LETTER;
					break;
				case LETTER:
					if (isSpace) {
						out.append(buffer);
						buffer = new StringBuilder(wordSize);
						state = State.SPACE;
					}
					else {
						buffer.append(chr);
						pos++;
					}

					break;
				case SPACE:
					if (isSpace) {
						buffer.append(chr);
						pos++;
					}
					else {
						state = State.LETTER;
					}
					break;
				default:
					break;
			}
		}
		out.append(sep);
		return out.toString();
	}
}
