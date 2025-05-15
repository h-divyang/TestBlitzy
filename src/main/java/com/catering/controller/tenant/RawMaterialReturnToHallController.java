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
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.dto.tenant.request.RawMaterialReturnToHallDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallInputTransferToHallDropDownDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallResponseDto;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.RawMaterialReturnToHallService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST controller for managing raw material return to hall operations.<br>
 * This controller exposes endpoints for creating, updating, retrieving, 
 * and deleting raw material return records to the hall.<br>
 * It also provides endpoints for fetching associated data like raw material details and 
 * input transfer information.
 * <p>
 * All methods in this class require specific user rights to access, ensuring that only authorized users can perform the actions.
 * </p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.RAW_MATERIAL_RETURN_TO_HALL)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = SwaggerConstant.RAW_MATERIAL_RETURN_TO_HALL)
public class RawMaterialReturnToHallController {

	/**
	 * Service responsible for handling messages and localization in the application.
	 * It provides methods to fetch localized and custom messages.
	 */
	MessageService messageService;

	/**
	 * Service that handles exceptions and provides functionality for error handling and throwing exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service that manages raw material return to hall operations, including create, update, delete, and retrieve operations.
	 * It communicates with the database to process raw material return data.
	 */
	RawMaterialReturnToHallService rawMaterialReturnToHallService;

	/**
	 * Endpoint to create a new raw material return to hall record.
	 * Only users with the appropriate permissions can add raw material return records.
	 * 
	 * @param rawMaterialReturnToHallDto the data transfer object containing the raw material return details.
	 * @return A ResponseContainerDto containing the created raw material return details.
	 */

	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_RETURN_TO_HALL + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<RawMaterialReturnToHallDto>> create(@Valid @RequestBody RawMaterialReturnToHallDto rawMaterialReturnToHallDto) {
		Optional<RawMaterialReturnToHallDto> rawMaterialReturnToHall = rawMaterialReturnToHallService.createUpdateRawMaterialReturnToHall(rawMaterialReturnToHallDto);
		return RequestResponseUtils.generateResponseDto(rawMaterialReturnToHall, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Endpoint to update an existing raw material return to hall record.
	 * Only users with the appropriate permissions can edit raw material return records.
	 * 
	 * @param rawMaterialReturnToHallDto the data transfer object containing the updated raw material return details.
	 * @return A ResponseContainerDto containing the updated raw material return details.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_RETURN_TO_HALL + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<RawMaterialReturnToHallDto>> update(@Valid @RequestBody RawMaterialReturnToHallDto rawMaterialReturnToHallDto) {
		Optional<RawMaterialReturnToHallDto> rawMaterialReturnToHall = rawMaterialReturnToHallService.createUpdateRawMaterialReturnToHall(rawMaterialReturnToHallDto);
		return RequestResponseUtils.generateResponseDto(rawMaterialReturnToHall, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint to retrieve all raw material return to hall records with optional 
	 * filtering and pagination.
	 * 
	 * @param filterDto the filter and pagination details to apply.
	 * @return A ResponseContainerDto containing the list of raw material return records.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_RETURN_TO_HALL + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<RawMaterialReturnToHallResponseDto>> getAllRawMaterialReturnToHall(FilterDto filterDto) {
		return rawMaterialReturnToHallService.getAllRawMaterialReturnToHall(filterDto);
	}

	/**
	 * Endpoint to retrieve a specific raw material return to hall record by its ID.
	 * 
	 * @param id the ID of the raw material return record.
	 * @return A ResponseContainerDto containing the raw material return to hall record.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_RETURN_TO_HALL + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<RawMaterialReturnToHallDto>> getrawmaterialReturnToHall(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String id) {
		return RequestResponseUtils.generateResponseDto(rawMaterialReturnToHallService.getRawMaterialReturnToHall(Long.valueOf(id)));
	}

	/**
	 * Endpoint to delete a specific raw material return to hall record by its ID.
	 * Only users with appropriate permissions can delete raw material return records.
	 * 
	 * @param id the ID of the raw material return record to delete.
	 * @return A ResponseContainerDto indicating the result of the delete operation.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_RETURN_TO_HALL + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> deleteRawMaterialReturnToHall(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		if(!ValidationUtils.isNumber(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		rawMaterialReturnToHallService.deleteRawMaterialReturnToHall(Long.valueOf(id));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Endpoint to retrieve a list of raw materials related to a specific input 
	 * transfer to hall by its ID.
	 * 
	 * @param inputTransferToHallId the ID of the input transfer to hall.
	 * @return A ResponseContainerDto containing a list of raw material details.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_RETURN_TO_HALL_RAW_MATERIAL_LIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_RETURN_TO_HALL + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<InputTransferToHallUpcomingOrderRawMaterial>> findRawMaterialByInputTransferToHallId(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long inputTransferToHallId) {
		return RequestResponseUtils.generateResponseDto(rawMaterialReturnToHallService.findRawMaterialByInputTransferToHallId(inputTransferToHallId));
	}

	/**
	 * Endpoint to retrieve the drop-down list of input transfers to hall for 
	 * selecting associated raw materials.
	 * 
	 * @param inputTransferToHallId the ID of the input transfer to hall (optional).
	 * @return A ResponseContainerDto containing the drop-down data for raw materials.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_RETURN_TO_HALL_INPUT_TRANSFER_HALL_LIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_RETURN_TO_HALL + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<RawMaterialReturnToHallInputTransferToHallDropDownDto>> getRawMaterialReturnToHallInputTransferToHallDropDownData(@PathVariable(name = FieldConstants.COMMON_FIELD_ID,required = false) Long inputTransferToHallId) {
		return RequestResponseUtils.generateResponseDto(rawMaterialReturnToHallService.getRawMaterialReturnToHallInputTransferToHallDropDownData(inputTransferToHallId));
	}

}