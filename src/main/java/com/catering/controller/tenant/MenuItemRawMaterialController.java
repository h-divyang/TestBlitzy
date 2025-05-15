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
import com.catering.dto.tenant.request.MenuItemRawMaterialDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.MenuItemRawMaterialService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class responsible for managing Menu Item Raw Material operations.
 * 
 * This controller exposes endpoints to create, read, update, and delete Menu Item Raw Materials. 
 * These operations are authorized based on user rights and security roles. The controller 
 * uses {@link MenuItemRawMaterialService} for business logic and {@link MessageService} to retrieve localized messages.
 * 
 * <p>Endpoints include:</p>
 * <ul>
 *   <li>POST /create - Create a new Menu Item Raw Material</li>
 *   <li>POST /list - Create multiple Menu Item Raw Materials by copying a recipe list</li>
 *   <li>GET /read - Retrieve a list of Menu Item Raw Materials with optional filters</li>
 *   <li>PUT /update - Update an existing Menu Item Raw Material</li>
 *   <li>DELETE /delete/{id} - Delete a Menu Item Raw Material by its ID</li>
 * </ul>
 */
@RestController
@RequestMapping(value = ApiPathConstant.MENU_ITEM_RAW_MATERIAL)
@Tag(name = SwaggerConstant.MENU_ITEM_RAW_MATERIAL)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuItemRawMaterialController {

	/**
	 * Service responsible for handling message-related operations such as retrieving localized success/failure messages.
	 * Used in response generation to include user-friendly messages for various operations like create, update, and delete.
	 */
	MessageService messageService;

	/**
	 * Service responsible for managing business logic related to Menu Item Raw Materials.
	 * Includes operations for creating, reading, updating, and deleting Menu Item Raw Materials.
	 */
	MenuItemRawMaterialService menuItemRawMaterialService;

	/**
	 * Endpoint for creating a new Menu Item Raw Material.
	 * 
	 * This endpoint receives a {@link MenuItemRawMaterialDto} object containing details about the raw material and 
	 * passes it to the {@link MenuItemRawMaterialService#createAndUpdate} method to perform the create operation.
	 * 
	 * @param menuItemRawMaterialDto DTO containing the data for the new raw material.
	 * @return Response containing the created raw material wrapped in an Optional and a success message.
	 * @throws RestException If an error occurs during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Optional<MenuItemRawMaterialDto>> create(@Valid @RequestBody MenuItemRawMaterialDto menuItemRawMaterialDto) throws RestException {
		Optional<MenuItemRawMaterialDto> menuItemRawMaterialResponseDto = menuItemRawMaterialService.createAndUpdate(menuItemRawMaterialDto);
		return RequestResponseUtils.generateResponseDto(menuItemRawMaterialResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	  * Endpoint for creating a list of Menu Item Raw Materials by copying a recipe.
	  * 
	  * This endpoint receives a list of {@link MenuItemRawMaterialDto} objects, processes them, and
	  * calls the {@link MenuItemRawMaterialService#createCopyRecipeList} method to handle the batch creation of raw materials.
	  * 
	  * @param menuItemRawMaterialDtos List of DTOs containing data for multiple raw materials.
	  * @return Response containing the list of created raw materials and a success message.
	  * @throws RestException If an error occurs during the creation process.
	  */
	@PostMapping(value = ApiPathConstant.LIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<MenuItemRawMaterialDto>> createCopyRecipeList(@RequestBody List<MenuItemRawMaterialDto> menuItemRawMaterialDtos) throws RestException {
		List<MenuItemRawMaterialDto> menuItemRawMaterialResponseDto = menuItemRawMaterialService.createCopyRecipeList(menuItemRawMaterialDtos);
		return RequestResponseUtils.generateResponseDto(menuItemRawMaterialResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Endpoint for retrieving all Menu Item Raw Materials with optional filters.
	 * 
	 * This endpoint retrieves raw materials based on the provided filters and pagination parameters.
	 * It passes the parameters to the {@link MenuItemRawMaterialService#read} method to fetch the raw materials.
	 * 
	 * @param menuItemId The ID of the menu item (optional).
	 * @param page The current page number (optional).
	 * @param size The number of records per page (optional).
	 * @param sortBy The field by which to sort the results (optional).
	 * @param sortDirection The direction in which to sort (optional).
	 * @param query The search query for filtering (optional).
	 * @return Response containing the list of raw materials based on the provided filters.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<MenuItemRawMaterialDto>> read(
			@RequestParam(value = Constants.MENU_ITEM_ID, required = false) String menuItemId,
			@RequestParam(value = Constants.CURRENT_PAGE, required = false) String page,
			@RequestParam(value = Constants.SIZE_PER_PAGE, required = false) String size,
			@RequestParam(value = Constants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = Constants.SORT_DIRECTION, required = false) String sortDirection,
			@RequestParam(value = Constants.QUERY, required = false) String query) {
		return menuItemRawMaterialService.read(Long.valueOf(menuItemId), page, size, sortBy, sortDirection, query);
	}

	/**
	 * Endpoint for updating an existing Menu Item Raw Material.
	 * 
	 * This endpoint receives a {@link MenuItemRawMaterialDto} object with updated information and calls
	 * the {@link MenuItemRawMaterialService#createAndUpdate} method to perform the update operation.
	 * 
	 * @param menuItemRawMaterialDto DTO containing the updated details for the raw material.
	 * @return Response containing the updated raw material and a success message.
	 * @throws RestException If an error occurs during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<MenuItemRawMaterialDto> update(@Valid @RequestBody MenuItemRawMaterialDto menuItemRawMaterialDto) throws RestException {
		Optional<MenuItemRawMaterialDto> menuItemRawMaterialResponseDto = menuItemRawMaterialService.createAndUpdate(menuItemRawMaterialDto);
		return RequestResponseUtils.generateResponseDto(menuItemRawMaterialResponseDto.get(), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint for deleting a Menu Item Raw Material by its ID.
	 * 
	 * This endpoint deletes a raw material identified by its ID by calling the 
	 * {@link MenuItemRawMaterialService#deleteById} method.
	 * 
	 * @param idStr The ID of the Menu Item Raw Material to be deleted.
	 * @return Response containing a success message indicating that the raw material was deleted.
	 * @throws RestException If an error occurs during the deletion process.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String idStr) throws RestException {
		menuItemRawMaterialService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

}