package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "menu_item_sub_category")
public class MenuItemSubCategoryModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	/**
	 * Creates a new instance of {@link MenuItemSubCategoryModel} with the name fields initialized based on the provided query.
	 * This method sets the default, preferred, and supportive language names of the subcategory model to the query string.
	 *
	 * This method is useful for searching or filtering menu item subcategories by their name in different languages.
	 *
	 * @param query The search query that will populate the name fields of the menu item subcategory model in different languages.
	 * @return A new instance of {@link MenuItemSubCategoryModel} with the name fields set according to the query value.
	 */
	public static MenuItemSubCategoryModel ofSearchingModel(String query) {
		return MenuItemSubCategoryModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.build();
	}

}