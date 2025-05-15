package com.catering.dao.order_reports.labour_and_agency;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.AdminReportOutsideChithhiReportDto;
import com.catering.dto.tenant.request.CommonDataForDropDownDto;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.LabourAndAgencyBookingReportCommonValueDto;
import com.catering.dto.tenant.request.LabourAndAgencyBookingReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyChefLabourChithhiReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyChefLabourReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyLabourChithhiReportDto;
import com.catering.dto.tenant.request.LabourAndAgencyLabourReportDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity representing the Labour and Agency report query.
 * This class extends {@link AuditIdModelOnly} to inherit audit-related fields.
 * It is used to map data for labour and agency reports in the database.
 */
@NamedNativeQuery(
	name = "findBookingReportCommonValue",
	resultSetMapping = "findBookingReportCommonValueResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND contact_party.name_prefer_lang IS NOT NULL AND contact_party.name_prefer_lang != '' THEN contact_party.name_prefer_lang "
		+ "WHEN :langType = 2 AND contact_party.name_supportive_lang IS NOT NULL AND contact_party.name_supportive_lang != '' THEN contact_party.name_supportive_lang "
		+ "ELSE contact_party.name_default_lang "
		+ "END AS customerName, "
		+ "contact_party.mobile_number AS customerNumber, "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND contact_party.home_address_prefer_lang IS NOT NULL AND contact_party.home_address_prefer_lang != '' THEN contact_party.home_address_prefer_lang "
		+ "WHEN :langType = 2 AND contact_party.home_address_supportive_lang IS NOT NULL AND contact_party.home_address_supportive_lang != '' THEN contact_party.home_address_supportive_lang "
		+ "ELSE contact_party.home_address_default_lang "
		+ "END , ' ') AS customerAddress, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END AS venue, "
		+ "CASE "
		+ "WHEN :langType = 1 AND contact_party.home_address_prefer_lang IS NOT NULL AND contact_party.home_address_prefer_lang != '' THEN contact_party.home_address_prefer_lang "
		+ "WHEN :langType = 2 AND contact_party.home_address_supportive_lang IS NOT NULL AND contact_party.home_address_supportive_lang != '' THEN contact_party.home_address_supportive_lang "
		+ "ELSE contact_party.home_address_default_lang "
		+ "END AS customerHomeAddress "
		+ "FROM customer_order_details cod "
		+ "LEFT JOIN contact contact_party ON contact_party.id = cod.fk_contact_customer_id "
		+ "WHERE cod.id = :orderId"
)
@SqlResultSetMapping(
	name = "findBookingReportCommonValueResult",
	classes = @ConstructorResult(
		targetClass = LabourAndAgencyBookingReportCommonValueDto.class,
		columns = {
			@ColumnResult(name = "customerName", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "customerAddress", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "customerHomeAddress", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "findBookingReportValue",
	resultSetMapping = "findBookingReportValueResult",
	query = "SELECT "
		+ "t.eventDate, "
		+ "t.venue, "
		+ "t.functionName, "
		+ "t.menuItemName, "
		+ "COALESCE(CONCAT(t.contactCategory, ' (', t.shortName, ')'), t.contactCategory) AS contactCategory, "
		+ "t.quantity, "
		+ "t.priority "
		+ "FROM (SELECT "
		+ "cod.event_main_date AS eventDate, "
		+ "CASE "
		+ "WHEN old.fk_godown_id IS NULL AND of2.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ "WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ "ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN old.fk_godown_id IS NULL THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
		+ "ELSE of2.function_address_default_lang "
		+ "END "
		+ "ELSE  "
		+ "CASE "
		+ "WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ "WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ "ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "menuItemNameSubquery.functionName, "
		+ "menuItemNameSubquery.menuItemName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND cc.name_prefer_lang IS NOT NULL AND cc.name_prefer_lang != '' THEN cc.name_prefer_lang "
		+ "WHEN :langType = 2 AND cc.name_supportive_lang IS NOT NULL AND cc.name_supportive_lang != '' THEN cc.name_supportive_lang "
		+ "ELSE cc.name_default_lang "
		+ "END AS contactCategory,"
		+ "CASE "
		+ "WHEN :langType = 1 AND c.short_name_prefer_lang IS NOT NULL AND c.short_name_prefer_lang != '' THEN c.short_name_prefer_lang "
		+ "WHEN :langType = 2 AND c.short_name_supportive_lang IS NOT NULL AND c.short_name_supportive_lang != '' THEN c.short_name_supportive_lang "
		+ "ELSE c.short_name_default_lang "
		+ "END AS shortName, "
		+ "GROUP_CONCAT(quantity SEPARATOR ' + ') AS quantity, "
		+ "count(*) AS countOfConcat, "
		+ "cc.priority AS priority "
		+ "FROM ( "
		+ "SELECT "
		+ "cod.id AS order_detail_id, "
		+ "GROUP_CONCAT(DISTINCT CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND final_m.name_prefer_lang IS NOT NULL AND final_m.name_prefer_lang != '' THEN final_m.name_prefer_lang "
		+ "WHEN :langType = 2 AND final_m.name_supportive_lang IS NOT NULL AND final_m.name_supportive_lang != '' THEN final_m.name_supportive_lang "
		+ "ELSE final_m.name_default_lang "
		+ "END SEPARATOR ', ') AS menuItemName, "
		+ "GROUP_CONCAT(DISTINCT CONCAT_WS(' - ', CASE "
		+ "WHEN :langType = 1 AND fm.name_prefer_lang IS NOT NULL AND fm.name_prefer_lang != '' THEN fm.name_prefer_lang "
		+ "WHEN :langType = 2 AND fm.name_supportive_lang IS NOT NULL AND fm.name_supportive_lang != '' THEN fm.name_supportive_lang "
		+ "ELSE fm.name_default_lang "
		+ "END, of2.person) SEPARATOR ', ') AS functionName "
		+ "FROM customer_order_details cod "
		+ "LEFT JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "LEFT JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id AND ompmi.order_type IN(1, 2) "
		+ "LEFT JOIN function_type fm ON fm.id = of2.fk_function_type_id "
		+ "LEFT JOIN menu_item final_m ON final_m.id = ompmi.fk_menu_item_id "
		+ "WHERE cod.id = :orderId "
		+ "GROUP BY cod.id "
		+ ") AS menuItemNameSubquery "
		+ "LEFT JOIN customer_order_details cod ON menuItemNameSubquery.order_detail_id = cod.id "
		+ "LEFT JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN order_labour_distribution old ON old.fk_order_function_id = of2.id "
		+ "LEFT JOIN godown g ON g.id = old.fk_godown_id "
		+ "LEFT JOIN contact_category cc ON cc.id = old.fk_contact_category_id "
		+ "LEFT JOIN contact c ON c.id = old.fk_contact_id "
		+ "GROUP BY venue, cc.id, shortName) AS t "
		+ "ORDER BY venue, contactCategory IS NULL, priority"
)
@SqlResultSetMapping(
	name = "findBookingReportValueResult",
	classes = @ConstructorResult(
		targetClass = LabourAndAgencyBookingReportDto.class,
		columns = {
			@ColumnResult(name = "eventDate", type = LocalDate.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "menuItemName", type = String.class),
			@ColumnResult(name = "contactCategory", type = String.class),
			@ColumnResult(name = "quantity", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getChefOrOutsideLabourSuppliersByOrder",
	resultSetMapping = "getChefOrOutsideLabourSupplierByOrder",
	query = "SELECT "
		+ "DISTINCT c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM order_menu_preparation_menu_item ompmi "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function of2 ON of2.id = omp.fk_order_function_id "
		+ "INNER JOIN contact c ON c.id = omat.fk_contact_id "
		+ "WHERE of2.fk_customer_order_details_id = :orderId AND ompmi.order_type = :orderType "
		+ "ORDER BY c.id;"
)
@SqlResultSetMapping(
	name = "getChefOrOutsideLabourSupplierByOrder",
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

@NamedNativeQuery(
	name = "getActiveFunctionsForOutsideOrChef",
	resultSetMapping = "getActiveFunctionByOrder",
	query = "SELECT DISTINCT "
		+ "fm.id, "
		+ "of2.id AS orderFunctionId, "
		+ "of2.date AS orderDate, "
		+ "fm.name_default_lang AS nameDefaultLang, "
		+ "fm.name_prefer_lang AS namePreferLang, "
		+ "fm.name_supportive_lang AS nameSupportiveLang "
		+ "FROM `customer_order_details` o "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = o.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id AND ompmi.order_type IN (:orderType) "
		+ "INNER JOIN function_type fm ON fm.id = of2.fk_function_type_id "
		+ "WHERE o.id = :orderId"
)

@NamedNativeQuery(
	name = "getActiveFunctionsByOrderForLabour",
	resultSetMapping = "getActiveFunctionByOrder",
	query = "SELECT DISTINCT "
		+ "ft.id, "
		+ "of2.id AS orderFunctionId, "
		+ "of2.date AS orderDate, "
		+ "ft.name_default_lang AS nameDefaultLang, "
		+ "ft.name_prefer_lang AS namePreferLang, "
		+ "ft.name_supportive_lang AS nameSupportiveLang "
		+ "FROM `customer_order_details` o "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = o.id "
		+ "INNER JOIN order_labour_distribution old ON old.fk_order_function_id = of2.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "WHERE o.id = :orderId"
)

@SqlResultSetMapping(
	name = "getActiveFunctionByOrder",
	classes = @ConstructorResult(
		targetClass = FunctionPerOrderDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "orderFunctionId", type = Long.class),
			@ColumnResult(name = "orderDate", type = LocalDateTime.class),
		}
	)
)

@NamedNativeQuery(
	name = "getChefOrOutsideLabourFinalMaterialsByOrder",
	resultSetMapping = "getChefOrOutsideLabourFinalMaterialByOrder",
	query = "SELECT "
		+ "DISTINCT mi.id, "
		+ "mi.name_default_lang AS nameDefaultLang, "
		+ "mi.name_prefer_lang AS namePreferLang, "
		+ "mi.name_supportive_lang AS nameSupportiveLang "
		+ "FROM order_menu_preparation_menu_item ompmi "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function of2 ON of2.id = omp.fk_order_function_id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "WHERE of2.fk_customer_order_details_id = :orderId AND ompmi.order_type = :orderType "
		+ "ORDER BY mi.priority"
)
@SqlResultSetMapping(
	name = "getChefOrOutsideLabourFinalMaterialByOrder",
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

@NamedNativeQuery(
	name = "getLabourSuppliersByOrder",
	resultSetMapping = "getLabourSupplierByOrder",
	query = "SELECT "
		+ "DISTINCT cc.id, "
		+ "cc.name_default_lang AS nameDefaultLang, "
		+ "cc.name_prefer_lang AS namePreferLang, "
		+ "cc.name_supportive_lang AS nameSupportiveLang "
		+ "FROM order_labour_distribution old "
		+ "INNER JOIN order_function of2 ON of2.id = old.fk_order_function_id "
		+ "INNER JOIN contact_category cc ON cc.id = old.fk_contact_category_id "
		+ "WHERE of2.fk_customer_order_details_id = :orderId "
		+ "ORDER BY cc.priority, cc.id;"
)
@SqlResultSetMapping(
	name = "getLabourSupplierByOrder",
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

@NamedNativeQuery(
	name = "getLabourSuppliersNameByOrder",
	resultSetMapping = "getLabourSupplierNameByOrder",
	query = "SELECT DISTINCT "
		+ "DISTINCT c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM order_labour_distribution old "
		+ "INNER JOIN order_function of2 ON of2.id = old.fk_order_function_id "
		+ "INNER JOIN contact c ON c.id = old.fk_contact_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "WHERE of2.fk_customer_order_details_id = :orderId AND (contactCategories.fk_contact_category_id = :supplierCategoryId or :supplierCategoryId = 0);"
)
@SqlResultSetMapping(
	name = "getLabourSupplierNameByOrder",
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

@NamedNativeQuery(
	name = "generateChefLabourReport",
	resultSetMapping = "generateChefLabourReportResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN ompmi.fk_godown_id IS NULL AND of2.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN ompmi.fk_godown_id IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
		+ " ELSE of2.function_address_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ " WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ " WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ " ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "CASE "
		+ " WHEN 0 = 1 AND cod.party_plot_name_prefer_lang IS NOT NULL AND cod.party_plot_name_prefer_lang != '' THEN cod.party_plot_name_prefer_lang "
		+ " WHEN 0 = 2 AND cod.party_plot_name_supportive_lang IS NOT NULL AND cod.party_plot_name_supportive_lang != '' THEN cod.party_plot_name_supportive_lang "
		+ " ELSE cod.party_plot_name_default_lang "
		+ "END AS hallName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ " WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ " ELSE c.name_default_lang "
		+ "END AS agencyName, "
		+ "c.mobile_number AS agencyNumber, "
		+ "CASE "
		+ " WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ " WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ " ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS menuItemName, "
		+ "omat.quantity as quantity, "
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
		+ " WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN ompmi.note_prefer_lang "
		+ " WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN ompmi.note_supportive_lang "
		+ " ELSE ompmi.note_default_lang "
		+ "END AS notes, "
		+ "ompmi.order_date AS orderDate, "
		+ "IF(omat.counter_no IS NOT NULL, CONCAT(omat.counter_no, IF(omat.is_plate = true, '', ' $labour')), NULL) AS counterNo, "
		+ "IF(omat.helper_no IS NOT NULL, CONCAT(omat.helper_no, ' $helper'), NULL) AS helperNo, "
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
		+ "IF(omat.is_plate, omat.counter_no, ompmi.person) AS pax "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "LEFT JOIN godown g ON g.id = ompmi.fk_godown_id "
		+ "INNER JOIN contact c ON c.id = omat.fk_contact_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "LEFT JOIN measurement m on m.id = omat.fk_unit_id "
		+ "WHERE cod.id = :orderId "
		+ "AND ompmi.order_type = :orderType "
		+ "AND (c.id IN (:contactIds) OR 0 IN (:contactIds)) "
		+ "AND (of2.id IN (:functionIds) OR 0 IN (:functionIds)) "
		+ "AND (mi.id IN (:menuItemIds) OR 0 IN (:menuItemIds)) "
		+ "ORDER BY venue, c.id, ompmi.order_date, ft.id, ompmi.menu_item_category_sequence, mic.id, ompmi.menu_item_sequence, mi.id"
)
@SqlResultSetMapping(
	name = "generateChefLabourReportResult",
	classes = @ConstructorResult(
		targetClass = LabourAndAgencyChefLabourReportDto.class,
		columns = {
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "hallName", type = String.class),
			@ColumnResult(name = "agencyName", type = String.class),
			@ColumnResult(name = "agencyNumber", type = String.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "menuItemName", type = String.class),
			@ColumnResult(name = "quantity", type = Double.class),
			@ColumnResult(name = "unit", type = String.class),
			@ColumnResult(name = "notes", type = String.class),
			@ColumnResult(name = "orderDate", type = LocalDateTime.class),
			@ColumnResult(name = "counterNo", type = String.class),
			@ColumnResult(name = "helperNo", type = String.class),
			@ColumnResult(name = "decimalLimitQtyForChef", type = Integer.class),
			@ColumnResult(name = "pax", type = Long.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateChefLabourChithhiReport",
	resultSetMapping = "generateChefLabourChithhiReportResult",
	query = "SELECT "
		+ "CASE "
		+ " WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ " WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ " ELSE c.name_default_lang "
		+ "END AS agencyName, "
		+ "c.mobile_number AS agencyNumber, "
		+ "CASE "
		+ " WHEN :langType = 1 AND contact_manager.name_prefer_lang IS NOT NULL AND contact_manager.name_prefer_lang != '' THEN contact_manager.name_prefer_lang "
		+ " WHEN :langType = 2 AND contact_manager.name_supportive_lang IS NOT NULL AND contact_manager.name_supportive_lang != '' THEN contact_manager.name_supportive_lang "
		+ " ELSE contact_manager.name_default_lang "
		+ "END AS managerName, "
		+ "contact_manager.mobile_number AS managerNumber, "
		+ "ompmi.order_date AS orderDate, "
		+ "CASE "
		+ "WHEN ompmi.fk_godown_id IS NULL AND of2.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN ompmi.fk_godown_id IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
		+ " ELSE of2.function_address_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ " WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ " WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ " ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "IF(omat.is_plate, omat.counter_no, ompmi.person) AS person, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END, "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN CONCAT(' (', ompmi.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN CONCAT(' (', ompmi.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmi.note_default_lang, ')') "
		+ "END , '') "
		+ ") AS menuItemName, "
		+ "IF(omat.counter_no IS NOT NULL, CONCAT(omat.counter_no, '',IF(omat.is_plate = true, '', ' $labour')), NULL) AS counterNo, "
		+ "IF(omat.helper_no IS NOT NULL, CONCAT(omat.helper_no, ' $helper'), NULL) AS helperNo, "
		+ "CASE "
		+ " WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ " WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS unit, "
		+ "CASE "
		+ " WHEN 0 = 1 AND cod.party_plot_name_prefer_lang IS NOT NULL AND cod.party_plot_name_prefer_lang != '' THEN cod.party_plot_name_prefer_lang "
		+ " WHEN 0 = 2 AND cod.party_plot_name_supportive_lang IS NOT NULL AND cod.party_plot_name_supportive_lang != '' THEN cod.party_plot_name_supportive_lang "
		+ " ELSE cod.party_plot_name_default_lang "
		+ "END AS hallName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ " WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ " ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "CASE "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ " ELSE '' "
		+ "END AS timePeriod,"
		+ "CASE "
		+ "WHEN (omat.is_plate = 1 AND omat.counter_no IS NOT NULL AND omat.fk_unit_id IS NOT NULL) "
		+ "THEN "
		+ " CASE "
		+ " WHEN (m.decimal_limit_qty = -1) AND (omat.counter_no % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0 "
		+ " WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3 "
		+ " ELSE m.decimal_limit_qty "
		+ "END "
		+ "ELSE NULL "
		+ "END AS decimalLimitQty, "
		+ "omat.is_plate AS plate "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "LEFT JOIN measurement m on m.id = omat.fk_unit_id "
		+ "LEFT JOIN godown g ON g.id = ompmi.fk_godown_id "
		+ "INNER JOIN contact c ON c.id = omat.fk_contact_id "
		+ "LEFT JOIN contact contact_manager ON contact_manager.id = cod.fk_contact_manager_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "WHERE cod.id = :orderId AND ompmi.order_type = :orderType "
		+ "AND (c.id IN (:contactIds) OR 0 IN (:contactIds)) "
		+ "AND (of2.id IN (:functionIds) OR 0 IN (:functionIds)) "
		+ "AND (mi.id IN (:menuItemIds) OR 0 IN (:menuItemIds)) "
		+ "ORDER BY venue, c.id, ompmi.order_date, of2.id, mic.id, mi.id"
)
@SqlResultSetMapping(
	name = "generateChefLabourChithhiReportResult",
	classes = @ConstructorResult(
		targetClass = LabourAndAgencyChefLabourChithhiReportDto.class,
		columns = {
			@ColumnResult(name = "agencyName", type = String.class),
			@ColumnResult(name = "agencyNumber", type = String.class),
			@ColumnResult(name = "managerName", type = String.class),
			@ColumnResult(name = "managerNumber", type = String.class),
			@ColumnResult(name = "orderDate", type = LocalDateTime.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "menuItemName", type = String.class),
			@ColumnResult(name = "counterNo", type = String.class),
			@ColumnResult(name = "helperNo", type = String.class),
			@ColumnResult(name = "unit", type = String.class),
			@ColumnResult(name = "hallName", type = String.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "timePeriod", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
			@ColumnResult(name = "plate", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateOutsideChithhiReportReport",
	resultSetMapping = "generateOutsideChithhiReportReportResult",
	query = "SELECT "
		+ "CASE "
		+ " WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ " WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ " ELSE c.name_default_lang "
		+ "END AS agencyName, "
		+ "c.mobile_number AS agencyNumber, "
		+ "CASE "
		+ " WHEN :langType = 1 AND contact_manager.name_prefer_lang IS NOT NULL AND contact_manager.name_prefer_lang != '' THEN contact_manager.name_prefer_lang "
		+ " WHEN :langType = 2 AND contact_manager.name_supportive_lang IS NOT NULL AND contact_manager.name_supportive_lang != '' THEN contact_manager.name_supportive_lang "
		+ " ELSE contact_manager.name_default_lang "
		+ "END AS managerName, "
		+ "contact_manager.mobile_number AS managerNumber, "
		+ "ompmi.order_date AS orderDate, "
		+ "CASE "
		+ "WHEN ompmi.fk_godown_id IS NULL AND of2.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN ompmi.fk_godown_id IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
		+ " ELSE of2.function_address_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ " WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ " WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ " ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "CONCAT(CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END, "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN CONCAT(' (', ompmi.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN CONCAT(' (', ompmi.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmi.note_default_lang, ')') "
		+ "END , '') "
		+ ") AS menuItemName, "
		+ "omat.quantity as quantity, "
		+ "CASE "
		+ " WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ " WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ " ELSE m.symbol_default_lang "
		+ "END AS unit, "
		+ "CASE "
		+ "WHEN (m.decimal_limit_qty = -1) AND (omat.quantity % 1 = 0) AND (m.id = 1 OR m.id = 3) THEN 0 "
		+ "WHEN (m.decimal_limit_qty = -1) AND (m.id = 1 OR m.id = 3) THEN 3 "
		+ "ELSE  m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "CASE "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ " WHEN TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(ompmi.order_date, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ " ELSE '' "
		+ "END AS timePeriod, "
		+ "CASE "
		+ " WHEN 0 = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN ompmi.note_prefer_lang "
		+ " WHEN 0 = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN ompmi.note_supportive_lang "
		+ " ELSE ompmi.note_default_lang "
		+ "END AS notes "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "LEFT JOIN godown g ON g.id = ompmi.fk_godown_id "
		+ "INNER JOIN contact c ON c.id = omat.fk_contact_id "
		+ "LEFT JOIN contact contact_manager ON contact_manager.id = cod.fk_contact_manager_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "LEFT JOIN measurement m on m.id = omat.fk_unit_id "
		+ "WHERE cod.id = :orderId AND ompmi.order_type = :orderType "
		+ "AND (c.id IN (:contactIds) OR 0 IN (:contactIds)) "
		+ "AND (of2.id IN (:functionIds) OR 0 IN (:functionIds)) "
		+ "AND (mi.id IN (:menuItemIds) OR 0 IN (:menuItemIds)) "
		+ "ORDER BY venue, c.id, of2.id, mic.id, mi.id"
)
@SqlResultSetMapping(
	name = "generateOutsideChithhiReportReportResult",
	classes = @ConstructorResult(
		targetClass = AdminReportOutsideChithhiReportDto.class,
		columns = {
			@ColumnResult(name = "agencyName", type = String.class),
			@ColumnResult(name = "agencyNumber", type = String.class),
			@ColumnResult(name = "managerName", type = String.class),
			@ColumnResult(name = "managerNumber", type = String.class),
			@ColumnResult(name = "orderDate", type = LocalDateTime.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "menuItemName", type = String.class),
			@ColumnResult(name = "timePeriod", type = String.class),
			@ColumnResult(name = "quantity", type = Double.class),
			@ColumnResult(name = "unit", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class)
		}
	)
)
/**
 * This class represents the query model for event agency distribution reports.
 * It extends the AuditIdModelOnly class to inherit basic auditing fields.
 *
 * @author Krushali Talaviya
 * @since 2023-10-29
 */
@NamedNativeQuery(
	name = "generateLabourReport",
	resultSetMapping = "generateLabourReportResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN old.fk_godown_id IS NULL AND of2.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END "
		+ "WHEN old.fk_godown_id IS NULL THEN "
		+ "CASE "
		+ " WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
		+ " ELSE of2.function_address_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ " WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
		+ " WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
		+ " ELSE g.address_default_lang "
		+ "END "
		+ "END AS venue, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.party_plot_name_prefer_lang IS NOT NULL AND cod.party_plot_name_prefer_lang != '' THEN cod.party_plot_name_prefer_lang "
		+ " WHEN :langType = 2 AND cod.party_plot_name_supportive_lang IS NOT NULL AND cod.party_plot_name_supportive_lang != '' THEN cod.party_plot_name_supportive_lang "
		+ " ELSE cod.party_plot_name_default_lang "
		+ "END AS hallName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ " WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ " ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cc.name_prefer_lang IS NOT NULL AND cc.name_prefer_lang != '' THEN cc.name_prefer_lang "
		+ " WHEN :langType = 2 AND cc.name_supportive_lang IS NOT NULL AND cc.name_supportive_lang != '' THEN cc.name_supportive_lang "
		+ " ELSE cc.name_default_lang "
		+ "END AS contactCategory, "
		+ "CASE "
		+ " WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ " WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ " ELSE c.name_default_lang "
		+ "END AS contact, "
		+ "c.mobile_number as mobileNumber, "
		+ "old.date, "
		+ "sum(old.quantity) AS quantity, "
		+ "CASE "
		+ " WHEN :langType = 1 AND ls.name_prefer_lang IS NOT NULL AND ls.name_prefer_lang != '' THEN ls.name_prefer_lang "
		+ " WHEN :langType = 2 AND ls.name_supportive_lang IS NOT NULL AND ls.name_supportive_lang != '' THEN ls.name_supportive_lang "
		+ " ELSE ls.name_default_lang "
		+ "END AS labourShift, "
		+ "CASE "
		+ "WHEN :langType = 1 AND old.note_prefer_lang IS NOT NULL AND old.note_prefer_lang != '' THEN old.note_prefer_lang "
		+ "WHEN :langType = 2 AND old.note_supportive_lang IS NOT NULL AND old.note_supportive_lang != '' THEN old.note_supportive_lang "
		+ "ELSE old.note_default_lang "
		+ "END AS note "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_labour_distribution old ON old.fk_order_function_id = of2.id "
		+ "LEFT JOIN godown g ON g.id = old.fk_godown_id "
		+ "INNER JOIN contact_category cc ON cc.id = old.fk_contact_category_id "
		+ "INNER JOIN contact c ON c.id = old.fk_contact_id "
		+ "LEFT JOIN labour_shift ls ON ls.id = old.fk_labour_shift_id "
		+ "WHERE cod.id = :orderId "
		+ "AND (cc.id IN (:supplierCategoryIds) OR 0 IN (:supplierCategoryIds)) "
		+ "AND (c.id IN (:supplierIds) OR 0 IN (:supplierIds)) "
		+ "AND (of2.id IN (:functionIds) OR 0 IN (:functionIds)) "
		+ "GROUP BY venue, ft.id, cc.id, date, c.id, labourShift "
		+ "ORDER BY venue, of2.sequence, cc.priority, cc.id"
)
@SqlResultSetMapping(
	name = "generateLabourReportResult",
	classes = @ConstructorResult(
		targetClass = LabourAndAgencyLabourReportDto.class,
		columns = {
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "hallName", type = String.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "contactCategory", type = String.class),
			@ColumnResult(name = "contact", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "quantity", type = Long.class),
			@ColumnResult(name = "labourShift", type = String.class),
			@ColumnResult(name = "note", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateLabourChithhiReport",
	resultSetMapping = "generateLabourChithhiReportResult",
	query = "SELECT "
			+ "CASE "
			+ "WHEN old.fk_godown_id IS NULL AND of2.function_address_default_lang IS NULL THEN "
			+ "CASE "
			+ "WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
			+ "WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
			+ "ELSE cod.venue_default_lang "
			+ "END "
			+ "WHEN old.fk_godown_id IS NULL THEN "
			+ "CASE "
			+ "WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
			+ "WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
			+ "ELSE of2.function_address_default_lang "
			+ "END "
			+ "ELSE "
			+ "CASE "
			+ "WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang "
			+ "WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang "
			+ "ELSE g.address_default_lang "
			+ "END "
			+ "END AS venue, "
			+ "old.date AS date, "
			+ "CASE "
			+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
			+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
			+ "ELSE c.name_default_lang "
			+ "END AS contactName, "
			+ "CASE "
			+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
			+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
			+ "ELSE ft.name_default_lang "
			+ "END AS functionName, "
			+ "c.mobile_number AS contactNumber, "
			+ "CASE "
			+ "WHEN :langType = 1 AND cc.name_prefer_lang IS NOT NULL AND cc.name_prefer_lang != '' THEN cc.name_prefer_lang "
			+ "WHEN :langType = 2 AND cc.name_supportive_lang IS NOT NULL AND cc.name_supportive_lang != '' THEN cc.name_supportive_lang "
			+ "ELSE cc.name_default_lang "
			+ "END AS contactCategory, "
			+ "DATE_FORMAT(old.date, '%d/%m/%Y') AS dateForRef, "
			+ "CASE "
			+ "WHEN :langType = 1 AND ls.name_prefer_lang IS NOT NULL AND ls.name_prefer_lang != '' THEN ls.name_prefer_lang "
			+ "WHEN :langType = 2 AND ls.name_supportive_lang IS NOT NULL AND ls.name_supportive_lang != '' THEN ls.name_supportive_lang "
			+ "ELSE ls.name_default_lang "
			+ "END AS timeData, "
			+ "SUM(old.quantity) AS quantity, "
			+ "CASE "
			+ "WHEN :langType = 1 AND c_manager.name_prefer_lang IS NOT NULL AND c_manager.name_prefer_lang != '' THEN c_manager.name_prefer_lang "
			+ "WHEN :langType = 2 AND c_manager.name_supportive_lang IS NOT NULL AND c_manager.name_supportive_lang != '' THEN c_manager.name_supportive_lang "
			+ "ELSE c_manager.name_default_lang "
			+ "END AS managerName, "
			+ "c_manager.mobile_number AS managerNumber, "
			+ "CASE "
			+ "WHEN :langType = 1 AND old.note_prefer_lang IS NOT NULL AND old.note_prefer_lang != '' THEN old.note_prefer_lang "
			+ "WHEN :langType = 2 AND old.note_supportive_lang IS NOT NULL AND old.note_supportive_lang != '' THEN old.note_supportive_lang "
			+ "ELSE old.note_default_lang "
			+ "END AS note "
			+ "FROM customer_order_details cod "
			+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
			+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
			+ "INNER JOIN order_labour_distribution old ON old.fk_order_function_id = of2.id "
			+ "LEFT JOIN godown g ON g.id = old.fk_godown_id "
			+ "INNER JOIN contact_category cc ON cc.id = old.fk_contact_category_id "
			+ "INNER JOIN contact c ON c.id = old.fk_contact_id "
			+ "LEFT JOIN contact c_manager ON c_manager.id = cod.fk_contact_manager_id "
			+ "LEFT JOIN labour_shift ls ON ls.id = old.fk_labour_shift_id "
			+ "WHERE cod.id = :orderId AND "
			+ "(cc.id IN (:supplierCategoryIds) OR 0 IN (:supplierCategoryIds)) AND "
			+ "(c.id IN (:supplierIds) OR 0 IN (:supplierIds)) AND " 
			+ "(of2.id IN (:functionIds) OR 0 IN (:functionIds)) "
			+ "GROUP BY venue, ft.id, date, cc.id, c.id, timeData "
			+ "ORDER BY venue, of2.sequence, old.date, cc.priority, cc.id, c.id"
)
@SqlResultSetMapping(
	name = "generateLabourChithhiReportResult",
	classes = @ConstructorResult(
		targetClass = LabourAndAgencyLabourChithhiReportDto.class,
		columns = {
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "contactNumber", type = String.class),
			@ColumnResult(name = "contactCategory", type = String.class),
			@ColumnResult(name = "dateForRef", type = String.class),
			@ColumnResult(name = "timeData", type = String.class),
			@ColumnResult(name = "quantity", type = Long.class),
			@ColumnResult(name = "managerName", type = String.class),
			@ColumnResult(name = "managerNumber", type = String.class),
			@ColumnResult(name = "note", type = String.class)
		}
	)
)

@Entity
public class LabourAndAgencyReportQuery extends AuditIdModelOnly {
}