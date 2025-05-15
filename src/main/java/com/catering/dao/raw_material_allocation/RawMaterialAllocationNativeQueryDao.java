package com.catering.dao.raw_material_allocation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import com.catering.dto.tenant.request.RawMaterialAllocationDto;
import com.catering.dto.tenant.request.RawMaterialAllocationMenuItemDto;
import com.catering.dto.tenant.request.RawMaterialAllocationRawMaterialDto;

/**
 * Repository interface for performing native SQL queries related to Raw Material Allocation.
 * Extends JpaRepository to provide basic CRUD operations for the entity {@link RawMaterialAllocationNativeQuery}.
 * 
 * This interface includes various methods to fetch data and invoke stored procedures that are specifically related 
 * to raw material allocation, menu items, and customer orders.
 * 
 * Methods:
 * - findItemCategoryByOrderId: Fetches a list of item categories associated with a specific order.
 * - findRawMaterialByOrderIdAndItemCategoryIdResult: Retrieves raw material information based on order and item category IDs.
 * - findMenuItemByOrderIdAndRawMaterialId: Finds menu items linked to a specific order and raw material.
 * - calculateExtraEventRawMaterial: Calls the stored procedure to manage additional raw material allocations for events.
 * 
 * Note: The queries used are native SQL queries defined in the entity's {@link NamedNativeQuery}.
 */
public interface RawMaterialAllocationNativeQueryDao extends JpaRepository<RawMaterialAllocationNativeQuery, Long> {

	/**
	 * Fetches a list of item categories associated with a specific order.
	 * 
	 * @param orderId The ID of the order for which item categories need to be retrieved.
	 * @return A list of {@link RawMaterialAllocationDto} representing the item categories for the specified order.
	 */
	@Query(name = "findItemCategoryByOrderId", nativeQuery = true)
	List<RawMaterialAllocationDto> findItemCategoryByOrderId(Long orderId);

	/**
	 * Retrieves raw material information based on the provided order ID and item category ID.
	 * This method also considers whether the raw material quantity should be adjusted.
	 * 
	 * @param orderId The ID of the order for which raw materials need to be retrieved.
	 * @param rawMaterialCategoryId The ID of the raw material category for filtering the results.
	 * @param isAdjustQuantity A boolean flag indicating whether the raw material quantities should be adjusted.
	 * @return A list of {@link RawMaterialAllocationRawMaterialDto} representing the raw material details 
	 *         for the specified order and item category, considering the adjustment flag.
	 */
	@Query(name = "findRawMaterialByOrderIdAndItemCategoryId", nativeQuery = true)
	List<RawMaterialAllocationRawMaterialDto> findRawMaterialByOrderIdAndItemCategoryIdResult(Long orderId, Long rawMaterialCategoryId, Boolean isAdjustQuantity);

	/**
	 * Finds the menu items linked to a specific order and raw material.
	 * 
	 * @param orderId The ID of the order for which menu items need to be fetched.
	 * @param rawMaterialCategoryId The ID of the raw material category for filtering menu items.
	 * @return A list of {@link RawMaterialAllocationMenuItemDto} representing the menu items related to the given order 
	 *         and raw material category.
	 */
	@Query(name = "findMenuItemByOrderIdAndRawMaterialId", nativeQuery = true)
	List<RawMaterialAllocationMenuItemDto> findMenuItemByOrderIdAndRawMaterialId(Long orderId, Long rawMaterialCategoryId);

	/**
	 * Invokes a stored procedure to manage additional raw material allocations for events.
	 * This procedure adjusts the raw material allocation based on the event requirements.
	 * 
	 * @param orderId The ID of the order for which the raw material allocation is to be adjusted.
	 * @param isAdjustQuantity A flag that determines whether the raw material quantity should be adjusted during the procedure.
	 */
	@Procedure(procedureName = "manageRawMaterialAllocationExtra")
	void calculateExtraEventRawMaterial(Long orderId, Boolean isAdjustQuantity);

}