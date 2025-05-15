package com.catering.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) representing a role.
 * This class contains information related to a specific role.
 * It provides getter methods to access the role's attributes.
 * An all-args constructor is also generated to initialize the object with all the fields.
 * 
 * @author krushali talaviya
 * @since July 2023
 */
@Getter
@AllArgsConstructor
public class RoleDto {

	/**
	 * The unique identifier of the role.
	 */
	private Long id;

	/**
	 * The role's designation or title.
	 */
	private String role;

	/**
	 * The name or description of the role.
	 */
	private String name;

}