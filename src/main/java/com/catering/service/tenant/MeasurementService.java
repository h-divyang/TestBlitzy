package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.CustomRangeDto;
import com.catering.dto.tenant.request.MeasurementDto;
import com.catering.dto.tenant.request.MeasurementWithCustomRangeDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MeasurementModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing measurements. Provides methods for creating, updating,
 * validating, and querying measurement entities and their related data transfer objects (DTOs).
 * Extends the GenericService interface for common CRUD operations.
 */
public interface MeasurementService extends GenericService<MeasurementDto, MeasurementModel, Long> {

	/**
	 * Creates or updates a measurement entity based on the provided 'MeasurementDto' object.
	 *
	 * @param measurementDto The 'MeasurementDto' object containing the data to create or update a measurement.
	 * @return An 'Optional' containing the created or updated 'MeasurementDto' object
	 *		   if the operation is successful, or an empty 'Optional' if the operation fails.
	 * @throws RestException If there is an error during the creation or update operation.
	 */
	Optional<MeasurementWithCustomRangeDto> createAndUpdate(MeasurementWithCustomRangeDto measurementDto) throws RestException;

	/**
	 * Checks if an entity with the given ID exists. If the entity does not exist, it throws a 'RestException'.
	 *
	 * @param id The unique identifier of the entity to check for existence.
	 * @throws RestException If no entity with the specified ID is found.
	 */
	void existByIdOrThrow(Long id) throws RestException;

	/**
	 * Retrieves a list of measurements based on the provided filter criteria.
	 *
	 * @param filterDto An object containing filtering, pagination, and sorting options for fetching measurement data.
	 * @return A ResponseContainerDto object containing a list of MeasurementDto instances that match the filtering criteria, along with response metadata.
	 */
	ResponseContainerDto<List<MeasurementDto>> read(FilterDto filterDto);

	/**
	 * Deletes an entity identified by its unique ID.
	 *
	 * @param id The unique identifier of the entity to be deleted.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves a list of MeasurementDto objects where the field 'isBaseUnit' is set to true.
	 *
	 * @return A list of MeasurementDto objects with 'isBaseUnit' set to true.
	 */
	List<MeasurementDto> getAllByIsBaseUnitTrue();

	/**
	 * Retrieves a list of MeasurementDto objects where the 'isActive' field is set to true.
	 *
	 * @return A list of MeasurementDto objects with 'isActive' set to true.
	 */
	List<MeasurementDto> readDataByIsActive();

	/**
	 * Retrieves a list of custom ranges associated with a given measurement unit ID.
	 * 
	 * @param measurementId The ID of the measurement unit for which the custom ranges need to be retrieved.
	 * @return A list of {@link CustomRangeDto} objects representing the custom ranges.
	 */
	List<CustomRangeDto> getCustomRangeByMeasurementId(Long measurementId);

}