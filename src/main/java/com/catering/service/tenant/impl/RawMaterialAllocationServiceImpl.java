package com.catering.service.tenant.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import com.catering.constant.MessagesConstant;
import com.catering.dao.order_reports.menu_allocation.MenuAllocationReportQueryDao;
import com.catering.dao.raw_material_allocation.RawMaterialAllocationNativeQueryService;
import com.catering.dto.tenant.request.AgencyAllocationDto;
import com.catering.dto.tenant.request.CommonRawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialAllocationFromRawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialAllocationRequestDto;
import com.catering.dto.tenant.request.RawMaterialDetailsDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.RawMaterialAllocationModel;
import com.catering.repository.tenant.BookOrderRepository;
import com.catering.repository.tenant.RawMaterialAllocationExtraRepository;
import com.catering.repository.tenant.RawMaterialAllocationRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.RawMaterialAllocationExtraService;
import com.catering.service.tenant.RawMaterialAllocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the RawMaterialAllocationService interface for managing raw material allocations.
 * This class provides concrete implementations for updating, reading, creating, deleting, and syncing
 * raw materials, as well as checking the existence of specific entities.
 * It utilizes repository, service, and utility components to handle data operations
 * and business logic related to raw material allocation.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialAllocationServiceImpl implements RawMaterialAllocationService {

	/**
	 * Repository for managing raw material allocations.
	 */
	RawMaterialAllocationRepository rawMaterialAllocationRepository;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for sending messages and notifications.
	 */
	MessageService messageService;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for raw material allocation native queries.
	 */
	RawMaterialAllocationNativeQueryService rawMaterialAllocationNativeQueryService;

	/**
	 * Service for handling extra raw material allocations.
	 */
	RawMaterialAllocationExtraService rawMaterialAllocationExtraService;

	/**
	 * Repository instance for handling database operations related to booking orders.
	 */
	BookOrderRepository bookOrderRepository;

	/**
	 * Repository instance for handling extra raw material allocations.
	 */
	RawMaterialAllocationExtraRepository rawMaterialAllocationExtraRepository;

	/**
	 * Data access object for querying the Menu Allocation Report data.
	 */
	MenuAllocationReportQueryDao menuAllocationReportQueryDao;

	/**
	 * {@inheritDoc}
	 * See {@link RawMaterialAllocationService#update(List, Long)} for details.
	 */
	@Override
	public void update(List<RawMaterialAllocationFromRawMaterialDto> orderRawMaterialDtos, Long orderId) {
		orderRawMaterialDtos.stream().filter(orderRawMaterial -> Boolean.FALSE.equals(orderRawMaterial.getIsExtra())).forEach(orderRawMaterial -> rawMaterialAllocationRepository.update(orderRawMaterial));
		orderRawMaterialDtos.stream().filter(orderRawMaterial -> Boolean.TRUE.equals(orderRawMaterial.getIsExtra())).forEach(orderRawMaterial -> rawMaterialAllocationExtraService.update(orderRawMaterial, orderId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<List<RawMaterialAllocationRequestDto>> read(Long menuPreparationMenuItemId) {
		List<RawMaterialAllocationModel> rawMaterialAllocationModels = rawMaterialAllocationRepository.findByMenuPreparationMenuItemId(menuPreparationMenuItemId);
		return Optional.of(modelMapperService.convertListEntityAndListDto(rawMaterialAllocationModels,RawMaterialAllocationRequestDto.class));
	}

	/**
	 * {@inheritDoc}
	 * See {@link RawMaterialAllocationService#createAndUpdate(List, Long)} for details.
	 */
	@Override
	public List<RawMaterialAllocationRequestDto> createAndUpdate(List<RawMaterialAllocationRequestDto> orderRawMaterialDtos, Long orderId) {
		List<RawMaterialAllocationModel> rawMaterialAllocationModels = new ArrayList<>();
		orderRawMaterialDtos.forEach(orderRawMaterial -> {
			RawMaterialAllocationModel rawMaterialAllocationModel = modelMapperService.convertEntityAndDto(orderRawMaterial, RawMaterialAllocationModel.class);
			rawMaterialAllocationModels.add(rawMaterialAllocationRepository.save(rawMaterialAllocationModel));
		});
		rawMaterialAllocationNativeQueryService.calculateExtraRawMaterialAllocation(orderId);
		return modelMapperService.convertListEntityAndListDto(rawMaterialAllocationModels, RawMaterialAllocationRequestDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id, Long orderId) throws RestException {
		if (!rawMaterialAllocationRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		rawMaterialAllocationRepository.deleteById(id);
		rawMaterialAllocationNativeQueryService.calculateExtraRawMaterialAllocation(orderId);
	}

	/**
	 * {@inheritDoc}
	 * See {@link RawMaterialAllocationService#agencyAllocation(List, Long)} for details.
	 */
	@Override
	public void agencyAllocation(List<AgencyAllocationDto> agencyAllocationDtoList, Long orderId) {
		new Thread(() -> {
			agencyAllocationDtoList.stream().forEach(agencyAllocationDto -> {
				if (Boolean.TRUE.equals(agencyAllocationDto.getIsExtra())) {
					rawMaterialAllocationExtraService.agencyAllocation(agencyAllocationDto, orderId);
				} else {
					rawMaterialAllocationRepository.agencyAllocation(agencyAllocationDto);
				}
			});
		}).start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RawMaterialAllocationModel> findByMenuPreparationMenuItemId(Long id) {
		return rawMaterialAllocationRepository.findByMenuPreparationMenuItemId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void syncRawMaterial(Long orderId, Long  menuPreparationMenuItemId, Long menuItemRawMaterialId, Double actualQty, Long actualMeasurementId, Long rawMaterialCategoryId, LocalDateTime orderTime, Long rawMaterialId) {
		rawMaterialAllocationRepository.syncRawMaterial(menuPreparationMenuItemId, menuItemRawMaterialId, actualQty, actualMeasurementId, orderTime, rawMaterialId, bookOrderRepository.getAdjustQuantityByOrderId(orderId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByGodownId(Long id) {
		return rawMaterialAllocationRepository.existsByGodown_Id(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double getSmallestMeasurementValue(Double quantity, Long measurementId) {
		return rawMaterialAllocationRepository.getSmallestMeasurementValue(quantity, measurementId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getSmallestMeasurementId(Long measurementId) {
		return rawMaterialAllocationRepository.getSmallestMeasurementId( measurementId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAdjustedAndExtraQuantity(Double quantity, Long measurementId, Boolean isAdjustQuantity, Boolean isSupplierRate) {
		return rawMaterialAllocationRepository.getAdjustedAndExtraQuantity(quantity, measurementId, isAdjustQuantity, isSupplierRate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateRawMaterialQuantity(List<CommonRawMaterialDto> commonRawMaterialDto) {
		// Iterate over each DTO in the list
		commonRawMaterialDto.forEach(dto -> {
			if (dto.getMenuItemAndRawMaterialId() != null) {
				// Retrieve the list of raw materials based on the provided IDs
				List<RawMaterialDetailsDto> rawMaterialDetailsDto = getRetrievedMaterials(dto);
				if (!rawMaterialDetailsDto.isEmpty()) {
					// Convert the final quantity to the smallest measurement unit
					double targetQuantity = rawMaterialAllocationRepository.getSmallestMeasurementValue(dto.getFinalQty(), dto.getFinalMeasurementId());
					// Calculate the total allocated quantity across all retrieved raw materials
					double totalAllocatedQuantity = rawMaterialDetailsDto.stream().mapToDouble(mat -> rawMaterialAllocationRepository.getSmallestMeasurementValue(mat.getFinalQty(), mat.getFinalMeasurementId())).sum();
					double quantityDifference = targetQuantity - totalAllocatedQuantity;
					if (quantityDifference >= 0) {
						// If additional quantity is needed, update the first raw material
						double updatedQty = rawMaterialAllocationRepository.getSmallestMeasurementValue(rawMaterialDetailsDto.get(0).getFinalQty(),rawMaterialDetailsDto.get(0).getFinalMeasurementId()) + quantityDifference;
						Long measurementId = rawMaterialAllocationRepository.getSmallestMeasurementId(rawMaterialDetailsDto.get(0).getFinalMeasurementId());
						// Adjust the quantity and unit based on the updated values
						rawMaterialDetailsDto.get(0).setFinalQty(rawMaterialAllocationRepository.adjustQuantity(updatedQty, measurementId, false));
						rawMaterialDetailsDto.get(0).setFinalMeasurementId(rawMaterialAllocationRepository.adjustQuantityUnit(updatedQty, measurementId, false));
						updateQuantityAndUnit(dto, rawMaterialDetailsDto.get(0));
					} else {
						// If excess quantity needs to be removed, distribute the reduction across rawmaterials
						double remainingDifference = Math.abs(quantityDifference);
						for (RawMaterialDetailsDto rawMaterial : rawMaterialDetailsDto) {
							double smallestValue = rawMaterialAllocationRepository.getSmallestMeasurementValue(rawMaterial.getFinalQty(), rawMaterial.getFinalMeasurementId());
							if (smallestValue <= remainingDifference) {
								// If the current raw material's quantity is within the difference, set it to zero
								remainingDifference -= smallestValue;
								rawMaterial.setFinalQty(0.0);
							} else {
								// Otherwise, reduce the current raw material's quantity accordingly
								double updatedQty = smallestValue - remainingDifference;
								Long measurementId = rawMaterialAllocationRepository.getSmallestMeasurementId(rawMaterial.getFinalMeasurementId());
								rawMaterial.setFinalQty(rawMaterialAllocationRepository.adjustQuantity(updatedQty, measurementId, false));
								rawMaterial.setFinalMeasurementId(rawMaterialAllocationRepository.adjustQuantityUnit(updatedQty, measurementId, false));
								remainingDifference = 0;
							}
							updateQuantityAndUnit(dto, rawMaterial);
							// If the remaining difference is fully adjusted, exit the loop
							if (remainingDifference == 0) {
								break;
							}
						}
					}
				}
			}
		});

		// If raw material allocation applies to specific functions only, recalculate extra allocation
		if (Boolean.FALSE.equals(commonRawMaterialDto.get(0).getIsAllFuntions())) {
			rawMaterialAllocationNativeQueryService.calculateExtraRawMaterialAllocation(commonRawMaterialDto.get(0).getOrderId());
		}
	}

	/**
	 * Retrieves a list of raw material details based on the menu item and raw material IDs.
	 * The IDs are stored as a comma-separated string in the DTO and are parsed to fetch the corresponding raw material details.
	 *
	 * @param commonRawMaterialDto The DTO containing menu item and raw material ID mappings.
	 * @return A list of RawMaterialDetailsDto objects containing raw material details.
	 */
	private List<RawMaterialDetailsDto> getRetrievedMaterials(CommonRawMaterialDto commonRawMaterialDto) {
		// Split the menu item and raw material ID string into pairs
		String[] rawMaterialPairs = commonRawMaterialDto.getMenuItemAndRawMaterialId().split(",\\s*");
		List<RawMaterialDetailsDto> rawMaterialDetailsList = new ArrayList<>();
		// Process each raw material pair (formatted as "isGeneral-menuItemId-rawMaterialId")
		Stream.of(rawMaterialPairs).map(pair -> pair.split("-"))
				.filter(parts -> parts.length == 3) // Ensure the split resulted in exactly 3 parts
				.forEach(parts -> {
					// Extract the values from the split string
					Long isGeneral = Long.parseLong(parts[0]);
					Long menuItemId = Long.parseLong(parts[1]);
					Long rawMaterialId = Long.parseLong(parts[2]);
					// Retrieve the raw material based on whether it's general or menu-item specific
					RawMaterialDetailsDto material = (isGeneral == 0)
							? menuAllocationReportQueryDao.findRawMaterialByMenuItemAndRawMaterialId(menuItemId,rawMaterialId)
							: menuAllocationReportQueryDao.findRawMaterialByFunctionAndRawMaterialId(menuItemId,rawMaterialId);
					// If a raw material is found, add it to the list
					if (material != null) {
						rawMaterialDetailsList.add(material);
					}
				});
		// Return the list of retrieved raw material details
		return rawMaterialDetailsList;
	}

	/**
	 * Updates the raw material quantity and unit based on whether it is a general fixed raw material or associated with a specific menu item.
	 * If the calculation applies to all functions, it deletes the extra allocation.
	 *
	 * @param commonRawMaterialDto  The DTO containing details about the raw material and its association.
	 * @param rawMaterialDetailsDto The DTO representing the raw material details that need to be updated.
	 */
	private void updateQuantityAndUnit(CommonRawMaterialDto commonRawMaterialDto, RawMaterialDetailsDto rawMaterialDetailsDto) {
		if (Boolean.TRUE.equals(rawMaterialDetailsDto.getIsGeneralFixRawMaterial())) {
			rawMaterialAllocationRepository.updateRawMaterialByFunctionAndRawMaterialId(rawMaterialDetailsDto);
		} else {
			rawMaterialAllocationRepository.updateRawMaterialByMenuItemAndRawMaterialId(rawMaterialDetailsDto);
		}
		// If the calculation applies to all functions, remove any extra allocated raw material for this order
		if (Boolean.TRUE.equals(commonRawMaterialDto.getIsAllFuntions())) {
			rawMaterialAllocationExtraRepository.deleteByRawMaterialIdAndOrderId(rawMaterialDetailsDto.getRawMaterialId(), commonRawMaterialDto.getOrderId());
		}
	}

}