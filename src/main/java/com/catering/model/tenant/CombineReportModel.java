package com.catering.model.tenant;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "combined_report")
public class CombineReportModel extends AuditByIdModel {

	@OneToOne
	@JoinColumn(name = "fk_report_master_id")
	private ReportMasterModel reportMaster;

}