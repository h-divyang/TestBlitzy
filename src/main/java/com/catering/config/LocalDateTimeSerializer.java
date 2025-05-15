package com.catering.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Custom serializer for {@link LocalDateTime} objects.
 * <p>
 * This serializer converts a {@link LocalDateTime} into its epoch millisecond representation when writing the object to JSON.
 * The conversion is performed by first converting the {@link LocalDateTime} to a {@link ZonedDateTime} using the system's default zone, 
 * and then converting that to an {@link Instant} to obtain the epoch milliseconds.
 * </p>
 * 
 * @see JsonSerializer
 * @see LocalDateTime
 * @see Instant
 * @see ZoneId
 */
@JsonComponent
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

	/**
	 * Serialize a {@link LocalDateTime} into a number representing the epoch milliseconds.
	 * <p>
	 * This method converts a {@link LocalDateTime} into the epoch milliseconds format by first converting it to a 
	 * {@link ZonedDateTime} with the system's default zone and then to an {@link Instant}. The resulting {@link Instant} 
	 * is used to get the epoch milliseconds.
	 * </p>
	 *
	 * @param value the {@link LocalDateTime} to serialize
	 * @param gen the {@link JsonGenerator} used to write the JSON output
	 * @param serializers the {@link SerializerProvider} providing serializers for other types
	 * @throws IOException if there is an issue writing the JSON
	 */
	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// Convert LocalDateTime to epoch milliseconds and write as a number
		gen.writeNumber(value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}

}