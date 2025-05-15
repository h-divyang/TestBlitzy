package com.catering.dao.accounting_reports.transaction_report;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.catering.bean.FileBean;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryServiceImpl;
import com.catering.dto.tenant.request.DateWisePurchaseOrderReportDto;
import com.catering.dto.tenant.request.DateWisePurchaseOrderWithoutRawMaterialDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.DateWiseBankPaymentReportDto;
import com.catering.dto.tenant.request.DateWiseCashPaymentReportDto;
import com.catering.dto.tenant.request.DateWiseInputTransferHallReportDto;
import com.catering.service.common.JasperReportService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link TransactionReportNativeQueryService} interface that provides 
 * methods for generating various transaction-related reports, including cash payment receipts, 
 * bank payment receipts, purchase orders, purchase bills, debit notes, and raw material transfers. 
 * This service leverages the {@link TransactionReportNativeQueryDao} to retrieve necessary data 
 * and the {@link JasperReportService} to generate reports in the desired formats. 
 * It also integrates with the {@link CompanyPreferencesNativeQueryServiceImpl} to apply company 
 * preferences for the reports.
 *
 * The service provides functionality for generating date-wise reports based on criteria such as 
 * date range, supplier category, transaction type, and language preferences. It is responsible 
 * for invoking the appropriate DAO methods, processing the data, and returning the reports in 
 * formats suitable for business operations, such as PDF or Excel files.
 *
 * Each report generation method is designed to accommodate various filtering options, 
 * such as the inclusion or exclusion of items, sorting preferences, and multi-language support. 
 * The reports assist with financial management, procurement analysis, and inventory control.
 * 
 * This class is intended to be used as a service that is injected into controllers or other 
 * service classes for reporting purposes.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionReportNativeQueryServiceImpl implements TransactionReportNativeQueryService{

	/**
	 * DAO interface for executing native queries related to transaction reports. 
	 * This is used to fetch data for various transaction-related reports such as 
	 * cash payment receipts, bank payment receipts, purchase orders, and more. 
	 * It is injected into the service to handle database operations efficiently.
	 */
	TransactionReportNativeQueryDao transactionReportNativeQueryDao;

	/**
	 * Service responsible for generating Jasper reports. 
	 * It is used to compile and export transaction-related reports, such as 
	 * purchase orders, payments, and debit notes, in formats like PDF or Excel.
	 * The service interacts with the DAO to retrieve necessary data and generate 
	 * the final report output.
	 */
	JasperReportService jasperReportService;

	/**
	 * Service responsible for fetching company preferences from the database. 
	 * It is used to retrieve company-specific configurations or settings that 
	 * might influence the report generation, such as language preferences or 
	 * other customizations.
	 */
	CompanyPreferencesNativeQueryServiceImpl companyPreferencesNativeQueryServiceImpl;

	// Used for reference to known whether report is of input transfer hall or raw material return to hall
	String IS_RAW_MATERIAL_RETURN_HALL = "IS_RAW_MATERIAL_RETURN_HALL";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getSuppilerContactForCashPaymentReceiptDropDown(LocalDate startDate, LocalDate endDate, Integer transactionTypeId) {
		return transactionReportNativeQueryDao.getSuppilerContactForCashPaymentReceiptDropDown(startDate, endDate, transactionTypeId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateCashPaymentReceiptReport(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long transactionTypeId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		List<DateWiseCashPaymentReportDto> dateWiseCashPaymentReport = transactionReportNativeQueryDao.getDateWiseCashPaymentReceiptReport(startDate, endDate, supplierCategoryId, transactionTypeId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_CASH_PAYMENT_REPORT, reportName);
		return jasperReportService.generatePdfReport(dateWiseCashPaymentReport, parameters, JasperReportNameConstant.DATE_WISE_CASH_PAYMENT_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getSuppilerContactForBankPaymentReceiptDropDown(LocalDate startDate, LocalDate endDate, Integer transactionTypeId) {
		return transactionReportNativeQueryDao.getSuppilerContactForBankPaymentReceiptDropDown(startDate, endDate, transactionTypeId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateBankPaymentReceiptReport(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long transactionTypeId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		List<DateWiseBankPaymentReportDto> dateWiseBankPaymentReport = transactionReportNativeQueryDao.getDateWiseBankPaymentReceiptReport(startDate, endDate, supplierCategoryId, transactionTypeId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_BANK_PAYMENT_REPORT, reportName);
		return jasperReportService.generatePdfReport(dateWiseBankPaymentReport, parameters, JasperReportNameConstant.DATE_WISE_BANK_PAYMENT_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getSuppilerContactForPurchaseOrderDropDown(Long contactCategoryTypeId, LocalDate startDate, LocalDate endDate) {
		return transactionReportNativeQueryDao.getSuppilerContactForPurchaseOrderDropDown(contactCategoryTypeId, startDate, endDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getRawMaterialForPurchaseOrderDropDownData(LocalDate startDate, LocalDate endDate, Long supplierCategoryId) {
		return transactionReportNativeQueryDao.getRawMaterialForPurchaseOrderDropDown(startDate, endDate, supplierCategoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWisePurchaseOrderReportWithItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		List<DateWisePurchaseOrderReportDto> dateWisePurchaseOrderReportWithItems = transactionReportNativeQueryDao.getDateWisePurchaseOrderReportWithitems(startDate, endDate, supplierCategoryId, rawMaterialId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_PURCHASE_ORDER_REPORT_WITH_ITEMS, reportName);
		return jasperReportService.generatePdfReport(dateWisePurchaseOrderReportWithItems, parameters, JasperReportNameConstant.DATE_WISE_PURCHASE_ORDER_REPORT_WITH_ITEMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWisePurchaseOrderReportWithoutItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		List<DateWisePurchaseOrderWithoutRawMaterialDto> dateWisePurchaseOrderReportWithoutItems = transactionReportNativeQueryDao.getDateWisePurchaseOrderReportWithoutItems(startDate, endDate, supplierCategoryId, rawMaterialId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_PURCHASE_ORDER_REPORT_WITHOUT_ITEMS, reportName);
		return jasperReportService.generatePdfReport(dateWisePurchaseOrderReportWithoutItems, parameters, JasperReportNameConstant.DATE_WISE_PURCHASE_ORDER_REPORT_WITHOUT_ITEMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getSuppilerContactForPurchaseBillDropDown(LocalDate startDate, LocalDate endDate, Long contactCategoryTypeId) {
		return transactionReportNativeQueryDao.getSuppilerContactForPurchaseBillDropDown(startDate, endDate, contactCategoryTypeId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getRawMaterialForPurchaseBillDropDownData(LocalDate startDate, LocalDate endDate, Long suppilerCategoryId) {
		return transactionReportNativeQueryDao.getRawMaterialForPurchaseBillDropDown(startDate, endDate, suppilerCategoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWisePurchaseBillReportWithItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		List<DateWisePurchaseOrderReportDto> dateWisePurchaseBillReportWithItems = transactionReportNativeQueryDao.getDateWisePurchaseBillReportWithitems(startDate, endDate, supplierCategoryId, rawMaterialId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_PURCHASE_BILL_REPORT_WITH_ITEMS, reportName);
		return jasperReportService.generatePdfReport(dateWisePurchaseBillReportWithItems, parameters, JasperReportNameConstant.DATE_WISE_PURCHASE_BILL_REPORT_WITH_ITEMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWisePurchaseBillReportWithoutItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		List<DateWisePurchaseOrderWithoutRawMaterialDto> dateWisePurchaseBillReportWithoutItems = transactionReportNativeQueryDao.getDateWisePurchaseBillReportWithoutItems(startDate, endDate, supplierCategoryId, rawMaterialId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_PURCHASE_BILL_REPORT_WITHOUT_ITEMS, reportName);
		return jasperReportService.generatePdfReport(dateWisePurchaseBillReportWithoutItems, parameters, JasperReportNameConstant.DATE_WISE_PURCHASE_BILL_REPORT_WITHOUT_ITEMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getInputTransferToHallDropDown(LocalDate startDate, LocalDate endDate) {
		return transactionReportNativeQueryDao.getInputTransferToHallDropDown(startDate, endDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getRawMaterialForInputTransferDropDownData(LocalDate startDate, LocalDate endDate, Long hallId) {
		return transactionReportNativeQueryDao.getRawMaterialForInputTransferToHallDropDown(startDate, endDate, hallId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseInputTransferHallReportWithItems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		parameters.put(IS_RAW_MATERIAL_RETURN_HALL, "0");
		List<DateWiseInputTransferHallReportDto> dateWisePurchaseBillReportWithItems = transactionReportNativeQueryDao.getDateWiseInputTransferHallReportWithitems(startDate, endDate, hallId, rawMaterialId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITH_ITEMS, reportName);
		return jasperReportService.generatePdfReport(dateWisePurchaseBillReportWithItems, parameters, JasperReportNameConstant.DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITH_ITEMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseInputTransferHallReportWithoutItems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		parameters.put(IS_RAW_MATERIAL_RETURN_HALL, "0");
		List<DateWiseInputTransferHallReportDto> dateWiseInputTransferHallReportWithoutItems = transactionReportNativeQueryDao.getDateWiseInputTransferHallReportWithoutItems(startDate, endDate, hallId, rawMaterialId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITHOUT_ITEMS, reportName);
		return jasperReportService.generatePdfReport(dateWiseInputTransferHallReportWithoutItems, parameters, JasperReportNameConstant.DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITHOUT_ITEMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getSuppilerContactForDebitNoteDropDown(LocalDate startDate, LocalDate endDate, Long contactCategoryTypeId) {
		return transactionReportNativeQueryDao.getSuppilerContactForDebitNoteDropDown(startDate, endDate, contactCategoryTypeId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getRawMaterialForDebitNoteDropDownData(LocalDate startDate, LocalDate endDate, Long suppilerCategoryId) {
		return transactionReportNativeQueryDao.getRawMaterialForDebitNoteDropDown(startDate, endDate, suppilerCategoryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseDebitNoteReportWithItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		List<DateWisePurchaseOrderReportDto> dateWiseDebitNoteReportWithItems = transactionReportNativeQueryDao.getDateWiseDebitNoteReportWithitems(startDate, endDate, supplierCategoryId, rawMaterialId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_DEBIT_NOTE_REPORT_WITH_ITEMS, reportName);
		return jasperReportService.generatePdfReport(dateWiseDebitNoteReportWithItems, parameters, JasperReportNameConstant.DATE_WISE_DEBIT_NOTE_REPORT_WITH_ITEMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseDebitNoteReportWithoutItems(LocalDate startDate, LocalDate endDate, Long supplierCategoryId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		List<DateWisePurchaseOrderWithoutRawMaterialDto> dateWiseDebitNoteReportWithoutItems = transactionReportNativeQueryDao.getDateWiseDebitNoteReportWithoutItems(startDate, endDate, supplierCategoryId, rawMaterialId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_DEBIT_NOTE_REPORT_WITHOUT_ITEMS, reportName);
		return jasperReportService.generatePdfReport(dateWiseDebitNoteReportWithoutItems, parameters, JasperReportNameConstant.DATE_WISE_DEBIT_NOTE_REPORT_WITHOUT_ITEMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getRawMaterialReturnToHallDropDown(LocalDate startDate, LocalDate endDate) {
		return transactionReportNativeQueryDao.getRawMaterialReturnToHallDropDown(startDate, endDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DateWiseReportDropDownCommonDto> getRawMaterialForRawMaterialReturnHallDropDownData(LocalDate startDate, LocalDate endDate, Long hallId) {
		return transactionReportNativeQueryDao.getRawMaterialForRawMaterialReturnToHallDropDown(startDate, endDate, hallId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseRawMaterialReturnHallReportWithItems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		parameters.put(IS_RAW_MATERIAL_RETURN_HALL, "1");
		List<DateWiseInputTransferHallReportDto> dateWiseRawMaterialReturnToHallReportWithItems = transactionReportNativeQueryDao.getDateWiseRawMaterialReturnHallReportWithitems(startDate, endDate, hallId, rawMaterialId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITH_ITEMS, reportName);
		return jasperReportService.generatePdfReport(dateWiseRawMaterialReturnToHallReportWithItems, parameters, JasperReportNameConstant.DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITH_ITEMS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean generateDateWiseRawMaterialReturnHallReportWithoutItems(LocalDate startDate, LocalDate endDate, Long hallId, Long rawMaterialId, Long orderById, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.TransactionReports.TransactionReport.class, StringUtils.dotSeparated(JasperParameterConstants.TRANSACTION_REPORTS, JasperParameterConstants.TransactionReports.TRANSACTION_REPORT));
		setCommonDataInAllTransactionReport(parameters, langType, reportName, request);
		parameters.put(IS_RAW_MATERIAL_RETURN_HALL, "1");
		List<DateWiseInputTransferHallReportDto> dateWiseRawMaterialReturnHallReportWithoutItems = transactionReportNativeQueryDao.getDateWiseRawMaterialReturnHallReportWithoutitems(startDate, endDate, hallId, rawMaterialId, orderById, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITHOUT_ITEMS, reportName);
		return jasperReportService.generatePdfReport(dateWiseRawMaterialReturnHallReportWithoutItems, parameters, JasperReportNameConstant.DATE_WISE_INPUT_TRANSFER_HALL_REPORT_WITHOUT_ITEMS);
	}

	/**
	 * Sets common data in the parameters map for generating transaction-related reports.
	 * This includes adding the company logo, loading company preferences based on the 
	 * language type, and setting the appropriate decimal patterns for the report.
	 *
	 * @param parameters A map that holds the parameters to be passed to the Jasper report.
	 * These parameters will include the company logo, company preferences, and decimal patterns.
	 * @param langType An integer representing the language type to be used in the report.
	 * This is used to fetch the correct language-specific preferences.
	 * @param request The HttpServletRequest object that contains the request information. 
	 * It is used to retrieve the company logo to be included in the report.
	 */
	private void setCommonDataInAllTransactionReport(Map<String, Object> parameters, Integer langType, String reportName, HttpServletRequest request) {
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setDecimalPatterns(parameters);
	}

}