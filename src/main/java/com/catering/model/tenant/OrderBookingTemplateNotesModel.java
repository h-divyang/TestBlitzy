package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_booking_report_template_notes")
public class OrderBookingTemplateNotesModel extends AuditByIdModel {

	@Column(name = "notes_default_lang")
	private String notesDefaultLang;

	@Column(name = "notes_prefer_lang")
	private String notesPreferLang;

	@Column(name = "notes_supportive_lang")
	private String notesSupportiveLang;

}