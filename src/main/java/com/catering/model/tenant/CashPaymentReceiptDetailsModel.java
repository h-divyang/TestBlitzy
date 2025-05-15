package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cash_payment_receipt_details")
public class CashPaymentReceiptDetailsModel extends AuditIdModelOnly {

	@JsonBackReference
	@JoinColumn(name = "fk_cash_payment_receipt_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private CashPaymentReceiptModel cashPaymentReceipt;

	@Column(name = "voucher_type")
	private int voucherType;

	@Column(name = "voucher_number")
	private Long voucherNumber;

	@JoinColumn(name = "fk_contact_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ContactModel contact;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "remark")
	private String remark;

}