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
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.MenuItemSubCategoryDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.MenuItemSubCategoryService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class responsible for managing operations related to Menu Item Subcategories.
 * 
 * This controller provides REST API endpoints for creating, reading, updating, and deleting
 * menu item subcategories. The endpoints are secured with user rights authorization to ensure 
 * that only authorized users can access these operations.
 * 
 * <p>Endpoints include:</p>
 * <ul>
 *   <li>POST /create - Create a new Menu Item Subcategory</li>
 *   <li>GET /read - Retrieve a list of Menu Item Subcategories, with optional filtering</li>
 *   <li>PUT /update - Update an existing Menu Item Subcategory</li>
 *   <li>DELETE /delete/{id} - Delete a Menu Item Subcategory by ID</li>
 *   <li>GET /active - Retrieve a list of active Menu Item Subcategories</li>
 * </ul>
 */
@RestController
@RequestMapping(value = ApiPathConstant.MENU_ITEM_SUB_CATEGORY)
@Tag(name = SwaggerConstant.MENU_ITEM_SUB_CATEGORY)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MenuItemSubCategoryController {

	/**
	 * Service responsible for handling message-related operations such as retrieving success/failure messages.
	 * Used in response generation to include human-readable messages.
	 */
	MessageService messageService;

	/**
	 * Service responsible for performing CRUD operations related to Menu Item Subcategories.
	 * Provides methods to create, read, update, and delete Menu Item Subcategories.
	 */
	MenuItemSubCategoryService menuItemSubCategoryService;

	/**
	 * Endpoint for creating a new Menu Item Subcategory.
	 * 
	 * This endpoint accepts a DTO containing the details of the new Menu Item Subcategory and passes it
	 * to the {@link MenuItemSubCategoryService#createAndUpdate} method. After the creation, it returns a response
	 * with the created subcategory and a success message.
	 * 
	 * @param menuItemSubCategoryDto DTO containing the data for the new subcategory.
	 * @return Response containing the created subcategory wrapped in an Optional and a success message.
	 * @throws RestException If any error occurs during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_SUB_CATEGORY + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<MenuItemSubCategoryDto>> create(@Valid @RequestBody MenuItemSubCategoryDto menuItemSubCategoryDto) throws RestException {
		Optional<MenuItemSubCategoryDto> menuItemSubCategoryResponse = menuItemSubCategoryService.createAndUpdate(menuItemSubCategoryDto);
		return RequestResponseUtils.generateResponseDto(menuItemSubCategoryResponse, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Endpoint for retrieving a list of Menu Item Subcategories.
	 * 
	 * This endpoint accepts filter criteria (optional) and retrieves a list of Menu Item Subcategories
	 * using the {@link MenuItemSubCategoryService#read} method.
	 * 
	 * @param filterDto Filter criteria for retrieving subcategories.
	 * @return Response containing the list of Menu Item Subcategories.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_SUB_CATEGORY + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<MenuItemSubCategoryDto>> read(FilterDto filterDto) {
		return menuItemSubCategoryService.read(filterDto);
	}

	/**
	 * Endpoint for updating an existing Menu Item Subcategory.
	 * 
	 * This endpoint accepts a DTO containing the updated details of the subcategory and passes it to
	 * the {@link MenuItemSubCategoryService#createAndUpdate} method to handle the update operation.
	 * 
	 * @param menuItemSubCategorydto DTO containing updated details for the subcategory.
	 * @return Response containing the updated Menu Item Subcategory wrapped in an Optional and a success message.
	 * @throws RestException If any error occurs during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_SUB_CATEGORY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<MenuItemSubCategoryDto>> update(@Valid @RequestBody MenuItemSubCategoryDto menuItemSubCategorydto) throws RestException {
		Optional<MenuItemSubCategoryDto> menuItemSubCategoryResponseDto = menuItemSubCategoryService.createAndUpdate(menuItemSubCategorydto);
		return RequestResponseUtils.generateResponseDto(menuItemSubCategoryResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint for deleting a Menu Item Subcategory by its ID.
	 * 
	 * This endpoint calls the {@link MenuItemSubCategoryService#deleteById} method to delete a specific
	 * subcategory based on its ID.
	 * 
	 * @param id The ID of the Menu Item Subcategory to be deleted.
	 * @return Response containing a success message indicating that the data was deleted.
	 * @throws RestException If any error occurs during the deletion process.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_SUB_CATEGORY + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id) throws RestException {
		menuItemSubCategoryService.deleteById(id);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Endpoint for retrieving a list of active Menu Item Subcategories.
	 * 
	 * This endpoint does not require any filter and simply retrieves all active subcategories using the
	 * {@link MenuItemSubCategoryService#findByIsActiveTrue} method.
	 * 
	 * @return Response containing the list of active Menu Item Subcategories.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_SUB_CATEGORY + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<MenuItemSubCategoryDto>> readActive() {
		return RequestResponseUtils.generateResponseDto(menuItemSubCategoryService.findByIsActiveTrue());
	}

}