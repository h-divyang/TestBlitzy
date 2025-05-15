package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The SubMenuModel class represents a sub-menu item in the catering system.
 * It extends the IdModel class, which provides an ID for the sub-menu item.
 *
 * <p>A sub-menu item contains the title, sequence number, main menu, and path.</p>
 *
 * <p>This class is annotated with the @Entity annotation to indicate that it is a persistent entity
 * in the database. The @Table annotation specifies the name of the corresponding database table.</p>
 *
 * <p>Use the getter and setter methods to access and modify the attributes of the sub-menu item.</p>
 *
 * <p>The class also provides a builder pattern using the @Builder annotation, allowing for
 * easy and flexible creation of SubMenuModel instances.</p>
 *
 * <p>The sub-menu item is associated with a main menu using a many-to-one relationship.</p>
 *
 * <p>The main menu is referenced by the fk_main_menu_id foreign key column.</p>
 *
 * @author Krushali Talaviya
 * @since July 2023
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sub_menu")
public class SubMenuModel extends AuditIdModelOnly {

	/**
	 * The title of the sub-menu item.
	 */
	@Column(name = "name")
	private String title;

	/**
	 * The sequence number of the sub-menu item.
	 */
	@Column(name = "sequence")
	private int sequence;

	/**
	 * The main menu associated with the sub-menu item.
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "fk_main_menu_id")
	private MainMenuModel mainMenu;

	/**
	 * The path of the sub-menu item.
	 */
	@Column(name = "path")
	private String path;

	/**
	 * The Api Rights Name of the API's.
	 */
	@Column(name = "api_rights_name")
	private String apiRightsName;

}