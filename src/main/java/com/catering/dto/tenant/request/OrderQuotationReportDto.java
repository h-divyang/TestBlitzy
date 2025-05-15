package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderQuotationReportDto {

	private LocalDateTime date;

	private String name;

	private String sac;

	private Long quantity;

	private Long extra;

	private Double rate;

	private String amount;

	private Boolean isFunction;

}