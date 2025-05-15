package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.RawMaterialCategoryTypeDto;
import com.catering.exception.RestException;

/**
 * Service interface for managing raw material category types.
 *
 * This service is responsible for providing CRUD operations,
 * data retrieval with filtering, and validation for raw material category types.
 * It extends both GenericService and CommonDataService, offering a comprehensive
 * set of features for managing RawMaterialCategoryTypeDto and RawMaterialCategoryTypeModel entities.
 */
public interface RawMaterialCategoryTypeService {

	/**
	 * Checks the existence of an entity with the specified ID and throws a RestException if it does not exist.
	 *
	 * @param id The unique identifier of the entity to check.
	 * @throws RestException If no entity with the given ID is found.
	 */
	void existByIdOrThrow(Long id) throws RestException;

	/**
	 * Reads a list of RawMaterialCategoryTypeDto objects based on the provided filter criteria.
	 *
	 * @param filterDto The filter criteria for retrieving raw material category types.
	 * @return A ResponseContainerDto containing the list of RawMaterialCategoryTypeDto objects and metadata.
	 */
	ResponseContainerDto<List<RawMaterialCategoryTypeDto>> read(FilterDto filterDto);

}