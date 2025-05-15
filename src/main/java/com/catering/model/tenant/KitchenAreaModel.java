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
@Table(name = "kitchen_area")
public class KitchenAreaModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	/**
	 * Creates a new instance of {@link KitchenAreaModel} with the name fields
	 * (default, preferred, and supportive language) initialized to the provided query string.
	 * This method is useful for creating search models where the name fields need to be
	 * populated with the same value across different languages.
	 *
	 * @param query The search query that will populate the name fields of the kitchen area.
	 * @return A new instance of {@link KitchenAreaModel} with the name fields set to the query value.
	 */
	public static KitchenAreaModel ofSearchingModel(String query) {
		return KitchenAreaModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.build();
	}

}