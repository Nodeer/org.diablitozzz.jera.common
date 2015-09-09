package org.diablitozzz.jera.ddd;

public class Check {

	public static boolean isContains(final Object needle, final Iterable<?> haystack) {

		for (final Object haystackItem : haystack) {
			if (haystackItem.equals(needle)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmpty(final Object value) {

		if (value == null) {
			return true;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue() == 0;
		}
		else if (value instanceof String) {
			return ((String) value)
					.trim()
					.isEmpty();
		}
		return false;
	}

	public static boolean isEquals(final Object valA, final Object valB) {

		if (valA == valB) {
			return true;
		}
		if (valA != null) {
			return valA.equals(valB);
		}
		return false;
	}

	public static boolean isNotEmpty(final Object value) {
		return !Check.isEmpty(value);
	}

}
