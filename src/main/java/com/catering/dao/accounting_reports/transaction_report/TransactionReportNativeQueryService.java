package com.catering.dao.accounting_reports.transaction_report;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;

/**
 * Service interface for generating various transaction-related reports, including 
 * cash payment receipts, bank payment receipts, purchase orders, purchase bills, 
 * debit notes, and raw material transfers. This service handles the retrieval of 
 * relevant dropdown data (e.g., supplier contacts, raw materials) and the 
 * generation of date-wise reports based on filtering criteria like date range, 
 * supplier category, transaction type, and language preferences.
 * 
 * Methods in this service allow for the generation of detailed or summary reports
 * with or without items, enabling flexible reporting options tailored to business 
 * needs. The reports are generated in various formats and are designed to support 
 * financial, procurement, and inventory management tasks.
 * 
 * Each report generation method takes various parameters, including date range, 
 * contact category type, raw material IDs, and sorting preferences to create 
 * custom reports that meet specific requirements.
 * 
 * This interface is intended to be implemented by a service class that 
 * interacts with the underlying data repositories to fetch data and generate 
 * reports accordingly.
 */
public interface TransactionReportNativeQueryService {

	/**
	 * Retrieves a list of supplier contacts for cash payment receipt dropdown data within the specified date range and transaction type.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param transactionTypeId The transaction type ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the supplier contacts.
	 */
	List<DateWiseReportDropDownCommonDto> getSuppilerContactForCashPaymentReceiptDropDown(LocalDate startDate, LocalDate endDate, Integer transactionTypeId);

	/**
	 * Generates a cash payment receipt report within the specified date range, supplier category, transaction type, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierCategoryId The supplier category ID.
	 * @param transactionTypeId The transaction type ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateCashPaymentReceiptReport(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long transactionTypeId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Retrieves a list of supplier contacts for bank payment receipt dropdown data within the specified date range and transaction type.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param transactionTypeId The transaction type ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the supplier contacts.
	 */
	List<DateWiseReportDropDownCommonDto> getSuppilerContactForBankPaymentReceiptDropDown(LocalDate startDate, LocalDate endDate, Integer transactionTypeId);

	/**
	 * Generates a bank payment receipt report within the specified date range, supplier category, transaction type, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierCategoryId The supplier category ID.
	 * @param transactionTypeId The transaction type ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateBankPaymentReceiptReport(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long transactionTypeId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Retrieves a list of supplier contacts for purchase order dropdown data within the specified date range and contact category type.
	 * 
	 * @param contactCategoryTypeId The contact category type ID.
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the supplier contacts.
	 */
	List<DateWiseReportDropDownCommonDto> getSuppilerContactForPurchaseOrderDropDown(Long contactCategoryTypeId, LocalDate startDate, LocalDate endDate);

	/**
	 * Retrieves a list of raw materials for purchase order dropdown data within the specified date range and supplier category.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param supplierCategoryId The supplier category ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the raw materials.
	 */
	List<DateWiseReportDropDownCommonDto> getRawMaterialForPurchaseOrderDropDownData(LocalDate startDate, LocalDate endDate, Long supplierCategoryId);

	/**
	 * Generates a date-wise purchase order report with items within the specified date range, supplier category, raw material, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierCategoryId The supplier category ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateDateWisePurchaseOrderReportWithItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a date-wise purchase order report without items within the specified date range, supplier category, raw material, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierCategoryId The supplier category ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateDateWisePurchaseOrderReportWithoutItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Retrieves a list of supplier contacts for purchase bill dropdown data within the specified date range.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param contactCategoryTypeId The contact category type ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the supplier contacts.
	 */
	List<DateWiseReportDropDownCommonDto> getSuppilerContactForPurchaseBillDropDown(LocalDate startDate, LocalDate endDate, Long contactCategoryTypeId);

	/**
	 * Retrieves a list of raw materials for purchase bill dropdown data within the specified date range and supplier category.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param suppilerCategoryId The supplier category ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the raw materials.
	 */
	List<DateWiseReportDropDownCommonDto> getRawMaterialForPurchaseBillDropDownData(LocalDate startDate, LocalDate endDate, Long suppilerCategoryId);

	/**
	 * Generates a date-wise purchase bill report with items within the specified date range, supplier category, raw material, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierCategoryId The supplier category ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateDateWisePurchaseBillReportWithItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a date-wise purchase bill report without items within the specified date range, supplier category, raw material, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierCategoryId The supplier category ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateDateWisePurchaseBillReportWithoutItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Retrieves a list of input transfer to hall dropdown data within the specified date range.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the input transfer data.
	 */
	List<DateWiseReportDropDownCommonDto> getInputTransferToHallDropDown(LocalDate startDate, LocalDate endDate);

	/**
	 * Retrieves a list of raw materials for input transfer dropdown data within the specified date range and hall ID.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param hallId The hall ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the raw materials.
	 */
	List<DateWiseReportDropDownCommonDto> getRawMaterialForInputTransferDropDownData(LocalDate startDate, LocalDate endDate, Long hallId);

	/**
	 * Generates a date-wise input transfer hall report with items within the specified date range, hall ID, raw material, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param hallId The hall ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateDateWiseInputTransferHallReportWithItems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a date-wise input transfer hall report without items within the specified date range, hall ID, raw material, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param hallId The hall ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateDateWiseInputTransferHallReportWithoutItems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Retrieves a list of supplier contacts for debit note dropdown data within the specified date range and contact category type.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param contactCategoryTypeId The contact category type ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the supplier contacts.
	 */
	List<DateWiseReportDropDownCommonDto> getSuppilerContactForDebitNoteDropDown(LocalDate startDate, LocalDate endDate, Long contactCategoryTypeId);

	/**
	 * Retrieves a list of raw materials for debit note dropdown data within the specified date range and supplier category.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param suppilerCategoryId The supplier category ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the raw materials.
	 */
	List<DateWiseReportDropDownCommonDto> getRawMaterialForDebitNoteDropDownData(LocalDate startDate, LocalDate endDate, Long suppilerCategoryId);

	/**
	 * Generates a date-wise debit note report with items within the specified date range, supplier category, raw material, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierCategoryId The supplier category ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateDateWiseDebitNoteReportWithItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a date-wise debit note report without items within the specified date range, supplier category, raw material, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierCategoryId The supplier category ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateDateWiseDebitNoteReportWithoutItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Retrieves a list of raw material return to hall dropdown data within the specified date range.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the raw material return data.
	 */
	List<DateWiseReportDropDownCommonDto> getRawMaterialReturnToHallDropDown(LocalDate startDate, LocalDate endDate);

	/**
	 * Retrieves a list of raw materials for raw material return hall dropdown data within the specified date range and hall ID.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param hallId The hall ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the raw materials.
	 */
	List<DateWiseReportDropDownCommonDto> getRawMaterialForRawMaterialReturnHallDropDownData(LocalDate startDate, LocalDate endDate, Long hallId);

	/**
	 * Generates a date-wise raw material return hall report with items within the specified date range, hall ID, raw material, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param hallId The hall ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateDateWiseRawMaterialReturnHallReportWithItems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType, String langCod, String reportName, HttpServletRequest request);

	/**
	 * Generates a date-wise raw material return hall report without items within the specified date range, hall ID, raw material, and other parameters.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param hallId The hall ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @param langCode The language code for localization.
	 * @param request The HTTP request for additional context.
	 * @return A {@link FileBean} representing the generated report.
	 */
	FileBean generateDateWiseRawMaterialReturnHallReportWithoutItems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request);

}