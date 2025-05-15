package com.catering.service.superadmin;

import com.catering.dto.superadmin.CateringPreferencesDto;

/**
 * Service interface for managing and retrieving catering preferences.
 *
 * This interface provides methods for accessing various attributes related to catering preferences,
 * including general information, logos, and the favicon.
 */
public interface CateringPreferencesService {

	/**
	 * Retrieves the catering preferences.
	 *
	 * @return A {@code CateringPreferencesDto} containing the details of the catering preferences such as name, time zone, and developed by information.
	 */
	CateringPreferencesDto read();

	/**
	 * Retrieves the horizontal logo associated with the catering preferences.
	 *
	 * @return A {@code String} representing the horizontal logo.
	 */
	String getLogoHorizontal();

	/**
	 * Retrieves the horizontal logo for dark theme associated with the catering preferences.
	 *
	 * @return A {@code String} representing the horizontal logo.
	 */
	String getLogoHorizontalWhite();

	/**
	 * Retrieves the vertical logo associated with the catering preferences.
	 *
	 * @return A {@code String} representing the vertical logo.
	 */
	String getLogoVertical();

	/**
	 * Retrieves the favicon associated with the catering preferences.
	 *
	 * @return A {@code String} representing the favicon.
	 */
	String getFavicon();

}