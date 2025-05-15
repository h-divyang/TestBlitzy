package com.catering.model.tenant;

import java.time.LocalDate;

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
@Table(name = "bank_payment_receipt_details")
public class BankPaymentReceiptDetailsModel extends AuditIdModelOnly {

	@JsonBackReference
	@JoinColumn(name = "fk_bank_payment_receipt_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private BankPaymentReceiptModel bankPaymentReceipt;

	@Column(name = "voucher_type")
	private int voucherType;

	@Column(name = "voucher_number")
	private Long voucherNumber;

	@JoinColumn(name = "fk_contact_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ContactModel contact;

	@Column(name = "payment_mode")
	private Integer paymentMode;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "cheque_transaction_number")
	private String chequeTransactionNumber;

	@Column(name = "cheque_transaction_date")
	private LocalDate chequeTransactionDate;

	@Column(name = "remark")
	private String remark;

}