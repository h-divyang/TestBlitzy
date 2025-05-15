package com.catering.dao.setting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.SettingMenuWithUserRightsDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for executing native queries related to settings, providing methods to retrieve setting menus
 * and submenus with user rights using custom SQL queries.
 *
 * @author Krushali Talaviya
 * @since 2023-12-04
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingNativeQueryServiceImpl implements SettingNativeQueryService {

	/**
	 * Data Access Object (DAO) for handling configuration settings-related queries.
	 * This component is responsible for interacting with the database to fetch, update, 
	 * or manage system settings, such as application configurations, preferences, or 
	 * other customizable values that control the system behavior.
	 */
	SettingNativeQueryDao settingNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SettingMenuWithUserRightsDto> getSettingMenuAndSubMenuWithUserRights(Long userId) {
		List<SettingMenuWithUserRightsDto> settingMenuWithUserRights = settingNativeQueryDao.getSettingMenuWithUserRights(userId);
		if (!settingMenuWithUserRights.isEmpty()) {
			settingMenuWithUserRights.forEach(settingMenu -> settingMenu.setSettingSubMenuWithUserRightsList(settingNativeQueryDao.getSettingSubMenuWithUserRights(userId, settingMenu.getMainMenuId())));
		}
		return settingMenuWithUserRights;
	}

}