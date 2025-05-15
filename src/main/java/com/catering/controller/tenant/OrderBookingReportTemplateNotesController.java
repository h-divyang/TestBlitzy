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
import com.catering.dto.tenant.request.OrderBookingTemplateNotesDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.OrderBookingReportTemplateNotesService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST Controller for managing Order Booking Report Template Notes.
 * <p>
 * This controller provides APIs to handle operations related to order booking report template notes, 
 * including creating and retrieving notes.
 * </p>
 * 
 * <p>
 * The endpoints are secured with specific user rights, ensuring that only authorized users can perform 
 * create or read operations.
 * </p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.ORDER_BOOKING_REPORT_TEMPLATE_NOTES)
@Tag(name = SwaggerConstant.ORDER_BOOKING_REPORT_TEMPLATE_NOTES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderBookingReportTemplateNotesController {

	/**
	 * Service to manage and retrieve localized messages for API responses.
	 */
	MessageService messageService;

	/**
	 * Service to handle operations related to order booking report template notes.
	 */
	OrderBookingReportTemplateNotesService orderBookingReportTemplateNotesService;

	/**
	 * Creates or updates order booking report template notes.
	 * <p>
	 * This endpoint allows authorized users to add new notes or update existing notes 
	 * associated with an order booking report template.
	 * </p>
	 *
	 * @param orderBookingTemplateNotesDto the data for the template notes, encapsulated in a {@link OrderBookingTemplateNotesDto}.
	 * @return a {@link ResponseContainerDto} containing the created or updated {@link OrderBookingTemplateNotesDto}.
	 * @throws RestException if there is an issue during the creation or update process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORT_TEMPLATE_NOTES + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.ORDER_BOOKING_REPORT_TEMPLATE_NOTES + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Optional<OrderBookingTemplateNotesDto>> create(@Valid @RequestBody OrderBookingTemplateNotesDto orderBookingTemplateNotesDto) throws RestException {
		Optional<OrderBookingTemplateNotesDto> bookingTemplateNotesDto = orderBookingReportTemplateNotesService.saveOrderBookingTemplateNotes(orderBookingTemplateNotesDto);
		return RequestResponseUtils.generateResponseDto(bookingTemplateNotesDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves order booking report template notes.
	 * <p>
	 * This endpoint allows authorized users to fetch the notes associated with an order booking report template.
	 * </p>
	 *
	 * @return a {@link ResponseContainerDto} containing the {@link OrderBookingTemplateNotesDto}.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORT_TEMPLATE_NOTES + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<OrderBookingTemplateNotesDto> read() {
		return RequestResponseUtils.generateResponseDto(orderBookingReportTemplateNotesService.getOrderBookingTemplateNotes());
	}

}