package com.catering.model.tenant;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.math.NumberUtils;

import com.catering.model.CustomRangeModel;
import com.catering.model.audit.AuditByIdModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "measurement")
public class MeasurementModel extends AuditByIdModel {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@Column(name = "symbol_default_lang")
	private String symbolDefaultLang;

	@Column(name = "symbol_prefer_lang")
	private String symbolPreferLang;

	@Column(name = "symbol_supportive_lang")
	private String symbolSupportiveLang;

	@Column(name = "is_base_unit")
	private Boolean isBaseUnit;

	@Column(name = "base_unit_equivalent")
	private Double baseUnitEquivalent;

	@OneToOne
	@JoinColumn(name = "base_unit_id", referencedColumnName = "id")
	private MeasurementModel baseUnitId;

	@Column(name = "decimal_limit_qty")
	private Integer decimalLimitQty;

	@Column(name = "adjust_type")
	private Integer adjustType;

	@Column(name = "step_wise_range")
	private Double stepWiseRange;

	@OneToMany(mappedBy = "measurement", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<CustomRangeModel> customRange;

	/**
	 * Creates a new instance of {@link MeasurementModel} with the following fields initialized based on the provided query:
	 * - Name fields (`nameDefaultLang`, `namePreferLang`, `nameSupportiveLang`) populated with the query string.
	 * - Symbol fields (`symbolDefaultLang`, `symbolPreferLang`, `symbolSupportiveLang`) populated with the query string.
	 * - The `isBaseUnit` field is set based on whether the query string is "yes" or "no", converting it to a Boolean.
	 * - The `baseUnitEquivalent` field is set to a numeric value if the query string can be parsed as a number.
	 *
	 * This method is useful for creating a search model where various attributes of the measurement need to be populated
	 * based on the query input.
	 *
	 * @param query The search query that will populate the name, symbol, and other fields of the measurement model.
	 * @return A new instance of {@link MeasurementModel} with the fields set according to the query value.
	 */
	public static MeasurementModel ofSearchingModel(String query) {
		Boolean isTrue = query.equals("yes") ? Boolean.TRUE : query.equals("no") ? Boolean.FALSE : null;
		return MeasurementModel.builder().
				nameDefaultLang(query)
				.namePreferLang(query)
				.nameSupportiveLang(query)
				.symbolDefaultLang(query)
				.symbolPreferLang(query)
				.symbolSupportiveLang(query)
				.isBaseUnit(isTrue)
				.baseUnitEquivalent(NumberUtils.isCreatable(query) ? Double.parseDouble(query) : null)
				.build();
	}

}