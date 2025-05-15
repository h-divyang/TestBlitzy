package com.catering.service.tenant;

import java.util.List;
import com.catering.dto.tenant.request.MenuHeaderDto;
import com.catering.dto.tenant.request.MenuWithUserRightsDto;
import com.catering.model.tenant.MenuHeaderModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for interacting with main menu data and user rights, providing methods to read main menu data
 * along with user rights information for a specific user.
 *
 * This interface extends the {@link GenericService} interface, inheriting common CRUD functionality.
 * The {@link #read(Long)} method is specifically designed to retrieve main menu items along with their associated
 * user rights data and sub-menu items for a given user.
 *
 * This method delegates the task to the {@link MenuWithUSerRightNativeQueryService},
 * which fetches main menu items and their associated user rights using the provided user ID.
 * It also populates each main menu item with its corresponding sub-menu items and their user rights.
 *
 * @param <MenuHeaderDto> The Data Transfer Object (DTO) type for menu headers.
 * @param <MenuHeaderModel> The Model type for menu headers.
 * @param <Long> The type of the ID attribute.
 * @author Krushali Talaviya
 * @since 2023-08-28
 */
public interface MenuHeaderService extends GenericService<MenuHeaderDto, MenuHeaderModel, Long> {

	/**
	 * Reads and retrieves main menu data along with user rights information for a specific user.
	 *
	 * This method delegates the task to the {@link MenuWithUSerRightNativeQueryService},
	 * which fetches main menu items and their associated user rights using the provided user ID.
	 * It also populates each main menu item with its corresponding sub-menu items and their user rights.
	 *
	 * @param userId The ID of the user for whom the menu data is retrieved.
	 * @return A list of {@link MenuWithUserRightsDto} objects representing main menu items and their associated user rights,
	 *		   along with sub-menu items and their user rights.
	 */
	List<MenuWithUserRightsDto> read(Long userId);

	/**
	 * Retrieves a list of sidebar menu items along with user rights based on the provided user ID and unique code.
	 *
	 * @param userId The ID of the user for whom the sidebar items are to be retrieved.
	 * @param uniqueCode A unique code representing additional context or configuration for fetching the sidebar items.
	 * @return A list of {@link MenuWithUserRightsDto} objects representing the sidebar menu items
	 *		   along with their associated user rights.
	 */
	List<MenuWithUserRightsDto> getAllSidebarItems(Long userId, String uniqueCode);

}