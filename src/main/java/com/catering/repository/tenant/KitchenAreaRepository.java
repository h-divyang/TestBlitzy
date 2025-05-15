package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.KitchenAreaModel;

/**
 * KitchenAreaRepository is an interface for managing KitchenAreaModel entities,
 * providing methods to interact with the database using the JpaRepository interface
 * and supporting additional operations from the CommonNameExistenceRepository interface.
 *
 * This repository facilitates CRUD operations, query methods, and specific checks
 * on name fields across multiple languages for KitchenAreaModel entities.
 */
public interface KitchenAreaRepository extends JpaRepository<KitchenAreaModel, Long>, CommonNameExistenceRepository {

	/**
	 * Retrieves a list of KitchenAreaModel entities where the 'isActive' status matches the specified value.
	 * The results are sorted in ascending order by their unique identifier (ID).
	 *
	 * @param isActive The boolean value indicating the 'isActive' status to filter the KitchenAreaModel entities.
	 * @return A list of KitchenAreaModel entities that match the specified 'isActive' status, sorted by ID in ascending order.
	 */
	List<KitchenAreaModel> findAllByIsActiveOrderByIdAsc(boolean isActive);

}