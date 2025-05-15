package com.catering.model.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.catering.model.audit.AuditIdModelOnly;

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
@Table(name = "report_master")
public class ReportMasterModel extends AuditIdModelOnly {

	@Column(name = "report_name")
	private String reportName;

	@ManyToOne
	@JoinColumn(name = "fk_report_category_id")
	private ReportCategoryModel reportCategory;

}
