package com.catering.dao.order.quotation;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.OrderQuotationRequestDto;
import com.catering.dto.tenant.request.OrderQuotationResponseDto;

/**
 * Service interface for managing and processing order quotations.
 * 
 * <p>This interface provides methods for creating, updating, retrieving, and generating reports related to order quotations.</p>
 * 
 * <p>It includes the following functionalities:</p>
 * <ul>
 * <li>Creating or updating an order quotation.</li>
 * <li>Retrieving order quotation details by order ID.</li>
 * <li>Generating an order quotation report with customizable language and format.</li>
 * </ul>
 */
public interface OrderQuotationNativeQueryService {

	/**
	 * Creates or updates an order quotation based on the provided request data.
	 * 
	 * @param orderQuotationRequestDto the data transfer object containing order quotation details.
	 * @param customerContactId the ID of the customer contact associated with the order quotation.
	 * @return the updated or newly created {@link OrderQuotationRequestDto} with persisted details.
	 */
	OrderQuotationRequestDto createAndUpdate(OrderQuotationRequestDto orderQuotationRequestDto, Long customerContactId);

	/**
	 * Retrieves the details of an order quotation by its order ID.
	 * 
	 * @param orderId the ID of the order for which the quotation is to be retrieved.
	 * @return an instance of {@link OrderQuotationResponseDto} containing the order quotation details.
	 */
	OrderQuotationResponseDto findOrderQuotationByOrderId(Long orderId);

	/**
	 * Generates an order quotation report for a specific order and customer.
	 * 
	 * @param orderId the ID of the order for which the report is to be generated.
	 * @param customerContactId the ID of the customer contact associated with the order.
	 * @param langType the language type for the report (e.g., English, Hindi).
	 * @param langCode the language code for the report (e.g., "en", "hi").
	 * @param request the HTTP servlet request containing context for report generation.
	 * @return an instance of {@link FileBean} containing the generated report file details.
	 */
	FileBean generateOrderQuotationReport(Long orderId, Long customerContactId, Integer langType, String langCode, String reportName, HttpServletRequest request);

}