package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a model class for managing user rights and permissions.
 * This class is used to define access control settings for various menus and actions
 * within an application. It extends the {@link AuditByIdModel} class to inherit auditing properties.
 *
 * <p>The class is annotated with various annotations including {@code @Entity} to indicate that
 * it is a JPA entity, {@code @Table} to specify the corresponding database table, and {@code @Builder}
 * to enable a builder pattern for constructing instances.</p>
 *
 * <p>Each instance of this class corresponds to a specific user's rights and permissions for accessing
 * different menus and performing actions such as adding, editing, deleting, viewing, and printing.</p>
 *
 * <p>The class provides getter and setter methods for each attribute to enable manipulation of user rights
 * and access control settings. It also includes constructor methods for creating instances of the class,
 * allowing both parameterized construction and no-argument construction with default values.</p>
 *
 * @see AuditByIdModel
 * @see Entity
 * @see Table
 * @see Builder
 * @see NoArgsConstructor
 * @see AllArgsConstructor
 *
 * @author Krushali Talaviya
 * @since August 2023
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_rights")
public class UserRightsModel extends AuditByIdModel {

	/**
	 * The unique identifier of the user associated with these rights.
	 */
	@Column(name = "fk_user_id")
	private Long userId;

	/**
	 * The identifier of the main menu that these rights pertain to.
	 */
	@Column(name = "fk_main_menu_id")
	private Long mainMenuId;

	/**
	 * The identifier of a sub-menu under the main menu.
	 */
	@Column(name = "fk_sub_menu_id")
	private Long subMenuId;

	/**
	 * Indicates whether the user is granted the privilege to add items.
	 */
	@Column(name = "can_add")
	private Boolean canAdd;

	/**
	 * Indicates whether the user is granted the privilege to edit items.
	 */
	@Column(name = "can_edit")
	private Boolean canEdit;

	/**
	 * Indicates whether the user is granted the privilege to delete items.
	 */
	@Column(name = "can_delete")
	private Boolean canDelete;

	/**
	 * Indicates whether the user is granted the privilege to view items.
	 */
	@Column(name = "can_view")
	private Boolean canView;

	/**
	 * Indicates whether the user is granted the privilege to print items.
	 */
	@Column(name = "can_print")
	private Boolean canPrint;

}