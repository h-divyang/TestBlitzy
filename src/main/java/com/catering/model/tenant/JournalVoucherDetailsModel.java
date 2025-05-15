package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "journal_voucher_details")
public class JournalVoucherDetailsModel extends AuditIdModelOnly {

	@JoinColumn(name = "fk_journal_voucher_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private JournalVoucherModel journalVoucher;

	@JoinColumn(name = "fk_contact_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ContactModel contact;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "transaction_type", columnDefinition = "TINYINT", length = 1)
	private boolean transactionType;

	@Column(name = "remark")
	private String remark;

}