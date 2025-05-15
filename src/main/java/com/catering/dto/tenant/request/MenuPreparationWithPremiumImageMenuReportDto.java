package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuPreparationWithPremiumImageMenuReportDto {

	private String customerName;

	private String mobileNumber;

	private String eventName;

	private String hallName;

	private String venue;

	private String customerAddress;

	private String mealTypeName;

	private String notes;

}