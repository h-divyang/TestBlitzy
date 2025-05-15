package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.catering.dto.tenant.request.CommonNotesDto;
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
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.OrderLabourDistributionDto;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.OrderLabourDistributionService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for managing labour distribution related operations for orders.
 * <p>
 * This controller provides APIs to create, update, fetch, and delete labour distribution records. 
 * It is designed to handle labour and other management-related functionalities for orders.
 * </p>
 *
 * <p>APIs include:</p>
 * <ul>
 *   <li>Create or update labour distribution records</li>
 *   <li>Fetch labour distribution records by order function ID</li>
 *   <li>Delete labour distribution records by ID</li>
 *   <li>Update notes for a labour distribution record</li>
 * </ul>
 */
@RestController
@RequestMapping(value = ApiPathConstant.ORDER_LABOUR_DISTRIBUTION)
@Tag(name = SwaggerConstant.ORDER_LABOUR_DISTRIBUTION)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderLabourDistributionController {

	/**
	 * Service to handle exceptions and generate error responses.
	 */
	ExceptionService exceptionService;

	/**
	 * Service to manage labour distribution operations.
	 */
	OrderLabourDistributionService orderLabourDistributionService;

	/**
	 * Service to manage and retrieve localized messages for API responses.
	 */
	MessageService messageService;

	/**
	 * Creates or updates labour distribution data for an order.
	 * <p>
	 * This endpoint accepts a list of {@link OrderLabourDistributionDto} objects, creates new records, 
	 * or updates existing ones based on the provided input.
	 * </p>
	 *
	 * @param orderLabourDistributionDtos the list of labour distribution data transfer objects to be created or updated.
	 * @return a response containing a list of created or updated {@link OrderLabourDistributionDto} objects along with a success message.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<List<OrderLabourDistributionDto>> create(@RequestBody List<@Valid OrderLabourDistributionDto> orderLabourDistributionDtos) {
		List<OrderLabourDistributionDto> orderLabourDistributionResponse = orderLabourDistributionService.createAndUpdate(orderLabourDistributionDtos);
		return RequestResponseUtils.generateResponseDto(orderLabourDistributionResponse, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED)); 
	}

	/**
	 * Fetches labour distribution data for a specific order function.
	 * <p>
	 * This endpoint retrieves a list of {@link OrderLabourDistributionDto} objects based on the provided order function ID.
	 * </p>
	 *
	 * @param orderFunctionId the ID of the order function to retrieve labour distribution records for.
	 * @return a response containing an optional list of {@link OrderLabourDistributionDto} objects.
	 */
	@GetMapping(value = ApiPathConstant.ORDER_FUNCTION_ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Optional<List<OrderLabourDistributionDto>>> read(@PathVariable(name = FieldConstants.COMMON_FIELD_ORDER_FUNCTION_ID) Long orderFunctionId) {
		return RequestResponseUtils.generateResponseDto(orderLabourDistributionService.findByOrderFunctionId(orderFunctionId));
	}

	/**
	 * Deletes a labour distribution record by its ID.
	 * <p>
	 * This endpoint removes a labour distribution record based on the provided ID.
	 * If the ID is invalid, a {@link RestException} is thrown.
	 * </p>
	 *
	 * @param id the ID of the labour distribution record to delete.
	 * @return a response indicating successful deletion along with a success message.
	 * @throws RestException if the ID is invalid or not a number.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) throws RestException {
		if (!ValidationUtils.isNumber(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		orderLabourDistributionService.deleteById(Long.valueOf(id));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Updates notes for a labour distribution record.
	 * <p>
	 * This endpoint updates the notes field for a specific labour distribution record using the provided data.
	 * </p>
	 *
	 * @param commonNotesDto the {@link CommonNotesDto} containing the updated notes information.
	 * @return a response indicating successful update along with a success message.
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_NOTE)
	public ResponseContainerDto<Object> updateNotes(@RequestBody CommonNotesDto commonNotesDto) {
		orderLabourDistributionService.updateNotes(commonNotesDto);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}