package com.catering.model.tenant;

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
@Table(name = "input_transfer_to_hall_raw_material")
public class InputTransferToHallRawMaterialModel extends AuditIdModelOnly {

	@JoinColumn(name = "fk_input_transfer_to_hall_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private InputTransferToHallModel inputTransferToHallRawMaterial;

	@JoinColumn(name = "fk_raw_material_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private RawMaterialModel rawMaterial;

	@Column(name = "weight")
	private Double weight;

	@JoinColumn(name = "fk_measurement_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MeasurementModel measurement;

}