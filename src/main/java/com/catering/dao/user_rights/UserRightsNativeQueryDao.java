package com.catering.dao.user_rights;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.UserRightsMainMenuSubMenuDto;

/**
 * Provides data access operations for retrieving user rights information using named native queries.
 * This interface extends the JpaRepository interface, enabling CRUD operations on UserRightsNativeQuery entities.
 * It offers methods to query and retrieve user rights for main menus and submenus based on specified criteria.
 * UserRightsNativeQueryDao encapsulates the interaction with the database using native SQL queries.
 * 
 * @author Krushali Talaviya
 * @since Auguest 2023
 */
public interface UserRightsNativeQueryDao extends JpaRepository<UserRightsNativeQuery, Long> {

	/**
	 * Retrieves a list of UserRightsMainMenuSubMenuDto objects representing user rights information for main menus.
	 * The query is executed using a named native query "findMainMenuWithUserRights".
	 *
	 * @param userId The user ID for which to retrieve user rights.
	 * @return A list of UserRightsMainMenuSubMenuDto objects containing user rights information.
	 */
	@Query(name = "findMainMenuWithUserRights", nativeQuery = true)
	List<UserRightsMainMenuSubMenuDto> getMainMenuWithUserRights(Long userId);

	/**
	 * Retrieves a list of UserRightsMainMenuSubMenuDto objects representing user rights information for submenus
	 * associated with a specified main menu.
	 * The query is executed using a named native query "findSubMenuWithUserRights".
	 *
	 * @param userId     The user ID for which to retrieve user rights.
	 * @param mainMenuId The ID of the main menu for which to retrieve submenu user rights.
	 * @return A list of UserRightsMainMenuSubMenuDto objects containing user rights information for submenus.
	 */
	@Query(name = "findSubMenuWithUserRights", nativeQuery = true)
	List<UserRightsMainMenuSubMenuDto> getSubMenuWithUserRights(Long userId, Long mainMenuId);

}