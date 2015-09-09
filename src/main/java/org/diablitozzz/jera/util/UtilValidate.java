package org.diablitozzz.jera.util;

import java.net.URL;

public class UtilValidate {

	private static final java.util.regex.Pattern emailValidationPattern = java.util.regex.Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");

	public static boolean isValidEmailAddress(final String email) {
		return UtilValidate.emailValidationPattern.matcher(email).matches();
	}

	public static boolean isValidHost(final String host) {
		try {
			@SuppressWarnings("unused")
			final URL url = new URL(host);
		} catch (final Throwable e) {
			return false;
		}
		return true;
	}
}
