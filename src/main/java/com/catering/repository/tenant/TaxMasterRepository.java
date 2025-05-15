package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.TaxMasterModel;

/**
 * The TaxMasterRepository interface provides methods to interact with the database
 * and perform operations on the TaxMasterModel entity.
 *
 * @author Neel Bhanderi
 * @since March 2024
 *
 */
public interface TaxMasterRepository extends JpaRepository<TaxMasterModel, Long>, CommonNameExistenceRepository {

	/**
	 * Retrieves all active tax records from the database.
	 * 
	 * This method fetches all records of type `TaxMasterModel` that are marked as active.
	 * 
	 * @return a list of active `TaxMasterModel` objects
	 */
	List<TaxMasterModel> findAllByIsActiveTrue();

}