package com.catering.dao.order_reports.order_general_fix_and_crockery_allocation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.catering.bean.FileBean;
import com.catering.config.TimeZoneInitializer;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryServiceImpl;
import com.catering.dao.company_user.CompanyUserNativeQueryService;
import com.catering.dao.order_reports.menu_preparation.MenuPreparationReportQueryService;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.CrockeryWithQuantityReportDto;
import com.catering.dto.tenant.request.CrockeryWithoutQuantityReportDto;
import com.catering.dto.tenant.request.EventDistributionNotesDto;
import com.catering.dto.tenant.request.EventMenuReportWithCrockeryReportWithQuantityDto;
import com.catering.dto.tenant.request.MenuPreparationCustomMenuReportDto;
import com.catering.dto.tenant.request.OrderGeneralFixReportWithQuantityDto;
import com.catering.dto.tenant.request.OrderGeneralFixReportWithoutQuantityDto;
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
 * Service implementation for generating event agency distribution labour reports.
 * This class provides the implementation of the `generateLabourReport` method, which generates labour reports
 * based on a specified order, language type, and language code.
 * 
 * @author Krushali Talaviya
 * @since 2023-10-29
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderGeneralFixAndCrockeryAllocationReportQueryServiceImpl implements OrderGeneralFixAndCrockeryAllocationReportQueryService {

	/**
	 * DAO for interacting with the database and fetching data related to the Order General Fix and Crockery Allocation Report.
	 */
	OrderGeneralFixAndCrockeryAllocationReportQueryDao orderGeneralFixAndCrockeryAllocationReportQueryDao;

	/**
	 * Service for generating JasperReports, including the report generation and export functionalities.
	 */
	JasperReportService jasperReportService;

	/**
	 * Service for managing menu preparation-related reports and queries.
	 */
	MenuPreparationReportQueryService menuPreparationReportQueryService;

	/**
	 * Service for interacting with company user-related queries and operations, such as fetching user details.
	 */
	CompanyUserNativeQueryService companyUserNativeQueryService;

	/**
	 * Service for initializing and managing time zone settings used across reports and data processing.
	 */
	TimeZoneInitializer timeZoneInitializer;

	/**
	 * Service for managing company preferences and configuration settings, used for customizing report behaviors.
	 */
	CompanyPreferencesNativeQueryServiceImpl companyPreferencesNativeQueryServiceImpl;

	/**
	 * Repository instance for handling database operations related to booking orders.
	 */
	BookOrderRepository bookOrderRepository;

	/**
	 * The service to fetch company settings.
	 */
	CompanySettingService companySettingService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCrockeryWithQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.CrockeryReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.CROCKERY_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<CrockeryWithQuantityReportDto> crockeryWithQuantityReportDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateCrockeryWithQuantityReport(orderId, DataUtils.getLangType(langType), bookOrderRepository.getAdjustQuantityByOrderId(orderId), companySettingDto.getDisplayCrockeryAndGeneralFix());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITH_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(crockeryWithQuantityReportDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITH_QUANTITY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCrockeryWithQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.CrockeryReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.CROCKERY_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<CrockeryWithQuantityReportDto> crockeryWithQuantityReportDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateCrockeryWithQuantityWithoutMaxSettingReport(orderId, DataUtils.getLangType(langType));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITH_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(crockeryWithQuantityReportDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITH_QUANTITY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCrockeryWithoutQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.CrockeryReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.CROCKERY_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<CrockeryWithoutQuantityReportDto> crockeryWithoutQuantityReportDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateCrockeryWithoutQuantityReport(orderId, DataUtils.getLangType(langType), companySettingDto.getDisplayCrockeryAndGeneralFix());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITHOUT_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(crockeryWithoutQuantityReportDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITHOUT_QUANTITY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCrockeryWithoutQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.CrockeryReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.CROCKERY_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<CrockeryWithoutQuantityReportDto> crockeryWithoutQuantityReportDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateCrockeryWithoutQuantityWithoutMaxSettingReport(orderId, DataUtils.getLangType(langType));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITHOUT_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(crockeryWithoutQuantityReportDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITHOUT_QUANTITY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCrockeryWithMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.CrockeryWithMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.CROCKERY_WITH_MENU_REPORT));
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		EventDistributionNotesDto eventDistributionNotesDto = orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType);
 		parameters.put(ReportParameterConstants.NOTES, Objects.nonNull(eventDistributionNotesDto) ? eventDistributionNotesDto.getNoteName() : "");
		companyUserNativeQueryService.setTheCommonDataInParameters(parameters, langType);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<Long> functionIds = Arrays.asList(functionId);
		parameters.put(ReportParameterConstants.SUB_REPORT_1_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_WITH_CROCKERY_SIMPLE_MENU_SUB_REPORT));
		parameters.put(ReportParameterConstants.SUB_REPORT_2_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_WITH_CROCKERY_SUB_REPORT));

		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<MenuPreparationCustomMenuReportDto> simpleMenuReport = menuPreparationReportQueryService.generateCustomMenuReport(orderId, functionIds, DataUtils.getLangType(langType), langCode);
		List<EventMenuReportWithCrockeryReportWithQuantityDto> crockeryReport = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateCrockeryReportWithQty(orderId, DataUtils.getLangType(langType), parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone());

		parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(simpleMenuReport));
		parameters.put(ReportParameterConstants.SUB_REPORT_2_DATASOURCE, new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(crockeryReport));
		jasperReportService.setDecimalPatterns(parameters);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITH_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITH_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCrockeryWithMenuWithoutMaxSettingReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.CrockeryWithMenuReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.CROCKERY_WITH_MENU_REPORT));
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		EventDistributionNotesDto eventDistributionNotesDto = orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType);
 		parameters.put(ReportParameterConstants.NOTES, Objects.nonNull(eventDistributionNotesDto) ? eventDistributionNotesDto.getNoteName() : "");
		companyUserNativeQueryService.setTheCommonDataInParameters(parameters, langType);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<Long> functionIds = Arrays.asList(functionId);
		parameters.put(ReportParameterConstants.SUB_REPORT_1_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_WITH_CROCKERY_SIMPLE_MENU_SUB_REPORT));
		parameters.put(ReportParameterConstants.SUB_REPORT_2_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_WITH_CROCKERY_SUB_REPORT));

		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<MenuPreparationCustomMenuReportDto> simpleMenuReport = menuPreparationReportQueryService.generateCustomMenuReport(orderId, functionIds, DataUtils.getLangType(langType), langCode);
		List<EventMenuReportWithCrockeryReportWithQuantityDto> crockeryReport = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateCrockeryReportWithQtyWithoutMaxSetting(orderId, DataUtils.getLangType(langType), parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone());

		parameters.put(ReportParameterConstants.SUB_REPORT_1_DATASOURCE, new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(simpleMenuReport));
		parameters.put(ReportParameterConstants.SUB_REPORT_2_DATASOURCE, new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(crockeryReport));
		jasperReportService.setDecimalPatterns(parameters);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITH_MENU_REPORT, reportName);
		return jasperReportService.generatePdfReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITH_MENU_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateOrderGeneralFixWithQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.GeneralFixReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.GENERAL_FIX_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findOrderGeneralFixRawMaterialNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findOrderGeneralFixRawMaterialNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<OrderGeneralFixReportWithQuantityDto> orderGeneralFixReportWithQuantityDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateOrderGeneralFixReportWithQuantity(orderId, DataUtils.getLangType(langType), companySettingDto.getDisplayCrockeryAndGeneralFix());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_GENERAL_FIX_WITH_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(orderGeneralFixReportWithQuantityDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_GENERAL_FIX_WITH_QUANTITY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateOrderGeneralFixWithQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.GeneralFixReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.GENERAL_FIX_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findOrderGeneralFixRawMaterialNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findOrderGeneralFixRawMaterialNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<OrderGeneralFixReportWithQuantityDto> orderGeneralFixReportWithQuantityDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateOrderGeneralFixReportWithQuantityWithoutMaxSettingReport(orderId, DataUtils.getLangType(langType), companySettingDto.getDisplayCrockeryAndGeneralFix());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_GENERAL_FIX_WITH_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(orderGeneralFixReportWithQuantityDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_GENERAL_FIX_WITH_QUANTITY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateOrderGeneralFixWithoutQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.GeneralFixReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.GENERAL_FIX_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findOrderGeneralFixRawMaterialNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findOrderGeneralFixRawMaterialNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<OrderGeneralFixReportWithoutQuantityDto> orderGeneralFixReportWithoutQuantityDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateOrderGeneralFixReportWithoutQuantity(orderId, DataUtils.getLangType(langType), companySettingDto.getDisplayCrockeryAndGeneralFix());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_GENERAL_FIX_WITHOUT_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(orderGeneralFixReportWithoutQuantityDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_GENERAL_FIX_WITHOUT_QUANTITY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateOrderGeneralFixWithoutQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.GeneralFixReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.GENERAL_FIX_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findOrderGeneralFixRawMaterialNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findOrderGeneralFixRawMaterialNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<OrderGeneralFixReportWithoutQuantityDto> orderGeneralFixReportWithoutQuantityDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateOrderGeneralFixReportWithoutQuantityWithoutMaxSettingReport(orderId, DataUtils.getLangType(langType), companySettingDto.getDisplayCrockeryAndGeneralFix());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_GENERAL_FIX_WITHOUT_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(orderGeneralFixReportWithoutQuantityDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_GENERAL_FIX_WITHOUT_QUANTITY_REPORT);
	}

	@Override
	public FileBean generateKitchenCrockeryWithQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.CrockeryReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.CROCKERY_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<CrockeryWithQuantityReportDto> crockeryWithQuantityReportDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateKitchenCrockeryWithQuantityReport(orderId, DataUtils.getLangType(langType), bookOrderRepository.getAdjustQuantityByOrderId(orderId), companySettingDto.getDisplayCrockeryAndGeneralFix());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_KITCHEN_CROCKERY_WITH_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(crockeryWithQuantityReportDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_KITCHEN_CROCKERY_WITH_QUANTITY_REPORT);
	}

	@Override
	public FileBean generateKitchenCrockeryWithQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.CrockeryReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.CROCKERY_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<CrockeryWithQuantityReportDto> crockeryWithQuantityReportDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateKitchenCrockeryWithQuantityWithoutMaxSettingReport(orderId, DataUtils.getLangType(langType), companySettingDto.getDisplayCrockeryAndGeneralFix());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_KITCHEN_CROCKERY_WITH_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(crockeryWithQuantityReportDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_KITCHEN_CROCKERY_WITH_QUANTITY_REPORT);
	}

	@Override
	public FileBean generateKitchenCrockeryWithoutQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.CrockeryReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.CROCKERY_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<CrockeryWithoutQuantityReportDto> crockeryWithoutQuantityReportDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateKitchenCrockeryWithoutQuantityReport(orderId, DataUtils.getLangType(langType), companySettingDto.getDisplayCrockeryAndGeneralFix());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_KITCHEN_CROCKERY_WITH_OUT_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(crockeryWithoutQuantityReportDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_KITCHEN_CROCKERY_WITH_OUT_QUANTITY_REPORT);
	}

	@Override
	public FileBean generateKitchenCrockeryWithoutQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.GeneralFixAndCrockeryAllocation.CrockeryReport.class, StringUtils.dotSeparated(JasperParameterConstants.GENERAL_FIX_AND_CROCKERY_ALLOCATION_REPORT, JasperParameterConstants.GeneralFixAndCrockeryAllocation.CROCKERY_REPORT));
		parameters.put(ReportParameterConstants.NOTES, orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType) != null ? orderGeneralFixAndCrockeryAllocationReportQueryDao.findCrockeryNotesValue(langType).getNoteName() : "");
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<CrockeryWithoutQuantityReportDto> crockeryWithoutQuantityReportDtoList = orderGeneralFixAndCrockeryAllocationReportQueryDao.generateKitchenCrockeryWithoutQuantityWithoutMaxSettingReport(orderId, DataUtils.getLangType(langType), companySettingDto.getDisplayCrockeryAndGeneralFix());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_KITCHEN_CROCKERY_WITH_OUT_QUANTITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(crockeryWithoutQuantityReportDtoList, parameters, JasperReportNameConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_KITCHEN_CROCKERY_WITH_OUT_QUANTITY_REPORT);
	}

}