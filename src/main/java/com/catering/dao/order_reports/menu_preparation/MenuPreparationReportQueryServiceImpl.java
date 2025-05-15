package com.catering.dao.order_reports.menu_preparation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.catering.bean.FileBean;
import com.catering.config.TimeZoneInitializer;
import com.catering.constant.Constants;
import com.catering.constant.FileConstants;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryService;
import com.catering.dao.company_user.CompanyUserNativeQueryService;
import com.catering.dto.tenant.request.CommanDataReportWithOutVenue;
import com.catering.dto.tenant.request.CommonDataForTheReportDto;
import com.catering.dto.tenant.request.CompanyPreferencesDto;
import com.catering.dto.tenant.request.EventDistributionNotesDto;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.MenuPreparationCustomMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationExclusiveMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationManagerMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationPremiumImageMenuDto;
import com.catering.dto.tenant.request.MenuPreparationPremiumImageMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationSimpleMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationSloganMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationTheManagerReportDto;
import com.catering.dto.tenant.request.MenuPreparationWithImageMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationWithPremiumImageMenuReportDto;
import com.catering.dto.tenant.request.ReportCompanyDetailRightsDto;
import com.catering.dto.tenant.request.SimpleMenuSubReportDto;
import com.catering.dto.tenant.request.TableMenuReportDto;
import com.catering.dto.tenant.request.TwoLanguageMenuReportDto;
import com.catering.service.common.FileService;
import com.catering.service.common.JasperReportService;
import com.catering.service.tenant.CompanyPreferencesService;
import com.catering.service.tenant.ReportCompanyDetailRightsService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.ResourceUtils;
import com.catering.util.StringUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Service implementation for generating various menu preparation reports. 
 * This service handles generating reports such as custom, simple, exclusive, and menu preparation reports,
 * including premium image reports and other related data for the specified order and functions. 
 * It integrates with several other services like file handling, company preferences, and timezone initialization 
 * to produce reports in the required formats (PDF, Excel, etc.).
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuPreparationReportQueryServiceImpl implements MenuPreparationReportQueryService {

	/**
	 * DAO interface for interacting with the database to fetch menu preparation report data.
	 */
	MenuPreparationReportQueryDao menuPreparationReportQueryDao;

	/**
	 * Service responsible for handling the generation and manipulation of Jasper Reports.
	 */
	JasperReportService jasperReportService;

	/**
	 * Service for managing file operations like saving, retrieving, or manipulating files.
	 */
	FileService fileService;

	/**
	 * Service for handling operations related to company users, particularly for retrieving user-related data.
	 */
	CompanyUserNativeQueryService companyUserNativeQueryService;

	/**
	 * Service for accessing and interacting with company preferences stored in the database.
	 */
	CompanyPreferencesNativeQueryService companyPreferencesNativeQueryService;

	/**
	 * Service for handling timezone initialization, ensuring the correct time zone is used for reports.
	 */
	TimeZoneInitializer timeZoneInitializer;

	ReportCompanyDetailRightsService reportCompanyDetailRightsService;

	/**
	 * Service for accessing and company preferences record which stored in the database.
	 */
	CompanyPreferencesService companyPreferencesService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCustomMenuPreparation(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.CustomMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.CUSTOM_MENU_REPORT));
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		List<JREmptyDataSource> oneEmptyRecord = new ArrayList<>(Arrays.asList(new JREmptyDataSource(1)));
		setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		parameters.put(ReportParameterConstants.SUB_REPORT_1_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_SIMPLE_MENU_SUB_REPORT));
		parameters.put(ReportParameterConstants.SUB_REPORT_2_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_SIMPLE_MENU_SUB_REPORT_CALCULATION));
		parameters.put(ReportParameterConstants.SUB_REPORT_3_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_SUB_REPORT));
		parameters.put(ReportParameterConstants.SUB_REPORT_4_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_SUB_REPORT_TERMS_AND_CONDITIONS));
		parameters.put(ReportParameterConstants.SUB_REPORT_5_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_SUB_REPORT_ABOUT_US));
		parameters.put(ReportParameterConstants.SUB_REPORT_6_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_SUB_REPORT_COMPANY_DETAILS));
		companyUserNativeQueryService.setTheCommonDataInParameters(parameters, langType);
		companyPreferencesNativeQueryService.setTermsAndConditionsInParameter(parameters, langType, reportName);
		companyPreferencesNativeQueryService.setAboutUsInParameter(parameters, langType);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setBackgroundImageInReport(parameters);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<Long> functionIds = Arrays.asList(functionId);
		Object tenant = request.getAttribute(Constants.TENANT);
		if (Objects.equals(tenant, Constants.SHREE_RAJ_CATERING)) {
			List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
			String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
			List<MenuPreparationCustomMenuReportDto> menuPreparationCustomMenuReport = menuPreparationReportQueryDao.generateShreeRajCateringCustomMenuReport(orderId, functionIds, DataUtils.getLangType(langType), langCode, parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone());
			return jasperReportService.generatePdfReport(menuPreparationCustomMenuReport, parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		if (Objects.equals(tenant, Constants.SATHIYA_CATERERS_TENANT)) {
			List<MenuPreparationCustomMenuReportDto> menuPreparationCustomMenuReport = menuPreparationReportQueryDao.generateSathiyaCaterersCustomMenuReport(orderId, functionIds, langType, langCode);
			parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationCustomMenuReport));
			parameters.put(ReportParameterConstants.SUB_REPORT_4_DATASOURCE, new JRBeanCollectionDataSource(oneEmptyRecord));
			parameters.put(ReportParameterConstants.SUB_REPORT_5_DATASOURCE, new JRBeanCollectionDataSource(oneEmptyRecord));
			parameters.put(ReportParameterConstants.SUB_REPORT_6_DATASOURCE, new JRBeanCollectionDataSource(oneEmptyRecord));
			jasperReportService.setFunctionIconInReport(parameters);
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		if (Objects.equals(tenant, Constants.SURTI_CATERERS_TENANT)) {
			List<MenuPreparationCustomMenuReportDto> menuPreparationCustomMenuReport = menuPreparationReportQueryDao.generateSathiyaCaterersCustomMenuReport(orderId, functionIds, langType, langCode);
			parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationCustomMenuReport));
			jasperReportService.setFunctionIconInReport(parameters);
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		List<MenuPreparationCustomMenuReportDto> menuPreparationCustomMenuReport = generateCustomMenuReport(orderId, functionIds, DataUtils.getLangType(langType), langCode);
		if (Objects.equals(tenant, Constants.YOGESHWAR_CATERERS)) {
			parameters.put(ReportParameterConstants.SUB_REPORT_3_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationCustomMenuReport));
			jasperReportService.setFunctionIconInReport(parameters);
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		if (Objects.equals(tenant, Constants.VISHAL_CATERERS)) {
			List<MenuPreparationSimpleMenuReportDto> menuPreparationImageMenuReportDtos = menuPreparationReportQueryDao.generateSimpleMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
			parameters.put(ReportParameterConstants.SUB_REPORT_3_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationImageMenuReportDtos));
			jasperReportService.setExclusiveImageInReport(parameters);
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		if (Objects.equals(tenant, Constants.BR_CATERERS)) {
			List<MenuPreparationSimpleMenuReportDto> menuPreparationImageMenuReportDtos = menuPreparationReportQueryDao.generateSimpleMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
			parameters.put(ReportParameterConstants.SUB_REPORT_3_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationImageMenuReportDtos));
			jasperReportService.setExclusiveImageInReport(parameters);
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		if (Objects.equals(tenant, Constants.PANDYA_CATERERS)) {
			List<MenuPreparationSimpleMenuReportDto> menuPreparationImageMenuReportDtos = menuPreparationReportQueryDao.generateSimpleMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
			List<MenuPreparationPremiumImageMenuReportDto> menuPreparationMenuWithImageOrMenuWithSloganOrMenuWithPremiumImageReport = menuPreparationReportQueryDao.generateMenuWithPremiumImageReport(orderId,functionIds, DataUtils.getLangType(langType));
			parameters.put(ReportParameterConstants.SUB_REPORT_3_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationImageMenuReportDtos));
			parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationMenuWithImageOrMenuWithSloganOrMenuWithPremiumImageReport));
			parameters.put(ReportParameterConstants.SUB_REPORT_1_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_SUB_REPORT_1));
			jasperReportService.setExclusiveImageInReport(parameters);
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		if (Objects.equals(tenant, Constants.ELITE_CATERERS_TENANT)) {
			List<MenuPreparationSimpleMenuReportDto> menuPreparationImageMenuReportDtos = menuPreparationReportQueryDao.generateSimpleMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
			parameters.put(ReportParameterConstants.SUB_REPORT_4_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_TERMS_AND_CONDITIONS_SUB_REPORT));
			parameters.put(ReportParameterConstants.SUB_REPORT_4_DATASOURCE, new JRBeanCollectionDataSource(oneEmptyRecord));
			parameters.put(ReportParameterConstants.SUB_REPORT_6_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_FUNCTION_DETAILS_SUB_REPORT));
			parameters.put(ReportParameterConstants.SUB_REPORT_6_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationImageMenuReportDtos));
			parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationCustomMenuReport));
			jasperReportService.setBackgroundImageInReport(parameters);
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		List<SimpleMenuSubReportDto> subReport = menuPreparationReportQueryDao.generateSimpleMenuSubReportDto(orderId, functionIds, langType);
		parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationCustomMenuReport));
		parameters.put(ReportParameterConstants.SUB_REPORT_2_DATASOURCE, new JRBeanCollectionDataSource(subReport));
		parameters.put(ReportParameterConstants.SUB_REPORT_3_DATASOURCE, new JRBeanCollectionDataSource(subReport));
		if (Objects.equals(tenant, Constants.SHYAM_CATERERS_SURAT_TENANT) || Objects.equals(tenant, Constants.SHYAM_CATERERS_AMRELI_TENANT)) {
			parameters.put(ReportParameterConstants.SUB_REPORT_4_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_TERMS_AND_CONDITIONS_SUB_REPORT));
			parameters.put(ReportParameterConstants.SUB_REPORT_4_DATASOURCE, new JRBeanCollectionDataSource(oneEmptyRecord));
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT, reportName);
		if (Objects.equals(tenant, Constants.KARUNA_CATERERS_TENANT)) {
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		if (Objects.equals(tenant, Constants.KARUNA_CATERERS_TENANT)) {
			return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
		}
		return jasperReportService.generatePdfReport(menuPreparationCustomMenuReport, parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_CUSTOM_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MenuPreparationCustomMenuReportDto> generateCustomMenuReport(Long orderId, List<Long> functionIds, Integer langType, String langCode) {
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		return menuPreparationReportQueryDao.generateCustomMenuReport(orderId, functionIds, DataUtils.getLangType(langType), langCode, parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateSimpleMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.ImageMenuCategoryReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.IMAGE_MENU_REPORT));
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		setCommonDataForImageMenuReport(parameters, orderId, langType, reportName);
		setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		jasperReportService.setDecimalPatterns(parameters);
		jasperReportService.setBackgroundImageInReport(parameters);
		jasperReportService.setFunctionIconInReport(parameters);
		List<Long> functionIds = Arrays.asList(functionId);
		List<MenuPreparationSimpleMenuReportDto> menuPreparationImageMenuReportDtos;
		parameters.put(ReportParameterConstants.LANG_CODE, langCode);
		if (Objects.equals(request.getAttribute(Constants.TENANT), Constants.RAJA_CATERERS_TENANT)) {
			menuPreparationImageMenuReportDtos = menuPreparationReportQueryDao.generateSimpleMenuRajaCaterersReport(orderId, functionIds, DataUtils.getLangType(langType));
		} else {
			menuPreparationImageMenuReportDtos = menuPreparationReportQueryDao.generateSimpleMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
		}

		if (Objects.equals(request.getAttribute(Constants.TENANT), Constants.SHYAM_CATERERS_SURAT_TENANT) || Objects.equals(request.getAttribute(Constants.TENANT), Constants.SHYAM_CATERERS_AMRELI_TENANT)) {
			parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.CustomMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.CUSTOM_MENU_REPORT));
			setCommonDataForImageMenuReport(parameters, orderId, langType, reportName);
			parameters.put(ReportParameterConstants.BACKGROUND_IMAGE, fileService.getUrl(Constants.REPORT_BACKGROUND));
			jasperReportService.setBackgroundImageInReport(parameters);
			parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationImageMenuReportDtos));
			parameters.put(ReportParameterConstants.SUB_REPORT_1_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_SIMPLE_MENU_REPORT_SUB_REPORT));
		}
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_SIMPLE_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuPreparationImageMenuReportDtos, parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_SIMPLE_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateManagerMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Object tenant = request.getAttribute(Constants.TENANT);
		List<Long> functionIds = Arrays.asList(functionId);
		Map<String, Object> parameters;
		if (Objects.equals(tenant, Constants.SHYAM_CATERERS_SURAT_TENANT) || Objects.equals(tenant, Constants.SHYAM_CATERERS_AMRELI_TENANT)) {
			parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.CustomMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.CUSTOM_MENU_REPORT));
			setTheCommonDataInParameter(parameters, orderId, langType, reportName);
			parameters.put(ReportParameterConstants.BACKGROUND_IMAGE, fileService.getUrl(Constants.REPORT_BACKGROUND));
		} else {
			parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.ManagerMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.MANAGER_MENU_REPORT));
			companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
			setCommonDataForImageMenuReport(parameters, orderId, langType, reportName);
			parameters.put(ReportParameterConstants.LANG_CODE, langCode);
			setCommonDataForManagerMenuReport(parameters, orderId, langType);
			jasperReportService.setCompanyLogo(parameters, reportName, request);
			jasperReportService.setFunctionIconInReport(parameters);
		}
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		// Generate the manager's working report data
		List<MenuPreparationManagerMenuReportDto> menuPreparationManagerMenuReport = menuPreparationReportQueryDao.generateManagerMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
		if (Objects.equals(tenant, Constants.GOKUL_CATERERS)) {
			parameters.put(ReportParameterConstants.SUB_REPORT_1_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_MANAGER_MENU_SUB_REPORT));
			parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new JRBeanCollectionDataSource(menuPreparationManagerMenuReport));
		}
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_MANAGER_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuPreparationManagerMenuReport, parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_MANAGER_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateInstructionMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		List<Long> functionIds = Arrays.asList(functionId);
		// Create parameters for the Jasper report
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.MenuReportWithInstruction.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.MANAGER_MENU_REPORT));
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		setCommonDataForManagerMenuReport(parameters, orderId, langType);
		setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		companyUserNativeQueryService.setTheCommonDataInParameters(parameters, langType);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<MenuPreparationManagerMenuReportDto> menuPreparationManagerWorkingReport = menuPreparationReportQueryDao.generateInstructionMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.INSTRUCTION_MENU_REPORT, reportName);
		// Generate the PDF report using JasperReports
		return jasperReportService.generatePdfReport(menuPreparationManagerWorkingReport, parameters, JasperReportNameConstant.INSTRUCTION_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generatePremiumImageMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.MenuWithImageReport1.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.PREMIUM_IMAGE_MENU_REPORT));
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		setCommonDataForPremiumImageReport(parameters, orderId, langType);
		setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		List<Long> functionIds = Arrays.asList(functionId);
		parameters.put(ReportParameterConstants.LANG_CODE, langCode);
		parameters.put(ReportParameterConstants.SUB_REPORT_1_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_PREMIUM_IMAGE_MENU_SUB_REPORT_1));
		parameters.put(ReportParameterConstants.SUB_REPORT_2_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.MENU_PREPARATION_REPORT_PREMIUM_IMAGE_MENU_WITH_SUB_REPORT_2));
		Object tenant = request.getAttribute(Constants.TENANT);
		if (Objects.equals(tenant, Constants.PANCHAMRUT_CATERERS_TENANT)) {
			parameters.put(ReportParameterConstants.BACKGROUND_IMAGE, fileService.getUrl(Constants.REPORT_BACKGROUND));
		}

		List<MenuPreparationPremiumImageMenuDto> menuPreparationMenuWithImageOrSloganMenuOrMenuWithImageAndSloganReport = menuPreparationReportQueryDao.generateImageAndSloganMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
		List<MenuPreparationPremiumImageMenuReportDto> menuPreparationMenuWithImageOrMenuWithSloganOrMenuWithPremiumImageReport = menuPreparationReportQueryDao.generateMenuWithPremiumImageReport(orderId,functionIds, DataUtils.getLangType(langType));

		menuPreparationMenuWithImageOrSloganMenuOrMenuWithImageAndSloganReport.forEach(menuWithImage -> 
			menuWithImage.setMenuItemCategoryImage(fileService.getUrl(FileConstants.MODULE_MENU_ITEM_CATEGORY, menuWithImage.getMenuItemCategoryId().toString()))
		);
		parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(menuPreparationMenuWithImageOrMenuWithSloganOrMenuWithPremiumImageReport));
		parameters.put(ReportParameterConstants.SUB_REPORT_2_DATASOURCE, new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(menuPreparationMenuWithImageOrSloganMenuOrMenuWithImageAndSloganReport));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_PREMIUM_IMAGE_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_PREMIUM_IMAGE_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateImageMenuCategoryReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.ImageMenuCategoryReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.IMAGE_MENU_REPORT));
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		setCommonDataForImageMenuReport(parameters, orderId, langType, reportName);
		setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		jasperReportService.setDecimalPatterns(parameters);
		List<Long> functionIds = Arrays.asList(functionId);
		List<MenuPreparationSimpleMenuReportDto> menuPreparationImageMenuCategoryReport = menuPreparationReportQueryDao.generateImageMenuCategoryReport(orderId, functionIds, DataUtils.getLangType(langType));
		Object tenant = request.getAttribute(Constants.TENANT);
		if (Objects.equals(tenant, Constants.PANCHAMRUT_CATERERS_TENANT)) {
			parameters.put(ReportParameterConstants.BACKGROUND_IMAGE, fileService.getUrl(Constants.REPORT_BACKGROUND));
		}
		if (Objects.equals(tenant, Constants.BRIJ_BHOG_CATERERS)) {
			parameters.put(ReportParameterConstants.BACKGROUND_IMAGE, fileService.getUrl(Constants.IMAGE_MENU_REPORT_BACKGROUND));
		}
		menuPreparationImageMenuCategoryReport.forEach(menuWithImage -> 
			menuWithImage.setMenuItemCategoryImage(fileService.getUrl(FileConstants.MODULE_MENU_ITEM_CATEGORY, menuWithImage.getMenuItemCategoryId().toString()))
		);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_IMAGE_MENU_CATEGORY_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuPreparationImageMenuCategoryReport, parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_IMAGE_MENU_CATEGORY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateImageMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.ImageMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.IMAGE_MENU_CATEGORY_REPORT));
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		jasperReportService.setCompanyLogo(parameters, reportName,request);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		setCommonDataForImageMenuReport(parameters, orderId, langType, reportName);
		setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		jasperReportService.setExclusiveImageInReport(parameters);
		jasperReportService.setDecimalPatterns(parameters);
		List<Long> functionIds = Arrays.asList(functionId);
		List<MenuPreparationPremiumImageMenuDto> menuPreparationImageMenuReport = menuPreparationReportQueryDao.generateImageAndSloganMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
		menuPreparationImageMenuReport.forEach(menuWithImage -> 
			menuWithImage.setMenuItemImage(fileService.getUrl(FileConstants.MODULE_MENU_ITEM, FileConstants.MODULE_DIRECTORY_IMAGE, menuWithImage.getMenuItemId().toString()))
		);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_IMAGE_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuPreparationImageMenuReport, parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_IMAGE_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateSloganMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.SloganMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.SLOGAN_MENU_REPORT));
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName,request);
		jasperReportService.setSloganMenuDesignInReport(parameters);
		jasperReportService.setDecimalPatterns(parameters);
		List<Long> functionIds = Arrays.asList(functionId);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		companyPreferencesNativeQueryService.setTermsAndConditionsInParameter(parameters, langType, reportName);
		List<MenuPreparationSloganMenuReportDto> menuPreparationMenuWithImageOrSloganMenuOrMenuWithImageAndSloganReport = menuPreparationReportQueryDao.generateSloganMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_PREPARATION_SLOGAN_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuPreparationMenuWithImageOrSloganMenuOrMenuWithImageAndSloganReport, parameters, JasperReportNameConstant.MENU_PREPARATION_SLOGAN_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateMenuWithImageAndSloganReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.ImageAndSloganMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.IMAGE_AND_SLOGAN_MENU_REPORT));
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setDecimalPatterns(parameters);
		List<Long> functionIds = Arrays.asList(functionId);
		List<MenuPreparationPremiumImageMenuDto> menuPreparationMenuWithImageOrMenuWithSloganOrMenuWithImageAndSloganReport = menuPreparationReportQueryDao.generateImageAndSloganMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
		menuPreparationMenuWithImageOrMenuWithSloganOrMenuWithImageAndSloganReport.forEach(menuWithImage -> 
			menuWithImage.setMenuItemCategoryImage(fileService.getUrl(FileConstants.MODULE_MENU_ITEM_CATEGORY, menuWithImage.getMenuItemCategoryId().toString()))
		);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_IMAGE_AND_SLOGAN_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuPreparationMenuWithImageOrMenuWithSloganOrMenuWithImageAndSloganReport, parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_IMAGE_AND_SLOGAN_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateExclusiveMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.ExclusiveMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.EXCLUSIVE_MENU_REPORT));
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		companyPreferencesNativeQueryService.setTermsAndConditionsInParameter(parameters, langType, reportName);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		companyUserNativeQueryService.setTheCommonDataInParameters(parameters, langType);
		jasperReportService.setExclusiveImageInReport(parameters);
		jasperReportService.setDecimalPatterns(parameters);
		jasperReportService.setCompanyLogo(parameters, reportName,request);
		setCommonDataForImageMenuReport(parameters, orderId, langType, reportName);
		List<Long> functionIds = Arrays.asList(functionId);
		Object tenant = request.getAttribute(Constants.TENANT);
		if (Objects.equals(tenant, Constants.PANCHAMRUT_CATERERS_TENANT)) {
			parameters.put(ReportParameterConstants.BACKGROUND_IMAGE, fileService.getUrl(Constants.REPORT_BACKGROUND));
		}
		List<MenuPreparationExclusiveMenuReportDto> menuPreparationExclusiveReport = menuPreparationReportQueryDao.generateExclusiveMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_EXCLUSIVE_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuPreparationExclusiveReport, parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_EXCLUSIVE_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateTwoLanguageMenuReport(Long orderId, Long[] functionId, Integer langType, String defaultLang, String preferLang, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.TwoLanguageMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.TWO_LANGUAGE_MENU_REPORT));
		setEmailIdAndFoodLicenceNumberValuesInParameter(parameters);
		setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		jasperReportService.setDecimalPatterns(parameters);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setBackgroundImageInReport(parameters);
		List<Long> functionIds = Arrays.asList(functionId);
		List<TwoLanguageMenuReportDto> twoLanguageMenuReport = menuPreparationReportQueryDao.generateTwoLanguageMenuReport(orderId, functionIds, langType, defaultLang, preferLang);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_TWO_LANGUAGE_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(twoLanguageMenuReport, parameters, JasperReportNameConstant.MENU_PREPARATION_REPORT_TWO_LANGUAGE_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateTableMenuReport(Long orderId, Integer langType, String langCode, Long[] functionId, HttpServletRequest request, String jasperReportNameConstant) {
		// Create parameters for the Jasper report
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.MenuReportWithInstruction.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.MANAGER_MENU_REPORT));
		setCommonDataForManagerMenuReport(parameters, orderId, langType);
		companyUserNativeQueryService.setTheCommonDataInParameters(parameters, langType);
		parameters.put(ReportParameterConstants.TABLE_MENU_BACKGROUND, fileService.getUrl(Constants.REPORT_TABLE_MENU_BACKGROUND));
		Object tenant = request.getAttribute(Constants.TENANT);
		if (Objects.equals(tenant, Constants.SHYAM_CATERERS_SURAT_TENANT) || Objects.equals(tenant, Constants.SHYAM_CATERERS_AMRELI_TENANT)) {
			parameters.put(ReportParameterConstants.TABLE_MENU_UNDERLINE_IMAGE, fileService.getUrl(Constants.REPORT_TABLE_MENU_UNDERLINE_IMAGE));
		}
		List<Long> functionIds = Arrays.asList(functionId);
		List<TableMenuReportDto> tableMenuReport = menuPreparationReportQueryDao.generateTableMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
		EventDistributionNotesDto getHeaderNotes = menuPreparationReportQueryDao.findTableMenuReportHeaderNotes(langType, orderId);
		EventDistributionNotesDto getFooterNotes = menuPreparationReportQueryDao.findTableMenuReportFooterNotes(langType, orderId);
		parameters.put(ReportParameterConstants.HEADER_NOTES, Objects.nonNull(getHeaderNotes) ? getHeaderNotes.getNoteName() : "");
		parameters.put(ReportParameterConstants.FOOTER_NOTES, Objects.nonNull(getFooterNotes) ? getFooterNotes.getNoteName() : "");
		// Generate the PDF report using JasperReports
		return jasperReportService.generatePdfReport(tableMenuReport, parameters, jasperReportNameConstant);
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, Object> setTheCommonDataInParameter(Map<String, Object> parameters, Long orderId, Integer langType, String reportName) {
		CommonDataForTheReportDto commonDataForReport = menuPreparationReportQueryDao.getCommonDataForReport(orderId, langType);
		ReportCompanyDetailRightsDto reportCompanyDetailRightsDto = reportCompanyDetailRightsService.getReportCompanyDetailRightsByReportName(reportName);
		if (Objects.nonNull(commonDataForReport)) {
			parameters.put(ReportParameterConstants.EVENT_NAME_VALUE, commonDataForReport.getEventName());
			parameters.put(ReportParameterConstants.CUSTOMER_NAME_VALUE, commonDataForReport.getCustomerName());
			parameters.put(ReportParameterConstants.VENUE_VALUE, commonDataForReport.getVenue());
			parameters.put(ReportParameterConstants.MEETING_DATE_VALUE, commonDataForReport.getMeetingDate());
			parameters.put(ReportParameterConstants.EVENT_DATE_VALUE, commonDataForReport.getEventMainDate());
			parameters.put(ReportParameterConstants.NOTES_VALUE, commonDataForReport.getNotes());
			parameters.put(ReportParameterConstants.CUSTOMER_NUMBER_VALUE, commonDataForReport.getMobileNumber());
			parameters.put(ReportParameterConstants.CUSTOMER_EMAIL_VALUE, commonDataForReport.getCustomerEmail());
			parameters.put(ReportParameterConstants.MANAGER_NAME_VALUE, commonDataForReport.getManagerName());
			parameters.put(ReportParameterConstants.MANAGER_NUMBER_VALUE, commonDataForReport.getManagerNumber());
			parameters.put(ReportParameterConstants.CUSTOMER_OFFICE_NUMBER, commonDataForReport.getCustomerOfficeNumber());
			parameters.put(ReportParameterConstants.MEAL_TYPE_NAME_VALUE, commonDataForReport.getMealTypeName());
			parameters.put(ReportParameterConstants.HALL_NAME_VALUE, commonDataForReport.getHallName());
			if (Boolean.TRUE.equals(reportCompanyDetailRightsDto.getOrderBookingNotes())) {
				parameters.put(ReportParameterConstants.ORDER_NOTES, commonDataForReport.getOrderNotes() != null ? commonDataForReport.getOrderNotes() : "");
			}
			parameters.put(ReportParameterConstants.MEAL_NOTES, commonDataForReport.getMealNotes() != null ? commonDataForReport.getMealNotes() : "");
		}
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FunctionPerOrderDto> getFunctionsPerOrderForMenuPreparation(Long orderId) {
		return menuPreparationReportQueryDao.getFunctionsPerOrderForMenuPreparation(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateTableMenuDocReport(Long orderId, Integer langType, String langCode, Long[] functionId, HttpServletRequest request, String jasperReportNameConstant) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuPreprationReport.MenuReportWithInstruction.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_PREPARATION_REPORT, JasperParameterConstants.MenuPreprationReport.MANAGER_MENU_REPORT));
		setCommonDataForManagerMenuReport(parameters, orderId, langType);
		companyUserNativeQueryService.setTheCommonDataInParameters(parameters, langType);
		parameters.put(ReportParameterConstants.TABLE_MENU_BACKGROUND, fileService.getUrl(Constants.REPORT_TABLE_MENU_BACKGROUND));
		Object tenant = request.getAttribute(Constants.TENANT);
		if (Objects.equals(tenant, Constants.SHYAM_CATERERS_SURAT_TENANT) || Objects.equals(tenant, Constants.SHYAM_CATERERS_AMRELI_TENANT)) {
			parameters.put(ReportParameterConstants.TABLE_MENU_UNDERLINE_IMAGE, fileService.getUrl(Constants.REPORT_TABLE_MENU_UNDERLINE_IMAGE));
		}
		List<Long> functionIds = Arrays.asList(functionId);
		List<TableMenuReportDto> tableMenuReport = menuPreparationReportQueryDao.generateTableMenuReport(orderId, functionIds, DataUtils.getLangType(langType));
		EventDistributionNotesDto getHeaderNotes = menuPreparationReportQueryDao.findTableMenuReportHeaderNotes(langType, orderId);
		EventDistributionNotesDto getFooterNotes = menuPreparationReportQueryDao.findTableMenuReportFooterNotes(langType, orderId);
		parameters.put(ReportParameterConstants.HEADER_NOTES, Objects.nonNull(getHeaderNotes) ? getHeaderNotes.getNoteName() : "");
		parameters.put(ReportParameterConstants.FOOTER_NOTES, Objects.nonNull(getFooterNotes) ? getFooterNotes.getNoteName() : "");
		// Generate the PDF report using JasperReports
		return jasperReportService.generateDocReport(tableMenuReport, parameters, jasperReportNameConstant);
	}

	/**
	 * Sets common data for the report parameters when the venue information is not provided.
	 * 
	 * @param parameters the map of parameters to be populated
	 * @param orderId the ID of the order for which the report is being generated
	 * @param langType the language type to determine the report language
	 * @return the map of parameters populated with common report data
	 */
	public Map<String, Object> setTheCommonDataInParameterWithOutVenue(Map<String, Object> parameters, Long orderId, Integer langType) {
		CommanDataReportWithOutVenue commanDataReportWithOutVenue = menuPreparationReportQueryDao.getCommonDataForReportWithOutVenue(orderId, langType);
		if (Objects.nonNull(commanDataReportWithOutVenue)) {
			parameters.put(ReportParameterConstants.EVENT_NAME_VALUE, commanDataReportWithOutVenue.getEventName());
			parameters.put(ReportParameterConstants.CUSTOMER_NAME_VALUE, commanDataReportWithOutVenue.getCustomerName());
			parameters.put(ReportParameterConstants.EVENT_DATE_VALUE, commanDataReportWithOutVenue.getEventMainDate());
			parameters.put(ReportParameterConstants.NOTES_VALUE, commanDataReportWithOutVenue.getNotes());
			parameters.put(ReportParameterConstants.CUSTOMER_NUMBER_VALUE, commanDataReportWithOutVenue.getMobileNumber());
			parameters.put(ReportParameterConstants.CUSTOMER_EMAIL_VALUE, commanDataReportWithOutVenue.getCustomerEmail());
			parameters.put(ReportParameterConstants.MANAGER_NAME_VALUE, commanDataReportWithOutVenue.getManagerName());
			parameters.put(ReportParameterConstants.MANAGER_NUMBER_VALUE, commanDataReportWithOutVenue.getManagerNumber());
			parameters.put(ReportParameterConstants.CUSTOMER_OFFICE_NUMBER, commanDataReportWithOutVenue.getCustomerOfficeNumber());
			parameters.put(ReportParameterConstants.MEAL_TYPE_NAME_VALUE, commanDataReportWithOutVenue.getMealTypeName());
			parameters.put(ReportParameterConstants.HALL_NAME_VALUE, commanDataReportWithOutVenue.getHallName());
		}
		return parameters;
	}

	/**
	 * Sets common data for the manager menu report parameters.
	 * 
	 * @param parameters the map of parameters to be populated
	 * @param orderId the ID of the order for which the report is being generated
	 * @param langType the language type to determine the report language
	 * @return the map of parameters populated with common manager menu report data
	 */
	private Map<String, Object> setCommonDataForManagerMenuReport(Map<String, Object> parameters, Long orderId, Integer langType) {
		MenuPreparationTheManagerReportDto commonDataForReport = menuPreparationReportQueryDao.getCommonDataForManagerWorkingReport(orderId, langType);
		if (Objects.nonNull(commonDataForReport)) {
			parameters.put(ReportParameterConstants.EVENT_NAME_VALUE, commonDataForReport.getEventName());
			parameters.put(ReportParameterConstants.CUSTOMER_NAME_VALUE, commonDataForReport.getCustomerName());
			parameters.put(ReportParameterConstants.EVENT_DATE_VALUE, commonDataForReport.getEventDate());
			parameters.put(ReportParameterConstants.NOTES_VALUE, commonDataForReport.getNotes());
			parameters.put(ReportParameterConstants.CUSTOMER_MOBILE_VALUE, commonDataForReport.getCustomerMobileNumber());
			parameters.put(ReportParameterConstants.MEAL_TYPE_NAME_VALUE, commonDataForReport.getMealTypeName());
			parameters.put(ReportParameterConstants.HALL_NAME_VALUE, commonDataForReport.getHallName());
			parameters.put(ReportParameterConstants.CUSTOMER_HOME_ADDRESS_VALUE, commonDataForReport.getCustomerHomeAddress());
			parameters.put(ReportParameterConstants.VENUE_VALUE, commonDataForReport.getVenue());
		}
		return parameters;
	}

	/**
	 * Sets common data for the premium image menu report parameters.
	 * 
	 * @param parameters the map of parameters to be populated
	 * @param orderId the ID of the order for which the report is being generated
	 * @param langType the language type to determine the report language
	 * @return the map of parameters populated with common premium image menu report data
	 */
	private Map<String, Object> setCommonDataForPremiumImageReport(Map<String, Object> parameters, Long orderId, Integer langType) {
		MenuPreparationWithPremiumImageMenuReportDto commonDataForReport = menuPreparationReportQueryDao.getCommonDataForThePremiumImageReport(orderId, langType);
		if (Objects.nonNull(commonDataForReport)) {
			parameters.put(ReportParameterConstants.EVENT_NAME_VALUE, commonDataForReport.getEventName());
			parameters.put(ReportParameterConstants.CUSTOMER_NAME_VALUE, commonDataForReport.getCustomerName());
			parameters.put(ReportParameterConstants.VENUE_VALUE, commonDataForReport.getVenue());
			parameters.put(ReportParameterConstants.NOTES_VALUE, commonDataForReport.getNotes());
			parameters.put(ReportParameterConstants.CUSTOMER_NUMBER_VALUE, commonDataForReport.getMobileNumber());
			parameters.put(ReportParameterConstants.MEAL_TYPE_NAME_VALUE, commonDataForReport.getMealTypeName());
			parameters.put(ReportParameterConstants.HALL_NAME_VALUE, commonDataForReport.getHallName());
			parameters.put(ReportParameterConstants.CUSTOMER_HOME_ADDRESS_VALUE, commonDataForReport.getCustomerAddress());
		}
		return parameters;
	}

	/**
	 * Sets common data for the image menu report parameters.
	 * 
	 * @param parameters the map of parameters to be populated
	 * @param orderId the ID of the order for which the report is being generated
	 * @param langType the language type to determine the report language
	 * @return the map of parameters populated with common image menu report data
	 */
	private Map<String, Object> setCommonDataForImageMenuReport(Map<String, Object> parameters, Long orderId, Integer langType, String reportName) {
		MenuPreparationWithImageMenuReportDto commonDataForReport = menuPreparationReportQueryDao.getCommonDataForTheImageMenuReport(orderId, langType);
		ReportCompanyDetailRightsDto reportCompanyDetailRightsDto = reportCompanyDetailRightsService.getReportCompanyDetailRightsByReportName(reportName);
		if (Objects.nonNull(commonDataForReport)) {
			parameters.put(ReportParameterConstants.COMPANY_USER_NAME, commonDataForReport.getCompanyUserName());
			if (Boolean.TRUE.equals(reportCompanyDetailRightsDto.getCompanyAddress())) {
				parameters.put(ReportParameterConstants.COMPANY_ADDRESS_VALUE, commonDataForReport.getCompanyAddress());
			}
			if (Boolean.TRUE.equals(reportCompanyDetailRightsDto.getCompanyMobileNumber())) {
				parameters.put(ReportParameterConstants.COMPANY_MOBILE_NUMBER_VALUE, commonDataForReport.getCompanyMobileNumber());
			}
			parameters.put(ReportParameterConstants.CUSTOMER_NAME_VALUE, commonDataForReport.getCustomerName());
			parameters.put(ReportParameterConstants.CUSTOMER_MOBILE, commonDataForReport.getCustomerMobile());
			parameters.put(ReportParameterConstants.CUSTOMER_MOBILE_VALUE, commonDataForReport.getCustomerMobile());
			parameters.put(ReportParameterConstants.EVENT_NAME_VALUE, commonDataForReport.getEventName());
			parameters.put(ReportParameterConstants.HALL_NAME_VALUE, commonDataForReport.getHallName());
			parameters.put(ReportParameterConstants.VENUE_VALUE, commonDataForReport.getVenue());
			parameters.put(ReportParameterConstants.CUSTOMER_HOME_ADDRESS_VALUE, commonDataForReport.getCustomerHomeAddress());
			parameters.put(ReportParameterConstants.CUSTOMER_EMAIL_VALUE, commonDataForReport.getCustomerEmail());
			parameters.put(ReportParameterConstants.MANAGER_NAME_VALUE, commonDataForReport.getManagerName());
			parameters.put(ReportParameterConstants.CHEF_NAME_VALUE, commonDataForReport.getChefName());
			parameters.put(ReportParameterConstants.EVENT_DATE_VALUE, commonDataForReport.getEventDate());
			parameters.put(ReportParameterConstants.MEAL_TYPE_NAME_VALUE, commonDataForReport.getMealTypename());
			parameters.put(ReportParameterConstants.NOTES_VALUE, commonDataForReport.getNotes());
		}
		return parameters;
	}

	/**
	 * Sets the company email and food license number values in the provided report parameters map.
	 * The values are retrieved from the company preferences, and if they are blank, they are set to null.
	 *
	 * @param parameters the map containing report parameters to which company email and food license number will be added
	 */
	private void setEmailIdAndFoodLicenceNumberValuesInParameter(Map<String, Object> parameters) {
		Optional<CompanyPreferencesDto> companyPreference = companyPreferencesService.find();
		companyPreference.ifPresent(preference -> {
			parameters.put(ReportParameterConstants.COMPANY_EMAIL_VALUE, org.apache.commons.lang3.StringUtils.isNotBlank(preference.getEmail()) ? preference.getEmail() : null);
			parameters.put(ReportParameterConstants.COMPANY_FOOD_LICENCE_NUMBER_VALUE, org.apache.commons.lang3.StringUtils.isNotBlank(preference.getFoodLicenceNumber()) ? preference.getFoodLicenceNumber() : null);
		});
	}

}