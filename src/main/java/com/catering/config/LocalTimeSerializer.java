package com.catering.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.boot.jackson.JsonComponent;

import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.service.tenant.CompanySettingService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Custom serializer for {@link LocalTime} objects.
 * <p>
 * This serializer converts a {@link LocalTime} into a JSON array containing two integers: 
 * the hour and the minute. The time is adjusted according to the target time zone 
 * specified in the company settings.
 * </p>
 * 
 * @see JsonSerializer
 * @see LocalTime
 * @see CompanySettingDto
 * @see TimeZoneInitializer
 */
@JsonComponent
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

	/**
	 * The service used to retrieve the time zone information for the target time zone.
	 */
	TimeZoneInitializer timeZoneInitializer;

	/**
	 * The service to fetch the company's settings, including the time zone.
	 */
	CompanySettingService companySettingService;

	/**
	 * Serialize a {@link LocalTime} into a JSON array containing two integers (hour and minute).
	 * <p>
	 * The {@link LocalTime} is first adjusted to the target time zone as per the company's time zone settings 
	 * before being serialized as an array of two integers: the hour and the minute.
	 * </p>
	 * 
	 * @param value the {@link LocalTime} object to be serialized
	 * @param gen the {@link JsonGenerator} used to write the JSON content
	 * @param serializers the provider that can be used to find serializers for other types
	 * @throws IOException if an error occurs during serialization
	 */
	@Override
	public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (Objects.nonNull(value)) {
			CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
			ZonedDateTime sourceDateTime = ZonedDateTime.of(LocalDate.now(), value, ZoneId.of(timeZoneInitializer.getTimeZone()));
			ZonedDateTime targetDateTime = sourceDateTime.withZoneSameInstant(ZoneId.of(companySettingDto.getTimeZone()));
			LocalTime time = targetDateTime.toLocalTime();
			// Write the hour and minute to the JSON array
			gen.writeStartArray();
			gen.writeNumber(time.getHour());
			gen.writeNumber(time.getMinute());
			gen.writeEndArray(); 
		}
	}

}