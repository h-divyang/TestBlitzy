package com.catering.dao.company_preferences;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.catering.constant.ReportParameterConstants;
import com.catering.dto.tenant.request.AboutUsDto;
import com.catering.dto.tenant.request.CompanyDetailsForReportDto;
import com.catering.dto.tenant.request.CompanyPreferencesForReportDto;
import com.catering.dto.tenant.request.ReportCompanyDetailRightsDto;
import com.catering.dto.tenant.request.TermsAndConditionsReportResultDto;
import com.catering.model.tenant.AboutUsModel;
import com.catering.repository.tenant.AboutUsRepository;
import com.catering.service.common.ModelMapperService;
import com.catering.service.tenant.ReportCompanyDetailRightsService;
import com.catering.util.DataUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link CompanyPreferencesNativeQueryService} interface for retrieving company preferences,
 * terms and conditions, and related data required for generating reports. This service handles the business logic
 * for fetching localized company preferences (name, address, GST number, etc.), terms and conditions, and other
 * necessary data based on the specified language type. The data is used for generating reports in different languages.
 * 
 * The service utilizes the {@link CompanyPreferencesNativeQueryDao} to access the underlying data source, 
 * the {@link AboutUsRepository} for fetching the 'About Us' information, and the {@link ModelMapperService} to 
 * map the data from database entities to report-specific DTOs.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyPreferencesNativeQueryServiceImpl implements CompanyPreferencesNativeQueryService {

	/**
	 * Data Access Object (DAO) for querying company preferences from the database.
	 */
	CompanyPreferencesNativeQueryDao companyPreferencesNativeQueryDao;

	/**
	 * Repository for fetching 'About Us' information for the company.
	 */
	AboutUsRepository aboutUsRepository;

	/**
	 * Service for mapping data between entities and DTOs.
	 */
	ModelMapperService modelMapperService;

	ReportCompanyDetailRightsService reportCompanyDetailRightsService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompanyPreferencesForReportDto find(Integer langType) {
		return companyPreferencesNativeQueryDao.find(langType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TermsAndConditionsReportResultDto findTermsAndConditions(Integer langType) {
		return companyPreferencesNativeQueryDao.findTermsAndConditions(langType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompanyDetailsForReportDto findCompanyDetailsInEnglish() {
		return companyPreferencesNativeQueryDao.findCompanyDetailsInEnglish();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> setTheCommonDataInParameters(Map<String, Object> parameters, Integer langType, String reportName) {
		CompanyPreferencesForReportDto companyPreferences = find(DataUtils.getLangType(langType));
		ReportCompanyDetailRightsDto reportCompanyDetailRightsDto = reportCompanyDetailRightsService.getReportCompanyDetailRightsByReportName(reportName);
		if (Objects.nonNull(companyPreferences)) {
			setCompanyParameters(companyPreferences, parameters, reportCompanyDetailRightsDto);
		}
		CompanyDetailsForReportDto companyDetailsForReportDto = findCompanyDetailsInEnglish();
		if (Objects.nonNull(companyDetailsForReportDto)) {
			if (Boolean.TRUE.equals(reportCompanyDetailRightsDto.getCompanyName())) {
				parameters.put(ReportParameterConstants.COMPANY_NAME_VALUE_IN_ENGLISH, companyDetailsForReportDto.getCompanyName());
			}
			if (Boolean.TRUE.equals(reportCompanyDetailRightsDto.getCompanyAddress())) {
				parameters.put(ReportParameterConstants.COMPANY_ADDRESS_VALUE_IN_ENGLISH, companyDetailsForReportDto.getCompanyAddress());
			}
		}
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> setTermsAndConditionsInParameter(Map<String, Object> parameters, Integer langType, String reportName) {
		ReportCompanyDetailRightsDto reportCompanyDetailRightsDto = reportCompanyDetailRightsService.getReportCompanyDetailRightsByReportName(reportName);
		if (Boolean.TRUE.equals(reportCompanyDetailRightsDto.getTermsAndConditions())) {
			TermsAndConditionsReportResultDto termsAndConditionsReport = findTermsAndConditions(langType);
			if(Objects.nonNull(termsAndConditionsReport)) {
				parameters.put(ReportParameterConstants.TERMS_AND_CONDITIONS, Objects.nonNull(termsAndConditionsReport.getTermsAndConditions()) ? termsAndConditionsReport.getTermsAndConditions() : "");
			}	
		}
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> setAboutUsInParameter(Map<String, Object> parameters, Integer langType) {
		List<AboutUsModel> aboutUsModel=  aboutUsRepository.findAll();
		if (!aboutUsModel.isEmpty()) {
			AboutUsDto aboutUsDto = modelMapperService.convertEntityAndDto(aboutUsModel.get(0), AboutUsDto.class);
			setAboutUsData(parameters, aboutUsDto, langType);
		}
		return parameters;
	}

	/**
	 * Sets the company-related parameters in the provided map for report generation.
	 * This method populates the map with values from the given company preferences object.
	 * If any of the fields are null, an empty string is used as the default.
	 *
	 * @param companyPreferences The company preferences object containing the details to be included in the report.
	 * @param parameters A map to hold the parameters that will be passed to the report generation engine.
	 */
	private void setCompanyParameters(CompanyPreferencesForReportDto companyPreferences, Map<String, Object> parameters, ReportCompanyDetailRightsDto reportRightsDto) {
			if (Boolean.TRUE.equals(reportRightsDto.getCompanyName())) {
				parameters.put(ReportParameterConstants.COMPANY_NAME_VALUE, Objects.nonNull(companyPreferences.getName()) ? companyPreferences.getName() : "");
			}
			if (Boolean.TRUE.equals(reportRightsDto.getCompanyAddress())) {
				parameters.put(ReportParameterConstants.COMPANY_ADDRESS_VALUE, Objects.nonNull(companyPreferences.getAddress()) ? companyPreferences.getAddress() : "");
			}
			if(Boolean.TRUE.equals(reportRightsDto.getCompanyEmail())) {
				parameters.put(ReportParameterConstants.COMPANY_EMAIL_VALUE, Objects.nonNull(companyPreferences.getEmail()) ? companyPreferences.getEmail() : "");
			}
			if (Boolean.TRUE.equals(reportRightsDto.getCompanyMobileNumber())) {
				parameters.put(ReportParameterConstants.COMPANY_MOBILE_NUMBER_VALUE, Objects.nonNull(companyPreferences.getMobileNumber()) ? companyPreferences.getMobileNumber() : "");
				parameters.put(ReportParameterConstants.COMPANY_OFFICE_NUMBER_VALUE, Objects.nonNull(companyPreferences.getOfficeNumber()) ? companyPreferences.getOfficeNumber() : "");
			}
		parameters.put(ReportParameterConstants.COMPANY_GST_NUMBER, Objects.nonNull(companyPreferences.getGstNo()) ? companyPreferences.getGstNo() : "");
		parameters.put(ReportParameterConstants.COMPANY_OFFICE_NUMBER,  Objects.nonNull(companyPreferences.getOfficeNumber()) ? companyPreferences.getOfficeNumber() : "");
		parameters.put(ReportParameterConstants.COMPANY_WEBSITE,  Objects.nonNull(companyPreferences.getWebsite()) ? companyPreferences.getWebsite() : "");
		parameters.put(ReportParameterConstants.COMPANY_INVOICE_ADDRESS_VALUE, companyPreferences.getInvoiceAddress());
	}

	/**
	 * Sets the "About Us" section data for the report based on the provided language type.
	 * Depending on the language type, the relevant "About Us" description is placed in the parameters map.
	 * 
	 * @param parameters A map to hold the parameters that will be passed to the report generation engine.
	 * @param aboutUsDto The "About Us" data object containing the descriptions in different languages.
	 * @param langType The language type (1 for preferred language, 2 for supportive language, and other values for default language).
	 */
	private void setAboutUsData(Map<String, Object> parameters, AboutUsDto aboutUsDto, Integer langType) {
		if (Objects.nonNull(aboutUsDto)) {
			if (langType == 1) {
				parameters.put(ReportParameterConstants.ABOUT_US, Objects.nonNull(aboutUsDto.getAboutUsPreferLang()) ? aboutUsDto.getAboutUsPreferLang() : "");
			} else if (langType == 2) {
				parameters.put(ReportParameterConstants.ABOUT_US, Objects.nonNull(aboutUsDto.getAboutUsSupportiveLang()) ? aboutUsDto.getAboutUsSupportiveLang() : "");
			} else {
				parameters.put(ReportParameterConstants.ABOUT_US, Objects.nonNull(aboutUsDto.getAboutUsDefaultLang()) ? aboutUsDto.getAboutUsDefaultLang() : "");
			}
		}
	}

}