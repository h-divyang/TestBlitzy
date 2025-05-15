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

/**
 * This class represents a TaxMasterModel in the tenant package.
 * It extends the AuditByIdModel class and defines the properties of a tax master.
 *
 * @author Neel Bhanderi
 * @since March 2024
 *
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tax_master")
public class TaxMasterModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name = "cgst")
	private Double cgst;

	@Column(name = "sgst")
	private Double sgst;

	@Column(name = "igst")
	private Double igst;

	@Column(name = "cgst", insertable = false, updatable = false)
	private String cgstString;

	@Column(name = "sgst", insertable = false, updatable = false)
	private String sgstString;

	@Column(name = "igst", insertable = false, updatable = false)
	private String igstString;

	/**
	 * Creates and returns a new instance of TaxMasterModel for searching with the specified query.
	 *
	 * @param query The search query used to initialize the model's name fields.
	 * @return A new instance of TaxMasterModel with name fields set to the specified query.
	 */
	public static TaxMasterModel ofSearchingModel(String query) {
		return TaxMasterModel.builder()
			.nameDefaultLang(query)
			.namePreferLang(query)
			.nameSupportiveLang(query)
			.cgstString(query)
			.sgstString(query)
			.igstString(query)
			.build();
	}

}