package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DateWiseLabourReoportDto {

	private String supplierName;
	
	private String mobileNumber;

	private String dateForRef;

	private LocalDateTime orderDate;

	private String venue;

	private String supplierCategory;

	private String timeData;

	private Long quantity;

	private Double rate;

	private Double total;

}