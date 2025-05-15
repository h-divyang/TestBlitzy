package com.catering.dao.all_data_reports;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.AllDataReportMenuItemReportDto;
import com.catering.dto.tenant.request.AllDataReportRawMaterialReportDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.MenuItemWiseQuantityRawMaterialReportDto;
import com.catering.dto.tenant.request.PackageReportDto;

/**
 * The AllDataReportsNativeQueryDao interface defines the repository methods for 
 * executing native SQL queries related to various reporting data. The methods in this 
 * interface are used to fetch specific report data from the database using custom 
 * native SQL queries, as defined in the `AllDataReportsNativeQuery` entity class.
 * 
 * This interface extends JpaRepository, providing CRUD functionality along with custom
 * query methods for generating reports.
 */
public interface AllDataReportsNativeQueryDao extends JpaRepository<AllDataReportsNativeQuery, Long> {

	/**
	 * Generates a menu item report based on the specified language type.
	 * 
	 * @param langType The language type to filter the report data.
	 * @return A list of `AllDataReportMenuItemReportDto` containing the menu item report data.
	 */
	@Query(name = "getMenuItemReportData", nativeQuery = true)
	List<AllDataReportMenuItemReportDto> generateMenuItemReport(Integer langType);

	/**
	 * Retrieves the dropdown data for raw materials in the report.
	 * 
	 * @return A list of `DateWiseReportDropDownCommonDto` containing dropdown data for raw materials.
	 */
	@Query(name = "getRawMaterialsReportDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getRawMaterialReportDropDownData();

	/**
	 * Generates a raw material report filtered by the raw material category and language type.
	 * 
	 * @param rawMaterialCategoryId The ID of the raw material category.
	 * @param langType The language type to filter the report data.
	 * @return A list of `AllDataReportRawMaterialReportDto` containing the raw material report data.
	 */
	@Query(name = "getRawMaterialsReportData", nativeQuery = true)
	List<AllDataReportRawMaterialReportDto> generateRawMaterialReport(Long rawMaterialCategoryId, Integer langType);

	/**
	 * Retrieves the dropdown data for menu item-wise raw material reports.
	 * 
	 * @return A list of `DateWiseReportDropDownCommonDto` containing dropdown data for menu item-wise raw materials.
	 */
	@Query(name = "getRawMaterialReportDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getMenuItemWiseRawMaterialReportDropDownData();

	/**
	 * Retrieves the raw material report data filtered by the raw material ID and language type for menu item-wise raw materials.
	 * 
	 * @param rawMaterialId The ID of the raw material.
	 * @param langType The language type to filter the report data.
	 * @return A list of `AllDataReportRawMaterialReportDto` containing the raw material report data for the menu item.
	 */
	@Query(name = "getRawMaterialReportResult", nativeQuery = true)
	List<AllDataReportRawMaterialReportDto> getMenuItemWiseRawMaterialReport(Long rawMaterialId, Integer langType);

	/**
	 * Retrieves the dropdown data for the menu item-wise quantity raw material report.
	 * 
	 * @return A list of `DateWiseReportDropDownCommonDto` containing dropdown data for menu item-wise quantity raw materials.
	 */
	@Query(name = "getMenuItemWiseQuantityRawMaterialReportDropdown", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getMenuItemWiseQuantityRawMaterialReportDropdown();

	/**
	 * Generates the menu item-wise quantity raw material report based on the category ID, data type IDs, and language type.
	 * 
	 * @param menuItemWiseQuantityRawMaterialCategoryId The category ID for menu item-wise quantity raw material.
	 * @param dataTypeIds A list of data type IDs to filter the report data.
	 * @param langType The language type to filter the report data.
	 * @return A list of `MenuItemWiseQuantityRawMaterialReportDto` containing the generated report data.
	 */
	@Query(name = "getMenuItemWiseQuantityRawMaterialReportData", nativeQuery = true)
	List<MenuItemWiseQuantityRawMaterialReportDto> generateMenuItemWiseQuantityRawMaterialReport(Long menuItemWiseQuantityRawMaterialCategoryId, List<Long> dataTypeIds, Integer langType);

	/**
	 * Retrieves the dropdown data for custom package reports.
	 * 
	 * @return A list of `DateWiseReportDropDownCommonDto` containing dropdown data for custom packages.
	 */
	@Query(name = "getCustomPackageReportDropDownData", nativeQuery = true)
	List<DateWiseReportDropDownCommonDto> getCustomPackageReportDropDownData();

	/**
	 * Generates a custom package report based on the provided custom package ID and language type.
	 * 
	 * @param customPackageId The ID of the custom package.
	 * @param langType The language type to filter the report data.
	 * @return A list of `PackageReportDto` containing the generated custom package report data.
	 */
	@Query(name = "getCustomPackageReportData", nativeQuery = true)
	List<PackageReportDto> generatePackageReport(Long customPackageId, Integer langType);

}