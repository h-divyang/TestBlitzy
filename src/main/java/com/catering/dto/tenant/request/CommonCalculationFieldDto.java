package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class representing calculation fields for a purchase order.
 *
 * @author Krushali Talaviya
 * @since 2024-05-31
 */
@Getter
@Setter
@AllArgsConstructor
public class CommonCalculationFieldDto {

	private Double amount;

	private Double taxableAmount;

	private Double totalAmount;

}