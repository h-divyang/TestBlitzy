package com.catering.model.tenant;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "cash_payment_receipt")
public class CashPaymentReceiptModel extends AuditByIdModel {

	@Column(name = "transaction_date")
	private LocalDate transactionDate;

	@Column(name = "transaction_date", insertable = false, updatable = false)
	private String transactionDateString;

	@Column(name = "transaction_type")
	private boolean transactionType;

	@JsonManagedReference
	@OneToMany(mappedBy = "cashPaymentReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
	List<CashPaymentReceiptDetailsModel> cashPaymentReceiptDetailsList;

}