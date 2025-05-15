package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
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
import com.catering.dto.tenant.request.MealTypeDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MealTypeModel;
import com.catering.repository.tenant.MealTypeRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.MealTypeService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the MealTypeService interface.
 * Extends the GenericServiceImpl class with MealTypeDto as the DTO type,
 * MealTypeModel as the entity type, and Long as the ID type.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 *
 * @author Krushali Talaviya
 * @since June 2023
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MealTypeServiceImpl extends GenericServiceImpl<MealTypeDto, MealTypeModel, Long> implements MealTypeService {

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
	 * Repository for managing Meal Type entities.
	 */
	MealTypeRepository mealTypeRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MealTypeDto> createAndUpdate(MealTypeDto mealTypeDto) throws RestException {
		ValidationUtils.validateFields(mealTypeDto, mealTypeRepository, exceptionService, messageService);
		MealTypeModel mealTypeModel = modelMapperService.convertEntityAndDto(mealTypeDto, MealTypeModel.class);
		if (Objects.nonNull(mealTypeModel.getMealTypeNoItems())) {
			mealTypeModel.getMealTypeNoItems().stream().filter(Objects::nonNull).forEach(noItem -> noItem.setMealType(mealTypeModel));
		}
		DataUtils.setAuditFields(mealTypeRepository, mealTypeDto.getId(), mealTypeModel);
		return Optional.of(modelMapperService.convertEntityAndDto(mealTypeRepository.save(mealTypeModel), MealTypeDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<MealTypeDto>> read(FilterDto filterDto) {
		Optional<Example<MealTypeModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			MealTypeModel mealTypeModel = MealTypeModel.ofSearchingModel(query);
			mealTypeModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(mealTypeModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(MealTypeDto.class, MealTypeModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		if (!mealTypeRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			mealTypeRepository.deleteById(id);
		} catch (Exception e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existById(Long id) {
		return Objects.nonNull(id) && mealTypeRepository.existsById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MealTypeDto> findByIsActiveTrue() {
		return modelMapperService.convertListEntityAndListDto(mealTypeRepository.findByIsActiveTrue(), MealTypeDto.class);
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