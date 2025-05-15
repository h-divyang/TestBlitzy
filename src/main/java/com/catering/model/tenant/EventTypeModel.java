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
@Table(name = "event_type")
public class EventTypeModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	/**
	 * Creates a new instance of {@link EventTypeModel} with its name fields populated
	 * using the given query. All three language-specific name fields are set to the same query value.
	 *
	 * @param query The input string used to populate the name fields of {@link EventTypeModel}.
	 * @return A new instance of {@link EventTypeModel} populated with the query.
	 */
	public static EventTypeModel ofSearchingModel(String query) {
		return EventTypeModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.build();
	}

}