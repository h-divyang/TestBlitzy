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
import com.catering.dao.accounting_reports.stock_report.StockReportNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.StockLedgerRawMaterialDropDownDto;
import com.catering.util.RequestResponseUtils;
import com.catering.util.StringToDateUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller for handling stock report-related requests.
 * This includes retrieving stock ledger reports, stock summary reports, and related drop-down lists for raw materials.
 */
@RestController
@RequestMapping(value = ApiPathConstant.STOCK_REPORTS)
@Tag(name = SwaggerConstant.STOCK_REPORTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StockReportController {

	/**
	 * Service for executing native queries related to stock reports.
	 * Provides functionality for retrieving and generating various stock report data,
	 * such as raw materials for stock ledger and stock summary reports.
	 */
	StockReportNativeQueryService stockReportNativeQueryService;

	/**
	 * Retrieves a list of raw materials for the stock ledger drop-down.
	 *
	 * @return a {@link ResponseContainerDto} containing a list of {@link StockLedgerRawMaterialDropDownDto}.
	 */
	@GetMapping(value = ApiPathConstant.STOCK_LEDGER_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.STOCK_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<StockLedgerRawMaterialDropDownDto>> getRawMaterialBySuppilerId() {
		return RequestResponseUtils.generateResponseDto(stockReportNativeQueryService.getRawMaterialForStockLedgerDropDown());
	}

	/**
	 * Generates a date-wise stock ledger report and returns it as a byte array.
	 *
	 * @param startDate    the start date for the report (optional).
	 * @param endDate      the end date for the report (optional).
	 * @param rawMaterialId the raw material ID to filter by (optional).
	 * @param langType     the language type for the report (optional).
	 * @param langCode     the language code for the report (optional).
	 * @param request      the HTTP request.
	 * @return a {@link ResponseContainerDto} containing the report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_STOCK_LEDGER_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.STOCK_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseStockLedgerReport(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = stockReportNativeQueryService.generateDateWiseStockLedgerReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), rawMaterialId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of raw material categories for the stock summary drop-down.
	 *
	 * @return a {@link ResponseContainerDto} containing a list of {@link DateWiseReportDropDownCommonDto}.
	 */
	@GetMapping(value = ApiPathConstant.STOCK_SUMMARY_RAW_MATERIAL_CATEGORY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.STOCK_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getStockSummaryRawMaterialCategoryList() {
		return RequestResponseUtils.generateResponseDto(stockReportNativeQueryService.getRawMaterialCategoryForStockSummaryDropDown());
	}

	/**
	 * Retrieves a list of raw materials for the stock summary drop-down based on raw material category ID.
	 *
	 * @param rawMaterialCategoryId the raw material category ID to filter by (optional).
	 * @return a {@link ResponseContainerDto} containing a list of {@link DateWiseReportDropDownCommonDto}.
	 */
	@GetMapping(value = ApiPathConstant.STOCK_SUMMARY_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.STOCK_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getStockSummaryRawMaterialList(@RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long rawMaterialCategoryId) {
		return RequestResponseUtils.generateResponseDto(stockReportNativeQueryService.getRawMaterialForStockSummaryDropDown(rawMaterialCategoryId));
	}

	/**
	 * Generates a stock summary report and returns it as a byte array.
	 *
	 * @param rawMaterialCategoryId the raw material category ID to filter by (optional).
	 * @param rawMaterialId         the raw material ID to filter by (optional).
	 * @param langType              the language type for the report (optional).
	 * @param langCode              the language code for the report (optional).
	 * @param request               the HTTP request.
	 * @return a {@link ResponseContainerDto} containing the report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.STOCK_SUMMARY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.STOCK_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> stockSummaryReport(@RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long rawMaterialCategoryId, @RequestParam(name = FieldConstants.RAW_MATERIAL_ID, required = false) Long rawMaterialId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = stockReportNativeQueryService.generateStockSummaryReport(rawMaterialCategoryId, rawMaterialId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

}