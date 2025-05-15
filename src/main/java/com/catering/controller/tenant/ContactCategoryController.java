package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
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
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.ContactCategoryDto;
import com.catering.dto.tenant.request.ContactCategoryWithContactsDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.ContactCategoryService;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * ContactCategoryController handles the operations related to Contact Categories.
 * It provides REST end points for creating, retrieving, updating, and deleting contact categories,
 * as well as specific operations such as fetching active contact categories or related contact data.
 */
@RestController
@RequestMapping(value = ApiPathConstant.CONTACT_CATEGORY)
@Tag(name = SwaggerConstant.CONTACT_CATEGORY)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactCategoryController {

	/**
	 * Service for handling messages and retrieving localized or predefined messages.
	 */
	MessageService messageService;

	/**
	 * Service for managing contact category operations, such as create, update, read, and delete.
	 */
	ContactCategoryService contactCategoryService;

	/**
	 * Creates a new Contact Category based on the provided ContactCategoryDto.
	 *
	 * @param contactCategoryDto The details of the contact category to be created; must be validated before processing.
	 * @return A ResponseContainerDto containing an Optional ContactCategoryDto if the creation is successful, or an empty Optional if the creation fails.
	 * @throws RestException If any error occurs during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT_CATEGORY + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<ContactCategoryDto>> create(@Valid @RequestBody ContactCategoryDto contactCategoryDto) throws RestException {
		Optional<ContactCategoryDto> contactCategoryResponseDto = contactCategoryService.createAndUpdate(contactCategoryDto);
		return RequestResponseUtils.generateResponseDto(contactCategoryResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves a list of contact categories based on the provided filter criteria.
	 *
	 * @param filterDto The filtering criteria including pagination, sorting, and query parameters.
	 * @return A ResponseContainerDto containing a list of ContactCategoryDto objects that match the filter criteria.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT_CATEGORY + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<ContactCategoryDto>> read(FilterDto filterDto) {
		return contactCategoryService.read(filterDto);
	}

	/**
	 * Retrieves a list of active contact categories.
	 *
	 * @return A ResponseContainerDto containing a list of ContactCategoryDto objects that are marked as active.
	 * @throws RestException If an error occurs during data retrieval.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.CONTACT + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<ContactCategoryDto>> readActiveAndPriority() throws RestException {
		return RequestResponseUtils.generateResponseDto(contactCategoryService.findByIsActiveTrueOrderByPriorityAscIdAsc());
	}

	/**
	 * Retrieves a list of contact categories along with their associated active contacts, filtered by the optional contact category type ID.
	 *
	 * @param contactCategoryTypeId An optional parameter specifying the ID of the contact category type to filter the results by. If null, all categories with active contacts are retrieved.
	 * @return A ResponseContainerDto containing a list of ContactCategoryWithContactsDto objects, which include contact categories and their associated active contacts.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE + ApiPathConstant.CONTACT_CATEGORY_WITH_CONTACTS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<ContactCategoryWithContactsDto>> findContactCategoryWithContacts(@RequestParam(required = false) Long contactCategoryTypeId) {
		return RequestResponseUtils.generateResponseDto(contactCategoryService.findByIsActiveTrueAndContactIsActiveTrue(contactCategoryTypeId));
	}

	/**
	 * Updates an existing Contact Category based on the provided ContactCategoryDto.
	 *
	 * @param contactCategoryDto The details of the contact category to be updated; must be validated before processing.
	 * @return A ResponseContainerDto containing the updated ContactCategoryDto.
	 * @throws RestException If any error occurs during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT_CATEGORY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<ContactCategoryDto> update(@Valid @RequestBody ContactCategoryDto contactCategoryDto) throws RestException {
		Optional<ContactCategoryDto> contactCategoryResponseDto = contactCategoryService.createAndUpdate(contactCategoryDto);
		return RequestResponseUtils.generateResponseDto(contactCategoryResponseDto.get(), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a Contact Category by its ID.
	 *
	 * @param idStr The ID of the Contact Category to be deleted as a String.
	 * @return A ResponseContainerDto containing a null body and a message confirming the deletion.
	 * @throws RestException If an error occurs during the deletion process.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT_CATEGORY + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String idStr) throws RestException {
		contactCategoryService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves a list of contact categories filtered by the specified category type ID.
	 *
	 * @param categoryTypeId The ID of the category type to filter the contact categories.
	 * @return A ResponseContainerDto containing a list of ContactCategoryDto objects that match the specified category type.
	 */
	@GetMapping(value = ApiPathConstant.CONTACT_BY_CATEGORY_TYPE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<ContactCategoryDto>> getCategoryByCategoryType(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long categoryTypeId) {
		return RequestResponseUtils.generateResponseDto(contactCategoryService.findByCategoryType(categoryTypeId));
	}

	/**
	 * Updates the priority of a contact category.
	 *
	 * <p>This endpoint is secured and requires the user to have edit rights for contact categories.
	 * The updated priority information is received through the request body.</p>
	 *
	 * @param contactCategoryDto the {@link ContactCategoryDto} object containing the updated priority details
	 * @return a {@link ResponseContainerDto} containing a success message upon successful update
	 * @throws RestException if an error occurs during the update operation
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_PRIORITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT_CATEGORY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Void> updatePriority(@Valid @RequestBody List<ContactCategoryDto> contactCategories) throws RestException {
		contactCategoryService.updatePriority(contactCategories);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves the highest priority value from the contact category records.
	 * <p>
	 * This endpoint is protected and requires the user to have edit rights for contact categories.
	 * It returns the maximum value of the {@code priority} field from the database.
	 *
	 * @return a {@link ResponseContainerDto} containing the highest priority value as a {@code Long}
	 * @throws RestException if an error occurs while fetching the data
	 */
	@GetMapping(value = ApiPathConstant.HIGHEST_PRIORITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CONTACT_CATEGORY + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Long> highestPriority() throws RestException {
		return RequestResponseUtils.generateResponseDto(contactCategoryService.getHighestPriority());
	}

}