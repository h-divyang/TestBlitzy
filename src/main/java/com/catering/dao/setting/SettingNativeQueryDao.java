package com.catering.dao.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.SettingMenuWithUserRightsDto;

/**
 * Data Access Object (DAO) interface for executing native queries related to settings,
 * providing methods to retrieve setting menus and submenus with user rights using custom SQL queries.
 *
 * @author Krushali Talaviya
 * @since 2023-12-04
 */
public interface SettingNativeQueryDao extends JpaRepository<SettingNativeQuery, Long> {

	/**
	 * Retrieves a list of setting menus and submenus with user rights for a given user ID using a native SQL query.
	 *
	 * @param userId The unique identifier of the user.
	 * @return A list of {@code SettingMenuWithUserRightsDto} representing user rights for setting menus.
	 */
	@Query(name = "getSettingMenuWithUserRights", nativeQuery = true)
	List<SettingMenuWithUserRightsDto> getSettingMenuWithUserRights(Long userId);

	/**
	 * Retrieves a list of submenus with user rights for a given user ID and main menu ID using a native SQL query.
	 *
	 * @param userId     The unique identifier of the user.
	 * @param mainMenuId The ID of the main menu.
	 * @return A list of {@code SettingMenuWithUserRightsDto} representing user rights for submenus.
	 */
	@Query(name = "getSettingSubMenuWithUserRights", nativeQuery = true)
	List<SettingMenuWithUserRightsDto> getSettingSubMenuWithUserRights(Long userId, Long mainMenuId);

}