package com.catering.controller.tenant;

import java.util.Optional;

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
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.TableMenuReportNotesDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.TableMenuReportHeaderNotesService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for handling requests related to Table Menu Report Header Notes.
 * This includes creating and reading table menu report notes associated with order bookings.
 */
@RestController
@RequestMapping(value = ApiPathConstant.TABLE_MENU_REPORT_HEADER_NOTES)
@Tag(name = SwaggerConstant.TABLE_MENU_REPORT_HEADER_NOTES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableMenuReportHeaderNotesController {

	/**
	 * Service for handling operations related to table menu report header notes.
	 * Provides functionality for saving, retrieving, and managing the notes associated with table menu reports.
	 */
	TableMenuReportHeaderNotesService tableMenuReportHeaderNotesService;

	/**
	 * Service for handling messaging operations.
	 * Provides functionality for sending and managing messages within the application.
	 */
	MessageService messageService;

	/**
	 * Creates a new table menu report header note. This method is used to save the notes for a table menu report.
	 *
	 * @param tableMenuHeaderNotesDto the data transfer object containing the table menu report notes.
	 * @return a {@link ResponseContainerDto} containing an {@link Optional} of {@link TableMenuReportNotesDto}.
	 * @throws RestException if there is an issue with saving the report notes.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Optional<TableMenuReportNotesDto>> create(@Valid @RequestBody TableMenuReportNotesDto tableMenuHeaderNotesDto) throws RestException {
		Optional<TableMenuReportNotesDto> tableMenuReportHeaderNotesDto = tableMenuReportHeaderNotesService.saveTableMenuReportHeaderNotes(tableMenuHeaderNotesDto);
		return RequestResponseUtils.generateResponseDto(tableMenuReportHeaderNotesDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves the table menu report header notes associated with a specific order ID.
	 *
	 * @param orderId the ID of the order to retrieve notes for.
	 * @return a {@link ResponseContainerDto} containing the {@link TableMenuReportNotesDto}.
	 */
	@GetMapping(value = ApiPathConstant.ORDER_ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<TableMenuReportNotesDto> read(@PathVariable(name = FieldConstants.COMMON_FIELD_ORDER_ID) Long orderId) {
		return RequestResponseUtils.generateResponseDto(tableMenuReportHeaderNotesService.getTableMenuReportHeaderNotes(orderId));
	}

}