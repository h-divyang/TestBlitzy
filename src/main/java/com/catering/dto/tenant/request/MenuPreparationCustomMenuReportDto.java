package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuPreparationCustomMenuReportDto {

	private Long orderId;

	private Long functionId;

	private String functionName;

	private Long person;

	private String functionAddress;

	private String functionNote;

	private LocalDateTime date;

	private LocalDateTime endDate;

	private Double rate;

	private String menuItemCategory;

	private String menuItem;

	private String notes;

	private String slogan;

	private String langCode;

	private String timeInWord;

}