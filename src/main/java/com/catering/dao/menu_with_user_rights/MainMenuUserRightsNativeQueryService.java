package com.catering.dao.menu_with_user_rights;

import java.util.List;

import com.catering.dto.tenant.request.MenuWithUserRightsDto;

/**
 * Service interface for querying main menu data along with user rights information using native queries.
 * Provides methods to retrieve menu items with associated user rights for a specific user.
 *
 * @author Krushali Talaviya
 * @since 2023-08-28
 */
public interface MainMenuUserRightsNativeQueryService {

	/**
	 * Retrieves a list of menu items along with user rights data for a specific user.
	 *
	 * @param userId The ID of the user for whom the menu data is retrieved.
	 * @return A list of MenuWithUserRightsDto objects representing menu items with associated user rights.
	 */
	List<MenuWithUserRightsDto> getMainMenuDataWithuserRightsData(Long userId);

	/**
	 * Fetch all sidebar items along with user rights for a specific user.
	 *
	 * @param userId The ID of the user for whom sidebar items with rights are to be fetched.
	 * @return A list of MenuWithUserRightsDto objects containing the sidebar items with user rights.
	 */
	List<MenuWithUserRightsDto> getAllSidebarItemsUserRights(Long userId);

}