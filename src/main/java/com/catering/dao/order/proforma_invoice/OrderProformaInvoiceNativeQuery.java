package com.catering.dao.order.proforma_invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import com.catering.dto.tenant.request.OrderInvoiceReportDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceFunctionResponseDto;
import com.catering.dto.tenant.request.OrderProformaInvoiceResponseDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Represents the native query entity for Order Proforma Invoice.
 * This class is mapped to the corresponding database table and extends {@code AuditIdModelOnly}
 * to include auditing fields and a primary identifier.
 */
@NamedNativeQuery(
	name = "findOrderProformaInvoiceByOrderId",
	resultSetMapping = "findOrderProformaInvoiceByOrderIdResult",
	query = "SELECT "
			+ "opi.id AS orderProformaInvoiceId, "
			+ "opi.fk_customer_order_details_id AS orderId, "
			+ "IFNULL(opi.fk_tax_master_id, 0) AS taxMasterId, "
			+ "IFNULL(opi.discount, 0) AS discount, "
			+ "IFNULL(opi.grand_total, 0) AS grandTotal, "
			+ "IFNULL(opi.advance_payment, 0) AS advancePayment, "
			+ "IFNULL(opi.round_off, 0) AS roundOff, "
			+ "ROUND((IFNULL(opi.grand_total, 0) - IFNULL(opi.advance_payment, 0)), 2) AS total, "
			+ "opi.remark, "
			+ "IFNULL(opi.bill_number, (SELECT IFNULL(MAX(CAST(bill_number AS UNSIGNED)), 0) + 1 FROM order_proforma_invoice)) AS billNumber, "
			+ "IFNULL(opi.bill_date, CURDATE()) AS billDate, "
			+ "opi.po_number AS poNumber, "
			+ "opi.company_register_address AS companyRegisteredAddress, "
			+ "opi.contact_person_name AS contactPersonName, "
			+ "opi.contact_person_mobile_number AS contactPersonMobileNo "
			+ "FROM customer_order_details od "
			+ "LEFT JOIN order_proforma_invoice opi ON opi.fk_customer_order_details_id = od.id "
			+ "WHERE od.id = :orderId "
			+ "GROUP BY orderProformaInvoiceId;"
)

@SqlResultSetMapping(
	name = "findOrderProformaInvoiceByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = OrderProformaInvoiceResponseDto.class,
		columns = {
			@ColumnResult(name = "orderProformaInvoiceId", type = Long.class),
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
	name = "findOrderProformaInvoiceFunctionByOrderId",
	resultSetMapping = "findOrderProformaInvoiceFunctionByOrderIdResult",
	query = "SELECT * FROM ((SELECT "
			+ "fm.name_default_lang AS functionNameDefaultLang, "
			+ "fm.name_prefer_lang AS functionNamePreferLang, "
			+ "fm.name_supportive_lang AS functionNameSupportiveLang, "
			+ "ofun.date AS date, "
			+ "ofun.sequence AS sequence, "
			+ "1 AS orderColumn, "
			+ "IF(opif.id IS NOT NULL, opif.person, ofun.person) AS person, "
			+ "IFNULL(opif.extra, 0) AS extra, "
			+ "IFNULL(IF(opif.id IS NOT NULL, opif.rate, ofun.rate), 0) AS rate, "
			+ "IFNULL(ofun.rate, 0) AS functionRate, "
			+ "ROUND((IF(opif.id IS NOT NULL, opif.person, ofun.person) * IFNULL(IF(opif.id IS NOT NULL, opif.rate, ofun.rate), 0)) + (IFNULL(opif.extra, 0) * IFNULL(IF(opif.id IS NOT NULL, opif.rate, ofun.rate), 0)), 2) AS amount, "
			+ "opi.id AS orderProformaInvoiceId, "
			+ "opif.id AS orderInvoiceFunctionId, "
			+ "ofun.id AS orderFunctionId "
			+ "FROM customer_order_details od "
			+ "LEFT JOIN order_function ofun ON ofun.fk_customer_order_details_id = od.id "
			+ "LEFT JOIN function_type fm ON fm.id = ofun.fk_function_type_id "
			+ "LEFT JOIN order_proforma_invoice opi ON opi.fk_customer_order_details_id = od.id "
			+ "LEFT JOIN order_proforma_invoice_function opif ON opif.fk_order_proforma_invoice_id = opi.id AND opif.fk_order_function_id = ofun.id "
			+ "WHERE od.id = :orderId "
			+ "ORDER BY sequence) "
			+ "UNION "
			+ "(SELECT "
			+ "opif.charges_name AS functionNameDefaultLang, "
			+ "opif.charges_name AS functionNamePreferLang, "
			+ "opif.charges_name AS functionNameSupportiveLang, "
			+ "opif.date AS date, "
			+ "0 AS sequence, "
			+ "2 AS orderColumn, "
			+ "opif.person AS person, "
			+ "IFNULL(opif.extra, 0) AS extra, "
			+ "IFNULL(IF(opif.id IS NOT NULL, opif.rate, ofun.rate), 0) AS rate, "
			+ "0 AS functionRate, "
			+ "ROUND((IFNULL(opif.extra, 0) * IFNULL(IF(opif.id IS NOT NULL, opif.rate, ofun.rate), 0)), 2) AS amount, "
			+ "opi.id AS orderProformaInvoiceId, "
			+ "opif.id AS orderInvoiceFunctionId, "
			+ "opif.fk_order_function_id AS orderFunctionId "
			+ "FROM customer_order_details od "
			+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = od.id "
			+ "INNER JOIN function_type fm ON fm.id = ofun.fk_function_type_id "
			+ "LEFT JOIN order_proforma_invoice opi ON opi.fk_customer_order_details_id = od.id "
			+ "LEFT JOIN order_proforma_invoice_function opif ON opif.fk_order_proforma_invoice_id = opi.id AND opif.fk_order_function_id IS NULL "
			+ "WHERE od.id = :orderId AND opif.id IS NOT NULL "
			+ "GROUP BY orderInvoiceFunctionId)) AS t ORDER BY orderColumn, sequence "
)

@SqlResultSetMapping(
	name = "findOrderProformaInvoiceFunctionByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = OrderProformaInvoiceFunctionResponseDto.class,
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
				@ColumnResult(name = "orderProformaInvoiceId", type = Long.class),
				@ColumnResult(name = "orderFunctionId", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "findOrderProformaInvoiceReportByOrderId",
	resultSetMapping = "findOrderProformaInvoiceReportByOrderIdResult",
	query = "SELECT "
			+ "od.id AS orderId, "
			+ "ofun.date AS date, "
			+ "ofun.sequence AS sequence, "
			+ "CASE "
			+ "WHEN :langType = 1 AND fm.name_prefer_lang IS NOT NULL AND fm.name_prefer_lang != '' THEN fm.name_prefer_lang "
			+ "WHEN :langType = 2 AND fm.name_supportive_lang IS NOT NULL AND fm.name_supportive_lang != '' THEN fm.name_supportive_lang "
			+ "ELSE fm.name_default_lang "
			+ "END AS chargesName, "
			+ "'996334' AS sac, "
			+ "IF(opif.id IS NOT NULL, opif.person, ofun.person) AS quantity, "
			+ "IFNULL(opif.extra, 0) AS extra, "
			+ "IFNULL(IF(opif.id IS NOT NULL, opif.rate, ofun.rate), 0) AS rate, "
			+ "ROUND((IF(opif.id IS NOT NULL, opif.person, ofun.person) * IFNULL(IF(opif.id IS NOT NULL, opif.rate, ofun.rate), 0)) + (IFNULL(opif.extra, 0) * IFNULL(IF(opif.id IS NOT NULL, opif.rate, ofun.rate), 0)), 2) AS amount, "
			+ "TRUE AS isFunction "
			+ "FROM customer_order_details od "
			+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = od.id "
			+ "INNER JOIN function_type fm ON fm.id = ofun.fk_function_type_id "
			+ "LEFT JOIN order_proforma_invoice opi ON opi.fk_customer_order_details_id = od.id "
			+ "LEFT JOIN order_proforma_invoice_function opif ON opif.fk_order_proforma_invoice_id = opi.id AND opif.fk_order_function_id = ofun.id "
			+ "WHERE od.id = :orderId "
			+ "UNION "
			+ "SELECT "
			+ "od.id AS orderId, "
			+ "opif.date AS date, "
			+ "'NULL' AS sequence, "
			+ "opif.charges_name AS chargesName, "
			+ "'' AS sac, "
			+ "opif.person AS quantity, "
			+ "IFNULL(opif.extra, 0) AS extra, "
			+ "IFNULL(IF(opif.id IS NOT NULL, opif.rate, ofun.rate), 0) AS rate, "
			+ "ROUND(IFNULL(opif.extra, 0) * IFNULL(opif.rate, 0), 2) AS amount, "
			+ "FALSE AS isFunction "
			+ "FROM customer_order_details od "
			+ "INNER JOIN order_function ofun ON ofun.fk_customer_order_details_id = od.id "
			+ "INNER JOIN function_type fm ON fm.id = ofun.fk_function_type_id "
			+ "LEFT JOIN order_proforma_invoice opi ON opi.fk_customer_order_details_id = od.id "
			+ "LEFT JOIN order_proforma_invoice_function opif ON opif.fk_order_proforma_invoice_id = opi.id AND opif.fk_order_function_id IS NULL "
			+ "WHERE od.id = :orderId AND opif.id IS NOT NULL "
			+ "GROUP BY opif.id "
			+ "ORDER BY date, sequence"
)

@SqlResultSetMapping(
	name = "findOrderProformaInvoiceReportByOrderIdResult",
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
public class OrderProformaInvoiceNativeQuery extends AuditIdModelOnly {
}