package com.catering.dao.order_reports.admin_reports;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dao.order_reports.menu_allocation.MenuAllocationReportQuery;
import com.catering.dto.tenant.request.AddressChitthiReportDto;
import com.catering.dto.tenant.request.AdminFeedBackReportDto;
import com.catering.dto.tenant.request.AdminSupplierDetailsReportDto;
import com.catering.dto.tenant.request.AdminWastageReportDto;
import com.catering.dto.tenant.request.CustomerFormatReportDto;
import com.catering.dto.tenant.request.AdminReportCounterNamePlateReportDto;
import com.catering.dto.tenant.request.AdminReportNamePlateDto;
import com.catering.dto.tenant.request.AdminReportTableMenuDto;
import com.catering.dto.tenant.request.AdminReportTwoLanguageCounterNamePlateReportDto;

/**
 * Data Access Object (DAO) interface for executing admin-related reports using JPA.
 * Extends JpaRepository for basic CRUD operations on MenuExecutionReportQuery entities.
 *
 * <p>
 * This interface provides methods to generate various admin reports, such as wastage reports,
 * feedback reports, and supplier details reports for a specific order and language type.
 * </p>
 *
 * @param <MenuAllocationReportQuery> The type of the entity for which the repository is to be used.
 * @param <Long> The type of the ID of the entity.
 *
 * @see JpaRepository
 * @see AdminWastageReportDto
 * @see AdminFeedBackReportDto
 * @see AdminSupplierDetailsReportDto
 * @see MenuAllocationReportQuery
 * @see Query
 * 
 * @author Krushali Talaviya
 * @since 1 January 2024
 */
public interface AdminReportQueryDao extends JpaRepository<AdminReportQuery, Long> {

	/**
	 * Generates a wastage report for a specific order and language type.
	 *
	 * @param orderId  - The unique identifier of the order for which the report is generated.
	 * @param langType - The language type identifier to specify the language type of the report.
	 * @return A list of AdminWastageReportDto objects containing the wastage report data.
	 */
	@Query(name = "generateWastageReport", nativeQuery = true)
	List<AdminWastageReportDto> generateWastageReport(Long orderId, Integer langType);

	/**
	 * Generates a feedback report for a specific order and language type.
	 *
	 * @param orderId  - The unique identifier of the order for which the report is generated.
	 * @param langType - The language type identifier to specify the language type of the report.
	 * @return A list of AdminFeedBackReportDto objects containing the feedback report data.
	 */
	@Query(name = "generateDishCountingReport", nativeQuery = true)
	List<AdminFeedBackReportDto> generateDishCountingReport(Long orderId, String morning, String noon, String evening, String night, Integer langType, String timeZone);

	/**
	 * Retrieves a list of supplier details reports based on the provided order ID and language type using a native SQL query.
	 *
	 * @param orderId   The unique identifier of the order for which the reports are generated.
	 * @param langType  The language type for localization.
	 * @return A list of {@link AdminSupplierDetailsReportDto} objects representing supplier details reports.
	 *
	 */
	@Query(name = "generateSupplierDetailsReport", nativeQuery = true)
	List<AdminSupplierDetailsReportDto> generateSupplierDetailsReport(Long orderId, Integer langType);

	@Query(name = "generateCustomerExtraCostReport", nativeQuery = true)
	List<CustomerFormatReportDto> generateCustomerExtraCostReport(Long orderId, Integer langType);

	/**
	 * Executes a native SQL query to generate a Name Plate Report for a specific order.
	 *
	 * @param orderId  The ID of the order for which the report is generated.
	 * @param langType The language type for the report.
	 * @return A list of MenuAllocationCounterNamePlateReportDto objects representing the report data.
	 */
	@Query(name = "generateCounterNamePlateReport", nativeQuery = true)
	List<AdminReportCounterNamePlateReportDto> generateCounterNamePlateReport(Long orderId, Integer langType);

	/**
	 * Executes a native SQL query to generate a report for two-language counter name plates.
	 *
	 * @param orderId The ID of the order for which the report is to be generated
	 * @param defaultLang The default language to be used in the report
	 * @param preferLang  The preferred language to be used in the report
	 * @return a list of {@link AdminReportTwoLanguageCounterNamePlateReportDto} objects containing the report data
	 */
	@Query(name = "generateTwoLanguageCounterNamePlateReport", nativeQuery = true)
	List<AdminReportTwoLanguageCounterNamePlateReportDto> generateTwoLanguageCounterNamePlateReport(Long orderId, String defaultLang, String preferLang);

	/**
	 * Executes a native SQL query to retrieve the final materials for generating a Name Plate Report for a specific order.
	 *
	 * @param orderId The ID of the order for which the final materials are retrieved.
	 * @return A list of MenuAllocationNamePlateDto objects representing the final materials.
	 */
	@Query(name = "getNamePlateDetails", nativeQuery = true)
	List<AdminReportNamePlateDto> getMenuItemsForNamePlateReport(Long orderId);

	/**
	 * Executes a native SQL query to retrieve table menu details for a specific order and set of functions.
	 *
	 * @param orderId The ID of the order for which the menu details are to be fetched
	 * @param functionIds A list of function IDs to filter the menu details
	 * @return A list of {@link AdminReportTableMenuDto} objects containing the table menu report details
	 */
	@Query(name = "getTableMenuDetails", nativeQuery = true)
	List<AdminReportTableMenuDto> getTableMenuReportDetails(Long orderId, List<Long> functionIds);

	/**
	 * Generates the Address Chitthi Report based on the provided parameters.
	 * 
	 * @param orderId   The ID of the order for which the report is generated.
	 * @param godownIds The list of godown IDs to filter the report data.
	 * @param langType  The language type identifier for localization.
	 * @return A list of {@link AddressChitthiReportDto} containing the report details.
	 */
	@Query(name = "generateAddressChitthiReport", nativeQuery = true)
	List<AddressChitthiReportDto> generateAddressChitthiReport(Long orderId, List<Long> godownIds, Integer langType);

}