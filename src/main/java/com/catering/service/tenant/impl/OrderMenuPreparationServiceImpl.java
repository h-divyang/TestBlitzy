package com.catering.service.tenant.impl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.catering.constant.FileConstants;
import com.catering.dao.labour_and_other_management.LabourAndOtherManagementNativeQueryDao;
import com.catering.dao.labour_and_other_management.LabourAndOtherManagementNativeQueryService;
import com.catering.dao.menu_allocation_type.MenuAllocationNativeQueryDao;
import com.catering.dao.raw_material_allocation.RawMaterialAllocationNativeQueryService;
import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.CompanyPreferencesDto;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.MenuItemCategoryInfoDto;
import com.catering.dto.tenant.request.MenuItemCategoryRupeesDto;
import com.catering.dto.tenant.request.MenuItemInfoDto;
import com.catering.dto.tenant.request.MenuItemRupeesDto;
import com.catering.dto.tenant.request.OrderCrockeryDto;
import com.catering.dto.tenant.request.OrderCrockeryItemCategoryDto;
import com.catering.dto.tenant.request.OrderCrockeryParameterDto;
import com.catering.dto.tenant.request.OrderGeneralFixRawMaterialDto;
import com.catering.dto.tenant.request.OrderGeneralFixRawMaterialResponseContainerDto;
import com.catering.dto.tenant.request.OrderMenuPreparationDto;
import com.catering.enums.MenuTypeEnum;
import com.catering.enums.OrderTypeEnum;
import com.catering.model.tenant.GetMenuPreparationForMenuItemCategoryModel;
import com.catering.model.tenant.GetMenuPreparationForMenuItemModel;
import com.catering.model.tenant.GetMenuPreparationMenuItemModel;
import com.catering.model.tenant.GetMenuPreparationModel;
import com.catering.model.tenant.GetMenuPreparationRawMaterialModel;
import com.catering.model.tenant.MenuItemCategoryInfoModel;
import com.catering.model.tenant.MenuItemCategoryRupeesModel;
import com.catering.model.tenant.MenuItemInfoModel;
import com.catering.model.tenant.MenuItemRupeesModel;
import com.catering.model.tenant.OrderMenuPreparationModel;
import com.catering.model.tenant.SaveMenuAllocationTypeModel;
import com.catering.model.tenant.SaveMenuPreparationMenuItemModel;
import com.catering.model.tenant.SaveMenuPreparationModel;
import com.catering.model.tenant.SaveMenuPreparationOrderFunctionModel;
import com.catering.repository.tenant.BookOrderRepository;
import com.catering.repository.tenant.GetMenuPreparationForMenuItemCategoryRepository;
import com.catering.repository.tenant.GetMenuPreparationRawMaterialRepository;
import com.catering.repository.tenant.GetOrderMenuPreparationRepository;
import com.catering.repository.tenant.MenuItemCategoryInfoRepository;
import com.catering.repository.tenant.MenuItemCategoryRupeesRepository;
import com.catering.repository.tenant.MenuItemInfoRepository;
import com.catering.repository.tenant.MenuItemRupeesRepository;
import com.catering.repository.tenant.OrderMenuPreparationRepository;
import com.catering.repository.tenant.SaveMenuPreparationMenuItemRepository;
import com.catering.repository.tenant.SaveMenuPreparationRepository;
import com.catering.service.common.FileService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CompanyPreferencesService;
import com.catering.service.tenant.CompanySettingService;
import com.catering.service.tenant.OrderCrockeryService;
import com.catering.service.tenant.OrderFunctionService;
import com.catering.service.tenant.OrderGeneralFixRawMaterialService;
import com.catering.service.tenant.OrderMenuPreparationService;
import com.catering.service.tenant.RawMaterialService;
import com.catering.util.DataUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation class for handling operations related to order menu preparation.
 *
 * This class extends the generic service implementation and provides functionality for
 * creating, updating, and managing menu preparation records, as well as associated entities
 * like menu items, raw materials, and menu categories. The service handles the business logic
 * and interacts with the underlying repositories and dependent services to achieve its objectives.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderMenuPreparationServiceImpl extends GenericServiceImpl<OrderMenuPreparationDto, OrderMenuPreparationModel, Long> implements OrderMenuPreparationService {

	/**
	 * Service for managing company preferences.
	 */
	CompanyPreferencesService companyPreferencesService;

	/**
	 * Service for model mapping between DTOs and entities.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for order menu preparation data.
	 */
	OrderMenuPreparationRepository orderMenuPreparationRepository;

	/**
	 * Repository for fetching order menu preparation details.
	 */
	GetOrderMenuPreparationRepository getOrderMenuPreparationRepository;

	/**
	 * Repository for menu item category information.
	 */
	MenuItemCategoryInfoRepository menuItemCategoryInfoRepository;

	/**
	 * Repository for menu item category rupees management.
	 */
	MenuItemCategoryRupeesRepository menuItemCategoryRupeesRepository;

	/**
	 * Repository for menu item information.
	 */
	MenuItemInfoRepository menuItemInfoRepository;

	/**
	 * Repository for menu item rupees data.
	 */
	MenuItemRupeesRepository menuItemRupeesRepository;

	/**
	 * Service for raw material handling.
	 */
	RawMaterialService rawMaterialService;

	/**
	 * Service for labour and other management operations.
	 */
	LabourAndOtherManagementNativeQueryService labourAndOtherManagementNativeQueryService;

	/**
	 * Service for handling fixed raw material for orders.
	 */
	OrderGeneralFixRawMaterialService orderGeneralFixRawMaterialService;

	/**
	 * DAO for labour and other management native queries.
	 */
	LabourAndOtherManagementNativeQueryDao labourAndOtherManagementNativeQueryDao;

	/**
	 * Service for managing order crockery data.
	 */
	OrderCrockeryService orderCrockeryService;

	/**
	 * Service for order function operations.
	 */
	OrderFunctionService orderFunctionService;

	/**
	 * Service for raw material allocation.
	 */
	RawMaterialAllocationNativeQueryService eventRawMaterialNativeQueryService;

	/**
	 * Repository for saving menu preparation menu items.
	 */
	SaveMenuPreparationMenuItemRepository saveMenuPreparationMenuItemRepository;

	/**
	 * Repository for saving menu preparation data.
	 */
	SaveMenuPreparationRepository saveMenuPreparationRepository;

	/**
	 * Repository for fetching menu preparation by menu item category.
	 */
	GetMenuPreparationForMenuItemCategoryRepository getMenuPreparationForMenuItemCategoryRepository;

	/**
	 * Repository for fetching menu preparation raw materials.
	 */
	GetMenuPreparationRawMaterialRepository getMenuPreparationRawMaterialRepository;

	/**
	 * Service for managing company settings.
	 */
	CompanySettingService companySettingService;

	/**
	 * DAO for menu allocation native queries.
	 */
	MenuAllocationNativeQueryDao menuAllocationNativeQueryDao;

	/**
	 * Repository instance for handling database operations related to booking orders.
	 */
	BookOrderRepository bookOrderRepository;

	/**
	 * Service for handling file-related operations such as upload, download, and management.
	 */
	FileService fileService;

	/**
	 * {@inheritDoc}
	 * See {@link OrderMenuPreparationService#createAndUpdate(List, Long)} for details.
	 */
	@Override
	public void createAndUpdate(List<SaveMenuPreparationModel> saveMenuPreparationModels, Long orderId) {
		Optional<CompanyPreferencesDto> companySettingDto = companyPreferencesService.find();
		boolean isModuleTwoActive = companySettingDto.isPresent() && companySettingDto.get().getSubscriptionId() == 2;
		List<Long> functionIds = saveMenuPreparationModels.stream().filter(saveMenuPreparationModel -> Objects.isNull(saveMenuPreparationModel.getId())).map(saveMenuPreparationModel -> saveMenuPreparationModel.getOrderFunction().getId()).collect(Collectors.toList());
		saveMenuPreparationModels.forEach(saveMenuPreparationModel -> {
			createOrderMenuPreparationModel(saveMenuPreparationModel);
			SaveMenuPreparationOrderFunctionModel orderFunctionModel = saveMenuPreparationModel.getOrderFunction();
			orderFunctionService.updateRate(orderFunctionModel.getRate(), orderFunctionModel.getId());
			SaveMenuPreparationModel savedData = saveMenuPreparationRepository.save(saveMenuPreparationModel);
			if (isModuleTwoActive) {
				manageAccountBalanceAndHistory(savedData);
			}
		});

		// Set the general fix raw material and crokery data when menu preparation save first time.
		new Thread(() -> {
			setOrderGeneralFixRawMaterialAndCrockeryData(saveMenuPreparationModels, orderId, functionIds);
			eventRawMaterialNativeQueryService.calculateExtraRawMaterialAllocation(orderId);
		}).start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetMenuPreparationModel> getMenuPreparationByOrderId(Long orderId) {
		List<GetMenuPreparationModel> orderMenuPreparationModels = getOrderMenuPreparationRepository.findByOrderFunctionBookOrderId(orderId);
		orderMenuPreparationModels.stream().map(GetMenuPreparationModel::getMenuPreparationMenuItem)
			.flatMap(List::stream)
			.map(GetMenuPreparationMenuItemModel::getMenuItem)
			.forEach(menuItem -> menuItem.setRawMaterials(modelMapperService.convertListEntityAndListDto(rawMaterialService.findByMenuItemId(menuItem.getId()), CommonDataForDropDownDto.class)));
		return orderMenuPreparationModels;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByCustomPackageId(Long id) {
		return orderMenuPreparationRepository.existsByCustomPackageId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MenuItemCategoryInfoModel updateMenuItemCategoryInfo(MenuItemCategoryInfoDto menuItemCategoryInfo) {
		return  menuItemCategoryInfoRepository.save(modelMapperService.convertEntityAndDto(menuItemCategoryInfo, MenuItemCategoryInfoModel.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MenuItemInfoModel updateMenuItemInfo(MenuItemInfoDto menuItemInfo) {
		return menuItemInfoRepository.save(modelMapperService.convertEntityAndDto(menuItemInfo, MenuItemInfoModel.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MenuItemRupeesModel updateMenuItemRupees(MenuItemRupeesDto menuItemRupees) {
		return menuItemRupeesRepository.save(modelMapperService.convertEntityAndDto(menuItemRupees, MenuItemRupeesModel.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MenuItemCategoryRupeesModel updateMenuItemCategoryRupees(MenuItemCategoryRupeesDto categoryRupees) {
		return menuItemCategoryRupeesRepository.save(modelMapperService.convertEntityAndDto(categoryRupees, MenuItemCategoryRupeesModel.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetMenuPreparationRawMaterialModel> getAllRawMaterial() {
		return getMenuPreparationRawMaterialRepository.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CommonDataForDropDownDto> getRawMaterialByMenuItemId(Long menuItemId) {
		return getMenuPreparationRawMaterialRepository.findByMenuItemId(menuItemId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetMenuPreparationForMenuItemCategoryModel> getMenuItemCategoryByMenuItemExist(Long orderId, String tenant) {
		List<GetMenuPreparationForMenuItemCategoryModel> getMenuPreparationForMenuItemCategoryModel = getMenuPreparationForMenuItemCategoryRepository.findDistinctByMenuItemCategoriesIsNotNullAndIsActiveTrueOrderByPriority(orderId);
		List<GetMenuPreparationForMenuItemModel> getMenuPreparationForMenuItemModel = getMenuPreparationForMenuItemCategoryRepository.findDistinctByMenuItemsIsNotNullAndIsActiveTrueOrderByPriority(orderId);
		getMenuPreparationForMenuItemCategoryModel.forEach(menuItemCategory -> {
			List<GetMenuPreparationForMenuItemModel> menuItems = getMenuPreparationForMenuItemModel.stream().filter(menuItem -> menuItem.getMenuItemCategory().equals(menuItemCategory.getId())).toList();
			menuItemCategory.setMenuItems(menuItems);
		});
		getMenuPreparationForMenuItemModel.forEach(menuItem -> menuItem.setImage(fileService.createStaticUrl(FileConstants.MODULE_MENU_ITEM, FileConstants.MODULE_DIRECTORY_IMAGE, menuItem.getId().toString())));
		return getMenuPreparationForMenuItemCategoryModel;
	}

	/**
	 * Manages account balance and history for menu preparation menu items.
	 *
	 * This method processes each menu preparation menu item and updates the account balance
	 * and history if the order type matches specific criteria and allocation type is not null.
	 *
	 * It delegates to {@link menuAllocationNativeQueryDao#manageAccountBalanceAndHistory} with the relevant details
	 * from the first allocation type associated with the menu preparation menu item.
	 *
	 * @param savedResponse The model containing the menu preparation menu items and relevant details.
	 */
	private void manageAccountBalanceAndHistory(SaveMenuPreparationModel savedResponse) {
		savedResponse.getMenuPreparationMenuItem().forEach(response -> {
			if ((response.getOrderType() == 1 || response.getOrderType() == 2) && response.getAllocationType() != null) {
				SaveMenuAllocationTypeModel firstAllocationType = response.getAllocationType().get(0);
				menuAllocationNativeQueryDao.manageAccountBalanceAndHistory(
						response.getOrderType(),
						firstAllocationType.getContactId(),
						firstAllocationType.getCounterNo(),
						firstAllocationType.getCounterPrice(),
						firstAllocationType.getHelperNo(),
						firstAllocationType.getHelperPrice(),
						firstAllocationType.getQuantity(),
						firstAllocationType.getPrice(),
						response.getOrderDate(),
						firstAllocationType.getId());
			}
		});
	}

	/**
	 * Creates and configures the order menu preparation model.
	 * This method sets the appropriate properties and handles associations with the SaveMenuPreparationModel instance provided.
	 *
	 * @param saveMenuPreparationModel The model containing menu preparation details.
	 */
	private void createOrderMenuPreparationModel(SaveMenuPreparationModel saveMenuPreparationModel) {
		saveMenuPreparationModel.setMenuTypeId((Objects.nonNull(saveMenuPreparationModel.getCustomPackageId()) && saveMenuPreparationModel.getCustomPackageId() > 0) ? MenuTypeEnum.PACKAGE.getId() : MenuTypeEnum.CUSTOM.getId());
		saveMenuPreparationModel.getMenuPreparationMenuItem().forEach(menuPreparationMenuItem -> {
			menuPreparationMenuItem.setMenuPreparation(saveMenuPreparationModel);
			DataUtils.setAuditFields(saveMenuPreparationMenuItemRepository, menuPreparationMenuItem.getId(), menuPreparationMenuItem);
			menuPreparationMenuItem.setIsActive(true);
			if (Objects.isNull(menuPreparationMenuItem.getPerson())) {
				menuPreparationMenuItem.setPerson(saveMenuPreparationModel.getOrderFunction().getPerson());
			}
			if (Objects.isNull(menuPreparationMenuItem.getOrderDate())) {
				menuPreparationMenuItem.setOrderDate(saveMenuPreparationModel.getOrderFunction().getDate());
			}
			if (Objects.isNull(menuPreparationMenuItem.getOrderType())) {
				menuPreparationMenuItem.setOrderType(OrderTypeEnum.DEFAULT.getValue());
			}
			// function call when record insert.
			if (Objects.isNull(menuPreparationMenuItem.getId())) {
				handleMenuPreparationMenuItem(menuPreparationMenuItem);
			}
		});
		if (Objects.nonNull(saveMenuPreparationModel.getNoItems())) {
			saveMenuPreparationModel.getNoItems().stream().filter(Objects::nonNull).forEach(noItem -> noItem.setMenuPreparation(saveMenuPreparationModel));
		}
		if (Objects.nonNull(saveMenuPreparationModel.getMenuPreparationMenuItemCategory())) {
			saveMenuPreparationModel.getMenuPreparationMenuItemCategory().stream().filter(Objects::nonNull).forEach(menuPreparationMenuItemCategory -> menuPreparationMenuItemCategory.setMenuPreparation(saveMenuPreparationModel));
		}
		DataUtils.setAuditFields(saveMenuPreparationRepository, saveMenuPreparationModel.getId(), saveMenuPreparationModel);
	}

	/**
	* Handles menu preparation menu items.
	*
	* This method processes menu preparation menu items, setting appropriate properties
	* based on the type of material, such as order type, contact ID, prices, and totals.
	*
	* @param menuPreparationMenuItem The menu preparation menu item to handle.
	*/
	private void handleMenuPreparationMenuItem(SaveMenuPreparationMenuItemModel menuPreparationMenuItem) {
		Integer isOutsideLabour = menuPreparationMenuItem.getMenuItem().getIsOutsideLabour();
		List<SaveMenuAllocationTypeModel> allocationTypes = new ArrayList<>();
		if (Objects.nonNull(isOutsideLabour) && isOutsideLabour == OrderTypeEnum.CHEF_LABOUR.getValue()) {
			menuPreparationMenuItem.setOrderType(OrderTypeEnum.OUTSIDE_FOOD.getValue());
			SaveMenuAllocationTypeModel allocationType = new SaveMenuAllocationTypeModel();
			allocationType.setContactId(menuPreparationMenuItem.getMenuItem().getContactAgencyId());
			DecimalFormat decimalFormat = new DecimalFormat("#.###");
			decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
			if (Objects.nonNull(menuPreparationMenuItem.getMenuItem().getQuantity())) {
				allocationType.setQuantity(Double.parseDouble(decimalFormat.format((menuPreparationMenuItem.getMenuItem().getQuantity() / 100) * menuPreparationMenuItem.getPerson()
				)));
			}
			allocationType.setUnitId(menuPreparationMenuItem.getMenuItem().getMeasurementId());
			allocationType.setPrice(menuPreparationMenuItem.getMenuItem().getPriceOutsideLabour());
			allocationType.setMenuPreparationMenuItem(menuPreparationMenuItem);
			if (Objects.nonNull(menuPreparationMenuItem.getMenuItem().getQuantity()) && Objects.nonNull(menuPreparationMenuItem.getMenuItem().getPriceOutsideLabour())) {
				allocationType.setTotal(((menuPreparationMenuItem.getMenuItem().getQuantity() / 100) * menuPreparationMenuItem.getPerson()) * menuPreparationMenuItem.getMenuItem().getPriceOutsideLabour());
			}
			allocationTypes.add(allocationType);
		}
		if (Objects.nonNull(isOutsideLabour) && isOutsideLabour == OrderTypeEnum.OUTSIDE_FOOD.getValue()) {
			menuPreparationMenuItem.setOrderType(OrderTypeEnum.CHEF_LABOUR.getValue());
			allocationTypes.add(getSaveMenuAllocationTypeModel(menuPreparationMenuItem));
		}
		menuPreparationMenuItem.setAllocationType(allocationTypes);
	}

	/**
	 * Creates and configures a SaveMenuAllocationTypeModel instance based on the provided
	 * SaveMenuPreparationMenuItemModel. This method sets various properties related to
	 * menu preparation, pricing, and allocation details.
	 *
	 * @param menuPreparationMenuItem The SaveMenuPreparationMenuItemModel instance containing
	 *								  details required for creating and configuring a SaveMenuAllocationTypeModel.
	 *
	 * @return A configured SaveMenuAllocationTypeModel instance containing allocation details
	 *		   derived from the provided menuPreparationMenuItem.
	 */
	private static SaveMenuAllocationTypeModel getSaveMenuAllocationTypeModel(SaveMenuPreparationMenuItemModel menuPreparationMenuItem) {
		SaveMenuAllocationTypeModel allocationType = new SaveMenuAllocationTypeModel();
		if (Objects.isNull(menuPreparationMenuItem.getMenuItem().getIsPlate())) {
			menuPreparationMenuItem.getMenuItem().setIsPlate(false);
		}
		allocationType.setIsPlate(menuPreparationMenuItem.getMenuItem().getIsPlate());
		allocationType.setUnitId(menuPreparationMenuItem.getMenuItem().getMeasurementId());
		allocationType.setContactId(menuPreparationMenuItem.getMenuItem().getContactAgencyId());
		allocationType.setHelperNo(menuPreparationMenuItem.getMenuItem().getHelper());
		allocationType.setHelperPrice(menuPreparationMenuItem.getMenuItem().getHelperPrice());
		allocationType.setCounterPrice(menuPreparationMenuItem.getMenuItem().getPriceOutsideLabour());
		allocationType.setMenuPreparationMenuItem(menuPreparationMenuItem);
		if (Objects.nonNull(menuPreparationMenuItem.getMenuItem().getQuantity())) {
			Integer counterNo = (int) (menuPreparationMenuItem.getMenuItem().getIsPlate() ? (menuPreparationMenuItem.getMenuItem().getQuantity() / 100) * menuPreparationMenuItem.getPerson() : menuPreparationMenuItem.getMenuItem().getQuantity().intValue());
			allocationType.setCounterNo(counterNo);
		}
		if (Objects.isNull(menuPreparationMenuItem.getMenuItem().getHelper()) && Objects.isNull(menuPreparationMenuItem.getMenuItem().getHelperPrice()) &&
			Objects.isNull(menuPreparationMenuItem.getMenuItem().getQuantity()) && Objects.isNull(menuPreparationMenuItem.getMenuItem().getPrice())) {
			allocationType.setTotal(null);
		} else {
			allocationType.setTotal(
				((allocationType.getCounterNo() != null ? allocationType.getCounterNo() : 0) * (allocationType.getCounterPrice() != null ? allocationType.getCounterPrice() : 0.0)) +
				((allocationType.getHelperNo() != null ? allocationType.getHelperNo() : 0) * (allocationType.getHelperPrice() != null ? allocationType.getHelperPrice() : 0.0)));
		}
		return allocationType;
	}

	/**
	 * Sets and processes the Order General Fix Raw Material and Crockery data for the given order.
	 * This includes saving raw material data, crockery data, and handling deletions based on specific logic.
	 *
	 * @param saveMenuPreparationModels The list of SaveMenuPreparationModel objects containing menu preparation data.
	 * @param orderId The ID of the order for which the data is being processed.
	 * @param functionIds The list of function IDs linked with the order.
	 */
	private void setOrderGeneralFixRawMaterialAndCrockeryData(List<SaveMenuPreparationModel> saveMenuPreparationModels, Long orderId, List<Long> functionIds) {
		// Save the Generalfix raw material when menu preparation save first time
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		List<OrderGeneralFixRawMaterialResponseContainerDto> orderGeneralFixRawMaterialResponseDtosList = new ArrayList<>();
		List<OrderGeneralFixRawMaterialDto> orderGeneralFixRawMaterialDtos = new ArrayList<>();
		if (!functionIds.isEmpty()) {
			saveMenuPreparationModels.forEach(saveMenuPreparationModel -> {
				if (functionIds.contains(saveMenuPreparationModel.getOrderFunction().getId())) {
					orderGeneralFixRawMaterialResponseDtosList.add(OrderGeneralFixRawMaterialResponseContainerDto.builder().functionId(saveMenuPreparationModel.getOrderFunction().getId()).orderGeneralFixRawMaterial(labourAndOtherManagementNativeQueryDao.findOrderGeneralFixRawMaterialByFunctionId(saveMenuPreparationModel.getOrderFunction().getId(), bookOrderRepository.getAdjustQuantityByOrderId(orderId))).build());
				}
			});
		}
		if (!orderGeneralFixRawMaterialResponseDtosList.isEmpty()) {
			orderGeneralFixRawMaterialResponseDtosList.stream().map(OrderGeneralFixRawMaterialResponseContainerDto::getOrderGeneralFixRawMaterial).flatMap(List::stream).forEach(orderGeneralFixKariyanu -> {
				OrderGeneralFixRawMaterialDto orderGeneralFixKariyanuDto =  modelMapperService.convertEntityAndDto(orderGeneralFixKariyanu, OrderGeneralFixRawMaterialDto.class);
				orderGeneralFixKariyanuDto.setOrderTime(orderGeneralFixKariyanu.getFunctionDate());
				orderGeneralFixRawMaterialDtos.add(orderGeneralFixKariyanuDto);
			});
			orderGeneralFixRawMaterialService.saveOrderGeneralFixRawMaterial(orderGeneralFixRawMaterialDtos);
		}

		// Save the Labour and Other Management when menu preparation save first time
		Long[] orderFunctionIdArray = functionIds.toArray(new Long[0]);
		if (orderFunctionIdArray.length > 0) {
			OrderCrockeryParameterDto orderCrockeryParameterDto = new OrderCrockeryParameterDto();
			orderCrockeryParameterDto.setBookOrderId(orderId);
			orderCrockeryParameterDto.setOrderFunction(orderFunctionIdArray);
			List<OrderCrockeryItemCategoryDto> orderCrockeryItemCategoryDtos = labourAndOtherManagementNativeQueryService.findCrockeryDataByOrderId(orderCrockeryParameterDto);
			List<OrderCrockeryDto> orderCrockeries = new ArrayList<>();
			orderCrockeryItemCategoryDtos.forEach(orderCrockeryItemCategoryDto -> {
				orderCrockeryItemCategoryDto.getRawMaterials().forEach(rawMaterial -> {
					OrderCrockeryDto orderCrockeryDto = new OrderCrockeryDto();
					orderCrockeryDto.setRawMaterialId(rawMaterial.getId());
					orderCrockeryDto.setOrderFunction(rawMaterial.getFunctionId());
					orderCrockeryDto.setQty(rawMaterial.getQty());
					orderCrockeryDto.setMeasurementId(rawMaterial.getMeasurementId());
					orderCrockeryDto.setOrderTime(rawMaterial.getFunctionDate());
					orderCrockeryDto.setAgency(rawMaterial.getAgency());
					orderCrockeryDto.setPrice(Objects.nonNull(rawMaterial.getQty()) && Objects.nonNull(rawMaterial.getSupplierRate()) ? rawMaterial.getQty() * rawMaterial.getSupplierRate() : 0);
					orderCrockeries.add(orderCrockeryDto);
				});
			});
			orderCrockeryService.saveCrockeryData(orderCrockeries);
		}

		// Delete the crockery and general fix raw material data of the function which has not the maximum person when the max person function is active
		if (Boolean.TRUE.equals(companySettingDto.getDisplayCrockeryAndGeneralFix())) {
			// Create a map to store function IDs and their respective person counts
			Map<Long, Integer> functionPersonCount = new HashMap<>();
			saveMenuPreparationModels.forEach(saveMenuPreparationModel -> {
				Long functionId = saveMenuPreparationModel.getOrderFunction().getId();
				int personCount = saveMenuPreparationModel.getOrderFunction().getPerson();
				functionPersonCount.putIfAbsent(functionId, personCount);
			});
			// Find the function with the maximum number of persons
			Long functionWithMaxPersons = functionPersonCount.entrySet().stream()
					.max(Map.Entry.comparingByValue())
					.map(Map.Entry::getKey).orElse(null);
			// If a function with the maximum persons is found, proceed with deletion
			if (functionWithMaxPersons != null) {
				List<Long> maxPersonFunctionList = Arrays.asList(functionWithMaxPersons);
				// Delete the crockery data
				orderCrockeryService.deleteCrockeryData(maxPersonFunctionList);
				// Delete the general fix raw material data
				orderGeneralFixRawMaterialService.deleteUnusedGeneralFixRawMaterial(maxPersonFunctionList);
			}
		}
	}

}