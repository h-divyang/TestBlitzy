package com.catering.dao.order.invoice;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.OrderInvoiceRequestDto;
import com.catering.dto.tenant.request.OrderInvoiceResponseDto;

/**
 * Service interface for handling native queries related to order invoices.
 * Defines methods for retrieving, creating or updating, and generating reports for order invoices.
 *
 * Implementing classes should provide the actual logic for these operations.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
public interface OrderInvoiceNativeQueryService {

	/**
	 * Retrieves an order invoice response based on the provided order ID.
	 *
	 * @param orderId The unique identifier of the order.
	 * @return An {@link OrderInvoiceResponseDto} containing order invoice details.
	 */
	OrderInvoiceResponseDto findOrderInvoiceByOrderId(Long orderId);

	/**
	 * Creates or updates an order invoice based on the provided request data.
	 *
	 * @param orderInvoiceRequestDto The request data for creating or updating an order invoice.
	 * @return An {@link OrderInvoiceRequestDto} representing the updated or newly created order invoice.
	 */
	OrderInvoiceRequestDto createAndUpdate(OrderInvoiceRequestDto orderInvoiceRequestDto, Long customerContactId);

	/**
	 * Generates an order invoice report for the specified order ID and optional parameters.
	 *
	 * @param orderId     The unique identifier of the order.
	 * @param langType    The language type for the report (optional).
	 * @param langCode    The language code for the report (optional).
	 * @param request     The HTTP servlet request.
	 * @return A {@link FileBean} representing the generated order invoice report.
	 */
	FileBean generateOrderInvoiceReport(Long orderId, Long customerContactId, Integer langType, String langCode, String reportName, HttpServletRequest request);

}