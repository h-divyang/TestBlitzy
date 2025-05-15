package com.catering.dao.order.invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.OrderInvoiceCommonDtoForReportDto;
import com.catering.dto.tenant.request.OrderInvoiceFunctionResponseDto;
import com.catering.dto.tenant.request.OrderInvoiceReportDto;
import com.catering.dto.tenant.request.OrderInvoiceResponseDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing a native query result for order invoices.
 * Extends {@link AuditIdModelOnly} to include audit-related fields.
 *
 * The class is mapped to the result of native queries retrieving order invoice details,
 * order invoice functions, common data for reports, and data for generating reports.
 *
 * The class defines named native queries along with SQL result set mappings for different scenarios.
 * Each named native query corresponds to a specific use case such as finding order invoices by order ID,
 * finding order invoice functions by order ID, finding common order invoice data for reports by order ID,
 * and finding order invoice report data by order ID.
 *
 * The named native queries involve joins between various tables such as order_detail, order_function,
 * function_type, order_invoice, order_invoice_function, and contact_party.
 *
 * @author Krushali Talaviya
 * @since 23rd January 2024
 */
@NamedNativeQuery(
	name = "findOrderInvoiceByOrderId",
	resultSetMapping = "findOrderInvoiceByOrderIdResult",
	query = "SELECT "
			+ "oi.id AS orderInvoiceId, "
			+ "oi.fk_customer_order_details_id AS orderId, "
			+ "IFNULL(oi.fk_tax_master_id, 0) AS taxMasterId, "
			+ "IFNULL(oi.discount, 0) AS discount, "
			+ "IFNULL(oi.grand_total, 0) AS grandTotal, "
			+ "IFNULL(oi.advance_payment, 0) AS advancePayment, "
			+ "IFNULL(oi.round_off, 0) AS roundOff, "
			+ "ROUND((IFNULL(oi.grand_total, 0) - IFNULL(oi.advance_payment, 0)), 2) AS total, "
			+ "oi.remark, "
			+ "IFNULL(oi.bill_number, (SELECT IFNULL(MAX(CAST(bill_number AS UNSIGNED)), 0) + 1 FROM order_invoice)) AS billNumber, "
			+ "IFNULL(oi.bill_date, CURDATE()) AS billDate, "
			+ "oi.po_number AS poNumber, "
			+ "oi.company_register_address AS companyRegisteredAddress, "
			+ "oi.contact_person_name AS contactPersonName, "
			+ "oi.contact_person_mobile_number AS contactPersonMobileNo "
			+ "FROM customer_order_details od "
			+ "LEFT JOIN order_invoice oi ON oi.fk_customer_order_details_id = od.id "
			+ "WHERE od.id = :orderId "
			+ "GROUP BY orderInvoiceId;"
)

@SqlResultSetMapping(
	name = "findOrderInvoiceByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = OrderInvoiceResponseDto.class,
		columns = {
			@ColumnResult(name = "orderInvoiceId", type = Long.class),
			@ColumnResult(name = "orderId", type = Long.class),
			@ColumnResult(name = "taxMasterId", type = Long.class),
			@ColumnResult(name = "discount", type = Double.class),
			@ColumnResult(name = "grandTotal", type = Double.class),
			@ColumnResult(name = "advancePayment", type = Double.class),
			@ColumnResult(name = "roundOff", type = Double.class),
			@ColumnResult(name = "total", type = Double.class),
			@ColumnResult(name = "remark", type = String.class),
			@ColumnResult(name = "billNumber", type = String.class),
			@ColumnResult(name = "billDate", type = LocalDate.class),
			@ColumnResult(name = "poNumber", type = String.class),
			@ColumnResult(name = "companyRegisteredAddress", type = String.class),
			@ColumnResult(name = "contactPersonName", type = String.class),
			@ColumnResult(name = "contactPersonMobileNo", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "findOrderInvoiceFunctionByOrderId",
	resultSetMapping = "findOrderInvoiceFunctionByOrderIdResult",
	query = "SELECT * FROM ((SELECT "
			+ "fm.name_default_lang AS functionNameDefaultLang, "
			+ "fm.name_prefer_lang AS functionNamePreferLang, "
			+ "fm.name_supportive_lang AS functionNameSupportiveLang, "
			+ "ofun.date AS date, "
			+ "ofun.sequence AS sequence, "
			+ "1 AS orderColumn, "
			+ "IF(oif.id IS NOT NULL, oif.person, ofun.person) AS person, "
			+ "IFNULL(oif.extra, 0) AS extra, "
			+ "IFNULL(IF(oif.id IS NOT NULL, oif.rate, ofun.rate), 0) AS rate, "
			+ "IFNULL(ofun.rate, 0) AS functionRate, "
			+ "ROUND((IF(oif.id IS NOT NULL, oif.person, ofun.person) * IFNULL(IF(oif.id IS NOT NULL, oif.rate, ofun.rate), 0)) + (IFNULL(oif.extra, 0) * IFNULL(IF(oif.id IS NOT NULL, oif.rate, ofun.rate), 0)), 2) AS amount, "
			+ "oi.id AS orderInvoiceId, "
			+ "oif.id AS orderInvoiceFunctionId, "
			+ "ofun.id AS orderFunctionId "
			+ "FROM customer_order_details od "
			+ "LEFT JOIN order_function ofun ON ofun.fk_customer_order_details_id = od.id "
			+ "LEFT JOIN function_type fm ON fm.id = ofun.fk_function_type_id "
			+ "LEFT JOIN order_invoice oi ON oi.fk_customer_order_details_id = od.id "
			+ "LEFT JOIN order_invoice_function oif ON oif.fk_order_invoice_id = oi.id AND oif.fk_order_function_id = ofun.id "
			+ "WHERE od.id = :orderId "
			+ "ORDER BY sequence) "
			+ "UNION "
			+ "(SELECT "
			+ "oif.charges_name AS functionNameDefaultLang, "
			+ "oif.charges_name AS functionNamePreferLang, "
			+ "oif.charges_name AS functionNameSupportiveLang, "
			+ "oif.date AS date, "
			+ "0 AS sequence, "
			+ "2 AS orderColumn, "
			+ "oif.person AS person, "
			+ "IFNULL(oif.extra, 0) AS extra, "
			+ "IFNULL(IF(oif.id IS NOT NULL, oif.rate, ofun.rate), 0) AS rate, "
			+ "0 AS functionRate, "
			+ "ROUND((IFNULL(oif.extra, 0) * IFNULL(IF(oif.id IS NOT NULL, oif.rate, ofun.rate), 0)), 2) AS amount, "
			+ "oi.id AS orderInvoiceId, "
			+ "oif.id AS orderInvoiceFunctionId, "
			+ "oif.fk_order_function_id AS orderFunctionId "
			+ "FROM customer_order_details od "
			+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = od.id "
			+ "INNER JOIN function_type fm ON fm.id = ofun.fk_function_type_id "
			+ "LEFT JOIN order_invoice oi ON oi.fk_customer_order_details_id = od.id "
			+ "LEFT JOIN order_invoice_function oif ON oif.fk_order_invoice_id = oi.id AND oif.fk_order_function_id IS NULL "
			+ "WHERE od.id = :orderId AND oif.id IS NOT NULL "
			+ "GROUP BY orderInvoiceFunctionId)) AS t ORDER BY orderColumn, sequence"
)

@SqlResultSetMapping(
	name = "findOrderInvoiceFunctionByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = OrderInvoiceFunctionResponseDto.class,
		columns = {
				@ColumnResult(name = "orderInvoiceFunctionId", type = Long.class),
				@ColumnResult(name = "functionNameDefaultLang", type = String.class),
				@ColumnResult(name = "functionNamePreferLang", type = String.class),
				@ColumnResult(name = "functionNameSupportiveLang", type = String.class),
				@ColumnResult(name = "date", type = LocalDateTime.class),
				@ColumnResult(name = "person", type = Double.class),
				@ColumnResult(name = "extra", type = Double.class),
				@ColumnResult(name = "rate", type = Double.class),
				@ColumnResult(name = "functionRate", type = Double.class),
				@ColumnResult(name = "amount", type = Double.class),
				@ColumnResult(name = "orderInvoiceId", type = Long.class),
				@ColumnResult(name = "orderFunctionId", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "findCommonOrderInvoiceDataForReportByOrderId",
	resultSetMapping = "findCommonOrderInvoiceDataForReportByOrderIdResult",
	query = "SELECT "
			+ "CASE "
			+ " WHEN :langType = 1 AND contact_party.name_prefer_lang IS NOT NULL AND contact_party.name_prefer_lang != '' THEN contact_party.name_prefer_lang "
			+ " WHEN :langType = 2 AND contact_party.name_supportive_lang IS NOT NULL AND contact_party.name_supportive_lang != '' THEN contact_party.name_supportive_lang "
			+ " ELSE contact_party.name_default_lang "
			+ "END AS customerName, "
			+ "CASE "
			+ " WHEN :langType = 1 AND od.venue_prefer_lang IS NOT NULL AND od.venue_prefer_lang != '' THEN od.venue_prefer_lang "
			+ " WHEN :langType = 2 AND od.venue_supportive_lang IS NOT NULL AND od.venue_supportive_lang != '' THEN od.venue_supportive_lang "
			+ " ELSE od.venue_default_lang "
			+ "END AS venue, "
			+ "contact_party.mobile_number AS customerNumber, "
			+ "contact_party.gst_number AS customerGSTNumber "
			+ "FROM customer_order_details od "
			+ "INNER JOIN contact contact_party ON contact_party.id = od.fk_contact_customer_id "
			+ "WHERE od.id = :orderId "
)

@SqlResultSetMapping(
	name = "findCommonOrderInvoiceDataForReportByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = OrderInvoiceCommonDtoForReportDto.class,
		columns = {
			@ColumnResult(name = "customerName", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "customerGSTNumber", type = String.class),
		}
	)
)

@NamedNativeQuery(
	name = "findOrderInvoiceReportByOrderId",
	resultSetMapping = "findOrderInvoiceReportByOrderIdResult",
	query = "SELECT "
			+ "od.id AS orderId,"
			+ "ofun.date AS date,"
			+ "ofun.sequence AS sequence, "
			+ "CASE "
			+ "WHEN :langType = 1 AND fm.name_prefer_lang IS NOT NULL AND fm.name_prefer_lang != '' THEN fm.name_prefer_lang "
			+ "WHEN :langType = 2 AND fm.name_supportive_lang IS NOT NULL AND fm.name_supportive_lang != '' THEN fm.name_supportive_lang "
			+ "ELSE fm.name_default_lang "
			+ "END AS chargesName, "
			+ "'996334' AS sac, "
			+ "IF(oif.id IS NOT NULL, oif.person, ofun.person) AS quantity, "
			+ "IFNULL(oif.extra, 0) AS extra, "
			+ "IFNULL(IF(oif.id IS NOT NULL, oif.rate, ofun.rate), 0) AS rate, "
			+ "ROUND((IF(oif.id IS NOT NULL, oif.person, ofun.person) * IFNULL(IF(oif.id IS NOT NULL, oif.rate, ofun.rate), 0)) + (IFNULL(oif.extra, 0) * IFNULL(IF(oif.id IS NOT NULL, oif.rate, ofun.rate), 0)), 2) AS amount, "
			+ "TRUE AS isFunction "
			+ "FROM customer_order_details od "
			+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = od.id "
			+ "INNER JOIN function_type fm ON fm.id = ofun.fk_function_type_id "
			+ "LEFT JOIN order_invoice oi ON oi.fk_customer_order_details_id = od.id "
			+ "LEFT JOIN order_invoice_function oif ON oif.fk_order_invoice_id = oi.id AND oif.fk_order_function_id = ofun.id "
			+ "WHERE od.id = :orderId "
			+ "UNION "
			+ "SELECT "
			+ "od.id AS orderId, "
			+ "oif.date AS date, "
			+ "'NULL' AS sequence, "
			+ "oif.charges_name AS chargesName, "
			+ "'' AS sac, "
			+ "oif.person AS quantity, "
			+ "IFNULL(oif.extra, 0) AS extra, "
			+ "IFNULL(IF(oif.id IS NOT NULL, oif.rate, ofun.rate), 0) AS rate, "
			+ "ROUND(IFNULL(oif.extra, 0) * IFNULL(oif.rate, 0), 2) AS amount, "
			+ "FALSE AS isFunction "
			+ "FROM customer_order_details od "
			+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = od.id "
			+ "INNER JOIN function_type fm ON fm.id = ofun.fk_function_type_id "
			+ "LEFT JOIN order_invoice oi ON oi.fk_customer_order_details_id = od.id "
			+ "LEFT JOIN order_invoice_function oif ON oif.fk_order_invoice_id = oi.id AND oif.fk_order_function_id IS NULL "
			+ "WHERE od.id = :orderId AND oif.id IS NOT NULL "
			+ "GROUP BY oif.id "
			+ "ORDER BY date, sequence "
)

@SqlResultSetMapping(
	name = "findOrderInvoiceReportByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = OrderInvoiceReportDto.class,
		columns = {
			@ColumnResult(name = "orderId", type = Long.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "chargesName", type = String.class),
			@ColumnResult(name = "quantity", type = Long.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "sac", type = String.class),
			@ColumnResult(name = "extra", type = Long.class),
			@ColumnResult(name = "isFunction", type = Boolean.class)
		}
	)
)
@Entity
public class OrderInvoiceNativeQuery extends AuditIdModelOnly {}