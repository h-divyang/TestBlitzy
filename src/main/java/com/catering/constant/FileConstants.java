package com.catering.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class contains constants representing different file-related paths and module identifiers.
 * These constants are used for organizing and referencing files within specific modules of the application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileConstants {

	public static final String MODULE_DIRECTORY_IMAGE = "image";

	public static final String MODULE_USER_PROFILE = "user-profile";
	public static final String MODULE_MENU_ITEM_CATEGORY = "menu-item-category";
	public static final String MODULE_MENU_ITEM = "menu-item";
	public static final String MODULE_HALL_MASTER = "hall-master";

}