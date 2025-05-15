package com.catering.dto.tenant.request;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a Data Transfer Object (DTO) for the main menu.
 * This class extends the OnlyIdDto class and includes additional properties and functionalities.
 *
 * The MainMenuDto class represents the main menu item in a menu system. It contains information
 * such as the title, sequence number, path, icon, icon type, and whether it is a group title.
 * This class is typically used for transferring menu-related data between different layers of an application,
 * such as between the backend and frontend.
 *
 * @author Krushali Talaviya
 * @since July 2023
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MainMenuDto extends MenuHeaderDto {

	/**
	 * The path associated with the main menu. The path represents the destination or URL that the menu item leads to.
	 */
	private String path;

	/**
	 * The icon of the main menu.
	 */
	private String icon;

	/**
	 * The type of the icon associated with the main menu.
	 */
	private String iconType;

	/**
	 * Represents a class name associated with an element.
	 * This field is used to apply CSS classes to an element for styling purposes.
	 * The value of this field is a string representing the class name.
	 */
	private String clazz;

	/**
	 * Represents a list of sub-menu DTOs associated with an element.
	 * This field is annotated with @JsonManagedReference to indicate a managed forward reference
	 * for JSON serialization and deserialization.
	 * The list contains instances of SubMenuDto that represent sub-menu items.
	 */
	@JsonManagedReference
	private List<MenuWithUserRightsDto> subMenu;

	/**
	 * The Api Rights Name of the API's.
	 */
	private String apiRightsName;

	/**
	 * Returns the path associated with this object.
	 *
	 * @return The path as a String, or an empty string if the path is null.
	 */
	public String getPath() {
		return Objects.nonNull(path) ? path : "";
	}

	/**
	 * Returns the icon associated with this object.
	 *
	 * @return The icon as a String, or an empty string if the icon is null.
	 */
	public String getIcon() {
		return Objects.nonNull(icon) ? icon : "";
	}

	/**
	 * Returns the type of icon associated with this object.
	 *
	 * @return The icon type as a String, or an empty string if the icon type is null.
	 */
	public String getIconType() {
		return Objects.nonNull(iconType) ? iconType : "";
	}

	/**
	 * Returns the class1 associated with this object.
	 *
	 * @return The class1 as a String, or an empty string if the class1 is null.
	 */
	public String getClazz() {
		return Objects.nonNull(clazz) ? clazz : "";
	}

	/**
	 * Returns the list of submenu objects associated with this object.
	 *
	 * @return A List of SubMenuDto objects, or an empty list if the submenu is null.
	 */
	public List<MenuWithUserRightsDto> getSubMenu() {
		return Objects.nonNull(subMenu) ? subMenu : Collections.emptyList();
	}

}