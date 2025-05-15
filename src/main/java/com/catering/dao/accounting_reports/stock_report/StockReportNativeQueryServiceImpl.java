package com.catering.dao.accounting_reports.stock_report;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.catering.bean.FileBean;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryServiceImpl;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.DateWiseStockReportDto;
import com.catering.dto.tenant.request.DateWiseStockSummaryReportDto;
import com.catering.dto.tenant.request.StockLedgerRawMaterialDropDownDto;
import com.catering.service.common.JasperReportService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link StockReportNativeQueryService} interface for handling stock report-related operations.
 * This service provides functionality to generate stock ledger reports, stock summary reports, 
 * and manage raw material dropdowns for filtering stock data.
 * 
 * The service leverages data from the {@link StockReportNativeQueryDao} and utilizes {@link JasperReportService}
 * for generating the required reports, while also incorporating company preferences via the 
 * {@link CompanyPreferencesNativeQueryServiceImpl}.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StockReportNativeQueryServiceImpl implements StockReportNativeQueryService {

	/**
	 * Data access object (DAO) for interacting with stock report-related data.
	 */
	StockReportNativeQueryDao stockReportNativeQueryDao;

	/**
	 * Service for generating Jasper reports.
	 */
	JasperReportService jasperReportService;

	/**
	 * Service for managing company preferences.
	 */
	CompanyPreferencesNativeQueryServiceImpl companyPreferencesNativeQueryServiceImpl;

	String RAW_MATERIAL_NAME = "RAW_MATERIAL_NAME";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StockLedgerRawMaterialDropDownDto> getRawMaterialForStockLedgerDropDown() {
		return stockReportNativeQueryDao.getRawMaterialForStockLedgerDropDown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseStockLedgerReport(LocalDate startDate, LocalDate endDate, Long rawMaterialId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.StockReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.STOCK_REPORT));
		setCommonDataInAllStockReport(parameters, langType, reportName, request);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		DateWiseStockReportDto constantData = stockReportNativeQueryDao.getRawMaterialNameForStockLedgerReport(rawMaterialId, langType);
		if (Objects.nonNull(constantData)) {
			parameters.put(RAW_MATERIAL_NAME, constantData.getRawMaterialName());
		}
		List<DateWiseStockReportDto> dateWiseStockLedgerReport = stockReportNativeQueryDao.getDateWiseStockLedgerReport(startDate, endDate, rawMaterialId, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_STOCK_LEDGER_REPORT, reportName);
		return jasperReportService.generatePdfReport(dateWiseStockLedgerReport, parameters, JasperReportNameConstant.DATE_WISE_STOCK_LEDGER_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getRawMaterialCategoryForStockSummaryDropDown() {
		return stockReportNativeQueryDao.getRawMaterialCategoryForStockSummaryDropDown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getRawMaterialForStockSummaryDropDown(Long rawMaterialCategoryId) {
		return stockReportNativeQueryDao.getRawMaterialForStockSummaryDropDown(rawMaterialCategoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateStockSummaryReport(Long rawMaterialCategoryId, Long rawMaterialId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.StockReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.STOCK_REPORT));
		setCommonDataInAllStockReport(parameters, langType, reportName, request);
		List<DateWiseStockSummaryReportDto> getStockSummaryReportData = stockReportNativeQueryDao.getStockSummaryReport(rawMaterialCategoryId, rawMaterialId, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_STOCK_SUMMARY_REPORT, reportName);
		return jasperReportService.generatePdfReport(getStockSummaryReportData, parameters, JasperReportNameConstant.DATE_WISE_STOCK_SUMMARY_REPORT);
	}

	/**
	 * Sets the common data required for generating stock reports.
	 * This includes setting the company logo, adding common company preferences to the report parameters,
	 * and configuring decimal patterns for proper number formatting.
	 * 
	 * @param parameters The map of parameters to be used in the Jasper report.
	 * @param langType The language type for localization.
	 * @param request The HTTP request used to retrieve company logo and other context-related data.
	 */
	private void setCommonDataInAllStockReport(Map<String, Object> parameters, Integer langType, String reportname, HttpServletRequest request) {
		jasperReportService.setCompanyLogo(parameters, reportname, request);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportname);
		jasperReportService.setDecimalPatterns(parameters);
	}

}