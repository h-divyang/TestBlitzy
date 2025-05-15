package com.catering.dao.raw_material_allocation;

import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.RawMaterialAllocationDto;
import com.catering.dto.tenant.request.RawMaterialAllocationMenuItemDto;
import com.catering.dto.tenant.request.RawMaterialAllocationRawMaterialDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * This class represents a RawMaterialAllocation entity with native queries.
 *
 * The intention is to handle raw material allocation queries efficiently and map results to the respective DTOs.
 * 
 * The native queries used in this class are designed to fetch specific information about raw material allocations 
 * related to customer orders and menu items. The `@NamedNativeQuery` annotations define the SQL queries 
 * that are executed to retrieve this data, and the result is mapped using `@SqlResultSetMapping` to populate 
 * the appropriate DTOs (Data Transfer Objects).
 */
@NamedNativeQuery(
	name = "findItemCategoryByOrderId",
	resultSetMapping = "findItemCategoryByOrderIdResult",
	query = "SELECT "
		+ "rmc.id AS rawMaterialCategoryId, "
		+ "rmc.name_default_lang AS rawMaterialCategoryNameDefaultLang, "
		+ "rmc.name_prefer_lang AS rawMaterialCategoryNamePreferLang, "
		+ "rmc.name_supportive_lang AS rawMaterialCategoryNameSupportiveLang, "
		+ "rmc.fk_raw_material_category_type_id AS rawMaterialCategoryTypeId "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function ofu ON ofu.id = omp.fk_order_function_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id ELSE rma.fk_raw_material_id END "
		+ "INNER JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "WHERE ofu.fk_customer_order_details_id = :orderId AND rmc.is_direct_order AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "GROUP BY rawMaterialCategoryId "
		+ "ORDER BY rmc.priority, rawMaterialCategoryId"
)

@SqlResultSetMapping(
	name = "findItemCategoryByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = RawMaterialAllocationDto.class,
		columns = {
			@ColumnResult(name = "rawMaterialCategoryId", type = Long.class),
			@ColumnResult(name = "rawMaterialCategoryNameDefaultLang", type = String.class),
			@ColumnResult(name = "rawMaterialCategoryNamePreferLang", type = String.class),
			@ColumnResult(name = "rawMaterialCategoryNameSupportiveLang", type = String.class),
			@ColumnResult(name = "rawMaterialCategoryTypeId", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "findRawMaterialByOrderIdAndItemCategoryId",
	resultSetMapping = "findRawMaterialByOrderIdAndItemCategoryIdResult",
	query = "SELECT "
		+ "t.rawMaterialId, "
		+ "t.rawMaterialNameDefaultLang, "
		+ "t.rawMaterialNamePreferLang, "
		+ "t.rawMaterialNameSupportiveLang, "
		+ "t.rawMaterialCategoryId, "
		+ "t.supplierRate, "
		+ "t.supplierMeasurementId, "
		+ "t.actualQty, "
		+ "t.actualQtyMeasurementId, "
		+ "t.finalQty, "
		+ "t.finalQtyMeasurementId, "
		+ "t.supplierM_isBaseUnit, "
		+ "t.supplierM_baseUnitEquivalent, "
		+ "t.orderRMfinalMeasurementId, "
		+ "ROUND(getSmallestMeasurementValue(t.finalQty, m.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(t.supplierRate, t.supplierMeasurementId, FALSE, TRUE), '$.value')), 2) AS total, "
		+ "t.agencyId,"
		+ "t.godownId "
		+ "FROM (SELECT "
		+ "rm.id AS rawMaterialId, "
		+ "rm.name_default_lang AS rawMaterialNameDefaultLang, "
		+ "rm.name_prefer_lang AS rawMaterialNamePreferLang, "
		+ "rm.name_supportive_lang AS rawMaterialNameSupportiveLang, "
		+ "rm.fk_raw_material_category_id AS rawMaterialCategoryId, "
		+ "rm.supplier_rate AS supplierRate , "
		+ "rm.fk_measurement_id AS supplierMeasurementId, "
		+ "supplierM.is_base_unit AS supplierM_isBaseUnit, "
		+ "supplierM.base_unit_equivalent AS supplierM_baseUnitEquivalent, "
		+ "rma.fk_final_measurement_id AS orderRMfinalMeasurementId, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(rma.actual_qty, actualM.id)), getSmallestMeasurementId(actualM.id), IF(:isAdjustQuantity, 1, 0)) AS actualQty, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(rma.actual_qty, actualM.id)), getSmallestMeasurementId(actualM.id), IF(:isAdjustQuantity, 1, 0)) AS actualQtyMeasurementId, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)) + IFNULL(getSmallestMeasurementValue(rmae.quantity, rmae.fk_measurement_id), 0), getSmallestMeasurementId(finalM.id), 0) AS finalQty, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)) + IFNULL(getSmallestMeasurementValue(rmae.quantity, rmae.fk_measurement_id), 0), getSmallestMeasurementId(finalM.id), 0) AS finalQtyMeasurementId, "
		+ "SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(rm.supplier_rate, rm.fk_measurement_id, FALSE, TRUE), '$.value'))) AS total, "
		+ "rma.fk_contact_agency_id AS agencyId,"
		+ "rma.fk_godown_id AS godownId "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function ofu ON ofu.id = omp.fk_order_function_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id ELSE rma.fk_raw_material_id END "
		+ "LEFT JOIN measurement actualM ON actualM.id = rma.fk_actual_measurement_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ "LEFT JOIN measurement supplierM ON supplierM.id = rm.fk_measurement_id "
		+ "LEFT JOIN raw_material_allocation_extra rmae ON rmae.fk_raw_material_id = rm.id AND rmae.fk_customer_order_details_id = :orderId "
		+ "LEFT JOIN raw_material_supplier rms ON rms.fk_raw_material_id = rm.id AND rms.is_default "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE ofu.fk_customer_order_details_id = :orderId AND rm.fk_raw_material_category_id = :rawMaterialCategoryId AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "GROUP BY rawMaterialId "
		+ "ORDER BY rawMaterialId) as t "
		+ "LEFT JOIN measurement m ON m.id = t.finalQtyMeasurementId"
)

@SqlResultSetMapping(
	name = "findRawMaterialByOrderIdAndItemCategoryIdResult",
	classes = @ConstructorResult(
		targetClass = RawMaterialAllocationRawMaterialDto.class,
		columns = {
			@ColumnResult(name = "rawMaterialId", type = Long.class),
			@ColumnResult(name = "rawMaterialNameDefaultLang", type = String.class),
			@ColumnResult(name = "rawMaterialNamePreferLang", type = String.class),
			@ColumnResult(name = "rawMaterialNameSupportiveLang", type = String.class),
			@ColumnResult(name = "rawMaterialCategoryId", type = Long.class),
			@ColumnResult(name = "supplierRate", type = Double.class),
			@ColumnResult(name = "supplierMeasurementId", type = Long.class),
			@ColumnResult(name = "actualQty", type = Double.class),
			@ColumnResult(name = "actualQtyMeasurementId", type = Long.class),
			@ColumnResult(name = "finalQty", type = Double.class),
			@ColumnResult(name = "finalQtyMeasurementId", type = Long.class),
			@ColumnResult(name = "total", type = Double.class),
			@ColumnResult(name = "agencyId", type = Long.class),
			@ColumnResult(name = "godownId", type = Long.class),
			@ColumnResult(name = "orderRMfinalMeasurementId", type = Long.class),
			@ColumnResult(name = "supplierM_isBaseUnit", type = Boolean.class),
			@ColumnResult(name = "supplierM_baseUnitEquivalent", type = Double.class),
		}
	)
)

@NamedNativeQuery(
name = "findMenuItemByOrderIdAndRawMaterialId",
resultSetMapping = "findMenuItemByOrderIdAndRawMaterialIdResult",
query = "SELECT "
		+ "rmae.id AS rawMaterialAllocationId, "
		+ "rm.id AS rawMaterialId, "
		+ "NULL AS functionId, "
		+ "NULL AS functionNameDefaultLang, "
		+ "NULL AS functionNamePreferLang, "
		+ "NULL AS functionNameSupportiveLang, "
		+ "NULL AS menuItemId, "
		+ "NULL AS itemNameDefaultLang, "
		+ "NULL AS itemNamePreferLang, "
		+ "NULL AS itemNameSupportiveLang, "
		+ "c.id AS agencyId, "
		+ "cc.id AS agencyTypeId, "
		+ "rmae.quantity AS finalQty, "
		+ "rmae.fk_measurement_id AS finalQtyMeasurementId, "
		+ "rma.order_time AS functionTime, "
		+ "rmae.total AS total, "
		+ "TRUE as isExtra, "
		+ "rmc.fk_raw_material_category_type_id AS rawMaterialCategoryTypeId, "
		+ "g.id AS godown "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function ofu ON ofu.id = omp.fk_order_function_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id ELSE rma.fk_raw_material_id END "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "LEFT JOIN raw_material_allocation_extra rmae ON rmae.fk_raw_material_id = rm.id AND rmae.fk_customer_order_details_id = ofu.fk_customer_order_details_id "
		+ "LEFT JOIN contact c ON c.id = CASE WHEN rmae.fk_contact_agency_id  IS NOT NULL THEN rmae.fk_contact_agency_id ELSE rma.fk_contact_agency_id END "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "LEFT JOIN godown g ON g.id = rmae.fk_godown_id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE ofu.fk_customer_order_details_id = :orderId AND rmc.id = :rawMaterialCategoryId AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "GROUP BY rm.id "
		+ "UNION ALL "
		+ "SELECT "
		+ "rma.id AS rawMaterialAllocationId, "
		+ "rm.id AS rawMaterialId, "
		+ "ft.id AS functionId, "
		+ "ft.name_default_lang AS functionNameDefaultLang, "
		+ "ft.name_prefer_lang AS functionNamePreferLang, "
		+ "ft.name_supportive_lang AS functionNameSupportiveLang, "
		+ "mi.id AS menuItemId, "
		+ "mi.name_default_lang AS itemNameDefaultLang, "
		+ "mi.name_prefer_lang AS itemNamePreferLang, "
		+ "mi.name_supportive_lang AS itemNameSupportiveLang, "
		+ "c.id AS agencyId, "
		+ "cc.id AS agencyTypeId, "
		+ "rma.final_qty AS finalQty, "
		+ "rma.fk_final_measurement_id AS finalQtyMeasurementId, "
		+ "rma.order_time AS functionTime, "
		+ "ROUND(getSmallestMeasurementValue(rma.final_qty, finalM.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(rm.supplier_rate, rm.fk_measurement_id, FALSE, TRUE), '$.value')), 2) AS total, "
		+ "FALSE as isExtra, "
		+ "rmc.fk_raw_material_category_type_id AS rawMaterialCategoryTypeId, "
		+ "rma.fk_godown_id AS godown "
		+ "FROM menu_item mi "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_item_id = mi.id "
		+ "INNER JOIN raw_material_allocation rma ON rma.fk_menu_preparation_menu_item_id = ompmi.id  "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function ofu ON ofu.id = omp.fk_order_function_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id ELSE rma.fk_raw_material_id END "
		+ "LEFT JOIN function_type ft ON ft.id = ofu.fk_function_type_id "
		+ "LEFT JOIN contact c ON c.id = rma.fk_contact_agency_id "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ "LEFT JOIN measurement supplierM ON supplierM.id = rm.fk_measurement_id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE ofu.fk_customer_order_details_id = :orderId AND rmc.id = :rawMaterialCategoryId AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "GROUP BY rma.id"
)

@SqlResultSetMapping(
	name = "findMenuItemByOrderIdAndRawMaterialIdResult",
	classes = @ConstructorResult(
		targetClass = RawMaterialAllocationMenuItemDto.class,
		columns = {
			@ColumnResult(name = "rawMaterialAllocationId", type = Long.class),
			@ColumnResult(name = "rawMaterialId", type = Long.class),
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "functionNameDefaultLang", type = String.class),
			@ColumnResult(name = "functionNamePreferLang", type = String.class),
			@ColumnResult(name = "functionNameSupportiveLang", type = String.class),
			@ColumnResult(name = "menuItemId", type = Long.class),
			@ColumnResult(name = "itemNameDefaultLang", type = String.class),
			@ColumnResult(name = "itemNamePreferLang", type = String.class),
			@ColumnResult(name = "itemNameSupportiveLang", type = String.class),
			@ColumnResult(name = "agencyId", type = Long.class),
			@ColumnResult(name = "agencyTypeId", type = Long.class),
			@ColumnResult(name = "finalQty", type = Double.class),
			@ColumnResult(name = "finalQtyMeasurementId", type = Long.class),
			@ColumnResult(name = "functionTime", type = LocalDateTime.class),
			@ColumnResult(name = "total", type = Double.class),
			@ColumnResult(name = "isExtra", type = Boolean.class),
			@ColumnResult(name = "rawMaterialCategoryTypeId", type = Long.class),
			@ColumnResult(name = "godown", type = Long.class)
		}
	)
)

@Entity
public class RawMaterialAllocationNativeQuery extends AuditIdModelOnly {
}