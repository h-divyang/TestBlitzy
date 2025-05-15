package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.KitchenAreaDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.KitchenAreaModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing and performing operations on kitchen area entities.
 * Extends the GenericService interface for common CRUD operations.
 */
public interface KitchenAreaService extends GenericService<KitchenAreaDto, KitchenAreaModel, Long> {

	/**
	 * Creates or updates a kitchen area based on the provided KitchenAreaDto.
	 *
	 * @param kitchenAreaDto The data transfer object representing the kitchen area to be created or updated.
	 * @return An Optional containing the created or updated KitchenAreaDto if successful, or an empty Optional if the operation failed.
	 * @throws RestException if an error occurs during the create or update process.
	 */
	Optional<KitchenAreaDto> createAndUpdate(KitchenAreaDto kitchenAreaDto) throws RestException;

	/**
	 * Retrieves a list of kitchen areas based on the filtering criteria specified in the provided FilterDto.
	 *
	 * @param filterDto The filtering criteria used to retrieve the kitchen area data.
	 * @return A ResponseContainerDto containing a list of KitchenAreaDto and associated response metadata.
	 */
	ResponseContainerDto<List<KitchenAreaDto>> read(FilterDto filterDto);

	/**
	 * Deletes a kitchen area entity based on the provided unique identifier.
	 *
	 * @param id The unique identifier of the kitchen area to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Checks if an entity with the specified ID exists.
	 * Throws a {@link RestException} if the entity does not exist.
	 *
	 * @param id The unique identifier of the entity to check for existence. Must not be null.
	 * @throws RestException If the entity with the specified ID does not exist.
	 */
	void existByIdOrThrow(Long id) throws RestException;

	/**
	 * Retrieves a list of kitchen area data where the 'isActive' status is true.
	 *
	 * @return A list of KitchenAreaDto objects representing active kitchen areas.
	 */
	List<KitchenAreaDto> readDataByIsActive();

}