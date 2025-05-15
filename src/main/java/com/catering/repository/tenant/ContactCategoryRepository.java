package com.catering.repository.tenant;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.catering.model.tenant.ContactCategoryModel;

/**
 * ContactCategoryRepository is a repository interface for managing ContactCategoryModel entities.
 * It extends JpaRepository for basic CRUD operations and provides additional methods tailored for
 * specific use cases, including checks for active entities and filtering by related IDs.
 */
public interface ContactCategoryRepository extends JpaRepository<ContactCategoryModel, Long>, CommonNameExistenceRepository {

	/**
	 * Checks if an entity with the specified ID exists and has the "isActive" status set to true.
	 *
	 * @param id The ID of the entity to check for existence with "isActive" status true.
	 * @return True if an entity with the given ID exists and is active; otherwise, false.
	 */
	boolean existsByIdAndIsActiveTrue(Long id);

	/**
	 * Retrieves a list of ContactCategoryModel entities where the "isActive" attribute is set to true.
	 * The results are ordered by the "priority" field in ascending order. 
	 * If multiple entities have the same "priority", they are further sorted by their "id" in ascending order.
	 *
	 * @return A list of active ContactCategoryModel entities ordered by priority and id.
	 */
	List<ContactCategoryModel> findByIsActiveTrueOrderByPriorityAscIdAsc();

	/**
	 * Retrieves a list of active ContactCategoryModel entities filtered by a specific contact category type ID.
	 * The results are ordered by the "priority" field in ascending order. 
	 * If multiple entities have the same "priority", they are further sorted by their "id" in ascending order.
	 *
	 * @param contactCategoryTypeId The ID of the contact category type used to filter the results.
	 * @return A list of ContactCategoryModel entities that are active, belong to the specified contact category type ID,
	 *         and are ordered by priority (ascending) and ID (ascending).
	 */
	List<ContactCategoryModel> findByIsActiveTrueAndContactCategoryTypeIdOrderByPriorityAscIdAsc(Long contactCategoryTypeId);

	/**
	 * Updates the priority, updated date, and updated by fields of a contact category entity.
	 *
	 * <p>This method performs a native SQL update on the {@code contact_category} table based on the
	 * ID provided in the {@link ContactCategoryModel}.</p>
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
	 * @param contactCategoryModel the {@link ContactCategoryModel} object containing the new priority and audit information
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE contact_category cc " 
			+ "SET cc.priority = :#{#contactCategoryModel.priority}, "
			+ "cc.updated_at = :#{#contactCategoryModel.updatedAt}, "
			+ "cc.updated_by = :#{#contactCategoryModel.updatedBy} "
			+ "WHERE cc.id = :#{#contactCategoryModel.id}", nativeQuery = true)
	void updatePriority(ContactCategoryModel contactCategoryModel);

	/**
	 * Retrieves the highest priority value from the {@code contact_category} table.
	 * <p>
	 * This method executes a native SQL query to return the maximum value of the {@code priority} column.
	 *
	 * @return the highest priority as a {@code long}. Returns 0 if no records are found.
	 */
	@Query(value = "SELECT MAX(cc.priority) FROM contact_category cc", nativeQuery = true)
	long getHighestPriority();

}