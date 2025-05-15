package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateWiseGeneralLedgerReportDto {

	private Long contactId;

	private String contactName;

	private LocalDateTime transactionDate;

	private Long voucherNumber;

	private String voucherType;

	private Double debit;

	private Double credit;

	private Double balance;

	private int decimalLimitForCurrency;

	private String remark;

	public DateWiseGeneralLedgerReportDto(Long contactId, String contactName, LocalDateTime transactionDate, Long voucherNumber, String voucherType, Double debit, Double credit, Double balance, String remark, int decimalLimitForCurrency) {
		this.contactId = contactId;
		this.contactName= contactName;
		this.transactionDate = transactionDate;
		this.voucherNumber = voucherNumber;
		this.voucherType = voucherType;
		this.debit = debit;
		this.credit = credit;
		this.balance = balance;
		this.remark = remark;
		this.decimalLimitForCurrency = decimalLimitForCurrency;
	}

	public DateWiseGeneralLedgerReportDto(String contactName) {
		this.contactName = contactName;
	}

}