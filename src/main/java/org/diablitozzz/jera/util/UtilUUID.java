package org.diablitozzz.jera.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;

public class UtilUUID {

	public static int generateInt() {
		return BigInteger.valueOf(UtilUUID.generateLong()).intValue();
	}

	public static long generateLong() {

		final long time = System.currentTimeMillis();
		final long nanoTime = System.nanoTime();
		final long freeMemory = Runtime.getRuntime().freeMemory();
		long addressHashCode;
		try {
			final InetAddress inetAddress = InetAddress.getLocalHost();
			addressHashCode = inetAddress.getHostName().hashCode() ^ inetAddress.getHostAddress().hashCode();
		} catch (final Exception err) {
			addressHashCode = 0;
		}

		//this.globalProcessID = time ^ nanoTime ^ freeMemory ^ addressHashCode;
		final Random random1 = new Random(time);
		final Random random2 = new Random(nanoTime);
		final Random random3 = new Random(addressHashCode ^ freeMemory);

		final long result = Math.abs(random1.nextLong() ^ random2.nextLong() ^ random3.nextLong());
		return result;
	}

	public static UUID generateNativeUUID() {
		return UUID.randomUUID();
	}

	public static UUID generateUUID() {

		final long mostSigBits = UtilUUID.generateLong();
		final long leastSigBits = UtilUUID.generateLong();
		return new UUID(mostSigBits, leastSigBits);
	}
}
