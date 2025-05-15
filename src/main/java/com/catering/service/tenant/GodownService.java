package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.GodownDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.GodownModel;
import com.catering.service.common.GenericService;

/**
 * Interface representing the service layer for managing godown-related operations.
 * Provides methods for creating, updating, reading, deleting, and checking the existence of godowns.
 * Includes additional specialized methods to fetch active godowns.
 */
public interface GodownService extends GenericService<GodownDto, GodownModel, Long> {

	/**
	 * Creates a new godown or updates an existing godown based on the provided GodownDto.
	 *
	 * @param godownDto The GodownDto object containing the details of the godown to be created or updated.
	 * @return An Optional containing the created or updated GodownDto. If the operation fails, an empty Optional is returned.
	 * @throws RestException If there is an error during the creation or update process.
	 */
	Optional<GodownDto> createAndUpdate(GodownDto godownDto) throws RestException;

	/**
	 * Retrieves a list of godowns based on the provided filter criteria.
	 *
	 * @param filterDto The filter criteria used to retrieve the list of godown.
	 * @return A ResponseContainerDto containing a list of GodownDto objects that match the filter criteria.
	 */
	ResponseContainerDto<List<GodownDto>> read(FilterDto filterDto);

	/**
	 * Deletes an entity or record based on its unique identifier.
	 *
	 * @param id The unique identifier of the entity or record to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves a list of godowns that are marked as active.
	 *
	 * @return A list of GodownDto objects representing the active godowns.
	 */
	List<GodownDto> findByIsActiveTrue();

}