package com.catering.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catering.model.tenant.ReportCategoryModel;

public interface ReportCategoryRepository extends JpaRepository<ReportCategoryModel, Long> {
}
