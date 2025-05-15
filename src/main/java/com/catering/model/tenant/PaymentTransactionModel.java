package com.catering.model.tenant;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_transaction")
public class PaymentTransactionModel extends AuditIdModelOnly {

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@OneToOne
	@JoinColumn(name = "created_by", updatable = false)
	private CompanyUserModelForAudit createdBy;

	@Column(name = "payment_date")
	private LocalDate paymentDate;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "payment_method")
	private Long paymentMethod;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "cheque_number")
	private String chequeNumber;

	@Column(name = "cheque_date")
	private LocalDate chequeDate;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "transaction_date")
	private LocalDate transactionDate;

}