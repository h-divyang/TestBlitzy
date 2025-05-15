package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.catering.constant.FieldConstants;
import com.catering.constant.FileConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.MenuItemCategoryAndMenuItemDto;
import com.catering.dto.tenant.request.MenuItemCategoryDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MenuItemCategoryModel;
import com.catering.repository.tenant.MenuItemCategoryRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.FileService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.S3Service;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.MenuItemCategoryService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the MenuItemCategoryService interface that provides
 * functionalities related to managing menu item categories.
 *
 * This service extends the GenericServiceImpl class and utilizes various
 * helper services and repositories to perform operations such as creating,
 * updating, retrieving, and deleting menu item categories.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuItemCategoryServiceImpl extends GenericServiceImpl<MenuItemCategoryDto, MenuItemCategoryModel, Long> implements MenuItemCategoryService {

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
	 * Repository for managing Menu Item Categories.
	 */
	MenuItemCategoryRepository menuItemCategoryRepository;

	/**
	 * Service for file management and processing.
	 */
	FileService fileService;

	/**
	 * Service for interacting with Amazon S3 storage.
	 */
	S3Service s3Service;

	/**
	 * Constant representing the label for the Menu Item Category.
	 */
	String MENU_ITEM_CATEGORY = "Menu Item Category";

	/**
	 * Constant representing the Menu Item Category ID prefix used in messages or logs.
	 */
	String MENU_ITEM_CATEGORY_ID = MENU_ITEM_CATEGORY + " ID: ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MenuItemCategoryDto> createAndUpdate(MenuItemCategoryDto menuItemCategoryDto, MultipartFile img) throws RestException {
		ValidationUtils.validateFields(menuItemCategoryDto, menuItemCategoryRepository, exceptionService, messageService);
		MenuItemCategoryModel menuItemCategoryModel = modelMapperService.convertEntityAndDto(menuItemCategoryDto, MenuItemCategoryModel.class);
		DataUtils.setAuditFields(menuItemCategoryRepository, menuItemCategoryDto.getId(), menuItemCategoryModel);
		MenuItemCategoryDto menuItemCategoryResponseDto = modelMapperService.convertEntityAndDto(menuItemCategoryRepository.save(menuItemCategoryModel), MenuItemCategoryDto.class);
		if (Objects.nonNull(img)) {
			fileService.upload(img, fileService.createKey(FileConstants.MODULE_MENU_ITEM_CATEGORY, menuItemCategoryResponseDto.getId().toString()));
		}
		return Optional.of(menuItemCategoryResponseDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<MenuItemCategoryDto>> read(FilterDto filterDto, HttpServletRequest request) {
		Optional<Example<MenuItemCategoryModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			MenuItemCategoryModel menuItemCategoryModel = MenuItemCategoryModel.ofSearchingModel(query);
			menuItemCategoryModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(menuItemCategoryModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		ResponseContainerDto<List<MenuItemCategoryDto>> menuItemCategoryResponseDto = read(MenuItemCategoryDto.class, MenuItemCategoryModel.class, filterDto, example);
		menuItemCategoryResponseDto.getBody().forEach(menuItemCategory -> menuItemCategory.setImage(fileService.createStaticUrl(FileConstants.MODULE_MENU_ITEM_CATEGORY, menuItemCategory.getId().toString())));
		return menuItemCategoryResponseDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuItemCategoryDto> readDataByIsActive() {
		return modelMapperService.convertListEntityAndListDto(menuItemCategoryRepository.findByIsActiveTrueOrderByPriority(), MenuItemCategoryDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuItemCategoryAndMenuItemDto> readDataByIsActiveMenuItemExist(String tenant) {
		List<MenuItemCategoryModel> menuItemCategoryModel = menuItemCategoryRepository.findDistinctByMenuItemsIsNotNullAndIsActiveTrueOrderByPriority();
		List<MenuItemCategoryAndMenuItemDto> menuItemCategoryAndMenuItemDtos = modelMapperService.convertListEntityAndListDto(menuItemCategoryModel, MenuItemCategoryAndMenuItemDto.class);
		menuItemCategoryAndMenuItemDtos.stream().map(MenuItemCategoryAndMenuItemDto::getMenuItems)
			.flatMap(List::stream)
			.forEach(menuItem -> menuItem.setImage(fileService.createStaticUrl(FileConstants.MODULE_MENU_ITEM, FileConstants.MODULE_DIRECTORY_IMAGE, menuItem.getId().toString())));
		return menuItemCategoryAndMenuItemDtos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id, Boolean isImage) {
		if (isImage) {
			s3Service.delete(fileService.createKey(FileConstants.MODULE_MENU_ITEM_CATEGORY, id.toString()));
		} else {
			if (!menuItemCategoryRepository.existsById(id)) {
				exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
			}
			try {
				menuItemCategoryRepository.deleteById(id);
				s3Service.delete(fileService.createKey(FileConstants.MODULE_MENU_ITEM_CATEGORY, id.toString()));
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
		if (Objects.isNull(id) || !menuItemCategoryRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST_BY_ID, MENU_ITEM_CATEGORY_ID));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MenuItemCategoryDto> updateStatus(Long id, Boolean status) throws RestException {
		Optional<MenuItemCategoryModel> menuItemCategoryModel = menuItemCategoryRepository.findById(id);
		menuItemCategoryModel.get().setIsActive(status);
		return Optional.of(modelMapperService.convertEntityAndDto(menuItemCategoryRepository.save(menuItemCategoryModel.get()), MenuItemCategoryDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MenuItemCategoryModel findById(Long categoryId) {
		Optional<MenuItemCategoryModel> categoryOptional = menuItemCategoryRepository.findById(categoryId);
		return categoryOptional.orElseThrow(() -> new EntityNotFoundException("Item category not found with ID: " + categoryId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuItemCategoryDto> findByMenuItemsNotNullOrderByPriority() {
		return modelMapperService.convertListEntityAndListDto(menuItemCategoryRepository.findDistinctByMenuItemsIsNotNullAndIsActiveTrueOrderByPriority(), MenuItemCategoryDto.class);
	}

	/**
	 * Constructs and returns an ExampleMatcher object configured for case-insensitive
	 * matching of specific field names and ignoring specified paths.
	 *
	 * @return an ExampleMatcher instance with defined matching rules and ignored paths.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePriority(List<MenuItemCategoryDto> menuItemCategories) throws RestException {
		List<MenuItemCategoryModel> menuItemCategoryModel = modelMapperService.convertListEntityAndListDto(menuItemCategories, MenuItemCategoryModel.class);
		menuItemCategoryModel.forEach(menuItemCategory -> {
			DataUtils.setAuditFields(menuItemCategoryRepository, menuItemCategory.getId(), menuItemCategory);
			menuItemCategoryRepository.updatePriority(menuItemCategory);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getHighestPriority() throws RestException {
		return menuItemCategoryRepository.getHighestPriority();
	}

}