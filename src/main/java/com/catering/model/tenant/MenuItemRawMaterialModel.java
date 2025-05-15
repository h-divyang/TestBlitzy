package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "menu_item_raw_material")
public class MenuItemRawMaterialModel extends AuditByIdModel {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_raw_material_id")
	private RawMaterialModel rawMaterial;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_menu_item_id")
	private MenuItemModel menuItem;

	@Column(name = "weight")
	private Double weight;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_measurement_id")
	private MeasurementModel measurement;

	/**
	 * Creates a new instance of {@link MenuItemRawMaterialModel} with the `weight` field initialized based on the provided query.
	 * If the query string is a valid number, it is parsed as a {@link Double} and assigned to the `weight` field. If not, the field is set to null.
	 *
	 * This method is useful for searching or filtering raw materials in a menu item, where the query string can be used to specify a weight.
	 *
	 * @param query The search query that will populate the `weight` field of the menu item raw material model.
	 * @return A new instance of {@link MenuItemRawMaterialModel} with the `weight` field set according to the query value.
	 */
	public static MenuItemRawMaterialModel ofSearchingModel(String query) {
		return MenuItemRawMaterialModel.builder()
				.weight(NumberUtils.isCreatable(query) ? Double.parseDouble(query) : null)
				.build();
	}

	/**
	 * Constructs a new instance of {@link MenuItemRawMaterialModel} using the provided {@link RawMaterialModel}, weight, and {@link MeasurementModel}.
	 * 
	 * @param rawMaterial The raw material model associated with this menu item raw material.
	 * @param weight The weight of the raw material in the menu item.
	 * @param measurement The measurement model associated with the weight.
	 */
	public MenuItemRawMaterialModel(RawMaterialModel rawMaterial, Double weight, MeasurementModel measurement) {
		this.rawMaterial = rawMaterial;
		this.weight = weight;
		this.measurement = measurement;
	}

}