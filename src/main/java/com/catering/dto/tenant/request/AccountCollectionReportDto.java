package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountCollectionReportDto {

	private Long contactId;

	private String name;

	private Double credit;

	private Double debit;

	private Integer decimalLimitCurrency;

}