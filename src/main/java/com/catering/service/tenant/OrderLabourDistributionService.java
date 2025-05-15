package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.tenant.request.CommonNotesDto;
import com.catering.dto.tenant.request.OrderLabourDistributionDto;
import com.catering.exception.RestException;

/**
 * This interface provides methods for managing labour distribution data
 * associated with orders. It includes operations for creating, updating,
 * retrieving, deleting, and validating such data, as well as updating associated notes.
 */
public interface OrderLabourDistributionService {

	/**
	 * Creates and updates a list of OrderLabourDistributionDto objects.
	 *
	 * @param orderLabourDistributionDtos The list of OrderLabourDistributionDto objects to create or update. Each object in the list must contain the necessary data for processing.
	 * @return A list of OrderLabourDistributionDto objects after they have been successfully created or updated.
	 */
	List<OrderLabourDistributionDto> createAndUpdate(List<OrderLabourDistributionDto> orderLabourDistributionDtos);

	/**
	 * Finds and retrieves a list of OrderLabourDistributionDto objects associated with the given order function ID.
	 *
	 * @param orderId The unique identifier of the order function for which the labour distribution details are to be retrieved.
	 * @return An Optional containing a list of OrderLabourDistributionDto objects associated with the given order function ID.
	 */
	Optional<List<OrderLabourDistributionDto>> findByOrderFunctionId(Long orderId);

	/**
	 * Deletes an entity identified by its unique ID.
	 *
	 * @param id The unique identifier of the entity to be deleted.
	 * @throws RestException if the deletion operation fails.
	 */
	void deleteById(Long id) throws RestException;

	/**
	 * Checks if an entity exists by the specified godown ID.
	 *
	 * @param id The unique identifier of the godown to check existence for.
	 * @return A boolean value indicating whether an entity with the given godown ID exists.
	 */
	boolean existsByGodownId(Long id);

	/**
	 * Updates the notes information based on the provided CommonNotesDto object.
	 *
	 * @param commonNotesDto The CommonNotesDto object containing updated notes details.
	 */
	void updateNotes(CommonNotesDto commonNotesDto);

}