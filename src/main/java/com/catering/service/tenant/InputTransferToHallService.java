package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.InputTransferToHallDto;
import com.catering.dto.tenant.request.InputTransferToHallRawMaterialDropDownDto;
import com.catering.dto.tenant.request.InputTransferToHallResponseDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.model.tenant.InputTransferToHallModel;
import com.catering.service.common.GenericService;

/**
 * Provides services to manage input transfers to halls, including creating, updating,
 * retrieving, and deleting operations, as well as retrieving related information
 * such as upcoming orders and raw materials.
 */
public interface InputTransferToHallService extends GenericService<InputTransferToHallResponseDto, InputTransferToHallModel, Long> {

	/**
	 * Creates or updates an input transfer to hall based on the provided InputTransferToHallDto.
	 *
	 * @param inputTransferToHallDto The data transfer object containing the details for creating or updating an input transfer to hall.
	 * @return An Optional containing the created or updated InputTransferToHallDto.
	 */
	Optional<InputTransferToHallDto> createUpdateInputTransferToHall(InputTransferToHallDto inputTransferToHallDto);

	/**
	 * Deletes an existing input transfer to hall entry based on its unique identifier.
	 *
	 * @param id The unique identifier of the input transfer to hall to be deleted.
	 */
	void deleteInputTransferToHall(Long id);

	/**
	 * Retrieves the details of an input transfer to hall based on its unique identifier.
	 *
	 * @param id The unique identifier of the input transfer to hall to be retrieved.
	 * @return An Optional containing the InputTransferToHallDto if found; otherwise, an empty Optional.
	 */
	Optional<InputTransferToHallDto> getInputTransferToHall(Long id);

	/**
	 * Retrieves all input transfers to hall based on provided filter criteria.
	 *
	 * @param filterDto The filter criteria including pagination, sorting, and query information.
	 * @return A ResponseContainerDto containing a list of InputTransferToHallResponseDto objects.
	 */
	ResponseContainerDto<List<InputTransferToHallResponseDto>> getAllInputTransferToHall(FilterDto filterDto);

	/**
	 * Retrieves a list of upcoming orders associated with input transfers to hall for a given order ID.
	 *
	 * @param orderId The unique identifier of the order for which upcoming orders need to be fetched.
	 * @return A list of InputTransferToHallUpcomingOrderDto objects containing details of the upcoming orders.
	 */
	List<InputTransferToHallUpcomingOrderDto> getUpcomingOrdersForInputTransferToHall(Long orderId);

	/**
	 * Finds and retrieves a list of raw materials associated with the input transfer to the hall for a specific order ID.
	 *
	 * @param orderId The unique identifier of the order for which the raw materials need to be fetched.
	 * @return A list of InputTransferToHallUpcomingOrderRawMaterial objects.
	 */
	List<InputTransferToHallUpcomingOrderRawMaterial> findInputTransferToHallRawMaterialByOrderId(Long orderId);

	/**
	 * Retrieves a list of raw materials available for input transfer to hall operations.
	 *
	 * @return A list of InputTransferToHallRawMaterialDropDownDto objects representing the raw material dropdown options.
	 */
	List<InputTransferToHallRawMaterialDropDownDto> getRawMaterial();

}