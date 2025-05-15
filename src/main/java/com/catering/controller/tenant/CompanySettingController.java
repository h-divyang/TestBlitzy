package com.catering.controller.tenant;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.ErrorGenerator;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.ColorThemeRequestDto;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.LayoutThemeRequestDto;
import com.catering.properties.FileProperties;
import com.catering.repository.tenant.CompanySettingRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CompanySettingService;
import com.catering.util.DataUtils;
import com.catering.util.FileUtils;
import com.catering.util.RequestResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for handling company-specific settings and configurations.
 * Provides end points to retrieve and update the company settings.
 */
@RestController
@RequestMapping(value = ApiPathConstant.COMPANY_SETTING)
@Tag(name = SwaggerConstant.COMPANY_SETTING)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanySettingController {

	Logger logger = LoggerFactory.getLogger(CompanySettingController.class);

	/**
	 * Service for managing company-specific settings and configurations.
	 */
	CompanySettingService companySettingService;

	/**
	 * Service for managing company-specific settings.
	 */
	CompanySettingRepository companySettingRepository;

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
	 * Retrieves the current company settings.
	 *
	 * @return a {@link ResponseContainerDto} containing the {@link CompanySettingDto} object with the company settings.
	 */
	@GetMapping
	public ResponseContainerDto<CompanySettingDto> read(@RequestParam(required = true) Boolean isBackgroundImageRequired) {
		return RequestResponseUtils.generateResponseDto(companySettingService.getCompannySetting(isBackgroundImageRequired));
	}

	/**
	 * Retrieves the pagination size setting for the company.
	 *
	 * @return an {@code Integer} representing the pagination size.
	 */
	@GetMapping(value = ApiPathConstant.PAGINATION_SIZE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.UTILITY + ApiUserRightsConstants.CAN_VIEW})
	public Integer getPaginationSize() {
		return companySettingService.getPaginationSize();
	}

	/**
	 * Updates the company settings with the provided data.
	 *
	 * @param companySettingDto The {@link CompanySettingDto} object containing the updated settings data.
	 * @return A {@link ResponseContainerDto} containing the updated {@link CompanySettingDto} and a message indicating the operation result.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.UTILITY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<CompanySettingDto> update(@Valid @RequestParam(required = true) String companyDetails, @RequestParam(name= Constants.IMAGE, required = false) MultipartFile backgroundImage) {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(backgroundImage, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		CompanySettingDto companySettingDto = null;
		try {
			companySettingDto = objectMapper.readValue(companyDetails, CompanySettingDto.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return RequestResponseUtils.generateResponseDto(companySettingService.createAndUpdate(companySettingDto, backgroundImage), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Updates menu preparation allocation settings for the company.
	 * 
	 * Allows configuration of the menu preparation visibility and tree display settings.
	 * Requires EDIT permission for Utility features.
	 *
	 * @param menuPreparationToggleValue (Optional) Flag to enable/disable menu preparation allocation feature. If not provided, the existing value will remain unchanged.
	 * @param menuItemPriceVisibility (Required) Flag to control visibility of the menu item price. True to show the menu item price, false to hide it.
	 */
	@PutMapping(value = ApiPathConstant.COMPANY_SETTING_MENU_PREPARATION_TOGGLE_PRICE_VISIBILITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.UTILITY + ApiUserRightsConstants.CAN_EDIT})
	public void updateIsMenuPreparationToggleEnabledAndIsMenuItemPrice(@RequestParam(required = false) Boolean isMenuPreparationToggleEnabled, @RequestParam(required = true) Boolean isMenuItemPriceVisible) {
		companySettingService.updateIsMenuPreparationToggleEnabledAndIsMenuItemPrice(isMenuPreparationToggleEnabled, isMenuItemPriceVisible);
	}

	/**
	 * Deletes the QR code associated with the company bank account.
	 *
	 * @return a ResponseContainerDto containing a null object and a success message indicating that the data has been deleted.
	 */
	@DeleteMapping(value = ApiPathConstant.BACKGROUND_IMAGE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.UTILITY + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete() {
		companySettingService.deleteBackgroundImage();
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Updates the layout theme (dark mode and color theme) for a company setting.
	 *
	 * @param id The company setting ID.
	 * @param isDarkTheme Whether dark mode is enabled.
	 * @param colourTheme The selected color theme.
	 * @return A success response with update confirmation message.
	 */
	@PutMapping(value = ApiPathConstant.COMPANY_SETTING_LAYOUT_THEME)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.UTILITY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Object> updateLayoutTheme(@RequestBody LayoutThemeRequestDto layoutThemeRequestDto) {
		companySettingRepository.updateLayoutTheme(layoutThemeRequestDto.getId(), layoutThemeRequestDto.getIsDarkTheme(), layoutThemeRequestDto.getColourTheme(), DataUtils.getCurrentAuditor().get().getId());
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Updates only the color theme for a specific company setting.
	 *
	 * @param id The company setting ID.
	 * @param colourTheme The new color theme.
	 * @return A success response with an update confirmation message.
	 */
	@PutMapping(value = ApiPathConstant.COMPANY_SETTING_COLOUR_THEME)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.UTILITY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Object> updateColourTheme(@RequestBody ColorThemeRequestDto colorThemeRequestDto) {
		companySettingRepository.updateColourTheme(colorThemeRequestDto.getId(), colorThemeRequestDto.getColourTheme(), DataUtils.getCurrentAuditor().get().getId());
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}