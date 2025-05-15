package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateWiseStockReportDto {

	private LocalDateTime transferDate;

	private Long voucherNo;

	private String voucherType;

	private String contactName;

	private Double issue;

	private Double receive;

	private String subUnit;

	private Integer subDecimalLimitQty;

	private Double balance;

	private String balanceUnit;

	private Integer decimalLimitQty;

	private String rawMaterialName;

	public DateWiseStockReportDto(LocalDateTime transferDate, Long voucherNo, String voucherType, String contactName, Double issue, Double receive, String subUnit, Integer subDecimalLimitQty, Double balance, String balanceUnit, Integer decimalLimitQty) {
		this.transferDate = transferDate;
		this.voucherNo = voucherNo;
		this.voucherType = voucherType;
		this.contactName = contactName;
		this.issue = issue;
		this.receive = receive;
		this.subUnit = subUnit;
		this.subDecimalLimitQty = subDecimalLimitQty;
		this.balance = balance;
		this.balanceUnit = balanceUnit;
		this.decimalLimitQty = decimalLimitQty;
	}

	public DateWiseStockReportDto(String rawMaterialName) {
		this.rawMaterialName = rawMaterialName;
	}

}