package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.RawMaterialCategoryDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.RawMaterialCategoryModel;
import com.catering.model.tenant.RawMaterialCategoryTypeModel;
import com.catering.repository.tenant.RawMaterialCategoryRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.RawMaterialCategoryService;
import com.catering.service.tenant.RawMaterialCategoryTypeService;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for handling operations related to Raw Material Categories.
 * This class extends the GenericServiceImpl for common CRUD operations and implements
 * RawMaterialCategoryService for specific methods tailored to raw material category management.
 * It integrates different services and repositories to perform business logic operations.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialCategoryServiceImpl extends GenericServiceImpl<RawMaterialCategoryDto, RawMaterialCategoryModel, Long> implements RawMaterialCategoryService {

	/**
	 * Service for sending messages and notifications.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing raw material categories.
	 */
	RawMaterialCategoryRepository rawMaterialCategoryRepository;

	/**
	 * Service for managing raw material category types.
	 */
	RawMaterialCategoryTypeService  rawMaterialCategoryTypeService;

	/**
	 * Constant for the raw material category name.
	 */
	String RAW_MATERIAL_CATEGORY_NAME = "Raw Material category name";

	/**
	 * Constant for the raw material category ID, derived from the category name.
	 */
	String RAW_MATERIAL_CATEGORY_ID = RAW_MATERIAL_CATEGORY_NAME + " ID: ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<RawMaterialCategoryDto> createAndUpdate(RawMaterialCategoryDto rawMaterialCategoryDto) throws RestException {
		// Check Item Category Type record is exist or not
		rawMaterialCategoryTypeService.existByIdOrThrow(rawMaterialCategoryDto.getRawMaterialCategoryType().getId());
		// Check for duplicate records with multiLanguage fields and throw an exception if any are found.
		ValidationUtils.validateFields(rawMaterialCategoryDto, rawMaterialCategoryRepository, exceptionService, messageService);
		// Convert DTO to Model
		if (Objects.isNull(rawMaterialCategoryDto.getIsDirectOrder())) {
			rawMaterialCategoryDto.setIsDirectOrder(Boolean.FALSE);
		}
		RawMaterialCategoryModel rawMaterialCategoryModel = modelMapperService.convertEntityAndDto(rawMaterialCategoryDto, RawMaterialCategoryModel.class);
		DataUtils.setAuditFields(rawMaterialCategoryRepository, rawMaterialCategoryDto.getId(), rawMaterialCategoryModel);
		return Optional.of(modelMapperService.convertEntityAndDto(rawMaterialCategoryRepository.save(rawMaterialCategoryModel), RawMaterialCategoryDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void existByIdOrThrow(Long id) throws RestException {
		if (Objects.isNull(id) || !rawMaterialCategoryRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST_BY_ID, RAW_MATERIAL_CATEGORY_ID));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RawMaterialCategoryDto> readDataByIsActive() {
		List<RawMaterialCategoryModel> rawMaterialCategoryModel = rawMaterialCategoryRepository.findAllByIsActiveOrderByPriorityAsc(true);
		return modelMapperService.convertListEntityAndListDto(rawMaterialCategoryModel, RawMaterialCategoryDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) throws RestException {
		if (!rawMaterialCategoryRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			rawMaterialCategoryRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<RawMaterialCategoryDto>> read(FilterDto filterDto) {
		Optional<Example<RawMaterialCategoryModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			RawMaterialCategoryModel rawMaterialCategoryModel = RawMaterialCategoryModel.ofSearchingModel(query);
			rawMaterialCategoryModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			rawMaterialCategoryModel.setRawMaterialCategoryType(RawMaterialCategoryTypeModel.ofSearchingModel(query));
			example = Optional.of(Example.of(rawMaterialCategoryModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(RawMaterialCategoryDto.class, RawMaterialCategoryModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RawMaterialCategoryDto> readByCategoryTypeId(List<Long> categoryTypeIds) {
		return modelMapperService.convertListEntityAndListDto(rawMaterialCategoryRepository.findByRawMaterialCategoryTypeIdInOrderByPriority(categoryTypeIds), RawMaterialCategoryDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RawMaterialCategoryDto> findByItemRawMaterialsNotNullOrderByPriority() {
		return modelMapperService.convertListEntityAndListDto(rawMaterialCategoryRepository.findDistinctByRawMaterialsNotNullOrderByPriority(), RawMaterialCategoryDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RawMaterialCategoryModel findById(Long categoryId) {
		Optional<RawMaterialCategoryModel> categoryOptional = rawMaterialCategoryRepository.findById(categoryId);
		return categoryOptional.orElseThrow(() -> new EntityNotFoundException("Item category not found with ID: " + categoryId));
	}

	/**
	 * Constructs and returns an ExampleMatcher configured to perform case-insensitive
	 * and substring-based matching for specific fields while ignoring audit fields.
	 *
	 * @return An ExampleMatcher instance customized to match specific fields with
	 *		   case-insensitive "contains" matchers and to ignore specified audit fields.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.RAW_MATERIAL_CATEGORY_FIELD_RAW_MATERIAL_CATEGORY_TYPE + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.RAW_MATERIAL_CATEGORY_FIELD_RAW_MATERIAL_CATEGORY_TYPE + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.RAW_MATERIAL_CATEGORY_FIELD_RAW_MATERIAL_CATEGORY_TYPE + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.PRIORITY, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.addAll(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.RAW_MATERIAL_CATEGORY_FIELD_RAW_MATERIAL_CATEGORY_TYPE)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePriority(List<RawMaterialCategoryDto> rawMaterialCategories) throws RestException {
		List<RawMaterialCategoryModel> rawMaterialCategoryModel = modelMapperService.convertListEntityAndListDto(rawMaterialCategories, RawMaterialCategoryModel.class);
		rawMaterialCategoryModel.forEach(rawMaterialCategory -> {
			DataUtils.setAuditFields(rawMaterialCategoryRepository, rawMaterialCategory.getId(), rawMaterialCategory);
			rawMaterialCategoryRepository.updatePriority(rawMaterialCategory);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getHighestPriority() throws RestException {
		return rawMaterialCategoryRepository.getHighestPriority();
	}

}