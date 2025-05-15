package com.catering.dao.all_data_reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;

/**
 * Service interface for generating various types of reports related to menu items, raw materials, 
 * menu item quantities, and custom packages. This interface defines methods to fetch dropdown 
 * data for report filtering and to generate the actual report files in different formats.
 * 
 * The methods in this service interact with the underlying data repositories to retrieve the 
 * necessary data and process it to generate the required reports. The reports are then returned 
 * as `FileBean` objects, which can be used to provide the report file to the user.
 * 
 * Key Features:
 * - Generate reports for menu items, raw materials, and custom packages.
 * - Retrieve dropdown data for report filters, including raw material categories, menu item 
 *   wise raw materials, and quantities.
 * - Support for multiple languages through `langType` and `langCode` parameters.
 */
public interface AllDataReportsNativeQueryService {

	/**
	 * Generates a menu item catalogue report based on the specified language type and language code.
	 *
	 * @param templateId The template id as an integer representing type of theme.
	 * @param langType The language type to filter the report data.
	 * @param langCode The language code to filter the report data.
	 * @param request The HTTP request, used for generating the report.
	 * @return A `FileBean` containing the generated menu item catalogue report file.
	 */
	FileBean generateMenuItemCatalogueReport(Integer templateId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a menu item menu report based on the specified language type and language code.
	 * 
	 * @param langType The language type to filter the report data.
	 * @param langCode The language code to filter the report data.
	 * @return A `FileBean` containing the generated menu item menu report file.
	 */
	FileBean generateMenuItemMenuReport(Integer langType, String langCode, String reportName);

	/**
	 * Retrieves the dropdown data for raw materials in the report.
	 * 
	 * @return A list of `DateWiseReportDropDownCommonDto` containing dropdown data for raw materials.
	 */
	List<DateWiseReportDropDownCommonDto> getRawMaterialReportDropDownData();

	/**
	 * Generates a raw materials report filtered by the raw material category and language type.
	 * 
	 * @param rawMaterialCategoryId The ID of the raw material category.
	 * @param langType The language type to filter the report data.
	 * @param langCode The language code to filter the report data.
	 * @return A `FileBean` containing the generated raw materials report file.
	 */
	FileBean generateRawMaterialsReport(Long rawMaterialCategoryId, Integer langType, String langCode, String reportName);

	/**
	 * Retrieves the dropdown data for menu item-wise raw material reports.
	 * 
	 * @return A list of `DateWiseReportDropDownCommonDto` containing dropdown data for menu item-wise raw materials.
	 */
	List<DateWiseReportDropDownCommonDto> getMenuItemWiseRawMaterialReportDropDownData();

	/**
	 * Retrieves the raw material report data filtered by the raw material ID and language type for menu item-wise raw materials.
	 * 
	 * @param rawMaterialCategoryId The ID of the raw material category.
	 * @param langType The language type to filter the report data.
	 * @param langCode The language code to filter the report data.
	 * @return A `FileBean` containing the generated raw material report file for menu item-wise raw materials.
	 */
	FileBean getMenuItemWiseRawMaterialReport(Long rawMaterialCategoryId, Integer langType, String langCode, String reportName);

	/**
	 * Retrieves the dropdown data for the menu item-wise quantity raw material report.
	 * 
	 * @return A list of `DateWiseReportDropDownCommonDto` containing dropdown data for menu item-wise quantity raw materials.
	 */
	List<DateWiseReportDropDownCommonDto> getMenuItemWiseQuantityRawMaterialReportDropdown();

	/**
	 * Generates the menu item-wise quantity raw material report based on the category ID, data type IDs, and language type.
	 * 
	 * @param menuItemWiseQuantityRawMaterialCategoryId The category ID for menu item-wise quantity raw material.
	 * @param dataTypeId An array of data type IDs to filter the report data.
	 * @param langType The language type to filter the report data.
	 * @param langCode The language code to filter the report data.
	 * @return A `FileBean` containing the generated menu item-wise quantity raw material report file.
	 */
	FileBean generateMenuItemWiseQuantityRawMaterialReport(Long menuItemWiseQuantityRawMaterialCategoryId, Long[] dataTypeId, Integer langType, String langCode, String reportName);

	/**
	 * Retrieves the dropdown data for custom package reports.
	 * 
	 * @return A list of `DateWiseReportDropDownCommonDto` containing dropdown data for custom packages.
	 */
	List<DateWiseReportDropDownCommonDto> getCustomPackageDropDownData();

	/**
	 * Generates a custom package report based on the specified package ID, language type, language code, and request.
	 * 
	 * @param packageId The ID of the custom package.
	 * @param langType The language type to filter the report data.
	 * @param langCode The language code to filter the report data.
	 * @param request The HTTP request, used for generating the report.
	 * @return A `FileBean` containing the generated custom package report file.
	 */
	FileBean generateCustomPackageReport(Long packageId, Integer langType, String langCode, String reportName, HttpServletRequest request);

}