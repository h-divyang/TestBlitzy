package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChefLabourWiseRawMaterialReportDto {

	private Long orderId;

	private Long orderType;

	private Long functionId;

	private String venue;

	private Boolean isExtra;

	private Long contactId;

	private String contactName;

	private Boolean isDateTime;

	private LocalDateTime date;

	private String functionName;

	private Long person;

	private String rawMaterialCategory;

	private String rawMaterial;

	private Long menuPreparationMenuItemId;

	private String menuItemAndRawMaterialId;

	private String menuItem;

	private String menuItemGroup;

	private Double finalQty;

	private String finalMeasurementName;

	private Long finalMeasurementId;

	private Integer decimalLimitQty;

	private Boolean isAllFuntions;

}