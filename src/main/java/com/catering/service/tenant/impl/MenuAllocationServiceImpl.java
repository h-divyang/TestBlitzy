package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.catering.constant.MessagesConstant;
import com.catering.dao.labour_and_other_management.LabourAndOtherManagementNativeQueryService;
import com.catering.dao.menu_allocation_type.MenuAllocationNativeQueryDao;
import com.catering.dao.raw_material_allocation.RawMaterialAllocationNativeQueryService;
import com.catering.dto.tenant.request.CommonNotesDto;
import com.catering.dto.tenant.request.CompanyPreferencesDto;
import com.catering.dto.tenant.request.MenuAllocationDTO;
import com.catering.dto.tenant.request.MenuAllocationDtoForNativeQuery;
import com.catering.dto.tenant.request.MenuAllocationTypeDto;
import com.catering.dto.tenant.request.MenuAllocationTypeForNativeQuery;
import com.catering.dto.tenant.request.RawMaterialCalculationDto;
import com.catering.dto.tenant.request.RawMaterialCalculationResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.GetMenuAllocationOrderMenuPreparationModel;
import com.catering.model.tenant.MenuItemRawMaterialModel;
import com.catering.model.tenant.OrderMenuPreparationMenuItemModel;
import com.catering.model.tenant.RawMaterialAllocationModel;
import com.catering.model.tenant.SaveCustomMenuAllocationTypeModel;
import com.catering.model.tenant.SaveMenuAllocationOrderMenuPreparationMenuItemModel;
import com.catering.repository.tenant.GetAllocationOrderMenuPreparationRepository;
import com.catering.repository.tenant.OrderMenuAllocationTypeRepository;
import com.catering.repository.tenant.OrderMenuPreparationMenuItemRepository;
import com.catering.repository.tenant.SaveAllocationOrderMenuPreparationMenuItemRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.CompanyPreferencesService;
import com.catering.service.tenant.MenuAllocationService;
import com.catering.service.tenant.MenuItemRawMaterialService;
import com.catering.service.tenant.RawMaterialAllocationService;
import com.catering.util.DataUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the MenuAllocationService interface responsible for the creation, update, and management
 * of menu allocation related entities. This class contains the business logic to handle and process
 * menu allocation details and updates while interacting with the respective repositories and services.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuAllocationServiceImpl implements MenuAllocationService {

	/**
	 * Service for managing company preferences and settings.
	 */
	CompanyPreferencesService companyPreferencesService;

	/**
	 * Repository for managing Order Menu Preparation Menu Items.
	 */
	OrderMenuPreparationMenuItemRepository orderMenuPreparationMenuItemRepository;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * DAO for executing native queries related to menu allocations.
	 */
	MenuAllocationNativeQueryDao menuAllocationNativeQueryDao;

	/**
	 * Service for executing native queries concerning labour and other management information.
	 */
	LabourAndOtherManagementNativeQueryService labourAndOtherManagementNativeQueryService;

	/**
	 * Service for executing native queries related to raw material allocations.
	 */
	RawMaterialAllocationNativeQueryService eventRawMaterialNativeQueryService;

	/**
	 * Repository for fetching Order Menu Preparation data related to allocation.
	 */
	GetAllocationOrderMenuPreparationRepository getOrderMenuPreparationRepository;

	/**
	 * Repository for saving Order Menu Preparation Menu Item allocations.
	 */
	SaveAllocationOrderMenuPreparationMenuItemRepository saveOrderMenuPreparationMenuItemRepository;

	/**
	 * Repository for managing Order Menu Allocation Types.
	 */
	OrderMenuAllocationTypeRepository orderMenuAllocationTypeRepository;

	/**
	 * Service for managing the allocation of raw materials.
	 */
	RawMaterialAllocationService rawMaterialAllocationService;

	/**
	 * Service for managing the association between menu items and raw materials.
	 */
	MenuItemRawMaterialService menuItemRawMaterialService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createAndUpdate(List<MenuAllocationDTO> menuAllocationDTOs, Long orderId) {
		Optional<CompanyPreferencesDto> companySettingDto = companyPreferencesService.find();
		boolean isModuleTwoActive = companySettingDto.isPresent() && companySettingDto.get().getSubscriptionId() == 2;
		List<MenuAllocationDTO> menuAllocationDTOList = new ArrayList<>();
		if (isModuleTwoActive) {
			List<MenuAllocationDtoForNativeQuery> menuAllocationResults = menuAllocationNativeQueryDao.findOrderMenuPreparationMenuItemByOrderId(orderId);
			List<MenuAllocationTypeForNativeQuery> allocationTypeResults = menuAllocationNativeQueryDao.findMenuAllocationTypeByOrderId(orderId);
			menuAllocationResults.forEach(result -> {
				MenuAllocationDTO dto = new MenuAllocationDTO();
				dto.setId(result.getId());
				dto.setOrderType(result.getOrderType());
				dto.setOrderDate(result.getOrderDate());
				dto.setAllocationType(new ArrayList<>());
				menuAllocationDTOList.add(dto);
			});
			// Match and add allocation types to corresponding MenuAllocationDTO
			allocationTypeResults.forEach(typeResult -> {
				menuAllocationDTOList.stream()
				.filter(dto -> dto.getId().equals(typeResult.getFkOrderMenuPreparationMenuItemId())).findFirst().ifPresent(matchingDto -> {
					MenuAllocationTypeDto typeDto = new MenuAllocationTypeDto();
					typeDto.setId(typeResult.getId());
					typeDto.setFkContactId(typeResult.getFkContactId());
					typeDto.setCounterNo(typeResult.getCounterNo());
					typeDto.setCounterPrice(typeResult.getCounterPrice());
					typeDto.setHelperNo(typeResult.getHelperNo());
					typeDto.setHelperPrice(typeResult.getHelperPrice());
					typeDto.setQuantity(typeResult.getQuantity());
					typeDto.setPrice(typeResult.getPrice());
					matchingDto.getAllocationType().add(typeDto);
				});
			});	
		}
		menuAllocationDTOs.forEach(menuAllocationDTO -> {
			SaveMenuAllocationOrderMenuPreparationMenuItemModel orderMenuPreparationMenuItemModel = modelMapperService.convertEntityAndDto(menuAllocationDTO, SaveMenuAllocationOrderMenuPreparationMenuItemModel.class);
			applySwitchCaseUpdates(menuAllocationDTO.getOrderType(), orderMenuPreparationMenuItemModel);
			DataUtils.setAuditFields(saveOrderMenuPreparationMenuItemRepository, menuAllocationDTO.getId(), orderMenuPreparationMenuItemModel);
			orderMenuPreparationMenuItemModel.setIsActive(true);
			// Handle allocation types if present
			if (menuAllocationDTO.getAllocationType() != null) {
				orderMenuPreparationMenuItemModel.getAllocationType().forEach(type -> type.setMenuPreparationMenuItem(orderMenuPreparationMenuItemModel));
			}
			SaveMenuAllocationOrderMenuPreparationMenuItemModel savedResponse = saveOrderMenuPreparationMenuItemRepository.save(orderMenuPreparationMenuItemModel);
			if (isModuleTwoActive) {
				updateMenuAllocationProcedure(orderMenuPreparationMenuItemModel, savedResponse, menuAllocationDTOList);
			}
		});
		eventRawMaterialNativeQueryService.calculateExtraRawMaterialAllocation(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RawMaterialCalculationResponseDto calculateRawMaterialCost(RawMaterialCalculationDto rawMaterialCalculationDto) {
		RawMaterialCalculationResponseDto rawMaterialCalculationResponseDto = new RawMaterialCalculationResponseDto();
		rawMaterialCalculationResponseDto.setTotal(labourAndOtherManagementNativeQueryService.calculateItemCost(rawMaterialCalculationDto.getOrderFunctionId(), rawMaterialCalculationDto.getMenuItemId()));
		return rawMaterialCalculationResponseDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetMenuAllocationOrderMenuPreparationModel> findByOrderId(Long orderId) {
		List<GetMenuAllocationOrderMenuPreparationModel> orderMenuPreparationModels = getOrderMenuPreparationRepository.findByOrderFunctionBookOrder(orderId);
		orderMenuPreparationModels.forEach(orderMenuPreparationModel -> {
			orderMenuPreparationModel.getMenuPreparationMenuItem().stream().filter(Objects::nonNull).forEach(menuItem -> {
				Double total = labourAndOtherManagementNativeQueryService.calculateItemCost(orderMenuPreparationModel.getOrderFunction().getId(), menuItem.getMenuItem().getId());
				menuItem.setCalculatedMenuItemAndRawMaterial(total);
			});
		});
		return orderMenuPreparationModels;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateNotes(CommonNotesDto menuAllocationNotes) throws RestException {
		if (!orderMenuPreparationMenuItemRepository.existsById(menuAllocationNotes.getId())) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		Optional<OrderMenuPreparationMenuItemModel> menuPreparationMenuItem = orderMenuPreparationMenuItemRepository.findById(menuAllocationNotes.getId());
		if (menuPreparationMenuItem.isPresent()) {
			OrderMenuPreparationMenuItemModel orderMenuPreparationMenuItemModel = menuPreparationMenuItem.get();
			orderMenuPreparationMenuItemModel.setNoteDefaultLang(menuAllocationNotes.getNoteDefaultLang());
			orderMenuPreparationMenuItemModel.setNotePreferLang(menuAllocationNotes.getNotePreferLang());
			orderMenuPreparationMenuItemModel.setNoteSupportiveLang(menuAllocationNotes.getNoteSupportiveLang());
			orderMenuPreparationMenuItemRepository.save(orderMenuPreparationMenuItemModel);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void syncRawMaterial(Long orderId) {
		List<OrderMenuPreparationMenuItemModel> orderMenuPreparationMenuItemList = orderMenuPreparationMenuItemRepository.findByMenuPreparationOrderFunctionBookOrderId(orderId);
		orderMenuPreparationMenuItemList.forEach(orderMenuPreparationMenuItem -> {
			List<MenuItemRawMaterialModel> menuItemRawMaterialList = menuItemRawMaterialService.findByMenuItemId(orderMenuPreparationMenuItem.getMenuItem().getId());
			List<RawMaterialAllocationModel> rawMaterialAllocationList = rawMaterialAllocationService.findByMenuPreparationMenuItemId(orderMenuPreparationMenuItem.getId());
			menuItemRawMaterialList.forEach(menuItemRawMaterial -> {
				if (rawMaterialAllocationList.stream().noneMatch(rawMaterialAllocation -> (Objects.nonNull(rawMaterialAllocation) && Objects.nonNull(rawMaterialAllocation.getMenuItemRawMaterialId()) && menuItemRawMaterial.getId().equals(rawMaterialAllocation.getMenuItemRawMaterialId().getId())) || (Objects.nonNull(rawMaterialAllocation) && Objects.nonNull(rawMaterialAllocation.getRawMaterialId()) && Objects.nonNull(menuItemRawMaterial.getRawMaterial()) && Objects.nonNull(rawMaterialAllocation.getMenuItemRawMaterialId()) && menuItemRawMaterial.getRawMaterial().getId().equals(rawMaterialAllocation.getMenuItemRawMaterialId().getRawMaterial().getId())))) {
					rawMaterialAllocationService.syncRawMaterial(orderId, orderMenuPreparationMenuItem.getId(), menuItemRawMaterial.getId(), menuItemRawMaterial.getWeight() * orderMenuPreparationMenuItem.getMenuPreparation().getOrderFunction().getPerson() / 100, menuItemRawMaterial.getMeasurement().getId(), menuItemRawMaterial.getRawMaterial().getRawMaterialCategory().getId(), orderMenuPreparationMenuItem.getMenuPreparation().getOrderFunction().getDate(), menuItemRawMaterial.getRawMaterial().getId());
				}
			});
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) throws RestException {
		if (!orderMenuAllocationTypeRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		orderMenuAllocationTypeRepository.deleteById(id);
	}

	/**
	 * Updates the menu allocation procedure by handling both new and existing allocation records.
	 * It performs the following operations:
	 * - Filters and inserts new allocation types.
	 * - Updates existing allocation records with changes in details.
	 * - Handles different cases of order type changes.
	 *
	 * @param payLoadRecord Object containing the payload record data for menu allocation.
	 *						This record includes order type, order date, and allocation type details.
	 * @param newMenuPreprationMenuItemMode Object containing the updated menu preparation item model data with new allocation types.
	 * @param oldListOfMenuAllocationRecords List of existing menu allocation records used to retrieve and update old allocation details.
	 */
	private void updateMenuAllocationProcedure(SaveMenuAllocationOrderMenuPreparationMenuItemModel payLoadRecord, SaveMenuAllocationOrderMenuPreparationMenuItemModel newMenuPreprationMenuItemMode, List<MenuAllocationDTO> oldListOfMenuAllocationRecords) {
		// Filter payload response get new data of allocationType
		List<SaveCustomMenuAllocationTypeModel> listOfNewAllocationTypes = payLoadRecord.getAllocationType().stream().filter(item -> item.getId() == null).toList();
		// Filter payload records which have id means those are the existed records
		List<Long> existingIds = payLoadRecord.getAllocationType().stream().filter(item -> item.getId() != null).map(item -> item.getId()).toList();
		// Filter from the save response which contain only those records which are newly added
		List<SaveCustomMenuAllocationTypeModel> newlyAddedAllocations = newMenuPreprationMenuItemMode.getAllocationType().stream().filter(item -> !existingIds.contains(item.getId())).toList();
		if (!listOfNewAllocationTypes.isEmpty()) { // check if new records are exists
			newlyAddedAllocations.forEach(dto -> {
				if (dto.getFkContactId() != null) { // only new records with agency records will be insert.
					menuAllocationNativeQueryDao.manageAccountBalanceAndHistory(
					payLoadRecord.getOrderType(), dto.getFkContactId(), dto.getCounterNo(), dto.getCounterPrice(), dto.getHelperNo(), dto.getHelperPrice(), dto.getQuantity(), dto.getPrice(), payLoadRecord.getOrderDate(), dto.getId());
				}
			});
		}

		MenuAllocationDTO menuAllocationDTO = oldListOfMenuAllocationRecords.stream().filter(response -> response.getId().equals(newMenuPreprationMenuItemMode.getId())).findFirst().orElse(null);
		if (menuAllocationDTO != null) {
			List<MenuAllocationTypeDto> oldAllocations = menuAllocationDTO.getAllocationType();
			List<SaveCustomMenuAllocationTypeModel> newAllocations = newMenuPreprationMenuItemMode.getAllocationType();
			if (!newAllocations.isEmpty()) {
				// Create a map of old allocations for easy lookup
				Map<Long, MenuAllocationTypeDto> oldAllocationMap = oldAllocations.stream().collect(Collectors.toMap(MenuAllocationTypeDto::getId, allocation -> allocation, (existing, replacement) -> existing));
				// Process each new allocation
				for (SaveCustomMenuAllocationTypeModel newAllocation : newAllocations) {
					// Find matching old allocation by ID
					MenuAllocationTypeDto oldAllocation = oldAllocationMap.get(newAllocation.getId());
					if (oldAllocation != null) {
						// Update with both old and new values
						menuAllocationNativeQueryDao.updateMenuAllocation(
							newAllocation.getId(), newMenuPreprationMenuItemMode.getOrderDate(), newMenuPreprationMenuItemMode.getOrderType(), newAllocation.getFkContactId(), newAllocation.getCounterNo(), newAllocation.getCounterPrice(), newAllocation.getHelperNo(), newAllocation.getHelperPrice(), newAllocation.getQuantity(), newAllocation.getPrice(),
							oldAllocation.getId(), menuAllocationDTO.getOrderDate(), menuAllocationDTO.getOrderType(), oldAllocation.getFkContactId(), oldAllocation.getCounterNo(), oldAllocation.getCounterPrice(), oldAllocation.getHelperNo(), oldAllocation.getHelperPrice(), oldAllocation.getQuantity(), oldAllocation.getPrice());
					}
				}
			} else { // Case: 2 It will call when user go from orderType(1 or 2) to 0.
				for (MenuAllocationTypeDto oldAllocation : oldAllocations) {
					menuAllocationNativeQueryDao.updateMenuAllocation(null, newMenuPreprationMenuItemMode.getOrderDate(), newMenuPreprationMenuItemMode.getOrderType(), null, 0, 0.0, 0, 0.0, 0.0, 0.0,
							oldAllocation.getId(), menuAllocationDTO.getOrderDate(), menuAllocationDTO.getOrderType(), oldAllocation.getFkContactId(), oldAllocation.getCounterNo(), oldAllocation.getCounterPrice(),
							oldAllocation.getHelperNo(), oldAllocation.getHelperPrice(), oldAllocation.getQuantity(), oldAllocation.getPrice());
				}
			}
		}
	}

	/**
	 * Applies specific updates to the allocation types based on the order type.
	 * This method processes a list of {@link SaveCustomMenuAllocationTypeModel} objects
	 * and modifies their fields depending on the provided order type.
	 *
	 * @param orderType The type of the order, determining which fields of the allocation types are to be updated.
	 *					Valid values are:
	 *					- 0: Clears all relevant fields for default allocation.
	 *					- 1: Clears fields related to price and quantity for chef labour allocation.
	 *					- 2: Clears fields associated with counter and helper details for outside allocation.
	 * @param model The menu preparation item model containing the allocation types to be updated.
	 */
	private void applySwitchCaseUpdates(int orderType, SaveMenuAllocationOrderMenuPreparationMenuItemModel model) {
		List<SaveCustomMenuAllocationTypeModel> allocationTypes = model.getAllocationType();
		switch (orderType) {
			case 1:
				allocationTypes.forEach(allocationType -> {
					allocationType.setPrice(null);
					allocationType.setQuantity(null);
				});
				break;
			case 2:
				allocationTypes.forEach(allocationType -> {
					allocationType.setCounterNo(null);
					allocationType.setCounterPrice(null);
					allocationType.setHelperNo(null);
					allocationType.setHelperPrice(null);
				});
				break;
			case 0:
				allocationTypes.forEach(allocationType -> {
					allocationType.setCounterNo(null);
					allocationType.setCounterPrice(null);
					allocationType.setHelperNo(null);
					allocationType.setHelperPrice(null);
					allocationType.setPrice(null);
					allocationType.setTotal(null);
					allocationType.setFkContactId(null);
					allocationType.setQuantity(null);
					allocationType.setUnit(null);
				});
				break;
		}
	}

}