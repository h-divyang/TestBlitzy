package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * A Data Transfer Object (DTO) representing a menu execution total raw material report.
 * This DTO is used to encapsulate information related to a menu execution report, including
 * report dates, function details, item categories,
 * raw material details, quantities, and measurement units.
 * 
 * @author Krushali Talaviya
 * @since 2023-09-13
 */
@Getter
@Setter
public class MenuAllocationDetailRawMaterialReportDto extends TotalRawMaterialReportDto {

	/**
	 * Represents the total sum or quantity of the raw material used.
	 */
	private String sumOfRawMaterial;

	/**
	 * Display time in the word.
	 */
	private String timeInWord;

	public MenuAllocationDetailRawMaterialReportDto(Long orderId, Long functionId, Boolean isExtra, String venue, Boolean isDateTime, LocalDateTime date, String functionName, String rawMaterialCategory, String rawMaterial, String sumOfRawMaterial, Double finalQty, String finalMeasurementName, Long finalMeasurementId, String menuItemAndRawMaterialId, Integer decimalLimitQty, String timeInWord, Boolean isAllFuntions) {
		super(orderId, functionId, venue, isExtra, isDateTime, date, functionName, rawMaterialCategory, rawMaterial, finalQty, finalMeasurementName, finalMeasurementId, menuItemAndRawMaterialId, decimalLimitQty, isAllFuntions);
		this.sumOfRawMaterial = sumOfRawMaterial;
		this.timeInWord = timeInWord;
	}

}