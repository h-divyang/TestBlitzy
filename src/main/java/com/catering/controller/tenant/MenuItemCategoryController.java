package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.ErrorGenerator;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.MenuItemCategoryAndMenuItemDto;
import com.catering.dto.tenant.request.MenuItemCategoryDto;
import com.catering.exception.RestException;
import com.catering.properties.FileProperties;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.MenuItemCategoryService;
import com.catering.util.FileUtils;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class for managing Menu Item Category operations.
 * 
 * This controller exposes endpoints to create, read, update, delete, and manage the status of Menu Item Categories. 
 * It integrates with {@link MenuItemCategoryService} to handle business logic and utilizes {@link MessageService} 
 * for generating user-friendly messages.
 * 
 * <p>Endpoints include:</p>
 * <ul>
 *   <li>POST /create - Create a new Menu Item Category</li>
 *   <li>GET /read - Retrieve a list of Menu Item Categories based on filters</li>
 *   <li>GET /isActive - Retrieve Menu Item Categories that are active</li>
 *   <li>GET /isActive/menuItemExist - Retrieve Menu Item Categories with active status and associated Menu Items</li>
 *   <li>PUT /update - Update an existing Menu Item Category</li>
 *   <li>DELETE /delete/{id}/{isImage} - Delete a Menu Item Category by ID, optionally removing associated image</li>
 *   <li>PATCH /update-status - Update the status (active/inactive) of a Menu Item Category</li>
 *   <li>GET /menuItemExist - Retrieve Menu Item Categories with associated Menu Items</li>
 * </ul>
 */
@RestController
@RequestMapping(value = ApiPathConstant.MENU_ITEM_CATEGORY)
@Tag(name = SwaggerConstant.MENU_ITEM_CATEGORY)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuItemCategoryController {

	/**
	 * Service for retrieving localized messages and error handling. 
	 * Used to generate messages for success or failure scenarios.
	 */
	MessageService messageService;

	/**
	 * Service responsible for managing business logic related to Menu Item Categories. 
	 * Handles creation, updates, deletions, and retrievals of Menu Item Categories.
	 */
	MenuItemCategoryService menuItemCategoryService;

	/**
	 * Configuration properties for file uploads, such as max size for image files.
	 * Used to validate uploaded files.
	 */
	FileProperties fileProperties;

	/**
	 * Service for handling exceptions and throwing customized error messages during request processing.
	 * Used to handle invalid inputs and other exceptions.
	 */
	ExceptionService exceptionService;

	/**
	 * Endpoint to create a new Menu Item Category.
	 * 
	 * This endpoint accepts a JSON representation of the MenuItemCategoryDto, along with an optional image file.
	 * It performs validation on the image file and creates a new Menu Item Category using the {@link MenuItemCategoryService}.
	 * 
	 * @param menuItemCategoryDto The DTO representing the Menu Item Category to be created.
	 * @param img The image file associated with the Menu Item Category (optional).
	 * @return Response containing the created Menu Item Category and a success message.
	 * @throws RestException If there are any validation errors or issues during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_CATEGORY + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<MenuItemCategoryDto>> create(@Valid MenuItemCategoryDto menuItemCategoryDto, @RequestParam(name= Constants.IMG, required = false) MultipartFile img) throws RestException {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(img, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		Optional<MenuItemCategoryDto> menuItemCategoryResponseDto = menuItemCategoryService.createAndUpdate(menuItemCategoryDto, img);
		return RequestResponseUtils.generateResponseDto(menuItemCategoryResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Endpoint to retrieve all Menu Item Categories based on filters and pagination.
	 * 
	 * This endpoint supports optional filters such as page number, page size, and sorting.
	 * It returns a list of Menu Item Categories using {@link MenuItemCategoryService#read}.
	 * 
	 * @param filterDto The filtering criteria for the request.
	 * @param request The HTTP request object.
	 * @return Response containing the list of Menu Item Categories.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_CATEGORY + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<MenuItemCategoryDto>> read(FilterDto filterDto, HttpServletRequest request) {
		return menuItemCategoryService.read(filterDto, request);
	}

	/**
	 * Endpoint to retrieve Menu Item Categories that are active.
	 * 
	 * This endpoint fetches all Menu Item Categories with an active status using the {@link MenuItemCategoryService}.
	 * 
	 * @return Response containing the list of active Menu Item Categories.
	 */
	@GetMapping(value = ApiPathConstant.IS_ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<MenuItemCategoryDto>> getByIsActive() {
		return RequestResponseUtils.generateResponseDto(menuItemCategoryService.readDataByIsActive());
	}

	/**
	 * Endpoint to retrieve Menu Item Categories that are active and have associated Menu Items.
	 * 
	 * This endpoint fetches Menu Item Categories that are both active and have at least one associated Menu Item.
	 * It uses the {@link MenuItemCategoryService} to retrieve the data.
	 * 
	 * @param request The HTTP request object containing tenant information.
	 * @return Response containing the list of active Menu Item Categories with Menu Items.
	 */
	@GetMapping(value = ApiPathConstant.IS_ACTIVE + ApiPathConstant.MENU_ITEM_EXIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CUSTOM_PACKAGE + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.CUSTOM_PACKAGE + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<MenuItemCategoryAndMenuItemDto>> getByIsActiveAndFinal(HttpServletRequest request) {
		return RequestResponseUtils.generateResponseDto(menuItemCategoryService.readDataByIsActiveMenuItemExist(request.getAttribute(Constants.TENANT).toString()));
	}

	/**
	 * Endpoint to update an existing Menu Item Category.
	 * 
	 * This endpoint accepts a JSON representation of the MenuItemCategoryDto and an optional image file.
	 * It performs validation on the image file and updates the Menu Item Category using {@link MenuItemCategoryService}.
	 * 
	 * @param menuItemCategoryDto The updated Menu Item Category DTO.
	 * @param img The updated image file (optional).
	 * @return Response containing the updated Menu Item Category and a success message.
	 * @throws RestException If there are validation errors or issues during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_CATEGORY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<MenuItemCategoryDto> update(@Valid MenuItemCategoryDto menuItemCategoryDto, @RequestParam(name= Constants.IMG, required = false) MultipartFile img) throws RestException {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(img, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		Optional<MenuItemCategoryDto> menuItemCategoryResponseDto = menuItemCategoryService.createAndUpdate(menuItemCategoryDto, img);
		return RequestResponseUtils.generateResponseDto(menuItemCategoryResponseDto.get(), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint to delete a Menu Item Category by ID, optionally removing the associated image.
	 * 
	 * This endpoint deletes a Menu Item Category and can optionally remove the associated image.
	 * It uses {@link MenuItemCategoryService#deleteById} to perform the deletion.
	 * 
	 * @param idStr The ID of the Menu Item Category to be deleted.
	 * @param isImage A flag indicating whether to remove the associated image.
	 * @return Response containing a success message.
	 * @throws RestException If an error occurs during the deletion process.
	 */
	@DeleteMapping(value = ApiPathConstant.ID + ApiPathConstant.IS_IMAGE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_CATEGORY + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String idStr, @PathVariable(name = FieldConstants.IS_IMAGE) Boolean isImage) throws RestException {
		menuItemCategoryService.deleteById(Long.valueOf(idStr), isImage);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Endpoint to update the status (active/inactive) of a Menu Item Category.
	 * 
	 * This endpoint updates the status of a Menu Item Category using {@link MenuItemCategoryService#updateStatus}.
	 * 
	 * @param menuItemCategoryDto DTO containing the ID and new status of the Menu Item Category.
	 * @return Response containing the updated Menu Item Category and a success message.
	 */
	@PatchMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_CATEGORY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<MenuItemCategoryDto>> updateStatus(@Valid @RequestBody MenuItemCategoryDto menuItemCategoryDto) {
		return RequestResponseUtils.generateResponseDto(menuItemCategoryService.updateStatus(menuItemCategoryDto.getId(), menuItemCategoryDto.getIsActive()), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint to retrieve Menu Item Categories that have associated Menu Items.
	 * 
	 * This endpoint fetches all Menu Item Categories that have associated Menu Items.
	 * 
	 * @return Response containing the list of Menu Item Categories with associated Menu Items.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ITEM_EXIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<MenuItemCategoryDto>> getByMenuItemExist() {
		return RequestResponseUtils.generateResponseDto(menuItemCategoryService.findByMenuItemsNotNullOrderByPriority());
	}

	/**
	 * Updates the priority of a menu item category.
	 *
	 * <p>This endpoint is secured and requires the user to have edit rights for menu item categories.
	 * The updated priority information is received through the request body.</p>
	 *
	 * @param MmenuItemCategoryDto the {@link MenuItemCategoryDto} object containing the updated priority details
	 * @return a {@link ResponseContainerDto} containing a success message upon successful update
	 * @throws RestException if an error occurs during the update operation
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_PRIORITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_CATEGORY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Void> updatePriority(@Valid @RequestBody List<MenuItemCategoryDto> menuItemCategories) throws RestException {
		menuItemCategoryService.updatePriority(menuItemCategories);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves the highest priority value from the menu item category records.
	 * <p>
	 * This endpoint is protected and requires the user to have edit rights for menu item categories.
	 * It returns the maximum value of the {@code priority} field from the database.
	 *
	 * @return a {@link ResponseContainerDto} containing the highest priority value as a {@code Long}
	 * @throws RestException if an error occurs while fetching the data
	 */
	@GetMapping(value = ApiPathConstant.HIGHEST_PRIORITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM_CATEGORY + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Long> highestPriority() throws RestException {
		return RequestResponseUtils.generateResponseDto(menuItemCategoryService.getHighestPriority());
	}

}