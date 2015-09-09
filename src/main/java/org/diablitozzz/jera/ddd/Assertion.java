package org.diablitozzz.jera.ddd;

public class Assertion {

	public static void isContains(final Object needle, final Iterable<?> haystack) {

		final String message = "Объект " + needle.toString() + " не содержится в списке " + haystack.toString();
		Assertion.isContains(needle, haystack, message);
	}

	public static void isContains(final Object needle, final Iterable<?> haystack, final String message) {

		if (!Check.isContains(needle, haystack)) {
			throw new AssertionError(message);
		}
	}

	public static void isEquals(final Object valA, final Object valB) {

		Assertion.isEquals(valA, valB, "values are not equals");
	}

	public static void isEquals(final Object valA, final Object valB, final String message) {

		if (!Check.isEquals(valA, valB)) {
			throw new AssertionError(message);
		}
	}

	public static void isFalse(final boolean condition) {

		Assertion.isFalse(condition, "condition is not false");
	}

	public static void isFalse(final boolean condition, final String message) {

		if (condition) {
			throw new AssertionError(message);
		}
	}

	public static void isNull(final Object object) {

		Assertion.isNull(object, "object is not null");
	}

	public static void isNull(final Object object, final String message) {

		if (object != null) {
			throw new AssertionError(message);
		}
	}

	public static void isTrue(final boolean condition) {

		Assertion.isTrue(condition, "condition is not true");
	}

	public static void isTrue(final boolean condition, final String message) {

		if (!condition) {
			throw new AssertionError(message);
		}
	}

	public static void notEmpty(final String value) {

		Assertion.notEmpty(value, "object is null");
	}

	public static void notEmpty(final String value, final String message) {

		if (!Check.isEmpty(value)) {
			throw new AssertionError(message);
		}

	}

	public static void notEquals(final Object valA, final Object valB) {

		Assertion.notEquals(valA, valB, "values are equals");
	}

	public static void notEquals(final Object valA, final Object valB, final String message) {

		if (Check.isEquals(valA, valB)) {
			throw new AssertionError(message);
		}
	}

	public static void notNull(final Object object) {

		Assertion.notNull(object, "object is null");
	}

	public static void notNull(final Object object, final String message) {

		if (object == null) {
			throw new AssertionError(message);
		}
	}

}
