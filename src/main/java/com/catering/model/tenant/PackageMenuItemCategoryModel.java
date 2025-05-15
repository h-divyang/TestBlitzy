package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a menu item category in a custom package.
 * <p>
 * This model maps the relationship between a custom package and a menu item category. 
 * It includes information about the number of items and the associated menu item category.
 * </p>
 * 
 * @author Priyansh Patel
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "package_menu_item_category")
public class PackageMenuItemCategoryModel extends AuditIdModelOnly {

	/**
	 * The ID of the associated menu item category.
	 */
	@Column(name = "fk_menu_item_category_id")
	private long menuItemCategoryId;

	/**
	 * The number of items in this category.
	 */
	@Column(name = "no_of_items")
	private Long numberOfItems;

	/**
	 * The custom package this menu item category belongs to.
	 */
	@ManyToOne
	@JoinColumn(name = "fk_custom_package_id")
	private CustomPackageModel customPackage;

	/*.........................For Relation and FK Only.........................*/

	/**
	 * The menu item category associated with this record.
	 */
	@ManyToOne
	@JoinColumn(name = "fk_menu_item_category_id", insertable = false, updatable = false)
	private MenuItemCategoryModel menuItemCategory;

}