package com.catering.config;

import java.time.ZoneId;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.catering.dto.superadmin.CateringPreferencesDto;
import com.catering.service.superadmin.CateringPreferencesService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This class initializes the time zone based on the catering preferences.
 * It retrieves the time zone from the CateringPreferencesService and sets it accordingly.
 * If no time zone is found in the preferences, it defaults to the system's time zone.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeZoneInitializer {

	Logger logger = LoggerFactory.getLogger(TimeZoneInitializer.class);

	/**
	 * The time zone used by the application.
	 * It is initialized either from catering preferences or system default.
	 */
	@Getter
	String timeZone;

	/**
	 * Service to retrieve catering preferences, including time zone information.
	 */
	final CateringPreferencesService cateringPreferencesService;

	/**
	 * Initializes the time zone.
	 * It attempts to read the time zone from the catering preferences.
	 * If no valid time zone is found, it defaults to the system's default time zone.
	 */
	@PostConstruct
	public void initialize() {
		try {
			CateringPreferencesDto cateringPreferencesDto = cateringPreferencesService.read();
			timeZone = Objects.nonNull(cateringPreferencesDto) && StringUtils.isNoneBlank(cateringPreferencesDto.getTimeZone()) ? cateringPreferencesDto.getTimeZone() : ZoneId.systemDefault().getId();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}