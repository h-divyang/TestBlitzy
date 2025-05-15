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
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.SearchFieldDto;
import com.catering.dto.tenant.request.AllRawMaterialSupplierDto;
import com.catering.dto.tenant.request.RawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.RawMaterialModel;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.RawMaterialService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST controller for managing raw materials. <br>
 * This controller exposes endpoints for creating, updating, retrieving, 
 * and deleting raw materials. It also provides methods for checking raw material existence, fetching raw materials with contractors, 
 * and getting filtered raw material lists. <br>
 * <p>
 * The endpoints require specific user rights, ensuring that only authorized 
 * users can perform these actions.
 * </p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.RAW_MATERIAL)
@Tag(name = SwaggerConstant.RAW_MATERIAL)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialController {

	/**
	 * Service for managing messages, providing localized messages 
	 * for the application's responses.
	 */
	MessageService messageService;

	/**
	 * Service for managing the business logic related to raw materials, 
	 * including creation, update, retrieval, and deletion.
	 */
	RawMaterialService rawMaterialService;

	/**
	 * Endpoint to create a new raw material.
	 * Only users with appropriate permissions can add new raw material records.
	 * 
	 * @param rawMaterialDto the data transfer object containing raw material details.
	 * @return A ResponseContainerDto containing the created raw material details.
	 * @throws RestException if an error occurs during the creation of raw material.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<RawMaterialDto>> create(@Valid @RequestBody RawMaterialDto rawMaterialDto) throws RestException {
		Optional<RawMaterialDto> rawMaterialResponseDto = rawMaterialService.createAndUpdate(rawMaterialDto);
		return RequestResponseUtils.generateResponseDto(rawMaterialResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Endpoint to retrieve all raw materials with optional filtering and searching.
	 * 
	 * @param filterDto the filter criteria to apply.
	 * @param searchField the fields to search by.
	 * @return A ResponseContainerDto containing the list of raw materials.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<RawMaterialDto>> read(FilterDto filterDto, SearchFieldDto searchField) {
		return rawMaterialService.read(filterDto, searchField);
	}

	/**
	 * Endpoint to retrieve a unique list of raw materials based on certain conditions.
	 * Users with permissions for menu allocation and menu item management can access this endpoint.
	 * 
	 * @param IDs the list of raw material IDs.
	 * @param menuItemId the ID of the menu item.
	 * @param userEditId the ID of the user editing the material (optional).
	 * @return A ResponseContainerDto containing the list of raw materials matching the conditions.
	 */
	@GetMapping(value = ApiPathConstant.UNIQUE_LIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<RawMaterialResponseDto>> uniqueList(
			@RequestParam(value = Constants.IDS, required = false) List<Long> IDs,
			@RequestParam(value = Constants.MENU_ITEM_ID, required = false) Long menuItemId,
			@RequestParam(value = Constants.EXIST_ID, required = false) Long userEditId) {
		return rawMaterialService.uniqueList(RawMaterialResponseDto.class, RawMaterialModel.class, IDs, menuItemId, userEditId);
	}

	/**
	 * Endpoint to retrieve raw material details along with contractors.
	 * 
	 * @return A ResponseContainerDto containing the raw material and contractor details.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_WITH_CONTRACTOR_LIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<AllRawMaterialSupplierDto>> findRawMaterialDetails() {
		return RequestResponseUtils.generateResponseDto(rawMaterialService.findRawMaterialDetails());
	}

	/**
	 * Endpoint to update an existing raw material.
	 * Only users with the appropriate permissions can edit raw material records.
	 * 
	 * @param rawMaterialDto the data transfer object containing updated raw material details.
	 * @return A ResponseContainerDto containing the updated raw material details.
	 * @throws RestException if an error occurs during the update of raw material.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<RawMaterialDto>> update(@Valid @RequestBody RawMaterialDto rawMaterialDto) throws RestException {
		Optional<RawMaterialDto> rawMaterialResponseDto = rawMaterialService.createAndUpdate(rawMaterialDto);
		return RequestResponseUtils.generateResponseDto(rawMaterialResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint to delete a raw material by its ID.
	 * Only users with the appropriate permissions can delete raw material records.
	 * 
	 * @param idStr the ID of the raw material to delete.
	 * @return A ResponseContainerDto indicating the result of the delete operation.
	 * @throws RestException if an error occurs during the deletion of raw material.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String idStr) throws RestException {
		rawMaterialService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Endpoint to retrieve all active raw materials.
	 * 
	 * @return A ResponseContainerDto containing the list of active raw materials.
	 */
	@GetMapping(value = ApiPathConstant.ALL)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MEAL_TYPE + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MEAL_TYPE + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<RawMaterialDto>> findAllByIsActiveTrue() {
		return RequestResponseUtils.generateResponseDto(rawMaterialService.findAll());
	}

	/**
	 * Endpoint to check if a raw material exists by its ID.
	 * 
	 * @param id the ID of the raw material.
	 * @return A ResponseContainerDto containing a boolean indicating whether the raw material exists.
	 */
	@GetMapping(value = ApiPathConstant.CHECK_EXISTENCE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Boolean> isExist(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id) {
		return RequestResponseUtils.generateResponseDto(rawMaterialService.isExists(id));
	}

	/**
	 * Updates the priority of a raw material.
	 *
	 * <p>This endpoint is secured and requires the user to have edit rights for raw materials.
	 * The updated priority information is received through the request body.</p>
	 *
	 * @param rawMaterialDto the {@link RawMaterialDto} object containing the updated priority details
	 * @return a {@link ResponseContainerDto} containing a success message upon successful update
	 * @throws RestException if an error occurs during the update operation
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_PRIORITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Void> updatePriority(@Valid @RequestBody List<RawMaterialDto> list) throws RestException {
		rawMaterialService.updatePriority(list);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves the highest priority value from the raw material record.
	 * <p>
	 * This endpoint is protected and requires the user to have edit rights for raw materials.
	 * It returns the maximum value of the {@code priority} field from the database.
	 *
	 * @return a {@link ResponseContainerDto} containing the highest priority value as a {@code Long}
	 * @throws RestException if an error occurs while fetching the data
	 */
	@GetMapping(value = ApiPathConstant.HIGHEST_PRIORITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Long> highestPriority() throws RestException {
		return RequestResponseUtils.generateResponseDto(rawMaterialService.getHighestPriority());
	}

}