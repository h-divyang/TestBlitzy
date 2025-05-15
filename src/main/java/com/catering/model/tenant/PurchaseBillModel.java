package com.catering.model.tenant;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "purchase_bill")
public class PurchaseBillModel extends AuditByIdModel {

	@Column(name = "bill_date")
	private LocalDate billDate;

	@Column(name = "bill_number")
	private String billNumber;

	@Column(name = "fk_purchase_order_id")
	private Long purchaseOrderId;

	@JoinColumn(name = "fk_contact_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ContactModel contactSupplier;

	@OneToMany(mappedBy = "purchaseBill", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private List<PurchaseBillRawMaterialModel> purchaseBillRawMaterialList;

	@Column(name = "remark")
	private String remark;

	@Column(name = "discount")
	private Double discount;

	@Column(name = "grand_total")
	private Double grandTotal;

	@Column(name = "extra_expense")
	private Double extraExpense;

	@Column(name = "round_off")
	private Double roundOff;

	@Column(name = "total")
	private Double total;

}