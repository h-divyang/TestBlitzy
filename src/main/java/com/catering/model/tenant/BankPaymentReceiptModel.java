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
@Table(name = "bank_payment_receipt")
public class BankPaymentReceiptModel extends AuditByIdModel {

	@Column(name = "transaction_date")
	private LocalDate transactionDate;

	@Column(name = "transaction_date", insertable = false, updatable = false)
	private String transactionDateString;

	@Column(name = "transaction_type")
	private boolean transactionType;

	@JoinColumn(name = "fk_contact_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ContactModel contact;

	@JsonManagedReference
	@OneToMany(mappedBy = "bankPaymentReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
	List<BankPaymentReceiptDetailsModel> bankPaymentReceiptDetailsList;

}