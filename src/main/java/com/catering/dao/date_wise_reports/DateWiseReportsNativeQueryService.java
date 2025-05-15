package com.catering.dao.date_wise_reports;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.catering.bean.FileBean;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;

/**
 * Service interface that provides methods to generate various date-wise reports and fetch dropdown data
 * for filtering reports related to suppliers, menu items, raw materials, labour, and other related entities.
 * 
 * Each method corresponds to a specific report generation or data fetching operation, typically utilizing 
 * SQL queries to retrieve and process data based on the provided filter parameters. The reports generated 
 * by this service can be used for various business analyses such as raw material consumption, labour costs, 
 * menu item sales, outside orders, and more.
 * 
 * Reports are generated for specific date ranges and support multi-filter criteria such as supplier categories, 
 * menu item categories, statuses, and language preferences. The results are returned in the form of a {@link FileBean}, 
 * which contains the generated report file.
 * 
 * The following types of reports are included:
 * - Raw Material Reports
 * - Outside Order Reports (with and without price)
 * - Chef Labour Reports (with and without price)
 * - General Labour Reports (with and without price)
 * - Order Booking Reports
 * - Menu Item Reports
 * 
 * Additionally, methods are provided to fetch dropdown data to populate filters for the reports, such as 
 * supplier categories, menu item categories, contact names, etc.
 * 
 * The service supports localization through the use of language types and language codes.
 */
public interface DateWiseReportsNativeQueryService {

	/**
	 * Fetches supplier category dropdown data based on contact category type ID.
	 *
	 * @param contactCategoryTypeId the ID of the contact category type.
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing supplier categories.
	 */
	List<DateWiseReportDropDownCommonDto> getSupplierCategoryDropDownData(Long contactCategoryTypeId);

	/**
	 * Fetches supplier names for a dropdown based on contact category type and supplier category ID.
	 *
	 * @param contactCategoryTypeId the ID of the contact category type.
	 * @param supplierCategoryId    the ID of the supplier category.
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing supplier names.
	 */
	List<DateWiseReportDropDownCommonDto> getSupplierNameDropDownData(Long contactCategoryTypeId, Long supplierCategoryId);

	/**
	 * Fetches contact names for a dropdown based on status IDs.
	 *
	 * @param statusId an array of status IDs.
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing contact names.
	 */
	List<DateWiseReportDropDownCommonDto> getContactNameDropDownData(Long[] statusId);

	/**
	 * Fetches menu item category dropdown data.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing menu item categories.
	 */
	List<DateWiseReportDropDownCommonDto> getMenuItemCategoryDropDownData();

	/**
	 * Fetches supplier category dropdown data for raw materials.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing supplier categories for raw materials.
	 */
	List<DateWiseReportDropDownCommonDto> getSupplierCategoryDropDownDataOfRawMaterial();

	/**
	 * Fetches supplier data for raw materials based on supplier category ID.
	 *
	 * @param supplierCategoryId the ID of the supplier category.
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing suppliers for raw materials.
	 */
	List<DateWiseReportDropDownCommonDto> getSupplierForRawMaterial(Long supplierCategoryId);

	/**
	 * Fetches menu item subcategory dropdown data.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing menu item subcategories.
	 */
	List<DateWiseReportDropDownCommonDto> getMenuItemSubCategoryDropDownData();

	/**
	 * Fetches kitchen area dropdown data.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing kitchen areas.
	 */
	List<DateWiseReportDropDownCommonDto> getKitchenAreaDropDownData();

	/**
	 * Fetches customer contact dropdown data based on customer contact ID.
	 *
	 * @param customerContactId the ID of the customer contact.
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing customer contacts.
	 */
	List<DateWiseReportDropDownCommonDto> getCustomerContactsDropDownData(Long customerContactId);

	/**
	 * Generates a raw material report with price details.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryId an array of supplier category IDs.
	 * @param supplierNameId   an array of supplier name IDs.
	 * @param statusId         an array of status IDs.
	 * @param langType         the language type for the report.
	 * @param langCode         the language code for the report.
	 * @param request          the HTTP request object.
	 * @return a {@link FileBean} representing the report file.
	 */
	FileBean generateDateWiseRawMaterialReportWithPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] rawMaterialCategoryId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a date wise total raw material report with price details.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryId an array of supplier category IDs.
	 * @param supplierNameId   an array of supplier name IDs.
	 * @param statusId         an array of status IDs.
	 * @param langType         the language type for the report.
	 * @param langCode         the language code for the report.
	 * @param request          the HTTP request object.
	 * @return a {@link FileBean} representing the report file.
	 */
	FileBean generateDateWiseTotalRawMaterialReportWithPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] rawMaterialCategoryId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates an outside order report with price details.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryId an array of supplier category IDs.
	 * @param supplierNameId   an array of supplier name IDs.
	 * @param categoryId       an array of category IDs.
	 * @param statusId         an array of status IDs.
	 * @param langType         the language type for the report.
	 * @param langCode         the language code for the report.
	 * @param request          the HTTP request object.
	 * @return a {@link FileBean} representing the report file.
	 */
	FileBean generateDateWiseOutsideOrderReportWithPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] categoryId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates an outside order report without price details.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryId an array of supplier category IDs.
	 * @param supplierNameId   an array of supplier name IDs.
	 * @param categoryId       an array of category IDs.
	 * @param statusId         an array of status IDs.
	 * @param langType         the language type for the report.
	 * @param langCode         the language code for the report.
	 * @param request          the HTTP request object.
	 * @return a {@link FileBean} representing the report file.
	 */
	FileBean generateDateWiseOutsideOrderReportWithoutPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] categoryId, Long[] statusId,  Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a chef labour report with price details.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryId an array of supplier category IDs.
	 * @param supplierNameId   an array of supplier name IDs.
	 * @param categoryId       an array of category IDs.
	 * @param statusId         an array of status IDs.
	 * @param langType         the language type for the report.
	 * @param langCode         the language code for the report.
	 * @param request          the HTTP request object.
	 * @return a {@link FileBean} representing the report file.
	 */
	FileBean generateDateWiseChefLabourReportWithPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] categoryId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a chef labour report without price details.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryId an array of supplier category IDs.
	 * @param supplierNameId   an array of supplier name IDs.
	 * @param categoryId       an array of category IDs.
	 * @param statusId         an array of status IDs.
	 * @param langType         the language type for the report.
	 * @param langCode         the language code for the report.
	 * @param request          the HTTP request object.
	 * @return a {@link FileBean} representing the report file.
	 */
	FileBean generateDateWiseChefLabourReportWithoutPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] categoryId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a labour report with price details.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryId an array of supplier category IDs.
	 * @param supplierNameId   an array of supplier name IDs.
	 * @param statusId         an array of status IDs.
	 * @param langType         the language type for the report.
	 * @param langCode         the language code for the report.
	 * @param request          the HTTP request object.
	 * @return a {@link FileBean} representing the report file.
	 */
	FileBean generateDateWiseLabourReportWithPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a labour report without price details.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryId an array of supplier category IDs.
	 * @param supplierNameId   an array of supplier name IDs.
	 * @param statusId         an array of status IDs.
	 * @param langType         the language type for the report.
	 * @param langCode         the language code for the report.
	 * @param request          the HTTP request object.
	 * @return a {@link FileBean} representing the report file.
	 */
	FileBean generateDateWiseLabourReportWithoutPrice(LocalDate startDate, LocalDate endDate, Long[] supplierCategoryId, Long[] supplierNameId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a date-wise order booking report based on the provided filters.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param statusId         an array of status IDs.
	 * @param contactId        an array of contact IDs.
	 * @param langType         the language type for the report.
	 * @param langCode         the language code for the report.
	 * @param request          the HTTP request for additional context.
	 * @return a {@link FileBean} containing the generated report.
	 */
	FileBean generatedateWiseOrderBookingReport(LocalDate startDate, LocalDate endDate, Long[] statusId, Long[] contactId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Generates a date-wise menu item report based on the provided filters.
	 *
	 * @param startDate            the start date for the report.
	 * @param endDate              the end date for the report.
	 * @param customerContactId    an array of customer contact IDs.
	 * @param categoryId           an array of category IDs.
	 * @param menuItemSubCategoryId an array of menu item subcategory IDs.
	 * @param kitchenAreaId        an array of kitchen area IDs.
	 * @param statusId             an array of status IDs.
	 * @param langType             the language type for the report.
	 * @param langCode             the language code for the report.
	 * @param request              the HTTP request for additional context.
	 * @return a {@link FileBean} containing the generated report.
	 */
	FileBean generateDateWiseMenuItemReport(LocalDate startDate, LocalDate endDate, Long[] customerContactId, Long[] categoryId, Long[] menuItemSubCategoryId, Long[] kitchenAreaId, Long[] statusId, Integer langType, String langCode, String reportName, HttpServletRequest request);

	/**
	 * Fetches raw materials category.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing raw materials categories.
	 */
	List<DateWiseReportDropDownCommonDto> getRawMaterialCategory();

}