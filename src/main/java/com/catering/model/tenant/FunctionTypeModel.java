package com.catering.model.tenant;

import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.catering.model.audit.AuditByIdModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
	
/**
 * This class represents a FunctionTypeModel in the tenant package.
 * It extends the AuditByIdModel class and defines the properties of a function master.
 *
 * @author Krushali Talaviya
 * @since June 2023
 *
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "function_type")
public class FunctionTypeModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name = "time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime time;

	@Column(name = "time", insertable = false, updatable = false)
	private String timeString;

	@Column(name = "end_time")
	private LocalTime endTime;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_shift_id")
	private LabourShiftModel labourShift;

	/**
	 * Creates and returns a new instance of {@link FunctionTypeModel} with the provided query.
	 * The query is used to set the default, preferred, and supportive language names in the model.
	 *
	 * @param query The search query or string used to populate the name fields of the {@link FunctionTypeModel}.
	 * @return A new instance of {@link FunctionTypeModel} with the specified query set for name fields.
	 */
	public static FunctionTypeModel ofSearchingModel(String query) {
		// Build the FunctionTypeModel and set the fields
		return FunctionTypeModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.build();
	}

}