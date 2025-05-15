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
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.date_wise_reports.DateWiseReportsNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.DateWiseRawMaterialReportWithPriceDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.util.RequestResponseUtils;
import com.catering.util.StringToDateUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This controller provides APIs for generating and fetching various date-wise reports
 * and dropdown data used in the reporting system. Each API is protected by user
 * rights and provides access to specific resources or operations.
 *
 * <b>API List:</b>
 *
 * <b>Dropdown Data APIs:</b>
 * 1. {@link #getSupplierCategoryForOutSide()} - Fetch supplier categories for outside contacts.
 * 2. {@link #getSupplierNameForOutSide(Long)} - Fetch supplier names for outside contacts.
 * 3. {@link #getMenuItemCategory()} - Fetch menu item categories for reports.
 * 4. {@link #getMenuItemSubCategory()} - Fetch menu item subcategories for reports.
 * 5. {@link #getKitchenArea()} - Fetch kitchen areas for reports.
 * 6. {@link #getCustomerContacts(Long)} - Fetch customer contact names for reports.
 * 7. {@link #getSupplierCategoryForChefLabour()} - Fetch supplier categories for chef/labour.
 * 8. {@link #getSupplierNameForChefLabour(Long)} - Fetch supplier names for chef/labour.
 * 9. {@link #getSupplierCategoryChefLabour()} - Fetch labour supplier categories.
 * 10. {@link #getSupplierNameForLabour(Long)} - Fetch labour supplier names.
 * 11. {@link #getContactForOrder(Long[])} - Fetch contact names for orders.
 * 12. {@link #getSupplierCategoryDropDownDataOfRawMaterial()} - Fetch supplier categories for raw materials.
 * 13. {@link #getSupplierForRawMaterial(Long)} - Fetch suppliers for raw materials.
 *
 * <b>Report Generation APIs:</b>
 * 1. {@link #dateWiseOutsideOrderReportWithPrice(String, String, Long[], Long[], Long[], Long[], Integer, String, HttpServletRequest)} - Generate outside order report with price.
 * 2. {@link #dateWiseOutsideOrderReportWithoutPrice(String, String, Long[], Long[], Long[], Long[], Integer, String, HttpServletRequest)} - Generate outside order report without price.
 * 3. {@link #dateWiseChefLabourReportWithPrice(String, String, Long[], Long[], Long[], Long[], Integer, String, HttpServletRequest)} - Generate chef/labour report with price.
 * 4. {@link #dateWiseChefLabourReportWithoutPrice(String, String, Long[], Long[], Long[], Long[], Integer, String, HttpServletRequest)} - Generate chef/labour report without price.
 * 5. {@link #dateWiseLabourReportWithPrice(String, String, Long[], Long[], Long[], Integer, String, HttpServletRequest)} - Generate labour report with price.
 * 6. {@link #dateWiseLabourReportWithoutPrice(String, String, Long[], Long[], Long[], Integer, String, HttpServletRequest)} - Generate labour report without price.
 *
 * Note: All APIs are secured with {@code @AuthorizeUserRights} to ensure proper access control.
 */
@RestController
@RequestMapping(value = ApiPathConstant.DATE_WISE_REPORTS)
@Tag(name = SwaggerConstant.DATE_WISE_REPORTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DateWiseReportController {

	/**
	 * Injected service for handling native queries related to Date Wise Reports.
	 */
	DateWiseReportsNativeQueryService dateWiseReportsNativeQueryService;

	/**
	 * Fetches dropdown data for supplier categories marked as "Outside".
	 *
	 * @return Response containing a list of supplier categories for "Outside" suppliers.
	 */
	@GetMapping(value = ApiPathConstant.OUTSIDE_SUPPLIER_CATEGORY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getSupplierCategoryForOutSide() {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getSupplierCategoryDropDownData(Constants.OUTSIDE_CONTACT_CATEGORY));
	}

	/**
	 * Fetches dropdown data for supplier names based on the given supplier category ID.
	 *
	 * @param supplierCategoryId (Optional) ID of the supplier category.
	 * @return Response containing a list of supplier names for the "Outside" category.
	 */
	@GetMapping(value = ApiPathConstant.OUTSIDE_SUPPLIER_NAME)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getSupplierNameForOutSide(@RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId) {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getSupplierNameDropDownData(Constants.OUTSIDE_CONTACT_CATEGORY, supplierCategoryId));
	}

	/**
	 * Retrieves the menu item categories for reports.
	 *
	 * @return Response containing a list of menu item subcategories.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ITEM_CATEGORY_FOR_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getMenuItemCategory() {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getMenuItemCategoryDropDownData());
	}

	/**
	 * Retrieves the menu item subcategories for reports.
	 *
	 * @return Response containing a list of menu item subcategories.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ITEM_SUB_CATEGORY_FOR_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getMenuItemSubCategory() {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getMenuItemSubCategoryDropDownData());
	}

	/**
	 * Retrieves the kitchen areas for reports.
	 *
	 * @return Response containing a list of kitchen areas.
	 */
	@GetMapping(value = ApiPathConstant.KITCHEN_AREA_FOR_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getKitchenArea() {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getKitchenAreaDropDownData());
	}

	/**
	 * Retrieves customer contacts for reports based on the given contact ID.
	 *
	 * @param customerContactId Optional customer contact ID for filtering results.
	 * @return Response containing a list of customer contacts.
	 */
	@GetMapping(value = ApiPathConstant.CUSTOMER_CONTACTS_FOR_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getCustomerContacts(@RequestParam(name = FieldConstants.CUSTOMER_CONTACT_ID, required = false) Long customerContactId) {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getCustomerContactsDropDownData(customerContactId));
	}

	/**
	 * Generates a date-wise report for outside orders with price details.
	 *
	 * @param startDate         Start date for the report (optional).
	 * @param endDate           End date for the report (optional).
	 * @param supplierCategoryId Array of supplier category IDs for filtering (optional).
	 * @param supplierNameId    Array of supplier name IDs for filtering (optional).
	 * @param categoryId        Array of category IDs for filtering (optional).
	 * @param statusId          Array of status IDs for filtering (optional).
	 * @param langType          Language type for the report (optional).
	 * @param langCode          Language code for the report (optional).
	 * @param request           HTTP servlet request for additional context.
	 * @return Response containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_OUTSIDE_ORDER_REPORT_WITH_PRICE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseOutsideOrderReportWithPrice(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long[] supplierCategoryId, @RequestParam(name = FieldConstants.SUPPLIER_NAME_ID, required = false) Long[] supplierNameId, @RequestParam(name = FieldConstants.CATEGORY_ID, required = false) Long[] categoryId, @RequestParam(name = FieldConstants.STATUS_ID, required = false) Long[] statusId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = dateWiseReportsNativeQueryService.generateDateWiseOutsideOrderReportWithPrice(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, supplierNameId, categoryId, statusId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a report of outside orders without price details for a specific date range and criteria.
	 *
	 * @param startDate       Start date of the report (optional).
	 * @param endDate         End date of the report (optional).
	 * @param supplierCategoryId Array of supplier category IDs (optional).
	 * @param supplierNameId  Array of supplier name IDs (optional).
	 * @param categoryId      Array of category IDs (optional).
	 * @param statusId        Array of status IDs (optional).
	 * @param langType        Language type (optional).
	 * @param langCode        Language code (optional).
	 * @param request         HTTP servlet request for additional context.
	 * @return A response containing the report file in byte array format.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_OUTSIDE_ORDER_REPORT_WITHOUT_PRICE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseOutsideOrderReportWithoutPrice(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long[] supplierCategoryId, @RequestParam(name = FieldConstants.SUPPLIER_NAME_ID, required = false) Long[] supplierNameId, @RequestParam(name = FieldConstants.CATEGORY_ID, required = false) Long[] categoryId, @RequestParam(name = FieldConstants.STATUS_ID, required = false) Long[] statusId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = dateWiseReportsNativeQueryService.generateDateWiseOutsideOrderReportWithoutPrice(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, supplierNameId, categoryId, statusId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves dropdown data for supplier categories specific to chef labour.
	 *
	 * @return A response containing a list of dropdown data.
	 */
	@GetMapping(value = ApiPathConstant.CHEF_LABOUR_SUPPLIER_CATEGORY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getSupplierCategoryForChefLabour() {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getSupplierCategoryDropDownData(Constants.CHEF_LABOUR_CONTACT_CATEGORY));
	}

	/**
	 * Retrieves a list of supplier names for the Chef Labour category based on the specified supplier category ID.
	 *
	 * @param supplierCategoryId Supplier category ID used to filter the supplier names (optional).
	 * @return A response containing a list of dropdown data for supplier names in the Chef Labour category.
	 */
	@GetMapping(value = ApiPathConstant.CHEF_LABOUR_SUPPLIER_NAME)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getSupplierNameForChefLabour(@RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId) {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getSupplierNameDropDownData(Constants.CHEF_LABOUR_CONTACT_CATEGORY, supplierCategoryId));
	}

	/**
	 * Generates a date-wise report of chef labour orders with price details.
	 *
	 * @param startDate       Start date of the report (optional).
	 * @param endDate         End date of the report (optional).
	 * @param supplierCategoryId Array of supplier category IDs (optional).
	 * @param supplierNameId  Array of supplier name IDs (optional).
	 * @param categoryId      Array of category IDs (optional).
	 * @param statusId        Array of status IDs (optional).
	 * @param langType        Language type (optional).
	 * @param langCode        Language code (optional).
	 * @param request         HTTP servlet request for additional context.
	 * @return A response containing the report file in byte array format.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_CHEF_LABOUR_REPORT_WITH_PRICE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseChefLabourReportWithPrice(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long[] supplierCategoryId, @RequestParam(name = FieldConstants.SUPPLIER_NAME_ID, required = false) Long[] supplierNameId, @RequestParam(name = FieldConstants.CATEGORY_ID, required = false) Long[] categoryId, @RequestParam(name = FieldConstants.STATUS_ID, required = false) Long[] statusId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = dateWiseReportsNativeQueryService.generateDateWiseChefLabourReportWithPrice(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, supplierNameId, categoryId, statusId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a date-wise chef labour report without price.
	 * 
	 * This endpoint generates a report based on the given filter parameters such as start and end date,
	 * supplier category, supplier name, category, status, and language type. The report is returned as a byte array.
	 *
	 * @param startDate The start date of the report (optional).
	 * @param endDate The end date of the report (optional).
	 * @param supplierCategoryId An array of supplier category IDs (optional).
	 * @param supplierNameId An array of supplier name IDs (optional).
	 * @param categoryId An array of category IDs (optional).
	 * @param statusId An array of status IDs (optional).
	 * @param langType The language type for the report (optional).
	 * @param langCode The language code for the report (optional).
	 * @param request The HttpServletRequest object to access the HTTP request.
	 * @return A ResponseContainerDto containing the report file as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_CHEF_LABOUR_REPORT_WITHOUT_PRICE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseChefLabourReportWithoutPrice(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long[] supplierCategoryId, @RequestParam(name = FieldConstants.SUPPLIER_NAME_ID, required = false) Long[] supplierNameId, @RequestParam(name = FieldConstants.CATEGORY_ID, required = false) Long[] categoryId, @RequestParam(name = FieldConstants.STATUS_ID, required = false) Long[] statusId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = dateWiseReportsNativeQueryService.generateDateWiseChefLabourReportWithoutPrice(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, supplierNameId, categoryId, statusId , langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Fetches the supplier categories for the chef labour report.
	 * 
	 * This endpoint returns the available supplier categories for the chef labour report.
	 *
	 * @return A ResponseContainerDto containing a list of DateWiseReportDropDownCommonDto for supplier categories.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_SUPPLIER_CATEGORY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getSupplierCategoryChefLabour() {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getSupplierCategoryDropDownData(Constants.LABOUR_CONTACT_CATEGORY));
	}

	/**
	 * Fetches the supplier names for the chef labour report based on the given supplier category.
	 * 
	 * This endpoint returns the available supplier names for the chef labour report filtered by supplier category.
	 *
	 * @param supplierCategoryId The supplier category ID for filtering the supplier names (optional).
	 * @return A ResponseContainerDto containing a list of DateWiseReportDropDownCommonDto for supplier names.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_SUPPLIER_NAME)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getSupplierNameForLabour(@RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId) {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getSupplierNameDropDownData(Constants.LABOUR_CONTACT_CATEGORY, supplierCategoryId));
	}

	/**
	 * Fetches the contact names for orders based on the given status ID.
	 * 
	 * This endpoint returns the available contact names for orders filtered by status ID.
	 *
	 * @param statusId The status ID for filtering the contact names (optional).
	 * @return A ResponseContainerDto containing a list of DateWiseReportDropDownCommonDto for contact names.
	 */
	@GetMapping(value = ApiPathConstant.CONTACT_NAME)
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getContactForOrder(@RequestParam(name = FieldConstants.STATUS_ID, required = false) Long[] statusId) {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getContactNameDropDownData(statusId));
	}

	/**
	 * Generates a date-wise labour report with price.
	 * 
	 * This endpoint generates a report with price based on the given filter parameters such as start and end date,
	 * supplier category, supplier name, status, and language type. The report is returned as a byte array.
	 *
	 * @param startDate The start date of the report (optional).
	 * @param endDate The end date of the report (optional).
	 * @param supplierCategoryId An array of supplier category IDs (optional).
	 * @param supplierNameId An array of supplier name IDs (optional).
	 * @param statusId An array of status IDs (optional).
	 * @param langType The language type for the report (optional).
	 * @param langCode The language code for the report (optional).
	 * @param request The HttpServletRequest object to access the HTTP request.
	 * @return A ResponseContainerDto containing the report file as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_LABOUR_REPORT_WITH_PRICE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseLabourReportWithPrice(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long[] supplierCategoryId, @RequestParam(name = FieldConstants.SUPPLIER_NAME_ID, required = false) Long[] supplierNameId, @RequestParam(name = FieldConstants.STATUS_ID, required = false) Long[] statusId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = dateWiseReportsNativeQueryService.generateDateWiseLabourReportWithPrice(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, supplierNameId, statusId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a date-wise labour report without price.
	 * 
	 * @param startDate The start date of the report period (optional).
	 * @param endDate The end date of the report period (optional).
	 * @param supplierCategoryId Array of supplier category IDs for filtering (optional).
	 * @param supplierNameId Array of supplier name IDs for filtering (optional).
	 * @param statusId Array of status IDs for filtering (optional).
	 * @param langType Language type for the report (optional).
	 * @param langCode Language code for the report (optional).
	 * @param request The HTTP request object.
	 * @return A response container containing the report file as byte array.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_LABOUR_REPORT_WITHOUT_PRICE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseLabourReportWithoutPrice(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long[] supplierCategoryId, @RequestParam(name = FieldConstants.SUPPLIER_NAME_ID, required = false) Long[] supplierNameId, @RequestParam(name = FieldConstants.STATUS_ID, required = false) Long[] statusId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = dateWiseReportsNativeQueryService.generateDateWiseLabourReportWithoutPrice(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), supplierCategoryId, supplierNameId, statusId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves the list of supplier categories for raw materials.
	 * 
	 * @return A response container containing the list of supplier categories for raw materials.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_RAW_MATERIAL_SUPPLIER_CATEGORY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getSupplierCategoryDropDownDataOfRawMaterial() {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getSupplierCategoryDropDownDataOfRawMaterial());
	}

	/**
	 * Retrieves the list of suppliers for raw materials based on the selected supplier category.
	 * 
	 * @param supplierCategoryId The supplier category ID for filtering (optional).
	 * @return A response container containing the list of suppliers for raw materials.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_RAW_MATERIAL_SUPPLIER)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getSupplierForRawMaterial(
			@RequestParam(name = FieldConstants.SUPPLIER_CATEGORY_ID, required = false) Long supplierCategoryId) {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getSupplierForRawMaterial(supplierCategoryId));
	}

	/**
	 * Generates a date-wise menu item report.
	 * 
	 * @param startDate The start date of the report period (optional).
	 * @param endDate The end date of the report period (optional).
	 * @param customerContactId Array of customer contact IDs for filtering (optional).
	 * @param categoryId Array of category IDs for filtering (optional).
	 * @param menuItemSubCategoryId Array of menu item sub-category IDs for filtering (optional).
	 * @param kitchenAreaId Array of kitchen area IDs for filtering (optional).
	 * @param statusId Array of status IDs for filtering (optional).
	 * @param langType Language type for the report (optional).
	 * @param langCode Language code for the report (optional).
	 * @param request The HTTP request object.
	 * @return A response container containing the report file as byte array.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_MENU_ITEM_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseMenuItemReport(@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, @RequestParam(name = FieldConstants.END_DATE, required = false) String endDate, @RequestParam(name = FieldConstants.CUSTOMER_CONTACT_ID, required = false) Long[] customerContactId, @RequestParam(name = FieldConstants.CATEGORY_ID, required = false) Long[] categoryId, @RequestParam(name = FieldConstants.MENU_ITEM_SUB_CATEGORY_ID, required = false) Long[] menuItemSubCategoryId, @RequestParam(name = FieldConstants.KITCHEN_AREA_ID, required = false) Long[] kitchenAreaId, @RequestParam(name = FieldConstants.STATUS_ID, required = false) Long[] statusId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = dateWiseReportsNativeQueryService.generateDateWiseMenuItemReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), customerContactId, categoryId, menuItemSubCategoryId, kitchenAreaId, statusId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a date-wise raw material report with price.
	 * 
	 * @param startDate The start date of the report period (optional).
	 * @param endDate The end date of the report period (optional).
	 * @param supplierCategoryId Array of supplier category IDs for filtering (optional).
	 * @param supplierNameId Array of supplier name IDs for filtering (optional).
	 * @param statusId Array of status IDs for filtering (optional).
	 * @param langType Language type for the report (optional).
	 * @param langCode Language code for the report (optional).
	 * @param currentDate The current date for the report (optional).
	 * @param request The HTTP request object.
	 * @return A response container containing the report file as byte array.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseEventRawMaterialReportWithPrice(DateWiseRawMaterialReportWithPriceDto dateWiseRawMaterialReportWithPriceDto, HttpServletRequest request) {
		FileBean file = dateWiseReportsNativeQueryService.generateDateWiseRawMaterialReportWithPrice(StringToDateUtils.convertStringToDate(dateWiseRawMaterialReportWithPriceDto.getStartDate()), StringToDateUtils.convertStringToDate(dateWiseRawMaterialReportWithPriceDto.getEndDate()), dateWiseRawMaterialReportWithPriceDto.getSupplierCategoryId(), dateWiseRawMaterialReportWithPriceDto.getSupplierNameId(), dateWiseRawMaterialReportWithPriceDto.getRawMaterialCategoryId(), dateWiseRawMaterialReportWithPriceDto.getStatusId(), dateWiseRawMaterialReportWithPriceDto.getLangType(), dateWiseRawMaterialReportWithPriceDto.getLangCode(), dateWiseRawMaterialReportWithPriceDto.getReportName(), request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a date-wise raw material report with price.
	 * 
	 * @param startDate The start date of the report period (optional).
	 * @param endDate The end date of the report period (optional).
	 * @param supplierCategoryId Array of supplier category IDs for filtering (optional).
	 * @param supplierNameId Array of supplier name IDs for filtering (optional).
	 * @param statusId Array of status IDs for filtering (optional).
	 * @param langType Language type for the report (optional).
	 * @param langCode Language code for the report (optional).
	 * @param currentDate The current date for the report (optional).
	 * @param request The HTTP request object.
	 * @return A response container containing the report file as byte array.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_TOTAL_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseTotalRawMaterialReportWithPrice(DateWiseRawMaterialReportWithPriceDto dateWiseRawMaterialReportWithPriceDto, HttpServletRequest request) {
		FileBean file = dateWiseReportsNativeQueryService.generateDateWiseTotalRawMaterialReportWithPrice(StringToDateUtils.convertStringToDate(dateWiseRawMaterialReportWithPriceDto.getStartDate()), StringToDateUtils.convertStringToDate(dateWiseRawMaterialReportWithPriceDto.getEndDate()), dateWiseRawMaterialReportWithPriceDto.getSupplierCategoryId(), dateWiseRawMaterialReportWithPriceDto.getSupplierNameId(), dateWiseRawMaterialReportWithPriceDto.getRawMaterialCategoryId(), dateWiseRawMaterialReportWithPriceDto.getStatusId(), dateWiseRawMaterialReportWithPriceDto.getLangType(), dateWiseRawMaterialReportWithPriceDto.getLangCode(), dateWiseRawMaterialReportWithPriceDto.getReportName(), request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a Date-Wise Order Booking Report based on the provided parameters.
	 * The report is generated by querying the DateWiseReportsNativeQueryService, 
	 * which processes the given date range, status IDs, contact IDs, and language preferences.
	 * 
	 * @param startDate The start date for the report range (in String format, optional).
	 * @param endDate The end date for the report range (in String format, optional).
	 * @param statusId An array of status IDs to filter the report (optional).
	 * @param contactId An array of contact IDs to filter the report (optional).
	 * @param langType The language type for the report (optional).
	 * @param langCode The language code for the report (optional).
	 * @param request The HttpServletRequest object, containing additional request details.
	 * 
	 * @return A ResponseContainerDto containing the generated report as a byte array, or an empty response if no report is generated.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_ORDER_BOOKING_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dateWiseOrderBookingReport(
			@RequestParam(name = FieldConstants.START_DATE, required = false) String startDate, 
			@RequestParam(name = FieldConstants.END_DATE, required = false) String endDate,
			@RequestParam(name = FieldConstants.STATUS_ID, required = false) Long[] statusId,
			@RequestParam(name = FieldConstants.CONTACT_ID, required = false) Long[] contactId, 
			@RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType,
			@RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode,
			@RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName,
			HttpServletRequest request) {
		FileBean file = dateWiseReportsNativeQueryService.generatedateWiseOrderBookingReport(StringToDateUtils.convertStringToDate(startDate), StringToDateUtils.convertStringToDate(endDate), statusId, contactId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves the list of raw materials category.
	 * 
	 * @return A response container containing the list of raw materials category.
	 */
	@GetMapping(value = ApiPathConstant.DATE_WISE_RAW_MATERIAL_CATEGORY)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.DATE_WISE_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getSupplierForRawMaterial() {
		return RequestResponseUtils.generateResponseDto(dateWiseReportsNativeQueryService.getRawMaterialCategory());
	}

}