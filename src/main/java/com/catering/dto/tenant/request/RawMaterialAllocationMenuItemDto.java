package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RawMaterialAllocationMenuItemDto {

	private Long rawMaterialAllocationId;

	private Long rawMaterialId;

	private Long functionId;

	private String functionNameDefaultLang;

	private String functionNamePreferLang;

	private String functionNameSupportiveLang;

	private Long menuItemId;

	private String itemNameDefaultLang;

	private String itemNamePreferLang;

	private String itemNameSupportiveLang;

	private Long agencyId;

	private Long agencyTypeId;

	private Double finalQty;

	private Long finalQtyMeasurementId;

	private LocalDateTime functionTime;

	private Double total;

	private Boolean isExtra;

	private Long rawMaterialCategoryTypeId;

	private Long godown;

}