package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.catering.bean.Paging;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.CompanyPreferencesDto;
import com.catering.dto.tenant.request.ContactResponseDto;
import com.catering.dto.tenant.request.RawMaterialSupplierDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.ContactModel;
import com.catering.model.tenant.RawMaterialModel;
import com.catering.model.tenant.RawMaterialSupplierModel;
import com.catering.repository.tenant.ContactRepository;
import com.catering.repository.tenant.RawMaterialSupplierRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CompanyPreferencesService;
import com.catering.service.tenant.RawMaterialSupplierService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the RawMaterialSupplierService interface.
 *
 * This class provides methods for managing and processing raw material supplier data,
 * including operations such as creation, updating, marking suppliers as default,
 * retrieving supplier data, and deletion. It extends the GenericServiceImpl class
 * and utilizes several services and repositories for its functionality.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialSupplierServiceImpl extends GenericServiceImpl<RawMaterialSupplierDto, RawMaterialSupplierModel, Long> implements RawMaterialSupplierService {

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Service for managing company preferences and settings.
	 */
	CompanyPreferencesService companyPreferencesService;

	/**
	 * Repository for managing raw material suppliers.
	 */
	RawMaterialSupplierRepository rawMaterialSupplierRepository;

	/**
	 * Repository for managing contact information.
	 */
	ContactRepository contactRepository;

	/**
	 * Service for sending messages and notifications.
	 */
	MessageService messageService;

	/**
	 * Service for handling application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<RawMaterialSupplierDto> createAndUpdate(RawMaterialSupplierDto rawMaterialSupplierDto) throws RestException {
		rawMaterialSupplierValidation(rawMaterialSupplierDto);
		RawMaterialSupplierModel rawMaterialSupplierModel = modelMapperService.convertEntityAndDto(rawMaterialSupplierDto, RawMaterialSupplierModel.class);
		DataUtils.setAuditFields(rawMaterialSupplierRepository, rawMaterialSupplierDto.getId(), rawMaterialSupplierModel);
		return Optional.of(modelMapperService.convertEntityAndDto(rawMaterialSupplierRepository.save(rawMaterialSupplierModel), RawMaterialSupplierDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<RawMaterialSupplierDto>> read(Long rawMaterialId, String page, String size, String sortBy, String sortDirection, String query) {
		Optional<Example<RawMaterialSupplierModel>> example = Optional.empty();
		if (StringUtils.isNotBlank(query)) {
			RawMaterialSupplierModel rawMaterialSupplierModel = RawMaterialSupplierModel.ofSearchingModel();
			rawMaterialSupplierModel.setContact(ContactModel.ofSearchingModel(query));
			example = Optional.of(Example.of(rawMaterialSupplierModel, getExampleMatcher()));
		}
		Optional<PageRequest> pageRequest = PagingUtils.pageRequestOf(page, size);
		Optional<Sort> sort = PagingUtils.sortOf(sortDirection, sortBy, RawMaterialSupplierModel.class);
		Optional<Page<RawMaterialSupplierModel>> pages = Optional.empty();
		List<RawMaterialSupplierModel> list = null;
		if (sort.isPresent() && pageRequest.isPresent()) {
			pageRequest = Optional.of(pageRequest.get().withSort(sort.get()));
		}
		if (pageRequest.isPresent() && example.isPresent()) {
			pages = Optional.of(rawMaterialSupplierRepository.findAll(getSpecFromDatesAndExample(rawMaterialId, example.get()), pageRequest.get()));
		} else if (pageRequest.isPresent()) {
			pages =  Optional.of(rawMaterialSupplierRepository.findByRawMaterial_Id(rawMaterialId, pageRequest.get()));
		}
		if (pages.isEmpty() && Objects.isNull(list)) {
			list = rawMaterialSupplierRepository.findByRawMaterial_Id(rawMaterialId);
		}
		if (Objects.nonNull(list)) {
			return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(list, RawMaterialSupplierDto.class));
		}
		Optional<Paging> paging = PagingUtils.getPaging(pages.get());
		return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(pages.get().getContent(), RawMaterialSupplierDto.class)).setCustomPaging(!pages.get().getContent().isEmpty() ? paging.get() : Paging.builder().build());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<RawMaterialSupplierDto> markDefault(Long rawMaterialId, Long contractorId, Boolean isDefault) throws RestException {
		List<RawMaterialSupplierModel> isDefaultList = rawMaterialSupplierRepository.findByRawMaterial_IdAndIsDefaultTrue(rawMaterialId);
		if (!isDefaultList.isEmpty()) {
			// There will be only one default field in contractor
			RawMaterialSupplierModel contractorModel = isDefaultList.get(0);
			contractorModel.setIsDefault(false);
			rawMaterialSupplierRepository.save(contractorModel);
		}
		RawMaterialSupplierModel markDefaultSupplier = rawMaterialSupplierRepository.findById(contractorId).orElse(null);
		if (markDefaultSupplier != null) {
			markDefaultSupplier.setIsDefault(true);
			markDefaultSupplier.setEditCount(markDefaultSupplier.getEditCount() + 1);
			rawMaterialSupplierRepository.save(markDefaultSupplier);
		}
		return Optional.of(modelMapperService.convertEntityAndDto(rawMaterialSupplierRepository.save(markDefaultSupplier), RawMaterialSupplierDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) {
		if (!rawMaterialSupplierRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			rawMaterialSupplierRepository.deleteById(id);
		} catch (Exception e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * Constructs and returns an ExampleMatcher with specific match conditions
	 * and configurations for performing case-insensitive and partial matching
	 * on certain fields while ignoring audit and specified fields.
	 *
	 * @return An ExampleMatcher configured with custom matchers and ignored paths.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.CONTACT + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.CONTACT + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.CONTACT + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(),
					BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.CONTACT),
					BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.RAW_MATERIAL),
					ArrayUtils.buildArray(FieldConstants.CONTACT + "." + FieldConstants.CONTACT_FIELD_EMAIL)));
	}

	/**
	 * Validates the provided RawMaterialSupplierDto to ensure it adheres to
	 * the business rules before processing. Checks the validity of the contact
	 * information and enforces specific constraints based on company preferences.
	 *
	 * @param rawMaterialSupplierDto The DTO representing the raw material supplier to be validated.
	 * @throws RestException If validation errors are found during the process.
	 */
	private void rawMaterialSupplierValidation(RawMaterialSupplierDto rawMaterialSupplierDto) throws RestException {
		Optional<CompanyPreferencesDto> companyPreferencesDtoOptional = companyPreferencesService.find();
		if(companyPreferencesDtoOptional.isPresent()) {
			Map<String, String> errors = new HashMap<>();
			ContactResponseDto contact = rawMaterialSupplierDto.getContact();
			contact.getCategoryMapping().forEach(data -> {
				if (data.getContactCategory() == null || data.getContactCategory().getId() == null) {
					Optional<ContactModel> contactModel = contactRepository.findById(contact.getId());
					contactModel.get().getCategoryMapping().forEach(subData -> {
						if (!subData.getContactCategory().getContactCategoryType().getId().equals(5l)) {
							errors.put(FieldConstants.CONTACT, messageService.getMessage(MessagesConstant.RAW_MATERIAL_SUPPLIER_CONTACT_FIELD_NOT_ACCEPTEBLE));
						}
					});
				}
			});
			if (!errors.isEmpty()) {
				exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_MANDATORY_FIELD_NOT_BLANK), errors);
			}
		}
	}

	/**
	 * Builds a dynamic Specification for querying RawMaterialSupplierModel based on the provided rawMaterialId and example.
	 *
	 * @param rawMaterialId The ID of the raw material to filter suppliers. Can be null.
	 * @param example An Example instance containing matching criteria for the RawMaterialSupplierModel.
	 * @return A Specification for querying the RawMaterialSupplierModel that combines
	 *		   the rawMaterialId filter and the criteria defined in the example.
	 */
	private Specification<RawMaterialSupplierModel> getSpecFromDatesAndExample(Long rawMaterialId, Example<RawMaterialSupplierModel> example) {
		return (Specification<RawMaterialSupplierModel>) (root, query, builder) -> {
			final List<Predicate> predicates = new ArrayList<>();
			Join<RawMaterialModel, RawMaterialSupplierModel> rawMaterial = root.join("rawMaterial");
			if (rawMaterialId != null) {
				predicates.add(builder.equal(rawMaterial.get("id"), rawMaterialId));
			}
			predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

}