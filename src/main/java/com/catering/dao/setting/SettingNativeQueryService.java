package com.catering.dao.setting;

import java.util.List;

import com.catering.dto.tenant.request.SettingMenuWithUserRightsDto;

/**
 * Service interface for executing native queries related to settings, providing methods to retrieve setting menus
 * and submenus with user rights using custom SQL queries.
 * 
 * @author Krushali Talaviya
 * @since 2023-12-04
 */
public interface SettingNativeQueryService {

	/**
	 * Retrieves a list of setting menus and submenus with user rights for a given user ID using a native SQL query.
	 *
	 * @param userId The unique identifier of the user.
	 * @return A list of {@code SettingMenuWithUserRightsDto} representing user rights for setting menus.
	 */
	List<SettingMenuWithUserRightsDto> getSettingMenuAndSubMenuWithUserRights(Long userId);

}