package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class representing the result of a Terms and Conditions report.
 *
 * <p>
 * This class includes a single field to store the content of the terms and conditions.
 * </p>
 *
 * @author Krushali Talaviya
 * @since 22 January 2024
 */
@Getter
@Setter
@AllArgsConstructor
public class TermsAndConditionsReportResultDto {

	/**
	 * The content of the terms and conditions.
	 */
	private String termsAndConditions;

}