package com.catering.repository.tenant;

import java.util.Objects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.catering.model.tenant.CompanyPreferencesModel;

/**
 * CompanyPreferencesRepository is an interface for managing the persistence and retrieval of
 * CompanyPreferencesModel entities in the database. This interface extends JpaRepository and
 * provides an additional set of query methods to handle complex filtering and logic related to
 * company preferences.
 */
public interface CompanyPreferencesRepository  extends JpaRepository<CompanyPreferencesModel, Long> {

	/**
	 * Checks if an entity with the specified mobile number exists in the database, excluding the entity with the given ID.
	 *
	 * @param mobileNumber The mobile number to check for existence.
	 * @param id The ID to exclude from the check.
	 * @return True if an entity with the specified mobile number exists and does not match the given ID, false otherwise.
	 */
	boolean existsByMobileNumberAndIdNot(String mobileNumber, Long id);

	/**
	 * Checks if an entity with the specified mobile number exists in the database.
	 *
	 * @param mobileNumber The mobile number to check for existence.
	 * @return True if an entity with the specified mobile number exists, false otherwise.
	 */
	boolean existsByMobileNumber(String mobileNumber);

	/**
	 * Determines whether the company preferences are active by checking if the due_date is
	 * greater than or equal to the current date.
	 *
	 * @return 1 if the company preferences are active, 0 otherwise.
	 */
	@Query(value = "SELECT due_date >= CURDATE() AND active_date <= CURDATE() FROM company_preferences", nativeQuery = true)
	Integer isActive();

	/**
	 * Determines whether the company is active based on its preferences.
	 * Retrieves the active status using the isActive method, which checks against
	 * the database if the due_date is greater than or equal to the current date.
	 *
	 * @return True if the company is active (non-null and does not equal 0), false otherwise.
	 */
	public default Boolean isCompanyActive() {
		Integer isActive = isActive();
		return Objects.nonNull(isActive) && !isActive.equals(0);
	}

	/**
	 * Retrieves the subscription ID from the CompanyPreferencesModel.
	 *
	 * @return The subscription ID as a Long value.
	 */
	@Query("SELECT c.subscriptionId FROM CompanyPreferencesModel c")
	Long findSubscriptionId();

}