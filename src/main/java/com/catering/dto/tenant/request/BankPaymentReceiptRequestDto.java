package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.AuditIdDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BankPaymentReceiptRequestDto extends AuditIdDto {

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private LocalDate transactionDate;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private boolean transactionType;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private ContactResponseDto contact;

	@JsonManagedReference
	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	List<BankPaymentReceiptDetailsDto> bankPaymentReceiptDetailsList;

}