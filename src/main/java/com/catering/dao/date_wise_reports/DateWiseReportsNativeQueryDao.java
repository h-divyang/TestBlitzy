package com.catering.dao.date_wise_reports;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.DateWiseChefLabourReportDto;
import com.catering.dto.tenant.request.DateWiseLabourReoportDto;
import com.catering.dto.tenant.request.DateWiseMenuItemReportDto;
import com.catering.dto.tenant.request.DateWiseOrderBookingReportDto;
import com.catering.dto.tenant.request.DateWiseOutsideOrderReportDto;
import com.catering.dto.tenant.request.DateWiseRawMaterialReportDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;

/**
 * DAO interface for executing native queries related to date-wise reports.
 * 
 * This interface extends {@link JpaRepository} to provide CRUD operations
 * and custom native query methods for handling various dropdown data and generating reports.
 * 
 * Each method represents a specific native query mapped to named queries defined in the database.
 * 
 * @see JpaRepository
 */
public interface DateWiseReportsNativeQueryDao extends JpaRepository<DateWiseReportsNativeQuery, Long> {

	/**
	 * Retrieves supplier category dropdown data based on the contact category type ID.
	 *
	 * @param contactCategoryTypeId the ID of the contact category type
	 * @return a list of {@link DateWiseReportDropDownCommonDto} containing dropdown data
	 */
	@Query(name = "getSupplierCategoryDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getSupplierCategoryDropDownData(Long contactCategoryTypeId);

	/**
	 * Retrieves supplier name dropdown data based on the contact category type ID and supplier category ID.
	 *
	 * @param contactCategoryTypeId the ID of the contact category type
	 * @param supplierCategoryId the ID of the supplier category
	 * @return a list of {@link DateWiseReportDropDownCommonDto} containing dropdown data
	 */
	@Query(name = "getSupplierNameDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getSupplierNameDropDownData(Long contactCategoryTypeId, Long supplierCategoryId);

	/**
	 * Fetches a list of menu item categories for a dropdown menu.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing menu item categories.
	 */
	@Query(name = "getMenuItemCategoryDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getMenuItemCategoryDropDownData();

	/**
	 * Fetches a list of supplier categories for raw materials for a dropdown menu.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing supplier categories.
	 */
	@Query(name = "getSupplierCategoryDropDownDataOfRawMaterial", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getSupplierCategoryDropDownDataOfRawMaterial(); 

	/**
	 * Fetches a list of suppliers for raw materials for a dropdown menu based on contact category type.
	 *
	 * @param contactCategoryTypeId the ID of the contact category type.
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing suppliers.
	 */
	@Query(name = "getSupplierDropDownDataOfRawMaterial", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getSupplierDropDownDataOfRawMaterial(Long contactCategoryTypeId); 

	/**
	 * Fetches a list of contact names for a dropdown menu based on status IDs.
	 *
	 * @param statusIds a list of status IDs to filter the contacts.
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing contact names.
	 */
	@Query(name = "getContactNameDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getContactNameDropDownData(List<Long> statusIds); 

	/**
	 * Fetches a list of menu item subcategories for a dropdown menu.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing menu item subcategories.
	 */
	@Query(name = "getMenuItemSubCategoryDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getMenuItemSubCategoryDropDownData();

	/**
	 * Fetches a list of kitchen areas for a dropdown menu.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing kitchen areas.
	 */
	@Query(name = "getKitchenAreaDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getKitchenAreaDropDownData();

	/**
	 * Fetches a list of customer contacts for a dropdown menu based on customer contact ID.
	 *
	 * @param customerContactId the ID of the customer contact.
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing customer contacts.
	 */
	@Query(name = "getCustomerContactsDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getCustomerContactsDropDownData(Long customerContactId);

	/**
	 * Generates a report of outside orders based on various filters.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryIds a list of supplier category IDs.
	 * @param supplierNameIds  a list of supplier name IDs.
	 * @param categoryIds      a list of category IDs.
	 * @param statusIds        a list of status IDs.
	 * @param langType         the language type for the report.
	 * @return a list of {@link DateWiseOutsideOrderReportDto} representing the report data.
	 */
	@Query(name = "generateDateWiseOutsideOrderReport", nativeQuery = true)
	List<DateWiseOutsideOrderReportDto> generateDateWiseOutsideOrderReport(LocalDate startDate, LocalDate endDate, List<Long> supplierCategoryIds, List<Long> supplierNameIds, List<Long> categoryIds, List<Long> statusIds, String morning, String noon, String evening, String night, String timeZone, Integer langType);

	/**
	 * Generates a raw material report with pricing details based on various filters.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryIds a list of supplier category IDs.
	 * @param supplierNameIds  a list of supplier name IDs.
	 * @param statusIds        a list of status IDs.
	 * @param morning          the morning time slot filter.
	 * @param noon             the noon time slot filter.
	 * @param evening          the evening time slot filter.
	 * @param night            the night time slot filter.
	 * @param langType         the language type for the report.
	 * @param isAdjustQuantity whether to adjust the quantity in the report.
	 * @param timeZone         the time zone for the report.
	 * @return a list of {@link DateWiseRawMaterialReportDto} representing the report data.
	 */
	@Query(name = "generateDateWiseRawMaterialReportWithPrice", nativeQuery = true)
	List<DateWiseRawMaterialReportDto> generateDateWiseRawMaterialReportWithPrice(LocalDate startDate, LocalDate endDate, List<Long> supplierCategoryIds, List<Long> supplierNameIds, List<Long> rawMaterialCategoryId, List<Long> statusIds, String morning, String noon, String evening, String night, Integer langType, String timeZone);

	/**
	 * Generates a total raw material report with pricing details based on various filters.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryIds a list of supplier category IDs.
	 * @param supplierNameIds  a list of supplier name IDs.
	 * @param statusIds        a list of status IDs.
	 * @param morning          the morning time slot filter.
	 * @param noon             the noon time slot filter.
	 * @param evening          the evening time slot filter.
	 * @param night            the night time slot filter.
	 * @param langType         the language type for the report.
	 * @param isAdjustQuantity whether to adjust the quantity in the report.
	 * @param timeZone         the time zone for the report.
	 * @return a list of {@link DateWiseRawMaterialReportDto} representing the report data.
	 */
	@Query(name = "generateDateWiseTotalRawMaterialReportWithPrice", nativeQuery = true)
	List<DateWiseRawMaterialReportDto> generateDateWiseTotalRawMaterialReportWithPrice(LocalDate startDate, LocalDate endDate, List<Long> supplierCategoryIds, List<Long> supplierNameIds, List<Long> rawMaterialCategoryId, List<Long> statusIds, String morning, String noon, String evening, String night, Integer langType, String timeZone);


	/**
	 * Generates a chef labour report based on various filters.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryIds a list of supplier category IDs.
	 * @param supplierNameIds  a list of supplier name IDs.
	 * @param categoryIds      a list of category IDs.
	 * @param statusIds        a list of status IDs.
	 * @param morning          the morning time slot filter.
	 * @param noon             the noon time slot filter.
	 * @param evening          the evening time slot filter.
	 * @param night            the night time slot filter.
	 * @param timeZone         the time zone for the report.
	 * @param langType         the language type for the report.
	 * @return a list of {@link DateWiseChefLabourReportDto} representing the report data.
	 */
	@Query(name = "generateDateWiseChefLabourReport", nativeQuery = true)
	List<DateWiseChefLabourReportDto> generateDateWiseChefLabourReport(LocalDate startDate, LocalDate endDate, List<Long> supplierCategoryIds, List<Long> supplierNameIds, List<Long> categoryIds, List<Long> statusIds, String morning, String noon, String evening, String night, String timeZone, Integer langType);

	/**
	 * Generates a labour report based on various filters.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param supplierCategoryIds a list of supplier category IDs.
	 * @param supplierNameIds  a list of supplier name IDs.
	 * @param statusIds        a list of status IDs.
	 * @param langType         the language type for the report.
	 * @return a list of {@link DateWiseLabourReoportDto} representing the report data.
	 */
	@Query(name = "generateDateWiseLabourReport", nativeQuery = true)
	List<DateWiseLabourReoportDto> generateDateWiseLabourReport(LocalDate startDate, LocalDate endDate, List<Long> supplierCategoryIds, List<Long> supplierNameIds, List<Long> statusIds, Integer langType);

	/**
	 * Generates an order booking report based on various filters.
	 *
	 * @param startDate        the start date for the report.
	 * @param endDate          the end date for the report.
	 * @param statusIds        a list of status IDs.
	 * @param contactIds       a list of contact IDs.
	 * @param langType         the language type for the report.
	 * @param timeZone         the time zone for the report.
	 * @return a list of {@link DateWiseOrderBookingReportDto} representing the report data.
	 */
	@Query(name = "generateDateWiseOrderBookingReport", nativeQuery = true)
	List<DateWiseOrderBookingReportDto> generateDateWiseOrderBookingReport(LocalDate startDate, LocalDate endDate, List<Long> statusIds, List<Long> contactIds, Integer langType, String timeZone);

	/**
	 * Generates a menu item report based on various filters.
	 *
	 * @param startDate          the start date for the report.
	 * @param endDate            the end date for the report.
	 * @param customerContactIds a list of customer contact IDs.
	 * @param menuItemCategoryIds a list of menu item category IDs.
	 * @param menuItemSubCategoryIds a list of menu item subcategory IDs.
	 * @param kitchenAreaIds     a list of kitchen area IDs.
	 * @param statusIds          a list of status IDs.
	 * @param langType           the language type for the report.
	 * @return a list of {@link DateWiseMenuItemReportDto} representing the report data.
	 */
	@Query(name = "generateDateWiseMenuItemReport", nativeQuery = true)
	List<DateWiseMenuItemReportDto> generateDateWiseMenuItemReport(LocalDate startDate, LocalDate endDate, List<Long> customerContactIds, List<Long> menuItemCategoryIds, List<Long> menuItemSubCategoryIds, List<Long> kitchenAreaIds, List<Long> statusIds, String morning, String noon, String evening, String night, String timeZone, Integer langType);

	/**
	 * Fetches a list of raw material categories for a dropdown.
	 *
	 * @return a list of {@link DateWiseReportDropDownCommonDto} representing raw material categories.
	 */
	@Query(name = "getRawMaterialCategoryDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getRawMaterialCategoryDropDownData();

}