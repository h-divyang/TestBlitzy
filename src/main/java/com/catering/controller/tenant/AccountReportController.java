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
import com.catering.constant.FieldConstants;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.accounting_reports.account_report.AccountReportNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.GeneralLedgerContactDropDownDto;
import com.catering.util.RequestResponseUtils;
import com.catering.util.StringToDateUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The AccountReportController class is responsible for handling and managing API end points
 * related to various accounting reports, including collection reports,
 * general ledger reports, cash book reports, group summary reports, daily activity reports,
 * and bank book reports.
 * 
 * @author Mashuk Patel
 */
@RestController
@RequestMapping(value = ApiPathConstant.ACCOUNT_REPORTS)
@Tag(name = SwaggerConstant.ACCOUNT_REPORTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountReportController {

	/**
	 * A service instance used to handle account-related report queries at a native query level.
	 */
	AccountReportNativeQueryService accountReportNativeQueryService;

	/**
	 * Retrieves a list of supplier contact category for collection report drop down based on contact id.
	 * 
	 * @return A ResponseContainerDto containing a list of DateWiseReportDropDownCommonDto objects.
	 */
	@GetMapping(value = ApiPathConstant.COLLECTION_CONTACT_CATEGORY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getcontactCategoryList() {
		return RequestResponseUtils.generateResponseDto(accountReportNativeQueryService.getContactCategoryForDropDown());
	}

	/**
	 * Retrieves a list of supplier contacts for collection report drop down based on the provided contact category ID.
	 *
	 * @param contactCategoryId The ID of the contact category, which is optional and may be null.
	 * @return A ResponseContainerDto containing a list of DateWiseReportDropDownCommonDto objects.
	 */
	@GetMapping(value = ApiPathConstant.COLLECTION_CONTACT_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS  + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getStockSummaryRawMaterialList(@RequestParam(name = FieldConstants.CONTACT_CATEGORY_ID, required = false) Long contactCategoryId) {
		return RequestResponseUtils.generateResponseDto(accountReportNativeQueryService.getContactForDropDown(contactCategoryId));
	}

	/**
	 * Generates a collection report based on the provided parameters.
	 *
	 * @param contactCategoryId The ID of the contact category.
	 * @param contactId The ID of the contact.
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @param request The HttpServletRequest object containing the HTTP request details.
	 * @return A ResponseContainerDto containing the generated collection report as a byte array. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.COLLECTION_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS  + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> accountCollectionReport(@RequestParam(name = FieldConstants.CONTACT_CATEGORY_ID, required = false) Long contactCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_CONTACT_DATE, required = false) Long contactId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = accountReportNativeQueryService.generateAccountCollectionReport(contactCategoryId, contactId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of supplier contacts for the general ledger report drop-down.
	 *
	 * @return A ResponseContainerDto containing a list of GeneralLedgerContactDropDownDto objects.
	 */
	@GetMapping(value = ApiPathConstant.GENERAL_LEDGER_SUPPILER_CONTACT_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<GeneralLedgerContactDropDownDto>> getGeneralLedgerSuppilerContactData() {
		return RequestResponseUtils.generateResponseDto(accountReportNativeQueryService.getGeneralLedgerSuppilerContactDropDownData());
	}

	/**
	 * Generates the General Ledger Report based on the provided parameters.
	 *
	 * @param startDate The start date for the report, represented as a string in the expected date format, or null if not specified.
	 * @param endDate The end date for the report, represented as a string in the expected date format, or null if not specified.
	 * @param supplierCategoryId The ID of the supplier category for which the report will be generated.
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @param request The HttpServletRequest object containing the HTTP request details.
	 * @return A ResponseContainerDto containing the generated General Ledger Report as a byte array. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.GENERAL_LEDGER_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateGeneralLedgerReport(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = accountReportNativeQueryService.generateGeneralLedgerReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates the cash book report for the specified date range and language preferences.
	 *
	 * @param startDate The start date of the report in string format or null if not specified.
	 * @param endDate The end date of the report in string format or null if not specified.
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @param request The HttpServletRequest object containing the HTTP request details.
	 * @return A ResponseContainerDto containing the byte array of the generated cash book report file. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.CASH_BOOK_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateCashBookReport(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = accountReportNativeQueryService.generateCashBookReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a group summary report based on the provided parameters.
	 *
	 * @param contactCategoryId The ID of the contact category to filter the report.
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @param request The HttpServletRequest object containing the HTTP request details.
	 * @return A ResponseContainerDto containing a byte array of the generated report file. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.GROUP_SUMMARY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateGroupSummaryReport(@RequestParam(name = FieldConstants.CONTACT_CATEGORY_ID, required = false) Long contactCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = accountReportNativeQueryService.generateGroupSummaryReport(contactCategoryId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a daily activity report based on the provided parameters.
	 *
	 * @param startDate The start date of the report in string format or null if not specified.
	 * @param endDate The end date of the report in string format or null if not specified.
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @param request The HttpServletRequest object containing the HTTP request details.
	 * @return A ResponseContainerDto containing the byte array representation of the generated report. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.DAILY_ACTIVITY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateDailyActivityReport(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = accountReportNativeQueryService.generateDailyActivityReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of bank contacts for bank book report contact drop down.
	 *
	 * @return A ResponseContainerDto containing a list of GeneralLedgerContactDropDownDto objects.
	 */
	@GetMapping(value = ApiPathConstant.BANK_BOOK_BANK_CONTACT_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<GeneralLedgerContactDropDownDto>> getBankBookBankContactData() {
		return RequestResponseUtils.generateResponseDto(accountReportNativeQueryService.getBankBookBankContactDropDownData());
	}

	/**
	 * Generates a bank book report based on the provided parameters.
	 *
	 * @param startDate The start date of the report in string format or null if not specified.
	 * @param endDate The end date of the report in string format or null if not specified.
	 * @param bankContactId The ID of the bank contact to filter the report.
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @param request The HttpServletRequest object containing the HTTP request details.
	 * @return A ResponseContainerDto containing the generated report as a byte array. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.BANK_BOOK_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateBankBookReport(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.BANK_CONTACT_ID, required = false) Long bankContactId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = accountReportNativeQueryService.generateBankBookReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), bankContactId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates the GST sales report based on the provided parameters.
	 *
	 * @param startDate The start date of the report in string format or null if not specified.
	 * @param endDate The end date of the report in string format or null if not specified.
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @param request The HttpServletRequest object containing the HTTP request details.
	 * @return A ResponseContainerDto containing the generated report as a byte array. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.GST_SALES_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateGstSalesReport(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = accountReportNativeQueryService.generateGstSalesReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a GST Purchase report based on the provided parameters.
	 *
	 * @param startDate The start date of the report in string format or null if not specified.
	 * @param endDate The end date of the report in string format or null if not specified.
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @param request The HttpServletRequest object containing the HTTP request details.
	 * @return A ResponseContainerDto containing the generated report as a byte array. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.GST_PURCHASE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ACCOUNT_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateGstPurchaseReport(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = accountReportNativeQueryService.generateGstPurchaseReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

}