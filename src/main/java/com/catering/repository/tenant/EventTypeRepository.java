package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.EventTypeModel;

/**
 * EventTypeRepository is an interface for managing EventTypeModel entities,
 * providing methods for interaction with the underlying database.
 *
 * This repository extends JpaRepository to facilitate basic CRUD operations
 * and CommonNameExistenceRepository to support custom existence checks based
 * on entity name fields. Additionally, it includes a method to retrieve
 * all active EventTypeModel entities.
 */
public interface EventTypeRepository extends JpaRepository<EventTypeModel, Long>, CommonNameExistenceRepository {

	/**
	 * Finds all EventTypeModel entities where the isActive field is set to true.
	 *
	 * @return A list of active EventTypeModel entities, or an empty list if no active entities are found.
	 */
	List<EventTypeModel> findByIsActiveTrue();

}