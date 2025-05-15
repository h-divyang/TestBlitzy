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
import com.catering.dto.tenant.request.EventTypeDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.EventTypeService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for managing Event Types. This class handles requests related to Event Type records, 
 * including creating, reading, updating, and deleting Event Types.
 * It also includes functionality for retrieving active Event Types from the system.
 * 
 * The controller interacts with the {@link EventTypeService} to perform operations on Event Types
 * and uses {@link MessageService} for message handling.
 */
@RestController
@RequestMapping(value = ApiPathConstant.EVENT_TYPE)
@Tag(name = SwaggerConstant.EVENT_TYPE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventTypeController {

	/**
	 * Service responsible for handling application messages. <br>
	 * It provides functionalities for retrieving and managing messages,
	 * which can be used for various purposes such as logging, notifications, or generating response messages in API endpoints.
	 */
	MessageService messageService;

	/**
	 * Service responsible for handling the business logic related to event types. <br>
	 * This service provides methods to perform CRUD (Create, Read, Update, Delete) operations
	 * on event type records in the system. It interacts with the database to persist event type data
	 * and includes the logic for validation and processing before interacting with the repository.
	 */
	EventTypeService eventTypeService;

	/**
	 * Creates a new Event Master.
	 *
	 * @param eventTypeDto The EventTypeDto object containing the data for creation.
	 * @return A ResponseContainerDto containing an Optional EventTypeDto, indicating the result of the creation.
	 * @throws RestException If an exception occurs during the operation.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.EVENT_TYPE + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<EventTypeDto>> create(@Valid @RequestBody EventTypeDto eventTypeDto) throws RestException {
		Optional<EventTypeDto> eventTypeResponseDto = eventTypeService.createAndUpdate(eventTypeDto);
		return RequestResponseUtils.generateResponseDto(eventTypeResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves a list of Event Master records based on the provided filter criteria.
	 *
	 * @param filterDto The FilterDto object containing the filter criteria.
	 * @return A ResponseContainerDto containing a list of EventTypeDto objects matching the filter criteria.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.EVENT_TYPE + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<EventTypeDto>> read(FilterDto filterDto) {
		return eventTypeService.read(filterDto);
	}

	/**
	 * Updates an existing Event Type record.
	 *
	 * @param eventTypeDto The EventTypeDto object containing the data for update.
	 * @return A ResponseContainerDto containing an Optional EventTypeDto, indicating the result of the update.
	 * @throws RestException If an exception occurs during the operation.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.EVENT_TYPE + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<EventTypeDto>> update(@Valid @RequestBody EventTypeDto eventTypeDto) throws RestException {
		Optional<EventTypeDto> eventTypeResponseDto = eventTypeService.createAndUpdate(eventTypeDto);
		return RequestResponseUtils.generateResponseDto(eventTypeResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a Event Master record by its ID.
	 *
	 * @param id The ID of the Event Master record to delete.
	 * @return A ResponseContainerDto containing null, indicating the successful deletion of the record.
	 * @throws RestException If an exception occurs during the operation or if the ID is invalid.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.EVENT_TYPE + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id) throws RestException {
		eventTypeService.deleteById(id);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves a list of active Event Types from the system.
	 * This method checks for active event types and returns them as a response.
	 * Only users with the appropriate rights (view permissions for Book Orders) 
	 * are authorized to access this data.
	 * 
	 * @return A {@link ResponseContainerDto} containing a list of {@link EventTypeDto} objects that represent the active event types in the system.
	 * 
	 * @throws RestException If any error occurs while fetching the event types.
	 * 
	 * @see EventTypeDto
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<EventTypeDto>> readActive() {
		return RequestResponseUtils.generateResponseDto(eventTypeService.findByIsActiveTrue());
	}

}