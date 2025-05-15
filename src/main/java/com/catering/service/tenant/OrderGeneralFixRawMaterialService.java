package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.tenant.request.OrderGeneralFixRawMaterialDto;
import com.catering.model.tenant.OrderGeneralFixRawMaterialModel;
import com.catering.service.common.GenericService;

/**
 * Defines business logic for managing general fixed raw material orders, including operations for saving,
 * deleting, and checking entities related to raw materials.
 */
public interface OrderGeneralFixRawMaterialService extends GenericService<OrderGeneralFixRawMaterialDto, OrderGeneralFixRawMaterialModel, Long> {

	/**
	 * Persists a list of general fixed raw material orders.
	 *
	 * @param orderGeneralFixRawMaterialDtos A list of {@code OrderGeneralFixRawMaterialDto} objects representing the details
	 *										 of raw material orders to be saved.
	 */
	void saveOrderGeneralFixRawMaterial(List<OrderGeneralFixRawMaterialDto> orderGeneralFixRawMaterialDtos);

	/**
	 * Deletes unused general fixed raw material entries based on the provided list of order function IDs.
	 *
	 * @param orderFunctionId A list of unique identifiers representing order functions for which
	 *						  unused general fixed raw material entries need to be deleted.
	 */
	void deleteUnusedGeneralFixRawMaterial(List<Long> orderFunctionId);

	/**
	 * Checks the existence of an entity based on the provided Godown ID.
	 *
	 * @param id The unique identifier of the Godown to be checked for existence.
	 * @return A boolean value indicating whether an entity with the given Godown ID exists (true if exists, false otherwise).
	 */
	boolean existsByGodownId(Long id);

}