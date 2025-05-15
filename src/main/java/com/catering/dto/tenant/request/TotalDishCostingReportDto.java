package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TotalDishCostingReportDto {

	private String functionName;

	private LocalDateTime date;

	private LocalDateTime endDate;

	private Double grandTotal;

	private Double dishCosting;

}