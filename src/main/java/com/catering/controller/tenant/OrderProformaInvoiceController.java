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
import com.catering.dao.order.proforma_invoice.OrderProformaInvoiceNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceRequestDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceResponseDto;
import com.catering.service.common.MessageService;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The {@link OrderProformaInvoiceController} is responsible for managing proforma invoices in the system.
 * It provides endpoints to retrieve proforma invoices based on order details, create new proforma invoices, 
 * and manage proforma invoice-related data.
 * <p>It ensures that only authorized users can perform actions related to proforma invoices, such as viewing,
 * creating, and updating them, using appropriate user roles and permissions.</p>
 *
 * <p>Dependencies:</p>
 * <ul>
 *   <li>{@link OrderProformaInvoiceNativeQueryService} - Provides the necessary methods for querying proforma invoices 
 *       from the database, including fetching proforma invoices by order and generating reports.</li>
 *   <li>{@link MessageService} - Fetches system messages used in responses to provide consistent and localized feedback.</li>
 * </ul>
 * 
 * <p>Endpoints provided by this controller:</p>
 * <ul>
 *   <li><b>GET /proforma-invoice/{orderId}</b> - Retrieves a proforma invoice based on the order ID.</li>
 *   <li><b>POST /proforma-invoice</b> - Creates a new proforma invoice based on provided data.</li>
 *   <li><b>GET /proforma-invoice/report/{orderId}</b> - Generates a downloadable report of the proforma invoice.</li>
 * </ul>
 */
@RestController
@RequestMapping(value = ApiPathConstant.ORDER_PROFORMA_INVOICE)
@Tag(name = SwaggerConstant.ORDER_PROFORMA_INVOICE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderProformaInvoiceController {

	/**
	 * Service to handle queries and reports related to proforma invoices.
	 */
	OrderProformaInvoiceNativeQueryService orderProformaInvoiceNativeQueryService;

	/**
	 * Service for fetching messages for responses, ensuring consistent and localized feedback.
	 */
	MessageService messageService;

	/**
	 * Retrieves an order proforma invoice by its associated order ID.
	 *
	 * @param orderId The unique identifier of the order.
	 * @return A {@link ResponseContainerDto} containing the order proforma invoice response data.
	 */
	@GetMapping(value = ApiPathConstant.ORDER_PROFORMA_INVOICE_BY_ORDER)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<OrderProformaInvoiceResponseDto> getOrderProformaInvoiceByOrderId(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId) {
		return RequestResponseUtils.generateResponseDto(orderProformaInvoiceNativeQueryService.findOrderProformaInvoiceByOrderId(orderId));
	}

	/**
	 * Saves and updates an order proforma invoice based on the provided request data.
	 *
	 * @param orderProformaInvoiceRequestDto The request data for creating or updating an order invoice.
	 * @return A {@link ResponseContainerDto} containing the order invoice request data and a success message.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<OrderProformaInvoiceRequestDto> saveAndUpdate(@Valid @RequestBody OrderProformaInvoiceRequestDto orderProformaInvoiceRequestDto, @RequestParam(name = FieldConstants.CUSTOMER_CONTACT_ID) Long customerContactId) {
		return RequestResponseUtils.generateResponseDto(orderProformaInvoiceNativeQueryService.createAndUpdate(orderProformaInvoiceRequestDto, customerContactId), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Generates a report for an order proforma invoice based on the provided order ID and
	 * optional parameters.
	 *
	 * @param orderId     The unique identifier of the order.
	 * @param langType    The language type for the report (optional).
	 * @param langCode    The language code for the report (optional).
	 * @param currentDate The current date for the report (optional).
	 * @param request     The HTTP servlet request.
	 * @return A {@link ResponseContainerDto} containing the generated report file as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.ORDER_PROFORMA_INVOICE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> orderProformaInvoiceReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.CUSTOMER_CONTACT_ID) Long customerContactId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = orderProformaInvoiceNativeQueryService.generateOrderProformaInvoiceReport(orderId, customerContactId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

}