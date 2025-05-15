package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A Data Transfer Object (DTO) representing a menu execution raw material list report.
 * This DTO is used to encapsulate information related to a menu execution report, including
 * report dates, function details, item categories,
 * raw material details, quantities, and measurement units.
 * 
 * @author Krushali Talaviya
 * @since 2024-05-07
 */
@Getter
@Setter
@AllArgsConstructor
public class TotalRawMaterialReportDto {

	private Long orderId;

	/**
	 * The id of the order function.
	 */
	private Long functionId;

	private String venue;

	private Boolean isExtra;

	private Boolean isDateTime;

	/**
	 * Represents the start date of the event or report period.
	 */
	private LocalDateTime date;

	/**
	 * Represents the name of the function or event type being reported.
	 */
	private String functionName;

	/**
	 * Represents the category of the menu item or product.
	 */
	private String rawMaterialCategory;

	/**
	 * Represents the name of the raw material used in the menu item or product.
	 */
	private String rawMaterial;

	/**
	 * Represents the final quantity or amount of the raw material.
	 */
	private Double finalQty;

	/**
	 * Represents the measurement unit (e.g., kilograms, liters) for the final quantity.
	 */
	private String finalMeasurementName;

	private Long finalMeasurementId;

	private String menuItemAndRawMaterialId;

	/**
	 * Precision Limit For the Qty.
	 */
	private Integer decimalLimitQty;

	private Boolean isAllFuntions;

}