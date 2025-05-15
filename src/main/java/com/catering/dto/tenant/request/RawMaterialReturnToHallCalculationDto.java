package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RawMaterialReturnToHallCalculationDto {

	private Long inputTransferToHallId;

	private Long rawMaterialReturnToHallId;

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

}