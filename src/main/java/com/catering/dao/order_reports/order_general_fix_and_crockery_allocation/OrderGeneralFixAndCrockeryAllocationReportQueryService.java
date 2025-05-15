package com.catering.dao.order_reports.order_general_fix_and_crockery_allocation;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;

/**
 * Service interface for generating event agency distribution labour reports.
 * This interface defines a method for generating labour reports based on a specified order, language type, and language code.
 * 
 * @author Krushali Talaviya
 * @since 2023-10-29
 */
public interface OrderGeneralFixAndCrockeryAllocationReportQueryService {

	/**
	 * Generates a Crockery Report With Quantity for a specific order.
	 * 
	 * @param orderId	The unique identifier of the order for which the crockery report with quantity is generated.
	 * @param langType	The language type for localization.
	 * @param langCode	The language code for the report's language.
	 * @return
	 */
	FileBean generateCrockeryWithQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a Crockery Report With Quantity for all functions for a specific order.
	 * 
	 * @param orderId	The unique identifier of the order for which the crockery report with quantity is generated.
	 * @param langType	The language type for localization.
	 * @param langCode	The language code for the report's language.
	 * @return
	 */
	FileBean generateCrockeryWithQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a Crockery Report Without Quantity for a specific order.
	 * 
	 * @param orderId	The unique identifier of the order for which the crockery report without is generated.
	 * @param langType	The language type for localization.
	 * @param langCode	The language code for the report's language.
	 * @return
	 */
	FileBean generateCrockeryWithoutQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a Crockery Report Without Quantity for all functions for a specific order.
	 * 
	 * @param orderId	The unique identifier of the order for which the crockery report without is generated.
	 * @param langType	The language type for localization.
	 * @param langCode	The language code for the report's language.
	 * @return
	 */
	FileBean generateCrockeryWithoutQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);
 
	/**
	 * Generates a menu report with crockery for a specific order.
	 *
	 * @param orderId   The unique identifier of the order.
	 * @param langType  The language type for the report.
	 * @param langCode  The language code for localization.
	 * @return A `FileBean` containing the generated menu report with crockery.
	 */
	FileBean generateCrockeryWithMenuReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a menu report for all functions with crockery for a specific order.
	 *
	 * @param orderId   The unique identifier of the order.
	 * @param langType  The language type for the report.
	 * @param langCode  The language code for localization.
	 * @return A `FileBean` containing the generated menu report with crockery.
	 */
	FileBean generateCrockeryWithMenuWithoutMaxSettingReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generate general fix report with quantity
	 * 
	 * @param orderId     The unique identifier of the order for which the general fix report with quantity is generated.
	 * @param langType    The language type for localization.
	 * @param langCode    The language code for the report's language.
	 * @return A `FileBean` containing the generated General Fix Report With Quantity, or null if the report could not be generated.
	 */
	FileBean generateOrderGeneralFixWithQuantityReport(Long orderId, Integer langType, String langCode, String reportName,  HttpServletRequest request);

	/**
	 * Generate general fix report with quantity and without max person setting
	 * 
	 * @param orderId     The unique identifier of the order for which the general fix report with quantity is generated.
	 * @param langType    The language type for localization.
	 * @param langCode    The language code for the report's language.
	 * @return A `FileBean` containing the generated General Fix Report With Quantity, or null if the report could not be generated.
	 */
	FileBean generateOrderGeneralFixWithQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generate general fix report without quantity
	 * 
	 * @param orderId     The unique identifier of the order for which the general fix report without quantity is generated.
	 * @param langType    The language type for localization.
	 * @param langCode    The language code for the report's language.
	 * @return A `FileBean` containing the generated General Fix Report Without Quantity, or null if the report could not be generated.
	 */
	FileBean generateOrderGeneralFixWithoutQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generate general fix report without quantity and without max person setting
	 * 
	 * @param orderId     The unique identifier of the order for which the general fix report without quantity is generated.
	 * @param langType    The language type for localization.
	 * @param langCode    The language code for the report's language.
	 * @return A `FileBean` containing the generated General Fix Report Without Quantity, or null if the report could not be generated.
	 */
	FileBean generateOrderGeneralFixWithoutQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a Kitchen Crockery Report With Quantity for a specific order.
	 * 
	 * @param orderId	The unique identifier of the order for which the kitchen crockery report with quantity is generated.
	 * @param langType	The language type for localization.
	 * @param langCode	The language code for the report's language.
	 * @return
	 */
	FileBean generateKitchenCrockeryWithQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a Kitchen Crockery Report With Quantity for all functions for a specific order.
	 * 
	 * @param orderId	The unique identifier of the order for which the kitchen crockery report with quantity is generated.
	 * @param langType	The language type for localization.
	 * @param langCode	The language code for the report's language.
	 * @return
	 */
	FileBean generateKitchenCrockeryWithQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a Kitchen Crockery Report Without Quantity for a specific order.
	 * 
	 * @param orderId	The unique identifier of the order for which the kitchen crockery report without is generated.
	 * @param langType	The language type for localization.
	 * @param langCode	The language code for the report's language.
	 * @return
	 */
	FileBean generateKitchenCrockeryWithoutQuantityReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a Kitchen Crockery Report Without Quantity for all functions for a specific order.
	 * 
	 * @param orderId	The unique identifier of the order for which the kitchen crockery report without is generated.
	 * @param langType	The language type for localization.
	 * @param langCode	The language code for the report's language.
	 * @return
	 */
	FileBean generateKitchenCrockeryWithoutQuantityWithoutMaxSettingReport(Long orderId, Integer langType, String langCode, String reportName, HttpServletRequest request);

}