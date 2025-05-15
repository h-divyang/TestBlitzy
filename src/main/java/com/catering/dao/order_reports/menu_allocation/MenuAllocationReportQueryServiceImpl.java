package com.catering.dao.order_reports.menu_allocation;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
import com.catering.dao.order_reports.menu_preparation.MenuPreparationReportQueryService;
import com.catering.dto.tenant.request.ChefLabourSupplierWiseRawMaterialReportDto;
import com.catering.dto.tenant.request.ChefLabourWiseRawMaterialReportDto;
import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.CommonRawMaterialDto;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.MenuAllocationDetailRawMaterialReportDto;
import com.catering.dto.tenant.request.MenuAllocationMenuItemWiseRawMaterialReportDto;
import com.catering.dto.tenant.request.MenuAllocationMenuWithQuantityAndWithOutQuantityReportDto;
import com.catering.dto.tenant.request.MenuAllocationSupplierWiseRawMaterialReportDto;
import com.catering.dto.tenant.request.RawMaterialCategoryDirectOrderDto;
import com.catering.dto.tenant.request.RawMaterialChithhiReportDto;
import com.catering.dto.tenant.request.RawMaterialOrderFileReportDto;
import com.catering.dto.tenant.request.TotalRawMaterialReportDto;
import com.catering.repository.tenant.BookOrderRepository;
import com.catering.service.common.JasperReportService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.ResourceUtils;
import com.catering.util.StringUtils;

import java.util.Collections;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The `MenuAllocationReportQueryServiceImpl` class provides the implementation of the
 * `MenuAllocationReportQueryService` interface for generating item-wise raw material reports.
 * It utilizes the `MenuAllocationReportQueryDao` and `JasperReportService` to retrieve data and
 * generate PDF reports.
 *
 * This service class plays a key role in generating and formatting menu allocation reports
 * for the catering system.
 *
 * @author Krushali Talaviya
 * @since 2023-09-11
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuAllocationReportQueryServiceImpl implements MenuAllocationReportQueryService {

	/**
	 * Data access object for querying the Menu Allocation Report data.
	 */
	MenuAllocationReportQueryDao menuAllocationReportQueryDao;

	/**
	 * Service responsible for generating Jasper reports.
	 */
	JasperReportService jasperReportService;

	/**
	 * Service for executing native queries related to company preferences.
	 */
	CompanyPreferencesNativeQueryServiceImpl companyPreferencesNativeQueryServiceImpl;

	/**
	 * Initializes and manages time zone-related operations.
	 */
	TimeZoneInitializer timeZoneInitializer;

	/**
	 * Service for querying menu preparation report data.
	 */
	MenuPreparationReportQueryService menuPreparationReportQueryService;

	/** 
	 * Service for executing custom native queries and managing company preferences data.
	 */
	CompanyPreferencesNativeQueryService companyPreferencesNativeQueryService;

	/**
	 * Repository instance for handling database operations related to booking orders.
	 */
	BookOrderRepository bookOrderRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FunctionPerOrderDto> getFunctionPerOrder(Long orderId) {
		return menuAllocationReportQueryDao.getFunctionsPerOrder(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CommonDataForDropDownDto> getItemCategoryPerOrder(Long orderId) {
		return menuAllocationReportQueryDao.getItemCategoryPerOrder(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateMenuItemWiseRawMaterialReport(Long orderId, Long[] functionId, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuAllocationReport.MenuItemWiseRawMaterialReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_ALLOCATION_REPORT, JasperParameterConstants.MenuAllocationReport.MENU_ITEM_WISE_RAW_MATERIAL_REPORT));
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		String parameter = ResourceUtils.orderGeneralFixRawMaterial(langCode);
		jasperReportService.setDecimalPatterns(parameters);
		List<Long> functionIds = Arrays.asList(functionId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<MenuAllocationMenuItemWiseRawMaterialReportDto> menuAllocationMenuItemWiseRawMaterialReport = menuAllocationReportQueryDao.generateMenuItemWiseRawMaterialReport(orderId, functionIds, rawMaterialCategoryIds, DataUtils.getLangType(langType), parameter, bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_MENU_ITEM_WISE_RAW_MATERIAL_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuAllocationMenuItemWiseRawMaterialReport, parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_MENU_ITEM_WISE_RAW_MATERIAL_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object generateDetailRawMaterialReport(Long orderId, Boolean isDateTime, LocalDateTime orderDate, Long[] functionId, Long count, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request, boolean isWithQuantity, Boolean isPopUp) {
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<Long> functionIds = Arrays.asList(functionId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<MenuAllocationDetailRawMaterialReportDto> menuAllocationDetailRawMaterialReportDtos = menuAllocationReportQueryDao.generateDetailRawMaterialReport(orderId, isDateTime, orderDate, functionIds, count, rawMaterialCategoryIds, DataUtils.getLangType(langType), parameterArray[0],parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone(), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		if(Boolean.TRUE.equals(isPopUp)) {
			List<String> requiredFields = Arrays.asList(Constants.FUNCTION_NAME);
			return CommonRawMaterialDto.convert(menuAllocationDetailRawMaterialReportDtos, reportName, requiredFields);
		}
		Map<String, Object> parameters = setCommonDataRawMaterialReportWithOutVenue(orderId, langType, langCode, reportName, request);
		parameters.put(ReportParameterConstants.WITH_QUANTITY, isWithQuantity);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_DETAIL_RAW_MATERIAL_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuAllocationDetailRawMaterialReportDtos, parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_DETAIL_RAW_MATERIAL_REPORT);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Object generateTotalRawMaterialReport(Long orderId, Boolean isDateTime, LocalDateTime orderDate, Long[] functionId, Long count, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request, String jasperReportNameConstant, boolean isWithQuantity, Boolean isPopUp) {
		Object tenant = request.getAttribute(Constants.TENANT);
		List<TotalRawMaterialReportDto> rawMaterialListReportDto;
		Map<String, Object> commonData;
		List<Long> functionIds = Arrays.asList(functionId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		if (Objects.equals(tenant, Constants.ELITE_CATERERS_TENANT)) {
			rawMaterialListReportDto = menuAllocationReportQueryDao.generateEliteCaterersTotalRawMaterialReport(orderId, isDateTime, orderDate, functionIds, count, rawMaterialCategoryIds, DataUtils.getLangType(langType), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
			commonData = setCommonDataRawMaterialReport(orderId, langType, langCode, reportName, request);
		} else {
			rawMaterialListReportDto = menuAllocationReportQueryDao.generateTotalRawMaterialReport(orderId, isDateTime, orderDate, functionIds, count, rawMaterialCategoryIds, DataUtils.getLangType(langType), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
			commonData = setCommonDataRawMaterialReportWithOutVenue(orderId, langType, langCode, reportName, request);
		}
		if(Boolean.TRUE.equals(isPopUp)) {
			return CommonRawMaterialDto.convert(rawMaterialListReportDto, reportName, Collections.emptyList());
		}
		commonData.put(ReportParameterConstants.WITH_QUANTITY, isWithQuantity);
		jasperReportService.setBackgroundImageInGeneralReport(commonData, jasperReportNameConstant, reportName);
		return jasperReportService.generatePdfReport(rawMaterialListReportDto, commonData, jasperReportNameConstant);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object generateChefLabourWiseRawMaterialReport(Long orderId, Boolean isDateTime, LocalDateTime orderDate, Long[] orderTypeId, Long[] contactId, Long[] functionId, Long count, Long[] rawMaterialCategoryId, Long[] menuItemId, Integer langType, String langCode, String reportName, HttpServletRequest request, Boolean isPopUp) {
		List<ChefLabourWiseRawMaterialReportDto> rawMaterialListReportDto;
		Map<String, Object> commonData;
		List<Long> orderTypeIds = Arrays.asList(orderTypeId);
		List<Long> contactIds = Arrays.asList(contactId);
		List<Long> functionIds = Arrays.asList(functionId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<Long> menuItemIds = Arrays.asList(menuItemId);
		rawMaterialListReportDto = menuAllocationReportQueryDao.generateChefLabourWiseRawMaterialReport(orderId, isDateTime, orderDate, orderTypeIds, contactIds, functionIds, count, rawMaterialCategoryIds, menuItemIds, DataUtils.getLangType(langType), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		rawMaterialListReportDto.stream().filter(rawMaterialDto -> rawMaterialDto.getOrderType() != null && rawMaterialDto.getOrderType() == 1)
		// Creates groups of records that have the same venue and contactId
		.collect(Collectors.groupingBy(rawMaterialReportDto -> 
			Arrays.asList(rawMaterialReportDto.getVenue(), rawMaterialReportDto.getContactId()), Collectors.toList()))
			.values().forEach(menuItemList -> {
				// Sort menu items by menuPreparationMenuItemId to display them in the same sequence as in the raw material allocation
				menuItemList.sort((item1, item2) -> item1.getMenuPreparationMenuItemId().compareTo(item2.getMenuPreparationMenuItemId()));
				// Creates a comma-separated string of unique menuItem and person for the group
				String menuItemGroup = menuItemList.stream().filter(menuItem -> menuItem.getMenuItem() != null)
					.map(menuItem -> menuItem.getMenuItem() + " - " + menuItem.getPerson()).distinct()
					.collect(Collectors.joining(", "));
				menuItemList.forEach(menuItem -> menuItem.setMenuItemGroup(menuItemGroup));
			});
		if(Boolean.TRUE.equals(isPopUp)) {
			List<String> requiredFields = Arrays.asList(Constants.CONTACT_ID, Constants.CONTACT_NAME, Constants.MENU_ITEM_GROUP);
			return CommonRawMaterialDto.convert(rawMaterialListReportDto, reportName, requiredFields);
		}
		commonData = setCommonDataRawMaterialReportWithOutVenue(orderId, langType, langCode, reportName, request);
		jasperReportService.setBackgroundImageInGeneralReport(commonData, JasperReportNameConstant.MENU_ALLOCATION_REPORT_CHEF_LABOUR_WISE_RAW_MATERIAL_REPORT, reportName);
		return jasperReportService.generatePdfReport(rawMaterialListReportDto, commonData, JasperReportNameConstant.MENU_ALLOCATION_REPORT_CHEF_LABOUR_WISE_RAW_MATERIAL_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object generateChefLabourSupplierWiseRawMaterialReport(Long orderId, Boolean isDateTime, LocalDateTime orderDate, Long[] orderTypeId, Long[] contactId, Long[] functionId, Long count, Long[] rawMaterialCategoryId, Long[] menuItemId, Integer langType, String langCode, String reportName, HttpServletRequest request, Boolean isPopUp) {
		List<ChefLabourSupplierWiseRawMaterialReportDto> rawMaterialListReportDto;
		Map<String, Object> commonData;
		List<Long> orderTypeIds = Arrays.asList(orderTypeId);
		List<Long> contactIds = Arrays.asList(contactId);
		List<Long> functionIds = Arrays.asList(functionId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<Long> menuItemIds = Arrays.asList(menuItemId);
		rawMaterialListReportDto = menuAllocationReportQueryDao.generateChefLabourSupplierWiseRawMaterialReport(orderId, isDateTime, orderDate, orderTypeIds, contactIds, functionIds, count, rawMaterialCategoryIds, menuItemIds, DataUtils.getLangType(langType), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		if(Boolean.TRUE.equals(isPopUp)) {
			List<String> requiredFields = Arrays.asList(Constants.CONTACT_ID, Constants.CONTACT_NAME, Constants.SUPPLIER_ID, Constants.SUPPLIER_NAME);
			return CommonRawMaterialDto.convert(rawMaterialListReportDto, reportName, requiredFields);
		}
		commonData = setCommonDataRawMaterialReportWithOutVenue(orderId, langType, langCode, reportName, request);
		jasperReportService.setBackgroundImageInGeneralReport(commonData, JasperReportNameConstant.MENU_ALLOCATION_REPORT_CHEF_LABOUR_SUPPLIER_WISE_RAW_MATERIAL_REPORT, reportName);
		return jasperReportService.generatePdfReport(rawMaterialListReportDto, commonData, JasperReportNameConstant.MENU_ALLOCATION_REPORT_CHEF_LABOUR_SUPPLIER_WISE_RAW_MATERIAL_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateMenuWithAndWithOutQuantityReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request, String jasperReportNameConstant) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuAllocationReport.MenuWithQuantityReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_ALLOCATION_REPORT, JasperParameterConstants.MenuAllocationReport.MENU_WITH_QUANTITY_REPORT));
		List<Long> functionIds = Arrays.asList(functionId);
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, jasperReportNameConstant, reportName);
		List<MenuAllocationMenuWithQuantityAndWithOutQuantityReportDto> menuAllocationMenuWithQuantityReport = menuAllocationReportQueryDao.generateMenuWithAndWithOutQuantityReport(orderId, functionIds, DataUtils.getLangType(langType));
		return jasperReportService.generatePdfReport(menuAllocationMenuWithQuantityReport, parameters, jasperReportNameConstant);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateSupplierWiseRawMaterialReport(Long orderId, Integer langType, String langCode, String reportName, Long[] contactId, Long[] rawMaterialCategoryId, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuAllocationReport.SupplierWiseRawMaterialReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_ALLOCATION_REPORT, JasperParameterConstants.MenuAllocationReport.SUPPLIER_WISE_RAW_MATERIAL_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		menuPreparationReportQueryService.setTheCommonDataInParameterWithOutVenue(parameters, orderId, langType);
		jasperReportService.setDecimalPatterns(parameters);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<Long> contactIds = Arrays.asList(contactId);
		List<MenuAllocationSupplierWiseRawMaterialReportDto> menuAllocationSupplierWiseRawMaterialReportDtos = menuAllocationReportQueryDao.generateSupplierWiseRawMaterialReport(orderId, DataUtils.getLangType(langType), contactIds, rawMaterialCategoryIds, bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_SUPPLIER_WISE_RAW_MATERIAL_REPORT, reportName);
		return jasperReportService.generatePdfReport(menuAllocationSupplierWiseRawMaterialReportDtos, parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_SUPPLIER_WISE_RAW_MATERIAL_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateOrderFileReport(Long orderId, Long[] functionId, Long[] rawMaterialCategoryId, Long[] dataTypeId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuAllocationReport.OrderFileReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_ALLOCATION_REPORT, JasperParameterConstants.MenuAllocationReport.ORDER_FILE_REPORT));
		menuPreparationReportQueryService.setTheCommonDataInParameterWithOutVenue(parameters, orderId, langType);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<Long> functionIds = Arrays.asList(functionId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<Long> dataTypeIds = Arrays.asList(dataTypeId);
		List<RawMaterialOrderFileReportDto> rawMaterialOrderFileReport = menuAllocationReportQueryDao.generateOrderFileReport(orderId, functionIds, rawMaterialCategoryIds, dataTypeIds, DataUtils.getLangType(langType), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_ORDER_FILE_REPORT, reportName);
		return jasperReportService.generatePdfReport(rawMaterialOrderFileReport, parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_ORDER_FILE_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object generateRawMaterialA5Report(Long orderId, Long[] functionId, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request, Boolean isPopUp) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuAllocationReport.ChithhiReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_ALLOCATION_REPORT, JasperParameterConstants.MenuAllocationReport.CHITHHI_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<Long> functionIds = Arrays.asList(functionId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<RawMaterialChithhiReportDto> rawMaterialChithhiReport = menuAllocationReportQueryDao.generateChithhiReport(orderId, functionIds, rawMaterialCategoryIds, DataUtils.getLangType(langType), parameterArray[0], parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone(), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		if(Boolean.TRUE.equals(isPopUp)) {
			List<String> requiredFields = Arrays.asList(Constants.AGENCY, Constants.CONTACT_AGENCY_ID, Constants.ORDER_DATE_REF, Constants.ORDER_TIME_REF, Constants.TIME_PERIOD);
			return CommonRawMaterialDto.convert(rawMaterialChithhiReport, reportName, requiredFields);
		}
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_A5_REPORT, reportName);
		return jasperReportService.generatePdfReport(rawMaterialChithhiReport, parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_A5_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object generateRawMaterialA6Report(Long orderId, Long[] functionId, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request, Boolean isPopUp) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuAllocationReport.ChithhiReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_ALLOCATION_REPORT, JasperParameterConstants.MenuAllocationReport.CHITHHI_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<Long> functionIds = Arrays.asList(functionId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<RawMaterialChithhiReportDto> rawMaterialChithhiReport = menuAllocationReportQueryDao.generateChithhiReport(orderId, functionIds, rawMaterialCategoryIds, DataUtils.getLangType(langType), parameterArray[0], parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone(), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		if(Boolean.TRUE.equals(isPopUp)) {
			List<String> requiredFields = Arrays.asList(Constants.AGENCY, Constants.CONTACT_AGENCY_ID, Constants.ORDER_DATE_REF, Constants.ORDER_TIME_REF, Constants.TIME_PERIOD);
			return CommonRawMaterialDto.convert(rawMaterialChithhiReport, reportName, requiredFields);
		}
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_A6_REPORT, reportName);
		return jasperReportService.generatePdfReport(rawMaterialChithhiReport, parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_A6_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object generateRawMaterialA4Report(Long orderId, Long[] functionId, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request, Boolean isPopUp) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuAllocationReport.ChithhiReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_ALLOCATION_REPORT, JasperParameterConstants.MenuAllocationReport.CHITHHI_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		List<String> parameterList = ResourceUtils.getTimePeriod(langCode);
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<Long> functionIds = Arrays.asList(functionId);
		List<Long> rawMaterialCategoryIds = Arrays.asList(rawMaterialCategoryId);
		List<RawMaterialChithhiReportDto> rawMaterialChithhiReport = menuAllocationReportQueryDao.generateChithhiReport(orderId, functionIds, rawMaterialCategoryIds, DataUtils.getLangType(langType), parameterArray[0], parameterArray[1], parameterArray[2], parameterArray[3], timeZoneInitializer.getTimeZone(), bookOrderRepository.getAdjustQuantityByOrderId(orderId));
		if(Boolean.TRUE.equals(isPopUp)) {
			List<String> requiredFields = Arrays.asList(Constants.AGENCY, Constants.CONTACT_AGENCY_ID, Constants.ORDER_DATE_REF, Constants.ORDER_TIME_REF, Constants.TIME_PERIOD);
			return CommonRawMaterialDto.convert(rawMaterialChithhiReport, reportName, requiredFields);
		}
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_A4_REPORT, reportName);
		return jasperReportService.generatePdfReport(rawMaterialChithhiReport, parameters, JasperReportNameConstant.MENU_ALLOCATION_REPORT_A4_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RawMaterialCategoryDirectOrderDto> findRawMaterialCategoriesByDirectOrderAndOrderId(Long orderId) {
		return menuAllocationReportQueryDao.findRawMaterialCategoriesByDirectOrderAndOrderId(orderId);
	}

	/**
	 * Sets common data for the raw material report with venue information.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param request The HTTP servlet request object containing additional request data.
	 * @return A {@link Map} containing the parameters to be used for the report.
	 */
	private Map<String, Object> setCommonDataRawMaterialReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuAllocationReport.DetailRawMaterialReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_ALLOCATION_REPORT, JasperParameterConstants.MenuAllocationReport.DETAIL_RAW_MATERIAL_REPORT));
		menuPreparationReportQueryService.setTheCommonDataInParameter(parameters, orderId, langType, reportName);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		return parameters;
	}

	/**
	 * Sets common data for the raw material report without venue information.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param request The HTTP servlet request object containing additional request data.
	 * @return A {@link Map} containing the parameters to be used for the report.
	 */
	private Map<String, Object> setCommonDataRawMaterialReportWithOutVenue(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.MenuAllocationReport.DetailRawMaterialReport.class, StringUtils.dotSeparated(JasperParameterConstants.MENU_ALLOCATION_REPORT, JasperParameterConstants.MenuAllocationReport.DETAIL_RAW_MATERIAL_REPORT));
		menuPreparationReportQueryService.setTheCommonDataInParameterWithOutVenue(parameters, orderId, langType);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		return parameters;
	}

}