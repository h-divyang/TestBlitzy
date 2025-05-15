package com.catering.dto.tenant.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a booking report for event agency distribution.
 *
 * This class encapsulates information about a booking, including function date, function name,
 * person details, final material name, contact category, and quantity.
 *
 * @author Krushali Talaviya
 * @since 2024-02-01
 */
@Getter
@Setter
@AllArgsConstructor
public class LabourAndAgencyBookingReportDto {

	/**
	 * The date of the event function.
	 */
	private LocalDate eventDate;

	private String venue;

	/**
	 * The name of the event function.
	 */
	private String functionName;

	/**
	 * The name of the final material used for the event.
	 */
	private String menuItemName;

	/**
	 * The category of contact related to the booking.
	 */
	private String contactCategory;

	/**
	 * The quantity or amount associated with the booking.
	 */
	private String quantity;

}