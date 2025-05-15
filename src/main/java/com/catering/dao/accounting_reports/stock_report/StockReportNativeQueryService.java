package com.catering.dao.accounting_reports.stock_report;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.StockLedgerRawMaterialDropDownDto;

/**
 * Service interface for handling stock report-related operations.
 * Provides methods for fetching raw material data for dropdowns, 
 * generating date-wise stock ledger reports, and generating stock summary reports.
 * The reports are generated based on various filters such as date range and raw material IDs.
 */
public interface StockReportNativeQueryService {

	/**
	 * Retrieves a list of raw materials for the stock ledger dropdown.
	 *
	 * @return a list of {@link StockLedgerRawMaterialDropDownDto} objects
	 */
	List<StockLedgerRawMaterialDropDownDto> getRawMaterialForStockLedgerDropDown();

	/**
	 * Generates a date-wise stock ledger report based on the provided date range and raw material ID.
	 *
	 * @param startDate the start date of the report
	 * @param endDate the end date of the report
	 * @param rawMaterialId the ID of the raw material to filter the report
	 * @param langType the language type for the report
	 * @param langCode the language code for the report
	 * @param request the {@link HttpServletRequest} containing request data
	 * @return a {@link FileBean} object containing the generated report
	 */
	FileBean generateDateWiseStockLedgerReport(LocalDate startDate, LocalDate endDate, Long rawMaterialId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Retrieves a list of raw material categories for the stock summary dropdown.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} objects
	 */
	List<DateWiseReportDropDownCommonDto> getRawMaterialCategoryForStockSummaryDropDown();

	/**
	 * Retrieves a list of raw materials for the stock summary dropdown based on the raw material category ID.
	 *
	 * @param rawMaterialCategoryId the ID of the raw material category
	 * @return a list of {@link DateWiseReportDropDownCommonDto} objects
	 */
	List<DateWiseReportDropDownCommonDto> getRawMaterialForStockSummaryDropDown(Long rawMaterialCategoryId);

	/**
	 * Generates a stock summary report based on the raw material category and raw material IDs.
	 *
	 * @param rawMaterialCategoryId the ID of the raw material category
	 * @param rawMaterialId the ID of the raw material
	 * @param langType the language type for the report
	 * @param langCode the language code for the report
	 * @param request the {@link HttpServletRequest} containing request data
	 * @return a {@link FileBean} object containing the generated report
	 */
	FileBean generateStockSummaryReport(Long rawMaterialCategoryId, Long rawMaterialId, Integer langType, String langCode, String reportName, HttpServletRequest request);

}