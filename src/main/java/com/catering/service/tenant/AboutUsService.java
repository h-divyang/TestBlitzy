package com.catering.service.tenant;

import java.util.Optional;
import com.catering.dto.tenant.request.AboutUsDto;

public interface AboutUsService {

	/**
	 * Fetches the "About Us" information for the application.
	 *
	 * @return An instance of AboutUsDto containing the "About Us" details in various supported languages.
	 */
	AboutUsDto getAboutUsData();

	/**
	 * Saves or updates the "About Us" information.
	 *
	 * @param aboutusDto The AboutUsDto object containing "About Us" details to be saved or updated.
	 * @return An Optional containing the saved or updated AboutUsDto. If the operation fails, an empty Optional is returned.
	 */
	Optional<AboutUsDto> saveAboutUsData(AboutUsDto aboutusDto);

}