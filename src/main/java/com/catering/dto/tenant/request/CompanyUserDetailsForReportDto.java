package com.catering.dto.tenant.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) class representing details of a company user for reporting purposes.
 *
 * <p>
 * This class includes two fields: {@code companyUserName} for the name of the company user
 * and {@code companyUserNumber} for the unique number associated with the company user.
 * </p>
 *
 * @author Krushali Talaviya
 * @since 22 January 2024
 */
@Getter
@Setter
@AllArgsConstructor
public class CompanyUserDetailsForReportDto {

	/**
	 * The name of the company user.
	 */
	private String companyUserName;

	/**
	 * The unique number associated with the company user.
	 */
	private String companyUserNumber;

}