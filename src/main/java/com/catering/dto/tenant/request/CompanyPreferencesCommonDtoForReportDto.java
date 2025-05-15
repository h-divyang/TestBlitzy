package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanyPreferencesCommonDtoForReportDto {

	private String companyName;

	private String companyAddress;

	private String companyNumber;

	private String emailId;

}