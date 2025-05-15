package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.MealTypeDto;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.MealTypeService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class for managing meal types in the application.
 * 
 * This controller exposes RESTful endpoints for performing CRUD (Create, Read, Update, Delete) 
 * operations on meal types. The operations allow for managing meal types, retrieving 
 * active meal types, and handling meal type status updates. It is part of the overall 
 * meal management system.
 * 
 * The endpoints are protected by authorization annotations to ensure that only authorized 
 * users can access them based on their assigned permissions.
 * 
 * @author Krushali Talaviya
 * @since June 2023
 * 
 */
@RestController
@RequestMapping(value = ApiPathConstant.MEAL_TYPE)
@Tag(name = SwaggerConstant.MEAL_TYPE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MealTypeController {

	/**
	 * Service responsible for managing and retrieving messages within the application.
	 */
	MessageService messageService;

	/**
	 * Service that contains the business logic for meal type-related operations.
	 */
	MealTypeService mealTypeService;

	/**
	 * Service responsible for handling custom exceptions and error management in the application.
	 */
	ExceptionService exceptionService;

	/**
	 * Creates a new Meal Type record.
	 *
	 * @param mealTypeDto The Meal Type object containing the data for creation.
	 * @return A ResponseContainerDto containing an Optional mealTypeDto, indicating the result of the creation.
	 * @throws RestException If an exception occurs during the operation.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEAL_TYPE + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<MealTypeDto>> create(@Valid @RequestBody MealTypeDto mealTypeDto) throws RestException {
		Optional<MealTypeDto> mealTypeResponseDto = mealTypeService.createAndUpdate(mealTypeDto);
		return RequestResponseUtils.generateResponseDto(mealTypeResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves a list of Meal Type records based on the provided filter criteria.
	 *
	 * @param filterDto The FilterDto object containing the filter criteria.
	 * @return A ResponseContainerDto containing a list of MealTypeDto objects matching the filter criteria.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEAL_TYPE + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<MealTypeDto>> read(FilterDto filterDto) {
		return mealTypeService.read(filterDto);
	}

	/**
	 * Updates an existing Meal Type record.
	 *
	 * @param mealTypeDto The MealTypeDto object containing the data for update.
	 * @return A ResponseContainerDto containing an Optional MealTypeDto, indicating the result of the updat	e.
	 * @throws RestException If an exception occurs during the operation.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEAL_TYPE + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<MealTypeDto>> update(@Valid @RequestBody MealTypeDto mealTypeDto) throws RestException {
		Optional<MealTypeDto> mealTypeResponseDto = mealTypeService.createAndUpdate(mealTypeDto);
		return RequestResponseUtils.generateResponseDto(mealTypeResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a Meal type record by its ID.
	 *
	 * @param idStr The ID of the Meal Type record to delete.
	 * @return A ResponseContainerDto containing null, indicating the successful deletion of the record.
	 * @throws RestException If an exception occurs during the operation or if the ID is invalid.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEAL_TYPE + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) throws RestException {
		if(!ValidationUtils.isNumber(idStr)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		mealTypeService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieve a list of active Meal Types.
	 * 
	 * This method retrieves all meal types that are marked as active (i.e., where the 'isActive' flag is true). 
	 * The method calls the {@link MealTypeService} to fetch the active meal types and returns them wrapped in a 
	 * standardized {@link ResponseContainerDto}.
	 * 
	 * The endpoint is secured by the {@link AuthorizeUserRights} annotation to ensure that only users with the appropriate rights
	 * (specifically the ability to add or edit meal orders) can access this endpoint.
	 * 
	 * @return A response container containing a list of active meal types.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<MealTypeDto>> readActive() {
		return RequestResponseUtils.generateResponseDto(mealTypeService.findByIsActiveTrue());
	}

}