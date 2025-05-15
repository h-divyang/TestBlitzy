package com.catering.dto.tenant.request;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderForDashboardCustomerDto extends IdDto {

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private String mobileNumber;

}