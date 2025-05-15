package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.MenuItemSubCategoryDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MenuItemSubCategoryModel;
import com.catering.repository.tenant.MenuItemSubCategoryRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.MenuItemService;
import com.catering.service.tenant.MenuItemSubCategoryService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the MenuItemSubCategoryService interface for managing MenuItemSubCategory entities.
 * Provides methods for creating, updating, reading, and deleting menu item subcategory data.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MenuItemSubCategoryServiceImpl extends GenericServiceImpl<MenuItemSubCategoryDto, MenuItemSubCategoryModel, Long> implements MenuItemSubCategoryService {

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
	 * Repository for managing Menu Item Sub-Categories.
	 */
	MenuItemSubCategoryRepository menuItemSubCategoryRepository;

	/**
	 * Service for managing Menu Items.
	 */
	MenuItemService menuItemService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MenuItemSubCategoryDto> createAndUpdate(MenuItemSubCategoryDto menuItemSubCategoryDto) throws RestException {
		ValidationUtils.validateFields(menuItemSubCategoryDto, menuItemSubCategoryRepository, exceptionService, messageService);
		MenuItemSubCategoryModel menuItemSubCategoryModel = modelMapperService.convertEntityAndDto(menuItemSubCategoryDto, MenuItemSubCategoryModel.class);
		DataUtils.setAuditFields(menuItemSubCategoryRepository, menuItemSubCategoryDto.getId(), menuItemSubCategoryModel);
		return Optional.of(modelMapperService.convertEntityAndDto(menuItemSubCategoryRepository.save(menuItemSubCategoryModel), MenuItemSubCategoryDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<MenuItemSubCategoryDto>> read(FilterDto filterDto) {
		Optional<Example<MenuItemSubCategoryModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			MenuItemSubCategoryModel menuItemSubCategoryModel = MenuItemSubCategoryModel.ofSearchingModel(query);
			menuItemSubCategoryModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(menuItemSubCategoryModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(MenuItemSubCategoryDto.class, MenuItemSubCategoryModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		if (!menuItemSubCategoryRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		if (menuItemService.existsByMenuItemSubCategoryId(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		menuItemSubCategoryRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuItemSubCategoryDto> findByIsActiveTrue() {
		return modelMapperService.convertListEntityAndListDto(menuItemSubCategoryRepository.findByIsActiveTrue(), MenuItemSubCategoryDto.class);
	}

	/**
	 * Retrieves an ExampleMatcher configured with specific matchers and ignore paths.
	 *
	 * @return An ExampleMatcher configured with matchers for common field names in default, preferred, and supportive languages, and with ignore paths for audit fields.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsName()));
	}

}