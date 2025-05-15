package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleMenuSubReportDto {

	private Long orderId;

	private Long functionId;

	private String functionName;

	private LocalDateTime startDate;

	private Double rate;

	private Long person;

	private String functionAddress;

}