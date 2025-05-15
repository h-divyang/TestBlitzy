package com.catering.dao.order_reports.admin_reports;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.AddressChitthiReportDto;
import com.catering.dto.tenant.request.AdminFeedBackReportDto;
import com.catering.dto.tenant.request.AdminSupplierDetailsReportDto;
import com.catering.dto.tenant.request.AdminWastageReportDto;
import com.catering.dto.tenant.request.CustomerFormatReportDto;
import com.catering.dto.tenant.request.AdminReportCounterNamePlateReportDto;
import com.catering.dto.tenant.request.AdminReportNamePlateDto;
import com.catering.dto.tenant.request.AdminReportTableMenuDto;
import com.catering.dto.tenant.request.AdminReportTwoLanguageCounterNamePlateReportDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * This class represents the query model for admin reports.
 * It extends the AuditIdModelOnly class to inherit basic auditing fields.
 *
 * @author Krushali Talaviya
 * @since 1 January 2024
 */
@NamedNativeQuery(
	name = "generateWastageReport",
	resultSetMapping = "generateWastageReportResult",
	query = "SELECT DISTINCT "
		+ "mi.id AS menuItemId, "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS menuItemName "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "WHERE cod.id = :orderId "
		+ "ORDER BY of2.sequence, ompmi.menu_item_category_sequence, ompmi.menu_item_sequence, mi.priority, mi.id"
)

@SqlResultSetMapping(
	name = "generateWastageReportResult",
	classes = @ConstructorResult(
		targetClass = AdminWastageReportDto.class,
		columns = {
			@ColumnResult(name = "menuItemId", type = Long.class),
			@ColumnResult(name = "menuItemName", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateDishCountingReport",
	resultSetMapping = "generateFeedbackReportResult",
	query = "SELECT "
		+ "of2.id AS orderFunctionId, "
		+ "CASE "
		+ " WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ " WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ " ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "of2.person AS person, "
		+ "of2.date AS date, "
		+ "of2.end_date AS endDate, "
		+ "CASE "
		+ "WHEN TIME(CONVERT_TZ(of2.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ "WHEN TIME(CONVERT_TZ(of2.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ "WHEN TIME(CONVERT_TZ(of2.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ "WHEN TIME(CONVERT_TZ(of2.date, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(of2.date, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ "ELSE '' "
		+ "END AS timeInWord, "
		+ "CASE "
		+ "WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
		+ "ELSE of2.function_address_default_lang "
		+ "END AS functionAddress "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN contact contact_party ON contact_party.id = cod.fk_contact_customer_id "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "WHERE cod.id = :orderId "
		+ "GROUP BY of2.id, of2.date "
		+ "ORDER BY ft.id"
)

@SqlResultSetMapping(
	name = "generateFeedbackReportResult",
	classes = @ConstructorResult(
		targetClass = AdminFeedBackReportDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "timeInWord", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateSupplierDetailsReport",
	resultSetMapping = "generateSupplierDetailsReportResult",
	query = "SELECT "
		+ "order_fun.id AS orderFunctionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "order_fun.person, "
		+ "CASE "
		+ "WHEN :langType = 1 AND order_fun.function_address_prefer_lang IS NOT NULL AND order_fun.function_address_prefer_lang != '' THEN order_fun.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND order_fun.function_address_supportive_lang IS NOT NULL AND order_fun.function_address_supportive_lang != '' THEN order_fun.function_address_supportive_lang "
		+ "ELSE order_fun.function_address_default_lang "
		+ "END AS functionAddress, "
		+ "order_fun.date AS date, "
		+ "order_fun.end_date AS endDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND contact_chef_labour.name_prefer_lang IS NOT NULL AND contact_chef_labour.name_prefer_lang != '' THEN contact_chef_labour.name_prefer_lang "
		+ "WHEN :langType = 2 AND contact_chef_labour.name_supportive_lang IS NOT NULL AND contact_chef_labour.name_supportive_lang != '' THEN contact_chef_labour.name_supportive_lang "
		+ "ELSE contact_chef_labour.name_default_lang "
		+ "END AS agencyName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND contact_category_name.name_prefer_lang IS NOT NULL AND contact_category_name.name_prefer_lang != '' THEN contact_category_name.name_prefer_lang "
		+ " WHEN :langType = 2 AND contact_category_name.name_supportive_lang IS NOT NULL AND contact_category_name.name_supportive_lang != '' THEN contact_category_name.name_supportive_lang "
		+ " ELSE contact_category_name.name_default_lang "
		+ "END AS agencyCategory, "
		+ "contact_chef_labour.mobile_number AS supplierNumber "
		+ "FROM "
		+ "((SELECT DISTINCT "
		+ "order_fun.id AS functionId, "
		+ "omat.fk_contact_id AS agencyId "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function order_fun ON order_fun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = order_fun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "WHERE cod.id = :orderId AND omat.fk_contact_id IS NOT NULL) "
		+ "UNION ALL "
		+ "(SELECT DISTINCT "
		+ "order_fun.id AS functionId, "
		+ "old.fk_contact_id AS agencyId "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function order_fun ON order_fun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_labour_distribution old ON old.fk_order_function_id = order_fun.id "
		+ "INNER JOIN contact agency_name ON agency_name.id = old.fk_contact_id "
		+ "INNER JOIN contact_category agency_category ON agency_category.id = old.fk_contact_category_id "
		+ "WHERE cod.id = :orderId AND agency_category.id IS NOT NULL)) AS t "
		+ "INNER JOIN contact contact_chef_labour ON contact_chef_labour.id = t.agencyId "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = contact_chef_labour.id "
		+ "INNER JOIN contact_category contact_category_name ON contact_category_name.id = contactCategories.fk_contact_category_id AND contact_category_name.fk_contact_category_type_id IN (4, 2, 5) "
		+ "INNER JOIN order_function order_fun ON order_fun.id = functionId "
		+ "INNER JOIN function_type ft ON ft.id = order_fun.fk_function_type_id "
		+ "ORDER BY ft.id, order_fun.date;"
)

@SqlResultSetMapping(
	name = "generateSupplierDetailsReportResult",
	classes = @ConstructorResult(
		targetClass = AdminSupplierDetailsReportDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "agencyName", type = String.class),
			@ColumnResult(name = "agencyCategory", type = String.class),
			@ColumnResult(name = "supplierNumber", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateCustomerExtraCostReport",
	resultSetMapping = "generateCustomerFormatReportResult",
	query = "SELECT "
		+ "of2.id AS orderFunctionId, "
		+ "cod.event_main_date AS eventDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ "WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ "ELSE cod.venue_default_lang "
		+ "END AS venue, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS customerName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
		+ "ELSE of2.function_address_default_lang "
		+ "END AS functionAddress, "
		+ "of2.person AS person "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN contact c ON c.id = cod.fk_contact_customer_id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "WHERE cod.id = :orderId "
)

@SqlResultSetMapping(
	name = "generateCustomerFormatReportResult",
	classes = @ConstructorResult(
		targetClass = CustomerFormatReportDto.class,
		columns = {
			@ColumnResult(name = "orderFunctionId", type = Long.class),
			@ColumnResult(name = "eventDate", type = LocalDate.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "customerName", type = String.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "person", type = Long.class)
		}
	)
)


@NamedNativeQuery(
	name = "getNamePlateDetails",
	resultSetMapping = "getNamePlateDetailsResult",
	query = "SELECT "
		+ "ompmi.id AS id, "
		+ "CASE "
		+ "WHEN ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' AND ompmi.counter_plate_menu_item_name_default_lang IS NULL THEN ompmi.menu_item_name_default_lang "
		+ "WHEN ompmi.counter_plate_menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_default_lang) != '' THEN ompmi.counter_plate_menu_item_name_default_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' AND ompmi.counter_plate_menu_item_name_prefer_lang IS NULL THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN ompmi.counter_plate_menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_prefer_lang) != '' THEN ompmi.counter_plate_menu_item_name_prefer_lang "
		+ "ELSE mi.name_prefer_lang "
		+ "END AS namePreferLang,"
		+ "CASE "
		+ "WHEN ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' AND ompmi.counter_plate_menu_item_name_supportive_lang IS NULL THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN ompmi.counter_plate_menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_supportive_lang) != '' THEN ompmi.counter_plate_menu_item_name_supportive_lang "
		+ "ELSE mi.name_supportive_lang "
		+ "END AS nameSupportiveLang, "
		+ "ompmi.counter_plate_menu_item_name_default_lang AS counterPlateMenuItemNameDefaultLang, "
		+ "ompmi.counter_plate_menu_item_name_prefer_lang AS counterPlateMenuItemNamePreferLang, "
		+ "ompmi.counter_plate_menu_item_name_supportive_lang AS counterPlateMenuItemNameSupportiveLang, "
		+ "ompmi.repeat_number AS repeatNumber "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "WHERE cod.id = :orderId "
		+ "GROUP BY mi.id "
		+ "ORDER BY of2.id, ompmi.menu_item_category_sequence, ompmi.menu_item_sequence, mi.id"
)

@SqlResultSetMapping(
	name = "getNamePlateDetailsResult",
	classes = @ConstructorResult(
		targetClass = AdminReportNamePlateDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "counterPlateMenuItemNameDefaultLang", type = String.class),
			@ColumnResult(name = "counterPlateMenuItemNamePreferLang", type = String.class),
			@ColumnResult(name = "counterPlateMenuItemNameSupportiveLang", type = String.class),
			@ColumnResult(name = "repeatNumber", type = Integer.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateCounterNamePlateReport",
	resultSetMapping = "generateNamePlateReportResult",
	query = "SELECT "
		+ "t.menuItemName "
		+ "FROM (SELECT "
		+ "CASE "
		+ " WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' AND ompmi.counter_plate_menu_item_name_default_lang IS NULL THEN ompmi.menu_item_name_default_lang "
		+ " WHEN :langType = 0 AND ompmi.counter_plate_menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_default_lang) != '' THEN ompmi.counter_plate_menu_item_name_default_lang "
		+ " WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' AND ompmi.counter_plate_menu_item_name_prefer_lang IS NULL THEN ompmi.menu_item_name_prefer_lang "
		+ " WHEN :langType = 1 AND ompmi.counter_plate_menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_prefer_lang) != '' THEN ompmi.counter_plate_menu_item_name_prefer_lang "
		+ " WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' AND ompmi.counter_plate_menu_item_name_supportive_lang IS NULL THEN ompmi.menu_item_name_supportive_lang "
		+ " WHEN :langType = 2 AND ompmi.counter_plate_menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_supportive_lang) != '' THEN ompmi.counter_plate_menu_item_name_supportive_lang "
		+ " WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ " WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ " ELSE mi.name_default_lang "
		+ "END AS menuItemName, "
		+ "ompmi.repeat_number "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "WHERE cod.id = :orderId AND ompmi.repeat_number != 0 "
		+ "GROUP BY mi.id "
		+ "ORDER BY of2.id, ompmi.menu_item_category_sequence, ompmi.menu_item_sequence, mi.id) as t "
		+ "LEFT JOIN numbers ON t.repeat_number >= numbers.number1"
)

@SqlResultSetMapping(
	name = "generateNamePlateReportResult",
	classes = @ConstructorResult(
		targetClass = AdminReportCounterNamePlateReportDto.class,
		columns = {
			@ColumnResult(name = "menuItemName", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateTwoLanguageCounterNamePlateReport",
	resultSetMapping = "generateTwoLanguageCounterNamePlateReportResult",
	query = "SELECT "
		+ "t.menuItemNameInDefaultLang,"
		+ "t.menuItemNameInPreferLang "
		+ "FROM (SELECT "
		+ "CASE "
		+ " WHEN :defaultLang = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' AND ompmi.counter_plate_menu_item_name_default_lang IS NULL THEN ompmi.menu_item_name_default_lang "
		+ " WHEN :defaultLang = 0 AND ompmi.counter_plate_menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_default_lang) != '' THEN ompmi.counter_plate_menu_item_name_default_lang "
		+ " WHEN :defaultLang = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' AND ompmi.counter_plate_menu_item_name_prefer_lang IS NULL THEN ompmi.menu_item_name_prefer_lang "
		+ " WHEN :defaultLang = 1 AND ompmi.counter_plate_menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_prefer_lang) != '' THEN ompmi.counter_plate_menu_item_name_prefer_lang "
		+ " WHEN :defaultLang = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' AND ompmi.counter_plate_menu_item_name_supportive_lang IS NULL THEN ompmi.menu_item_name_supportive_lang "
		+ " WHEN :defaultLang = 2 AND ompmi.counter_plate_menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_supportive_lang) != '' THEN ompmi.counter_plate_menu_item_name_supportive_lang "
		+ " WHEN :defaultLang = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ " WHEN :defaultLang = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ " ELSE mi.name_default_lang "
		+ "END AS menuItemNameInDefaultLang, "
		+ "CASE "
		+ " WHEN :preferLang = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' AND ompmi.counter_plate_menu_item_name_default_lang IS NULL THEN ompmi.menu_item_name_default_lang "
		+ " WHEN :preferLang = 0 AND ompmi.counter_plate_menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_default_lang) != '' THEN ompmi.counter_plate_menu_item_name_default_lang "
		+ " WHEN :preferLang = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' AND ompmi.counter_plate_menu_item_name_prefer_lang IS NULL THEN ompmi.menu_item_name_prefer_lang "
		+ " WHEN :preferLang = 1 AND ompmi.counter_plate_menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_prefer_lang) != '' THEN ompmi.counter_plate_menu_item_name_prefer_lang "
		+ " WHEN :preferLang = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' AND ompmi.counter_plate_menu_item_name_supportive_lang IS NULL THEN ompmi.menu_item_name_supportive_lang "
		+ " WHEN :preferLang = 2 AND ompmi.counter_plate_menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_supportive_lang) != '' THEN ompmi.counter_plate_menu_item_name_supportive_lang "
		+ " WHEN :preferLang = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ " WHEN :preferLang = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ " ELSE mi.name_default_lang "
		+ "END AS menuItemNameInPreferLang, "
		+ "ompmi.repeat_number "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "WHERE cod.id = :orderId AND ompmi.repeat_number != 0 "
		+ "GROUP BY mi.id "
		+ "ORDER BY of2.id, ompmi.menu_item_category_sequence, ompmi.menu_item_sequence, mi.id) as t "
		+ "LEFT JOIN numbers ON t.repeat_number >= numbers.number1"
	)

@SqlResultSetMapping(
	name = "generateTwoLanguageCounterNamePlateReportResult",
	classes = @ConstructorResult(
		targetClass = AdminReportTwoLanguageCounterNamePlateReportDto.class,
		columns = {
			@ColumnResult(name = "menuItemNameInDefaultLang", type = String.class),
			@ColumnResult(name = "menuItemNameInPreferLang", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getTableMenuDetails",
	resultSetMapping = "getTableMenuDetailsResult",
	query = "SELECT "
		+ "ompmi.id AS id, "
		+ "CASE "
		+ "WHEN ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' AND ompmi.counter_plate_menu_item_name_default_lang IS NULL THEN ompmi.menu_item_name_default_lang "
		+ "WHEN ompmi.counter_plate_menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_default_lang) != '' THEN ompmi.counter_plate_menu_item_name_default_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN ompmi.counter_plate_menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_prefer_lang) != '' THEN ompmi.counter_plate_menu_item_name_prefer_lang "
		+ "ELSE mi.name_prefer_lang "
		+ "END AS namePreferLang,"
		+ "CASE "
		+ "WHEN ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN ompmi.counter_plate_menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.counter_plate_menu_item_name_supportive_lang) != '' THEN ompmi.counter_plate_menu_item_name_supportive_lang "
		+ "ELSE mi.name_supportive_lang "
		+ "END AS nameSupportiveLang, "
		+ "ompmi.counter_plate_menu_item_name_default_lang AS counterPlateMenuItemNameDefaultLang, "
		+ "ompmi.counter_plate_menu_item_name_prefer_lang AS counterPlateMenuItemNamePreferLang, "
		+ "ompmi.counter_plate_menu_item_name_supportive_lang AS counterPlateMenuItemNameSupportiveLang, "
		+ "ompmi.repeat_number AS repeatNumber,"
		+ "ompmi.isMenuItemSelected AS isMenuItemSelected "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "WHERE cod.id = :orderId AND IF( 0 IN (:functionIds), TRUE, of2.id IN(:functionIds)) "
		+ "GROUP BY mi.id "
		+ "ORDER BY of2.id, ompmi.menu_item_category_sequence, ompmi.menu_item_sequence, mi.id"
)

@SqlResultSetMapping(
	name = "getTableMenuDetailsResult",
	classes = @ConstructorResult(
		targetClass = AdminReportTableMenuDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "counterPlateMenuItemNameDefaultLang", type = String.class),
			@ColumnResult(name = "counterPlateMenuItemNamePreferLang", type = String.class),
			@ColumnResult(name = "counterPlateMenuItemNameSupportiveLang", type = String.class),
			@ColumnResult(name = "repeatNumber", type = Integer.class),
			@ColumnResult(name = "isMenuItemSelected", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateAddressChitthiReport",
	resultSetMapping = "generateAddressChitthiReportResult",
	query = "SELECT "
		+ "0 AS id, "
		+ " CASE  "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang  "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang  "
		+ " ELSE cod.venue_default_lang "
		+ " END AS address "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN contact c ON c.id = cod.fk_contact_customer_id "
		+ "WHERE -1 IN (:godownIds) AND cod.id = :orderId OR 0 IN (:godownIds) AND cod.id = :orderId   "
		+ "UNION "
		+ "SELECT "
		+ "g.id AS id, "
		+ " CASE "
		+ " WHEN :langType = 1 AND g.address_prefer_lang IS NOT NULL AND g.address_prefer_lang != '' THEN g.address_prefer_lang  "
		+ " WHEN :langType = 2 AND g.address_supportive_lang IS NOT NULL AND g.address_supportive_lang != '' THEN g.address_supportive_lang  "
		+ " ELSE g.address_default_lang "
		+ " END AS address "
		+ "FROM godown g "
		+ "WHERE IF(0 IN (:godownIds), TRUE, g.id IN (:godownIds)) AND g.is_active = 1;"
)

@SqlResultSetMapping(
	name = "generateAddressChitthiReportResult",
	classes = @ConstructorResult(
		targetClass = AddressChitthiReportDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "address", type = String.class)
		}
	)
)

@Entity
public class AdminReportQuery extends AuditIdModelOnly {
}