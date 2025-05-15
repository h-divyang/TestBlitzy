package com.catering.dto.tenant.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * EventAgencyDistributionLabourChithhiReportDto.
 * This class represents a Data Transfer Object (DTO) for the Labour Chithhi Report in an event agency distribution.
 *
 * @author Krushali Talaviya
 * @since 2023-11-01
 */
@Getter
@Setter
@AllArgsConstructor
public class LabourAndAgencyLabourChithhiReportDto {

	private String venue;

	private LocalDateTime date;

	private String contactName;

	private String functionName;

	private String contactNumber;

	private String contactCategory;

	private String dateForRef;

	private String timeData;

	private Long quantity;

	private String managerName;

	private String managerNumber;

	private String note;

}