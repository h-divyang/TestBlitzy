package com.catering.dao.order_reports.order_general_fix_and_crockery_allocation;

import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.CrockeryWithQuantityReportDto;
import com.catering.dto.tenant.request.CrockeryWithoutQuantityReportDto;
import com.catering.dto.tenant.request.EventDistributionNotesDto;
import com.catering.dto.tenant.request.EventMenuReportWithCrockeryReportWithQuantityDto;
import com.catering.dto.tenant.request.OrderGeneralFixReportWithQuantityDto;
import com.catering.dto.tenant.request.OrderGeneralFixReportWithoutQuantityDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing the Order General Fix and Crockery Allocation Report query.
 * This class extends {@link AuditIdModelOnly} and is used for persisting data related to
 * the general fix and crockery allocation for orders in the database.
 * The class serves as a data model for reports, and it inherits audit-related fields from
 * the {@link AuditIdModelOnly} class.
 * 
 * @see AuditIdModelOnly for inherited audit fields (e.g., created/updated timestamps).
 */
@NamedNativeQuery(
	name = "generateCrockeryWithQuantityReport",
	resultSetMapping = "generateCrockeryWithQuantityReportResult",
	query = "SELECT "
		+ "orderFunctionId, "
		+ "functionName, "
		+ "category, "
		+ "crockeryName, "
		+ "m.id, "
		+ "date, "
		+ "quantity, "
		+ "CASE  "
		+ "WHEN (m.decimal_limit_qty = -1) AND (quantity % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0  "
		+ "WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3  "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS quantityDecimalLimit, "
		+ "isMaxPerson "
		+ "from (SELECT "
		+ "functionName AS functionName, "
		+ "orderFunctionId AS orderFunctionId, "
		+ "category AS category, "
		+ "crockeryName AS crockeryName, "
		+ "date AS date, "
		+ "function_sequence AS function_sequence, "
		+ "rmc_priority AS rmc_priority, "
		+ "rmc_id AS rmc_id,  "
		+ "rm_priority AS rm_priority,  "
		+ "rm_id AS rm_id, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS finalM_id, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS quantity,  "
		+ "quantityDecimalLimit AS quantityDecimalLimit, "
		+ ":isMaxPerson AS isMaxPerson "
		+ "from ((SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND fm.name_prefer_lang IS NOT NULL AND fm.name_prefer_lang != '' THEN fm.name_prefer_lang "
		+ "WHEN :langType = 2 AND fm.name_supportive_lang IS NOT NULL AND fm.name_supportive_lang != '' THEN fm.name_supportive_lang "
		+ "ELSE fm.name_default_lang "
		+ "END AS functionName, "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "rm.id AS crockeryId, "
		+ "ofn.date AS date, "
		+ "ofn.`sequence` AS function_sequence,  "
		+ "oc.qty AS quantity,  "
		+ "rmc.priority AS rmc_priority,  "
		+ "rmc.id AS rmc_id, "
		+ "rm.priority AS rm_priority, "
		+ "rm.id AS rm_id, "
		+ "finalM.is_base_unit AS final_base_unit, "
		+ "finalM.base_unit_equivalent AS final_base_unit_equivalent, "
		+ "finalM.id AS finalM_id, "
		+ "finalM.base_unit_id AS final_base_unit_id, "
		+ "fk_raw_material_category_type_id AS raw_material_id, "
		+ "m.decimal_limit_qty AS quantityDecimalLimit "
		+ "FROM order_crockery oc "
		+ "INNER JOIN order_function ofn ON oc.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type fm ON ofn.fk_function_type_id = fm.id "
		+ "INNER JOIN raw_material rm ON oc.fk_raw_material_id = rm.id "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "LEFT JOIN measurement m ON m.id = oc.fk_measurement_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = oc.fk_measurement_id  "
		+ "WHERE "
		+ "rmc.fk_raw_material_category_type_id != 8 AND "
		+ "ofn.id = (SELECT ofun.id FROM order_function ofun INNER JOIN order_crockery oc ON oc.fk_order_function_id = ofun.id WHERE ofun.fk_customer_order_details_id = :orderId ORDER BY ofun.person DESC LIMIT 1) "
		+ "ORDER BY "
		+ "rmc.priority, rmc.id, rm.priority, rm.id ) "
		+ "UNION ALL "
		+ "(SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "of2.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "rm.id AS crockeryId, "
		+ "max_order_function.`date` AS date, "
		+ "max_order_function.`sequence` AS function_sequence, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS quantity, "
		+ "rmc.priority AS rmc_priority, "
		+ "rmc.id AS rmc_id, "
		+ "rm.priority AS rm_priority, "
		+ "rm.id AS rm_id, "
		+ "finalM.is_base_unit AS final_base_unit, "
		+ "finalM.base_unit_equivalent AS final_base_unit_equivalent, "
		+ "finalM.id AS finalM_id, "
		+ "finalM.base_unit_id AS final_base_unit_id, "
		+ "fk_raw_material_category_type_id AS raw_material_id, "
		+ "m.decimal_limit_qty AS quantityDecimalLimit "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function of2 ON of2.id = omp.fk_order_function_id "
		+ "LEFT JOIN menu_item_raw_material mirm on mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE "
		+ "WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id "
		+ "ELSE rma.fk_raw_material_id "
		+ "END "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "INNER JOIN menu_item_category mic ON mic.id  = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN measurement m ON m.id = rma.fk_actual_measurement_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ "INNER JOIN (SELECT * FROM order_function WHERE fk_customer_order_details_id = :orderId ORDER BY person DESC, id LIMIT 1) AS max_order_function "
		+ "INNER JOIN function_type ft ON max_order_function.fk_function_type_id  = ft.id "
		+ "WHERE of2.fk_customer_order_details_id = :orderId "
		+ "AND rmc.fk_raw_material_category_type_id = 3 GROUP BY rm.id)) combined_query "
		+ "JOIN measurement m on m.id = finalM_id "
		+ "GROUP BY crockeryId "
		+ "ORDER BY rmc_priority, rmc_id, rm_priority, rm_id ) combined_query2 "
		+ "JOIN measurement m on m.id = finalM_id "
		+ "ORDER BY function_sequence, rmc_priority, rmc_id, rm_priority, rm_id"
)

@NamedNativeQuery(
	name = "generateCrockeryWithQuantityWithoutMaxSettingReport",
	resultSetMapping = "generateCrockeryWithQuantityReportResult",
	query = "SELECT "
		+ "orderFunctionId, "
		+ "functionName, "
		+ "category, "
		+ "crockeryName, "
		+ "m.id, "
		+ "date, "
		+ "quantity, "
		+ "CASE  "
		+ "WHEN (m.decimal_limit_qty = -1) AND (quantity % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0  "
		+ "WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3  "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS quantityDecimalLimit, "
		+ "isMaxPerson "
		+ "FROM (SELECT "
		+ "functionName AS functionName, "
		+ "orderFunctionId AS orderFunctionId, "
		+ "category AS category, "
		+ "crockeryName AS crockeryName, "
		+ "date AS date, "
		+ "function_sequence AS function_sequence, "
		+ "rmc_priority AS rmc_priority, "
		+ "rmc_id AS rmc_id,  "
		+ "rm_priority AS rm_priority,  "
		+ "rm_id AS rm_id, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS finalM_id, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS quantity,  "
		+ "quantityDecimalLimit AS quantityDecimalLimit, "
		+ "NULL AS isMaxPerson "
		+ "from ((SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND fm.name_prefer_lang IS NOT NULL AND fm.name_prefer_lang != '' THEN fm.name_prefer_lang "
		+ "WHEN :langType = 2 AND fm.name_supportive_lang IS NOT NULL AND fm.name_supportive_lang != '' THEN fm.name_supportive_lang "
		+ "ELSE fm.name_default_lang "
		+ "END AS functionName, "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "rm.id AS crockeryId, "
		+ "ofn.date AS date, "
		+ "ofn.`sequence` AS function_sequence,  "
		+ "oc.qty AS quantity,  "
		+ "rmc.priority AS rmc_priority,  "
		+ "rmc.id AS rmc_id, "
		+ "rm.priority AS rm_priority, "
		+ "rm.id AS rm_id, "
		+ "finalM.is_base_unit AS final_base_unit, "
		+ "finalM.base_unit_equivalent AS final_base_unit_equivalent, "
		+ "finalM.id AS finalM_id, "
		+ "finalM.base_unit_id AS final_base_unit_id, "
		+ "fk_raw_material_category_type_id AS raw_material_id, "
		+ "m.decimal_limit_qty AS quantityDecimalLimit "
		+ "FROM order_crockery oc "
		+ "INNER JOIN order_function ofn ON oc.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type fm ON ofn.fk_function_type_id = fm.id "
		+ "INNER JOIN raw_material rm ON oc.fk_raw_material_id = rm.id "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "LEFT JOIN measurement m ON m.id = oc.fk_measurement_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = oc.fk_measurement_id  "
		+ "WHERE "
		+ "rmc.fk_raw_material_category_type_id != 8 AND "
		+ "ofn.fk_customer_order_details_id = :orderId "
		+ "ORDER BY "
		+ "rmc.priority, rmc.id, rm.priority, rm.id ) "
		+ "UNION ALL "
		+ "(SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName,"
		+ "of2.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "rm.id AS crockeryId, "
		+ "of2.`date` AS date , "
		+ "of2.`sequence` AS function_sequence,  "
		+ "rma.final_qty AS quantity,  "
		+ "rmc.priority AS rmc_priority,  "
		+ "rmc.id AS rmc_id, "
		+ "rm.priority AS rm_priority, "
		+ "rm.id AS rm_id, "
		+ "finalM.is_base_unit AS final_base_unit, "
		+ "finalM.base_unit_equivalent AS final_base_unit_equivalent, "
		+ "finalM.id AS finalM_id, "
		+ "finalM.base_unit_id AS final_base_unit_id, "
		+ "fk_raw_material_category_type_id AS raw_material_id, "
		+ "m.decimal_limit_qty AS quantityDecimalLimit "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi on ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp on omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function of2 ON of2.id = omp.fk_order_function_id "
		+ "INNER JOIN function_type ft on of2.fk_function_type_id  = ft.id "
		+ "LEFT JOIN menu_item_raw_material mirm on mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE "
		+ "WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id "
		+ "ELSE rma.fk_raw_material_id "
		+ "END "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "INNER JOIN menu_item_category mic on mic.id  = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN measurement m ON m.id = rma.fk_actual_measurement_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id  "
		+ "WHERE of2.fk_customer_order_details_id = :orderId  "
		+ "AND rmc.fk_raw_material_category_type_id = 3)) combined_query "
		+ "JOIN measurement m on m.id = finalM_id "
		+ "GROUP BY orderFunctionId, crockeryId "
		+ "ORDER BY rmc_priority, rmc_id, rm_priority, rm_id ) combined_query2 "
		+ "JOIN measurement m on m.id = finalM_id "
		+ "ORDER BY function_sequence, rmc_priority, rmc_id, rm_priority, rm_id "
)

@SqlResultSetMapping(
	name = "generateCrockeryWithQuantityReportResult",
	classes = @ConstructorResult(
		targetClass = CrockeryWithQuantityReportDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Integer.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "category", type = String.class),
			@ColumnResult(name = "crockeryName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "quantity", type = Double.class),
			@ColumnResult(name = "quantityDecimalLimit", type = Byte.class),
			@ColumnResult(name = "isMaxPerson", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateCrockeryReportWithQty",
	resultSetMapping = "generateCrockeryReportWithQtyResult",
	query = "SELECT "
		+ "NULL AS orderFunctionId, "
		+ "CASE "
		+ " WHEN :langType = 1 AND fm.name_prefer_lang IS NOT NULL AND fm.name_prefer_lang != '' THEN fm.name_prefer_lang "
		+ " WHEN :langType = 2 AND fm.name_supportive_lang IS NOT NULL AND fm.name_supportive_lang != '' THEN fm.name_supportive_lang "
		+ " ELSE fm.name_default_lang "
		+ "END AS functionName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ " WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ " ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ " WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ " WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ " ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "CASE "
		+ " WHEN TIME(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ " WHEN TIME(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ " WHEN TIME(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ " WHEN TIME(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ " ELSE '' "
		+ "END AS timeInWord, "
		+ "ofn.date AS date, "
		+ "oc.qty AS quantity, "
		+ "m.decimal_limit_qty AS quantityDecimalLimit, "
		+ "NULL AS isMaxPerson "
		+ "FROM order_crockery oc "
		+ "INNER JOIN order_function ofn ON oc.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type fm ON ofn.fk_function_type_id = fm.id "
		+ "INNER JOIN raw_material rm ON oc.fk_raw_material_id = rm.id "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "LEFT JOIN measurement m ON m.id = oc.fk_measurement_id "
		+ "WHERE "
		+ "rmc.fk_raw_material_category_type_id != 8 AND "
		+ "rm.is_active = true AND "
		+ " ofn.id = (SELECT ofun.id FROM order_function ofun INNER JOIN order_crockery oc ON oc.fk_order_function_id = ofun.id WHERE ofun.fk_customer_order_details_id = :orderId ORDER BY ofun.person DESC LIMIT 1) "
		+ "ORDER BY "
		+ "rmc.priority, rmc.id, rm.priority, rm.id "
)

@NamedNativeQuery(
	name = "generateCrockeryReportWithQtyWithoutMaxSetting",
	resultSetMapping = "generateCrockeryReportWithQtyResult",
	query = "SELECT "
		+ "NULL AS orderFunctionId, "
		+ "CASE "
		+ " WHEN :langType = 1 AND fm.name_prefer_lang IS NOT NULL AND fm.name_prefer_lang != '' THEN fm.name_prefer_lang "
		+ " WHEN :langType = 2 AND fm.name_supportive_lang IS NOT NULL AND fm.name_supportive_lang != '' THEN fm.name_supportive_lang "
		+ " ELSE fm.name_default_lang "
		+ "END AS functionName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ " WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ " ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ " WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ " WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ " ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "CASE "
		+ " WHEN TIME(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ " WHEN TIME(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ " WHEN TIME(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ " WHEN TIME(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ " ELSE '' "
		+ "END AS timeInWord, "
		+ "ofn.date AS date, "
		+ "oc.qty AS quantity, "
		+ "m.decimal_limit_qty AS quantityDecimalLimit, "
		+ "NULL AS isMaxPerson "
		+ "FROM order_crockery oc "
		+ "INNER JOIN order_function ofn ON oc.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type fm ON ofn.fk_function_type_id = fm.id "
		+ "INNER JOIN raw_material rm ON oc.fk_raw_material_id = rm.id "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "LEFT JOIN measurement m ON m.id = oc.fk_measurement_id "
		+ "WHERE "
		+ "rmc.fk_raw_material_category_type_id != 8 AND "
		+ "rm.is_active = true AND "
		+ " ofn.id IN (SELECT ofun.id FROM order_function ofun INNER JOIN order_crockery oc ON oc.fk_order_function_id = ofun.id WHERE ofun.fk_customer_order_details_id = :orderId) "
		+ "ORDER BY "
		+ "ofn.sequence,rmc.priority, rmc.id, rm.priority, rm.id "
)

@SqlResultSetMapping(
	name = "generateCrockeryReportWithQtyResult",
	classes = @ConstructorResult(
		targetClass = EventMenuReportWithCrockeryReportWithQuantityDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Integer.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "category", type = String.class),
			@ColumnResult(name = "crockeryName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "quantity", type = Double.class),
			@ColumnResult(name = "timeInWord", type = String.class),
			@ColumnResult(name = "quantityDecimalLimit", type = Byte.class),
			@ColumnResult(name = "isMaxPerson", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateCrockeryReportWithoutQuantity",
	resultSetMapping = "getCrockeryReportWithoutQuantityValueResult",
	query = "SELECT "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "ofn.date AS date, "
		+ ":isMaxPerson AS isMaxPerson "
		+ "FROM order_crockery oc "
		+ "INNER JOIN order_function ofn ON oc.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type ft ON ofn.fk_function_type_id = ft.id "
		+ "INNER JOIN raw_material rm ON oc.fk_raw_material_id = rm.id "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "WHERE "
		+ "rmc.fk_raw_material_category_type_id != 8 AND "
		+ "ofn.id = (SELECT ofun.id FROM order_function ofun INNER JOIN order_crockery oc ON oc.fk_order_function_id = ofun.id WHERE ofun.fk_customer_order_details_id = :orderId ORDER BY ofun.person DESC "
		+ "LIMIT 1) "
		+ "ORDER BY "
		+ "rmc.priority, rmc.id, rm.priority, rm.id"
)

@NamedNativeQuery(
	name = "generateCrockeryWithoutQuantityWithoutMaxSettingReport",
	resultSetMapping = "getCrockeryReportWithoutQuantityValueResult",
	query = "SELECT "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "ofn.date AS date, "
		+ "NULL AS isMaxPerson "
		+ "FROM order_crockery oc "
		+ "INNER JOIN order_function ofn ON oc.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type ft ON ofn.fk_function_type_id = ft.id "
		+ "INNER JOIN raw_material rm ON oc.fk_raw_material_id = rm.id "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "WHERE "
		+ "rmc.fk_raw_material_category_type_id != 8 AND "
		+ "ofn.id IN (SELECT ofun.id FROM order_function ofun INNER JOIN order_crockery oc ON oc.fk_order_function_id = ofun.id WHERE ofun.fk_customer_order_details_id = :orderId"
		+ ") "
		+ "ORDER BY "
		+ "ofn.sequence, rmc.priority, rmc.id, rm.priority, rm.id"
)

@SqlResultSetMapping(
	name = "getCrockeryReportWithoutQuantityValueResult",
	classes = @ConstructorResult(
		targetClass = CrockeryWithoutQuantityReportDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Integer.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "category", type = String.class),
			@ColumnResult(name = "crockeryName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "isMaxPerson", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateOrderGeneralFixReportWithQuantity",
	resultSetMapping = "generateOrderGeneralFixReportWithQuantityResult",
	query = "SELECT "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "ogfrm.qty AS quantity, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.name_prefer_lang IS NOT NULL AND m.name_prefer_lang != '' THEN m.name_prefer_lang "
		+ "WHEN :langType = 2 AND m.name_supportive_lang IS NOT NULL AND m.name_supportive_lang != '' THEN m.name_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS unit , "
		+ "m.decimal_limit_qty AS decimaLimitQty, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofn.date AS date, "
		+ ":isMaxPerson AS isMaxPerson "
		+ "FROM "
		+ "order_general_fix_raw_material ogfrm "
		+ "INNER JOIN raw_material rm ON ogfrm.fk_raw_material_id = rm.id "
		+ "LEFT JOIN measurement m ON ogfrm.fk_measurement_id = m.id "
		+ "INNER JOIN order_function ofn ON ogfrm.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type ft ON ofn.fk_function_type_id = ft.id "
		+ "WHERE "
		+ "ofn.fk_customer_order_details_id = :orderId "
		+ "AND rm.is_general_fix_raw_material = true "
		+ "AND ofn.id = ( "
		+ "SELECT "
		+ "id "
		+ "FROM "
		+ "order_function "
		+ "WHERE "
		+ "fk_customer_order_details_id = :orderId "
		+ "GROUP BY "
		+ "id "
		+ "ORDER BY "
		+ "MAX(person) DESC "
		+ "LIMIT 1 "
		+ ") "
		+ "GROUP BY "
		+ "ofn.id, rm.id "
		+ "ORDER BY "
		+ "ogfrm.fk_order_function_id, rm.priority,rm.id, ogfrm.id; "
)

@NamedNativeQuery(
	name = "generateOrderGeneralFixReportWithQuantityWithoutMaxSetting",
	resultSetMapping = "generateOrderGeneralFixReportWithQuantityResult",
	query = "SELECT "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "ogfrm.qty AS quantity, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.name_prefer_lang IS NOT NULL AND m.name_prefer_lang != '' THEN m.name_prefer_lang "
		+ "WHEN :langType = 2 AND m.name_supportive_lang IS NOT NULL AND m.name_supportive_lang != '' THEN m.name_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS unit , "
		+ "CASE "
		+ "WHEN (m.decimal_limit_qty = -1) AND (ogfrm.qty % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0 "
		+ "WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimaLimitQty, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofn.date AS date, "
		+ ":isMaxPerson AS isMaxPerson "
		+ "FROM "
		+ "order_general_fix_raw_material ogfrm "
		+ "INNER JOIN raw_material rm ON ogfrm.fk_raw_material_id = rm.id "
		+ "LEFT JOIN measurement m ON ogfrm.fk_measurement_id = m.id "
		+ "INNER JOIN order_function ofn ON ogfrm.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type ft ON ofn.fk_function_type_id = ft.id "
		+ "WHERE "
		+ "ofn.fk_customer_order_details_id = :orderId "
		+ "AND rm.is_general_fix_raw_material = true "
		+ "AND ofn.id IN ( "
		+ "SELECT "
		+ "id "
		+ "FROM "
		+ "order_function "
		+ "WHERE "
		+ "fk_customer_order_details_id = :orderId "
		+ "GROUP BY "
		+ "id "
		+ ") "
		+ "GROUP BY "
		+ "ofn.id, rm.id "
		+ "ORDER BY "
		+ "ogfrm.fk_order_function_id, rm.priority,rm.id, ogfrm.id; "
)

@SqlResultSetMapping(
	name = "generateOrderGeneralFixReportWithQuantityResult",
	classes = @ConstructorResult(
		targetClass = OrderGeneralFixReportWithQuantityDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Integer.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "rawMaterialName", type = String.class),
			@ColumnResult(name = "quantity", type = Double.class),
			@ColumnResult(name = "unit", type = String.class),
			@ColumnResult(name = "decimaLimitQty", type = Long.class),
			@ColumnResult(name = "isMaxPerson", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateOrderGeneralFixReportWithoutQuantity",
	resultSetMapping = "generateOrderGeneralFixReportWithoutQuantityResult",
	query = "SELECT "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "ogfrm.qty AS quantity, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.name_prefer_lang IS NOT NULL AND m.name_prefer_lang != '' THEN m.name_prefer_lang "
		+ "WHEN :langType = 2 AND m.name_supportive_lang IS NOT NULL AND m.name_supportive_lang != '' THEN m.name_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS unit , "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofn.date AS date, "
		+ ":isMaxPerson AS isMaxPerson "
		+ "FROM "
		+ "order_general_fix_raw_material ogfrm "
		+ "INNER JOIN raw_material rm ON ogfrm.fk_raw_material_id = rm.id "
		+ "LEFT JOIN measurement m ON ogfrm.fk_measurement_id = m.id "
		+ "INNER JOIN order_function ofn ON ogfrm.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type ft ON ofn.fk_function_type_id = ft.id "
		+ "WHERE "
		+ "ofn.fk_customer_order_details_id = :orderId "
		+ "AND rm.is_general_fix_raw_material = true "
		+ "AND ofn.fk_function_type_id = ( "
		+ "SELECT "
		+ "fk_function_type_id "
		+ "FROM "
		+ "order_function "
		+ "WHERE "
		+ " fk_customer_order_details_id = :orderId "
		+ "GROUP BY "
		+ "id "
		+ "ORDER BY "
		+ "MAX(person) DESC "
		+ "LIMIT 1 "
		+ ") "
		+ "GROUP BY "
		+ "ofn.id, rm.id "
		+ "ORDER BY "
		+ "ogfrm.fk_order_function_id, rm.priority, rm.id, ogfrm.id; "
)

@NamedNativeQuery(
	name = "generateOrderGeneralFixReportWithoutQuantityWithoutMaxSetting",
	resultSetMapping = "generateOrderGeneralFixReportWithoutQuantityResult",
	query = "SELECT "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "ogfrm.qty AS quantity, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.name_prefer_lang IS NOT NULL AND m.name_prefer_lang != '' THEN m.name_prefer_lang "
		+ "WHEN :langType = 2 AND m.name_supportive_lang IS NOT NULL AND m.name_supportive_lang != '' THEN m.name_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS unit , "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofn.date AS date, "
		+ ":isMaxPerson AS isMaxPerson "
		+ "FROM "
		+ "order_general_fix_raw_material ogfrm "
		+ "INNER JOIN raw_material rm ON ogfrm.fk_raw_material_id = rm.id "
		+ "LEFT JOIN measurement m ON ogfrm.fk_measurement_id = m.id "
		+ "INNER JOIN order_function ofn ON ogfrm.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type ft ON ofn.fk_function_type_id = ft.id "
		+ "WHERE "
		+ "ofn.fk_customer_order_details_id = :orderId "
		+ "AND rm.is_general_fix_raw_material = true "
		+ "AND ofn.fk_function_type_id IN ( "
		+ "SELECT "
		+ "fk_function_type_id "
		+ "FROM "
		+ "order_function "
		+ "WHERE "
		+ " fk_customer_order_details_id = :orderId "
		+ "GROUP BY "
		+ "id "
		+ ") "
		+ "GROUP BY "
		+ "ofn.id, rm.id "
		+ "ORDER BY "
		+ "ogfrm.fk_order_function_id, rm.priority,rm.id, ogfrm.id; "
)

@SqlResultSetMapping(
	name = "generateOrderGeneralFixReportWithoutQuantityResult",
	classes = @ConstructorResult(
		targetClass = OrderGeneralFixReportWithoutQuantityDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Integer.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "rawMaterialName", type = String.class),
			@ColumnResult(name = "isMaxPerson", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "findOrderGeneralFixRawMaterialNotesValue",
	resultSetMapping = "findNotesValueResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND note_prefer_lang IS NOT NULL AND note_prefer_lang != '' THEN note_prefer_lang "
		+ "WHEN :langType = 2 AND note_supportive_lang IS NOT NULL AND note_supportive_lang != '' THEN note_supportive_lang "
		+ "ELSE note_default_lang "
		+ "END AS noteName "
		+ "FROM general_fix_raw_material_notes; "
)

@NamedNativeQuery(
	name = "findCrockeryNotesValue",
	resultSetMapping = "findNotesValueResult",
	query = " SELECT "
		+ "CASE "
		+ " WHEN :langType = 1 AND note_prefer_lang IS NOT NULL AND note_prefer_lang != '' THEN note_prefer_lang "
		+ " WHEN :langType = 2 AND note_supportive_lang IS NOT NULL AND note_supportive_lang != '' THEN note_supportive_lang "
		+ " ELSE note_default_lang "
		+ "END AS noteName "
		+ "FROM crockery_report_notes; "
)

@SqlResultSetMapping(
	name = "findNotesValueResult",
	classes = @ConstructorResult(
		targetClass = EventDistributionNotesDto.class,
		columns = {
			@ColumnResult(name = "noteName", type = String.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateKitchenCrockeryWithQuantityReport",
	resultSetMapping = "generateKitchenCrockeryWithQuantityReportResult",
	query = "SELECT "
		+ "orderFunctionId, "
		+ "functionName, "
		+ "category, "
		+ "crockeryName, "
		+ "m.id, "
		+ "date, "
		+ "quantity, "
		+ "CASE  "
		+ "WHEN (m.decimal_limit_qty = -1) AND (quantity % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0  "
		+ "WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3  "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS quantityDecimalLimit, "
		+ "isMaxPerson "
		+ "from (SELECT "
		+ "functionName AS functionName, "
		+ "orderFunctionId AS orderFunctionId, "
		+ "category AS category, "
		+ "crockeryName AS crockeryName, "
		+ "date AS date, "
		+ "function_sequence AS function_sequence, "
		+ "rmc_priority AS rmc_priority, "
		+ "rmc_id AS rmc_id,  "
		+ "rm_priority AS rm_priority,  "
		+ "rm_id AS rm_id, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS finalM_id, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS quantity,  "
		+ "quantityDecimalLimit AS quantityDecimalLimit, "
		+ ":isMaxPerson AS isMaxPerson "
		+ "from ((SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND fm.name_prefer_lang IS NOT NULL AND fm.name_prefer_lang != '' THEN fm.name_prefer_lang "
		+ "WHEN :langType = 2 AND fm.name_supportive_lang IS NOT NULL AND fm.name_supportive_lang != '' THEN fm.name_supportive_lang "
		+ "ELSE fm.name_default_lang "
		+ "END AS functionName, "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "rm.id AS crockeryId, "
		+ "ofn.date AS date, "
		+ "ofn.`sequence` AS function_sequence,  "
		+ "oc.qty AS quantity,  "
		+ "rmc.priority AS rmc_priority,  "
		+ "rmc.id AS rmc_id, "
		+ "rm.priority AS rm_priority, "
		+ "rm.id AS rm_id, "
		+ "finalM.is_base_unit AS final_base_unit, "
		+ "finalM.base_unit_equivalent AS final_base_unit_equivalent, "
		+ "finalM.id AS finalM_id, "
		+ "finalM.base_unit_id AS final_base_unit_id, "
		+ "fk_raw_material_category_type_id AS raw_material_id, "
		+ "m.decimal_limit_qty AS quantityDecimalLimit "
		+ "FROM order_crockery oc "
		+ "INNER JOIN order_function ofn ON oc.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type fm ON ofn.fk_function_type_id = fm.id "
		+ "INNER JOIN raw_material rm ON oc.fk_raw_material_id = rm.id "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "LEFT JOIN measurement m ON m.id = oc.fk_measurement_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = oc.fk_measurement_id  "
		+ "WHERE "
		+ "rmc.fk_raw_material_category_type_id = 8 AND "
		+ "ofn.id = (SELECT ofun.id FROM order_function ofun INNER JOIN order_crockery oc ON oc.fk_order_function_id = ofun.id WHERE ofun.fk_customer_order_details_id = :orderId ORDER BY ofun.person DESC LIMIT 1) "
		+ "ORDER BY "
		+ "rmc.priority, rmc.id, rm.priority, rm.id ) "
		+ "UNION ALL "
		+ "(SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "of2.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "rm.id AS crockeryId, "
		+ "max_order_function.`date` AS date, "
		+ "max_order_function.`sequence` AS function_sequence, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS quantity, "
		+ "rmc.priority AS rmc_priority, "
		+ "rmc.id AS rmc_id, "
		+ "rm.priority AS rm_priority, "
		+ "rm.id AS rm_id, "
		+ "finalM.is_base_unit AS final_base_unit, "
		+ "finalM.base_unit_equivalent AS final_base_unit_equivalent, "
		+ "finalM.id AS finalM_id, "
		+ "finalM.base_unit_id AS final_base_unit_id, "
		+ "fk_raw_material_category_type_id AS raw_material_id, "
		+ "m.decimal_limit_qty AS quantityDecimalLimit "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function of2 ON of2.id = omp.fk_order_function_id "
		+ "LEFT JOIN menu_item_raw_material mirm on mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE "
		+ "WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id "
		+ "ELSE rma.fk_raw_material_id "
		+ "END "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "INNER JOIN menu_item_category mic ON mic.id  = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN measurement m ON m.id = rma.fk_actual_measurement_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ "INNER JOIN (SELECT * FROM order_function WHERE fk_customer_order_details_id = :orderId ORDER BY person DESC, id LIMIT 1) AS max_order_function "
		+ "INNER JOIN function_type ft ON max_order_function.fk_function_type_id  = ft.id "
		+ "WHERE of2.fk_customer_order_details_id = :orderId "
		+ "AND rmc.fk_raw_material_category_type_id = 8 GROUP BY rm.id)) combined_query "
		+ "JOIN measurement m on m.id = finalM_id  "
		+ "GROUP BY crockeryId "
		+ "ORDER BY rmc_priority, rmc_id, rm_priority, rm_id ) combined_query2 "
		+ "JOIN measurement m on m.id = finalM_id "
		+ "ORDER BY function_sequence, rmc_priority, rmc_id, rm_priority, rm_id"
)

@SqlResultSetMapping(
	name = "generateKitchenCrockeryWithQuantityReportResult",
	classes = @ConstructorResult(
		targetClass = CrockeryWithQuantityReportDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Integer.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "category", type = String.class),
			@ColumnResult(name = "crockeryName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "quantity", type = Double.class),
			@ColumnResult(name = "quantityDecimalLimit", type = Byte.class),
			@ColumnResult(name = "isMaxPerson", type = Boolean.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateKitchenCrockeryWithQuantityWithoutMaxSettingReport",
	resultSetMapping = "generateKitchenCrockeryWithQuantityWithoutMaxSettingReportResult",
	query = "SELECT "
		+ "orderFunctionId, "
		+ "functionName, "
		+ "category, "
		+ "crockeryName, "
		+ "m.id, "
		+ "date, "
		+ "quantity, "
		+ "NULL as timeInWord, "
		+ "CASE  "
		+ "WHEN (m.decimal_limit_qty = -1) AND (quantity % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0  "
		+ "WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3  "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS quantityDecimalLimit, "
		+ "isMaxPerson "
		+ "FROM (SELECT "
		+ "functionName AS functionName, "
		+ "orderFunctionId AS orderFunctionId, "
		+ "category AS category, "
		+ "crockeryName AS crockeryName, "
		+ "date AS date, "
		+ "function_sequence AS function_sequence, "
		+ "rmc_priority AS rmc_priority, "
		+ "rmc_id AS rmc_id,  "
		+ "rm_priority AS rm_priority,  "
		+ "rm_id AS rm_id, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS finalM_id, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS quantity,  "
		+ "quantityDecimalLimit AS quantityDecimalLimit, "
		+ ":isMaxPerson AS isMaxPerson "
		+ "from ((SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND fm.name_prefer_lang IS NOT NULL AND fm.name_prefer_lang != '' THEN fm.name_prefer_lang "
		+ "WHEN :langType = 2 AND fm.name_supportive_lang IS NOT NULL AND fm.name_supportive_lang != '' THEN fm.name_supportive_lang "
		+ "ELSE fm.name_default_lang "
		+ "END AS functionName, "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "rm.id AS crockeryId, "
		+ "ofn.date AS date, "
		+ "ofn.`sequence` AS function_sequence,  "
		+ "oc.qty AS quantity,  "
		+ "rmc.priority AS rmc_priority,  "
		+ "rmc.id AS rmc_id, "
		+ "rm.priority AS rm_priority, "
		+ "rm.id AS rm_id, "
		+ "finalM.is_base_unit AS final_base_unit, "
		+ "finalM.base_unit_equivalent AS final_base_unit_equivalent, "
		+ "finalM.id AS finalM_id, "
		+ "finalM.base_unit_id AS final_base_unit_id, "
		+ "fk_raw_material_category_type_id AS raw_material_id, "
		+ "m.decimal_limit_qty AS quantityDecimalLimit "
		+ "FROM order_crockery oc "
		+ "INNER JOIN order_function ofn ON oc.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type fm ON ofn.fk_function_type_id = fm.id "
		+ "INNER JOIN raw_material rm ON oc.fk_raw_material_id = rm.id "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "LEFT JOIN measurement m ON m.id = oc.fk_measurement_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = oc.fk_measurement_id  "
		+ "WHERE "
		+ "rmc.fk_raw_material_category_type_id = 8 AND "
		+ "ofn.fk_customer_order_details_id = :orderId "
		+ "ORDER BY "
		+ "rmc.priority, rmc.id, rm.priority, rm.id ) "
		+ "UNION ALL "
		+ "(SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName,"
		+ "of2.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "rm.id AS crockeryId, "
		+ "of2.`date` AS date , "
		+ "of2.`sequence` AS function_sequence,  "
		+ "rma.final_qty AS quantity,  "
		+ "rmc.priority AS rmc_priority,  "
		+ "rmc.id AS rmc_id, "
		+ "rm.priority AS rm_priority, "
		+ "rm.id AS rm_id, "
		+ "finalM.is_base_unit AS final_base_unit, "
		+ "finalM.base_unit_equivalent AS final_base_unit_equivalent, "
		+ "finalM.id AS finalM_id, "
		+ "finalM.base_unit_id AS final_base_unit_id, "
		+ "fk_raw_material_category_type_id AS raw_material_id, "
		+ "m.decimal_limit_qty AS quantityDecimalLimit "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi on ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp on omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function of2 ON of2.id = omp.fk_order_function_id "
		+ "INNER JOIN function_type ft on of2.fk_function_type_id  = ft.id "
		+ "LEFT JOIN menu_item_raw_material mirm on mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE "
		+ "WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id "
		+ "ELSE rma.fk_raw_material_id "
		+ "END "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "INNER JOIN menu_item_category mic on mic.id  = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN measurement m ON m.id = rma.fk_actual_measurement_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id  "
		+ "WHERE of2.fk_customer_order_details_id = :orderId  "
		+ "AND rmc.fk_raw_material_category_type_id = 8)) combined_query "
		+ "JOIN measurement m on m.id = finalM_id "
		+ "GROUP BY orderFunctionId, crockeryId "
		+ "ORDER BY rmc_priority, rmc_id, rm_priority, rm_id ) combined_query2 "
		+ "JOIN measurement m on m.id = finalM_id "
		+ "ORDER BY function_sequence, rmc_priority, rmc_id, rm_priority, rm_id "
)

@SqlResultSetMapping(
	name = "generateKitchenCrockeryWithQuantityWithoutMaxSettingReportResult",
	classes = @ConstructorResult(
		targetClass = EventMenuReportWithCrockeryReportWithQuantityDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Integer.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "category", type = String.class),
			@ColumnResult(name = "crockeryName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "quantity", type = Double.class),
			@ColumnResult(name = "timeInWord", type = String.class),
			@ColumnResult(name = "quantityDecimalLimit", type = Byte.class),
			@ColumnResult(name = "isMaxPerson", type = Boolean.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateKitchenCrockeryReportWithoutQuantity",
	resultSetMapping = "generateKitchenCrockeryReportWithOrWithoutQuantityResult",
	query = "SELECT "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "ofn.date AS date, "
		+ ":isMaxPerson AS isMaxPerson "
		+ "FROM order_crockery oc "
		+ "INNER JOIN order_function ofn ON oc.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type ft ON ofn.fk_function_type_id = ft.id "
		+ "INNER JOIN raw_material rm ON oc.fk_raw_material_id = rm.id "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "WHERE "
		+ "rmc.fk_raw_material_category_type_id = 8 AND "
		+ "ofn.id = (SELECT ofun.id FROM order_function ofun INNER JOIN order_crockery oc ON oc.fk_order_function_id = ofun.id WHERE ofun.fk_customer_order_details_id = :orderId ORDER BY ofun.person DESC "
		+ "LIMIT 1) "
		+ "ORDER BY "
		+ "rmc.priority, rmc.id, rm.priority, rm.id"
)

@NamedNativeQuery(
	name = "generateKitchenCrockeryWithoutQuantityWithoutMaxSettingReport",
	resultSetMapping = "generateKitchenCrockeryReportWithOrWithoutQuantityResult",
	query = "SELECT "
		+ "ofn.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS category, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS crockeryName, "
		+ "ofn.date AS date, "
		+ ":isMaxPerson AS isMaxPerson "
		+ "FROM order_crockery oc "
		+ "INNER JOIN order_function ofn ON oc.fk_order_function_id = ofn.id "
		+ "INNER JOIN function_type ft ON ofn.fk_function_type_id = ft.id "
		+ "INNER JOIN raw_material rm ON oc.fk_raw_material_id = rm.id "
		+ "INNER JOIN raw_material_category rmc ON rm.fk_raw_material_category_id = rmc.id "
		+ "WHERE "
		+ "rmc.fk_raw_material_category_type_id = 8 AND "
		+ "ofn.id IN (SELECT ofun.id FROM order_function ofun INNER JOIN order_crockery oc ON oc.fk_order_function_id = ofun.id WHERE ofun.fk_customer_order_details_id = :orderId"
		+ ") "
		+ "ORDER BY "
		+ "ofn.sequence, rmc.priority, rmc.id, rm.priority, rm.id"
)

@SqlResultSetMapping(
	name = "generateKitchenCrockeryReportWithOrWithoutQuantityResult",
	classes = @ConstructorResult(
		targetClass = CrockeryWithoutQuantityReportDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Integer.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "category", type = String.class),
			@ColumnResult(name = "crockeryName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "isMaxPerson", type = Boolean.class)
		}
	)
)

@Entity
public class OrderGeneralFixAndCrockeryAllocationReportQuery extends AuditIdModelOnly {
}