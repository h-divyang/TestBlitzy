package com.catering.service.tenant;

import com.catering.dto.tenant.request.CommonNotesDto;
import com.catering.dto.tenant.request.FunctionAddressDto;
import com.catering.dto.tenant.request.OrderFunctionRawMaterialTimeDto;

/**
 * Service interface for managing orders and related functional details.
 * This interface defines methods to update specific details of an order function,
 * including rates, notes, and address information.
 */
public interface OrderFunctionService {

	/**
	 * Updates the rate for a specific entity identified by its unique ID.
	 *
	 * @param rate The new rate to be set.
	 * @param id The unique identifier of the entity for which the rate is being updated.
	 */
	void updateRate(Double rate, Long id);

	/**
	 * Updates the notes details for a specific entity.
	 *
	 * @param commonNotesDto CommonNotesDto object containing the details of the notes
	 *						 to be updated, including default, preferred, and supportive languages.
	 */
	void updateNotes(CommonNotesDto commonNotesDto);

	/**
	 * Updates the address details of the function by providing a FunctionAddressDto object.
	 *
	 * @param functionAddressDto The FunctionAddressDto object containing the updated address details,
	 *							 including default, preferred, and supportive languages, for the function.
	 */
	void updateFunctionAddress(FunctionAddressDto functionAddressDto);

	/**
	 * Updates the raw material timing details of the function by providing a orderFunctionRawMaterialTimeDto object.
	 *
	 * @param orderFunctionRawMaterialTimeDto The OrderFunctionRawMaterialTimeDto object containing the raw material time details, for the function.
	 */
	void updateRawMaterialTime(OrderFunctionRawMaterialTimeDto orderFunctionRawMaterialTimeDto);

}