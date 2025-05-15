package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuPreparationPremiumImageMenuDto extends MenuPreparationSloganMenuReportDto {

	private Long menuItemCategoryId;

	private String menuItemCategoryImage;

	private String menuItemImage;

	private Long menuItemId;

	public MenuPreparationPremiumImageMenuDto(Long functionId, String functionName, LocalDateTime date, LocalDateTime endDate, Double rate, String functionAddress, String functionNote, 
			Long person, String menuItemCategory, String menuItem, String slogan, Long menuItemCategoryId, Long menuItemId) {
		super(functionId, functionName, date, endDate, rate, functionAddress, functionNote, person, menuItemCategory, menuItem, slogan);
		this.menuItemCategoryId = menuItemCategoryId;
		this.menuItemId = menuItemId;
	}

}