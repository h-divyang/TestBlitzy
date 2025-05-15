package com.catering.dao.company_preferences;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CompanyDetailsForReportDto;
import com.catering.dto.tenant.request.CompanyPreferencesForReportDto;
import com.catering.dto.tenant.request.TermsAndConditionsReportResultDto;

/**
 * DAO interface for accessing company preferences data from the database using native SQL queries.
 * This interface extends `JpaRepository` to provide CRUD operations on the `CompanyPreferencesNativeQuery` entity.
 * The interface includes methods to retrieve company details, terms and conditions, and preferences
 * based on the specified language type. It uses named native queries to fetch the relevant data and map 
 * the results to the corresponding DTOs.
 */
public interface CompanyPreferencesNativeQueryDao extends JpaRepository<CompanyPreferencesNativeQuery, Long> {

	/**
	 * Retrieves the company preferences based on the specified language type.
	 * This query uses a named native query (`find`) to fetch data such as the company name, address,
	 * email, mobile number, GST number, invoice address, office number, and website, with language-specific content 
	 * (e.g., preferred, supportive, or default language).
	 *
	 * @param langType The language type (1 for preferred language, 2 for supportive language).
	 * @return A `CompanyPreferencesForReportDto` object containing the company preferences data.
	 */
	@Query(name = "find", nativeQuery = true)
	CompanyPreferencesForReportDto find(Integer langType);

	/**
	 * Retrieves the company details (name and address) in English.
	 * This query uses a named native query (`findCompanyDetailsInEnglish`) to fetch the company name 
	 * and address in the preferred language, or falls back to the default if no preferred language is available.
	 *
	 * @return A `CompanyDetailsForReportDto` object containing the company name and address.
	 */
	@Query(name = "findCompanyDetailsInEnglish", nativeQuery = true)
	CompanyDetailsForReportDto findCompanyDetailsInEnglish();

	/**
	 * Retrieves the terms and conditions based on the specified language type.
	 * This query uses a named native query (`findTermsAndConditions`) to fetch the content of the terms and conditions
	 * in the preferred or supportive language, or falls back to the default language if neither is available.
	 *
	 * @param langType The language type (1 for preferred language, 2 for supportive language).
	 * @return A `TermsAndConditionsReportResultDto` object containing the terms and conditions.
	 */
	@Query(name = "findTermsAndConditions", nativeQuery = true)
	TermsAndConditionsReportResultDto findTermsAndConditions(Integer langType);

}