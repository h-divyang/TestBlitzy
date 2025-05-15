package com.catering.dto.tenant.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawMaterialAllocationRawMaterialDto {

	private Long rawMaterialId;

	private String rawMaterialNameDefaultLang;

	private String rawMaterialNamePreferLang;

	private String rawMaterialNameSupportiveLang;

	private Long rawMaterialCategoryId;

	private Double supplierRate;

	private Long supplierMeasurementId;
	
	private boolean supplierMIsBaseUnit;

	private Double supplierMBaseUnitEquivalent;

	private Double actualQty;

	private Long actualQtyMeasurementId;

	private Double finalQty;

	private Long finalQtyMeasurementId;

	private Double total;

	private Long agencyId;

	private Long godownId;

	private Long orderRMfinalMeasurementId;

	private List<RawMaterialAllocationMenuItemDto> menuItems;

	public RawMaterialAllocationRawMaterialDto(Long rawMaterialId, String rawMaterialNameDefaultLang,
			String rawMaterialNamePreferLang, String rawMaterialNameSupportiveLang, Long rawMaterialCategoryId,
			Double supplierRate, Long supplierMeasurementId, Double actualQty, Long actualQtyMeasurementId,
			Double finalQty, Long finalQtyMeasurementId, Double total, Long agencyId, Long godownId, Long orderRMfinalMeasurementId, Boolean supplierMIsBaseUnit, Double supplierMBaseUnitEquivalent) {
		this.rawMaterialId = rawMaterialId;
		this.rawMaterialNameDefaultLang = rawMaterialNameDefaultLang;
		this.rawMaterialNamePreferLang = rawMaterialNamePreferLang;
		this.rawMaterialNameSupportiveLang = rawMaterialNameSupportiveLang;
		this.rawMaterialCategoryId = rawMaterialCategoryId;
		this.supplierRate = supplierRate;
		this.supplierMeasurementId = supplierMeasurementId;
		this.actualQty = actualQty;
		this.actualQtyMeasurementId = actualQtyMeasurementId;
		this.finalQty = finalQty;
		this.finalQtyMeasurementId = finalQtyMeasurementId;
		this.total = total;
		this.agencyId = agencyId;
		this.godownId = godownId;
		this.orderRMfinalMeasurementId = orderRMfinalMeasurementId;
		this.supplierMIsBaseUnit = supplierMIsBaseUnit;
		this.supplierMBaseUnitEquivalent = supplierMBaseUnitEquivalent;
	}

}