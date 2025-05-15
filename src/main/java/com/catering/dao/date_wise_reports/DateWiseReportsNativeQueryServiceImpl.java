package com.catering.dao.date_wise_reports;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.catering.bean.FileBean;
import com.catering.config.TimeZoneInitializer;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryServiceImpl;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.DateWiseChefLabourReportDto;
import com.catering.dto.tenant.request.DateWiseLabourReoportDto;
import com.catering.dto.tenant.request.DateWiseMenuItemReportDto;
import com.catering.dto.tenant.request.DateWiseOrderBookingReportDto;
import com.catering.dto.tenant.request.DateWiseOutsideOrderReportDto;
import com.catering.dto.tenant.request.DateWiseRawMaterialReportDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
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
 * Implementation of the {@link DateWiseReportsNativeQueryService} interface that provides the logic 
 * for generating various date-wise reports and fetching dropdown data used for filtering reports. 
 * This service relies on multiple dependencies for interacting with the data layer, generating reports, 
 * and handling company-specific settings.
 * 
 * The methods in this class generate reports related to raw materials, outside orders, labour, menu items, 
 * and bookings, based on date ranges and various filter criteria such as supplier categories, menu item 
 * categories, statuses, and language preferences. It utilizes the {@link DateWiseReportsNativeQueryDao} 
 * for data fetching and passes the results to the {@link JasperReportService} for report generation.
 * 
 * Key components and their responsibilities:
 * - {@link DateWiseReportsNativeQueryDao}: Interacts with the database to fetch dropdown data and 
 *   execute native queries for report generation.
 * - {@link CompanyPreferencesNativeQueryServiceImpl}: Provides company-specific settings related to reports.
 * - {@link JasperReportService}: Generates JasperReports based on fetched data.
 * - {@link CompanySettingService}: Handles company-level settings that may affect report generation.
 * - {@link TimeZoneInitializer}: Manages time zone adjustments during report generation.
 * 
 * This service supports multi-filter criteria and localization for report generation, ensuring that the reports 
 * are customized to the specific needs of the user and the company.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DateWiseReportsNativeQueryServiceImpl implements DateWiseReportsNativeQueryService {

	/**
	 * Data access object for executing native queries related to date-wise reports.
	 * This object is responsible for fetching the raw data required to generate the reports, 
	 * including supplier categories, menu items, raw materials, etc.
	 */
	DateWiseReportsNativeQueryDao dateWiseReportsNativeQueryDao;

	/**
	 * Service that provides company-specific preferences and settings required for report generation.
	 * This service helps to ensure that reports are generated according to the company's configurations, 
	 * such as default language, currency, and other company-specific rules.
	 */
	CompanyPreferencesNativeQueryServiceImpl companyPreferencesNativeQueryServiceImpl;

	/**
	 * Service responsible for generating and exporting reports using the JasperReports library.
	 * This service takes care of compiling and filling the reports based on the fetched data and preferences 
	 * from the other services, producing the final report files.
	 */
	JasperReportService jasperReportService;

	/**
	 * Service that manages time zone-related adjustments in report generation.
	 * It ensures that the reports are generated with accurate time zone data, particularly when dealing with 
	 * international data or users from different time zones.
	 */
	TimeZoneInitializer timeZoneInitializer;

	/**
	 * The service to fetch company settings.
	 */
	CompanySettingService companySettingService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getSupplierCategoryDropDownData(Long contactCategoryTypeId) {
		return dateWiseReportsNativeQueryDao.getSupplierCategoryDropDownData(contactCategoryTypeId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getSupplierNameDropDownData(Long contactCategoryTypeId, Long supplierCategoryId) {
		return dateWiseReportsNativeQueryDao.getSupplierNameDropDownData(contactCategoryTypeId, supplierCategoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getMenuItemCategoryDropDownData() {
		return dateWiseReportsNativeQueryDao.getMenuItemCategoryDropDownData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getSupplierCategoryDropDownDataOfRawMaterial() {
		return dateWiseReportsNativeQueryDao.getSupplierCategoryDropDownDataOfRawMaterial();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getSupplierForRawMaterial(Long supplierCategoryId) {
		return dateWiseReportsNativeQueryDao.getSupplierDropDownDataOfRawMaterial(supplierCategoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getMenuItemSubCategoryDropDownData() {
		return dateWiseReportsNativeQueryDao.getMenuItemSubCategoryDropDownData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getKitchenAreaDropDownData() {
		return dateWiseReportsNativeQueryDao.getKitchenAreaDropDownData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getCustomerContactsDropDownData(Long customerContactId) {
		return dateWiseReportsNativeQueryDao.getCustomerContactsDropDownData(customerContactId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getContactNameDropDownData(Long[] statusId) {
		List<Long> statusIds = Arrays.asList(statusId);
		return dateWiseReportsNativeQueryDao.getContactNameDropDownData(statusIds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseOutsideOrderReportWithPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] categoryId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.DateWiseReports.OutSideLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.DATE_WISE_REPORT, JasperParameterConstants.DateWiseReports.DATE_WISE_OUTSIDE_LABOUR_REPORT));
		setCommomParametersInDateWiseReports(parameters, reportName, langType, request);
		jasperReportService.setDecimalPatterns(parameters);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<Long> supplierCategoryIds = Arrays.asList(supplierCategoryId);
		List<Long> supplierNameIds = Arrays.asList(supplierNameId);
		List<Long> categoryIds = Arrays.asList(categoryId);
		List<Long> statusIds = Arrays.asList(statusId);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<DateWiseOutsideOrderReportDto> dateWiseReportDateWiseOutsideOrderReportWithPrice = dateWiseReportsNativeQueryDao.generateDateWiseOutsideOrderReport(startDate, endDate, supplierCategoryIds, supplierNameIds, categoryIds, statusIds, parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone(), langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_OUTSIDE_ORDER_REPORT_WITH_PRICE, reportName);
		return jasperReportService.generatePdfReport(dateWiseReportDateWiseOutsideOrderReportWithPrice, parameters, JasperReportNameConstant.DATE_WISE_OUTSIDE_ORDER_REPORT_WITH_PRICE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseOutsideOrderReportWithoutPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] categoryId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.DateWiseReports.OutSideLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.DATE_WISE_REPORT, JasperParameterConstants.DateWiseReports.DATE_WISE_OUTSIDE_LABOUR_REPORT));
		setCommomParametersInDateWiseReports(parameters, reportName, langType, request);
		jasperReportService.setDecimalPatterns(parameters);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<Long> supplierCategoryIds = Arrays.asList(supplierCategoryId);
		List<Long> supplierNameIds = Arrays.asList(supplierNameId);
		List<Long> categoryIds = Arrays.asList(categoryId);
		List<Long> statusIds = Arrays.asList(statusId);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<DateWiseOutsideOrderReportDto> dateWiseReportDateWiseOutsideOrderReportWithoutPrice = dateWiseReportsNativeQueryDao.generateDateWiseOutsideOrderReport(startDate, endDate, supplierCategoryIds, supplierNameIds, categoryIds, statusIds, parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone(), langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_OUTSIDE_ORDER_REPORT_WITHOUT_PRICE, reportName);
		return jasperReportService.generatePdfReport(dateWiseReportDateWiseOutsideOrderReportWithoutPrice, parameters, JasperReportNameConstant.DATE_WISE_OUTSIDE_ORDER_REPORT_WITHOUT_PRICE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseRawMaterialReportWithPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] rawMaterialCategoryId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.DateWiseReports.OutSideLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.DATE_WISE_REPORT, JasperParameterConstants.DateWiseReports.DATE_WISE_OUTSIDE_LABOUR_REPORT));
		setCommomParametersInDateWiseReports(parameters, reportName, langType, request);
		jasperReportService.setDecimalPatterns(parameters);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<Long> supplierCategoryIds = Arrays.asList(supplierCategoryId);
		List<Long> supplierNameIds = Arrays.asList(supplierNameId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<Long> statusIds = Arrays.asList(statusId);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<DateWiseRawMaterialReportDto> dateWiseReportDateWiseRawMaterialReportDto = dateWiseReportsNativeQueryDao.generateDateWiseRawMaterialReportWithPrice(startDate, endDate, supplierCategoryIds, supplierNameIds, rawMaterialCategoryIds, statusIds, parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], langType, timeZoneInitializer.getTimeZone());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_RAW_MATERIAL, reportName);
		return jasperReportService.generatePdfReport(dateWiseReportDateWiseRawMaterialReportDto, parameters, JasperReportNameConstant.DATE_WISE_RAW_MATERIAL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseTotalRawMaterialReportWithPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] rawMaterialCategoryId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.DateWiseReports.OutSideLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.DATE_WISE_REPORT, JasperParameterConstants.DateWiseReports.DATE_WISE_OUTSIDE_LABOUR_REPORT));
		setCommomParametersInDateWiseReports(parameters, reportName, langType, request);
		jasperReportService.setDecimalPatterns(parameters);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<Long> supplierCategoryIds = Arrays.asList(supplierCategoryId);
		List<Long> supplierNameIds = Arrays.asList(supplierNameId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<Long> statusIds = Arrays.asList(statusId);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<DateWiseRawMaterialReportDto> dateWiseReportDateWiseRawMaterialReportDto = dateWiseReportsNativeQueryDao.generateDateWiseTotalRawMaterialReportWithPrice(startDate, endDate, supplierCategoryIds, supplierNameIds, rawMaterialCategoryIds, statusIds, parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], langType, timeZoneInitializer.getTimeZone());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, Boolean.TRUE.equals(companySettingDto.getIsDynamicDesign()) ? JasperReportNameConstant.DATE_WISE_TOTAL_RAW_MATERIAL_DYNAMIC_DESIGN : JasperReportNameConstant.DATE_WISE_TOTAL_RAW_MATERIAL, reportName);
		return jasperReportService.generatePdfReport(dateWiseReportDateWiseRawMaterialReportDto, parameters, Boolean.TRUE.equals(companySettingDto.getIsDynamicDesign()) ? JasperReportNameConstant.DATE_WISE_TOTAL_RAW_MATERIAL_DYNAMIC_DESIGN : JasperReportNameConstant.DATE_WISE_TOTAL_RAW_MATERIAL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseChefLabourReportWithPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] categoryId, Long[] statusId,Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.DateWiseReports.OutSideLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.DATE_WISE_REPORT, JasperParameterConstants.DateWiseReports.DATE_WISE_OUTSIDE_LABOUR_REPORT));
		setCommomParametersInDateWiseReports(parameters, reportName, langType, request);
		jasperReportService.setDecimalPatterns(parameters);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<Long> supplierCategoryIds = Arrays.asList(supplierCategoryId);
		List<Long> supplierNameIds = Arrays.asList(supplierNameId);
		List<Long> categoryIds = Arrays.asList(categoryId);
		List<Long> statusIds = Arrays.asList(statusId);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<DateWiseChefLabourReportDto> dateWiseReportDateWiseChefLabourReportWithPrice = dateWiseReportsNativeQueryDao.generateDateWiseChefLabourReport(startDate, endDate, supplierCategoryIds, supplierNameIds, categoryIds, statusIds, parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone(), langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_CHEF_LABOUR_REPORT_WITH_PRICE, reportName);
		return jasperReportService.generatePdfReport(dateWiseReportDateWiseChefLabourReportWithPrice, parameters, JasperReportNameConstant.DATE_WISE_CHEF_LABOUR_REPORT_WITH_PRICE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseChefLabourReportWithoutPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] categoryId, Long[] statusId,  Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.DateWiseReports.OutSideLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.DATE_WISE_REPORT, JasperParameterConstants.DateWiseReports.DATE_WISE_OUTSIDE_LABOUR_REPORT));
		setCommomParametersInDateWiseReports(parameters, reportName, langType, request);
		jasperReportService.setDecimalPatterns(parameters);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<Long> supplierCategoryIds = Arrays.asList(supplierCategoryId);
		List<Long> supplierNameIds = Arrays.asList(supplierNameId);
		List<Long> categoryIds = Arrays.asList(categoryId);
		List<Long> statusIds = Arrays.asList(statusId);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<DateWiseChefLabourReportDto> dateWiseReportDateWiseChefLabourReportWithoutPrice = dateWiseReportsNativeQueryDao.generateDateWiseChefLabourReport(startDate, endDate, supplierCategoryIds, supplierNameIds, categoryIds, statusIds, parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone(), langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_CHEF_LABOUR_REPORT_WITHOUT_PRICE, reportName);
		return jasperReportService.generatePdfReport(dateWiseReportDateWiseChefLabourReportWithoutPrice, parameters, JasperReportNameConstant.DATE_WISE_CHEF_LABOUR_REPORT_WITHOUT_PRICE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseLabourReportWithPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.DateWiseReports.OutSideLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.DATE_WISE_REPORT, JasperParameterConstants.DateWiseReports.DATE_WISE_OUTSIDE_LABOUR_REPORT));
		setCommomParametersInDateWiseReports(parameters, reportName, langType, request);
		jasperReportService.setDecimalPatterns(parameters);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<Long> supplierCategoryIds = Arrays.asList(supplierCategoryId);
		List<Long> supplierNameIds = Arrays.asList(supplierNameId);
		List<Long> statusIds = Arrays.asList(statusId);
		List<DateWiseLabourReoportDto> dateWiseReportDateWiseLabourReportWithPrice = dateWiseReportsNativeQueryDao.generateDateWiseLabourReport(startDate, endDate, supplierCategoryIds, supplierNameIds, statusIds, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_LABOUR_REPORT_WITH_PRICE, reportName);
		return jasperReportService.generatePdfReport(dateWiseReportDateWiseLabourReportWithPrice, parameters, JasperReportNameConstant.DATE_WISE_LABOUR_REPORT_WITH_PRICE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseLabourReportWithoutPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.DateWiseReports.OutSideLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.DATE_WISE_REPORT, JasperParameterConstants.DateWiseReports.DATE_WISE_OUTSIDE_LABOUR_REPORT));
		setCommomParametersInDateWiseReports(parameters, reportName, langType, request);
		jasperReportService.setDecimalPatterns(parameters);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<Long> supplierCategoryIds = Arrays.asList(supplierCategoryId);
		List<Long> supplierNameIds = Arrays.asList(supplierNameId);
		List<Long> statusIds = Arrays.asList(statusId);
		List<DateWiseLabourReoportDto> dateWiseReportDateWiseLabourReportWithoutPrice = dateWiseReportsNativeQueryDao.generateDateWiseLabourReport(startDate, endDate, supplierCategoryIds, supplierNameIds, statusIds, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_LABOUR_REPORT_WITHOUT_PRICE, reportName);
		return jasperReportService.generatePdfReport(dateWiseReportDateWiseLabourReportWithoutPrice, parameters, JasperReportNameConstant.DATE_WISE_LABOUR_REPORT_WITHOUT_PRICE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generatedateWiseOrderBookingReport(LocalDate startDate, LocalDate endDate,Long[] statusId, Long[] contactId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.DateWiseReports.OutSideLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.DATE_WISE_REPORT, JasperParameterConstants.DateWiseReports.DATE_WISE_OUTSIDE_LABOUR_REPORT));
		setCommomParametersInDateWiseReports(parameters, reportName, langType, request);
		jasperReportService.setDecimalPatterns(parameters);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<Long> statusIds = Arrays.asList(statusId);
		List<Long> contactIds = Arrays.asList(contactId);
		List<DateWiseOrderBookingReportDto> dateWiseOrderBookingReport = dateWiseReportsNativeQueryDao.generateDateWiseOrderBookingReport(startDate, endDate, statusIds, contactIds, langType, timeZoneInitializer.getTimeZone());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_ORDER_BOOKING_REPORT, reportName);
		return jasperReportService.generatePdfReport(dateWiseOrderBookingReport, parameters, JasperReportNameConstant.DATE_WISE_ORDER_BOOKING_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseMenuItemReport(LocalDate startDate, LocalDate endDate, Long[] customerContactId, Long[] menuItemCategoryId, Long[] menuItemSubCategoryId, Long[] kitchenAreaId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.DateWiseReports.OutSideLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.DATE_WISE_REPORT, JasperParameterConstants.DateWiseReports.DATE_WISE_OUTSIDE_LABOUR_REPORT));
		setCommomParametersInDateWiseReports(parameters, reportName, langType, request);
		jasperReportService.setDecimalPatterns(parameters);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<Long> customerContactIds = Arrays.asList(customerContactId);
		List<Long> menuItemCategoryIds = Arrays.asList(menuItemCategoryId);
		List<Long> menuItemSubCategoryIds = Arrays.asList(menuItemSubCategoryId);
		List<Long> kitchenAreaIds = Arrays.asList(kitchenAreaId);
		List<Long> statusIds = Arrays.asList(statusId);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<DateWiseMenuItemReportDto> dateWiseReportDateWiseMenuItemReport = dateWiseReportsNativeQueryDao.generateDateWiseMenuItemReport(startDate, endDate, customerContactIds, menuItemCategoryIds, menuItemSubCategoryIds, kitchenAreaIds, statusIds, parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone(), langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_MENU_ITEM_REPORT, reportName);
		return jasperReportService.generatePdfReport(dateWiseReportDateWiseMenuItemReport, parameters, JasperReportNameConstant.DATE_WISE_MENU_ITEM_REPORT);
	}

	private void setCommomParametersInDateWiseReports(Map<String, Object> parameters, String reportName, Integer langType, HttpServletRequest request) {
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getRawMaterialCategory() {
		return dateWiseReportsNativeQueryDao.getRawMaterialCategoryDropDownData();
	}

}