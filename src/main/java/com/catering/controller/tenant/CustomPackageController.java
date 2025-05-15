package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;
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
import com.catering.dto.tenant.request.CustomPackageDto;
import com.catering.dto.tenant.request.CustomPackageListResponseDto;
import com.catering.dto.tenant.request.CustomPackageRecordResponseDto;
import com.catering.dto.tenant.request.SaveCustomPackageRecordRequestDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.GetMenuPreparationForMenuItemCategoryModel;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.CustomPackageService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class responsible for handling requests related to Custom Package operations.
 * Provides endpoints for CRUD operations on custom packages.
 * 
 * @author Priyansh Patel
 */

@RestController
@RequestMapping(value = ApiPathConstant.CUSTOM_PACKAGE)
@Tag(name = SwaggerConstant.CUSTOM_PACKAGE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomPackageController {

	/**
	 * Service for handling business logic related to custom package.
	 *
	 * This service provides methods for creating, updating, retrieving, and deleting custom package records.
	 */
	CustomPackageService customPackageService;

	/**
	 * Service responsible for obtaining localized messages.
	 */
	MessageService messageService;

	/**
	 * Service used for handling and throwing custom exceptions within the application.
	 */
	ExceptionService exceptionService;

	/**
	 * Creates a new custom package.
	 *
	 * @param saveCustomPackageRecordRequestDto the DTO containing the data for the new custom package. It must be a valid package.
	 * @return ResponseContainerDto containing a success message and the request body for the created custom package.
	 * @throws RestException if there is an error during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CUSTOM_PACKAGE + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<SaveCustomPackageRecordRequestDto> create(@Valid @RequestBody SaveCustomPackageRecordRequestDto saveCustomPackageRecordRequestDto) {
		customPackageService.createAndUpdate(saveCustomPackageRecordRequestDto);
		return RequestResponseUtils.generateResponseDto(new SaveCustomPackageRecordRequestDto(), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates an existing custom package.
	 *
	 * @param saveCustomPackageRecordRequestDto the DTO containing the updated data for the custom package. It must contain valid fields.
	 * @return ResponseContainerDto containing a success message and the updated request body.
	 * @throws RestException if there is an error during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CUSTOM_PACKAGE + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<SaveCustomPackageRecordRequestDto> update(@Valid @RequestBody SaveCustomPackageRecordRequestDto saveCustomPackageRecordRequestDto) {
		customPackageService.createAndUpdate(saveCustomPackageRecordRequestDto);
		return RequestResponseUtils.generateResponseDto(new SaveCustomPackageRecordRequestDto(), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves a list of custom packages based on the provided filters(searching, sorting, and pagination).
	 *
	 * @param filterDto the DTO containing the filter criteria(searching, sorting, and pagination) to apply for fetching custom packages.
	 * @return ResponseContainerDto containing a list of custom packages that match the filter criteria.
	 * @throws RestException if there is an issue with the filtering process.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CUSTOM_PACKAGE + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CustomPackageListResponseDto>> read(FilterDto filterDto) {
		return customPackageService.read(filterDto);
	}

	/**
	 * Retrieves a custom package record by its ID.
	 *
	 * @param id the ID of the custom package to retrieve.
	 * @return ResponseContainerDto containing the custom package record.
	 * @throws RestException if the custom package ID is invalid or an error occurs.
	 */
	@GetMapping(value = ApiPathConstant.GET_CUSTOM_PACKAGE_RECORD)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CUSTOM_PACKAGE + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<CustomPackageRecordResponseDto> getById(@RequestParam(value = FieldConstants.COMMON_FIELD_CUSTOM_PACKAGE_ID, required = true) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String id) throws RestException {
		return RequestResponseUtils.generateResponseDto(customPackageService.getById(Long.valueOf(id)));
	}

	/**
	 * Deletes a custom package by its ID.
	 *
	 * @param idStr the ID of the custom package to delete.
	 * @return ResponseContainerDto containing a success message.
	 * @throws RestException if the custom package ID is invalid or if an error occurs during the deletion process.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CUSTOM_PACKAGE + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) {
		if(!ValidationUtils.isNumber(idStr)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		customPackageService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Updates the status of a custom package (active/inactive).
	 *
	 * @param customPackageListResponse the DTO containing the custom package ID and the status to update.
	 * @return ResponseContainerDto containing the updated custom package status.
	 * @throws RestException if there is an error during the status update process.
	 */
	@PatchMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CUSTOM_PACKAGE + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<CustomPackageListResponseDto>> updateStatus(@Valid @RequestBody CustomPackageListResponseDto customPackageListResponse) {
		return RequestResponseUtils.generateResponseDto(customPackageService.updateStatus(customPackageListResponse.getId(), customPackageListResponse.getIsActive()), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves menu item categories and items for a specific custom package.
	 * It is used at custom package record page.
	 *
	 * @param customPackageId the ID of the custom package.
	 * @param request the HTTP request object containing tenant information.
	 * @return ResponseContainerDto containing a list of menu item categories.
	 * @throws RestException if there is an issue with fetching the menu item categories.
	 */
	@GetMapping(value = ApiPathConstant.GET_CUSTOM_PACKAGE_MENU_ITEM_CATEGORY_MENU_ITEM_EXIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CUSTOM_PACKAGE + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<GetMenuPreparationForMenuItemCategoryModel>> getByIsActiveAndFinal(@RequestParam(name = FieldConstants.COMMON_FIELD_CUSTOM_PACKAGE_ID) String customPackageId, HttpServletRequest request) {
		return RequestResponseUtils.generateResponseDto(customPackageService.getMenuItemCategoryByMenuItemExist(StringUtils.isNotBlank(customPackageId) ? Long.valueOf(customPackageId) : null, request.getAttribute(Constants.TENANT).toString()));
	}

	/**
	 * Retrieves a list of active custom packages.
	 * It is used at Menu Preparation > Select on custom package.
	 *
	 * @return ResponseContainerDto containing a list of active custom packages.
	 * @throws RestException if there is an issue with fetching the active custom packages.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<CustomPackageDto>> readActive() {
		return RequestResponseUtils.generateResponseDto(customPackageService.readActive());
	}

}