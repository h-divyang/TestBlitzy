package com.catering.repository.tenant;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.dto.tenant.request.AgencyAllocationDto;
import com.catering.dto.tenant.request.RawMaterialAllocationFromRawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialDetailsDto;
import com.catering.model.tenant.RawMaterialAllocationModel;

/**
 * Repository interface for accessing and manipulating raw material allocation data.
 * 
 * This interface extends `JpaRepository` to provide CRUD operations for `RawMaterialAllocationModel` and defines
 * custom queries for updating and retrieving raw material allocation data.
 */
public interface RawMaterialAllocationRepository extends JpaRepository<RawMaterialAllocationModel, Long> {

	/**
	 * Updates the raw material allocation record with new values based on the provided DTO.
	 * 
	 * This query updates the fields of the raw material allocation record, such as final quantity, final measurement ID,
	 * contact agency ID, order time, and godown ID, where the record ID matches the provided `orderRawMaterial` ID.
	 * 
	 * @param orderRawMaterial the DTO containing updated values for the raw material allocation
	 * @return the number of rows affected by the update
	 */
	@Modifying
	@Transactional
	@Query("UPDATE "
		+ "RawMaterialAllocationModel orm "
		+ "SET "
		+ "orm.finalQty = :#{#orderRawMaterial.finalQty}, "
		+ "orm.finalMeasurementId.id = :#{#orderRawMaterial.finalMeasurementId}, "
		+ "orm.contactAgencyId.id = :#{#orderRawMaterial.contactAgencyId}, "
		+ "orm.orderTime = :#{#orderRawMaterial.orderTime}, "
		+ "orm.godown.id = :#{#orderRawMaterial.godown} "
		+ "WHERE "
		+ "orm.id = :#{#orderRawMaterial.id}")
	int update(RawMaterialAllocationFromRawMaterialDto orderRawMaterial);

	/**
	 * Finds raw material allocation records based on the menu preparation menu item ID.
	 * 
	 * This query retrieves all raw material allocations for the specified menu preparation menu item ID, ensuring
	 * that the records do not have any associated items in the `order_no_items` table.
	 * 
	 * @param orderMenuPreparationMenuItemId the menu preparation menu item ID
	 * @return a list of raw material allocation models that meet the criteria
	 */
	@Query(value = "SELECT rma.id as id, "
		+ "rma.fk_actual_measurement_id, "
		+ "rma.actual_qty, "
		+ "rma.fk_contact_agency_id, "
		+ "rma.fk_menu_item_raw_material_id, "
		+ "rma.fk_final_measurement_id, "
		+ "rma.final_qty, "
		+ "rma.fk_raw_material_id, "
		+ "rma.fk_menu_preparation_menu_item_id, "
		+ "rma.order_time, "
		+ "rma.fk_godown_id "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id ELSE rma.fk_raw_material_id END "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = ompmi.fk_menu_preparation_id AND oni.fk_raw_material_id = rm.id "
		+ "WHERE rma.fk_menu_preparation_menu_item_id = :orderMenuPreparationMenuItemId "
		+ "AND oni.id IS NULL ", nativeQuery = true)
	List<RawMaterialAllocationModel> findByMenuPreparationMenuItemId(@Param("orderMenuPreparationMenuItemId") Long orderMenuPreparationMenuItemId);

	/**
	 * Allocates a contact agency and godown to a raw material allocation record.
	 * 
	 * This query updates the contact agency, godown, and order time fields for a raw material allocation based on
	 * the provided `agencyAllocationDto` object.
	 * 
	 * @param agencyAllocationDto the DTO containing the allocation information for the contact agency and godown
	 */
	@Modifying
	@Transactional
	@Query("UPDATE RawMaterialAllocationModel rma SET rma.contactAgencyId.id = :#{#agencyAllocationDto.contactAgencyId}, rma.godown.id = :#{#agencyAllocationDto.godownId}, rma.orderTime = :#{#agencyAllocationDto.orderTime} WHERE rma.id = :#{#agencyAllocationDto.rawMaterialAllocationId}")
	void agencyAllocation(AgencyAllocationDto agencyAllocationDto);

	/**
	 * Synchronizes raw material allocation data with a specified menu item and raw material.
	 * 
	 * This query inserts a new raw material allocation record with the specified values for menu item ID, raw material ID,
	 * actual quantity, final quantity, order time, and contact agency. It also adjusts the quantity based on certain conditions.
	 * 
	 * @param menuPreparationMenuItemId the menu preparation menu item ID
	 * @param menuItemRawMaterialId the menu item raw material ID
	 * @param actualQty the actual quantity of raw material
	 * @param actualMeasurementId the ID of the actual measurement
	 * @param rawMaterialCategoryId the ID of the raw material category
	 * @param orderTime the order time for the raw material allocation
	 * @param rawMaterialId the raw material ID
	 * @param isAdjustQuantity flag indicating whether to adjust the quantity
	 */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO raw_material_allocation ("
		+ "fk_menu_preparation_menu_item_id, "
		+ "fk_menu_item_raw_material_id, "
		+ "actual_qty, "
		+ "fk_actual_measurement_id, "
		+ "final_qty, "
		+ "fk_final_measurement_id, "
		+ "order_time, "
		+ "fk_contact_agency_id"
		+ ") VALUES ("
		+ ":menuPreparationMenuItemId, "
		+ ":menuItemRawMaterialId, "
		+ ":actualQty, "
		+ ":actualMeasurementId, "
		+ "adjustQuantity(getSmallestMeasurementValue(:actualQty, :actualMeasurementId), getSmallestMeasurementId(:actualMeasurementId), IF(:isAdjustQuantity, 1, 0)), "
		+ "adjustQuantityUnit(:actualQty, :actualMeasurementId, IF(:isAdjustQuantity, 1, 0)), "
		+ ":orderTime, "
		+ "(SELECT fk_contact_id FROM raw_material_supplier WHERE fk_raw_material_id = :rawMaterialId AND is_default))", nativeQuery = true)
	void syncRawMaterial(Long menuPreparationMenuItemId, Long menuItemRawMaterialId, Double actualQty, Long actualMeasurementId, LocalDateTime orderTime, Long rawMaterialId, Boolean isAdjustQuantity);

	/**
	 * Checks if a raw material allocation exists for a specific godown ID.
	 * 
	 * This query checks if any raw material allocation exists for the specified godown ID.
	 * 
	 * @param id the godown ID
	 * @return true if an allocation exists for the given godown ID, otherwise false
	 */
	boolean existsByGodown_Id(Long id);

	/**
	 * Retrieves the smallest possible measurement value by executing a native SQL query.
	 * This method calls the database function `getSmallestMeasurementValue` to determine the smallest measurement value for a given input value and measurement ID.
	 * 
	 * @param value The input value for which the smallest measurement value is to be determined.
	 * @param measurementId The ID of the measurement unit.
	 * @return The smallest measurement value corresponding to the given input value and measurement unit.
	 */
	@Query(value = "SELECT getSmallestMeasurementValue(:quantity, :measurementId)", nativeQuery = true)
	Double getSmallestMeasurementValue(@Param("quantity") Double quantity, @Param("measurementId") Long measurementId);

	/**
	 * Retrieves the smallest measurement ID by executing a native SQL query.
	 * This method calls the database function `getSmallestMeasurementId` to fetch the smallest measurement ID associated with a given measurement unit.
	 * 
	 * @param measurementId The ID of the measurement unit.
	 * @return The smallest measurement ID associated with the given measurement unit.
	 */
	@Query(value = "SELECT getSmallestMeasurementId(:measurementId)", nativeQuery = true)
	Long getSmallestMeasurementId(@Param("measurementId") Long measurementId);

	/**
	 * Retrieves the adjusted measurement value by executing a native SQL query.
	 * 
	 * @param quantity The quantity of the measurement unit.
	 * @param measurementId The ID of the measurement unit.
	 * @return The smallest measurement ID associated with the given measurement unit.
	 */
	@Query(value = "SELECT adjustQuantity(:quantity, :measurementId, :isAdjustQuantity)", nativeQuery = true)
	Double adjustQuantity(@Param("quantity") Double quantity, @Param("measurementId") Long measurementId, @Param("isAdjustQuantity") Boolean isAdjustQuantity);

	/**
	 * Retrieves the adjusted measurement ID value by executing a native SQL query.
	 * 
	 * @param quantity The quantity of the measurement unit.
	 * @param measurementId The ID of the measurement unit.
	 * @return The smallest measurement ID associated with the given measurement unit.
	 */
	@Query(value = "SELECT adjustQuantityUnit(:quantity, :measurementId, :isAdjustQuantity)", nativeQuery = true)
	Long adjustQuantityUnit(@Param("quantity") Double quantity, @Param("measurementId") Long measurementId, @Param("isAdjustQuantity") Boolean isAdjustQuantity);

	/**
	 * Retrieves a string containing the adjusted quantity and extra quantity along with their respective IDs.
	 * This method calls the database function `getAdjustedAndExtraQuantity` to calculate 
	 * and return the adjusted and extra quantities based on the provided input.
	 * 
	 * @param value The input value for which the adjusted and extra quantities need to be determined.
	 * @param measurementId The ID of the measurement unit.
	 * @param isAdjustQuantity A flag indicating whether the quantity should be adjusted.
	 * @return A string representation of the adjusted quantity, extra quantity, and their respective IDs.
	 */
	@Query(value = "SELECT getAdjustedAndExtraQuantity(:quantity, :measurementId, :isAdjustQuantity, :isSupplierRate)", nativeQuery = true)
	String getAdjustedAndExtraQuantity(@Param("quantity") Double quantity, @Param("measurementId") Long measurementId, @Param("isAdjustQuantity") Boolean isAdjustQuantity, @Param("isSupplierRate") Boolean isSupplierRate); 

	/**
	 * Updates the final quantity and measurement ID of a raw material allocation
	 * based on the menu item and raw material ID.
	 *
	 * @param rawMaterialDetailsDto DTO containing the raw material information.
	 */
	@Modifying
	@Transactional
	@Query("UPDATE RawMaterialAllocationModel rma SET rma.finalQty = :#{#rawMaterialDetailsDto.finalQty}, rma.finalMeasurementId.id = :#{#rawMaterialDetailsDto.finalMeasurementId} WHERE rma.menuPreparationMenuItemId = :#{#rawMaterialDetailsDto.menuPreparationMenuItemId} AND rma.menuItemRawMaterialId.id = :#{#rawMaterialDetailsDto.menuItemRawMaterialId}")
	void updateRawMaterialByMenuItemAndRawMaterialId(RawMaterialDetailsDto rawMaterialDetailsDto);

	/**
	 * Updates the quantity and measurement ID of a raw material in an order 
	 * based on the function and raw material ID.
	 *
	 * @param rawMaterialDetailsDto DTO containing the raw material information.
	 */
	@Modifying
	@Transactional
	@Query("UPDATE OrderGeneralFixRawMaterialModel ogfrm SET ogfrm.qty = :#{#rawMaterialDetailsDto.finalQty}, ogfrm.measurementId = :#{#rawMaterialDetailsDto.finalMeasurementId} WHERE ogfrm.orderFunctionId = :#{#rawMaterialDetailsDto.functionId} AND ogfrm.rawMaterialId = :#{#rawMaterialDetailsDto.rawMaterialId}")
	void updateRawMaterialByFunctionAndRawMaterialId(RawMaterialDetailsDto rawMaterialDetailsDto);

}