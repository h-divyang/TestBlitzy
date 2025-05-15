package com.catering.model.tenant;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "journal_voucher")
public class JournalVoucherModel extends AuditByIdModel {

	@Column(name = "voucher_date")
	private LocalDate voucherDate;

	@OneToMany(mappedBy = "journalVoucher", cascade = CascadeType.ALL, orphanRemoval = true)
	List<JournalVoucherDetailsModel> journalVoucherDetails;

}