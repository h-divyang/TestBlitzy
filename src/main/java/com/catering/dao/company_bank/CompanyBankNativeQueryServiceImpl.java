package com.catering.dao.company_bank;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.CompanyBankForReportDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link CompanyBankNativeQueryService} interface 
 * that handles the retrieval of company bank details from the database.
 * 
 * This service provides a method to fetch company bank information, including 
 * account name, account number, branch name, and IFSC code, based on a given 
 * language type. It interacts with the {@link CompanyBankNativeQueryDao} 
 * to execute the native SQL query and retrieve the data in the required format.
 * 
 * The {@link #find(Integer langType)} method queries the database using the 
 * provided language type and returns the relevant company bank details as 
 * a {@link CompanyBankForReportDto}.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyBankNativeQueryServiceImpl implements CompanyBankNativeQueryService {

	/**
	 * Data access object (DAO) for querying company bank details from the database.
	 * This DAO is responsible for executing the native SQL query to retrieve bank details
	 * like account name, account number, branch name, and IFSC code based on the 
	 * specified language type.
	 */
	CompanyBankNativeQueryDao companyBankNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompanyBankForReportDto find(Integer langType) {
		return companyBankNativeQueryDao.find(langType);
	}

}