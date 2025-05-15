package com.catering.dto.tenant.request;

import com.catering.dto.audit.OnlyIdDto;
import com.catering.model.tenant.ReportCategoryModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportMasterDto extends OnlyIdDto {

	private ReportCategoryModel reportCategory;

	private String reportName;

}
