package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DateWiseRawMaterialReportDto {

	private String supplierCategory;

	private String supplierName;
	
	private String mobileNumber;

	private LocalDateTime orderDate;

	private String timeInWord;

	private String venue;

	private String rawMaterialName;

	private Double quantity;

	private String unit;

	private Long rawMaterialCategoryId;

	private Long contactAgencyId;

	private Integer decimalLimitQty;

}