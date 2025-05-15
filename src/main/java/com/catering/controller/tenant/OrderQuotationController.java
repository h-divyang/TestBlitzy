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
import com.catering.dao.order.quotation.OrderQuotationNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.OrderQuotationRequestDto;
import com.catering.dto.tenant.request.OrderQuotationResponseDto;
import com.catering.service.common.MessageService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The {@link OrderQuotationController} is responsible for managing order quotations in the system.
 * It provides endpoints to create, retrieve, and generate reports for order quotations. 
 * This controller serves as the interface between the frontend and the backend, allowing users to interact with 
 * the quotations of book orders, update them, and generate reports based on the provided data.
 * 
 * <p>This controller includes:</p>
 * <ul>
 *   <li>Fetching order quotations based on an order ID.</li>
 *   <li>Creating and updating order quotations.</li>
 *   <li>Generating printable reports of order quotations.</li>
 * </ul>
 * 
 * <p>Each endpoint ensures that only authorized users with the appropriate rights (view, add, edit, print) 
 * can perform actions related to order quotations.</p>
 * 
 * <p>Endpoints:</p>
 * <ul>
 *   <li><b>GET /order-quotation/{orderId}</b> - Retrieves the order quotation associated with the provided order ID.</li>
 *   <li><b>POST /order-quotation</b> - Creates or updates an order quotation.</li>
 *   <li><b>GET /order-quotation/report/{orderId}</b> - Generates a report for an order quotation, downloadable as a byte array.</li>
 * </ul>
 */
@RestController
@RequestMapping(value = ApiPathConstant.ORDER_QUOTATION)
@Tag(name = SwaggerConstant.ORDER_QUOTATION)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderQuotationController {

	/**
	 * Service responsible for creating and updating order quotations.
	 */
	OrderQuotationNativeQueryService orderQuotationNativeQueryService;

	/**
	 * Service for retrieving messages for system responses.
	 */
	MessageService messageService;

	/**
	 * Retrieves the order quotation associated with the provided order ID.
	 * This method fetches an order quotation, allowing the user to view the details of the quotation
	 * for the specified order ID.
	 * 
	 * @param orderId the ID of the order whose quotation you want to retrieve.
	 * @return A {@link ResponseContainerDto} containing the {@link OrderQuotationResponseDto} with the order's quotation details.
	 */
	@GetMapping(value = ApiPathConstant.ORDER_QUOTATION_BY_ORDER)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<OrderQuotationResponseDto> getOrderQuotationByOrderId(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId) {
		return RequestResponseUtils.generateResponseDto(orderQuotationNativeQueryService.findOrderQuotationByOrderId(orderId));
	}

	/**
	 * Creates or updates an order quotation.
	 * This method allows the user to save or update the order quotation associated with a given customer contact ID.
	 * 
	 * @param orderQuotationRequestDto the data for the order quotation to be created or updated.
	 * @param customerContactId the ID of the customer contact for which the order quotation is to be created or updated.
	 * @return A {@link ResponseContainerDto} containing the {@link OrderQuotationRequestDto} with the created/updated quotation details.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<OrderQuotationRequestDto> saveAndUpdate(@Valid @RequestBody OrderQuotationRequestDto orderQuotationRequestDto, @RequestParam(name = FieldConstants.CUSTOMER_CONTACT_ID) Long customerContactId) {
		return RequestResponseUtils.generateResponseDto(orderQuotationNativeQueryService.createAndUpdate(orderQuotationRequestDto, customerContactId), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Generates a printable report for an order quotation.
	 * This method generates a downloadable report for the order quotation, based on the provided order ID and customer contact ID. 
	 * Optionally, it supports specifying the language type and code for the report.
	 * 
	 * @param orderId the ID of the order whose quotation report is to be generated.
	 * @param customerContactId the ID of the customer contact associated with the order.
	 * @param langType optional language type for the report.
	 * @param langCode optional language code for the report.
	 * @param request the HTTP request for generating the report.
	 * @return A {@link ResponseContainerDto} containing a byte array representing the generated report.
	 */
	@GetMapping(value = ApiPathConstant.ORDER_QUOTATION_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> orderQuotationReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.CUSTOMER_CONTACT_ID) Long customerContactId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = orderQuotationNativeQueryService.generateOrderQuotationReport(orderId, customerContactId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

}