package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountGroupSummaryReportDto extends AccountCollectionReportDto {

	private Double openingBalance;

	private Double balance;

	private Double totalOpeningBalance;

	private Double totalCredit;

	private Double totalDebit;

	private Double totalBalance;

	public AccountGroupSummaryReportDto(Long contactId, String name, Double openingBalance, Double credit, Double debit, Double balance, Double totalOpeningBalance, Double totalCredit, Double totalDebit, Double totalBalance, Integer decimalLimitCurrency) {
		super(contactId, name, credit, debit, decimalLimitCurrency);
		this.openingBalance = openingBalance;
		this.balance = balance;
		this.totalOpeningBalance = totalOpeningBalance;
		this.totalCredit = totalCredit;
		this.totalDebit = totalDebit;
		this.totalBalance = totalBalance;
	}

}