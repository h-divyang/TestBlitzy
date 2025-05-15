package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountBankBookReportDto {

	private Double pastBalance;

	private LocalDateTime transactionDate;

	private Long voucherNumber;

	private String voucherType;

	private String description;

	private Double debit;

	private Double credit;

	private int paymentMode;

	private Long paymentNumber;

	private LocalDateTime paymentDate;

	private Double openingBalance;

	private Double closingBalance;

	private int decimalLimitForCurrency;

}