package com.catering.dao.accounting_reports.account_report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
import com.catering.dto.tenant.request.AccountCollectionReportDto;
import com.catering.dto.tenant.request.AccountDailyActivityReportDto;
import com.catering.dto.tenant.request.AccountGroupSummaryReportDto;
import com.catering.dto.tenant.request.CompanySettingDto;
import com.catering.dto.tenant.request.AccountBankBookReportDto;
import com.catering.dto.tenant.request.AccountCashBookReportDto;
import com.catering.dto.tenant.request.DateWiseGeneralLedgerReportDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.GeneralLedgerContactDropDownDto;
import com.catering.dto.tenant.request.GstSalesPurchaseReportCommonDto;
import com.catering.service.common.JasperReportService;
import com.catering.service.tenant.CompanySettingService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link AccountReportNativeQueryService} interface for generating various account-related reports.
 * This class interacts with data access objects (DAOs), services, and other dependencies to gather necessary data and
 * generate reports based on specific criteria. The reports include Account Collection, General Ledger, Cash Book,
 * Group Summary, Bank Book, Daily Activity, and GST Sales/Purchase reports.
 * 
 * The implementation makes use of the `JasperReportService` to generate reports, `AccountReportNativeQueryDao` 
 * for data retrieval, and various other services like `CompanyPreferencesNativeQueryServiceImpl` and `CompanySettingService` 
 * to configure report parameters based on company settings and preferences.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountReportNativeQueryServiceImpl implements AccountReportNativeQueryService {

	/**
	 * Data access object (DAO) for executing native queries related to account reports.
	 * Provides methods to retrieve data for various types of reports such as collection, general ledger, cash book, etc.
	 */
	AccountReportNativeQueryDao accountReportNativeQueryDao;

	/**
	 * Service for generating reports using JasperReports.
	 * This service handles the report generation logic, including the passing of parameters and the rendering of the reports.
	 */
	JasperReportService jasperReportService;

	/**
	 * Service that retrieves and manages company preferences related to reports.
	 * Provides methods for retrieving company-specific settings like name, address, and other report-related preferences.
	 */
	CompanyPreferencesNativeQueryServiceImpl companyPreferencesNativeQueryServiceImpl;

	/**
	 * Service for managing company settings.
	 * Provides configuration details about the company that are used to customize reports, including preferences and parameters.
	 */
	CompanySettingService companySettingService;

	/**
	 * Constant for the bank contact name parameter used in reports.
	 * This is used as a key when passing the bank contact name information to the report parameters.
	 */
	String BANK_CONTACT_NAME = "BANK_CONTACT_NAME";

	/**
	 * Constant for the stock data flag used in reports.
	 * This flag indicates whether the report is related to stock data or not.
	 */
	String IS_STOCK_DATA = "IS_STOCK_DATA";

	/**
	 * Constant for the account data flag used in reports.
	 * This flag indicates whether the report is related to account data or not.
	 */
	String IS_ACCOUNT_DATA = "IS_ACCOUNT_DATA";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getContactCategoryForDropDown() {
		return accountReportNativeQueryDao.getContactCategoryForDropDown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getContactForDropDown(Long contactCategoryId) {
		return accountReportNativeQueryDao.getContactForDropDown(contactCategoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateAccountCollectionReport(Long contactCategoryId, Long contactId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.AccountReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.ACCOUNT_REPORT));
		setCommonDataInAllAccountReport(parameters, langType, reportName, request);
		List<AccountCollectionReportDto> getCollectionReportData = accountReportNativeQueryDao.getCollectionReport(contactCategoryId, contactId, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ACCOUNT_COLLECTION_REPORT, reportName);
		return jasperReportService.generatePdfReport(getCollectionReportData, parameters, JasperReportNameConstant.ACCOUNT_COLLECTION_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GeneralLedgerContactDropDownDto> getGeneralLedgerSuppilerContactDropDownData() {
		return accountReportNativeQueryDao.getGeneralLedgerContactSuppilerForDropDown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateGeneralLedgerReport(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.AccountReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.ACCOUNT_REPORT));
		setCommonDataInAllAccountReport(parameters, langType, reportName, request);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<DateWiseGeneralLedgerReportDto> getGeneralLedgerReportData = accountReportNativeQueryDao.getGeneralLedgerReportData(startDate, endDate, supplierCategoryId, langType);
		// If there is no transaction then set default data to 0 for all
		if (getGeneralLedgerReportData == null || getGeneralLedgerReportData.isEmpty()) {
			getGeneralLedgerReportData = new ArrayList<>();
			CompanySettingDto companySettingDto = companySettingService.getCompannySetting(false);
			DateWiseGeneralLedgerReportDto suppilerContactName = accountReportNativeQueryDao.getSuppilerContactNameForGeneralLedgerReport(supplierCategoryId, langType); 
			DateWiseGeneralLedgerReportDto newItem = new DateWiseGeneralLedgerReportDto(supplierCategoryId, suppilerContactName.getContactName() != null ? suppilerContactName.getContactName() : "", null, 0l, "OPB", 0.0, 0.0, 0.0, "", companySettingDto.getDecimalLimitForCurrency());
			getGeneralLedgerReportData.add(newItem);
		}
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_GENERAL_LEDGER_REPORT, reportName);
		return jasperReportService.generatePdfReport(getGeneralLedgerReportData, parameters, JasperReportNameConstant.DATE_WISE_GENERAL_LEDGER_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateGroupSummaryReport(Long contactCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.GroupSummaryReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.GROUP_SUMMARY_REPORT));
		setCommonDataInAllAccountReport(parameters, langType, reportName, request);
		List<AccountGroupSummaryReportDto> getGroupSummaryReportData = accountReportNativeQueryDao.getGroupSummaryReport(contactCategoryId, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ACCOUNT_GROUP_SUMMARY_REPORT, reportName);
		return jasperReportService.generatePdfReport(getGroupSummaryReportData, parameters, JasperReportNameConstant.ACCOUNT_GROUP_SUMMARY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCashBookReport(LocalDate startDate, LocalDate endDate, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.CashBookReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.CASH_BOOK_REPORT));
		setCommonDataInAllAccountReport(parameters, langType, reportName, request);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		List<AccountCashBookReportDto> getCashBookReportData = accountReportNativeQueryDao.getCashBookReportData(startDate, endDate, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ACCOUNT_CASH_BOOK_REPORT, reportName);
		return jasperReportService.generatePdfReport(getCashBookReportData, parameters, JasperReportNameConstant.ACCOUNT_CASH_BOOK_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GeneralLedgerContactDropDownDto> getBankBookBankContactDropDownData() {
		return accountReportNativeQueryDao.getBankBookBankSuppilerForDropDown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateBankBookReport(LocalDate startDate, LocalDate endDate, Long bankContactId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.CashBookReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.CASH_BOOK_REPORT));
		setCommonDataInAllAccountReport(parameters, langType, reportName, request);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		DateWiseGeneralLedgerReportDto suppilerContactName = accountReportNativeQueryDao.getSuppilerContactNameForGeneralLedgerReport(bankContactId, langType); 
		if (Objects.nonNull(suppilerContactName)) {
			parameters.put(BANK_CONTACT_NAME, suppilerContactName.getContactName());
		}
		List<AccountBankBookReportDto> getBankBookReportData = accountReportNativeQueryDao.getBankBookReportData(startDate, endDate, bankContactId, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ACCOUNT_BANK_BOOK_REPORT, reportName);
		return jasperReportService.generatePdfReport(getBankBookReportData, parameters, JasperReportNameConstant.ACCOUNT_BANK_BOOK_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDailyActivityReport(LocalDate startDate, LocalDate endDate, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.DailyActivityReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.DAILY_ACTIVITY_REPORT));
		setCommonDataInAllAccountReport(parameters, langType, reportName, request);
		parameters.put(ReportParameterConstants.START_DATE, startDate);
		parameters.put(ReportParameterConstants.END_DATE, endDate);
		parameters.put(ReportParameterConstants.SUB_REPORT_STOCK_DETAILS_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.ACCOUNT_DAILY_ACTIVITY_STOCK_REPORT));
		parameters.put(ReportParameterConstants.SUB_REPORT_ACCOUNT_DETAILS_PATH, jasperReportService.getJasperReport(JasperReportNameConstant.ACCOUNT_DAILY_ACTIVITY_ACCOUNT_REPORT));
		List<AccountDailyActivityReportDto> getDailyActivityStockReportData = accountReportNativeQueryDao.getDailyActivityStockReportData(startDate, endDate, langType);
		List<AccountDailyActivityReportDto> getDailyActivityAccountReportData = accountReportNativeQueryDao.getDailyActivityAccountReportData(startDate, endDate, langType);
		boolean isStockData = getDailyActivityStockReportData != null && !getDailyActivityStockReportData.isEmpty();
		boolean isAccountData = getDailyActivityAccountReportData != null && !getDailyActivityAccountReportData.isEmpty();
		parameters.put(IS_STOCK_DATA, isStockData);
		parameters.put(IS_ACCOUNT_DATA, isAccountData);
		parameters.put(ReportParameterConstants.SUB_REPORT_STOCK_DETAILS_DATASOURCE,new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(getDailyActivityStockReportData));
		parameters.put(ReportParameterConstants.SUB_REPORT_ACCOUNT_DETAILS_DATASOURCE,new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(getDailyActivityAccountReportData));
		List<Object> mainReportData = Collections.singletonList(new Object());  // Create empty object to pass in main report
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.ACCOUNT_DAILY_ACTIVITY_REPORT, reportName);
		return jasperReportService.generatePdfReport(mainReportData, parameters, JasperReportNameConstant.ACCOUNT_DAILY_ACTIVITY_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateGstSalesReport(LocalDate startDate, LocalDate endDate, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.GstSalePurchaseReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.GST_SALES_PURCHASE_REPORT));
		setCommonDataInAllAccountReport(parameters, langType, reportName, request);
		List<GstSalesPurchaseReportCommonDto> getReportData = accountReportNativeQueryDao.getGstSalesReportData(startDate, endDate, langType);
		int reportType = -1;
		boolean allIgstZero = getReportData.stream().allMatch(data -> data.getIgstRate() == 0);
		boolean allCgstAndSgstZero = getReportData.stream().allMatch(data -> data.getCgstRate() == 0 && data.getSgstRate() == 0);
		if (allIgstZero) {
			reportType = 0;
		} else if (allCgstAndSgstZero) {
			reportType = 1;
		} else {
			reportType = 2;
		}
		jasperReportService.setBackgroundImageInGeneralReport(parameters, getReportTypeForGstSalesAndPurchase(reportType, true), reportName);
		return jasperReportService.generatePdfReport(getReportData, parameters, getReportTypeForGstSalesAndPurchase(reportType, true));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateGstPurchaseReport(LocalDate startDate, LocalDate endDate, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.GstSalePurchaseReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.GST_SALES_PURCHASE_REPORT));
		setCommonDataInAllAccountReport(parameters, langType, reportName, request);
		List<GstSalesPurchaseReportCommonDto> getReportData = accountReportNativeQueryDao.getGstPurchaseReportData(startDate, endDate, langType);
		int reportType = -1;
		boolean allIgstZero = getReportData.stream().allMatch(data -> data.getIgstPrice() == 0);
		boolean allCgstAndSgstZero = getReportData.stream().allMatch(data -> data.getCgstPrice() == 0 && data.getSgstPrice() == 0);
		if (allIgstZero) {
			reportType = 0;
		} else if (allCgstAndSgstZero) {
			reportType = 1;
		} else {
			reportType = 2;
		}
		jasperReportService.setBackgroundImageInGeneralReport(parameters, getReportTypeForGstSalesAndPurchase(reportType, true), reportName);
		return jasperReportService.generatePdfReport(getReportData, parameters, getReportTypeForGstSalesAndPurchase(reportType, false));
	}

	/**
	 * Determines the report type for GST sales or purchase based on the given report type and GST sales flag.
	 * 
	 * @param reportType The type of the report (0, 1, or 2), where:
	 *                   0 = CGST/SGST report
	 *                   1 = IGST report
	 *                   2 = Combined GST report
	 * @param isGstSales A flag indicating whether the report is for GST sales (true) or GST purchase (false)
	 * @return The appropriate report type constant based on the input parameters
	 * @throws IllegalArgumentException If an invalid report type is provided
	 */
	private String getReportTypeForGstSalesAndPurchase(int reportType, boolean isGstSales) {
		switch (reportType) {
			case 0:
				return isGstSales ? JasperReportNameConstant.ACCOUNT_GST_SALES_CGST_SGST_REPORT : JasperReportNameConstant.ACCOUNT_GST_PURCHASE_CGST_SGST_REPORT;
			case 1:
				return isGstSales ? JasperReportNameConstant.ACCOUNT_GST_SALES_IGST_REPORT : JasperReportNameConstant.ACCOUNT_GST_PURCHASE_IGST_REPORT;
			case 2:
				return isGstSales ? JasperReportNameConstant.ACCOUNT_GST_SALES_COMBINE_GST_REPORT : JasperReportNameConstant.ACCOUNT_GST_PURCHASE_COMBINE_GST_REPORT;
			default:
				throw new IllegalArgumentException("Invalid report type: " + reportType);
		}
	}

	/**
	 * Sets common data to be used in all account reports.
	 * This method adds the company logo, company preferences, and decimal patterns to the report parameters.
	 * 
	 * @param parameters The map of report parameters to which the common data will be added
	 * @param langType The language type to determine the localization of the data
	 * @param request The HTTP request used to retrieve the company logo and other preferences
	 */
	private void setCommonDataInAllAccountReport(Map<String, Object> parameters, Integer langType, String reportName, HttpServletRequest request) {
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setDecimalPatterns(parameters);
	}

}