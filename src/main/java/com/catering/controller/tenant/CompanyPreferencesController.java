package com.catering.controller.tenant;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.ErrorGenerator;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.CompanyPreferencesDto;
import com.catering.properties.FileProperties;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CompanyPreferencesService;
import com.catering.util.FileUtils;
import com.catering.util.RequestResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for managing company preferences and settings.
 * This class handles API end points related to the retrieval and update of company preferences,
 * including features such as logo management and validation.
 */
@RestController
@RequestMapping(value = ApiPathConstant.COMPANY_PREFERENCES)
@Tag(name = SwaggerConstant.COMPANY_PREFERENCES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyPreferencesController {

	Logger logger = LoggerFactory.getLogger(CompanyPreferencesController.class);

	/**
	 * Service for managing company preferences and settings.
	 */
	CompanyPreferencesService companyPreferencesService;

	/**
	 * Service for handling messages or notifications within the application.
	 */
	MessageService messageService;

	/**
	 * Configuration properties for file handling within the application.
	 */
	FileProperties fileProperties;

	/**
	 * Service for managing exceptions and error handling.
	 */
	ExceptionService exceptionService;

	/**
	 * Utility for mapping Java objects to JSON data and vice versa.
	 */
	ObjectMapper objectMapper;

	/**
	 * Retrieves the company preferences.
	 *
	 * @return A {@code ResponseContainerDto} containing an {@code Optional} of {@code CompanyPreferencesDto}.
	 * The response may include the company preferences if available or an empty optional if not found.
	 */
	@GetMapping
	public ResponseContainerDto<Optional<CompanyPreferencesDto>> read() {
		return RequestResponseUtils.generateResponseDto(companyPreferencesService.find());
	}

	/**
	 * Retrieves the company preferences by their unique identifier.
	 *
	 * @param id The unique identifier of the company preferences.
	 * @return A {@code ResponseContainerDto} containing an {@code Optional} of {@code CompanyPreferencesDto}.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	public ResponseContainerDto<Optional<CompanyPreferencesDto>> read(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String id) {
		return RequestResponseUtils.generateResponseDto(companyPreferencesService.read(Long.valueOf(id), CompanyPreferencesDto.class));
	}

	/**
	 * Updates the company preferences and optionally updates the associated logo.
	 *
	 *
	 * @param companyPreference A JSON string representation of the company preferences to be updated. Must not be null or empty.
	 * @param logo An optional file representing the company's logo. This file will be validated for size and format compliance.
	 * @param request The HTTP request that contains additional request-specific information.
	 * @return A {@code ResponseContainerDto} containing the updated {@code CompanyPreferencesDto}.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.COMPANY_PROFILE + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<CompanyPreferencesDto> update(@Valid @RequestParam(required = true) String companyPreference, @RequestParam(name= Constants.IMAGE, required = false) MultipartFile logo, HttpServletRequest request) {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(logo, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		CompanyPreferencesDto dto = null;
		try {
			dto = objectMapper.readValue(companyPreference, CompanyPreferencesDto.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		Optional<CompanyPreferencesDto> companyPrefrenceResponseDto = companyPreferencesService.update(dto, logo, request);
		return RequestResponseUtils.generateResponseDto(companyPrefrenceResponseDto.isPresent() ? companyPrefrenceResponseDto.get() : null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}