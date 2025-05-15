package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CashBankPaymentReceiptCommonResultListDto {

	private String nameDefaultLang;

	private String namePreferLang;

	private String nameSupportiveLang;

	private Double totalAmount;

}