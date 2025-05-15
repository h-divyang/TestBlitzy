package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Optional;
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
import com.catering.dto.tenant.request.TaxMasterDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.TaxMasterModel;
import com.catering.repository.tenant.TaxMasterRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.TaxMasterService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This class provides the implementation of the TaxMasterService interface.
 * It extends the GenericServiceImpl class with TaxMasterDto, TaxMasterModel, and Long as the generic types.
 * It handles the CRUD operations for TaxMasterDto objects.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 *
 * @author Neel Bhanderi
 * @since March 2024
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaxMasterServiceImpl extends GenericServiceImpl<TaxMasterDto, TaxMasterModel, Long> implements TaxMasterService {

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
	 * Repository for managing tax master data.
	 */
	TaxMasterRepository taxMasterRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<TaxMasterDto> createAndUpdate(TaxMasterDto taxMasterDto) {
		ValidationUtils.validateFields(taxMasterRepository, exceptionService, messageService, taxMasterDto.getNameDefaultLang(), taxMasterDto.getNamePreferLang(), taxMasterDto.getNameSupportiveLang(), taxMasterDto.getId());
		TaxMasterModel taxMasterModel = modelMapperService.convertEntityAndDto(taxMasterDto, TaxMasterModel.class);
		DataUtils.setAuditFields(taxMasterRepository, taxMasterDto.getId(), taxMasterModel);
		return Optional.of(modelMapperService.convertEntityAndDto(taxMasterRepository.save(taxMasterModel), TaxMasterDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<TaxMasterDto>> read(FilterDto filterDto) {
		Optional<Example<TaxMasterModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			TaxMasterModel taxMasterModel = TaxMasterModel.ofSearchingModel(query);
			taxMasterModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(taxMasterModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(TaxMasterDto.class, TaxMasterModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) throws RestException {
		if (!taxMasterRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			taxMasterRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TaxMasterDto> findAllByIsActiveTrue() {
		return modelMapperService.convertListEntityAndListDto(taxMasterRepository.findAllByIsActiveTrue(), TaxMasterDto.class);
	}

	/**
	 * Creates and returns an ExampleMatcher object for performing queries on TaxMasterModel objects.
	 *
	 * @return The ExampleMatcher object for TaxMasterModel queries.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.TAX_MASTER_FIELD_TAX, matcher -> matcher.transform(value -> value).contains())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsName()));
	}

}