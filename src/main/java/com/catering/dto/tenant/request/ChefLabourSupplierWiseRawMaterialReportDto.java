package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChefLabourSupplierWiseRawMaterialReportDto {

	private Long orderId;

	private Long functionId;

	private String venue;

	private Boolean isExtra;

	private Long contactId;

	private String contactName;

	private Long supplierId;

	private String supplierName;

	private Boolean isDateTime;

	private LocalDateTime date;

	private String functionName;

	private String rawMaterialCategory;

	private String rawMaterial;

	private String menuItemAndRawMaterialId;

	private Double finalQty;

	private String finalMeasurementName;

	private Long finalMeasurementId;

	private Integer decimalLimitQty;

	private Boolean isAllFuntions;

}