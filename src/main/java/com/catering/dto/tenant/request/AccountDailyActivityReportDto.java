package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDailyActivityReportDto {

	private Long materialId;

	private String contactName;

	private LocalDateTime transactionDate;

	private Long voucherNo;

	private String voucherType;

	private String accountName;

	private Double jama;

	private Double udhar;

	private String subUnit;

	private Integer subDecimalLimitQty;

	private Double balance;

	private String balanceUnit;

	private Integer decimalLimitQty;

	private Long contactId;

	private String remark;

	private Integer decimalLimitForCurrency;

	public AccountDailyActivityReportDto(Long materialId, String contactName, LocalDateTime transactionDate,
			Long voucherNo, String voucherType, String accountName, Double jama, Double udhar, String subUnit,
			Integer subDecimalLimitQty, Double balance, String balanceUnit, Integer decimalLimitQty) {
		this.materialId = materialId;
		this.contactName = contactName;
		this.transactionDate = transactionDate;
		this.voucherNo = voucherNo;
		this.voucherType = voucherType;
		this.accountName = accountName;
		this.jama = jama;
		this.udhar = udhar;
		this.subUnit = subUnit;
		this.subDecimalLimitQty = subDecimalLimitQty;
		this.balance = balance;
		this.balanceUnit = balanceUnit;
		this.decimalLimitQty = decimalLimitQty;
	}

	public AccountDailyActivityReportDto(Long contactId, String contactName, LocalDateTime transactionDate, Long voucherNo,
			String voucherType, String accountName, Double jama, Double udhar, Double balance,
			String remark, Integer decimalLimitForCurrency) {
		this.contactId = contactId;
		this.contactName = contactName;
		this.transactionDate = transactionDate;
		this.voucherNo = voucherNo;
		this.voucherType = voucherType;
		this.accountName = accountName;
		this.jama = jama;
		this.udhar = udhar;
		this.balance = balance;
		this.remark = remark;
		this.decimalLimitForCurrency = decimalLimitForCurrency;
	}

}