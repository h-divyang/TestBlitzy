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
@Table(name = "debit_note")
public class DebitNoteModel extends AuditByIdModel {

	@Column(name = "fk_purchase_bill_id")
	private Long purchaseBillId;

	@JoinColumn(name = "fk_contact_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ContactModel contactSupplier;

	@Column(name = "bill_date")
	private LocalDate billDate;

	@Column(name = "bill_number")
	private String billNumber;

	@OneToMany(mappedBy = "debitNote", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private List<DebitNoteRawMaterialModel> debitNoteRawMaterialList;

}