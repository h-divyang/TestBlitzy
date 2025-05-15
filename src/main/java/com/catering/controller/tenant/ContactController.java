package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.ContactCustomDto;
import com.catering.dto.tenant.request.ContactResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.ContactModel;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.ContactService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for managing Contact Categories, providing endpoints to create, view, update, and delete contacts.
 * <p>
 * The following API endpoints are available:
 * <ul>
 * <li><b>POST /contact</b> - Creates a new Contact Category. Requires add permissions.</li>
 * <li><b>GET /contact</b> - Retrieves all Contact Categories with optional filtering. Requires view permissions.</li>
 * <li><b>GET /contact/unique-list</b> - Retrieves a unique list of Contact Categories based on specified criteria. Requires add/edit permissions for Raw Material.</li>
 * <li><b>PUT /contact</b> - Updates an existing Contact Category. Requires edit permissions.</li>
 * <li><b>DELETE /contact/{id}</b> - Deletes a Contact Category by its ID. Requires delete permissions.</li>
 * <li><b>GET /contact/category-type/{id}</b> - Retrieves Contact Categories filtered by the category type ID. Requires multiple role-based permissions such as Menu Item, Book Order, and Menu Allocation.</li>
 * <li><b>GET /contact/active</b> - Retrieves all active contacts. Requires permissions for Journal Voucher.</li>
 * <li><b>GET /contact/bank-list</b> - Retrieves all bank details for contacts. Requires view permissions.</li>
 * </ul>
 * </p>
 * This controller handles CRUD operations for Contact Categories and provides endpoints to retrieve contact information based on various filters.
 * The methods use ContactService for the business logic, MessageService for localized success messages, and ExceptionService for handling errors.
 * All API endpoints enforce user rights based on roles and permissions defined in {@link ApiUserRightsConstants}.
 * <p>
 * @see ContactService
 * @see MessageService
 * @see ExceptionService
 */
@RestController
@RequestMapping(value = ApiPathConstant.CONTACT)
@Tag(name = SwaggerConstant.CONTACT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactController {

	/**
	 * Service for handling localization and returning appropriate messages based on the context.
	 * <p>
	 * The MessageService is used to retrieve localized success, error, or informational messages for API responses.
	 * It is injected into the controller to ensure that responses are consistent with the system's language settings.
	 * </p>
	 * @see MessageService
	 */
	MessageService messageService;

	/**
	 * Service for handling exceptions and throwing appropriate error responses when needed.
	 * <p>
	 * The ExceptionService is responsible for managing error handling, such as validating inputs or handling unexpected exceptions.
	 * It is injected into the controller to throw customized exceptions when necessary, ensuring that meaningful error messages are returned to the users.
	 * </p>
	 * @see ExceptionService
	 */
	ExceptionService exceptionService;

	/**
	 * Service for managing contact-related operations such as creation, reading, updating, and deletion of contacts.
	 * <p>
	 * The ContactService is the core service used by the controller to perform CRUD operations for Contact Categories.
	 * It contains the business logic for interacting with contact data and retrieving filtered contact lists based on various criteria.
	 * </p>
	 * @see ContactService
	 */
	ContactService contactService;

	/**
	 * Creates a new Contact Category.
	 * <p>
	 * This endpoint allows the creation of a new Contact Category by accepting a {@link ContactResponseDto}.
	 * The user must have permission to add contact categories, as specified in {@link ApiUserRightsConstants}.
	 * </p>
	 * 
	 * @param contactDto the contact category data to be created.
	 * @return a response containing the created contact category, with a success message.
	 * @throws RestException if any error occurs during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<ContactResponseDto>> create(@Valid @RequestBody ContactResponseDto contactDto) throws RestException {
		Optional<ContactResponseDto> contactResponseDto = contactService.createAndUpdate(contactDto);
		return RequestResponseUtils.generateResponseDto(contactResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves all Contact Categories.
	 * <p>
	 * This endpoint retrieves a list of all existing contact categories, with optional filtering based on {@link FilterDto}.
	 * The user must have permission to view contact categories, as specified in {@link ApiUserRightsConstants}.
	 * </p>
	 * 
	 * @param filterDto an optional filter object to filter the contact categories.
	 * @return a response containing the list of contact categories.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<ContactResponseDto>> read(FilterDto filterDto) {
		return contactService.read(filterDto);
	}

	/**
	 * Retrieves a unique list of Contact Categories.
	 * <p>
	 * This endpoint returns a unique list of contact categories based on various parameters, such as {@link Constants#IDS}, {@link Constants#RAW_MATERIAL_ID}, and {@link Constants#EXIST_ID}.
	 * The user must have permission to add or edit raw material categories, as specified in {@link ApiUserRightsConstants}.
	 * </p>
	 * 
	 * @param IDs list of contact IDs to filter by.
	 * @param rawMaterialId ID of the raw material.
	 * @param userEditId ID of the user editing the contact.
	 * @return a response containing the unique list of contact categories.
	 */
	@GetMapping(value = ApiPathConstant.UNIQUE_LIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<ContactResponseDto>> uniqueList(
			@RequestParam(value = Constants.IDS, required = false) List<Long> IDs,
			@RequestParam(value = Constants.RAW_MATERIAL_ID, required = false) Long rawMaterialId,
			@RequestParam(value = Constants.EXIST_ID, required = false) Long userEditId) {
		return contactService.uniqueList(ContactResponseDto.class, ContactModel.class, IDs, rawMaterialId, userEditId);
	}

	/**
	 * Updates an existing Contact Category.
	 * <p>
	 * This endpoint allows the user to update an existing Contact Category by providing a {@link ContactResponseDto}.
	 * The user must have permission to edit contact categories, as specified in {@link ApiUserRightsConstants}.
	 * </p>
	 * 
	 * @param contactDto the contact category data to be updated.
	 * @return a response containing the updated contact category, with a success message.
	 * @throws RestException if any error occurs during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<ContactResponseDto>> update(@Valid @RequestBody ContactResponseDto contactDto) throws RestException {
		Optional<ContactResponseDto> contactResponseDto = contactService.createAndUpdate(contactDto);
		return RequestResponseUtils.generateResponseDto(contactResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a Contact Category by ID.
	 * <p>
	 * This endpoint deletes a contact category by its unique ID. The user must have permission to delete contact categories, as specified in {@link ApiUserRightsConstants}.
	 * </p>
	 * 
	 * @param idStr the ID of the contact category to be deleted.
	 * @return a response indicating successful deletion.
	 * @throws RestException if the ID is invalid or an error occurs during deletion.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) throws RestException {
		if (!ValidationUtils.isNumber(idStr)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		contactService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves Contact Categories by Category Type.
	 * <p>
	 * This endpoint returns contact categories based on the provided category type ID. The user must have appropriate permissions, as specified in {@link ApiUserRightsConstants}.
	 * </p>
	 * 
	 * @param categoryTypeId the ID of the category type to filter by.
	 * @return a response containing the list of contact categories filtered by category type.
	 */
	@GetMapping(value = ApiPathConstant.CONTACT_BY_CATEGORY_TYPE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT,
			ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.BOOK_ORDER + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<ContactResponseDto>> getContactByCategoryType(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long categoryTypeId) {
		return RequestResponseUtils.generateResponseDto(contactService.findByCategoryType(categoryTypeId));
	}

	/**
	 * Retrieves a list of all active contacts.
	 * <p>
	 * This endpoint retrieves all active contacts, available for users with the appropriate permissions as specified in {@link ApiUserRightsConstants}.
	 * </p>
	 * 
	 * @return a response containing the list of active contacts.
	 */
	@GetMapping(value = ApiPathConstant.IS_ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.JOURNAL_VOUCHER + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.JOURNAL_VOUCHER + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<ContactCustomDto>> findAllContact() {
		return RequestResponseUtils.generateResponseDto(contactService.findAllContact());
	}

	/**
	 * Retrieves all bank details associated with contacts.
	 * <p>
	 * This endpoint retrieves a list of all bank details related to contacts, available for users with the appropriate permissions as specified in {@link ApiUserRightsConstants}.
	 * </p>
	 * 
	 * @return a response containing the list of bank details.
	 */
	@GetMapping(value = ApiPathConstant.BANK_LIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<ContactCustomDto>> findAllBankDetails() {
		return RequestResponseUtils.generateResponseDto(contactService.findAllBankList());
	}

}