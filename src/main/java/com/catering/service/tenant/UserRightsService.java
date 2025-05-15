package com.catering.service.tenant;

import java.util.List;

import com.catering.dto.tenant.request.UserRightsDto;
import com.catering.dto.tenant.request.UserRightsMainMenuSubMenuDto;
import com.catering.dto.tenant.request.UsersWithMainMenuListDto;
import com.catering.model.tenant.CompanyUserModel;
import com.catering.model.tenant.UserRightsModel;
import com.catering.service.common.GenericService;

/**
 * Service interface for managing user rights and permissions.
 * This interface extends the {@link GenericService} interface, providing methods to work with
 * {@link UserRightsDto} and {@link UserRightsModel} entities, and performs operations related to
 * user access control settings for various menus and actions within an application.
 *
 * The service is responsible for handling CRUD operations, as well as additional methods
 * for managing user-specific access control settings.
 *
 * @see GenericService
 *
 * @author Krushali Talaviya
 * @since August 2023
 */
public interface UserRightsService extends GenericService<UserRightsDto, UserRightsModel, Long> {

	/**
	 * Sets user-specific access control settings for a given {@link CompanyUserModel},
	 * specifying whether the settings should be enabled or disabled.
	 *
	 * @param companyUserModel The company user for whom access control settings are being modified.
	 * @param isTrueOrFalse {@code true} to enable the access control settings, {@code false} to disable.
	 */
	void setUserDataWithMainMenuAndSubMenu(CompanyUserModel companyUserModel, boolean isTrueOrFalse);

	/**
	 * Retrieves a list of user data along with their associated main menu and sub-menu rights.
	 *
	 * @return A list of UsersWithMainMenuListDto objects containing user information and menu rights.
	 */
	List<UsersWithMainMenuListDto> getUserRights();

	/**
	 * Saves or updates user rights data for a list of main menu submenus.
	 * Converts the provided list of UserRightsMainMenuSubMenuDto instances into UserRightsModel instances
	 * and persists them using the userRightsRepository. Audit fields are set for each model.
	 *
	 * @param userRightsMainMenuSubMenuDto - The list of UserRightsMainMenuSubMenuDto instances to be saved or updated.
	 * @param uniqueCode - The current company uniqueCode will get for caching.
	 * @return The list of UserRightsMainMenuSubMenuDto instances after being saved or updated.
	 */
	List<UserRightsMainMenuSubMenuDto> saveOrUpdateUserRight(List<UserRightsMainMenuSubMenuDto> userRightsMainMenuSubMenuDto, String uniqueCode);

}