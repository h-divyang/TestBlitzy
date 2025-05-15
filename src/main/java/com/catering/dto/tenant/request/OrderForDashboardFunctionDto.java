package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderForDashboardFunctionDto extends IdDto {

	private OrderForDashboardFunctionTypeDto functionType;

	private Long person;

	private LocalDateTime date;

	private LocalDateTime endDate;

	private Integer sequence;

}