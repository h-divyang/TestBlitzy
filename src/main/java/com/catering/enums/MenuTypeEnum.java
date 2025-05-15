package com.catering.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing different types of menus in the system.
 * This enum is used to categorize menus into different types, 
 * such as "Custom" and "Package".
 * 
 * <p>Each enum constant has a unique ID and a name associated with it.</p>
 * 
 * <p>It also provides a static method to get the map of all menu types,
 * which maps each menu type's ID to its corresponding {@link MenuTypeEnum} constant.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * MenuTypeEnum menuType = MenuTypeEnum.getMenuTypes().get(1L);
 * </pre>
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {

	CUSTOM(1L, "Custom"),
	PACKAGE(2L, "Package");

	private Long id;
	private String name;
	private static final Map<Long, MenuTypeEnum> menuTypes = new HashMap<>();

	/**
	 * Returns the map of all menu types, mapping their IDs to the corresponding enum constants.
	 * 
	 * @return a map of menu type IDs to their corresponding enum constant
	 */
	public static Map<Long, MenuTypeEnum> getMenuTypes() {
		return menuTypes;
	}

	static {
		// Populate the map with all menu type enum constants
		for (MenuTypeEnum e : MenuTypeEnum.values()) {
			menuTypes.put(e.getId(), e);
		}
	}

}