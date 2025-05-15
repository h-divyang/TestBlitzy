package com.catering.dao.company_user;

import java.util.Map;

import com.catering.constant.ReportParameterConstants;
import com.catering.dto.tenant.request.CompanyUserDetailsForReportDto;
import com.catering.util.DataUtils;

/**
 * Service interface for retrieving company user details using native queries.
 *
 * <p>
 * This service provides a method to find company admin details based on a specified language type.
 * </p>
 *
 * @param <CompanyUserDetailsForReportDto> The type of the DTO representing company user details for reporting.
 *
 * @author Krushali Talaviya
 * @since 22 January 2024
 */
public interface CompanyUserNativeQueryService {

	/**
	 * Finds company admin details based on the specified language type.
	 *
	 * @param langType The language type identifier for localization.
	 * @return A {@link CompanyUserDetailsForReportDto} object containing the company admin details.
	 *
	 * @see CompanyUserDetailsForReportDto
	 */
	CompanyUserDetailsForReportDto find(Integer langType);

	/**
	 * Finds company admin details in english.
	 *
	 * @return A {@link CompanyUserDetailsForReportDto} object containing the company admin details.
	 *
	 * @see CompanyUserDetailsForReportDto
	 */
	CompanyUserDetailsForReportDto findDataInEnglish();

	/**
	 * Sets common data in the provided parameters map for generating reports.<br>
	 * Retrieves company user details based on the specified language type and populates relevant parameters with the retrieved values.
	 *
	 * @param parameters A map containing parameters for generating a report.
	 * @param langType   The language type identifier for localization.
	 * @return The modified parameters map with common data values added, or the original map if no details are found.
	 *
	 * @see CompanyUserDetailsForReportDto
	 * @see ReportParameterConstants
	 * @see DataUtils
	 */
	Map<String, Object> setTheCommonDataInParameters(Map<String, Object> parameters, Integer langType);

}