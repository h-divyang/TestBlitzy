package com.catering.dao.order.proforma_invoice;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.catering.dto.tenant.request.OrderInvoiceReportDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceFunctionResponseDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceResponseDto;

/**
 * Repository interface for managing and executing native queries related to 
 * the {@link OrderProformaInvoiceNativeQuery} entity.
 * 
 * Provides custom methods for retrieving Proforma Invoice data, including 
 * associated functions and report generation, using native SQL queries.
 */
public interface OrderProformaInvoiceNativeQueryDao extends JpaRepository<OrderProformaInvoiceNativeQuery, Long> {

	/**
	 * Retrieves a Proforma Invoice by its associated Order ID.
	 * 
	 * @param orderId the ID of the order for which the Proforma Invoice is to be retrieved.
	 * @return an {@link OrderProformaInvoiceResponseDto} containing the Proforma Invoice details.
	 */
	@Query(name = "findOrderProformaInvoiceByOrderId", nativeQuery = true)
	OrderProformaInvoiceResponseDto findOrderProformaInvoiceByOrderId(Long orderId);

	/**
	 * Retrieves the functions associated with a Proforma Invoice by Order ID.
	 * 
	 * @param orderId the ID of the order for which associated functions are to be retrieved.
	 * @return a list of {@link OrderProformaInvoiceFunctionResponseDto} representing the associated functions.
	 */
	@Query(name = "findOrderProformaInvoiceFunctionByOrderId", nativeQuery = true)
	List<OrderProformaInvoiceFunctionResponseDto> findOrderProformaInvoiceFunctionByOrderId(Long orderId);

	/**
	 * Generates a report for a Proforma Invoice by Order ID.
	 * 
	 * @param orderId the ID of the order for which the Proforma Invoice report is to be generated.
	 * @param langType the language type code for localizing the report.
	 * @return a list of {@link OrderInvoiceReportDto} containing the Proforma Invoice report data.
	 */
	@Query(name = "findOrderProformaInvoiceReportByOrderId", nativeQuery = true)
	List<OrderInvoiceReportDto> generateOrderProformaInvoiceReport(Long orderId, Integer langType);

}