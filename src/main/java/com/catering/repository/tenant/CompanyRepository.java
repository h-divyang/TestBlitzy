package com.catering.repository.tenant;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.superadmin.CompanyModel;

/**
 * CompanyRepository is an interface for managing CompanyModel entities,
 * providing methods to interact with the underlying database through the
 * JpaRepository interface. It includes additional methods for custom queries
 * related to unique code and tenant fields in the CompanyModel entity.
 *
 * This repository extends JpaRepository to inherit basic CRUD operations
 * while also offering specialized methods for data retrieval and existence checks.
 */
public interface CompanyRepository extends JpaRepository<CompanyModel, Long> {

	/**
	 * Checks the existence of a CompanyModel entity by its unique code.
	 *
	 * @param uniqueCode The unique code to be checked for existence.
	 * @return True if an entity with the specified unique code exists, false otherwise.
	 */
	boolean existsByUniqueCode(String uniqueCode);

	/**
	 * Retrieves an Optional containing a CompanyModel entity that matches the specified unique code.
	 * If no entity is found, an empty Optional is returned.
	 *
	 * @param uniqueCode The unique code to search for in the CompanyModel entities.
	 * @return An Optional containing the matching CompanyModel entity, or an empty Optional if no match is found.
	 */
	Optional<CompanyModel> findByUniqueCode(String uniqueCode);

	/**
	 * Checks the existence of a CompanyModel entity by its associated tenant identifier.
	 *
	 * @param tenant The UUID of the tenant to be checked for existence.
	 * @return True if an entity associated with the specified tenant exists, false otherwise.
	 */
	boolean existsByTenant(UUID tenant);

	/**
	 * Retrieves an Optional containing a CompanyModel entity associated with the specified tenant UUID.
	 * If no entity is found, an empty Optional is returned.
	 *
	 * @param uuid The UUID of the tenant to search for in the CompanyModel entities.
	 * @return An Optional containing the matching CompanyModel entity, or an empty Optional if no match is found.
	 */
	Optional<CompanyModel> findByTenant(UUID uuid);

}