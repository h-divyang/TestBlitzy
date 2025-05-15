package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LabourAndAgencyLabourReportDto {

	private String venue;

	private String hallName;

	private String functionName;

	private String contactCategory;

	private String contact;

	private String mobileNumber;

	private LocalDateTime date;

	private Long quantity;

	private String labourShift;

	private String note;

}