package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a menu item in a custom package.
 * <p>
 * This model maps the relationship between a custom package and a menu item. 
 * It includes information about the menu item sequence and category sequence.
 * </p>
 * 
 * @author Priyansh Patel
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "package_menu_item")
public class PackageMenuItemModel extends AuditByIdModel {

	/**
	 * The ID of the associated menu item.
	 */
	@Column(name = "fk_menu_item_id")
	private long menuItemId;

	/**
	 * The ID of the associated menu item category.
	 */
	@Column(name = "fk_menu_item_category_id")
	private long menuItemCategoryId;

	/**
	 * The sequence/order of the menu item in the custom package.
	 */
	@Column(name = "menu_item_sequence")
	private Long menuItemSequence;

	/**
	 * The sequence/order of the menu item category in the custom package.
	 */
	@Column(name = "menu_item_category_sequence")
	private Long menuItemCategorySequence;

	/**
	 * The custom package this menu item belongs to.
	 */
	@ManyToOne
	@JoinColumn(name = "fk_custom_package_id")
	private CustomPackageModel customPackage;

	/*.........................For Relation and FK Only.........................*/

	/**
	 * The associated menu item.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_menu_item_id", insertable = false, updatable = false)
	private MenuItemModel menuItem;

	/**
	 * The associated menu item category.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_menu_item_category_id", insertable = false, updatable = false)
	private MenuItemCategoryModel menuItemCategory;

}