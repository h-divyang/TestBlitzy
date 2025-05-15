package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "order_booking_report_notes")
public class NotesModel extends AuditByIdModel {

	@Column(name = "fk_customer_order_details_id")
	private Long bookOrderId;

	@Column(name = "notes_default_lang")
	private String notesDefaultLang;

	@Column(name = "notes_prefer_lang")
	private String notesPreferLang;

	@Column(name = "notes_supportive_lang")
	private String notesSupportiveLang;

}