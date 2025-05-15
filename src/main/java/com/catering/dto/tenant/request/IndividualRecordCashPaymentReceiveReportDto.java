package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IndividualRecordCashPaymentReceiveReportDto {

	private Long customerId;

	private String contactName;

	private String customerNumber;

	private Long invoiceNumber;

	private LocalDate transactionDate;

	private String remark;

	private Double amount;

	private Boolean isReceipt;

	private String venue;

}