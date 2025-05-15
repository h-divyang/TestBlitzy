package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.TaxMasterDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.TaxMasterModel;
import com.catering.service.common.GenericService;

/**
 * The TaxMasterService interface provides methods to manage TaxMasterDto objects,
 * which represent data transfer objects for TaxMasterModel entities.
 * It extends the GenericService interface with specific type parameters, TaxMasterDto as the DTO type,
 * TaxMasterModel as the entity type, and Long as the ID type.
 *
 * @author Neel Bhanderi
 * @since March 2024
 */
public interface TaxMasterService extends GenericService<TaxMasterDto, TaxMasterModel, Long> {

	/**
	 * Creates or updates a TaxMasterDto object.
	 * 
	 * @param taxMasterDto The TaxMasterDto object to be created or updated.
	 * @return An Optional containing the created or updated TaxMasterDto object.
	 * @throws RestException If the TaxMasterDto object already exists.
	 */
	Optional<TaxMasterDto> createAndUpdate(TaxMasterDto taxMasterDto);

	/**
	 * Retrieves a list of TaxMasterDto objects based on the provided filter.
	 * 
	 * @param filterDto The filter criteria for retrieving the TaxMasterDto objects.
	 * @return A ResponseContainerDto containing a list of TaxMasterDto objects.
	 */
	ResponseContainerDto<List<TaxMasterDto>> read(FilterDto filterDto);

	/**
	 * Deletes a TaxMasterDto object with the specified ID.
	 * 
	 * @param id The ID of the TaxMasterDto object to be deleted.
	 * @throws RestException if the TaxMasterDto object with the specified ID does not exist.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves a list of TaxMasterDto objects that are marked as active (isActive is true).
	 *
	 * @return a list of active TaxMasterDto objects.
	 */
	List<TaxMasterDto> findAllByIsActiveTrue();

}