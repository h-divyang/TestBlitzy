package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.tenant.request.OrderCrockeryDto;
import com.catering.model.tenant.OrderCrockeryModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing and performing operations related to crockery orders.
 *
 * Extends the GenericService interface, providing additional methods specific to
 * OrderCrockeryDto and OrderCrockeryModel.
 */
public interface OrderCrockeryService extends GenericService<OrderCrockeryDto, OrderCrockeryModel, Long> {

	/**
	 * Saves a list of crockery-related data.
	 *
	 * @param orderCrockeries A list of OrderCrockeryDto objects representing the crockery details to be saved.
	 */
	void saveCrockeryData(List<OrderCrockeryDto> orderCrockeries);

	/**
	 * Deletes crockery data associated with the provided list of order function IDs.
	 *
	 * @param orderFunctionId A list of unique IDs representing order functions for which the crockery data should be deleted.
	 */
	void deleteCrockeryData(List<Long> orderFunctionId);

	/**
	 * Checks if there exists a record associated with the specified Godown ID.
	 *
	 * @param id The unique identifier of the Godown to check for existence.
	 * @return A boolean value, true if a record associated with the provided Godown ID exists, false otherwise.
	 */
	boolean existsByGodownId(Long id);

}