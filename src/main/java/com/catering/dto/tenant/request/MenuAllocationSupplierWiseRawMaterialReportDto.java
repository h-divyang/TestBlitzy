package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Data Transfer Object (DTO) for supplier-wise raw material
 * reports related to menu allocation for events.
 *
 * This class encapsulates information about a specific event, including its
 * name, main date, party name, venue, agency name, mobile number, current date,
 * item category, raw material, and final quantity.
 *
 * Use this DTO to store and transfer event-related data in a structured manner.
 *
 * @author Krushali Talaviya
 * @since 2023-09-15
 */
@Getter
@Setter
@AllArgsConstructor
public class MenuAllocationSupplierWiseRawMaterialReportDto {

	/**
	 * The name of the agency.
	 */
	private String agencyName;

	/**
	 * The mobile number associated with the event.
	 */
	private String mobileNumber;

	private String venue;

	/**
	 * The name of the item category.
	 */
	private String rawMaterialCategory;

	/**
	 * The name of the raw material.
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

}