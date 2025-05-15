package com.catering.dao.accounting_reports.individual_record_report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.IndividualRecordBankPaymentReceiveReportDto;
import com.catering.dto.tenant.request.IndividualRecordCashPaymentReceiveReportDto;
import com.catering.dto.tenant.request.IndividualRecordCommonDto;
import com.catering.dto.tenant.request.IndividualRecordInputTransferToHallReportDto;
import com.catering.dto.tenant.request.IndividualRecordJournalVoucherReportDto;

/**
 * DAO interface for interacting with the database to fetch individual record report data.
 * This interface contains various native SQL queries to retrieve different types of 
 * reports based on the provided parameters. The queries are mapped to JPA repository methods.
 * 
 * Each method in this interface corresponds to a specific report query that retrieves 
 * data from the database related to cash payments, bank payments, purchase orders, 
 * purchase bills, debit notes, journal vouchers, input transfers, and raw material returns.
 * 
 * The reports include different variations, such as those based on GST for the same or 
 * different states. These reports are filtered by IDs and transaction types, and the language 
 * type is also considered for localized results.
 */
public interface IndividualRecordReportNativeQueryDao extends JpaRepository<IndividualRecordReportNativeQuery, Long> {

	/**
	 * Retrieves the cash payment receive report based on the provided ID, transaction type, 
	 * and language type.
	 * 
	 * @param id the ID to filter the report
	 * @param transactionType the type of transaction (e.g., cash payment)
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordCashPaymentReceiveReportDto} containing the report data
	 */
	@Query(name = "getCashPaymentReceiveReport", nativeQuery = true)
	List<IndividualRecordCashPaymentReceiveReportDto> getIndividualRecordCashPaymentReceiveReport(Long id, int transactionType, Integer langType);

	/**
	 * Retrieves the bank payment receive report based on the provided ID, transaction type, 
	 * and language type.
	 * 
	 * @param id the ID to filter the report
	 * @param transactionType the type of transaction (e.g., bank payment)
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordBankPaymentReceiveReportDto} containing the report data
	 */
	@Query(name = "getBankPaymentReceiveReport", nativeQuery = true)
	List<IndividualRecordBankPaymentReceiveReportDto> getIndividualRecordBankPaymentReceiveReport(Long id, int transactionType, Integer langType);

	/**
	 * Retrieves the GST report for purchase orders in the same state based on the provided ID and 
	 * language type.
	 * 
	 * @param id the ID to filter the report
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordCommonDto} containing the report data
	 */
	@Query(name = "getPurchaseOrderGstSameStateReport", nativeQuery = true)
	List<IndividualRecordCommonDto> getPurchaseOrderGstSameStateReport(Long id, Integer langType);

	/**
	 * Retrieves the GST report for purchase orders in different states based on the provided ID and 
	 * language type.
	 * 
	 * @param id the ID to filter the report
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordCommonDto} containing the report data
	 */
	@Query(name = "getPurchaseOrderGstDifferentStateReport", nativeQuery = true)
	List<IndividualRecordCommonDto> getPurchaseOrderGstDifferentStateReport(Long id, Integer langType);

	/**
	 * Retrieves the GST report for purchase bills in the same state based on the provided ID and 
	 * language type.
	 * 
	 * @param id the ID to filter the report
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordCommonDto} containing the report data
	 */
	@Query(name = "getPurchaseBillGstSameStateReport", nativeQuery = true)
	List<IndividualRecordCommonDto> getPurchaseBillGstSameStateReport(Long id, Integer langType);

	/**
	 * Retrieves the GST report for purchase bills in different states based on the provided ID and 
	 * language type.
	 * 
	 * @param id the ID to filter the report
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordCommonDto} containing the report data
	 */
	@Query(name = "getPurchaseBillGstDifferentStateReport", nativeQuery = true)
	List<IndividualRecordCommonDto> getPurchaseBillGstDifferentStateReport(Long id, Integer langType);

	/**
	 * Retrieves the GST report for debit notes in the same state based on the provided ID and 
	 * language type.
	 * 
	 * @param id the ID to filter the report
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordCommonDto} containing the report data
	 */
	@Query(name = "getDebitNoteGstSameStateReport", nativeQuery = true)
	List<IndividualRecordCommonDto> getDebitNoteGstSameStateReport(Long id, Integer langType);

	/**
	 * Retrieves the GST report for debit notes in different states based on the provided ID and 
	 * language type.
	 * 
	 * @param id the ID to filter the report
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordCommonDto} containing the report data
	 */
	@Query(name = "getDebitNoteGstDifferentStateReport", nativeQuery = true)
	List<IndividualRecordCommonDto> getDebitNoteGstDifferentStateReport(Long id, Integer langType);

	/**
	 * Retrieves the journal voucher report based on the provided journal voucher ID and 
	 * language type.
	 * 
	 * @param journalVoucherId the journal voucher ID to filter the report
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordJournalVoucherReportDto} containing the report data
	 */
	@Query(name = "getJournalVoucherReport", nativeQuery = true)
	List<IndividualRecordJournalVoucherReportDto> getIndividualRecordJournalVoucherReport(Long journalVoucherId, Integer langType);

	/**
	 * Retrieves the input transfer to hall report based on the provided ID and language type.
	 * 
	 * @param inputTransferToHallId the ID to filter the report
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordInputTransferToHallReportDto} containing the report data
	 */
	@Query(name = "getInputTransferToHallReport", nativeQuery = true)
	List<IndividualRecordInputTransferToHallReportDto> getIndividualRecordInputTransferToHallReport(Long inputTransferToHallId, Integer langType);

	/**
	 * Retrieves the raw material return to hall report based on the provided ID and language type.
	 * 
	 * @param rawMaterialReturnToHallId the ID to filter the report
	 * @param langType the language type for the report
	 * @return a list of {@link IndividualRecordInputTransferToHallReportDto} containing the report data
	 */
	@Query(name = "getRawMaterialReturnToHallReport", nativeQuery = true)
	List<IndividualRecordInputTransferToHallReportDto> getIndividualRecordRawMaterialReturnToHallReport(Long rawMaterialReturnToHallId, Integer langType);

}