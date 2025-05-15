package com.catering.dao.menu_with_user_rights;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.MenuWithUserRightsDto;

/**
 * Repository interface for querying main menu data along with user rights information using native queries.
 * Extends {@link org.springframework.data.jpa.repository.JpaRepository} to provide CRUD functionality.
 *
 * This repository interface allows querying for main menu items and their associated user rights data,
 * as well as sub-menu items and their user rights, using native SQL queries.
 * It is used in conjunction with the {@link MenuWithUserRightNativeQueryServiceImpl} service.
 *
 * Note: This interface defines custom query methods using {@link org.springframework.data.jpa.repository.Query}
 * annotations with nativeQuery set to true.
 *
 * @author Krushali Talaviya
 * @since 2023-08-28
 */
public interface MainMenuUserRightsNativeQueryDao extends JpaRepository<MainMenuUserRightsNativeQuery, Long> {

	/**
	 * Retrieves main menu items along with user rights data for a specific user.
	 *
	 * @param userId The ID of the user for whom the menu data is retrieved.
	 * @return A list of {@link MenuWithUserRightsDto} objects representing main menu items with associated user rights.
	 */
	@Query(name = "findMainMenuDataWithUserRightsData", nativeQuery = true)
	List<MenuWithUserRightsDto> getMainMenuWithUserRights(Long userId, boolean isOnlySidebar);

	/**
	 * Retrieves sub-menu items along with user rights data for a specific user and main menu ID.
	 *
	 * @param userId 	 The ID of the user for whom the menu data is retrieved.
	 * @param mainMenuId The ID of the main menu item for which sub-menu data is retrieved.
	 * @return A list of {@link MenuWithUserRightsDto} objects representing sub-menu items with associated user rights.
	 */
	@Query(name = "findSubMenuDataWithUserRightsData", nativeQuery = true)
	List<MenuWithUserRightsDto> getSubMenuWithUserRights(Long userId, Long mainMenuId);

}