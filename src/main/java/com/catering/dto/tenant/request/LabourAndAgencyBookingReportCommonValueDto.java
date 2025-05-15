package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing common values for a booking report in event agency distribution.
 *
 * This class encapsulates common information shared among booking reports, including party name,
 * party number, party address, and venue details.
 *
 * @author Krushali Talaviya
 * @since 2024-02-01
 */
@Getter
@Setter
@AllArgsConstructor
public class LabourAndAgencyBookingReportCommonValueDto {

	private String customerName;

	private String customerNumber;

	private String customerAddress;

	private String venue;

	private String customerHomeAddress;

}