package com.catering.controller.tenant;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.MenuItemCategoryInfoDto;
import com.catering.dto.tenant.request.MenuItemCategoryRupeesDto;
import com.catering.dto.tenant.request.MenuItemInfoDto;
import com.catering.dto.tenant.request.MenuItemRupeesDto;
import com.catering.model.tenant.GetMenuPreparationForMenuItemCategoryModel;
import com.catering.model.tenant.GetMenuPreparationModel;
import com.catering.model.tenant.GetMenuPreparationRawMaterialModel;
import com.catering.model.tenant.MenuItemCategoryInfoModel;
import com.catering.model.tenant.MenuItemCategoryRupeesModel;
import com.catering.model.tenant.MenuItemInfoModel;
import com.catering.model.tenant.MenuItemRupeesModel;
import com.catering.model.tenant.SaveMenuPreparationModel;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.OrderMenuPreparationService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The {@link OrderMenuPreparationController} class is a REST controller responsible for managing the 
 * menu preparation process within an order. It handles operations related to menu items, categories, 
 * and raw materials involved in the preparation of the order.
 *
 * <p>This controller provides endpoints for creating, updating, and retrieving information related to 
 * menu preparations, including operations on menu item categories, raw materials, and the overall order 
 * preparation process.</p>
 *
 * <p>Key responsibilities of this controller:</p>
 * <ul>
 *   <li><b>Create and Update Menu Preparation:</b> Handles the creation and updates to menu preparations
 *       based on a list of menu items and order IDs.</li>
 *   <li><b>Update Menu Item Information:</b> Allows updating of individual menu item categories, rupees 
 *       (price), and other item-specific details in the menu.</li>
 *   <li><b>Retrieve Menu Preparation Details:</b> Provides data on the preparation process for specific
 *       orders, including fetching raw materials and associated information for menu items.</li>
 *   <li><b>Access Raw Material Information:</b> Fetches details about raw materials used in menu 
 *       preparations, including material data linked to specific menu items.</li>
 * </ul>
 *
 * <p>Endpoints provided by this controller include:</p>
 * <ul>
 *   <li><b>POST /order-menu-preparation</b> - Create or update the menu preparation details for a given order.</li>
 *   <li><b>PUT /menu-item-category-info</b> - Update information about the category of a menu item.</li>
 *   <li><b>PUT /menu-item-category-rupees</b> - Update rupees (price) for a menu item category.</li>
 *   <li><b>PUT /menu-item-info</b> - Update general information for menu items.</li>
 *   <li><b>PUT /menu-item-rupees</b> - Update rupees (price) for specific menu items.</li>
 *   <li><b>GET /get-menu-preparation/{id}</b> - Retrieve the menu preparation details for a specific order.</li>
 *   <li><b>GET /get-menu-preparation-raw-material</b> - Fetch all raw material information used in menu preparation.</li>
 *   <li><b>GET /get-menu-preparation-raw-material/{menuItemId}</b> - Fetch raw materials used by a specific menu item.</li>
 *   <li><b>GET /get-menu-preparation-menu-item-category-menu-item-exist/{orderId}</b> - Retrieve menu item categories 
 *       that exist for the specified order, based on the menu preparation status.</li>
 * </ul>
 */
@RestController
@RequestMapping(value = ApiPathConstant.ORDER_MENU_PREPARATION)
@Tag(name = SwaggerConstant.ORDER_MENU_PREPARATION)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderMenuPreparationController {

	/**
	 * Service to handle the core logic for menu preparation processes.
	 */
	OrderMenuPreparationService orderMenuPreparationService;

	/**
	 * Service to fetch messages for responses.
	 */
	MessageService messageService;

	/**
	 * Creates or updates the menu preparation details for a given order.
	 * <p>
	 * This endpoint allows for creating or updating menu preparation items by accepting a list of 
	 * {@link SaveMenuPreparationModel} objects and a corresponding order ID.
	 * </p>
	 *
	 * @param saveMenuPreparationModels the list of menu preparation models to be created or updated.
	 * @param orderId the order ID associated with the menu preparation.
	 * @return a response containing the created or updated {@link SaveMenuPreparationModel} with a success message.
	 */
	@PostMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<Object> create(@RequestBody List<SaveMenuPreparationModel> saveMenuPreparationModels, @RequestParam Long orderId) {
		orderMenuPreparationService.createAndUpdate(saveMenuPreparationModels, orderId);
		return RequestResponseUtils.generateResponseDto(null, messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates the menu item category information for a given menu item.
	 * <p>
	 * This endpoint updates the category information for a specific menu item using the data provided 
	 * in the {@link MenuItemCategoryInfoDto}.
	 * </p>
	 *
	 * @param categoryInfo the data transfer object containing the new category information for the menu item.
	 * @return a response containing the updated {@link MenuItemCategoryInfoModel} along with a success message.
	 */
	@PutMapping(value = ApiPathConstant.MENU_ITEM_CATEGORY_INFO)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<MenuItemCategoryInfoModel> updateMenuItemCategoryInfo(@RequestBody MenuItemCategoryInfoDto categoryInfo) {
		return RequestResponseUtils.generateResponseDto(orderMenuPreparationService.updateMenuItemCategoryInfo(categoryInfo), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates the general information for a menu item.
	 * <p>
	 * This endpoint updates the general information for a menu item as specified in the 
	 * {@link MenuItemInfoDto}.
	 * </p>
	 *
	 * @param menuItemInfo the data transfer object containing the new information for the menu item.
	 * @return a response containing the updated {@link MenuItemInfoModel} along with a success message.
	 */
	@PutMapping(value = ApiPathConstant.MENU_ITEM_CATEGORY_RUPEES)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<MenuItemCategoryRupeesModel> updateMenuItemCategoryRupees(@RequestBody MenuItemCategoryRupeesDto categoryRupees) {
		return RequestResponseUtils.generateResponseDto(orderMenuPreparationService.updateMenuItemCategoryRupees(categoryRupees), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates the general information for a menu item.
	 * <p>
	 * This endpoint updates the general information for a menu item as specified in the 
	 * {@link MenuItemInfoDto}.
	 * </p>
	 *
	 * @param menuItemInfo the data transfer object containing the new information for the menu item.
	 * @return a response containing the updated {@link MenuItemInfoModel} along with a success message.
	 */
	@PutMapping(value = ApiPathConstant.MENU_ITEM_INFO)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<MenuItemInfoModel> updateMenuItemInfo(@RequestBody MenuItemInfoDto menuItemInfo) {
		return RequestResponseUtils.generateResponseDto(orderMenuPreparationService.updateMenuItemInfo(menuItemInfo), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Updates the rupees (price) for a specific menu item.
	 * <p>
	 * This endpoint updates the price (rupees) for a menu item as specified in the 
	 * {@link MenuItemRupeesDto}.
	 * </p>
	 *
	 * @param menuItemRupees the data transfer object containing the updated rupees (price) for the menu item.
	 * @return a response containing the updated {@link MenuItemRupeesModel} along with a success message.
	 */
	@PutMapping(value = ApiPathConstant.MENU_ITEM_RUPEES)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<MenuItemRupeesModel> updateMenuItemRupees(@RequestBody MenuItemRupeesDto menuItemRupees) {
		return RequestResponseUtils.generateResponseDto(orderMenuPreparationService.updateMenuItemRupees(menuItemRupees), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_ADDED));
	}

	/**
	 * Retrieves the menu preparation details for a given order ID.
	 * <p>
	 * This endpoint provides the menu preparation details associated with a specific order ID. 
	 * It fetches the relevant data from the {@link OrderMenuPreparationService}.
	 * </p>
	 *
	 * @param id the order ID for which menu preparation details are to be retrieved.
	 * @return a response containing a list of {@link GetMenuPreparationModel} objects with the menu preparation details.
	 */
	@GetMapping(value = ApiPathConstant.GET_MENU_PREPARATION)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_VIEW, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD}, checkAll = false)
	public ResponseContainerDto<List<GetMenuPreparationModel>> readForMenuPreparationAPI(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id) {
		return RequestResponseUtils.generateResponseDto(orderMenuPreparationService.getMenuPreparationByOrderId(id));
	}

	/**
	 * Retrieves a list of all active raw materials used in the menu preparation.
	 * <p>
	 * This endpoint provides a list of all active raw materials currently in use for menu preparation.
	 * </p>
	 *
	 * @return a response containing a list of {@link GetMenuPreparationRawMaterialModel} objects with the raw material details.
	 */
	@GetMapping(value = ApiPathConstant.GET_MENU_PREPARATION_RAW_MATEIRAL)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT}, checkAll = false)
	public ResponseContainerDto<List<GetMenuPreparationRawMaterialModel>> findAllByIsActiveTrue() {
		return RequestResponseUtils.generateResponseDto(orderMenuPreparationService.getAllRawMaterial());
	}

	/**
	 * Retrieves raw materials for a specific menu item based on its ID.
	 * <p>
	 * This endpoint provides a list of raw materials used for a given menu item, identified by its 
	 * menu item ID.
	 * </p>
	 *
	 * @param menuItemId the menu item ID for which raw materials are to be retrieved.
	 * @return a response containing a list of {@link CommonDataForDropDownDto} objects with raw material details.
	 */
	@GetMapping(value = ApiPathConstant.GET_MENU_PREPARATION_RAW_MATEIRAL_BY_MENU_ITEM_ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findRawMaterialByMenuItemId(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long menuItemId) {
		return RequestResponseUtils.generateResponseDto(orderMenuPreparationService.getRawMaterialByMenuItemId(menuItemId));
	}

	/**
	 * Retrieves menu item categories for the menu item existence in the preparation process.
	 * <p>
	 * This endpoint fetches the menu item categories that exist for a specific order, based on the 
	 * current menu preparation status.
	 * </p>
	 *
	 * @param orderId the order ID associated with the menu item category information.
	 * @param request the HTTP request containing additional context, such as tenant information.
	 * @return a response containing a list of {@link GetMenuPreparationForMenuItemCategoryModel} objects with 
	 *         menu item category details.
	 */
	@GetMapping(value = ApiPathConstant.GET_MENU_PREPARATION_MENU_ITEM_CATEGORY_MENU_ITEM_EXIST)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_ADD, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_EDIT, ApiUserRightsConstants.MENU_PREPARATION + ApiUserRightsConstants.CAN_VIEW}, checkAll = false)
	public ResponseContainerDto<List<GetMenuPreparationForMenuItemCategoryModel>> getByIsActiveAndFinal(@PathVariable(name = FieldConstants.COMMON_FIELD_ORDER_ID) Long orderId, HttpServletRequest request) {
		return RequestResponseUtils.generateResponseDto(orderMenuPreparationService.getMenuItemCategoryByMenuItemExist(orderId, request.getAttribute(Constants.TENANT).toString()));
	}

}