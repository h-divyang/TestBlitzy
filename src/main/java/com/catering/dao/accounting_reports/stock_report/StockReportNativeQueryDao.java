package com.catering.dao.accounting_reports.stock_report;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.DateWiseStockReportDto;
import com.catering.dto.tenant.request.DateWiseStockSummaryReportDto;
import com.catering.dto.tenant.request.StockLedgerRawMaterialDropDownDto;

/**
 * Repository interface for querying stock report-related data from the database.
 * This interface extends {@link JpaRepository} and provides custom queries for
 * generating stock ledger reports, stock summary reports, and dropdown data for raw materials.
 * The queries are executed using native SQL, defined by the names of the corresponding SQL queries.
 */
public interface StockReportNativeQueryDao extends JpaRepository<StockReportNativeQuery, Long> {

	/**
	 * Retrieves a list of raw materials for the stock ledger dropdown.
	 * 
	 * @return a list of {@link StockLedgerRawMaterialDropDownDto} objects
	 */
	@Query(name = "getRawMaterialForStockLedgerDropDown", nativeQuery = true)
	List<StockLedgerRawMaterialDropDownDto> getRawMaterialForStockLedgerDropDown();

	/**
	 * Retrieves the date-wise stock ledger report based on the provided date range and raw material ID.
	 *
	 * @param startDate the start date of the report
	 * @param endDate the end date of the report
	 * @param rawMaterialId the ID of the raw material to filter the report
	 * @param langType the language type for the report
	 * @return a list of {@link DateWiseStockReportDto} objects
	 */
	@Query(name = "getDateWiseStockLedgerReport", nativeQuery = true)
	List<DateWiseStockReportDto> getDateWiseStockLedgerReport(LocalDate startDate, LocalDate endDate, Long rawMaterialId, Integer langType);

	/**
	 * Retrieves the name of the raw material for the stock ledger report.
	 *
	 * @param rawMaterialId the ID of the raw material
	 * @param langType the language type for the report
	 * @return a {@link DateWiseStockReportDto} object containing the raw material name
	 */
	@Query(name = "getRawMaterialNameForStockLedgerReport", nativeQuery = true)
	DateWiseStockReportDto getRawMaterialNameForStockLedgerReport(Long rawMaterialId, Integer langType);

	/**
	 * Retrieves a list of raw material categories for the stock summary dropdown.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} objects
	 */
	@Query(name = "getRawMaterialCategoryForStockSummaryDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getRawMaterialCategoryForStockSummaryDropDown();

	/**
	 * Retrieves a list of raw materials for the stock summary dropdown based on the raw material category ID.
	 *
	 * @param rawMaterialCategoryId the ID of the raw material category
	 * @return a list of {@link DateWiseReportDropDownCommonDto} objects
	 */
	@Query(name = "getRawMaterialForStockSummaryDropDown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getRawMaterialForStockSummaryDropDown(Long rawMaterialCategoryId);

	/**
	 * Retrieves the stock summary report based on the raw material category and raw material IDs.
	 *
	 * @param rawMaterialCategoryId the ID of the raw material category
	 * @param rawMaterialId the ID of the raw material
	 * @param langType the language type for the report
	 * @return a list of {@link DateWiseStockSummaryReportDto} objects
	 */
	@Query(name = "getStockSummaryReport", nativeQuery = true)
	List<DateWiseStockSummaryReportDto> getStockSummaryReport(Long rawMaterialCategoryId, Long rawMaterialId, Integer langType);

}