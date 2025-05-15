package com.catering.dao.order_reports.labour_and_agency;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.catering.bean.FileBean;
import com.catering.config.TimeZoneInitializer;
import com.catering.constant.Constants;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryService;
import com.catering.dto.tenant.request.AdminReportOutsideChithhiReportDto;
import com.catering.dto.tenant.request.ChefOrOutsideLabourReportParams;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.LabourAndAgencyBookingReportCommonValueDto;
import com.catering.dto.tenant.request.LabourAndAgencyBookingReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyChefLabourChithhiReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyChefLabourReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyLabourChithhiReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyLabourReportDto;
import com.catering.dto.tenant.request.LabourReportParams;
import com.catering.service.common.JasperReportService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.ResourceUtils;
import com.catering.util.StringUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LabourAndAgencyReportQueryServiceImpl implements LabourAndAgencyReportQueryService {

	JasperReportService jasperReportService;

	LabourAndAgencyReportQueryDao labourAndAgencyReportQueryDao;

	TimeZoneInitializer timeZoneInitializer;

	CompanyPreferencesNativeQueryService companyPreferencesNativeQueryService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateBookingReport(Long orderId, Integer langType, String langCode, String reportName) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.LabourAndAgencyReport.BookingReport.class, StringUtils.dotSeparated(JasperParameterConstants.LABOUR_AND_AGENCY_REPORT, JasperParameterConstants.LabourAndAgencyReport.BOOKING_REPORT));
		LabourAndAgencyBookingReportCommonValueDto labourAndAgencyBookingReportCommonValue = labourAndAgencyReportQueryDao.getBookingReportCommonValue(orderId, langType);
		if (Objects.nonNull(labourAndAgencyBookingReportCommonValue)) {
			parameters.put(ReportParameterConstants.CUSTOMER_NAME_VALUE, labourAndAgencyBookingReportCommonValue.getCustomerName());
			parameters.put(ReportParameterConstants.CUSTOMER_NUMBER_VALUE, labourAndAgencyBookingReportCommonValue.getCustomerNumber());
			parameters.put(ReportParameterConstants.CUSTOMER_ADDRESS_VALUE, labourAndAgencyBookingReportCommonValue.getCustomerAddress());
			parameters.put(ReportParameterConstants.CUSTOMER_HOME_ADDRESS_VALUE, labourAndAgencyBookingReportCommonValue.getCustomerHomeAddress());
		}
		List<LabourAndAgencyBookingReportDto> labourAndAgencyBookingReport = labourAndAgencyReportQueryDao.getBookingReportValue(orderId, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_REPORT_BOOKING_REPORT, reportName);
		return jasperReportService.generatePdfReport(labourAndAgencyBookingReport, parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_REPORT_BOOKING_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateChefLabourReport(Long orderId, ChefOrOutsideLabourReportParams params, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(params.getLangCode()), JasperParameterConstants.LabourAndAgencyReport.ChefLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.LABOUR_AND_AGENCY_REPORT, JasperParameterConstants.LabourAndAgencyReport.CHEF_LABOUR_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		jasperReportService.setBackgroundImageInReport(parameters);
		List<Long> contactIds = Arrays.asList(params.getContactId());
		List<Long> functionIds = Arrays.asList(params.getFunctionId());
		List<Long> menuItemIds = Arrays.asList(params.getMenuItemId());
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, DataUtils.getLangType(params.getLangType()), reportName);
		List<LabourAndAgencyChefLabourReportDto> labourAndAgencyChefLabourReport = labourAndAgencyReportQueryDao.generateChefLabourReport(orderId, DataUtils.getLangType(params.getLangType()), Constants.ORDER_TYPE_FOR_CHEF_LABOUR, contactIds, functionIds, menuItemIds);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_CHEF_LABOUR_REPORT, reportName);
		return jasperReportService.generatePdfReport(labourAndAgencyChefLabourReport, parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_CHEF_LABOUR_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateOutsideAgencyReport(Long orderId, ChefOrOutsideLabourReportParams params, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(params.getLangCode()), JasperParameterConstants.LabourAndAgencyReport.ChefLabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.LABOUR_AND_AGENCY_REPORT, JasperParameterConstants.LabourAndAgencyReport.CHEF_LABOUR_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<Long> contactIds = Arrays.asList(params.getContactId());
		List<Long> functionIds = Arrays.asList(params.getFunctionId());
		List<Long> menuItemIds = Arrays.asList(params.getMenuItemId());
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, DataUtils.getLangType(params.getLangType()), reportName);
		List<LabourAndAgencyChefLabourReportDto> labourAndAgencyOutsideAgencyReport = labourAndAgencyReportQueryDao.generateChefLabourReport(orderId, DataUtils.getLangType(params.getLangType()), Constants.ORDER_TYPE_FOR_OUT_SIDE_LABOUR, contactIds, functionIds, menuItemIds);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_OUTSIDE_AGENCY_REPORT, reportName);
		return jasperReportService.generatePdfReport(labourAndAgencyOutsideAgencyReport, parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_OUTSIDE_AGENCY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateChefLabourChithhiReport(Long orderId, ChefOrOutsideLabourReportParams params, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(params.getLangCode()), JasperParameterConstants.LabourAndAgencyReport.ChefLabourChithhiReport.class, StringUtils.dotSeparated(JasperParameterConstants.LABOUR_AND_AGENCY_REPORT, JasperParameterConstants.LabourAndAgencyReport.CHEF_LABOUR_CHITHHI_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		jasperReportService.setBackgroundImageInReport(parameters);
		List<String> parameterList = ResourceUtils.getTimePeriod(params.getLangCode());
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<Long> contactIds = Arrays.asList(params.getContactId());
		List<Long> functionIds = Arrays.asList(params.getFunctionId());
		List<Long> menuItemIds = Arrays.asList(params.getMenuItemId());
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, DataUtils.getLangType(params.getLangType()), reportName);
		List<LabourAndAgencyChefLabourChithhiReportDto> labourAndAgencyChefLabourChithhiReport = labourAndAgencyReportQueryDao.generateChefLabourChithhiReport(orderId, DataUtils.getLangType(params.getLangType()), Constants.ORDER_TYPE_FOR_CHEF_LABOUR, parameterArray[0],  parameterArray[1], parameterArray[2], parameterArray[3], contactIds, functionIds, menuItemIds, timeZoneInitializer.getTimeZone());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_CHEF_LABOUR_CHITHHI_REPORT, reportName);
		return jasperReportService.generatePdfReport(labourAndAgencyChefLabourChithhiReport, parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_CHEF_LABOUR_CHITHHI_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateOutsideAgencyChithhiReport(Long orderId, ChefOrOutsideLabourReportParams params, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(params.getLangCode()), JasperParameterConstants.LabourAndAgencyReport.ChefLabourChithhiReport.class, StringUtils.dotSeparated(JasperParameterConstants.LABOUR_AND_AGENCY_REPORT, JasperParameterConstants.LabourAndAgencyReport.CHEF_LABOUR_CHITHHI_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		jasperReportService.setDecimalPatterns(parameters);
		List<String> parameterList = ResourceUtils.getTimePeriod(params.getLangCode());
		String[] parameterArray = parameterList.toArray(new String[parameterList.size()]);
		List<Long> contactIds = Arrays.asList(params.getContactId());
		List<Long> functionIds = Arrays.asList(params.getFunctionId());
		List<Long> menuItemIds = Arrays.asList(params.getMenuItemId());
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, DataUtils.getLangType(params.getLangType()), reportName);
		List<AdminReportOutsideChithhiReportDto> labourAndAgencyOutsideAgencyChithhiReport = labourAndAgencyReportQueryDao.generateOutsideChithhiReport(orderId, DataUtils.getLangType(params.getLangType()), Constants.ORDER_TYPE_FOR_OUT_SIDE_LABOUR, parameterArray[0], parameterArray[1], parameterArray[2], parameterArray[3], contactIds, functionIds, menuItemIds, timeZoneInitializer.getTimeZone());
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_OUTSIDE_AGENCY_CHITHHI_REPORT, reportName);
		return jasperReportService.generatePdfReport(labourAndAgencyOutsideAgencyChithhiReport, parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_OUTSIDE_AGENCY_CHITHHI_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateLabourReport(Long orderId, LabourReportParams labourReportParams, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(labourReportParams.getLangCode()), JasperParameterConstants.LabourAndAgencyReport.LabourReport.class, StringUtils.dotSeparated(JasperParameterConstants.LABOUR_AND_AGENCY_REPORT, JasperParameterConstants.LabourAndAgencyReport.LABOUR_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		LabourAndAgencyBookingReportCommonValueDto labourAndAgencyBookingReportCommonValue = labourAndAgencyReportQueryDao.getBookingReportCommonValue(orderId, labourReportParams.getLangType());
		if (Objects.nonNull(labourAndAgencyBookingReportCommonValue)) {
			parameters.put(ReportParameterConstants.CUSTOMER_NAME_VALUE, labourAndAgencyBookingReportCommonValue.getCustomerName());
			parameters.put(ReportParameterConstants.CUSTOMER_NUMBER_VALUE, labourAndAgencyBookingReportCommonValue.getCustomerNumber());
			parameters.put(ReportParameterConstants.CUSTOMER_ADDRESS_VALUE, labourAndAgencyBookingReportCommonValue.getCustomerAddress());
			parameters.put(ReportParameterConstants.VENUE_VALUE, labourAndAgencyBookingReportCommonValue.getVenue());
		}
		List<Long> supplierCategoryIds = Arrays.asList(labourReportParams.getSupplierCategoryId());
		List<Long> supplierIds = Arrays.asList(labourReportParams.getSupplierId());
		List<Long> functionIds = Arrays.asList(labourReportParams.getFunctionId());
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, DataUtils.getLangType(labourReportParams.getLangType()), reportName);
		List<LabourAndAgencyLabourReportDto> labourAndAgencyLabourReport = labourAndAgencyReportQueryDao.generateLabourReport(orderId, DataUtils.getLangType(labourReportParams.getLangType()), supplierCategoryIds, supplierIds, functionIds);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_REPORT_LABOUR_REPORT, reportName);
		return jasperReportService.generatePdfReport(labourAndAgencyLabourReport, parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_REPORT_LABOUR_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateLabourChithhiReport(Long orderId, LabourReportParams labourReportParams, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(labourReportParams.getLangCode()), JasperParameterConstants.LabourAndAgencyReport.LabourChithhiReport.class, StringUtils.dotSeparated(JasperParameterConstants.LABOUR_AND_AGENCY_REPORT, JasperParameterConstants.LabourAndAgencyReport.LABOUR_CHITHHI_REPORT));
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		List<Long> supplierCategoryIds = Arrays.asList(labourReportParams.getSupplierCategoryId());
		List<Long> supplierIds = Arrays.asList(labourReportParams.getSupplierId());
		List<Long> functionIds = Arrays.asList(labourReportParams.getFunctionId());
		companyPreferencesNativeQueryService.setTheCommonDataInParameters(parameters, DataUtils.getLangType(labourReportParams.getLangType()), reportName);
		List<LabourAndAgencyLabourChithhiReportDto> labourAndAgencyLabourChithhiReport = labourAndAgencyReportQueryDao.generateLabourChithhiReport(orderId, DataUtils.getLangType(labourReportParams.getLangType()), supplierCategoryIds, supplierIds, functionIds);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_REPORT_LABOUR_CHITHHI_REPORT, reportName);
		return jasperReportService.generatePdfReport(labourAndAgencyLabourChithhiReport, parameters, JasperReportNameConstant.LABOUR_AND_AGENCY_REPORT_LABOUR_CHITHHI_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FunctionPerOrderDto> findFunctionForChefOrOutside(Long orderId, Integer orderType) {
		return labourAndAgencyReportQueryDao.findFunctionForCheforOutside(orderId, orderType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FunctionPerOrderDto> findFunctionForLabour(Long orderId) {
		return labourAndAgencyReportQueryDao.findFunctionForLabour(orderId);
	}
}
