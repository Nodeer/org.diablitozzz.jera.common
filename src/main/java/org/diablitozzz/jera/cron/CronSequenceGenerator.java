
package org.diablitozzz.jera.cron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * second, minute, hour, day, month, weekday
 *
 * Month and weekday names can begiven as the first three letters of the English names.
 *
 *
 *
 *
 * Date sequence generator for a <a href="http://www.manpagez.com/man/5/crontab/">Crontab pattern</a>,
 * allowing clients to specify a pattern that the sequence matches.
 *
 * <p>The pattern is a list of six single space-separated fields: representing
 * second, minute, hour, day, month, weekday. Month and weekday names can be
 * given as the first three letters of the English names.
 *
 * <p>Example patterns:
 * <ul>
 * <li>"0 0 * * * *" = the top of every hour of every day.</li>
 * <li>"*&#47;10 * * * * *" = every ten seconds.</li>
 * <li>"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.</li>
 * <li>"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.</li>
 * <li>"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays</li>
 * <li>"0 0 0 25 12 ?" = every Christmas Day at midnight</li>
 * </ul>
 */
public class CronSequenceGenerator {
    
    public static void validateExpression(final String expression) throws IllegalArgumentException {
        try {
            
            @SuppressWarnings("unused")
            final CronSequenceGenerator gen = new CronSequenceGenerator(expression, TimeZone.getDefault());
        } catch (final Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
    
    private final BitSet seconds = new BitSet(60);
    
    private final BitSet minutes = new BitSet(60);
    
    private final BitSet hours = new BitSet(24);
    
    private final BitSet daysOfWeek = new BitSet(7);
    
    private final BitSet daysOfMonth = new BitSet(31);
    
    private final BitSet months = new BitSet(12);
    
    private final String expression;
    
    private final TimeZone timeZone;
    
    /**
     * Construct a {@link CronSequenceGenerator} from the pattern provided.
     * @param expression a space-separated list of time fields
     * @param timeZone the TimeZone to use for generated trigger times
     * @throws IllegalArgumentException if the pattern cannot be parsed
     */
    public CronSequenceGenerator(final String expression, final TimeZone timeZone) {
        this.expression = expression;
        this.timeZone = timeZone;
        this.parse(expression);
    }
    
    private void doNext(final Calendar calendar, final int dot) {
        final List<Integer> resets = new ArrayList<Integer>();
        
        final int second = calendar.get(Calendar.SECOND);
        final List<Integer> emptyList = Collections.emptyList();
        final int updateSecond = this.findNext(this.seconds, second, calendar, Calendar.SECOND, Calendar.MINUTE, emptyList);
        if (second == updateSecond) {
            resets.add(Calendar.SECOND);
        }
        
        final int minute = calendar.get(Calendar.MINUTE);
        final int updateMinute = this.findNext(this.minutes, minute, calendar, Calendar.MINUTE, Calendar.HOUR_OF_DAY, resets);
        if (minute == updateMinute) {
            resets.add(Calendar.MINUTE);
        } else {
            this.doNext(calendar, dot);
        }
        
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int updateHour = this.findNext(this.hours, hour, calendar, Calendar.HOUR_OF_DAY, Calendar.DAY_OF_WEEK, resets);
        if (hour == updateHour) {
            resets.add(Calendar.HOUR_OF_DAY);
        } else {
            this.doNext(calendar, dot);
        }
        
        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final int updateDayOfMonth = this.findNextDay(calendar, this.daysOfMonth, dayOfMonth, this.daysOfWeek, dayOfWeek, resets);
        if (dayOfMonth == updateDayOfMonth) {
            resets.add(Calendar.DAY_OF_MONTH);
        } else {
            this.doNext(calendar, dot);
        }
        
        final int month = calendar.get(Calendar.MONTH);
        final int updateMonth = this.findNext(this.months, month, calendar, Calendar.MONTH, Calendar.YEAR, resets);
        if (month != updateMonth) {
            if (calendar.get(Calendar.YEAR) - dot > 4) {
                throw new IllegalStateException("Invalid cron expression led to runaway search for next trigger");
            }
            this.doNext(calendar, dot);
        }
        
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof CronSequenceGenerator)) {
            return false;
        }
        final CronSequenceGenerator cron = (CronSequenceGenerator) obj;
        return cron.months.equals(this.months) && cron.daysOfMonth.equals(this.daysOfMonth)
                && cron.daysOfWeek.equals(this.daysOfWeek) && cron.hours.equals(this.hours)
                && cron.minutes.equals(this.minutes) && cron.seconds.equals(this.seconds);
    }
    
    /**
     * Search the bits provided for the next set bit after the value provided,
     * and reset the calendar.
     * @param bits a {@link BitSet} representing the allowed values of the field
     * @param value the current value of the field
     * @param calendar the calendar to increment as we move through the bits
     * @param field the field to increment in the calendar (@see
     * {@link Calendar} for the static constants defining valid fields)
     * @param lowerOrders the Calendar field ids that should be reset (i.e. the
     * ones of lower significance than the field of interest)
     * @return the value of the calendar field that is next in the sequence
     */
    private int findNext(final BitSet bits, final int value, final Calendar calendar, final int field, final int nextField, final List<Integer> lowerOrders) {
        int nextValue = bits.nextSetBit(value);
        // roll over if needed
        if (nextValue == -1) {
            calendar.add(nextField, 1);
            this.reset(calendar, Arrays.asList(field));
            nextValue = bits.nextSetBit(0);
        }
        if (nextValue != value) {
            calendar.set(field, nextValue);
            this.reset(calendar, lowerOrders);
        }
        return nextValue;
    }
    
    private int findNextDay(final Calendar calendar, final BitSet daysOfMonth, final int dayOfMonth, final BitSet daysOfWeek, final int dayOfWeek, final List<Integer> resets) {
        
        int dayOfMonthCur = dayOfMonth;
        int dayOfWeekCur = dayOfWeek;
        
        int count = 0;
        final int max = 366;
        // the DAY_OF_WEEK values in java.util.Calendar start with 1 (Sunday),
        // but in the cron pattern, they start with 0, so we subtract 1 here
        while ((!daysOfMonth.get(dayOfMonthCur) || !daysOfWeek.get(dayOfWeekCur - 1)) && count++ < max) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dayOfMonthCur = calendar.get(Calendar.DAY_OF_MONTH);
            dayOfWeekCur = calendar.get(Calendar.DAY_OF_WEEK);
            this.reset(calendar, resets);
        }
        if (count >= max) {
            throw new IllegalStateException("Overflow in day for expression=" + this.expression);
        }
        return dayOfMonthCur;
    }
    
    // Parsing logic invoked by the constructor.
    
    private int[] getRange(final String field, final int min, final int max) {
        final int[] result = new int[2];
        if (field.contains("*")) {
            result[0] = min;
            result[1] = max - 1;
            return result;
        }
        if (!field.contains("-")) {
            result[0] = result[1] = Integer.valueOf(field);
        } else {
            final String[] split = CronUtils.delimitedListToStringArray(field, "-");
            if (split.length > 2) {
                throw new IllegalArgumentException("Range has more than two fields: " + field);
            }
            result[0] = Integer.valueOf(split[0]);
            result[1] = Integer.valueOf(split[1]);
        }
        if (result[0] >= max || result[1] >= max) {
            throw new IllegalArgumentException("Range exceeds maximum (" + max + "): " + field);
        }
        if (result[0] < min || result[1] < min) {
            throw new IllegalArgumentException("Range less than minimum (" + min + "): " + field);
        }
        return result;
    }
    
    @Override
    public int hashCode() {
        return 37 + 17 * this.months.hashCode() + 29 * this.daysOfMonth.hashCode() + 37 * this.daysOfWeek.hashCode()
                + 41 * this.hours.hashCode() + 53 * this.minutes.hashCode() + 61 * this.seconds.hashCode();
    }
    
    public Calendar next(final Calendar calendar) {
        final Calendar calendarCur = (Calendar) calendar.clone();
        // Truncate to the next whole second
        calendarCur.add(Calendar.SECOND, 1);
        calendarCur.set(Calendar.MILLISECOND, 0);
        
        this.doNext(calendarCur, calendarCur.get(Calendar.YEAR));
        return calendarCur;
    }
    
    /**
     * Get the next {@link Date} in the sequence matching the Cron pattern and
     * after the value provided. The return value will have a whole number of
     * seconds, and will be after the input value.
     * @param date a seed value
     * @return the next value matching the pattern
     */
    public Date next(final Date date) {
        /*
        The plan:
        
        1 Round up to the next whole second
        
        2 If seconds match move on, otherwise find the next match:
        2.1 If next match is in the next minute then roll forwards
        
        3 If minute matches move on, otherwise find the next match
        3.1 If next match is in the next hour then roll forwards
        3.2 Reset the seconds and go to 2
        
        4 If hour matches move on, otherwise find the next match
        4.1 If next match is in the next day then roll forwards,
        4.2 Reset the minutes and seconds and go to 2

        ...
        */
        
        final Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(this.timeZone);
        calendar.setTime(date);
        
        // Truncate to the next whole second
        calendar.add(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        
        this.doNext(calendar, calendar.get(Calendar.YEAR));
        
        return calendar.getTime();
    }
    
    /**
     * Parse the given pattern expression.
     */
    private void parse(final String expression) throws IllegalArgumentException {
        final String[] fields = CronUtils.tokenizeToStringArray(expression, " ");
        if (fields.length != 6) {
            throw new IllegalArgumentException(String.format(""
                    + "cron expression must consist of 6 fields (found %d in %s)", fields.length, expression));
        }
        this.setNumberHits(this.seconds, fields[0], 0, 60);
        this.setNumberHits(this.minutes, fields[1], 0, 60);
        this.setNumberHits(this.hours, fields[2], 0, 24);
        this.setDaysOfMonth(this.daysOfMonth, fields[3]);
        this.setMonths(this.months, fields[4]);
        this.setDays(this.daysOfWeek, this.replaceOrdinals(fields[5], "SUN,MON,TUE,WED,THU,FRI,SAT"), 8);
        if (this.daysOfWeek.get(7)) {
            // Sunday can be represented as 0 or 7
            this.daysOfWeek.set(0);
            this.daysOfWeek.clear(7);
        }
    }
    
    /**
     * Replace the values in the commaSeparatedList (case insensitive) with
     * their index in the list.
     * @return a new string with the values from the list replaced
     */
    private String replaceOrdinals(final String value, final String commaSeparatedList) {
        
        String valueCur = value;
        final String[] list = CronUtils.commaDelimitedListToStringArray(commaSeparatedList);
        for (int i = 0; i < list.length; i++) {
            final String item = list[i].toUpperCase();
            valueCur = CronUtils.replace(valueCur.toUpperCase(), item, "" + i);
        }
        return valueCur;
    }
    
    /**
     * Reset the calendar setting all the fields provided to zero.
     */
    private void reset(final Calendar calendar, final List<Integer> fields) {
        for (final int field : fields) {
            calendar.set(field, field == Calendar.DAY_OF_MONTH ? 1 : 0);
        }
    }
    
    private void setDays(final BitSet bits, final String field, final int max) {
        
        String fieldCur = field;
        if (fieldCur.contains("?")) {
            fieldCur = "*";
        }
        this.setNumberHits(bits, fieldCur, 0, max);
    }
    
    private void setDaysOfMonth(final BitSet bits, final String field) {
        final int max = 31;
        // Days of month start with 1 (in Cron and Calendar) so add one
        this.setDays(bits, field, max + 1);
        // ... and remove it from the front
        bits.clear(0);
    }
    
    private void setMonths(final BitSet bits, final String value) {
        
        String valueCur = value;
        final int max = 12;
        valueCur = this.replaceOrdinals(valueCur, "FOO,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC");
        final BitSet months = new BitSet(13);
        // Months start with 1 in Cron and 0 in Calendar, so push the values first into a longer bit set
        this.setNumberHits(months, valueCur, 1, max + 1);
        // ... and then rotate it to the front of the months
        for (int i = 1; i <= max; i++) {
            if (months.get(i)) {
                bits.set(i - 1);
            }
        }
    }
    
    private void setNumberHits(final BitSet bits, final String value, final int min, final int max) {
        final String[] fields = CronUtils.delimitedListToStringArray(value, ",");
        for (final String field : fields) {
            if (!field.contains("/")) {
                // Not an incrementer so it must be a range (possibly empty)
                final int[] range = this.getRange(field, min, max);
                bits.set(range[0], range[1] + 1);
            } else {
                final String[] split = CronUtils.delimitedListToStringArray(field, "/");
                if (split.length > 2) {
                    throw new IllegalArgumentException("Incrementer has more than two fields: " + field);
                }
                final int[] range = this.getRange(split[0], min, max);
                if (!split[0].contains("-")) {
                    range[1] = max - 1;
                }
                final int delta = Integer.valueOf(split[1]);
                for (int i = range[0]; i <= range[1]; i += delta) {
                    bits.set(i);
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + this.expression;
    }
    
}
