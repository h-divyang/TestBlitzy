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
import com.catering.dto.tenant.request.KitchenAreaDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.KitchenAreaModel;
import com.catering.repository.tenant.KitchenAreaRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.KitchenAreaService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link KitchenAreaService} interface for managing operations related to kitchen areas.
 * This class extends the {@link GenericServiceImpl} to provide shared functionality for common CRUD operations,
 * while introducing methods specific to kitchen area management.
 *
 * The implementation utilizes services for exception handling, entity-to-DTO mapping, and message management,
 * as well as the {@link KitchenAreaRepository} for persistence operations.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KitchenAreaServiceImpl extends GenericServiceImpl<KitchenAreaDto, KitchenAreaModel, Long> implements KitchenAreaService {

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
	 * Repository for managing Kitchen Area entities.
	 */
	KitchenAreaRepository kitchenAreaRepository;

	/**
	 * Constant representing the label for Kitchen Area.
	 */
	String KITCHEN_AREA = "Kitchen Area";

	/**
	 * Constant for representing the Kitchen Area ID prefix used in messages or logs.
	 */
	String KITCHEN_AREA_ID = KITCHEN_AREA + " ID: ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<KitchenAreaDto> createAndUpdate(KitchenAreaDto kitchenAreaDto) throws RestException {
		// Check for duplicate records with multiLanguage fields and throw an exception if any are found.
		ValidationUtils.validateFields(kitchenAreaDto, kitchenAreaRepository, exceptionService, messageService);
		KitchenAreaModel kitchenAreaModel = modelMapperService.convertEntityAndDto(kitchenAreaDto, KitchenAreaModel.class);
		DataUtils.setAuditFields(kitchenAreaRepository, kitchenAreaDto.getId(), kitchenAreaModel);
		return Optional.of(modelMapperService.convertEntityAndDto(kitchenAreaRepository.save(kitchenAreaModel), KitchenAreaDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void existByIdOrThrow(Long id) throws RestException {
		if (Objects.isNull(id) || !kitchenAreaRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST_BY_ID, KITCHEN_AREA_ID));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<KitchenAreaDto>> read(FilterDto filterDto) {
		Optional<Example<KitchenAreaModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			KitchenAreaModel kitchenAreaModel = KitchenAreaModel.ofSearchingModel(query);
			kitchenAreaModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(kitchenAreaModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(KitchenAreaDto.class, KitchenAreaModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) throws RestException {
		if (!kitchenAreaRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			kitchenAreaRepository.deleteById(id);
		} catch (Exception e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<KitchenAreaDto> readDataByIsActive() {
		List<KitchenAreaModel> kitchenAreaModel = kitchenAreaRepository.findAllByIsActiveOrderByIdAsc(true);
		return modelMapperService.convertListEntityAndListDto(kitchenAreaModel, KitchenAreaDto.class);
	}

	/**
	 * Constructs an ExampleMatcher configured for matching criteria based on specified fields.
	 * The matcher is set to perform a case-insensitive contains operation on specific language-related fields
	 * and to ignore specified audit-related fields.
	 *
	 * @return an ExampleMatcher instance with the desired configurations for field matching.
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