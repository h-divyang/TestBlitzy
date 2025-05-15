package com.catering.controller.tenant;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.CommonNotesDto;
import com.catering.dto.tenant.request.MenuAllocationDTO;
import com.catering.dto.tenant.request.RawMaterialCalculationDto;
import com.catering.dto.tenant.request.RawMaterialCalculationResponseDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.GetMenuAllocationOrderMenuPreparationModel;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.MenuAllocationService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for managing Menu Allocations within the system.
 * 
 * This controller provides the endpoints for performing CRUD operations and business logic
 * related to Menu Allocations, such as:
 * <ul>
 *   <li>Creating and updating Menu Allocations</li>
 *   <li>Calculating raw material costs for menu items</li>
 *   <li>Reading Menu Allocations by order ID</li>
 *   <li>Updating allocation notes</li>
 *   <li>Synchronizing raw material with order allocations</li>
 *   <li>Deleting Menu Allocations by ID</li>
 * </ul>
 * 
 * <p>Usage of Controller Methods:</p>
 * <ul>
 *   <li><b>PUT /menu-allocation</b> - Updates a list of menu allocations associated with an order.</li>
 *   <li><b>GET /menu-allocation/raw-material-calculation</b> - Calculates the raw material cost based on a provided input DTO.</li>
 *   <li><b>GET /menu-allocation/{orderId}</b> - Fetches menu allocation data for a given order ID.</li>
 *   <li><b>PUT /menu-allocation/update-note</b> - Updates the notes associated with a Menu Allocation.</li>
 *   <li><b>GET /menu-allocation/sync-raw-material</b> - Syncs raw material data for the specified order ID.</li>
 *   <li><b>DELETE /menu-allocation/{id}</b> - Deletes a Menu Allocation record by its ID.</li>
 * </ul>
 */
@RestController
@RequestMapping(value = ApiPathConstant.MENU_ALLOCATION)
@Tag(name = SwaggerConstant.MENU_ALLOCATION)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuAllocationController {

	/**
	 * Service responsible for handling menu allocation business logic.
	 */
	MenuAllocationService menuAllocationService;

	/**
	 * Service responsible for managing message retrieval and translation.
	 */
	MessageService messageService;

	/**
	 * Updates a list of Menu Allocations associated with a specific order ID.
	 * 
	 * This method processes a list of Menu Allocation DTOs and updates them in the system.
	 * The order ID is passed as a request parameter to associate the allocations with the specific order.
	 * 
	 * @param menuAllocationDTOs List of Menu Allocation DTOs to be updated.
	 * @param orderId ID of the order to associate the allocations with.
	 * @return A response container with a message indicating successful update.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Object> update(@RequestBody List<MenuAllocationDTO> menuAllocationDTOs, @RequestParam Long orderId) {
		menuAllocationService.createAndUpdate(menuAllocationDTOs, orderId);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Calculates the raw material cost based on a provided Raw Material Calculation DTO.
	 * 
	 * This method takes a DTO containing raw material details and calculates the associated cost
	 * for the menu allocation. The calculation is based on the data in the request body.
	 * 
	 * @param rawMaterialCalculationDto DTO containing raw material details for the cost calculation.
	 * @return A response container with the result of the raw material calculation.
	 */	
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_CALCULATION)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto< RawMaterialCalculationResponseDto > calculateRawMaterialCost(@Valid RawMaterialCalculationDto rawMaterialCalculationDto) {
		return RequestResponseUtils.generateResponseDto(menuAllocationService.calculateRawMaterialCost(rawMaterialCalculationDto));
	}

	/**
	 * Retrieves Menu Allocations by Order ID.
	 * 
	 * This method fetches the menu allocation data associated with the specified order ID.
	 * The order ID is provided in the request path to fetch the corresponding allocations.
	 * 
	 * @param orderId ID of the order to retrieve the menu allocations for.
	 * @return A response container with a list of Menu Allocation data associated with the given order ID.
	 */
	@GetMapping(value = ApiPathConstant.GET_MENU_ALLOCATION)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<GetMenuAllocationOrderMenuPreparationModel>> read(@PathVariable(name = FieldConstants.COMMON_FIELD_ORDER_ID) Long orderId) {
		return RequestResponseUtils.generateResponseDto(menuAllocationService.findByOrderId(orderId));
	}

	/**
	 * Updates the notes associated with a Menu Allocation.
	 * 
	 * This method allows the user to update notes related to menu allocations. The notes
	 * are passed in the request body as a DTO and processed by the service.
	 * 
	 * @param menuAllocationNotes DTO containing the new notes to be updated.
	 * @return A response container indicating the successful update of the notes.
	 */
	@PutMapping(value = ApiPathConstant.UPDATE_NOTE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Object> updateNotes(@RequestBody CommonNotesDto menuAllocationNotes) {
		menuAllocationService.updateNotes(menuAllocationNotes);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

	/**
	 * Synchronizes raw material data for the specified order ID.
	 * 
	 * This method triggers synchronization of raw material data for the given order ID,
	 * ensuring that the menu allocation information is updated accordingly.
	 * 
	 * @param orderId ID of the order to sync raw material data for.
	 * @return A response container indicating the successful synchronization.
	 */
	@GetMapping(value = ApiPathConstant.SYNC_RAW_MATERIAL)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_ALLOCATION + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<Object> syncRawMaterial(@RequestParam Long orderId) {
		menuAllocationService.syncRawMaterial(orderId);
		return RequestResponseUtils.generateResponseDto(null);
	}

	/**
	 * Deletes a Menu Allocation by ID.
	 * 
	 * This method allows the deletion of a specific Menu Allocation by its ID. The ID is provided
	 * in the request path and used to identify the allocation to be deleted.
	 * 
	 * @param id ID of the Menu Allocation to be deleted.
	 * @return A response container indicating the successful deletion of the allocation.
	 * @throws RestException if the deletion fails.
	 */
	@DeleteMapping(value = ApiPathConstant.ID)
	public ResponseContainerDto<Object> delete(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id) throws RestException {
		menuAllocationService.deleteById(id);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_DELETED));
	}

}