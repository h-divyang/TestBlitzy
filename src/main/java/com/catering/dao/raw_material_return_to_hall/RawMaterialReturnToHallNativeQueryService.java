package com.catering.dao.raw_material_return_to_hall;

import java.util.List;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.dto.tenant.request.RawMaterialReturnToHallCalculationDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallInputTransferToHallDropDownDto;

/**
 * Service interface for handling operations related to raw material return to hall data.
 * This interface defines methods for fetching dropdown data, calculating raw material return,
 * and retrieving raw materials based on input transfer to hall data.
 * 
 * <p>These operations are commonly used in scenarios involving managing raw materials
 * and their associated calculations, particularly in the context of input transfers to halls.</p>
 */
public interface RawMaterialReturnToHallNativeQueryService {

	/**
	 * Fetches a list of dropdown data for raw material return to hall input transfer.
	 * This method retrieves relevant data that can be used to populate UI dropdowns 
	 * or provide filtered input options based on the given input transfer to hall ID.
	 *
	 * @param inputTransferToHallId the ID of the input transfer to hall for which the dropdown data is required
	 * @return a list of {@link RawMaterialReturnToHallInputTransferToHallDropDownDto} containing dropdown data
	 */
	List<RawMaterialReturnToHallInputTransferToHallDropDownDto> getRawMaterialReturnToHallInputTransferToHallDropDownData(Long inputTransferToHallId);

	/**
	 * Retrieves the calculation data related to raw material return to hall for a specific ID.
	 * This method performs necessary calculations or aggregations based on the raw material data 
	 * and returns a {@link RawMaterialReturnToHallCalculationDto} containing the results.
	 *
	 * @param id the ID of the raw material return to hall for which calculation data is requested
	 * @return a {@link RawMaterialReturnToHallCalculationDto} containing the result of the calculation
	 */
	RawMaterialReturnToHallCalculationDto getRawMaterialReturnToHallCalculation(Long id);

	/**
	 * Finds the raw materials associated with a specific input transfer to hall ID.
	 * This method fetches all raw materials and their respective quantities associated with 
	 * the provided input transfer to hall ID.
	 *
	 * @param inputTransferToHallId the ID of the input transfer to hall for which raw materials are to be retrieved
	 * @return a list of {@link InputTransferToHallUpcomingOrderRawMaterial} representing raw materials
	 *         related to the specified input transfer to hall
	 */
	List<InputTransferToHallUpcomingOrderRawMaterial> findRawMaterialByInputTransferToHallId(Long inputTransferToHallId);

}