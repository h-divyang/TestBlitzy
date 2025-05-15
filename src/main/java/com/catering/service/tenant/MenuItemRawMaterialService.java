package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.MenuItemRawMaterialDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MenuItemRawMaterialModel;
import com.catering.service.common.GenericService;

/**
 * A service interface for managing operations related to MenuItemRawMaterial entities.
 * Provides methods for creating, updating, deleting, and retrieving data associated
 * with raw materials used in menu items.
 */
public interface MenuItemRawMaterialService extends GenericService<MenuItemRawMaterialDto, MenuItemRawMaterialModel, Long> {

	/**
	 * Creates or updates a MenuItemRawMaterialDto resource.
	 *
	 * The method handles both creation and update scenarios for MenuItemRawMaterialDto entities.
	 * If the entity contains an identifier already associated with an existing resource, it will
	 * update that resource. Otherwise, it will create a new resource.
	 *
	 * @param menuItemRawMaterialDto The MenuItemRawMaterialDto object containing the necessary data for creation or update.
	 * 								 It must not be null and should comply with validation constraints.
	 * @return An Optional containing the created or updated MenuItemRawMaterialDto if the operation
	 *		   is successful. If the operation fails, an empty Optional is returned.
	 * @throws RestException If an error occurs during the operation, such as validation failures or unexpected server errors.
	 */
	Optional<MenuItemRawMaterialDto> createAndUpdate(MenuItemRawMaterialDto menuItemRawMaterialDto) throws RestException;

	/**
	 * Reads and retrieves a paginated, sorted list of MenuItemRawMaterialDto objects, associated with a given MenuItemId,
	 * optionally filtered by a custom query.
	 *
	 * @param menuItemId The unique identifier of the menu item to retrieve associated raw materials for. Must not be null.
	 * @param page The page number to retrieve in the paginated result set. This parameter is optional and can be null.
	 * @param size The number of items to include per page in the paginated result set. This parameter is optional and can be null.
	 * @param sortBy The field by which the results should be sorted. This parameter is optional and can be null.
	 * @param sortDirection The direction of sorting (e.g., ASC or DESC). This parameter is optional and can be null.
	 * @param query A custom query string to filter the result set. This parameter is optional and can be null.
	 * @return A ResponseContainerDto containing a list of MenuItemRawMaterialDto objects and related pagination details.
	 */
	ResponseContainerDto<List<MenuItemRawMaterialDto>> read(Long menuItemId, String page, String size, String sortBy, String sortDirection, String query);

	/**
	 * Deletes an entity by its unique identifier.
	 *
	 * @param id The unique identifier of the entity to delete. Must not be null.
	 */
	void deleteById(Long id);

	/**
	 * Retrieves a list of MenuItemRawMaterialModel entities associated with a given menu item ID.
	 *
	 * @param id The unique identifier of the menu item whose associated raw materials are to be retrieved.
	 *			 This parameter must not be null.
	 * @return A list of MenuItemRawMaterialModel entities associated with the specified menu item ID.
	 */
	List<MenuItemRawMaterialModel> findByMenuItemId(Long id);

	/**
	 * Creates a copy of the provided list of MenuItemRawMaterialDto objects, preserving the data structure
	 * and values of the input list. Each object in the list is duplicated and returned as a new list,
	 * ensuring no references are shared between the original and the copied list.
	 *
	 * @param menuItemRawMaterialDtos The list of MenuItemRawMaterialDto objects to be copied.
	 * 								  Must not be null and should contain valid data objects.
	 * @return A new list containing copies of the objects from the provided list.
	 * @throws RestException If an error occurs during the copying process.
	 */
	List<MenuItemRawMaterialDto> createCopyRecipeList(List<MenuItemRawMaterialDto> menuItemRawMaterialDtos) throws RestException;

}