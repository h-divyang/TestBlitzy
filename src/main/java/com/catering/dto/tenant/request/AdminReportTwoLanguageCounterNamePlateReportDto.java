package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminReportTwoLanguageCounterNamePlateReportDto {

	private String menuItemNameInDefaultLang;

	private String menuItemNameInPreferLang;

}