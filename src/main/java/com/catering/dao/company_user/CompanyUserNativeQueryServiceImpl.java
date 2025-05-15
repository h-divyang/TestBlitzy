package com.catering.dao.company_user;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catering.constant.ReportParameterConstants;
import com.catering.dto.tenant.request.CompanyUserDetailsForReportDto;
import com.catering.util.DataUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for retrieving company user details using native queries.
 *
 * <p>
 * This service implements methods to find company admin details based on a specified language type and set common data
 * in parameters for generating reports. It utilizes the CompanyUserNativeQueryDao for data access.
 * </p>
 *
 * @see Service
 * @see Autowired
 * @see CompanyUserNativeQueryService
 * @see CompanyUserNativeQueryDao
 * @see CompanyUserDetailsForReportDto
 * @see ReportParameterConstants
 * @see DataUtils
 * 
 * @author Krushali Talaviya
 * @since 22 January 2024
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyUserNativeQueryServiceImpl implements CompanyUserNativeQueryService {

	/**
	 * Data Access Object for executing native queries related to company user details.
	 */
	CompanyUserNativeQueryDao companyUserNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompanyUserDetailsForReportDto find(Integer langType) {
		return companyUserNativeQueryDao.find(langType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompanyUserDetailsForReportDto findDataInEnglish() {
		return companyUserNativeQueryDao.findDataInEnglish();
	}

	/**
	 * Sets common data related to the company user in the provided parameters map, 
	 * based on the specified language type.
	 * 
	 * The method retrieves the company user's details based on the provided language type 
	 * and adds the relevant information (company user name, mobile number) to the parameters map. 
	 * Additionally, it also retrieves the company user's details in English and adds the English name 
	 * to the parameters map if available.
	 * 
	 * This method is typically used to populate the parameters map for report generation, 
	 * ensuring that both localized and English versions of the company user information are included.
	 * 
	 * @param parameters The map of parameters to which common company user data will be added.
	 * @param langType The language type used to retrieve the company user's details in the desired language.
	 * @return The updated map of parameters containing the company user data.
	 */
	public Map<String, Object> setTheCommonDataInParameters(Map<String, Object> parameters, Integer langType) {
		CompanyUserDetailsForReportDto companyUserDetails = find(DataUtils.getLangType(langType));
		if (Objects.nonNull(companyUserDetails)) {
			parameters.put(ReportParameterConstants.COMPANY_USER_NAME, Objects.nonNull(companyUserDetails.getCompanyUserName()) ? companyUserDetails.getCompanyUserName() : "");
			parameters.put(ReportParameterConstants.COMPANY_USER_MOBILE_NUMBER, Objects.nonNull(companyUserDetails.getCompanyUserNumber()) ? companyUserDetails.getCompanyUserNumber() : "");
		}
		CompanyUserDetailsForReportDto companyUserDetailsInEnglish = findDataInEnglish();
		if (Objects.nonNull(companyUserDetails)) {
			parameters.put(ReportParameterConstants.COMPANY_USER_NAME_IN_ENGLISH, companyUserDetailsInEnglish.getCompanyUserName());
		}
		return parameters;
	}

}