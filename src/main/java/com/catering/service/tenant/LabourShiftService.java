package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.LabourShiftDto;
import com.catering.model.tenant.LabourShiftModel;
import com.catering.service.common.GenericService;

/**
 * Interface for managing labour shifts, providing methods for creating, updating,
 * retrieving, and deleting labour shift records. This service extends the functionality
 * of the generic service interface.
 */
public interface LabourShiftService extends GenericService<LabourShiftDto, LabourShiftModel, Long> {

	/**
	 * Creates a new LabourShift or updates an existing one based on the provided LabourShiftDto.
	 *
	 * @param labourShiftDto The LabourShiftDto object containing the details of the labour shift to be created or updated.
	 * @return An Optional containing the created or updated LabourShiftDto. If the operation fails, an empty Optional is returned.
	 */
	Optional<LabourShiftDto> createAndUpdate(LabourShiftDto labourShiftDto);

	/**
	 * Retrieves a list of labour shifts based on the specified filtering criteria.
	 *
	 * @param filterDto The filtering criteria for retrieving labour shifts.
	 * @return A 'ResponseContainerDto' containing the list of 'LabourShiftDto' objects.
	 */
	ResponseContainerDto<List<LabourShiftDto>> read(FilterDto filterDto);

	/**
	 * Retrieves a list of LabourShiftDto objects where the 'isActive' property is true.
	 *
	 * @return A list of LabourShiftDto objects with 'isActive' property set to true.
	 */
	List<LabourShiftDto> findByIsActiveTrue();

}