package com.catering.model.superadmin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "contact_category_type")
public class ContactCategoryTypeMasterModel extends AuditIdModel {

	@Column(name = "en")
	private String en;

	@Column(name = "hi")
	private String hi;

	@Column(name = "gu")
	private String gu;

}