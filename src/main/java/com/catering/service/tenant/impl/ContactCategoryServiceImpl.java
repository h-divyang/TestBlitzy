package com.catering.service.tenant.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.ContactCategoryDto;
import com.catering.dto.tenant.request.ContactCategoryWithContactsDto;
import com.catering.dto.tenant.request.ContactResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.ContactCategoryModel;
import com.catering.model.tenant.ContactCategoryTypeModel;
import com.catering.repository.tenant.ContactCategoryRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.ContactCategoryService;
import com.catering.service.tenant.ContactCategoryTypeService;
import com.catering.service.tenant.ContactService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.ValidationUtils;

/**
 * Implementation of the ContactCategoryService interface, providing methods for managing
 * contact categories and their associated operations. This class extends the GenericServiceImpl
 * to leverage common data access methods and includes additional custom business logic.
 * ContactCategoryServiceImpl interacts with various services, repositories, and utilities
 * to perform CRUD operations, data validations, and handle application exceptions.
 *
 * This service manages the lifecycle of ContactCategory entities, including creation,
 * updating, deletion, and retrieval operations, while enforcing business rules and validation checks.
 */
@Service
public class ContactCategoryServiceImpl extends GenericServiceImpl<ContactCategoryDto, ContactCategoryModel, Long> implements ContactCategoryService {

	/**
	 * Service for managing and retrieving localized messages.
	 */
	@Autowired
	private MessageService messageService;

	/**
	 * Service for handling and throwing application-specific exceptions.
	 */
	@Autowired
	private ExceptionService exceptionService;

	/**
	 * Service for mapping between DTOs and entities.
	 */
	@Autowired
	private ModelMapperService modelMapperService;

	/**
	 * Repository for CRUD operations on ContactCategory entities.
	 */
	@Autowired
	private ContactCategoryRepository contactCategoryRepository;

	/**
	 * Service for handling Contact-related operations, injected lazily to resolve potential circular dependencies.
	 */
	@Autowired
	@Lazy
	private ContactService contactService;

	/**
	 * Service for managing ContactCategoryType entities and their validations.
	 */
	@Autowired
	private ContactCategoryTypeService contactCategoryTypeService;

	/**
	 * Constant for the entity name 'Contact Category'.
	 */
	private static final String CONTACT_CATEGORY = "Contact Category";

	/**
	 * Constant for the identifier field name of 'Contact Category'.
	 */
	private static final String CONTACT_CATEGORY_ID = CONTACT_CATEGORY + " ID";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<ContactCategoryDto> createAndUpdate(ContactCategoryDto contactCategoryDto) throws RestException {
		ValidationUtils.validateFields(contactCategoryDto, contactCategoryRepository, exceptionService, messageService);
		if (Boolean.FALSE.equals(contactCategoryDto.getIsActive()) && contactService.existsByContactCategoryId(contactCategoryDto.getId())) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		contactCategoryTypeService.existByIdOrThrow(contactCategoryDto.getContactCategoryType().getId());
		contactCategoryDto.setIsNonUpdatable(0);
		ContactCategoryModel contactCategoryModel = modelMapperService.convertEntityAndDto(contactCategoryDto, ContactCategoryModel.class);
		DataUtils.setAuditFields(contactCategoryRepository, contactCategoryDto.getId(), contactCategoryModel);
		return Optional.of(modelMapperService.convertEntityAndDto(contactCategoryRepository.save(contactCategoryModel), ContactCategoryDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<ContactCategoryDto>> read(FilterDto filterDto) {
		Optional<Example<ContactCategoryModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			ContactCategoryModel contactCategoryModel = ContactCategoryModel.ofSearchingModel(query);
			contactCategoryModel.setId(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null);
			contactCategoryModel.setContactCategoryType(ContactCategoryTypeModel.ofSearchingModel(query));
			example = Optional.of(Example.of(contactCategoryModel, getExampleMatcher()));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(ContactCategoryDto.class, ContactCategoryModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long id) throws RestException {
		if (!contactCategoryRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		if (contactService.existsByContactCategoryId(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		contactCategoryRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existByIdAndIsActiveTrue(Long id) {
		if (Objects.nonNull(id)) {
			return contactCategoryRepository.existsByIdAndIsActiveTrue(id);
		}
		return Boolean.FALSE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void existByIdOrThrow(Long id) {
		if (Objects.isNull(id) || !contactCategoryRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST_BY_ID, CONTACT_CATEGORY_ID));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContactCategoryDto> findByIsActiveTrueOrderByPriorityAscIdAsc() {
		return modelMapperService.convertListEntityAndListDto(contactCategoryRepository.findByIsActiveTrueOrderByPriorityAscIdAsc(), ContactCategoryDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContactCategoryDto> findByCategoryType(Long categoryTypeId) {
		contactCategoryTypeService.existByIdOrThrow(categoryTypeId);
		return modelMapperService.convertListEntityAndListDto(contactCategoryRepository.findByIsActiveTrueAndContactCategoryTypeIdOrderByPriorityAscIdAsc(categoryTypeId), ContactCategoryDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContactCategoryDto> findByIsActiveTrueAndContactCategoryTypeId(Long contactCategoryTypeId) {
		return modelMapperService.convertListEntityAndListDto(contactCategoryRepository.findByIsActiveTrueAndContactCategoryTypeIdOrderByPriorityAscIdAsc(contactCategoryTypeId), ContactCategoryDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContactCategoryWithContactsDto> findByIsActiveTrueAndContactIsActiveTrue(Long contactCategoryTypeId) {
		List<ContactCategoryWithContactsDto> contactCategories = modelMapperService.convertListEntityAndListDto(Objects.nonNull(contactCategoryTypeId) && contactCategoryTypeId > 0 ? findByIsActiveTrueAndContactCategoryTypeId(contactCategoryTypeId) : findByIsActiveTrueOrderByPriorityAscIdAsc(), ContactCategoryWithContactsDto.class);
		contactCategories.forEach(contactCategory -> {
			List<ContactResponseDto> contacts = contactService.findByIsActiveTrueAndContactCategoryId(contactCategory.getId());
			contactCategory.setContacts(contacts);
		});
		return contactCategories;
	}

	/**
	 * Constructs and returns an instance of {@link ExampleMatcher} configured to perform case-insensitive
	 * searches based on specific fields, and to ignore certain audit-related fields.
	 *
	 * @return An {@link ExampleMatcher} instance configured for matching based on predefined rules.
	 */
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher
			.matchingAny()
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.CONTACT_CATEGORY_FIELD_CONTACT_CATEGORY_TYPE + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.CONTACT_CATEGORY_FIELD_CONTACT_CATEGORY_TYPE + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.CONTACT_CATEGORY_FIELD_CONTACT_CATEGORY_TYPE + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withMatcher(FieldConstants.PRIORITY, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
			.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.CONTACT_CATEGORY_FIELD_CONTACT_CATEGORY_TYPE)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePriority(List<ContactCategoryDto> contactCategories) throws RestException {
		List<ContactCategoryModel> contactCategoryModel = modelMapperService.convertListEntityAndListDto(contactCategories, ContactCategoryModel.class);
		contactCategoryModel.forEach(contactCategory -> {
			DataUtils.setAuditFields(contactCategoryRepository, contactCategory.getId(), contactCategory);
			contactCategoryRepository.updatePriority(contactCategory);
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getHighestPriority() throws RestException {
		return contactCategoryRepository.getHighestPriority();
	}

}