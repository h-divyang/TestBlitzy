package com.catering.dao.accounting_reports.individual_record_report;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.catering.bean.FileBean;
import com.catering.constant.JasperParameterConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.ReportParameterConstants;
import com.catering.dao.company_preferences.CompanyPreferencesNativeQueryServiceImpl;
import com.catering.dto.tenant.request.IndividualRecordBankPaymentReceiveReportDto;
import com.catering.dto.tenant.request.IndividualRecordCashPaymentReceiveReportDto;
import com.catering.dto.tenant.request.IndividualRecordCommonDto;
import com.catering.dto.tenant.request.IndividualRecordInputTransferToHallReportDto;
import com.catering.dto.tenant.request.IndividualRecordJournalVoucherReportDto;
import com.catering.repository.tenant.ContactRepository;
import com.catering.service.common.JasperReportService;
import com.catering.util.DataUtils;
import com.catering.util.JasperUtils;
import com.catering.util.NumberToStringUtils;
import com.catering.util.StringUtils;
import java.util.Collections;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link IndividualRecordReportNativeQueryService} interface.
 * This service provides methods for generating various individual record reports such as 
 * cash payment receipt, bank payment receipt, journal voucher, purchase orders, 
 * purchase bills, debit notes, and raw material returns. It utilizes the repository 
 * layer for fetching data and the JasperReport service for report generation.
 * 
 * Reports are generated in various formats based on the provided parameters such as 
 * report type, language, and other specific filtering criteria. The service also handles 
 * localization using language type and code.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IndividualRecordReportNativeQueryServiceImpl implements IndividualRecordReportNativeQueryService {

	/**
	 * Data access object for querying individual record report data.
	 */
	IndividualRecordReportNativeQueryDao individualRecordReportNativeQueryDao;

	/**
	 * Service for generating reports using JasperReports.
	 */
	JasperReportService jasperReportService;

	/**
	 * Service for retrieving company preferences.
	 */
	CompanyPreferencesNativeQueryServiceImpl companyPreferencesNativeQueryServiceImpl;

	/**
	 * Repository for managing contact-related data.
	 */
	ContactRepository contactRepository;

	/**
	 * Constant for checking if raw material return to hall report should be generated.
	 */
	String IS_RAW_MATERIAL_RETURN_TO_HALL_REPORT = "IS_RAW_MATERIAL_RETURN_TO_HALL_REPORT";

	/**
	 * Constant for handling extra expenses in reports.
	 */
	String EXTRA_EXPENSE = "EXTRA_EXPENSE";

	/**
	 * Constant for handling discounts in reports.
	 */
	String DISCOUNT = "DISCOUNT";

	/**
	 * Constant for handling rounding adjustments in reports.
	 */
	String ROUND_OFF = "ROUND_OFF";

	/**
	 * Constant for representing the grand total in financial reports.
	 */
	String GRAND_TOTAL = "GRAND_TOTAL";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean getIndividualRecordCashPaymentReceiveReport(Long id, int transactionType, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.IndividualRecordReport.CashBankPaymentReceiptReport.class, StringUtils.dotSeparated(JasperParameterConstants.INDIVIDUAL_RECORD_REPORTS, JasperParameterConstants.IndividualRecordReport.CASH_BANK_PAYMENT_RECEIVE_REPORT));
		setLogoAndPatameter(parameters, reportName, request, langType);
		List<IndividualRecordCashPaymentReceiveReportDto> getIndividualRecordCashPaymentReceiveReportData = individualRecordReportNativeQueryDao.getIndividualRecordCashPaymentReceiveReport(id, transactionType, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.INDIVIDUAL_RECORD_CASH_PAYMENT_RECEIPT_REPORT, reportName);
		return jasperReportService.generatePdfReport(getIndividualRecordCashPaymentReceiveReportData, parameters, JasperReportNameConstant.INDIVIDUAL_RECORD_CASH_PAYMENT_RECEIPT_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean getIndividualRecordBankPaymentReceiveReport(Long id, int transactionType, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.IndividualRecordReport.CashBankPaymentReceiptReport.class, StringUtils.dotSeparated(JasperParameterConstants.INDIVIDUAL_RECORD_REPORTS, JasperParameterConstants.IndividualRecordReport.CASH_BANK_PAYMENT_RECEIVE_REPORT));
		setLogoAndPatameter(parameters, reportName, request, langType);
		List<IndividualRecordBankPaymentReceiveReportDto> getIndividualRecordBankPaymentReceiveReportData = individualRecordReportNativeQueryDao.getIndividualRecordBankPaymentReceiveReport(id, transactionType, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.INDIVIDUAL_RECORD_BANK_PAYMENT_RECEIPT_REPORT, reportName);
		return jasperReportService.generatePdfReport(getIndividualRecordBankPaymentReceiveReportData, parameters, JasperReportNameConstant.INDIVIDUAL_RECORD_BANK_PAYMENT_RECEIPT_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean getIndividualRecordJournalVoucherReport(Long id, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.IndividualRecordReport.JournalVoucherReport.class, StringUtils.dotSeparated(JasperParameterConstants.INDIVIDUAL_RECORD_REPORTS, JasperParameterConstants.IndividualRecordReport.JOURNAL_VOUCHER_REPORT));
		setLogoAndPatameter(parameters, reportName, request, langType);
		List<IndividualRecordJournalVoucherReportDto> getIndividualRecordJournalVoucherReportData = individualRecordReportNativeQueryDao.getIndividualRecordJournalVoucherReport(id, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.INDIVIDUAL_RECORD_JOURNAL_VOUCHER_REPORT, reportName);
		return jasperReportService.generatePdfReport(getIndividualRecordJournalVoucherReportData, parameters, JasperReportNameConstant.INDIVIDUAL_RECORD_JOURNAL_VOUCHER_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean getIndividualRecordPurchaseOrderReport(Long id, Long contactId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		return generateReport(id, contactId, langType, langCode, reportName, ReportParameterConstants.INDIVIDUAL_RECORD_PURCHASE_ORDER_REPORT, request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean getIndividualRecordPurchaseBillReport(Long id, Long contactId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		return generateReport(id, contactId, langType, langCode, reportName, ReportParameterConstants.INDIVIDUAL_RECORD_PURCHASE_BILL_REPORT, request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean getIndividualRecordDebitNoteReport(Long id, Long contactId, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		return generateReport(id, contactId, langType, langCode, reportName, ReportParameterConstants.INDIVIDUAL_RECORD_DEBIT_NOTE_REPORT, request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean getIndividualRecordInputTransferToHallReport(Long id, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.IndividualRecordReport.InputTransferToHallReport.class, StringUtils.dotSeparated(JasperParameterConstants.INDIVIDUAL_RECORD_REPORTS, JasperParameterConstants.IndividualRecordReport.INPUT_TRANSFER_TO_HALL_REPORT));
		setLogoAndPatameter(parameters, reportName, request, langType);
		parameters.put(IS_RAW_MATERIAL_RETURN_TO_HALL_REPORT, "0");
		List<IndividualRecordInputTransferToHallReportDto> getIndividualRecordInputTransferToHallReportData = individualRecordReportNativeQueryDao.getIndividualRecordInputTransferToHallReport(id, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.INDIVIDUAL_RECORD_INPUT_TRANSFER_TO_HALL_REPORT, reportName);
		return jasperReportService.generatePdfReport(getIndividualRecordInputTransferToHallReportData, parameters, JasperReportNameConstant.INDIVIDUAL_RECORD_INPUT_TRANSFER_TO_HALL_REPORT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileBean getIndividualRecordRawMaterialReturnToHallReport(Long id, Integer langType, String langCode, String reportName, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.IndividualRecordReport.InputTransferToHallReport.class, StringUtils.dotSeparated(JasperParameterConstants.INDIVIDUAL_RECORD_REPORTS, JasperParameterConstants.IndividualRecordReport.INPUT_TRANSFER_TO_HALL_REPORT));
		setLogoAndPatameter(parameters, reportName, request, langType);
		parameters.put(IS_RAW_MATERIAL_RETURN_TO_HALL_REPORT, "1");
		List<IndividualRecordInputTransferToHallReportDto> getIndividualRecordInputTransferToHallReportData = individualRecordReportNativeQueryDao.getIndividualRecordRawMaterialReturnToHallReport(id, langType);
		jasperReportService.setBackgroundImageInGeneralReport(parameters, JasperReportNameConstant.INDIVIDUAL_RECORD_INPUT_TRANSFER_TO_HALL_REPORT, reportName);
		return jasperReportService.generatePdfReport(getIndividualRecordInputTransferToHallReportData, parameters, JasperReportNameConstant.INDIVIDUAL_RECORD_INPUT_TRANSFER_TO_HALL_REPORT);
	}

	/**
	 * Generates a report based on the given parameters.
	 *
	 * @param id The unique identifier for the report.
	 * @param contactId The contact ID used to check GST number.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param reportType The type of report to generate.
	 * @param request The HTTP request used for setting parameters.
	 * @return A FileBean containing the generated report.
	 */
	private FileBean generateReport(Long id, Long contactId, Integer langType, String langCode, String reportName, String reportType, HttpServletRequest request) {
		Map<String, Object> parameters = JasperUtils.createParameterMap(DataUtils.getLangCode(langCode), JasperParameterConstants.IndividualRecordReport.PurchaseOrderReport.class, StringUtils.dotSeparated(JasperParameterConstants.INDIVIDUAL_RECORD_REPORTS, JasperParameterConstants.IndividualRecordReport.PURCHASE_ORDER_REPORT));
		setLogoAndPatameter(parameters, reportName, request, langType);
		boolean gstSameOrNot = contactRepository.isGstNumberSame(contactId);
		jasperReportService.setDecimalPatterns(parameters);
		List<IndividualRecordCommonDto> reportData = getReportData(id, langType, gstSameOrNot, reportType);
		double totalAmount = 0;
		if (reportData != null) {
			totalAmount = reportData.stream().mapToDouble(IndividualRecordCommonDto::getAmount).sum();
			// If it is purchase bill then set this all fields else set to 0.
			if (reportType.equals(ReportParameterConstants.INDIVIDUAL_RECORD_PURCHASE_BILL_REPORT)) {
				parameters.put(DISCOUNT, reportData.get(0).getDiscount());
				parameters.put(EXTRA_EXPENSE, reportData.get(0).getExtraExpense());
				parameters.put(ROUND_OFF, reportData.get(0).getRoundOff());
				parameters.put(GRAND_TOTAL, reportData.get(0).getGrandTotal());
			} else {
				parameters.put(DISCOUNT, 0.0);
				parameters.put(EXTRA_EXPENSE, 0.0);
				parameters.put(ROUND_OFF, 0.0);
				parameters.put(GRAND_TOTAL, totalAmount);
			}
		}
		// If the amount is more than karba then will not display amount in words 
		parameters.put("AMOUNT_IN_WORDS", String.valueOf((long)totalAmount).length() <= 13 ? new NumberToStringUtils().convertToWords(Math.round(totalAmount)) : "" );
		jasperReportService.setBackgroundImageInGeneralReport(parameters, gstSameOrNot ? getReportName(reportType, true) : getReportName(reportType, false), reportName);
		return jasperReportService.generatePdfReport(reportData, parameters, gstSameOrNot ? getReportName(reportType, true) : getReportName(reportType, false));
	}

	/**
	 * Retrieves the data for the report based on the report type and GST status.
	 *
	 * @param id The unique identifier for the report.
	 * @param langType The language type for the report.
	 * @param gstSameOrNot Whether the GST is the same.
	 * @param reportType The type of report to retrieve data for.
	 * @return A list of report data.
	 */
	private List<IndividualRecordCommonDto> getReportData(Long id, Integer langType, boolean gstSameOrNot, String reportType) {
		if (reportType.equals(ReportParameterConstants.INDIVIDUAL_RECORD_PURCHASE_ORDER_REPORT)) {
			return gstSameOrNot ? individualRecordReportNativeQueryDao.getPurchaseOrderGstDifferentStateReport(id, langType) : individualRecordReportNativeQueryDao.getPurchaseOrderGstSameStateReport(id, langType);
		} else if (reportType.equals(ReportParameterConstants.INDIVIDUAL_RECORD_PURCHASE_BILL_REPORT)) {
			return gstSameOrNot ? individualRecordReportNativeQueryDao.getPurchaseBillGstDifferentStateReport(id, langType) : individualRecordReportNativeQueryDao.getPurchaseBillGstSameStateReport(id, langType);
		} else if (reportType.equals(ReportParameterConstants.INDIVIDUAL_RECORD_DEBIT_NOTE_REPORT)) {
			return gstSameOrNot ? individualRecordReportNativeQueryDao.getDebitNoteGstDifferentStateReport(id, langType) : individualRecordReportNativeQueryDao.getDebitNoteGstSameStateReport(id, langType);
		}
		return Collections.emptyList();
	}

	/**
	 * Sets common parameters like company logo and decimal patterns.
	 *
	 * @param parameters The parameter map to populate.
	 * @param request The HTTP request to get the logo from.
	 * @param langType The language type for the report.
	 */
	private void setLogoAndPatameter(Map<String, Object> parameters, String reportName, HttpServletRequest request, Integer langType) {
		jasperReportService.setCompanyLogo(parameters, reportName, request);
		companyPreferencesNativeQueryServiceImpl.setTheCommonDataInParameters(parameters, langType, reportName);
		jasperReportService.setDecimalPatterns(parameters);
	}

	/**
	 * Returns the report name based on the report type and GST status.
	 *
	 * @param reportType The type of the report.
	 * @param gstSameOrNot Whether the GST is the same.
	 * @return The report name.
	 * @throws IllegalArgumentException If the report type is invalid.
	 */
	private static String getReportName(String reportType, boolean gstSameOrNot) {
		switch (reportType) {
			case ReportParameterConstants.INDIVIDUAL_RECORD_PURCHASE_ORDER_REPORT:
				return gstSameOrNot ? JasperReportNameConstant.INDIVIDUAL_RECORD_PURCHASE_ORDER_DIFFERENT_STATE_REPORT : JasperReportNameConstant.INDIVIDUAL_RECORD_PURCHASE_ORDER_SAME_STATE_REPORT;
			case ReportParameterConstants.INDIVIDUAL_RECORD_PURCHASE_BILL_REPORT,
				 ReportParameterConstants.INDIVIDUAL_RECORD_DEBIT_NOTE_REPORT:
				return gstSameOrNot ? JasperReportNameConstant.INDIVIDUAL_RECORD_PURCHASE_BILL_DEBIT_NOTE_DIFFERENT_STATE_REPORT : JasperReportNameConstant.INDIVIDUAL_RECORD_PURCHASE_BILL_DEBIT_NOTE_SAME_STATE_REPORT;
			default:
				throw new IllegalArgumentException("Invalid report type: " + reportType);
		}
	}

}