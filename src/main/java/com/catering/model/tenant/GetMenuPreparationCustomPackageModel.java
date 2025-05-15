package com.catering.model.tenant;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
@Table(name = "custom_package")
public class GetMenuPreparationCustomPackageModel extends AuditIdModelOnly {

	/**
	 * The name of the package in the default language.
	 */
	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	/**
	 * The name of the package in the preferred language.
	 */
	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	/**
	 * The name of the package in the supportive language.
	 */
	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	/**
	 * The price of the package.
	 */
	@Column(name = "price")
	private Double price;

	@OneToMany(mappedBy = "pkg")
	private List<GetMenuPreparationPackageMenuItemCategoryModel> packageMenuItemCategoryList;

}