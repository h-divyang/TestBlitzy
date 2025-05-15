package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.MenuItemCategoryInfoDto;
import com.catering.dto.tenant.request.MenuItemCategoryRupeesDto;
import com.catering.dto.tenant.request.MenuItemInfoDto;
import com.catering.dto.tenant.request.MenuItemRupeesDto;
import com.catering.dto.tenant.request.OrderMenuPreparationDto;
import com.catering.model.tenant.GetMenuPreparationForMenuItemCategoryModel;
import com.catering.model.tenant.GetMenuPreparationModel;
import com.catering.model.tenant.GetMenuPreparationRawMaterialModel;
import com.catering.model.tenant.MenuItemCategoryInfoModel;
import com.catering.model.tenant.MenuItemCategoryRupeesModel;
import com.catering.model.tenant.MenuItemInfoModel;
import com.catering.model.tenant.MenuItemRupeesModel;
import com.catering.model.tenant.OrderMenuPreparationModel;
import com.catering.model.tenant.SaveMenuPreparationModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing order menu preparation.
 * Provides methods to handle operations related to menu preparation, including creation,
 * updates, and retrieval of menu preparation details, menu item categories, and raw materials.
 * This interface extends a generic service for CRUD operations.
 */
public interface OrderMenuPreparationService extends GenericService<OrderMenuPreparationDto, OrderMenuPreparationModel, Long> {

	/**
	 * Creates and updates menu preparation details based on the provided data.
	 *
	 * @param saveMenuPreparationModels A list of SaveMenuPreparationModel objects containing the menu preparation details to save or update.
	 * @param orderId The ID of the order to which the menu preparation details belong.
	 */
	void createAndUpdate(List<SaveMenuPreparationModel> saveMenuPreparationModels, Long orderId);

	/**
	 * Checks if an entity with the specified custom package ID exists.
	 *
	 * @param id The custom package ID to check for existence.
	 * @return True if an entity with the given custom package ID exists, false otherwise.
	 */
	boolean existsByCustomPackageId(Long id);

	/**
	 * Updates the menu item category information.
	 *
	 * @param categoryInfo The data transfer object containing the details of the category to be updated.
	 * @return The updated MenuItemCategoryInfoModel entity object.
	 */
	MenuItemCategoryInfoModel updateMenuItemCategoryInfo(MenuItemCategoryInfoDto categoryInfo);

	/**
	 * Updates the MenuItemCategoryRupees data with the details provided in the MenuItemCategoryRupeesDto.
	 *
	 * @param categoryRupees The data transfer object containing the updated details for the menu item category rupees.
	 * @return The updated MenuItemCategoryRupeesModel containing the new values for menu item category rupees.
	 */
	MenuItemCategoryRupeesModel updateMenuItemCategoryRupees(MenuItemCategoryRupeesDto categoryRupees);

	/**
	 * Updates the menu item information based on the data provided in the MenuItemInfoDto.
	 *
	 * @param menuItemInfo The data transfer object containing the updated details for the menu item.
	 * @return The updated MenuItemInfoModel object reflecting the changes applied to the menu item information.
	 */
	MenuItemInfoModel updateMenuItemInfo(MenuItemInfoDto menuItemInfo);

	/**
	 * Updates the rupees value for a menu item using the provided data transfer object.
	 *
	 * @param menuItemRupees The data transfer object containing the ID of the menu item and the new rupees value.
	 * @return The updated MenuItemRupeesModel reflecting the changes in the rupees value.
	 */
	MenuItemRupeesModel updateMenuItemRupees(MenuItemRupeesDto menuItemRupees);

	/**
	 * Retrieves a list of menu preparation details associated with a specific order ID.
	 *
	 * @param id The ID of the order for which to fetch the menu preparation details.
	 * @return A list of GetMenuPreparationModel objects representing the menu preparation details for the specified order.
	 */
	List<GetMenuPreparationModel> getMenuPreparationByOrderId(Long id);

	/**
	 * Retrieves a list of all raw materials available for menu preparation.
	 *
	 * @return A list of GetMenuPreparationRawMaterialModel objects representing raw materials.
	 */
	List<GetMenuPreparationRawMaterialModel> getAllRawMaterial();

	/**
	 * Retrieves a list of raw materials associated with a specific menu item ID.
	 *
	 * @param menuItemId The ID of the menu item for which to fetch the associated raw materials.
	 * @return A list of CommonDataForDropDownDto objects representing the raw materials linked to the specified menu item.
	 */
	List<CommonDataForDropDownDto> getRawMaterialByMenuItemId(Long menuItemId);

	/**
	 * Retrieves a list of menu item categories associated with menu items that exist for a given order ID and tenant.
	 *
	 * @param orderId The ID of the order for which to fetch the menu item categories.
	 * @param tenant The tenant identifier used to filter the menu item categories.
	 * @return A list of GetMenuPreparationForMenuItemCategoryModel objects representing
	 *		   the menu item categories for the specified order and tenant.
	 */
	List<GetMenuPreparationForMenuItemCategoryModel> getMenuItemCategoryByMenuItemExist(Long orderId, String tenant);

}