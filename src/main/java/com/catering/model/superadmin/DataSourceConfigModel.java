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
@Table(name = "data_source_config")
public class DataSourceConfigModel extends AuditIdModel {

	@Column(name = "tenant")
	private String tenant;

}