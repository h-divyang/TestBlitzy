package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseBillOrderDropDownDto extends CommonDataForDropDownDto {

	private Long purchaseOrderContactId;

	private LocalDate purchaseDate;

	private Double totalAmount;

	public PurchaseBillOrderDropDownDto(Long id, String nameDefaultLang, String namePreferLang, String nameSupportiveLang, Long purchaseOrderContactId, LocalDate purchaseDate, Double totalAmount) {
		super(id, nameDefaultLang, namePreferLang, nameSupportiveLang);
		this.purchaseOrderContactId = purchaseOrderContactId;
		this.purchaseDate = purchaseDate;
		this.totalAmount = totalAmount;
	}

}