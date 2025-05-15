package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDtoForReport {

	private String eventName;

	private LocalDate eventTime;

	private String customerName;

	private String customerAddress;

	private String customerMobileNumber;

	private String customerGstNo;

	private String email;

	private String venue;

}