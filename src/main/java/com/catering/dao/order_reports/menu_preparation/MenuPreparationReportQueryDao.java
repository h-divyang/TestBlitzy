package com.catering.dao.order_reports.menu_preparation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CommanDataReportWithOutVenue;
import com.catering.dto.tenant.request.CommonDataForTheReportDto;
import com.catering.dto.tenant.request.EventDistributionNotesDto;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.MenuPreparationCustomMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationExclusiveMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationManagerMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationPremiumImageMenuDto;
import com.catering.dto.tenant.request.MenuPreparationPremiumImageMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationSimpleMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationSloganMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationTheManagerReportDto;
import com.catering.dto.tenant.request.MenuPreparationWithImageMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationWithPremiumImageMenuReportDto;
import com.catering.dto.tenant.request.SimpleMenuSubReportDto;
import com.catering.dto.tenant.request.TableMenuReportDto;
import com.catering.dto.tenant.request.TwoLanguageMenuReportDto;

/**
 * DAO interface for interacting with the database to generate menu preparation reports and related data.
 * This interface contains several native SQL queries for generating custom menu reports, exclusive reports, and other event-related data.
 */
public interface MenuPreparationReportQueryDao extends JpaRepository<MenuPreparationReportQuery, Long> {

	/**
	 * Generates a custom menu report for a specified order and functions.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds The list of function IDs associated with the order.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param morning The morning time slot for the report.
	 * @param noon The noon time slot for the report.
	 * @param evening The evening time slot for the report.
	 * @param night The night time slot for the report.
	 * @param timeZone The time zone for the report.
	 * @return A list of MenuPreparationCustomMenuReportDto objects containing the report data.
	 */
	@Query(name = "generateCustomMenuReport", nativeQuery = true)
	List<MenuPreparationCustomMenuReportDto> generateCustomMenuReport(Long orderId, List<Long> functionIds, Integer langType, String langCode, String morning, String noon, String evening, String night, String timeZone);

	/**
	 * Generates a custom menu report specific to Sathiya Caterers.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds The list of function IDs associated with the order.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @return A list of MenuPreparationCustomMenuReportDto objects containing the report data.
	 */
	@Query(name = "generateSathiyaCaterersCustomMenuReport", nativeQuery = true)
	List<MenuPreparationCustomMenuReportDto> generateSathiyaCaterersCustomMenuReport(Long orderId, List<Long> functionIds, Integer langType, String langCode);

	/**
	 * Generates a custom menu report specific to Shree Raj Catering.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds The list of function IDs associated with the order.
	 * @param langType The language type for the report.
	 * @param langCode The language code for the report.
	 * @param morning The morning time slot for the report.
	 * @param noon The noon time slot for the report.
	 * @param evening The evening time slot for the report.
	 * @param night The night time slot for the report.
	 * @param timeZone The time zone for the report.
	 * @return A list of MenuPreparationCustomMenuReportDto objects containing the report data.
	 */
	@Query(name = "generateShreeRajCateringCustomMenuReport", nativeQuery = true)
	List<MenuPreparationCustomMenuReportDto> generateShreeRajCateringCustomMenuReport(Long orderId, List<Long> functionIds, Integer langType, String langCode, String morning, String noon, String evening, String night, String timeZone);

	/**
	 * Generates a simple menu sub-report for a specified order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds The list of function IDs associated with the order.
	 * @param langType The language type for the report.
	 * @return A list of SimpleMenuSubReportDto objects containing the report data.
	 */
	@Query(name = "generateSimpleMenuSubReport", nativeQuery = true)
	List<SimpleMenuSubReportDto> generateSimpleMenuSubReportDto(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Retrieves common data for the report based on date.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @return A CommonDataForTheReportDto object containing the report data.
	 */
	@Query(name = "getCommonDataDateWise", nativeQuery = true)
	CommonDataForTheReportDto getCommonDataForReport(Long orderId, Integer langType);

	/**
	 * Retrieves common data for the report without venue information.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @return A CommanDataReportWithOutVenue object containing the report data.
	 */
	@Query(name = "getCommonDataForReportWithOutVenue", nativeQuery = true)
	CommanDataReportWithOutVenue getCommonDataForReportWithOutVenue(Long orderId, Integer langType);

	/**
	 * Generates an exclusive menu report for a specified order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds The list of function IDs associated with the order.
	 * @param langType The language type for the report.
	 * @return A list of MenuPreparationExclusiveMenuReportDto objects containing the report data.
	 */
	@Query(name = "generateExclusiveMenuReport", nativeQuery = true)
	List<MenuPreparationExclusiveMenuReportDto> generateExclusiveMenuReport(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Retrieves a list of manager's working report data for a specified order and language type using a native SQL query.
	 *
	 * @param orderId   The unique identifier of the order for which the report data is retrieved.
	 * @param langType  The language type identifier used to filter the report data.
	 * @return A list of {@link MenuPreparationManagerMenuReportDto} objects containing the manager's working report data.
	 */
	@Query(name = "generateManagerWorkingReport", nativeQuery = true)
	List<MenuPreparationManagerMenuReportDto> generateManagerMenuReport(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Retrieves a list of menu report with instruction data for a specified order and language type using a native SQL query.
	 *
	 * @param orderId The unique identifier of the order for which the report data is retrieved.
	 * @param langType The language type identifier used to filter the report data.
	 * @return A list of {@link MenuPreparationManagerMenuReportDto} objects containing the menu report with instruction data.
	 */
	@Query(name = "generateInstructionMenuReport", nativeQuery = true)
	List<MenuPreparationManagerMenuReportDto> generateInstructionMenuReport(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Retrieves common data for the Manager Working Report based on the specified order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @return A MenuPreparationTheManagerReportDto object containing the common data for the report.
	 */
	@Query(name = "generateCommonDataForTheManagerWorkingReport", nativeQuery = true)
	MenuPreparationTheManagerReportDto getCommonDataForManagerWorkingReport(Long orderId, Integer langType);

	/**
	 * Executes a native SQL query to generate a menu preparation report with images, slogans, or both.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @return A list of `MenuPreparationSloganMenuReportDto` objects representing the generated report data.
	 */
	@Query(name = "generateMenuPreparationSloganMenuReport", nativeQuery = true)
	List<MenuPreparationSloganMenuReportDto> generateSloganMenuReport(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Generates a report for image and slogan-based menus for a given order and functions.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds A list of function IDs to filter the report.
	 * @param langType The language type for the report.
	 * @return A list of MenuPreparationPremiumImageMenuDto objects representing the report data.
	 */
	@Query(name = "generateMenuPreparationImageAndSloganMenuReport", nativeQuery = true)
	List<MenuPreparationPremiumImageMenuDto> generateImageAndSloganMenuReport(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Retrieves the common data for the Premium Image Menu Report based on the specified order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @return A MenuPreparationWithPremiumImageMenuReportDto object containing the common data for the report.
	 */
	@Query(name = "generateCommonDataForThePremiumImageReport", nativeQuery = true)
	MenuPreparationWithPremiumImageMenuReportDto getCommonDataForThePremiumImageReport(Long orderId, Integer langType);

	/**
	 * Generates a menu report with premium images for a given order and functions.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds A list of function IDs to filter the report.
	 * @param langType The language type for the report.
	 * @return A list of MenuPreparationPremiumImageMenuReportDto objects representing the menu report.
	 */
	@Query(name = "generateMenuWithPremiumImageReport", nativeQuery = true)
	List<MenuPreparationPremiumImageMenuReportDto> generateMenuWithPremiumImageReport(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Retrieves the common data for the Image Menu Report based on the specified order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param langType The language type for the report.
	 * @return A MenuPreparationWithImageMenuReportDto object containing the common data for the report.
	 */
	@Query(name = "generateCommonDataForTheMenuWithImageMenuReport", nativeQuery = true)
	MenuPreparationWithImageMenuReportDto getCommonDataForTheImageMenuReport(Long orderId, Integer langType);

	/**
	 * Generates a simple menu report for a given order and functions.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds A list of function IDs to filter the report.
	 * @param langType The language type for the report.
	 * @return A list of MenuPreparationSimpleMenuReportDto objects representing the simple menu report.
	 */
	@Query(name = "generateSimpleMenuReport", nativeQuery = true)
	List<MenuPreparationSimpleMenuReportDto> generateSimpleMenuReport(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Generates a menu category report for a given order and functions, including images.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds A list of function IDs to filter the report.
	 * @param langType The language type for the report.
	 * @return A list of MenuPreparationSimpleMenuReportDto objects representing the image menu category report.
	 */
	@Query(name = "generateImageMenuCategoryReport", nativeQuery = true)
	List<MenuPreparationSimpleMenuReportDto> generateImageMenuCategoryReport(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Generates a simple menu report for Raja Caterers based on the order and functions.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds A list of function IDs to filter the report.
	 * @param langType The language type for the report.
	 * @return A list of MenuPreparationSimpleMenuReportDto objects representing the Raja Caterers' menu report.
	 */
	@Query(name = "generateSimpleMenuRajaCaterersReport", nativeQuery = true)
	List<MenuPreparationSimpleMenuReportDto> generateSimpleMenuRajaCaterersReport(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Generates a two-language menu report for a given order and functions.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds A list of function IDs to filter the report.
	 * @param langType The language type for the report.
	 * @param defaultLang The default language for the report.
	 * @param preferLang The preferred language for the report.
	 * @return A list of TwoLanguageMenuReportDto objects representing the two-language menu report.
	 */
	@Query(name = "generateTwoLanguageMenuReport", nativeQuery = true)
	List<TwoLanguageMenuReportDto> generateTwoLanguageMenuReport(Long orderId, List<Long> functionIds, Integer langType, String defaultLang, String preferLang);

	/**
	 * Generates a table menu report for a given order and functions.
	 *
	 * @param orderId The unique identifier of the order.
	 * @param functionIds A list of function IDs to filter the report.
	 * @param langType The language type for the report.
	 * @return A list of TableMenuReportDto objects representing the table menu report.
	 */
	@Query(name = "generateTableMenuReport", nativeQuery = true)
	List<TableMenuReportDto> generateTableMenuReport(Long orderId, List<Long> functionIds, Integer langType);

	/**
	 * Retrieves a list of functions for a given order related to menu preparation.
	 *
	 * @param orderId The unique identifier of the order.
	 * @return A list of FunctionPerOrderDto objects representing the functions for the order.
	 */
	@Query(name = "getFunctionPerOrderForMenuPreparation", nativeQuery = true)
	List<FunctionPerOrderDto> getFunctionsPerOrderForMenuPreparation(Long orderId);

	/**
	 * Retrieves the header notes for the table menu report for a given order.
	 *
	 * @param langType The language type for the notes.
	 * @param orderId The unique identifier of the order.
	 * @return An EventDistributionNotesDto object containing the header notes.
	 */
	@Query(name = "findTableMenuReportHeaderNotes", nativeQuery = true)
	EventDistributionNotesDto findTableMenuReportHeaderNotes(Integer langType, Long orderId);

	/**
	 * Retrieves the footer notes for the table menu report for a given order.
	 *
	 * @param langType The language type for the notes.
	 * @param orderId The unique identifier of the order.
	 * @return An EventDistributionNotesDto object containing the footer notes.
	 */
	@Query(name = "findTableMenuReportFooterNotes", nativeQuery = true)
	EventDistributionNotesDto findTableMenuReportFooterNotes(Integer langType, Long orderId);

}