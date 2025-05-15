package com.catering.model.tenant;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_quotation")
public class OrderQuotationModel extends AuditByIdModel {

	@Column(name = "fk_customer_order_details_id")
	private Long orderId;

	@Column(name = "fk_tax_master_id")
	private Long taxMasterId;

	@Column(name = "discount")
	private Double discount;

	@Column(name ="grand_total")
	private String grandTotal;

	@Column(name = "advance_payment")
	private Double advancePayment;

	@Column(name = "round_off")
	private Double roundOff;

	@Column(name = "remark")
	private String remark;

	@Column(name = "is_rough_estimation")
	private Boolean isRoughEstimation;

	@OneToMany(mappedBy = "orderQuotationId", cascade = CascadeType.ALL, orphanRemoval = true)
	List<OrderQuotationFunctionModel> functions;

}