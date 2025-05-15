package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.catering.dto.tenant.request.AllRawMaterialSupplierDto;
import com.catering.dto.tenant.request.NameDto;
import com.catering.model.tenant.RawMaterialModel;

/**
 * Repository interface for accessing and manipulating data related to raw materials.
 * 
 * This interface extends `JpaRepository` to provide CRUD operations, `PriorityRepository` for prioritization
 * of raw material records, and `CommonNameExistenceRepository` to check for name existence across raw materials.
 */
public interface RawMaterialRepository extends JpaRepository<RawMaterialModel, Long>, PriorityRepository<RawMaterialModel>, CommonNameExistenceRepository, JpaSpecificationExecutor<RawMaterialModel> {

	/**
	 * Finds all active raw materials.
	 * 
	 * This query retrieves all raw material records that are marked as active (`isActive` = true).
	 * 
	 * @return a list of active raw materials
	 */
	List<RawMaterialModel> findAllByIsActiveTrue();

	/**
	 * Finds all active raw materials except those whose IDs are in the provided list.
	 * 
	 * This query retrieves all raw materials that are active (`isActive` = true) and not in the specified list of IDs.
	 * 
	 * @param IDs a list of raw material IDs to exclude from the result
	 * @return a list of active raw materials excluding those with the specified IDs
	 */
	List<RawMaterialModel> findAllByIsActiveTrueAndIdNotIn(List<Long> IDs);

	/**
	 * Finds raw materials that are not linked to a specific menu item and are not being edited by the user.
	 * 
	 * This query retrieves raw materials that are active and not linked to the specified menu item. If a user edit ID 
	 * is provided, it also excludes the raw material that is being edited by the user.
	 * 
	 * @param menuItemId the ID of the menu item
	 * @param userEditId the ID of the user-editing raw material, or null if not applicable
	 * @return a list of raw materials that are not linked to the specified menu item and not being edited by the user
	 */
	@Query(value = "SELECT * FROM raw_material ir WHERE ir.is_active = 1 AND ir.id NOT IN (SELECT fi.fk_raw_material_id FROM menu_item_raw_material fi WHERE "
			+ "fi.fk_menu_item_id = :menuItemId AND (CASE WHEN :userEditId IS NOT NULL THEN fi.fk_raw_material_id != :userEditId ELSE TRUE END))", nativeQuery = true)
	List<RawMaterialModel> findRawMaterialUsingMenuItemRawMaterial(Long menuItemId, Long userEditId);

	/**
	 * Retrieves raw material names associated with a specific menu item.
	 * 
	 * This query retrieves a list of raw materials with their names (in multiple languages) that are linked to the specified menu item.
	 * 
	 * @param menuItemId the ID of the menu item
	 * @return a list of `NameDto` containing raw material IDs and their names in different languages
	 */
	@Query(value = "SELECT new com.catering.dto.tenant.request.NameDto(irm.id, irm.nameDefaultLang, irm.namePreferLang, irm.nameSupportiveLang) FROM RawMaterialModel irm INNER JOIN MenuItemRawMaterialModel firm ON firm.rawMaterial.id = irm.id WHERE firm.menuItem.id = :menuItemId")
	List<NameDto> findByMenuItemId(Long menuItemId);

	/**
	 * Retrieves raw material details along with supplier information.
	 * 
	 * This query retrieves raw material details (such as name, measurement, and supplier info) for all raw materials,
	 * including the default supplier for each, if available.
	 * 
	 * @return a list of `AllRawMaterialSupplierDto` containing raw material and supplier details
	 */
	@Query(value = "SELECT new com.catering.dto.tenant.request.AllRawMaterialSupplierDto(rm.id, rm.isActive, rm.nameDefaultLang, rm.namePreferLang, rm.nameSupportiveLang, " +
			"rm.measurement, rms.contact.id, rm.rawMaterialCategory.rawMaterialCategoryType.id) " +
			"FROM RawMaterialModel rm " +
			"LEFT JOIN RawMaterialSupplierModel rms ON rms.rawMaterial.id = rm.id AND rms.isDefault = true " +
			"ORDER BY rm.id")
	List<AllRawMaterialSupplierDto> findRawMaterialDetails();

	/**
	 * Checks if a raw material is linked to any other entities (e.g., orders, menu items, suppliers).
	 * 
	 * This query checks whether the specified raw material ID is linked to any other entities, such as orders, 
	 * menu items, suppliers, etc., to determine if it is in use.
	 * 
	 * @param id the ID of the raw material
	 * @return `true` if the raw material is linked to any other entities, otherwise `false`
	 */
	@Query(value = "SELECT IF(COUNT(rm.id) > 1, 'TRUE', 'FALSE') AS result "
		+ "FROM raw_material rm "
		+ "LEFT JOIN order_crockery oc ON oc.fk_raw_material_id = rm.id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.fk_raw_material_id = rm.id "
		+ "LEFT JOIN order_general_fix_raw_material ogfra ON ogfra.fk_raw_material_id = rm.id "
		+ "LEFT JOIN raw_material_supplier rms ON rms.fk_raw_material_id = rm.id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_raw_material_id = rm.id "
		+ "LEFT JOIN raw_material_allocation rma ON rma.fk_raw_material_id = rm.id "
		+ "LEFT JOIN raw_material_allocation_extra rmae ON rmae.fk_raw_material_id = rm.id "
		+ "WHERE rm.id = :id", nativeQuery = true)
	Boolean isExists(Long id);

	/**
	 * Calls the stored procedure to check if a raw material is being used.
	 *
	 * @param rawMaterialId the ID of the raw material to check
	 * @return the number of references or usages of the raw material
	 */
	@Query(value = "CALL checkRawMaterialUsage(:rawMaterialId)", nativeQuery = true)
	Integer checkRawMaterialUsage(@Param("rawMaterialId") Long rawMaterialId);

	/**
	 * Deletes the raw material entry from the `total_stock` table.
	 *
	 * @param rawMaterialId the ID of the raw material to delete
	 * @return the number of rows affected by the delete operation
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM total_stock WHERE fk_raw_material_id = :rawMaterialId ", nativeQuery = true)
	Integer deleteRawMaterialFromStock(@Param("rawMaterialId") Long rawMaterialId);

	/**
	 * Deletes the raw material's historical stock records from the `stock_history` table.
	 *
	 * @param rawMaterialId the ID of the raw material to delete history for
	 * @return the number of rows affected by the delete operation
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM stock_history WHERE fk_raw_material_id = :rawMaterialId", nativeQuery = true)
	Integer deleteRawMaterialFromStockHistory(@Param("rawMaterialId") Long rawMaterialId);

	/**
	 * Updates the priority, updated date, and updated by fields of a raw material entity.
	 *
	 * <p>This method performs a native SQL update on the {@code raw_material} table based on the
	 * ID provided in the {@link RawMaterialModel}.</p>
	 *
	 * <p>The fields updated are:
	 * <ul>
	 *		<li>{@code priority}</li>
	 *		<li>{@code updated_at}</li>
	 *		<li>{@code updated_by}</li>
	 * </ul>
	 * </p>
	 *
	 * <p>Marked as {@code @Modifying} and {@code @Transactional} to execute the update operation in a transactional context.</p>
	 *
	 * @param rawMaterial the {@link RawMaterialModel} object containing the new priority and audit information
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE raw_material rm "
			+ "SET rm.priority = :#{#rawMaterial.priority}, "
			+ "rm.updated_at = :#{#rawMaterial.updatedAt}, "
			+ "rm.updated_by = :#{#rawMaterial.updatedBy} "
			+ "WHERE rm.id = :#{#rawMaterial.id}", nativeQuery = true)
	void updatePriority(RawMaterialModel rawMaterial);

	/**
	 * Retrieves the highest priority value from the {@code raw_material} table.
	 * <p>
	 * This method executes a native SQL query to return the maximum value of the {@code priority} column.
	 *
	 * @return the highest priority as a {@code long}. Returns 0 if no records are found.
	 */
	@Query(value = "SELECT MAX(rm.priority) FROM raw_material rm", nativeQuery = true)
	long getHighestPriority();

}