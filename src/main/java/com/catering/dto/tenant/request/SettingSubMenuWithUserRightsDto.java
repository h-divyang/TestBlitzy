package com.catering.dto.tenant.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing user rights for a list of sub-menus within a setting.
 * This class is used to encapsulate user rights for each submenu.
 *
 * @see SettingMenuWithUserRightsDto
 * @author Krushali Talaviya
 * @since 2023-12-04
 */
@Getter
@Setter
public class SettingSubMenuWithUserRightsDto {

	/**
	 * List of {@code SettingMenuWithUserRightsDto} objects representing user rights for each setting menu.
	 */
	private List<SettingMenuWithUserRightsDto> settingSubMenuWithUserRightsList;

}