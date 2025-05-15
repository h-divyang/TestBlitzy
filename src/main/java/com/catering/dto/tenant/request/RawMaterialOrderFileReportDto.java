package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a order file report for raw
 * materials. This DTO encapsulates information about a order file report,
 * including details such as company information, event details, manager
 * information, agency details, order information, and event date.
 * 
 * @since 2023-10-09
 * @author Krushali Talaviya
 */
@Getter
@Setter
@AllArgsConstructor
public class RawMaterialOrderFileReportDto {

	private Integer functionId;

	private String venue;

	/**
	 * The type of agency associated with the event.
	 */
	private String agencyType;

	/**
	 * The agency responsible for the event.
	 */
	private String agency;

	/**
	 * The particulars or details of the event.
	 */
	private String particulars;

	private String mobileNumber;
	/**
	 * The quantity ordered for the event.
	 */
	private String orderQuantity;

	/**
	 * The unit of measurement for the ordered quantity.
	 */
	private String unit;

	/**
	 * The date of the order.
	 */
	private LocalDateTime orderDate;

	/**
	 * Display the decimal Digit Limit for Qty.
	 */
	private Integer decimalLimitQty;

	private Integer isPlate;

	private Integer orderType;

}