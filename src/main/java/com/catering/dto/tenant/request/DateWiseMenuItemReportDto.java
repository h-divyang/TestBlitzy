package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DateWiseMenuItemReportDto {

	private LocalDateTime orderDate;

	private String timeInWord;

	private String venue;

	private String menuItemName;

	private String menuItemCategory;

	private Long person;

}