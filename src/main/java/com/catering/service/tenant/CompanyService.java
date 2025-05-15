package com.catering.service.tenant;

import java.util.Optional;
import java.util.UUID;
import com.catering.dto.tenant.request.CompanyDto;

/**
 * Interface providing methods for operations related to Company entities,
 * such as checking existence and retrieving company details by various identifiers.
 */
public interface CompanyService {

	/**
	 * Checks if a tenant exists by its unique identifier.
	 *
	 * @param tenant The UUID of the tenant to check.
	 * @return True if a tenant with the specified UUID exists, false otherwise.
	 */
	boolean existsByTenant(UUID tenant);

	/**
	 * Checks if an entity exists by its unique code.
	 *
	 * @param header The unique code of the entity to check.
	 * @return True if an entity with the specified unique code exists, false otherwise.
	 */
	boolean existsByUniqueCode(String header);

	/**
	 * Retrieves a company entity by its unique code.
	 *
	 * @param header The unique code of the company to retrieve.
	 * @return An Optional containing the matching CompanyDto if found, or an empty Optional if no match is found.
	 */
	Optional<CompanyDto> findByByUniqueCode(String header);

	/**
	 * Retrieves a company entity by its unique identifier.
	 *
	 * @param fkCompanyId The unique identifier of the company to retrieve.
	 * @return An Optional containing the matching CompanyDto if found, or an empty Optional if no match is found.
	 */
	Optional<CompanyDto> findById(Long fkCompanyId);

	/**
	 * Retrieves a company entity based on its tenant's unique identifier.
	 *
	 * @param uuid The unique identifier of the tenant associated with the company.
	 * @return An Optional containing the matching CompanyDto if a company entity with the specified tenant UUID exists,
	 *		   or an empty Optional if no such entity is found.
	 */
	Optional<CompanyDto> findByTenant(UUID uuid);

}