package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catering.bean.ErrorGenerator;
import com.catering.bean.Paging;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.MessagesFieldConstants;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.ContactCustomDto;
import com.catering.dto.tenant.request.ContactResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.ContactModel;
import com.catering.repository.tenant.CompanyPreferencesRepository;
import com.catering.repository.tenant.ContactRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.CompanySettingService;
import com.catering.service.tenant.ContactCategoryService;
import com.catering.service.tenant.ContactCategoryTypeService;
import com.catering.service.tenant.ContactService;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import com.catering.util.RequestResponseUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of {@link ContactService} that provides functionality for managing contacts.
 * This class contains methods for creating, updating, retrieving, validating, and deleting contacts.
 * It also includes logic for handling contact categories, unique constraints, and various validations.
 *
 * The class extends {@link GenericServiceImpl} to reuse common service behavior, and it interacts with multiple
 * helper services, repositories, and utilities to perform its operations.
 * 
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactServiceImpl extends GenericServiceImpl<ContactResponseDto, ContactModel, Long> implements ContactService {

	/**
	 * Service for mapping between DTOs and entities.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing Contact entities.
	 */
	ContactRepository contactRepository;

	/**
	 * Service for handling operations related to Contact Categories.
	 */
	ContactCategoryService contactCategoryService;

	/**
	 * Service for handling and throwing application-specific exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for managing and retrieving localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling operations related to Contact Category Types.
	 */
	ContactCategoryTypeService contactCategoryTypeService;

	/**
	 * Service for managing company-specific settings.
	 */
	CompanySettingService companySettingService;

	/**
	 * Service for managing company-specific preferences.
	 */
	CompanyPreferencesRepository companyPreferencesRepository;

	/**
	 * Constant for the entity name 'Contact'.
	 */
	String CONTACT = "Contact";

	/**
	 * Constant for the identifier field name of 'Contact'.
	 */
	String CONTACT_ID = CONTACT + " ID";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<ContactResponseDto> createAndUpdate(ContactResponseDto contactDto) throws RestException {
		validateFields(contactDto);
		ContactModel contactModel = modelMapperService.convertEntityAndDto(contactDto, ContactModel.class);
		contactModel.getCategoryMapping().forEach(categoryData -> categoryData.setContact(contactModel));
		DataUtils.setAuditFields(contactRepository, contactDto.getId(), contactModel);
		return Optional.of(modelMapperService.convertEntityAndDto(contactRepository.save(contactModel), ContactResponseDto.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void existByIdOrThrow(Long id) throws RestException {
		if (Objects.isNull(id) || !contactRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST_BY_ID, CONTACT_ID));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existById(Long id) {
		return Objects.nonNull(id) && contactRepository.existsById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<ContactResponseDto>> read(FilterDto filterDto) {
		Optional<Example<ContactModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		List<ContactResponseDto> categorySearchFoundData = new ArrayList<>();
		if (StringUtils.isNotBlank(query)) {
			categorySearchFoundData = modelMapperService.convertListEntityAndListDto(contactRepository.findContactsWithCategorySearching(query), ContactResponseDto.class);
			ExampleMatcher exampleMatcher = ExampleMatcher
				.matchingAny()
				.withMatcher(FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.CONTACT_FIELD_MOBILE_NUMBER, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsName(), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.CONTACT_FIELD_CONTACT_CATEGORY)));
			ContactModel contactModel = ContactModel.ofSearchingModel(query);
			example = Optional.of(Example.of(contactModel, exampleMatcher));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		ResponseContainerDto<List<ContactResponseDto>> responseContainer = read(ContactResponseDto.class, ContactModel.class, filterDto, example);
		// If category searched records found then add all to response and set pagination.
		if (!categorySearchFoundData.isEmpty() || StringUtils.isNotBlank(query)) {
			List<ContactResponseDto> combineRecords = new ArrayList<>(responseContainer.getBody());
			combineRecords.addAll(categorySearchFoundData);
			responseContainer.setBody(combineRecords);
			responseContainer.setBody(responseContainer.getBody().stream().collect(Collectors.toMap(record -> record.getId(), record -> record, (existing, replacement) -> existing )).values().stream().toList());
			if (StringUtils.isNotBlank(filterDto.getSortBy())) {
				List<ContactResponseDto> sortedList = new ArrayList<>(responseContainer.getBody());
				sortListByField(sortedList, filterDto.getSortBy(), filterDto.getSortDirection());
				responseContainer.setBody(sortedList);
			}
			responseContainer = setCustomPagination(responseContainer, Integer.parseInt(filterDto.getSizePerPage()), Integer.parseInt(filterDto.getCurrentPage()), responseContainer.getBody().size());
		}
		if ((filterDto.getSortBy().equals(FieldConstants.CONTACT_FIELD_CATEGORY_MAPPING + "." + FieldConstants.CONTACT_FIELD_CONTACT_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG) ||
				filterDto.getSortBy().equals(FieldConstants.CONTACT_FIELD_CATEGORY_MAPPING + "." + FieldConstants.CONTACT_FIELD_CONTACT_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG)	||
				filterDto.getSortBy().equals(FieldConstants.CONTACT_FIELD_CATEGORY_MAPPING + "." + FieldConstants.CONTACT_FIELD_CONTACT_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG)) && StringUtils.isBlank(query)) {
			List<ContactResponseDto> allData = new ArrayList<>(modelMapperService.convertListEntityAndListDto(contactRepository.findAll(), ContactResponseDto.class));
			sortListByField(allData, filterDto.getSortBy(), filterDto.getSortDirection());
			responseContainer.setBody(allData);
			responseContainer = setCustomPagination(responseContainer, Integer.parseInt(filterDto.getSizePerPage()), Integer.parseInt(filterDto.getCurrentPage()), responseContainer.getBody().size());
		}
		return responseContainer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteById(Long id) throws RestException {
		if (!contactRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		try {
			Integer result = contactRepository.checkContactUsage(id);
			if (result != null && result == 0) {
				Long idOfSubscription = Objects.nonNull(companyPreferencesRepository.getById(1l).getSubscriptionId()) ? companyPreferencesRepository.getById(1l).getSubscriptionId() : 0;
				if (idOfSubscription > 1) {
					contactRepository.deleteContactFromAccount(id);
					contactRepository.deleteContactFromAccountHistory(id);
				}
				contactRepository.deleteById(id);
			} else {
				throw new DataIntegrityViolationException(messageService.getMessage(MessagesConstant.IN_USE));
			}
		} catch (DataIntegrityViolationException e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByContactCategoryId(Long id) {
		return contactRepository.existsByCategoryMapping_ContactCategory_Id(id);
	}

	/**
	 * Retrieves a unique list of contacts based on specified criteria. The method fetches data
	 * using raw material ID, a list of excluded IDs, or default configurations, and converts
	 * the entity list into DTOs before returning the result.
	 *
	 * @param dtoType The data transfer object class type for the response.
	 * @param modelType The entity class type for the data model.
	 * @param IDs A list of IDs to exclude from the result set.
	 * @param rawMaterialId The ID of the raw material to filter the contacts.
	 * @param userEditId The ID of the user editing or filtering the contacts.
	 * @return A ResponseContainerDto containing a list of ContactResponseDto objects.
	 */
	@Override
	public ResponseContainerDto<List<ContactResponseDto>> uniqueList(Class<ContactResponseDto> dtoType, Class<ContactModel> modelType, List<Long> IDs, Long rawMaterialId, Long userEditId) {
		List<ContactModel> list;
		if (rawMaterialId != null && rawMaterialId != 0) {
			list = contactRepository.findContactCategoryUsingRawMaterial(rawMaterialId, userEditId);
		} else if (!IDs.isEmpty()) {
			list = contactRepository.findDistinctByCategoryMapping_ContactCategory_ContactCategoryType_IdAndIsActiveTrueAndIdNotIn(3L, IDs);
		} else {
			list = contactRepository.findDistinctByCategoryMapping_ContactCategory_ContactCategoryType_IdAndIsActiveTrue(3L);
		}
		return RequestResponseUtils.generateResponseDto(modelMapperService.convertListEntityAndListDto(list, dtoType));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContactResponseDto> findByCategoryType(Long categoryTypeId) {
		contactCategoryTypeService.existByIdOrThrow(categoryTypeId);
		return modelMapperService.convertListEntityAndListDto(contactRepository.findDistinctActiveContactsByContactCategoryTypeId(categoryTypeId), ContactResponseDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContactResponseDto> findByIsActiveTrueAndContactCategoryId(Long contactCategoryId) {
		return modelMapperService.convertListEntityAndListDto(contactRepository.findDistinctByIsActiveTrueAndCategoryMapping_ContactCategory_Id(contactCategoryId), ContactResponseDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContactCustomDto> findAllContact() {
		return modelMapperService.convertListEntityAndListDto(contactRepository.findByIsActiveTrue(), ContactCustomDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContactCustomDto> findAllBankList() {
		return modelMapperService.convertListEntityAndListDto(contactRepository.findAllBankList(), ContactCustomDto.class);
	}

	/**
	 * Validates the fields of the given ContactResponseDto. It ensures required properties are set
	 * and checks for constraints such as uniqueness of mobile number and Aadhaar number, as well as
	 * the validity and activeness of associated contact categories.
	 *
	 * @param contactDto The ContactResponseDto object containing the contact details to validate.
	 */
	private void validateFields(ContactResponseDto contactDto) {
		if (Objects.isNull(contactDto.getIsCash())) {
			contactDto.setIsCash(0);
		}
		// Check contact category is exists in database or not
		contactDto.getCategoryMapping().forEach(data -> {
			contactCategoryService.existByIdOrThrow(data.getContactCategory().getId());
		});
		if (Objects.nonNull(contactDto.getIsCash()) && contactDto.getIsCash() == 1) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.ACCESS_DENIED));
		}
		contactDto.getCategoryMapping().forEach(data -> {
			if (!contactCategoryService.existByIdAndIsActiveTrue(data.getContactCategory().getId())) {
				ErrorGenerator errors = ErrorGenerator.builder().putError(FieldConstants.CONTACT_FIELD_CONTACT_CATEGORY, messageService.getMessage(MessagesConstant.CONTACT_CATEGORY_INACTIVE));
				exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
			}
		});
		// Check the mobile number and aadhar card exists or not
		ErrorGenerator errors = ErrorGenerator.builder();
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		boolean isMobileExist = false;
		if (Boolean.TRUE.equals(companySettingDto.getIsMobileNumberUnique())) {
			isMobileExist = Objects.nonNull(contactDto.getId()) ? contactRepository.existsByMobileNumberAndIdNot(contactDto.getMobileNumber(), contactDto.getId()) : contactRepository.existsByMobileNumber(contactDto.getMobileNumber());
			if (isMobileExist) {
				errors.putError(FieldConstants.CONTACT_FIELD_MOBILE_NUMBER, messageService.getMessage(MessagesConstant.ALREADY_EXITS_FIELD, MessagesFieldConstants.COMMON_FIELD_MOBILE_NUMBER));
			}
		}
		boolean isAadharExist = false;
		if (contactDto.getAadharNumber() != null && Objects.nonNull(contactDto.getId())) {
			isAadharExist = contactRepository.existsByAadharNumberAndIdNot(contactDto.getAadharNumber(), contactDto.getId());
		} else if (contactDto.getAadharNumber() != null) {
			isAadharExist = contactRepository.existsByAadharNumber(contactDto.getAadharNumber());
		}
		if (isAadharExist) {
			errors.putError(FieldConstants.CONTACT_FIELD_AADHAR_NUMBER, messageService.getMessage(MessagesConstant.ALREADY_EXITS_FIELD, MessagesFieldConstants.CONTACT_CATEGORY_FIELD_AADHAR_NUMBER));
		}
		if (isMobileExist || isAadharExist) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
	}

	/**
	 * Sets custom pagination information for a response container, including pagination metadata and filtered data based on 
	 * the current page, size per page, and total records.
	 *
	 * @param responseContainer The response container that holds the data and pagination details.
	 * @param sizePerPage The number of records to be displayed per page.
	 * @param currentPage The current page number being requested.
	 * @param totalRecords The total number of records available.
	 * @return A modified response container with updated pagination details and filtered data for the current page.
	 */
	private ResponseContainerDto<List<ContactResponseDto>> setCustomPagination(ResponseContainerDto<List<ContactResponseDto>> responseContainer, int sizePerPage, int currentPage, long totalRecords) {
		long firstIndex = (sizePerPage * (currentPage - 1)) + 1;
		long lastIndex = Math.min(firstIndex + sizePerPage - 1, totalRecords);
		long totalPages = (long) Math.ceil((double) totalRecords / sizePerPage);
		Paging paging = new Paging(currentPage, sizePerPage, firstIndex, lastIndex, totalRecords, totalPages, Math.min(lastIndex, totalRecords) - firstIndex + 1);
		responseContainer.setPaging(paging);
		responseContainer.setBody(responseContainer.getBody().subList((int) firstIndex - 1, (int) lastIndex));
		return responseContainer;
	}

	/**
	 * Sorts a list of {@code ContactResponseDto} objects based on the specified field name and sort direction.
	 *
	 * @param list The list of {@code ContactResponseDto} objects to be sorted.
	 * @param fieldName The name of the field by which the list should be sorted. 
	 * @param sortDirection The sort direction, which can be either {@code "asc"} for ascending 
	 *						or {@code "desc"} for descending. The comparison is case-insensitive.
	 *
	 * @throws NullPointerException If {@code list} or {@code fieldName} is {@code null}.
	 * @throws IllegalArgumentException If an unsupported {@code fieldName} is provided.
	 */
	private void sortListByField(List<ContactResponseDto> list, String fieldName, String sortDirection) {
		Comparator<ContactResponseDto> comparator = Comparator.comparing(contact -> {
			switch (fieldName) {
				case FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG:
					return contact.getNameDefaultLang() != null ? contact.getNameDefaultLang().toLowerCase() : "";
				case FieldConstants.COMMON_FIELD_NAME_PREFER_LANG:
					return contact.getNamePreferLang() != null ? contact.getNamePreferLang().toLowerCase() : "";
				case FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG:
					return contact.getNameSupportiveLang() != null ? contact.getNameSupportiveLang().toLowerCase() : "";
				case FieldConstants.CONTACT_FIELD_MOBILE_NUMBER:
					return contact.getMobileNumber();
				case FieldConstants.CONTACT_FIELD_CATEGORY_MAPPING + "." + FieldConstants.CONTACT_FIELD_CONTACT_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG:
					return !contact.getCategoryMapping().isEmpty() && contact.getCategoryMapping().get(0).getContactCategory().getNameDefaultLang() != null ? contact.getCategoryMapping().get(0).getContactCategory().getNameDefaultLang().toLowerCase() : "";
				case FieldConstants.CONTACT_FIELD_CATEGORY_MAPPING + "." + FieldConstants.CONTACT_FIELD_CONTACT_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG:
					return !contact.getCategoryMapping().isEmpty() && contact.getCategoryMapping().get(0).getContactCategory().getNamePreferLang() != null ? contact.getCategoryMapping().get(0).getContactCategory().getNamePreferLang().toLowerCase() : "";
				case FieldConstants.CONTACT_FIELD_CATEGORY_MAPPING + "." + FieldConstants.CONTACT_FIELD_CONTACT_CATEGORY + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG:
					return !contact.getCategoryMapping().isEmpty() && contact.getCategoryMapping().get(0).getContactCategory().getNameSupportiveLang() != null ? contact.getCategoryMapping().get(0).getContactCategory().getNameSupportiveLang().toLowerCase() : "";
				default:
					return "";
			}
		});
		if ("desc".equalsIgnoreCase(sortDirection)) {
			comparator = comparator.reversed();
		}
		list.sort(comparator);
	}

}