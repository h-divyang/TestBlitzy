package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyPreferencesForReportDto {

	private String name;

	private String address;

	private String email;

	private String mobileNumber;

	private String gstNo;

	private String invoiceAddress;

	private String officeNumber;

	private String website;

}