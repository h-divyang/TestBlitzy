package com.catering.dao.order_reports.admin_reports;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;

/**
 * Service interface for generating various admin reports based on specific order details and localization options.
 *
 * <p>
 * This service provides methods to generate wastage reports, feedback reports, and supplier details reports for a
 * given order ID, language type, language code, and current date.
 * </p>
 * 
 * @author Krushali Talaviya
 * @since 1 January 2024
 */
public interface AdminReportQueryService {

	/**
	 * Generates a wastage report for a given order ID, language type, and language code.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType (Optional) The language type for localization. If provided, the report may consider this language type.
	 * @param langCode (Optional) The language code for localization. If provided, the report will be generated in the specified language.
	 * @return A FileBean containing the wastage report, or null if the report is not available.
	 */
	FileBean generateWastageReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a feedback report for a specific order, language type, and language code.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType The language type identifier to specify the language type of the report.
	 * @param langCode The language code to specify the language of the report.
	 * @return A FileBean object representing the feedback report file.
	 */
	FileBean generateDishCountingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a supplier details report in PDF format based on the provided order ID and optional language parameters.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType (Optional) The language type for localization.
	 * @param langCode (Optional) The language code for localization.
	 * @return A {@link FileBean} containing the generated supplier details report in PDF format.
	 *
	 */
	FileBean generateSupplierDetailsReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a dish costing report in PDF format based on the provided order ID and optional language parameters.
	 * 
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType (Optional) The language type for localization.
	 * @param langCode (Optional) The language code for localization.
	 * @return	A {@link FileBean} containing the generated dish costing report in PDF format.
	 */
	FileBean generateDishCostingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a total dish costing report in PDF format based on the provided order ID and optional language parameters.
	 * 
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType (Optional) The language type for localization.
	 * @param langCode (Optional) The language code for localization.
	 * @return	A {@link FileBean} containing the generated dish costing report in PDF format.
	 */
	FileBean generateTotalDishCostingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a report for additional costs incurred by a customer for a specific order.
	 *
	 * @param orderId the ID of the order for which the extra cost report is to be generated
	 * @param langType the type of language to be used in the report (e.g., 1 for default, 2 for preferred)
	 * @param langCode the language code representing the report's language (e.g., "en" for English, "fr" for French)
	 * @param request the HTTP request object providing additional context or details for the report generation
	 * @return a {@link FileBean} object representing the generated report, which may include file metadata and content
	 */
	FileBean generateCustomerExtraCostReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a party complain report for a given order ID and language code.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langCode The language code for localization. If provided, the report will be generated in the specified language.
	 * @return A FileBean containing the party complain report, or null if the report is not available.
	 */
	FileBean generatePartyComplainReport(Long orderId, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a Name Plate Report in a FileBean format for a specific order.
	 *
	 * @param orderId The ID of the order for which the report is generated.
	 * @param langType The language type for the report.
	 * @return A FileBean containing the Name Plate Report.
	 */
	FileBean generateCounterNamePlateReport(Long orderId, Integer langType, Byte reportSize, String reportName);

	/**
	 * Generates a two-language counter nameplate report in a specified size.
	 *
	 * @param orderId The ID of the order for which the report is to be generated
	 * @param reportSize The size of the report (e.g., small, medium, or large represented as a byte)
	 * @param defaultLang The default language to be used in the report
	 * @param preferLang The preferred language to be used in the report
	 * @return A {@link FileBean} object representing the generated report, including file metadata and content
	 */
	FileBean generateTwoLanguageCounterNamePlateReport(Long orderId, Byte reportSize, String reportName, String defaultLang, String preferLang);

	/**
	 * Generates a two-language counter nameplate document report in a specified size.
	 *
	 * @param orderId The ID of the order for which the document report is to be generated
	 * @param reportSize The size of the document report (e.g., small, medium, or large represented as a byte)
	 * @param defaultLang The default language to be used in the document
	 * @param preferLang The preferred language to be used in the document
	 * @return A {@link FileBean} object representing the generated document report, including file metadata and content
	 */
	FileBean generateTwoLanguageCounterNamePlateDocReport(Long orderId, Byte reportSize, String defaultLang, String preferLang);

	/**
	 * Generates a counter nameplate document report for a specific language type and size.
	 *
	 * @param orderId The ID of the order for which the document report is to be generated
	 * @param langType The type of language to be used in the document (e.g., 1 for default, 2 for preferred)
	 * @param reportSize The size of the document report (e.g., small, medium, or large represented as a byte)
	 * @return A {@link FileBean} object representing the generated document report, including file metadata and content
	 */
	FileBean generateCounterNamePlateDocReport(Long orderId, Integer langType, Byte reportSize);

	/**
	 * Generates the Address Chitthi report.
	 *
	 * This method retrieves the necessary data based on the provided orderId and godownId values,
	 * prepares the parameters including language settings and company logo, and generates a PDF report
	 * using JasperReports.
	 *
	 * @param orderId The ID of the order for which the report is being generated.
	 * @param godownId An array of IDs representing the godowns involved in the report.
	 * @param langType The type of language to be used in the document (e.g., 1 for default, 2 for preferred)
	 * @param langCode The language code for localization. If provided, the report will be generated in the specified language.
	 * @param request The HttpServletRequest object to fetch company logo and other request-specific data.
	 * @return A {@link FileBean} object representing the generated document report, including file metadata and content
	 */
	FileBean generateAddressChitthiReport(Long orderId, Long[] godownId, Integer langType, String langCode, String reportName, HttpServletRequest request);

}