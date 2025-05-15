package com.catering.dao.date_wise_reports;

import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.DateWiseChefLabourReportDto;
import com.catering.dto.tenant.request.DateWiseLabourReoportDto;
import com.catering.dto.tenant.request.DateWiseMenuItemReportDto;
import com.catering.dto.tenant.request.DateWiseOrderBookingReportDto;
import com.catering.dto.tenant.request.DateWiseOutsideOrderReportDto;
import com.catering.dto.tenant.request.DateWiseRawMaterialReportDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing date-wise reports for native queries.
 * 
 * This class extends {@link AuditIdModelOnly}, inheriting auditing fields 
 * such as ID and other metadata. It serves as a mapping for the corresponding
 * database table used for managing date-specific report data.
 * 
 * Annotations:
 * - {@code @Entity}: Indicates that this class is a JPA entity and will be mapped to a database table.
 */
@NamedNativeQuery(
	name = "getSupplierCategoryDropDownData",
	resultSetMapping = "getDropDownDataResult",
	query = "SELECT "
		+ "cc.id AS id, "
		+ "cc.name_default_lang AS nameDefaultLang, "
		+ "cc.name_prefer_lang AS namePreferLang, "
		+ "cc.name_supportive_lang AS nameSupportiveLang "
		+ "FROM contact_category cc "
		+ "WHERE cc.fk_contact_category_type_id = :contactCategoryTypeId AND cc.is_active = TRUE "
		+ "ORDER BY cc.priority ASC, cc.id"
)

@NamedNativeQuery(
	name = "getSupplierNameDropDownData",
	resultSetMapping = "getDropDownDataResult",
	query = "SELECT DISTINCT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang , "
		+ "c.name_prefer_lang AS namePreferLang , "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM contact c "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "INNER JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "INNER JOIN contact_category_type cct ON cct.id = cc.fk_contact_category_type_id "
		+ "WHERE IF (0 = :supplierCategoryId , TRUE, contactCategories.fk_contact_category_id = :supplierCategoryId) AND c.is_active = TRUE AND cct.id = :contactCategoryTypeId"
)

@NamedNativeQuery(
	name = "getMenuItemCategoryDropDownData",
	resultSetMapping = "getDropDownDataResult",
	query = "SELECT "
		+ "mic.id, "
		+ "mic.name_default_lang AS nameDefaultLang, "
		+ "mic.name_prefer_lang AS namePreferLang , "
		+ "mic.name_supportive_lang AS nameSupportiveLang "
		+ "FROM menu_item_category mic "
		+ "WHERE mic.is_active = TRUE"
)

@NamedNativeQuery(
	name = "getMenuItemSubCategoryDropDownData",
	resultSetMapping = "getDropDownDataResult",
	query = "SELECT "
		+ "misc.id, "
		+ "misc.name_default_lang AS nameDefaultLang, "
		+ "misc.name_prefer_lang AS namePreferLang, "
		+ "misc.name_supportive_lang AS nameSupportiveLang "
		+ "FROM menu_item_sub_category misc "
		+ "WHERE misc.is_active = TRUE"	
)

@NamedNativeQuery(
	name = "getKitchenAreaDropDownData",
	resultSetMapping = "getDropDownDataResult",
	query = "SELECT "
		+ "ka.id, "
		+ "ka.name_default_lang AS nameDefaultLang, "
		+ "ka.name_prefer_lang AS namePreferLang , "
		+ "ka.name_supportive_lang AS nameSupportiveLang "
		+ "FROM kitchen_area ka "
		+ "WHERE ka.is_active = TRUE"
)

@NamedNativeQuery(
	name = "getCustomerContactsDropDownData",
	resultSetMapping = "getDropDownDataResult",
	query = "SELECT DISTINCT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM contact c "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "INNER JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE cc.fk_contact_category_type_id = :customerContactId "
)

@NamedNativeQuery(
	name = "getSupplierCategoryDropDownDataOfRawMaterial",
	resultSetMapping = "getDropDownDataResult",
	query = "SELECT DISTINCT "
		+ "cc.id, "
		+ "cc.name_default_lang AS nameDefaultLang, "
		+ "cc.name_prefer_lang AS namePreferLang, "
		+ "cc.name_supportive_lang AS nameSupportiveLang "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN contact c ON c.id = rma.fk_contact_agency_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "INNER JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "ORDER BY cc.priority ASC, cc.id "
)

@NamedNativeQuery(
	name = "getSupplierDropDownDataOfRawMaterial",
	resultSetMapping = "getDropDownDataResult",
	query = "SELECT DISTINCT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN contact c ON c.id = rma.fk_contact_agency_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "WHERE (contactCategories.fk_contact_category_id = :contactCategoryTypeId OR :contactCategoryTypeId = 0) "
)

@NamedNativeQuery(
	name = "getContactNameDropDownData",
	resultSetMapping = "getDropDownDataResult",
	query = "SELECT DISTINCT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN contact c ON c.id = cod.fk_contact_customer_id "
		+ "WHERE (cod.fk_order_status_id IN (:statusIds) OR 0 IN (:statusIds)) "
)

@SqlResultSetMapping(
	name = "getDropDownDataResult",
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
	name = "generateDateWiseOutsideOrderReport",
	resultSetMapping = "generateDatewiseOutsideOrderReportResult",
	query = "SELECT DISTINCT "
		+ "CASE "
		+ " WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ " WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ " ELSE c.name_default_lang "
		+ "END AS supplierName, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ompmi.order_date AS orderDate, "
		+ "CASE "
		+ "WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ "WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ "WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ "WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ "ELSE '' "
		+ "END AS timeInWord, "
		+ "CASE "
		+ " WHEN ompmi.fk_godown_id IS NULL AND o_fun.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN ompmi.fk_godown_id IS NULL THEN "
		+ " CASE "
		+ " WHEN :langType = 1 AND o_fun.function_address_prefer_lang IS NOT NULL AND o_fun.function_address_prefer_lang != '' THEN o_fun.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND o_fun.function_address_supportive_lang IS NOT NULL AND o_fun.function_address_supportive_lang != '' THEN o_fun.function_address_supportive_lang "
		+ " ELSE o_fun.function_address_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ " WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ " WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ " ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS menuItemName, "
		+ "COALESCE(omat.quantity, 0) AS quantity, "
		+ "CASE "
		+ " WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ " WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ " ELSE m.symbol_default_lang "
		+ "END AS unit, "
		+ "CASE "
		+ "WHEN (m.decimal_limit_qty = -1) AND (omat.quantity % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0 "
		+ "WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ " WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ " ELSE mic.name_default_lang "
		+ "END AS menuItemCategory, "
		+ "COALESCE(omat.price, 0) AS rate, "
		+ "(COALESCE(omat.quantity, 0) * COALESCE(omat.price, 0)) AS total "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function o_fun ON o_fun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = o_fun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "LEFT JOIN godown g ON g.id = ompmi.fk_godown_id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = mi.fk_menu_item_category_id "
		+ "INNER JOIN contact c ON c.id = omat.fk_contact_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN measurement m on m.id = omat.fk_unit_id "
		+ "WHERE DATE(ompmi.order_date) BETWEEN DATE(:startDate) AND DATE(:endDate) AND ompmi.order_type = 2 AND IF(0 IN (:supplierCategoryIds), TRUE, contactCategories.fk_contact_category_id IN (:supplierCategoryIds)) AND IF(0 IN (:supplierNameIds), TRUE, c.id IN (:supplierNameIds)) AND IF(0 IN (:categoryIds), TRUE, mic.id IN (:categoryIds)) AND IF(0 IN (:statusIds), TRUE, cod.fk_order_status_id IN (:statusIds))"
		+ "ORDER BY ompmi.order_date, contactCategories.fk_contact_category_id, c.id, cod.id, mi.id, mic.id"
)

@SqlResultSetMapping(
	name = "generateDatewiseOutsideOrderReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseOutsideOrderReportDto.class,
		columns = {
			@ColumnResult(name = "supplierName", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "orderDate", type = LocalDateTime.class),
			@ColumnResult(name = "timeInWord", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "menuItemName", type = String.class),
			@ColumnResult(name = "quantity", type = Double.class),
			@ColumnResult(name = "unit", type = String.class),
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "total", type = Double.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateDateWiseRawMaterialReportWithPrice",
	resultSetMapping = "generateDateWiseRawMaterialReportWithPriceResult",
	query = "SELECT "
		+ "t.supplierCategory, "
		+ "t.supplierName, "
		+ "t.mobileNumber,"
		+ "t.rawMaterialName, "
		+ "t.quantity, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS unit, "
		+ "t.orderDate, "
		+ "t.timeInWord, "
		+ "t.supplierPriority, "
		+ "t.venue, "
		+ "t.contactAgencyId, "
		+ "t.rawMaterialCategoryId, "
		+ "CASE "
		+ "WHEN (m.decimal_limit_qty = -1) AND (t.quantity % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0 "
		+ "WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM "
		+ "(SELECT "
		+ "t.orderId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS supplierCategory, "
		+ "CASE "
		+ "WHEN :langType = 1 AND contact_agency.name_prefer_lang IS NOT NULL AND contact_agency.name_prefer_lang != '' THEN contact_agency.name_prefer_lang "
		+ "WHEN :langType = 2 AND contact_agency.name_supportive_lang IS NOT NULL AND contact_agency.name_supportive_lang != '' THEN contact_agency.name_supportive_lang "
		+ "ELSE contact_agency.name_default_lang "
		+ "END AS supplierName, "
		+ "contact_agency.mobile_number AS mobileNumber, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(t.quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS quantity, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(t.quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS unit,"
		+ "t.venue, "
		+ "t.orderDate, "
		+ "CASE "
		+ "WHEN TIME(CONVERT_TZ(t.orderDate, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ "WHEN TIME(CONVERT_TZ(t.orderDate, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ "WHEN TIME(CONVERT_TZ(t.orderDate, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ "WHEN TIME(CONVERT_TZ(t.orderDate, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(t.orderDate, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ "ELSE '' "
		+ "END AS timeInWord, "
		+ "IF(contact_agency.id, 0, 1) AS supplierPriority, "
		+ "contact_agency.id AS contactAgencyId, "
		+ "rmc.id AS rawMaterialCategoryId, "
		+ "rm.id AS rawMaterialId, "
		+ "rmc.priority AS rawMaterialCategoryPriority, "
		+ "rm.priority AS rawMaterialPriority "
		+ "FROM "
		+ "((SELECT "
		+ "cod.id as orderId, "
		+ "contact_agency.id AS contactAgencyId, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), 0) AS quantity, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), 0) AS unit, "
		+ "CASE "
		+ "WHEN rma.fk_godown_id IS NULL AND of2.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN rma.fk_godown_id IS NULL THEN "
		+ " CASE "
		+ " WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != ''THEN of2.function_address_supportive_lang "
		+ " ELSE of2.function_address_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ " WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ " WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ " ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "rma.order_time AS orderDate, "
		+ "rmc.id AS rawMaterialCategoryId, "
		+ "rm.id AS rawMaterialId, "
		+ "rmc.priority AS rawMaterialCategoryPriority, "
		+ "rm.priority AS rawMaterialPriority "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN raw_material_allocation rma ON rma.fk_menu_preparation_menu_item_id = ompmi.id "
		+ "LEFT JOIN godown g ON g.id = rma.fk_godown_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE "
		+ "WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id "
		+ "ELSE rma.fk_raw_material_id "
		+ "END "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "LEFT JOIN contact contact_agency ON contact_agency.id = rma.fk_contact_agency_id "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = contact_agency.id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "WHERE DATE(rma.order_time) BETWEEN DATE(:startDate) AND DATE(:endDate) "
		+ "AND (contactCategories.fk_contact_category_id IN (:supplierCategoryIds) OR 0 IN (:supplierCategoryIds)) "
		+ "AND (contact_agency.id IN (:supplierNameIds) OR 0 IN (:supplierNameIds)) "
		+ "AND IF(0 IN (:rawMaterialCategoryId), TRUE, rmc.id IN (:rawMaterialCategoryId)) "
		+ "AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "AND IF(0 IN (:statusIds), TRUE, cod.fk_order_status_id IN (:statusIds)) "
		+ "GROUP BY contact_agency.id, rm.id, rma.order_time, venue) "
		+ "UNION ALL "
		+ "(SELECT "
		+ "o.id as orderId, "
		+ "contact_agency.id AS contactAgencyId, "
		+ "adjustQuantity(SUM(DISTINCT getSmallestMeasurementValue(rmae.quantity, finalM.id)), getSmallestMeasurementId(finalM.id), 0) AS quantity, "
		+ "adjustQuantityUnit(SUM(DISTINCT getSmallestMeasurementValue(rmae.quantity, finalM.id)), getSmallestMeasurementId(finalM.id), 0) AS unit, "
		+ "CASE "
		+ "WHEN rmae.fk_godown_id IS NULL THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND o.venue_prefer_lang IS NOT NULL AND o.venue_prefer_lang != '' THEN o.venue_prefer_lang "
		+ "WHEN :langType = 2 AND o.venue_supportive_lang IS NOT NULL AND o.venue_supportive_lang != '' THEN o.venue_supportive_lang "
		+ "ELSE o.venue_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ "WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ "ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "rmae.order_time AS orderDate, "
		+ "rmc.id AS rawMaterialCategoryId, "
		+ "rm.id AS rawMaterialId, "
		+ "rmc.priority AS rawMaterialCategoryPriority, "
		+ "rm.priority AS rawMaterialPriority "
		+ "FROM customer_order_details o "
		+ "INNER JOIN ( "
		+ "SELECT "
		+ "fk_customer_order_details_id, "
		+ "MAX(id) AS max_function_id "
		+ "FROM order_function "
		+ "GROUP BY fk_customer_order_details_id "
		+ ") mof ON mof.fk_customer_order_details_id = o.id "
		+ "INNER JOIN order_function of2 ON of2.id = mof.max_function_id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN raw_material_allocation rma ON rma.fk_menu_preparation_menu_item_id = ompmi.id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE "
		+ "WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id "
		+ "ELSE rma.fk_raw_material_id "
		+ "END "
		+ "INNER JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "LEFT JOIN raw_material_allocation_extra rmae ON rmae.fk_raw_material_id = rm.id AND rmae.fk_customer_order_details_id = o.id "
		+ "LEFT JOIN godown g ON g.id = rmae.fk_godown_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = rmae.fk_measurement_id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "LEFT JOIN contact contact_agency ON contact_agency.id = rmae.fk_contact_agency_id "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = contact_agency.id "
		+ "WHERE rmae.quantity > 0 AND DATE(rmae.order_time) BETWEEN DATE(:startDate) AND DATE(:endDate) "
		+ "AND (contactCategories.fk_contact_category_id IN (:supplierCategoryIds) OR 0 IN (:supplierCategoryIds)) "
		+ "AND (contact_agency.id IN (:supplierNameIds) OR 0 IN (:supplierNameIds)) "
		+ "AND IF(0 IN (:rawMaterialCategoryId), TRUE, rmc.id IN (:rawMaterialCategoryId)) "
		+ "AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "AND IF(0 IN (:statusIds), TRUE, o.fk_order_status_id IN (:statusIds)) "
		+ "GROUP BY  contact_agency.id, rm.id, rmae.order_time, venue)) AS t "
		+ "LEFT JOIN measurement m ON m.id = t.unit "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = t.rawMaterialCategoryId "
		+ "LEFT JOIN raw_material rm ON rm.id = t.rawMaterialId "
		+ "LEFT JOIN contact contact_agency ON contact_agency.id = t.contactAgencyId "
		+ "GROUP BY contact_agency.id, rm.id, t.orderDate, venue) AS t "
		+ "LEFT JOIN measurement m ON m.id = t.unit "
		+ "HAVING quantity IS NOT NULL AND quantity != '' AND quantity != 0 "
		+ "ORDER BY t.supplierPriority, t.rawMaterialCategoryPriority, t.rawMaterialCategoryId, t.contactAgencyId, t.orderDate, t.orderId, t.rawMaterialPriority, t.rawMaterialId"
)

@NamedNativeQuery(
	name = "generateDateWiseTotalRawMaterialReportWithPrice",
	resultSetMapping = "generateDateWiseRawMaterialReportWithPriceResult",
	query = "SELECT "
		+ "t.supplierCategory, "
		+ "t.supplierName, "
		+ "t.mobileNumber,"
		+ "t.rawMaterialName, "
		+ "t.quantity, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS unit, "
		+ "t.orderDate, "
		+ "t.timeInWord, "
		+ "t.supplierPriority, "
		+ "t.venue, "
		+ "t.contactAgencyId, "
		+ "t.rawMaterialCategoryId, "
		+ "CASE "
		+ "WHEN (m.decimal_limit_qty = -1) AND (t.quantity % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0 "
		+ "WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM "
		+ "(SELECT "
		+ "t.orderId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS supplierCategory, "
		+ "CASE "
		+ "WHEN :langType = 1 AND contact_agency.name_prefer_lang IS NOT NULL AND contact_agency.name_prefer_lang != '' THEN contact_agency.name_prefer_lang "
		+ "WHEN :langType = 2 AND contact_agency.name_supportive_lang IS NOT NULL AND contact_agency.name_supportive_lang != '' THEN contact_agency.name_supportive_lang "
		+ "ELSE contact_agency.name_default_lang "
		+ "END AS supplierName, "
		+ "contact_agency.mobile_number AS mobileNumber, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(t.quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS quantity, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(t.quantity, m.id)), getSmallestMeasurementId(m.id), 0) AS unit, "
		+ "t.venue, "
		+ "t.orderDate, "
		+ "CASE "
		+ "WHEN TIME(CONVERT_TZ(t.orderDate, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ "WHEN TIME(CONVERT_TZ(t.orderDate, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ "WHEN TIME(CONVERT_TZ(t.orderDate, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ "WHEN TIME(CONVERT_TZ(t.orderDate, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(t.orderDate, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ "ELSE '' "
		+ "END AS timeInWord, "
		+ "IF(contact_agency.id, 0, 1) AS supplierPriority, "
		+ "contact_agency.id AS contactAgencyId, "
		+ "rmc.id AS rawMaterialCategoryId, "
		+ "rm.id AS rawMaterialId, "
		+ "rmc.priority AS rawMaterialCategoryPriority, "
		+ "rm.priority AS rawMaterialPriority "
		+ "FROM "
		+ "((SELECT "
		+ "cod.id as orderId, "
		+ "contact_agency.id AS contactAgencyId, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), 0) AS quantity, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), 0) AS unit, "
		+ "CASE "
		+ "WHEN rma.fk_godown_id IS NULL AND of2.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN rma.fk_godown_id IS NULL THEN "
		+ " CASE "
		+ " WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != ''THEN of2.function_address_supportive_lang "
		+ " ELSE of2.function_address_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ " WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ " WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ " ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "rma.order_time AS orderDate, "
		+ "rmc.id AS rawMaterialCategoryId, "
		+ "rm.id AS rawMaterialId, "
		+ "rmc.priority AS rawMaterialCategoryPriority, "
		+ "rm.priority AS rawMaterialPriority "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN raw_material_allocation rma ON rma.fk_menu_preparation_menu_item_id = ompmi.id "
		+ "LEFT JOIN godown g ON g.id = rma.fk_godown_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE "
		+ "WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id "
		+ "ELSE rma.fk_raw_material_id "
		+ "END "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "LEFT JOIN contact contact_agency ON contact_agency.id = rma.fk_contact_agency_id "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = contact_agency.id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "WHERE DATE(rma.order_time) BETWEEN DATE(:startDate) AND DATE(:endDate) "
		+ "AND (contactCategories.fk_contact_category_id IN (:supplierCategoryIds) OR 0 IN (:supplierCategoryIds)) "
		+ "AND (contact_agency.id IN (:supplierNameIds) OR 0 IN (:supplierNameIds)) "
		+ "AND IF(0 IN (:rawMaterialCategoryId), TRUE, rmc.id IN (:rawMaterialCategoryId)) "
		+ "AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "AND IF(0 IN (:statusIds), TRUE, cod.fk_order_status_id IN (:statusIds)) "
		+ "GROUP BY contact_agency.id, rm.id) "
		+ "UNION ALL "
		+ "(SELECT "
		+ "o.id as orderId, "
		+ "contact_agency.id AS contactAgencyId, "
		+ "adjustQuantity(SUM(DISTINCT getSmallestMeasurementValue(rmae.quantity, finalM.id)), getSmallestMeasurementId(finalM.id), 0) AS quantity, "
		+ "adjustQuantityUnit(SUM(DISTINCT getSmallestMeasurementValue(rmae.quantity, finalM.id)), getSmallestMeasurementId(finalM.id), 0) AS unit, "
		+ "CASE "
		+ "WHEN rmae.fk_godown_id IS NULL THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND o.venue_prefer_lang IS NOT NULL AND o.venue_prefer_lang != '' THEN o.venue_prefer_lang "
		+ "WHEN :langType = 2 AND o.venue_supportive_lang IS NOT NULL AND o.venue_supportive_lang != '' THEN o.venue_supportive_lang "
		+ "ELSE o.venue_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ "WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ "ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "rmae.order_time AS orderDate, "
		+ "rmc.id AS rawMaterialCategoryId, "
		+ "rm.id AS rawMaterialId, "
		+ "rmc.priority AS rawMaterialCategoryPriority, "
		+ "rm.priority AS rawMaterialPriority "
		+ "FROM customer_order_details o "
		+ "INNER JOIN ( "
		+ "SELECT "
		+ "fk_customer_order_details_id, "
		+ "MAX(id) AS max_function_id "
		+ "FROM order_function "
		+ "GROUP BY fk_customer_order_details_id "
		+ ") mof ON mof.fk_customer_order_details_id = o.id "
		+ "INNER JOIN order_function of2 ON of2.id = mof.max_function_id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN raw_material_allocation rma ON rma.fk_menu_preparation_menu_item_id = ompmi.id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE "
		+ "WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id "
		+ "ELSE rma.fk_raw_material_id "
		+ "END "
		+ "INNER JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "LEFT JOIN raw_material_allocation_extra rmae ON rmae.fk_raw_material_id = rm.id AND rmae.fk_customer_order_details_id = o.id "
		+ "LEFT JOIN godown g ON g.id = rmae.fk_godown_id "
		+ "LEFT JOIN measurement finalM ON finalM.id = rmae.fk_measurement_id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "LEFT JOIN contact contact_agency ON contact_agency.id = rmae.fk_contact_agency_id "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = contact_agency.id "
		+ "WHERE rmae.quantity > 0 AND DATE(rmae.order_time) BETWEEN DATE(:startDate) AND DATE(:endDate) "
		+ "AND (contactCategories.fk_contact_category_id IN (:supplierCategoryIds) OR 0 IN (:supplierCategoryIds)) "
		+ "AND (contact_agency.id IN (:supplierNameIds) OR 0 IN (:supplierNameIds)) "
		+ "AND IF(0 IN (:rawMaterialCategoryId), TRUE, rmc.id IN (:rawMaterialCategoryId)) "
		+ "AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "AND IF(0 IN (:statusIds), TRUE, o.fk_order_status_id IN (:statusIds)) "
		+ "GROUP BY  contact_agency.id, rm.id)) AS t "
		+ "LEFT JOIN measurement m ON m.id = t.unit "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = t.rawMaterialCategoryId "
		+ "LEFT JOIN raw_material rm ON rm.id = t.rawMaterialId "
		+ "LEFT JOIN contact contact_agency ON contact_agency.id = t.contactAgencyId "
		+ "GROUP BY contact_agency.id, rm.id) AS t "
		+ "LEFT JOIN measurement m ON m.id = t.unit "
		+ "HAVING quantity IS NOT NULL AND quantity != '' AND quantity != 0 "
		+ "ORDER BY t.supplierPriority, t.rawMaterialCategoryPriority, t.rawMaterialCategoryId, t.contactAgencyId, t.orderDate, t.orderId, t.rawMaterialPriority, t.rawMaterialId"
)

@SqlResultSetMapping(
	name = "generateDateWiseRawMaterialReportWithPriceResult",
	classes = @ConstructorResult(
		targetClass = DateWiseRawMaterialReportDto.class,
		columns = {
			@ColumnResult(name = "supplierCategory", type = String.class),
			@ColumnResult(name = "supplierName", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "orderDate", type = LocalDateTime.class),
			@ColumnResult(name = "timeInWord", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "rawMaterialName", type = String.class),
			@ColumnResult(name = "quantity", type = Double.class),
			@ColumnResult(name = "unit", type = String.class),
			@ColumnResult(name = "rawMaterialCategoryId", type = Long.class),
			@ColumnResult(name = "contactAgencyId", type = Long.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateDateWiseChefLabourReport",
	resultSetMapping = "generateDateWiseChefLabourReportResult",
	query = "SELECT DISTINCT "
		+ "CASE "
		+ " WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ " WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ " ELSE c.name_default_lang "
		+ "END AS supplierName, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ompmi.order_date AS orderDate, "
		+ "CASE "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ " ELSE '' "
		+ "END AS timeInWord, "
		+ "CASE "
		+ "WHEN ompmi.fk_godown_id IS NULL AND o_fun.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN ompmi.fk_godown_id IS NULL THEN "
		+ " CASE "
		+ " WHEN :langType = 1 AND o_fun.function_address_prefer_lang IS NOT NULL AND o_fun.function_address_prefer_lang != '' THEN o_fun.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND o_fun.function_address_supportive_lang IS NOT NULL AND o_fun.function_address_supportive_lang != '' THEN o_fun.function_address_supportive_lang "
		+ " ELSE o_fun.function_address_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ " WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ " WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ " ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS menuItemName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ " WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ " ELSE mic.name_default_lang "
		+ "END AS menuItemCategory, "
		+ "CONCAT_WS('', IF(omat.counter_no, CONCAT(omat.counter_no, IF(omat.is_plate = true, '', ' $labour')), NULL)) AS counterNo, "
		+ "CONCAT_WS('', IF(omat.helper_no IS NOT NULL, CONCAT(omat.helper_no, ' $helper'), NULL)) AS helperNo, "
		+ "omat.counter_price AS counterPrice, "
		+ "omat.helper_price AS helperPrice, "
		+ "CASE "
		+ " WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ " WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ " ELSE m.symbol_default_lang "
		+ "END AS unit, "
		+ "CASE "
		+ "WHEN (omat.is_plate = 1 AND omat.counter_no IS NOT NULL AND omat.fk_unit_id IS NOT NULL) "
		+ "THEN "
		+ " CASE "
		+ " WHEN (m.decimal_limit_qty = -1) AND (omat.counter_no % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0 "
		+ " WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3 "
		+ " ELSE m.decimal_limit_qty "
		+ "END "
		+ "ELSE NULL "
		+ "END AS decimalLimitQtyForChef, "
		+ "CASE "
		+ "WHEN omat.is_plate = true THEN IFNULL(omat.counter_no, 0) * IFNULL(omat.counter_price, 0) "
		+ "WHEN omat.counter_no IS NULL AND omat.helper_no IS NULL THEN 0 "
		+ "ELSE (IFNULL(omat.counter_no, 0) * IFNULL(omat.counter_price, 0) + IFNULL(omat.helper_no, 0) * IFNULL(omat.helper_price, 0)) END AS total "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function o_fun ON o_fun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = o_fun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "LEFT JOIN measurement m on m.id = omat.fk_unit_id "
		+ "LEFT JOIN godown g ON g.id = ompmi.fk_godown_id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = mi.fk_menu_item_category_id "
		+ "INNER JOIN contact c ON c.id = omat.fk_contact_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "WHERE DATE(ompmi.order_date) BETWEEN DATE(:startDate) AND DATE(:endDate) AND ompmi.order_type = 1 AND IF(0 IN (:supplierCategoryIds), TRUE, contactCategories.fk_contact_category_id IN (:supplierCategoryIds)) AND IF(0 IN (:supplierNameIds), TRUE, c.id IN (:supplierNameIds)) AND IF(0 IN (:categoryIds), TRUE, mic.id IN (:categoryIds)) AND IF(0 IN (:statusIds), TRUE, cod.fk_order_status_id IN (:statusIds))"
		+ "ORDER BY ompmi.order_date, contactCategories.fk_contact_category_id, c.id, cod.id, mi.id, mic.id"
)

@SqlResultSetMapping(
	name = "generateDateWiseChefLabourReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseChefLabourReportDto.class,
		columns = {
			@ColumnResult(name = "supplierName", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "orderDate", type = LocalDateTime.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "menuItemName", type = String.class),
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "counterNo", type = String.class),
			@ColumnResult(name = "helperNo", type = String.class),
			@ColumnResult(name = "counterPrice", type = Double.class),
			@ColumnResult(name = "helperPrice", type = Double.class),
			@ColumnResult(name = "timeInWord", type = String.class),
			@ColumnResult(name = "unit", type = String.class),
			@ColumnResult(name = "decimalLimitQtyForChef", type = Integer.class),
			@ColumnResult(name = "total", type = Double.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateDateWiseLabourReport",
	resultSetMapping = "generateDateWiseLabourReportResult",
	query = "SELECT "
		+ "CASE "
		+ " WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ " WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ " ELSE c.name_default_lang "
		+ "END AS supplierName, "
		+ "c.mobile_number AS mobileNumber, "
		+ "DATE_FORMAT(old.date, '%d/%m/%Y') AS dateForRef, "
		+ "old.date AS orderDate, "
		+ "CASE "
		+ "WHEN old.fk_godown_id IS NULL AND o_fun.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN old.fk_godown_id IS NULL THEN "
		+ " CASE "
		+ " WHEN :langType = 1 AND o_fun.function_address_prefer_lang IS NOT NULL AND o_fun.function_address_prefer_lang != '' THEN o_fun.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND o_fun.function_address_supportive_lang IS NOT NULL AND o_fun.function_address_supportive_lang != '' THEN o_fun.function_address_supportive_lang "
		+ " ELSE o_fun.function_address_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ " WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ " WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ " ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cc.name_prefer_lang IS NOT NULL AND cc.name_prefer_lang != '' THEN cc.name_prefer_lang "
		+ " WHEN :langType = 2 AND cc.name_supportive_lang IS NOT NULL AND cc.name_supportive_lang != '' THEN cc.name_supportive_lang "
		+ " ELSE cc.name_default_lang "
		+ "END AS supplierCategory, "
		+ "CASE "
		+ " WHEN :langType = 1 AND ls.name_prefer_lang IS NOT NULL AND ls.name_prefer_lang != '' THEN ls.name_prefer_lang "
		+ " WHEN :langType = 2 AND ls.name_supportive_lang IS NOT NULL AND ls.name_supportive_lang != '' THEN ls.name_supportive_lang "
		+ " ELSE ls.name_default_lang "
		+ "END AS timeData, "
		+ "old.quantity AS quantity, "
		+ "old.labour_price AS rate, "
		+ "(old.quantity * old.labour_price) AS total "
		+ "FROM customer_order_details cod "
		+ "LEFT JOIN order_function o_fun ON o_fun.fk_customer_order_details_id = cod.id "
		+ "RIGHT JOIN order_labour_distribution old ON old.fk_order_function_id = o_fun.id "
		+ "LEFT JOIN godown g ON g.id = old.fk_godown_id "
		+ "LEFT JOIN contact_category cc ON cc.id = old.fk_contact_category_id "
		+ "LEFT JOIN contact c ON c.id = old.fk_contact_id "
		+ "LEFT JOIN labour_shift ls ON ls.id = old.fk_labour_shift_id "
		+ "WHERE DATE(old.date) BETWEEN DATE(:startDate) AND DATE(:endDate) AND IF(0 IN (:supplierCategoryIds) , TRUE, old.fk_contact_category_id IN (:supplierCategoryIds)) AND IF(0 IN (:supplierNameIds) , TRUE, old.fk_contact_id IN (:supplierNameIds)) AND IF(0 IN (:statusIds), TRUE, cod.fk_order_status_id IN (:statusIds))"
		+ "GROUP BY supplierName, orderDate, venue, supplierCategory, old.labour_price "
		+ "ORDER BY old.date, c.id, dateForRef, cc.priority, cc.id, ls.id"
)

@SqlResultSetMapping(
	name = "generateDateWiseLabourReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseLabourReoportDto.class,
		columns = {
			@ColumnResult(name = "supplierName", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "dateForRef", type = String.class),
			@ColumnResult(name = "orderDate", type = LocalDateTime.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "supplierCategory", type = String.class),
			@ColumnResult(name = "timeData", type = String.class),
			@ColumnResult(name = "quantity", type = Long.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "total", type = Double.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateDateWiseOrderBookingReport",
	resultSetMapping = "generateDateWiseOrderBookingReportResult",
	query ="SELECT "
		+ "c.mobile_number AS mobileNumber, "
		+ "DATE(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) AS ofn_date, "
		+ "GROUP_CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END "
		+ "ORDER BY ofn.sequence SEPARATOR '\\n' "
		+ ") AS functions, "
		+ "GROUP_CONCAT(ofn.person ORDER BY ofn.sequence SEPARATOR '\\n') AS pax, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS party, "
		+ "CASE "
		+ "WHEN ofn.function_address_default_lang IS NULL OR ofn.function_address_default_lang = '' THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ "WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ "ELSE cod.venue_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofn.function_address_prefer_lang IS NOT NULL AND ofn.function_address_prefer_lang != '' THEN ofn.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND ofn.function_address_supportive_lang IS NOT NULL AND ofn.function_address_supportive_lang != '' THEN ofn.function_address_supportive_lang "
		+ "ELSE ofn.function_address_default_lang "
		+ "END "
		+ "END AS venue "
		+ "FROM "
		+ "order_function ofn "
		+ "INNER JOIN function_type ft ON ofn.fk_function_type_id = ft.id "
		+ "INNER JOIN customer_order_details cod ON ofn.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN contact c ON cod.fk_contact_customer_id = c.id "
		+ "WHERE "
		+ "DATE(CONVERT_TZ(ofn.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN DATE(:startDate) AND DATE(:endDate) "
		+ "AND IF(0 IN (:statusIds), TRUE, cod.fk_order_status_id IN (:statusIds)) "
		+ "AND IF(0 IN (:contactIds), TRUE, cod.fk_contact_customer_id IN (:contactIds)) "
		+ "GROUP BY "
		+ "ofn_date, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END, "
		+ "CASE "
		+ "WHEN ofn.function_address_default_lang IS NULL OR ofn.function_address_default_lang = '' THEN  "
		+ "CASE "
		+ "WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ "WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ "ELSE cod.venue_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofn.function_address_prefer_lang IS NOT NULL AND ofn.function_address_prefer_lang != '' THEN ofn.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND ofn.function_address_supportive_lang IS NOT NULL AND ofn.function_address_supportive_lang != '' THEN ofn.function_address_supportive_lang "
		+ "ELSE ofn.function_address_default_lang "
		+ "END "
		+ "END "
		+ "ORDER BY ofn_date"
)

@SqlResultSetMapping(
	name = "generateDateWiseOrderBookingReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseOrderBookingReportDto.class,
		columns = {
				@ColumnResult(name = "mobileNumber", type = String.class),
				@ColumnResult(name = "ofn_date", type = LocalDateTime.class),
				@ColumnResult(name = "functions", type = String.class),
				@ColumnResult(name = "pax", type = String.class),
				@ColumnResult(name = "party", type = String.class),
				@ColumnResult(name = "venue", type = String.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateDateWiseMenuItemReport",
	resultSetMapping = "generateDateWiseMenuItemReportResult",
	query ="SELECT "
		+ "ompmi.order_date AS orderDate, "
		+ "CASE "
		+ "WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ "WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ "WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ "WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ "ELSE '' "
		+ "END AS timeInWord,"
		+ "ompmi.person AS person, "
		+ "CASE "
		+ "WHEN ompmi.fk_godown_id IS NULL AND o_fun.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ "WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ "ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN ompmi.fk_godown_id IS NULL THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND o_fun.function_address_prefer_lang IS NOT NULL AND o_fun.function_address_prefer_lang != '' THEN o_fun.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND o_fun.function_address_supportive_lang IS NOT NULL AND o_fun.function_address_supportive_lang != '' THEN o_fun.function_address_supportive_lang "
		+ "ELSE o_fun.function_address_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ "WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ "ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS menuItemName,"
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END AS menuItemCategory "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function o_fun ON o_fun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = o_fun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "LEFT JOIN godown g ON g.id = ompmi.fk_godown_id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "LEFT JOIN menu_item_sub_category misc ON misc.id = mi.fk_menu_item_sub_category_id "
		+ "LEFT JOIN kitchen_area ka ON ka.id = mi.fk_kitchen_area_id "
		+ "INNER JOIN contact c ON c.id = cod.fk_contact_customer_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "INNER JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id AND cc.fk_contact_category_type_id = 1 "
		+ "WHERE DATE(ompmi.order_date) BETWEEN DATE(:startDate) AND DATE(:endDate) AND IF(0 IN (:customerContactIds), TRUE, c.id IN (:customerContactIds)) AND IF(0 IN (:menuItemCategoryIds), TRUE, mic.id IN (:menuItemCategoryIds)) AND IF(0 IN (:menuItemSubCategoryIds), TRUE, misc.id IN (:menuItemSubCategoryIds)) AND IF(0 IN (:kitchenAreaIds), TRUE, ka.id IN (:kitchenAreaIds)) AND IF(0 IN (:statusIds), TRUE, cod.fk_order_status_id IN (:statusIds))"
		+ "ORDER BY mic.id, ompmi.order_date, ompmi.menu_item_category_sequence, ompmi.fk_menu_item_category_id"
)

@SqlResultSetMapping(
	name = "generateDateWiseMenuItemReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseMenuItemReportDto.class,
		columns = {
				@ColumnResult(name = "orderDate", type = LocalDateTime.class),
				@ColumnResult(name = "timeInWord", type = String.class),
				@ColumnResult(name = "venue", type = String.class),
				@ColumnResult(name = "menuItemName", type = String.class),
				@ColumnResult(name = "menuItemCategory", type = String.class),
				@ColumnResult(name = "person", type = Long.class),
		}
	)
)

@NamedNativeQuery(
	name = "getRawMaterialCategoryDropDownData",
	resultSetMapping = "getDropDownDataResult",
	query = "SELECT DISTINCT "
		+ "rmc.id AS id, "
		+ "rmc.name_default_lang AS nameDefaultLang, "
		+ "rmc.name_prefer_lang AS namePreferLang, "
		+ "rmc.name_supportive_lang AS nameSupportiveLang "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "INNER JOIN raw_material rm ON rm.id = mirm.fk_raw_material_id "
		+ "INNER JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "UNION "
		+ "SELECT DISTINCT  "
		+ "rmc.id AS id, "
		+ "rmc.name_default_lang AS nameDefaultLang, "
		+ "rmc.name_prefer_lang AS namePreferLang, "
		+ "rmc.name_supportive_lang AS nameSupportiveLang "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN raw_material rm ON rm.id = rma.fk_raw_material_id "
		+ "INNER JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id"
)

@Entity
public class DateWiseReportsNativeQuery extends AuditIdModelOnly {
}