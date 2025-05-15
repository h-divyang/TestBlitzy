package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.catering.bean.Paging;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.FunctionTypeDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.FunctionTypeModel;
import com.catering.repository.tenant.FunctionTypeRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.BookOrderService;
import com.catering.service.tenant.CompanySettingService;
import com.catering.service.tenant.FunctionTypeService;
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
 * This class provides the implementation of the FunctionTypeService interface.
 * It extends the GenericServiceImpl class with FunctionTypeDto, FunctionTypeModel, and Long as the generic types.
 * It handles the CRUD operations for FunctionTypeDto objects.
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
public class FunctionTypeServiceImpl extends GenericServiceImpl<FunctionTypeDto, FunctionTypeModel, Long> implements FunctionTypeService {

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
	 * Repository for managing Function Type entities.
	 */
	FunctionTypeRepository functionTypeRepository;

	/**
	 * Service for handling operations related to Book Orders.
	 */
	BookOrderService orderService;

	/**
	 * Service for managing company settings.
	 */
	CompanySettingService companySettingService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<FunctionTypeDto> createAndUpdate(FunctionTypeDto functionTypeDto) throws RestException {
		ValidationUtils.validateFields(functionTypeDto, functionTypeRepository, exceptionService, messageService);
		FunctionTypeModel functionTypeModel = modelMapperService.convertEntityAndDto(functionTypeDto, FunctionTypeModel.class);
		DataUtils.setAuditFields(functionTypeRepository, functionTypeDto.getId(), functionTypeModel);
		return Optional.of(modelMapperService.convertEntityAndDto(functionTypeRepository.save(functionTypeModel), FunctionTypeDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<FunctionTypeDto>> read(FilterDto filterDto) {
		Optional<Example<FunctionTypeModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			CompanySettingDto companyDetails = companySettingService.getCompannySetting(false);
			FunctionTypeModel functionTypeModel = FunctionTypeModel.ofSearchingModel(query);
			String formattedTime = companyDetails.getIs24Hour() ? com.catering.util.StringUtils.convert24HourTimeToUTC(query, companyDetails.getTimeZone()) : com.catering.util.StringUtils.convert12HourTimeToUTC(query, companyDetails.getTimeZone());
			functionTypeModel.setTimeString(formattedTime);
			functionTypeModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(functionTypeModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		if ("time".equalsIgnoreCase(filterDto.getSortBy())) {
			CompanySettingDto companyDetails = companySettingService.getCompannySetting(false);
			// Convert the query time string to UTC format based on time format and timezone
			String formattedTime = companyDetails.getIs24Hour() ? com.catering.util.StringUtils.convert24HourTimeToUTC(query, companyDetails.getTimeZone()) : com.catering.util.StringUtils.convert12HourTimeToUTC(query, companyDetails.getTimeZone());
			// Build pageable object based on the filterDto's pagination values
			Pageable pageable = PagingUtils.buildPageRequestFromFilterDto(filterDto);
			// Prepare search value: if the query is not blank, wrap it with wildcards for LIKE search
			String searchValue = (filterDto.getQuery() == null || filterDto.getQuery().isBlank()) ? null : "%" + (formattedTime != null ? formattedTime.toLowerCase() : filterDto.getQuery().toLowerCase()) + "%";
			boolean hasSearch = searchValue != null;
			Optional<Page<FunctionTypeModel>> pages = Optional.empty();
			// Apply appropriate repository method based on sort direction and whether search is applied
			if ("desc".equals(filterDto.getSortDirection().toLowerCase())) {
				pages = hasSearch ? functionTypeRepository.findAllSortedByCompanyTimeDESCWithExample(searchValue, pageable, formattedTime != null) : functionTypeRepository.findAllSortedByCompanyTimeDESC(pageable);
			} else {
				pages = hasSearch ? functionTypeRepository.findAllSortedByCompanyTimeASCWithExample(searchValue, pageable, formattedTime != null) : functionTypeRepository.findAllSortedByCompanyTimeASC(pageable);
			}
			Optional<Paging> paging = PagingUtils.getPaging(pages.get());
			// Convert entities to DTOs and construct final response with optional paging
			return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(pages.get().getContent(), FunctionTypeDto.class)).setCustomPaging(!pages.get().getContent().isEmpty() ? paging.get() : Paging.builder().build());
		}
		return read(FunctionTypeDto.class, FunctionTypeModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		if (!functionTypeRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		if (orderService.existsOrderFunctionByFunctionTypeId(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		functionTypeRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FunctionTypeDto> findByIsActiveTrue() {
		return modelMapperService.convertListEntityAndListDto(functionTypeRepository.findByIsActiveTrue(), FunctionTypeDto.class);
	}

	/**
	 * Creates and returns an ExampleMatcher object for performing queries on FunctionTypeModel objects.
	 * 
	 * @return The ExampleMatcher object for FunctionTypeModel queries.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_FUNCTION_TYPE_TIME, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsName()));
	}

}