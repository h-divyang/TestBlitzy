package com.catering.controller.tenant;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.FlagDto;
import com.catering.dto.tenant.request.NotesDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.NotesService;
import com.catering.util.RequestResponseUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This controller handles the operations related to notes.
 * It provides endpoints for creating, reading, updating, and deleting notes.
 * The controller is mapped under the API path defined in `ApiPathConstant.NOTES`.
 */
@RestController
@RequestMapping(value = ApiPathConstant.NOTES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotesController {

	/**
	 * The service used to handle the business logic related to notes.
	 * This service is injected into the controller via constructor injection.
	 */
	NotesService notesService;

	/**
	 * The service used for handling messages related to notes.
	 * This service is injected into the controller via constructor injection.
	 */
	MessageService messageService;

	/**
	 * TODO: API is used in the order booking reports. Which is located inside the main_menu of Order Execution.
	 * Service: OrderReportService
	 * Component: OrderBookingReportsNotesComponent
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<Optional<NotesDto>> create(@Valid @RequestBody NotesDto notesDto) throws RestException {
		Optional<NotesDto> notesDataDto = notesService.saveNotes(notesDto);
		return RequestResponseUtils.generateResponseDto(notesDataDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * TODO: API is used in the order booking reports. Which is located inside the main_menu of Order Execution.
	 * Service: OrderReportService
	 * Component: OrderBookingReportsNotesComponent
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<NotesDto> read(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id) {
		return RequestResponseUtils.generateResponseDto(notesService.getNotes(id));
	}

	/**
	 * TODO: API is used in the order booking reports. Which is located inside the main_menu of Order Execution.
	 * Service: OrderReportService
	 * Component: OrderBookingReportsNotesComponent
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public FlagDto getFlagForNotesAndReports(HttpServletRequest request) {
		return notesService.getFlagForNotesAndReports(request);
	}

}
