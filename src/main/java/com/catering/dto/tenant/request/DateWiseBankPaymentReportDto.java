package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DateWiseBankPaymentReportDto {

	private Long contactId;

	private String bankName;

	private Long refNo;

	private LocalDateTime transactionDate;

	private String accountName;

	private Double amount;

	private String cheqNo;

	private LocalDateTime cheqDate;

	private String particulars;

	private int decimalLimitForCurrency;

	private int transactionType;

}