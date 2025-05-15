package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuPreparationWithImageMenuReportDto {

	private String companyUserName;

	private String companyAddress;

	private String companyMobileNumber;

	private String customerName;

	private String customerMobile;

	private String eventName;

	private String hallName;

	private String venue;

	private String customerHomeAddress;

	private String customerEmail;

	private String managerName;

	private String chefName;

	private LocalDate eventDate;

	private String mealTypename;

	private String notes;

}