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
@Table(name = "godown")
public class GodownModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name = "address_default_lang")
	private String addressDefaultLang;

	@Column(name = "address_prefer_lang")
	private String addressPreferLang;

	@Column(name = "address_supportive_lang")
	private String addressSupportiveLang;

	/**
	 * Creates a new instance of {@link GodownModel} with all name and address fields
	 * initialized to the provided query string. This is useful for searching godown records
	 * where multiple fields may contain similar or identical search terms.
	 *
	 * @param query The search query to initialize all name and address fields.
	 * @return A new instance of {@link GodownModel} with fields populated by the query string.
	 */
	public static GodownModel ofSearchingModel(String query) {
		return GodownModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.addressDefaultLang(query)
			.addressPreferLang(query)
			.addressSupportiveLang(query)
			.build();
	}

}