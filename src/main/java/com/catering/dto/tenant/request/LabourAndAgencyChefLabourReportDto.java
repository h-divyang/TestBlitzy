package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a data transfer object (DTO) for the Chef Labour Report in a menu execution.
 *
 * This DTO contains information about chef labour reports, including details such as mobile number, email ID,
 * venue, agency name, agency number, function name, item name, order date, and the number of pax (guests).
 *
 * @since 1.0
 * @author 2023-09-22
 */
@Getter
@Setter
@AllArgsConstructor
public class LabourAndAgencyChefLabourReportDto {

	private String venue;

	private String hallName;

	private String agencyName;

	private String agencyNumber;

	private String functionName;

	private String menuItemName;

	private Double quantity;

	private String unit;

	private String notes;

	private LocalDateTime orderDate;

	private String counterNo;

	private String helperNo;

	private Integer decimalLimitQtyForChef;

	private Long pax;

	private Integer decimalLimitQty;

}