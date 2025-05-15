package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DateWiseStockSummaryReportDto {

	private Long rawMaterialCategoryId;

	private String rawMaterialCategoryName;

	private String rawMaterialName;

	private Double stock;

	private int decimalLimitQty;

	private String unit;

	private Double rate;

	private int decimalLimitForCurrency;

}