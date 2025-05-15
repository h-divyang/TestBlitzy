package com.catering.dao.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.DishCostingDto;
import com.catering.dto.tenant.request.DishCostingReportDto;
import com.catering.dto.tenant.request.OrderDtoForReport;
import com.catering.dto.tenant.request.TotalDishCostingReportDto;

/**
 * Repository interface for performing native SQL queries related to Book Orders.
 * This interface extends {@link JpaRepository} for CRUD operations and custom queries
 * involving BookOrderNativeQuery and related DTOs.
 */
public interface BookOrderNativeQueryDao extends JpaRepository<BookOrderNativeQuery, Long> {

	/**
	 * Retrieves the order details for a report based on the given order ID and language type.
	 * 
	 * @param orderId The ID of the order to be fetched.
	 * @param langType The language type for the report (e.g., 1 for English, 2 for Hindi).
	 * @return An {@link OrderDtoForReport} containing the order details.
	 */
	@Query(name = "findOrderForReport", nativeQuery = true)
	OrderDtoForReport findOrderForReport(Long orderId, Integer langType);

	/**
	 * Retrieves the dish costing details for a given order ID and adjusts quantity if required.
	 * 
	 * @param orderId The ID of the order.
	 * @param isAdjustQuantity A flag indicating whether to adjust the quantity for costing.
	 * @return A list of {@link DishCostingDto} containing the dish costing information.
	 */
	@Query(name = "dishCostingByOrderId", nativeQuery = true)
	List<DishCostingDto> dishCostingByOrderId(Long orderId, Boolean isAdjustQuantity);

	/**
	 * Generates a report for dish costing based on the order ID, language type, and quantity adjustment flag.
	 * 
	 * @param orderId The ID of the order.
	 * @param langType The language type for the report.
	 * @param isAdjustQuantity A flag indicating whether to adjust the quantity for the report.
	 * @return A list of {@link DishCostingReportDto} containing the detailed dish costing report.
	 */
	@Query(name = "generateDishCostingReport", nativeQuery = true)
	List<DishCostingReportDto> generateDishCostingReport(Long orderId, Integer langType, Boolean isAdjustQuantity);

	/**
	 * Generates a total dish costing report based on the order ID, language type, and quantity adjustment flag.
	 * 
	 * @param orderId The ID of the order.
	 * @param langType The language type for the report.
	 * @param isAdjustQuantity A flag indicating whether to adjust the quantity for the report.
	 * @return A list of {@link TotalDishCostingReportDto} containing the total dish costing report.
	 */
	@Query(name = "generateTotalDishCostingReport", nativeQuery = true)
	List<TotalDishCostingReportDto> generateTotalDishCostingReport(Long orderId, Integer langType, Boolean isAdjustQuantity);

	/**
	 * Retrieves the customer name for the table menu footer notes based on the given order ID.
	 * 
	 * @param orderId The ID of the order.
	 * @return A {@link DateWiseReportDropDownCommonDto} containing the customer name for footer notes.
	 */
	@Query(name = "getCustomerNameForTableMenuFooterNotes", nativeQuery = true)
	DateWiseReportDropDownCommonDto getCustomerNameForTableMenuFooterNotes(Long orderId);

}