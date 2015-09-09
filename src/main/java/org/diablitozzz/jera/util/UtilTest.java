package org.diablitozzz.jera.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class UtilTest {

	private static Random random = new Random();

	public static void assertEqualsAny(final Iterable<?> one, final Iterable<?> two) {

		if (!UtilTest.equalsAny(one, two)) {
			throw new AssertionError("Is not equals");
		}
	}

	public static void assertEqualsByteArray(final byte[] one, final byte[] two) {
		if (one == two) {
			return;
		}
		if (one == null || two == null) {
			throw new AssertionError("Arrays is not equals");
		}
		if (one.length != two.length) {
			throw new AssertionError("Arrays is not equals");
		}
		for (int i = 0; i < one.length; i++) {
			if (one[i] != two[i]) {
				throw new AssertionError("Arrays is not equals");
			}
		}
	}

	public static void assertIn(final Object needle, final Iterable<?> haystack) {

		if (!UtilTest.in(needle, haystack)) {
			throw new AssertionError("Объект " + needle.toString() + " не содержится в списке " + haystack.toString());
		}
	}

	public static void assertNotEqualsAny(final Iterable<?> one, final Iterable<?> two) {

		if (UtilTest.equalsAny(one, two)) {
			throw new AssertionError("Списки  эквиваленты");
		}
	}

	public static void assertNotIn(final Object needle, final Iterable<?> haystack) {

		if (UtilTest.in(needle, haystack)) {
			throw new AssertionError("Объект " + needle.toString() + " содержится в списке " + haystack.toString());
		}
	}

	public static boolean equalsAny(final Iterable<?> one, final Iterable<?> two) {

		//проверяем на null
		if (one == null && two != null) {
			return false;
		}
		else if (one == null && two == null) {
			return true;
		}
		else if (one != null && two == null) {
			return false;
		}

		//переносим в list  и делаем копии
		final List<Object> oneList = new LinkedList<Object>();
		for (final Object oneItem : one) {
			oneList.add(oneItem);
		}
		final List<Object> twoList = new LinkedList<Object>();
		for (final Object twoItem : two) {
			twoList.add(twoItem);
		}

		//проверяем размеры
		if (oneList.size() != twoList.size()) {
			return false;
		}

		//проверяем содержание
		for (final Object oneItem : oneList) {
			boolean flag = false;

			for (final Object twoItem : twoList) {

				if (oneItem != null && oneItem.equals(twoItem)) {
					flag = true;
					break;
				}
			}

			if (!flag) {
				return false;
			}
		}

		return true;
	}

	public static byte[] getRandByteArray() {
		return UtilTest.getRandString().getBytes();
	}

	public static Date getRandDate()
	{
		return new Date(System.currentTimeMillis());
	}

	public static double getRandDouble() {
		return UtilTest.getRandLong() / 1000d;
	}

	public static int getRandInteger() {

		return (int) UtilTest.getRandLong();
	}

	public static long getRandLong() {
		return Math.abs(UtilTest.random.nextLong()) % System.nanoTime();
	}

	public static String getRandString() {
		return "rand." + UtilTest.getRandLong();
	}

	public static boolean in(final Object needle, final Iterable<?> haystack) {

		for (final Object haystackItem : haystack) {
			if (haystackItem.equals(needle)) {
				return true;
			}
		}
		return false;
	}
}
