package com.catering.service.tenant;

import com.catering.dto.tenant.request.AgencyAllocationDto;
import com.catering.dto.tenant.request.RawMaterialAllocationFromRawMaterialDto;

/**
 * Interface for managing operations related to raw material allocation with additional services.
 * This service provides methods to update raw material allocation details and allocate raw materials to an agency.
 *
 * Implementations of this interface are expected to handle the business logic associated with the allocation
 * of raw materials for orders and agencies.
 */
public interface RawMaterialAllocationExtraService {

	/**
	 * Updates the details of raw material allocation based on the provided data.
	 *
	 * @param orderRawMaterial The object containing the updated raw material allocation details.
	 */
	void update(RawMaterialAllocationFromRawMaterialDto orderRawMaterial, Long orderId);

	/**
	 * Allocates raw materials to an agency based on the provided allocation details and order ID.
	 *
	 * @param agencyAllocationDto The object containing the agency allocation details.
	 * @param orderId The unique identifier of the order for which the allocation is to be made.
	 */
	void agencyAllocation(AgencyAllocationDto agencyAllocationDto, Long orderId);

}