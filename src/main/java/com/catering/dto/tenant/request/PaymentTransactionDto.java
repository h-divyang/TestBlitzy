package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentTransactionDto {

	private Double amount;

	private LocalDate paymentDate;

}