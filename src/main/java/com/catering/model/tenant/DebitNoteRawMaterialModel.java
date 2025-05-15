package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "debit_note_raw_material")
public class DebitNoteRawMaterialModel extends AuditIdModelOnly {

	@ManyToOne
	@JoinColumn(name = "fk_debit_note_id")
	private DebitNoteModel debitNote;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_raw_material_id")
	private RawMaterialModel rawMaterial;

	@Column(name = "weight")
	private Double weight;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_measurement_id")
	private MeasurementModel measurement;

	@Column(name = "price")
	private Double price;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_tax_master_id")
	private TaxMasterModel taxMaster;

	@Column(name = "total_amount")
	private Double totalAmount;

}