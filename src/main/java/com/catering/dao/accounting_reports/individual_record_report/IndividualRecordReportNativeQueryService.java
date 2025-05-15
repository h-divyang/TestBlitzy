package com.catering.dao.accounting_reports.individual_record_report;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;

/**
 * Service interface for handling the generation of various individual record reports.
 * Each method corresponds to a specific report generation task, utilizing data 
 * from the respective data sources and processing it based on the provided parameters.
 * 
 * The reports generated include cash payment receipts, bank payment receipts, 
 * journal vouchers, input transfers, purchase orders, purchase bills, debit notes, 
 * and raw material returns. Reports are localized based on the specified language 
 * type and language code.
 */
public interface IndividualRecordReportNativeQueryService {

	/**
	 * Generates the individual record cash payment receive report based on the provided parameters.
	 * 
	 * @param id the ID to filter the report
	 * @param transactionType the type of transaction (e.g., cash payment)
	 * @param langType the language type for the report
	 * @param langCode the language code for report localization
	 * @param request the HttpServletRequest to capture request details
	 * @return a {@link FileBean} containing the generated report
	 */
	FileBean getIndividualRecordCashPaymentReceiveReport(Long id, int transactionType, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates the individual record bank payment receive report based on the provided parameters.
	 * 
	 * @param id the ID to filter the report
	 * @param transactionType the type of transaction (e.g., bank payment)
	 * @param langType the language type for the report
	 * @param langCode the language code for report localization
	 * @param request the HttpServletRequest to capture request details
	 * @return a {@link FileBean} containing the generated report
	 */
	FileBean getIndividualRecordBankPaymentReceiveReport(Long id, int transactionType, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates the individual record journal voucher report based on the provided parameters.
	 * 
	 * @param id the journal voucher ID to filter the report
	 * @param langType the language type for the report
	 * @param langCode the language code for report localization
	 * @param request the HttpServletRequest to capture request details
	 * @return a {@link FileBean} containing the generated report
	 */
	FileBean getIndividualRecordJournalVoucherReport(Long id, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates the individual record input transfer to hall report based on the provided parameters.
	 * 
	 * @param id the ID to filter the report
	 * @param langType the language type for the report
	 * @param langCode the language code for report localization
	 * @param request the HttpServletRequest to capture request details
	 * @return a {@link FileBean} containing the generated report
	 */
	FileBean getIndividualRecordInputTransferToHallReport(Long id, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates the individual record purchase order report based on the provided parameters.
	 * 
	 * @param id the ID to filter the report
	 * @param contactId the contact ID associated with the purchase order
	 * @param langType the language type for the report
	 * @param langCode the language code for report localization
	 * @param request the HttpServletRequest to capture request details
	 * @return a {@link FileBean} containing the generated report
	 */
	FileBean getIndividualRecordPurchaseOrderReport(Long id, Long contactId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates the individual record purchase bill report based on the provided parameters.
	 * 
	 * @param id the ID to filter the report
	 * @param contactId the contact ID associated with the purchase bill
	 * @param langType the language type for the report
	 * @param langCode the language code for report localization
	 * @param request the HttpServletRequest to capture request details
	 * @return a {@link FileBean} containing the generated report
	 */
	FileBean getIndividualRecordPurchaseBillReport(Long id, Long contactId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates the individual record debit note report based on the provided parameters.
	 * 
	 * @param id the ID to filter the report
	 * @param contactId the contact ID associated with the debit note
	 * @param langType the language type for the report
	 * @param langCode the language code for report localization
	 * @param request the HttpServletRequest to capture request details
	 * @return a {@link FileBean} containing the generated report
	 */
	FileBean getIndividualRecordDebitNoteReport(Long id, Long contactId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates the individual record raw material return to hall report based on the provided parameters.
	 * 
	 * @param id the ID to filter the report
	 * @param langType the language type for the report
	 * @param langCode the language code for report localization
	 * @param request the HttpServletRequest to capture request details
	 * @return a {@link FileBean} containing the generated report
	 */
	FileBean getIndividualRecordRawMaterialReturnToHallReport(Long id, Integer langType, String langCode, String reportName, HttpServletRequest request);

}