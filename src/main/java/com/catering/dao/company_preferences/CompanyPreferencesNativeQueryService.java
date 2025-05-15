package com.catering.dao.company_preferences;

import java.util.Map;

import com.catering.dto.tenant.request.CompanyDetailsForReportDto;
import com.catering.dto.tenant.request.CompanyPreferencesForReportDto;
import com.catering.dto.tenant.request.TermsAndConditionsReportResultDto;

/**
 * Service interface for accessing company preferences and related data for generating reports.
 * This interface defines methods to retrieve company details, terms and conditions, and other preferences
 * based on the specified language type. It also includes methods for setting common report parameters, 
 * terms and conditions, and additional data in the report parameters map for localized reports.
 */
public interface CompanyPreferencesNativeQueryService {

	/**
	 * Retrieves company preferences for generating reports based on the specified language type.
	 *
	 * @param langType The language type identifier for localization.
	 * @return A {@link CompanyPreferencesForReportDto} object containing the company preferences for reports.
	 */
	CompanyPreferencesForReportDto find(Integer langType);

	/**
	 * Retrieves company preferences for generating reports in english.
	 *
	 * @param langType The language type identifier for localization.
	 * @return A {@link CompanyDetailsForReportDto} object containing the company preferences for reports.
	 */
	CompanyDetailsForReportDto findCompanyDetailsInEnglish();

	/**
	 * Retrieves terms and conditions for generating reports based on the specified language type.
	 *
	 * @param langType The language type identifier for localization.
	 * @return A {@link TermsAndConditionsReportResultDto} object containing the terms and conditions for reports.
	 */
	TermsAndConditionsReportResultDto findTermsAndConditions(Integer langType);

	/**
	 * Sets common data in the report parameters map, such as company user name, mobile number, and email,
	 * based on the specified language type.
	 * This data is often used in report headers and other sections for localization.
	 *
	 * @param parameters The map containing report parameters.
	 * @param langType   The language type identifier for localization.
	 * @return The updated parameters map with the common data added.
	 */
	Map<String, Object> setTheCommonDataInParameters(Map<String, Object> parameters, Integer langType, String reportName);

	/**
	 * Sets the terms and conditions data in the report parameters map based on the specified language type.
	 * The terms and conditions are fetched in the appropriate language (preferred, supportive, or default).
	 *
	 * @param parameters The map containing report parameters.
	 * @param langType   The language type identifier for localization.
	 * @return The updated parameters map with the terms and conditions added.
	 */
	Map<String, Object> setTermsAndConditionsInParameter(Map<String, Object> parameters, Integer langType, String reportName);

	/**
	 * Sets the 'About Us' data in the report parameters map based on the specified language type.
	 * The 'About Us' data is fetched in the appropriate language for localization.
	 *
	 * @param parameters The map containing report parameters.
	 * @param langType   The language type identifier for localization.
	 * @return The updated parameters map with the 'About Us' data added.
	 */
	Map<String, Object> setAboutUsInParameter(Map<String, Object> parameters, Integer langType);

}