package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "crockery_report_notes")
public class CrockeryNotesModel extends AuditByIdModel {

	@Column(name = "note_default_lang")
	private String nameDefaultLang;

	@Column(name = "note_prefer_lang")
	private String namePreferLang;

	@Column(name = "note_supportive_lang")
	private String nameSupportiveLang;

}