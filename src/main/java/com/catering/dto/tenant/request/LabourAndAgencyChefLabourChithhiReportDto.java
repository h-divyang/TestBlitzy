package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LabourAndAgencyChefLabourChithhiReportDto {

	private String agencyName;

	private String agencyNumber;

	private String managerName;

	private String managerNumber;

	private LocalDateTime orderDate;

	private Long person;

	private String venue;

	private String menuItemName;
	
	private String counterNo;

	private String helperNo;

	private String unit;

	private String hallName;

	private String functionName;

	private String timePeriod;

	private Integer decimalLimitQty;

	private Integer plate;

}