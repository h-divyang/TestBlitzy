package com.catering.dao.order_reports.menu_preparation;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;
import com.catering.constant.ReportParameterConstants;
import com.catering.dto.tenant.request.CommonDataForTheReportDto;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.MenuPreparationCustomMenuReportDto;

/**
 * The {@code MenuPreparationReportQueryService} interface provides methods for generating various types 
 * of menu preparation reports. These reports include custom menu reports, simple menu reports, 
 * two-language menu reports, and exclusive menu reports, tailored for different order requirements.
 *
 * <p>This service is used to fetch report data based on order details and language preferences 
 * and is commonly used for generating JasperReports in the application.</p>
 *
 */
public interface MenuPreparationReportQueryService {

	/**
	 * Generates a custom menu report for a given order.
	 *
	 * This method takes the order ID, language type, language code, and current date as parameters to custom a simple menu report for the specified order. 
	 * The custom menu report provides information about the menu preparation associated with the order.
	 *
	 * @param orderId The unique identifier of the order for which the custom menu report is generated.
	 * @param langType The language type used for the report. This could be an integer representing a specific language type.
	 * @param langCode The language code specifying the language in which the report should be generated (e.g., "en" for English, "es" for Spanish).
	 *
	 * @return FileBean A FileBean object representing the generated custom menu report file. This file contains information about the menu preparation associated with the specified order.
	 */
	FileBean generateCustomMenuPreparation(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a simple menu report as a file for a specified order.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param functionId An array of function IDs associated with the report.
	 * @param langType The language type identifier for the report generation.
	 * @param langCode The language code for localizing the report content.
	 * @param request The HTTP servlet request containing client request details.
	 * @return A {@code FileBean} containing the generated simple menu report.
	 */
	FileBean generateSimpleMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a two-language menu report for a specified order.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param functionId An array of function IDs associated with the report.
	 * @param langType The language type identifier for the report generation.
	 * @param defaultLang The default language code for the report.
	 * @param preferLang The preferred language code for the report.
	 * @param langCode The language code for localizing the report content.
	 * @param request The HTTP servlet request containing client request details.
	 * @return A {@code FileBean} containing the generated two-language menu report.
	 */
	FileBean generateTwoLanguageMenuReport(Long orderId, Long[] functionId, Integer langType, String defaultLang, String preferLang, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates an exclusive menu report as a file for a specified order.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param functionId An array of function IDs associated with the report.
	 * @param langType The language type identifier for the report generation.
	 * @param langCode The language code for localizing the report content.
	 * @param request The HTTP servlet request containing client request details.
	 * @return A {@code FileBean} containing the generated exclusive menu report.
	 */
	FileBean generateExclusiveMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a manager's menu report for a specified order.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType The language type identifier for the report.
	 * @param langCode The language code for localization.
	 * @return A {@link FileBean} object representing the generated report in PDF format.
	 */
	FileBean generateManagerMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a menu report with instruction for a specified order.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType The language type identifier for the report.
	 * @param langCode The language code for localization.
	 * @return A {@link FileBean} object representing the generated report in PDF format.
	 */
	FileBean generateInstructionMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a menu preparation report with images 1 for a specific order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @param langCode The language code for localization.
	 * @return A `FileBean` containing the generated menu preparation report.
	 */
	FileBean generatePremiumImageMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a menu preparation report with image menu report for a specific order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @param langCode The language code for localization.
	 * @return A `FileBean` containing the generated menu preparation report.
	 */
	FileBean generateImageMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates an image-based menu category report for a specific order.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param functionId An array of function IDs associated with the order.
	 * @param langType The language type identifier for the report generation.
	 * @param langCode The language code for localizing the report content.
	 * @param request The HTTP servlet request containing client request details.
	 * @return A {@code FileBean} containing the generated image-based menu category report.
	 */
	FileBean generateImageMenuCategoryReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a menu preparation report with slogans for a specific order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @param langCode The language code for localization.
	 * @return A `FileBean` containing the generated menu preparation report.
	 */
	FileBean generateSloganMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a menu preparation report with both images and slogans for a specific order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @param langCode The language code for localization.
	 * @return A `FileBean` containing the generated menu preparation report.
	 */
	FileBean generateMenuWithImageAndSloganReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Sets the common data values in the provided parameters map for generating a
	 * report. Retrieves common data for the specified order ID and language type
	 * from the menu preparation report query DAO. If common data is available, it
	 * populates the relevant parameters with the retrieved values.
	 *
	 * @param parameters A map containing parameters for generatineg a report.
	 * @param orderId The unique identifier for the order.
	 * @param langType The language type for the report.
	 * @return The modified parameters map with common data values added, or the original map if no common data is found.
	 *
	 * @see CommonDataForTheReportDto
	 * @see ReportParameterConstants
	 * @see menuPreparationReportQueryDao
	 */
	Map<String, Object> setTheCommonDataInParameter(Map<String, Object> parameters, Long orderId, Integer langType, String reportName);

	/**
	 * Sets common data parameters for a report without venue details.
	 *
	 * @param parameters The map of report parameters to be set.
	 * @param orderId The unique identifier of the order for which data is being set.
	 * @param langType The language type identifier for localization purposes.
	 */
	Map<String, Object> setTheCommonDataInParameterWithOutVenue(Map<String, Object> parameters, Long orderId, Integer langType);

	/**
	 * Generates a custom menu report for a specific order and functions.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param functionId A list of function IDs included in the report.
	 * @param langType The language type identifier for the report generation.
	 * @param langCode The language code for localizing the report content.
	 * @return A list of {@code MenuPreparationCustomMenuReportDto} containing the custom menu report data.
	 */
	List<MenuPreparationCustomMenuReportDto> generateCustomMenuReport(Long orderId, List<Long> functionId, Integer langType, String langCode);

	/**
	 * Generates a table menu report as a file for a specific order.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType The language type identifier for the report generation.
	 * @param langCode The language code for localizing the report content.
	 * @param functionId An array of function IDs associated with the report.
	 * @param request The HTTP servlet request containing client request details.
	 * @param jasperReportNameConstant The name constant for the Jasper report template.
	 * @return A {@code FileBean} containing the generated table menu report.
	 */
	FileBean generateTableMenuReport(Long orderId, Integer langType, String langCode, Long[] functionId, HttpServletRequest request, String jasperReportNameConstant);

	/**
	 * Generates a table menu document report as a file for a specific order.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType The language type identifier for the report generation.
	 * @param langCode The language code for localizing the report content.
	 * @param functionId An array of function IDs associated with the report.
	 * @param request The HTTP servlet request containing client request details.
	 * @param jasperReportNameConstant The name constant for the Jasper report template.
	 * @return A {@code FileBean} containing the generated table menu document report.
	 */
	FileBean generateTableMenuDocReport(Long orderId, Integer langType, String langCode, Long[] functionId, HttpServletRequest request, String jasperReportNameConstant);

	/**
	 * Retrieves a list of functions for a specific order used in menu preparation.
	 *
	 * @param orderId The unique identifier of the order.
	 * @return A list of {@code FunctionPerOrderDto} containing the functions related to the specified order.
	 */
	List<FunctionPerOrderDto> getFunctionsPerOrderForMenuPreparation(Long orderId);

}