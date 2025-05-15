package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.math.NumberUtils;

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
@Table(name = "contact_category")
public class ContactCategoryModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name ="is_non_updatable")
	private Integer isNonUpdatable;

	@Column(name ="priority")
	private Integer priority;

	@JoinColumn(name = "fk_contact_category_type_id")
	@ManyToOne(targetEntity = ContactCategoryTypeModel.class, fetch = FetchType.LAZY)
	private ContactCategoryTypeModel contactCategoryType;

	@Column(name = "display_labour_record")
	private Boolean displayLabourRecord;

	/**
	 * Creates a new instance of {@link ContactCategoryModel} with the provided query value.
	 * Populates the `nameDefaultLang`, `namePreferLang`, and `nameSupportiveLang` fields with the given query.
	 *
	 * @param query The search query used to populate the fields of {@link ContactCategoryModel}.
	 * @return A new instance of {@link ContactCategoryModel} populated with the query.
	 */
	public static ContactCategoryModel ofSearchingModel(String query) {
		return ContactCategoryModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.priority(NumberUtils.isDigits(query) ? Integer.parseInt(query) : null)
			.build();
	}

}