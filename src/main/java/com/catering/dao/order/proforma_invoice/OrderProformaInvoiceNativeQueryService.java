package com.catering.dao.order.proforma_invoice;

import javax.servlet.http.HttpServletRequest;
import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.OrderProformaInvoiceRequestDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceResponseDto;

/**
 * Service interface for managing and retrieving Proforma Invoice data and related operations.
 * Provides methods to interact with the Proforma Invoice details, associated functions, 
 * and report generation based on the order ID.
 */
public interface OrderProformaInvoiceNativeQueryService {

	/**
	 * Retrieves an order proforma invoice response based on the provided order ID.
	 *
	 * @param orderId The unique identifier of the order.
	 * @return An {@link OrderProformaInvoiceResponseDto} containing order invoice details.
	 */
	OrderProformaInvoiceResponseDto findOrderProformaInvoiceByOrderId(Long orderId);

	/**
	 * Creates or updates an order proforma invoice based on the provided request data.
	 *
	 * @param orderProformaInvoiceRequestDto The request data for creating or updating an order invoice.
	 * @return An {@link OrderProformaInvoiceRequestDto} representing the updated or newly created order invoice.
	 */
	OrderProformaInvoiceRequestDto createAndUpdate(OrderProformaInvoiceRequestDto orderProformaInvoiceRequestDto, Long customerContactId);

	/**
	 * Generates an order proforma invoice report for the specified order ID and optional parameters.
	 *
	 * @param orderId     The unique identifier of the order.
	 * @param langType    The language type for the report (optional).
	 * @param langCode    The language code for the report (optional).
	 * @param request     The HTTP servlet request.
	 * @return A {@link FileBean} representing the generated order invoice report.
	 */
	FileBean generateOrderProformaInvoiceReport(Long orderId, Long customerContactId, Integer langType, String langCode, String reportName, HttpServletRequest request);

}