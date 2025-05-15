package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.ReportCompanyDetailRightsModel;

public interface ReportCompanyDetailRightsRepository extends JpaRepository<ReportCompanyDetailRightsModel, Long> {

	ReportCompanyDetailRightsModel findByReportMasterId(Long reportCategoryId);

}