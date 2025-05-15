package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.common.SearchFieldDto;
import com.catering.dto.tenant.request.AllRawMaterialSupplierDto;
import com.catering.dto.tenant.request.NameDto;
import com.catering.dto.tenant.request.RawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialResponseDto;
import com.catering.dto.tenant.request.RawMaterialSupplierDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MeasurementModel;
import com.catering.model.tenant.RawMaterialCategoryModel;
import com.catering.model.tenant.RawMaterialModel;
import com.catering.model.tenant.RawMaterialSupplierModel;
import com.catering.repository.tenant.CompanyPreferencesRepository;
import com.catering.repository.tenant.RawMaterialRepository;
import com.catering.repository.tenant.RawMaterialSupplierRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.MeasurementService;
import com.catering.service.tenant.RawMaterialCategoryService;
import com.catering.service.tenant.RawMaterialService;
import com.catering.specification.GenericSpecificationService;
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
 * Implementation of the RawMaterialService interface. This class provides methods for managing raw materials, including
 * creating and updating raw materials, performing validation, managing associations with suppliers, and performing CRUD operations.
 * It extends the GenericServiceImpl to leverage common service functionalities.
 *
 * The class integrates multiple auxiliary services such as exception handling, model mapping, and repository operations.
 * It also handles specific functionalities related to raw material categories and measurements.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RawMaterialServiceImpl extends GenericServiceImpl<RawMaterialDto, RawMaterialModel, Long> implements RawMaterialService {

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
	 * Service for managing raw material categories.
	 */
	RawMaterialCategoryService rawMaterialCategoryService;

	/**
	 * Service for managing measurements.
	 */
	MeasurementService measurementService;

	/**
	 * Repository for managing raw material suppliers.
	 */
	RawMaterialSupplierRepository rawMaterialSupplierRepository;

	/**
	 * Repository for managing raw materials.
	 */
	RawMaterialRepository rawMaterialRepository;

	/**
	 * Service for managing generic specification
	 */
	GenericSpecificationService genericSpecificationService;

	/**
	 * Service for managing company-specific preferences.
	 */
	CompanyPreferencesRepository companyPreferencesRepository;

	/**
	 * Constant representing the raw material name.
	 */
	String RAW_MATERIAL = "Raw Material";

	/**
	 * Constant for the raw material ID, derived from the raw material name.
	 */
	String RAW_MATERIAL_ID = RAW_MATERIAL + " ID";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<RawMaterialDto> createAndUpdate(RawMaterialDto rawMaterialDto) throws RestException {
		rawMaterialValidation(rawMaterialDto);
		RawMaterialModel rawMaterialModel = modelMapperService.convertEntityAndDto(rawMaterialDto, RawMaterialModel.class);
		DataUtils.setAuditFields(rawMaterialRepository, rawMaterialDto.getId(), rawMaterialModel);
		rawMaterialModel = rawMaterialRepository.save(rawMaterialModel);
		// Save temporary contractor list 
		List<RawMaterialSupplierModel> rawMaterialSupplierModels = new ArrayList<>();
		RawMaterialSupplierModel contractorModel = null;
		if (rawMaterialDto.getRawMaterialSupplierList() != null && !rawMaterialDto.getRawMaterialSupplierList().isEmpty()) {
			for (RawMaterialSupplierDto contractorDto : rawMaterialDto.getRawMaterialSupplierList()) {
				contractorModel = modelMapperService.convertEntityAndDto(contractorDto, RawMaterialSupplierModel.class);
				contractorModel.setRawMaterial(rawMaterialModel);
				DataUtils.setAuditFields(rawMaterialSupplierRepository, contractorDto.getId(), contractorModel);
				rawMaterialSupplierModels.add(contractorModel);
			}
			if (!rawMaterialSupplierModels.isEmpty()) {
				rawMaterialSupplierRepository.saveAll(rawMaterialSupplierModels);
			}
		}
		return Optional.of(modelMapperService.convertEntityAndDto(rawMaterialModel, RawMaterialDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void existByIdOrThrow(Long id) throws RestException {
		if (Objects.isNull(id) || !rawMaterialRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST_BY_ID, RAW_MATERIAL_ID));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<RawMaterialDto>> read(FilterDto filterDto, SearchFieldDto searchFieldDto) {
		Optional<Example<RawMaterialModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		if (StringUtils.isNotBlank(query) && searchFieldDto.getFieldValue() == -1) { // CASE 1: Perform free-text search using ExampleMatcher if query is present and no category filter is applied
			RawMaterialModel rawMaterialModel = RawMaterialModel.ofSearchingModel(query);
			rawMaterialModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			rawMaterialModel.setRawMaterialCategory(RawMaterialCategoryModel.ofSearchingModel(query));
			rawMaterialModel.setMeasurement(MeasurementModel.ofSearchingModel(query));
			example = Optional.of(Example.of(rawMaterialModel, getExampleMatcher()));
			return read(RawMaterialDto.class, RawMaterialModel.class, filterDto, example);
		} else if (searchFieldDto.getFieldName().equals(FieldConstants.CATEGORY) && searchFieldDto.getFieldValue() != -1 && Objects.nonNull(searchFieldDto.getFieldValue())) { // CASE 2: Apply category-based filtering using Specifications
			// Build a specification based on filter DTOs
			Specification<RawMaterialModel> spec = buildSpecification(filterDto, searchFieldDto);
			return genericSpecificationService.readWithSpecification(RawMaterialDto.class, RawMaterialModel.class, filterDto, spec, this::mapToDto);
		} else { // CASE 3: Fallback — read everything without any filters
			return read(RawMaterialDto.class, RawMaterialModel.class, filterDto, example);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteById(Long id) throws RestException {
		if (!rawMaterialRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			Integer result = rawMaterialRepository.checkRawMaterialUsage(id);
			if (result != null && result == 0) {
				Long idOfSubscription = Objects.nonNull(companyPreferencesRepository.getById(1l).getSubscriptionId()) ? companyPreferencesRepository.getById(1l).getSubscriptionId() : 0;
				if (idOfSubscription > 1) {
					rawMaterialRepository.deleteRawMaterialFromStock(id);
					rawMaterialRepository.deleteRawMaterialFromStockHistory(id);
				}
				rawMaterialSupplierRepository.deleteByRawMaterial_Id(id);
				rawMaterialRepository.deleteById(id);
			} else {
				throw new DataIntegrityViolationException(messageService.getMessage(MessagesConstant.IN_USE));
			}
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 * See {@link RawMaterialService#uniqueList(Class, Class, List, Long, Long)} for details.
	 */
	@Override
	public ResponseContainerDto<List<RawMaterialResponseDto>> uniqueList(Class<RawMaterialResponseDto> dtoType, Class<RawMaterialModel> modelType, List<Long> IDs, Long menuItemId, Long userEditId) {
		List<RawMaterialModel> rawMaterialModelList = new ArrayList<>();
		// Temporary data and based on selected id
		if (menuItemId != null && menuItemId != 0) {
			rawMaterialModelList = rawMaterialRepository.findRawMaterialUsingMenuItemRawMaterial(menuItemId, userEditId);
		} else if(!IDs.isEmpty()) { // Edit data 
			rawMaterialModelList = rawMaterialRepository.findAllByIsActiveTrueAndIdNotIn(IDs);
		} else { // New data
			rawMaterialModelList = rawMaterialRepository.findAllByIsActiveTrue();
		}
		return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(rawMaterialModelList, dtoType));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AllRawMaterialSupplierDto> findRawMaterialDetails() {
		return rawMaterialRepository.findRawMaterialDetails();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<NameDto> findByMenuItemId(Long menuItemId) {
		return rawMaterialRepository.findByMenuItemId(menuItemId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RawMaterialDto> findAll() {
		return modelMapperService.convertListEntityAndListDto(rawMaterialRepository.findAll(), RawMaterialDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isExists(Long id) {
		return rawMaterialRepository.isExists(id);
	}

	/**
	 * Validates the raw material data contained in the provided {@code RawMaterialDto}.
	 * Ensures that the associated raw material category and measurement exist,
	 * and performs duplicate field validation using the multi-language fields in the repository.
	 *
	 * @param rawMaterialDto The data transfer object containing raw material details.
	 * @throws RestException If the raw material category or measurement does not exist, or if duplicate records are detected.
	 */
	private void rawMaterialValidation(RawMaterialDto rawMaterialDto) throws RestException {
		rawMaterialCategoryService.existByIdOrThrow(rawMaterialDto.getRawMaterialCategory().getId());
		measurementService.existByIdOrThrow(rawMaterialDto.getMeasurement().getId());
		// Check for duplicate records with multiLanguage fields and throw an exception if any are found.
		ValidationUtils.validateFields(rawMaterialDto, rawMaterialRepository, exceptionService, messageService);
	}

	/**
	 * Creates and returns an ExampleMatcher instance configured to perform
	 * case-insensitive partial matches on specific fields. This matcher is tailored
	 * to handle multi-language fields and nested properties in the context of raw materials.
	 * Additionally, it ignores predefined audit fields and measurement-related fields.
	 *
	 * @return An ExampleMatcher object configured for advanced matching scenarios.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.RAW_MATERIAL_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.RAW_MATERIAL_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.RAW_MATERIAL_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MEASUREMENT + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MEASUREMENT + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.MEASUREMENT + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(
					BeanUtils.getAuditFieldsName(),
					BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.RAW_MATERIAL_CATEGORY),
					BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.MEASUREMENT),
					ArrayUtils.buildArray(FieldConstants.MEASUREMENT + "." + FieldConstants.MEASUREMENT_SYMBOL_DEFAULT_LANG,
							FieldConstants.MEASUREMENT + "." + FieldConstants.MEASUREMENT_SYMBOL_PREFER_LANG,
							FieldConstants.MEASUREMENT + "." + FieldConstants.MEASUREMENT_SYMBOL_SUPPORTIVE_LANG)
			))
			.withIgnorePaths("rawMaterialCategory.isDirectOrder");
	}

	/**
	 * Builds a JPA Specification for filtering and searching RawMaterialModel records based on
	 * both structured filters (like category) and unstructured search queries (like keyword search).
	 *
	 * @param filterDto      contains query string for keyword-based search
	 * @param searchFieldDto contains structured filter fields (like category ID)
	 * @return a Specification<RawMaterialModel> combining all filter and search conditions
	 */
	private Specification<RawMaterialModel> buildSpecification(FilterDto filterDto, SearchFieldDto searchFieldDto) {
		Specification<RawMaterialModel> spec = Specification.where(null); // Initialize an empty specification

		// Check if the structured filter is for CATEGORY and the value is valid (not null and not -1)
		if (FieldConstants.CATEGORY.equals(searchFieldDto.getFieldName()) && searchFieldDto.getFieldValue() != null && searchFieldDto.getFieldValue() != -1) { // Category filter (AND condition)
			spec = spec.and((root, query, cb) -> cb.equal(root.get(FieldConstants.RAW_MATERIAL_CATEGORY).get(FieldConstants.COMMON_FIELD_ID), searchFieldDto.getFieldValue()));
		}

		if (StringUtils.isNotBlank(filterDto.getQuery())) { // Search query (OR conditions)
			spec = spec.and(buildSearchSpecification(filterDto.getQuery()));
		}
		return spec;
	}

	/**
	 * Maps a RawMaterialModel entity to a RawMaterialDto using a model mapper service.
	 *
	 * @param rawMaterialModel the entity to convert
	 * @return the converted DTO
	 */
	private RawMaterialDto mapToDto(RawMaterialModel rawMaterialModel) {
		return modelMapperService.convertEntityAndDto(rawMaterialModel, RawMaterialDto.class);
	}

	/**
	 * Constructs a JPA Specification for full-text search across multiple fields
	 * in both RawMaterialModel and its related RawMaterialCategoryModel.
	 *
	 * @param query the search string entered by the user
	 * @return a Specification<RawMaterialModel> containing OR conditions for fuzzy search
	 */
	private Specification<RawMaterialModel> buildSearchSpecification(String query) {
		return (root, queryBuilder, cb) -> {
			// Search in RawMaterial fields
			Predicate itemPredicate = cb.or(
					cb.like(cb.lower(root.get(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG)), "%" + query.toLowerCase() + "%"),
					cb.like(cb.lower(root.get(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG)), "%" + query.toLowerCase() + "%"),
					cb.like(cb.lower(root.get(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG)), "%" + query.toLowerCase() + "%"));

			// Join with the related RawMaterialCategoryModel entity
			Join<RawMaterialModel, RawMaterialCategoryModel> categoryJoin = root.join(FieldConstants.RAW_MATERIAL_CATEGORY);
			Predicate categoryPredicate = cb.or(
					cb.like(cb.lower(categoryJoin.get(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG)), "%" + query.toLowerCase() + "%"),
					cb.like(cb.lower(categoryJoin.get(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG)), "%" + query.toLowerCase() + "%"),
					cb.like(cb.lower(categoryJoin.get(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG)), "%" + query.toLowerCase() + "%"));

			// Join with the related MeasurementModel entity
			Join<RawMaterialModel, MeasurementModel> measurementJoin = root.join(FieldConstants.MEASUREMENT);
			Predicate measurementPredicate = cb.or(
					cb.like(cb.lower(measurementJoin.get(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG)), "%" + query.toLowerCase() + "%"),
					cb.like(cb.lower(measurementJoin.get(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG)), "%" + query.toLowerCase() + "%"),
					cb.like(cb.lower(measurementJoin.get(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG)), "%" + query.toLowerCase() + "%"));

			return cb.or(itemPredicate, categoryPredicate, measurementPredicate);
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePriority(List<RawMaterialDto> list) throws RestException {
		List<RawMaterialModel> rawMaterialModelList = modelMapperService.convertListEntityAndListDto(list, RawMaterialModel.class);
		rawMaterialModelList.forEach(rawMaterial -> {
			DataUtils.setAuditFields(rawMaterialRepository, rawMaterial.getId(), rawMaterial);
			rawMaterialRepository.updatePriority(rawMaterial);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getHighestPriority() throws RestException {
		return rawMaterialRepository.getHighestPriority();
	}

}