package com.catering.service.tenant.impl;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.catering.constant.Constants;
import com.catering.dao.menu_with_user_rights.MainMenuUserRightsNativeQueryService;
import com.catering.dto.tenant.request.MenuHeaderDto;
import com.catering.dto.tenant.request.MenuWithUserRightsDto;
import com.catering.model.tenant.MenuHeaderModel;
import com.catering.service.common.impl.GenericServiceWithAuditIdModelImpl;
import com.catering.service.tenant.MenuHeaderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing menu headers and their associated main menus.
 * This class extends the GenericServiceWithIdModelImpl class and implements the MenuHeaderService interface.
 * It provides methods for reading and managing menu headers and their main menus.
 *
 * The service retrieves menu header and main menu data from repositories, performs necessary conversions,
 * and provides functionality to read and manipulate the menu structure.
 *
 * This service uses the MenuHeaderRepository and MainMenuRepository for data access.
 * It also utilizes the ModelMapperService for entity-to-DTO conversions.
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 *
 * @see GenericServiceWithIdModelImpl
 * @see MenuHeaderService
 * @since July 2023
 * @author Krushali Talaviya
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuHeaderServiceImpl extends GenericServiceWithAuditIdModelImpl<MenuHeaderDto, MenuHeaderModel, Long> implements MenuHeaderService {

	/**
	 * Service for executing native queries related to main menu user rights.
	 */
	MainMenuUserRightsNativeQueryService menuWithUSerRightNativeQueryService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuWithUserRightsDto> read(Long userId) {
		return menuWithUSerRightNativeQueryService.getMainMenuDataWithuserRightsData(userId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Cacheable(value = Constants.SIDEBAR_ALL_MENUS_CACHE, key="#userId+'-'+#uniqueCode")
	@Override
	public List<MenuWithUserRightsDto> getAllSidebarItems(Long userId, String uniqueCode) {
		return menuWithUSerRightNativeQueryService.getAllSidebarItemsUserRights(userId);
	}

}