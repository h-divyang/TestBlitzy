package com.catering.dao.order.invoice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.OrderInvoiceCommonDtoForReportDto;
import com.catering.dto.tenant.request.OrderInvoiceFunctionResponseDto;
import com.catering.dto.tenant.request.OrderInvoiceReportDto;
import com.catering.dto.tenant.request.OrderInvoiceResponseDto;

/**
 * DAO (Data Access Object) interface for executing native queries related to order invoices.
 * Extends {@link JpaRepository} for basic CRUD operations.
 *
 * Provides methods for retrieving order invoice details, functions, generating reports, and finding common data for reports.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
public interface OrderInvoiceNativeQueryDao extends JpaRepository<OrderInvoiceNativeQuery, Long> {

	/**
	 * Retrieves an order invoice response based on the provided order ID.
	 *
	 * @param orderId The unique identifier of the order.
	 * @return An {@link OrderInvoiceResponseDto} containing order invoice details.
	 */
	@Query(name = "findOrderInvoiceByOrderId", nativeQuery = true)
	OrderInvoiceResponseDto findOrderInvoiceByOrderId(Long orderId);

	/**
	 * Retrieves a list of order invoice function responses based on the provided order ID.
	 *
	 * @param orderId The unique identifier of the order.
	 * @return A list of {@link OrderInvoiceFunctionResponseDto} containing order invoice function details.
	 */
	@Query(name = "findOrderInvoiceFunctionByOrderId", nativeQuery = true)
	List<OrderInvoiceFunctionResponseDto> findOrderInvoiceFunctionByOrderId(Long orderId);

	/**
	 * Retrieves the maximum bill number from the order invoices and increments it by one.
	 *
	 * @return The next available bill number.
	 */
	@Query(value = "SELECT IFNULL(MAX(CAST(bill_number AS UNSIGNED)), 0) + 1 FROM order_invoice", nativeQuery = true)
	Long getMaxBillNumber();

	/**
	 * Retrieves common order invoice data for generating reports based on the provided order ID and language type.
	 *
	 * @param orderId  The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @return An {@link OrderInvoiceCommonDtoForReportDto} containing common order invoice data for reporting.
	 */
	@Query(name = "findCommonOrderInvoiceDataForReportByOrderId", nativeQuery = true)
	OrderInvoiceCommonDtoForReportDto findCommonOrderInvoiceDataForReportByOrderId(Long orderId, Integer langType);

	/**
	 * Generates an order invoice report based on the provided order ID.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @return A list of {@link OrderInvoiceReportDto} containing data for generating the order invoice report.
	 */
	@Query(name = "findOrderInvoiceReportByOrderId", nativeQuery = true)
	List<OrderInvoiceReportDto> generateOrderInvoiceReport(Long orderId, Integer langType);

}