package com.catering.dao.accounting_reports.account_report;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.GeneralLedgerContactDropDownDto;

/**
 * Service interface for handling account-related reports. 
 * Provides methods for generating various types of reports, including account collection, general ledger, cash book, 
 * group summary, bank book, daily activity, and GST sales/purchase reports. 
 * Also provides methods to retrieve dropdown data for contact categories, suppliers, and bank contacts.
 * 
 * Each report is generated based on specific parameters, such as date ranges, contact categories, and language types.
 * These reports are returned as `FileBean` objects for further processing or downloading.
 */
public interface AccountReportNativeQueryService {

	/**
	 * Retrieves a list of contact category options for dropdown menus.
	 * 
	 * @return List of {@link DateWiseReportDropDownCommonDto} representing the contact categories.
	 */
	List<DateWiseReportDropDownCommonDto> getContactCategoryForDropDown();

	/**
	 * Retrieves a list of contacts for a given contact category, to be used in dropdown menus.
	 * 
	 * @param contactCategoryId The ID of the contact category.
	 * @return List of {@link DateWiseReportDropDownCommonDto} representing the contacts for the specified category.
	 */
	List<DateWiseReportDropDownCommonDto> getContactForDropDown(Long contactCategoryId);

	/**
	 * Generates an account collection report based on the provided contact category, contact, language type, and language code.
	 * 
	 * @param contactCategoryId The ID of the contact category.
	 * @param contactId The ID of the contact.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @param langCode The language code for the report.
	 * @param request The HTTP request for context (if needed for report generation).
	 * @return {@link FileBean} representing the generated account collection report.
	 */
	FileBean generateAccountCollectionReport(Long contactCategoryId, Long contactId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Retrieves a list of supplier contact options for the general ledger, to be used in dropdown menus.
	 * 
	 * @return List of {@link GeneralLedgerContactDropDownDto} representing the supplier contacts.
	 */
	List<GeneralLedgerContactDropDownDto> getGeneralLedgerSuppilerContactDropDownData();

	/**
	 * Generates a general ledger report based on the specified date range, supplier category, language type, and language code.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param supplierCategoryId The ID of the supplier category.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @param langCode The language code for the report.
	 * @param request The HTTP request for context (if needed for report generation).
	 * @return {@link FileBean} representing the generated general ledger report.
	 */
	FileBean generateGeneralLedgerReport(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a group summary report for a given contact category, language type, and language code.
	 * 
	 * @param contactCategoryId The ID of the contact category.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @param langCode The language code for the report.
	 * @param request The HTTP request for context (if needed for report generation).
	 * @return {@link FileBean} representing the generated group summary report.
	 */
	FileBean generateGroupSummaryReport(Long contactCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a cash book report based on the provided date range, language type, and language code.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @param langCode The language code for the report.
	 * @param request The HTTP request for context (if needed for report generation).
	 * @return {@link FileBean} representing the generated cash book report.
	 */
	FileBean generateCashBookReport(LocalDate startDate, LocalDate endDate, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Retrieves a list of bank contacts for the bank book report, to be used in dropdown menus.
	 * 
	 * @return List of {@link GeneralLedgerContactDropDownDto} representing the bank contacts.
	 */
	List<GeneralLedgerContactDropDownDto> getBankBookBankContactDropDownData();

	/**
	 * Generates a bank book report based on the specified date range, selected bank contact, language type, and language code.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param bankContactId The ID of the selected bank contact.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @param langCode The language code for the report.
	 * @param request The HTTP request for context (if needed for report generation).
	 * @return {@link FileBean} representing the generated bank book report.
	 */
	FileBean generateBankBookReport(LocalDate startDate, LocalDate endDate, Long bankContactId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a daily activity report based on the specified date range, language type, and language code.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @param langCode The language code for the report.
	 * @param request The HTTP request for context (if needed for report generation).
	 * @return {@link FileBean} representing the generated daily activity report.
	 */
	FileBean generateDailyActivityReport(LocalDate startDate, LocalDate endDate, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a GST sales report based on the provided date range, language type, and language code.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @param langCode The language code for the report.
	 * @param request The HTTP request for context (if needed for report generation).
	 * @return {@link FileBean} representing the generated GST sales report.
	 */
	FileBean generateGstSalesReport(LocalDate startDate, LocalDate endDate, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a GST purchase report based on the specified date range, language type, and language code.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @param langCode The language code for the report.
	 * @param request The HTTP request for context (if needed for report generation).
	 * @return {@link FileBean} representing the generated GST purchase report.
	 */
	FileBean generateGstPurchaseReport(LocalDate startDate, LocalDate endDate, Integer langType, String langCode, String reportName, HttpServletRequest request);

}