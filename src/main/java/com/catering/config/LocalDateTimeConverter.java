package com.catering.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A custom converter that converts a string representation of a date-time into a {@link LocalDateTime} object.
 * <p>
 * This class implements the {@link Converter} interface from Spring to provide a conversion mechanism
 * between a string and a {@link LocalDateTime} object. The string is expected to be in ISO 8601 format
 * (e.g., "2025-01-01T10:15:30Z").
 * </p>
 * <p>
 * The conversion process attempts to parse the string as an {@link Instant} and then converts it to a
 * {@link LocalDateTime} object using the system's default time zone. If parsing fails or the input string is blank,
 * the method returns <code>null</code>.
 * </p>
 * 
 * @see Converter
 * @see LocalDateTime
 * @see Instant
 * @see ZoneId
 */
@Component
public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

	/**
	 * Converts a string representation of a date-time into a {@link LocalDateTime} object.
	 * <p>
	 * This method parses the given string as an {@link Instant} and then converts it to a
	 * {@link LocalDateTime} using the system's default time zone. If the input string is blank
	 * or cannot be parsed, the method returns <code>null</code>.
	 * </p>
	 * 
	 * @param source the string to convert, in ISO 8601 format (e.g., "2025-01-01T10:15:30Z")
	 * @return the converted {@link LocalDateTime} object, or <code>null</code> if conversion fails or input is blank
	 */
	@Override
	public LocalDateTime convert(String source) {
		// Check if the input string is blank
		if (StringUtils.isBlank(source)) {
			return null;
		}

		try {
			// Try to parse the string as an Instant and convert it to LocalDateTime
			return LocalDateTime.ofInstant(Instant.parse(source), ZoneId.systemDefault());
		} catch (Exception e) {
			// If parsing fails, return null
			return null;
		}
	}

}