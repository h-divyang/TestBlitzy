package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerFormatReportDto {

	private Long orderFunctionId;

	private LocalDate eventDate;

	private String venue;

	private String customerName;

	private String functionName;

	private String functionAddress;

	private Long person;

}