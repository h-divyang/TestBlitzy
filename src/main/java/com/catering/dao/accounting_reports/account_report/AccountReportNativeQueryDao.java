package com.catering.dao.accounting_reports.account_report;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.catering.dto.tenant.request.AccountCollectionReportDto;
import com.catering.dto.tenant.request.AccountDailyActivityReportDto;
import com.catering.dto.tenant.request.AccountGroupSummaryReportDto;
import com.catering.dto.tenant.request.AccountBankBookReportDto;
import com.catering.dto.tenant.request.AccountCashBookReportDto;
import com.catering.dto.tenant.request.DateWiseGeneralLedgerReportDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.GeneralLedgerContactDropDownDto;
import com.catering.dto.tenant.request.GstSalesPurchaseReportCommonDto;

/**
 * Repository interface for accessing account-related reports via native SQL queries.
 * This interface extends {@link JpaRepository} to provide CRUD operations on the {@link AccountReportNativeQuery} entity.
 * It contains methods to retrieve various types of reports and dropdown data for account-related information.
 * 
 * The methods in this interface utilize native SQL queries, defined by the `@Query` annotations, to fetch data 
 * such as contact categories, collection reports, general ledger data, and GST-related reports.
 */
public interface AccountReportNativeQueryDao extends JpaRepository<AccountReportNativeQuery, Long> {

	/**
	 * Retrieves a list of contact category options for dropdown menus.
	 * 
	 * @return List of {@link DateWiseReportDropDownCommonDto} representing the contact categories.
	 */
	@Query(name = "getContactCategoryForDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getContactCategoryForDropDown();

	/**
	 * Retrieves a list of contacts for a given contact category, to be used in dropdown menus.
	 * 
	 * @param contactCategoryId The ID of the contact category.
	 * @return List of {@link DateWiseReportDropDownCommonDto} representing the contacts for the specified category.
	 */
	@Query(name = "getContactForDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getContactForDropDown(Long contactCategoryId);

	/**
	 * Retrieves a collection report for a specified contact category and contact.
	 * 
	 * @param contactCategoryId The ID of the contact category.
	 * @param contactId The ID of the contact.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @return List of {@link AccountCollectionReportDto} containing the collection report data.
	 */
	@Query(name = "getCollectionReport", nativeQuery = true)
	List<AccountCollectionReportDto> getCollectionReport(Long contactCategoryId, Long contactId, Integer langType);

	/**
	 * Retrieves a list of supplier contact options for the general ledger, to be used in dropdown menus.
	 * 
	 * @return List of {@link GeneralLedgerContactDropDownDto} representing the supplier contacts.
	 */
	@Query(name = "getGeneralLedgerSuppilerContactForDropDown", nativeQuery = true)
	List<GeneralLedgerContactDropDownDto> getGeneralLedgerContactSuppilerForDropDown();

	/**
	 * Retrieves the supplier contact name for the general ledger report based on the supplier category and language.
	 * 
	 * @param supplierCategoryId The ID of the supplier category.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @return {@link DateWiseGeneralLedgerReportDto} containing the supplier contact name.
	 */
	@Query(name = "getSuppilerContactNameForGeneralLedgerReport", nativeQuery = true)
	DateWiseGeneralLedgerReportDto getSuppilerContactNameForGeneralLedgerReport(Long supplierCategoryId, Integer langType);

	/**
	 * Retrieves general ledger report data based on the supplier category and a date range.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param supplierCategoryId The ID of the supplier category.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @return List of {@link DateWiseGeneralLedgerReportDto} containing the general ledger report data.
	 */

	@Query(name = "getGeneralLedgerReportData", nativeQuery = true)
	List<DateWiseGeneralLedgerReportDto> getGeneralLedgerReportData(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Integer langType);

	/**
	 * Retrieves a group summary report for a given contact category and language type.
	 * 
	 * @param contactCategoryId The ID of the contact category.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @return List of {@link AccountGroupSummaryReportDto} containing the group summary report data.
	 */
	@Query(name = "getGroupSummaryReport", nativeQuery = true)
	List<AccountGroupSummaryReportDto> getGroupSummaryReport(Long contactCategoryId, Integer langType);

	/**
	 * Retrieves cash book report data based on a date range and language type.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @return List of {@link AccountCashBookReportDto} containing the cash book report data.
	 */
	@Query(name = "getCashBookReport", nativeQuery = true)
	List<AccountCashBookReportDto> getCashBookReportData(LocalDate startDate, LocalDate endDate, Integer langType);

	/**
	 * Retrieves a list of bank supplier contacts for the bank book report, to be used in dropdown menus.
	 * 
	 * @return List of {@link GeneralLedgerContactDropDownDto} representing the bank supplier contacts.
	 */
	@Query(name = "getBankBookBankContactForDropDown", nativeQuery = true)
	List<GeneralLedgerContactDropDownDto> getBankBookBankSuppilerForDropDown();

	/**
	 * Retrieves bank book report data based on a date range and selected bank contact.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param bankContactId The ID of the selected bank contact.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @return List of {@link AccountBankBookReportDto} containing the bank book report data.
	 */
	@Query(name = "getBankBookReportData", nativeQuery = true)
	List<AccountBankBookReportDto> getBankBookReportData(LocalDate startDate, LocalDate endDate, Long bankContactId, Integer langType);

	/**
	 * Retrieves daily activity stock details report data based on a date range and language type.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @return List of {@link AccountDailyActivityReportDto} containing the daily activity stock details report data.
	 */
	@Query(name = "getDailyActivityStockDetailsReport", nativeQuery = true)
	List<AccountDailyActivityReportDto> getDailyActivityStockReportData(LocalDate startDate, LocalDate endDate, Integer langType);

	/**
	 * Retrieves daily activity account details report data based on a date range and language type.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @return List of {@link AccountDailyActivityReportDto} containing the daily activity account details report data.
	 */
	@Query(name = "getDailyActivityAccountDetailsReport", nativeQuery = true)
	List<AccountDailyActivityReportDto> getDailyActivityAccountReportData(LocalDate startDate, LocalDate endDate, Integer langType);

	/**
	 * Retrieves GST sales report data based on a date range and language type.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @return List of {@link GstSalesPurchaseReportCommonDto} containing the GST sales report data.
	 */
	@Query(name = "getGstSalesReportData", nativeQuery = true)
	List<GstSalesPurchaseReportCommonDto> getGstSalesReportData(LocalDate startDate, LocalDate endDate, Integer langType);

	/**
	 * Retrieves GST purchase report data based on a date range and language type.
	 * 
	 * @param startDate The start date of the report.
	 * @param endDate The end date of the report.
	 * @param langType The language type for the report (1 for preferred language, 2 for supportive language).
	 * @return List of {@link GstSalesPurchaseReportCommonDto} containing the GST purchase report data.
	 */
	@Query(name = "getGstPurchaseReportData", nativeQuery = true)
	List<GstSalesPurchaseReportCommonDto> getGstPurchaseReportData(LocalDate startDate, LocalDate endDate, Integer langType);

}