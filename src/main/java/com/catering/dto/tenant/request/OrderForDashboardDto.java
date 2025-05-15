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
public class OrderForDashboardDto extends IdDto {

	private OrderForDashboardCustomerDto contactCustomer;

	private String venueDefaultLang;

	private String venuePreferLang;

	private String venueSupportiveLang;

	private LocalDate eventMainDate;

	private CommonMultiLanguageDto status;

	private Boolean isMenuPrepared;

	private List<OrderForDashboardFunctionDto> functions;

}