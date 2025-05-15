package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.MealTypeModel;

/**
 * Repository interface for managing Meal Type entities.
 *
 * @author Krushali Talaviya
 * @since June 2023
 *
 */
public interface MealTypeRepository extends JpaRepository<MealTypeModel, Long>, CommonNameExistenceRepository {

	/**
	 * Retrieves a list of active meal types.
	 * @return a list of active meal types
	 */
	List<MealTypeModel> findByIsActiveTrue();

}