package com.catering.dto.tenant.request;

import com.catering.dto.audit.AuditIdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object (DTO) class representing user rights and permissions.
 * This class is used to transfer user access control settings for various menus
 * and actions within an application. It extends the {@link AuditIdDto} class to
 * inherit auditing properties.
 *
 * <p>
 * The class is annotated with {@code @SuperBuilder} to enable a builder pattern
 * for constructing instances.
 * </p>
 *
 * <p>
 * Each instance of this DTO class corresponds to a specific user's rights and
 * permissions for accessing different menus and performing actions such as
 * adding, editing, deleting, viewing, and printing.
 * </p>
 *
 * <p>
 * The class provides getter and setter methods for each attribute to enable
 * manipulation of user rights and access control settings. It also includes a
 * no-argument constructor for creating instances of the class.
 * </p>
 *
 * @see AuditIdDto
 * @see SuperBuilder
 *
 * @author Krushali Talaviya
 * @since August 2023
 *
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserRightsDto extends AuditIdDto {

	/**
	 * The unique identifier of the user associated with these rights.
	 */
	private Long userId;

	/**
	 * This field is used to identify a sequence.
	 */
	private Long sequence;

	/**
	 * The identifier of the main menu that these rights pertain to.
	 */
	private Long mainMenuId;

	/**
	 * The identifier of a sub-menu under the main menu.
	 */
	private Long subMenuId;

	/**
	 * Indicates whether the user is granted the privilege to add items.
	 */
	private Boolean canAdd;

	/**
	 * Indicates whether the user is granted the privilege to edit items.
	 */
	private Boolean canEdit;

	/**
	 * Indicates whether the user is granted the privilege to delete items.
	 */
	private Boolean canDelete;

	/**
	 * Indicates whether the user is granted the privilege to view items.
	 */
	private Boolean canView;

	/**
	 * Indicates whether the user is granted the privilege to print items.
	 */
	private Boolean canPrint;

}