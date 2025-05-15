package com.catering.model.tenant;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name = "order_menu_preparation")
public class GetMenuPreparationModel extends AuditIdModelOnly {

	@JoinColumn(name = "fk_custom_package_id")
	@ManyToOne
	private GetMenuPreparationCustomPackageModel customPackage;

	@Column(name = "menu_type")
	private Long menuTypeId;

	@JoinColumn(name = "fk_order_function_id")
	@OneToOne(fetch = FetchType.LAZY)
	private GetMenuPreparationOrderFunctionModel orderFunction;

	@OneToMany(mappedBy = "menuPreparation")
	private List<GetMenuPreparationNoItemsModel> noItems;

	@Column(name = "rate")
	private Float rate;

	@OneToMany(mappedBy = "menuPreparationId")
	private List<GetMenuPreparationMenuItemCategoryModel> menuPreparationMenuItemCategory;

	@OneToMany(mappedBy = "menuPreparation")
	private List<GetMenuPreparationMenuItemModel> menuPreparationMenuItem;

}