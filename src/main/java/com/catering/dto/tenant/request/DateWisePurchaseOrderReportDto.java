package com.catering.dto.tenant.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object (DTO) for representing a purchase order with raw material.
 * 
 * @author Mashuk Patel
 */
@Getter
@Setter
@AllArgsConstructor
public class DateWisePurchaseOrderReportDto {

	private String contactName;

	private Long purchaseOrderId;

	private LocalDateTime purchaseOrderDate;

	private String rawMaterialName;

	private Double finalQty;

	private String finalQuantityMeasurement;

	private int decimalLimitQty;

	private Double rate;

	private Double amt;

	private Double netAmt;

	private Double suppilerContactSubTotal;

	private Double suppilerContactTotal;

	private int decimalLimitForCurrency;

}