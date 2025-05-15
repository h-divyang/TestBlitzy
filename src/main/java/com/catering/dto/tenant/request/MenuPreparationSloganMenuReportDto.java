package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Data Transfer Object (DTO) for generating menu preparation reports with various options:
 * - Menu with an image
 * - Menu with a slogan
 * - Menu with both an image and a slogan
 *
 * This DTO includes information about an event, its date, contact details, venue, meal type,
 * notes, function details, date range, number of persons, item category, item image, item, slogan,
 * and the current date.
 *
 * This class provides a constructor to initialize all the necessary attributes.
 *
 * @author Krushali Talaviya
 * @since 18-09-2023
 */
@Getter
@Setter
public class MenuPreparationSloganMenuReportDto extends MenuPreparationExclusiveMenuReportDto {

	private String slogan;

	public MenuPreparationSloganMenuReportDto(Long functionId, String functionName, LocalDateTime date, LocalDateTime endDate, Double rate, String functionAddress, String functionNote, Long person,
			String menuItemCategory, String menuItem, String slogan) {
		super(functionId, functionName, person, date, endDate, rate, functionAddress, functionNote, menuItemCategory, menuItem, null);
		this.slogan = slogan;
	}

}