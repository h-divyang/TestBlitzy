package com.catering.dao.company_bank;

import com.catering.dto.tenant.request.CompanyBankForReportDto;

/**
 * Service interface for retrieving company bank details.
 * This interface defines methods for accessing bank account information 
 * from the database using language-specific queries.
 * 
 * The method {@link #find(Integer langType)} retrieves company bank details,
 * including account name, account number, branch name, and IFSC code, based 
 * on the provided language type. The result is returned as a {@link CompanyBankForReportDto}.
 * 
 * The service abstracts the data retrieval logic and ensures that the correct
 * language-specific data is provided for reports and other business processes.
 * 
 * @see CompanyBankForReportDto
 */
public interface CompanyBankNativeQueryService {

	/**
	 * Retrieves company bank details based on the specified language type.
	 * 
	 * @param langType The language type identifier for localization of the bank details.
	 * @return A {@link CompanyBankForReportDto} containing the company bank details.
	 */
	CompanyBankForReportDto find(Integer langType);

}