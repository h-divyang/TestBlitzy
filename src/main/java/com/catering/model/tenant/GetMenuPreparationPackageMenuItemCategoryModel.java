package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "package_menu_item_category")
public class GetMenuPreparationPackageMenuItemCategoryModel extends AuditIdModelOnly {

	@Column(name = "fk_custom_package_id")
	private Long pkg;

	@Column(name = "fk_menu_item_category_id")
	private Long menuItemCategory;

	@Column(name = "no_of_items")
	private Long numberOfItems;

}