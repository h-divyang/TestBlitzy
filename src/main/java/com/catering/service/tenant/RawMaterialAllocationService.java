package com.catering.service.tenant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.catering.dto.tenant.request.AgencyAllocationDto;
import com.catering.dto.tenant.request.CommonRawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialAllocationFromRawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialAllocationRequestDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.RawMaterialAllocationModel;

/**
 * Service interface for managing raw material allocations. This interface provides
 * methods to update, read, create, delete, and synchronize raw materials, as well
 * as check the existence of certain entities. It acts as an abstraction layer for
 * handling raw material allocation operations.
 */
public interface RawMaterialAllocationService {

	/**
	 * Updates the raw material allocations associated with a specific order.
	 *
	 * @param orderRawMaterialDtos A list of RawMaterialAllocationFromRawMaterialDto objects containing details of the raw material allocations to be updated.
	 * @param orderId The unique identifier of the order for which the raw material allocations are to be updated.
	 */
	void update(List<RawMaterialAllocationFromRawMaterialDto> orderRawMaterialDtos, Long orderId);

	/**
	 * Reads raw material allocation requests associated with the given menu preparation menu item ID.
	 *
	 * @param menuPreparationMenuItemId The unique identifier of the menu preparation menu item.
	 * @return An Optional containing a list of RawMaterialAllocationRequestDto objects if found, or an empty Optional otherwise.
	 */
	Optional<List<RawMaterialAllocationRequestDto>> read(Long menuPreparationMenuItemId);

	/**
	 * Creates and updates raw material allocation requests for a specific order.
	 *
	 * @param orderRawMaterialDtos A list of RawMaterialAllocationRequestDto objects representing the raw material allocation requests to be created or updated.
	 * @param orderId The unique identifier of the order associated with the raw material allocation requests.
	 * @return A list of RawMaterialAllocationRequestDto objects after creation or update.
	 */
	List<RawMaterialAllocationRequestDto> createAndUpdate(List<RawMaterialAllocationRequestDto> orderRawMaterialDtos, Long orderId);

	/**
	 * Deletes a raw material allocation associated with the specified identifiers.
	 *
	 * @param id The unique identifier of the raw material allocation to be deleted.
	 * @param orderId The unique identifier of the order associated with the raw material allocation.
	 * @throws RestException if there is an error during the deletion process or if the raw material allocation cannot be found.
	 */
	void deleteById(Long id, Long orderId) throws RestException;

	/**
	 * Allocates raw materials to agencies for a specific order.
	 *
	 * @param agencyAllocationDtoList A list of AgencyAllocationDto objects detailing the raw material allocation for each agency.
	 * @param orderId The unique identifier of the order for which the allocations are to be made.
	 */
	void agencyAllocation(List<AgencyAllocationDto> agencyAllocationDtoList, Long orderId);

	/**
	 * Retrieves a list of RawMaterialAllocationModel entities associated with the given menu preparation menu item ID.
	 *
	 * @param id The unique identifier of the menu preparation menu item.
	 * @return A list of RawMaterialAllocationModel entities that match the specified menu preparation menu item ID.
	 */
	List<RawMaterialAllocationModel> findByMenuPreparationMenuItemId(Long id);

	/**
	 * Synchronizes the raw material details with the given parameters.
	 *
	 * @param menuPreparationMenuItemId The unique identifier of the menu preparation menu item.
	 * @param menuItemRawMaterialId The unique identifier of the menu item raw material.
	 * @param actualQty The actual quantity of the raw material used.
	 * @param actualMeasurementId The unique identifier of the measurement unit used for the raw material.
	 * @param rawMaterialCategoryId The unique identifier of the raw material category.
	 * @param orderTime The timestamp when the order was placed.
	 * @param rawMaterialId The unique identifier of the raw material.
	 */
	void syncRawMaterial(Long orderId, Long menuPreparationMenuItemId, Long menuItemRawMaterialId, Double actualQty, Long actualMeasurementId, Long rawMaterialCategoryId, LocalDateTime orderTime, Long rawMaterialId);

	/**
	 * Checks whether a record exists with the specified godown ID.
	 *
	 * @param id The unique identifier of the godown.
	 * @return true if a record exists with the specified godown ID, false otherwise.
	 */
	boolean existsByGodownId(Long id);

	/**
	 * Retrieves the smallest measurement value based on the given input value and measurement unit.
	 * 
	 * @param value The input value for which the smallest measurement value is to be determined.
	 * @param measurementId The ID of the measurement unit.
	 * @return The smallest measurement value corresponding to the given input value and measurement unit.
	 */
	Double getSmallestMeasurementValue(Double quantity, Long measurementId);

	/**
	 * Retrieves the smallest measurement ID for a given measurement unit.
	 * 
	 * @param measurementId The ID of the measurement unit.
	 * @return The smallest measurement ID associated with the given measurement unit.
	 */
	Long getSmallestMeasurementId(Long measurementId);

	/**
	 * Retrieves a string containing the adjusted quantity and extra quantity along with their respective IDs.
	 * 
	 * @param value The input value for which the adjusted and extra quantities need to be determined.
	 * @param measurementId The ID of the measurement unit.
	 * @param orderId The ID of the order for which the quantities are calculated.
	 * @return A string representation of the adjusted quantity, extra quantity, and their respective IDs.
	 */
	String getAdjustedAndExtraQuantity(Double quantity, Long measurementId, Boolean isAdjustQuantity, Boolean isSupplierRate);

	/**
	 * Updates the raw material quantity based on the provided list of CommonRawMaterialDto.
	 * The method calculates the required quantity adjustments and updates the raw material allocation accordingly.
	 *
	 * @param commonRawMaterialDto List of DTOs containing raw material allocation details.
	 */
	void updateRawMaterialQuantity(List<CommonRawMaterialDto> commonRawMaterialDto);

}