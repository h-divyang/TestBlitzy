package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.ContactCategoryTypeDto;
import com.catering.exception.RestException;

/**
 * Service interface for managing ContactCategoryType entities and their respective DTOs.
 * This interface provides methods for validation, CRUD operations, and specific business
 * logic related to ContactCategoryType entities.
 */
public interface ContactCategoryTypeService {

	/**
	 * Validates the existence of an entity by its unique identifier.
	 *
	 * @param id The unique identifier of the entity.
	 * @throws RestException If the entity with the specified ID does not exist.
	 */
	void existByIdOrThrow(Long id) throws RestException;

	/**
	 * Reads a list of ContactCategoryTypeDto based on the provided filter criteria.
	 *
	 * @param filterDto The filter criteria used to retrieve ContactCategoryTypeDto entities.
	 * @return A ResponseContainerDto containing a list of ContactCategoryTypeDto entities along with response metadata.
	 */
	ResponseContainerDto<List<ContactCategoryTypeDto>> read(FilterDto filterDto);

}