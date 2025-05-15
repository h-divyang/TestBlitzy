package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndividualRecordBankPaymentReceiveReportDto extends IndividualRecordCashPaymentReceiveReportDto {

	private boolean paymentMode;

	private String chequeTransactionNumber;

	private LocalDate chequeTransactionDate;

	public IndividualRecordBankPaymentReceiveReportDto(Long customerId, String contactName, String customerNumber,
			Long invoiceNumber, LocalDate transactionDate, String remark, Double amount, Boolean isReceipt, String venue, boolean paymentMode, String chequeTransactionNumber, LocalDate chequeTransactionDate) {
		super(customerId, contactName, customerNumber, invoiceNumber, transactionDate, remark, amount, isReceipt, venue);
		this.paymentMode = paymentMode;
		this.chequeTransactionNumber = chequeTransactionNumber;
		this.chequeTransactionDate = chequeTransactionDate;
	}

}