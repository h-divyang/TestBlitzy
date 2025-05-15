package com.catering.dto.tenant.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object (DTO) for representing a dropdown option for purchase order raw material.
 * Extends CommonDataForDropDownDto.
 * 
 * @author Krushali Talaviya
 * @since 2024-06-01
 */

@Getter
@Setter
public class PurchaseOrderRawMaterialDropDownDto extends CommonDataForDropDownDto {

	private String hsnCode;

	private Long taxMasterId;

	private Long measurementId;

	private Double supplierRate;

	private List<PurchaseOrderContactSupplierDataDto> contactSupplierList;

	/**
	 * Constructor for PurchaseOrderRawMaterialDropDownDto.
	 *
	 * @param id                 The ID of the raw material
	 * @param nameDefaultLang    The name in the default language
	 * @param namePreferLang     The name in the preferred language
	 * @param nameSupportiveLang The name in a supportive language
	 * @param hsnCode            The HSN code of the raw material
	 * @param taxMasterId        The ID of the tax master associated with the raw material
	 * @param measurementId      The ID of the measurement associated with the raw material
	 * @param supplierRate       The supplier rate of the raw material
	 */
	public PurchaseOrderRawMaterialDropDownDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, String hsnCode, Long taxMasterId, Long measurementId, Double supplierRate) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.setHsnCode(hsnCode);
		this.setTaxMasterId(taxMasterId);
		this.setMeasurementId(measurementId);
		this.setSupplierRate(supplierRate);
	}

}