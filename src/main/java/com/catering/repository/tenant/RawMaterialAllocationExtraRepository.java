package com.catering.repository.tenant;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.catering.dto.tenant.request.AgencyAllocationDto;
import com.catering.dto.tenant.request.RawMaterialAllocationFromRawMaterialDto;
import com.catering.model.tenant.RawMaterialAllocationExtraModel;

/**
 * Repository interface for accessing and manipulating raw material allocation extra data.
 * 
 * This interface extends `JpaRepository` to provide CRUD operations for `RawMaterialAllocationExtraModel` and defines
 * custom queries for updating and handling extra raw material allocations.
 */
public interface RawMaterialAllocationExtraRepository extends JpaRepository<RawMaterialAllocationExtraModel, Long> {

	/**
	 * Updates the raw material allocation extra record with new values based on the provided DTO.
	 * 
	 * This query updates the fields of the raw material allocation extra record, such as contact agency ID, order time,
	 * and godown ID, where the record ID matches the provided `orderRawMaterial` ID.
	 * 
	 * @param orderRawMaterial the DTO containing updated values for the raw material allocation extra
	 */
	@Modifying
	@Transactional
	@Query("UPDATE "
		+ "RawMaterialAllocationExtraModel rmae "
		+ "SET "
		+ "rmae.contactAgencyId = :#{#orderRawMaterial.contactAgencyId}, "
		+ "rmae.orderTime = :#{#orderRawMaterial.orderTime}, "
		+ "rmae.godownId = :#{#orderRawMaterial.godown}, "
		+ "rmae.total = :#{#orderRawMaterial.total}, "
		+ "rmae.quantity = :#{#orderRawMaterial.finalQty}, "
		+ "rmae.measurementId = :#{#orderRawMaterial.finalMeasurementId} "
		+ "WHERE "
		+ "rmae.orderId = :#{#orderId} AND rmae.rawMaterialId = :#{#orderRawMaterial.rawMaterialId}")
	void update(RawMaterialAllocationFromRawMaterialDto orderRawMaterial, Long orderId);

	/**
	 * Inserts a new record into the raw_material_allocation_extra table.
	 *
	 * @param rawMaterialAllocationFromRawMaterialDto the DTO containing raw material allocation details
	 * @param orderId the ID of the order associated with this allocation
	 */
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO "
		+ "raw_material_allocation_extra "
		+ "(fk_customer_order_details_id, fk_raw_material_id, fk_contact_agency_id, order_time, fk_godown_id, total, quantity, fk_measurement_id) "
		+ "VALUES ( "
		+ ":#{#orderId}, "
		+ ":#{#rawMaterialAllocationFromRawMaterialDto.rawMaterialId}, "
		+ ":#{#rawMaterialAllocationFromRawMaterialDto.contactAgencyId}, "
		+ ":#{#rawMaterialAllocationFromRawMaterialDto.orderTime}, "
		+ ":#{#rawMaterialAllocationFromRawMaterialDto.godown}, "
		+ ":#{#rawMaterialAllocationFromRawMaterialDto.total}, "
		+ ":#{#rawMaterialAllocationFromRawMaterialDto.finalQty}, "
		+ ":#{#rawMaterialAllocationFromRawMaterialDto.finalMeasurementId})", nativeQuery = true)
	void save(RawMaterialAllocationFromRawMaterialDto rawMaterialAllocationFromRawMaterialDto, Long orderId);

	/**
	 * Deletes a record on the given raw material ID and order ID.
	 *
	 * @param rawMaterialId the ID of the raw material to be deleted
	 * @param orderId the ID of the associated order
	 */
	@Transactional
	void deleteByRawMaterialIdAndOrderId(Long rawMaterialId, Long orderId);

	/**
	 * Retrieves a record based on the given raw material ID and order ID.
	 *
	 * @param rawMaterialId the ID of the raw material to be fetched
	 * @param orderId the ID of the associated order
	 * @return an instance of RawMaterialAllocationExtraModel if found, otherwise null
	 */
	RawMaterialAllocationExtraModel findByRawMaterialIdAndOrderId(Long rawMaterialId, Long orderId);

	/**
	 * Allocates a contact agency, godown, and order time to a raw material allocation extra record.
	 * 
	 * This query updates the contact agency, order time, and godown ID for a raw material allocation extra record 
	 * based on the provided `agencyAllocationDto` and order ID.
	 * 
	 * @param agencyAllocationDto the DTO containing allocation information for the contact agency, godown, and order time
	 * @param orderId the ID of the associated order
	 */
	@Modifying
	@Transactional
	@Query("UPDATE RawMaterialAllocationExtraModel rmae SET rmae.contactAgencyId = :#{#agencyAllocationDto.contactAgencyId}, rmae.orderTime = :#{#agencyAllocationDto.orderTime}, rmae.godownId = :#{#agencyAllocationDto.godownId} WHERE rmae.orderId = :orderId AND rmae.rawMaterialId = :#{#agencyAllocationDto.rawMaterialId}")
	void agencyAllocation(AgencyAllocationDto agencyAllocationDto, Long orderId);

}