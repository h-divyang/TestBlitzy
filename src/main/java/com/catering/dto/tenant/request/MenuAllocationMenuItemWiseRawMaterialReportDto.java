package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The `MenuExecutionItemWiseRawMaterialReportDto` class represents a data transfer object (DTO) for
 * item-wise raw material reports related to menu execution. It contains properties to store information
 * about an event, party, venue, dates, mobile number, function, person, item category, raw material,
 * final material, and final quantity.
 *
 * This DTO is used for transferring report data between layers of the catering system.
 *
 * @author Krushali Talaviya
 * @since 2023-09-11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuAllocationMenuItemWiseRawMaterialReportDto {

	/**
	 * The id of the order function.
	 */
	private Long functionId;

	/**
	 * The name of the function.
	 */
	private String functionName;

	/**
	 * The identifier of the person associated with the event.
	 */
	private Long person;

	private String functionAddress;

	/**
	 * The start date of the event.
	 */
	private LocalDateTime date;

	/**
	 * The end date of the event.
	 */
	private LocalDateTime endDate;

	/**
	 * The name of the item category.
	 */
	private String rawMaterialCategory;

	/**
	 * The name of the raw material.
	 */
	private String rawMaterial;

	/**
	 * The Id of menu item.
	 */
	private Long menuItemId;

	/**
	 * The name of the menu item.
	 */
	private String menuItem;

	/**
	 * The final quantity of the material.
	 */
	private Double finalQty;

	/**
	 * The final measurement of the raw material.
	 */
	private String finalQuantityMeasurement;

	/**
	 * The total quantity of the material.
	 */
	private Double totalQty;

	/**
	 * The total measurement of the raw material.
	 */
	private String totalQuantityMeasurement;

	/**
	 * If value is 1 then item is general fix raw material otherwise not.
	 */
	private int isGeneralFixRawMaterial;

	/**
	 * Measurement Id for the toal qty.
	 */
	private Integer decimalLimitQtyForFinal;

	/**
	 * Display the decimal limit qty.
	 */
	private Integer decimalLimitQtyForTotal;

	private Double extraQty;

	private Integer extraDecimalLimitQty;

	private String extraMeasurementSymbol;

}