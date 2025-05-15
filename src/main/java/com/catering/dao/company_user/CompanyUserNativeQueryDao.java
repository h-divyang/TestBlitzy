package com.catering.dao.company_user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CompanyUserDetailsForReportDto;

/**
 * Data Access Object (DAO) interface for executing native queries related to company user details.
 * Extends JpaRepository for basic CRUD operations on CompanyUserNativeQuery entities.
 *
 * <p>
 * This interface provides a method to find company admin details based on a specified language type using a named native query.
 * </p>
 *
 * @param <CompanyUserNativeQuery> The type of the entity for which the repository is to be used.
 * @param <Long> The type of the ID of the entity.
 * @param <CompanyUserDetailsForReportDto> The type of the DTO representing company user details for reporting.
 *
 * @see JpaRepository
 * @see Query
 * @see CompanyUserDetailsForReportDto
 * 
 * @author Krushali Talaviya
 * @since 22 January 2024
 */
public interface CompanyUserNativeQueryDao extends JpaRepository<CompanyUserNativeQuery, Long> {

	/**
	 * Finds company admin details based on the specified language type using a named native query.
	 *
	 * @param langType The language type identifier for localization.
	 * @return A {@link CompanyUserDetailsForReportDto} object containing the company admin details.
	 *
	 * @see CompanyUserDetailsForReportDto
	 * @see Query
	 */
	@Query(name = "findCompanyAdminDetails", nativeQuery = true)
	CompanyUserDetailsForReportDto find(Integer langType);

	/**
	 * Retrieves the details of the company admin in English from the database.
	 * This query fetches the company admin's information that is relevant for reporting purposes, 
	 * ensuring that the data is provided in English.
	 * 
	 * The returned {@link CompanyUserDetailsForReportDto} contains the relevant details of the admin 
	 * for report generation, such as their name, role, contact information, and any other required attributes.
	 * 
	 * @return {@link CompanyUserDetailsForReportDto} containing the company admin details in English.
	 */
	@Query(name = "findCompanyAdminDetailsInEnglish", nativeQuery = true)
	CompanyUserDetailsForReportDto findDataInEnglish();

}