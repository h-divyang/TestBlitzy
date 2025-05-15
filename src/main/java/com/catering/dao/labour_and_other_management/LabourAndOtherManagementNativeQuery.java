package com.catering.dao.labour_and_other_management;

import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.GetOrderCrockeryDto;
import com.catering.dto.tenant.request.OrderCrockeryRawMaterialDto;
import com.catering.dto.tenant.request.OrderGeneralFixRawMaterialResponseDto;
import com.catering.model.audit.AuditIdModelOnly;

@NamedNativeQuery(
	name = "findCrockeryDataByOrderIdAndItemCategoryId",
	resultSetMapping = "findCrockeryDataByOrderIdAndItemCategoryIdResult",
	query = "SELECT "
		+ "t.rawMaterialId, "
		+ "t.nameDefaultLang, "
		+ "t.namePreferLang, "
		+ "t.nameSupportiveLang, "
		+ "t.agency, "
		+ "t.orderTime, "
		+ "t.orderCrockeryId, "
		+ "t.qty, "
		+ "t.measurementId, "
		+ "t.functionId, "
		+ "CASE "
		+ " WHEN cs.is_auto_time_raw_material = 1 AND t.functionTypeShiftId IS NOT NULL AND t.shiftTime IS NOT NULL AND t.shiftId = 5 THEN "
		+ " CONCAT(DATE_SUB(DATE(t.functionDate), INTERVAL 1 DAY), ' ', TIME(t.shiftTime)) "
		+ " WHEN cs.is_auto_time_raw_material = 1 AND t.functionTypeShiftId IS NOT NULL AND t.shiftTime IS NOT NULL THEN "
		+ " CONCAT(DATE(t.functionDate), ' ', TIME(t.shiftTime)) "
		+ "ELSE t.functionDate "
		+ "END AS functionDate, "
		+ "t.rawMaterialMeasurementId, "
		+ "t.supplierRate, "
		+ "IF(t.price IS NULL OR t.price <= 0, IFNULL(ROUND(getSmallestMeasurementValue(t.qty, ocm.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(t.supplierRate, t.rawMaterialMeasurementId, FALSE, TRUE), '$.value')), 2), 0), t.price) AS price, "
		+ "t.priority, "
		+ "t.godown "
		+ "FROM (SELECT "
		+ "rm.id AS rawMaterialId, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "rm.name_prefer_lang AS namePreferLang, "
		+ "rm.name_supportive_lang AS nameSupportiveLang, "
		+ "COALESCE(oc.fk_agency_id, rms.fk_contact_id) AS agency, "
		+ "oc.order_time as ordertime, "
		+ "oc.id AS orderCrockeryId, "
		+ "IF(oc.qty IS NULL AND rm.weight_per_100_pax IS NOT NULL AND rm.weight_per_100_pax > 0, "
		+ " adjustQuantity(getSmallestMeasurementValue((SUM(ofun.person) / COUNT(ofun.id)) * rm.weight_per_100_pax / 100, rm.fk_measurement_id), getSmallestMeasurementId(IF(oc.qty IS NULL, rm.fk_measurement_id, NULL)), IF(:isAdjustQuantity, 1, 0)), "
		+ " IF(oc.qty IS NOT NULL OR oc.qty > 0, oc.qty, 0)) AS qty, "
		+ "IF(oc.qty IS NULL, "
		+ " adjustQuantityUnit(getSmallestMeasurementValue((SUM(ofun.person) / COUNT(ofun.id)) * rm.weight_per_100_pax / 100, rm.fk_measurement_id), getSmallestMeasurementId(IF(oc.qty IS NULL, rm.fk_measurement_id, NULL)), IF(:isAdjustQuantity, 1, 0)), "
		+ " oc.fk_measurement_id "
		+ ") AS measurementId, "
		+ "ofun.id AS functionId, "
		+ "ofun.date AS functionDate, "
		+ "rm.fk_measurement_id AS rawMaterialMeasurementId, "
		+ "rm.supplier_rate AS supplierRate, "
		+ "oc.price, "
		+ "rm.priority, "
		+ "oc.fk_godown_id AS godown, "
		+ "ls.time AS shiftTime, "
		+ "ls.id AS shiftId, "
		+ "ft.fk_shift_id AS functionTypeShiftId "
		+ "FROM raw_material rm "
		+ "LEFT JOIN raw_material_supplier rms ON rms.fk_raw_material_id = rm.id "
		+ "LEFT JOIN order_crockery oc ON oc.fk_raw_material_id = rm.id AND oc.fk_order_function_id = :orderFunctionId "
		+ "LEFT JOIN order_function ofun ON ofun.fk_customer_order_details_id = :orderId AND ofun.id = :orderFunctionId "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN labour_shift ls ON ls.id = ft.fk_shift_id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE rm.fk_raw_material_category_id = :rawMaterialCategoryId AND (rm.is_active = true ) "
		+ "GROUP BY rm.id) AS t "
		+ "LEFT JOIN measurement ocm ON ocm.id = t.measurementId "
		+ "LEFT JOIN measurement rmm ON rmm.id = t.rawMaterialMeasurementId "
		+ "LEFT JOIN company_setting cs ON cs.is_auto_time_raw_material = 1 "
		+ "ORDER BY t.priority, t.rawMaterialId "
	
)

@SqlResultSetMapping(
	name = "findCrockeryDataByOrderIdAndItemCategoryIdResult",
	classes = @ConstructorResult(
		targetClass = OrderCrockeryRawMaterialDto.class,
		columns = {
			@ColumnResult(name = "rawMaterialId", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "agency", type = Long.class),
			@ColumnResult(name = "orderTime", type = LocalDateTime.class),
			@ColumnResult(name = "orderCrockeryId", type = Long.class),
			@ColumnResult(name = "qty", type = Double.class),
			@ColumnResult(name = "measurementId", type = Long.class),
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "functionDate", type = LocalDateTime.class),
			@ColumnResult(name = "rawMaterialMeasurementId", type = Long.class),
			@ColumnResult(name = "supplierRate", type = Double.class),
			@ColumnResult(name = "price", type = Double.class),
			@ColumnResult(name = "godown", type = Long.class)
		}
	)
)

@NamedNativeQuery(
name = "calculateItemCost",
query = "SELECT "
		+ "ROUND(SUM(t.total), 2) AS total "
		+ "FROM (SELECT "
		+ "adjustQuantity(getSmallestMeasurementValue(rma.final_qty, finalM.id), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS finalQty, "
		+ "adjustQuantityUnit(getSmallestMeasurementValue(rma.final_qty, finalM.id), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS finalQtyMeasurementId, "
		+ "ROUND(getSmallestMeasurementValue(rma.final_qty, finalM.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(rm.supplier_rate, supplierM.id, FALSE, TRUE), '$.value')), 2) AS total, "
		+ "rma.final_qty AS originalFinalQty, "
		+ "rma.fk_final_measurement_id AS originalFinalQtyUnitId, "
		+ "rma.actual_qty AS originalActualQty, "
		+ "rma.fk_actual_measurement_id AS originalActualQtyUnitId, "
		+ "rm.supplier_rate AS supplierRate, "
		+ "supplierM.is_base_unit AS supplierM_isBaseUnit, "
		+ "supplierM.base_unit_equivalent AS supplierM_baseUnitEquivalent "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function ofu ON ofu.id = omp.fk_order_function_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id ELSE rma.fk_raw_material_id END "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ "LEFT JOIN measurement supplierM ON supplierM.id = rm.fk_measurement_id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE ompmi.fk_menu_item_id = :menuItemId AND ofu.id = :functionId AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1)) AS t "
		+ "LEFT JOIN measurement m ON m.id = t.finalQtyMeasurementId " 
)

@NamedNativeQuery(
name = "findOrderGeneralFixRawMaterialByFunctionId",
resultSetMapping = "findOrderGeneralFixRawMaterialByFunctionIdResult",
query = "SELECT "
		+ "t.rawMaterialId, "
		+ "t.nameDefaultLang, "
		+ "t.namePreferLang, "
		+ "t.nameSupportiveLang, "
		+ "t.agency, "
		+ "t.orderTime, "
		+ "t.orderGeneralFixRawMaterialId, "
		+ "t.qty, "
		+ "t.measurementId, "
		+ "t.orderFunctionId, "
		+ "CASE "
		+ " WHEN cs.is_auto_time_raw_material = 1 AND t.functionTypeShiftId IS NOT NULL AND t.shiftTime IS NOT NULL AND t.shiftId = 5 THEN "
		+ " CONCAT(DATE_SUB(DATE(t.functionDate), INTERVAL 1 DAY), ' ', TIME(t.shiftTime)) "
		+ " WHEN cs.is_auto_time_raw_material = 1 AND t.functionTypeShiftId IS NOT NULL AND t.shiftTime IS NOT NULL THEN "
		+ " CONCAT(DATE(t.functionDate), ' ', TIME(t.shiftTime)) "
		+ "ELSE t.functionDate "
		+ "END AS functionDate, "
		+ "t.rawMaterialMeasurementId, "
		+ "t.supplierRate, "
		+ "t.godown, "
		+ "IF(t.price IS NULL OR t.price <= 0, IFNULL(ROUND(getSmallestMeasurementValue(t.qty, ogfrmm.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(t.supplierRate, rmm.id, FALSE, TRUE), '$.value')), 2), 0), t.price) AS price, "
		+ "t.priority "
		+ "FROM (SELECT "
		+ "rm.id AS rawMaterialId, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "rm.name_prefer_lang AS namePreferLang, "
		+ "rm.name_supportive_lang AS nameSupportiveLang, "
		+ "COALESCE(ogfrm.fk_agency_id, CASE WHEN rms.is_default = 1 THEN rms.fk_contact_id ELSE NULL END) AS agency, "
		+ "ogfrm.order_time AS orderTime, "
		+ "ogfrm.id AS orderGeneralFixRawMaterialId, "
		+ "IF(ogfrm.qty IS NULL AND rm.weight_per_100_pax IS NOT NULL AND rm.weight_per_100_pax > 0, "
		+ " adjustQuantity(getSmallestMeasurementValue(ofun.person * rm.weight_per_100_pax / 100, IF(ogfrm.qty IS NULL, rm.fk_measurement_id, NULL)), getSmallestMeasurementId(IF(ogfrm.qty IS NULL, rm.fk_measurement_id, NULL)), IF(:isAdjustQuantity, 1, 0)), "
		+ " IF(ogfrm.qty IS NOT NULL OR ogfrm.qty > 0, ogfrm.qty, 0) "
		+ ") AS qty, "
		+ "IF(ogfrm.qty IS NULL, "
		+ " adjustQuantityUnit(ofun.person * rm.weight_per_100_pax / 100, rm.fk_measurement_id, IF(:isAdjustQuantity, 1, 0)), "
		+ " ogfrm.fk_measurement_id "
		+ ") AS measurementId, "
		+ ":orderFunctionId AS orderFunctionId, "
		+ "ofun.date AS functionDate, "
		+ "rm.fk_measurement_id AS rawMaterialMeasurementId, "
		+ "rm.supplier_rate AS supplierRate, "
		+ "ogfrm.price, "
		+ "rm.priority, "
		+ "ogfrm.fk_godown_id AS godown, "
		+ "ls.time AS shiftTime, "
		+ "ls.id AS shiftId, "
		+ "ft.fk_shift_id AS functionTypeShiftId "
		+ "FROM raw_material rm "
		+ "LEFT JOIN raw_material_supplier rms ON rms.fk_raw_material_id = rm.id AND rms.is_default "
		+ "LEFT JOIN order_general_fix_raw_material ogfrm ON ogfrm.fk_raw_material_id = rm.id AND ogfrm.fk_order_function_id = :orderFunctionId "
		+ "LEFT JOIN order_function ofun ON ofun.id = :orderFunctionId "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN labour_shift ls ON ls.id = ft.fk_shift_id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE rm.is_general_fix_raw_material = 1 AND (rm.is_active = true )) as t "
		+ "LEFT JOIN measurement ogfrmm ON ogfrmm.id = t.measurementId "
		+ "LEFT JOIN measurement rmm ON rmm.id = t.rawMaterialMeasurementId "
		+ "LEFT JOIN company_setting cs ON cs.is_auto_time_raw_material = 1 "
		+ "ORDER BY t.priority, t.rawMaterialId "
)

@SqlResultSetMapping(
	name = "findOrderGeneralFixRawMaterialByFunctionIdResult",
	classes = @ConstructorResult(
		targetClass = OrderGeneralFixRawMaterialResponseDto.class,
		columns = {
			@ColumnResult(name = "rawMaterialId", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "agency", type = Long.class),
			@ColumnResult(name = "orderTime", type = LocalDateTime.class),
			@ColumnResult(name = "orderGeneralFixRawMaterialId", type = Long.class),
			@ColumnResult(name = "qty", type = Double.class),
			@ColumnResult(name = "measurementId", type = Long.class),
			@ColumnResult(name = "orderFunctionId", type = Long.class),
			@ColumnResult(name = "functionDate", type = LocalDateTime.class),
			@ColumnResult(name = "rawMaterialMeasurementId", type = Long.class),
			@ColumnResult(name = "supplierRate", type = Double.class),
			@ColumnResult(name = "price", type = Double.class),
			@ColumnResult(name = "godown", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "findGeneralFixRawMaterialByOrderFunctionId",
	resultSetMapping = "findCrockeryDataByOrderFunctionAndRawMaterialCategoryIdResult",
	query ="SELECT "
		+ "t.orderGeneralFixRawMaterialId AS id, "
		+ "t.rawMaterialId, "
		+ "t.nameDefaultLang, "
		+ "t.namePreferLang, "
		+ "t.nameSupportiveLang, "
		+ "t.rawMaterialAgencyId, "
		+ "t.crockeryAgencyId, "
		+ "t.orderTime, "
		+ "t.qty, "
		+ "t.measurementId, "
		+ "CASE "
		+ "WHEN cs.is_auto_time_raw_material = 1 AND t.functionTypeShiftId IS NOT NULL AND t.shiftTime IS NOT NULL AND t.shiftId = 5 THEN "
		+ "CONCAT(DATE_SUB(DATE(t.functionDate), INTERVAL 1 DAY), ' ', TIME(t.shiftTime)) "
		+ "WHEN cs.is_auto_time_raw_material = 1 AND t.functionTypeShiftId IS NOT NULL AND t.shiftTime IS NOT NULL THEN "
		+ "CONCAT(DATE(t.functionDate), ' ', TIME(t.shiftTime)) "
		+ "ELSE t.functionDate "
		+ "END AS functionDate, "
		+ "t.rawMaterialMeasurementId, "
		+ "t.supplierRate, "
		+ "IF(t.price IS NULL OR t.price <= 0, "
		+ "IFNULL(ROUND(getSmallestMeasurementValue(t.qty, ogfrmm.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(t.supplierRate, rmm.id, FALSE, TRUE), '$.value')), 2), 0), t.price) AS price, "
		+ "t.godown "
		+ "FROM (SELECT "
		+ "rm.id AS rawMaterialId, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "rm.name_prefer_lang AS namePreferLang, "
		+ "rm.name_supportive_lang AS nameSupportiveLang, "
		+ "(SELECT rms.fk_contact_id "
		+ "FROM raw_material_supplier rms "
		+ "WHERE rms.fk_raw_material_id = rm.id AND rms.is_default = 1 "
		+ "LIMIT 1) AS rawMaterialAgencyId, "
		+ "ogfrm.fk_agency_id AS crockeryAgencyId, "
		+ "ogfrm.order_time AS orderTime, "
		+ "ogfrm.id AS orderGeneralFixRawMaterialId, "
		+ "IF(ogfrm.qty IS NULL AND rm.weight_per_100_pax IS NOT NULL AND rm.weight_per_100_pax > 0, "
		+ "adjustQuantity(getSmallestMeasurementValue(ofun.person * rm.weight_per_100_pax / 100, rm.fk_measurement_id), getSmallestMeasurementId(rm.fk_measurement_id), IF(:isAdjustQuantity, 1, 0)), "
		+ "IF(ogfrm.qty IS NOT NULL OR ogfrm.qty > 0, ogfrm.qty, 0)) AS qty, "
		+ "IF(ogfrm.qty IS NULL, "
		+ "adjustQuantityUnit(getSmallestMeasurementValue(ofun.person * rm.weight_per_100_pax / 100, rm.fk_measurement_id), getSmallestMeasurementId(rm.fk_measurement_id), IF(:isAdjustQuantity, 1, 0)), "
		+ "ogfrm.fk_measurement_id) AS measurementId, "
		+ "ofun.date AS functionDate, "
		+ "rm.fk_measurement_id AS rawMaterialMeasurementId, "
		+ "rm.supplier_rate AS supplierRate, "
		+ "ogfrm.price, "
		+ "rm.priority, "
		+ "ogfrm.fk_godown_id AS godown, "
		+ "ls.time AS shiftTime, "
		+ "ls.id AS shiftId, "
		+ "ft.fk_shift_id AS functionTypeShiftId "
		+ "FROM raw_material rm "
		+ "LEFT JOIN raw_material_supplier rms ON rms.fk_raw_material_id = rm.id AND rms.is_default = 1 "
		+ "LEFT JOIN order_general_fix_raw_material ogfrm ON ogfrm.fk_raw_material_id = rm.id AND ogfrm.fk_order_function_id = :orderFunctionId "
		+ "LEFT JOIN order_function ofun ON ofun.id = :orderFunctionId "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN labour_shift ls ON ls.id = ft.fk_shift_id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE rm.is_general_fix_raw_material = 1 "
		+ "AND (rm.is_active = true OR ogfrm.id IS NOT NULL)) as t "
		+ "LEFT JOIN measurement ogfrmm ON ogfrmm.id = t.measurementId "
		+ "LEFT JOIN measurement rmm ON rmm.id = t.rawMaterialMeasurementId "
		+ "LEFT JOIN company_setting cs ON cs.is_auto_time_raw_material = 1 "
		+ "ORDER BY t.priority, t.rawMaterialId"
)

@NamedNativeQuery(
	name = "findCrockeryDataByOrderFunctionAndRawMaterialCategoryId",
	resultSetMapping = "findCrockeryDataByOrderFunctionAndRawMaterialCategoryIdResult",
	query ="SELECT "
		+ "t.orderCrockeryId AS id, "
		+ "t.rawMaterialId, "
		+ "t.nameDefaultLang, "
		+ "t.namePreferLang, "
		+ "t.nameSupportiveLang, "
		+ "t.rawMaterialAgencyId, "
		+ "t.crockeryAgencyId, "
		+ "t.orderTime, "
		+ "t.qty, "
		+ "t.measurementId, "
		+ "CASE "
		+ "WHEN cs.is_auto_time_raw_material = 1 AND t.functionTypeShiftId IS NOT NULL AND t.shiftTime IS NOT NULL AND t.shiftId = 5 THEN "
		+ "CONCAT(DATE_SUB(DATE(t.functionDate), INTERVAL 1 DAY), ' ', TIME(t.shiftTime)) "
		+ "WHEN cs.is_auto_time_raw_material = 1 AND t.functionTypeShiftId IS NOT NULL AND t.shiftTime IS NOT NULL THEN "
		+ "CONCAT(DATE(t.functionDate), ' ', TIME(t.shiftTime)) "
		+ "ELSE t.functionDate "
		+ "END AS functionDate, "
		+ "t.rawMaterialMeasurementId, "
		+ "t.supplierRate, "
		+ "IF(t.price IS NULL OR t.price <= 0, "
		+ "IFNULL(ROUND(getSmallestMeasurementValue(t.qty, ocm.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(t.supplierRate, rmm.id, FALSE, TRUE), '$.value')), 2), 0), "
		+ "t.price) AS price, "
		+ "t.godown "
		+ "FROM ( "
		+ "SELECT "
		+ "rm.id AS rawMaterialId, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "rm.name_prefer_lang AS namePreferLang, "
		+ "rm.name_supportive_lang AS nameSupportiveLang, "
		+ "(SELECT rms.fk_contact_id "
		+ "FROM raw_material_supplier rms "
		+ "WHERE rms.fk_raw_material_id = rm.id AND rms.is_default = 1 "
		+ "LIMIT 1) AS rawMaterialAgencyId, "
		+ "oc.fk_agency_id AS crockeryAgencyId, "
		+ "oc.order_time AS orderTime, "
		+ "oc.id AS orderCrockeryId, "
		+ "IF(oc.qty IS NULL AND rm.weight_per_100_pax IS NOT NULL AND rm.weight_per_100_pax > 0, "
		+ "adjustQuantity(getSmallestMeasurementValue((SUM(ofun.person) / COUNT(ofun.id)) * rm.weight_per_100_pax / 100, IF(oc.qty IS NULL, rm.fk_measurement_id, NULL)), "
		+ "getSmallestMeasurementId(IF(oc.qty IS NULL, rm.fk_measurement_id, NULL)), "
		+ "IF(:isAdjustQuantity, 1, 0)), "
		+ "IF(oc.qty IS NOT NULL OR oc.qty > 0, oc.qty, 0)) AS qty, "
		+ "IF(oc.qty IS NULL, "
		+ "adjustQuantityUnit((SUM(ofun.person) / COUNT(ofun.id)) * rm.weight_per_100_pax / 100, "
		+ "IF(oc.qty IS NULL, rm.fk_measurement_id, NULL), "
		+ "IF(:isAdjustQuantity, 1, 0)), "
		+ "oc.fk_measurement_id) AS measurementId, "
		+ "ofun.date AS functionDate, "
		+ "rm.fk_measurement_id AS rawMaterialMeasurementId, "
		+ "rm.supplier_rate AS supplierRate, "
		+ "oc.price, "
		+ "rm.priority, "
		+ "oc.fk_godown_id AS godown, "
		+ "ls.time AS shiftTime, "
		+ "ls.id AS shiftId, "
		+ "ft.fk_shift_id AS functionTypeShiftId "
		+ "FROM raw_material rm "
		+ "LEFT JOIN raw_material_supplier rms ON rms.fk_raw_material_id = rm.id AND rms.is_default = 1 "
		+ "LEFT JOIN order_crockery oc ON oc.fk_raw_material_id = rm.id AND oc.fk_order_function_id = :orderFunctionId "
		+ "LEFT JOIN order_function ofun ON ofun.id = :orderFunctionId "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN labour_shift ls ON ls.id = ft.fk_shift_id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE rmc.fk_raw_material_category_type_id IN(3, 8) "
		+ "AND (rm.is_active = true OR oc.id IS NOT NULL) "
		+ "AND rmc.id = :rawMaterialCategoryId "
		+ " GROUP BY rm.id "
		+ ") AS t "
		+ "LEFT JOIN measurement ocm ON ocm.id = t.measurementId "
		+ "LEFT JOIN measurement rmm ON rmm.id = t.rawMaterialMeasurementId "
		+ "LEFT JOIN company_setting cs ON cs.is_auto_time_raw_material = 1 "
		+ "ORDER BY t.priority, t.rawMaterialId;"
)

@SqlResultSetMapping(
	name = "findCrockeryDataByOrderFunctionAndRawMaterialCategoryIdResult",
	classes = @ConstructorResult(
		targetClass = GetOrderCrockeryDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "rawMaterialId", type = Long.class),
			@ColumnResult(name = "rawMaterialAgencyId", type = Long.class),
			@ColumnResult(name = "crockeryAgencyId", type = Long.class),
			@ColumnResult(name = "orderTime", type = LocalDateTime.class),
			@ColumnResult(name = "qty", type = Double.class),
			@ColumnResult(name = "measurementId", type = Long.class),
			@ColumnResult(name = "functionDate", type = LocalDateTime.class),
			@ColumnResult(name = "rawMaterialMeasurementId", type = Long.class),
			@ColumnResult(name = "supplierRate", type = Double.class),
			@ColumnResult(name = "price", type = Double.class),
			@ColumnResult(name = "godown", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "findCrockerySupplierContactByOrderFunctionId",
	resultSetMapping = "getSupplierAndRawMaterialCategoryCommonResult",
	query = "SELECT DISTINCT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM contact c "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "LEFT JOIN order_crockery oc ON oc.fk_agency_id = c.id AND oc.fk_order_function_id = :orderFunctionId "
		+ "WHERE cc.fk_contact_category_type_id = 3 AND (c.is_active = true OR oc.fk_agency_id IS NOT NULL) "
		+ "ORDER BY c.id;"
)

@NamedNativeQuery(
	name = "findCrockeryRawMaterialCategoryByOrderFunctionId",
	resultSetMapping = "getSupplierAndRawMaterialCategoryCommonResult",
	query = "SELECT DISTINCT "
		+ "rmc.id, "
		+ "rmc.name_default_lang AS nameDefaultLang, "
		+ "rmc.name_prefer_lang AS namePreferLang, "
		+ "rmc.name_supportive_lang AS nameSupportiveLang "
		+ "FROM raw_material_category rmc "
		+ "INNER JOIN raw_material rm ON rmc.id = rm.fk_raw_material_category_id AND rm.is_active = true "
		+ "LEFT JOIN order_crockery oc ON oc.fk_raw_material_id = rm.id AND oc.fk_order_function_id = :orderFunctionId "
		+ "WHERE rmc.fk_raw_material_category_type_id IN (3, 8) "
		+ "AND rmc.is_active = true "
		+ "AND (oc.fk_order_function_id = :orderFunctionId OR oc.fk_order_function_id IS NULL) "
		+ "ORDER BY rm.priority, rm.id;"
)

@NamedNativeQuery(
	name = "findGeneralFixRawMaterialSupplierContactByOrderFunctionId",
	resultSetMapping = "getSupplierAndRawMaterialCategoryCommonResult",
	query = "SELECT DISTINCT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM contact c "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "LEFT JOIN order_general_fix_raw_material ogfrm ON ogfrm.fk_agency_id = c.id AND ogfrm.fk_order_function_id = :orderFunctionId "
		+ "WHERE cc.fk_contact_category_type_id = 3 AND (c.is_active = true OR ogfrm.fk_agency_id IS NOT NULL) "
		+ "ORDER BY c.id;"
)

@SqlResultSetMapping(
	name = "getSupplierAndRawMaterialCategoryCommonResult",
	classes = @ConstructorResult(
		targetClass = CommonDataForDropDownDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class)
		}
	)
)
@Entity
public class LabourAndOtherManagementNativeQuery extends AuditIdModelOnly {
}