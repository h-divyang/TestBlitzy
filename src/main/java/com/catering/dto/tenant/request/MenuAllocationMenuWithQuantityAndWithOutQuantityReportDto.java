package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a menu allocation report with quantity details.
 *
 * @author Krushali Talaviya
 * @since 2023-09-13
 */
@Getter
@Setter
@AllArgsConstructor
public class MenuAllocationMenuWithQuantityAndWithOutQuantityReportDto {

	/**
	 * The id of the order function.
	 */
	private Long functionId;

	/**
	 * The name of the function or event.
	 */
	private String functionName;

	/**
	 * The number of persons involved in the event.
	 */
	private Long person;

	private String functionAddress;

	private String functionNote;

	/**
	 * The start date of the event.
	 */
	private LocalDateTime date;

	/**
	 * The end date of the event.
	 */
	private LocalDateTime endDate;

	/**
	 * The id of menu item.
	 */
	private Long menuItemId;

	/**
	 * The Menu Item used in the event.
	 */
	private String menuItem;

	/**
	 * The raw material used in the event.
	 */
	private String rawMaterial;

	/**
	 * The final Qty of the raw material.
	 */
	private Double finalQty;

	/**
	 * The measurement name of the final material used in the event.
	 */
	private String finalQuantityMeasurement;

	/**
	 * Precision Digit Limit will be display.
	 */
	private Integer decimalLimitQty;

	/**
	 * Menu allocation orderType will display (e.g chef labour = 1, outside = 2).
	 */
	private Integer orderType;

}