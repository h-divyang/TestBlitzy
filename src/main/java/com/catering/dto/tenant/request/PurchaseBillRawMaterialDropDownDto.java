package com.catering.dto.tenant.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseBillRawMaterialDropDownDto extends CommonDataForDropDownDto {

	private String hsnCode;

	private Long taxMasterId;

	private Long measurementId;

	private List<PurchaseBillRawMaterialSupplierDto> contactSupplierList;

	public PurchaseBillRawMaterialDropDownDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, String hsnCode, Long taxMasterId, Long measurementId) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.hsnCode = hsnCode;
		this.taxMasterId = taxMasterId;
		this.measurementId = measurementId;
	}

}