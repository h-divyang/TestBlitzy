package com.catering.model.tenant;

import java.time.LocalTime;
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
@Table(name = "labour_shift")
public class LabourShiftModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name = "time")
	private LocalTime time;

	@Column(name = "time", insertable = false, updatable = false)
	private String timeString;

	/**
	 * Creates and returns a new instance of {@link LabourShiftModel} with the provided query.
	 * The query is used to set the default, preferred, and supportive language names in the model.
	 *
	 * @param query The search query or string used to populate the name fields of the {@link LabourShiftModel}.
	 * @return A new instance of {@link LabourShiftModel} with the specified query set for name fields.
	 */
	public static LabourShiftModel ofSearchingModel(String query) {
		// Build the FunctionTypeModel and set the fields
		return LabourShiftModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.build();
	}

}