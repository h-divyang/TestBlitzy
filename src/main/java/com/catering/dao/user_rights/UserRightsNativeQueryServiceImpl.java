package com.catering.dao.user_rights;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.UserRightsMainMenuSubMenuDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the UserRightsNativeQueryService interface for retrieving user rights information related to main menus and submenus.
 * This class provides methods to fetch user rights data using the UserRightsNativeQueryDao and constructs a hierarchical structure
 * of UserRightsMainMenuSubMenuDto objects representing user rights for main menus and their associated submenus.
 * 
 * @author Krushali Talaviya
 * @since August 2023
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRightsNativeQueryServiceImpl implements UserRightsNativeQueryService {

	/**
	 * Data Access Object (DAO) for handling user rights-related queries.
	 * This component interacts with the database to fetch, update, or manage user rights 
	 * and permissions in the system. Typically used in a service layer for managing 
	 * user access control and authorization.
	 */
	UserRightsNativeQueryDao userRightsNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserRightsMainMenuSubMenuDto> getMainMenuWithuserRightsData(Long userId) {
		List<UserRightsMainMenuSubMenuDto> userRightWithMainMenuDtos = userRightsNativeQueryDao.getMainMenuWithUserRights(userId);

		// Populate submenus for each main menu
		userRightWithMainMenuDtos.forEach(mainMenu -> mainMenu.setSubMenu(userRightsNativeQueryDao.getSubMenuWithUserRights(userId, mainMenu.getMainMenuId())));
		return userRightWithMainMenuDtos;
	}

}