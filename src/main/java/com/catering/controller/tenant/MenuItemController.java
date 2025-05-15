package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.catering.dto.common.SearchFieldDto;
import com.catering.dto.tenant.request.CustomMenuItem;
import com.catering.dto.tenant.request.MenuItemDto;
import com.catering.exception.RestException;
import com.catering.properties.FileProperties;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.MenuItemService;
import com.catering.util.FileUtils;
import com.catering.util.RequestResponseUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class for managing Menu Item operations.
 * 
 * This controller exposes endpoints to create, read, update, and delete Menu Items. 
 * It handles requests related to Menu Item creation, updates, deletion, and status modification. 
 * The operations are authorized based on user rights and security roles. 
 * The controller leverages the {@link MenuItemService} for business logic and 
 * {@link MessageService} to fetch localized success/failure messages.
 * 
 * <p>Endpoints include:</p>
 * <ul>
 *   <li>POST /create - Create a new Menu Item</li>
 *   <li>GET /read - Retrieve a list of Menu Items based on filters and search fields</li>
 *   <li>GET /names - Retrieve Menu Items with only names</li>
 *   <li>PUT /update - Update an existing Menu Item</li>
 *   <li>PATCH /update-status - Update the status of a Menu Item (active/inactive)</li>
 * </ul>
 */
@RestController
@RequestMapping(value = ApiPathConstant.MENU_ITEM)
@Tag(name = SwaggerConstant.MENU_ITEM)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuItemController {

	Logger logger = LoggerFactory.getLogger(MenuItemController.class);

	/**
	 * Service for retrieving localized messages and error handling. 
	 * Used for generating user-friendly messages for success/failure responses.
	 */
	MessageService messageService;

	/**
	 * Service responsible for managing business logic related to Menu Items. 
	 * Includes operations for creating, updating, deleting, and retrieving Menu Items.
	 */
	MenuItemService menuItemService;

	/**
	 * Configuration properties for file uploads, including max size for image files.
	 * Used for validating uploaded files to ensure they meet size constraints.
	 */
	FileProperties fileProperties;

	/**
	 * Service for handling exception generation and throwing customized error messages when needed.
	 * Used for managing invalid inputs and other request processing errors.
	 */
	ExceptionService exceptionService;

	/**
	 * ObjectMapper instance for mapping JSON strings to Java objects (DTOs).
	 * Used for deserializing Menu Item JSON payloads into the {@link MenuItemDto} objects.
	 */
	ObjectMapper objectMapper;

	/**
	 * Endpoint to create a new Menu Item.
	 * 
	 * This endpoint receives a JSON string representing the {@link MenuItemDto} and optional files (image).
	 * It validates the files and creates a new Menu Item using {@link MenuItemService#createAndUpdate}.
	 * 
	 * @param menuItemDto The Menu Item DTO in JSON format.
	 * @param img The image file associated with the Menu Item (optional).
	 * @return Response containing the created Menu Item wrapped in an Optional and a success message.
	 * @throws RestException If there are validation errors or issues during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<MenuItemDto>> create(@Valid @RequestParam(required = true) String menuItemDto, @RequestParam(name= Constants.IMG, required = false) MultipartFile img) throws RestException {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(img, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		MenuItemDto dto = null;
		try {
			dto = objectMapper.readValue(menuItemDto, MenuItemDto.class);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
		return RequestResponseUtils.generateResponseDto(menuItemService.createAndUpdate(dto, img), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Endpoint to retrieve all Menu Items based on filters and search fields.
	 * 
	 * This endpoint supports optional filters such as page number, page size, sorting, and search query.
	 * The {@link MenuItemService#read} method is used to fetch the Menu Items based on the provided parameters.
	 * 
	 * @param filterDto Filter criteria for the request.
	 * @param searchField Search criteria for the request.
	 * @param request The HTTP request object.
	 * @return Response containing the list of Menu Items based on the given filters.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<MenuItemDto>> read(FilterDto filterDto, SearchFieldDto searchField, HttpServletRequest request) {
		return menuItemService.read(filterDto, searchField, request);
	}

	/**
	 * Endpoint to retrieve only the names of Menu Items.
	 * 
	 * This endpoint fetches a simplified list of Menu Items containing only their names, using the 
	 * {@link MenuItemService#read} method with no additional filters.
	 * 
	 * @return Response containing the list of Menu Item names.
	 */
	@GetMapping(value = ApiPathConstant.NAMES)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CustomMenuItem>> read() {
		return menuItemService.read();
	}
	
	/**
	 * Endpoint to update an existing Menu Item.
	 * 
	 * This endpoint receives a JSON string representing the updated {@link MenuItemDto} and optional files (image).
	 * It validates the files and updates the Menu Item using {@link MenuItemService#createAndUpdate}.
	 * 
	 * @param menuItemDto The updated Menu Item DTO in JSON format.
	 * @param img The updated image file (optional).
	 * @return Response containing the updated Menu Item and a success message.
	 * @throws RestException If there are validation errors or issues during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<MenuItemDto> update(@Valid @RequestParam(required = true) String menuItemDto, @RequestParam(name= Constants.IMG, required = false) MultipartFile img) throws RestException {
		ErrorGenerator errors = ErrorGenerator.builder();
		errors.getErrors().putAll(FileUtils.imageValidate(img, fileProperties.getImageMaxSize(), messageService).getErrors());
		if (errors.hasError()) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.VALIDATION_INVALID_INPUT), errors.getErrors());
		}
		MenuItemDto dto = null;
		try {
			dto = objectMapper.readValue(menuItemDto, MenuItemDto.class);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
		Optional<MenuItemDto> menuItemResponseDto = menuItemService.createAndUpdate(dto, img);
		return RequestResponseUtils.generateResponseDto(menuItemResponseDto.get(), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint to delete a Menu Item by ID, with optional removal of associated image.
	 * 
	 * This endpoint deletes a Menu Item and optionally removes associated image files.
	 * It uses {@link MenuItemService#deleteById} to perform the deletion.
	 * 
	 * @param idStr The ID of the Menu Item to be deleted.
	 * @param isImage A flag indicating whether to remove both the image.
	 * @return Response containing a success message.
	 * @throws RestException If an error occurs during the deletion process.
	 */
	@DeleteMapping(value = ApiPathConstant.ID + ApiPathConstant.IS_IMAGE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String idStr, @PathVariable(name = FieldConstants.IS_IMAGE) Boolean isImage) throws RestException {
		menuItemService.deleteById(Long.valueOf(idStr), isImage);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/** Endpoint to update the status (active/inactive) of a Menu Item.
	 * 
	 * This endpoint modifies the status of a Menu Item based on its ID. It uses 
	 * {@link MenuItemService#updateStatus} to perform the status update.
	 * 
	 * @param menuItemDto DTO containing the ID and new status of the Menu Item.
	 * @return Response containing the updated Menu Item and a success message.
	 */
	@PatchMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<MenuItemDto>> updateStatus(@Valid @RequestBody MenuItemDto menuItemDto) {
		return RequestResponseUtils.generateResponseDto(menuItemService.updateStatus(menuItemDto.getId(), menuItemDto.getIsActive()), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Updates the priority of a menu item.
	 *
	 * <p>This endpoint is secured and requires the user to have edit rights for menu items.
	 * The updated priority information is received through the request body.</p>
	 *
	 * @param menuItemDto the {@link MenuItemDto} object containing the updated priority details
	 * @return a {@link ResponseContainerDto} containing a success message upon successful update
	 * @throws RestException if an error occurs during the update operation
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_PRIORITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Void> updatePriority(@Valid @RequestBody List<MenuItemDto> menuItems) throws RestException {
		menuItemService.updatePriority(menuItems);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves the highest priority value from the menu item records.
	 * <p>
	 * This endpoint is protected and requires the user to have edit rights for menu items.
	 * It returns the maximum value of the {@code priority} field from the database.
	 *
	 * @return a {@link ResponseContainerDto} containing the highest priority value as a {@code Long}
	 * @throws RestException if an error occurs while fetching the data
	 */
	@GetMapping(value = ApiPathConstant.HIGHEST_PRIORITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Long> highestPriority() throws RestException {
		return RequestResponseUtils.generateResponseDto(menuItemService.getHighestPriority());
	}

}