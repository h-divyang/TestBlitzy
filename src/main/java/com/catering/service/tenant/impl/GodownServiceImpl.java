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
import com.catering.dto.tenant.request.GodownDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.GodownModel;
import com.catering.repository.tenant.GodownRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.GodownService;
import com.catering.service.tenant.OrderCrockeryService;
import com.catering.service.tenant.OrderGeneralFixRawMaterialService;
import com.catering.service.tenant.OrderLabourDistributionService;
import com.catering.service.tenant.RawMaterialAllocationService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link GodownService} interface for managing Godown operations.
 * Extends the {@link GenericServiceImpl} to reuse common CRUD functionalities.
 * This service provides methods for creating, reading, updating, and deleting Godown entities.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GodownServiceImpl extends GenericServiceImpl<GodownDto, GodownModel, Long> implements GodownService {

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
	 * Repository for managing Godown entities.
	 */
	GodownRepository godownRepository;

	/**
	 * Service for managing operations related to general fixed raw materials in orders.
	 */
	OrderGeneralFixRawMaterialService orderGeneralFixRawMaterialService;

	/**
	 * Service for managing operations related to crockery orders.
	 */
	OrderCrockeryService orderCrockeryService;

	/**
	 * Service for managing raw material allocation operations.
	 */
	RawMaterialAllocationService rawMaterialAllocationService;

	/**
	 * Service for managing labour distribution in orders.
	 */
	OrderLabourDistributionService orderLabourService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<GodownDto> createAndUpdate(GodownDto godownDto) throws RestException {
		ValidationUtils.validateFields(godownDto, godownRepository, exceptionService, messageService);
		GodownModel godownModel = modelMapperService.convertEntityAndDto(godownDto, GodownModel.class);
		DataUtils.setAuditFields(godownRepository, godownDto.getId(), godownModel);
		return Optional.of(modelMapperService.convertEntityAndDto(godownRepository.save(godownModel), GodownDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<GodownDto>> read(FilterDto filterDto) {
		Optional<Example<GodownModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			GodownModel godownModel = GodownModel.ofSearchingModel(query);
			godownModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(godownModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(GodownDto.class, GodownModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		if (!godownRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		if (orderGeneralFixRawMaterialService.existsByGodownId(id) || orderCrockeryService.existsByGodownId(id) || orderLabourService.existsByGodownId(id) || rawMaterialAllocationService.existsByGodownId(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		godownRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GodownDto> findByIsActiveTrue() {
		return modelMapperService.convertListEntityAndListDto(godownRepository.findByIsActiveTrue(), GodownDto.class);
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
			.withMatcher(FieldConstants.ADDRESS_FIELD_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.ADDRESS_FIELD_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.ADDRESS_FIELD_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsName()));
	}

}