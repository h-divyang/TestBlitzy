package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.util.List;

import com.catering.dto.audit.IdDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderForUpcomingEventDto extends IdDto {

	private OrderForDashboardCustomerDto contactCustomer;

	private LocalDate eventMainDate;

	private CommonMultiLanguageWithoutAuditDto eventType;

	private List<OrderForDashboardFunctionDto> functions;

}