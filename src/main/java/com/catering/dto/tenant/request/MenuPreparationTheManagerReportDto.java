package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuPreparationTheManagerReportDto {

	private String customerName;

	private String customerMobileNumber;

	private String eventName;

	private LocalDate eventDate;

	private String hallName;

	private String customerHomeAddress;

	private String mealTypeName;

	private String notes;

	private String venue;

}