package com.catering.model.tenant;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_menu_preparation")
public class OrderMenuPreparationModel extends AuditByIdModel {

	@JoinColumn(name = "fk_order_function_id")
	@OneToOne(fetch = FetchType.LAZY)
	private OrderFunctionModel orderFunction;

	@Column(name = "menu_type")
	private Long menuTypeId;

	@Column(name = "rate")
	private Float rate;

	@OneToMany(mappedBy = "menuPreparation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderMenuPreparationMenuItemModel> menuPreparationMenuItem;

	@OneToMany(mappedBy = "menuPreparationId", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderMenuPreparationMenuItemCategoryModel> menuPreparationMenuItemCategory;

	@OneToMany(mappedBy = "menuPreparation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderNoItemsModel> noItems;

	@JoinColumn(name = "fk_custom_package_id")
	@ManyToOne
	private CustomPackageModel customPackage;

}