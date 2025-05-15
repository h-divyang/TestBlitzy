package com.catering.dao.menu_allocation_type;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import com.catering.dto.tenant.request.MenuAllocationDtoForNativeQuery;
import com.catering.dto.tenant.request.MenuAllocationTypeForNativeQuery;

/**
 * Interface for performing native queries related to menu allocation and preparation.
 * Extends {@link JpaRepository} to leverage CRUD operations for {@link MenuAllocationNativeQuery} entities.
 * Includes queries for fetching menu preparation items, allocation types, and procedures 
 * for managing menu allocations.
 */
public interface MenuAllocationNativeQueryDao extends JpaRepository<MenuAllocationNativeQuery, Long> {

	/**
	 * Retrieves the menu preparation items associated with a specific order ID.
	 * This query returns a list of menu preparation items and their details, 
	 * such as order type and order date, mapped to {@link MenuAllocationDtoForNativeQuery}.
	 * 
	 * @param orderId The ID of the customer order whose menu preparation items are being fetched.
	 * @return A list of {@link MenuAllocationDtoForNativeQuery} containing the details of the menu preparation items.
	 */
	@Query(name = "findOrderMenuPreparationMenuItemByOrderId", nativeQuery = true)
	List<MenuAllocationDtoForNativeQuery> findOrderMenuPreparationMenuItemByOrderId(Long orderId);

	/**
	 * Retrieves the menu allocation types for a specific order ID.
	 * This query returns a list of menu allocation types, including counter and helper details,
	 * mapped to {@link MenuAllocationTypeForNativeQuery}.
	 * 
	 * @param orderId The ID of the customer order whose menu allocation types are being fetched.
	 * @return A list of {@link MenuAllocationTypeForNativeQuery} containing the details of the menu allocation types.
	 */
	@Query(name = "findMenuAllocationTypeByOrderId", nativeQuery = true)
	List<MenuAllocationTypeForNativeQuery> findMenuAllocationTypeByOrderId(Long orderId);

	/**
	 * Executes the stored procedure {@code manageMenuAllocationInsert} to manage the account balance and history
	 * during the insertion of menu allocation data. This procedure includes parameters for menu item allocation 
	 * such as order type, contact ID, counter details, helper details, and pricing information.
	 * 
	 * @param orderType The type of the order (e.g., dine-in, take-out).
	 * @param contactId The ID of the contact associated with the order.
	 * @param counterNo The counter number associated with the allocation.
	 * @param counterPrice The price for the counter allocation.
	 * @param helperNo The helper number associated with the allocation.
	 * @param helperPrice The price for the helper allocation.
	 * @param quantity The quantity of the menu item.
	 * @param price The price for the menu item.
	 * @param orderDate The date and time of the order.
	 * @param id The ID of the menu allocation.
	 */
	@Procedure(procedureName = "manageMenuAllocationInsert")
	void manageAccountBalanceAndHistory(
			Integer orderType,
			Long contactId,
			Integer counterNo,
			Double counterPrice,
			Integer helperNo,
			Double helperPrice,
			Double quantity,
			Double price,
			LocalDateTime orderDate,
			Long id);

	/**
	* Executes the stored procedure {@code manageMenuAllocationUpdate} to update an existing menu allocation.
	* This procedure includes both new and old values for the menu allocation data to track changes and 
	* perform necessary updates in the account balance and history.
	* 
	* @param id The ID of the menu allocation being updated.
	* @param orderDate The updated order date and time.
	* @param orderType The updated order type (e.g., dine-in, take-out).
	* @param contactId The updated contact ID.
	* @param counterNo The updated counter number.
	* @param counterPrice The updated price for the counter allocation.
	* @param helperNo The updated helper number.
	* @param helperPrice The updated price for the helper allocation.
	* @param quantity The updated quantity of the menu item.
	* @param price The updated price for the menu item.
	* @param oldId The previous ID of the menu allocation.
	* @param oldOrderDate The previous order date and time.
	* @param oldOrderType The previous order type.
	* @param oldContactId The previous contact ID.
	* @param oldCounterNo The previous counter number.
	* @param oldCounterPrice The previous counter price.
	* @param oldHelperNo The previous helper number.
	* @param oldHelperPrice The previous helper price.
	* @param oldQuantity The previous quantity of the menu item.
	* @param oldPrice The previous price for the menu item.
	*/
	@Procedure(procedureName = "manageMenuAllocationUpdate")
	void updateMenuAllocation(
			Long id,
			LocalDateTime orderDate,
			Integer orderType,
			Long contactId,
			Integer counterNo,
			Double counterPrice,
			Integer helperNo,
			Double helperPrice,
			Double quantity,
			Double price,
			Long oldId,
			LocalDateTime oldOrderDate,
			Integer oldOrderType,
			Long oldContactId,
			Integer oldCounterNo,
			Double oldCounterPrice,
			Integer oldHelperNo,
			Double oldHelperPrice,
			Double oldQuantity,
			Double oldPrice);
}