package com.catering.dto.tenant.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) class representing user rights configuration for main menu and sub-menu items.
 * Extends UserRightsDto to inherit basic user rights attributes.
 *
 * @author Krushali Talaviya
 * @since August 2023
 *
 */
@Getter
@Setter
@SuperBuilder
@Builder
@NoArgsConstructor
public class UserRightsMainMenuSubMenuDto extends UserRightsDto {

	/**
	 * Represents a menu item, which can be either a main menu or a sub-menu, in the user rights configuration.
	 */
	private String name;

	/**
	 * Represents a list of sub-menu items under a main menu or sub-menu in the user rights configuration.
	 */
	private List<UserRightsMainMenuSubMenuDto> subMenu;

	/**
	 * Constructor for creating a UserRightsMainMenuSubMenuDto instance with specific attribute values.
	 *
	 * @param id        The ID of the user rights configuration.
	 * @param userId    The ID of the associated user.
	 * @param mainMenuId The ID of the main menu item.
	 * @param name      The name of the main menu or sub-menu item.
	 * @param subMenuId The ID of the sub-menu item, if applicable.
	 * @param canAdd     Boolean indicating whether 'Add' privilege is granted.
	 * @param canEdit    Boolean indicating whether 'Edit' privilege is granted.
	 * @param canDelete  Boolean indicating whether 'Delete' privilege is granted.
	 * @param canView    Boolean indicating whether 'View' privilege is granted.
	 * @param canPrint   Boolean indicating whether 'Print' privilege is granted.
	 */
	public UserRightsMainMenuSubMenuDto(Long id, Long userId, Long sequence, Long mainMenuId, String name, Long subMenuId, 
										Boolean canAdd, Boolean canEdit, Boolean canDelete, Boolean canView, Boolean canPrint, Boolean isActive) {
		this.setId(id);
		this.setUserId(userId);
		this.setSequence(sequence);
		this.setMainMenuId(mainMenuId);
		this.name = name;
		this.setSubMenuId(subMenuId);
		this.setCanAdd(canAdd);
		this.setCanEdit(canEdit);
		this.setCanDelete(canDelete);
		this.setCanView(canView);
		this.setCanPrint(canPrint);
		this.setIsActive(isActive);
	}

}