package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a Chithhi report for event raw materials.
 * This DTO encapsulates information about a Chithhi report, including details such as company information,
 * venue, item category, agency, start date, particulars, and unit.
 * 
 * @since 2023-10-09
 * @author Krushali Talaviya
 */
@Getter
@Setter
@AllArgsConstructor
public class RawMaterialChithhiReportDto {

	private Long orderId;

	/**
	 * The venue related to the Chithhi report.
	 */
	private String venue;

	private Boolean isExtra;

	/**
	 * The item category for the Chithhi report.
	 */
	private String rawMaterialCategory;

	private Long contactAgencyId;

	/**
	 * The agency associated with the Chithhi report.
	 */
	private String agency;

	/**
	 * The start date of the Chithhi report.
	 */
	private LocalDateTime orderDate;

	/**
	 * The order date for the references.
	 */
	private String orderDateRef;

	/**
	 * The order time for the references.
	 */
	private String orderTimeRef;

	/**
	 * The particulars or details of the Chithhi report.
	 */
	private String rawMaterial;

	private String menuItemAndRawMaterialId;

	private Double finalQty;

	private Long finalMeasurementId;

	
	/**
	 * The orderQuantity display quantity of the order.
	 */
	private String orderQuantity;

	/**
	 * Time period of the order time.
	 */
	private String timePeriod;

	private Integer decimalLimitQty;

	private Boolean isAllFuntions;

}