package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputTransferToHallRawMaterialDropDownDto extends CommonDataForDropDownDto {

	private Long measurementId;

	public InputTransferToHallRawMaterialDropDownDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, Long measurementId) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.measurementId = measurementId;
	}

}