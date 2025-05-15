package com.catering.dao.all_data_reports;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.AllDataReportMenuItemReportDto;
import com.catering.dto.tenant.request.AllDataReportRawMaterialReportDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.MenuItemWiseQuantityRawMaterialReportDto;
import com.catering.dto.tenant.request.PackageReportDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * The AllDataReportsNativeQuery class serves as the entity for holding the results
 * of various native SQL queries related to reporting data in the system. This class is
 * annotated with multiple `@NamedNativeQuery` and `@SqlResultSetMapping` annotations
 * to handle custom SQL queries and map the result sets to specific DTOs.
 * 
 * These queries are used to fetch data for various reports like:
 * - Menu Item Report
 * - Raw Materials Report
 * - Menu Item Wise Quantity Raw Material Report
 * - Custom Package Report
 * 
 * The `NamedNativeQuery` annotations define the SQL queries that are executed directly 
 * on the database, with dynamic language selection based on the `langType` parameter 
 * (which determines whether to fetch data in the preferred, supportive, or default language).
 * 
 * The `@SqlResultSetMapping` annotations define how the results of the SQL queries 
 * are mapped to specific DTO classes, such as `AllDataReportMenuItemReportDto`,
 * `DateWiseReportDropDownCommonDto`, `AllDataReportRawMaterialReportDto`, 
 * `MenuItemWiseQuantityRawMaterialReportDto`, and `PackageReportDto`.
 * 
 * This class extends `AuditIdModelOnly`, indicating that it inherits audit fields like
 * created and updated timestamps, as per the application's audit requirements.
 */
@NamedNativeQuery(
	name = "getMenuItemReportData",
	resultSetMapping = "getMenuItemReportDataResult",
	query = "SELECT "
		+ "CASE "
		+ " WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ " WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ " ELSE mic.name_default_lang "
		+ "END AS menuItemCategory, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ " WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ " ELSE mi.name_default_lang "
		+ "END AS menuItemName "
		+ "FROM menu_item mi "
		+ "INNER JOIN menu_item_category mic ON mic.id = mi.fk_menu_item_category_id "
		+ "WHERE mi.is_active = TRUE "
		+ "ORDER BY mic.id, mi.id"
)

@SqlResultSetMapping(
	name = "getMenuItemReportDataResult",
	classes = @ConstructorResult(
		targetClass = AllDataReportMenuItemReportDto.class,
		columns = {
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "menuItemName", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getRawMaterialsReportDropDownData",
	resultSetMapping = "getRawMaterialsReportDropDownDataResult",
	query = "SELECT "
		+ "rmc.id AS id, "
		+ "rmc.name_default_lang AS nameDefaultLang, "
		+ "rmc.name_prefer_lang AS namePreferLang, "
		+ "rmc.name_supportive_lang AS nameSupportiveLang "
		+ "FROM raw_material_category rmc "
		+ "WHERE rmc.is_active = TRUE"
)

@NamedNativeQuery(
	name = "getRawMaterialReportDropDownData",
	resultSetMapping = "getRawMaterialsReportDropDownDataResult",
	query = "SELECT "
		+ "rm.id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "rm.name_prefer_lang AS namePreferLang, "
		+ "rm.name_supportive_lang AS nameSupportiveLang "
		+ "FROM raw_material rm "
		+ "WHERE rm.is_active = TRUE"
)

@NamedNativeQuery(
	name = "getMenuItemWiseQuantityRawMaterialReportDropdown",
	resultSetMapping = "getRawMaterialsReportDropDownDataResult",
	query = "SELECT "
		+ "mic.id AS id, "
		+ "mic.name_default_lang AS nameDefaultLang, "
		+ "mic.name_prefer_lang AS namePreferLang, "
		+ "mic.name_supportive_lang AS nameSupportiveLang "
		+ "FROM menu_item_category mic "
		+ "WHERE mic.is_active = TRUE"
)

@SqlResultSetMapping(
	name = "getRawMaterialsReportDropDownDataResult",
	classes = @ConstructorResult(
		targetClass = DateWiseReportDropDownCommonDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getRawMaterialsReportData",
	resultSetMapping = "getRawMaterialsReportDataResult",
	query = "SELECT "
		+ "ROW_NUMBER() OVER() AS id, "
		+ "CASE "
		+ " WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ " WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ " ELSE rmc.name_default_lang "
		+ "END AS rawMaterialCategory, "
		+ "CASE "
		+ " WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ " WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ " ELSE rm.name_default_lang "
		+ "END AS rawMaterial "
		+ "FROM raw_material_category rmc "
		+ "INNER JOIN raw_material rm ON rm.fk_raw_material_category_id = rmc.id "
		+ "WHERE rm.is_active = TRUE AND rmc.is_active = TRUE AND IF(0 = :rawMaterialCategoryId , TRUE, rmc.id = :rawMaterialCategoryId) "
		+ "ORDER BY rmc.id, rm.id"
)

@NamedNativeQuery(
	name = "getRawMaterialReportResult",
	resultSetMapping = "getRawMaterialsReportDataResult",
	query = "SELECT "
		+ "'' AS id, "
		+ "CASE "
		+ " WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ " WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ " ELSE rm.name_default_lang "
		+ "END AS rawMaterialCategory, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ " WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ " ELSE mi.name_default_lang "
		+ "END AS rawMaterial "
		+ "FROM raw_material rm "
		+ "INNER JOIN menu_item_raw_material mirm ON mirm.fk_raw_material_id = rm.id "
		+ "INNER JOIN menu_item mi ON mi.id = mirm.fk_menu_item_id  "
		+ "WHERE rm.is_active = TRUE AND IF(0 = :rawMaterialId , TRUE, rm.id = :rawMaterialId)"
		+ "ORDER BY rm.id, mi.id"
)

@SqlResultSetMapping(
	name = "getRawMaterialsReportDataResult",
	classes = @ConstructorResult(
		targetClass = AllDataReportRawMaterialReportDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "rawMaterialCategory", type = String.class),
			@ColumnResult(name = "rawMaterial", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getMenuItemWiseQuantityRawMaterialReportData",
	resultSetMapping = "getMenuItemWiseQuantityRawMaterialReportDataResult",
	query = "SELECT "
		+ "CASE "
		+ " WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ " WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ " ELSE mic.name_default_lang "
		+ "END AS menuItemCategory, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ " WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ " ELSE mi.name_default_lang "
		+ "END AS menuItem, "
		+ "CASE "
		+ " WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ " WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ " ELSE rm.name_default_lang "
		+ "END AS rawMaterial, "
		+ "CASE "
		+ " WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ " WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ " ELSE rmc.name_default_lang "
		+ "END AS rawMaterialCategory, "
		+ "mirm.weight AS qty, "
		+ "CASE "
		+ "WHEN (m.decimal_limit_qty = -1) AND (mirm.weight % 1 = 0) AND (m.id = 1 Or m.id = 3) THEN 0 "
		+ "WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "CASE "
		+ " WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ " WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ " ELSE m.symbol_default_lang "
		+ "END AS measurement "
		+ "FROM menu_item_category mic "
		+ "INNER JOIN menu_item mi ON mi.fk_menu_item_category_id = mic.id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.fk_menu_item_id = mi.id "
		+ "LEFT JOIN raw_material rm ON rm.id = mirm.fk_raw_material_id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "LEFT JOIN measurement m ON m.id = mirm.fk_measurement_id "
		+ "WHERE mic.is_active = TRUE AND IF(0 = :menuItemWiseQuantityRawMaterialCategoryId , TRUE, mic.id = :menuItemWiseQuantityRawMaterialCategoryId) AND IF ( 0 IN (:dataTypeIds), TRUE, IF (1 IN (:dataTypeIds), rm.id IS NOT NULL, IF (2 IN (:dataTypeIds), rm.id IS NULL, FALSE))) "
		+ "ORDER BY mic.id, mi.id, mirm.id, rmc.id "
)

@SqlResultSetMapping(
	name = "getMenuItemWiseQuantityRawMaterialReportDataResult",
	classes = @ConstructorResult(
		targetClass = MenuItemWiseQuantityRawMaterialReportDto.class,
		columns = {
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "menuItem", type = String.class),
			@ColumnResult(name = "rawMaterial", type = String.class),
			@ColumnResult(name = "rawMaterialCategory", type = String.class),
			@ColumnResult(name = "qty", type = Double.class),
			@ColumnResult(name = "measurement", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getCustomPackageReportDropDownData",
	resultSetMapping = "getRawMaterialsReportDropDownDataResult",
	query = "SELECT "
		+ " cp.id, "
		+ " cp.name_default_lang AS nameDefaultLang, "
		+ " cp.name_prefer_lang AS namePreferLang, "
		+ " cp.name_supportive_lang AS nameSupportiveLang "
		+ " FROM custom_package cp "
		+ " WHERE cp.is_active = TRUE "
)

@NamedNativeQuery(
	name = "getCustomPackageReportData",
	resultSetMapping = "getCustomPackageReportDataResult",
	query = "SELECT "
			+ "CASE "
			+ " WHEN :langType = 1 AND cp.name_prefer_lang IS NOT NULL AND cp.name_prefer_lang != '' THEN cp.name_prefer_lang "
			+ " WHEN :langType = 2 AND cp.name_supportive_lang IS NOT NULL AND cp.name_supportive_lang != '' THEN cp.name_supportive_lang "
			+ "ELSE cp.name_default_lang "
			+ "END AS customPackageName, "
			+ "pmic.no_of_items as noOfItems, "
			+ "cp.price AS customPackagePrice, "
			+ "CASE "
			+ " WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
			+ " WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
			+ "ELSE mic.name_default_lang "
			+ "END AS menuItemCategory, "
			+ "CASE "
			+ " WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
			+ " WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
			+ "ELSE mi.name_default_lang "
			+ "END AS menuItem "
			+ "FROM package_menu_item pmi "
			+ "INNER JOIN custom_package cp on cp.id = pmi.fk_custom_package_id "
			+ "INNER JOIN package_menu_item_category pmic ON pmic.fk_custom_package_id = pmi.fk_custom_package_id and pmic.fk_menu_item_category_id = pmi.fk_menu_item_category_id "
			+ "LEFT JOIN menu_item_category mic ON mic.id = pmi.fk_menu_item_category_id "
			+ "LEFT JOIN menu_item mi ON mi.id = pmi.fk_menu_item_id "
			+ "WHERE cp.id = :customPackageId OR :customPackageId = 0 AND cp.is_active = TRUE "
			+ "ORDER BY cp.id , pmi.menu_item_category_sequence, pmi.menu_item_sequence;"
)

@SqlResultSetMapping(
	name = "getCustomPackageReportDataResult",
	classes = @ConstructorResult(
		targetClass = PackageReportDto.class,
		columns = {
			@ColumnResult(name = "customPackageName", type = String.class),
			@ColumnResult(name = "noOfItems", type = Long.class),
			@ColumnResult(name = "customPackagePrice", type = Double.class),
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "menuItem", type = String.class)
		}
	)
)

@Entity
public class AllDataReportsNativeQuery extends AuditIdModelOnly {
}