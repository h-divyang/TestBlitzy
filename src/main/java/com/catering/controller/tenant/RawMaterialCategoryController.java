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
import com.catering.dto.tenant.request.RawMaterialCategoryDto;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.RawMaterialCategoryService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST controller for managing raw material categories.
 * This controller exposes endpoints for creating, retrieving, updating, and deleting raw material categories,
 * along with additional functionality for managing categories based on their active status and raw material existence.
 */
@RestController
@RequestMapping(value = ApiPathConstant.RAW_MATERIAL_CATEGORY)
@Tag(name = SwaggerConstant.RAW_MATERIAL_CATEGORY)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialCategoryController {

	/**
	 * Service for managing raw material categories, including creation, updates, retrieval, and deletion.
	 */
	MessageService messageService;

	/**
	 * Service for handling exceptions within raw material category operations.
	 */
	ExceptionService exceptionService;

	/**
	 * Service for managing raw material category logic, including CRUD operations.
	 */
	RawMaterialCategoryService rawMaterialCategoryService;

	/**
	 * Endpoint to create a new raw material category.
	 * Only users with appropriate permissions can create a category.
	 * 
	 * @param rawMaterialCategoryDto the raw material category data to be created.
	 * @return A ResponseContainerDto containing the created raw material category.
	 * @throws RestException if an error occurs during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_CATEGORY + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Optional<RawMaterialCategoryDto>> create(@Valid @RequestBody RawMaterialCategoryDto rawMaterialCategoryDto) throws RestException {
		Optional<RawMaterialCategoryDto> rawMaterialCategoryResponseDto = rawMaterialCategoryService.createAndUpdate(rawMaterialCategoryDto);
		return RequestResponseUtils.generateResponseDto(rawMaterialCategoryResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Endpoint to retrieve all raw material categories.
	 * Only users with appropriate permissions can view raw material categories.
	 * 
	 * @param filterDto the filter criteria to apply when retrieving raw material categories.
	 * @return A ResponseContainerDto containing a list of raw material categories.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_CATEGORY + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<RawMaterialCategoryDto>> read(FilterDto filterDto) {
		return rawMaterialCategoryService.read(filterDto);
	}

	/**
	 * Endpoint to retrieve all raw material categories with active status set to true.
	 * 
	 * @return A ResponseContainerDto containing a list of raw material categories that are active.
	 * @throws RestException if an error occurs during the retrieval process.
	 */
	@GetMapping(value = ApiPathConstant.IS_ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<RawMaterialCategoryDto>> getByIsActive() {
		return RequestResponseUtils.generateResponseDto(rawMaterialCategoryService.readDataByIsActive());
	}

	/**
	 * Endpoint to update an existing raw material category.
	 * Only users with appropriate permissions can update raw material categories.
	 * 
	 * @param rawMaterialCategoryDto the raw material category data to be updated.
	 * @return A ResponseContainerDto containing the updated raw material category.
	 * @throws RestException if an error occurs during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_CATEGORY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<RawMaterialCategoryDto> update(@Valid @RequestBody RawMaterialCategoryDto rawMaterialCategoryDto) throws RestException {
		Optional<RawMaterialCategoryDto> rawMaterialCategoryResponseDto = rawMaterialCategoryService.createAndUpdate(rawMaterialCategoryDto);
		return RequestResponseUtils.generateResponseDto(rawMaterialCategoryResponseDto.get(), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint to delete a raw material category by its ID.
	 * Only users with appropriate permissions can delete raw material categories.
	 * 
	 * @param idStr the ID of the raw material category to be deleted.
	 * @return A ResponseContainerDto confirming the deletion of the raw material category.
	 * @throws RestException if an error occurs during the deletion process.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_CATEGORY + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) throws RestException {
		if (!ValidationUtils.isNumber(idStr)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		rawMaterialCategoryService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Endpoint to retrieve all raw material categories that are associated with existing raw materials.
	 * 
	 * @return A ResponseContainerDto containing a list of raw material categories that are linked to raw materials.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_EXIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<RawMaterialCategoryDto>> getByRawMaterialExist() {
		return RequestResponseUtils.generateResponseDto(rawMaterialCategoryService.findByItemRawMaterialsNotNullOrderByPriority());
	}

	/**
	 * Updates the priority of a raw material category.
	 *
	 * <p>This endpoint is secured and requires the user to have edit rights for raw material categories.
	 * The updated priority information is received through the request body.</p>
	 *
	 * @param rawMaterialCategoryDto the {@link RawMaterialCategoryDto} object containing the updated priority details
	 * @return a {@link ResponseContainerDto} containing a success message upon successful update
	 * @throws RestException if an error occurs during the update operation
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_PRIORITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_CATEGORY + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Void> updatePriority(@Valid @RequestBody List<RawMaterialCategoryDto> rawMaterialCategories) throws RestException {
		rawMaterialCategoryService.updatePriority(rawMaterialCategories);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves the highest priority value from the raw material category records.
	 * <p>
	 * This endpoint is protected and requires the user to have edit rights for raw material categories.
	 * It returns the maximum value of the {@code priority} field from the database.
	 *
	 * @return a {@link ResponseContainerDto} containing the highest priority value as a {@code Long}
	 * @throws RestException if an error occurs while fetching the data
	 */
	@GetMapping(value = ApiPathConstant.HIGHEST_PRIORITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_CATEGORY + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Long> highestPriority() throws RestException {
		return RequestResponseUtils.generateResponseDto(rawMaterialCategoryService.getHighestPriority());
	}

}