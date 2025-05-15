package com.catering.dao.order_reports.admin_reports;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.catering.bean.FileBean;
import com.catering.config.TimeZoneInitializer;
import com.catering.constant.Constants;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryService;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryServiceImpl;
import com.catering.dao.order.BookOrderNativeQueryDao;
import com.catering.dao.order_reports.menu_preparation.MenuPreparationReportQueryService;
import com.catering.dto.tenant.request.AddressChitthiReportDto;
import com.catering.dto.tenant.request.AdminFeedBackReportDto;
import com.catering.dto.tenant.request.AdminReportCounterNamePlateReportDto;
import com.catering.dto.tenant.request.AdminReportTwoLanguageCounterNamePlateReportDto;
import com.catering.dto.tenant.request.AdminSupplierDetailsReportDto;
import com.catering.dto.tenant.request.AdminWastageReportDto;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.CustomerFormatReportDto;
import com.catering.dto.tenant.request.DishCostingReportDto;
import com.catering.dto.tenant.request.TotalDishCostingReportDto;
import com.catering.repository.tenant.BookOrderRepository;
import com.catering.service.common.JasperReportService;
import com.catering.service.tenant.CompanySettingService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.ResourceUtils;
import com.catering.util.StringUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for generating various admin reports based on specific order details and localization options.
 *
 * <p>
 * This service implements methods to generate wastage reports, feedback reports, and supplier details reports for a
 * given order ID, language type, language code, and current date. It utilizes the AdminReportQueryDao for data access
 * and the JasperReportService for generating JasperReports.
 * </p>
 * 
 * @author Krushali Talaviya
 * @since 1 January 2024
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminReportQueryServiceImpl implements AdminReportQueryService {

	/**
	 * DAO for executing queries related to administrative reports.
	 */
	AdminReportQueryDao adminReportQueryDao;

	/**
	 * Service for generating reports using JasperReports.
	 */
	JasperReportService jasperReportService;

	/**
	 * Implementation of the service handling native queries for company preferences.
	 */
	CompanyPreferencesNativeQueryServiceImpl companyPreferencesNativeQueryServiceImpl;

	/**
	 * Service for handling queries related to menu preparation reports.
	 */
	MenuPreparationReportQueryService menuPreparationReportQueryService;

	/**
	 * DAO for executing native queries related to book orders.
	 */
	BookOrderNativeQueryDao bookOrderNativeQueryDao;

	/** 
	 * Service for executing custom native queries and managing company preferences data.
	 */
	CompanyPreferencesNativeQueryService companyPreferencesNativeQueryService;

	/**
	 * Service for managing company-specific settings and configurations.
	 */
	CompanySettingService companySettingService;

	/**
	 * Utility for initializing and managing time zone configurations.
	 */
	TimeZoneInitializer timeZoneInitializer;

	/**
	 * Repository instance for handling database operations related to booking orders.
	 */
	BookOrderRepository bookOrderRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateWastageReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AdminReport.WastageReport.class, StringUtils.dotSeparated(JasperParameterConstants.ADMIN_REPORT, JasperParameterConstants.AdminReport.WASTAGE_REPORT));
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<AdminWastageReportDto> adminWastageReport = adminReportQueryDao.generateWastageReport(orderId, DataUtils.getLangType(langType));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_REPORT_WASTAGE_REPORT, reportName);
		return jasperReportService.generatePdfReport(adminWastageReport, parameters, JasperReportNameConstant.ADMIN_REPORT_WASTAGE_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDishCountingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AdminReport.DishCountingReport.class, StringUtils.dotSeparated(JasperParameterConstants.ADMIN_REPORT, JasperParameterConstants.AdminReport.DISH_COUNTING_REPORT));
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<AdminFeedBackReportDto> adminDishCountingReport = adminReportQueryDao.generateDishCountingReport(orderId, parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], DataUtils.getLangType(langType), timeZoneInitializer.getTimeZone());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_REPORT_DISH_COUNTING_REPORT, reportName);
		return jasperReportService.generatePdfReport(adminDishCountingReport, parameters, JasperReportNameConstant.ADMIN_REPORT_DISH_COUNTING_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateSupplierDetailsReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AdminReport.SupplierDetailsReport.class, StringUtils.dotSeparated(JasperParameterConstants.ADMIN_REPORT, JasperParameterConstants.AdminReport.SUPPLIER_DETAILS_REPORT));
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<AdminSupplierDetailsReportDto> adminSupplierDetailsReport = adminReportQueryDao.generateSupplierDetailsReport(orderId, DataUtils.getLangType(langType));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_REPORT_SUPPLIER_DETAILS_REPORT, reportName);
		return jasperReportService.generatePdfReport(adminSupplierDetailsReport, parameters, JasperReportNameConstant.ADMIN_REPORT_SUPPLIER_DETAILS_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDishCostingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AdminReport.DishCostingReport.class, StringUtils.dotSeparated(JasperParameterConstants.ADMIN_REPORT, JasperParameterConstants.AdminReport.DISH_COSTING_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setDecimalPatterns(parameters);
		List<DishCostingReportDto> dishCostingDtos = bookOrderNativeQueryDao.generateDishCostingReport(orderId, DataUtils.getLangType(langType), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_REPORT_DISH_COSTING_REPORT, reportName);
		return jasperReportService.generatePdfReport(dishCostingDtos, parameters, JasperReportNameConstant.ADMIN_REPORT_DISH_COSTING_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateTotalDishCostingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AdminReport.TotalDishCostingReport.class, StringUtils.dotSeparated(JasperParameterConstants.ADMIN_REPORT, JasperParameterConstants.AdminReport.TOTAL_DISH_COSTING_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<TotalDishCostingReportDto> totalDishCostingDtos = bookOrderNativeQueryDao.generateTotalDishCostingReport(orderId, DataUtils.getLangType(langType), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_REPORT_TOTAL_DISH_COSTING_REPORT, reportName);
		return jasperReportService.generatePdfReport(totalDishCostingDtos, parameters, JasperReportNameConstant.ADMIN_REPORT_TOTAL_DISH_COSTING_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCustomerExtraCostReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AdminReport.CustomerFormatReport.class, StringUtils.dotSeparated(JasperParameterConstants.ADMIN_REPORT, JasperParameterConstants.AdminReport.CUSTOMER_EXTRA_COST_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setBackgroundImageInReport(parameters);
		List<CustomerFormatReportDto> customerFormatReport = adminReportQueryDao.generateCustomerExtraCostReport(orderId, DataUtils.getLangType(langType));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_CUSTOMER_EXTRA_COST_REPORT, reportName);
		return jasperReportService.generatePdfReport(customerFormatReport, parameters, JasperReportNameConstant.ADMIN_CUSTOMER_EXTRA_COST_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generatePartyComplainReport(Long orderId, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AdminReport.PartyComplainReport.class, StringUtils.dotSeparated(JasperParameterConstants.ADMIN_REPORT, JasperParameterConstants.AdminReport.PARTY_COMPLAIN_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_REPORT_PARTY_COMPLAIN_REPORT, reportName);
		return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.ADMIN_REPORT_PARTY_COMPLAIN_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCounterNamePlateReport(Long orderId, Integer langType, Byte reportSize, String reportName) {
		List<AdminReportCounterNamePlateReportDto> menuExecutionNamePlateReport = adminReportQueryDao.generateCounterNamePlateReport(orderId, DataUtils.getLangType(langType));
		Map<String, Object> parameters = new HashMap<>();
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		parameters.put(ReportParameterConstants.BG_COLOUR, companySettingDto.getBackgroundColour());
		parameters.put(ReportParameterConstants.FONT_COLOUR, companySettingDto.getFontColour());
		if (reportSize == Constants.REPORT_SIZE_12) {
			jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_REPORT_NAME_PLATE_REPORT_12, reportName);
			return jasperReportService.generatePdfReport(menuExecutionNamePlateReport, parameters, JasperReportNameConstant.ADMIN_REPORT_NAME_PLATE_REPORT_12);
		} else if (reportSize == Constants.REPORT_SIZE_16) {
			jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_REPORT_NAME_PLATE_REPORT_16, reportName);
			return jasperReportService.generatePdfReport(menuExecutionNamePlateReport, parameters, JasperReportNameConstant.ADMIN_REPORT_NAME_PLATE_REPORT_16);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCounterNamePlateDocReport(Long orderId, Integer langType, Byte reportSize) {
		List<AdminReportCounterNamePlateReportDto> menuExecutionNamePlateReport = adminReportQueryDao.generateCounterNamePlateReport(orderId, DataUtils.getLangType(langType));
		Map<String, Object> parameters = new HashMap<>();
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		parameters.put(ReportParameterConstants.BG_COLOUR, companySettingDto.getBackgroundColour());
		parameters.put(ReportParameterConstants.FONT_COLOUR, companySettingDto.getFontColour());
		if (reportSize == Constants.REPORT_SIZE_12) {
			return jasperReportService.generateDocReport(menuExecutionNamePlateReport, parameters, JasperReportNameConstant.ADMIN_REPORT_NAME_PLATE_REPORT_12);
		} else if (reportSize == Constants.REPORT_SIZE_16) {
			return jasperReportService.generateDocReport(menuExecutionNamePlateReport, parameters, JasperReportNameConstant.ADMIN_REPORT_NAME_PLATE_REPORT_16);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateTwoLanguageCounterNamePlateReport(Long orderId, Byte reportSize, String reportName, String defaultLang, String preferLang) {
		Map<String, Object> parameters = new HashMap<>();
		setDefaultAndPreferLang(defaultLang, preferLang, parameters);
		if (reportSize == Constants.REPORT_SIZE_12) {
			jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_REPORT_TWO_LANGUAGE_NAME_PLATE_REPORT_12, reportName);
			return jasperReportService.generatePdfReport(getNamePlateReportData(orderId, defaultLang, preferLang), parameters, JasperReportNameConstant.ADMIN_REPORT_TWO_LANGUAGE_NAME_PLATE_REPORT_12);
		} else if (reportSize == Constants.REPORT_SIZE_16) {
			jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADMIN_REPORT_TWO_LANGUAGE_NAME_PLATE_REPORT_16, reportName);
			return jasperReportService.generatePdfReport(getNamePlateReportData(orderId, defaultLang, preferLang), parameters, JasperReportNameConstant.ADMIN_REPORT_TWO_LANGUAGE_NAME_PLATE_REPORT_16);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateTwoLanguageCounterNamePlateDocReport(Long orderId, Byte reportSize, String defaultLang, String preferLang) {
		Map<String, Object> parameters = new HashMap<>();
		setDefaultAndPreferLang(defaultLang, preferLang, parameters);
		if (reportSize == Constants.REPORT_SIZE_12) {
			return jasperReportService.generateDocReport(getNamePlateReportData(orderId, defaultLang, preferLang), parameters, JasperReportNameConstant.ADMIN_REPORT_TWO_LANGUAGE_NAME_PLATE_REPORT_12);
		} else if (reportSize == Constants.REPORT_SIZE_16) {
			return jasperReportService.generateDocReport(getNamePlateReportData(orderId, defaultLang, preferLang), parameters, JasperReportNameConstant.ADMIN_REPORT_TWO_LANGUAGE_NAME_PLATE_REPORT_16);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateAddressChitthiReport(Long orderId, Long[] godownId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AdminReport.WastageReport.class, StringUtils.dotSeparated(JasperParameterConstants.ADMIN_REPORT, JasperParameterConstants.AdminReport.WASTAGE_REPORT));
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<Long> godownIds = Arrays.asList(godownId);
		List<AddressChitthiReportDto> addressChitthiReport = adminReportQueryDao.generateAddressChitthiReport(orderId, godownIds, DataUtils.getLangType(langType));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ADDRESS_CHITHI_REPORT, reportName);
		return jasperReportService.generatePdfReport(addressChitthiReport, parameters, JasperReportNameConstant.ADDRESS_CHITHI_REPORT);
	}

	/**
	 * Sets the default and preferred language codes based on the company preferences.
	 * If the provided languages match the company's default or preferred languages, 
	 * they are converted to corresponding numeric codes: "0" for default, "1" for preferred, and "2" for others.
	 * Also, it retrieves and sets the background and font colors from the company settings in the provided parameters map.
	 *
	 * @param defaultLang the default language code to be validated and modified
	 * @param preferLang  the preferred language code to be validated and modified
	 * @param parameters  the map where additional report parameters such as colors are stored
	 */
	private void setDefaultAndPreferLang(String defaultLang, String preferLang, Map<String, Object> parameters) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		parameters.put(ReportParameterConstants.BG_COLOUR, companySettingDto.getBackgroundColour());
		parameters.put(ReportParameterConstants.FONT_COLOUR, companySettingDto.getFontColour());
	}

	/**
	 * Fetches the data for a two-language counter nameplate report.
	 *
	 * @param orderId     the ID of the order for which the report data is retrieved
	 * @param defaultLang the default language code for the report data
	 * @param preferLang  the preferred language code for the report data
	 * @return a list of {@link AdminReportTwoLanguageCounterNamePlateReportDto} containing the report data
	 */
	private List<AdminReportTwoLanguageCounterNamePlateReportDto> getNamePlateReportData(Long orderId, String defaultLang, String preferLang) {
		return adminReportQueryDao.generateTwoLanguageCounterNamePlateReport(orderId, defaultLang, preferLang);
	}
}