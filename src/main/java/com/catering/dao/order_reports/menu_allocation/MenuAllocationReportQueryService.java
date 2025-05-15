package com.catering.dao.order_reports.menu_allocation;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.RawMaterialCategoryDirectOrderDto;

/**
 * The `MenuAllocationReportQueryService` interface defines a contract for generating item-wise raw material reports
 * related to menu allocation. Implementing classes are responsible for providing the actual implementation of this service.
 *
 * @author Krushali Talaviya
 * @since 2023-09-11
 */
public interface MenuAllocationReportQueryService {

	/**
	 * Retrieves the list of functions for a specified order.
	 *
	 * @param orderId The unique identifier of the order for which the functions are retrieved.
	 * @return A list of {@link FunctionPerOrderDto} containing the functions for the specified order.
	 */
	List<FunctionPerOrderDto> getFunctionPerOrder(Long orderId);

	/**
	 * Retrieves the list of item categories for a specified order.
	 *
	 * @param orderId The unique identifier of the order for which the item categories are retrieved.
	 * @return A list of {@link CommonDataForDropDownDto} containing the item categories for the specified order.
	 */
	List<CommonDataForDropDownDto> getItemCategoryPerOrder(Long orderId);

	/**
	 * Generate an menu-item-wise raw material report for a specific order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionId (Optional) The ID of the function related to the report.
	 * @param rawMaterialCategoryId (Optional) The ID of the item category related to the report.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @return A FileBean containing the generated raw material report in PDF format.
	 */
	FileBean generateMenuItemWiseRawMaterialReport(Long orderId, Long[] functionId, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a detail raw material report based on the provided parameters.
	 *
	 * @param orderId The ID of the order for which the report is generated.
     * @param isDateTime Flag indicating whether to display date and time (true) or only date (false).
	 * @param orderDate The date and time of the order for which the report is generated.
	 * @param functionId The ID of the function or event related to the report.
	 * @param rawMaterialCategoryId The ID of the item category used to filter the report.
	 * @param langType The language type used for report generation.
	 * @param langCode The language code used for localization (e.g., "en_US").
	 * @param request The HttpServletRequest object.
	 * @return A FileBean containing the generated total raw material report.
	 */
	Object generateDetailRawMaterialReport(Long orderId, Boolean isDateTime, LocalDateTime orderDate, Long[] functionId, Long count, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request, boolean isWithQuantity, Boolean isPopUp);

	/**
	 * Generates a total raw material report based on the provided parameters.
	 *
	 * @param orderId The ID of the order for which the report is generated.
     * @param isDateTime Flag indicating whether to display date and time (true) or only date (false).
	 * @param orderDate The date and time of the order for which the report is generated.
	 * @param functionId The ID of the function or event related to the report.
	 * @param rawMaterialCategoryId The ID of the item category used to filter the report.
	 * @param langType The language type used for report generation.
	 * @param langCode The language code used for localization (e.g., "en_US").
	 * @param request The HttpServletRequest object.
	 * @return A FileBean containing the generated raw material list report.
	 */
	Object generateTotalRawMaterialReport(Long orderId, Boolean isDateTime, LocalDateTime orderDate, Long[] functionId, Long count, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request, String jasperReportNameConstant, boolean isWithQuantity, Boolean isPopUp);

	/**
	 * Generates a menu report with quantity information based on the given order ID, language type,
	 * and language code for localization.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType The language type (e.g., 1 for English, 2 for Spanish) used for localization.
	 * @param langCode The language code (e.g., "en" for English, "es" for Spanish) used for localization.
	 * @param currentDate The current date for which the report is generated.
	 * @return A FileBean containing the generated menu report with quantity information.
	 */
	FileBean generateMenuWithAndWithOutQuantityReport(Long orderId, Long[] functionId, Integer langType, String langCode, String reportName, HttpServletRequest request, String jasperReportNameConstant);

	/**
	 * Generates a chef labour wise raw material report for a specified order and other parameters.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
     * @param isDateTime Flag indicating whether to display date and time (true) or only date (false).
	 * @param orderDate The date and time of the order for which the report is generated.
	 * @param orderTypeId An array of order type IDs to filter the report.
	 * @param contactId An array of contact IDs to filter the report.
	 * @param functionId An array of function IDs to filter the report.
	 * @param count The number of items for which the report is generated.
	 * @param rawMaterialCategoryId An array of raw material category IDs to filter the report.
	 * @param menuItemId An array of menu item IDs to filter the report.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param request The HTTP servlet request object containing additional request data.
	 * @return A {@link FileBean} containing the chef labour wise raw material report.
	 */
	Object generateChefLabourWiseRawMaterialReport(Long orderId, Boolean isDateTime, LocalDateTime orderDate, Long[] orderTypeId, Long[] contactId, Long[] functionId, Long count, Long[] rawMaterialCategoryId, Long[] menuItemId, Integer langType, String langCode, String reportName, HttpServletRequest request, Boolean isPopUp);

	/**
	 * Generates a chef labour supplier wise raw material report for a specified order and other parameters.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
     * @param isDateTime Flag indicating whether to display date and time (true) or only date (false).
	 * @param orderDate The date and time of the order for which the report is generated.
	 * @param orderTypeId An array of order type IDs to filter the report.
	 * @param contactId An array of contact IDs to filter the report.
	 * @param functionId An array of function IDs to filter the report.
	 * @param count The number of items for which the report is generated.
	 * @param rawMaterialCategoryId An array of raw material category IDs to filter the report.
	 * @param menuItemId An array of menu item IDs to filter the report.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param request The HTTP servlet request object containing additional request data.
	 * @return A {@link FileBean} containing the chef labour supplier wise raw material report.
	 */
	Object generateChefLabourSupplierWiseRawMaterialReport(Long orderId, Boolean isDateTime, LocalDateTime orderDate, Long[] orderTypeId, Long[] contactId, Long[] functionId, Long count, Long[] rawMaterialCategoryId, Long[] menuItemId, Integer langType, String langCode, String reportName, HttpServletRequest request, Boolean isPopUp);

	/**
	 * Generates a supplier-wise raw material report in PDF format for the specified order,
	 * language type, and language code.
	 *
	 * @param orderId The ID of the order for which the report is generated.
	 * @param langType The language type (e.g., 1 for English, 2 for another language).
	 * @param langCode The language code (e.g., "en_US" for English).
	 * @return A {@link FileBean} representing the generated supplier-wise raw material report in PDF format.
	 */
	FileBean generateSupplierWiseRawMaterialReport(Long orderId, Integer langType, String langCode, String reportName, Long[] contactId, Long[] rawMaterialCategoryId, HttpServletRequest request);

	/**
	 * Generates an order file report for a specified order and other parameters.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param functionId An array of function IDs to filter the report.
	 * @param rawMaterialCategoryId An array of raw material category IDs to filter the report.
	 * @param dataTypeId An array of data type IDs to filter the report.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param request The HTTP servlet request object containing additional request data.
	 * @return A {@link FileBean} containing the order file report.
	 */
	FileBean generateOrderFileReport(Long orderId, Long[] functionId, Long[] rawMaterialCategoryId, Long[] dataTypeId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a raw material A5 report for a specified order and other parameters.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param functionId An array of function IDs to filter the report.
	 * @param rawMaterialCategoryId An array of raw material category IDs to filter the report.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param request The HTTP servlet request object containing additional request data.
	 * @return A {@link FileBean} containing the raw material A5 report.
	 */
	Object generateRawMaterialA5Report(Long orderId, Long[] functionId, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request, Boolean isPopUp);

	/**
	 * Generates a raw material A6 report for a specified order and other parameters.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param functionId An array of function IDs to filter the report.
	 * @param rawMaterialCategoryId An array of raw material category IDs to filter the report.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param request The HTTP servlet request object containing additional request data.
	 * @return A {@link FileBean} containing the raw material A6 report.
	 */
	Object generateRawMaterialA6Report(Long orderId, Long[] functionId, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request, Boolean isPopUp);

	/**
	 * Generates a raw material A4 report for a specified order and other parameters.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param functionId An array of function IDs to filter the report.
	 * @param rawMaterialCategoryId An array of raw material category IDs to filter the report.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param request The HTTP servlet request object containing additional request data.
	 * @return A {@link FileBean} containing the raw material A4 report.
	 */
	Object generateRawMaterialA4Report(Long orderId, Long[] functionId, Long[] rawMaterialCategoryId, Integer langType, String langCode, String reportName, HttpServletRequest request, Boolean isPopUp);

	/**
	 * Retrieves the list of raw material categories for a direct order and specific order ID.
	 *
	 * @param orderId The unique identifier of the order for which the raw material categories are retrieved.
	 * @return A list of {@link RawMaterialCategoryDirectOrderDto} containing the raw material categories for the specified order.
	 */
	List<RawMaterialCategoryDirectOrderDto> findRawMaterialCategoriesByDirectOrderAndOrderId(Long orderId);

}