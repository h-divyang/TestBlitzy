package com.catering.model.tenant;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
public class GetMenuAllocationOrderMenuPreparationMenuItemModel extends AuditIdModelOnly {

	@Column(name = "fk_menu_preparation_id")
	private Long menuPreparation;

	@JoinColumn(name = "fk_menu_item_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private GetMenuAllocationMenuItemModel menuItem;

	@JoinColumn(name = "fk_menu_item_category_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private GetMenuAllocationItemCategoryModel menuItemCategory;

	@Column(name = "order_type")
	private Integer orderType;

	@Column(name = "order_date")
	private LocalDateTime orderDate;

	@Column(name = "person")
	private Integer person;

	@Column(name = "fk_godown_id")
	private Long godown;

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

	@Transient
	private Double calculatedMenuItemAndRawMaterial;

	@Column(name = "menu_item_category_sequence")
	private Integer menuItemCategorySequence;

	@Column(name = "menu_item_sequence")
	private Integer menuItemSequence;

	@OneToMany(mappedBy = "menuPreparationMenuItem")
	private List<GetMenuAllocationTypeModel> allocationType;

}