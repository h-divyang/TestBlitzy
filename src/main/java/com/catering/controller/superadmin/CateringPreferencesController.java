package com.catering.controller.superadmin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.catering.constant.ApiPathConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.superadmin.CateringPreferencesDto;
import com.catering.service.superadmin.CateringPreferencesService;
import com.catering.util.RequestResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * CateringPreferencesController is a REST controller responsible for managing catering preferences and related assets such as logos and favicons.
 * It provides end points to retrieve various preferences and assets.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstant.CATERING_PREFERENCES)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CateringPreferencesController {

	/**
	 * This service is responsible for handling business logic related to retrieving and managing catering preferences.
	 */
	CateringPreferencesService cateringPreferencesService;

	/**
	 * Handles the GET request to retrieve catering preferences.
	 *
	 * @return a {@code ResponseContainerDto} containing the catering preferences as a {@code CateringPreferencesDto}.
	 */
	@GetMapping
	public ResponseContainerDto<CateringPreferencesDto> getCateringPreferences() {
		return RequestResponseUtils.generateResponseDto(cateringPreferencesService.read());
	}

	/**
	 * Handles the GET request to retrieve the horizontal logo.
	 *
	 * @return a {@link ResponseContainerDto} containing the horizontal logo as a {@link String}.
	 */
	@GetMapping(value = ApiPathConstant.LOGO_HORIZONTAL)
	public ResponseContainerDto<String> getLogoHorizontal() {
		return RequestResponseUtils.generateResponseDto(cateringPreferencesService.getLogoHorizontal());
	}

	/**
	 * Handles the GET request to retrieve the horizontal logo for dark theme.
	 *
	 * @return a {@link ResponseContainerDto} containing the horizontal logo as a {@link String}.
	 */
	@GetMapping(value = ApiPathConstant.LOGO_HORIZONTAL_WHITE)
	public ResponseContainerDto<String> getLogoHorizontalWhite() {
		return RequestResponseUtils.generateResponseDto(cateringPreferencesService.getLogoHorizontalWhite());
	}

	/**
	 * Handles the GET request to retrieve the vertical logo.
	 *
	 * @return a {@link ResponseContainerDto} containing the vertical logo as a {@link String}.
	 */
	@GetMapping(value = ApiPathConstant.LOGO_VERTICAL)
	public ResponseContainerDto<String> getLogoVertical() {
		return RequestResponseUtils.generateResponseDto(cateringPreferencesService.getLogoVertical());
	}

	/**
	 * Handles the GET request to retrieve the favicon.
	 *
	 * @return a {@link ResponseContainerDto} containing the favicon as a {@link String}.
	 */
	@GetMapping(value = ApiPathConstant.FAVICON)
	public ResponseContainerDto<String> getFavicon() {
		return RequestResponseUtils.generateResponseDto(cateringPreferencesService.getFavicon());
	}

}