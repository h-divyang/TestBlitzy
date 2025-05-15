package com.catering.dto.tenant.request;

import com.catering.model.tenant.MeasurementModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllRawMaterialSupplierDto {

	private Long id;

	private Boolean isActive;

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private MeasurementModel measurement;

	private Long supplier;

	private Long rawMaterialCategoryTypeId;

}