package com.catering.controller.tenant;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.FileBean;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.FieldConstants;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.accounting_reports.individual_record_report.IndividualRecordReportNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for handling individual record report generation requests.
 * Provides endpoints for generating various types of reports including cash payment receipts,
 * bank payment receipts, purchase orders, purchase bills, debit notes, journal vouchers, and others.
 * <p>
 * Each endpoint supports specific user rights for authorization and processes input parameters to generate reports.
 * The responses contain the generated report file as a byte array.
 * </p>
 */
@RestController
@RequestMapping(value = ApiPathConstant.INDIVIDUAL_RECORD_REPORT)
@Tag(name = SwaggerConstant.INDIVIDUAL_RECORD_REPORT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IndividualRecordReportController {

	/**
	 * Service to handle native query-based report generation for individual records.
	 */
	IndividualRecordReportNativeQueryService individualRecordReportNativeQueryService;

	/**
	 * Generates a cash payment or receipt report based on the given ID and transaction type.
	 *
	 * @param id            the ID of the cash payment or receipt record
	 * @param transactionType the type of transaction (payment or receipt)
	 * @param langType      the optional language type for the report
	 * @param langCode      the optional language code for localization
	 * @param request       the HTTP request containing user details for context
	 * @return a byte array of the generated report file
	 */
	@GetMapping(value = ApiPathConstant.INDIVIDUAL_RECORD_CASH_PAYMENT_RECEIPT_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.CASH_PAYMENT + ApiUserRightsConstants.CAN_PRINT, ApiUserRightsConstants.CASH_RECEIPT + ApiUserRightsConstants.CAN_PRINT}, checkAll = false)
	public ResponseContainerDto<byte[]> cashPaymentReceiptReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id, @RequestParam(name = FieldConstants.TRANSACTION_TYPE) int transactionType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = individualRecordReportNativeQueryService.getIndividualRecordCashPaymentReceiveReport(id, transactionType, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a bank payment or receipt report based on the given ID and transaction type.
	 *
	 * @param id            the ID of the bank payment or receipt record
	 * @param transactionType the type of transaction (payment or receipt)
	 * @param langType      the optional language type for the report
	 * @param langCode      the optional language code for localization
	 * @param request       the HTTP request containing user details for context
	 * @return a byte array of the generated report file
	 */
	@GetMapping(value = ApiPathConstant.INDIVIDUAL_RECORD_BANK_PAYMENT_RECEIPT_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.BANK_PAYMENT + ApiUserRightsConstants.CAN_PRINT, ApiUserRightsConstants.BANK_RECEIPT + ApiUserRightsConstants.CAN_PRINT}, checkAll = false)
	public ResponseContainerDto<byte[]> bankPaymentReceiptReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id, @RequestParam(name = FieldConstants.TRANSACTION_TYPE) int transactionType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = individualRecordReportNativeQueryService.getIndividualRecordBankPaymentReceiveReport(id, transactionType, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a purchase order report based on the given ID.
	 *
	 * @param id       the ID of the purchase order record
	 * @param contactId the optional contact ID related to the purchase order
	 * @param langType the optional language type for the report
	 * @param langCode the optional language code for localization
	 * @param request  the HTTP request containing user details for context
	 * @return a byte array of the generated report file
	 */
	@GetMapping(value = ApiPathConstant.INDIVIDUAL_RECORD_PURCHASE_ORDER_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_ORDER + ApiUserRightsConstants.CAN_PRINT}, checkAll = false)
	public ResponseContainerDto<byte[]> purchaseOrderReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id, @RequestParam(name = FieldConstants.COMMON_FIELD_CONTACT_DATE, required = false) Long contactId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = individualRecordReportNativeQueryService.getIndividualRecordPurchaseOrderReport(id, contactId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a purchase bill report based on the given ID.
	 *
	 * @param id       the ID of the purchase bill record
	 * @param contactId the optional contact ID related to the purchase bill
	 * @param langType the optional language type for the report
	 * @param langCode the optional language code for localization
	 * @param request  the HTTP request containing user details for context
	 * @return a byte array of the generated report file
	 */
	@GetMapping(value = ApiPathConstant.INDIVIDUAL_RECORD_PURCHASE_BILL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.PURCHASE_BILL + ApiUserRightsConstants.CAN_PRINT}, checkAll = false)
	public ResponseContainerDto<byte[]> purchaseBillReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id, @RequestParam(name = FieldConstants.COMMON_FIELD_CONTACT_DATE, required = false) Long contactId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = individualRecordReportNativeQueryService.getIndividualRecordPurchaseBillReport(id, contactId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a debit note report based on the given ID.
	 *
	 * @param id       the ID of the debit note record
	 * @param contactId the optional contact ID related to the debit note
	 * @param langType the optional language type for the report
	 * @param langCode the optional language code for localization
	 * @param request  the HTTP request containing user details for context
	 * @return a byte array of the generated report file
	 */
	@GetMapping(value = ApiPathConstant.INDIVIDUAL_RECORD_DEBIT_NOTE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DEBIT_NOTE + ApiUserRightsConstants.CAN_PRINT}, checkAll = false)
	public ResponseContainerDto<byte[]> debitNoteReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id, @RequestParam(name = FieldConstants.COMMON_FIELD_CONTACT_DATE, required = false) Long contactId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = individualRecordReportNativeQueryService.getIndividualRecordDebitNoteReport(id, contactId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a journal voucher report based on the given ID.
	 *
	 * @param id       the ID of the journal voucher record
	 * @param langType the optional language type for the report
	 * @param langCode the optional language code for localization
	 * @param request  the HTTP request containing user details for context
	 * @return a byte array of the generated report file
	 */
	@GetMapping(value = ApiPathConstant.INDIVIDUAL_RECORD_JOURNAL_VOUCHER_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.JOURNAL_VOUCHER + ApiUserRightsConstants.CAN_PRINT}, checkAll = false)
	public ResponseContainerDto<byte[]> journalVoucherReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = individualRecordReportNativeQueryService.getIndividualRecordJournalVoucherReport(id, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates an Input Transfer to Hall report based on the given ID.
	 *
	 * @param id       the ID of the input transfer to hall record
	 * @param langType the optional language type for the report
	 * @param langCode the optional language code for localization
	 * @param request  the HTTP request containing user details for context
	 * @return a byte array of the generated report file
	 */
	@GetMapping(value = ApiPathConstant.INDIVIDUAL_RECORD_INPUT_TRANSFER_TO_HALL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.INPUT_TRANSFER_TO_HALL + ApiUserRightsConstants.CAN_PRINT}, checkAll = false)
	public ResponseContainerDto<byte[]> inputTransferToHallReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = individualRecordReportNativeQueryService.getIndividualRecordInputTransferToHallReport(id, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a Raw Material Return to Hall report based on the given ID.
	 *
	 * @param id       the ID of the raw material return to hall record
	 * @param langType the optional language type for the report
	 * @param langCode the optional language code for localization
	 * @param request  the HTTP request containing user details for context
	 * @return a byte array of the generated report file
	 */
	@GetMapping(value = ApiPathConstant.INDIVIDUAL_RECORD_RAW_MATERIAL_RETURN_TO_HALL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.RAW_MATERIAL_RETURN_TO_HALL + ApiUserRightsConstants.CAN_PRINT}, checkAll = false)
	public ResponseContainerDto<byte[]> rawMaterialReturnToHallReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long id, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = individualRecordReportNativeQueryService.getIndividualRecordRawMaterialReturnToHallReport(id, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

}