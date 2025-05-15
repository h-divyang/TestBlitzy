package com.catering.dao.user_rights;

import java.util.List;

import com.catering.dto.tenant.request.UserRightsMainMenuSubMenuDto;

/**
 * Service interface for retrieving user rights information related to main menus.
 * This interface defines methods to retrieve and process user rights data for main menus.
 * Implementing classes should provide logic to fetch and return user rights information
 * using appropriate data access mechanisms.
 * 
 * @author Krushali Talaviya
 * @since August 2023
 */

public interface UserRightsNativeQueryService {

	/**
	 * Retrieves a list of UserRightsMainMenuSubMenuDto objects containing user rights information
	 * for main menus based on the provided user ID.
	 *
	 * @param userId The user ID for which to retrieve user rights data.
	 * @return A list of UserRightsMainMenuSubMenuDto objects representing user rights information.
	 */
	List<UserRightsMainMenuSubMenuDto> getMainMenuWithuserRightsData(Long userId);

}