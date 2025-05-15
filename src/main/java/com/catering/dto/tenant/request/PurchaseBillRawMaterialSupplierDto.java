package com.catering.dto.tenant.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseBillRawMaterialSupplierDto extends PurchaseOrderContactSupplierDataDto {

	private Long taxMasterId;

	public PurchaseBillRawMaterialSupplierDto(Long contactId, Double itemPrice, Long taxMasterId) {
		super(contactId, itemPrice);
		this.taxMasterId = taxMasterId;
	}

}