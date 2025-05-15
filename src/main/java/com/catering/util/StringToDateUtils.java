package com.catering.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for converting string representations of dates to LocalDate objects.
 * <p>This class provides methods for converting specific string date formats into LocalDate.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringToDateUtils {

	static Logger logger = LoggerFactory.getLogger(StringToDateUtils.class);

	/**
	 * Converts a string representation of a date into a LocalDate object.
	 * <p>The input string is expected to be in the format "EEE MMM dd yyyy" (e.g., "Mon Jan 01 2025").</p>
	 * 
	 * @param date The string representation of the date to convert.
	 * @return A LocalDate object corresponding to the input string, or null if the conversion fails or the string is "null".
	 */
	public static LocalDate convertStringToDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
		// Check if the input is not the string "null"
		if (!date.equals("null")) {
			try {
				return dateFormat.parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}
		// Return null if input is "null" or parsing fails
		return null;
	}

}