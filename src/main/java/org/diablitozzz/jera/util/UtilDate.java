/*
 * Copyright (c) 2012, diablitozzz.org All rights reserved. Redistribution
 * and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met: * Redistributions
 * of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of diablitozzz.org nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.diablitozzz.jera.util;

import java.util.Calendar;
import java.util.Date;

import org.diablitozzz.jera.date.ISO8601;

public class UtilDate {

	public static int compare(final Calendar calendarA, final Calendar calendarB) {
		return calendarA.compareTo(calendarB);
	}

	public static int compare(final Calendar calendarA, final Date dateB) {
		final Calendar calendarB = UtilDate.createCalendar(dateB);
		return calendarA.compareTo(calendarB);
	}

	public static int compare(final Date dateA, final Calendar calendarB) {
		final Calendar calendarA = UtilDate.createCalendar(dateA);
		return calendarA.compareTo(calendarB);
	}

	public static int compare(final Date dateA, final Date dateB) {
		final Calendar calendarA = UtilDate.createCalendar(dateA);
		final Calendar calendarB = UtilDate.createCalendar(dateB);
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
		return ISO8601.format(calendar);
	}

	public static String toISO8601(final Date date) {

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return ISO8601.format(calendar);
	}

}
