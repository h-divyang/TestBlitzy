package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catering.constant.FieldConstants;
import com.catering.constant.FileConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordExistDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.common.SearchFieldDto;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.CustomMenuItem;
import com.catering.dto.tenant.request.MenuItemDto;
import com.catering.dto.tenant.request.MenuItemRawMaterialDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MenuItemCategoryModel;
import com.catering.model.tenant.MenuItemModel;
import com.catering.model.tenant.MenuItemRawMaterialModel;
import com.catering.repository.tenant.MenuItemRawMaterialRepository;
import com.catering.repository.tenant.MenuItemRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.FileService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.S3Service;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CompanySettingService;
import com.catering.service.tenant.ContactCategoryService;
import com.catering.service.tenant.ContactService;
import com.catering.service.tenant.KitchenAreaService;
import com.catering.service.tenant.MeasurementService;
import com.catering.service.tenant.MenuItemCategoryService;
import com.catering.service.tenant.MenuItemService;
import com.catering.specification.GenericSpecificationService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The MenuItemServiceImpl class is the implementation of the MenuItemService interface.
 * It provides methods for creating, updating, reading, and deleting menu items while
 * managing their relationships with other entities like categories, raw materials,
 * kitchen areas, and associated files images.
 *
 * This service class extends GenericServiceImpl which provides generic CRUD functionality
 * and utilizes several dependent services and repositories for business logic and database operations.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuItemServiceImpl extends GenericServiceImpl<MenuItemDto, MenuItemModel, Long> implements MenuItemService {

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing Menu Items.
	 */
	MenuItemRepository menuItemRepository;

	/**
	 * Repository for managing the association between Menu Items and Raw Materials.
	 */
	MenuItemRawMaterialRepository menuItemRawMaterialRepository;

	/**
	 * Service for managing Menu Item Categories.
	 */
	MenuItemCategoryService menuItemCategoryService;

	/**
	 * Service for managing Kitchen Areas.
	 */
	KitchenAreaService kitchenAreaService;

	/**
	 * Service for managing contacts.
	 */
	ContactService contactService;

	/**
	 * Service for managing contact categories.
	 */
	ContactCategoryService contactCategoryService;

	/**
	 * Service for managing measurements.
	 */
	MeasurementService measurementService;

	/**
	 * Service for file management and processing.
	 */
	FileService fileService;

	/**
	 * Service for interacting with Amazon S3 storage.
	 */
	S3Service s3Service;

	/**
	 * Service for managing company-specific settings.
	 */
	CompanySettingService companySettingService;

	/**
	 * Service for managing generic specification
	 */
	GenericSpecificationService genericSpecificationService;

	/**
	 * Constant representing the label for Menu Items.
	 */
	String MENU_ITEM = "Menu Item";

	/**
	 * Constant representing the Menu Item ID prefix used in messages or logs.
	 */
	String MENU_ITEM_ID = MENU_ITEM + " ID: ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MenuItemDto> createAndUpdate(MenuItemDto menuItemDto, MultipartFile img) throws RestException {
		menuItemValidation(menuItemDto);
		MenuItemModel menuItemModel = modelMapperService.convertEntityAndDto(menuItemDto, MenuItemModel.class);
		DataUtils.setAuditFields(menuItemRepository, menuItemDto.getId(), menuItemModel);
		menuItemModel = menuItemRepository.save(menuItemModel);
		// Save Temporary Menu Item Raw Material List
		List<MenuItemRawMaterialModel> menuItemRawMaterialModels = new ArrayList<>();
		MenuItemRawMaterialModel menuItemRawMaterialModel = null;
		if(menuItemDto.getMenuItemRawMaterialList() != null && !menuItemDto.getMenuItemRawMaterialList().isEmpty()) {
			for (MenuItemRawMaterialDto menuItemRawMaterialDto : menuItemDto.getMenuItemRawMaterialList()) {
				menuItemRawMaterialModel = modelMapperService.convertEntityAndDto(menuItemRawMaterialDto, MenuItemRawMaterialModel.class);
				menuItemRawMaterialModel.setMenuItem(menuItemModel);
				DataUtils.setAuditFields(menuItemRawMaterialRepository, menuItemDto.getId(), menuItemRawMaterialModel);
				menuItemRawMaterialModels.add(menuItemRawMaterialModel);
			}
			if (!menuItemRawMaterialModels.isEmpty()) {
				menuItemRawMaterialRepository.saveAll(menuItemRawMaterialModels);
			}
		}
		MenuItemDto menuItemResponseDto = modelMapperService.convertEntityAndDto(menuItemModel, MenuItemDto.class);
		if (Objects.nonNull(img)) {
			fileService.upload(img, fileService.createKey(FileConstants.MODULE_MENU_ITEM, FileConstants.MODULE_DIRECTORY_IMAGE, menuItemResponseDto.getId().toString()));
		}
		return Optional.of(menuItemResponseDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<MenuItemDto>> read(FilterDto filterDto, SearchFieldDto searchFieldDto, HttpServletRequest request) {
		Optional<Example<MenuItemModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		ResponseContainerDto<List<MenuItemDto>> menuItemResponseDto;
		if (StringUtils.isNotBlank(query) && searchFieldDto.getFieldValue() == -1) { // CASE 1: Perform free-text search using ExampleMatcher if query is present and no category filter is applied
			MenuItemModel menuItemModel = MenuItemModel.ofSearchingModel(query);
			menuItemModel.setMenuItemCategory(MenuItemCategoryModel.ofSearchingModel(query));
			example = Optional.of(Example.of(menuItemModel, getExampleMatcher()));
			menuItemResponseDto = read(MenuItemDto.class, MenuItemModel.class, filterDto, example);
		} else if (searchFieldDto.getFieldName().equals(FieldConstants.CATEGORY) && searchFieldDto.getFieldValue() != -1 && Objects.nonNull(searchFieldDto.getFieldValue())) { // CASE 2: Apply category-based filtering using Specifications
			// Build a specification based on filter DTOs
			Specification<MenuItemModel> spec = buildSpecification(filterDto, searchFieldDto);
			menuItemResponseDto = genericSpecificationService.readWithSpecification(MenuItemDto.class, MenuItemModel.class, filterDto, spec, this::mapToDto);
		} else { // CASE 3: Fallback — read everything without any filters
			menuItemResponseDto = read(MenuItemDto.class, MenuItemModel.class, filterDto, example);
		}
		// For each returned menu item, generate a static URL for the image
		menuItemResponseDto.getBody().forEach(menuItem -> {
			menuItem.setImage(fileService.createStaticUrl(FileConstants.MODULE_MENU_ITEM, FileConstants.MODULE_DIRECTORY_IMAGE, menuItem.getId().toString()));
		});
		return menuItemResponseDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id, Boolean isImage) {
		if (Boolean.TRUE.equals(isImage)) {
			s3Service.delete(fileService.createKey(FileConstants.MODULE_MENU_ITEM, FileConstants.MODULE_DIRECTORY_IMAGE, id.toString()));
		} else {
			if (!menuItemRepository.existsById(id)) {
				exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
			}
			try {
				menuItemRepository.deleteById(id);
				s3Service.delete(fileService.createKey(FileConstants.MODULE_MENU_ITEM, FileConstants.MODULE_DIRECTORY_IMAGE, id.toString()));
			} catch (DataIntegrityViolationException e) {
				exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void existByIdOrThrow(Long id) throws RestException {
		if (Objects.isNull(id) || !menuItemRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST_BY_ID, MENU_ITEM_ID));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MenuItemDto> updateStatus(Long id, Boolean status) throws RestException {
		Optional<MenuItemModel> menuItemModel = menuItemRepository.findById(id);
		menuItemModel.get().setIsActive(status);
		return Optional.of(modelMapperService.convertEntityAndDto(menuItemRepository.save(menuItemModel.get()), MenuItemDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<CustomMenuItem>> read() {
		List<MenuItemModel> menuItems = menuItemRepository.findByIsActiveTrue();
		return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(menuItems, CustomMenuItem.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByMenuItemSubCategoryId(Long id) {
		return menuItemRepository.existsByMenuItemSubCategory_Id(id);
	}

	/**
	 * Validates a MenuItemDto object by ensuring related entities referenced by the DTO exist
	 * and that fields are valid as per business requirements. If any validation fails, a
	 * RestException is thrown.
	 *
	 * @param menuItemDto The MenuItemDto object containing details of the menu item to validate.
	 * @throws RestException If any referenced entity in the MenuItemDto does not exist or if the field validation fails.
	 */
	private void menuItemValidation(MenuItemDto menuItemDto) throws RestException {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		menuItemCategoryService.existByIdOrThrow(menuItemDto.getMenuItemCategory().getId());
		if (Objects.nonNull(menuItemDto.getKitchenArea())) {
			kitchenAreaService.existByIdOrThrow(menuItemDto.getKitchenArea().getId());
		}
		if (Objects.nonNull(menuItemDto.getContactCategory())) {
			contactCategoryService.existByIdOrThrow(menuItemDto.getContactCategory().getId());
		}
		if (Objects.nonNull(menuItemDto.getContactResponse())) {
			contactService.existByIdOrThrow(menuItemDto.getContactResponse().getId());
		}
		if (Objects.nonNull(menuItemDto.getMeasurement())) {
			measurementService.existByIdOrThrow(menuItemDto.getMeasurement().getId());
		}
		if (Boolean.TRUE.equals(companySettingDto.getIsMenuItemUnique())) {
			ValidationUtils.validateFields(menuItemDto, menuItemRepository, exceptionService, messageService);
		} else {
			validateMenuItem(menuItemDto);
		}
	}

	/**
	 * Configures and returns an {@code ExampleMatcher} for performing case-insensitive and partial string matches
	 * on specified fields of a menu item entity. This matcher is customized to handle fields related to
	 * multiple languages, menu item categories, and other specific fields, while ignoring specified fields,
	 * such as audit-related fields and certain other properties that do not contribute to matching logic.
	 *
	 * @return An {@code ExampleMatcher} with customized matching and ignored path settings.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MENU_ITEM_PRICE, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MENU_ITEM_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MENU_ITEM_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MENU_ITEM_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.MENU_ITEM_CATEGORY), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.FINAL_KITCHEN_AREA)))
			.withIgnorePaths(FieldConstants.IS_PLATE);
	}

	/**
	 * Validates whether a menu item with the same name and same category already exists.
	 * This method checks for duplicates in the default, preferred, and supportive language names.
	 *
	 * @param menuItemDto The DTO containing menu item details.
	 * @throws BadRequestException if a menu item with the same name & category already exists.
	 */
	private void validateMenuItem(MenuItemDto menuItemDto) {
		boolean isDefaultExist = menuItemDto.getId() != null ? menuItemRepository.existsByNameDefaultLangIgnoreCaseAndMenuItemCategoryIdAndIdNot(menuItemDto.getNameDefaultLang(), menuItemDto.getMenuItemCategory().getId(), menuItemDto.getId()) : menuItemRepository.existsByNameDefaultLangIgnoreCaseAndMenuItemCategoryId(menuItemDto.getNameDefaultLang(), menuItemDto.getMenuItemCategory().getId());
		boolean isPreferExist = false;
		boolean isSupportiveExist = false;
		if (StringUtils.isNotBlank(menuItemDto.getNamePreferLang())) {
			isPreferExist = menuItemDto.getId() != null ? menuItemRepository.existsByNamePreferLangIgnoreCaseAndMenuItemCategoryIdAndIdNot(menuItemDto.getNamePreferLang(), menuItemDto.getMenuItemCategory().getId(), menuItemDto.getId()) : menuItemRepository.existsByNamePreferLangIgnoreCaseAndMenuItemCategoryId(menuItemDto.getNamePreferLang(), menuItemDto.getMenuItemCategory().getId());
		}
		if (StringUtils.isNotBlank(menuItemDto.getNameSupportiveLang())) {
			isSupportiveExist = menuItemDto.getId() != null ? menuItemRepository.existsByNameSupportiveLangIgnoreCaseAndMenuItemCategoryIdAndIdNot(menuItemDto.getNameSupportiveLang(),menuItemDto.getMenuItemCategory().getId(),menuItemDto.getId()) : menuItemRepository.existsByNameSupportiveLangIgnoreCaseAndMenuItemCategoryId(menuItemDto.getNameSupportiveLang(),menuItemDto.getMenuItemCategory().getId());
		}
		if (isDefaultExist || isPreferExist || isSupportiveExist) {
			RecordExistDto recordExistDto = RecordExistDto.builder().isExist(true).isNameDefaultLang(isDefaultExist).isNamePreferLang(isPreferExist).isNameSupportiveLang(isSupportiveExist).build();
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.ALREADY_EXIST), recordExistDto);
		}
	}

	/**
	 * Maps a MenuItemModel entity to a MenuItemDto using a model mapper service.
	 *
	 * @param model the entity to convert
	 * @return the converted DTO
	 */
	private MenuItemDto mapToDto(MenuItemModel model) {
		return modelMapperService.convertEntityAndDto(model, MenuItemDto.class);
	}

	/**
	 * Builds a JPA Specification for filtering and searching MenuItemModel records based on
	 * both structured filters (like category) and unstructured search queries (like keyword search).
	 *
	 * @param filterDto      contains query string for keyword-based search
	 * @param searchFieldDto contains structured filter fields (like category ID)
	 * @return a Specification<MenuItemModel> combining all filter and search conditions
	 */
	private Specification<MenuItemModel> buildSpecification(FilterDto filterDto, SearchFieldDto searchFieldDto) {
		Specification<MenuItemModel> spec = Specification.where(null); // Initialize an empty specification

		// Check if the structured filter is for CATEGORY and the value is valid (not null and not -1)
		if (FieldConstants.CATEGORY.equals(searchFieldDto.getFieldName()) && searchFieldDto.getFieldValue() != null && searchFieldDto.getFieldValue() != -1) { // Category filter (AND condition)
			spec = spec.and((root, query, cb) -> cb.equal(root.get(FieldConstants.MENU_ITEM_CATEGORY).get(FieldConstants.COMMON_FIELD_ID), searchFieldDto.getFieldValue()));
		}

		if (StringUtils.isNotBlank(filterDto.getQuery())) { // Search query (OR conditions)
			spec = spec.and(buildSearchSpecification(filterDto.getQuery()));
		}
		return spec;
	}

	/**
	 * Constructs a JPA Specification for full-text search across multiple fields
	 * in both MenuItemModel and its related MenuItemCategoryModel.
	 *
	 * @param query the search string entered by the user
	 * @return a Specification<MenuItemModel> containing OR conditions for fuzzy search
	 */
	private Specification<MenuItemModel> buildSearchSpecification(String query) {
		return (root, queryBuilder, cb) -> {
			// Search in MenuItem fields
			Predicate itemPredicate = cb.or(
					cb.like(cb.lower(root.get(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG)), "%" + query.toLowerCase() + "%"),
					cb.like(cb.lower(root.get(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG)), "%" + query.toLowerCase() + "%"),
					cb.like(cb.lower(root.get(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG)), "%" + query.toLowerCase() + "%"));

			// Join with the related MenuItemCategory entity
			Join<MenuItemModel, MenuItemCategoryModel> categoryJoin = root.join(FieldConstants.MENU_ITEM_CATEGORY);
			Predicate categoryPredicate = cb.or(
					cb.like(cb.lower(categoryJoin.get(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG)), "%" + query.toLowerCase() + "%"),
					cb.like(cb.lower(categoryJoin.get(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG)), "%" + query.toLowerCase() + "%"),
					cb.like(cb.lower(categoryJoin.get(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG)), "%" + query.toLowerCase() + "%"));

			return cb.or(itemPredicate, categoryPredicate);
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePriority(List<MenuItemDto> menuItems) throws RestException {
		List<MenuItemModel> rawMaterialCategoryModel = modelMapperService.convertListEntityAndListDto(menuItems, MenuItemModel.class);
		rawMaterialCategoryModel.forEach(rawMaterialCategory -> {
			DataUtils.setAuditFields(menuItemRepository, rawMaterialCategory.getId(), rawMaterialCategory);
			menuItemRepository.updatePriority(rawMaterialCategory);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getHighestPriority() throws RestException {
		return menuItemRepository.getHighestPriority();
	}

}