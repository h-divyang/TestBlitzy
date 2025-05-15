package com.catering.model.tenant;

import java.time.LocalDate;

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

/**
 * Represents the entity for purchase order raw materials.
 * 
 * @since 2024-05-31
 * @author Krushali Talaviya
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase_order_raw_material")
public class PurchaseOrderRawMaterialModel extends AuditIdModelOnly {

	@ManyToOne
	@JoinColumn(name = "fk_purchase_order_id")
	private PurchaseOrderModel purchaseOrder;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_raw_material_id")
	private RawMaterialModel rawMaterial;

	@Column(name = "hsn_code")
	private String hsnCode;

	@Column(name = "weight")
	private Double weight;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_measurement_id")
	private MeasurementModel measurement;

	@Column(name = "price")
	private Double price;

	@Column(name = "delivery_date")
	private LocalDate deliveryDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_tax_master_id")
	private TaxMasterModel taxMaster;

	@Column(name = "total_amount")
	private Double totalAmount;

}