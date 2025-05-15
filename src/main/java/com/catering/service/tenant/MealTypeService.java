package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.MealTypeDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MealTypeModel;
import com.catering.service.common.GenericService;

/**
 * Interface defining the service operations for handling Meal Type entities and DTOs.
 * Extends the GenericService interface, inheriting basic CRUD operation methods.
 *
 * @author Krushali Talaviya
 * @since June 2023
 *
 */
public interface MealTypeService extends GenericService<MealTypeDto, MealTypeModel, Long> {

	/**
	 * Creates or updates a Meal Type record based on the provided DTO.
	 *
	 * @param mealTypeDto The MealTypeDto object containing the data for creation or update.
	 * @return An Optional containing the created or updated MealTypeDto, or empty if an error occurs.
	 * @throws RestException If an exception occurs during the operation.
	 */
	Optional<MealTypeDto> createAndUpdate(MealTypeDto mealTypeDto) throws RestException;

	/**
	 * Retrieves a list of Meal Type records based on the provided filter criteria.
	 *
	 * @param filterDto The FilterDto object containing the filter criteria.
	 * @return A ResponseContainerDto containing a list of MealTypeDto objects matching the filter criteria.
	 */
	ResponseContainerDto<List<MealTypeDto>> read(FilterDto filterDto);

	/**
	 * Deletes a Meal Type record by its ID.
	 *
	 * @param id The ID of the Meal Type record to delete.
	 */
	void deleteById(Long id);

	/**
	 * Checks whether a record with the specified ID exists in the repository.
	 *
	 * @param id The unique identifier of the record to check.
	 * @return true if a record with the specified ID exists, false otherwise.
	 */
	boolean existById(Long id);

	/**
	 * Retrieves a list of MealTypeDto objects that are marked as active.
	 *
	 * @return A list of MealTypeDto objects where the "isActive" field is true.
	 */
	List<MealTypeDto> findByIsActiveTrue();

}