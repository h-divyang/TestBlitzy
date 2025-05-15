package com.catering.controller.tenant;

import java.util.List;

import javax.validation.Valid;

import com.catering.dto.tenant.request.*;
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
import com.catering.dao.labour_and_other_management.LabourAndOtherManagementNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.ExtraExpenseService;
import com.catering.service.tenant.OrderCrockeryService;
import com.catering.service.tenant.OrderGeneralFixRawMaterialService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for handling operations related to Labour and Other Management in the system. This controller provides endpoints
 * to manage crockery, extra expenses, and raw materials for orders, along with fetching drop-down data for various fields.
 * It contains CRUD operations for resources, as well as functionality for specific business operations like supplier management
 * and quantity adjustment.
 * 
 * <p>Endpoints provided by this controller include:</p>
 * 
 * <ul>
 * <li><b>GET /crockery</b>: Fetches crockery data for a given order function and raw material category ID.</li>
 * <li><b>GET /raw-material-category</b>: Fetches raw material categories based on a specific order function ID.</li>
 * <li><b>GET /crockery-supplier</b>: Fetches crockery supplier contacts for a given order function ID.</li>
 * <li><b>POST /crockery</b>: Saves the crockery data for a given list of crockery.</li>
 * <li><b>GET /extra</b>: Retrieves extra expenses for an order based on parameters like order function ID.</li>
 * <li><b>POST /extra</b>: Adds new extra expenses for an order.</li>
 * <li><b>PUT /extra</b>: Updates an existing extra expense.</li>
 * <li><b>DELETE /extra/{id}</b>: Deletes an extra expense by its ID.</li>
 * <li><b>GET /order-general-fix-raw-material</b>: Retrieves general fix raw material data for an order function.</li>
 * <li><b>GET /general-fix-raw-material-supplier</b>: Retrieves supplier contacts for general fix raw materials.</li>
 * <li><b>POST /order-general-fix-raw-material</b>: Saves the order general fix raw material data.</li>
 * </ul>
 * 
 * <p>All endpoints are protected by role-based authorization, ensuring that only authorized users can access or modify data.</p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.LABOUR_AND_OTHER_MANAGEMENT)
@Tag(name = SwaggerConstant.LABOUR_AND_OTHER_MANAGEMENT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LabourAndOtherManagementController {

	/**
	 * Service for handling native queries related to labour and other management operations.
	 */
	LabourAndOtherManagementNativeQueryService labourAndOtherManagementNativeQueryService;

	/**
	 * Service for managing system messages.
	 */
	MessageService messageService;

	/**
	 * Service for handling order crockery data.
	 */
	OrderCrockeryService orderCrockeryService;

	/**
	 * Service for handling extra expenses related to orders.
	 */
	ExtraExpenseService extraExpenseService;

	/**
	 * Service for handling general fix raw material data for orders.
	 */
	OrderGeneralFixRawMaterialService orderGeneralFixRawMaterialService;

	/**
	 * Retrieves crockery data based on the provided order function and raw material category IDs.
	 * 
	 * @param getIsAdjustQuantity A flag indicating if the quantity needs to be adjusted.
	 * @param orderFunctionId The ID of the order function.
	 * @param rawMaterialCategoryId The ID of the raw material category.
	 * @return A {@link ResponseContainerDto} containing a list of {@link GetOrderCrockeryDto} objects.
	 */
	@GetMapping(value = ApiPathConstant.CROCKERY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<GetOrderCrockeryDto>> findCrockeryDataByOrderFunctionAndRawMaterialCategoryId(@RequestParam Long orderFunctionId, @RequestParam Long rawMaterialCategoryId) {
		return RequestResponseUtils.generateResponseDto(labourAndOtherManagementNativeQueryService.findCrockeryDataByOrderFunctionAndRawMaterialCategoryId(orderFunctionId, rawMaterialCategoryId));
	}

	/**
	 * Retrieves raw material categories based on the provided order function ID.
	 * 
	 * @param orderFunctionId The ID of the order function.
	 * @return A {@link ResponseContainerDto} containing a list of {@link CommonDataForDropDownDto} objects.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_AND_OTHER_MANAGEMENT_RAW_MATERIAL_CATEGORY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findCrockeryRawMaterialCategoryByOrderFunctionId(@RequestParam Long orderFunctionId) {
		return RequestResponseUtils.generateResponseDto(labourAndOtherManagementNativeQueryService.findCrockeryRawMaterialCategoryByOrderFunctionId(orderFunctionId));
	}

	/**
	 * Retrieves supplier contacts for crockery based on the provided order function ID.
	 * 
	 * @param orderFunctionId The ID of the order function.
	 * @return A {@link ResponseContainerDto} containing a list of {@link CommonDataForDropDownDto} objects.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_AND_OTHER_MANAGEMENT_CROCKEY_SUPPLIER)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findCrockerySupplierContactByOrderFunctionId(@RequestParam Long orderFunctionId) {
		return RequestResponseUtils.generateResponseDto(labourAndOtherManagementNativeQueryService.findCrockerySupplierContactByOrderFunctionId(orderFunctionId));
	}

	/**
	 * Saves crockery data for the provided list of crockery.
	 * 
	 * @param orderCrockeries The list of order crockery data to save.
	 * @return A {@link ResponseContainerDto} indicating the result of the operation.
	 */
	@PostMapping(value = ApiPathConstant.CROCKERY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Object> saveCrockeryData(@RequestBody List<OrderCrockeryDto> orderCrockeries) {
		orderCrockeryService.saveCrockeryData(orderCrockeries);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves extra expenses associated with the given order function ID.
	 * 
	 * @param parameterDto The parameters for fetching extra expenses.
	 * @return A {@link ResponseContainerDto} containing a list of {@link ExtraExpenseDto} objects.
	 */
	@GetMapping(value = ApiPathConstant.EXTRA)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<ExtraExpenseDto>> findByOrderId(@Valid ExtraExpenseParameterDto parameterDto) {
		return RequestResponseUtils.generateResponseDto(extraExpenseService.findByOrderFunctionId(parameterDto));
	}

	/**
	 * Adds a new extra expense for the given order.
	 * 
	 * @param extraExpenseDto The extra expense data to be added.
	 * @return A {@link ResponseContainerDto} indicating the result of the operation.
	 */
	@PostMapping(value = ApiPathConstant.EXTRA)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_ADD})
	public ResponseContainerDto<Object> saveExtra(@RequestBody ExtraExpenseDto extraExpenseDto) {
		return RequestResponseUtils.generateResponseDto(extraExpenseService.createAndUpdate(extraExpenseDto), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates an existing extra expense.
	 * 
	 * @param extraExpenseDto The updated extra expense data.
	 * @return A {@link ResponseContainerDto} indicating the result of the operation.
	 */
	@PutMapping(value = ApiPathConstant.EXTRA)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Object> updateExtra(@RequestBody ExtraExpenseDto extraExpenseDto) {
		extraExpenseService.createAndUpdate(extraExpenseDto);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Deletes an extra expense by its ID.
	 * 
	 * @param id The ID of the extra expense to delete.
	 * @return A {@link ResponseContainerDto} indicating the result of the operation.
	 */
	@DeleteMapping(value = ApiPathConstant.EXTRA + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_DELETE})
	public ResponseContainerDto<Object> deleteExtra(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id) {
		extraExpenseService.deleteById(id);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

	/**
	 * Retrieves general fix raw material data for a given order function ID.
	 * 
	 * @param getIsAdjustQuantity A flag indicating if the quantity should be adjusted.
	 * @param orderFunctionId The ID of the order function.
	 * @return A {@link ResponseContainerDto} containing a list of {@link OrderGeneralFixRawMaterialDto} objects.
	 */
	@GetMapping(value = ApiPathConstant.ORDER_GENERAL_FIX_RAW_MATERIAL)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<GetOrderCrockeryDto>> findGeneralFixRawMaterialByOrderFunctionId(@RequestParam Long orderFunctionId) {
		return RequestResponseUtils.generateResponseDto(labourAndOtherManagementNativeQueryService.findGeneralFixRawMaterialByOrderFunctionId(orderFunctionId));
	}

	/**
	 * Retrieves supplier contacts for general fix raw materials based on the provided order function ID.
	 * 
	 * @param orderFunctionId The ID of the order function.
	 * @return A {@link ResponseContainerDto} containing a list of {@link CommonDataForDropDownDto} objects.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_AND_OTHER_MANAGEMENT_GENERAL_FIX_RAW_MATERIAL_SUPPLIER)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findGeneralFixRawMaterialSupplierContactByOrderFunctionId(@RequestParam Long orderFunctionId) {
		return RequestResponseUtils.generateResponseDto(labourAndOtherManagementNativeQueryService.findGeneralFixRawMaterialSupplierContactByOrderFunctionId(orderFunctionId));
	}

	/**
	 * Saves the general fix raw material data for an order.
	 * 
	 * @param orderGeneralFixRawMaterials The list of general fix raw material data to save.
	 * @return A {@link ResponseContainerDto} indicating the result of the operation.
	 */
	@PostMapping(value = ApiPathConstant.ORDER_GENERAL_FIX_RAW_MATERIAL)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.LABOUR_AND_OTHER_MANAGEMENT + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Object> saveOrderGeneralFixRawMaterial(@RequestBody List<OrderGeneralFixRawMaterialDto> orderGeneralFixRawMaterialDtos) {
		orderGeneralFixRawMaterialService.saveOrderGeneralFixRawMaterial(orderGeneralFixRawMaterialDtos);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

}