package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.tenant.request.CommonNotesDto;
import com.catering.dto.tenant.request.MenuAllocationDTO;
import com.catering.dto.tenant.request.RawMaterialCalculationDto;
import com.catering.dto.tenant.request.RawMaterialCalculationResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.GetMenuAllocationOrderMenuPreparationModel;

/**
 * Service interface for handling operations related to menu allocations,
 * raw material cost calculations, and associated order preparations.
 */
public interface MenuAllocationService {

	/**
	 * Creates and updates menu allocations for a given order.
	 *
	 * @param menuAllocationDTOs A list of MenuAllocationDTO objects containing the menu allocation details to be created or updated.
	 * @param orderId The unique identifier of the order for which the menu allocations are to be created or updated.
	 */
	void createAndUpdate(List<MenuAllocationDTO> menuAllocationDTOs, Long orderId);

	/**
	 * Calculates the cost of raw materials based on the provided input details.
	 *
	 * @param rawMaterialCalculationDto An object containing details such as order function ID and menu item ID
	 *									that are used to calculate the raw material cost.
	 * @return A RawMaterialCalculationResponseDto object containing the calculated total cost of raw materials.
	 */
	RawMaterialCalculationResponseDto calculateRawMaterialCost(RawMaterialCalculationDto rawMaterialCalculationDto);

	/**
	 * Retrieves a list of menu allocation and preparation details for a specific order.
	 *
	 * @param orderId The unique identifier of the order whose menu allocation and preparation details are to be retrieved.
	 * @return A list of GetMenuAllocationOrderMenuPreparationModel objects containing the menu allocation and preparation details for the specified order.
	 */
	List<GetMenuAllocationOrderMenuPreparationModel> findByOrderId(Long orderId);

	/**
	 * Updates the notes for a menu allocation.
	 *
	 * @param menuAllocationNotes An object containing updated notes with details such as default language, preferred language, and supportive language.
	 */
	void updateNotes(CommonNotesDto menuAllocationNotes);

	/**
	 * Synchronizes the raw material data for a given order.
	 *
	 * @param orderId The unique identifier of the order for which raw material data is to be synchronized.
	 */
	void syncRawMaterial(Long orderId);

	/**
	 * Deletes a specific entity by its unique identifier.
	 *
	 * @param id The unique identifier of the entity to be deleted.
	 * @throws RestException Thrown if an issue occurs during the delete operation.
	 */
	void deleteById(Long id) throws RestException;

}