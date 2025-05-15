package com.catering.repository.tenant;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.ReportMasterModel;

public interface ReportMasterRepository extends JpaRepository<ReportMasterModel, Long> {

	public List<ReportMasterModel> findByReportCategoryId(Long reportCategoryId);

	public ReportMasterModel findByReportName(String reportName);

	public List<ReportMasterModel> findByReportCategoryIdBetween(Long startCategoryId, Long endCategoryId);

}