package com.catering.dao.accounting_reports.transaction_report;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.catering.dto.tenant.request.DateWisePurchaseOrderReportDto;
import com.catering.dto.tenant.request.DateWisePurchaseOrderWithoutRawMaterialDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.DateWiseBankPaymentReportDto;
import com.catering.dto.tenant.request.DateWiseCashPaymentReportDto;
import com.catering.dto.tenant.request.DateWiseInputTransferHallReportDto;

/**
 * Data Access Object (DAO) interface for interacting with the native queries related to transaction reports.
 * This interface provides methods for fetching various transaction-related data from the database 
 * through native SQL queries. It supports retrieving data for cash payment receipts, bank payment receipts, 
 * purchase orders, purchase bills, debit notes, input transfers to halls, and raw material returns to halls, 
 * all based on date ranges, supplier contacts, raw material categories, and other related parameters.
 * 
 * The following are some of the primary operations available:
 * 
 * <ul>
 * <li>Fetching supplier contact details for different transaction types (e.g., cash payment, bank payment, purchase orders, etc.).</li>
 * <li>Generating date-wise reports for cash payment receipts, bank payment receipts, purchase orders, purchase bills, debit notes, and raw material transfers to halls.</li>
 * <li>Generating reports with or without items based on the provided parameters.</li>
 * </ul>
 * 
 * Each method is associated with a specific native query that corresponds to a report or data extraction from the database.
 * The query results are mapped to DTOs for further processing.
 * 
 * @see DateWiseReportDropDownCommonDto for the drop-down data structures.
 * @see DateWiseCashPaymentReportDto for cash payment receipt reports.
 * @see DateWiseBankPaymentReportDto for bank payment receipt reports.
 * @see DateWisePurchaseOrderReportDto for purchase order reports.
 * @see DateWisePurchaseOrderWithoutRawMaterialDto for purchase order reports without items.
 * @see DateWiseInputTransferHallReportDto for input transfer to hall reports.
 * @see DateWisePurchaseOrderWithoutRawMaterialDto for reports related to debit notes.
 * 
 * Methods in this interface provide efficient access to reports based on specific date ranges and transaction types, 
 * enabling users to retrieve detailed transaction data and generate comprehensive reports.
 */
public interface TransactionReportNativeQueryDao extends JpaRepository<TransactionReportNativeQuery, Long> {

	/**
	 * Retrieves a list of supplier contacts for cash payment receipt dropdown.
	 * This method executes a native query to fetch a list of supplier contacts based on the provided date range and transaction type.
	 * The result is mapped to a {@link DateWiseReportDropDownCommonDto} containing the supplier contact details.
	 * 
	 * @param startDate The start date of the range for filtering the records.
	 * @param endDate The end date of the range for filtering the records.
	 * @param transactionTypeId The ID of the transaction type to filter the records by.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the supplier contacts for the specified transaction type and date range.
	 * @throws IllegalArgumentException If the start date or end date is null, or if the transaction type ID is invalid.
	 */
	@Query(name = "getSuppilerContactForCashPaymnetReceiptDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getSuppilerContactForCashPaymentReceiptDropDown(LocalDate startDate, LocalDate endDate, Integer transactionTypeId);

	/**
	 * Retrieves a date-wise cash payment receipt report within the specified date range, supplier category, and transaction type.
	 * This method executes a native SQL query to fetch the report data and maps it to a {@link DateWiseCashPaymentReportDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierCategoryId The supplier category ID.
	 * @param transactionTypeId The transaction type ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWiseCashPaymentReportDto} representing the cash payment receipt report.
	 */
	@Query(name = "getDateWiseCashPaymentReceiptReport", nativeQuery = true)
	List<DateWiseCashPaymentReportDto> getDateWiseCashPaymentReceiptReport(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long transactionTypeId, Long orderById, Integer langType);

	/**
	 * Retrieves a list of supplier contacts for bank payment receipt dropdown within the specified date range and transaction type.
	 * This method executes a native SQL query to fetch supplier contacts and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param transactionTypeId The transaction type ID to filter the records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the supplier contacts.
	 */
	@Query(name = "getSuppilerContactForBankPaymnetReceiptDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getSuppilerContactForBankPaymentReceiptDropDown(LocalDate startDate, LocalDate endDate, Integer transactionTypeId);

	/**
	 * Retrieves a date-wise bank payment receipt report within the specified date range, supplier category, and transaction type.
	 * This method executes a native SQL query to fetch the report data and maps it to a {@link DateWiseBankPaymentReportDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierCategoryId The supplier category ID.
	 * @param transactionTypeId The transaction type ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWiseBankPaymentReportDto} representing the bank payment receipt report.
	 */
	@Query(name = "getDateWiseBankPaymentReceiptReport", nativeQuery = true)
	List<DateWiseBankPaymentReportDto> getDateWiseBankPaymentReceiptReport(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long transactionTypeId, Long orderById, Integer langType);

	/**
	 * Retrieves a list of supplier contacts for purchase order dropdown within the specified date range and contact category type.
	 * This method executes a native SQL query to fetch supplier contacts and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param contactCategoryTypeId The contact category type ID.
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the supplier contacts.
	 */
	@Query(name = "getSuppilerContactForPurchaseOrderDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getSuppilerContactForPurchaseOrderDropDown(Long contactCategoryTypeId, LocalDate startDate, LocalDate endDate);

	/**
	 * Retrieves a list of raw materials for purchase order dropdown within the specified date range and supplier category.
	 * This method executes a native SQL query to fetch raw material details and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param supplierCategoryId The supplier category ID.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing raw materials.
	 */
	@Query(name = "getRawMaterialForPurchaseOrderDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getRawMaterialForPurchaseOrderDropDown(LocalDate startDate, LocalDate endDate, Long supplierCategoryId);

	/**
	 * Retrieves a date-wise purchase order report with items within the specified date range, supplier contact, and raw material.
	 * This method executes a native SQL query to fetch the report data and maps it to a {@link DateWisePurchaseOrderReportDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierContactId The supplier contact ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWisePurchaseOrderReportDto} representing the purchase order report with items.
	 */
	@Query(name = "getDateWisePurchaseOrderReportWithitems", nativeQuery = true)
	List<DateWisePurchaseOrderReportDto> getDateWisePurchaseOrderReportWithitems(LocalDate startDate, LocalDate endDate, Long supplierContactId, Long rawMaterialId, Long orderById, Integer langType);

	/**
	 * Retrieves a date-wise purchase order report without items within the specified date range, supplier contact, and raw material.
	 * This method executes a native SQL query to fetch the report data and maps it to a {@link DateWisePurchaseOrderWithoutRawMaterialDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierContactId The supplier contact ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWisePurchaseOrderWithoutRawMaterialDto} representing the purchase order report without items.
	 */
	@Query(name = "getDateWisePurchaseOrderReportWithoutitems", nativeQuery = true)
	List<DateWisePurchaseOrderWithoutRawMaterialDto> getDateWisePurchaseOrderReportWithoutItems(LocalDate startDate, LocalDate endDate, Long supplierContactId, Long rawMaterialId, Long orderById, Integer langType);

	/**
	 * Retrieves a list of supplier contacts for purchase bill dropdown within the specified date range and contact category type.
	 * This method executes a native SQL query to fetch supplier contacts and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param contactCategoryTypeId The contact category type ID.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the supplier contacts.
	 */
	@Query(name = "getSuppilerContactForPurchaseBillDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getSuppilerContactForPurchaseBillDropDown(LocalDate startDate, LocalDate endDate, Long contactCategoryTypeId);

	/**
	 * Retrieves a list of raw materials for purchase bill dropdown within the specified date range and supplier category.
	 * This method executes a native SQL query to fetch raw material details and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param supplierCategoryId The supplier category ID.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing raw materials.
	 */
	@Query(name = "getRawMaterialForPurchaseBillDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getRawMaterialForPurchaseBillDropDown(LocalDate startDate, LocalDate endDate, Long supplierCategoryId);

	/**
	 * Retrieves a date-wise purchase bill report with items within the specified date range, supplier contact, and raw material.
	 * This method executes a native SQL query to fetch the report data and maps it to a {@link DateWisePurchaseOrderReportDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierContactId The supplier contact ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWisePurchaseOrderReportDto} representing the purchase bill report with items.
	 */
	@Query(name = "getDateWisePurchaseBillReportWithitems", nativeQuery = true)
	List<DateWisePurchaseOrderReportDto> getDateWisePurchaseBillReportWithitems(LocalDate startDate, LocalDate endDate, Long supplierContactId, Long rawMaterialId, Long orderById, Integer langType);

	/**
	 * Retrieves a date-wise purchase bill report without items within the specified date range, supplier contact, and raw material.
	 * This method executes a native SQL query to fetch the report data and maps it to a {@link DateWisePurchaseOrderWithoutRawMaterialDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierContactId The supplier contact ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWisePurchaseOrderWithoutRawMaterialDto} representing the purchase bill report without items.
	 */
	@Query(name = "getDateWisePurchaseBillReportWithoutitems", nativeQuery = true)
	List<DateWisePurchaseOrderWithoutRawMaterialDto> getDateWisePurchaseBillReportWithoutItems(LocalDate startDate, LocalDate endDate, Long supplierContactId, Long rawMaterialId, Long orderById, Integer langType);

	/**
	 * Retrieves a list of input transfer to hall dropdown data within the specified date range.
	 * This method executes a native SQL query to fetch input transfer data and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the input transfer data.
	 */
	@Query(name = "getInputTransferToHallDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getInputTransferToHallDropDown(LocalDate startDate, LocalDate endDate);

	/**
	 * Retrieves a list of raw materials for input transfer to hall dropdown data within the specified date range and hall ID.
	 * This method executes a native SQL query to fetch raw material data and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param hallId The hall ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the raw material data.
	 */
	@Query(name = "getRawMaterialForInputTransferDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getRawMaterialForInputTransferToHallDropDown(LocalDate startDate, LocalDate endDate, Long hallId);

	/**
	 * Retrieves a date-wise input transfer hall report with items within the specified date range, hall ID, and raw material.
	 * This method executes a native SQL query to fetch the report data and maps the results to a {@link DateWiseInputTransferHallReportDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param hallId The hall ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWiseInputTransferHallReportDto} representing the input transfer hall report with items.
	 */
	@Query(name = "getDateWiseInputTransferHallReportWithitems", nativeQuery = true)
	List<DateWiseInputTransferHallReportDto> getDateWiseInputTransferHallReportWithitems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType);

	/**
	 * Retrieves a date-wise input transfer hall report without items within the specified date range, hall ID, and raw material.
	 * This method executes a native SQL query to fetch the report data and maps the results to a {@link DateWiseInputTransferHallReportDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param hallId The hall ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWiseInputTransferHallReportDto} representing the input transfer hall report without items.
	 */
	@Query(name = "getDateWiseInputTransferHallReportWithoutitems", nativeQuery = true)
	List<DateWiseInputTransferHallReportDto> getDateWiseInputTransferHallReportWithoutItems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType);

	/**
	 * Retrieves a list of supplier contacts for debit note dropdown data within the specified date range and contact category type.
	 * This method executes a native SQL query to fetch supplier contacts and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param contactCategoryTypeId The contact category type ID.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the supplier contacts.
	 */
	@Query(name = "getSuppilerContactForDebitNoteDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getSuppilerContactForDebitNoteDropDown(LocalDate startDate, LocalDate endDate, Long contactCategoryTypeId);

	/**
	 * Retrieves a list of raw materials for debit note dropdown data within the specified date range and supplier category.
	 * This method executes a native SQL query to fetch raw materials and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param supplierCategoryId The supplier category ID.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the raw materials.
	 */
	@Query(name = "getRawMaterialForDebitNoteDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getRawMaterialForDebitNoteDropDown(LocalDate startDate, LocalDate endDate, Long supplierCategoryId);

	/**
	 * Retrieves a date-wise debit note report with items within the specified date range, supplier contact, and raw material.
	 * This method executes a native SQL query to fetch the report data and maps the results to a {@link DateWisePurchaseOrderReportDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierContactId The supplier contact ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWisePurchaseOrderReportDto} representing the debit note report with items.
	 */
	@Query(name = "getDateWiseDebitNoteReportWithitems", nativeQuery = true)
	List<DateWisePurchaseOrderReportDto> getDateWiseDebitNoteReportWithitems(LocalDate startDate, LocalDate endDate, Long supplierContactId, Long rawMaterialId, Long orderById, Integer langType);

	/**
	 * Retrieves a date-wise debit note report without items within the specified date range, supplier contact, and raw material.
	 * This method executes a native SQL query to fetch the report data and maps the results to a {@link DateWisePurchaseOrderWithoutRawMaterialDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param supplierContactId The supplier contact ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWisePurchaseOrderWithoutRawMaterialDto} representing the debit note report without items.
	 */
	@Query(name = "getDateWiseDebitNoteReportWithoutitems", nativeQuery = true)
	List<DateWisePurchaseOrderWithoutRawMaterialDto> getDateWiseDebitNoteReportWithoutItems(LocalDate startDate, LocalDate endDate, Long supplierContactId, Long rawMaterialId, Long orderById, Integer langType);

	/**
	 * Retrieves a list of raw materials for raw material return to hall dropdown data within the specified date range.
	 * This method executes a native SQL query to fetch raw material return data and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the raw material return data.
	 */
	@Query(name = "getRawMaterialReturnToHallDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getRawMaterialReturnToHallDropDown(LocalDate startDate, LocalDate endDate);

	/**
	 * Retrieves a list of raw materials for raw material return to hall dropdown data within the specified date range and hall ID.
	 * This method executes a native SQL query to fetch raw material return data and maps the results to a {@link DateWiseReportDropDownCommonDto}.
	 * 
	 * @param startDate The start date for filtering records.
	 * @param endDate The end date for filtering records.
	 * @param hallId The hall ID for filtering records.
	 * @return A list of {@link DateWiseReportDropDownCommonDto} representing the raw material return data.
	 */
	@Query(name = "getRawMaterialForRawMaterialReturnToHallDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getRawMaterialForRawMaterialReturnToHallDropDown(LocalDate startDate, LocalDate endDate, Long hallId);

	/**
	 * Retrieves a date-wise raw material return hall report with items within the specified date range, hall ID, and raw material.
	 * This method executes a native SQL query to fetch the report data and maps the results to a {@link DateWiseInputTransferHallReportDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param hallId The hall ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWiseInputTransferHallReportDto} representing the raw material return hall report with items.
	 */
	@Query(name = "getDateWiseRawMaterialReturnHallReportWithitems", nativeQuery = true)
	List<DateWiseInputTransferHallReportDto> getDateWiseRawMaterialReturnHallReportWithitems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType);

	/**
	 * Retrieves a date-wise raw material return hall report without items within the specified date range, hall ID, and raw material.
	 * This method executes a native SQL query to fetch the report data and maps the results to a {@link DateWiseInputTransferHallReportDto}.
	 * 
	 * @param startDate The start date for the report.
	 * @param endDate The end date for the report.
	 * @param hallId The hall ID.
	 * @param rawMaterialId The raw material ID.
	 * @param orderById The order ID for sorting.
	 * @param langType The language type for localization.
	 * @return A list of {@link DateWiseInputTransferHallReportDto} representing the raw material return hall report without items.
	 */
	@Query(name = "getDateWiseRawMaterialReturnHallReportWithoutitems", nativeQuery = true)
	List<DateWiseInputTransferHallReportDto> getDateWiseRawMaterialReturnHallReportWithoutitems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType);

}