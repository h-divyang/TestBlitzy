package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.math.NumberUtils;

import com.catering.interfaces.Priority;
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
@Table(name = "raw_material")
public class RawMaterialModel extends AuditByIdModel implements Priority {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_raw_material_category_id")
	private RawMaterialCategoryModel rawMaterialCategory;

	@Column(name = "weight_per_100_pax")
	private Double weightPer100Pax;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_measurement_id")
	private MeasurementModel measurement;

	@Column(name = "supplier_rate")
	private Double supplierRate;

	@Column(name = "priority")
	private Integer priority;

	@Column(name = "is_general_fix_raw_material")
	private Boolean isGeneralFixRawMaterial;

	@Column(name = "opening_balance")
	private Double openingBalance;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_opb_measurement_id")
	private MeasurementModel opbMeasurement;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_tax_master_id")
	private TaxMasterModel tax;

	@Column(name = "hsn_code")
	private String hsnCode;

	/**
	 * Creates a new instance of {@link RawMaterialModel} with the name fields initialized based on the provided query.
	 * The name fields are set for each language (default, preferred, and supportive). Additionally, if the query represents
	 * a valid integer, the priority field will be set.
	 *
	 * This method is useful for searching or filtering raw materials by their name and priority.
	 *
	 * @param query The search query that will populate the name fields (for each language) and priority of the raw material model.
	 * @return A new instance of {@link RawMaterialModel} with the name fields and priority set according to the query value.
	 */
	public static RawMaterialModel ofSearchingModel(String query) {
		return RawMaterialModel.builder()
				.nameDefaultLang(query)
				.namePreferLang(query)
				.nameSupportiveLang(query)
				.priority(NumberUtils.isDigits(query) ? Integer.parseInt(query) : null)
				.build();
	}

}