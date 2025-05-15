package com.catering.model.tenant;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

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
@Table(name = "raw_material_allocation")
public class RawMaterialAllocationModel extends AuditIdModelOnly {

	@Column(name = "fk_menu_preparation_menu_item_id")
	private Long menuPreparationMenuItemId;

	@JoinColumn(name = "fk_menu_item_raw_material_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MenuItemRawMaterialModel menuItemRawMaterialId;

	@Column(name = "actual_qty")
	private Double actualQty;

	@JoinColumn(name = "fk_actual_measurement_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MeasurementModel actualMeasurementId;

	@Column(name = "final_qty")
	private Double finalQty;

	@JoinColumn(name = "fk_final_measurement_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MeasurementModel finalMeasurementId;

	@Column(name = "order_time")
	private LocalDateTime orderTime;

	@JoinColumn(name = "fk_contact_agency_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ContactModel contactAgencyId;

	@JoinColumn(name = "fk_raw_material_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private RawMaterialModel rawMaterialId;

	@JoinColumn(name = "fk_godown_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private GodownModel godown;

}