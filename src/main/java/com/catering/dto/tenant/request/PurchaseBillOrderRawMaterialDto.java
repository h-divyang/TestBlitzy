package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseBillOrderRawMaterialDto {

	private Long rawMaterialId;

	private String hsnCode;

	private Double weight;

	private Long measurementId;

	private Double price;

	private Long taxMasterId;

	private Double totalAmount;

}