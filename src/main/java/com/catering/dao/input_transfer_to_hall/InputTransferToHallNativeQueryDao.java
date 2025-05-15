package com.catering.dao.input_transfer_to_hall;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.InputTransferToHallCalculationDto;
import com.catering.dto.tenant.request.InputTransferToHallRawMaterialDropDownDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;

/**
 * Repository interface for handling native queries related to input transfer to hall operations.
 * Extends {@link JpaRepository} to provide CRUD operations on the {@link InputTransferToHallNativeQuery} entity.
 */
public interface InputTransferToHallNativeQueryDao extends JpaRepository<InputTransferToHallNativeQuery, Long> {

	/**
	 * Retrieves a list of upcoming orders for input transfer to hall.
	 * 
	 * This method executes the named native query {@code upcomingOrdersForInputTransferToHall}.
	 * The query returns a list of upcoming orders with details such as customer names and event types.
	 *
	 * @param orderId The order ID to exclude from the results (optional).
	 * @return A list of {@link InputTransferToHallUpcomingOrderDto} containing order details.
	 */
	@Query(name = "upcomingOrdersForInputTransferToHall", nativeQuery = true)
	List<InputTransferToHallUpcomingOrderDto> getUpcomingOrdersForInputTransferToHall(Long orderId);

	/**
	 * Retrieves the raw material information for a specific order ID in the input transfer to hall process.
	 * 
	 * This method executes the named native query {@code findInputTransferToHallRawMaterialByOrderId}.
	 * It returns a list of raw materials associated with the specified order ID, adjusting quantities as needed.
	 *
	 * @param orderId The order ID to fetch raw materials for.
	 * @param isAdjustQuantity A flag indicating whether quantities should be adjusted.
	 * @return A list of {@link InputTransferToHallUpcomingOrderRawMaterial} containing raw material details.
	 */
	@Query(name = "findInputTransferToHallRawMaterialByOrderId", nativeQuery = true)
	List<InputTransferToHallUpcomingOrderRawMaterial> findInputTransferToHallRawMaterialByOrderId(Long orderId, Boolean isAdjustQuantity);

	/**
	 * Retrieves the calculation for input transfer to hall based on a specific ID.
	 * 
	 * This method executes the named native query {@code getInputTransferToHallCalculation}.
	 * It returns a DTO containing the calculated results for input transfer to hall based on the provided ID.
	 *
	 * @param id The ID of the input transfer to hall for which calculations are needed.
	 * @return A {@link InputTransferToHallCalculationDto} containing the calculated data.
	 */
	@Query(name = "getInputTransferToHallCalculation", nativeQuery = true)
	InputTransferToHallCalculationDto getInputTransferToHallCalculation(Long id);

	/**
	 * Retrieves a list of raw materials available for input transfer to hall.
	 * 
	 * This method executes the named native query {@code getRawMaterial}.
	 * It returns a list of raw materials with their details such as ID, names in different languages, and associated measurement IDs.
	 *
	 * @return A list of {@link InputTransferToHallRawMaterialDropDownDto} containing raw material information.
	 */
	@Query(name = "getRawMaterial", nativeQuery = true)
	List<InputTransferToHallRawMaterialDropDownDto> getRawMaterial();

}