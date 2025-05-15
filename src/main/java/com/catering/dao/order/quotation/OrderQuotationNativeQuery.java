package com.catering.dao.order.quotation;

import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.OrderQuotationFunctionResponseDto;
import com.catering.dto.tenant.request.OrderQuotationReportDto;
import com.catering.dto.tenant.request.OrderQuotationResponseDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing an Order Quotation.
 * 
 * <p>This class extends {@link AuditIdModelOnly}, which provides audit fields such as 
 * created and updated timestamps, as well as an ID field.</p>
 * 
 * <p>The `OrderQuotationNativeQuery` entity is mapped to the database to store and retrieve
 * quotation details associated with orders.</p>
 */
@NamedNativeQuery(
	name = "findOrderQuotationByOrderId",
	resultSetMapping = "findOrderQuotationByOrderIdResult",
	query = "SELECT "
		+ "oq.id AS orderQuotationId, "
		+ "oq.fk_customer_order_details_id AS orderId, "
		+ "IFNULL(oq.fk_tax_master_id, 0) AS taxMasterId, "
		+ "IFNULL(oq.discount, 0) AS discount, "
		+ "IFNULL(oq.advance_payment, 0) AS advancePayment, "
		+ "IFNULL(oq.round_off, 0) AS roundOff, "
		+ "IFNULL(oq.grand_total, 0) AS grandTotal, "
		+ "ROUND((IFNULL(oq.grand_total, 0) - IFNULL(oq.advance_payment, 0)), 2) AS total, "
		+ "oq.remark, "
		+ "oq.is_rough_estimation AS isRoughEstimation "
		+ "FROM customer_order_details cod "
		+ "LEFT JOIN order_quotation oq ON oq.fk_customer_order_details_id = cod.id "
		+ "WHERE cod.id = :orderId "
		+ "GROUP BY orderQuotationId;"
)

@SqlResultSetMapping(
	name = "findOrderQuotationByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = OrderQuotationResponseDto.class,
		columns = {
			@ColumnResult(name = "orderQuotationId", type = Long.class),
			@ColumnResult(name = "orderId", type = Long.class),
			@ColumnResult(name = "taxMasterId", type = Long.class),
			@ColumnResult(name = "discount", type = Double.class),
			@ColumnResult(name = "advancePayment", type = Double.class),
			@ColumnResult(name = "roundOff", type = Double.class),
			@ColumnResult(name = "grandTotal", type = String.class),
			@ColumnResult(name = "total", type = Double.class),
			@ColumnResult(name = "remark", type = String.class),
			@ColumnResult(name = "isRoughEstimation", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "findOrderQuotationFunctionByOrderId",
	resultSetMapping = "findOrderQuotationFunctionByOrderIdResult",
	query = "SELECT * FROM ((SELECT "
		+ "ft.name_default_lang AS functionNameDefaultLang, "
		+ "ft.name_prefer_lang AS functionNamePreferLang, "
		+ "ft.name_supportive_lang AS functionNameSupportiveLang, "
		+ "ofun.date AS date, "
		+ "ofun.sequence AS sequence, "
		+ "1 AS orderColumn, "
		+ "IF(oqf.id IS NOT NULL, oqf.person, ofun.person) AS person, "
		+ "IFNULL(oqf.extra, 0) AS extra, "
		+ "IFNULL(IF(oqf.id IS NOT NULL, oqf.rate, ofun.rate), 0) AS rate, "
		+ "IF(oq.is_rough_estimation, oqf.amount, ROUND((IF(oqf.id IS NOT NULL, oqf.person, ofun.person) * IFNULL(IF(oqf.id IS NOT NULL, oqf.rate, ofun.rate), 0)) + (IFNULL(oqf.extra, 0) * IFNULL(IF(oqf.id IS NOT NULL, oqf.rate, ofun.rate), 0)),2)) AS amount, "
		+ "oq.id AS orderQuotationId, "
		+ "oqf.id AS orderQuotationFunctionId, "
		+ "ofun.id AS orderFunctionId "
		+ "FROM customer_order_details cod "
		+ "LEFT JOIN order_function ofun ON ofun.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_quotation oq ON oq.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN order_quotation_function oqf ON oqf.fk_order_quotation_id = oq.id AND oqf.fk_order_function_id = ofun.id "
		+ "WHERE cod.id = :orderId "
		+ "ORDER BY sequence) "
		+ "UNION "
		+ "(SELECT "
		+ "oqf.charges_name AS functionNameDefaultLang, "
		+ "oqf.charges_name AS functionNamePreferLang, "
		+ "oqf.charges_name AS functionNameSupportiveLang, "
		+ "oqf.date AS date, "
		+ "0 AS sequence, "
		+ "2 AS orderColumn, "
		+ "oqf.person AS person, "
		+ "IFNULL(oqf.extra, 0) AS extra, "
		+ "IFNULL(IF(oqf.id IS NOT NULL, oqf.rate, ofun.rate), 0) AS rate, "
		+ "IF(oq.is_rough_estimation, oqf.amount, ROUND(IFNULL(oqf.extra, 0) * IFNULL(IF(oqf.id IS NOT NULL, oqf.rate, ofun.rate), 0), 2)) AS amount, "
		+ "oq.id AS orderQuotationId, "
		+ "oqf.id AS orderQuotationFunctionId, "
		+ "oqf.fk_order_function_id AS orderFunctionId "
		+ "FROM customer_order_details cod "
		+ "LEFT JOIN order_function ofun ON ofun.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_quotation oq ON oq.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN order_quotation_function oqf ON oqf.fk_order_quotation_id = oq.id AND oqf.fk_order_function_id IS NULL "
		+ "WHERE cod.id = :orderId AND oqf.id IS NOT NULL "
		+ "GROUP BY orderQuotationFunctionId)) AS t ORDER BY orderColumn, sequence "
)

@SqlResultSetMapping(
	name = "findOrderQuotationFunctionByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = OrderQuotationFunctionResponseDto.class,
		columns = {
			@ColumnResult(name = "orderQuotationFunctionId", type = Long.class),
			@ColumnResult(name = "functionNameDefaultLang", type = String.class),
			@ColumnResult(name = "functionNamePreferLang", type = String.class),
			@ColumnResult(name = "functionNameSupportiveLang", type = String.class),
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "person", type = Double.class),
			@ColumnResult(name = "extra", type = Double.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "amount", type = String.class),
			@ColumnResult(name = "orderQuotationId", type = Long.class),
			@ColumnResult(name = "orderFunctionId", type = Long.class),
		}
	)
)

@NamedNativeQuery(
	name = "generateOrderQuotationReport",
	resultSetMapping = "generateOrderQuotationReportResult",
	query = "SELECT "
		+ "ofun.sequence AS sequence, "
		+ "ofun.date AS date, "
		+ "CASE "
		+ "WHEN :langType = 1 AND ft.name_prefer_lang IS NOT NULL AND ft.name_prefer_lang != '' THEN ft.name_prefer_lang "
		+ "WHEN :langType = 2 AND ft.name_supportive_lang IS NOT NULL AND ft.name_supportive_lang != '' THEN ft.name_supportive_lang "
		+ "ELSE ft.name_default_lang "
		+ "END AS name, "
		+ "'996334' AS sac, "
		+ "IF(oqf.id IS NOT NULL, oqf.person, ofun.person) AS quantity, "
		+ "IFNULL(oqf.extra, 0) AS extra, "
		+ "IFNULL(IF(oqf.id IS NOT NULL, oqf.rate, ofun.rate), 0) AS rate, "
		+ "IF(oq.is_rough_estimation, oqf.amount, ROUND((IF(oqf.id IS NOT NULL, oqf.person, ofun.person) * IFNULL(IF(oqf.id IS NOT NULL, oqf.rate, ofun.rate), 0)) + (IFNULL(oqf.extra, 0) * IFNULL(IF(oqf.id IS NOT NULL, oqf.rate, ofun.rate), 0)),2)) AS amount, "
		+ "TRUE AS isFunction "
		+ "FROM customer_order_details cod "
		+ "LEFT JOIN order_function ofun ON ofun.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_quotation oq ON oq.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN order_quotation_function oqf ON oqf.fk_order_quotation_id = oq.id AND oqf.fk_order_function_id = ofun.id "
		+ "WHERE cod.id = :orderId "
		+ "UNION "
		+ "SELECT "
		+ "'NULL' AS sequence, "
		+ "oqf.date AS date, "
		+ "oqf.charges_name AS name, "
		+ "'' AS sac, "
		+ "oqf.person AS quantity, "
		+ "IFNULL(oqf.extra, 0) AS extra, "
		+ "IFNULL(IF(oqf.id IS NOT NULL, oqf.rate, ofun.rate), 0) AS rate, "
		+ "IF(oq.is_rough_estimation, oqf.amount, ROUND(IFNULL(oqf.extra, 0) * IFNULL(IF(oqf.id IS NOT NULL, oqf.rate, ofun.rate), 0), 2)) AS amount,"
		+ "FALSE AS isFunction "
		+ "FROM customer_order_details cod "
		+ "LEFT JOIN order_function ofun ON ofun.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN function_type ft ON ft.id = ofun.fk_function_type_id "
		+ "LEFT JOIN order_quotation oq ON oq.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN order_quotation_function oqf ON oqf.fk_order_quotation_id = oq.id AND oqf.fk_order_function_id IS NULL "
		+ "WHERE cod.id = :orderId AND oqf.id IS NOT NULL "
		+ "GROUP BY oqf.id "
		+ "ORDER BY date, sequence "
)

@SqlResultSetMapping(
	name = "generateOrderQuotationReportResult",
	classes = @ConstructorResult(
		targetClass = OrderQuotationReportDto.class,
		columns = {
			@ColumnResult(name = "date", type = LocalDateTime.class),
			@ColumnResult(name = "name", type = String.class),
			@ColumnResult(name = "sac", type = String.class),
			@ColumnResult(name = "quantity", type = Long.class),
			@ColumnResult(name = "extra", type = Long.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "amount", type = String.class),
			@ColumnResult(name = "isFunction", type = Boolean.class)
		}
	)
)
@Entity
public class OrderQuotationNativeQuery extends AuditIdModelOnly {
}