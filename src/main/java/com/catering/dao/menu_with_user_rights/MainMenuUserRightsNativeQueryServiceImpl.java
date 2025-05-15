package com.catering.dao.menu_with_user_rights;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.MenuWithUserRightsDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link MenuWithUSerRightNativeQueryService} interface that provides methods for querying main menu data
 * along with user rights information using native queries.
 *
 * This service retrieves main menu items and their associated user rights data for a specific user.
 * It also populates each main menu item with its corresponding sub-menu items and their user rights.
 *
 * @author Krushali Talaviya
 * @since 2023-08-28
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainMenuUserRightsNativeQueryServiceImpl implements MainMenuUserRightsNativeQueryService {

	/**
	 * DAO interface to interact with the database for fetching main menu items 
	 * and their respective user rights based on a user's ID.
	 */
	MainMenuUserRightsNativeQueryDao mainMenuUserRightsNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuWithUserRightsDto> getMainMenuDataWithuserRightsData(Long userId) {
		// Retrieve main menu items with associated user rights
		return setSubmenuUserRights(userId, mainMenuUserRightsNativeQueryDao.getMainMenuWithUserRights(userId, true));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuWithUserRightsDto> getAllSidebarItemsUserRights(Long userId) {
		// Retrieve main menu items with associated user rights
		return setSubmenuUserRights(userId, mainMenuUserRightsNativeQueryDao.getMainMenuWithUserRights(userId, false));
	}

	/**
	 * Sets the sub-menu items with associated user rights for each main menu item.
	 * 
	 * @param userId the ID of the user whose rights are being checked.
	 * @param userRightWithMainMenuDtos a list of main menu items with user rights information.
	 * @return the updated list of main menu items, each with its sub-menu items and user rights.
	 */
	private List<MenuWithUserRightsDto> setSubmenuUserRights(Long userId, List<MenuWithUserRightsDto> userRightWithMainMenuDtos) {
		// For each main menu item, retrieve and set its sub-menu items along with user rights
		userRightWithMainMenuDtos.forEach(userRightWithMainMenuDto -> userRightWithMainMenuDto.setSubMenu(mainMenuUserRightsNativeQueryDao.getSubMenuWithUserRights(userId, userRightWithMainMenuDto.getMainMenuId())));
		return userRightWithMainMenuDtos;
	}

}