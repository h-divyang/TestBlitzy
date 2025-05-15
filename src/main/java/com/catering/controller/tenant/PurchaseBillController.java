package com.catering.controller.tenant;

import java.util.List;
import java.util.Objects;
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
import com.catering.dto.tenant.request.PurchaseBillGetByIdDto;
import com.catering.dto.tenant.request.PurchaseBillOrderDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillRequestDto;
import com.catering.dto.tenant.request.PurchaseBillResponseDto;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.PurchaseBillService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for managing purchase bills. It allows creating, updating, deleting, and retrieving purchase bill data.
 * Provides endpoints for getting raw material data, purchase order data, and managing purchase bills.
 */
@RestController
@RequestMapping(value = ApiPathConstant.PURCHASE_BILL)
@Tag(name = SwaggerConstant.PURCHASE_BILL)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseBillController {

	/**
	 * Service for handling purchase bill operations like creating, updating, and retrieving purchase bills.
	 */
	PurchaseBillService purchaseBillService;

	/**
	 * Service for managing messages, including response messages for various operations.
	 */
	MessageService messageService;

	/**
	 * Service for handling exceptions, including throwing custom exceptions and managing error responses.
	 */
	ExceptionService exceptionService;

	/**
	 * Creates a new purchase bill entry in the system.
	 * This method processes the request data, validates it, and then invokes the service layer to persist the new purchase bill.
	 * 
	 * @param purchaseBillRequestDto The request body containing the purchase bill data.
	 * @return A response containing the created purchase bill's data and a success message.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<PurchaseBillRequestDto>> create(@Valid @RequestBody PurchaseBillRequestDto purchaseBillRequestDto) {
		Optional<PurchaseBillRequestDto> purchaseBillRequestDtoList = purchaseBillService.createAndUpdate(purchaseBillRequestDto);
		return RequestResponseUtils.generateResponseDto(purchaseBillRequestDtoList, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates an existing purchase bill entry in the system.
	 * This method processes the provided data and invokes the service layer to update the purchase bill in the database.
	 * 
	 * @param purchaseBillRequestDto The request body containing the updated purchase bill data.
	 * @return A response containing the updated purchase bill's data and a success message.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<PurchaseBillRequestDto>> update(@Valid @RequestBody PurchaseBillRequestDto purchaseBillRequestDto) {
		Optional<PurchaseBillRequestDto> purchaseBillRequestDtoList = purchaseBillService.createAndUpdate(purchaseBillRequestDto);
		return RequestResponseUtils.generateResponseDto(purchaseBillRequestDtoList, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Retrieves a list of purchase bills based on a filter.
	 * Allows filtering of purchase bills based on parameters defined in the filter DTO.
	 * 
	 * @param filterDto Contains the filter criteria for retrieving the purchase bills.
	 * @return A response containing a list of purchase bills that match the filter criteria.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<PurchaseBillResponseDto>> read(FilterDto filterDto) {
		return purchaseBillService.read(filterDto);
	}

	/**
	 * Retrieves a specific purchase bill by its ID.
	 * This method fetches the purchase bill details based on the ID passed in the path variable.
	 * 
	 * @param idStr The ID of the purchase bill to retrieve.
	 * @return A response containing the data for the requested purchase bill.
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<PurchaseBillGetByIdDto> getById(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) {
		return RequestResponseUtils.generateResponseDto(purchaseBillService.getById(Long.parseLong(idStr)));
	}

	/**
	 * Deletes a purchase bill based on its ID.
	 * This method validates the ID, and if valid, proceeds to delete the purchase bill from the system.
	 * 
	 * @param idStr The ID of the purchase bill to delete.
	 * @return A response confirming that the purchase bill was deleted successfully.
	 * @throws RestException If the ID is invalid or if the deletion fails.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) throws RestException {
		if (!ValidationUtils.isNumber(idStr)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		purchaseBillService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves raw material data for populating the raw material dropdown in the UI.
	 * This method fetches a list of raw materials that are available for selection.
	 * 
	 * @return A response containing the raw material data.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_DATA)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<PurchaseBillRawMaterialDropDownDto>> getRawMaterialDropDownData() {
		return purchaseBillService.getRawMaterialDropDownData();
	}

	/**
	 * Retrieves purchase order data for the dropdown.
	 * This method fetches a list of purchase orders that can be selected for the purchase bill.
	 * 
	 * @param id The ID used for filtering the purchase order data.
	 * @return A response containing the purchase order dropdown data.
	 */
	@GetMapping(value = ApiPathConstant.PURCHASE_ORDER_DROPDOWN)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<PurchaseBillOrderDropDownDto>> getPurchaseOrderDropDownData(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		return purchaseBillService.getPurchaseOrderDropDownData(Objects.nonNull(Long.parseLong(id)) ? Long.parseLong(id) : 0l);
	}

	/**
	 * Retrieves raw material data for a specific purchase order.
	 * This method fetches the raw materials associated with the given purchase order ID.
	 * 
	 * @param id The ID of the purchase order whose raw materials are to be retrieved.
	 * @return A response containing the raw material data for the given purchase order.
	 */
	@GetMapping(value = ApiPathConstant.PURCHASE_ORDER_RAW_MATERIAL)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<PurchaseBillOrderRawMaterialDto>> getPurchaseOrderRawMaterial(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String id) {
		return purchaseBillService.getPurchaseBillOrderRawMaterial(Long.parseLong(id));
	}

}