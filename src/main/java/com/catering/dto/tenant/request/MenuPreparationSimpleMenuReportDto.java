package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuPreparationSimpleMenuReportDto extends MenuPreparationExclusiveMenuReportDto {

	private LocalDateTime endDate;

	private String menuItemCategoryImage;

	private Long menuItemCategoryId;

	public MenuPreparationSimpleMenuReportDto(Long functionId, String functionName, Long person, LocalDateTime date, Double rate, String functionAddress, String functionNote,
			String menuItemCategory, String menuItem, LocalDateTime endDate, Long menuItemCategoryId) {
		super(functionId, functionName, person, date, null, rate, functionAddress, functionNote, menuItemCategory, menuItem, null);
		this.endDate = endDate;
		this.menuItemCategoryId = menuItemCategoryId;
	}

}