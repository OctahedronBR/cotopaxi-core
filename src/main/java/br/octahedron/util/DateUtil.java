/*
 *  This file is part of Cotopaxi.
 *
 *  Cotopaxi is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  Cotopaxi is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the Lesser GNU General Public License
 *  along with Cotopaxi. If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.util;

import static br.octahedron.cotopaxi.CotopaxiProperty.TIMEZONE;
import static br.octahedron.cotopaxi.CotopaxiProperty.property;
import static java.lang.Integer.parseInt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Classe utilitaria para lidar com datas
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class DateUtil {

	private DateUtil() {
		// static class
	}

	/**
	 * Short date format: dd/MM/yy
	 */
	public static final String SHORT = "dd/MM/yy";

	/**
	 * Long date format = "dd/MM/yy HH:mm:ss"
	 */
	public static final String LONG = "dd/MM/yy HH:mm:ss";

	private static Map<String, SimpleDateFormat> formatters = new HashMap<String, SimpleDateFormat>();
	/**
	 * The default date format to be used to parse/format dates. Initially set to
	 * {@link DateUtil#LONG}
	 */
	public static String defaultFormat = LONG;
	private static boolean loaded = false;
	private static TimeZone defaultTz = null;

	/**
	 * 
	 */
	private static void load() {
		String offset = property(TIMEZONE);
		if (offset != null) {
			int offsetMillis = parseInt(offset) * 36 * 1000;
			TimeZone systemTz = new SimpleTimeZone(offsetMillis, "Cotopaxi/System");
			defaultTimeZone(systemTz);
		}
		loaded = true;
	}

	/**
	 * Gets the default {@link TimeZone} to be used to format / parse
	 * 
	 * @return the default to be used to format / parse or <code>null</code> if it's using JVM
	 *         default timezone
	 */
	public static TimeZone defaultTimeZone() {
		if (!loaded) {
			load();
		}
		return defaultTz;
	}

	/**
	 * Sets the default {@link TimeZone} to be used to format / parse.
	 * 
	 * @param tz
	 *            The default {@link TimeZone} to be set
	 * 
	 * @throw {@link NullPointerException} if tz was null.
	 */
	public static void defaultTimeZone(TimeZone tz) {
		if (tz != null) {
			defaultTz = tz;
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Gets the default date format to be used to parse/format dates.
	 * 
	 * @return The default date format to be used to parse/format dates.
	 * 
	 */
	public static DateFormat defaultDateFormat() {
		return dateFormat(defaultFormat);
	}

	/**
	 * Sets the default date format to be used to parse/format dates.
	 * 
	 * @param dateFormat
	 *            The default date format to be used to parse/format dates.
	 * 
	 * @see SimpleDateFormat
	 */
	public static void defaultDateFormat(String dateFormat) {
		defaultFormat = dateFormat;
	}

	public static DateFormat dateFormat(String dateFormat) {
		return dateFormat(dateFormat, defaultTimeZone());
	}

	public static DateFormat dateFormat(String dateFormat, TimeZone timeZone) {
		DateFormat formatter = formatters.get(dateFormat);
		if (formatter == null) {
			formatter = formatters.put(dateFormat, new SimpleDateFormat(dateFormat));
		}
		if (timeZone != null) {
			formatter.setTimeZone(timeZone);
		}
		return formatter;
	}

	/**
	 * Formats the given date using the default DateFormat
	 * 
	 * @param date
	 *            The date to be formatted.
	 * @return The formatted date as String.
	 */
	public static String format(Date date) {
		return format(date, defaultFormat);
	}

	/**
	 * Formats the given date to the given date format.
	 * 
	 * @param date
	 *            The date to be formatted.
	 * @param format
	 *            The date format.
	 * @return The formatted date as String.
	 * 
	 * @see SimpleDateFormat
	 */
	public static String format(Date date, String format) {
		return format(date, format, defaultTimeZone());
	}

	/**
	 * Formats the given date to the given date format.
	 * 
	 * @param date
	 *            The date to be formatted.
	 * @param dateFormat
	 *            The date format.
	 * @param timeZone
	 *            The {@link TimeZone} to be used to format date.
	 * @return The formatted date as String.
	 * 
	 * @see SimpleDateFormat
	 */
	public static String format(Date date, String dateFormat, TimeZone timeZone) {
		DateFormat formatter = dateFormat(dateFormat, timeZone);
		return formatter.format(date);
	}

	/**
	 * Parses the given String to the given date format.
	 * 
	 * @param date
	 *            The date to be parsed, as String.
	 * @return The parsed date.
	 */
	public static Date parse(String date) {
		return parse(date, LONG);
	}

	/**
	 * Parses the given String to the given date format.
	 * 
	 * @param date
	 *            The date to be parsed, as String.
	 * @param format
	 *            The date format.
	 * @return The parsed date.
	 * 
	 * @see SimpleDateFormat
	 */
	public static Date parse(String date, String format) {
		return parse(date, format, defaultTz);
	}

	/**
	 * Parses the given String to the given date format.
	 * 
	 * @param date
	 *            The date to be parsed, as String.
	 * @param dateFormat
	 *            The date format.
	 * 
	 * @param timeZone
	 *            The {@link TimeZone} to be used to parse date
	 * @return The parsed date.
	 * 
	 * @see SimpleDateFormat
	 */
	public static Date parse(String date, String dateFormat, TimeZone timeZone) {
		try {
			DateFormat formatter = dateFormat(dateFormat, timeZone);
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the current {@link Date}
	 * 
	 * @return the current {@link Date}
	 */
	public static Date date() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * Gets the current {@link Date}
	 * 
	 * @return the current {@link Date}
	 */
	public static long now() {
		return date().getTime();
	}

	/**
	 * Returns a {@link Date} with the first day of the current month.
	 */
	public static Date firstDateOfCurrentMonth() {
		return parse("01/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR), SHORT);
	}

	/**
	 * Gets a future date, based on a given number of days.
	 * 
	 * @param numberOfDays
	 *            The number of days, since now, for the future date.
	 * @return the future date.
	 */
	public static Date futureDate(int numberOfDays) {
		if (numberOfDays > 0) {
			// convert number of days to milliseconds
			long millis = (long) numberOfDays * 24 * 60 * 1000;
			Date future = (Date) date().clone();
			future.setTime(future.getTime() + millis);
			return future;
		} else {
			return date();
		}
	}

	/**
	 * Gets the current {@link Date}. This method is deprecated, you should use
	 * {@link DateUtil#now()} instead.
	 * 
	 * @return the current {@link Date}
	 */
	@Deprecated
	public static Date getDate() {
		return date();
	}

	/**
	 * Returns a {@link Date} with the first day of the current month.
	 * 
	 * @see DateUtil#firstDateOfCurrentMonth()
	 */
	@Deprecated
	public static Date getFirstDateOfCurrentMonth() {
		return firstDateOfCurrentMonth();
	}

	/**
	 * Gets a future date, based on a given number of days.
	 * 
	 * @param numberOfDays
	 *            The number of days, since now, for the future date.
	 * @return the future date.
	 * 
	 * @see DateUtil#futureDate(int)
	 */
	@Deprecated
	public static Date getFutureDate(int numberOfDays) {
		return futureDate(numberOfDays);
	}

}
