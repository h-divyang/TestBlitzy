package com.catering.dao.order_reports.order_general_fix_and_crockery_allocation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CrockeryWithQuantityReportDto;
import com.catering.dto.tenant.request.CrockeryWithoutQuantityReportDto;
import com.catering.dto.tenant.request.EventDistributionNotesDto;
import com.catering.dto.tenant.request.EventMenuReportWithCrockeryReportWithQuantityDto;
import com.catering.dto.tenant.request.OrderGeneralFixReportWithQuantityDto;
import com.catering.dto.tenant.request.OrderGeneralFixReportWithoutQuantityDto;

/**
 * Repository interface for accessing and managing event agency distribution reports in the database.
 * This interface extends JpaRepository for basic CRUD operations and defines custom queries for generating labour reports.
 * 
 * @author Krushali Talaviya
 * @since 2023-10-29
 */
public interface OrderGeneralFixAndCrockeryAllocationReportQueryDao extends JpaRepository<OrderGeneralFixAndCrockeryAllocationReportQuery, Long> {

	/**
	 * Generates a list of crockery reports with quantity for a specified event agency distribution order.
	 * @param orderId   The unique identifier of the order.
	 * @param langType  The language type for localization.
	 * @return A list of crockery reports with quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateCrockeryWithQuantityReport", nativeQuery = true)
	List<CrockeryWithQuantityReportDto> generateCrockeryWithQuantityReport(Long orderId, Integer langType, Boolean isAdjustQuantity, Boolean isMaxPerson);

	/**
	 * Generates a list of crockery reports with quantity for a specified event agency distribution order for all functions.
	 * @param orderId   The unique identifier of the order.
	 * @param langType  The language type for localization.
	 * @return A list of crockery reports with quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateCrockeryWithQuantityWithoutMaxSettingReport", nativeQuery = true)
	List<CrockeryWithQuantityReportDto> generateCrockeryWithQuantityWithoutMaxSettingReport(Long orderId, Integer langType);

	/**
	 * Generates a list of crockery reports without quantity for a specified event agency distribution order.
	 * @param orderId   The unique identifier of the order.
	 * @param langType  The language type for localization.
	 * @return A list of crockery reports without quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateCrockeryReportWithoutQuantity", nativeQuery = true)
	List<CrockeryWithoutQuantityReportDto> generateCrockeryWithoutQuantityReport(Long orderId, Integer langType, Boolean isMaxPerson);

	/**
	 * Generates a list of crockery reports without quantity for a specified event agency distribution order for all functions.
	 * @param orderId   The unique identifier of the order.
	 * @param langType  The language type for localization.
	 * @return A list of crockery reports without quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateCrockeryWithoutQuantityWithoutMaxSettingReport", nativeQuery = true)
	List<CrockeryWithoutQuantityReportDto> generateCrockeryWithoutQuantityWithoutMaxSettingReport(Long orderId, Integer langType);

	/**
	 * Repository method to retrieve a list of notes for event agency distribution.
	 * 
	 * @param langType The language type for localization.
	 * @return A {@link EventDistributionNotesDto} containing values.
	 */
	@Query(name = "findOrderGeneralFixRawMaterialNotesValue", nativeQuery = true)
	EventDistributionNotesDto findOrderGeneralFixRawMaterialNotesValue(Integer langType);

	/**
	 * Repository method to retrieve a list of notes for event agency distribution.
	 * 
	 * @param langType The language type for localization.
	 * @return A {@link EventDistributionNotesDto} containing values.
	 */
	@Query(name = "findCrockeryNotesValue", nativeQuery = true)
	EventDistributionNotesDto findCrockeryNotesValue(Integer langType);

	/**
	 * Retrieves list of the general fix report with quantity for a specific order.
	 * @param orderId	The unique identifier of the order.
	 * @param langType	The language type for the localization.
	 * @return A list of general fix report with quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateOrderGeneralFixReportWithQuantity", nativeQuery = true)
	List<OrderGeneralFixReportWithQuantityDto> generateOrderGeneralFixReportWithQuantity(Long orderId, Integer langType, Boolean isMaxPerson);

	/**
	 * Retrieves list of the general fix report with quantity for all functions.
	 * @param orderId	The unique identifier of the order.
	 * @param langType	The language type for the localization.
	 * @return A list of general fix report with quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateOrderGeneralFixReportWithQuantityWithoutMaxSetting", nativeQuery = true)
	List<OrderGeneralFixReportWithQuantityDto> generateOrderGeneralFixReportWithQuantityWithoutMaxSettingReport(Long orderId, Integer langType, Boolean isMaxPerson);

	/**
	 * Retrieves list of the general fix report without quantity for a specific order.
	 * @param orderId	The unique identifier of the order.
	 * @param langType	The language type for the localization.
	 * @return A list of general fix report with quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateOrderGeneralFixReportWithoutQuantity", nativeQuery = true)
	List<OrderGeneralFixReportWithoutQuantityDto> generateOrderGeneralFixReportWithoutQuantity(Long orderId, Integer langType, Boolean isMaxPerson);

	/**
	 * Retrieves list of the general fix report without quantity for a specific order for all functions.
	 * @param orderId	The unique identifier of the order.
	 * @param langType	The language type for the localization.
	 * @return A list of general fix report with quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateOrderGeneralFixReportWithoutQuantityWithoutMaxSetting", nativeQuery = true)
	List<OrderGeneralFixReportWithoutQuantityDto> generateOrderGeneralFixReportWithoutQuantityWithoutMaxSettingReport(Long orderId, Integer langType, Boolean isMaxPerson);

	/**
	 * Generates a crockery report with quantities based on the given order ID, language type, time periods (morning, noon, evening, night),
	 * and time zone. This report includes crockery details, along with quantities and other order-specific information.
	 * 
	 * @param orderId The ID of the order for which the crockery report is generated.
	 * @param langType The language type for the report (e.g., 1 for English, 2 for Hindi).
	 * @param morning The morning time period for the report.
	 * @param noon The noon time period for the report.
	 * @param evening The evening time period for the report.
	 * @param night The night time period for the report.
	 * @param timeZone The time zone to be used for the report generation.
	 * @return A list of {@link EventMenuReportWithCrockeryReportWithQuantityDto} containing the report data.
	 */
	@Query(name = "generateCrockeryReportWithQty", nativeQuery = true)
	List<EventMenuReportWithCrockeryReportWithQuantityDto> generateCrockeryReportWithQty(Long orderId, Integer langType, String morning, String noon, String evening, String night, String timeZone);

	/**
	 * Generates a crockery report with quantities, but without applying the max setting to the report data. 
	 * Similar to {@link #generateCrockeryReportWithQty(Long, Integer, String, String, String, String, String)},
	 * this method does not take into account any maximum settings that might restrict the report output.
	 * 
	 * @param orderId The ID of the order for which the crockery report is generated.
	 * @param langType The language type for the report.
	 * @param morning The morning time period for the report.
	 * @param noon The noon time period for the report.
	 * @param evening The evening time period for the report.
	 * @param night The night time period for the report.
	 * @param timeZone The time zone for the report generation.
	 * @return A list of {@link EventMenuReportWithCrockeryReportWithQuantityDto} containing the report data.
	 */
	@Query(name = "generateCrockeryReportWithQtyWithoutMaxSetting", nativeQuery = true)
	List<EventMenuReportWithCrockeryReportWithQuantityDto> generateCrockeryReportWithQtyWithoutMaxSetting(Long orderId, Integer langType, String morning, String noon, String evening, String night, String timeZone);

	/**
	 * Generates a list of kitchen crockery reports with quantity for a specified event agency distribution order.
	 * @param orderId   The unique identifier of the order.
	 * @param langType  The language type for localization.
	 * @return A list of kitchen crockery reports with quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateKitchenCrockeryWithQuantityReport", nativeQuery = true)
	List<CrockeryWithQuantityReportDto> generateKitchenCrockeryWithQuantityReport(Long orderId, Integer langType, Boolean isAdjustQuantity, Boolean isMaxPerson);

	/**
	 * Generates a list of kitchen crockery reports with quantity for a specified event agency distribution order for all functions.
	 * @param orderId   The unique identifier of the order.
	 * @param langType  The language type for localization.
	 * @return A list of kitchen crockery reports with quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateKitchenCrockeryWithQuantityWithoutMaxSettingReport", nativeQuery = true)
	List<CrockeryWithQuantityReportDto> generateKitchenCrockeryWithQuantityWithoutMaxSettingReport(Long orderId, Integer langType, Boolean isMaxPerson);

	/**
	 * Generates a list of kitchen crockery reports without quantity for a specified event agency distribution order.
	 * @param orderId   The unique identifier of the order.
	 * @param langType  The language type for localization.
	 * @return A list of kitchen crockery reports without quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateKitchenCrockeryReportWithoutQuantity", nativeQuery = true)
	List<CrockeryWithoutQuantityReportDto> generateKitchenCrockeryWithoutQuantityReport(Long orderId, Integer langType, Boolean isMaxPerson);

	/**
	 * Generates a list of kitchen crockery reports without quantity for a specified event agency distribution order for all functions.
	 * @param orderId   The unique identifier of the order.
	 * @param langType  The language type for localization.
	 * @return A list of kitchen crockery reports without quantity or an empty list if no reports are found.
	 */
	@Query(name = "generateKitchenCrockeryWithoutQuantityWithoutMaxSettingReport", nativeQuery = true)
	List<CrockeryWithoutQuantityReportDto> generateKitchenCrockeryWithoutQuantityWithoutMaxSettingReport(Long orderId, Integer langType, Boolean isMaxPerson);

}