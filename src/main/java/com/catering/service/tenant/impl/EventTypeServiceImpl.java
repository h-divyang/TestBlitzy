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
import com.catering.dto.tenant.request.EventTypeDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.EventTypeModel;
import com.catering.repository.tenant.EventTypeRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.EventTypeService;
import com.catering.service.tenant.BookOrderService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link EventTypeService} interface for managing EventType entities and their corresponding DTOs.
 * Extends {@link GenericServiceImpl} to inherit basic CRUD operations and provides additional functionalities
 * specific to EventType entities. Includes validations, custom filtering, and mapping between entity and DTO layers.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventTypeServiceImpl extends GenericServiceImpl<EventTypeDto, EventTypeModel, Long> implements EventTypeService {

	/**
	 * Service for managing and retrieving localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling and throwing application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for mapping between DTOs and entities.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing Event Type entities.
	 */
	EventTypeRepository eventTypeRepository;

	/**
	 * Service for handling operations related to Book Orders.
	 */
	BookOrderService orderService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<EventTypeDto> createAndUpdate(EventTypeDto eventTypeDto) throws RestException {
		ValidationUtils.validateFields(eventTypeDto, eventTypeRepository, exceptionService, messageService);
		EventTypeModel eventTypeModel = modelMapperService.convertEntityAndDto(eventTypeDto, EventTypeModel.class);
		DataUtils.setAuditFields(eventTypeRepository, eventTypeDto.getId(), eventTypeModel);
		return Optional.of(modelMapperService.convertEntityAndDto(eventTypeRepository.save(eventTypeModel), EventTypeDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<EventTypeDto>> read(FilterDto filterDto) {
		Optional<Example<EventTypeModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			EventTypeModel eventTypeModel = EventTypeModel.ofSearchingModel(query);
			eventTypeModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(eventTypeModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(EventTypeDto.class, EventTypeModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		if (!eventTypeRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		if (orderService.existsByEventTypeId(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		eventTypeRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EventTypeDto> findByIsActiveTrue() {
		return modelMapperService.convertListEntityAndListDto(eventTypeRepository.findByIsActiveTrue(), EventTypeDto.class);
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