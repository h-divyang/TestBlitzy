package com.catering.enums;

import java.util.ArrayList;
import java.util.List;

import com.catering.dto.common.RoleDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing different roles in the system.
 * Each role has an associated ID, role name, and role description.
 * <p>The roles are used to define user permissions and access levels.</p>
 * 
 * <p>Available roles include:</p>
 * <ul>
 * <li><b>ADMIN:</b> Role for administrators with higher-level permissions.</li>
 * <li><b>STAFF:</b> Role for regular staff members with limited permissions.</li>
 * </ul>
 * 
 * <p>This enum also maintains a static list of RoleDto objects, {@link #ROLES}, 
 * which contains all roles as data transfer objects.</p>
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

	ADMIN(1L, "ADMIN", "DESIGNATION.ADMIN"),
	STAFF(2L, "STAFF", "DESIGNATION.STAFF");

	private Long id;
	private String role;
	private String name;

	/**
	 * A list of RoleDto objects representing various roles.
	 * This list is initialized with RoleEnum values during class initialization.
	 * The list is immutable as it is declared as 'final'.
	 */
	public static final List<RoleDto> ROLES = new ArrayList<>();

	/**
	 * Initializes the 'ROLES' list with RoleDto objects corresponding to RoleEnum values.
	 * This static initializer block is executed once when the class is loaded.
	 * It creates a new RoleDto object for each RoleEnum and adds it to the 'ROLES' list.
	 * The RoleDto object is constructed with the ID, role, and name obtained from the RoleEnum.
	 *
	 * @see RoleEnum
	 * @see RoleDto
	 */
	static {
		for (RoleEnum roleEnum : RoleEnum.values()) {
			RoleDto roleData = new RoleDto(roleEnum.getId(), roleEnum.getRole(), roleEnum.getName());
			ROLES.add(roleData);
		}
	}

}