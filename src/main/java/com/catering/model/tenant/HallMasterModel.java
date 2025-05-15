package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.math.NumberUtils;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Hall Master Model class
 * 
 * @author Rohan Parmar
 * @since February 2024
 * @see AuditByIdModel
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hall_master")
public class HallMasterModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name = "capacity")
	private Integer capacity;

	/**
	 * Creates a new instance of {@link HallMasterModel} with all name fields initialized
	 * to the provided query string.
	 *
	 * @param query The search query to initialize the name fields and potentially the capacity.
	 * @return A new instance of {@link HallMasterModel} with fields populated by the query string.
	 */
	public static HallMasterModel ofSearchingModel(String query) {
		return HallMasterModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.capacity(NumberUtils.isDigits(query) ? Integer.parseInt(query) : null)
			.build();
	}

}