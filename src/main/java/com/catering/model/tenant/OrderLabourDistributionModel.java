package com.catering.model.tenant;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "order_labour_distribution")
public class OrderLabourDistributionModel extends AuditByIdModel {

	@JoinColumn(name = "fk_order_function_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private OrderFunctionModel orderFunction;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_contact_id")
	private ContactModel contact;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_contact_category_id")
	private ContactCategoryModel contactCategory;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_labour_shift_id")
	private LabourShiftModel labourShift;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_godown_id")
	private GodownModel godown;

	@Column(name = "date")
	private LocalDateTime date;

	@Column(name = "labour_price")
	private Double labourPrice;

	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "note_default_lang")
	private String noteDefaultLang;

	@Column(name = "note_prefer_lang")
	private String notePreferLang;

	@Column(name = "note_supportive_lang")
	private String noteSupportiveLang;

}