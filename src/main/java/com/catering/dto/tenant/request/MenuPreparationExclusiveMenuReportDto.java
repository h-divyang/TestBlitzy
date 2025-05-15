package com.catering.dto.tenant.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuPreparationExclusiveMenuReportDto {

	private Long functionId;

	private String functionName;

	private Long person;

	private LocalDateTime date;

	private LocalDateTime endDate;

	private Double rate;

	private String functionAddress;

	private String functionNote;

	private String menuItemCategory;

	private String menuItem;

	private LocalDate bookingDate;

}