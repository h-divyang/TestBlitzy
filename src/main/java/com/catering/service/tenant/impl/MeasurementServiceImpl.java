package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordExistDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.CompanyPreferencesDto;
import com.catering.dto.tenant.request.CustomRangeDto;
import com.catering.dto.tenant.request.MeasurementDto;
import com.catering.dto.tenant.request.MeasurementWithCustomRangeDto;
import com.catering.exception.RestException;
import com.catering.model.CustomRangeModel;
import com.catering.model.tenant.MeasurementModel;
import com.catering.repository.tenant.CustomRangeRepository;
import com.catering.repository.tenant.MeasurementRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CompanyPreferencesService;
import com.catering.service.tenant.MeasurementService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing Measurement entities and operations.
 * Extends {@link GenericServiceImpl} and implements {@link MeasurementService}.
 * This service facilitates creating, updating, deleting, and retrieving Measurement data,
 * with validation and business logic applied.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MeasurementServiceImpl extends GenericServiceImpl<MeasurementDto, MeasurementModel, Long> implements MeasurementService {

	/**
	 * Service for converting between DTOs and entity models.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for managing company preferences and settings.
	 */
	CompanyPreferencesService companyPreferencesService;

	/**
	 * Service for retrieving and managing localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Repository for managing Measurement entities.
	 */
	MeasurementRepository measurementRepository;

	/**
	 * Repository for handling database operations related to custom range
	 */
	CustomRangeRepository customRangeRepository;

	/**
	 * Constant representing the label for Measurement.
	 */
	String MEASURMENT = "Measurment";

	/**
	 * Constant representing the Measurement ID prefix used in messages or logs.
	 */
	String MEASURMENT_ID = MEASURMENT + " ID: ";

	/**
	 * Injects an EntityManager instance, which is used to interact with the persistence context (i.e., the database).
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MeasurementWithCustomRangeDto> createAndUpdate(MeasurementWithCustomRangeDto measurementDto) throws RestException {
		if(measurementDto.getId() != null) {
			Optional<MeasurementModel> existingMeasurementModel = measurementRepository.findById(measurementDto.getId());
			if (existingMeasurementModel.isPresent() && !Objects.equals(measurementDto.getIsActive(), existingMeasurementModel.get().getIsActive())) {
				List<String> tablesWithMeasurementColumn = measurementRepository.findTableNamesWithMeasurementColumn();
				for (String table : tablesWithMeasurementColumn) {
					if (isMeasurementIdExistsInTable(table, measurementDto.getId())) {
						exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(true).build());
						return Optional.empty();
					}
				}
			}
		}
		measurementValidation(measurementDto);
		validateUniqueNamesAndSymbols(measurementDto);
		MeasurementModel measurementModel = modelMapperService.convertEntityAndDto(measurementDto, MeasurementModel.class);
		// Check if adjustType is null or 3 to delete custom ranges
		if (measurementModel.getAdjustType() == null || measurementModel.getAdjustType() == 3) {
			// If the adjustType is null or 3, remove custom ranges
			measurementModel.setCustomRange(new ArrayList<>());
		} else if (measurementDto.getCustomRange() != null && !measurementDto.getCustomRange().isEmpty()) {
			// If adjustType is not null or 3, then map and add custom ranges
			List<CustomRangeModel> customRangeModels = measurementDto.getCustomRange().stream().map(customRangeDto -> {
				CustomRangeModel customRangeModel = modelMapperService.convertEntityAndDto(customRangeDto, CustomRangeModel.class);
				customRangeModel.setMeasurement(measurementModel); // Link the customRange to the parent measurement
				return customRangeModel;
			}).collect(Collectors.toList());
			// Set the custom ranges to the measurement model
			measurementModel.setCustomRange(customRangeModels);
		}
		DataUtils.setAuditFields(measurementRepository, measurementDto.getId(), measurementModel);
		if (measurementModel.getAdjustType() == null ||  measurementModel.getAdjustType() != 3) {
			measurementModel.setStepWiseRange(null);
		}
		return Optional.of(modelMapperService.convertEntityAndDto(measurementRepository.save(measurementModel), MeasurementWithCustomRangeDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void existByIdOrThrow(Long id) throws RestException {
		if (Objects.isNull(id) || !measurementRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST_BY_ID, MEASURMENT_ID));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<MeasurementDto>> read(FilterDto filterDto) {
		Optional<Example<MeasurementModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			MeasurementModel measurementModel = MeasurementModel.ofSearchingModel(query);
			measurementModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(measurementModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection())); 
		return read(MeasurementDto.class, MeasurementModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MeasurementDto> getAllByIsBaseUnitTrue() {
		 List<MeasurementModel> findByIsBaseUnitTrue = measurementRepository.findRecordsNotUsedAsParentUnit();
		return modelMapperService.convertListEntityAndListDto(findByIsBaseUnitTrue, MeasurementDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) throws RestException {
		if (!measurementRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			measurementRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MeasurementDto> readDataByIsActive() {
		List<MeasurementModel> measurementModel = measurementRepository.findAllByIsActiveOrderByIdAsc(true);
		return modelMapperService.convertListEntityAndListDto(measurementModel, MeasurementDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CustomRangeDto> getCustomRangeByMeasurementId(Long measurementId) {
		List<CustomRangeModel> customRangeModel = customRangeRepository.findByMeasurementId(measurementId);
		return modelMapperService.convertListEntityAndListDto(customRangeModel, CustomRangeDto.class); 
	}

	/**
	 * Creates and returns an instance of ExampleMatcher configured with specific matchers and ignored paths.
	 * The matchers are designed to handle case-insensitive partial matching for specified fields.
	 * Certain fields related to auditing and configuration are excluded from matching.
	 *
	 * @return An ExampleMatcher instance with predefined property matchers and ignored paths.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MEASUREMENT_SYMBOL_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MEASUREMENT_SYMBOL_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MEASUREMENT_SYMBOL_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MEASUREMENT_IS_BASE_UNIT, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MEASUREMENT_BASE_EQUIVALENT, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), ArrayUtils.buildArray(FieldConstants.MEASUREMENT +"."+ FieldConstants.IS_ACTIVE, FieldConstants.MEASUREMENT +"."+ FieldConstants.EDIT_COUNT)));
	}

	/**
	 * Validates the provided {@link MeasurementDto} object to ensure it complies with certain business rules.
	 * The validation checks include conditions on the base unit, equivalent values, and related identifiers.
	 * If any validation fails, a {@link RestException} is thrown with the corresponding error details.
	 *
	 * @param measurementDto The {@link MeasurementDto} object to validate.
	 * @throws RestException If validation fails, containing details about the validation errors.
	 */
	private void measurementValidation(MeasurementDto measurementDto) throws RestException {
		Optional<CompanyPreferencesDto> companyPreferencesDtoOptional = companyPreferencesService.find();
		if (companyPreferencesDtoOptional.isPresent()) {
			Map<String, String> errors = new HashMap<>();
			boolean isBaseUnit = measurementDto.getIsBaseUnit();
			if (isBaseUnit && Objects.nonNull(measurementDto.getBaseUnitEquivalent())) {
				errors.put(FieldConstants.MEASUREMENT_BASE_EQUIVALENT,  messageService.getMessage(MessagesConstant.MEASUREMENT_BASE_UNIT_EQUIVALENT_NOT_VALID));
			}
			if (isBaseUnit && Objects.nonNull(measurementDto.getBaseUnitId())) {
				errors.put(FieldConstants.MEASUREMENT_BASE_UNIT_ID ,  messageService.getMessage(MessagesConstant.MEASUREMENT_BASE_UNIT_ID_NOT_VALID));
			}
			if (measurementRepository.count() == 0 && !isBaseUnit) {
				errors.put(FieldConstants.MEASUREMENT_IS_BASE_UNIT, messageService.getMessage(MessagesConstant.MEASUREMENT_IS_BASE_UNIT));
			}
			if (!isBaseUnit && Objects.isNull(measurementDto.getBaseUnitEquivalent())) {
				errors.put(FieldConstants.MEASUREMENT_BASE_EQUIVALENT,messageService.getMessage(MessagesConstant.MEASUREMENT_BASE_UNIT_EQUIVALENT_BLANK));
			}
			if (!isBaseUnit && Objects.isNull(measurementDto.getBaseUnitId())) {
				errors.put(FieldConstants.MEASUREMENT_BASE_UNIT_ID ,messageService.getMessage(MessagesConstant.MEASUREMENT_BASE_UNIT));
			}
			if (!errors.isEmpty()) {
				exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_MANDATORY_FIELD_NOT_BLANK), errors);
			}
		}
	}

	/**
	 * Validates that the names and symbols provided in the {@link MeasurementDto} object are unique.
	 * It checks for existing records with the same default, preferred, or supportive language names and symbols
	 * in the database. If duplicates are detected, a {@link RestException} is thrown containing relevant error details.
	 *
	 * @param measurementDto The {@link MeasurementDto} object containing name and symbol details to be validated.
	 * @throws RestException If a name or symbol conflict is found, an exception is thrown with detailed information.
	 */
	private void validateUniqueNamesAndSymbols(MeasurementDto measurementDto) throws RestException {
		boolean isDefaultExist = ValidationUtils.materialNameExists(measurementRepository, measurementDto.getNameDefaultLang(), Constants.LANGUAGE_DEFAULT, measurementDto.getId());
		boolean isPreferExist = measurementDto.getNamePreferLang() != null && ValidationUtils.materialNameExists(measurementRepository, measurementDto.getNamePreferLang(), Constants.LANGUAGE_PREFER, measurementDto.getId());
		boolean isSupportiveExist = measurementDto.getNameSupportiveLang() != null && ValidationUtils.materialNameExists(measurementRepository, measurementDto.getNameSupportiveLang(), Constants.LANGUAGE_SUPPORTIVE, measurementDto.getId());

		boolean isSymbolDefaultExist = materialSymbolNameExists(measurementDto.getSymbolDefaultLang(), Constants.LANGUAGE_DEFAULT, measurementDto.getId());
		boolean isSymbolPreferExist = measurementDto.getSymbolPreferLang() != null && materialSymbolNameExists(measurementDto.getSymbolPreferLang(), Constants.LANGUAGE_PREFER, measurementDto.getId());
		boolean isSymbolSupportiveExist = measurementDto.getSymbolSupportiveLang() != null && materialSymbolNameExists(measurementDto.getSymbolSupportiveLang(), Constants.LANGUAGE_SUPPORTIVE, measurementDto.getId());

		boolean isExist = isDefaultExist || isPreferExist || isSupportiveExist;
		boolean isExistSymbol = isSymbolDefaultExist || isSymbolPreferExist || isSymbolSupportiveExist;

		if (isExist || isExistSymbol) {
			RecordExistDto recordExistDto = RecordExistDto.builder()
					.isExist(isExist)
					.isNameDefaultLang(isDefaultExist)
					.isNamePreferLang(isPreferExist)
					.isNameSupportiveLang(isSupportiveExist)
					.isExistSymbol(isExistSymbol)
					.isSymbolDefaultLang(isSymbolDefaultExist)
					.isSymbolPreferLang(isSymbolPreferExist)
					.isSymbolSupportiveLang(isSymbolSupportiveExist)
					.build();
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.ALREADY_EXIST), recordExistDto);
		}
	}

	/**
	 * Checks if a material symbol name exists in the database for a given language and optionally excludes a specific record by its ID.
	 * The method searches based on the specified language type: default, preferred, or supportive, and determines whether a symbol name already exists.
	 *
	 * @param name The symbol name to check for existence.
	 * @param language The language category to search in (e.g., default, preferred, or supportive).
	 * @param id The ID of the record to exclude from the search. If null, all records are considered.
	 * @return {@code true} If the symbol name exists in the specified language (excluding the given ID if provided), otherwise {@code false}.
	 */
	private boolean materialSymbolNameExists(String name, String language, Long id) {
		switch (language) {
			case Constants.LANGUAGE_DEFAULT:
				return id != null ? measurementRepository.existsBySymbolDefaultLangIgnoreCaseAndIdNot(name, id) : measurementRepository.existsBySymbolDefaultLangIgnoreCase(name);
			case Constants.LANGUAGE_PREFER:
				return id != null ? measurementRepository.existsBySymbolPreferLangIgnoreCaseAndIdNot(name, id) : measurementRepository.existsBySymbolPreferLangIgnoreCase(name);
			case Constants.LANGUAGE_SUPPORTIVE:
				return id != null ? measurementRepository.existsBySymbolSupportiveLangIgnoreCaseAndIdNot(name, id) : measurementRepository.existsBySymbolSupportiveLangIgnoreCase(name);
			default:
				return false; // Handle unsupported symbol
		}
	}

	/**
	 * Checks if a given measurement ID exists in the specified table.
	 *
	 * @param tableName the name of the table to search for the measurement ID
	 * @param measurementId the ID of the measurement to check for
	 * @return true if the measurement ID exists in the table, false otherwise
	 */
	public boolean isMeasurementIdExistsInTable(String tableName, Long measurementId) {
		try {
			String query = "SELECT COUNT(*) FROM " + tableName + " WHERE fk_measurement_id = :measurementId";
			return ((Number) entityManager.createNativeQuery(query).setParameter("measurementId", measurementId).getSingleResult()).longValue() > 0;
		} catch (Exception e) {
			return false;
		}
	}

}