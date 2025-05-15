package com.catering.service.tenant.impl;

import com.catering.bean.ErrorGenerator;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.RecordInUse;
import com.catering.dto.tenant.request.*;
import com.catering.enums.VoucherTypeEnum;
import com.catering.exception.RestException;
import com.catering.model.tenant.BookOrderModel;
import com.catering.model.tenant.EventTypeModel;
import com.catering.model.tenant.GetBookOrderContactModel;
import com.catering.model.tenant.GetBookOrderListModel;
import com.catering.model.tenant.GetMenuPreparationMenuItemModel;
import com.catering.model.tenant.GetMenuPreparationModel;
import com.catering.model.tenant.GetMenuPreparationOrderFunctionModel;
import com.catering.model.tenant.SaveMenuPreparationMenuItemModel;
import com.catering.model.tenant.SaveMenuPreparationModel;
import com.catering.model.tenant.SaveMenuPreparationOrderFunctionModel;
import com.catering.repository.tenant.BookOrderRepository;
import com.catering.repository.tenant.CompanyPreferencesRepository;
import com.catering.repository.tenant.GetMenuPreparationOrderFunctionRepository;
import com.catering.repository.tenant.GetOrderMenuPreparationRepository;
import com.catering.repository.tenant.OrderFunctionRepository;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.*;
import com.catering.util.ArrayUtils;
import com.catering.util.BeanUtils;
import com.catering.util.DataUtils;
import com.catering.util.PagingUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for managing book orders.
 * Extends the base service implementation and provides specific functionalities for handling book orders,
 * including creation, updating, validation, filtering, and deletion.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookOrderServiceImpl extends GenericServiceImpl<BookOrderDto, GetBookOrderListModel, Long> implements BookOrderService {

	/**
	 * Service providing functionalities for mapping objects using ModelMapper.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing Book Order entity interactions with the database.
	 */
	BookOrderRepository bookOrderRepository;

	/**
	 * Repository for managing Order Function specific data interactions.
	 */
	OrderFunctionRepository orderFunctionRepository;

	/**
	 * Service responsible for managing contact-related operations.
	 */
	ContactService contactService;

	/**
	 * Service for managing meal type-related operations.
	 */
	MealTypeService mealTypeService;

	/**
	 * Service for managing and delivering localized messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling custom exceptions and exception logic.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for processing and managing Cash Payment Receipts.
	 */
	CashPaymentReceiptService cashPaymentReceiptService;

	/**
	 * Repository for accessing and managing company preferences data.
	 */
	CompanyPreferencesRepository companyPreferencesRepository;

	/**
	 * Service for handling operations related to contact categories.
	 */
	ContactCategoryService contactCategoryService;

	/**
	 * Service responsible for distributing and managing labour-related details for orders.
	 */
	OrderLabourDistributionService orderLabourDistributionService;

	/**
	 * Repository for fetching Order Menu Preparation data related to allocation.
	 */
	GetOrderMenuPreparationRepository getOrderMenuPreparationRepository;

	/**
	 * Repository for fetching Order Menu Preparation Order Function data related to allocation.
	 */
	GetMenuPreparationOrderFunctionRepository getMenuPreparationOrderFunctionRepository;

	/**
	 * Service to handle the core logic for menu preparation processes.
	 */
	OrderMenuPreparationService orderMenuPreparationRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BookOrderDto createAndUpdate(BookOrderDto bookOrderDto) {
		if(bookOrderDto.getCopiedOrderId() != null) {
			bookOrderDto.setId(null);
		}
		validateFields(bookOrderDto);
		BookOrderModel bookOrderModel = modelMapperService.convertEntityAndDto(bookOrderDto, BookOrderModel.class);
		bookOrderModel.getFunctions().forEach(function -> function.setBookOrder(bookOrderModel));
		DataUtils.setAuditFields(bookOrderRepository, bookOrderDto.getId(), bookOrderModel);
		BookOrderModel bookOrderModelResponse = bookOrderRepository.save(bookOrderModel);
		saveLabourDistribution(bookOrderModelResponse, bookOrderDto.getId());
		if (bookOrderDto.getCopiedOrderId() != null) {
			copyMenuPreparationsFromOriginalOrder(bookOrderDto.getCopiedOrderId(), bookOrderModelResponse.getId());
		}
		return modelMapperService.convertEntityAndDto(bookOrderModelResponse, BookOrderDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseContainerDto<List<BookOrderDto>> read(FilterDto filterDto) {
		Optional<Example<GetBookOrderListModel>> example = Optional.empty();
		String query = filterDto.getQuery();
		if (StringUtils.isNotBlank(query)) {
			ExampleMatcher exampleMatcher = ExampleMatcher
				.matchingAny()
				.withMatcher(FieldConstants.COMMON_FIELD_ID, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.BOOK_ORDER_FIELD_EVENT_MAIN_DATE_STRING, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.BOOK_ORDER_FIELD_CONTACT_CUSTOMER + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.BOOK_ORDER_FIELD_CONTACT_CUSTOMER + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.BOOK_ORDER_FIELD_CONTACT_CUSTOMER + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.EVENT_TYPE + "." + FieldConstants.COMMON_FIELD_NAME_DEFAULT_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.EVENT_TYPE + "." + FieldConstants.COMMON_FIELD_NAME_PREFER_LANG, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(FieldConstants.EVENT_TYPE + "." + FieldConstants.COMMON_FIELD_NAME_SUPPORTIVE_LANG,ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withIgnorePaths(ArrayUtils.mergeStringArray(BeanUtils.getAuditFieldsNameWithoutId(), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.BOOK_ORDER_FIELD_CONTACT_CUSTOMER), BeanUtils.getAuditFieldsNameWithConcat(FieldConstants.BOOK_ORDER_FIELD_EVENT_TYPE)));
			GetBookOrderListModel bookOrderModel = GetBookOrderListModel.ofSearchingModel(query);
			bookOrderModel.setContactCustomer(GetBookOrderContactModel.builder().nameDefaultLang(query).namePreferLang(query).nameSupportiveLang(query).build());
			bookOrderModel.setEventType(EventTypeModel.builder().nameDefaultLang(query).namePreferLang(query).nameSupportiveLang(query).build());
			bookOrderModel.setId(NumberUtils.isDigits(query) ? Long.valueOf(query) : null);
			example = Optional.of(Example.of(bookOrderModel, exampleMatcher));
		}
		filterDto.setSortBy(PagingUtils.getDefaultSortingField(filterDto.getSortBy()));
		filterDto.setSortDirection(PagingUtils.getDefaultSortingDirection(filterDto.getSortDirection()));
		return read(BookOrderDto.class, GetBookOrderListModel.class, filterDto, example);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OrderForUpcomingEventDto> upcomingOrders() {
		return modelMapperService.convertListEntityAndListDto(bookOrderRepository.findUpcomingOrders(), OrderForUpcomingEventDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OrderForDashboardDto> getBookOrderForDashboard() {
		List<OrderForDashboardDto> bookOrderDtoList = modelMapperService.convertListEntityAndListDto(bookOrderRepository.findAll(), OrderForDashboardDto.class);
		bookOrderDtoList.stream().filter(bookOrder ->  Constants.ORDER_STATUS_CONFIRM.equals(bookOrder.getStatus().getId()))
			.forEach(bookOrder -> bookOrder.setIsMenuPrepared(bookOrderRepository.isMenuPrepared(bookOrder.getId())));
		return bookOrderDtoList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BookOrderDto getBookOrderForDashboardById(Long id) {
		BookOrderModel bookOrderOptional = bookOrderRepository.findById(id).orElse(null);
		BookOrderDto bookOrderDto = modelMapperService.convertEntityAndDto(bookOrderOptional, BookOrderDto.class);
		if (Objects.nonNull(bookOrderDto.getStatus()) && Constants.ORDER_STATUS_CONFIRM.equals(bookOrderDto.getStatus().getId())) {
			bookOrderDto.setIsMenuPrepared(bookOrderRepository.isMenuPrepared(bookOrderDto.getId()));
		}
		return bookOrderDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsById(Long id) {
		return bookOrderRepository.existsById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByOrderFunctionId(Long id) {
		return orderFunctionRepository.existsById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsOrderFunctionByFunctionTypeId(Long id) {
		return orderFunctionRepository.existsByFunctionTypeId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByMealTypeId(Long id) {
		return bookOrderRepository.existsByMealTypeId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByEventTypeId(Long id) {
		return bookOrderRepository.existsByEventTypeId(id);
	}

	/**
	 * Deletes a BookOrder entity by its unique identifier. Ensures that the entity is not in use
	 * in any associated operations or references before deletion. Throws an exception if the entity
	 * is in use or does not exist.
	 *
	 * @param id The unique identifier of the BookOrder entity to delete.
	 * @throws RestException If the BookOrder entity is in use, does not exist, or if any error occurs during the deletion process.
	 */
	@Override
	@Transactional
	public void deleteById(Long id) throws RestException {
		Long idOfSubscription = Objects.nonNull(companyPreferencesRepository.getById(1l).getSubscriptionId()) ? companyPreferencesRepository.getById(1l).getSubscriptionId() : 0;
		Integer count = 0;
		if (idOfSubscription > 1) {
			count = cashPaymentReceiptService.isUseInCashOrBank(id, VoucherTypeEnum.getIds(VoucherTypeEnum.CHEF_LABOUR, VoucherTypeEnum.OUTSIDE_LABOUR, VoucherTypeEnum.LABOUR_ALLOCATION, VoucherTypeEnum.INVOICE, VoucherTypeEnum.ADVANCE_PAYMENT));
		}
		if (Objects.nonNull(count) && count > 0) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
		if (!bookOrderRepository.existsById(id)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.NOT_EXIST));
		}
		bookOrderRepository.deleteMenuItem(id);
		bookOrderRepository.deleteLabour(id);
		bookOrderRepository.deleteInvoice(id);
		try {
			bookOrderRepository.deleteById(id);
		} catch (Exception e) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.IN_USE), RecordInUse.builder().inUse(Boolean.TRUE).build());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateStatus(CommonMultiLanguageDto orderStatusDto, Long orderId) {
		bookOrderRepository.updateStatus(orderStatusDto, orderId);
	}

	/**
	 * Validates the fields of the given {@code BookOrderDto} object and ensures that required fields are present and valid.
	 * Throws a bad request exception if any validation errors are found.
	 *
	 * @param bookOrderDto The data transfer object representing the book order to be validated.
	 */
	private void validateFields(BookOrderDto bookOrderDto) {
		ErrorGenerator error = ErrorGenerator.builder();
		if (!contactService.existById(bookOrderDto.getContactCustomer().getId())) {
			error.putError(FieldConstants.BOOK_ORDER_FIELD_CONTACT_CUSTOMER, messageService.getMessage(MessagesConstant.VALIDATION_INVALID_FIELD, FieldConstants.BOOK_ORDER_FIELD_CONTACT_CUSTOMER));
		}
		if (!mealTypeService.existById(bookOrderDto.getMealType().getId())) {
			error.putError(FieldConstants.BOOK_ORDER_FIELD_MEAL_TYPE, messageService.getMessage(MessagesConstant.VALIDATION_INVALID_FIELD, FieldConstants.BOOK_ORDER_FIELD_MEAL_TYPE));
		}
		if (bookOrderDto.getFunctions().isEmpty()) {
			error.putError(FieldConstants.BOOK_ORDER_FIELD_FUNCTIONS, messageService.getMessage(MessagesConstant.VALIDATION_BOOK_ORDER_FUNCTIONS_REQUIRED));
		}
		if (error.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), error.getErrors());
		}
	}

	/**
	 * Saves the labour distribution details for a given book order model. This method generates and
	 * persists a list of labour distribution entries based on the provided book order model
	 * and contact categories that have active contacts with displayLabourRecord set to true.
	 *
	 * @param bookOrderModel The model object representing the book order details.
	 * @param id The unique identifier to decide whether the labour distribution needs to be created (if the identifier is null).
	 */
	private void saveLabourDistribution(BookOrderModel bookOrderModel, Long id) {
		if (Objects.isNull(id)) {
			List<ContactCategoryWithContactsDto> contactCategories = contactCategoryService.findByIsActiveTrueAndContactIsActiveTrue(Constants.LABOUR_CONTACT_CATEGORY).stream().filter(category -> category.getContacts() != null && !category.getContacts().isEmpty() && Boolean.TRUE.equals(category.getDisplayLabourRecord())).toList();
			BookOrderDto bookOrderDto = modelMapperService.convertEntityAndDto(bookOrderModel, BookOrderDto.class);
			List<OrderLabourDistributionDto> orderLabourDistributionDtos = new ArrayList<>();
			bookOrderDto.getFunctions().forEach(function -> {
				contactCategories.forEach(contactCategory -> {
					ContactResponseDto contact = contactCategory.getContacts().get(0);
					OrderLabourDistributionDto orderLabourDistributionDto = new OrderLabourDistributionDto();
					orderLabourDistributionDto.setOrderFunction(function);
					orderLabourDistributionDto.setLabourShift(function.getFunctionType().getLabourShift());
					orderLabourDistributionDto.setDate(function.getDate());
					orderLabourDistributionDto.setContactCategory(contactCategory);
					orderLabourDistributionDto.setContact(contact);
					orderLabourDistributionDto.setLabourPrice(Optional.ofNullable(contact.getLabourPrice()).orElse(0.0));
					orderLabourDistributionDto.setQuantity(0.0);
					orderLabourDistributionDto.setGodown(null);
					orderLabourDistributionDtos.add(orderLabourDistributionDto);
				});
			});
			orderLabourDistributionService.createAndUpdate(orderLabourDistributionDtos);
		}
	}

	/**
	 * Copies the menu preparation data from an original (copied) order to a new order.
	 * 
	 * This method retrieves the menu preparation entries from the original order
	 * and copies them to the new order. 
	 * While copying:
	 *   - All IDs are reset to avoid conflicts.
	 *   - Menu items, categories, and 'no item' entries are also reset and detached.
	 *   - Corresponding order functions from the new order are mapped and set accordingly.
	 *
	 * @param originalOrderId the ID of the original order from which the menu preparations are copied
	 * @param newOrderId      the ID of the new order to which the menu preparations are copied
	 */
	private void copyMenuPreparationsFromOriginalOrder(Long copiedOrderId, Long newOrderId) {
		List<GetMenuPreparationOrderFunctionModel> getMenuPreparationOrderFunctionModelList = getMenuPreparationOrderFunctionRepository.findOrderFunctionsOfLastestOrder(newOrderId);
		List<GetMenuPreparationModel> getMenuPreparationModelList = getOrderMenuPreparationRepository.findByOrderFunctionBookOrderId(copiedOrderId);
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		// Map original menu preparations by function sequence
		Map<Integer, GetMenuPreparationModel> menuPreparationBySequence = getMenuPreparationModelList.stream().collect(Collectors.toMap(menuPreparationModel -> menuPreparationModel.getOrderFunction().getSequence(), menuPreparationModel -> menuPreparationModel));
		List<SaveMenuPreparationModel> saveMenuPreparationModelList = new ArrayList<>();
		for (GetMenuPreparationOrderFunctionModel newOrderFunction : getMenuPreparationOrderFunctionModelList) {
			GetMenuPreparationModel getMenuPreparationModel = (newOrderFunction.getCopiedFunctionSequence() != null) ? menuPreparationBySequence.get(newOrderFunction.getCopiedFunctionSequence()) : null;
			if (getMenuPreparationModel != null) {
				SaveMenuPreparationModel saveMenuPreparationModel = modelMapper.map(getMenuPreparationModel, SaveMenuPreparationModel.class);
				saveMenuPreparationModel.setId(null);
				resetCustomPackage(saveMenuPreparationModel, getMenuPreparationModel);
				resetMenuItem(saveMenuPreparationModel, getMenuPreparationModel);
				resetMenuItemCategory(saveMenuPreparationModel);
				resetNoItem(saveMenuPreparationModel);
				saveMenuPreparationModel.setOrderFunction(modelMapper.map(newOrderFunction, SaveMenuPreparationOrderFunctionModel.class));
				saveMenuPreparationModelList.add(saveMenuPreparationModel);
			}
		}

		orderMenuPreparationRepository.createAndUpdate(saveMenuPreparationModelList, newOrderId);
	}

	/**
	 * Resets and maps the custom package ID from the original to the new menu preparation model.
	 *
	 * @param savePreparationModel    the model being prepared for the new order
	 * @param getMenuPreparationModel the model from the original order
	 */
	private void resetCustomPackage(SaveMenuPreparationModel savePreparationModel, GetMenuPreparationModel getMenuPreparationModel) {
		if (getMenuPreparationModel.getCustomPackage() != null) {
			savePreparationModel.setCustomPackageId(getMenuPreparationModel.getCustomPackage().getId());
		}
	}

	/**
	 * Resets menu item IDs and detaches them from previous associations.
	 * Also copies menu item names from original to new items.
	 *
	 * @param savePreparationModel     the model being prepared for the new order
	 * @param getMenuPreparationModel  the model from the original order
	 */
	private void resetMenuItem(SaveMenuPreparationModel savePreparationModel, GetMenuPreparationModel getMenuPreparationModel) {
		if (getMenuPreparationModel.getMenuPreparationMenuItem() == null) {
			return;
		}
		List<GetMenuPreparationMenuItemModel> getMenuPreparationMenuItemModels = getMenuPreparationModel.getMenuPreparationMenuItem();
		List<SaveMenuPreparationMenuItemModel> saveMenuPreparationMenuItemModels = savePreparationModel.getMenuPreparationMenuItem();
		for (int i = 0; i < getMenuPreparationMenuItemModels.size(); i++) {
			GetMenuPreparationMenuItemModel getMenuPreparationMenuItemModel = getMenuPreparationMenuItemModels.get(i);
			SaveMenuPreparationMenuItemModel saveMenuPreparationMenuItemModel = saveMenuPreparationMenuItemModels.get(i);
			saveMenuPreparationMenuItemModel.setMenuItemNameDefaultLang(getMenuPreparationMenuItemModel.getMenuItemNameDefaultLang());
			saveMenuPreparationMenuItemModel.setMenuItemNamePreferLang(getMenuPreparationMenuItemModel.getMenuItemNamePreferLang());
			saveMenuPreparationMenuItemModel.setMenuItemNameSupportiveLang(getMenuPreparationMenuItemModel.getMenuItemNameSupportiveLang());
			saveMenuPreparationMenuItemModel.setId(null);
			saveMenuPreparationMenuItemModel.setMenuPreparation(null);
		}
	}

	/**
	 * Resets menu item category IDs and detaches them from any previous menu preparation association.
	 *
	 * @param savePreparationModel the model being prepared for the new order
	 */
	private void resetMenuItemCategory(SaveMenuPreparationModel savePreparationModel) {
		if (savePreparationModel.getMenuPreparationMenuItemCategory() != null) {
			savePreparationModel.getMenuPreparationMenuItemCategory().forEach(menuItemCategory -> {
				menuItemCategory.setId(null);
				menuItemCategory.setMenuPreparation(null);
			});
		}
	}

	/**
	 * Resets No Item IDs and detaches them from any previous menu preparation association.
	 *
	 * @param savePreparationModel the model being prepared for the new order
	 */
	private void resetNoItem(SaveMenuPreparationModel savePreparationModel) {
		if (savePreparationModel.getNoItems() != null) {
			savePreparationModel.getNoItems().forEach(noItem -> {
				noItem.setId(null);
				noItem.setMenuPreparation(null);
			});
		}
	}

}