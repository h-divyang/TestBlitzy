package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DateWiseCashPaymentReportDto {

	private Long refNo;

	private LocalDateTime transactionDate;

	private String accountName;

	private Double amount;

	private String particulars;

	private int decimalLimitForCurrency;

	private int transactionType;

}