package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object (DTO) for representing a date wise purchase order without raw material.
 * 
 * @author Mashuk Patel
 */
@Getter
@Setter
@AllArgsConstructor
public class DateWisePurchaseOrderWithoutRawMaterialDto {

	private String contactName;

	private Long purchaseOrderId;

	private LocalDateTime purchaseOrderDate;

	private Double suppilerContactSubTotal;

	private Double suppilerContactTotal;

	private int decimalLimitForCurrency;

}