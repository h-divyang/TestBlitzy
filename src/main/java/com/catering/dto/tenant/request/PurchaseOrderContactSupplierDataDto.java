package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents supplier details in a purchase order.
 * 
 * @Author Krushali Talaviya
 * @Since 2024-06-03
 */
@Getter
@Setter
@AllArgsConstructor
public class PurchaseOrderContactSupplierDataDto {

	private Long contactId;

	private Double itemPrice;

}