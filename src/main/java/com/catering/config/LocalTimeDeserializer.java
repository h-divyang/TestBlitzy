package com.catering.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.jackson.JsonComponent;

import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.service.tenant.CompanySettingService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Custom deserializer for {@link LocalTime} objects.
 * <p>
 * This deserializer converts a JSON array containing two integers (representing hours and minutes) 
 * into a {@link LocalTime}. The time is then adjusted according to the company's time zone 
 * and converted to the specified target time zone.
 * </p>
 * 
 * @see JsonDeserializer
 * @see LocalTime
 * @see CompanySettingDto
 * @see TimeZoneInitializer
 */
@JsonComponent
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

	/**
	 * The service used to retrieve company settings, including time zone information.
	 */
	TimeZoneInitializer timeZoneInitializer;

	/**
	 * The service to fetch company settings.
	 */
	CompanySettingService companySettingService;

	/**
	 * Deserialize a JSON array containing two integers representing hours and minutes into a {@link LocalTime}.
	 * <p>
	 * The deserialized {@link LocalTime} is adjusted to the time zone defined by the company's settings and 
	 * then converted to the target time zone.
	 * </p>
	 * 
	 * @param p the {@link JsonParser} used to read the JSON content
	 * @param ctxt the {@link DeserializationContext} used for additional deserialization features
	 * @return the deserialized and adjusted {@link LocalTime} object, or {@code null} if the input is invalid
	 * @throws IOException if an error occurs during the deserialization process
	 */
	@Override
	public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		List<Integer> times = new ArrayList<>();
		// Read the JSON array elements and add them as integers to the times list
		while (p.nextToken() != JsonToken.END_ARRAY) {
			if (p.currentToken() == JsonToken.VALUE_NUMBER_INT) {
				times.add(p.getValueAsInt());
			}
		}
		// If the array contains exactly two elements (hours and minutes)
		if (times.size() == 2) {
			LocalTime time = LocalTime.of(times.get(0), times.get(1));
			CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
			ZonedDateTime sourceDateTime = ZonedDateTime.of(LocalDate.now(), time, ZoneId.of(companySettingDto.getTimeZone()));
			ZonedDateTime targetDateTime = sourceDateTime.withZoneSameInstant(ZoneId.of(timeZoneInitializer.getTimeZone()));
			return targetDateTime.toLocalTime();
		}
		return null;
	}

}