package com.catering.controller.tenant;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.MessagesConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.UserRightsMainMenuSubMenuDto;
import com.catering.dto.tenant.request.UsersWithMainMenuListDto;
import com.catering.service.common.MessageService;
import com.catering.service.tenant.UserRightsService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller class that handles HTTP requests related to user rights management.
 * This class defines endpoints for retrieving and updating user rights configurations
 * for main menu and sub-menu items.
 *
 * @author Krushali Talaviya
 * @since August 2023
 *
 */
@RestController
@RequestMapping(value = ApiPathConstant.USER_RIGHTS)
@Tag(name = SwaggerConstant.USER_RIGHTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRightsController {

	/**
	 * Service for managing user rights and permissions.
	 */
	UserRightsService userRightsService;

	/**
	 * Service for handling messaging operations.
	 */
	MessageService messageService;

	/**
	 * Handles HTTP GET requests to retrieve a list of users with their main menu and sub-menu data.
	 *
	 * @param filterDto The filter criteria for querying user rights data.
	 * @return A ResponseContainerDto containing a list of UsersWithMainMenuListDto.
	 */
	@GetMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER_RIGHTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<UsersWithMainMenuListDto>> read() {
		return RequestResponseUtils.generateResponseDto(userRightsService.getUserRights());
	}

	/**
	 * Handles HTTP PUT requests to update or save user rights configuration for main menu and sub-menu items.
	 *
	 * @param userRightsMainMenuSubMenuDto The DTO containing user rights data to be updated or saved.
	 * @return A ResponseContainerDto containing the updated UserRightsMainMenuSubMenuDto and a success message.
	 */
	@PutMapping
	@AuthorizeUserRights(value = {ApiUserRightsConstants.USER_RIGHTS + ApiUserRightsConstants.CAN_EDIT})
	public ResponseContainerDto<List<UserRightsMainMenuSubMenuDto>> save(@RequestBody List<UserRightsMainMenuSubMenuDto> userRightsMainMenuSubMenuDto, @RequestParam String uniqueCode) {
		return RequestResponseUtils.generateResponseDto(userRightsService.saveOrUpdateUserRight(userRightsMainMenuSubMenuDto, uniqueCode), messageService.getMessage(MessagesConstant.REST_RESPONSE_DATA_UPDATED));
	}

}