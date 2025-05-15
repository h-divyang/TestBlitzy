package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catering.dto.tenant.request.GetMenuPreparationItemCategoryModel;
import com.catering.model.audit.AuditIdModelOnly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_menu_preparation_menu_item")
public class GetMenuPreparationMenuItemModel extends AuditIdModelOnly {

	@Column(name = "fk_menu_preparation_id")
	private Long menuPreparation;

	@ManyToOne
	@JoinColumn(name = "fk_menu_item_id")
	private GetMenuPreparationItemModel menuItem;

	@ManyToOne
	@JoinColumn(name = "fk_menu_item_category_id")
	private GetMenuPreparationItemCategoryModel menuItemCategory;

	@Column(name = "note_default_lang")
	private String noteDefaultLang;

	@Column(name = "note_prefer_lang")
	private String notePreferLang;

	@Column(name = "note_supportive_lang")
	private String noteSupportiveLang;

	@Column(name = "menu_item_name_default_lang")
	private String menuItemNameDefaultLang;

	@Column(name = "menu_item_name_prefer_lang")
	private String menuItemNamePreferLang;

	@Column(name = "menu_item_name_supportive_lang")
	private String menuItemNameSupportiveLang;

	@Column(name = "rupees")
	private Double rupees;

	@Column(name = "menu_item_category_sequence")
	private Integer menuItemCategorySequence;

	@Column(name = "menu_item_sequence")
	private Integer menuItemSequence;

	@Column(name = "fk_godown_id")
	private Long godown;

}