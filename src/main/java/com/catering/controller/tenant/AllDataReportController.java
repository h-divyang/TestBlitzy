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
import com.catering.constant.FieldConstants;
import com.catering.constant.SwaggerConstant;
import com.catering.dao.all_data_reports.AllDataReportsNativeQueryService;
import com.catering.dto.ResponseContainerDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.util.RequestResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Controller that provides end points for generating and retrieving various types of all data reports and drop down data.
 * It integrates with the AllDataReportsNativeQueryService to fetch data and generate reports.
 */
@RestController
@RequestMapping(value = ApiPathConstant.ALL_DATA_REPORT)
@Tag(name = SwaggerConstant.ALL_DATA_REPORTS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AllDataReportController {

	/**
	 * A service instance used to handle all data report queries at a native query level.
	 */
	AllDataReportsNativeQueryService allDataReportsNativeQueryService;

	/**
	 * Generates a catalogue report for menu items.
	 * @param templateId The template id as an integer representing type of theme
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @param request The HttpServletRequest object containing the request details.
	 * @return A ResponseContainerDto containing a byte array of the generated report file. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.CATALOGUE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ALL_DATA_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> menuItemCatalogueReport(@RequestParam(name = FieldConstants.TEMPLATE_ID) Integer templateId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = allDataReportsNativeQueryService.generateMenuItemCatalogueReport(templateId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Generates a menu item menu report.
	 *
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @return A ResponseContainerDto containing a byte array of the generated report file. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.MENU_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ALL_DATA_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> menuItemMenuReport(@RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName) {
		FileBean file = allDataReportsNativeQueryService.generateMenuItemMenuReport(langType, langCode, reportName);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves the drop down data for raw material categories.
	 *
	 * @return A ResponseContainerDto containing a list of DateWiseReportDropDownCommonDto.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_CATEGORY_DROP_DOWN)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ALL_DATA_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getRawMaterialCategoryDropDownData() {
		return RequestResponseUtils.generateResponseDto(allDataReportsNativeQueryService.getRawMaterialReportDropDownData());
	}

	/**
	 * Generates a raw material report based on the specified category and language parameters.
	 *
	 * @param rawMaterialCategoryId The ID of the raw material category for which the report is to be generated.
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @return A ResponseContainerDto containing a byte array of the generated report file. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ALL_DATA_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> rawMaterialReport(@RequestParam(name = FieldConstants.RAW_MATERIAL_CATEGORY_ID) Long rawMaterialCategoryId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName) {
		FileBean file = allDataReportsNativeQueryService.generateRawMaterialsReport(rawMaterialCategoryId, langType, langCode, reportName);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves drop down data of raw material for generating menu item-wise raw material report.
	 *
	 * @return A ResponseContainerDto containing a list of DateWiseReportDropDownCommonDto.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ITEM_WISE_RAW_MATERIAL_REPORT_DROPDOWN)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ALL_DATA_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getMenuItemWiseRawMaterialReportDropDownData() {
		return RequestResponseUtils.generateResponseDto(allDataReportsNativeQueryService.getMenuItemWiseRawMaterialReportDropDownData());
	}

	/**
	 * Generates a menu item-wise raw material report.
	 *
	 * @param rawMaterialId The ID of the raw material for which the report is to be generated.
	 * @param langType The language type as an integer representing the language type for the report.
	 * @param langCode The language code as a string representing the language code for the report.
	 * @return A ResponseContainerDto containing a byte array of the generated report file. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ITEM_WISE_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ALL_DATA_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> getMenuItemWiseRawMaterialReport(@RequestParam(name = FieldConstants.RAW_MATERIAL_ID) Long rawMaterialId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName) {
		FileBean file = allDataReportsNativeQueryService.getMenuItemWiseRawMaterialReport(rawMaterialId, langType, langCode, reportName);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves drop down data for menu items to be used in generating menu item wise quantity raw material report.
	 *
	 * @return A ResponseContainerDto containing a list of DateWiseReportDropDownCommonDto.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ITEM_WISE_QUANTITY_RAW_MATERIAL_REPORT_DROPDOWN)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ALL_DATA_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getMenuItemWiseQuantityRawMaterialReportDropdown() {
		return RequestResponseUtils.generateResponseDto(allDataReportsNativeQueryService.getMenuItemWiseQuantityRawMaterialReportDropdown());
	}

	/**
	 * Generates a report for menu items-wise quantity raw material details based on the specified parameters.
	 *
	 * @param menuItemCategoryId The ID of the menu item category for which the report is to be generated.
	 * @param dataTypeId An array of data type IDs specifying the types of data to include in the report.
	 * @param langType The language type as an integer representing the language configuration for the report.
	 * @param langCode The language code as a string representing the specific language for the report.
	 * @return A ResponseContainerDto containing a byte array of the generated report file. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.MENU_ITEM_WISE_QUANTITY_RAW_MATERIAL_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ALL_DATA_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> getMenuItemWiseQuantityRawMaterialReport(@RequestParam(name = FieldConstants.MENU_ITEM_CATEGORY_ID) Long menuItemCategoryId, @RequestParam(name = FieldConstants.DATA_TYPE_ID) Long[] dataTypeId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName) {
		FileBean file = allDataReportsNativeQueryService.generateMenuItemWiseQuantityRawMaterialReport(menuItemCategoryId, dataTypeId, langType, langCode, reportName);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

	/**
	 * Retrieves drop down data for custom packages.
	 *
	 * @return A ResponseContainerDto containing a list of DateWiseReportDropDownCommonDto.
	 */
	@GetMapping(value = ApiPathConstant.CUSTOM_PACKAGE_DROP_DOWN)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ALL_DATA_REPORTS + ApiUserRightsConstants.CAN_VIEW})
	public ResponseContainerDto<List<DateWiseReportDropDownCommonDto>> getCustomPackageDropDownData() {
		return RequestResponseUtils.generateResponseDto(allDataReportsNativeQueryService.getCustomPackageDropDownData());
	}

	/**
	 * Generates a report for a custom package based on the specified parameters.
	 *
	 * @param customPackageId The ID of the custom package for which the report is to be generated.
	 * @param langType The language type as an integer representing the language configuration for the report.
	 * @param langCode The language code as a string representing the specific language for the report.
	 * @param request The HttpServletRequest object containing the request details.
	 * @return A ResponseContainerDto containing a byte array of the generated report file. Returns null if no data is available.
	 */
	@GetMapping(value = ApiPathConstant.CUSTOM_PACKAGE_REPORT)
	@AuthorizeUserRights(value = {ApiUserRightsConstants.ALL_DATA_REPORTS + ApiUserRightsConstants.CAN_PRINT})
	public ResponseContainerDto<byte[]> generateCustomPackageReport(@RequestParam(name = FieldConstants.CUSTOM_PACKAGE_ID) Long customPackageId, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_TYPE, required = false) Integer langType, @RequestParam(name = FieldConstants.COMMON_FIELD_LANG_CODE, required = false) String langCode, @RequestParam(name = FieldConstants.REPORT_NAME, required = false) String reportName, HttpServletRequest request) {
		FileBean file = allDataReportsNativeQueryService.generateCustomPackageReport(customPackageId, langType, langCode, reportName, request);
		return RequestResponseUtils.generateResponseDto(Objects.nonNull(file) ? file.getFile() : null);
	}

}