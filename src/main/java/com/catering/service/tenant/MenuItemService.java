package com.catering.service.tenant;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.catering.dto.ResponseContainerDto;
import com.catering.dto.common.FilterDto;
import com.catering.dto.common.SearchFieldDto;
import com.catering.dto.tenant.request.CustomMenuItem;
import com.catering.dto.tenant.request.MenuItemDto;
import com.catering.exception.RestException;
import com.catering.model.tenant.MenuItemModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing menu items. Provides methods for creating, updating,
 * retrieving, and deleting menu items, including handling of associated media files and sub-categories.
 */
public interface MenuItemService extends GenericService<MenuItemDto, MenuItemModel, Long> {

	/**
	 * Creates a new menu item or updates an existing menu item with the provided details, including
	 * optional image file.
	 *
	 * @param menuItemCategoryDto The data transfer object containing the menu item details to create or update.
	 * @param img The MultipartFile representing the image file for the menu item. This parameter is optional.
	 * @return An Optional object containing the created or updated MenuItemDto. If the operation fails, an empty Optional is returned.
	 * @throws RestException If there is an error during the creation or updating process.
	 */
	Optional<MenuItemDto> createAndUpdate(MenuItemDto menuItemCategoryDto, MultipartFile img) throws RestException;

	/**
	 * Handles reading (fetching) a list of MenuItemDto objects with optional filtering,
	 * searching, and sorting. Supports:
	 * - Keyword-based searching using ExampleMatcher
	 * - Filtering by category using Specifications
	 * - Default pagination and sorting
	 *
	 * @param filterDto DTO containing pagination, sorting, and search query
	 * @param searchFieldDto DTO containing specific structured field-based filter info (e.g., category ID)
	 * @param request HTTP request object (optional, can be used for future context-based logic)
	 * @return a ResponseContainerDto wrapping a list of MenuItemDto objects
	 */
	ResponseContainerDto<List<MenuItemDto>> read(FilterDto filterDto, SearchFieldDto searchFieldDto, HttpServletRequest request);

	/**
	 * Retrieves a list of custom menu items.
	 *
	 * @return A ResponseContainerDto containing a list of CustomMenuItem objects and additional response metadata.
	 */
	ResponseContainerDto<List<CustomMenuItem>> read();

	/**
	 * Deletes an entity or associated media files by its unique identifier.
	 *
	 * @param id The unique identifier of the entity to delete.
	 * @param isImage A boolean flag indicating if both image files are available for deletion.
	 */
	void deleteById(Long id, Boolean isImage);

	/**
	 * Checks if an entity exists by its unique identifier.
	 *
	 * @param id The unique identifier of the entity to check.
	 * @throws RestException If the entity with the given identifier does not exist.
	 */
	void existByIdOrThrow(Long id) throws RestException;

	/**
	 * Updates the status of an existing menu item by its unique identifier.
	 *
	 * @param id The unique identifier of the menu item to update.
	 * @param isDefault A boolean flag indicating if the menu item should be marked as default.
	 * @return An Optional containing the updated MenuItemDto if the update is successful, or an empty Optional if no menu item is found with the given identifier.
	 * @throws RestException If an error occurs during the update process.
	 */
	Optional<MenuItemDto> updateStatus(Long id, Boolean isDefault) throws RestException;

	/**
	 * Checks if a menu item sub-category exists by its unique identifier.
	 *
	 * @param id The unique identifier of the menu item sub-category to check.
	 * @return true if a menu item sub-category exists with the provided identifier, otherwise false.
	 */
	boolean existsByMenuItemSubCategoryId(Long id);

	/**
	 * Updates the priority of a menu item based on the provided DTO.
	 *
	 * <p>This method performs business logic validation and delegates the update operation
	 * to the data access layer. It may throw a {@link RestException} in case of validation
	 * failure, missing data, or update errors.</p>
	 *
	 * @param menuItemDto the {@link MenuItemDto} containing the updated priority and necessary metadata
	 * @throws RestException if the update operation fails due to validation errors or internal issues
	 */
	void updatePriority(List<MenuItemDto> menuItems) throws RestException;

	/**
	 * Fetches the highest priority value from the menu items records.
	 * <p>
	 * This method queries the database to determine the maximum value of the
	 * {@code priority} field in the {@code menu_item} table.
	 *
	 * @return the highest priority as a {@code Long}, or {@code null} if no records exist
	 * @throws RestException if an error occurs during the retrieval process
	 */
	Long getHighestPriority() throws RestException;

}