package com.catering.controller.tenant;

import java.util.Optional;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.AboutUsDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.AboutUsService;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for handling "About Us" data.
 * This API is used in the Settings functionality where we add, view, and manage the "About Us" information.
 * 
 * Service: CompanyPreferencesService
 * Component: SettingComponent
 */
@RestController
@RequestMapping(value = ApiPathConstant.ABOUT_US)
@Tag(name = SwaggerConstant.ABOUT_US)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AboutUsController {

	/**
	 * Service for managing "About Us" information.
	 * Provides methods to retrieve and save the data related to the "About Us" section.
	 */
	AboutUsService aboutUsService;

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Retrieves the "About Us" information.
	 *
	 * @return a ResponseContainerDto containing the "About Us" details encapsulated in an AboutUsDto object.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ABOUT_US + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<AboutUsDto> getAboutUs() {
		return RequestResponseUtils.generateResponseDto(aboutUsService.getAboutUsData());
	}

	/**
	 * Creates or updates the "About Us" data.
	 *
	 * @param aboutUsDto the DTO containing the details of the "About Us" data to be saved
	 * @return a ResponseContainerDto containing an Optional of AboutUsDto with the saved "About Us" data
	 * @throws RestException if saving "About Us" data fails.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ABOUT_US + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.ABOUT_US + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Optional<AboutUsDto>> create(@Valid @RequestBody AboutUsDto aboutUsDto) throws RestException {
		Optional<AboutUsDto> aboutUsData = aboutUsService.saveAboutUsData(aboutUsDto);
		return RequestResponseUtils.generateResponseDto(aboutUsData, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

}