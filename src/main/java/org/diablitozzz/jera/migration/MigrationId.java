package org.diablitozzz.jera.migration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class MigrationId {

	public static MigrationId createFromDate(final Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return new MigrationId(calendar);

	}

	/**
	 * yyyy_MM_dd__HH_mm_ss
	 */
	public static MigrationId createFromString(final String date) throws MigrationException {
		final Calendar calendar = Calendar.getInstance();
		try {
			final SimpleDateFormat format = new SimpleDateFormat(MigrationId.PATTERN_DATE_TIME);
			calendar.setTime(format.parse(date));
		} catch (final Throwable e) {
			throw new MigrationException(e);
		}
		return new MigrationId(calendar);
	}

	public static MigrationId createNow() {
		return new MigrationId(Calendar.getInstance());
	}

	static boolean isValidDate(final String date) {
		if (date == null) {
			return false;
		}
		if (date.isEmpty()) {
			return false;
		}
		try {
			if (!MigrationId.PATTERN_DATE_TIME_MATCH.matcher(date).matches()) {
				return false;
			}
			final SimpleDateFormat format = new SimpleDateFormat(MigrationId.PATTERN_DATE_TIME);
			final Date dateTmp = format.parse(date);
			return format.format(dateTmp).equals(date);
		} catch (final Throwable e) {
			return false;
		}
	}

	private static final Pattern PATTERN_DATE_TIME_MATCH = Pattern.compile("^\\d{4}_\\d{2}_\\d{2}__\\d{2}_\\d{2}_\\d{2}$");
	private static final String PATTERN_DATE_TIME = "yyyy_MM_dd__HH_mm_ss";
	private static final String PATTERN_DAY = "yyyy_MM";

	private final Calendar calendar;

	private MigrationId(final Calendar calendar) {
		this.calendar = calendar;
	}

	public int compareTo(final MigrationId another) {
		return this.calendar.compareTo(another.calendar);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final MigrationId other = (MigrationId) obj;
		return this.calendar.equals(other.calendar);
	}

	@Override
	public int hashCode() {
		return this.calendar.hashCode();
	}

	public String toDateTime() {
		final SimpleDateFormat format = new SimpleDateFormat(MigrationId.PATTERN_DATE_TIME);
		return format.format(this.calendar.getTime());
	}

	public String toDay() {
		final SimpleDateFormat format = new SimpleDateFormat(MigrationId.PATTERN_DAY);
		return format.format(this.calendar.getTime());
	}

	@Override
	public String toString() {
		return this.toDateTime();
	}

	public Date toTime() {
		return this.calendar.getTime();
	}
}
