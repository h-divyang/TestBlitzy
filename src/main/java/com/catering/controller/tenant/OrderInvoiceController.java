package com.catering.controller.tenant;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.FileBean;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.order.invoice.OrderInvoiceNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.OrderInvoiceRequestDto;
import com.catering.dto.tenant.request.OrderInvoiceResponseDto;
import com.catering.service.common.MessageService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class for handling operations related to order invoices.
 * Provides endpoints for retrieving, saving, updating, and generating reports for order invoices.
 *
 * This controller is mapped to the base path {@link ApiPathConstant#ORDER_INVOICE}.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 * @version 1.0
 */
@RestController
@RequestMapping(value = ApiPathConstant.ORDER_INVOICE)
@Tag(name = SwaggerConstant.ORDER_INVOICE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderInvoiceController {

	/**
	 * Service to perform native query operations related to order invoices.
	 */
	OrderInvoiceNativeQueryService orderInvoiceNativeQueryService;

	/**
	 * Service to manage and retrieve localized messages for API responses.
	 */
	MessageService messageService;

	/**
	 * Retrieves an order invoice by its associated order ID.
	 *
	 * @param orderId The unique identifier of the order.
	 * @return A {@link ResponseContainerDto} containing the order invoice response data.
	 */
	@GetMapping(value = ApiPathConstant.ORDER_INVOICE_BY_ORDER)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<OrderInvoiceResponseDto> getOrderInvoiceByOrderId(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId) {
		return RequestResponseUtils.generateResponseDto(orderInvoiceNativeQueryService.findOrderInvoiceByOrderId(orderId));
	}

	/**
	 * Saves and updates an order invoice based on the provided request data.
	 *
	 * @param orderInvoiceRequestDto The request data for creating or updating an order invoice.
	 * @return A {@link ResponseContainerDto} containing the order invoice request data and a success message.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<OrderInvoiceRequestDto> saveAndUpdate(@Valid @RequestBody OrderInvoiceRequestDto orderInvoiceRequestDto, @RequestParam(name = FieldConstants.CUSTOMER_CONTACT_ID) Long customerContactId) {
		return RequestResponseUtils.generateResponseDto(orderInvoiceNativeQueryService.createAndUpdate(orderInvoiceRequestDto, customerContactId), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Generates a report for an order invoice based on the provided order ID and
	 * optional parameters.
	 *
	 * @param orderId     The unique identifier of the order.
	 * @param langType    The language type for the report (optional).
	 * @param langCode    The language code for the report (optional).
	 * @param currentDate The current date for the report (optional).
	 * @param request     The HTTP servlet request.
	 * @return A {@link ResponseContainerDto} containing the generated report file as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.ORDER_INVOICE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> orderInvoiceReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.CUSTOMER_CONTACT_ID) Long customerContactId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = orderInvoiceNativeQueryService.generateOrderInvoiceReport(orderId, customerContactId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

}