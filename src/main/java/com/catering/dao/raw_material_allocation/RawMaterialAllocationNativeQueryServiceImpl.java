package com.catering.dao.raw_material_allocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.RawMaterialAllocationDto;
import com.catering.dto.tenant.request.RawMaterialAllocationMenuItemDto;
import com.catering.dto.tenant.request.RawMaterialAllocationRawMaterialDto;
import com.catering.repository.tenant.BookOrderRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of {@link RawMaterialAllocationNativeQueryService} that interacts with the underlying database
 * to perform raw material allocation queries and calculations. This class implements the business logic for
 * querying and calculating raw material allocations related to specific orders, raw material categories, and menu items.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialAllocationNativeQueryServiceImpl implements RawMaterialAllocationNativeQueryService {

	/**
	 * Data access object for interacting with the raw material allocation queries.
	 * This DAO handles native queries to fetch raw material allocation data.
	 */
	RawMaterialAllocationNativeQueryDao nativeQueryDao;

	/**
	 * Repository instance for handling database operations related to booking orders.
	 */
	BookOrderRepository bookOrderRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RawMaterialAllocationRawMaterialDto> findRawMaterialAllocationByOrderId(Long orderId, Long rawMaterialCategoryId) {
		List<RawMaterialAllocationRawMaterialDto> rawMaterials = nativeQueryDao.findRawMaterialByOrderIdAndItemCategoryIdResult(orderId, rawMaterialCategoryId, bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		List<RawMaterialAllocationMenuItemDto> menuItems = nativeQueryDao.findMenuItemByOrderIdAndRawMaterialId(orderId, rawMaterialCategoryId);
		// Group menu items by rawMaterialId
		Map<Long, List<RawMaterialAllocationMenuItemDto>> menuItemsGroupedByRawMaterialId = menuItems.stream().collect(Collectors.groupingBy(RawMaterialAllocationMenuItemDto::getRawMaterialId));
		// Map menu items to corresponding raw materials
		rawMaterials.forEach(rawMaterial -> rawMaterial.setMenuItems(menuItemsGroupedByRawMaterialId.getOrDefault(rawMaterial.getRawMaterialId(), Collections.emptyList())));
		return rawMaterials;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RawMaterialAllocationDto> finRawMaterialCategoryByOrderId(Long orderId) {
		return nativeQueryDao.findItemCategoryByOrderId(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void calculateExtraRawMaterialAllocation(Long orderId) {
		nativeQueryDao.calculateExtraEventRawMaterial(orderId, bookOrderRepository.getAdjustQuantityByOrderId(orderId));
	}

}