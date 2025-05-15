package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.tenant.request.MenuItemCategoryAndMenuItemDto;
import com.catering.dto.tenant.request.MenuItemCategoryDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MenuItemCategoryModel;
import com.catering.service.common.GenericService;

/**
 * This interface provides service methods for managing MenuItemCategory entities and their associated
 * data transfer objects (DTOs). It extends the GenericService interface with type parameters specific to
 * MenuItemCategoryDto, MenuItemCategoryModel, and Long identifier.
 */
public interface MenuItemCategoryService extends GenericService<MenuItemCategoryDto, MenuItemCategoryModel, Long> {

	/**
	 * Creates or updates a menu item category.
	 *
	 * This method is responsible for creating a new menu item category or updating
	 * an existing one based on the provided data in the MenuItemCategoryDto object.
	 * If an image is provided, it will be processed and associated with the category.
	 *
	 * @param menuItemCategoryDto The data transfer object containing information about the menu item category.
	 * @param img An optional image file to be associated with the menu item category. If no image is provided, the existing image (if any) will remain unchanged.
	 * @return An Optional containing the created or updated MenuItemCategoryDto. If the operation fails, it returns an empty Optional.
	 * @throws RestException If the creation or update process encounters an error.
	 */
	Optional<MenuItemCategoryDto> createAndUpdate(MenuItemCategoryDto menuItemCategoryDto, MultipartFile img) throws RestException;

	/**
	 * Retrieves a list of menu item categories based on the provided filter criteria.
	 *
	 * @param filterDto The filtering criteria for retrieving menu item categories.
	 * @param request   The HTTP request object, providing additional context for the request.
	 * @return A ResponseContainerDto containing the list of menu item categories and associated metadata.
	 */
	ResponseContainerDto<List<MenuItemCategoryDto>> read(FilterDto filterDto, HttpServletRequest request);

	/**
	 * Checks if an entity exists by its identifier. If the entity does not exist, a {@link RestException} is thrown.
	 *
	 * @param id The unique identifier of the entity to check.
	 * @throws RestException If the entity does not exist.
	 */
	void existByIdOrThrow(Long id) throws RestException;

	/**
	 * Deletes an entity or associated image based on the provided identifier.
	 *
	 * @param id The unique identifier of the entity or image to be deleted.
	 * @param isImage A boolean flag indicating whether the operation is targeting an image (true) or an entity (false).
	 */
	void deleteById(Long id, Boolean isImage);

	/**
	 * Updates the status of a menu item category.
	 *
	 * This method is used to update the status of a menu item category, allowing for the modification
	 * of its default status. If the operation is successful, it returns the updated category details.
	 *
	 * @param id The unique identifier of the menu item category to be updated.
	 * @param isDefault A boolean value indicating the desired default status of the menu item category (true for default, false otherwise).
	 * @return An Optional containing the updated MenuItemCategoryDto if the category is found and updated; otherwise, an empty Optional.
	 * @throws RestException If an error occurs during the update process, or if the category cannot be found.
	 */
	Optional<MenuItemCategoryDto> updateStatus(Long id, Boolean isDefault) throws RestException;

	/**
	 * Retrieves a list of active menu item categories.
	 *
	 * @return A list of MenuItemCategoryDto representing active menu item categories.
	 */
	List<MenuItemCategoryDto> readDataByIsActive();

	/**
	 * Retrieves a list of menu item categories and their associated menu items for a given tenant,
	 * where the menu items are active and exist.
	 *
	 * @param tenant The identifier of the tenant for which the data is to be retrieved.
	 * @return A list of MenuItemCategoryAndMenuItemDto objects representing the menu item
	 *		   categories and their associated menu items that match the specified criteria.
	 */
	List<MenuItemCategoryAndMenuItemDto> readDataByIsActiveMenuItemExist(String tenant);

	/**
	 * Retrieves a menu item category based on its unique identifier.
	 *
	 * @param categoryId The unique identifier of the menu item category to be retrieved.
	 * @return A MenuItemCategoryModel representing the menu item category with the specified identifier.
	 *		   If no category is found, returns null or an appropriate response based on implementation.
	 */
	MenuItemCategoryModel findById(Long categoryId);

	/**
	 * Retrieves a list of MenuItemCategoryDto objects where the menu items are not null,
	 * sorted by their priority in ascending order.
	 *
	 * @return A list of MenuItemCategoryDto instances with non-null menu items, ordered by their priority.
	 */
	List<MenuItemCategoryDto> findByMenuItemsNotNullOrderByPriority();

	/**
	 * Updates the priority of a menu item category based on the provided DTO.
	 *
	 * <p>This method performs business logic validation and delegates the update operation
	 * to the data access layer. It may throw a {@link RestException} in case of validation
	 * failure, missing data, or update errors.</p>
	 *
	 * @param menuItemCategoryDto the {@link MenuItemCategoryDto} containing the updated priority and necessary metadata
	 * @throws RestException if the update operation fails due to validation errors or internal issues
	 */
	void updatePriority(List<MenuItemCategoryDto> menuItemCategories) throws RestException;

	/**
	 * Fetches the highest priority value from the menu item category records.
	 * <p>
	 * This method queries the database to determine the maximum value of the
	 * {@code priority} field in the {@code menu_item_category} table.
	 *
	 * @return the highest priority as a {@code Long}, or {@code null} if no records exist
	 * @throws RestException if an error occurs during the retrieval process
	 */
	Long getHighestPriority() throws RestException;

}