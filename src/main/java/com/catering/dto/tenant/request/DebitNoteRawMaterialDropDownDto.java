package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebitNoteRawMaterialDropDownDto extends CommonDataForDropDownDto {

	private Long taxMasterId;

	private Long measurementId;

	public DebitNoteRawMaterialDropDownDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, Long taxMasterId, Long measurementId) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.setTaxMasterId(taxMasterId);
		this.setMeasurementId(measurementId);
	}

}