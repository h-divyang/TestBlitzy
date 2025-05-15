package com.catering.service.tenant.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.catering.dao.user_rights.UserRightsNativeQueryService;
import com.catering.dto.tenant.request.MainMenuDto;
import com.catering.dto.tenant.request.UserRightsDto;
import com.catering.dto.tenant.request.UserRightsMainMenuSubMenuDto;
import com.catering.dto.tenant.request.UsersWithMainMenuListDto;
import com.catering.model.tenant.CompanyUserModel;
import com.catering.model.tenant.CompanyUserModelForAudit;
import com.catering.model.tenant.MainMenuModel;
import com.catering.model.tenant.UserRightsModel;
import com.catering.repository.tenant.CompanyUserRepository;
import com.catering.repository.tenant.MainMenuRepository;
import com.catering.repository.tenant.UserRightsRepository;
import com.catering.service.common.CommonService;
import com.catering.service.common.ModelMapperService;
import com.catering.service.common.impl.GenericServiceImpl;
import com.catering.service.tenant.UserRightsService;
import com.catering.util.DataUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing user rights and permissions.
 * This class provides methods to handle user access control settings for various menus and actions
 * within an application. It extends the {@link GenericServiceImpl} class to inherit generic service functionality.
 *
 * The service is responsible for CRUD operations and additional methods related to user-specific access control settings.
 *
 * @see GenericServiceImpl
 *
 * Annotations:
 * - @Service: Marks this class as a Service component in Spring's context.
 * - @RequiredArgsConstructor: Generates a constructor for final fields, enabling dependency injection.
 * - @FieldDefaults: Configures field access level as private and makes fields final wherever applicable.
 *
 * @author Krushali Talaviya
 * @since August 2023
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRightsServiceImpl extends GenericServiceImpl<UserRightsDto, UserRightsModel, Long> implements UserRightsService {

	/**
	 * Repository for managing main menu data.
	 */
	MainMenuRepository mainMenuRepository;

	/**
	 * Service for mapping models and DTOs.
	 */
	ModelMapperService modelMapperService;

	/**
	 * Repository for managing user rights data.
	 */
	UserRightsRepository userRightsRepository;

	/**
	 * Utility for mapping between objects.
	 */
	ModelMapper modelMapper;

	/**
	 * Repository for managing company user information.
	 */
	CompanyUserRepository companyUserRepository;

	/**
	 * Service for executing native queries related to user rights.
	 */
	UserRightsNativeQueryService userRightsNativeQueryService;

	/**
	 * Service for handling common application functionalities.
	 */
	CommonService commonService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserDataWithMainMenuAndSubMenu(CompanyUserModel companyUserModel, boolean isTrueOrFalse) {
		// Fetch the list of main menu items
		List<MainMenuModel> mainMenuModelList = fetchMainMenuModelList();
		// Convert main menu models to DTOs
		List<MainMenuDto> mainMenuDtos = modelMapperService.convertListEntityAndListDto(mainMenuModelList, MainMenuDto.class);
		List<UserRightsModel> userRightsModelLists = new ArrayList<>();
		// Loop through each main menu DTO to create user rights models
		mainMenuDtos.forEach(mainMenuDto -> {
			// Create a new user rights model
			UserRightsModel userRightModel = new UserRightsModel();
			// Set user-specific information
			userRightModel.setUserId(companyUserModel.getId());
			userRightModel.setMainMenuId(mainMenuDto.getId());
			userRightModel.setCanAdd(isTrueOrFalse);
			userRightModel.setCanPrint(isTrueOrFalse);
			userRightModel.setCanView(isTrueOrFalse);
			userRightModel.setCanEdit(isTrueOrFalse);
			userRightModel.setCanDelete(isTrueOrFalse);
			userRightModel.setIsActive(true);
			CompanyUserModelForAudit auditBy = CompanyUserModelForAudit.builder().build();
			auditBy.setId(1l);
			userRightModel.setCreatedBy(auditBy);
			// Set audit fields using DataUtils
			DataUtils.setAuditFields(userRightsRepository, null, userRightModel);
			// Check for sub-menu items and create user rights models for each
			if (Objects.nonNull(mainMenuDto.getSubMenu()) && !mainMenuDto.getSubMenu().isEmpty()) {
				mainMenuDto.getSubMenu().forEach(subMenu -> {
					// Create a new user rights model for sub-menu
					UserRightsModel userRightSubMenuModel = new UserRightsModel();
					// Map properties from the main menu model
					modelMapper.map(userRightModel, userRightSubMenuModel);
					// Set sub-menu specific information
					userRightSubMenuModel.setSubMenuId(subMenu.getId());
					// Add sub-menu user rights model to the list
					userRightsModelLists.add(userRightSubMenuModel);
				});
			} else {
				// No sub-menu, add main menu user rights model to the list
				userRightsModelLists.add(userRightModel);
			}
		});
		// Save all user rights models to the repository
		userRightsRepository.saveAll(userRightsModelLists);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UsersWithMainMenuListDto> getUserRights() {
		// Retrieve active company user models
		List<CompanyUserModel> companyUserModels = companyUserRepository.findAllByIsActiveOrderByIdAsc(true);
		// Convert company user models to user DTOs
		List<UsersWithMainMenuListDto> userDtos = modelMapperService.convertListEntityAndListDto(companyUserModels, UsersWithMainMenuListDto.class);
		// Populate main menu and sub-menu data for each user
		userDtos.forEach(user -> user.setMainMenu(userRightsNativeQueryService.getMainMenuWithuserRightsData(user.getId())));
		// Return the list of user DTOs with main menu and sub-menu data
		return userDtos;
	}

	/**
	 * {@inheritDoc}
	 * See {@link UserRightsService#saveOrUpdateUserRight(List, String)} for details.
	 */
	@Override
	public List<UserRightsMainMenuSubMenuDto> saveOrUpdateUserRight(List<UserRightsMainMenuSubMenuDto> userRightsMainMenuSubMenuDto, String uniqueCode) {
		// Convert sub-menu items to UserRightsModel instances and save
		List<UserRightsModel> userRightsModels = modelMapperService.convertListEntityAndListDto(userRightsMainMenuSubMenuDto, UserRightsModel.class);
		userRightsModels.forEach(userRights -> DataUtils.setAuditFields(userRightsRepository, userRights.getId(), userRights));
		userRightsModels.forEach(userRights -> {
			if (Objects.isNull(userRights.getId())) {
				userRights.setIsActive(true);
				userRights.setEditCount(1);
			}
		});
		userRightsRepository.saveAll(userRightsModels);
		commonService.updateCaching(uniqueCode);
		return userRightsMainMenuSubMenuDto;
	}

	/**
	 * Fetches the list of main menu items for user rights settings, sorted by sequence.
	 *
	 * @return A list of {@link MainMenuModel} representing the main menu items.
	 */
	@Cacheable("MenuForTheUerRight")
	private List<MainMenuModel> fetchMainMenuModelList() {
		return mainMenuRepository.findAll(Sort.by(Sort.Direction.ASC, "sequence"));
	}

}