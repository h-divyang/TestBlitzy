package com.catering.dao.order_reports.labour_and_agency;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.AdminReportOutsideChithhiReportDto;
import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.LabourAndAgencyBookingReportCommonValueDto;
import com.catering.dto.tenant.request.LabourAndAgencyBookingReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyChefLabourChithhiReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyChefLabourReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyLabourChithhiReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyLabourReportDto;

/**
 * DAO interface for executing various native queries related to labour and agency reports.
 * Provides methods to generate and retrieve different types of labour, chef, and agency reports.
 * Extends {@link JpaRepository} for standard CRUD operations.
 */
public interface LabourAndAgencyReportQueryDao extends JpaRepository<LabourAndAgencyReportQuery, Long> {

	/**
	 * Retrieves common booking report values for a specified order and language type.
	 *
	 * @param orderId  the ID of the order for which the report is generated
	 * @param langType the language type identifier
	 * @return a {@link LabourAndAgencyBookingReportCommonValueDto} containing the common booking report data
	 */
	@Query(name = "findBookingReportCommonValue", nativeQuery = true)
	LabourAndAgencyBookingReportCommonValueDto getBookingReportCommonValue(Long orderId, Integer langType);

	/**
	 * Retrieves booking report details for a specified order and language type.
	 *
	 * @param orderId  the unique identifier of the order
	 * @param langType the language type identifier
	 * @return a list of {@link LabourAndAgencyBookingReportDto} containing the booking report data
	 */
	@Query(name = "findBookingReportValue", nativeQuery = true)
	List<LabourAndAgencyBookingReportDto> getBookingReportValue(Long orderId, Integer langType);

	/**
	 * Retrieves a list of chefs or outside labour suppliers for a specific order and order type.
	 *
	 * @param orderId the unique identifier of the order
	 * @param orderType the type of the order
	 * @return a list of {@link CommonDataForDropDownDto} containing supplier details
	 */
	@Query(name = "getChefOrOutsideLabourSuppliersByOrder", nativeQuery = true)
	List<CommonDataForDropDownDto> findChefOrOutsideLabourSuppliersByOrder(Long orderId, Integer orderType);

	/**
	 * Fetches active functions related to chefs or outside labour for a specified order and type.
	 *
	 * @param orderId   the unique identifier of the order
	 * @param orderType the type of the order
	 * @return a list of {@link FunctionPerOrderDto} containing active function details
	 */
	@Query(name = "getActiveFunctionsForOutsideOrChef", nativeQuery = true)
	List<FunctionPerOrderDto> findFunctionForCheforOutside(Long orderId, Integer orderType);

	/**
	 * Fetches active functions for labour based on the specified order.
	 *
	 * @param orderId the unique identifier of the order
	 * @return a list of {@link FunctionPerOrderDto} containing active functions for labour
	 */
	@Query(name = "getActiveFunctionsByOrderForLabour", nativeQuery = true)
	List<FunctionPerOrderDto> findFunctionForLabour(Long orderId);

	/**
	 * Retrieves final materials for chefs or outside labour based on order and order type.
	 *
	 * @param orderId the unique identifier of the order
	 * @param orderType the type of the order
	 * @return a list of {@link CommonDataForDropDownDto} containing the final material details
	 */
	@Query(name = "getChefOrOutsideLabourFinalMaterialsByOrder", nativeQuery = true)
	List<CommonDataForDropDownDto> findChefOrOutsideLabourFinalMaterialByOrder(Long orderId, Integer orderType);

	/**
	 * Retrieves labour supplier categories for a specific order.
	 *
	 * @param orderId the unique identifier of the order
	 * @return a list of {@link CommonDataForDropDownDto} containing supplier categories
	 */
	@Query(name = "getLabourSuppliersByOrder", nativeQuery = true)
	List<CommonDataForDropDownDto> findLabourSupplierCategory(Long orderId);

	/**
	 * Retrieves labour supplier names based on the supplier category for a specified order.
	 *
	 * @param orderId the unique identifier of the order
	 * @param supplierCategoryId the ID of the supplier category
	 * @return a list of {@link CommonDataForDropDownDto} containing supplier names
	 */
	@Query(name = "getLabourSuppliersNameByOrder", nativeQuery = true)
	List<CommonDataForDropDownDto> findLabourSupplierCategoryName(Long orderId, Long supplierCategoryId);

	/**
	 * Generates a chef labour report for a specific order and language type.
	 *
	 * @param orderId - The unique identifier of the order for which the report is generated.
	 * @param langType - The language type identifier to specify the language type of the report.
	 * @return A list of MenuExecutionChefLabourReportDto objects containing the chef labour report data.
	 */
	@Query(name = "generateChefLabourReport", nativeQuery = true)
	List<LabourAndAgencyChefLabourReportDto> generateChefLabourReport(Long orderId, Integer langType, Integer orderType, List<Long> contactIds, List<Long> functionIds, List<Long> menuItemIds);

	/**
	 * Executes a native SQL query to generate a Chef Labour Chithhi Report for a specific order.
	 *
	 * @param orderId The ID of the order for which the report is generated.
	 * @param langType The language type for the report.
	 * @param orderType The type of the order.
	 * @return A list of MenuExecutionChefLabourChithhiReportDto objects representing the report data.
	 */
	@Query(name = "generateChefLabourChithhiReport", nativeQuery = true)
	List<LabourAndAgencyChefLabourChithhiReportDto> generateChefLabourChithhiReport(Long orderId, Integer langType, Integer orderType, String morning, String noon, String evening, String night, List<Long> contactIds, List<Long> functionIds, List<Long> menuItemIds, String timeZone);

	/**
	 * Executes a native SQL query to generate a Chef Labour Chithhi Report for a specific order.
	 *
	 * @param orderId The ID of the order for which the report is generated.
	 * @param langType The language type for the report.
	 * @param orderType The type of the order.
	 * @return A list of MenuExecutionChefLabourChithhiReportDto objects representing the report data.
	 */
	@Query(name = "generateOutsideChithhiReportReport", nativeQuery = true)
	List<AdminReportOutsideChithhiReportDto> generateOutsideChithhiReport(Long orderId, Integer langType, Integer orderType, String morning, String noon, String evening, String night, List<Long> contactIds, List<Long> functionIds, List<Long> menuItemIds, String timeZone);

	/**
	 * Generates a list of labour reports for a specified event agency distribution order.
	 *
	 * @param orderId The unique identifier of the event agency distribution order for which reports are generated.
	 * @param langType (Optional) The language type for the report. If provided, the reports will be generated in the specified language.
	 * @return A list of EventAgencyDistributionLabourReportDto containing labour reports for the specified order.
	 */
	@Query(name = "generateLabourReport", nativeQuery = true)
	List<LabourAndAgencyLabourReportDto> generateLabourReport(Long orderId, Integer langType, List<Long> supplierCategoryIds, List<Long> supplierIds, List<Long> functionIds);

	/**
	 * Retrieves a list of Labour Chithhi Reports for a specific order.
	 *
	 * This query method executes a native SQL query named "generateLabourChithhiReport" to fetch a list of Labour Chithhi Reports for the given order, considering various parameters including language type, loading, unloading, and time slots (morning, evening, night).
	 *
	 * @param orderId The unique identifier of the order for which the reports are generated.
	 * @param langType (Optional) The language type for the reports, if specified.
	 * @param loading The loading information for the reports.
	 * @param unloading The unloading information for the reports.
	 * @param morning (Optional) The morning time slot information for the reports, if specified.
	 * @param evening (Optional) The evening time slot information for the reports, if specified.
	 * @param night (Optional) The night time slot information for the reports, if specified.
	 * @return A list of Labour Chithhi Reports or an empty list if no reports are found.
	 */
	@Query(name = "generateLabourChithhiReport", nativeQuery = true)
	List<LabourAndAgencyLabourChithhiReportDto> generateLabourChithhiReport(Long orderId, Integer langType, List<Long> supplierCategoryIds, List<Long> supplierIds, List<Long> functionIds);

}
