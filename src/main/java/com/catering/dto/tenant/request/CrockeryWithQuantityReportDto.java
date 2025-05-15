package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a crockery report for event agency distribution.
 *
 * @author Amit Chauhan
 * @since 2024-02-22
 */
@Getter
@Setter
public class CrockeryWithQuantityReportDto extends CrockeryWithoutQuantityReportDto {

	private Double quantity;

	private Byte quantityDecimalLimit;

	public CrockeryWithQuantityReportDto(Integer orderFunctionId, String functionName, String category,
			String crockeryName, LocalDateTime date, Double quantity, Byte quantityDecimalLimit, Boolean isMaxPerson) {
		super(orderFunctionId, functionName, category, crockeryName, date, isMaxPerson);
		this.quantity = quantity;
		this.quantityDecimalLimit = quantityDecimalLimit;
	}

}