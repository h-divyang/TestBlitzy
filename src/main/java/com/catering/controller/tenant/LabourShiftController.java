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
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.LabourShiftDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.LabourShiftService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST controller for managing Labour Shifts.
 * <p>
 * This controller provides endpoints for creating, reading, updating, and deleting Labour Shifts.
 * Additionally, it offers an endpoint for fetching active Labour Shifts used in Labour/Other Management.
 * </p>
 * <p>
 * Each endpoint is secured with user rights-based authorization to ensure proper access control.
 * </p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.LABOUR_SHIFT)
@Tag(name = SwaggerConstant.LABOUR_SHIFT)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LabourShiftController {

	/**
	 * Service for handling localized messages for API responses.
	 */
	MessageService messageService;

	/**
	 * Service layer for Labour Shift business logic.
	 */
	LabourShiftService labourShiftService;

	/**
	 * Creates a new Labour Shift or updates an existing one.
	 *
	 * @param labourShiftDto the data transfer object representing the Labour Shift details.
	 * @return a response container DTO containing the created or updated Labour Shift data.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_SHIFT + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<LabourShiftDto>> create(@Valid @RequestBody LabourShiftDto labourShiftDto) {
		Optional<LabourShiftDto> labourShiftResponseDto = labourShiftService.createAndUpdate(labourShiftDto);
		return RequestResponseUtils.generateResponseDto(labourShiftResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves a list of Labour Shifts based on the provided filter criteria.
	 *
	 * @param filterDto the filter criteria for retrieving Labour Shifts.
	 * @return a response container DTO containing the list of Labour Shifts that match the filter criteria.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_SHIFT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<LabourShiftDto>> read(FilterDto filterDto) {
		return labourShiftService.read(filterDto);
	}

	/**
	 * Updates an existing Labour Shift.
	 *
	 * @param labourShiftDto the data transfer object representing the updated Labour Shift details.
	 * @return a response container DTO containing the updated Labour Shift data.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_SHIFT + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<LabourShiftDto> update(@Valid @RequestBody LabourShiftDto labourShiftDto) {
		Optional<LabourShiftDto> labourShiftResponseDto = labourShiftService.createAndUpdate(labourShiftDto);
		return RequestResponseUtils.generateResponseDto(labourShiftResponseDto.isPresent() ? labourShiftResponseDto.get() : null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a Labour Shift by its ID.
	 *
	 * @param id the unique identifier of the Labour Shift to be deleted.
	 * @return a response container DTO confirming the deletion.
	 * @throws RestException if the deletion fails due to a business logic or database constraint.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_SHIFT + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable Long id) throws RestException {
		labourShiftService.deleteById(id);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves a list of active Labour Shifts.
	 * <p>
	 * This endpoint is used in the Labour/Other Management module for the Labour Shift dropdown.
	 * </p>
	 *
	 * @return a response container DTO containing the list of active Labour Shifts.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<LabourShiftDto>> readActive() {
		return RequestResponseUtils.generateResponseDto(labourShiftService.findByIsActiveTrue());
	}

}