package com.catering.service.tenant.impl;

import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.AgencyAllocationDto;
import com.catering.dto.tenant.request.RawMaterialAllocationFromRawMaterialDto;
import com.catering.model.tenant.RawMaterialAllocationExtraModel;
import com.catering.repository.tenant.RawMaterialAllocationExtraRepository;
import com.catering.service.tenant.RawMaterialAllocationExtraService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing operations related to raw material allocation with additional services.
 * This class communicates with the RawMaterialAllocationExtraRepository to handle database-level operations
 * for updating raw material allocation details and allocating raw materials to an agency.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialAllocationExtraServiceImpl implements RawMaterialAllocationExtraService {

	/**
	 * Repository for managing extra raw material allocations.
	 */
	RawMaterialAllocationExtraRepository rawMaterialAllocationExtraRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(RawMaterialAllocationFromRawMaterialDto orderRawMaterial, Long orderId) {
		RawMaterialAllocationExtraModel allocationExtraModel = rawMaterialAllocationExtraRepository.findByRawMaterialIdAndOrderId(orderRawMaterial.getRawMaterialId(), orderId);
		if(allocationExtraModel != null && orderRawMaterial.getFinalQty() == 0) {
			rawMaterialAllocationExtraRepository.deleteByRawMaterialIdAndOrderId(orderRawMaterial.getRawMaterialId(), orderId);
		} else if(allocationExtraModel != null) {
			rawMaterialAllocationExtraRepository.update(orderRawMaterial, orderId);
		} else if (orderRawMaterial.getFinalQty() != 0){
			rawMaterialAllocationExtraRepository.save(orderRawMaterial, orderId);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void agencyAllocation(AgencyAllocationDto agencyAllocationDto, Long orderId) {
		rawMaterialAllocationExtraRepository.agencyAllocation(agencyAllocationDto, orderId);
	}

}