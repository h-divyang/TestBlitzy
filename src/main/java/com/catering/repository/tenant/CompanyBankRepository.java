package com.catering.repository.tenant;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.catering.model.tenant.CompanyBankModel;

/**
 * CompanyBankRepository is an interface for performing CRUD operations on the CompanyBankModel entity.
 * It extends JpaRepository to provide methods for standard database interactions.
 *
 * This repository includes a custom method to retrieve the first CompanyBankModel ordered by ascending ID.
 */
public interface CompanyBankRepository  extends JpaRepository<CompanyBankModel, Long> {

	/**
	 * Retrieves the first CompanyBankModel entity when ordered by ascending ID.
	 *
	 * @return An Optional containing the first CompanyBankModel entity if found, or an empty Optional if no entity exists.
	 */
	Optional<CompanyBankModel> findFirstByOrderByIdAsc();

}