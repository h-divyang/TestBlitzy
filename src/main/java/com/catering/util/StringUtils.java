package com.catering.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Provide utilities for the String
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

	/**
	 * Provide String with the normal word from Camel Case text
	 * 
	 * @param camelCase text
	 * @return {@link String} from Camel Case
	 * <pre>
	 * StringUtils.splitCamelCase(null)     = <code>null</code>
	 * StringUtils.splitCamelCase("")           = ""
	 * StringUtils.splitCamelCase(" ")          = ""
	 * StringUtils.splitCamelCase("camelCase")  = "Camel Case"
	 * </pre> 
	 * */
	public static String splitCamelCase(String camelCase) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(camelCase)) {
			return org.apache.commons.lang3.StringUtils.capitalize(camelCase.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
					"(?<=[A-Za-z])(?=[^A-Za-z])"), " "));
		}
		return camelCase;
	}

	/**
	 * Joins multiple strings with a dot separator.
	 * 
	 * @param keys The strings to be joined.
	 * @return A string formed by joining the input strings with dots.
	 * <pre>
	 * StringUtils.dotSeparated("a", "b", "c") = "a.b.c"
	 * </pre>
	 */
	public static String dotSeparated(String... keys) {
		return String.join(".", keys);
	}

	/**
	 * Converts a date in the format "MM/DD/YYYY" into "YYYY-MM-DD" for database compatibility.
	 * <p>This method ensures that the day and month are always two digits, even if the input has single-digit days or months.</p>
	 * 
	 * @param query The date string to be formatted.
	 * @return A string representing the date in "YYYY-MM-DD" format.
	 * <pre>
	 * StringUtils.getDateInDatabaseFormat("1/3/2025") = "2025-03-01"
	 * </pre>
	 */
	public static String getDateInDatabaseFormat(String query) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(query)) {
			List<String> dateList = Arrays.asList(org.apache.commons.lang3.StringUtils.split(query, "/"));
			dateList.forEach(date -> {
				if (date.length() == 1 && NumberUtils.isDigits(date) && Integer.parseInt(date) > 0) {
					dateList.set(dateList.indexOf(date), String.format("%02d", Integer.parseInt(date)));
				}
			});
			Collections.reverse(dateList);
			return String.join("-", dateList);
		}
		return query;
	}

	/**
	 * Converts a given 12-hour formatted time string to its UTC equivalent.
	 *
	 * This method normalizes the input by removing any extra spaces around "AM" or "PM"
	 * and converts the text to lowercase for uniform processing. It validates whether the
	 * input matches a 12-hour time format (e.g., "1:30pm", "12:45 AM", "8am") and parses it
	 * into a {@link LocalTime} object. If the input is valid, the time is converted to UTC.
	 *
	 * @param query The 12-hour formatted time string to be converted to UTC.
	 * @param timeZone The time zone ID for the input time.
	 * @return The UTC equivalent of the provided time string in "HH:mm" format,
	 *		   or {@code null} if the input format is invalid.
	 * @throws DateTimeParseException If the input cannot be parsed to a valid time.
	 */
	public static String convert12HourTimeToUTC(String query, String timeZone) {
		try {
			// Normalize the input by removing any space before AM/PM & set text to lower case
			query = query.trim().replaceAll("\\s*(AM|PM|am|pm)\\s*", "$1").toLowerCase();
			// Check if the query matches a valid 12-hour time format
			if (query.matches("^\\d{1,2}:\\d{1,2}(am|pm)$") || query.matches("^\\d{1,2}(am|pm)$")) {
				String hourPart = query.replaceAll("[^0-9:]", ""); // Extract the numeric part
				String amPmPart = query.replaceAll("[^a-z]", ""); // Extract AM/PM part
				// If the hour contains minutes, handle them separately
				String[] timeParts = hourPart.split(":");
				int hour = Integer.parseInt(timeParts[0]);
				int minute = timeParts.length > 1 ? Integer.parseInt(timeParts[1]) : 0;
				// Format to a valid 12-hour time string (e.g., '01:01 AM' or '12:30 PM')
				query = String.format("%02d:%02d %s", hour, minute, amPmPart);
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
			LocalTime time = LocalTime.parse(query, formatter);
			return convertToUTC(time, timeZone);
		} catch (DateTimeParseException e) {
			// Handle invalid 12-hour time format
			return null;
		}
	}

	/**
	 * Converts a given 24-hour formatted time string to its UTC equivalent.
	 *
	 * This method takes a time string in 24-hour format (e.g., "14:30", "8") and converts 
	 * it to UTC. If the input is a single hour without minutes (e.g., "8"), it formats the 
	 * input by appending ":00" to represent the top of the hour (e.g., "08:00"). The method 
	 * returns the UTC equivalent of the provided time string in "HH:mm" format.
	 *
	 * @param query The 24-hour formatted time string to be converted to UTC.
	 * @param timeZone The time zone ID for the input time.
	 * @return The UTC equivalent of the provided time string in "HH:mm" format,
	 *		   or {@code null} if the input format is invalid.
	 */
	public static String convert24HourTimeToUTC(String query, String timeZone) {
		try {
			LocalTime time = LocalTime.parse(query);
			return convertToUTC(time, timeZone);
		} catch (Exception e) {
			if (query.matches("^\\d{1,2}$")) {
				String formattedQuery = String.format("%02d", Integer.parseInt(query)) + ":00";
				LocalTime time = LocalTime.parse(formattedQuery);
				return convertToUTC(time, timeZone);
			} else {
				return null;
			}
		}
	}

	/**
	 * Converts the provided local time in a specified time zone to UTC time and returns the formatted UTC time.
	 *
	 * This method takes a {@link LocalTime} object representing a time in a given time zone, 
	 * combines it with the current date, and converts it to UTC. The output is returned as a 
	 * string formatted in "HH:mm" (24-hour) format.
	 *
	 * @param localTime The {@link LocalTime} object representing the time in the provided time zone.
	 * @param timeZone The time zone ID for the input time.
	 * @return A string representing the equivalent UTC time in "HH:mm" format.
	 */
	public static String convertToUTC(LocalTime localTime, String timeZone) {
		ZoneId timeZoneId = ZoneId.of(timeZone);
		// Combine the current date with the local time in the specified time zone
		ZonedDateTime specifiedZoneDateTime = ZonedDateTime.of(LocalDate.now(), localTime, timeZoneId);
		// Convert local time to UTC time
		ZonedDateTime utcDateTime = specifiedZoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));
		// Format the UTC time in "HH:mm" format and return
		return utcDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
	}

}