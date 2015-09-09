package org.diablitozzz.jera.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UtilClass {

	public static boolean classExists(final String className) {

		try {
			Class.forName(className, false, Thread.currentThread().getContextClassLoader());
		} catch (final ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> findBeanConsructor(final Class<T> clazz) {

		final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		for (final Constructor<?> cons : constructors) {

			if (cons.getGenericParameterTypes().length == 0) {
				return (Constructor<T>) cons;
			}
		}
		return null;
	}

	public static Class<?> getClassByName(final String className) throws ClassNotFoundException {

		return Class.forName(className, false, Thread.currentThread().getContextClassLoader());
	}

	public static boolean isInstanceOf(final Class<?> typeA, final Class<?> typeB) {
		return typeB.isAssignableFrom(typeA);
	}

	public static <T> T newInstance(final Class<T> cl) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		// find constructor without parametrs
		final Constructor<T> constructor = UtilClass.findBeanConsructor(cl);

		if (constructor == null) {
			return cl.newInstance();
		}
		constructor.setAccessible(true);
		return constructor.newInstance();
	}

}