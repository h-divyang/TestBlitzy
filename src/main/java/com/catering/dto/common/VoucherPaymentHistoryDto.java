package com.catering.dto.common;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class VoucherPaymentHistoryDto {

	private Long id;

	private Double amount;

	private LocalDate transactionDate;

	private int paymentType;

}