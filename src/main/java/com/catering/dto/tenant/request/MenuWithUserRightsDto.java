package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a Data Transfer Object (DTO) that extends the MainMenuDto class and includes user rights information for menu items.
 *
 * @author Krushali Talaviya
 * @since 2023-08-28
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MenuWithUserRightsDto extends MainMenuDto {

	/**
	 * The ID of the main menu.
	 */
	private Long mainMenuId;

	/**
	 * Indicates whether the user has permission to add.
	 */
	private Boolean canAdd;

	/**
	 * Indicates whether the user has permission to edit.
	 */
	private Boolean canEdit;

	/**
	 * Indicates whether the user has permission to view.
	 */
	private Boolean canView;

	/**
	 * Indicates whether the user has permission to print.
	 */
	private Boolean canPrint;

	/**
	 * Indicates whether the user has permission to delete.
	 */
	private Boolean canDelete;

	/**
	 * Temporary information associated with the menu item.
	 */
	private String temp;

	/**
	 * Constructs a MenuWithUserRightsDto object with the specified parameters.
	 *
	 * @param title		The title of the menu item.
	 * @param groupTitle	Whether the menu item is a group title.
	 * @param path	 The path associated with the menu item.
	 * @param icon	 The icon representing the menu item.
	 * @param iconType The type of the icon (e.g., image or font-based).
	 * @param clazz		The CSS class to be applied to the menu item.
	 * @param mainMenuId	The ID of the main menu.
	 * @param canAdd  Whether the user has permission to add.
	 * @param canEdit Whether the user has permission to edit.
	 * @param canDelete		Whether the user has permission to delete.
	 * @param canView Whether the user has permission to view.
	 * @param canPrint		Whether the user has permission to print.
	 * @param temp	Temporary information associated with the menu item.
	 */
	public MenuWithUserRightsDto(Long id, String title, Boolean groupTitle, String path, String icon, String iconType, String clazz, Long mainMenuId,
								Boolean canAdd, Boolean canEdit, Boolean canDelete, Boolean canView, Boolean canPrint, String temp, String apiRightsName) {
		this.setId(id);
		this.setTitle(title);
		this.setGroupTitle(groupTitle);
		this.setPath(path);
		this.setIcon(icon);
		this.setIconType(iconType);
		this.setClazz(clazz);
		this.setApiRightsName(apiRightsName);
		this.mainMenuId = mainMenuId;
		this.canAdd = canAdd;
		this.canEdit = canEdit;
		this.canDelete = canDelete;
		this.canView = canView;
		this.canPrint = canPrint;
		this.temp = temp;
	}

}