package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.FunctionTypeDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.FunctionTypeService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This class serves as the controller for FunctionType related operations in the tenant module.
 * It handles the RESTful endpoints for creating, reading, updating, and deleting FunctionTypeDto objects.
 * 
 * @author Krushali Talaviya
 * @since June 2023
 * 
 */
@RestController
@RequestMapping(value = ApiPathConstant.FUNCTION_TYPE)
@Tag(name = SwaggerConstant.FUNCTION_TYPE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FunctionTypeController {

	/**
	 * Service for handling various messaging operations, including retrieving localized 
	 * messages and handling error messages for the application. 
	 * This service is used across the controller to provide appropriate feedback to the user.
	 * 
	 * @see MessageService
	 */
	private final MessageService messageService;

	/**
	 * Service for handling business logic related to FunctionType records.
	 * This service is used to interact with the underlying data store to create, read, update, and 
	 * delete function types, including retrieving active function types as required by the application.
	 * 
	 * @see FunctionTypeService
	 */
	private final FunctionTypeService functionTypeService;

	/**
	 * Creates a new function master record.
	 *
	 * @param functionTypeDto The FunctionTypeDto object containing the details of the function master.
	 * @return A ResponseContainerDto wrapping an Optional FunctionTypeDto object representing the created function master record, along with a status message.
	 * @throws RestException if an error occurs during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.FUNCTION_TYPE + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<FunctionTypeDto>> create(@Valid @RequestBody FunctionTypeDto functionTypeDto) throws RestException {
		Optional<FunctionTypeDto> functionTypeResponseDto = functionTypeService.createAndUpdate(functionTypeDto);
		return RequestResponseUtils.generateResponseDto(functionTypeResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves a list of function master records based on the provided filter.
	 *
	 * @param filterDto The FilterDto object containing the filter parameters for retrieving function master records.
	 * @return A ResponseContainerDto wrapping a List of FunctionTypeDto objects representing the retrieved function master records.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.FUNCTION_TYPE + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<FunctionTypeDto>> read(FilterDto filterDto) {
		return functionTypeService.read(filterDto);
	}

	/**
	 * Updates an existing function master record.
	 *
	 * @param functionTypeDto The FunctionTypeDto object containing the updated details of the function master.
	 * @return A ResponseContainerDto wrapping a FunctionTypeDto object representing the updated function master record, along with a status message.
	 * @throws RestException if an error occurs during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.FUNCTION_TYPE + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<FunctionTypeDto> update(@Valid @RequestBody FunctionTypeDto functionTypeDto) throws RestException {
		Optional<FunctionTypeDto> functionTypeResponseDto = functionTypeService.createAndUpdate(functionTypeDto);
		return RequestResponseUtils.generateResponseDto(functionTypeResponseDto.isPresent() ? functionTypeResponseDto.get() : null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a function master record by ID.
	 *
	 * @param idStr The ID of the function master record to be deleted.
	 * @return A ResponseContainerDto wrapping an empty object, along with a status message indicating the success of the deletion.
	 * @throws RestException if an error occurs during the deletion process.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.FUNCTION_TYPE + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String idStr) throws RestException {
		functionTypeService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves a list of active FunctionType records.
	 * 
	 * This API endpoint is used to fetch all FunctionType records where the `isActive` flag is true. 
	 * It is secured with the appropriate user rights, requiring either `CAN_ADD` or `CAN_EDIT` 
	 * permissions for the Book Order module.
	 * 
	 * @return A ResponseContainerDto containing a list of FunctionTypeDto objects representing the active FunctionType records.
	 * 
	 * This API is part of the Book Order module, which is located in the main menu under the "Other" section.
	 * 
	 * Service: FunctionTypeService
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<FunctionTypeDto>> readActive() {
		return RequestResponseUtils.generateResponseDto(functionTypeService.findByIsActiveTrue());
	}

}