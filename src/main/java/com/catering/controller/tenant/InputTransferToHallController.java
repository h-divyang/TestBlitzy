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
import com.catering.dto.tenant.request.InputTransferToHallDto;
import com.catering.dto.tenant.request.InputTransferToHallRawMaterialDropDownDto;
import com.catering.dto.tenant.request.InputTransferToHallResponseDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.InputTransferToHallService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST controller for managing Input Transfers to Hall operations.
 * <p>
 * This controller provides endpoints for creating, updating, retrieving, and deleting Input Transfer to Hall records.
 * Additionally, it includes endpoints for retrieving raw material data and upcoming orders related to Input Transfers.
 * Role-based authorization ensures secure access to these operations.
 * </p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.INPUT_TRANSFER_TO_HALL)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = SwaggerConstant.INPUT_TRANSFER_TO_HALL)
public class InputTransferToHallController {

	/**
	 * Service for managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling application exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for business logic related to Input Transfer to Hall operations.
	 */
	InputTransferToHallService inputTransferToHallService;

	/**
	 * Creates a new Input Transfer to Hall record.
	 *
	 * @param inputTransferToHallDto the DTO containing details of the Input Transfer to Hall to create.
	 * @return a response container DTO with the created Input Transfer to Hall record.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<InputTransferToHallDto>> create(@Valid @RequestBody InputTransferToHallDto inputTransferToHallDto) {
		Optional<InputTransferToHallDto> inputTransferToHall = inputTransferToHallService.createUpdateInputTransferToHall(inputTransferToHallDto);
		return RequestResponseUtils.generateResponseDto(inputTransferToHall, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates an existing Input Transfer to Hall record.
	 *
	 * @param inputTransferToHallDto the DTO containing updated details of the Input Transfer to Hall.
	 * @return a response container DTO with the updated Input Transfer to Hall record.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<InputTransferToHallDto>> update(@Valid @RequestBody InputTransferToHallDto inputTransferToHallDto) {
		Optional<InputTransferToHallDto> inputTransferToHall = inputTransferToHallService.createUpdateInputTransferToHall(inputTransferToHallDto);
		return RequestResponseUtils.generateResponseDto(inputTransferToHall, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves all Input Transfers to Hall based on filter criteria.
	 *
	 * @param filterDto the filter criteria to apply when retrieving Input Transfers to Hall.
	 * @return a response container DTO containing a list of Input Transfer to Hall records.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<InputTransferToHallResponseDto>> getAllInputTransferToHall(FilterDto filterDto) {
		return inputTransferToHallService.getAllInputTransferToHall(filterDto);
	}

	/**
	 * Retrieves a specific Input Transfer to Hall record by its ID.
	 *
	 * @param id the ID of the Input Transfer to Hall record to retrieve, validated as numeric.
	 * @return a response container DTO with the requested Input Transfer to Hall record.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<InputTransferToHallDto>> getInputTransferToHall(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String id) {
		return RequestResponseUtils.generateResponseDto(inputTransferToHallService.getInputTransferToHall(Long.valueOf(id)));
	}

	/**
	 * Deletes an Input Transfer to Hall record by its ID.
	 *
	 * @param id the ID of the Input Transfer to Hall record to delete, validated as numeric.
	 * @return a response container DTO confirming the deletion.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> deleteInputTransferToHall(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		if(!ValidationUtils.isNumber(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		inputTransferToHallService.deleteInputTransferToHall(Long.valueOf(id));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves raw material data by Order ID for Input Transfers to Hall.
	 *
	 * @param orderId the ID of the order to retrieve raw material data for.
	 * @return a response container DTO containing a list of raw material data.
	 */
	@GetMapping(value = ApiPathConstant.INPUT_TRANSFER_TO_HALL_RAW_MATERIAL_LIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<InputTransferToHallUpcomingOrderRawMaterial>> findInputTransferToHallRawMaterialByOrderId(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId) {
		return RequestResponseUtils.generateResponseDto(inputTransferToHallService.findInputTransferToHallRawMaterialByOrderId(orderId));
	}

	/**
	 * Retrieves upcoming orders related to Input Transfers to Hall by Order ID.
	 *
	 * @param orderId the ID of the order to retrieve upcoming orders for.
	 * @return a response container DTO containing a list of upcoming orders.
	 */
	@GetMapping(value = ApiPathConstant.INPUT_TRANSFER_TO_HALL_ORDERS_lIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<InputTransferToHallUpcomingOrderDto>> upcomingOrdersForInputTransferToHall(@PathVariable(name = FieldConstants.COMMON_FIELD_ID,required = false) Long orderId) {
		return RequestResponseUtils.generateResponseDto(inputTransferToHallService.getUpcomingOrdersForInputTransferToHall(orderId));
	}

	/**
	 * Retrieves all raw material data for Input Transfers to Hall.
	 *
	 * @return a response container DTO containing a list of raw material dropdown data.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_DATA)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.RAW_MATERIAL_RETURN_TO_HALL + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<InputTransferToHallRawMaterialDropDownDto>> findRawMaterial() {
		return RequestResponseUtils.generateResponseDto(inputTransferToHallService.getRawMaterial());
	}

}