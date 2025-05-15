package com.catering.model.tenant;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The MainMenuModel class represents a main menu item in the catering system.
 * It extends the IdModel class, which provides an ID for the main menu item.
 *
 * <p>A main menu item contains the title, sequence number, menu header, path, and icon.</p>
 *
 * <p>This class is annotated with the @Entity annotation to indicate that it is a persistent entity
 * in the database. The @Table annotation specifies the name of the corresponding database table.</p>
 *
 * <p>Use the getter and setter methods to access and modify the attributes of the main menu item.</p>
 *
 * <p>The class also provides a builder pattern using the @Builder annotation, allowing for
 * easy and flexible creation of MainMenuModel instances.</p>
 *
 * <p>The main menu item is associated with a menu header using a many-to-one relationship.</p>
 *
 * <p>The menu header is referenced by the fk_menu_header_id foreign key column.</p>
 *
 * @author Krushali Talaviya
 * @since July 2023
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "main_menu")
public class MainMenuModel extends CommonMenuModel {

	/**
	 * The menu header associated with the main menu item.
	 */
	@ManyToOne
	@JoinColumn(name = "fk_menu_header_id")
	private MenuHeaderModel menuHeaderModel;

	/**
	 * The path of the main menu item.
	 */
	@Column(name = "path")
	private String path;

	/**
	 * The icon of the main menu item.
	 */
	@Column(name = "icon")
	private String icon;

	/**
	 * Represents a one-to-many relationship between the MainMenuModel and SubMenuModel entities.
	 * The relationship is mapped by the "mainMenu" field in the SubMenuModel entity.
	 * This annotation specifies the cascading behavior, orphan removal, and fetching strategy for the relationship.
	 *
	 * - The cascade attribute indicates that all operations (e.g., persist, merge, remove) applied to the MainMenuModel
	 *   entity should be cascaded to the associated SubMenuModel entities.
	 *
	 * - The orphanRemoval attribute specifies that when a SubMenuModel entity is no longer referenced by the MainMenuModel entity,
	 *   it should be removed from the database.
	 *
	 * - The fetch attribute indicates that the associated SubMenuModel entities should be eagerly fetched
	 *   from the database when the MainMenuModel entity is retrieved.
	 */
	@OneToMany(mappedBy = "mainMenu", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<SubMenuModel> subMenu;

	/**
	 * The Api Rights Name of the API's.
	 */
	@Column(name = "api_rights_name")
	private String apiRightsName;

}