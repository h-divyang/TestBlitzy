package com.catering.model.superadmin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditByIdModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "language")
public class LanguageModel extends AuditByIdModel {

	@Column(name = "name")
	private String name;

	@Column(name = "code")
	private String code;

	@Column(name = "native_name")
	private String nativeName;

}