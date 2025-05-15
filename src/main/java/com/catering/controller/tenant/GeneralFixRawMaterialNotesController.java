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
import com.catering.dto.tenant.request.GeneralFixRawMaterialNotesDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.GeneralFixRawMaterialNotesService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST Controller for managing General Fix Raw Material Notes. Provides APIs for creating and 
 * retrieving General Fix Raw Material Notes records. Each API is secured with user rights 
 * validation to ensure appropriate access control.
 * 
 * Services involved:
 * - {@link GeneralFixRawMaterialNotesService}
 * - {@link MessageService}
 * 
 * This controller includes the following operations:
 * 1. Creating a General Fix Raw Material Note.
 * 2. Retrieving the current General Fix Raw Material Notes.
 */
@RestController
@RequestMapping(value = ApiPathConstant.GENERAL_FIX_RAW_MATERIAL_NOTES)
@Tag(name = SwaggerConstant.GENERAL_FIX_RAW_MATERIAL_NOTES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeneralFixRawMaterialNotesController {

	/**
	 * Service for managing General Fix Raw Material Notes data, including CRUD operations.
	 */
	GeneralFixRawMaterialNotesService generalFixRawMaterialNotesService;

	/**
	 * Service for handling messages, including validation and response messages.
	 */
	MessageService messageService;

	/**
	 * Creates a new General Fix Raw Material Note record.
	 *
	 * @param generalFixRawMaterialNotesDto The data transfer object containing the data for creation.
	 * @return A ResponseContainerDto containing an Optional GeneralFixRawMaterialNotesDto, indicating the result of the creation.
	 * @throws RestException If an exception occurs during the operation.
	 * 
	 * This API is used in the General Fix Raw Material Notes module and allows users with the 
	 * appropriate permissions (either CAN_ADD or CAN_EDIT) to create or update a note.
	 * 
	 * Service: GeneralFixRawMaterialNotesService
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.GENERAL_FIX_RAW_MATERIAL_NOTES + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.GENERAL_FIX_RAW_MATERIAL_NOTES + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Optional<GeneralFixRawMaterialNotesDto>> create(@Valid @RequestBody GeneralFixRawMaterialNotesDto generalFixRawMaterialNotesDto) throws RestException {
		Optional<GeneralFixRawMaterialNotesDto> generalFixRawMaterialNotesData = generalFixRawMaterialNotesService.saveGeneralFixRawMaterialNotes(generalFixRawMaterialNotesDto);
		return RequestResponseUtils.generateResponseDto(generalFixRawMaterialNotesData, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves the General Fix Raw Material Notes.
	 *
	 * @return A ResponseContainerDto containing the GeneralFixRawMaterialNotesDto with the current note data.
	 * 
	 * This API is used to retrieve the current General Fix Raw Material Notes record.
	 * 
	 * Service: GeneralFixRawMaterialNotesService
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.GENERAL_FIX_RAW_MATERIAL_NOTES + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<GeneralFixRawMaterialNotesDto> read() {
		return RequestResponseUtils.generateResponseDto(generalFixRawMaterialNotesService.getGeneralFixRawMaterialNotes());
	}

}