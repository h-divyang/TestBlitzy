package com.catering.dao.order;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.DishCostingDto;
import com.catering.dto.tenant.request.DishCostingReportDto;
import com.catering.dto.tenant.request.OrderDtoForReport;
import com.catering.dto.tenant.request.TotalDishCostingReportDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing a book order in the system.
 * It extends {@link AuditIdModelOnly} to inherit audit-related properties such as creation and modification timestamps.
 * This class is used for querying book orders in native SQL queries within the application.
 * It may contain additional attributes and behaviors related to book order data.
 */
@NamedNativeQuery(
	name = "findOrderForReport",
	resultSetMapping = "findOrderForReportResult",
	query = "SELECT "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND et.name_prefer_lang IS NOT NULL AND et.name_prefer_lang != '' THEN et.name_prefer_lang "
		+ "WHEN :langType = 2 AND et.name_supportive_lang IS NOT NULL AND et.name_supportive_lang != '' THEN et.name_supportive_lang "
		+ "ELSE et.name_default_lang "
		+ "END, '') AS eventName, "
		+ "cod.event_main_date AS eventTime, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END AS venue, "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END, '') AS customerName, "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND c.home_address_prefer_lang IS NOT NULL AND c.home_address_prefer_lang != '' THEN c.home_address_prefer_lang "
		+ "WHEN :langType = 2 AND c.home_address_supportive_lang IS NOT NULL AND c.home_address_supportive_lang != '' THEN c.home_address_supportive_lang "
		+ "ELSE c.home_address_default_lang "
		+ "END, '') AS customerAddress, "
		+ "IFNULL(c.mobile_number, '') AS customerMobileNumber, "
		+ "IFNULL(c.gst_number, '') AS customerGstNo, "
		+ "c.email "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN event_type et ON et.id = cod.fk_event_type_id "
		+ "INNER JOIN contact c ON c.id = cod.fk_contact_customer_id "
		+ "WHERE cod.id = :orderId"
)

@SqlResultSetMapping(
	name = "findOrderForReportResult",
	classes = @ConstructorResult(
		targetClass = OrderDtoForReport.class,
		columns = {
			@ColumnResult(name = "eventName", type = String.class),
			@ColumnResult(name = "eventTime", type = LocalDate.class),
			@ColumnResult(name = "customerName", type = String.class),
			@ColumnResult(name = "customerAddress", type = String.class),
			@ColumnResult(name = "customerMobileNumber", type = String.class),
			@ColumnResult(name = "customerGstNo", type = String.class),
			@ColumnResult(name = "email", type = String.class),
			@ColumnResult(name = "venue", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "dishCostingByOrderId",
	resultSetMapping = "dishCostingByOrderIdResult",
	query = "SELECT "
		+ " ft.name_default_lang AS nameDefaultLang, "
		+ " ft.name_prefer_lang AS namePreferLang, "
		+ " ft.name_supportive_lang AS nameSupportiveLang, "
		+ " ofun.id AS functionId, "
		+ " ROUND(SUM(t.chefLabourCharges)) AS chefLabourCharges, "
		+ " ROUND(SUM(t.labourCharges)) AS labourCharges, "
		+ " ROUND(SUM(t.outsideAgencyCharges)) AS outsideAgencyCharges, "
		+ " ROUND(SUM(t.extraExpenseCharges)) AS extraExpenseCharges, "
		+ " ROUND(SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges) + SUM(t.extraExpenseCharges)) AS totalCharges, "
		+ " ROUND(SUM(t.totalRawMaterialCharges)) AS totalRawMaterialCharges, "
		+ " ROUND(SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges)) AS totalAgencyCharges, "
		+ " IF(ROUND(SUM(t.totalGeneralFixCharges)), ROUND(SUM(t.totalGeneralFixCharges)), 0) AS totalGeneralFixCharges, "
		+ " ROUND(SUM(t.totalCrockeryCharges)) AS totalCrockeryCharges, "
		+ " IF(ROUND(SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges) + SUM(t.extraExpenseCharges) + SUM(t.totalRawMaterialCharges) + SUM(t.totalGeneralFixCharges) + SUM(t.totalCrockeryCharges)), ROUND(SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges) + SUM(t.extraExpenseCharges) + SUM(t.totalRawMaterialCharges) + SUM(t.totalGeneralFixCharges) + SUM(t.totalCrockeryCharges)), 0) AS grandTotal, "
		+ " IF(ROUND((SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges) + SUM(t.extraExpenseCharges) + SUM(t.totalRawMaterialCharges) + SUM(t.totalGeneralFixCharges) + SUM(t.totalCrockeryCharges)) / ofun.person), ROUND((SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges) + SUM(t.extraExpenseCharges) + SUM(t.totalRawMaterialCharges) + SUM(t.totalGeneralFixCharges) + SUM(t.totalCrockeryCharges)) / ofun.person), 0) AS dishCosting "
		+ "FROM (SELECT "
		+ " SUM(omat.total) AS chefLabourCharges, "
		+ " 0 AS labourCharges, "
		+ " 0 AS outsideAgencyCharges, "
		+ " 0 AS extraExpenseCharges, "
		+ " 0 AS totalRawMaterialCharges, "
		+ " 0 AS totalGeneralFixCharges, "
		+ " 0 AS totalCrockeryCharges, "
		+ " ofun.id AS functionId "
		+ "FROM order_function ofun "
		+ "LEFT JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "AND ompmi.order_type = 1 "
		+ "GROUP BY ofun.id "
		+ "UNION "
		+ "SELECT "
		+ " 0 AS chefLabourCharges, "
		+ " SUM(old.labour_price * old.quantity) AS labourCharges, "
		+ " 0 AS outsideAgencyCharges, "
		+ " 0 AS extraExpenseCharges, "
		+ " 0 AS totalRawMaterialCharges, "
		+ " 0 AS totalGeneralFixCharges, "
		+ " 0 AS totalCrockeryCharges, "
		+ " ofun.id AS functionId "
		+ "FROM order_labour_distribution old "
		+ "LEFT JOIN order_function ofun ON ofun.id = old.fk_order_function_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id "
		+ "UNION "
		+ "SELECT "
		+ " 0 AS chefLabourCharges, "
		+ " 0 AS labourCharges, "
		+ " SUM(omat.total) AS outsideAgencyCharges, "
		+ " 0 AS extraExpenseCharges, "
		+ " 0 AS totalRawMaterialCharges, "
		+ " 0 AS totalGeneralFixCharges, "
		+ " 0 AS totalCrockeryCharges, "
		+ " ofun.id AS functionId "
		+ "FROM order_function ofun "
		+ "LEFT JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "LEFT JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId AND ompmi.order_type = 2 "
		+ "GROUP BY ofun.id "
		+ "UNION "
		+ "SELECT "
		+ " 0 AS chefLabourCharges, "
		+ " 0 AS labourCharges, "
		+ " 0 AS outsideAgencyCharges, "
		+ " SUM(ee.qty * ee.price) AS extraExpenseCharges, "
		+ " 0 AS totalRawMaterialCharges, "
		+ " 0 AS totalGeneralFixCharges, "
		+ " 0 AS totalCrockeryCharges, "
		+ " ofun.id AS functionId "
		+ "FROM extra_expense ee "
		+ "LEFT JOIN order_function ofun ON ofun.id = ee.fk_order_function_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id "
		+ "UNION "
		+ "SELECT "
		+ " 0 AS chefLabourCharges, "
		+ " 0 AS outsideAgencyCharges, "
		+ " 0 AS labourCharges, "
		+ " 0 AS extraExpenseCharges, "
		+ " SUM(getSmallestMeasurementValue(t.finalQty, m.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(supplierRate, supplierM_id, FALSE, TRUE), '$.value'))) AS totalRawMaterialCharges, "
		+ " 0 AS totalGeneralFixCharges, "
		+ " 0 AS totalCrockeryCharges, "
		+ " t.functionId "
		+ " FROM (SELECT "
		+ " rm.id AS rawMaterialId, "
		+ " rm.supplier_rate AS supplierRate, "
		+ " supplierM.id AS supplierM_id, "
		+ " supplierM.is_base_unit AS supplierM_isBaseUnit, "
		+ " supplierM.base_unit_equivalent AS supplierM_baseUnitEquivalent, "
		+ " adjustQuantity(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS finalQty, "
		+ " adjustQuantityUnit(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS finalQtyMeasurementId, "
		+ " ofu.id AS functionId "
		+ " FROM raw_material_allocation rma "
		+ " INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ " INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ " INNER JOIN order_function ofu ON ofu.id = omp.fk_order_function_id "
		+ " LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ " LEFT JOIN raw_material rm ON rm.id = CASE WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id ELSE rma.fk_raw_material_id END "
		+ " LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ " LEFT JOIN measurement supplierM ON supplierM.id = rm.fk_measurement_id "
		+ " LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ " LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ " WHERE ofu.fk_customer_order_details_id = :orderId AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ " GROUP BY rawMaterialId, functionId "
		+ " ORDER BY rawMaterialId) as t "
		+ " LEFT JOIN measurement m ON m.id = t.finalQtyMeasurementId "
		+ " GROUP BY t.functionId "
		+ "UNION "
		+ "SELECT "
		+ " 0 AS chefLabourCharges, "
		+ " 0 AS outsideAgencyCharges, "
		+ " 0 AS labourCharges, "
		+ " 0 AS extraExpenseCharges, "
		+ " 0 AS totalRawMaterialCharges, "
		+ "ROUND(SUM(IF(ogfrm.price IS NULL OR ogfrm.price <= 0, getSmallestMeasurementValue(ogfrm.qty, ogfrmM.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(rm.supplier_rate, rmM.id, FALSE, TRUE), '$.value')), ogfrm.price))) AS totalGeneralFixCharges, "
		+ " 0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM order_function ofun "
		+ "LEFT JOIN raw_material rm ON rm.is_general_fix_raw_material = 1 "
		+ "LEFT JOIN order_general_fix_raw_material ogfrm ON ogfrm.fk_raw_material_id = rm.id AND ogfrm.fk_order_function_id = ofun.id "
		+ "LEFT JOIN measurement rmM ON rmM.id = rm.fk_measurement_id "
		+ "LEFT JOIN measurement ogfrmM ON ogfrmM.id = ogfrm.fk_measurement_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id "
		+ "UNION "
		+ "SELECT "
		+ " 0 AS chefLabourCharges,"
		+ " 0 AS outsideAgencyCharges,"
		+ " 0 AS labourCharges,"
		+ " 0 AS extraExpenseCharges,"
		+ " 0 AS totalRawMaterialCharges,"
		+ " 0 AS totalGeneralFixCharges,"
		+ " SUM(oc.price) AS totalCrockeryCharges,"
		+ " ofun.id AS functionId"
		+ " FROM order_crockery oc"
		+ " LEFT JOIN order_function ofun ON ofun.id = oc.fk_order_function_id"
		+ " WHERE ofun.fk_customer_order_details_id = :orderId"
		+ " GROUP BY ofun.id ) AS t "
		+ "INNER JOIN order_function ofun ON ofun.id = t.functionId "
		+ "INNER JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "GROUP BY t.functionId "
		+ "ORDER BY ft.id"
)

@SqlResultSetMapping(
	name = "dishCostingByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = DishCostingDto.class,
		columns = {
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "chefLabourCharges", type = Double.class),
			@ColumnResult(name = "labourCharges", type = Double.class),
			@ColumnResult(name = "outsideAgencyCharges", type = Double.class),
			@ColumnResult(name = "extraExpenseCharges", type = Double.class),
			@ColumnResult(name = "totalCharges", type = Double.class),
			@ColumnResult(name = "totalRawMaterialCharges", type = Double.class),
			@ColumnResult(name = "totalAgencyCharges", type = Double.class),
			@ColumnResult(name = "totalGeneralFixCharges", type = Double.class),
			@ColumnResult(name = "totalCrockeryCharges", type = Double.class),
			@ColumnResult(name = "grandTotal", type = Double.class),
			@ColumnResult(name = "dishCosting", type = Double.class),
			@ColumnResult(name = "functionId", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateDishCostingReport",
	resultSetMapping = "dishCostingReportByOrderIdResult",
	query = "SELECT "
		+ "ofun.id, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "IFNULL(ROUND(SUM(t.chefLabourCharges)), 0) AS chefLabourCharges, "
		+ "IFNULL(ROUND(SUM(t.labourCharges)), 0) AS labourCharges, "
		+ "IFNULL(ROUND(SUM(t.outsideAgencyCharges)), 0) AS outsideAgencyCharges, "
		+ "IFNULL(ROUND(SUM(t.extraExpenseCharges)), 0) AS extraExpenseCharges, "
		+ "IFNULL(ROUND(SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges) + SUM(t.extraExpenseCharges)), 0) AS totalCharges, "
		+ "IFNULL(ROUND(SUM(t.totalRawMaterialCharges)), 0) AS totalRawMaterialCharges, "
		+ "IFNULL(ROUND(SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges)), 0) AS totalAgencyCharges, "
		+ "IFNULL(ROUND(SUM(t.totalGeneralFixCharges)), 0) AS totalGeneralFixCharges, "
		+ "IFNULL(ROUND(SUM(t.totalCrockeryCharges)), 0) AS totalCrockeryCharges,"
		+ "IFNULL(ROUND(SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges) + SUM(t.extraExpenseCharges) + SUM(t.totalRawMaterialCharges) + SUM(t.totalGeneralFixCharges) + SUM(t.totalCrockeryCharges)), 0) AS grandTotal, "
		+ "IFNULL(ROUND((SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges) + SUM(t.extraExpenseCharges) + SUM(t.totalRawMaterialCharges) + SUM(t.totalGeneralFixCharges) + SUM(t.totalCrockeryCharges)) / ofun.person), 0) AS dishCosting "
		+ "FROM (SELECT "
		+ "SUM(omat.total) AS chefLabourCharges, "
		+ "0 AS labourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM order_function ofun "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "AND ompmi.order_type = 1 "
		+ "GROUP BY ofun.id "
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges, "
		+ "SUM(old.labour_price * old.quantity) AS labourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM order_labour_distribution old "
		+ "INNER JOIN order_function ofun ON ofun.id = old.fk_order_function_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id " 
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges, "
		+ "0 AS labourCharges, "
		+ "SUM(omat.total) AS outsideAgencyCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM order_function ofun "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId AND ompmi.order_type = 2 "
		+ "GROUP BY ofun.id "
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges, "
		+ "0 AS labourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "SUM(ee.qty * ee.price) AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM extra_expense ee "
		+ "LEFT JOIN order_function ofun ON ofun.id = ee.fk_order_function_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id "
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "0 AS labourCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "SUM(IF(m.is_base_unit = 1, t.finalQty, t.finalQty * m.base_unit_equivalent) * t.supplierRate / IF(t.supplierM_isBaseUnit = 1, 1, 1 * t.supplierM_baseUnitEquivalent)) AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "t.functionId "
		+ "FROM (SELECT "
		+ "rm.id AS rawMaterialId, "
		+ "rm.supplier_rate AS supplierRate, "
		+ "supplierM.is_base_unit AS supplierM_isBaseUnit, "
		+ "supplierM.base_unit_equivalent AS supplierM_baseUnitEquivalent, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS finalQty, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS finalQtyMeasurementId, "
		+ "ofu.id AS functionId "
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
		+ "WHERE ofu.fk_customer_order_details_id = :orderId AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "GROUP BY rawMaterialId, ofu.id "
		+ ") AS t "
		+ "LEFT JOIN measurement m ON m.id = t.finalQtyMeasurementId "
		+ "GROUP BY t.functionId "
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "0 AS labourCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "ROUND(SUM(IF(ogfrm.price IS NULL OR ogfrm.price <= 0, getSmallestMeasurementValue(ogfrm.qty, ogfrmM.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(rm.supplier_rate, rmM.id, FALSE, TRUE), '$.value')), ogfrm.price))) AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM order_function ofun "
		+ "INNER JOIN raw_material rm ON rm.is_general_fix_raw_material = 1 "
		+ "INNER JOIN order_general_fix_raw_material ogfrm ON ogfrm.fk_raw_material_id = rm.id AND ogfrm.fk_order_function_id = ofun.id "
		+ "INNER JOIN measurement rmM ON rmM.id = rm.fk_measurement_id "
		+ "INNER JOIN measurement ogfrmM ON ogfrmM.id = ogfrm.fk_measurement_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id "
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "0 AS labourCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "SUM(oc.price) AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM order_crockery oc "
		+ "RIGHT JOIN order_function ofun ON ofun.id = oc.fk_order_function_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id "
		+ ") AS t "
		+ "INNER JOIN order_function ofun ON ofun.id = t.functionId "
		+ "INNER JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "GROUP BY ofun.id "
		+ "ORDER BY ofun.sequence, ft.id"
)

@SqlResultSetMapping(
	name = "dishCostingReportByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = DishCostingReportDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "chefLabourCharges", type = Double.class),
			@ColumnResult(name = "labourCharges", type = Double.class),
			@ColumnResult(name = "outsideAgencyCharges", type = Double.class),
			@ColumnResult(name = "extraExpenseCharges", type = Double.class),
			@ColumnResult(name = "totalCharges", type = Double.class),
			@ColumnResult(name = "totalRawMaterialCharges", type = Double.class),
			@ColumnResult(name = "totalAgencyCharges", type = Double.class),
			@ColumnResult(name = "totalGeneralFixCharges", type = Double.class),
			@ColumnResult(name = "totalCrockeryCharges", type = Double.class),
			@ColumnResult(name = "grandTotal", type = Double.class),
			@ColumnResult(name = "dishCosting", type = Double.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateTotalDishCostingReport",
	resultSetMapping = "totalDishCostingReportByOrderIdResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName," 
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "IFNULL(ROUND(SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges) + SUM(t.extraExpenseCharges) + SUM(t.totalRawMaterialCharges) + SUM(t.totalGeneralFixCharges) + SUM(t.totalCrockeryCharges)), 0) AS grandTotal," 
		+ "IFNULL(ROUND((SUM(t.chefLabourCharges) + SUM(t.labourCharges) + SUM(t.outsideAgencyCharges) + SUM(t.extraExpenseCharges) + SUM(t.totalRawMaterialCharges) + SUM(t.totalGeneralFixCharges) + SUM(t.totalCrockeryCharges)) / ofun.person), 0) AS dishCosting "
		+ "FROM (" 
		+ "SELECT "
		+ "SUM(omat.total) AS chefLabourCharges, "
		+ "0 AS labourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM order_function ofun "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "AND ompmi.order_type = 1 "
		+ "GROUP BY ofun.id "
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges," 
		+ "SUM(old.labour_price * old.quantity) AS labourCharges," 
		+ "0 AS outsideAgencyCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM order_labour_distribution old "
		+ "INNER JOIN order_function ofun ON ofun.id = old.fk_order_function_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id " 
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges," 
		+ "0 AS labourCharges," 
		+ "SUM(omat.total) AS outsideAgencyCharges,"
		+ "0 AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM order_function ofun "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "AND ompmi.order_type = 2 "
		+ "GROUP BY ofun.id "
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges, "
		+ "0 AS labourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "SUM(ee.qty * ee.price) AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM extra_expense ee "
		+ "LEFT JOIN order_function ofun ON ofun.id = ee.fk_order_function_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id "
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges, "
		+ "0 AS labourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "SUM( "
		+ "IF(m.is_base_unit = 1, t.finalQty, t.finalQty * m.base_unit_equivalent) * t.supplierRate / "
		+ "IF(t.supplierM_isBaseUnit = 1, 1, t.supplierM_baseUnitEquivalent)) AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "t.functionId "
		+ "FROM (" 
		+ "SELECT "
		+ "rm.id AS rawMaterialId, "
		+ "rm.supplier_rate AS supplierRate, "
		+ "supplierM.is_base_unit AS supplierM_isBaseUnit, "
		+ "supplierM.base_unit_equivalent AS supplierM_baseUnitEquivalent, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS finalQty, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS finalQtyMeasurementId, "
		+ "ofu.id AS functionId "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function ofu ON ofu.id = omp.fk_order_function_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = COALESCE(mirm.fk_raw_material_id, rma.fk_raw_material_id) "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ "LEFT JOIN measurement supplierM ON supplierM.id = rm.fk_measurement_id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE ofu.fk_customer_order_details_id = :orderId "
		+ "AND oni.id IS NULL "
		+ "AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "GROUP BY rawMaterialId, ofu.id "
		+ ") AS t "
		+ "LEFT JOIN measurement m ON m.id = t.finalQtyMeasurementId "
		+ "GROUP BY t.functionId "
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges, "
		+ "0 AS labourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "ROUND(SUM( "
		+ "IF(ogfrm.price IS NULL OR ogfrm.price <= 0, getSmallestMeasurementValue(ogfrm.qty, ogfrmM.id) * JSON_UNQUOTE(JSON_EXTRACT(getAdjustedAndExtraQuantity(rm.supplier_rate, rmM.id, FALSE, TRUE), '$.value')), ogfrm.price)) "
		+ ") AS totalGeneralFixCharges, "
		+ "0 AS totalCrockeryCharges, "
		+ "ofun.id AS functionId "
		+ "FROM order_function ofun "
		+ "INNER JOIN raw_material rm ON rm.is_general_fix_raw_material = 1 "
		+ "INNER JOIN order_general_fix_raw_material ogfrm ON ogfrm.fk_raw_material_id = rm.id AND ogfrm.fk_order_function_id = ofun.id "
		+ "INNER JOIN measurement rmM ON rmM.id = rm.fk_measurement_id "
		+ "INNER JOIN measurement ogfrmM ON ogfrmM.id = ogfrm.fk_measurement_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id "
		+ "UNION ALL "
		+ "SELECT "
		+ "0 AS chefLabourCharges, "
		+ "0 AS labourCharges, "
		+ "0 AS outsideAgencyCharges, "
		+ "0 AS extraExpenseCharges, "
		+ "0 AS totalRawMaterialCharges, "
		+ "0 AS totalGeneralFixCharges, "
		+ "SUM(oc.price) AS totalCrockeryCharges, "
		+ "ofun.id AS functionId " 
		+ "FROM order_crockery oc "
		+ "RIGHT JOIN order_function ofun ON ofun.id = oc.fk_order_function_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId "
		+ "GROUP BY ofun.id "
		+ ") AS t "
		+ "INNER JOIN order_function ofun ON ofun.id = t.functionId "
		+ "INNER JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "GROUP BY ofun.id "
		+ "ORDER BY ofun.sequence, ft.id"
)

@SqlResultSetMapping(
	name = "totalDishCostingReportByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = TotalDishCostingReportDto.class,
		columns = {
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "grandTotal", type = Double.class),
			@ColumnResult(name = "dishCosting", type = Double.class)
		}
	)
)

@NamedNativeQuery(
	name = "getCustomerNameForTableMenuFooterNotes",
	resultSetMapping = "getCustomerNameForTableMenuFooterNotesResult",
	query = "SELECT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN contact c ON cod.fk_contact_customer_id = c.id "
		+ "WHERE cod.id = :orderId"
)

@SqlResultSetMapping(
	name = "getCustomerNameForTableMenuFooterNotesResult",
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

@Entity
public class BookOrderNativeQuery extends AuditIdModelOnly {}