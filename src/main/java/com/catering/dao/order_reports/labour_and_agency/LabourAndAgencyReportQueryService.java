package com.catering.dao.order_reports.labour_and_agency;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.ChefOrOutsideLabourReportParams;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.LabourReportParams;

public interface LabourAndAgencyReportQueryService {

	/**
	 * Generates a booking report based on the provided parameters.
	 *
	 * This method processes the specified order, language type, language code, and current date to generate
	 * a booking report in the form of a {@link FileBean}.
	 *
	 * @param orderId The unique identifier of the order for which the booking report is generated.
	 * @param langType The language type for localization.
	 * @param langCode The language code for the report's language.
	 * @return A {@link FileBean} representing the generated booking report.
	 */
	FileBean generateBookingReport(Long orderId, Integer langType, String langCode, String reportName);

	/**
	 * Generates a chef labour report for a specific order, language type, and language code.
	 *
	 * @param orderId - The unique identifier of the order for which the report is generated.
	 * @param langType - The language type identifier to specify the language type of the report.
	 * @param langCode - The language code to specify the language of the report.
	 * @param currentDate The current date for which the report is generated.
	 * @return A FileBean object representing the chef labour report file.
	 */
	FileBean generateChefLabourReport(Long orderId, ChefOrOutsideLabourReportParams params, String reportName, HttpServletRequest request);

	/**
	 * Generates an Outside Agency Report in a FileBean format for a specific order.
	 *
	 * @param orderId The ID of the order for which the report is generated.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param currentDate The current date for which the report is generated.
	 * @return A FileBean containing the Outside Agency Report.
	 */
	FileBean generateOutsideAgencyReport(Long orderId, ChefOrOutsideLabourReportParams params, String reportName, HttpServletRequest request);

	/**
	 * Generates a Chef Labour Chithhi Report in a FileBean format for a specific order.
	 *
	 * @param orderId The ID of the order for which the report is generated.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param currentDate The current date for which the report is generated.
	 * @return A FileBean containing the Chef Labour Chithhi Report.
	 */
	FileBean generateChefLabourChithhiReport(Long orderId, ChefOrOutsideLabourReportParams params, String reportName, HttpServletRequest request);

	/**
	 * Generates an Outside Agency Chithhi Report in a FileBean format for a specific order.
	 *
	 * @param orderId The ID of the order for which the report is generated.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param currentDate The current date for which the report is generated.
	 * @return A FileBean containing the Outside Agency Chithhi Report.
	 */
	FileBean generateOutsideAgencyChithhiReport(Long orderId, ChefOrOutsideLabourReportParams params, String reportName, HttpServletRequest request);

	/**
	 * Generates a labour report for a given event agency distribution order.
	 *
	 * @param orderId The unique identifier of the event agency distribution order for which the report is generated.
	 * @param langType (Optional) The language type for the report. If provided, the report will be generated in the specified language.
	 * @param langCode (Optional) The language code for the report. If provided, the report will be generated in the specified language.
	 * @param currentDate The current date for which the report is generated.
	 * @return A FileBean representing the generated labour report, or null if the report generation was unsuccessful.
	 */
	FileBean generateLabourReport(Long orderId, LabourReportParams labourReportParams, String reportName, HttpServletRequest request);

	/**
	 * Generates a Labour Chithhi Report for a specific order.
	 *
	 * This method generates a Labour Chithhi Report for the given order and returns it as a `FileBean`. It populates the report with parameters such as language code, report type, and report sub-type. It also sets the company logo in the report. The report is created based on the provided `orderId`, `langType`, and additional parameters for loading, unloading, and time slots.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType (Optional) The language type for the report, if specified.
	 * @param langCode (Optional) The language code for the report, if specified.
	 * @param currentDate The current date for which the report is generated.
	 * @return A `FileBean` containing the generated Labour Chithhi Report, or null if the report could not be generated.
	 */
	FileBean generateLabourChithhiReport(Long orderId, LabourReportParams labourReportParams, String reportName, HttpServletRequest request);

	/**
	 * Retrieves active functions related to chefs or outside labour for a specified order and order type.
	 *
	 * @param orderId   the unique identifier of the order
	 * @param orderType the type of the order
	 * @return a list of {@link FunctionPerOrderDto} containing active functions for chefs or outside labour
	 */
	List<FunctionPerOrderDto> findFunctionForChefOrOutside(Long orderId, Integer orderType);

	/**
	 * Retrieves active functions related to labour for a specified order.
	 *
	 * @param orderId the unique identifier of the order
	 * @return a list of {@link FunctionPerOrderDto} containing active functions for labour
	 */
	List<FunctionPerOrderDto> findFunctionForLabour(Long orderId);

}
