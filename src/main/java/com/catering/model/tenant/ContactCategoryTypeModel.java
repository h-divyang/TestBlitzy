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
@Table(name = "contact_category_type")
public class ContactCategoryTypeModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	/**
	 * Creates a new instance of {@link ContactCategoryTypeModel} with the provided query value.
	 * Assigns the query value to the `nameDefaultLang`, `namePreferLang`, and `nameSupportiveLang` fields.
	 *
	 * @param query The search query used to populate the fields of {@link ContactCategoryTypeModel}.
	 * @return A new instance of {@link ContactCategoryTypeModel} populated with the query.
	 */
	public static ContactCategoryTypeModel ofSearchingModel(String query) {
		return ContactCategoryTypeModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.build();
	}

}