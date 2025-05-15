package com.catering.dto.tenant.request;

import java.time.LocalDate;

import com.catering.dto.audit.AuditIdDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CashPaymentReceiptResponseDto extends AuditIdDto {

	private LocalDate transactionDate;

	private boolean transactionType;

	private ContactResponseDto contactSupplier;

	private Double totalAmount;

}