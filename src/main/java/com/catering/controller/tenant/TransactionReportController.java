package com.catering.controller.tenant;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.FileBean;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.accounting_reports.transaction_report.TransactionReportNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.util.RequestResponseUtils;
import com.catering.util.StringToDateUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(value = ApiPathConstant.TRANSACTION_REPORTS)
@Tag(name = SwaggerConstant.TRANSACTION_REPORTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionReportController {

	/**
	 * Service for executing native queries related to transaction reports.
	 */
	TransactionReportNativeQueryService transactionReportNativeQueryService;

	/**
	 * Retrieves a list of supplier contacts for cash payment/receipt drop down.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.CASH_PAYMENT_RECEIPT_SUPPILER_CONTACT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getCashPaymentReceiptSuppilerContactList(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.CASH_PAYMENT_TRANSACTION_TYPE_REPORT, required = false) Integer transactionTypeId) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getSuppilerContactForCashPaymentReceiptDropDown(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), transactionTypeId));
	}

	/**
	 * Retrieves a list of data for cash payment/receipt report.
	 * @param startDate The start date to get report data.
	 * @param endDate The end date to get report data.
	 * @param supplierCategoryId The id of supplier contact.
	 * @param transactionTypeId The id of transaction type 
	 * 							0 for cash payment
	 * 							1 for cash receipt
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.CASH_PAYMENT_RECEIPT_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseCashPaymentReceiptReport(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId, @RequestParam(name = FieldConstants.CASH_PAYMENT_TRANSACTION_TYPE_REPORT, required = false) Long transactionTypeId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateCashPaymentReceiptReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, transactionTypeId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of supplier contacts for bank payment/receipt drop down.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.BANK_PAYMENT_RECEIPT_SUPPILER_CONTACT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getBankPaymentReceiptSuppilerContactList(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.CASH_PAYMENT_TRANSACTION_TYPE_REPORT, required = false) Integer transactionTypeId) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getSuppilerContactForBankPaymentReceiptDropDown(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), transactionTypeId));
	}

	/**
	 * Retrieves a list of data for bank payment/receipt report.
	 * @param startDate The start date to get report data.
	 * @param endDate The end date to get report data.
	 * @param supplierCategoryId The id of supplier contact.
	 * @param transactionTypeId The id of transaction type 
	 * 							0 for bank payment
	 * 							1 for bank receipt
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.BANK_PAYMENT_RECEIPT_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseBankPaymentReceiptReport(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId, @RequestParam(name = FieldConstants.CASH_PAYMENT_TRANSACTION_TYPE_REPORT, required = false) Long transactionTypeId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateBankPaymentReceiptReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, transactionTypeId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of supplier contacts for purchase orders drop down.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.PURCHASE_ORDER_SUPPILER_CONTACT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getSuppilerContactList(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getSuppilerContactForPurchaseOrderDropDown(Constants.PURCHASE_ORDER_SUPPILER_CATEGORY_TYPE_ID, StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate)));
	}

	/**
	 * Retrieves a list of raw material for purchase orders drop down.
	 * @param suppilerId The supplier id for raw material.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.PURCHASE_ORDER_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getRawMaterialBySuppilerId(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long suppilerId) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getRawMaterialForPurchaseOrderDropDownData(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), suppilerId));
	}

	/**
	 * Retrieves a list of data for purchase orders report with items.
	 * @param startDate The start date to get purchase order detail.
	 * @param endDate The end date to get purchase order detail.
	 * @param supplierCategoryId The id of supplier to get particular raw material.
	 * @param rawMaterialId The id of raw material.
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_PURCHASE_ORDER_REPORT_WITH_ITEMS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWisePurchaseOrderReportWithItems(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateDateWisePurchaseOrderReportWithItems(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, rawMaterialId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of data for purchase orders report without items.
	 * @param startDate The start date to get purchase order detail.
	 * @param endDate The end date to get purchase order detail.
	 * @param supplierCategoryId The id of supplier to get particular raw material.
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_PURCHASE_ORDER_REPORT_WITHOUT_ITEMS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWisePurchaseOrderReportWithoutItems(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateDateWisePurchaseOrderReportWithoutItems(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, rawMaterialId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of supplier contacts for purchase bill drop down.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.PURCHASE_BILL_SUPPILER_CONTACT_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getPurchaseBillSuppilerContactListForDropDown(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getSuppilerContactForPurchaseBillDropDown(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), Constants.PURCHASE_ORDER_SUPPILER_CATEGORY_TYPE_ID));
	}

	/**
	 * Retrieves a list of raw material for purchase bill drop down.
	 * @param suppilerId The supplier id for raw material.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.PURCHASE_BILL_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getPurchaseBillRawMaterialListForDropDown(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long suppilerId) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getRawMaterialForPurchaseBillDropDownData(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), suppilerId));
	}

	/**
	 * Retrieves a list of data for purchase bill report with items.
	 * @param startDate The start date to get purchase bill detail.
	 * @param endDate The end date to get purchase bill detail.
	 * @param supplierCategoryId The id of supplier to get particular raw material.
	 * @param rawMaterialId The id of raw material.
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_PURCHASE_BILL_REPORT_WITH_ITEMS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWisePurchaseBillReportWithItems(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateDateWisePurchaseBillReportWithItems(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, rawMaterialId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of data for purchase bill report without items.
	 * @param startDate The start date to get purchase bill detail.
	 * @param endDate The end date to get purchase bill detail.
	 * @param supplierCategoryId The id of supplier to get particular raw material.
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_PURCHASE_BILL_REPORT_WITHOUT_ITEMS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWisePurchaseBillReportWithoutItems(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateDateWisePurchaseBillReportWithoutItems(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, rawMaterialId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of hall for input transfer to hall drop down.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.INPUT_TRANSFER_TO_HALL_DROP_DOWN_DATA)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getInputTransferHallList(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getInputTransferToHallDropDown(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate)));
	}

	/**
	 * Retrieves a list of raw material for input transfer to hall drop down.
	 * @param hallId The hall id for raw material.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.INPUT_TRANSFER_TO_HALL_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getRawMaterialByHallId(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.INPUT_TRANSFER_TO_HALL_ID, required = false) Long hallId) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getRawMaterialForInputTransferDropDownData(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), hallId));
	}

	/**
	 * Retrieves a list of data for input transfer to hall report with items.
	 * @param startDate The start date to get input transfer to hall detail.
	 * @param endDate The end date to get transfer to hall detail.
	 * @param hallId The id of hall to get particular raw material.
	 * @param rawMaterialId The id of raw material.
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITH_ITEMS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseInputTransferHallReportWithItems(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.INPUT_TRANSFER_TO_HALL_ID, required = false) Long hallId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateDateWiseInputTransferHallReportWithItems(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), hallId, rawMaterialId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of data for input transfer to hall report without items.
	 * @param startDate The start date to get input transfer to hall detail.
	 * @param endDate The end date to get input transfer to hall detail.
	 * @param hallId The id of hall to get particular raw material.
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITHOUT_ITEMS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseInputTransferHallReportWithoutItems(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.INPUT_TRANSFER_TO_HALL_ID, required = false) Long hallId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateDateWiseInputTransferHallReportWithoutItems(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), hallId, rawMaterialId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of supplier contacts for debit note drop down.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DEBIT_NOTE_SUPPILER_CONTACT_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getDebitNoteSuppilerContactListForDropDown(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getSuppilerContactForDebitNoteDropDown(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), Constants.PURCHASE_ORDER_SUPPILER_CATEGORY_TYPE_ID));
	}

	/**
	 * Retrieves a list of raw material for debit note drop down.
	 * @param suppilerId The suppiler id for raw material.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DEBIT_NOTE_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getDebitNoteRawMaterialListForDropDown(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long suppilerId) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getRawMaterialForDebitNoteDropDownData(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), suppilerId));
	}

	/**
	 * Retrieves a list of data for debit note report with items.
	 * @param startDate The start date to get debit note detail.
	 * @param endDate The end date to get debit note detail.
	 * @param supplierCategoryId The id of supplier to get particular raw material.
	 * @param rawMaterialId The id of raw material.
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_DEBIT_NOTE_REPORT_WITH_ITEMS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseDebitNoteReportWithItems(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateDateWiseDebitNoteReportWithItems(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, rawMaterialId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of data for debit note report without items.
	 * @param startDate The start date to get debit note detail.
	 * @param endDate The end date to get debit note detail.
	 * @param supplierCategoryId The id of supplier to get particular raw material.
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_DEBIT_NOTE_REPORT_WITHOUT_ITEMS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseDebitNoteReportWithoutItems(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateDateWiseDebitNoteReportWithoutItems(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, rawMaterialId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of hall for raw material return to hall drop down.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_RETURN_TO_HALL_DROP_DOWN_DATA)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getRawMaterialReturnToHallList(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getRawMaterialReturnToHallDropDown(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate)));
	}

	/**
	 * Retrieves a list of raw material for raw material return to hall drop down.
	 * @param hallId The hall id for raw material.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_RETURN_TO_HALL_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getRawMaterialReturnToHallByHallId(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.INPUT_TRANSFER_TO_HALL_ID, required = false) Long hallId) {
		return RequestResponseUtils.generateResponseDto(transactionReportNativeQueryService.getRawMaterialForRawMaterialReturnHallDropDownData(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), hallId));
	}

	/**
	 * Retrieves a list of data for raw material return to hall report with items.
	 * @param startDate The start date to get raw material return to hall detail.
	 * @param endDate The end date to get raw material return to hall detail.
	 * @param hallId The id of hall to get particular raw material.
	 * @param rawMaterialId The id of raw material.
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_RAW_MATERIAL_RETURN_HALL_REPORT_WITH_ITEMS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseRawMaterialReturnHallReportWithItems(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.INPUT_TRANSFER_TO_HALL_ID, required = false) Long hallId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateDateWiseRawMaterialReturnHallReportWithItems(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), hallId, rawMaterialId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of data for raw material return to hall report without items.
	 * @param startDate The start date to get raw material return to hall detail.
	 * @param endDate The end date to get raw material return to hall detail.
	 * @param hallId The id of hall to get particular raw material.
	 * @param orderById The id to order by report data.
	 * @param langType The type of language.
	 * @param langCode The particular language code.
	 * @param request The HttpServlet request.
	 * @return A ResponseContainerDto.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_RAW_MATERIAL_RETURN_HALL_REPORT_WITHOUT_ITEMS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.TRANSACTION_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseRawMaterialReturnHallReportWithoutItems(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.INPUT_TRANSFER_TO_HALL_ID, required = false) Long hallId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.ORDER_BY_ID, required = false) Long orderById, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = transactionReportNativeQueryService.generateDateWiseRawMaterialReturnHallReportWithoutItems(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), hallId, rawMaterialId, orderById, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

}