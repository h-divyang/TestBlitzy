package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.CustomPackageDto;
import com.catering.dto.tenant.request.CustomPackageListResponseDto;
import com.catering.dto.tenant.request.CustomPackageRecordResponseDto;
import com.catering.dto.tenant.request.SaveCustomPackageRecordRequestDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.CustomPackageModel;
import com.catering.model.tenant.GetMenuPreparationForMenuItemCategoryModel;
import com.catering.model.tenant.GetMenuPreparationForMenuItemModel;
import com.catering.model.tenant.MenuItemCategoryModel;
import com.catering.model.tenant.MenuItemModel;
import com.catering.properties.ServerProperties;
import com.catering.repository.tenant.CustomPackageRepository;
import com.catering.repository.tenant.PackageMenuItemRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CustomPackageService;
import com.catering.service.tenant.OrderMenuPreparationService;
import com.catering.service.tenant.RawMaterialService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the CustomPackageService interface.
 * <p>
 * This class provides the business logic for managing custom packages. It includes 
 * operations such as creating, updating, reading, and deleting custom packages, 
 * as well as managing their statuses and associated menu item categories.
 * </p>
 * 
 * @author Priyansh Patel
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomPackageServiceImpl extends GenericServiceImpl<CustomPackageListResponseDto, CustomPackageModel, Long> implements CustomPackageService {

	CustomPackageRepository customPackageRepository;

	ModelMapperService modelMapperService;

	MessageService messageService;

	ExceptionService exceptionService;

	RawMaterialService rawMaterialService;

	OrderMenuPreparationService orderMenuPreparationService;

	ServerProperties serverProperties;

	PackageMenuItemRepository packageMenuItemRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createAndUpdate(SaveCustomPackageRecordRequestDto saveCustomPackageRecord) throws RestException {
		// Check for duplicate records with multiLanguage fields and throw an exception if any are found.
		ValidationUtils.validateFields(saveCustomPackageRecord, customPackageRepository, exceptionService, messageService);
		createOrderMenuPreparationModel(saveCustomPackageRecord);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<CustomPackageListResponseDto>> read(FilterDto filterDto) {
		Optional<Example<CustomPackageModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			CustomPackageModel customPackageModel = CustomPackageModel.ofSearchingModel(query);
			customPackageModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(customPackageModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(CustomPackageListResponseDto.class, CustomPackageModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomPackageRecordResponseDto getById(Long id) {
		CustomPackageModel customPackageModel = customPackageRepository.findById(id).orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessagesConstant.INVALID_ID)));
		return modelMapperService.convertEntityAndDto(customPackageModel, CustomPackageRecordResponseDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		if (!customPackageRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		if (orderMenuPreparationService.existsByCustomPackageId(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		customPackageRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<CustomPackageListResponseDto> updateStatus(Long id, Boolean status) throws RestException {
		if (orderMenuPreparationService.existsByCustomPackageId(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		CustomPackageModel customPackageModel = customPackageRepository.findById(id).orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST, messageService.getMessage(MessagesConstant.NOT_EXIST)));
		customPackageModel.setIsActive(status);
		return Optional.of(modelMapperService.convertEntityAndDto(customPackageRepository.save(customPackageModel), CustomPackageListResponseDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetMenuPreparationForMenuItemCategoryModel> getMenuItemCategoryByMenuItemExist(Long packageId, String tenant) {
		List<GetMenuPreparationForMenuItemCategoryModel> getMenuPreparationForMenuItemCategoryModel = customPackageRepository.findDistinctByMenuItemCategoriesIsNotNullAndIsActiveTrueOrderByPriority(packageId);
		List<GetMenuPreparationForMenuItemModel> getMenuPreparationForMenuItemModel = customPackageRepository.findDistinctByMenuItemsIsNotNullAndIsActiveTrueOrderByPriority(packageId);
		getMenuPreparationForMenuItemCategoryModel.forEach(menuItemCategory -> {
			List<GetMenuPreparationForMenuItemModel> menuItems = getMenuPreparationForMenuItemModel.stream().filter(menuItem -> menuItem.getMenuItemCategory().equals(menuItemCategory.getId())).toList();
			menuItemCategory.setMenuItems(menuItems);
		});
		getMenuPreparationForMenuItemModel.forEach(menuItem -> menuItem.setImage("https://jucas.s3.ap-south-1.amazonaws.com/" + (serverProperties.isProduction() ? "jucas" : "dev") + "/" + tenant + "/menu-item/image/" + menuItem.getId().toString()));
		return getMenuPreparationForMenuItemCategoryModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomPackageDto> readActive() {
		List<CustomPackageModel> customPackages = customPackageRepository.findByIsActiveTrue();
		List<CustomPackageDto> packages = modelMapperService.convertListEntityAndListDto(customPackages, CustomPackageDto.class);
		packages.stream().map(CustomPackageDto::getPackageMenuItemsList)
		.flatMap(List::stream)
		.forEach( menuItem -> menuItem.getMenuItem().setRawMaterials(rawMaterialService.findByMenuItemId(menuItem.getId())));
	return packages;
	}

	/**
	 * Creates an ExampleMatcher for filtering customPackageModel based on the provided search criteria.
	 * 
	 * @return An ExampleMatcher configured with the desired matching rules.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.PACKAGE_PRICE, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.PACKAGE_TOTAL_ITEMS, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsName()));
	}

	/**
	 * Creates and saves or update custom package model with its associated menu items and categories.
	 * 
	 * @param saveCustomPackageRecord the DTO containing the data for the custom package.
	 */
	private void createOrderMenuPreparationModel(SaveCustomPackageRecordRequestDto saveCustomPackageRecord) {
		CustomPackageModel customPackageModel = modelMapperService.convertEntityAndDto(saveCustomPackageRecord, CustomPackageModel.class);
		if (customPackageModel.getId() == null) {
			customPackageModel.setIsActive(true);
		}
		customPackageModel.setTotalItems(Long.valueOf(customPackageModel.getPackageMenuItemsList().size()));

		customPackageModel.getPackageMenuItemsList().forEach(customPackageMenuItem -> {
			customPackageMenuItem.setCustomPackage(customPackageModel);
			customPackageMenuItem.setMenuItemCategory(new MenuItemCategoryModel(customPackageMenuItem.getMenuItemCategoryId()));
			customPackageMenuItem.setMenuItem(new MenuItemModel(customPackageMenuItem.getMenuItemId()));
			DataUtils.setAuditFields(packageMenuItemRepository, customPackageMenuItem.getId(), customPackageMenuItem);
			customPackageMenuItem.setIsActive(true);
		});

		if (Objects.nonNull(customPackageModel.getPackageMenuItemCategoryList())) {
			customPackageModel.getPackageMenuItemCategoryList().stream().filter(Objects::nonNull)
			.forEach(customPackageMenuItemCategory -> {
				customPackageMenuItemCategory.setCustomPackage(customPackageModel);
				customPackageMenuItemCategory.setMenuItemCategory(new MenuItemCategoryModel(customPackageMenuItemCategory.getMenuItemCategoryId()));
			});
		}
		DataUtils.setAuditFields(customPackageRepository, customPackageModel.getId(), customPackageModel);
		customPackageRepository.save(customPackageModel);
	}

}