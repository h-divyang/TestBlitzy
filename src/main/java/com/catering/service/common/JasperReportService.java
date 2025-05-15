package com.catering.service.common;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;

import net.sf.jasperreports.engine.JasperReport;

/**
 * Provides services for generating reports using JasperReports templates.
 * This interface encapsulates methods to generate PDF and document reports by populating Jasper templates with provided data and configurations.
 * Additionally, it provides utility methods for configuring report visuals and settings.
 */
public interface JasperReportService {

	/**
	 * Generates a PDF report using the provided data, parameters, and Jasper report template.
	 *
	 * @param <T> The type of the data elements used in the report.
	 * @param data A list of data elements to populate the report.
	 * @param parameters A map containing parameters to be used in the report generation.
	 * @param jasperPath The file path to the Jasper report template.
	 * @return A FileBean object containing the generated PDF report as a byte array and its content type.
	 */
	<T> FileBean generatePdfReport(List<T> data, Map<String, Object> parameters, String jasperPath);

	/**
	 * Generates a PDF report using the provided map of parameters and a Jasper report template path.
	 *
	 * @param parameters A map containing the parameters to be passed to the Jasper report template.
	 * @param jasperPath The file path to the Jasper report template.
	 * @return A FileBean object containing the generated PDF report as a byte array and its content type.
	 */
	FileBean generatePdfReport(Map<String, Object> parameters, String jasperPath);

	/**
	 * Generates a document report using the provided data, parameters, and a Jasper report template.
	 *
	 * @param <T> The type of data elements used in the report.
	 * @param data A list of data elements to populate the report.
	 * @param parameters A map containing parameters to be used in report generation.
	 * @param jasperPath The file path to the Jasper report template.
	 * @return A FileBean object containing the generated document report as a byte array and its content type.
	 */
	<T> FileBean generateDocReport(List<T> data, Map<String, Object> parameters, String jasperPath);

	/**
	 * Retrieves a compiled JasperReport object from the specified file path.
	 *
	 * @param jasperPath The file path to the Jasper report template.
	 * @return A JasperReport object representing the compiled Jasper report template.
	 */
	JasperReport getJasperReport(String jasperPath);

	/**
	 * Sets the company logo in the provided parameters map using the request information.
	 *
	 * @param parameters A map where the company logo information will be added.
	 * @param request The HTTP request object used to retrieve the context or necessary details for setting the logo.
	 * @return A map containing the updated parameters with the company logo information included.
	 */
	Map<String, Object> setCompanyLogo(Map<String, Object> patameters, String reportName, HttpServletRequest request);

	/**
	 * Retrieves the company logo using the provided HTTP request.
	 *
	 * @param request The HTTP request object that contains details necessary for obtaining the company logo.
	 * @return A String representing the company logo path.
	 */
	String getCompanyLogo(HttpServletRequest request);

	/**
	 * Sets the background image in the provided parameters map to be used in report generation.
	 *
	 * @param parameters A map containing key-value pairs where the background image information will be added.
	 * @return A map containing the updated parameters with the background image included.
	 */
	Map<String, Object> setBackgroundImageInReport(Map<String, Object> parameters);

	/**
	 * Adds a background image to the parameters of a general report.
	 *
	 * @param parameters A map containing key-value pairs where the background image information will be added.
	 * @param jasperPath The directory path where the JasperReports templates or related resources are stored.
	 * @param reportName The name of the report, used to determine the specific background image path.
	 * @return A map containing the updated parameters with the background image included.
	 */
	Map<String, Object> setBackgroundImageInGeneralReport(Map<String, Object> parameters, String jasperPath, String reportName);

	/**
	 * Sets a specific exclusive image in the provided parameters map for report generation.
	 *
	 * @param parameters A map containing key-value pairs where the exclusive image information will be added.
	 * @return A map containing the updated parameters with the exclusive image included.
	 */
	Map<String, Object> setExclusiveImageInReport(Map<String, Object> parameters);

	/**
	 * Adds a custom package report back image to the provided parameters map for report generation.
	 *
	 * @param parameters A map containing key-value pairs where the custom package report back image information will be added.
	 * @return A map containing the updated parameters with the custom package report back image included.
	 */
	Map<String, Object> setCustomPackageReportBackImage(Map<String, Object> parameters);

	/**
	 * Configures decimal patterns to be used in reports by modifying the provided parameters map.
	 *
	 * @param parameters A map containing key-value pairs where decimal pattern configurations will be added.
	 * @return A map containing the updated parameters with the decimal patterns included.
	 */
	Map<String, Object> setDecimalPatterns(Map<String, Object> parameters);

	/**
	 * Configures the parameters map to include a "slogan menu design" customization for use in report generation.
	 *
	 * @param parameters A map containing key-value pairs where the slogan menu design information will be added.
	 * @return A map containing the updated parameters with the slogan menu design included.
	 */
	Map<String, Object> setSloganMenuDesignInReport(Map<String, Object> parameters);

	/**
	 * Adds a function icon to the provided parameters map for report generation.
	 *
	 * @param parameters A map containing key-value pairs where the function icon will be added.
	 * @return A map containing the updated parameters with the function icon included.
	 */
	Map<String, Object> setFunctionIconInReport(Map<String, Object> parameters);

}