package com.catering.model.tenant;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.catering.interfaces.Audit;
import com.catering.model.audit.AuditIdModelOnly;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_menu_preparation")
public class SaveMenuPreparationModel extends AuditIdModelOnly implements Audit {

	@Column(name = "fk_custom_package_id")
	private Long customPackageId;

	@Column(name = "menu_type")
	private Long menuTypeId;

	@Column(name = "rate")
	private Float rate;

	@OneToOne
	@JoinColumn(name = "fk_order_function_id")
	private SaveMenuPreparationOrderFunctionModel orderFunction;

	@OneToMany(mappedBy = "menuPreparation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SaveMenuPreparationOrderNoItemsModel> noItems;

	@OneToMany(mappedBy = "menuPreparation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SaveMenuPreparationMenuItemCategoryModel> menuPreparationMenuItemCategory;

	@OneToMany(mappedBy = "menuPreparation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SaveMenuPreparationMenuItemModel> menuPreparationMenuItem;

	@Column(name = "created_by", updatable = false)
	private Long createdById;

	@Column(name = "updated_by")
	private Long updatedById;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "edit_count")
	private Integer editCount = 0;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

}