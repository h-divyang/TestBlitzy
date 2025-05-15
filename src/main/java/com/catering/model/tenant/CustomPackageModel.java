package com.catering.model.tenant;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;
import com.catering.util.ValidationUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Custom Package in the system.
 * <p>
 * This model corresponds to a custom package entity, including information like the package name in different languages, 
 * price, total items, and associations with menu items and categories.
 * </p>
 * 
 * @author Priyansh Patel
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "custom_package")
public class CustomPackageModel extends AuditByIdModel {

	/**
	 * Name of the package in the default language.
	 */
	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	/**
	 * Name of the package in the preferred language.
	 */
	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	/**
	 * Name of the package in the supportive language.
	 */
	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	/**
	 * Price of the custom package.
	 */
	@Column(name = "price")
	private Double price;

	/**
	 * Total number of items in the custom package.
	 */
	@Column(name = "total_items")
	private Long totalItems;

	/**
	 * List of menu item categories associated with the custom package.
	 */
	@OneToMany(mappedBy = "customPackage", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<PackageMenuItemCategoryModel> packageMenuItemCategoryList;

	/**
	 * List of menu items associated with the custom package.
	 */
	@OneToMany(mappedBy = "customPackage", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<PackageMenuItemModel> packageMenuItemsList;

	/**
	 * Creates a CustomPackageModel instance based on the provided search query. The
	 * model is initialized with the provided query values, including price and
	 * total items if the query is numeric.
	 *
	 * @param query The search query to set for the name and price fields.
	 * @return A new CustomPackageModel instance with the name fields and numeric fields set.
	 */
	public static CustomPackageModel ofSearchingModel(String query) {
		return CustomPackageModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.price(ValidationUtils.isNumber(query) ? Double.valueOf(query) : null)
			.totalItems(ValidationUtils.isNumber(query) ? Long.valueOf(query) : null)
			.build();
	}

}