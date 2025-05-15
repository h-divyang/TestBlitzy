package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabourReportParams {

	private Integer langType;

	private String langCode;

	private String currentDate;

	private Long[] supplierCategoryId;

	private Long[] supplierId;

	private Long[] functionId;

}