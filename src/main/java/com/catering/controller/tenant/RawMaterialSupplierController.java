package com.catering.controller.tenant;

import java.util.List;
import java.util.Optional;

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

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.RawMaterialSupplierDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.RawMaterialSupplierService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for handling operations related to raw material suppliers.
 * Provides endpoints for creating, reading, updating, marking as default, and deleting raw material suppliers.
 */
@RestController
@RequestMapping(value = ApiPathConstant.RAW_MATERIAL_SUPPLIER)
@Tag(name = SwaggerConstant.RAW_MATERIAL_SUPPLIER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialSupplierController {

	/**
	 * Service responsible for handling messages and localization for the application.
	 * It provides functionality to retrieve and manage localized messages for different components.
	 */
	MessageService messageService;

	/**
	 * Service responsible for managing operations related to raw material suppliers.
	 * It includes methods for creating, updating, retrieving, and deleting raw material suppliers.
	 */
	RawMaterialSupplierService rawMaterialSupplierService;

	/**
	 * Creates a new raw material supplier.
	 * This method saves a raw material supplier and returns the result in a response DTO.
	 *
	 * @param rawMaterialContractorDto the DTO representing the raw material supplier to be created or updated
	 * @return a {@link ResponseContainerDto} containing the created/updated raw material supplier DTO
	 * @throws RestException if an error occurs during the operation
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Optional<RawMaterialSupplierDto>> create(@Valid @RequestBody RawMaterialSupplierDto rawMaterialContractorDto) throws RestException {
		Optional<RawMaterialSupplierDto> rawMaterialResponseDto = rawMaterialSupplierService.createAndUpdate(rawMaterialContractorDto);
		return RequestResponseUtils.generateResponseDto(rawMaterialResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves a list of raw material suppliers based on various optional query parameters.
	 * Supports pagination, sorting, and filtering by raw material ID and search query.
	 *
	 * @param rawMaterialId the ID of the raw material (optional)
	 * @param page the page number for pagination (optional)
	 * @param size the number of items per page (optional)
	 * @param sortBy the field by which to sort (optional)
	 * @param sortDirection the direction of sorting (optional)
	 * @param query the search query to filter results (optional)
	 * @return a {@link ResponseContainerDto} containing a list of raw material supplier DTOs
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<RawMaterialSupplierDto>> read(
			@RequestParam(value = Constants.RAW_MATERIAL_ID, required = false) String rawMaterialId,
			@RequestParam(value = Constants.CURRENT_PAGE, required = false) String page,
			@RequestParam(value = Constants.SIZE_PER_PAGE, required = false) String size,
			@RequestParam(value = Constants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = Constants.SORT_DIRECTION, required = false) String sortDirection,
			@RequestParam(value = Constants.QUERY, required = false) String query) {
		return rawMaterialSupplierService.read(Long.valueOf(rawMaterialId), page, size, sortBy, sortDirection, query);
	}

	/**
	 * Updates an existing raw material supplier.
	 * This method updates the raw material supplier with the provided DTO and returns the updated result in a response DTO.
	 *
	 * @param rawMaterialContractorDto the DTO representing the raw material supplier to be updated
	 * @return a {@link ResponseContainerDto} containing the updated raw material supplier DTO
	 * @throws RestException if an error occurs during the operation
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Optional<RawMaterialSupplierDto>> update(@Valid @RequestBody RawMaterialSupplierDto rawMaterialContractorDto) throws RestException {
		Optional<RawMaterialSupplierDto> rawMaterialResponseDto = rawMaterialSupplierService.createAndUpdate(rawMaterialContractorDto);
		return RequestResponseUtils.generateResponseDto(rawMaterialResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Marks a raw material supplier as the default.
	 * This method marks the specified raw material supplier as the default for a given raw material.
	 *
	 * @param rawMaterialContractorDto the DTO representing the raw material supplier to be marked as default
	 * @return a {@link ResponseContainerDto} containing the updated raw material supplier DTO
	 * @throws RestException if an error occurs during the operation
	 */
	@PatchMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Optional<RawMaterialSupplierDto>> markDefault(@Valid @RequestBody RawMaterialSupplierDto rawMaterialContractorDto) throws RestException {
		Optional<RawMaterialSupplierDto> rawMaterialContractorDto2 = rawMaterialSupplierService.markDefault(rawMaterialContractorDto.getRawMaterial().getId(), rawMaterialContractorDto.getId(), rawMaterialContractorDto.getIsDefault());
		return RequestResponseUtils.generateResponseDto(rawMaterialContractorDto2, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a raw material supplier by ID.
	 * This method deletes the raw material supplier identified by the provided ID.
	 *
	 * @param idStr the ID of the raw material supplier to be deleted
	 * @return a {@link ResponseContainerDto} indicating the result of the delete operation
	 * @throws RestException if an error occurs during the operation
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.RAW_MATERIAL + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String idStr) throws RestException {
		rawMaterialSupplierService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

}