package com.catering.repository.tenant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.GodownModel;

/**
 * GodownRepository is an interface for managing GodownModel entities,
 * providing methods to interact with the underlying database through the JpaRepository interface.
 * This repository supports common CRUD operations along with custom query methods.
 *
 * It extends JpaRepository to inherit basic data access functionality and
 * CommonNameExistenceRepository to include methods for checking the existence
 * of entities based on name fields in multiple languages.
 */
public interface GodownRepository extends JpaRepository<GodownModel, Long>, CommonNameExistenceRepository {

	/**
	 * Retrieves a list of GodownModel entities where the 'isActive' flag is set to true.
	 *
	 * @return A list of GodownModel entities with 'isActive' set to true.
	 */
	List<GodownModel> findByIsActiveTrue();

}