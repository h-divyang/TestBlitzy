package com.catering.dao.company_bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CompanyBankForReportDto;

/**
 * Data Access Object (DAO) interface for performing operations on the {@link CompanyBankNativeQuery} entity.
 * This interface extends {@link JpaRepository} and provides methods for executing native SQL queries 
 * related to company bank details.
 * 
 * The interface defines a method for retrieving company bank details, including account name, account number, 
 * branch name, and IFSC code. The results are mapped to a {@link CompanyBankForReportDto} object, which 
 * represents the bank details required for generating reports.
 * 
 * The {@link @Query} annotation is used to specify the native SQL query `findBankDetails` for fetching 
 * the data. The `langType` parameter allows for language-specific localization if needed for the report.
 * 
 * @see CompanyBankNativeQuery
 * @see CompanyBankForReportDto
 */
public interface CompanyBankNativeQueryDao extends JpaRepository<CompanyBankNativeQuery, Long> {

	/**
	 * Retrieves company bank details based on the specified language type.
	 * 
	 * @param langType The language type identifier for localization of the bank details.
	 * @return A {@link CompanyBankForReportDto} containing the company bank details.
	 */
	@Query(name = "findBankDetails", nativeQuery = true)
	CompanyBankForReportDto find(Integer langType);

}