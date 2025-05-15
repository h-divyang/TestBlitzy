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
import com.catering.dto.tenant.request.PurchaseOrderContactDto;
import com.catering.dto.tenant.request.PurchaseOrderGetByIdDto;
import com.catering.dto.tenant.request.PurchaseOrderRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseOrderRequestDto;
import com.catering.dto.tenant.request.PurchaseOrderResponseDto;
import com.catering.exception.RestException;
import com.catering.service.common.ExceptionService;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.PurchaseOrderService;
import com.catering.util.RequestResponseUtils;
import com.catering.util.ValidationUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This class handles the RESTful endpoints for managing purchase orders.
 * @author Krushali Talaviya
 * @since 2024-06-01
 */
@RestController
@RequestMapping(value = ApiPathConstant.PURCHASE_ORDER)
@Tag(name = SwaggerConstant.PURCHASE_ORDER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseOrderController {

	/**
	 * Service class for managing purchase orders, including creating, updating, and retrieving purchase order data.
	 */
	PurchaseOrderService purchaseOrderService;

	/**
	 * Service for handling messaging, including sending and receiving messages, and managing responses.
	 */
	MessageService messageService;

	/**
	 * Service for handling exceptions, including throwing custom exceptions and handling error responses.
	 */
	ExceptionService exceptionService;

	/**
	 * Endpoint for creating a new purchase order.
	 * 
	 * @param purchaseOrderRequestDto The purchase order request DTO
	 * @return Response container DTO with optional purchase order request DTO
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_ORDER + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Optional<PurchaseOrderRequestDto>> create(@Valid @RequestBody PurchaseOrderRequestDto purchaseOrderRequestDto) {
		Optional<PurchaseOrderRequestDto> purchaseOrderRequestDtoList = purchaseOrderService.createAndUpdate(purchaseOrderRequestDto);
		return RequestResponseUtils.generateResponseDto(purchaseOrderRequestDtoList, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Endpoint for updating an existing purchase order.
	 * 
	 * @param purchaseOrderRequestDto The purchase order request DTO
	 * @return Response container DTO with optional purchase order request DTO
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_ORDER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Optional<PurchaseOrderRequestDto>> update(@Valid @RequestBody PurchaseOrderRequestDto purchaseOrderRequestDto) {
		Optional<PurchaseOrderRequestDto> purchaseOrderRequestDtoList = purchaseOrderService.createAndUpdate(purchaseOrderRequestDto);
		return RequestResponseUtils.generateResponseDto(purchaseOrderRequestDtoList, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Endpoint for retrieving a list of purchase orders.
	 * 
	 * @param filterDto The filter DTO
	 * @return Response container DTO with a list of purchase order response DTOs
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_ORDER + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<PurchaseOrderResponseDto>> read(FilterDto filterDto) {
		return purchaseOrderService.read(filterDto);
	}

	/**
	 * Endpoint for get a purchase order by ID.
	 * 
	 * @param id The ID of the purchase order to get
	 * @return Response container DTO with PurchaseOrderGetByIdDto data
	 */
	@GetMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_ORDER + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<PurchaseOrderGetByIdDto> getById(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) {
		return RequestResponseUtils.generateResponseDto(purchaseOrderService.getById(Long.parseLong(idStr)));
	}

	/**
	 * Endpoint for deleting a purchase order by ID.
	 * 
	 * @param id The ID of the purchase order to delete
	 * @return Response container DTO with null data
	 * @throws RestException If an invalid ID is provided
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_ORDER + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) String idStr) throws RestException {
		if (!ValidationUtils.isNumber(idStr)) {
			exceptionService.throwBadRequestException(messageService.getMessage(MessagesConstant.INVALID_ID));
		}
		purchaseOrderService.deleteById(Long.valueOf(idStr));
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Endpoint for retrieving dropdown data for supplier contacts.
	 * 
	 * @return Response container DTO with a list of purchase order contact DTOs
	 */
	@GetMapping(value = ApiPathConstant.SUPPLIER_CONTACT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_ORDER + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.PURCHASE_ORDER + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<PurchaseOrderContactDto>> getContactDropDownData() {
		return purchaseOrderService.getContactDropDownData();
	}

	/**
	 * Endpoint for retrieving dropdown data for raw materials.
	 * 
	 * @return Response container DTO with a list of purchase order raw material dropdown DTOs
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_DATA)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_ORDER + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.PURCHASE_ORDER + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<PurchaseOrderRawMaterialDropDownDto>> getRawMaterialDropDownData() {
		return purchaseOrderService.getRawMaterialDropDownData();
	}

}