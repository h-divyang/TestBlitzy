package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_menu_preparation_menu_item_category")
public class MenuItemCategoryRupeesModel extends AuditIdModelOnly {

	@Column(name = "fk_menu_preparation_id")
	private Long menuPreparationId;

	@Column(name = "fk_menu_item_category_id")
	private Long categoryId;

	@Column(name = "rupees")
	private Double rupees;

}