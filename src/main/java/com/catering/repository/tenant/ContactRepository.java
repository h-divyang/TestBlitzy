package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.catering.model.tenant.ContactModel;

/**
 * ContactRepository is an interface for managing ContactModel entities,
 * providing methods to interact with the persistence layer for CRUD operations and custom queries.
 * This interface extends JpaRepository to inherit basic database operations and declares
 * additional methods for entity-specific queries and existence checks.
 */
public interface ContactRepository extends JpaRepository<ContactModel, Long> {

	/**
	 * Checks if a contact entity exists associated with the given contact category ID.
	 *
	 * @param id The ID of the contact category to check for existence.
	 **/
	boolean existsByCategoryMapping_ContactCategory_Id(Long id);

	/**
	 * Checks whether a contact entity exists with the specified mobile number.
	 *
	 * @param mobileNumber The mobile number to check for existence.
	 * @return True if a contact entity with the given mobile number exists, false otherwise.
	 */
	boolean existsByMobileNumber(String mobileNumber);

	/**
	 * Checks whether an entity exists with the specified mobile number and a different ID.
	 *
	 * @param mobileNumber The mobile number to check for existence.
	 * @param id The ID that should not match in the existence check.
	 * @return True if an entity with the given mobile number exists and its ID does not match the provided ID, false otherwise.
	 */
	boolean existsByMobileNumberAndIdNot(String mobileNumber, Long id);

	/**
	 * Checks if a contact entity exists associated with the given Aadhar number.
	 *
	 * @param aadharNumber The Aadhar number to check for existence.
	 * @return True if a contact entity with the given Aadhar number exists, false otherwise.
	 */
	boolean existsByAadharNumber(String aadharNumber);

	/**
	 * Checks whether an entity exists with the specified Aadhar number and a different ID.
	 *
	 * @param aadharNumber The Aadhar number to check for existence.
	 * @param id The ID that should not match in the existence check.
	 * @return True if an entity with the given Aadhar number exists and its ID does not match the provided ID, false otherwise.
	 */
	boolean existsByAadharNumberAndIdNot(String aadharNumber, Long id);

	/**
	 * Retrieves a list of active ContactModel entities associated with a specific
	 * contact category type ID, where the contacts are marked as active.
	 *
	 * @param contactCategoryTypeId The ID of the contact category type to filter the contacts.
	 * @return A list of active ContactModel entities that belong to the specified contact category type.
	 */
	List<ContactModel> findDistinctByCategoryMapping_ContactCategory_ContactCategoryType_IdAndIsActiveTrue(Long contactCategoryTypeId);

	/**
	 * Retrieves a list of active ContactModel entities that belong to a specific contact category type,
	 * excluding the entities with IDs specified in the provided list.
	 *
	 * @param contactCategoryTypeId The ID of the contact category type to filter the contacts.
	 * @param IDs A list of IDs to be excluded from the result.
	 * @return A list of active ContactModel entities associated with the given contact category type
	 *		   ID, excluding those with IDs matching the specified list.
	 */
	List<ContactModel> findDistinctByCategoryMapping_ContactCategory_ContactCategoryType_IdAndIsActiveTrueAndIdNotIn(Long contactCategoryTypeId, List<Long> IDs);

	/**
	 * Retrieves a list of active ContactModel entities that belong to a specific contact category type,
	 * excluding the contacts already associated with a given raw material and optionally excluding
	 * a specific contact ID if provided. The method ensures that the retrieved contacts are active
	 * and belong to the category type with ID 3.
	 *
	 * @param rawMaterialId The ID of the raw material to filter associated contacts.
	 * @param userEditId The contact ID to exclude from the list, provided for editing purposes.
	 *					 If null, no specific contact ID is excluded.
	 * @return A list of contact entities meeting the specified criteria, including active status and
	 *		   absence of association with the raw material.
	 */
	@Query(value = "SELECT * FROM contact c "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE c.is_active = 1 AND cc.fk_contact_category_type_id = 3 "
		+ "AND c.id NOT IN (SELECT rms.fk_contact_id FROM raw_material_supplier rms WHERE "
		+ "rms.fk_raw_material_id = :rawMaterialId AND (CASE WHEN :userEditId IS NOT NULL THEN rms.fk_contact_id != :userEditId ELSE TRUE END)) "
		+ "GROUP BY c.id", nativeQuery = true)
	List<ContactModel> findContactCategoryUsingRawMaterial(Long rawMaterialId, Long userEditId);

	/**
	 * Retrieves a list of active ContactModel entities where both the contact and
	 * its associated contact category are active, and the contact category belongs
	 * to the specified contact category type. 
	 * The results are ordered by the priority of the contact category in ascending order,
	 * and by the contact ID in ascending order as a secondary sorting criterion.
	 *
	 * @param categoryTypeId The ID of the contact category type to filter by.
	 * @return A list of ContactModel entities that are active, belong to active contact
	 *         categories of the specified type, and are ordered by contact category priority
	 *         (ascending) and contact ID (ascending).
	 */
	@Query(
		"SELECT DISTINCT c  "
		+ "FROM ContactModel c "
		+ "JOIN c.categoryMapping cm "
		+ "JOIN cm.contactCategory cc "
		+ "WHERE c.isActive = TRUE " 
		+ "AND cc.isActive = TRUE "
		+ "AND cc.contactCategoryType.id = :contactCategoryTypeId " 
		+ "ORDER BY cc.priority ASC, cc.id ASC, c.id ASC")
	List<ContactModel> findDistinctActiveContactsByContactCategoryTypeId(@Param("contactCategoryTypeId") Long contactCategoryTypeId);

	/**
	 * Retrieves a list of active ContactModel entities that belong to the specified contact category.
	 * The contacts returned will have their `isActive` property set to true, and are filtered based on the provided contact category ID.
	 *
	 * @param contactCategoryId The ID of the contact category to filter the active contacts.
	 * @return A list of active ContactModel entities associated with the given contact category ID.
	 */
	List<ContactModel> findDistinctByIsActiveTrueAndCategoryMapping_ContactCategory_Id(Long contactCategoryId);

	/**
	 * Retrieves a list of ContactModel entities where the `isActive` property is set to true.
	 *
	 * @return A list of active ContactModel entities.
	 */
	List<ContactModel> findByIsActiveTrue();

	/**
	 * Determines whether the GST (Goods and Services Tax) number associated with a contact
	 * and the company preferences have mismatching state codes (first two digits).
	 *
	 * @param contactId The ID of the contact whose GST numbers are to be compared.
	 * @return True if the state codes of the GST numbers do not match, false otherwise.
	 */
	@Query(value = "SELECT CASE "
		+ "WHEN c.gst_number IS NOT NULL AND cp.gst_number IS NOT NULL AND SUBSTRING(cp.gst_number, 1, 2) != SUBSTRING(c.gst_number, 1, 2) "
		+ "THEN 'true' ELSE 'false' END AS gstNumber "
		+ "FROM contact c "
		+ "JOIN company_preferences cp "
		+ "WHERE c.id = :contactId", nativeQuery = true)
	boolean isGstNumberSame(Long contactId);

	/**
	 * Retrieves a list of ContactModel entities that are classified as banks.
	 * These contacts are associated with a contact category where the default language name is 'BANK'
	 * and the category is marked as non-updatable.
	 *
	 * @return A list of ContactModel entities corresponding to banks.
	 */
	@Query(value = "SELECT * FROM contact c LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id JOIN contact_category cc ON contactCategories.fk_contact_category_id = cc.id "
		+ "WHERE cc.name_default_lang = 'BANK' AND cc.is_non_updatable = 1 "
		+ "GROUP BY c.id", nativeQuery = true)
	List<ContactModel> findAllBankList();

	/**
	 * Searches for {@code ContactModel} objects by a query string in the category names.
	 *
	 * @param query The search query, case-insensitive, allowing partial matches.
	 * @return A list of {@code ContactModel} objects with categories matching the query.
	 */
	@Query(value = "SELECT c.* "
			+ "FROM contact c "
			+ "JOIN contact_categories contactCategories ON c.id = contactCategories.fk_contact_id "
			+ "JOIN contact_category cc ON contactCategories.fk_contact_category_id = cc.id "
			+ "WHERE LOWER(cc.name_supportive_lang) LIKE LOWER(CONCAT('%', :query, '%')) "
			+ "OR LOWER(cc.name_prefer_lang) LIKE LOWER(CONCAT('%', :query, '%')) "
			+ "OR LOWER(cc.name_default_lang) LIKE LOWER(CONCAT('%', :query, '%'));", nativeQuery = true)
	List<ContactModel> findContactsWithCategorySearching(@Param("query") String query);

	/**
	 * Calls the stored procedure to check if a contact is being used in any related data.
	 *
	 * @param contactId the ID of the contact to check
	 * @return the number of references or usages of the contact
	 */
	@Query(value = "CALL checkContactUsage(:contactId)", nativeQuery = true)
	Integer checkContactUsage(@Param("contactId") Long contactId);

	/**
	 * Deletes the contact entry from the `account_balance` table.
	 *
	 * @param contactId the ID of the contact to delete
	 * @return the number of rows affected by the delete operation
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM account_balance WHERE fk_contact_id = :contactId ", nativeQuery = true)
	Integer deleteContactFromAccount(@Param("contactId") Long contactId);

	/**
	 * Deletes the contact's transaction history from the `account_history` table,
	 * including records where the contact is either the sender or receiver.
	 *
	 * @param contactId the ID of the contact to delete history for
	 * @return the number of rows affected by the delete operation
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM account_history WHERE fk_contact_from_id = :contactId OR fk_contact_to_id = :contactId ", nativeQuery = true)
	Integer deleteContactFromAccountHistory(@Param("contactId") Long contactId);

}