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
@Table(name = "raw_material_category_type")
public class RawMaterialCategoryTypeModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	/**
	 * Creates a new instance of {@link RawMaterialCategoryTypeModel} with the name fields initialized based on the provided query.
	 * The name fields are set for each language (default, preferred, and supportive).
	 * 
	 * This method is useful for searching or filtering raw material category types by their name in different languages.
	 *
	 * @param query The search query that will populate the name fields (for each language) of the raw material category type model.
	 * @return A new instance of {@link RawMaterialCategoryTypeModel} with the name fields set according to the query value.
	 */
	public static RawMaterialCategoryTypeModel ofSearchingModel(String query) {
		return RawMaterialCategoryTypeModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.build();
	}

}