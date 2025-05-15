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
@Table(name = "catering_preferences")
public class CateringPreferencesModel extends AuditIdModel {

	@Column(name = "name")
	private String name;

	@Column(name = "time_zone")
	private String timeZone;

	@Column(name = "developed_by")
	private String developedBy;

}