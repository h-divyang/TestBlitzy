package com.catering.dao.raw_material_allocation;

import java.util.List;

import com.catering.dto.tenant.request.RawMaterialAllocationDto;
import com.catering.dto.tenant.request.RawMaterialAllocationRawMaterialDto;

/**
 * Service interface for managing raw material allocation queries.
 * This interface provides methods for querying and calculating raw material allocation data 
 * related to specific orders, raw material categories, and menu items.
 */
public interface RawMaterialAllocationNativeQueryService {

	 /**
	 * Retrieves the list of raw material categories associated with a specific order.
	 * 
	 * @param orderId The ID of the order for which the raw material categories need to be fetched.
	 * @return A list of {@link RawMaterialAllocationDto} representing the raw material categories for the given order.
	 */
	List<RawMaterialAllocationDto> finRawMaterialCategoryByOrderId(Long orderId);

	/**
	 * Finds raw material allocation details for a specific order and raw material category.
	 * 
	 * @param orderId The ID of the order for which raw material allocations need to be retrieved.
	 * @param rawMaterialCategoryId The ID of the raw material category for which allocation data should be fetched.
	 * @return A list of {@link RawMaterialAllocationRawMaterialDto} representing the raw material allocation details 
	 *         for the specified order and raw material category.
	 */
	List<RawMaterialAllocationRawMaterialDto> findRawMaterialAllocationByOrderId(Long orderId, Long rawMaterialCategoryId);

	/**
	 * Triggers the calculation of extra raw material allocation for a specific order.
	 * This method handles the adjustments and additional allocations for the raw materials.
	 * 
	 * @param orderId The ID of the order for which extra raw material allocation needs to be calculated.
	 */
	void calculateExtraRawMaterialAllocation(Long orderId);

}