package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.FunctionTypeDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.FunctionTypeModel;
import com.catering.service.common.GenericService;

/**
 * The FunctionTypeService interface provides methods to manage FunctionTypeDto objects,
 * which represent data transfer objects for FunctionTypeModel entities.
 * It extends the GenericService interface with specific type parameters, FunctionTypeDto as the DTO type,
 * FunctionTypeModel as the entity type, and Long as the ID type.
 *
 * @author Krushali Talaviya
 * @since June 2023
 *
 */
public interface FunctionTypeService extends GenericService<FunctionTypeDto, FunctionTypeModel, Long> {

	/**
	 * Creates or updates a FunctionTypeDto object.
	 * 
	 * @param functionTypeDto The FunctionTypeDto object to be created or updated.
	 * @return An Optional containing the created or updated FunctionTypeDto object.
	 * @throws RestException if the FunctionTypeDto object already exists or if the associated FunctionTypeType does not exist.
	 */
	Optional<FunctionTypeDto> createAndUpdate(FunctionTypeDto functionTypeDto) throws RestException;

	/**
	 * Retrieves a list of FunctionTypeDto objects based on the provided filter.
	 * 
	 * @param filterDto The filter criteria for retrieving the FunctionTypeDto objects.
	 * @return A ResponseContainerDto containing a list of FunctionTypeDto objects.
	 */
	ResponseContainerDto<List<FunctionTypeDto>> read(FilterDto filterDto);

	/**
	 * Deletes a FunctionTypeDto object with the specified ID.
	 * 
	 * @param id The ID of the FunctionTypeDto object to be deleted.
	 * @throws RestException if the FunctionTypeDto object with the specified ID does not exist.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves a list of active FunctionTypeDto objects where the 'isActive' field is true.
	 *
	 * @return A list of FunctionTypeDto objects that are currently active.
	 */
	List<FunctionTypeDto> findByIsActiveTrue();

}