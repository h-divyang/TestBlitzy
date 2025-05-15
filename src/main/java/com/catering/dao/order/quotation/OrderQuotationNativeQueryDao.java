package com.catering.dao.order.quotation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.OrderQuotationFunctionResponseDto;
import com.catering.dto.tenant.request.OrderQuotationReportDto;
import com.catering.dto.tenant.request.OrderQuotationResponseDto;

/**
 * Data Access Object (DAO) interface for handling operations related to the `OrderQuotationNativeQuery` entity.
 * 
 * <p>This interface extends {@link JpaRepository}, providing CRUD operations and custom query methods 
 * for managing order quotations in the database.</p>
 * 
 * <p>It includes the following custom native query methods:</p>
 * <ul>
 *   <li>{@code findOrderQuotationByOrderId(Long orderId)}: Retrieves a single order quotation response based on the provided order ID.</li>
 *   <li>{@code findOrderQuotationFunctionByOrderId(Long orderId)}: Retrieves a list of functions related to the order quotation based on the provided order ID.</li>
 *   <li>{@code generateOrderQuotationReport(Long orderId, Integer langType)}: Generates a report of the order quotation based on the order ID and language type.</li>
 * </ul>
 * 
 * <p>Each method is associated with a named native query defined in the database.</p>
 * 
 * @see OrderQuotationNativeQuery
 * @see JpaRepository
 */
public interface OrderQuotationNativeQueryDao extends JpaRepository<OrderQuotationNativeQuery, Long> {

	/**
	 * Retrieves a single order quotation response based on the provided order ID.
	 * 
	 * @param orderId the ID of the order for which the quotation is to be retrieved.
	 * @return an instance of {@link OrderQuotationResponseDto} containing the order quotation details.
	 */
	@Query(name = "findOrderQuotationByOrderId", nativeQuery = true)
	OrderQuotationResponseDto findOrderQuotationByOrderId(Long orderId);

	/**
	 * Retrieves a list of functions related to the order quotation based on the provided order ID.
	 * 
	 * @param orderId the ID of the order for which the quotation functions are to be retrieved.
	 * @return a list of {@link OrderQuotationFunctionResponseDto} containing the function details.
	 */
	@Query(name = "findOrderQuotationFunctionByOrderId", nativeQuery = true)
	List<OrderQuotationFunctionResponseDto> findOrderQuotationFunctionByOrderId(Long orderId);

	/**
	 * Generates a report of the order quotation based on the order ID and language type.
	 * 
	 * @param orderId the ID of the order for which the quotation report is to be generated.
	 * @param langType the language type for the report.
	 * @return a list of {@link OrderQuotationReportDto} containing the report details.
	 */
	@Query(name = "generateOrderQuotationReport", nativeQuery = true)
	List<OrderQuotationReportDto> generateOrderQuotationReport(Long orderId, Integer langType);

}