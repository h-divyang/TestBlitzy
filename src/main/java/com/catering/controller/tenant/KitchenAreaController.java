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
import com.catering.dto.tenant.request.KitchenAreaDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.KitchenAreaService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST controller for managing Kitchen Areas.
 * <p>
 * This controller provides APIs for creating, reading, updating, and deleting Kitchen Areas.
 * Additionally, it provides an endpoint for fetching active Kitchen Areas.
 * </p>
 * <p>
 * Each endpoint is secured using user rights-based authorization to ensure only authorized users
 * can access or modify the data.
 * </p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.KITCHEN_AREA)
@Tag(name = SwaggerConstant.KITCHEN_AREA)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KitchenAreaController {

	/**
	 * Service to handle messages for API responses.
	 */
	MessageService messageService;

	/**
	 * Service layer for business logic related to Kitchen Areas.
	 */
	KitchenAreaService kitchenAreaService;

	/**
	 * Creates a new Kitchen Area.
	 *
	 * @param kitchenAreaDto the DTO containing the details of the Kitchen Area to be created.
	 * @return a response container DTO containing the created Kitchen Area details.
	 * @throws RestException if an error occurs during the creation process.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.KITCHEN_AREA + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<KitchenAreaDto>> create(@Valid @RequestBody KitchenAreaDto kitchenAreaDto) throws RestException {
		Optional<KitchenAreaDto> kitchenAreaResponseDto = kitchenAreaService.createAndUpdate(kitchenAreaDto);
		return RequestResponseUtils.generateResponseDto(kitchenAreaResponseDto, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves all Kitchen Areas based on filter criteria.
	 *
	 * @param filterDto the filter criteria to apply when fetching Kitchen Areas.
	 * @return a response container DTO containing the list of Kitchen Areas.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.KITCHEN_AREA + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<KitchenAreaDto>> read(FilterDto filterDto) {
		return kitchenAreaService.read(filterDto);
	}

	/**
	 * Retrieves all active Kitchen Areas.
	 *
	 * @return a response container DTO containing the list of active Kitchen Areas.
	 */
	@GetMapping(value = ApiPathConstant.IS_ACTIVE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.KITCHEN_AREA + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ITEM + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<List<KitchenAreaDto>> getByIsActive() {
		return RequestResponseUtils.generateResponseDto(kitchenAreaService.readDataByIsActive());
	}

	/**
	 * Updates an existing Kitchen Area.
	 *
	 * @param kitchenAreaDto the DTO containing the updated details of the Kitchen Area.
	 * @return a response container DTO containing the updated Kitchen Area details.
	 * @throws RestException if an error occurs during the update process.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.KITCHEN_AREA + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<KitchenAreaDto> update(@Valid @RequestBody KitchenAreaDto kitchenAreaDto) throws RestException {
		Optional<KitchenAreaDto> kitchenAreaResponseDto = kitchenAreaService.createAndUpdate(kitchenAreaDto);
		return RequestResponseUtils.generateResponseDto(!kitchenAreaResponseDto.isEmpty() ? kitchenAreaResponseDto.get() : new KitchenAreaDto(), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes a Kitchen Area by its ID.
	 *
	 * @param idStr the ID of the Kitchen Area to be deleted, as a string matching the numeric pattern.
	 * @return a response container DTO confirming the deletion.
	 * @throws RestException if the deletion fails due to a business logic or database constraint.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.KITCHEN_AREA + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String idStr) throws RestException {
		kitchenAreaService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

}