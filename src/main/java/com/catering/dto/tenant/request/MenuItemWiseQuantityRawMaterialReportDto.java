package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuItemWiseQuantityRawMaterialReportDto {

	private String menuItemCategory;

	private String menuItem;

	private String rawMaterial;

	private String rawMaterialCategory;

	private Double qty;

	private String measurement;

	private Integer decimalLimitQty;

}