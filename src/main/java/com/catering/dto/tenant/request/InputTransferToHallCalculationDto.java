package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InputTransferToHallCalculationDto {

	private Long customerOrderId;

	private Long inputTransferToHallId;

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

}