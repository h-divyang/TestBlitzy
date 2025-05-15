package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebitNotePurchaseBillDropDownDto extends CommonDataForDropDownDto {

	private Long purchaseBillContactId;

	private LocalDate billDate;

	private Double totalAmount;

	public DebitNotePurchaseBillDropDownDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, Long purchaseBillContactId, LocalDate billDate, Double totalAmount) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.purchaseBillContactId = purchaseBillContactId;
		this.billDate = billDate;
		this.totalAmount = totalAmount;
	}

}