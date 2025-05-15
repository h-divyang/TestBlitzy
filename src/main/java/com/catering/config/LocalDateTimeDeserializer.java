package com.catering.config;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jackson.JsonComponent;

import com.catering.bean.ErrorGenerator;
import com.catering.constant.MessagesConstant;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Custom deserializer for {@link LocalDateTime} objects. 
 * <p>
 * This deserializer is used to deserialize JSON strings or long values (representing milliseconds since the Unix epoch) 
 * into {@link LocalDateTime} objects. It handles both formats, where the input could either be a timestamp or 
 * an ISO 8601 string representing a date-time.
 * </p>
 * <p>
 * The deserialization logic checks if the value is a valid long (milliseconds since epoch) or a valid ISO 8601 string.
 * If the value is not valid, it throws a {@link BadRequestException}.
 * </p>
 * 
 * @see JsonDeserializer
 * @see LocalDateTime
 * @see Instant
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonComponent
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	/**
	 * Service used to handle exception-related functionality.
	 */
	ExceptionService exceptionService;

	/**
	 * Service used to retrieve messages for localization.
	 */
	MessageService messageService;

	/**
	 * Deserialize a JSON value into a {@link LocalDateTime}.
	 * <p>
	 * This method handles two types of input values:
	 * <ul>
	 *     <li>A long value representing milliseconds since the Unix epoch, which is converted to a {@link LocalDateTime}.</li>
	 *     <li>A string value in ISO 8601 format, which is parsed into an {@link Instant} and then converted to a {@link LocalDateTime}.</li>
	 * </ul>
	 * If the input is invalid or cannot be parsed, a {@link BadRequestException} is thrown with a relevant error message.
	 * </p>
	 * 
	 * @param p the JSON parser
	 * @param ctxt the deserialization context
	 * @return the deserialized {@link LocalDateTime} object, or null if the input is not valid
	 * @throws IOException if an input-output error occurs during deserialization
	 */
	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		// Check if the input is a valid long (milliseconds since epoch)
		if (p.getValueAsLong() > 0) {
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(p.getValueAsLong()), ZoneId.systemDefault());
		} else if (StringUtils.isNotBlank(p.getValueAsString())) {
			try {
				// Try parsing the string as an Instant and convert to LocalDateTime
				return LocalDateTime.ofInstant(Instant.parse(p.getValueAsString()), ZoneId.systemDefault());
			} catch (Exception e) {
				// If parsing fails, throw a BadRequestException with an error message
				ErrorGenerator errors = ErrorGenerator.builder();
				exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INPUT_DATE_TIME_PARSE_ISSUE), errors.getErrors());
			}
		}
		// Return null if input is invalid or empty
		return null;
	}

}