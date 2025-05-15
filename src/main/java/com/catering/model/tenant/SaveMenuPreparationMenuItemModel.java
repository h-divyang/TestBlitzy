package com.catering.model.tenant;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.catering.interfaces.Audit;
import com.catering.model.audit.AuditIdModelOnly;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_menu_preparation_menu_item")
public class SaveMenuPreparationMenuItemModel extends AuditIdModelOnly implements Audit {

	@ManyToOne
	@JoinColumn(name = "fk_menu_preparation_id")
	private SaveMenuPreparationModel menuPreparation;

	@ManyToOne
	@JoinColumn(name = "fk_menu_item_id")
	private SaveMenuPreparationMenuItemMenuItemModel menuItem;

	@Column(name = "fk_menu_item_category_id")
	private Long menuItemCategoryId;

	@Column(name = "order_type", updatable = false)
	private Integer orderType;

	@Column(name = "order_date", updatable = false)
	private LocalDateTime orderDate;

	@Column(name = "person", updatable = false)
	private Integer person;

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

	@OneToMany(mappedBy = "menuPreparationMenuItem", cascade = CascadeType.ALL)
	private List<SaveMenuAllocationTypeModel> allocationType;

	@Column(name = "created_by", updatable = false)
	private Long createdById;

	@Column(name = "updated_by")
	private Long updatedById;

	@Column(name = "is_active", updatable = false)
	private Boolean isActive = true;

	@Column(name = "edit_count")
	private Integer editCount = 0;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}