package com.catering.dao.order_reports.menu_preparation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.CommanDataReportWithOutVenue;
import com.catering.dto.tenant.request.CommonDataForTheReportDto;
import com.catering.dto.tenant.request.EventDistributionNotesDto;
import com.catering.dto.tenant.request.FunctionPerOrderDto;
import com.catering.dto.tenant.request.MenuPreparationCustomMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationExclusiveMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationManagerMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationPremiumImageMenuDto;
import com.catering.dto.tenant.request.MenuPreparationPremiumImageMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationSimpleMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationSloganMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationTheManagerReportDto;
import com.catering.dto.tenant.request.MenuPreparationWithImageMenuReportDto;
import com.catering.dto.tenant.request.MenuPreparationWithPremiumImageMenuReportDto;
import com.catering.dto.tenant.request.SimpleMenuSubReportDto;
import com.catering.dto.tenant.request.TableMenuReportDto;
import com.catering.dto.tenant.request.TwoLanguageMenuReportDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class for handling the query related to the Menu Preparation Report.
 * This entity is associated with the `table_menu_report_footer_notes` table and includes a 
 * native query for fetching the footer notes in different languages based on the order ID.
 * The entity extends {@link AuditIdModelOnly} to include common audit fields like created/updated timestamps.
 * 
 * @see AuditIdModelOnly for inherited audit fields.
 */
@NamedNativeQuery(
	name = "generateCustomMenuReport",
	resultSetMapping = "generateCustomMenuReportResult",
	query = "SELECT "
		+ "ofun.id AS functionId, "
		+ "cod.id AS orderId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofun.person AS person, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.function_address_prefer_lang IS NOT NULL AND ofun.function_address_prefer_lang != '' THEN ofun.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.function_address_supportive_lang IS NOT NULL AND ofun.function_address_supportive_lang != '' THEN ofun.function_address_supportive_lang "
		+ "ELSE ofun.function_address_default_lang "
		+ "END AS functionAddress, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.note_prefer_lang IS NOT NULL AND ofun.note_prefer_lang != '' THEN ofun.note_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.note_supportive_lang IS NOT NULL AND ofun.note_supportive_lang != '' THEN ofun.note_supportive_lang "
		+ "ELSE ofun.note_default_lang "
		+ "END AS functionNote, "
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "ofun.rate AS rate, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END, "
		+ "IF(ompmic.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', ompmic.rupees, ')'), ''), "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmic.note_prefer_lang IS NOT NULL AND ompmic.note_prefer_lang != '' THEN CONCAT(' (', ompmic.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmic.note_supportive_lang IS NOT NULL AND ompmic.note_supportive_lang != '' THEN CONCAT(' (', ompmic.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmic.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItemCategory, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END, "
		+ "IF(ompmi.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', ompmi.rupees, ')'), '') "
		+ ") AS menuItem,"
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN CONCAT('(', ompmi.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN CONCAT('(', ompmi.note_supportive_lang, ')') "
		+ "ELSE CONCAT('(', ompmi.note_default_lang, ')') "
		+ "END, '') AS notes, "
		+ "mi.slogan AS slogan, "
		+ ":langCode AS langCode, "
		+ "CASE "
		+ " WHEN TIME(CONVERT_TZ(ofun.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ " WHEN TIME(CONVERT_TZ(ofun.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ " WHEN TIME(CONVERT_TZ(ofun.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ " WHEN TIME(CONVERT_TZ(ofun.date, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(ofun.date, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ " ELSE '' "
		+ "END AS timeInWord, "
		+ "TIME(ofun.date) AS `time` "
		+ "FROM `customer_order_details` cod "
		+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp on omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "WHERE cod.id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "ORDER BY ofun.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id"
)

@NamedNativeQuery(
	name = "generateShreeRajCateringCustomMenuReport",
	resultSetMapping = "generateCustomMenuReportResult",
	query = "SELECT "
		+ "ofun.id AS functionId, "
		+ "cod.id AS orderId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofun.person AS person, "
		+ "null AS functionAddress, "
		+ "null AS functionNote, "
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "ofun.rate AS rate, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ " WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ " ELSE mic.name_default_lang "
		+ "END AS menuItemCategory, "
		+ "CASE "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN CONCAT(mi.name_prefer_lang, ' (', mi.name_default_lang, ')') "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE IF(mi.name_prefer_lang IS NULL OR mi.name_prefer_lang = '', mi.name_default_lang, CONCAT(mi.name_default_lang, ' (', mi.name_prefer_lang, ') ')) "
		+ "END AS menuItem, "
		+ "null AS notes, "
		+ "mi.slogan AS slogan, "
		+ "CASE "
		+ " WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN ompmi.note_prefer_lang "
		+ " WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN ompmi.note_supportive_lang "
		+ " ELSE ompmi.note_default_lang "
		+ "END AS notes, "
		+ ":langCode AS langCode, "
		+ "CASE "
		+ " WHEN TIME(CONVERT_TZ(ofun.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '04:00:00' AND '10:45:59' THEN :morning "
		+ " WHEN TIME(CONVERT_TZ(ofun.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '10:46:00' AND '16:00:00' THEN :noon "
		+ " WHEN TIME(CONVERT_TZ(ofun.date, :timeZone, (SELECT time_zone FROM company_setting))) BETWEEN '16:01:00' AND '20:00:00' THEN :evening "
		+ " WHEN TIME(CONVERT_TZ(ofun.date, :timeZone, (SELECT time_zone FROM company_setting))) >= '20:01:00' OR TIME(CONVERT_TZ(ofun.date, :timeZone, (SELECT time_zone FROM company_setting))) <= '03:59:59' THEN :night "
		+ " ELSE '' "
		+ "END AS timeInWord, "
		+ "TIME(ofun.date) AS `time` "
		+ "FROM `customer_order_details` cod "
		+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp on omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "WHERE cod.id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "ORDER BY ofun.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id"
)

@NamedNativeQuery(
	name = "generateSathiyaCaterersCustomMenuReport",
	resultSetMapping = "generateCustomMenuReportResult",
	query = "SELECT "
		+ "ofun.id AS functionId, "
		+ "cod.id AS orderId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofun.person AS person, "
		+ "null AS functionAddress, "
		+ "null AS functionNote, "
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "ofun.rate AS rate, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END, "
		+ "IF(ompmic.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', ompmic.rupees, ')'), ''), "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmic.note_prefer_lang IS NOT NULL AND ompmic.note_prefer_lang != '' THEN CONCAT(' (', ompmic.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmic.note_supportive_lang IS NOT NULL AND ompmic.note_supportive_lang != '' THEN CONCAT(' (', ompmic.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmic.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItemCategory, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END, "
		+ "IF(ompmi.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', ompmi.rupees, ')'), ''), "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN CONCAT(' (', ompmi.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN CONCAT(' (', ompmi.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmi.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItem,"
		+ "null AS notes, "
		+ "mi.slogan AS slogan, "
		+ ":langCode AS langCode, "
		+ "NULL AS timeInWord, "
		+ "TIME(ofun.date) AS `time` "
		+ "FROM `customer_order_details` cod "
		+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp on omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "WHERE cod.id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "ORDER BY ofun.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id"
	)

@SqlResultSetMapping(
	name = "generateCustomMenuReportResult",
	classes = @ConstructorResult(
		targetClass = MenuPreparationCustomMenuReportDto.class,
		columns = {
			@ColumnResult(name = "orderId", type = Long.class),
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "functionNote", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "menuItem", type = String.class),
			@ColumnResult(name = "notes", type = String.class),
			@ColumnResult(name = "slogan", type = String.class),
			@ColumnResult(name = "langCode", type = String.class),
			@ColumnResult(name = "timeInWord", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateSimpleMenuSubReport",
	resultSetMapping = "generateSimpleMenuSubReportResult",
	query = "SELECT "
		+ "ofun.id AS functionId, "
		+ ":orderId AS orderId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofun.person AS person, "
		+ "ofun.date AS startDate, "
		+ "ofun.rate AS rate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.function_address_prefer_lang IS NOT NULL AND ofun.function_address_prefer_lang != '' THEN ofun.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.function_address_supportive_lang IS NOT NULL AND ofun.function_address_supportive_lang != '' THEN ofun.function_address_supportive_lang "
		+ "ELSE ofun.function_address_default_lang "
		+ "END AS functionAddress "
		+ "FROM order_function ofun "
		+ "INNER JOIN order_menu_preparation omp on omp.fk_order_function_id = ofun.id "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "ORDER BY ofun.sequence"
)

@SqlResultSetMapping(
	name = "generateSimpleMenuSubReportResult",
	classes = @ConstructorResult(
		targetClass = SimpleMenuSubReportDto.class,
		columns = {
			@ColumnResult(name = "orderId", type = Long.class),
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "startDate", type = LocalDateTime.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "functionAddress", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getCommonDataDateWise",
	resultSetMapping = "getCommonDataDateWiseResult",
	query = "SELECT "
		+ "CASE "
		+ " WHEN :langType = 1 AND et.name_prefer_lang IS NOT NULL AND et.name_prefer_lang != '' THEN et.name_prefer_lang "
		+ " WHEN :langType = 2 AND et.name_supportive_lang IS NOT NULL AND et.name_supportive_lang != '' THEN et.name_supportive_lang "
		+ " ELSE et.name_default_lang "
		+ "END AS eventName, "
		+ "cod.meeting_date AS meetingDate, "
		+ "cod.event_main_date AS eventMainDate, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.meal_note_prefer_lang IS NOT NULL AND cod.meal_note_prefer_lang != '' THEN cod.meal_note_prefer_lang "
		+ " WHEN :langType = 2 AND cod.meal_note_supportive_lang IS NOT NULL AND cod.meal_note_supportive_lang != '' THEN cod.meal_note_supportive_lang "
		+ " ELSE cod.meal_note_default_lang "
		+ "END AS notes, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mt.name_default_lang IS NOT NULL AND mt.name_prefer_lang != '' THEN mt.name_prefer_lang "
		+ " WHEN :langType = 2 AND mt.name_supportive_lang IS NOT NULL AND mt.name_supportive_lang != '' THEN mt.name_supportive_lang "
		+ " ELSE mt.name_default_lang "
		+ "END AS mealTypeName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND contact_customer.name_prefer_lang IS NOT NULL AND contact_customer.name_prefer_lang != '' THEN contact_customer.name_prefer_lang "
		+ " WHEN :langType = 2 AND contact_customer.name_supportive_lang IS NOT NULL AND contact_customer.name_supportive_lang != '' THEN contact_customer.name_supportive_lang "
		+ " ELSE contact_customer.name_default_lang "
		+ "END AS customerName, "
		+ "contact_customer.mobile_number AS mobileNumber, "
		+ "contact_customer.email AS customerEmail, "
		+ "contact_manager.mobile_number AS managerNumber, "
		+ "contact_customer.office_number AS customerOfficeNumber, "
		+ "CASE "
		+ " WHEN :langType = 1 AND contact_manager.name_prefer_lang IS NOT NULL AND contact_manager.name_prefer_lang != '' THEN contact_manager.name_prefer_lang "
		+ " WHEN :langType = 2 AND contact_manager.name_supportive_lang IS NOT NULL AND contact_manager.name_supportive_lang != '' THEN contact_manager.name_supportive_lang "
		+ " ELSE contact_manager.name_default_lang "
		+ "END AS managerName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END AS venue, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.party_plot_name_prefer_lang IS NOT NULL AND cod.party_plot_name_prefer_lang != '' THEN cod.party_plot_name_prefer_lang "
		+ " WHEN :langType = 2 AND cod.party_plot_name_supportive_lang IS NOT NULL AND cod.party_plot_name_supportive_lang != '' THEN cod.party_plot_name_supportive_lang "
		+ " ELSE cod.party_plot_name_default_lang "
		+ "END AS hallName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND obrn.notes_prefer_lang IS NOT NULL AND obrn.notes_prefer_lang != '' THEN obrn.notes_prefer_lang "
		+ " WHEN :langType = 2 AND obrn.notes_supportive_lang IS NOT NULL AND obrn.notes_supportive_lang != '' THEN obrn.notes_supportive_lang "
		+ " ELSE obrn.notes_default_lang "
		+ "END AS orderNotes, "
		+ "CASE "
		+ "WHEN :langType = 1 AND cod.meal_note_prefer_lang IS NOT NULL AND cod.meal_note_prefer_lang != '' THEN cod.meal_note_prefer_lang "
		+ "WHEN :langType = 2 AND cod.meal_note_supportive_lang IS NOT NULL AND cod.meal_note_supportive_lang != '' THEN cod.meal_note_supportive_lang "
		+ "ELSE cod.meal_note_default_lang "
		+ "END AS mealNotes "
		+ "FROM customer_order_details cod "
		+ "LEFT JOIN order_booking_report_notes obrn ON obrn.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN event_type et ON et.id = cod.fk_event_type_id "
		+ "INNER JOIN meal_type mt ON mt.id = cod.fk_meal_type_id "
		+ "LEFT JOIN contact contact_customer ON contact_customer.id = cod.fk_contact_customer_id "
		+ "LEFT JOIN contact contact_manager ON contact_manager.id = cod.fk_contact_manager_id "
		+ "WHERE cod.id = :orderId"
)

@SqlResultSetMapping(
	name = "getCommonDataDateWiseResult",
	classes = @ConstructorResult(
		targetClass = CommonDataForTheReportDto.class,
		columns = {
			@ColumnResult(name = "eventName", type = String.class),
			@ColumnResult(name = "meetingDate", type = LocalDate.class),
			@ColumnResult(name = "eventMainDate", type = LocalDate.class),
			@ColumnResult(name = "notes", type = String.class),
			@ColumnResult(name = "mealTypeName", type = String.class),
			@ColumnResult(name = "customerName", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "customerEmail", type = String.class),
			@ColumnResult(name = "managerNumber", type = String.class),
			@ColumnResult(name = "managerName", type = String.class),
			@ColumnResult(name = "customerOfficeNumber", type = String.class),
			@ColumnResult(name = "hallName", type = String.class),
			@ColumnResult(name = "orderNotes", type = String.class),
			@ColumnResult(name = "mealNotes", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getCommonDataForReportWithOutVenue",
	resultSetMapping = "getCommonDataForReportWithOutVenueResult",
	query = "SELECT "
		+ "CASE "
		+ " WHEN :langType = 1 AND et.name_prefer_lang IS NOT NULL AND et.name_prefer_lang != '' THEN et.name_prefer_lang "
		+ " WHEN :langType = 2 AND et.name_supportive_lang IS NOT NULL AND et.name_supportive_lang != '' THEN et.name_supportive_lang "
		+ " ELSE et.name_default_lang "
		+ "END AS eventName, "
		+ "cod.event_main_date AS eventMainDate, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.meal_note_prefer_lang IS NOT NULL AND cod.meal_note_prefer_lang != '' THEN cod.meal_note_prefer_lang "
		+ " WHEN :langType = 2 AND cod.meal_note_supportive_lang IS NOT NULL AND cod.meal_note_supportive_lang != '' THEN cod.meal_note_supportive_lang "
		+ " ELSE cod.meal_note_default_lang "
		+ "END AS notes, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mt.name_default_lang IS NOT NULL AND mt.name_prefer_lang != '' THEN mt.name_prefer_lang "
		+ " WHEN :langType = 2 AND mt.name_supportive_lang IS NOT NULL AND mt.name_supportive_lang != '' THEN mt.name_supportive_lang "
		+ " ELSE mt.name_default_lang "
		+ "END AS mealTypeName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND contact_customer.name_prefer_lang IS NOT NULL AND contact_customer.name_prefer_lang != '' THEN contact_customer.name_prefer_lang "
		+ " WHEN :langType = 2 AND contact_customer.name_supportive_lang IS NOT NULL AND contact_customer.name_supportive_lang != '' THEN contact_customer.name_supportive_lang "
		+ " ELSE contact_customer.name_default_lang "
		+ "END AS customerName, "
		+ "contact_customer.mobile_number AS mobileNumber, "
		+ "contact_customer.email AS customerEmail, "
		+ "contact_manager.mobile_number AS managerNumber, "
		+ "contact_customer.office_number AS customerOfficeNumber, "
		+ "CASE "
		+ " WHEN :langType = 1 AND contact_manager.name_prefer_lang IS NOT NULL AND contact_manager.name_prefer_lang != '' THEN contact_manager.name_prefer_lang "
		+ " WHEN :langType = 2 AND contact_manager.name_supportive_lang IS NOT NULL AND contact_manager.name_supportive_lang != '' THEN contact_manager.name_supportive_lang "
		+ " ELSE contact_manager.name_default_lang "
		+ "END AS managerName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.party_plot_name_prefer_lang IS NOT NULL AND cod.party_plot_name_prefer_lang != '' THEN cod.party_plot_name_prefer_lang "
		+ " WHEN :langType = 2 AND cod.party_plot_name_supportive_lang IS NOT NULL AND cod.party_plot_name_supportive_lang != '' THEN cod.party_plot_name_supportive_lang "
		+ " ELSE cod.party_plot_name_default_lang "
		+ "END AS hallName "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN event_type et ON et.id = cod.fk_event_type_id "
		+ "INNER JOIN meal_type mt ON mt.id = cod.fk_meal_type_id "
		+ "LEFT JOIN contact contact_customer ON contact_customer.id = cod.fk_contact_customer_id "
		+ "LEFT JOIN contact contact_manager ON contact_manager.id = cod.fk_contact_manager_id "
		+ "WHERE cod.id = :orderId"
)

@SqlResultSetMapping(
	name = "getCommonDataForReportWithOutVenueResult",
	classes = @ConstructorResult(
		targetClass = CommanDataReportWithOutVenue.class,
		columns = {
			@ColumnResult(name = "eventName", type = String.class),
			@ColumnResult(name = "eventMainDate", type = LocalDate.class),
			@ColumnResult(name = "notes", type = String.class),
			@ColumnResult(name = "mealTypeName", type = String.class),
			@ColumnResult(name = "customerName", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "customerEmail", type = String.class),
			@ColumnResult(name = "managerNumber", type = String.class),
			@ColumnResult(name = "managerName", type = String.class),
			@ColumnResult(name = "customerOfficeNumber", type = String.class),
			@ColumnResult(name = "hallName", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateManagerWorkingReport",
	resultSetMapping = "generateInstructionMenuReportAndManagerWorkingReportResult",
	query = "SELECT "
		+ "ofun.id AS functionId, "
		+ "ofun.fk_customer_order_details_id AS orderId, "
		+ "ofun.person AS person, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.function_address_prefer_lang IS NOT NULL AND ofun.function_address_prefer_lang != '' THEN ofun.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.function_address_supportive_lang IS NOT NULL AND ofun.function_address_supportive_lang != '' THEN ofun.function_address_supportive_lang "
		+ "ELSE ofun.function_address_default_lang "
		+ "END AS functionAddress, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.note_prefer_lang IS NOT NULL AND ofun.note_prefer_lang != '' THEN ofun.note_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.note_supportive_lang IS NOT NULL AND ofun.note_supportive_lang != '' THEN ofun.note_supportive_lang "
		+ "ELSE ofun.note_default_lang "
		+ "END AS functionNote, "
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END,"
		+ "'<br>', "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmic.note_prefer_lang IS NOT NULL AND ompmic.note_prefer_lang != '' THEN CONCAT(' (', ompmic.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmic.note_supportive_lang IS NOT NULL AND ompmic.note_supportive_lang != '' THEN CONCAT(' (', ompmic.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmic.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItemCategory, "
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
		+ "END, '') "
		+ ") AS menuItem, "
		+ "CONCAT(CASE WHEN ompmi.order_type = 1 THEN '$ChefLabour' WHEN ompmi.order_type = 2 THEN '$OutsideLabour' ELSE NULL END, ' - ', GROUP_CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END  SEPARATOR ', ')) AS agencyName "
		+ "FROM order_function ofun "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "LEFT JOIN order_menu_allocation_type omat ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "LEFT JOIN contact c on c.id = omat.fk_contact_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "GROUP BY ompmi.id "
		+ "ORDER BY ofun.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id"
)

@NamedNativeQuery(
	name = "generateInstructionMenuReport",
	resultSetMapping = "generateInstructionMenuReportAndManagerWorkingReportResult",
	query = "SELECT "
		+ "ofun.id AS functionId, "
		+ "NULL AS orderId, "
		+ "ofun.person AS person, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.function_address_prefer_lang IS NOT NULL AND ofun.function_address_prefer_lang != '' THEN ofun.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.function_address_supportive_lang IS NOT NULL AND ofun.function_address_supportive_lang != '' THEN ofun.function_address_supportive_lang "
		+ "ELSE ofun.function_address_default_lang "
		+ "END AS functionAddress, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.note_prefer_lang IS NOT NULL AND ofun.note_prefer_lang != '' THEN ofun.note_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.note_supportive_lang IS NOT NULL AND ofun.note_supportive_lang != '' THEN ofun.note_supportive_lang "
		+ "ELSE ofun.note_default_lang "
		+ "END AS functionNote, "
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END,"
		+ "'<br>', "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmic.note_prefer_lang IS NOT NULL AND ompmic.note_prefer_lang != '' THEN CONCAT(' (', ompmic.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmic.note_supportive_lang IS NOT NULL AND ompmic.note_supportive_lang != '' THEN CONCAT(' (', ompmic.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmic.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItemCategory, "
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
		+ "END, '') "
		+ ") AS menuItem, "
		+ "NULL AS agencyName "
		+ "FROM order_function ofun "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "ORDER BY ofun.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id"
)

@SqlResultSetMapping(
	name = "generateInstructionMenuReportAndManagerWorkingReportResult",
	classes = @ConstructorResult(
		targetClass = MenuPreparationManagerMenuReportDto.class,
		columns = {
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "orderId", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "functionNote", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "menuItem", type = String.class),
			@ColumnResult(name = "agencyName", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateCommonDataForTheManagerWorkingReport",
	resultSetMapping = "generateCommonDataForTheManagerWorkingReportResult",
	query = "SELECT "
		+ "CASE "
		+ " WHEN :langType = 1 AND c_customer.name_prefer_lang IS NOT NULL AND c_customer.name_prefer_lang != '' THEN c_customer.name_prefer_lang "
		+ " WHEN :langType = 2 AND c_customer.name_supportive_lang IS NOT NULL AND c_customer.name_supportive_lang != '' THEN c_customer.name_supportive_lang "
		+ " ELSE c_customer.name_default_lang "
		+ "END AS customerName, "
		+ "c_customer.mobile_number AS customerMobileNumber, "
		+ "CASE "
		+ " WHEN :langType = 1 AND em.name_prefer_lang IS NOT NULL AND em.name_prefer_lang != '' THEN em.name_prefer_lang "
		+ " WHEN :langType = 2 AND em.name_supportive_lang IS NOT NULL AND em.name_supportive_lang != '' THEN em.name_supportive_lang "
		+ " ELSE em.name_default_lang "
		+ "END AS eventName, "
		+ "cod.event_main_date AS eventDate, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.party_plot_name_prefer_lang IS NOT NULL AND cod.party_plot_name_prefer_lang != '' THEN cod.party_plot_name_prefer_lang "
		+ " WHEN :langType = 2 AND cod.party_plot_name_supportive_lang IS NOT NULL AND cod.party_plot_name_supportive_lang != '' THEN cod.party_plot_name_supportive_lang "
		+ " ELSE cod.party_plot_name_default_lang "
		+ "END AS hallName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND c_customer.home_address_prefer_lang IS NOT NULL AND c_customer.home_address_prefer_lang != '' THEN c_customer.home_address_prefer_lang "
		+ " WHEN :langType = 2 AND c_customer.home_address_supportive_lang IS NOT NULL AND c_customer.home_address_supportive_lang != '' THEN c_customer.home_address_supportive_lang "
		+ " ELSE c_customer.home_address_default_lang "
		+ "END AS customerHomeAddress, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mt.name_default_lang IS NOT NULL AND mt.name_prefer_lang != '' THEN mt.name_prefer_lang "
		+ " WHEN :langType = 2 AND mt.name_supportive_lang IS NOT NULL AND mt.name_supportive_lang != '' THEN mt.name_supportive_lang "
		+ " ELSE mt.name_default_lang "
		+ "END AS mealTypeName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.meal_note_prefer_lang IS NOT NULL AND cod.meal_note_prefer_lang != '' THEN cod.meal_note_prefer_lang "
		+ " WHEN :langType = 2 AND cod.meal_note_supportive_lang IS NOT NULL AND cod.meal_note_supportive_lang != '' THEN cod.meal_note_supportive_lang "
		+ " ELSE cod.meal_note_default_lang "
		+ "END AS notes, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END AS venue "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN event_type em ON em.id = cod.fk_event_type_id "
		+ "LEFT JOIN contact c_customer ON c_customer.id = cod.fk_contact_customer_id "
		+ "LEFT JOIN meal_type mt ON mt.id = cod.fk_meal_type_id "
		+ "WHERE cod.id = :orderId"
)

@SqlResultSetMapping(
	name = "generateCommonDataForTheManagerWorkingReportResult",
	classes = @ConstructorResult(
		targetClass = MenuPreparationTheManagerReportDto.class,
		columns = {
			@ColumnResult(name = "customerName", type = String.class),
			@ColumnResult(name = "customerMobileNumber", type = String.class),
			@ColumnResult(name = "eventName", type = String.class),
			@ColumnResult(name = "eventDate", type = LocalDate.class),
			@ColumnResult(name = "hallName", type = String.class),
			@ColumnResult(name = "customerHomeAddress", type = String.class),
			@ColumnResult(name = "mealTypeName", type = String.class),
			@ColumnResult(name = "notes", type = String.class),
			@ColumnResult(name = "venue", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateMenuPreparationSloganMenuReport",
	resultSetMapping = "generateMenuPreparationSloganMenuReportResult",
	query = "SELECT "
		+ "of2.id AS functionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "of2.date AS date, "
		+ "of2.end_date AS endDate, "
		+ "of2.rate AS rate, "
		+ "CASE "
		+ " WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
		+ " ELSE of2.function_address_default_lang "
		+ "END AS functionAddress, "
		+ "CASE "
		+ " WHEN :langType = 1 AND of2.note_prefer_lang IS NOT NULL AND of2.note_prefer_lang != '' THEN of2.note_prefer_lang "
		+ " WHEN :langType = 2 AND of2.note_supportive_lang IS NOT NULL AND of2.note_supportive_lang != '' THEN of2.note_supportive_lang "
		+ " ELSE of2.note_default_lang "
		+ "END AS functionNote, "
		+ "of2.person AS person, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END, "
		+ "IF(ompmic.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', ompmic.rupees, ')'), ''), "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmic.note_prefer_lang IS NOT NULL AND ompmic.note_prefer_lang != '' THEN CONCAT(' (', ompmic.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmic.note_supportive_lang IS NOT NULL AND ompmic.note_supportive_lang != '' THEN CONCAT(' (', ompmic.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmic.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItemCategory, "
		+ "CONCAT( "
		+ "CASE "
		+ " WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ " WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ " WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ " WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ " WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ " ELSE mi.name_default_lang "
		+ "END, "
		+ "IF(ompmi.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', ompmi.rupees, ')'), ''), "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN CONCAT(' (', ompmi.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN CONCAT(' (', ompmi.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmi.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItem, "
		+ "mi.slogan AS slogan "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN meal_type mt ON mt.id = cod.fk_meal_type_id "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "WHERE cod.id = :orderId AND IF( 0 IN (:functionIds), TRUE, of2.id IN(:functionIds)) "
		+ "ORDER BY of2.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id"
)

@NamedNativeQuery(
	name = "generateMenuPreparationSloganMenuReportCustomized",
	resultSetMapping = "generateMenuPreparationSloganMenuReportResult",
	query = "SELECT "
		+ "of2.id AS functionId, "
		+ "CASE "
		+ " WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ " WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ " ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "of2.date AS date, "
		+ "of2.end_date AS endDate, "
		+ "of2.rate AS rate, "
		+ "of2.person AS person, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ " WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ " ELSE mic.name_default_lang "
		+ "END AS menuItemCategory, "
		+ "mi.slogan AS slogan, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ " WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ " ELSE mi.name_default_lang "
		+ "END AS menuItem, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ompmi.note_default_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN ompmi.note_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN ompmi.note_supportive_lang "
		+ "ELSE ompmi.note_default_lang "
		+ "END AS notes "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN meal_type mt ON mt.id = cod.fk_meal_type_id "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "WHERE cod.id = :orderId "
		+ "ORDER BY of2.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id"
)

@SqlResultSetMapping(
	name = "generateMenuPreparationSloganMenuReportResult",
	classes = @ConstructorResult(
		targetClass = MenuPreparationSloganMenuReportDto.class,
		columns = {
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "functionNote", type = String.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "menuItem", type = String.class),
			@ColumnResult(name = "slogan", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateMenuPreparationImageAndSloganMenuReport",
	resultSetMapping = "generateMenuPreparationImageAndSloganMenuReportResult",
	query = "SELECT "
		+ "of2.id AS functionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "of2.date AS date, "
		+ "of2.end_date AS endDate, "
		+ "of2.rate AS rate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
		+ "ELSE of2.function_address_default_lang "
		+ "END AS functionAddress, "
		+ "CASE "
		+ "WHEN :langType = 1 AND of2.note_prefer_lang IS NOT NULL AND of2.note_prefer_lang != '' THEN of2.note_prefer_lang "
		+ "WHEN :langType = 2 AND of2.note_supportive_lang IS NOT NULL AND of2.note_supportive_lang != '' THEN of2.note_supportive_lang "
		+ "ELSE of2.note_default_lang "
		+ "END AS functionNote, "
		+ "of2.person AS person, "
		+ "mic.id AS menuItemCategoryId, "
		+ "mi.id AS menuItemId, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END, "
		+ "IF(ompmic.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', ompmic.rupees, ')'), ''), "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmic.note_prefer_lang IS NOT NULL AND ompmic.note_prefer_lang != '' THEN CONCAT(' (', ompmic.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmic.note_supportive_lang IS NOT NULL AND ompmic.note_supportive_lang != '' THEN CONCAT(' (', ompmic.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmic.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItemCategory, "
		+ "mi.slogan AS slogan, "
		+ "CONCAT( "
		+ "CASE "
		+ " WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ " WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ " WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ " WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ " WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ " ELSE mi.name_default_lang "
		+ "END, "
		+ "IF(ompmi.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', ompmi.rupees, ')'), ''), "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN CONCAT(' (', ompmi.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN CONCAT(' (', ompmi.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmi.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItem "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN meal_type mt ON mt.id = cod.fk_meal_type_id "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "WHERE cod.id = :orderId AND IF( 0 IN (:functionIds), TRUE, of2.id IN(:functionIds)) "
		+ "ORDER BY of2.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id"
)

@SqlResultSetMapping(
	name = "generateMenuPreparationImageAndSloganMenuReportResult",
	classes = @ConstructorResult(
		targetClass = MenuPreparationPremiumImageMenuDto.class,
		columns = {
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "functionNote", type = String.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "menuItem", type = String.class),
			@ColumnResult(name = "slogan", type = String.class),
			@ColumnResult(name = "menuItemCategoryId", type = Long.class),
			@ColumnResult(name = "menuItemId", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateExclusiveMenuReport",
	resultSetMapping = "generateExclusiveMenuReportResult",
	query = "SELECT "
		+ "ofun.id AS functionId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofun.person AS person, "
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "ofun.rate AS rate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.function_address_prefer_lang IS NOT NULL AND ofun.function_address_prefer_lang != '' THEN ofun.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.function_address_supportive_lang IS NOT NULL AND ofun.function_address_supportive_lang != '' THEN ofun.function_address_supportive_lang "
		+ "ELSE ofun.function_address_default_lang "
		+ "END AS functionAddress, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.note_prefer_lang IS NOT NULL AND ofun.note_prefer_lang != '' THEN ofun.note_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.note_supportive_lang IS NOT NULL AND ofun.note_supportive_lang != '' THEN ofun.note_supportive_lang "
		+ "ELSE ofun.note_default_lang "
		+ "END AS functionNote, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END, "
		+ "IF(ompmic.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', ompmic.rupees, ')'), ''), "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmic.note_prefer_lang IS NOT NULL AND ompmic.note_prefer_lang != '' THEN CONCAT(' (', ompmic.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmic.note_supportive_lang IS NOT NULL AND ompmic.note_supportive_lang != '' THEN CONCAT(' (', ompmic.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmic.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItemCategory, "
		+ "CONCAT( "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END, "
		+ "IF(ompmi.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', ompmi.rupees, ')'), ''), "
		+ "IFNULL(CASE "
		+ "WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN CONCAT(' (', ompmi.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN CONCAT(' (', ompmi.note_supportive_lang, ')') "
		+ "ELSE CONCAT(' (', ompmi.note_default_lang, ')') "
		+ "END, '') "
		+ ") AS menuItem, "
		+ "CASE "
		+ " WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN ompmi.note_prefer_lang "
		+ " WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN ompmi.note_supportive_lang "
		+ " ELSE ompmi.note_default_lang "
		+ "END AS notes, "
		+ "cod.event_main_date AS bookingDate "
		+ "FROM order_function ofun "
		+ "INNER JOIN order_menu_preparation omp on omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "INNER JOIN customer_order_details cod ON ofun.fk_customer_order_details_id = cod.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "ORDER BY ofun.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id"
)

@SqlResultSetMapping(
	name = "generateExclusiveMenuReportResult",
	classes = @ConstructorResult(
		targetClass = MenuPreparationExclusiveMenuReportDto.class,
		columns = {
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "functionNote", type = String.class),
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "menuItem", type = String.class),
			@ColumnResult(name = "bookingDate", type = LocalDate.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateCommonDataForThePremiumImageReport",
	resultSetMapping = "generateCommonDataForThePremiumImageReportResult",
	query = "SELECT "
		+ "CASE "
		+ " WHEN :langType = 1 AND contact_customer.name_prefer_lang IS NOT NULL AND contact_customer.name_prefer_lang != '' THEN contact_customer.name_prefer_lang "
		+ " WHEN :langType = 2 AND contact_customer.name_supportive_lang IS NOT NULL AND contact_customer.name_supportive_lang != '' THEN contact_customer.name_supportive_lang "
		+ " ELSE contact_customer.name_default_lang "
		+ "END AS customerName, "
		+ "contact_customer.mobile_number AS mobileNumber, "
		+ "CASE "
		+ " WHEN :langType = 1 AND em.name_prefer_lang IS NOT NULL AND em.name_prefer_lang != '' THEN em.name_prefer_lang "
		+ " WHEN :langType = 2 AND em.name_supportive_lang IS NOT NULL AND em.name_supportive_lang != '' THEN em.name_supportive_lang "
		+ " ELSE em.name_default_lang "
		+ "END AS eventName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.party_plot_name_prefer_lang IS NOT NULL AND cod.party_plot_name_prefer_lang != '' THEN cod.party_plot_name_prefer_lang "
		+ " WHEN :langType = 2 AND cod.party_plot_name_supportive_lang IS NOT NULL AND cod.party_plot_name_supportive_lang != '' THEN cod.party_plot_name_supportive_lang "
		+ " ELSE cod.party_plot_name_default_lang "
		+ "END AS hallName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END AS venue, "
		+ "CASE "
		+ " WHEN :langType = 1 AND contact_customer.home_address_prefer_lang IS NOT NULL AND contact_customer.home_address_prefer_lang != '' THEN contact_customer.home_address_prefer_lang "
		+ " WHEN :langType = 2 AND contact_customer.home_address_supportive_lang IS NOT NULL AND contact_customer.home_address_supportive_lang != '' THEN contact_customer.home_address_supportive_lang "
		+ " ELSE contact_customer.home_address_default_lang "
		+ "END AS customerAddress, "
		+ "CASE "
		+ " WHEN :langType = 1 AND fp.name_default_lang IS NOT NULL AND fp.name_prefer_lang != '' THEN fp.name_prefer_lang "
		+ " WHEN :langType = 2 AND fp.name_supportive_lang IS NOT NULL AND fp.name_supportive_lang != '' THEN fp.name_supportive_lang "
		+ " ELSE fp.name_default_lang "
		+ "END AS mealTypeName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.meal_note_prefer_lang IS NOT NULL AND cod.meal_note_prefer_lang != '' THEN cod.meal_note_prefer_lang "
		+ " WHEN :langType = 2 AND cod.meal_note_supportive_lang IS NOT NULL AND cod.meal_note_supportive_lang != '' THEN cod.meal_note_supportive_lang "
		+ " ELSE cod.meal_note_default_lang "
		+ "END AS notes "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN event_type em ON em.id = cod.fk_event_type_id "
		+ "INNER JOIN meal_type fp ON fp.id = cod.fk_meal_type_id "
		+ "LEFT JOIN contact contact_customer ON contact_customer.id = cod.fk_contact_customer_id "
		+ "WHERE cod.id = :orderId"
)

@SqlResultSetMapping(
	name = "generateCommonDataForThePremiumImageReportResult",
	classes = @ConstructorResult(
		targetClass = MenuPreparationWithPremiumImageMenuReportDto.class,
		columns = {
			@ColumnResult(name = "customerName", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "eventName", type = String.class),
			@ColumnResult(name = "hallName", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "customerAddress", type = String.class),
			@ColumnResult(name = "mealTypeName", type = String.class),
			@ColumnResult(name = "notes", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateMenuWithPremiumImageReport",
	resultSetMapping = "generateMenuWithPremiumImageReportResult",
	query = "SELECT "
		+ "of2.id AS functionId, "
		+ "CASE "
		+ " WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ " WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ " ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "of2.date AS date, "
		+ "of2.end_date AS endDate, "
		+ "of2.person AS person, "
		+ "CASE "
		+ " WHEN :langType = 1 AND of2.function_address_prefer_lang IS NOT NULL AND of2.function_address_prefer_lang != '' THEN of2.function_address_prefer_lang "
		+ " WHEN :langType = 2 AND of2.function_address_supportive_lang IS NOT NULL AND of2.function_address_supportive_lang != '' THEN of2.function_address_supportive_lang "
		+ " ELSE of2.function_address_default_lang "
		+ "END AS functionAddress, "
		+ "CASE "
		+ " WHEN :langType = 1 AND of2.note_prefer_lang IS NOT NULL AND of2.note_prefer_lang != '' THEN of2.note_prefer_lang "
		+ " WHEN :langType = 2 AND of2.note_supportive_lang IS NOT NULL AND of2.note_supportive_lang != '' THEN of2.note_supportive_lang "
		+ " ELSE of2.note_default_lang "
		+ "END AS functionNote, "
		+ "of2.rate AS rate "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN meal_type mt ON mt.id = cod.fk_meal_type_id "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "WHERE cod.id = :orderId AND IF( 0 IN (:functionIds), TRUE, of2.id IN(:functionIds)) "
		+ "ORDER BY of2.sequence"
)

@SqlResultSetMapping(
	name = "generateMenuWithPremiumImageReportResult",
	classes = @ConstructorResult(
		targetClass = MenuPreparationPremiumImageMenuReportDto.class,
		columns = {
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "functionNote", type = String.class),
			@ColumnResult(name = "rate", type = Double.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateCommonDataForTheMenuWithImageMenuReport",
	resultSetMapping = "generateCommonDataForTheMenuWithImageMenuReportResult",
	query = "SELECT "
		+ "CONCAT_WS(' ', "
		+ "CASE "
		+ " WHEN :langType = 1 AND cu.first_name_prefer_lang IS NOT NULL AND cu.first_name_prefer_lang != '' THEN cu.first_name_prefer_lang "
		+ " WHEN :langType = 2 AND cu.first_name_supportive_lang IS NOT NULL AND cu.first_name_supportive_lang != '' THEN cu.first_name_supportive_lang "
		+ " ELSE cu.first_name_default_lang END, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cu.last_name_prefer_lang IS NOT NULL AND cu.last_name_prefer_lang != '' THEN cu.last_name_prefer_lang "
		+ " WHEN :langType = 2 AND cu.last_name_supportive_lang IS NOT NULL AND cu.last_name_supportive_lang != '' THEN cu.last_name_supportive_lang "
		+ " ELSE cu.last_name_default_lang END "
		+ ") AS companyUserName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND cp.address_prefer_lang IS NOT NULL AND cp.address_prefer_lang != '' THEN cp.address_prefer_lang "
		+ "WHEN :langType = 2 AND cp.address_supportive_lang IS NOT NULL AND cp.address_supportive_lang != '' THEN cp.address_supportive_lang "
		+ "ELSE cp.address_default_lang "
		+ "END AS companyAddress, "
		+ "cp.mobile_number AS companyMobileNumber, "
		+ "CASE "
		+ " WHEN :langType = 1 AND c_customer.name_prefer_lang IS NOT NULL AND c_customer.name_prefer_lang != '' THEN c_customer.name_prefer_lang "
		+ " WHEN :langType = 2 AND c_customer.name_supportive_lang IS NOT NULL AND c_customer.name_supportive_lang != '' THEN c_customer.name_supportive_lang "
		+ " ELSE c_customer.name_default_lang "
		+ "END AS customerName, "
		+ "c_customer.mobile_number AS customerMobile, "
		+ "CASE "
		+ " WHEN :langType = 1 AND et.name_prefer_lang IS NOT NULL AND et.name_prefer_lang != '' THEN et.name_prefer_lang "
		+ " WHEN :langType = 2 AND et.name_supportive_lang IS NOT NULL AND et.name_supportive_lang != '' THEN et.name_supportive_lang "
		+ " ELSE et.name_default_lang "
		+ "END AS eventName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.party_plot_name_prefer_lang IS NOT NULL AND cod.party_plot_name_prefer_lang != '' THEN cod.party_plot_name_prefer_lang "
		+ " WHEN :langType = 2 AND cod.party_plot_name_supportive_lang IS NOT NULL AND cod.party_plot_name_supportive_lang != '' THEN cod.party_plot_name_supportive_lang "
		+ " ELSE cod.party_plot_name_default_lang "
		+ "END AS hallName, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ " WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ " ELSE cod.venue_default_lang "
		+ "END AS venue, "
		+ "CASE "
		+ " WHEN :langType = 1 AND c_customer.home_address_prefer_lang IS NOT NULL AND c_customer.home_address_prefer_lang != '' THEN c_customer.home_address_prefer_lang "
		+ " WHEN :langType = 2 AND c_customer.home_address_supportive_lang IS NOT NULL AND c_customer.home_address_supportive_lang != '' THEN c_customer.home_address_supportive_lang "
		+ " ELSE c_customer.home_address_default_lang "
		+ "END AS customerHomeAddress, "
		+ "c_customer.email AS customerEmail, "
		+ "CASE "
		+ " WHEN :langType = 1 AND c_manager.name_prefer_lang IS NOT NULL AND c_manager.name_prefer_lang != '' THEN c_manager.name_prefer_lang "
		+ " WHEN :langType = 2 AND c_manager.name_supportive_lang IS NOT NULL AND c_manager.name_supportive_lang != '' THEN c_manager.name_supportive_lang "
		+ " ELSE c_manager.name_default_lang "
		+ "END AS managerName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c_chef.name_prefer_lang IS NOT NULL AND c_chef.name_prefer_lang != '' THEN c_chef.name_prefer_lang "
		+ "WHEN :langType = 2 AND c_chef.name_supportive_lang IS NOT NULL AND c_chef.name_supportive_lang != '' THEN c_chef.name_supportive_lang "
		+ "ELSE c_chef.name_default_lang "
		+ "END AS chefName, "
		+ "cod.event_main_date AS eventDate, "
		+ "CASE "
		+ " WHEN :langType = 1 AND mt.name_default_lang IS NOT NULL AND mt.name_prefer_lang != '' THEN mt.name_prefer_lang "
		+ " WHEN :langType = 2 AND mt.name_supportive_lang IS NOT NULL AND mt.name_supportive_lang != '' THEN mt.name_supportive_lang "
		+ " ELSE mt.name_default_lang "
		+ "END AS mealTypename, "
		+ "CASE "
		+ " WHEN :langType = 1 AND cod.meal_note_prefer_lang IS NOT NULL AND cod.meal_note_prefer_lang != '' THEN cod.meal_note_prefer_lang "
		+ " WHEN :langType = 2 AND cod.meal_note_supportive_lang IS NOT NULL AND cod.meal_note_supportive_lang != '' THEN cod.meal_note_supportive_lang "
		+ " ELSE cod.meal_note_default_lang "
		+ "END AS notes "
		+ "FROM company_user cu "
		+ "JOIN company_preferences cp "
		+ "JOIN customer_order_details cod "
		+ "INNER JOIN event_type et ON et.id = cod.fk_event_type_id "
		+ "LEFT JOIN contact c_customer ON c_customer.id = cod.fk_contact_customer_id "
		+ "LEFT JOIN contact c_manager ON c_manager.id = cod.fk_contact_manager_id "
		+ "LEFT JOIN contact c_chef ON c_chef.id = cod.fk_contact_chef_id "
		+ "LEFT JOIN meal_type mt ON mt.id = cod.fk_meal_type_id "
		+ "WHERE cu.id = 1 AND cod.id = :orderId"
)
@SqlResultSetMapping(
	name = "generateCommonDataForTheMenuWithImageMenuReportResult",
	classes = @ConstructorResult(
		targetClass = MenuPreparationWithImageMenuReportDto.class,
		columns = {
			@ColumnResult(name = "companyUserName", type = String.class),
			@ColumnResult(name = "companyAddress", type = String.class),
			@ColumnResult(name = "companyMobileNumber", type = String.class),
			@ColumnResult(name = "customerName", type = String.class),
			@ColumnResult(name = "customerMobile", type = String.class),
			@ColumnResult(name = "eventName", type = String.class),
			@ColumnResult(name = "hallName", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "customerHomeAddress", type = String.class),
			@ColumnResult(name = "customerEmail", type = String.class),
			@ColumnResult(name = "managerName", type = String.class),
			@ColumnResult(name = "chefName", type = String.class),
			@ColumnResult(name = "eventDate", type = LocalDate.class),
			@ColumnResult(name = "mealTypename", type = String.class),
			@ColumnResult(name = "notes", type = String.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateSimpleMenuReport",
	resultSetMapping = "generateImageMenuReportAndSimpleMenuRajaCaterersReportResult",
	query = "SELECT "
		+ "t.functionId, "
		+ "t.functionName, "
		+ "t.person, "
		+ "t.date, "
		+ "t.rate, "
		+ "t.functionAddress, "
		+ "t.functionNote, "
		+ "t.menuItemCategoryId, "
		+ "GROUP_CONCAT( "
		+ "CASE "
		+ "WHEN t.notes IS NOT NULL AND t.notes != '' "
		+ "THEN CONCAT(t.menuItem, CHAR(10), '', IF(t.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', t.rupees, ')'), ''), ' (', t.notes, ') <br>' , '"
		+ "') " 
		+ "ELSE CONCAT(t.menuItem, IF(t.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', t.rupees, ') <br>'), CONCAT('', '<br>'))) "
		+ "END "
		+ "ORDER BY t.menuItemSequence ASC SEPARATOR '\\n') AS menuItem,"
		+ "CASE "
		+ "WHEN t.categoryNotes IS NOT NULL AND t.categoryNotes != '' "
		+ "THEN CONCAT(' ', t.menuItemCategory, CHAR(10), '', IF(t.categoryRupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', t.categoryRupees, ')'), ''), ' (', t.categoryNotes, ')') "
		+ "ELSE CONCAT(' ', t.menuItemCategory, IF(t.categoryRupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', t.categoryRupees, ')'), '')) "
		+ "END "
		+ "AS menuItemCategory, "
		+ "t.endDate "
		+ "FROM ( "
		+ "SELECT "
		+ "ompmi.menu_item_sequence AS menuItemSequence, "
		+ "ompmi.menu_item_category_sequence AS menuItemCategorySequence, "
		+ "ompmi.fk_menu_item_category_id AS ompMenuItemCategoryId, "
		+ "ofun.id AS functionId, "
		+ "ofun.sequence AS functionSequence, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofun.person AS person, "
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "ofun.rate AS rate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.function_address_prefer_lang IS NOT NULL AND ofun.function_address_prefer_lang != '' THEN ofun.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.function_address_supportive_lang IS NOT NULL AND ofun.function_address_supportive_lang != '' THEN ofun.function_address_supportive_lang "
		+ "ELSE ofun.function_address_default_lang "
		+ "END AS functionAddress, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.note_prefer_lang IS NOT NULL AND ofun.note_prefer_lang != '' THEN ofun.note_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.note_supportive_lang IS NOT NULL AND ofun.note_supportive_lang != '' THEN ofun.note_supportive_lang "
		+ "ELSE ofun.note_default_lang "
		+ "END AS functionNote, "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END AS menuItemCategory, "
		+ "mic.id AS menuItemCategoryId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN ompmi.note_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN ompmi.note_supportive_lang "
		+ "ELSE ompmi.note_default_lang "
		+ "END AS notes, "
		+ "ompmi.rupees AS rupees, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ompmic.note_prefer_lang IS NOT NULL AND ompmic.note_prefer_lang != '' THEN ompmic.note_prefer_lang "
		+ "WHEN :langType = 2 AND ompmic.note_supportive_lang IS NOT NULL AND ompmic.note_supportive_lang != '' THEN ompmic.note_supportive_lang "
		+ "ELSE ompmic.note_default_lang "
		+ "END AS categoryNotes, "
		+ "ompmic.rupees AS categoryRupees, "
		+ "mic.priority AS menuItemCategoryPriority, "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS menuItem "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp on omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "ORDER BY ofun.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence ASC, mi.priority, mi.id) AS t "
		+ "GROUP BY t.functionId, t.ompMenuItemCategoryId "
		+ "ORDER BY t.functionSequence, t.menuItemCategorySequence, t.menuItemCategoryPriority, t.menuItemCategoryId"
)

@NamedNativeQuery(
	name = "generateSimpleMenuRajaCaterersReport",
	resultSetMapping = "generateImageMenuReportAndSimpleMenuRajaCaterersReportResult",
	query = "SELECT "
		+ "NULL AS notes, "
		+ "t.functionId, "
		+ "t.functionName, "
		+ "t.person, "
		+ "t.startDate AS date, "
		+ "t.rate, "
		+ "NULL AS functionAddress, "
		+ "NULL AS functionNote, "
		+ "t.menuItemCategory, "
		+ "CONCAT( "
		+ "GROUP_CONCAT( "
		+ "CONCAT( "
		+ "'(', "
		+ "(SELECT COUNT(*) + 0 "
		+ "FROM order_function ofun_inner "
		+ "INNER JOIN order_menu_preparation omp_inner ON omp_inner.fk_order_function_id = ofun_inner.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi_inner ON ompmi_inner.fk_menu_preparation_id = omp_inner.id "
		+ "WHERE ofun_inner.id = t.functionId "
		+ "AND ompmi_inner.fk_menu_item_category_id = t.menuItemCategoryId "
		+ "AND ompmi_inner.menu_item_sequence <= t.menuItemSequence), "
		+ "') ', "
		+ "t.menuItem "
		+ ") "
		+ "ORDER BY t.menuItemSequence ASC "
		+ "SEPARATOR ' ' "
		+ ") "
		+ ") AS menuItem, "
		+ "t.endDate, "
		+ "t.menuItemCategoryId "
		+ "FROM ( "
		+ "SELECT "
		+ "ompmi.menu_item_sequence AS menuItemSequence, "
		+ "ompmi.menu_item_category_sequence AS menuItemCategorySequence, "
		+ "ompmi.fk_menu_item_category_id AS menuItemCategoryId, "
		+ "ofun.id AS functionId, "
		+ "ofun.sequence AS functionSequence, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofun.person AS person, "
		+ "ofun.date AS startDate, "
		+ "ofun.end_date AS endDate, "
		+ "ofun.rate AS rate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END AS menuItemCategory, "
		+ "mic.priority AS menuItemCategoryPriority, "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS menuItem "
		+ "FROM "
		+ "order_function ofun "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "ORDER BY ofun.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence ASC, mi.priority, mi.id ) AS t "
		+ "GROUP BY t.functionId, "
		+ "t.menuItemCategoryId "
		+ "ORDER BY t.functionSequence, t.menuItemCategorySequence, t.menuItemCategoryPriority, t.menuItemCategoryId; "
)

@NamedNativeQuery(
	name = "generateImageMenuCategoryReport",
	resultSetMapping = "generateImageMenuReportAndSimpleMenuRajaCaterersReportResult",
	query = "SELECT "
		+ "t.functionId, "
		+ "t.functionName, "
		+ "t.person, "
		+ "t.date, "
		+ "t.rate, "
		+ "t.functionAddress, "
		+ "t.functionNote, "
		+ "t.endDate, "
		+ "t.menuItemCategoryId, "
		+ "GROUP_CONCAT( "
		+ "CASE "
		+ "WHEN t.notes IS NOT NULL AND t.notes != '' "
		+ "THEN CONCAT(t.menuItem, CHAR(10), '', IF(t.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', t.rupees, ')'), ''), ' (', t.notes, ') <br>' , '"
		+ "') " 
		+ "ELSE CONCAT(t.menuItem, IF(t.rupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', t.rupees, ') <br>'), CONCAT('', '<br>'))) "
		+ "END "
		+ "ORDER BY t.menuItemSequence ASC SEPARATOR '\\n') AS menuItem,"
		+ "CASE "
		+ "WHEN t.categoryNotes IS NOT NULL AND t.categoryNotes != '' "
		+ "THEN CONCAT(' ', t.menuItemCategory, CHAR(10), '', IF(t.categoryRupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', t.categoryRupees, ')'), ''), ' (', t.categoryNotes, ')') "
		+ "ELSE CONCAT(' ', t.menuItemCategory, IF(t.categoryRupees IS NOT NULL, CONCAT(' (RUPEES_SYMBOL ', t.categoryRupees, ')'), '')) "
		+ "END "
		+ "AS menuItemCategory "
		+ "FROM ( "
		+ "SELECT "
		+ "ompmi.menu_item_sequence AS menuItemSequence, "
		+ "ompmi.menu_item_category_sequence AS menuItemCategorySequence, "
		+ "ompmi.fk_menu_item_category_id AS ompMenuItemCategoryId, "
		+ "ofun.id AS functionId, "
		+ "ofun.sequence AS functionSequence, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS functionName, "
		+ "ofun.person AS person, "
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "ofun.rate AS rate, "
		+ "CASE "
		+ "WHEN ofun.function_address_default_lang IS NULL THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND cod.venue_prefer_lang IS NOT NULL AND cod.venue_prefer_lang != '' THEN cod.venue_prefer_lang "
		+ "WHEN :langType = 2 AND cod.venue_supportive_lang IS NOT NULL AND cod.venue_supportive_lang != '' THEN cod.venue_supportive_lang "
		+ "ELSE cod.venue_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.function_address_prefer_lang IS NOT NULL AND ofun.function_address_prefer_lang != '' THEN ofun.function_address_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.function_address_supportive_lang IS NOT NULL AND ofun.function_address_supportive_lang != '' THEN ofun.function_address_supportive_lang "
		+ "ELSE ofun.function_address_default_lang "
		+ "END "
		+ "END AS functionAddress, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ofun.note_prefer_lang IS NOT NULL AND ofun.note_prefer_lang != '' THEN ofun.note_prefer_lang "
		+ "WHEN :langType = 2 AND ofun.note_supportive_lang IS NOT NULL AND ofun.note_supportive_lang != '' THEN ofun.note_supportive_lang "
		+ "ELSE ofun.note_default_lang "
		+ "END AS functionNote, "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END AS menuItemCategory, "
		+ "mic.id AS menuItemCategoryId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ompmi.note_prefer_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN ompmi.note_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN ompmi.note_supportive_lang "
		+ "ELSE ompmi.note_default_lang "
		+ "END AS notes, "
		+ "ompmi.rupees AS rupees, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ompmic.note_prefer_lang IS NOT NULL AND ompmic.note_prefer_lang != '' THEN ompmic.note_prefer_lang "
		+ "WHEN :langType = 2 AND ompmic.note_supportive_lang IS NOT NULL AND ompmic.note_supportive_lang != '' THEN ompmic.note_supportive_lang "
		+ "ELSE ompmic.note_default_lang "
		+ "END AS categoryNotes, "
		+ "ompmic.rupees AS categoryRupees, "
		+ "mic.priority AS menuItemCategoryPriority, "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' THEN mi.name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS menuItem "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp on omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "ORDER BY ofun.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence ASC, mi.priority, mi.id) AS t "
		+ "GROUP BY t.functionId, t.ompMenuItemCategoryId "
		+ "ORDER BY t.functionSequence, t.menuItemCategorySequence, t.menuItemCategoryPriority, t.menuItemCategoryId"
)

@SqlResultSetMapping(
	name = "generateImageMenuReportAndSimpleMenuRajaCaterersReportResult",
	classes = @ConstructorResult(
		targetClass = MenuPreparationSimpleMenuReportDto.class,
		columns = {
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "functionNote", type = String.class),
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "menuItem", type = String.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "menuItemCategoryId", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "generateTwoLanguageMenuReport",
	resultSetMapping = "generateTwoLanguageMenuReportResult",
	query = "SELECT "
		+ "ofun.id AS functionId, "
		+ "ofun.fk_customer_order_details_id AS orderId, "
		+ "cod.event_main_date AS bookingDate, "
		+ "CASE "
		+ "WHEN :defaultLang = 0 AND os.name_default_lang IS NOT NULL THEN os.name_default_lang "
		+ "WHEN :defaultLang = 1 AND os.name_prefer_lang IS NOT NULL THEN os.name_prefer_lang "
		+ "WHEN :defaultLang = 2 AND os.name_supportive_lang IS NOT NULL THEN os.name_supportive_lang "
		+ "WHEN :preferLang = 0 AND os.name_default_lang IS NOT NULL THEN os.name_default_lang "
		+ "WHEN :preferLang = 1 AND os.name_prefer_lang IS NOT NULL THEN os.name_prefer_lang "
		+ "WHEN :preferLang = 2 AND os.name_supportive_lang IS NOT NULL THEN os.name_supportive_lang "
		+ "ELSE NULL "
		+ "END AS orderStatus, "
		+ "CASE "
		+ "WHEN :defaultLang = 0 AND c.name_default_lang IS NOT NULL THEN c.name_default_lang "
		+ "WHEN :defaultLang = 1 AND c.name_prefer_lang IS NOT NULL THEN c.name_prefer_lang "
		+ "WHEN :defaultLang = 2 AND c.name_supportive_lang IS NOT NULL THEN c.name_supportive_lang "
		+ "WHEN :preferLang = 0 AND c.name_default_lang IS NOT NULL THEN c.name_default_lang "
		+ "WHEN :preferLang = 1 AND c.name_prefer_lang IS NOT NULL THEN c.name_prefer_lang "
		+ "WHEN :preferLang = 2 AND c.name_supportive_lang IS NOT NULL THEN c.name_supportive_lang "
		+ "ELSE NULL "
		+ "END AS customerName, "
		+ "c.mobile_number AS customerNumber, "
		+ "CASE "
		+ "WHEN :defaultLang = 0 AND cod.venue_default_lang IS NOT NULL THEN cod.venue_default_lang "
		+ "WHEN :defaultLang = 1 AND cod.venue_prefer_lang IS NOT NULL THEN cod.venue_prefer_lang "
		+ "WHEN :defaultLang = 2 AND cod.venue_supportive_lang IS NOT NULL THEN cod.venue_supportive_lang "
		+ "WHEN :preferLang = 0 AND cod.venue_default_lang IS NOT NULL THEN cod.venue_default_lang "
		+ "WHEN :preferLang = 1 AND cod.venue_prefer_lang IS NOT NULL THEN cod.venue_prefer_lang "
		+ "WHEN :preferLang = 2 AND cod.venue_supportive_lang IS NOT NULL THEN cod.venue_supportive_lang "
		+ "ELSE NULL "
		+ "END AS venue, "
		+ "CASE "
		+ "WHEN :defaultLang = 0 AND ft.name_default_lang IS NOT NULL THEN ft.name_default_lang "
		+ "WHEN :defaultLang = 1 AND ft.name_prefer_lang IS NOT NULL THEN ft.name_prefer_lang "
		+ "WHEN :defaultLang = 2 AND ft.name_supportive_lang IS NOT NULL THEN ft.name_supportive_lang "
		+ "WHEN :preferLang = 0 AND ft.name_default_lang IS NOT NULL THEN ft.name_default_lang "
		+ "WHEN :preferLang = 1 AND ft.name_prefer_lang IS NOT NULL THEN ft.name_prefer_lang "
		+ "WHEN :preferLang = 2 AND ft.name_supportive_lang IS NOT NULL THEN ft.name_supportive_lang "
		+ "ELSE NULL "
		+ "END AS functionName, "
		+ "ofun.date AS date, "
		+ "ofun.end_date AS endDate, "
		+ "ofun.person AS person, "
		+ "CASE "
		+ "WHEN :defaultLang = 0 AND ofun.function_address_default_lang IS NOT NULL THEN ofun.function_address_default_lang "
		+ "WHEN :defaultLang = 1 AND ofun.function_address_prefer_lang IS NOT NULL THEN ofun.function_address_prefer_lang "
		+ "WHEN :defaultLang = 2 AND ofun.function_address_supportive_lang IS NOT NULL THEN ofun.function_address_supportive_lang "
		+ "WHEN :preferLang = 0 AND ofun.function_address_default_lang IS NOT NULL THEN ofun.function_address_supportive_lang "
		+ "WHEN :preferLang = 1 AND ofun.function_address_prefer_lang IS NOT NULL THEN ofun.function_address_prefer_lang "
		+ "WHEN :preferLang = 2 AND ofun.function_address_supportive_lang IS NOT NULL THEN ofun.function_address_supportive_lang "
		+ "ELSE NULL "
		+ "END AS functionAddress, "
		+ "CASE "
		+ "WHEN :defaultLang = 0 AND ofun.note_default_lang IS NOT NULL THEN ofun.note_default_lang "
		+ "WHEN :defaultLang = 1 AND ofun.note_prefer_lang IS NOT NULL THEN ofun.note_prefer_lang "
		+ "WHEN :defaultLang = 2 AND ofun.note_supportive_lang IS NOT NULL THEN ofun.note_supportive_lang "
		+ "WHEN :preferLang = 0 AND ofun.note_default_lang IS NOT NULL THEN ofun.note_default_lang "
		+ "WHEN :preferLang = 1 AND ofun.note_prefer_lang IS NOT NULL THEN ofun.note_prefer_lang "
		+ "WHEN :preferLang = 2 AND ofun.note_supportive_lang IS NOT NULL THEN ofun.note_supportive_lang "
		+ "ELSE NULL "
		+ "END AS functionNote, "
		+ "ofun.rate AS rate, "
		+ " COUNT(*) OVER (PARTITION BY ofun.id) AS menuTotal, "
		+ "(ofun.person * ofun.rate) AS totalPrice, "
		+ "CASE "
		+ "WHEN :defaultLang = 0 AND mic.name_default_lang IS NOT NULL THEN mic.name_default_lang "
		+ "WHEN :defaultLang = 1 AND mic.name_prefer_lang IS NOT NULL THEN mic.name_prefer_lang "
		+ "WHEN :defaultLang = 2 AND mic.name_supportive_lang IS NOT NULL THEN mic.name_supportive_lang "
		+ "ELSE NULL "
		+ "END AS menuItemCategoryNameInEnglish, "
		+ "CASE "
		+ "WHEN :preferLang = 0 AND mic.name_default_lang IS NOT NULL THEN mic.name_default_lang "
		+ "WHEN :preferLang = 1 AND mic.name_prefer_lang IS NOT NULL THEN mic.name_prefer_lang "
		+ "WHEN :preferLang = 2 AND mic.name_supportive_lang IS NOT NULL THEN mic.name_supportive_lang "
		+ "ELSE NULL "
		+ "END AS menuItemCategoryNameInGujarati, "
		+ "CASE "
		+ "WHEN :defaultLang = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :defaultLang = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :defaultLang = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :defaultLang = 0 AND mi.name_default_lang IS NOT NULL THEN mi.name_default_lang "
		+ "WHEN :defaultLang = 1 AND mi.name_prefer_lang IS NOT NULL THEN mi.name_prefer_lang "
		+ "WHEN :defaultLang = 2 AND mi.name_supportive_lang IS NOT NULL THEN mi.name_supportive_lang "
		+ "ELSE NULL "
		+ "END AS menuItemInEnglish, "
		+ "CASE "
		+ "WHEN :preferLang = 0 AND ompmi.menu_item_name_default_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_default_lang) != '' THEN ompmi.menu_item_name_default_lang "
		+ "WHEN :preferLang = 1 AND ompmi.menu_item_name_prefer_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_prefer_lang) != '' THEN ompmi.menu_item_name_prefer_lang "
		+ "WHEN :preferLang = 2 AND ompmi.menu_item_name_supportive_lang IS NOT NULL AND TRIM(ompmi.menu_item_name_supportive_lang) != '' THEN ompmi.menu_item_name_supportive_lang "
		+ "WHEN :preferLang = 0 AND mi.name_default_lang IS NOT NULL THEN mi.name_default_lang "
		+ "WHEN :preferLang = 1 AND mi.name_prefer_lang IS NOT NULL THEN mi.name_prefer_lang "
		+ "WHEN :preferLang = 2 AND mi.name_supportive_lang IS NOT NULL THEN mi.name_supportive_lang "
		+ "ELSE NULL "
		+ "END AS menuItemInGujarati, "
		+ "CONCAT_WS("
		+ "' ', "
		+ "CASE "
		+ "WHEN :langType = 1 AND ompmi.rupees IS NOT NULL AND ompmi.rupees != '' THEN CONCAT(' (રૂ. ', ompmi.rupees, ')') "
		+ "WHEN :langType = 2 AND ompmi.rupees IS NOT NULL AND ompmi.rupees != '' THEN CONCAT(' (र�?. ', ompmi.rupees, ')') "
		+ "ELSE CONCAT(' (Rs. ', ompmi.rupees, ')') "
		+ "END, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ompmi.note_default_lang IS NOT NULL AND ompmi.note_prefer_lang != '' THEN CONCAT('(', ompmi.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmi.note_supportive_lang IS NOT NULL AND ompmi.note_supportive_lang != '' THEN CONCAT('(', ompmi.note_supportive_lang, ')') "
		+ "ELSE CONCAT('(', ompmi.note_default_lang, ')') "
		+ "END "
		+ ") AS menuItemInfo, "
		+ "CONCAT_WS("
		+ "' ', "
		+ "CASE "
		+ "WHEN :langType = 1 AND ompmic.rupees IS NOT NULL AND ompmic.rupees != '' THEN CONCAT(' (રૂ. ', ompmic.rupees, ')') "
		+ "WHEN :langType = 2 AND ompmic.rupees IS NOT NULL AND ompmic.rupees != '' THEN CONCAT(' (र�?. ', ompmic.rupees, ')') "
		+ "ELSE CONCAT(' (Rs. ', ompmic.rupees, ')') "
		+ "END, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ompmic.note_default_lang IS NOT NULL AND ompmic.note_prefer_lang != '' THEN CONCAT('(', ompmic.note_prefer_lang, ')') "
		+ "WHEN :langType = 2 AND ompmic.note_supportive_lang IS NOT NULL AND ompmic.note_supportive_lang != '' THEN CONCAT('(', ompmic.note_supportive_lang, ')') "
		+ "ELSE CONCAT('(', ompmic.note_default_lang, ')') "
		+ "END "
		+ ") AS menuItemCategoryInfo "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN order_status os ON os.id = cod.fk_order_status_id "
		+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = cod.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "INNER JOIN contact c ON c.id = cod.fk_contact_customer_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "WHERE ofun.fk_customer_order_details_id = :orderId AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "GROUP BY ofun.id, ft.id, mic.id, mi.id "
		+ "ORDER BY ofun.sequence, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id;"
)

@SqlResultSetMapping(
	name = "generateTwoLanguageMenuReportResult",
	classes = @ConstructorResult(
		targetClass = TwoLanguageMenuReportDto.class,
		columns = {
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "orderId", type = Long.class),
			@ColumnResult(name = "bookingDate", type = LocalDate.class),
			@ColumnResult(name = "orderStatus", type = String.class),
			@ColumnResult(name = "customerName", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "functionName", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "endDate", type = LocalDateTime.class),
			@ColumnResult(name = "person", type = Long.class),
			@ColumnResult(name = "functionAddress", type = String.class),
			@ColumnResult(name = "functionNote", type = String.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "menuTotal", type = Long.class),
			@ColumnResult(name = "totalPrice", type = Double.class),
			@ColumnResult(name = "menuItemCategoryNameInEnglish", type = String.class),
			@ColumnResult(name = "menuItemCategoryNameInGujarati", type = String.class),
			@ColumnResult(name = "menuItemInEnglish", type = String.class),
			@ColumnResult(name = "menuItemInGujarati", type = String.class),
			@ColumnResult(name = "menuItemInfo", type = String.class),
			@ColumnResult(name = "menuItemCategoryInfo", type = String.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateTableMenuReport",
	resultSetMapping = "generateTableMenuReportResult",
	query = "SELECT "
		+ "ofun.id AS functionId, "
		+ "NULL AS orderId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND mic.name_prefer_lang IS NOT NULL AND mic.name_prefer_lang != '' THEN mic.name_prefer_lang "
		+ "WHEN :langType = 2 AND mic.name_supportive_lang IS NOT NULL AND mic.name_supportive_lang != '' THEN mic.name_supportive_lang "
		+ "ELSE mic.name_default_lang "
		+ "END AS menuItemCategory, "
		+ "CASE "
		+ "WHEN :langType = 0 AND ompmi.counter_plate_menu_item_name_default_lang IS NOT NULL AND ompmi.counter_plate_menu_item_name_default_lang != '' THEN ompmi.counter_plate_menu_item_name_default_lang "
		+ "WHEN :langType = 1 AND mi.name_prefer_lang IS NOT NULL AND mi.name_prefer_lang != '' AND ompmi.counter_plate_menu_item_name_default_lang IS NULL THEN mi.name_prefer_lang "
		+ "WHEN :langType = 1 AND ompmi.counter_plate_menu_item_name_prefer_lang IS NOT NULL AND ompmi.counter_plate_menu_item_name_prefer_lang != '' THEN ompmi.counter_plate_menu_item_name_prefer_lang "
		+ "WHEN :langType = 2 AND mi.name_supportive_lang IS NOT NULL AND mi.name_supportive_lang != '' THEN mi.name_supportive_lang "
		+ "WHEN :langType = 2 AND ompmi.counter_plate_menu_item_name_supportive_lang IS NOT NULL AND ompmi.counter_plate_menu_item_name_supportive_lang != '' THEN ompmi.counter_plate_menu_item_name_supportive_lang "
		+ "ELSE mi.name_default_lang "
		+ "END AS menuItem,"
		+ "CASE "
		+ "WHEN :langType = 1 AND cu.first_name_prefer_lang IS NOT NULL AND cu.first_name_prefer_lang != '' THEN cu.first_name_prefer_lang "
		+ "WHEN :langType = 2 AND cu.first_name_supportive_lang IS NOT NULL AND cu.first_name_supportive_lang != '' THEN cu.first_name_supportive_lang "
		+ "ELSE cu.first_name_default_lang "
		+ "END AS companyUserName, "
		+ "cu.mobile_number AS mobileNumber "
		+ "FROM "
		+ "order_function ofun "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = ofun.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN menu_item_category mic ON mic.id = ompmi.fk_menu_item_category_id "
		+ "INNER JOIN menu_item mi ON mi.id = ompmi.fk_menu_item_id "
		+ "LEFT JOIN order_menu_preparation_menu_item_category ompmic ON ompmic.fk_menu_preparation_id = omp.id AND ompmic.fk_menu_item_category_id = mic.id "
		+ "LEFT JOIN company_user cu ON cu.id = (SELECT id FROM company_user ORDER BY created_at LIMIT 1)"
		+ "WHERE "
		+ "ofun.fk_customer_order_details_id = :orderId AND ompmi.isMenuItemSelected = TRUE AND IF( 0 IN (:functionIds), TRUE, ofun.id IN(:functionIds)) "
		+ "ORDER BY "
		+ "ofun.id, ompmi.menu_item_category_sequence, mic.priority, mic.id, ompmi.menu_item_sequence, mi.priority, mi.id;"
)

@SqlResultSetMapping(
	name = "generateTableMenuReportResult",
	classes = @ConstructorResult(
		targetClass = TableMenuReportDto.class,
		columns = {
			@ColumnResult(name = "functionId", type = Long.class),
			@ColumnResult(name = "orderId", type = Long.class),
			@ColumnResult(name = "menuItemCategory", type = String.class),
			@ColumnResult(name = "menuItem", type = String.class),
			@ColumnResult(name = "companyUserName", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
		}
	)
)

@NamedNativeQuery(
	name = "getFunctionPerOrderForMenuPreparation",
	resultSetMapping = "getFunctionPerOrderForMenuPreparationResult",
	query = "SELECT DISTINCT "
		+ "ft.id, "
		+ "of2.id AS orderFunctionId, "
		+ "of2.date AS orderDate, "
		+ "ft.name_default_lang AS nameDefaultLang, "
		+ "ft.name_prefer_lang AS namePreferLang, "
		+ "ft.name_supportive_lang AS nameSupportiveLang "
		+ "FROM `customer_order_details` o "
		+ "INNER JOIN order_function of2 ON of2.fk_customer_order_details_id = o.id "
		+ "INNER JOIN order_menu_preparation omp ON omp.fk_order_function_id = of2.id "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.fk_menu_preparation_id = omp.id "
		+ "INNER JOIN function_type ft ON ft.id = of2.fk_function_type_id "
		+ "WHERE o.id = :orderId"
)

@SqlResultSetMapping(
	name = "getFunctionPerOrderForMenuPreparationResult",
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

@SqlResultSetMapping(
	name = "findTableMenuNotesValueResult",
	classes = @ConstructorResult(
		targetClass = EventDistributionNotesDto.class,
		columns = {
			@ColumnResult(name = "noteName", type = String.class),
		}
	)
)

@NamedNativeQuery(
	name = "findTableMenuReportHeaderNotes",
	resultSetMapping = "findTableMenuNotesValueResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 0 AND note_default_lang IS NOT NULL AND note_default_lang != '' THEN note_default_lang "
		+ "WHEN :langType = 1 AND note_prefer_lang IS NOT NULL AND note_prefer_lang != '' THEN note_prefer_lang "
		+ "WHEN :langType = 2 AND note_supportive_lang IS NOT NULL AND note_supportive_lang != '' THEN note_supportive_lang "
		+ "END AS noteName "
		+ "FROM table_menu_report_header_notes tmrhn "
		+ "WHERE tmrhn.fk_customer_order_details_id = :orderId"
)

@NamedNativeQuery(
	name = "findTableMenuReportFooterNotes",
	resultSetMapping = "findTableMenuNotesValueResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 0 AND note_default_lang IS NOT NULL AND note_default_lang != '' THEN note_default_lang "
		+ "WHEN :langType = 1 AND note_prefer_lang IS NOT NULL AND note_prefer_lang != '' THEN note_prefer_lang "
		+ "WHEN :langType = 2 AND note_supportive_lang IS NOT NULL AND note_supportive_lang != '' THEN note_supportive_lang "
		+ "END AS noteName "
		+ "FROM table_menu_report_footer_notes tmrfn "
		+ "WHERE tmrfn.fk_customer_order_details_id = :orderId"
)

@Entity
public class MenuPreparationReportQuery extends AuditIdModelOnly {
}