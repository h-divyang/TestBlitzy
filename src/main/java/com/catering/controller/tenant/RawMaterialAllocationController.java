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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.raw_material_allocation.RawMaterialAllocationNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.AgencyAllocationDto;
import com.catering.dto.tenant.request.CommonRawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialAllocationDto;
import com.catering.dto.tenant.request.RawMaterialAllocationFromRawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialAllocationRawMaterialDto;
import com.catering.dto.tenant.request.RawMaterialAllocationRequestDto;
import com.catering.exception.RestException;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.RawMaterialAllocationService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * REST controller for managing raw material allocations.
 * This controller exposes endpoints for creating, updating, retrieving, and deleting raw material allocations,
 * along with handling allocation queries and providing response messages.
 */
@RestController
@RequestMapping(value = ApiPathConstant.RAW_MATERIAL_ALLOCATION)
@Tag(name = SwaggerConstant.RAW_MATERIAL_ALLOCATION)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RawMaterialAllocationController {

	/**
	 * Service for managing raw material allocation logic, including CRUD operations.
	 */
	RawMaterialAllocationService rawMaterialService;

	/**
	 * Service for sending and handling response messages related to raw material allocations.
	 */
	MessageService messageService;

	/**
	 * Service for handling native queries related to raw material allocation.
	 */
	RawMaterialAllocationNativeQueryService nativeQueryService;

	//-------------------------------------------------- Menu Allocation Raw Material --------------------------------------------
	/**
	* Handles HTTP GET requests to retrieve raw material orders.
	*
	* @param menuPreparationMenuItemId Optional parameter for filtering orders based on menu preparation final material ID.
	* @return ResponseContainerDto containing an optional list of RawMaterialAllocationRequestDto objects.
	*/
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Optional<List<RawMaterialAllocationRequestDto>>> read(@RequestParam(required = false) Long menuPreparationMenuItemId) {
		return RequestResponseUtils.generateResponseDto(rawMaterialService.read(menuPreparationMenuItemId));
	}

	/**
	 * Handles HTTP POST requests to create and update raw material orders.
	 *
	 * @param RawMaterialAllocationRequestDtos List of validated OrderRawMaterialDto objects for creation and update.
	 * @return ResponseContainerDto containing the created or updated raw material order details.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<RawMaterialAllocationRequestDto>> createAndUpdate(@RequestBody List<@Valid RawMaterialAllocationRequestDto> rawMaterialAllocationDtos, @RequestParam Long orderId) {
		List<RawMaterialAllocationRequestDto> rawMaterialAllocationDtoResponse = rawMaterialService.createAndUpdate(rawMaterialAllocationDtos, orderId);
		return RequestResponseUtils.generateResponseDto(rawMaterialAllocationDtoResponse, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	* Handles HTTP DELETE requests to delete a raw material order by its ID.
	*
	* @param id String representation of the raw material order ID to be deleted.
	* @return ResponseContainerDto with a message indicating the success of the deletion operation.
	* @throws RestException Thrown if the provided ID is invalid.
	*/
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id, @RequestParam Long orderId) throws RestException {
		rawMaterialService.deleteById(id, orderId);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	//-------------------------------------------------- Raw Material Allocation Raw Material --------------------------------------------
	/**
	 * Update the raw material allocation for a given order.
	 * This endpoint handles both adding and editing raw material allocation data.
	 * 
	 * @param rawMaterialAllocationDtos the list of raw material allocation data transfer objects to be updated.
	 * @param orderId the ID of the order to which the raw materials are allocated.
	 * @return a response container containing the updated data or a success message.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<RawMaterialAllocationFromRawMaterialDto> create(@RequestBody List<RawMaterialAllocationFromRawMaterialDto> rawMaterialAllocationDtos, @RequestParam Long orderId) {
		rawMaterialService.update(rawMaterialAllocationDtos, orderId);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Updates the raw material quantity based on the provided list of raw material data.
	 * This endpoint handles editing raw material allocation and raw material allocation extra data.
	 *
	 * @param commonRawMaterialDto A list of {@link CommonRawMaterialDto} containing raw material details for updating quantities.
	 * @return a response container containing the updated data or a success message.
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_RAW_MATERIAL_QUANTITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<CommonRawMaterialDto>> updateRawMaterialQuantity(@RequestBody List<CommonRawMaterialDto> commonRawMaterialDto) {	
		rawMaterialService.updateRawMaterialQuantity(commonRawMaterialDto);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieve raw material allocation details for a given order and raw material category.
	 * 
	 * @param orderId the ID of the order for which raw material allocations are to be retrieved.
	 * @param rawMaterialCategoryId the ID of the raw material category to filter the allocation data.
	 * @return a response container with the raw material allocation data for the specified order and category.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<RawMaterialAllocationRawMaterialDto>> findRawMaterialAllocationByOrderId(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam Long rawMaterialCategoryId) {
		return RequestResponseUtils.generateResponseDto(nativeQueryService.findRawMaterialAllocationByOrderId(orderId, rawMaterialCategoryId));
	}

	/**
	 * Retrieve raw material category data for a specific order.
	 * This endpoint is used for reporting purposes to get the raw materials allocated under each category for a given order.
	 * 
	 * @param orderId the ID of the order to retrieve the raw material categories for.
	 * @return a response container with the raw material category allocation data for the specified order.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_REPORT_MENU_ALLOCATION_RAW_MATERIAL_CATEGORY_PER_ORDER + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<RawMaterialAllocationDto>> findRawMaterialCategoryByOrderId(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId) {
		return RequestResponseUtils.generateResponseDto(nativeQueryService.finRawMaterialCategoryByOrderId(orderId));
	}

	/**
	 * Allocate raw materials to an agency for a specific order.
	 * This endpoint handles agency allocation of raw materials, including updating the allocation details.
	 * 
	 * @param agencyAllocationDtoList the list of agency allocation data to be updated.
	 * @param orderId the ID of the order to which the raw materials are allocated.
	 * @return a response container indicating the successful update of the agency allocation data.
	 */
	@PutMapping(value = ApiPathConstant.AGENCY_ALLOCATION)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Object> agencyAllocation(@RequestBody List<AgencyAllocationDto> agencyAllocationDtoList, @RequestParam Long orderId) {
		rawMaterialService.agencyAllocation(agencyAllocationDtoList, orderId);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves the smallest measurement value for a given measurement ID and value.
	 * This endpoint is used to determine the smallest possible measurement value based on the provided value and measurement unit.
	 * 
	 * @param value The input value for which the smallest measurement needs to be determined.
	 * @param measurementId The ID of the measurement unit.
	 * @return A {@link ResponseContainerDto} containing the smallest measurement value.
	 */
	@GetMapping(value = ApiPathConstant.SMALLEST_MEASUREMENT_VALUE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Double> getSmallestMeasurementValue(@RequestParam Double quantity, @RequestParam Long measurementId) {
		return RequestResponseUtils.generateResponseDto(rawMaterialService.getSmallestMeasurementValue(quantity, measurementId));
	}

	/**
	 * Retrieves the smallest measurement ID for a given measurement unit.
	 * This endpoint is used to determine the smallest available measurement ID based on the provided measurement unit ID.
	 * 
	 * @param measurementId The ID of the measurement unit.
	 * @return A {@link ResponseContainerDto} containing the smallest measurement ID.
	 */
	@GetMapping(value = ApiPathConstant.SMALLEST_MEASUREMENT_ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<Long> getSmallestMeasurementId(@RequestParam Long measurementId) {
		return RequestResponseUtils.generateResponseDto(rawMaterialService.getSmallestMeasurementId(measurementId));
	}

	/**
	 * Retrieves the adjusted quantity and extra quantity along with their respective IDs for a given value, measurement ID, and order ID.
	 * This endpoint calculates and returns a string containing the adjusted quantity, extra quantity, and their corresponding IDs.
	 * 
	 * @param value The input value for which the adjusted and extra quantities need to be determined.
	 * @param measurementId The ID of the measurement unit.
	 * @param orderId The ID of the order for which the quantities are calculated.
	 * @return A {@link ResponseContainerDto} containing a string representation of the adjusted quantity, extra quantity, and their respective IDs.
	 */
	@GetMapping(value = ApiPathConstant.ADJUSTED_QUANTITY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_ALLOCATION + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<String> getAdjustedQuantity(@RequestParam Double quantity, @RequestParam Long measurementId, @RequestParam Boolean isAdjustQuantity, @RequestParam Boolean isSupplierRate) {
		return RequestResponseUtils.generateResponseDto(rawMaterialService.getAdjustedAndExtraQuantity(quantity, measurementId, isAdjustQuantity, isSupplierRate));
	}

}