package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommanDataReportWithOutVenue {

	private String eventName;

	private LocalDate eventMainDate;

	private String notes;

	private String mealTypeName;

	private String customerName;

	private String mobileNumber;

	private String customerEmail;

	private String managerNumber;

	private String managerName;

	private String customerOfficeNumber;

	private String hallName;
}