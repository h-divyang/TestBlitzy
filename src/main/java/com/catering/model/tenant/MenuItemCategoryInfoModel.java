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
public class MenuItemCategoryInfoModel extends AuditIdModelOnly {

	@Column(name = "fk_menu_preparation_id")
	private Long menuPreparationId;

	@Column(name = "fk_menu_item_category_id")
	private Long categoryId;

	@Column(name = "note_default_lang")
	private String noteDefaultLang;

	@Column(name = "note_prefer_lang")
	private String notePreferLang;

	@Column(name = "note_supportive_lang")
	private String noteSupportiveLang;

}