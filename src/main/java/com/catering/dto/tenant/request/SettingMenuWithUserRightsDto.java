package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Data Transfer Object (DTO) for managing user rights related to a specific setting menu.
 * This class extends the {@code SettingSubMenuWithUserRightsDto} class.
 *
 * @see SettingSubMenuWithUserRightsDto
 * @author Krushali Talaviya
 * @since 2023-12-04
 */
@Getter
@Setter
public class SettingMenuWithUserRightsDto extends SettingSubMenuWithUserRightsDto {

	/**
	 * Unique identifier for the setting menu.
	 */
	private Long id;

	/**
	 * The path associated with the setting menu.
	 */
	private String path;

	/**
	 * The tag name for the <span> element.
	 */
	private String spanTagName;

	/**
	 * The icon associated with the setting menu.
	 */
	private String icon;

	/**
	 * The CSS class for the label.
	 */
	private String labelClass;

	/**
	 * The tag name for the parameter.
	 */
	private String parameterTagName;

	/**
	 * The ID attribute for the parameter tag.
	 */
	private String parameterTagId;

	/**
	 * The ID of the main menu to which the setting menu belongs.
	 */
	private Long mainMenuId;

	/**
	 * Indicates whether the user has the privilege to add items associated with this setting menu.
	 */
	private Boolean canAdd;

	/**
	 * Indicates whether the user has the privilege to edit items associated with this setting menu.
	 */
	private Boolean canEdit;

	/**
	 * Indicates whether the user has the privilege to delete items associated with this setting menu.
	 */
	private Boolean canDelete;

	/**
	 * Indicates whether the user has the privilege to view items associated with this setting menu.
	 */
	private Boolean canView;

	/**
	 * Indicates whether the user has the privilege to print items associated with this setting menu.
	 */
	private Boolean canPrint;

	/**
	 * Constructs a new SettingMenuWithUserRightsDto with the specified parameters.
	 *
	 * @param id               The unique identifier for the setting menu.
	 * @param path             The path associated with the setting menu.
	 * @param spanTagName      The tag name for the <span> element.
	 * @param icon             The icon associated with the setting menu.
	 * @param labelClass       The CSS class for the label.
	 * @param parameterTagName The tag name for the parameter.
	 * @param parameterTagId   The ID attribute for the parameter tag.
	 * @param mainMenuId       The ID of the main menu to which the setting menu belongs.
	 * @param canAdd           Indicates whether the user can add items.
	 * @param canEdit          Indicates whether the user can edit items.
	 * @param canDelete        Indicates whether the user can delete items.
	 * @param canView          Indicates whether the user can view items.
	 * @param canPrint         Indicates whether the user can print items.
	 */
	public SettingMenuWithUserRightsDto(Long id, String path, String spanTagName, String icon, String labelClass,String parameterTagName, String parameterTagId, 
			Long mainMenuId, Boolean canAdd, Boolean canEdit, Boolean canDelete, Boolean canView, Boolean canPrint) {
		this.id = id;
		this.path = path;
		this.spanTagName = spanTagName;
		this.icon = icon;
		this.labelClass = labelClass;
		this.parameterTagName = parameterTagName;
		this.parameterTagId = parameterTagId;
		this.mainMenuId = mainMenuId;
		this.canAdd = canAdd;
		this.canEdit = canEdit;
		this.canDelete = canDelete;
		this.canView = canView;
		this.canPrint = canPrint;
	}

}