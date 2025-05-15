package com.catering.dto.tenant.request;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.catering.constant.MessagesConstant;
import com.catering.dto.audit.OnlyIdDto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankPaymentReceiptDetailsDto extends OnlyIdDto {

	@JsonBackReference
	private BankPaymentReceiptRequestDto bankPaymentReceipt;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private int voucherType;

	private Long voucherNumber;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private PaymentContactCustomDto contact;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private Integer paymentMode;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private Double amount;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private Long chequeTransactionNumber;

	@NotNull(message = MessagesConstant.VALIDATION_IS_REQUIRED)
	private LocalDate chequeTransactionDate;

	private String remark;

}