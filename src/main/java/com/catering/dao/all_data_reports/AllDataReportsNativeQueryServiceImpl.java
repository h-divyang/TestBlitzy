package com.catering.dao.all_data_reports;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.catering.bean.FileBean;
import com.catering.constant.Constants;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryService;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryServiceImpl;
import com.catering.dto.tenant.request.AllDataReportMenuItemReportDto;
import com.catering.dto.tenant.request.AllDataReportRawMaterialReportDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.MenuItemWiseQuantityRawMaterialReportDto;
import com.catering.dto.tenant.request.PackageReportDto;
import com.catering.service.common.JasperReportService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.StringUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Implementation of the {@link AllDataReportsNativeQueryService} interface.
 * This service is responsible for generating various types of reports related to menu items, 
 * raw materials, menu item quantities, and custom packages. It interacts with the 
 * `AllDataReportsNativeQueryDao` to fetch the required data and processes it to generate 
 * reports in the appropriate format using `JasperReportService`.
 * 
 * The service also depends on `CompanyPreferencesNativeQueryServiceImpl` for fetching 
 * company-specific preferences and configurations related to the reports.
 * 
 * Key Responsibilities:
 * - Implements the business logic to generate reports using data from the database.
 * - Uses `JasperReportService` to generate reports in a specific format (e.g., PDF, Excel).
 * - Retrieves dropdown data for filtering reports through interaction with the DAO layer.
 * - Fetches company-specific preferences to tailor the reports according to user needs.
 * 
 * Dependencies:
 * - {@link JasperReportService}: Used to generate reports in the desired formats.
 * - {@link AllDataReportsNativeQueryDao}: Interface to fetch report data from the database.
 * - {@link CompanyPreferencesNativeQueryServiceImpl}: Retrieves company-specific preferences 
 *   for report generation.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AllDataReportsNativeQueryServiceImpl implements AllDataReportsNativeQueryService {

	/**
	 * Service for generating reports using JasperReports.
	 */
	JasperReportService jasperReportService;

	/**
	 * DAO used to query the database and retrieve report data.
	 */
	AllDataReportsNativeQueryDao allDataReportsNativeQueryDao;

	/**
	 * Service to fetch company preferences and configurations for report generation.
	 */
	CompanyPreferencesNativeQueryService companyPreferencesNativeQueryService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateMenuItemCatalogueReport(Integer templateId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AllDataReports.MenuItemReport.class, StringUtils.dotSeparated(JasperParameterConstants.ALL_DATA_REPORT, JasperParameterConstants.AllDataReports.MENU_ITEM_REPORT));
		List<AllDataReportMenuItemReportDto> allDataReportMenuItemReport = allDataReportsNativeQueryDao.generateMenuItemReport(langType);
		Object tenant = request.getAttribute(Constants.TENANT);
		if (Objects.equals(tenant, Constants.PANDYA_CATERERS) && templateId.equals(0)) {
			return jasperReportService.generatePdfReport(allDataReportMenuItemReport, parameters, JasperReportNameConstant.ALL_DATA_REPORT_CATALOGUE_REPORT);
		}
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		companyPreferencesNativeQueryService.setTermsAndConditionsInParameter(parameters, langType, reportName);
		parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new JRBeanCollectionDataSource(allDataReportMenuItemReport));
		if (templateId.equals(1)) {
			parameters.put(ReportParameterConstants.SUB_REPORT_1_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.ALL_DATA_REPORT_CATALOGUE_THEME_ONE_SUB_REPORT));
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.ALL_DATA_REPORT_CATALOGUE_THEME_ONE_REPORT);
		}
		if (templateId.equals(2)) {
			parameters.put(ReportParameterConstants.SUB_REPORT_1_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.ALL_DATA_REPORT_CATALOGUE_THEME_TWO_SUB_REPORT));
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.ALL_DATA_REPORT_CATALOGUE_THEME_TWO_REPORT);
		}
		if (templateId.equals(3)) {
			parameters.put(ReportParameterConstants.SUB_REPORT_1_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.ALL_DATA_REPORT_CATALOGUE_THEME_THREE_SUB_REPORT));
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.ALL_DATA_REPORT_CATALOGUE_THEME_THREE_REPORT);
		}
		return jasperReportService.generatePdfReport(allDataReportMenuItemReport, parameters, JasperReportNameConstant.ALL_DATA_REPORT_CATALOGUE_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateMenuItemMenuReport(Integer langType, String langCode, String reportName) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AllDataReports.MenuItemReport.class, StringUtils.dotSeparated(JasperParameterConstants.ALL_DATA_REPORT, JasperParameterConstants.AllDataReports.MENU_ITEM_REPORT));
		List<AllDataReportMenuItemReportDto> allDataReportMenuItemReport = allDataReportsNativeQueryDao.generateMenuItemReport(langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ALL_DATA_REPORT_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(allDataReportMenuItemReport, parameters, JasperReportNameConstant.ALL_DATA_REPORT_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getRawMaterialReportDropDownData() {
		return allDataReportsNativeQueryDao.getRawMaterialReportDropDownData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateRawMaterialsReport(Long rawMaterialCategoryId, Integer langType, String langCode, String reportName) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AllDataReports.RawMaterialReport.class, StringUtils.dotSeparated(JasperParameterConstants.ALL_DATA_REPORT, JasperParameterConstants.AllDataReports.RAW_MATERIAL_REPORT));
		List<AllDataReportRawMaterialReportDto> allDataReportRawMaterialReport = allDataReportsNativeQueryDao.generateRawMaterialReport(rawMaterialCategoryId, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ALL_DATA_REPORT_RAW_MATERIAL_REPORT, reportName);
		return jasperReportService.generatePdfReport(allDataReportRawMaterialReport, parameters, JasperReportNameConstant.ALL_DATA_REPORT_RAW_MATERIAL_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getMenuItemWiseRawMaterialReportDropDownData() {
		return allDataReportsNativeQueryDao.getMenuItemWiseRawMaterialReportDropDownData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean getMenuItemWiseRawMaterialReport(Long itemMaterialId, Integer langType, String langCode, String reportName) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AllDataReports.RawMaterialReport.class, StringUtils.dotSeparated(JasperParameterConstants.ALL_DATA_REPORT, JasperParameterConstants.AllDataReports.RAW_MATERIAL_REPORT));
		List<AllDataReportRawMaterialReportDto> menuItemWiseRawMaterialReport = allDataReportsNativeQueryDao.getMenuItemWiseRawMaterialReport(itemMaterialId, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ALL_DATA_REPORT_MENU_ITEM_WISE_RAW_MATERIAL_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuItemWiseRawMaterialReport, parameters, JasperReportNameConstant.ALL_DATA_REPORT_MENU_ITEM_WISE_RAW_MATERIAL_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getMenuItemWiseQuantityRawMaterialReportDropdown() {
		return allDataReportsNativeQueryDao.getMenuItemWiseQuantityRawMaterialReportDropdown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateMenuItemWiseQuantityRawMaterialReport(Long menuItemWiseQuantityRawMaterialCategoryId, Long[] dataTypeId, Integer langType, String langCode, String reportName) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AllDataReports.MenuItemReport.class, StringUtils.dotSeparated(JasperParameterConstants.ALL_DATA_REPORT, JasperParameterConstants.AllDataReports.MENU_ITEM_REPORT));
		jasperReportService.setDecimalPatterns(parameters);
		List<Long> dataTypeIds = Arrays.asList(dataTypeId);
		List<MenuItemWiseQuantityRawMaterialReportDto> menuItemWiseQuantityRawMaterialReport = allDataReportsNativeQueryDao.generateMenuItemWiseQuantityRawMaterialReport(menuItemWiseQuantityRawMaterialCategoryId, dataTypeIds, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ALL_DATA_REPORT_MENU_ITEM_WISE_QUANTITY_RAW_MATERIAL_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuItemWiseQuantityRawMaterialReport, parameters, JasperReportNameConstant.ALL_DATA_REPORT_MENU_ITEM_WISE_QUANTITY_RAW_MATERIAL_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getCustomPackageDropDownData() {
		return allDataReportsNativeQueryDao.getCustomPackageReportDropDownData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCustomPackageReport(Long customPackageId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.AllDataReports.CustomPackageReport.class, StringUtils.dotSeparated(JasperParameterConstants.ALL_DATA_REPORT, JasperParameterConstants.AllDataReports.REPORT_REPORT));
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCustomPackageReportBackImage(parameters);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<PackageReportDto> packageReportDto = allDataReportsNativeQueryDao.generatePackageReport(customPackageId, langType);
		return jasperReportService.generatePdfReport(packageReportDto, parameters, JasperReportNameConstant.ALL_DATA_REPORT_CUSTOM_PACKAGE_REPORT);
	}

}