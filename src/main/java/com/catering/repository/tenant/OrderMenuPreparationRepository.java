package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.OrderMenuPreparationModel;

/**
 * Repository interface for managing {@link OrderMenuPreparationModel} entities.
 * 
 * This interface extends {@link JpaRepository} to provide standard CRUD operations on {@link OrderMenuPreparationModel}.
 * It includes custom methods for checking the existence of menu preparations based on custom package IDs.
 */
public interface OrderMenuPreparationRepository extends JpaRepository<OrderMenuPreparationModel, Long> {

	/**
	 * Checks if a menu preparation exists for a given custom package ID.
	 * 
	 * This method verifies if an {@link OrderMenuPreparationModel} exists for the specified custom package ID.
	 * It returns {@code true} if a record exists, and {@code false} otherwise.
	 * 
	 * @param id the custom package ID to check
	 * @return {@code true} if a menu preparation exists for the specified custom package ID; {@code false} otherwise
	 */
	boolean existsByCustomPackageId(Long id);

}