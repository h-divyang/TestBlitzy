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
import com.catering.dto.tenant.request.TaxMasterDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.TaxMasterService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This class serves as the controller for TaxMaster related operations in the tenant module.
 * It handles the RESTful endpoints for creating, reading, updating, and deleting TaxMasterDto objects.
 * 
 * @author Neel Bhanderi
 * @since March 2024
 * 
 */
@RestController
@RequestMapping(value = ApiPathConstant.TAX_MASTER)
@Tag(name = SwaggerConstant.TAX_MASTER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaxMasterController {

	/**
	 * Service for handling messaging operations.
	 */
	MessageService messageService;

	/**
	 * Service for managing tax master data and operations.
	 */
	TaxMasterService taxMasterService;

	/**
	 * Creates a new tax master record.
	 *
	 * @param taxMasterDto The TaxMasterDto object containing the details of the tax master.
	 * @return A ResponseContainerDto wrapping an Optional TaxMasterDto object representing the created tax master record, along with a status message.
	 * @throws RestException if an error occurs during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TAX_MASTER + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<TaxMasterDto>> create(@Valid @RequestBody TaxMasterDto taxMasterDto) {
		Optional<TaxMasterDto> taxMasterResponseDto = taxMasterService.createAndUpdate(taxMasterDto);
		return RequestResponseUtils.generateResponseDto(taxMasterResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves a list of tax master records based on the provided filter.
	 *
	 * @param filterDto The FilterDto object containing the filter parameters for retrieving tax master records.
	 * @return A ResponseContainerDto wrapping a List of TaxMasterDto objects representing the retrieved tax master records.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TAX_MASTER + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<TaxMasterDto>> read(FilterDto filterDto) {
		return taxMasterService.read(filterDto);
	}

	/**
	 * Updates an existing tax master record.
	 *
	 * @param taxMasterDto The TaxMasterDto object containing the updated details of the tax master.
	 * @return A ResponseContainerDto wrapping a TaxMasterDto object representing the updated tax master record, along with a status message.
	 * @throws RestException if an error occurs during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TAX_MASTER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<TaxMasterDto> update(@Valid @RequestBody TaxMasterDto taxMasterDto) {
		Optional<TaxMasterDto> taxMasterResponseDto = taxMasterService.createAndUpdate(taxMasterDto);
		return RequestResponseUtils.generateResponseDto(taxMasterResponseDto.isPresent() ? taxMasterResponseDto.get() : null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a tax master record by ID.
	 *
	 * @param idStr The ID of the tax master record to be deleted.
	 * @return A ResponseContainerDto wrapping an empty object, along with a status message indicating the success of the deletion.
	 * @throws RestException if an error occurs during the deletion process.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TAX_MASTER + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String idStr) throws RestException {
		taxMasterService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves a list of active tax master entries.
	 *
	 * @return a {@link ResponseContainerDto} containing a list of {@link TaxMasterDto} where all entries have the active status set to true.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE)
	public ResponseContainerDto<List<TaxMasterDto>> findAllByIsActiveTrue() {
		return RequestResponseUtils.generateResponseDto(taxMasterService.findAllByIsActiveTrue());
	}

}