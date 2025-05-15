package com.catering.repository.tenant;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.catering.model.tenant.CompanyUserModel;
import com.catering.model.tenant.CompanyUserModelForAudit;

/**
 * Repository interface for managing CompanyUserModel entities in the database.
 * It provides basic CRUD operations and additional methods for retrieving
 * and updating CompanyUserModel based on specific conditions.
 */
@EnableJpaRepositories
public interface CompanyUserRepository extends JpaRepository<CompanyUserModel, Long> {

	/**
	 * Retrieves a CompanyUserModel by the given username.
	 *
	 * @param username The username of the CompanyUserModel to be retrieved.
	 * @return An Optional containing the found CompanyUserModel, or an empty Optional if no user is found with the given username.
	 */
	Optional<CompanyUserModel> findByUsername(String username);

	/**
	 * Checks if a user record with the given username exists in the database.
	 *
	 * @param username The username to check for existence.
	 * @return true if a user record with the given username exists, false otherwise.
	 */
	boolean existsByUsername(String username);

	/**
	 * Checks if a user record with the given mobile number exists in the database.
	 *
	 * @param mobileNumber The mobile number to check for existence.
	 * @return true if a user record with the given mobile number exists, false otherwise.
	 */
	boolean existsByMobileNumber(String mobileNumber);

	/**
	 * Checks if a user record with the given email exists in the database.
	 *
	 * @param email The email to check for existence.
	 * @return true if a user record with the given email exists, false otherwise.
	 */
	boolean existsByEmail(String email);

	/**
	 * Checks if a user record with the given mobile number exists in the database
	 * and its ID is not equal to the provided ID.
	 *
	 * @param mobileNumber The mobile number to check for existence.
	 * @param id The ID to exclude from the check.
	 * @return True if a user record with the given mobile number exists and its ID is not equal to the provided ID, false otherwise.
	 */
	boolean existsByMobileNumberAndIdNot(String mobileNumber, Long id);

	/**
	 * Checks if a user record with the given email exists in the database and its ID is not equal to the provided ID.
	 *
	 * @param email The email to check for existence.
	 * @param id The ID to exclude from the check.
	 * @return true if a user record with the given email exists and its ID is not equal to the provided ID, false otherwise.
	 */
	boolean existsByEmailAndIdNot(String email, Long id);

	/**
	 * Checks if a user record with the given username exists in the database
	 * and its ID is not equal to the provided ID.
	 *
	 * @param username The username to check for existence.
	 * @param id The ID to exclude from the check.
	 * @return True if a user record with the given username exists and its ID is not equal to the provided ID, false otherwise.
	 */
	boolean existsByUsernameAndIdNot(String username, Long id);

	/**
	 * Checks if a user record with the given reportsTo ID exists in the database and is active.
	 *
	 * @param reportsTo The ID of the associated reportsTo entity.
	 * @param isActive The active status to check for (true for active, false for inactive).
	 * @return True if a user record with the given reportsTo ID exists and is active, false otherwise.
	 */
	boolean existsByIdAndIsActive(Long reportsTo, boolean isActive);

	/**
	 * Retrieves a list of CompanyUserModel objects from the database where the isActive field matches the provided boolean value,
	 * sorted in ascending order by ID.
	 *
	 * @param isActive The active status to filter the results (true for active, false for inactive).
	 * @return A list of CompanyUserModel objects matching the isActive criteria, sorted by ID in ascending order.
	 */
	List<CompanyUserModel> findAllByIsActiveOrderByIdAsc(boolean isActive);

	/**
	 * Checks if a user record exists in the database with the specified reportsTo ID.
	 *
	 * @param reportsTo The ID of the associated reportsTo entity to check for existence.
	 * @return True if a user record with the specified reportsTo ID exists, false otherwise.
	 */
	boolean existsByReportsTo(Long reportsTo);

	/**
	 * Updates the details of an existing CompanyUserModel in the database.
	 * The fields updated include names, mobile number, permitted IP address,
	 * edit count, and the updated timestamp. The update is performed on the
	 * record identified by the given ID.
	 *
	 * @param companyUserModel The CompanyUserModel object containing the updated
	 *						   field values and the ID of the record to update.
	 * @return The number of records updated (should be 1 if the update is
	 *		   successful and the ID exists, 0 otherwise).
	 */
	@Modifying
	@Transactional
	@Query("UPDATE "
		+ "CompanyUserModel cum "
		+ "SET "
		+ "cum.firstNameDefaultLang = :#{#companyUserModel.firstNameDefaultLang}, "
		+ "cum.firstNamePreferLang = :#{#companyUserModel.firstNamePreferLang}, "
		+ "cum.firstNameSupportiveLang = :#{#companyUserModel.firstNameSupportiveLang}, "
		+ "cum.lastNameDefaultLang = :#{#companyUserModel.lastNameDefaultLang}, "
		+ "cum.lastNamePreferLang = :#{#companyUserModel.lastNamePreferLang}, "
		+ "cum.lastNameSupportiveLang = :#{#companyUserModel.lastNameSupportiveLang}, "
		+ "cum.mobileNumber = :#{#companyUserModel.mobileNumber}, "
		+ "cum.permittedIP = :#{#companyUserModel.permittedIP}, "
		+ "cum.editCount = :#{#companyUserModel.editCount}, "
		+ "cum.updatedAt = now() "
		+ "WHERE "
		+ "cum.id = :#{#companyUserModel.id}")
	int update(CompanyUserModel companyUserModel);

	/**
	 * Updates the password of a CompanyUserModel record in the database along with
	 * the edit count and the updated timestamp. The update is performed on the
	 * record identified by the given ID.
	 *
	 * @param companyUserModel The CompanyUserModel object containing the new
	 *                         password, updated edit count, and the ID of the record to be updated.
	 * @return The number of records updated (should be 1 if the update is successful and the ID exists, 0 otherwise).
	 */
	@Modifying
	@Transactional
	@Query("UPDATE "
		+ "CompanyUserModel cum "
		+ "SET "
		+ "cum.password = :#{#companyUserModel.password}, "
		+ "cum.editCount = :#{#companyUserModel.editCount}, "
		+ "cum.updatedAt = now() "
		+ "WHERE "
		+ "cum.id = :#{#companyUserModel.id}")
	int changePassword(CompanyUserModel companyUserModel);

	/**
	 * Updates the details of an existing Company User.
	 *
	 * @param companyUserModel The CompanyUserModel object containing the updated details of the user.
	 * @param updatedBy The CompanyUserModelForAudit object representing the user performing the update action.
	 * @return The number of rows affected as an integer.
	 */
	@Modifying
	@Transactional
	@Query("UPDATE "
		+ "CompanyUserModel cum "
		+ "SET "
		+ "cum.firstNameDefaultLang = :#{#companyUserModel.firstNameDefaultLang}, "
		+ "cum.firstNamePreferLang = :#{#companyUserModel.firstNamePreferLang}, "
		+ "cum.firstNameSupportiveLang = :#{#companyUserModel.firstNameSupportiveLang}, "
		+ "cum.lastNameDefaultLang = :#{#companyUserModel.lastNameDefaultLang}, "
		+ "cum.lastNamePreferLang = :#{#companyUserModel.lastNamePreferLang}, "
		+ "cum.lastNameSupportiveLang = :#{#companyUserModel.lastNameSupportiveLang}, "
		+ "cum.username = :#{#companyUserModel.username}, "
		+ "cum.designationId = :#{#companyUserModel.designationId}, "
		+ "cum.reportsTo = :#{#companyUserModel.reportsTo}, "
		+ "cum.email = :#{#companyUserModel.email}, "
		+ "cum.mobileNumber = :#{#companyUserModel.mobileNumber}, "
		+ "cum.permittedIP = :#{#companyUserModel.permittedIP}, "
		+ "cum.editCount = :#{#companyUserModel.editCount}, "
		+ "cum.isActive = :#{#companyUserModel.isActive}, "
		+ "cum.updatedBy = :updatedBy, "
		+ "cum.updatedAt = now() "
		+ "WHERE "
		+ "cum.id = :#{#companyUserModel.id}")
	int updateUser(CompanyUserModel companyUserModel, CompanyUserModelForAudit updatedBy);

}