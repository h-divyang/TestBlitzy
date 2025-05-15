package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.catering.dto.tenant.request.LabourShiftDto;
import com.catering.model.tenant.LabourShiftModel;
import com.catering.repository.tenant.LabourShiftRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CompanySettingService;
import com.catering.service.tenant.LabourShiftService;
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
 * Implementation of the LabourShiftService interface, providing methods for managing labour shifts.
 * This class handles the creation, updating, retrieval, and deletion of labour shift records.
 * It utilizes the GenericServiceImpl for common CRUD operations and adds business-specific functionality.
 *
 * This service relies on repositories, utilities, and other supporting services like MessageService,
 * ExceptionService, and ModelMapperService to execute its operations, while adhering to the application's
 * validation and exception handling mechanisms.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LabourShiftServiceImpl extends GenericServiceImpl<LabourShiftDto, LabourShiftModel, Long> implements LabourShiftService {

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
	 * Repository for managing Labour Shift entities.
	 */
	LabourShiftRepository labourShiftRepository;

	/**
	 * Service for managing company settings.
	 */
	CompanySettingService companySettingService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<LabourShiftDto> createAndUpdate(LabourShiftDto labourShiftDto) {
		ValidationUtils.validateFields(labourShiftDto, labourShiftRepository, exceptionService, messageService);
		LabourShiftModel labourShiftModel = modelMapperService.convertEntityAndDto(labourShiftDto, LabourShiftModel.class);
		DataUtils.setAuditFields(labourShiftRepository, labourShiftDto.getId(), labourShiftModel);
		return Optional.of(modelMapperService.convertEntityAndDto(labourShiftRepository.save(labourShiftModel), LabourShiftDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<LabourShiftDto>> read(FilterDto filterDto) {
		Optional<Example<LabourShiftModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			CompanySettingDto companyDetails = companySettingService.getCompannySetting(false);
			LabourShiftModel labourShiftModel = LabourShiftModel.ofSearchingModel(query);
			String formattedTime = companyDetails.getIs24Hour() ? com.catering.util.StringUtils.convert24HourTimeToUTC(query, companyDetails.getTimeZone()) : com.catering.util.StringUtils.convert12HourTimeToUTC(query, companyDetails.getTimeZone());
			labourShiftModel.setTimeString(formattedTime);
			example = Optional.of(Example.of(labourShiftModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		if ("time".equalsIgnoreCase(filterDto.getSortBy())) { // Check if the sorting is based on 'time'
			CompanySettingDto companyDetails = companySettingService.getCompannySetting(false);
			// Convert the user query time string to UTC format based on 12/24-hour setting
			String formattedTime = companyDetails.getIs24Hour() ? com.catering.util.StringUtils.convert24HourTimeToUTC(query, companyDetails.getTimeZone()) : com.catering.util.StringUtils.convert12HourTimeToUTC(query, companyDetails.getTimeZone());
			// Build pageable request object for pagination
			Pageable pageable = PagingUtils.buildPageRequestFromFilterDto(filterDto);
			// Prepare the search value with wildcards for SQL LIKE clause
			String searchValue = (filterDto.getQuery() == null || filterDto.getQuery().isBlank()) ? null : "%" + (formattedTime != null ? formattedTime.toLowerCase() : filterDto.getQuery().toLowerCase()) + "%";
			boolean hasSearch = searchValue != null;
			Optional<Page<LabourShiftModel>> pages = Optional.empty();
			// Determine sorting direction and fetch data accordingly
			if ("desc".equals(filterDto.getSortDirection().toLowerCase())) {
				pages = hasSearch ? labourShiftRepository.findAllSortedByCompanyTimeDESCWithExample(searchValue, pageable, formattedTime != null) : labourShiftRepository.findAllSortedByCompanyTimeDESC(pageable);
			} else {
				pages = hasSearch ? labourShiftRepository.findAllSortedByCompanyTimeASCWithExample(searchValue, pageable, formattedTime != null) : labourShiftRepository.findAllSortedByCompanyTimeASC(pageable);
			}
			Optional<Paging> paging = PagingUtils.getPaging(pages.get());
			// Convert entity list to DTO list and return response with optional paging
			return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(pages.get().getContent(), LabourShiftDto.class)).setCustomPaging(!pages.get().getContent().isEmpty() ? paging.get() : Paging.builder().build());
		}
		return read(LabourShiftDto.class, LabourShiftModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<LabourShiftDto> findByIsActiveTrue() {
		return modelMapperService.convertListEntityAndListDto(labourShiftRepository.findByIsActiveTrue(), LabourShiftDto.class);
	}

	/**
	 * Deletes a labour shift entity by its ID. If the entity does not exist, throws a bad request exception.
	 * Additionally, handles scenarios where the entity is in use and cannot be deleted.
	 *
	 * @param id The unique identifier of the labour shift entity to delete.
	 * @throws BadRequestException If the entity does not exist or is in use.
	 */
	@Override
	public void deleteById(Long id) {
		if (!labourShiftRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			labourShiftRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * Configures and returns a customized {@link ExampleMatcher} instance for matching.
	 *
	 * The method sets up matchers for specific fields to perform case-insensitive and partial matching.
	 * Additionally, certain paths (typically audit fields) are ignored during the matching process.
	 *
	 * @return An {@link ExampleMatcher} instance configured with field matchers and ignored paths.
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