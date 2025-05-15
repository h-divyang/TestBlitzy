package com.catering.dao.input_transfer_to_hall;

import java.util.List;

import com.catering.dto.tenant.request.InputTransferToHallCalculationDto;
import com.catering.dto.tenant.request.InputTransferToHallRawMaterialDropDownDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;

/**
 * Service interface for managing input transfer to hall related operations.
 * This interface defines methods to interact with the data layer to retrieve
 * upcoming orders, raw materials, and calculations for input transfer to hall.
 */
public interface InputTransferToHallNativeQueryService {

	/**
	 * Retrieves a list of upcoming orders for input transfer to hall.
	 * 
	 * This method calls the corresponding service to fetch the upcoming orders.
	 * It returns a list of orders with details such as customer names, event types,
	 * and event main dates.
	 *
	 * @param orderId The order ID to exclude from the results (optional).
	 * @return A list of {@link InputTransferToHallUpcomingOrderDto} containing upcoming order details.
	 */
	List<InputTransferToHallUpcomingOrderDto> getUpcomingOrdersForInputTransferToHall(Long orderId);

	/**
	 * Retrieves the raw material information for a specific order ID in the input transfer to hall process.
	 * 
	 * This method fetches the raw materials associated with the given order ID and returns a list of raw materials.
	 * The quantities can be adjusted depending on the flag passed to the service.
	 *
	 * @param orderId The order ID to fetch raw materials for.
	 * @return A list of {@link InputTransferToHallUpcomingOrderRawMaterial} containing raw material details.
	 */
	List<InputTransferToHallUpcomingOrderRawMaterial> findInputTransferToHallRawMaterialByOrderId(Long orderId);

	/**
	 * Retrieves the calculation for input transfer to hall based on a specific ID.
	 * 
	 * This method fetches the calculated values for input transfer to hall based on the provided ID.
	 * The calculation includes aggregated data related to input transfer and associated raw materials.
	 *
	 * @param id The ID of the input transfer to hall for which calculations are needed.
	 * @return A {@link InputTransferToHallCalculationDto} containing the calculated data for the input transfer.
	 */
	InputTransferToHallCalculationDto getInputTransferToHallCalculation(Long id);

	/**
	 * Retrieves a list of raw materials available for input transfer to hall.
	 * 
	 * This method returns a list of raw materials with their details such as ID, names in different languages,
	 * and associated measurement IDs for use in input transfer to hall operations.
	 *
	 * @return A list of {@link InputTransferToHallRawMaterialDropDownDto} containing raw material information.
	 */
	List<InputTransferToHallRawMaterialDropDownDto> getRawMaterial();

}