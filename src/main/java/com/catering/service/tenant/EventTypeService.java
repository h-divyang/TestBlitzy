package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.EventTypeDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.EventTypeModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing EventType entities and their corresponding Data Transfer Objects (DTOs).
 * Extends the GenericService interface to inherit basic CRUD operations and provides additional functionalities
 * specific to EventType entities.
 */
public interface EventTypeService extends GenericService<EventTypeDto, EventTypeModel, Long> {

	/**
	 * Creates or updates the given EventTypeDto entity.
	 *
	 * @param eventTypeDto The EventTypeDto object containing details for creation or update.
	 * @return An Optional containing the created or updated EventTypeDto.
	 * @throws RestException If there is an error during the operation.
	 */
	Optional<EventTypeDto> createAndUpdate(EventTypeDto eventTypeDto) throws RestException;

	/**
	 * Reads a list of EventTypeDto objects based on the filtering criteria provided in the FilterDto.
	 *
	 * @param filterDto The filter criteria for retrieving event types.
	 * @return A ResponseContainerDto containing a list of EventTypeDto objects that match the filter criteria.
	 */
	ResponseContainerDto<List<EventTypeDto>> read(FilterDto filterDto);

	/**
	 * Deletes an entity by its unique identifier.
	 *
	 * @param id The unique identifier of the entity to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves a list of event types that are marked as active.
	 *
	 * @return A List containing EventTypeDto objects representing the active event types.
	 */
	List<EventTypeDto> findByIsActiveTrue();

}