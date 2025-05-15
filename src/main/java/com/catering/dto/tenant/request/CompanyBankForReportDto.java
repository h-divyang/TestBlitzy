package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyBankForReportDto {

	private String accountName;

	private String accountNumber;

	private String branchName;

	private String ifscCode;

}