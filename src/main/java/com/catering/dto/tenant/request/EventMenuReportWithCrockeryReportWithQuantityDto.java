package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventMenuReportWithCrockeryReportWithQuantityDto extends CrockeryWithQuantityReportDto {

	private String timeInWord;

	public EventMenuReportWithCrockeryReportWithQuantityDto(Integer orderFunctionId, String functionName, String category, String crockeryName,
			LocalDateTime date, Double quantity, String timeInWord, Byte quantityDecimalLimit, Boolean isMaxPerson) {
		super(orderFunctionId, functionName, category, crockeryName, date, quantity, quantityDecimalLimit, isMaxPerson);
		this.timeInWord = timeInWord;
	}

}