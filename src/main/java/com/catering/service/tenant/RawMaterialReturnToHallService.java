package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.dto.tenant.request.RawMaterialReturnToHallDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallInputTransferToHallDropDownDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallResponseDto;
import com.catering.model.tenant.RawMaterialReturnToHallModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing raw material returns to a hall, providing methods
 * for creating, updating, retrieving, deleting, and listing raw material return entries.
 * This interface extends the GenericService, inheriting core CRUD operations and
 * additional query functionalities.
 */
public interface RawMaterialReturnToHallService extends GenericService<RawMaterialReturnToHallResponseDto, RawMaterialReturnToHallModel, Long> {

	/**
	 * Creates or updates a raw material return to hall entry based on the provided data.
	 *
	 * @param rawMaterialReturnToHallDto The RawMaterialReturnToHallDto object containing details
	 *									 of the raw material return to hall to be created or updated.
	 * @return An Optional containing the created or updated RawMaterialReturnToHallDto if successful, or an empty Optional otherwise.
	 */
	Optional<RawMaterialReturnToHallDto> createUpdateRawMaterialReturnToHall(RawMaterialReturnToHallDto rawMaterialReturnToHallDto);

	/**
	 * Deletes a raw material return to hall entry based on its unique identifier.
	 *
	 * @param id The unique identifier of the raw material return to hall entry to be deleted.
	 */
	void deleteRawMaterialReturnToHall(Long id);

	/**
	 * Retrieves the details of a raw material return to hall entry based on the provided identifier.
	 *
	 * @param id The unique identifier of the raw material return to hall entry to be retrieved.
	 * @return An Optional containing the RawMaterialReturnToHallDto if the entry is found, or an empty Optional if not.
	 */
	Optional<RawMaterialReturnToHallDto> getRawMaterialReturnToHall(Long id);

	/**
	 * Retrieves a list of raw material return to hall entries based on the provided filter criteria.
	 *
	 * @param filterDto The FilterDto object containing filtering, sorting, and pagination parameters.
	 * @return A ResponseContainerDto containing a list of RawMaterialReturnToHallResponseDto objects
	 *		   along with metadata such as status, message, and paging information.
	 */
	ResponseContainerDto<List<RawMaterialReturnToHallResponseDto>> getAllRawMaterialReturnToHall(FilterDto filterDto);

	/**
	 * Retrieves a list of drop down data for raw material return to hall input transfer to hall entries
	 * based on the provided input transfer to hall identifier.
	 *
	 * @param inputTransferToHallId The unique identifier of the input transfer to hall for which
	 *								the drop down data is to be retrieved.
	 * @return A list of RawMaterialReturnToHallInputTransferToHallDropDownDto containing the drop down data
	 *		   for the specified input transfer to hall.
	 */
	List<RawMaterialReturnToHallInputTransferToHallDropDownDto> getRawMaterialReturnToHallInputTransferToHallDropDownData(Long inputTransferToHallId);

	/**
	 * Retrieves a list of raw materials associated with a specific input transfer to hall identifier.
	 *
	 * @param inputTransferToHallId The unique identifier of the input transfer to hall.
	 * @return A list of InputTransferToHallUpcomingOrderRawMaterial objects representing the raw materials
	 *		   for the specified input transfer to hall.
	 */
	List<InputTransferToHallUpcomingOrderRawMaterial> findRawMaterialByInputTransferToHallId(Long inputTransferToHallId);

}