package com.catering.controller.tenant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catering.annotation.AuthorizeUserRights;
import com.catering.bean.FileBean;
import com.catering.constant.ApiPathConstant;
import com.catering.constant.ApiUserRightsConstants;
import com.catering.constant.Constants;
import com.catering.constant.FieldConstants;
import com.catering.constant.JasperReportNameConstant;
import com.catering.constant.MessagesConstant;
import com.catering.constant.RegexConstant;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.order_reports.admin_reports.AdminReportQueryService;
import com.catering.dao.order_reports.combine_report.CombineReportQueryService;
import com.catering.dao.order_reports.labour_and_agency.LabourAndAgencyReportQueryDao;
import com.catering.dao.order_reports.labour_and_agency.LabourAndAgencyReportQueryService;
import com.catering.dao.order_reports.menu_allocation.MenuAllocationReportQueryDao;
import com.catering.dao.order_reports.menu_allocation.MenuAllocationReportQueryService;
import com.catering.dao.order_reports.menu_preparation.MenuPreparationReportQueryService;
import com.catering.dao.order_reports.order_general_fix_and_crockery_allocation.OrderGeneralFixAndCrockeryAllocationReportQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.ChefOrOutsideLabourReportParams;
import com.catering.dto.tenant.request.CombineReportRequestParmDto;
import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.LabourReportParams;
import com.catering.dto.tenant.request.RawMaterialCategoryDirectOrderDto;
import com.catering.service.tenant.CompanySettingService;
import com.catering.util.RequestResponseUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * The `ReportController` class is responsible for handling HTTP requests related to menu allocation reports.
 * It provides endpoints for retrieving information about item categories and functions per order, as well
 * as generating item-wise raw material reports.
 *
 * @RestController: Indicates that this class is a Spring MVC controller and defines the base request mapping.
 * @RequestMapping: Specifies the base URL path for all the endpoints defined in this controller.
 * @Tag: Provides a Swagger tag for documentation purposes.
 *
 * @author Krushali Talaviya
 * @since 2023-09-11
 */
@RestController
@RequestMapping(value = ApiPathConstant.ORDER_BOOKING_REPORTS)
@Tag(name = SwaggerConstant.ORDER_BOOKING_REPORTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderBookingReportsController {

	/**
	 * Service for handling combine report.
	 * <p>
	 * This service contains business logic for processing combine report
	 * </p>
	 */
	CombineReportQueryService combinedReportQueryService;

	/**
	 * DAO for querying menu allocation reports.
	 * <p>
	 * This DAO provides direct database interactions for fetching and processing data 
	 * related to menu allocations.
	 * </p>
	 */
	MenuAllocationReportQueryDao menuAllocationReportQueryDao;

	/**
	 * Service for handling menu preparation report queries.
	 * <p>
	 * This service contains business logic for retrieving and processing menu preparation 
	 * reports, ensuring accurate and efficient data retrieval.
	 * </p>
	 */
	MenuPreparationReportQueryService menuPreparationReportQueryService;

	/**
	 * Service for managing general fix and crockery allocation report queries.
	 * <p>
	 * This service handles the operations related to retrieving reports that cover 
	 * general fixes and crockery allocations in orders.
	 * </p>
	 */
	OrderGeneralFixAndCrockeryAllocationReportQueryService generalFixAndCrockeryAllocationReportQueryService;

	/**
	 * Service for handling administrative report queries.
	 * <p>
	 * Provides functionality for retrieving and processing reports required for 
	 * administrative purposes.
	 * </p>
	 */
	AdminReportQueryService adminReportQueryService;

	/**
	 * DAO for labour and agency report queries.
	 * <p>
	 * This DAO is responsible for executing database operations related to labour and 
	 * agency reporting.
	 * </p>
	 */
	LabourAndAgencyReportQueryDao labourAndAgencyReportQueryDao;

	/**
	 * Service for handling labour and agency report queries.
	 * <p>
	 * Provides the business logic for retrieving, processing, and analyzing data 
	 * related to labour and agency reports.
	 * </p>
	 */
	LabourAndAgencyReportQueryService labourAndAgencyReportQueryService;

	/**
	 * Service for managing menu allocation report queries.
	 * <p>
	 * Handles business logic for retrieving data related to menu allocation reports.
	 * </p>
	 */
	MenuAllocationReportQueryService menuAllocationReportQueryService;

	/**
	 * Service for handling company settings.
	 * <p>
	 * Provides functionality for retrieving and updating configuration settings 
	 * specific to the company.
	 * </p>
	 */
	CompanySettingService companySettingService;

	/**
	 * Generates a custom menu preparation report for the specified order.
	 * 
	 * @param orderId   the ID of the order for which the report is generated.
	 * @param functionId an optional array of function IDs associated with the order.
	 * @param langType  an optional language type for the report.
	 * @param langCode  an optional language code for the report.
	 * @param request   the HTTP request containing tenant-specific information.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_CUSTOM_MENU_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> customMenuReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateCustomMenuPreparation(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a simple menu preparation report for the specified order.
	 * 
	 * @param orderId   the ID of the order for which the report is generated.
	 * @param functionId an optional array of function IDs associated with the order.
	 * @param langType  an optional language type for the report.
	 * @param langCode  an optional language code for the report.
	 * @param request   the HTTP request containing tenant-specific information.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_SIMPLE_MENU_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> simpleMenuReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId,  @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateSimpleMenuReport(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a menu preparation report in two languages for the specified order.
	 * 
	 * @param orderId     the ID of the order for which the report is generated.
	 * @param functionId  an optional array of function IDs associated with the order.
	 * @param langType    an optional language type for the report.
	 * @param defaultLang the default language for the report.
	 * @param preferLang  the preferred secondary language for the report.
	 * @param langCode    an optional language code for the report.
	 * @param request     the HTTP request containing tenant-specific information.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_TWO_LANGUGAE_MENU_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> menuReportWithTwoLanguage(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam String defaultLang, @RequestParam String preferLang, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateTwoLanguageMenuReport(orderId, functionId, langType, defaultLang, preferLang, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates an exclusive menu preparation report for the specified order.
	 * 
	 * @param orderId   the ID of the order for which the report is generated.
	 * @param functionId an optional array of function IDs associated with the order.
	 * @param langType  an optional language type for the report.
	 * @param langCode  an optional language code for the report.
	 * @param request   the HTTP request containing tenant-specific information.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_EXCLUSIVE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> exclusiveMenuReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateExclusiveMenuReport(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves and generates a manager's menu report for a specified order.
	 *
	 * @param orderId   The unique identifier of the order for which the report is generated.
	 * @param langType  (Optional) The language type for the report, if specified.
	 * @param langCode  (Optional) The language code for the report, if specified.
	 * @return A ResponseContainerDto containing the generated report in byte format. If the report cannot be generated,
	 *			it returns a null value.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_MANAGER_MENU_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> managerMenuReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateManagerMenuReport(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves and generates a menu report with instruction for a specified order.
	 *
	 * @param orderId   The unique identifier of the order for which the report is generated.
	 * @param langType  (Optional) The language type for the report, if specified.
	 * @param langCode  (Optional) The language code for the report, if specified.
	 * @return A ResponseContainerDto containing the generated report in byte format. If the report cannot be generated,
	 *			it returns a null value.
	 */
	@GetMapping(value = ApiPathConstant.INSTRUCTION_MENU_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> instructionMenuReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateInstructionMenuReport(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a menu preparation report with images for a specific order.
	 *
	 * @param orderId   The unique identifier of the order.
	 * @param langType  (Optional) The language type for the report.
	 * @param langCode  (Optional) The language code for localization.
	 * @return A response containing the menu preparation report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_PREMIUM_IMAGE_MENU_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> premiumImageMenuReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generatePremiumImageMenuReport(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a menu preparation report with images for a specific order.
	 *
	 * @param orderId   The unique identifier of the order.
	 * @param langType  (Optional) The language type for the report.
	 * @param langCode  (Optional) The language code for localization.
	 * @return A response containing the menu preparation report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_IMAGE_MENU_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> menuWithImage2Report(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateImageMenuReport(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates an image menu category report for the specified order.
	 * 
	 * @param orderId   the ID of the order for which the report is generated.
	 * @param functionId an optional array of function IDs associated with the order.
	 * @param langType  an optional language type for the report.
	 * @param langCode  an optional language code for the report.
	 * @param request   the HTTP request containing tenant-specific information.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_IMAGE_MENU_CATEGORY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> imageMenuCategoryReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateImageMenuCategoryReport(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a menu preparation report with slogans for a specific order.
	 *
	 * @param orderId   The unique identifier of the order.
	 * @param langType  (Optional) The language type for the report.
	 * @param langCode  (Optional) The language code for localization.
	 * @return A response containing the menu preparation report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_SLOGAN_MENU_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> menuWithSloganReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateSloganMenuReport(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a menu preparation report with both images and slogans for a specific order.
	 *
	 * @param orderId   The unique identifier of the order.
	 * @param langType  (Optional) The language type for the report.
	 * @param langCode  (Optional) The language code for localization.
	 * @return A response containing the menu preparation report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_IMAGE_AND_SLOGAN_MENU_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> menuWithImageAndSloganReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateMenuWithImageAndSloganReport(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a list of functions associated with a specific order for menu preparation.
	 *
	 * @param orderId the ID of the order as a string, which must consist of only numbers. 
	 *	It is validated against the regex pattern {@link RegexConstant#ONLY_NUMBERS_WITHOUT_SPACE}. 
	 *	If invalid, an appropriate error message is returned.
	 * @return a {@link ResponseContainerDto} containing a list of {@link FunctionPerOrderDto} objects.
	 */
	@GetMapping(value = ApiPathConstant.MENU_PREPARATION_FUNCTION_PER_ORDER + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<FunctionPerOrderDto>> getFunctionsPerOrderForMenuPreparation(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(menuPreparationReportQueryService.getFunctionsPerOrderForMenuPreparation(Long.parseLong(orderId)));
	}

	/**
	 * Retrieve a list of item categories for a specific order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @return A ResponseContainerDto containing the list of item categories in raw material per order.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_REPORT_MENU_ALLOCATION_RAW_MATERIAL_CATEGORY_PER_ORDER + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> getItemCategoryPerOrder(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(menuAllocationReportQueryService.getItemCategoryPerOrder(Long.parseLong(orderId)));
	}

	/**
	 * Retrieve a list of functions for a specific order.
	 *
	 * @param orderId The unique identifier of the order.
	 * @return A ResponseContainerDto containing the list of functions per order.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_REPORT_MENU_ALLOCATION_FUNCTION_PER_ORDER + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<FunctionPerOrderDto>> getFunctionsPerOrder(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(menuAllocationReportQueryService.getFunctionPerOrder(Long.parseLong(orderId)));
	}

	/**
	 * Generate an menu item-wise raw material report for a specific order.
	 *
	 * @param orderId        The unique identifier of the order.
	 * @param functionId     (Optional) The ID of the function related to the report.
	 * @param rawMaterialCategoryId (Optional) The ID of the item category related to the report.
	 * @param langType       (Optional) The language type for the report.
	 * @param langCode       (Optional) The language code for the report.
	 * @return A ResponseContainerDto containing the generated raw material report in byte array format.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ALLOCATION_MENU_ITEM_WISE_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> itemWiseRawMaterialReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuAllocationReportQueryService.generateMenuItemWiseRawMaterialReport(orderId, functionId, rawMaterialCategoryId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Handles a GET request to generate and retrieve a total raw material report.
	 *
	 * @param orderId       The ID of the order for which the report is generated.
	 * @param isDateTime    Flag to indicate if date-time should be displayed (true) or only date (false).
	 * @param orderDate     The date of the order.
	 * @param functionId    The optional ID of the function or event related to the report.
	 * @param rawMaterialCategoryId The optional ID of the item category used to filter the report.
	 * @param langType      The optional language type used for report generation.
	 * @param langCode      The optional language code used for localization (e.g., "en_US").
	 * @return A ResponseContainerDto containing the generated total raw material report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ALLOCATION_DETAIL_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<Object> totalRawMaterialReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.IS_DATE_TIME, required = false) Boolean isDateTime, @RequestParam(name = FieldConstants.ORDER_DATE, required = false) LocalDateTime orderDate, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COUNT,required = false) Long count, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, @RequestParam(required = false) boolean isWithQuantity, HttpServletRequest request, Boolean isPopUp ) {
		Object result = menuAllocationReportQueryService.generateDetailRawMaterialReport(orderId, isDateTime, orderDate, functionId, count, rawMaterialCategoryId, langType, langCode, reportName, request, isWithQuantity, isPopUp);
		return (result instanceof FileBean fileBean) ? RequestResponseUtils.generateResponseDto(fileBean.getFile()) : RequestResponseUtils.generateResponseDto(result);
	}

	/**
	 * Handles a GET request to generate and retrieve a total raw material report.
	 *
	 * @param orderId       The ID of the order for which the report is generated.
	 * @param isDateTime    Flag to indicate if date-time should be displayed (true) or only date (false).
	 * @param orderDate     The date of the order.
	 * @param functionId    The optional ID of the function or event related to the report.
	 * @param rawMaterialCategoryId The optional ID of the item category used to filter the report.
	 * @param langType      The optional language type used for report generation.
	 * @param langCode      The optional language code used for localization (e.g., "en_US").
	 * @param request       The HttpServletRequest object.
	 * @return A ResponseContainerDto containing the generated raw material list report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ALLOCATION_TOTAL_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<Object> rawMaterialListReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.IS_DATE_TIME, required = false) Boolean isDateTime, @RequestParam(name = FieldConstants.ORDER_DATE, required = false) LocalDateTime orderDate, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COUNT, required = false) Long count, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, @RequestParam(required = false) boolean isWithQuantity, HttpServletRequest request, Boolean isPopUp) {
		Object result = null;
		if (Boolean.TRUE.equals(companySettingService.getCompannySetting(false).getIsDynamicDesign())) {
			result = menuAllocationReportQueryService.generateTotalRawMaterialReport(orderId, isDateTime, orderDate, functionId, count, rawMaterialCategoryId, langType, langCode, reportName, request, JasperReportNameConstant.MENU_ALLOCATION_REPORT_TOTAL_RAW_MATERIAL_DYNAMIC_DESIGN_REPORT, isWithQuantity, isPopUp);
		} else {
			result = menuAllocationReportQueryService.generateTotalRawMaterialReport(orderId, isDateTime, orderDate, functionId, count, rawMaterialCategoryId, langType, langCode, reportName, request, JasperReportNameConstant.MENU_ALLOCATION_REPORT_TOTAL_RAW_MATERIAL_REPORT, isWithQuantity, isPopUp);
		}
		return (result instanceof FileBean fileBean) ? RequestResponseUtils.generateResponseDto(fileBean.getFile()) : RequestResponseUtils.generateResponseDto(result);
	}

	/**
	 * Handles a GET request to generate and retrieve a total raw material wih price report.
	 *
	 * @param orderId       The ID of the order for which the report is generated.
	 * @param isDateTime    Flag to indicate if date-time should be displayed (true) or only date (false).
	 * @param orderDate     The date of the order.
	 * @param functionId    The optional ID of the function or event related to the report.
	 * @param rawMaterialCategoryId The optional ID of the item category used to filter the report.
	 * @param langType      The optional language type used for report generation.
	 * @param langCode      The optional language code used for localization (e.g., "en_US").
	 * @param request       The HttpServletRequest object.
	 * @return A ResponseContainerDto containing the generated raw material list report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ALLOCATION_TOTAL_RAW_MATERIAL_WITH_PRICE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<Object> totalRawMaterialWithPriceReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.IS_DATE_TIME, required = false) Boolean isDateTime, @RequestParam(name = FieldConstants.ORDER_DATE, required = false) LocalDateTime orderDate, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COUNT, required = false) Long count, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, @RequestParam(required = false) boolean isWithQuantity, HttpServletRequest request, Boolean isPopUp) {
		Object result = null;
		if (Boolean.TRUE.equals(companySettingService.getCompannySetting(false).getIsDynamicDesign())) {
			result = menuAllocationReportQueryService.generateTotalRawMaterialReport(orderId, isDateTime, orderDate, functionId, count, rawMaterialCategoryId, langType, langCode, reportName, request, JasperReportNameConstant.MENU_ALLOCATION_REPORT_TOTAL_RAW_MATERIAL_WITH_PRICE_DYNAMIC_DESIGN_REPORT, isWithQuantity, isPopUp);
		} else {
			result = menuAllocationReportQueryService.generateTotalRawMaterialReport(orderId, isDateTime, orderDate, functionId, count, rawMaterialCategoryId, langType, langCode, reportName, request, JasperReportNameConstant.MENU_ALLOCATION_REPORT_TOTAL_RAW_MATERIAL_WITH_PRICE_REPORT, isWithQuantity, isPopUp);
		}
		return (result instanceof FileBean fileBean) ? RequestResponseUtils.generateResponseDto(fileBean.getFile()) : RequestResponseUtils.generateResponseDto(result);
	}

	/**
	 * Generates a report of the total raw materials with category details for a specific order.
	 *
	 * @param orderId the ID of the order.
	 * @param isDateTime Flag to indicate if date-time should be displayed (true) or only date (false).
	 * @param orderDate the date of the order.
	 * @param functionId optional array of function IDs to filter the report.
	 * @param count optional count value for additional filtering.
	 * @param rawMaterialCategoryId optional array of raw material category IDs for filtering.
	 * @param langType optional language type for localization.
	 * @param langCode optional language code for localization.
	 * @param isWithQuantity boolean flag indicating whether the report should include quantities.
	 * @param request the HTTP servlet request containing context information.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ALLOCATION_TOTAL_RAW_MATERIAL_WITH_CATEGORY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<Object> rawMaterialListWReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.IS_DATE_TIME, required = false) Boolean isDateTime, @RequestParam(name = FieldConstants.ORDER_DATE, required = false) LocalDateTime orderDate, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COUNT, required = false) Long count, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(required = false) boolean isWithQuantity, HttpServletRequest request, Boolean isPopUp) {
		Object result = menuAllocationReportQueryService.generateTotalRawMaterialReport(orderId, isDateTime, orderDate, functionId, count, rawMaterialCategoryId, langType, langCode, reportName, request, JasperReportNameConstant.MENU_ALLOCATION_REPORT_TOTAL_RAW_MATERIAL_WITH_CATEGORY_REPORT, isWithQuantity, isPopUp);
		return (result instanceof FileBean fileBean) ? RequestResponseUtils.generateResponseDto(fileBean.getFile()) : RequestResponseUtils.generateResponseDto(result);
	}

	/**
	 * Generates a raw material report categorized by chef and labour for a specific order.
	 *
	 * @param orderId the ID of the order.
	 * @param isDateTime Flag to indicate if date-time should be displayed (true) or only date (false).
	 * @param orderDate the date of the order.
	 * @param orderTypeId optional array of order type IDs for filtering.
	 * @param contactId optional array of contact IDs for filtering.
	 * @param functionId optional array of function IDs to filter the report.
	 * @param count optional count value for additional filtering.
	 * @param rawMaterialCategoryId optional array of raw material category IDs for filtering.
	 * @param menuItemId optional array of menu item IDs for filtering.
	 * @param langType optional language type for localization.
	 * @param langCode optional language code for localization.
	 * @param request the HTTP servlet request containing context information.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ALLOCATION_CHEF_LABOUR_WISE_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<Object> chefLabourWiseRawMaterialReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.IS_DATE_TIME, required = false) Boolean isDateTime, @RequestParam(name = FieldConstants.ORDER_DATE, required = false) LocalDateTime orderDate, @RequestParam(name = FieldConstants.ORDER_TYPE_ID, required = false) Long[] orderTypeId, @RequestParam(name = FieldConstants.CONTACT_ID, required = false) Long[] contactId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COUNT,required = false) Long count, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.MENU_ITEM_ID, required = false) Long[] menuItemId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request, Boolean isPopUp) {
		Object result = menuAllocationReportQueryService.generateChefLabourWiseRawMaterialReport(orderId, isDateTime, orderDate, orderTypeId, contactId, functionId, count, rawMaterialCategoryId, menuItemId, langType, langCode, reportName, request, isPopUp);
		return (result instanceof FileBean fileBean) ? RequestResponseUtils.generateResponseDto(fileBean.getFile()) : RequestResponseUtils.generateResponseDto(result);
	}

	/**
	 * Generates a raw material report categorized by chef, labour, and supplier for a specific order.
	 *
	 * @param orderId the ID of the order.
	 * @param isDateTime Flag to indicate if date-time should be displayed (true) or only date (false).
	 * @param orderDate the date of the order.
	 * @param orderTypeId optional array of order type IDs for filtering.
	 * @param contactId optional array of contact IDs for filtering.
	 * @param functionId optional array of function IDs to filter the report.
	 * @param count optional count value for additional filtering.
	 * @param rawMaterialCategoryId optional array of raw material category IDs for filtering.
	 * @param menuItemId optional array of menu item IDs for filtering.
	 * @param langType optional language type for localization.
	 * @param langCode optional language code for localization.
	 * @param request the HTTP servlet request containing context information.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ALLOCATION_CHEF_LABOUR_SUPPLIER_WISE_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<Object> chefLabourSupplierWiseRawMaterialReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.IS_DATE_TIME, required = false) Boolean isDateTime, @RequestParam(name = FieldConstants.ORDER_DATE, required = false) LocalDateTime orderDate, @RequestParam(name = FieldConstants.ORDER_TYPE_ID, required = false) Long[] orderTypeId, @RequestParam(name = FieldConstants.CONTACT_ID, required = false) Long[] contactId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COUNT,required = false) Long count, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.MENU_ITEM_ID, required = false) Long[] menuItemId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request, Boolean isPopUp) {
		Object result = menuAllocationReportQueryService.generateChefLabourSupplierWiseRawMaterialReport(orderId, isDateTime, orderDate, orderTypeId, contactId, functionId, count, rawMaterialCategoryId, menuItemId, langType, langCode, reportName, request, isPopUp);
		return (result instanceof FileBean fileBean) ? RequestResponseUtils.generateResponseDto(fileBean.getFile()) : RequestResponseUtils.generateResponseDto(result);
	}

	/**
	 * Handles an HTTP GET request to generate and retrieve a menu with quantity report
	 * with quantity information based on the provided order ID, language type, and language code.
	 *
	 * @param orderId   The unique identifier of the order for which the report is generated.
	 * @param langType  (Optional) The language type (e.g., 1 for English, 2 for Spanish) used for localization.
	 * @param langCode  (Optional) The language code (e.g., "en" for English, "es" for Spanish) used for localization.
	 * @return A ResponseContainerDto containing the generated report as a byte array, or null if the report could not be generated.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ALLOCATION_MENU_WITH_QUANTITY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> menuWithQuantityReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuAllocationReportQueryService.generateMenuWithAndWithOutQuantityReport(orderId, functionId, langType, langCode, reportName, request, JasperReportNameConstant.MENU_ALLOCATION_REPORT_MENU_WITH_QUANTITY_REPORT);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a menu allocation report for a specified order without quantity information.
	 *
	 * @param orderId  The unique identifier of the order for which the report is generated.
	 * @param langType An optional parameter specifying the language type for the report.
	 * @param langCode An optional parameter specifying the language code for the report.
	 * @return A ResponseContainerDto containing the generated report file in bytes, or null if the file could not be generated.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ALLOCATION_MENU_WITH_OUT_QUANTITY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> menuWithOutQuantityReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuAllocationReportQueryService.generateMenuWithAndWithOutQuantityReport(orderId, functionId, langType, langCode, reportName, request, JasperReportNameConstant.MENU_ALLOCATION_REPORT_MENU_OUT_WITH_QUANTITY_REPORT);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieve and serve the supplier-wise raw material report as a downloadable file.
	 * This endpoint allows clients to request and download a supplier-wise raw material report for a specific order,
	 * language type, and language code.
	 *
	 * @param orderId  The ID of the order for which the report is generated.
	 * @param langType (Optional) The language type (e.g., 1 for English, 2 for  another language).
	 * @param langCode (Optional) The language code (e.g., "en_US" for English).
	 * @return A {@link ResponseContainerDto} containing the raw material report file as a byte array. If the file is not available, the response will contain null data.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ALLOCATION_SUPPLIER_WISE_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> supplierWiseRawMaterialReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, 
		@RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, 
		@RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, 
		@RequestParam(name = FieldConstants.REPORT_NAME) String reportName, 
		@RequestParam(name = FieldConstants.COMMON_FIELD_CONTACT_DATE, required = false) Long[] contactId, 
		@RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId,
		HttpServletRequest request) {
		FileBean file = menuAllocationReportQueryService.generateSupplierWiseRawMaterialReport(orderId, langType, langCode, reportName, contactId, rawMaterialCategoryId, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a party complain report for a given order ID and language code.
	 *
	 * @param orderId   The unique identifier of the order for which the report is generated.
	 * @param langCode  (Optional) The language code for localization. If provided, the report will be generated in the specified language.
	 * @return A ResponseContainerDto containing the party complain report in bytes, or null if the report is not available.
	 */
	@GetMapping(value = ApiPathConstant.PARTY_COMPLAIN_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> partyComplainReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = adminReportQueryService.generatePartyComplainReport(orderId, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves and generates a Name Plate Report in a byte array format.
	 *
	 * @param orderId   The ID of the order for which the report is generated.
	 * @param langType  The language type for the report (optional).
	 * @param reportSize The report will be generate based on the report size
	 * @return A ResponseContainerDto containing the Name Plate Report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.NAME_PLATE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> namePlateReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, Byte reportSize, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName) {
		FileBean file = adminReportQueryService.generateCounterNamePlateReport(orderId, langType, reportSize, reportName);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a two-language name plate report for a specific order.
	 * The report contains information in both the default and preferred languages.
	 *
	 * @param orderId the ID of the order.
	 * @param reportSize the size of the report (can be used for controlling the format or layout).
	 * @param defaultLang the default language for the report.
	 * @param preferLang the preferred language for the report.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.TWO_LANGUAGE_NAME_PLATE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> twoLanguageNamePlateReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, Byte reportSize, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName,  @RequestParam String defaultLang, @RequestParam String preferLang) {
		FileBean file = adminReportQueryService.generateTwoLanguageCounterNamePlateReport(orderId, reportSize, reportName, defaultLang, preferLang);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a two-language name plate document report for a specific order.
	 * The document contains information in both the default and preferred languages.
	 *
	 * @param orderId the ID of the order.
	 * @param reportSize the size of the report (can be used for controlling the format or layout).
	 * @param defaultLang the default language for the report.
	 * @param preferLang the preferred language for the report.
	 * @return a {@link ResponseContainerDto} containing the generated document as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.TWO_LANGUAGE_NAME_PLATE_DOC_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> twoLanguageNamePlateDocReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, Byte reportSize, @RequestParam String defaultLang, @RequestParam String preferLang) {
		FileBean file = adminReportQueryService.generateTwoLanguageCounterNamePlateDocReport(orderId, reportSize, defaultLang, preferLang);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a name plate document report for a specific order in a specified language.
	 *
	 * @param orderId the ID of the order.
	 * @param langType optional language type for localization.
	 * @param reportSize the size of the report (can be used for controlling the format or layout).
	 * @return a {@link ResponseContainerDto} containing the generated document as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.NAME_PLATE_DOC_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> namePlateDocReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId,@RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, Byte reportSize) {
		FileBean file = adminReportQueryService.generateCounterNamePlateDocReport(orderId, langType, reportSize);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves and returns the Address Chitthi report for a specific order.
	 *
	 * This endpoint generates an Address Chitthi report for the given order and language parameters.
	 * It uses the `adminReportQueryService` to generate the report and returns the file content as a byte array.
	 *
	 * @param orderId   The unique identifier of the order for which the Address Chitthi report is requested.
	 * @param godownId  An array of godown IDs to filter the report by specific godowns.
	 * @param langType  The language type identifier (optional) specifying the language of the report.
	 * @param langCode  The language code (optional) used to define the language of the report.
	 * @param request   The HttpServletRequest object to extract additional request-specific data (e.g., company logo).
	 * @return A ResponseContainerDto containing the byte array of the Address Chitthi report file, or null if not found.
	 */
	@GetMapping(value = ApiPathConstant.ADMIN_ADDRESS_CHITHHI_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> addressChitthiReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_GODOWN_ID) Long[] godownId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = adminReportQueryService.generateAddressChitthiReport(orderId, godownId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves and returns the Chef Labour Report for a specific order.
	 *
	 * This endpoint generates a Chef Labour Report for the given order and language parameters.
	 *
	 * @param orderId   The unique identifier of the order for which the report is requested.
	 * @param langType  The language type identifier to specify the language type of the report (optional).
	 * @param langCode  The language code to specify the language of the report (optional).
	 * @param currentDate  The current date for which the report is generated.
	 * @return A ResponseContainerDto containing the byte array of the Chef Labour Report file, or null if not found.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_AND_CROCKERY_CHEF_LABOUR_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> chefLabourReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, ChefOrOutsideLabourReportParams params, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = labourAndAgencyReportQueryService.generateChefLabourReport(orderId, params, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves and generates a Chef Labour Chithhi Report in a byte array format.
	 *
	 * @param orderId   The ID of the order for which the report is generated.
	 * @param langType  The language type for the report (optional).
	 * @param langCode  The language code for the report (optional).
	 * @return A ResponseContainerDto containing the Chef Labour Chithhi Report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_AND_CROCKERY_CHEF_LABOUR_CHITHHI_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> chefLabourChithhiReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, ChefOrOutsideLabourReportParams params, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = labourAndAgencyReportQueryService.generateChefLabourChithhiReport(orderId, params, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves and generates an Outside Agency Report in a byte array format.
	 *
	 * @param orderId   The ID of the order for which the report is generated.
	 * @param langType  The language type for the report (optional).
	 * @param langCode  The language code for the report (optional).
	 * @return A ResponseContainerDto containing the Outside Agency Report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_AND_CROCKERY_OUTSIDE_AGENCY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> outSideAgencyReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, ChefOrOutsideLabourReportParams params, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = labourAndAgencyReportQueryService.generateOutsideAgencyReport(orderId, params, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves and generates an Outside Agency Chithhi Report in a byte array format.
	 *
	 * @param orderId   The ID of the order for which the report is generated.
	 * @param langType  The language type for the report (optional).
	 * @param langCode  The language code for the report (optional).
	 * @return A ResponseContainerDto containing the Outside Agency Chithhi Report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_AND_CROCKERY_OUTSIDE_AGENCY_CHITHHI_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> outSideAgencyChithhiReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, ChefOrOutsideLabourReportParams params, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = labourAndAgencyReportQueryService.generateOutsideAgencyChithhiReport(orderId, params, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Controller method for generating a order file report for raw materials.
	 *
	 * @param orderId   The unique identifier of the order for which the report is generated.
	 * @param langType  (Optional) The language type for the report, if provided.
	 * @param langCode  (Optional) The language code for the report, if provided.
	 * @return A {@link ResponseContainerDto} containing the generated order file report data as bytes, or an empty response if the report could not be generated.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_ORDER_FILE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateOrderFileReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.DATA_TYPE_ID) Long[] dataTypeId , @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = menuAllocationReportQueryService.generateOrderFileReport(orderId, functionId, rawMaterialCategoryId, dataTypeId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a raw material report in A5 format for a specific order.
	 * The report includes information related to raw materials based on the provided function and category filters.
	 *
	 * @param orderId the ID of the order.
	 * @param functionId optional filter for report generation based on function IDs.
	 * @param rawMaterialCategoryId optional filter for raw material categories.
	 * @param langType optional language type for localization.
	 * @param langCode optional language code for localization.
	 * @param request the HTTP request object used for generating the report.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.TIME_WISE_RAW_MATERIAL_A5_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<Object> generateRawMaterialA5Report(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request, Boolean isPopUp) {
		Object result = menuAllocationReportQueryService.generateRawMaterialA5Report(orderId, functionId, rawMaterialCategoryId, langType, langCode, reportName, request, isPopUp);
		return (result instanceof FileBean fileBean) ? RequestResponseUtils.generateResponseDto(fileBean.getFile()) : RequestResponseUtils.generateResponseDto(result);
	}

	/**
	 * Generates a raw material report in A6 format for a specific order.
	 * The report includes information related to raw materials based on the provided function and category filters.
	 *
	 * @param orderId the ID of the order.
	 * @param functionId optional filter for report generation based on function IDs.
	 * @param rawMaterialCategoryId optional filter for raw material categories.
	 * @param langType optional language type for localization.
	 * @param langCode optional language code for localization.
	 * @param request the HTTP request object used for generating the report.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.TIME_WISE_RAW_MATERIAL_A6_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<Object> generateRawMaterialA6Report(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request, Boolean isPopUp) {
		Object result = menuAllocationReportQueryService.generateRawMaterialA6Report(orderId, functionId, rawMaterialCategoryId, langType, langCode, reportName, request, isPopUp);
		return (result instanceof FileBean fileBean) ? RequestResponseUtils.generateResponseDto(fileBean.getFile()) : RequestResponseUtils.generateResponseDto(result);
	}

	/**
	 * Generates a raw material report in A4 format for a specific order.
	 * The report includes information related to raw materials based on the provided function and category filters.
	 *
	 * @param orderId the ID of the order.
	 * @param functionId optional filter for report generation based on function IDs.
	 * @param rawMaterialCategoryId optional filter for raw material categories.
	 * @param langType optional language type for localization.
	 * @param langCode optional language code for localization.
	 * @param request the HTTP request object used for generating the report.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.TIME_WISE_RAW_MATERIAL_A4_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<Object> generateRawMaterialA4Report(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID, required = false) Long[] rawMaterialCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request, Boolean isPopUp) {
		Object result = menuAllocationReportQueryService.generateRawMaterialA4Report(orderId, functionId, rawMaterialCategoryId, langType, langCode, reportName, request, isPopUp);
		return (result instanceof FileBean fileBean) ? RequestResponseUtils.generateResponseDto(fileBean.getFile()) : RequestResponseUtils.generateResponseDto(result);
	}

	/**
	 * Retrieve a list of item categories which are type is direct order for a specific order.
	 * @param orderId The unique identifier of the order.
	 * @return A ResponseContainerDto containing the list of item categories in raw material per order.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_DIRECT_ORDER + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<RawMaterialCategoryDirectOrderDto>> findRawMaterialCategoriesByDirectOrder(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(menuAllocationReportQueryService.findRawMaterialCategoriesByDirectOrderAndOrderId(Long.parseLong(orderId)));
	}

	/**
	 * Generates a labour report for an event agency distribution order and returns it as a downloadable file.
	 *
	 * This endpoint generates a labour report for the specified order and provides it in a downloadable format.
	 *
	 * @param orderId The unique identifier of the event agency distribution order for which the report is generated.
	 * @param langType (Optional) The language type for the report. If provided, the report will be generated in the specified language.
	 * @param langCode (Optional) The language code for the report. If provided, the report will be generated in the specified language.
	 * @return A ResponseContainerDto containing the generated labour report as a byte array. If the report is generated successfully, the byte array is included in the response. If the report generation fails or if the specified order does not exist, the response will contain a null byte array.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_AND_CROCKERY_LABOUR_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> labourReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, LabourReportParams labourReportParams, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = labourAndAgencyReportQueryService.generateLabourReport(orderId, labourReportParams, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a Labour Chithhi Report for a specific order.
	 *
	 * This endpoint generates a Labour Chithhi Report for the given order and returns it as a byte array.
	 *
	 * @param orderId The unique identifier of the order for which the report is generated.
	 * @param langType (Optional) The language type for the report, if specified.
	 * @param langCode (Optional) The language code for the report, if specified.
	 * @return A byte array containing the generated Labour Chithhi Report, or null if the report could not be generated.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_AND_CROCKERY_LABOUR_CITHHI_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> labourChithhiReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, LabourReportParams labourReportParams, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = labourAndAgencyReportQueryService.generateLabourChithhiReport(orderId, labourReportParams, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Endpoint for generating and retrieving an event agency distribution booking report.
	 *
	 * This endpoint takes parameters such as orderId, langType, langCode, and currentDate to generate a booking
	 * report using the {@link OrderGeneralFixAndCrockeryAllocationReportQueryService}. The generated report is encapsulated in
	 * a {@link FileBean} and returned as a byte array in the response.
	 *
	 * @param orderId       The unique identifier of the order for which the booking report is generated.
	 * @param langType      The language type for localization (optional).
	 * @param langCode      The language code for the report's language (optional).
	 * @return A {@link ResponseContainerDto} containing the byte array of the generated booking report file.
	 *         If the report is not generated successfully, the response will contain a null byte array.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_AND_CROCKERY_BOOKING_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> bookingReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName) {
		FileBean file = labourAndAgencyReportQueryService.generateBookingReport(orderId, langType, langCode, reportName);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a wastage report for a given order ID, language type, and language code.
	 *
	 * @param orderId   The unique identifier of the order for which the report is generated.
	 * @param langType  (Optional) The language type for localization. If provided, the report may consider this language type.
	 * @param langCode  (Optional) The language code for localization. If provided, the report will be generated in the specified language.
	 * @return A ResponseContainerDto containing the wastage report in bytes, or null if the report is not available.
	 */
	@GetMapping(value = ApiPathConstant.ADMIN_WASTAGE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> wastage(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = adminReportQueryService.generateWastageReport(orderId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves and returns the Feedback Report for a specific order.
	 *
	 * This endpoint generates a Feedback Report for the given order and language parameters.
	 *
	 * @param orderId   The unique identifier of the order for which the report is requested.
	 * @param langType  The language type identifier to specify the language type of the report (optional).
	 * @param langCode  The language code to specify the language of the report (optional).
	 * @return A ResponseContainerDto containing the byte array of the Feedback Report file, or null if not found.
	 */
	@GetMapping(value = ApiPathConstant.ADMIN_DISH_COUNTING)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> feedbackReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = adminReportQueryService.generateDishCountingReport(orderId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a supplier details report based on the provided order ID and optional language parameters.
	 *
	 * @param orderId   The unique identifier of the order for which the report is generated.
	 * @param langType  (Optional) The language type for localization.
	 * @param langCode  (Optional) The language code for localization.
	 * @return A {@link ResponseContainerDto} containing the generated supplier details report in byte format.
	 */
	@GetMapping(value = ApiPathConstant.ADMIN_SUPPLIER_DETAILS_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> supplierDetailsReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = adminReportQueryService.generateSupplierDetailsReport(orderId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a dish costing report based on the provided order ID and optional language parameters.
	 * 
	 * @param orderId	The unique identifier of the order for which the report is generated.
	 * @param langType	(Optional) The language type for localization.
	 * @param langCode	(Optional) The language code for localization.
	 * @return	A {@link ResponseContainerDto} containing the generated dish costing report in byte format.
	 */
	@GetMapping(value = ApiPathConstant.ADMIN_DISH_COSTING_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> dishCostingReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = adminReportQueryService.generateDishCostingReport(orderId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a function wise dish costing report based on the provided order ID and optional language parameters.
	 * 
	 * @param orderId	The unique identifier of the order for which the report is generated.
	 * @param langType	(Optional) The language type for localization.
	 * @param langCode	(Optional) The language code for localization.
	 * @return	A {@link ResponseContainerDto} containing the generated dish costing report in byte format.
	 */
	@GetMapping(value = ApiPathConstant.ADMIN_TOTAL_DISH_COSTING)
	public ResponseContainerDto<byte[]> totalDishCostingReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = adminReportQueryService.generateTotalDishCostingReport(orderId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a customer extra cost report for a specific order.
	 * The report includes details of extra costs incurred by the customer, with optional language support.
	 *
	 * @param orderId the ID of the order for which the report is generated.
	 * @param langType optional language type for report localization.
	 * @param langCode optional language code for report localization.
	 * @param request the HTTP request object used for generating the report.
	 * @return a {@link ResponseContainerDto} containing the generated report as a byte array.
	 */
	@GetMapping(value = ApiPathConstant.ADMIN_CUSTOMER_EXTRA_COST_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> customerFormatReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, HttpServletRequest request) {
		FileBean file = adminReportQueryService.generateCustomerExtraCostReport(orderId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves crockery report on the specific order ID and optional language parameters.
	 * 
	 * @param orderId    The unique identifier of the order for which the report is generated.
	 * @param langType   (Optional) The language type for localization.
	 * @param langCode   (Optional) The language code for localization.
	 * @param withQty    Flag to indicate whether to include quantity in the report.
	 * @return A {@link ResponseContainerDto} containing the generated crockery report in byte format.
	 */
	@GetMapping(value = ApiPathConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateCrockeryReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, @RequestParam(name = "withQty", defaultValue = "false") boolean withQty,  @RequestParam(name = "maxSetting", required = true) boolean maxSetting, HttpServletRequest request) {
		FileBean file;
		if (withQty && maxSetting) {
			file = generalFixAndCrockeryAllocationReportQueryService.generateCrockeryWithQuantityReport(orderId, langType, langCode, reportName, request);
		} else if (!withQty && maxSetting) {
			file = generalFixAndCrockeryAllocationReportQueryService.generateCrockeryWithoutQuantityReport(orderId, langType, langCode, reportName, request);
		} else if (withQty && !maxSetting) {
			file = generalFixAndCrockeryAllocationReportQueryService.generateCrockeryWithQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);
		} else { 
			file = generalFixAndCrockeryAllocationReportQueryService.generateCrockeryWithoutQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);
		}
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves kitchen crockery report on the specific order ID and optional language parameters.
	 * 
	 * @param orderId    The unique identifier of the order for which the report is generated.
	 * @param langType   (Optional) The language type for localization.
	 * @param langCode   (Optional) The language code for localization.
	 * @param withQty    Flag to indicate whether to include quantity in the report.
	 * @return A {@link ResponseContainerDto} containing the generated kitchen crockery report in byte format.
	 */
	@GetMapping(value = ApiPathConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_KITCHEN_CROCKERY_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateKitchenCrockeryReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, @RequestParam(defaultValue = "false") boolean withQty,  @RequestParam(required = true) boolean maxSetting, HttpServletRequest request) {
		FileBean file;
		if (withQty && maxSetting) {
			file = generalFixAndCrockeryAllocationReportQueryService.generateKitchenCrockeryWithQuantityReport(orderId, langType, langCode, reportName, request);
		} else if (!withQty && maxSetting) {
			file = generalFixAndCrockeryAllocationReportQueryService.generateKitchenCrockeryWithoutQuantityReport(orderId, langType, langCode, reportName, request);
		} else if (withQty) {
			file = generalFixAndCrockeryAllocationReportQueryService.generateKitchenCrockeryWithQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);
		} else {
			file = generalFixAndCrockeryAllocationReportQueryService.generateKitchenCrockeryWithoutQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);
		}
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves a menu report with crockery for a specific order.
	 *
	 * @param orderId   The unique identifier of the order.
	 * @param langType  (Optional) The language type for the report.
	 * @param langCode  (Optional) The language code for localization.
	 * @return A response containing the menu report with crockery as a byte array.
	 */
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	@GetMapping(value = ApiPathConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_CROCKERY_WITH_MENU_REPORT)
	public ResponseContainerDto<byte[]> generateCrockeryWithMenuReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, @RequestParam(name = "maxSetting", required = true) boolean maxSetting, HttpServletRequest request) {
		FileBean file = maxSetting
				? generalFixAndCrockeryAllocationReportQueryService.generateCrockeryWithMenuReport(orderId, functionId, langType, langCode, reportName, request)
				: generalFixAndCrockeryAllocationReportQueryService.generateCrockeryWithMenuWithoutMaxSettingReport(orderId, functionId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves general fix report on the specific order ID and optional language parameters.
	 * 
	 * @param orderId    The unique identifier of the order for which the report is generated.
	 * @param langType   (Optional) The language type for localization.
	 * @param langCode   (Optional) The language code for localization.
	 * @param withQty    Flag to indicate whether to include quantity in the report.
	 * @return A {@link ResponseContainerDto} containing the generated general fix report in byte format.
	 */
	@GetMapping(value = ApiPathConstant.GENERAL_FIX_AND_CROCKERY_ALLOCATION_GENERAL_FIX_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateGeneralFixReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME) String reportName, @RequestParam(name = "withQty", defaultValue = "false") boolean withQty, @RequestParam(name = "maxSetting", required = true) boolean maxSetting, HttpServletRequest request) {
		FileBean file;
		if (withQty && maxSetting) {
			file = generalFixAndCrockeryAllocationReportQueryService.generateOrderGeneralFixWithQuantityReport(orderId, langType, langCode, reportName, request);
		} else if(!withQty && maxSetting) {
			file = generalFixAndCrockeryAllocationReportQueryService.generateOrderGeneralFixWithoutQuantityReport(orderId, langType, langCode, reportName, request);
		} else if (withQty && !maxSetting) {
			file = generalFixAndCrockeryAllocationReportQueryService.generateOrderGeneralFixWithQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);
		} else {
			file = generalFixAndCrockeryAllocationReportQueryService.generateOrderGeneralFixWithoutQuantityWithoutMaxSettingReport(orderId, langType, langCode, reportName, request);
		}
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves the list of Chef Labour suppliers for the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of Chef Labour suppliers.
	 */
	@GetMapping(value = ApiPathConstant.CHEF_LABOUR_SUPPLIERS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findChefLabourSuppliersByOrder(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(labourAndAgencyReportQueryDao.findChefOrOutsideLabourSuppliersByOrder(Long.parseLong(orderId), Constants.ORDER_TYPE_FOR_CHEF_LABOUR));
	}

	/**
	 * Retrieves the list of Outside Labour suppliers for the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of Outside Labour suppliers.
	 */
	@GetMapping(value = ApiPathConstant.OUTSIDE_LABOUR_SUPPLIERS)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findOutsideLabourSuppliersByOrder(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(labourAndAgencyReportQueryDao.findChefOrOutsideLabourSuppliersByOrder(Long.parseLong(orderId), Constants.ORDER_TYPE_FOR_OUT_SIDE_LABOUR));
	}

	/**
	 * Retrieves the list of active functions for Chef Labour based on the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of active functions for Chef Labour.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE_FUNCTIONS_CHEF_LABOUR)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<FunctionPerOrderDto>> findActiveFunctionsByOrderForChefLabour(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(labourAndAgencyReportQueryService.findFunctionForChefOrOutside(Long.parseLong(orderId), Constants.ORDER_TYPE_FOR_CHEF_LABOUR));
	}

	/**
	 * Retrieves the list of active functions for Outside Labour based on the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of active functions for Outside Labour.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE_FUNCTIONS_OUTSIDE)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<FunctionPerOrderDto>> findActiveFunctionsByOrderForOutside(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(labourAndAgencyReportQueryService.findFunctionForChefOrOutside(Long.parseLong(orderId), Constants.ORDER_TYPE_FOR_OUT_SIDE_LABOUR));
	}

	/**
	 * Retrieves the list of active functions for Labour based on the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of active functions for Labour.
	 */
	@GetMapping(value = ApiPathConstant.ACTIVE_FUNCTIONS_LABOUR)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<FunctionPerOrderDto>> findActiveFunctionsByOrderForLabour(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(labourAndAgencyReportQueryService.findFunctionForLabour(Long.parseLong(orderId)));
	}

	/**
	 * Retrieves the list of final materials for Chef or Outside Labour based on the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of final materials for Chef or Outside Labour.
	 */
	@GetMapping(value = ApiPathConstant.CHEF_LABOUR_ITEM_NAME)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findFinalMaterialOfChefOrOutsideLabour(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(labourAndAgencyReportQueryDao.findChefOrOutsideLabourFinalMaterialByOrder(Long.parseLong(orderId), Constants.ORDER_TYPE_FOR_CHEF_LABOUR));
	}

	/**
	 * Retrieves the list of final materials for Outside Labour based on the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of final materials for Outside Labour.
	 */
	@GetMapping(value = ApiPathConstant.OUTSIDE_LABOUR_ITEM_NAME)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findFinalMaterialOfOutsideLabour(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(labourAndAgencyReportQueryDao.findChefOrOutsideLabourFinalMaterialByOrder(Long.parseLong(orderId), Constants.ORDER_TYPE_FOR_OUT_SIDE_LABOUR));
	}

	/**
	 * Retrieves the list of Labour Supplier categories for the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of Labour Supplier categories.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_SUPPLIER_CATEGORY + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findLabourSupplierCategory(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(labourAndAgencyReportQueryDao.findLabourSupplierCategory(Long.parseLong(orderId)));
	}

	/**
	 * Retrieves the list of Labour Supplier category names based on the given order and category ID.
	 * 
	 * @param orderId the ID of the order.
	 * @param supplierCategoryId the ID of the supplier category.
	 * @return a response container containing a list of Labour Supplier category names.
	 */
	@GetMapping(value = ApiPathConstant.LABOUR_SUPPLIER_NAME + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findLabourSupplierCategoryName(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId, @RequestParam(required = false) Long supplierCategoryId) {
		return RequestResponseUtils.generateResponseDto(labourAndAgencyReportQueryDao.findLabourSupplierCategoryName(Long.parseLong(orderId), supplierCategoryId));
	}

	/**
	 * Retrieves the list of event raw material supplier categories for the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of raw material supplier categories.
	 */
	@GetMapping(value = "raw-material-allocation-supplier-category" + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findEventRawMaterialSupplierCategory(@PathVariable(value = FieldConstants.COMMON_FIELD_ID, required = false) @Pattern(regexp = RegexConstant.ONLY_NUMBERS_WITHOUT_SPACE, message = MessagesConstant.INVALID_ID) String orderId) {
		return RequestResponseUtils.generateResponseDto(menuAllocationReportQueryDao.generateRawMaterialAllocationSupplierCategory(Long.parseLong(orderId)));
	}

	/**
	 * Generates a table menu report 1 for the given order, with optional language and function filters.
	 * 
	 * @param orderId the ID of the order.
	 * @param langType the language type for the report.
	 * @param langCode the language code for the report.
	 * @param functionId the list of function IDs to include in the report.
	 * @param request the HTTP request object used for report generation.
	 * @return a response container containing the generated table menu report.
	 */
	@GetMapping(value = ApiPathConstant.TABLE_MENU_REPORT_1)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<byte[]> tableMenuReport1(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateTableMenuReport(orderId, langType, langCode, functionId, request, JasperReportNameConstant.TABLE_MENU_REPORT_1);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a table menu document report 1 for the given order, with optional language and function filters.
	 * 
	 * @param orderId the ID of the order.
	 * @param langType the language type for the report.
	 * @param langCode the language code for the report.
	 * @param functionId the list of function IDs to include in the report.
	 * @param request the HTTP request object used for report generation.
	 * @return a response container containing the generated table menu document report.
	 */
	@GetMapping(value = ApiPathConstant.TABLE_MENU_DOC_REPORT_1)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<byte[]> tableMenuDocReport1(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateTableMenuDocReport(orderId, langType, langCode, functionId, request, JasperReportNameConstant.TABLE_MENU_REPORT_1);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a table menu report 2 for the given order, with optional language and function filters.
	 * 
	 * @param orderId the ID of the order.
	 * @param langType the language type for the report.
	 * @param langCode the language code for the report.
	 * @param functionId the list of function IDs to include in the report.
	 * @param request the HTTP request object used for report generation.
	 * @return a response container containing the generated table menu report.
	 */
	@GetMapping(value = ApiPathConstant.TABLE_MENU_REPORT_2)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<byte[]> tableMenuReport2(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateTableMenuReport(orderId, langType, langCode, functionId, request, JasperReportNameConstant.TABLE_MENU_REPORT_2);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a table menu document report 2 for the given order, with optional language and function filters.
	 * 
	 * @param orderId the ID of the order.
	 * @param langType the language type for the report.
	 * @param langCode the language code for the report.
	 * @param functionId the list of function IDs to include in the report.
	 * @param request the HTTP request object used for report generation.
	 * @return a response container containing the generated table menu document report.
	 */
	@GetMapping(value = ApiPathConstant.TABLE_MENU_DOC_REPORT_2)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<byte[]> tableMenuDocReport2(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_FUNCTION_ID, required = false) Long[] functionId, HttpServletRequest request) {
		FileBean file = menuPreparationReportQueryService.generateTableMenuDocReport(orderId, langType, langCode, functionId, request, JasperReportNameConstant.TABLE_MENU_REPORT_2);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves the list of selected Chef Labour agencies for the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of selected Chef Labour agencies.
	 */
	@GetMapping(value = ApiPathConstant.SELECTED_CHEF_LABOUR_AGENCY + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findSelectedChefLabourAgency(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId) {
		return RequestResponseUtils.generateResponseDto(menuAllocationReportQueryDao.findSelectedChefLabourAgency(orderId));
	}

	/**
	 * Retrieves the list of all menu items for the given order.
	 * 
	 * @param orderId the ID of the order.
	 * @return a response container containing a list of all menu items for the order.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ITEMS + ApiPathConstant.ID)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<CommonDataForDropDownDto>> findAllMenuItems(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId) {
		return RequestResponseUtils.generateResponseDto(menuAllocationReportQueryDao.findAllMenuItemsByOrderId(orderId));
	}

	/**
	 * Generates a combined report for an order based on the provided parameters.
	 * 
	 * @param orderId the ID of the order for which to generate the combined report
	 * @param params the parameters specifying which reports to include and their configurations
	 * @param request the HTTP request containing tenant-specific information
	 * @return a response container containing the generated combined report as a byte array
	 */
	@PostMapping(value = ApiPathConstant.ORDER_BOOKING_COMBINE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ORDER_BOOKING_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<byte[]> findAllCombinedReport(@PathVariable(name = FieldConstants.COMMON_FIELD_ID) Long orderId, @RequestBody CombineReportRequestParmDto params, HttpServletRequest request) {
		FileBean file = combinedReportQueryService.generateCombineReport(orderId, params, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

}