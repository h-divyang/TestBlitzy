package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateWiseRawMaterialReportWithPriceDto {

	private String startDate;

	private String endDate;

	private Long[] supplierCategoryId;

	private Long[] supplierNameId;

	private Long[] rawMaterialCategoryId;

	private Long[] statusId;

	private Integer langType;

	private String langCode;

	private String reportName;

}