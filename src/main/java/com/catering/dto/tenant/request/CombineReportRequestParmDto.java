package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO class for combining reports.
 */
@Getter
@Setter
public class CombineReportRequestParmDto {

	private List<CombineReportDto> selectedReportList;

	private Long[] functionId;

	private Integer langType;

	private String langCode;

	private String defaultLang;

	private String preferLang;

	private Long[] rawMaterialCategoryId;

	private Boolean isDateTime;

	private LocalDateTime orderDate;

	private boolean withQuantity;

	private Long count;

	private Long[] supplierWiseContactId;

	private Long[] generalFixRawMaterialCategoryId;

	private Long[] typesOfData;

	private Long[] agencyType;

	private Long[] agencyNameId;

	private Long[] menuItemId;

	private Long[] chefLabourSupplier;

	private Long[] chefLabourMenuItemName;

	private Long[] outsideLabourSupplier;

	private Long[] outsideLabourMenuItemName;

	private Long[] labourSupplierCategoryId;

	private Long[] labourSupplierId;

	private boolean maxSetting;

}