package com.catering.repository.superadmin;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.superadmin.CateringPreferencesModel;

/**
 * Repository interface for managing catering preferences data.
 *
 * Extends the {@link JpaRepository} to provide basic CRUD operations and query method support for the
 * {@code CateringPreferencesModel} entity. This repository enables interaction with the database table
 * associated with catering preferences. It includes a custom query method to fetch the first record
 * ordered by ID in ascending order.
 */
public interface CateringPreferencesRepository extends JpaRepository<CateringPreferencesModel, Long> {

	/**
	 * Retrieves the first record of the CateringPreferencesModel entity ordered by ID in ascending order.
	 *
	 * @return The first {@code CateringPreferencesModel} record sorted by ID in ascending order.
	 *		   Returns {@code null} if no records are found.
	 */
	CateringPreferencesModel findFirstByOrderByIdAsc();

}