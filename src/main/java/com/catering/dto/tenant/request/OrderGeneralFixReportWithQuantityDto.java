package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * GeneralFixReportWithQuantityDto.
 * This class represents a Data Transfer Object (DTO). for the General Fix Raw Material Report in an event agency distribution.
 * 
 * @author Amit Chauhan
 * @since 2024-02-16
 */
@Getter
@Setter
public class OrderGeneralFixReportWithQuantityDto extends OrderGeneralFixReportWithoutQuantityDto {

	private Double quantity;

	private String unit;

	private Long decimaLimitQty;

	public OrderGeneralFixReportWithQuantityDto(Integer orderFunctionId, String functionName, LocalDateTime date, String rawMaterialName, Double quantity, String unit, Long decimaLimitQty, Boolean isMaxPerson) {
		super(orderFunctionId, functionName, date, rawMaterialName, isMaxPerson);
		this.quantity = quantity;
		this.unit = unit;
		this.decimaLimitQty = decimaLimitQty;
	}

}