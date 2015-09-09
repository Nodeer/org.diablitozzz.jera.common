package org.diablitozzz.jera.json;

import java.util.Calendar;
import java.util.Date;

public class JsonUtilDate {
    
    public static int compare(final Calendar calendarA, final Calendar calendarB) {
        return calendarA.compareTo(calendarB);
    }
    
    public static int compare(final Calendar calendarA, final Date dateB) {
        final Calendar calendarB = JsonUtilDate.createCalendar(dateB);
        return calendarA.compareTo(calendarB);
    }
    
    public static int compare(final Date dateA, final Calendar calendarB) {
        final Calendar calendarA = JsonUtilDate.createCalendar(dateA);
        return calendarA.compareTo(calendarB);
    }
    
    public static int compare(final Date dateA, final Date dateB) {
        final Calendar calendarA = JsonUtilDate.createCalendar(dateA);
        final Calendar calendarB = JsonUtilDate.createCalendar(dateB);
        return calendarA.compareTo(calendarB);
    }
    
    public static Calendar createCalendar(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    
    /**
     * Возвращает текущую дату
     */
    public static Date NOW() {
        return Calendar.getInstance().getTime();
    }
    
    public static String toISO8601(final Calendar calendar) {
        return JsonDateISO8601.format(calendar);
    }
    
    public static String toISO8601(final Date date) {
        
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return JsonDateISO8601.format(calendar);
    }
    
}
