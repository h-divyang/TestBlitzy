package com.catering.repository.tenant;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.catering.model.tenant.RawMaterialCategoryModel;

/**
 * Repository interface for accessing and manipulating raw material category data.
 * 
 * This interface extends `JpaRepository` to provide CRUD operations, `PriorityRepository` for prioritization
 * of raw material category records, and `CommonNameExistenceRepository` to check for name existence across categories.
 */
public interface RawMaterialCategoryRepository extends JpaRepository<RawMaterialCategoryModel, Long>, PriorityRepository<RawMaterialCategoryModel>, CommonNameExistenceRepository {

	/**
	 * Finds all active raw material categories, ordered by priority.
	 * 
	 * This query retrieves all raw material categories that are marked as active (`isActive`), ordered by their
	 * priority in ascending order.
	 * 
	 * @param isActive a flag indicating whether to filter by active categories
	 * @return a list of active raw material categories ordered by priority
	 */
	List<RawMaterialCategoryModel> findAllByIsActiveOrderByPriorityAsc(boolean isActive);

	/**
	 * Finds raw material categories by their category type ID, ordered by priority.
	 * 
	 * This query retrieves raw material categories filtered by the specified category type ID, ordered by their
	 * priority in ascending order.
	 * 
	 * @param categoryTypeId the ID of the raw material category type
	 * @return a list of raw material categories filtered by category type ID, ordered by priority
	 */
	List<RawMaterialCategoryModel> findByRawMaterialCategoryTypeIdInOrderByPriority(List<Long> categoryTypeIds);

	/**
	 * Finds a raw material category by its ID.
	 * 
	 * This query retrieves a raw material category based on the specified ID.
	 * 
	 * @param id the ID of the raw material category
	 * @return an `Optional` containing the raw material category if found, or empty if not found
	 */
	Optional<RawMaterialCategoryModel> findById(Long id);

	/**
	 * Finds all raw material categories that have at least one associated raw material, ordered by priority.
	 * 
	 * This query retrieves raw material categories that have one or more associated raw materials, ordered by
	 * their priority in ascending order.
	 * 
	 * @return a list of raw material categories that have associated raw materials, ordered by priority
	 */
	List<RawMaterialCategoryModel> findDistinctByRawMaterialsNotNullOrderByPriority();

	/**
	 * Updates the priority, updated date, and updated by fields of a raw material category entity.
	 *
	 * <p>This method performs a native SQL update on the {@code raw_material_category} table based on the
	 * ID provided in the {@link RawMaterialCategoryModel}.</p>
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
	 * @param rawMaterialCategoryModel the {@link RawMaterialCategoryModel} object containing the new priority and audit information
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE raw_material_category rmc " 
			+ "SET rmc.priority = :#{#rawMaterialCategoryModel.priority}, "
			+ "rmc.updated_at = :#{#rawMaterialCategoryModel.updatedAt}, "
			+ "rmc.updated_by = :#{#rawMaterialCategoryModel.updatedBy} "
			+ "WHERE rmc.id = :#{#rawMaterialCategoryModel.id}", nativeQuery = true)
	void updatePriority(RawMaterialCategoryModel rawMaterialCategoryModel);

	/**
	 * Retrieves the highest priority value from the {@code raw_material_category} table.
	 * <p>
	 * This method executes a native SQL query to return the maximum value of the {@code priority} column.
	 *
	 * @return the highest priority as a {@code long}. Returns 0 if no records are found.
	 */
	@Query(value = "SELECT MAX(rmc.priority) FROM raw_material_category rmc", nativeQuery = true)
	long getHighestPriority();

}