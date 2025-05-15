package com.catering.dao.accounting_reports.individual_record_report;

import java.time.LocalDate;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.IndividualRecordBankPaymentReceiveReportDto;
import com.catering.dto.tenant.request.IndividualRecordCashPaymentReceiveReportDto;
import com.catering.dto.tenant.request.IndividualRecordCommonDto;
import com.catering.dto.tenant.request.IndividualRecordInputTransferToHallReportDto;
import com.catering.dto.tenant.request.IndividualRecordJournalVoucherReportDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Represents the entity for storing individual record report data.
 * This class extends {@link AuditIdModelOnly} to inherit auditing functionality 
 * such as tracking created and modified timestamps and user information.
 * <p>
 * The class is mapped to a database table using the JPA Entity annotation and 
 * is intended for use with native SQL queries related to individual record reports.
 * </p>
 * <p>
 * Currently, this class does not contain any specific fields or methods. It is 
 * likely used as a placeholder or base class for queries and reporting.
 * </p>
 */
@NamedNativeQuery(
	name = "getCashPaymentReceiveReport",
	resultSetMapping = "getCashPaymentReceiveReportResult",
	query = "SELECT "
		+ "c.id AS customerId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "c.mobile_number AS customerNumber, "
		+ "cpr.id AS invoiceNumber, "
		+ "cpr.transaction_date AS transactionDate, "
		+ "cprd.remark AS remark, "
		+ "cprd.amount AS amount, "
		+ ":transactionType AS isReceipt, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.venue_prefer_lang IS NOT NULL AND c.venue_prefer_lang != '' THEN c.venue_prefer_lang "
		+ "WHEN :langType = 2 AND c.venue_supportive_lang IS NOT NULL AND c.venue_supportive_lang != '' THEN c.venue_supportive_lang "
		+ "ELSE c.venue_default_lang "
		+ "END AS venue "
		+ "FROM cash_payment_receipt cpr "
		+ "INNER JOIN cash_payment_receipt_details cprd ON cprd.fk_cash_payment_receipt_id = cpr.id "
		+ "INNER JOIN contact c ON c.id = cprd.fk_contact_id "
		+ "WHERE cpr.id = :id AND cpr.transaction_type = :transactionType "
		+ "ORDER BY c.id;"
)

@SqlResultSetMapping(
	name = "getCashPaymentReceiveReportResult",
	classes = @ConstructorResult(
		targetClass = IndividualRecordCashPaymentReceiveReportDto.class,
		columns = {
			@ColumnResult(name = "customerId", type = Long.class),
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "invoiceNumber", type = Long.class),
			@ColumnResult(name = "transactionDate", type = LocalDate.class),
			@ColumnResult(name = "remark", type = String.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "isReceipt", type = Boolean.class),
			@ColumnResult(name = "venue", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getBankPaymentReceiveReport",
	resultSetMapping = "getBankPaymentReceiveReportResult",
	query = "SELECT "
		+ "c.id AS customerId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "c.mobile_number AS customerNumber, "
		+ "bpr.id AS invoiceNumber, "
		+ "bpr.transaction_date AS transactionDate, "
		+ "bprd.remark AS remark, "
		+ "bprd.amount AS amount, "
		+ ":transactionType AS isReceipt, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.venue_prefer_lang IS NOT NULL AND c.venue_prefer_lang != '' THEN c.venue_prefer_lang "
		+ "WHEN :langType = 2 AND c.venue_supportive_lang IS NOT NULL AND c.venue_supportive_lang != '' THEN c.venue_supportive_lang "
		+ "ELSE c.venue_default_lang "
		+ "END AS venue, "
		+ "bprd.payment_mode AS paymentMode, "
		+ "bprd.cheque_transaction_number AS chequeTransactionNumber, "
		+ "bprd.cheque_transaction_date AS chequeTransactionDate "
		+ "FROM bank_payment_receipt bpr "
		+ "INNER JOIN bank_payment_receipt_details bprd ON bprd.fk_bank_payment_receipt_id = bpr.id "
		+ "INNER JOIN contact c ON c.id = bprd.fk_contact_id "
		+ "WHERE bpr.id = :id AND bpr.transaction_type = :transactionType "
		+ "ORDER BY c.id;"
)

@SqlResultSetMapping(
	name = "getBankPaymentReceiveReportResult",
	classes = @ConstructorResult(
		targetClass = IndividualRecordBankPaymentReceiveReportDto.class,
		columns = {
			@ColumnResult(name = "customerId", type = Long.class),
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "invoiceNumber", type = Long.class),
			@ColumnResult(name = "transactionDate", type = LocalDate.class),
			@ColumnResult(name = "remark", type = String.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "isReceipt", type = Boolean.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "paymentMode", type = Boolean.class),
			@ColumnResult(name = "chequeTransactionNumber", type = String.class),
			@ColumnResult(name = "chequeTransactionDate", type = LocalDate.class)
		}
	)
)

@NamedNativeQuery(
	name = "getJournalVoucherReport",
	resultSetMapping = "getJournalVoucherReportResult",
	query = "SELECT "
		+ "jv.id AS id, "
		+ "jv.voucher_date AS transactionDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "jvd.amount, "
		+ "jvd.transaction_type AS transactionType, "
		+ "jvd.remark "
		+ "FROM journal_voucher jv "
		+ "INNER JOIN journal_voucher_details jvd ON jvd.fk_journal_voucher_id = jv.id "
		+ "INNER JOIN contact c ON c.id = jvd.fk_contact_id "
		+ "WHERE jv.id = :journalVoucherId "
		+ "ORDER BY jvd.id;"
)

@SqlResultSetMapping(
	name = "getJournalVoucherReportResult",
	classes = @ConstructorResult(
		targetClass = IndividualRecordJournalVoucherReportDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "transactionDate", type = LocalDate.class),
			@ColumnResult(name = "remark", type = String.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "transactionType", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "getPurchaseOrderGstSameStateReport",
	resultSetMapping = "getPurchaseOrderGstSameStateReportResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "c.mobile_number AS customerNumber, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.venue_prefer_lang IS NOT NULL AND c.venue_prefer_lang != '' THEN c.venue_prefer_lang "
		+ "WHEN :langType = 2 AND c.venue_supportive_lang IS NOT NULL AND c.venue_supportive_lang != '' THEN c.venue_supportive_lang "
		+ "ELSE c.venue_default_lang "
		+ "END AS venue, "
		+ "po.id AS invoiceNumber, "
		+ "po.purchase_date AS purchaseDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterial, "
		+ "porm.weight, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS measurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND porm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND porm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "porm.delivery_date AS deliveryDate, "
		+ "porm.price, "
		+ "tm.cgst, "
		+ "(tm.cgst * price * porm.weight) / 100 AS cgstPrice, "
		+ "tm.sgst, "
		+ "(tm.sgst * price * porm.weight) / 100 AS sgstPrice, "
		+ "porm.total_amount AS amount, "
		+ "cp.gst_number AS gstNumber, "
		+ "SUBSTRING(cp.gst_number, 3, 10) AS panNumber "
		+ "FROM purchase_order po "
		+ "INNER JOIN purchase_order_raw_material porm ON porm.fk_purchase_order_id = po.id "
		+ "INNER JOIN raw_material rm ON rm.id = porm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = porm.fk_measurement_id "
		+ "INNER JOIN contact c ON c.id = po.fk_contact_id "
		+ "LEFT JOIN tax_master tm ON tm.id = porm.fk_tax_master_id "
		+ "JOIN company_preferences cp "
		+ "WHERE po.id = :id"
)

@SqlResultSetMapping(
	name = "getPurchaseOrderGstSameStateReportResult",
	classes = @ConstructorResult(
		targetClass = IndividualRecordCommonDto.class,
		columns = {
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "invoiceNumber", type = Long.class),
			@ColumnResult(name = "purchaseDate", type = LocalDate.class),
			@ColumnResult(name = "rawMaterial", type = String.class),
			@ColumnResult(name = "weight", type = Double.class),
			@ColumnResult(name = "measurement", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
			@ColumnResult(name = "deliveryDate", type = LocalDate.class),
			@ColumnResult(name = "price", type = Double.class),
			@ColumnResult(name = "cgst", type = Double.class),
			@ColumnResult(name = "cgstPrice", type = Double.class),
			@ColumnResult(name = "sgst", type = Double.class),
			@ColumnResult(name = "sgstPrice", type = Double.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "gstNumber", type = String.class),
			@ColumnResult(name = "panNumber", type = String.class),
		}
	)
)

@NamedNativeQuery(
	name = "getPurchaseOrderGstDifferentStateReport",
	resultSetMapping = "getPurchaseOrderGstDifferentStateReportResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "c.mobile_number AS customerNumber, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.venue_prefer_lang IS NOT NULL AND c.venue_prefer_lang != '' THEN c.venue_prefer_lang "
		+ "WHEN :langType = 2 AND c.venue_supportive_lang IS NOT NULL AND c.venue_supportive_lang != '' THEN c.venue_supportive_lang "
		+ "ELSE c.venue_default_lang "
		+ "END AS venue, "
		+ "po.id AS invoiceNumber, "
		+ "po.purchase_date AS purchaseDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterial, "
		+ "porm.weight, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS measurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND porm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND porm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "porm.delivery_date AS deliveryDate, "
		+ "porm.price, "
		+ "tm.igst, "
		+ "(tm.igst * price * porm.weight) / 100 AS igstPrice, "
		+ "porm.total_amount AS amount, "
		+ "cp.gst_number AS gstNumber, "
		+ "SUBSTRING(cp.gst_number, 3, 10) AS panNumber "
		+ "FROM purchase_order po "
		+ "INNER JOIN purchase_order_raw_material porm ON porm.fk_purchase_order_id = po.id "
		+ "INNER JOIN raw_material rm ON rm.id = porm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = porm.fk_measurement_id "
		+ "INNER JOIN contact c ON c.id = po.fk_contact_id "
		+ "LEFT JOIN tax_master tm ON tm.id = porm.fk_tax_master_id "
		+ "JOIN company_preferences cp "
		+ "WHERE po.id = :id"
)

@SqlResultSetMapping(
	name = "getPurchaseOrderGstDifferentStateReportResult",
	classes = @ConstructorResult(
		targetClass = IndividualRecordCommonDto.class,
		columns = {
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "invoiceNumber", type = Long.class),
			@ColumnResult(name = "purchaseDate", type = LocalDate.class),
			@ColumnResult(name = "rawMaterial", type = String.class),
			@ColumnResult(name = "weight", type = Double.class),
			@ColumnResult(name = "measurement", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
			@ColumnResult(name = "deliveryDate", type = LocalDate.class),
			@ColumnResult(name = "price", type = Double.class),
			@ColumnResult(name = "igst", type = Double.class),
			@ColumnResult(name = "igstPrice", type = Double.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "gstNumber", type = String.class),
			@ColumnResult(name = "panNumber", type = String.class),
		}
	)
)

@NamedNativeQuery(
	name = "getPurchaseBillGstSameStateReport",
	resultSetMapping = "getPurchaseBillGstSameStateReportResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "c.mobile_number AS customerNumber, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.venue_prefer_lang IS NOT NULL AND c.venue_prefer_lang != '' THEN c.venue_prefer_lang "
		+ "WHEN :langType = 2 AND c.venue_supportive_lang IS NOT NULL AND c.venue_supportive_lang != '' THEN c.venue_supportive_lang "
		+ "ELSE c.venue_default_lang "
		+ "END AS venue, "
		+ "pb.id AS invoiceNumber, "
		+ "pb.bill_date AS purchaseDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_default_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterial, "
		+ "pbrm.weight, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS measurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND pbrm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND pbrm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "pbrm.price, "
		+ "tm.cgst, "
		+ "(tm.cgst * price * pbrm.weight) / 100 AS cgstPrice, "
		+ "tm.sgst, "
		+ "(tm.sgst * price * pbrm.weight) / 100 AS sgstPrice, "
		+ "pbrm.total_amount AS amount, "
		+ "IFNULL(pb.discount,0) AS discount, "
		+ "IFNULL(pb.extra_expense, 0) AS extraExpense, "
		+ "IFNULL(pb.round_off,0) AS roundOff, "
		+ "pb.grand_total AS grandTotal, "
		+ "cp.gst_number AS gstNumber, "
		+ "SUBSTRING(cp.gst_number, 3, 10) AS panNumber, "
		+ "true AS isPurchaseBill, "
		+ "pb.remark AS remark "
		+ "FROM purchase_bill pb "
		+ "INNER JOIN purchase_bill_raw_material pbrm ON pbrm.fk_purchase_bill_id = pb.id "
		+ "INNER JOIN raw_material rm ON rm.id = pbrm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = pbrm.fk_measurement_id "
		+ "INNER JOIN contact c ON c.id = pb.fk_contact_id "
		+ "LEFT JOIN tax_master tm ON tm.id = pbrm.fk_tax_master_id "
		+ "JOIN company_preferences cp "
		+ "WHERE pb.id = :id"
)

@SqlResultSetMapping(
	name = "getPurchaseBillGstSameStateReportResult",
	classes = @ConstructorResult(
		targetClass = IndividualRecordCommonDto.class,
		columns = {
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "invoiceNumber", type = Long.class),
			@ColumnResult(name = "purchaseDate", type = LocalDate.class),
			@ColumnResult(name = "rawMaterial", type = String.class),
			@ColumnResult(name = "weight", type = Double.class),
			@ColumnResult(name = "measurement", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
			@ColumnResult(name = "price", type = Double.class),
			@ColumnResult(name = "cgst", type = Double.class),
			@ColumnResult(name = "cgstPrice", type = Double.class),
			@ColumnResult(name = "sgst", type = Double.class),
			@ColumnResult(name = "sgstPrice", type = Double.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "discount", type = Double.class),
			@ColumnResult(name = "extraExpense", type = Double.class),
			@ColumnResult(name = "roundOff", type = Double.class),
			@ColumnResult(name = "grandTotal", type = Double.class),
			@ColumnResult(name = "gstNumber", type = String.class),
			@ColumnResult(name = "panNumber", type = String.class),
			@ColumnResult(name = "isPurchaseBill", type = Boolean.class),
			@ColumnResult(name = "remark", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getDebitNoteGstSameStateReport",
	resultSetMapping = "getPurchaseBillDebitNoteGstSameStateReportResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "c.mobile_number AS customerNumber, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.venue_prefer_lang IS NOT NULL AND c.venue_prefer_lang != '' THEN c.venue_prefer_lang "
		+ "WHEN :langType = 2 AND c.venue_supportive_lang IS NOT NULL AND c.venue_supportive_lang != '' THEN c.venue_supportive_lang "
		+ "ELSE c.venue_default_lang "
		+ "END AS venue, "
		+ "dn.id AS invoiceNumber, "
		+ "dn.bill_date AS purchaseDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_default_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterial, "
		+ "dnrm.weight, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS measurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND dnrm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND dnrm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "dnrm.price, "
		+ "tm.cgst, "
		+ "(tm.cgst * price * dnrm.weight) / 100 AS cgstPrice, "
		+ "tm.sgst, "
		+ "(tm.sgst * price * dnrm.weight) / 100 AS sgstPrice, "
		+ "dnrm.total_amount AS amount, "
		+ "cp.gst_number AS gstNumber, "
		+ "SUBSTRING(cp.gst_number, 3, 10) AS panNumber, "
		+ "false AS isPurchaseBill "
		+ "FROM debit_note dn "
		+ "INNER JOIN debit_note_raw_material dnrm ON dnrm.fk_debit_note_id = dn.id "
		+ "INNER JOIN raw_material rm ON rm.id = dnrm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = dnrm.fk_measurement_id "
		+ "INNER JOIN contact c ON c.id = dn.fk_contact_id "
		+ "LEFT JOIN tax_master tm ON tm.id = dnrm.fk_tax_master_id "
		+ "JOIN company_preferences cp "
		+ "WHERE dn.id = :id"
)

@SqlResultSetMapping(
	name = "getPurchaseBillDebitNoteGstSameStateReportResult",
	classes = @ConstructorResult(
		targetClass = IndividualRecordCommonDto.class,
		columns = {
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "invoiceNumber", type = Long.class),
			@ColumnResult(name = "purchaseDate", type = LocalDate.class),
			@ColumnResult(name = "rawMaterial", type = String.class),
			@ColumnResult(name = "weight", type = Double.class),
			@ColumnResult(name = "measurement", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
			@ColumnResult(name = "price", type = Double.class),
			@ColumnResult(name = "cgst", type = Double.class),
			@ColumnResult(name = "cgstPrice", type = Double.class),
			@ColumnResult(name = "sgst", type = Double.class),
			@ColumnResult(name = "sgstPrice", type = Double.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "gstNumber", type = String.class),
			@ColumnResult(name = "panNumber", type = String.class),
			@ColumnResult(name = "isPurchaseBill", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "getPurchaseBillGstDifferentStateReport",
	resultSetMapping = "getPurchaseBillGstDifferentStateReportResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "c.mobile_number AS customerNumber, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.venue_prefer_lang IS NOT NULL AND c.venue_prefer_lang != '' THEN c.venue_prefer_lang "
		+ "WHEN :langType = 2 AND c.venue_supportive_lang IS NOT NULL AND c.venue_supportive_lang != '' THEN c.venue_supportive_lang "
		+ "ELSE c.venue_default_lang "
		+ "END AS venue, "
		+ "pb.id AS invoiceNumber, "
		+ "pb.bill_date AS purchaseDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterial, "
		+ "pbrm.weight, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS measurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND pbrm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND pbrm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "pbrm.price, "
		+ "tm.igst, "
		+ "(tm.igst * price * pbrm.weight) / 100 AS igstPrice, "
		+ "pbrm.total_amount AS amount, "
		+ "IFNULL(pb.discount,0) AS discount, "
		+ "IFNULL(pb.extra_expense, 0) AS extraExpense, "
		+ "IFNULL(pb.round_off,0) AS roundOff, "
		+ "pb.grand_total AS grandTotal, "
		+ "cp.gst_number AS gstNumber, "
		+ "SUBSTRING(cp.gst_number, 3, 10) AS panNumber, "
		+ "true AS isPurchaseBill, "
		+ "pb.remark AS remark "
		+ "FROM purchase_bill pb "
		+ "INNER JOIN purchase_bill_raw_material pbrm ON pbrm.fk_purchase_bill_id = pb.id "
		+ "INNER JOIN raw_material rm ON rm.id = pbrm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = pbrm.fk_measurement_id "
		+ "INNER JOIN contact c ON c.id = pb.fk_contact_id "
		+ "LEFT JOIN tax_master tm ON tm.id = pbrm.fk_tax_master_id "
		+ "JOIN company_preferences cp "
		+ "WHERE pb.id = :id"
)

@SqlResultSetMapping(
	name = "getPurchaseBillGstDifferentStateReportResult",
	classes = @ConstructorResult(
		targetClass = IndividualRecordCommonDto.class,
		columns = {
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "invoiceNumber", type = Long.class),
			@ColumnResult(name = "purchaseDate", type = LocalDate.class),
			@ColumnResult(name = "rawMaterial", type = String.class),
			@ColumnResult(name = "weight", type = Double.class),
			@ColumnResult(name = "measurement", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
			@ColumnResult(name = "price", type = Double.class),
			@ColumnResult(name = "igst", type = Double.class),
			@ColumnResult(name = "igstPrice", type = Double.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "discount", type = Double.class),
			@ColumnResult(name = "extraExpense", type = Double.class),
			@ColumnResult(name = "roundOff", type = Double.class),
			@ColumnResult(name = "grandTotal", type = Double.class),
			@ColumnResult(name = "gstNumber", type = String.class),
			@ColumnResult(name = "panNumber", type = String.class),
			@ColumnResult(name = "isPurchaseBill", type = Boolean.class),
			@ColumnResult(name = "remark", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getDebitNoteGstDifferentStateReport",
	resultSetMapping = "getPurchaseBillDebitNoteGstDifferentStateReportResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "c.mobile_number AS customerNumber, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.venue_prefer_lang IS NOT NULL AND c.venue_prefer_lang != '' THEN c.venue_prefer_lang "
		+ "WHEN :langType = 2 AND c.venue_supportive_lang IS NOT NULL AND c.venue_supportive_lang != '' THEN c.venue_supportive_lang "
		+ "ELSE c.venue_default_lang "
		+ "END AS venue, "
		+ "dn.id AS invoiceNumber, "
		+ "dn.bill_date AS purchaseDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterial, "
		+ "dnrm.weight, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS measurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND dnrm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND dnrm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "dnrm.price, "
		+ "tm.igst, "
		+ "(tm.igst * price * dnrm.weight) / 100 AS igstPrice, "
		+ "dnrm.total_amount AS amount, "
		+ "cp.gst_number AS gstNumber, "
		+ "SUBSTRING(cp.gst_number, 3, 10) AS panNumber, "
		+ "false AS isPurchaseBill "
		+ "FROM debit_note dn "
		+ "INNER JOIN debit_note_raw_material dnrm ON dnrm.fk_debit_note_id = dn.id "
		+ "INNER JOIN raw_material rm ON rm.id = dnrm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = dnrm.fk_measurement_id "
		+ "INNER JOIN contact c ON c.id = dn.fk_contact_id "
		+ "LEFT JOIN tax_master tm ON tm.id = dnrm.fk_tax_master_id "
		+ "JOIN company_preferences cp "
		+ "WHERE dn.id = :id"
)

@SqlResultSetMapping(
	name = "getPurchaseBillDebitNoteGstDifferentStateReportResult",
	classes = @ConstructorResult(
		targetClass = IndividualRecordCommonDto.class,
		columns = {
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "customerNumber", type = String.class),
			@ColumnResult(name = "venue", type = String.class),
			@ColumnResult(name = "invoiceNumber", type = Long.class),
			@ColumnResult(name = "purchaseDate", type = LocalDate.class),
			@ColumnResult(name = "rawMaterial", type = String.class),
			@ColumnResult(name = "weight", type = Double.class),
			@ColumnResult(name = "measurement", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
			@ColumnResult(name = "price", type = Double.class),
			@ColumnResult(name = "igst", type = Double.class),
			@ColumnResult(name = "igstPrice", type = Double.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "gstNumber", type = String.class),
			@ColumnResult(name = "panNumber", type = String.class),
			@ColumnResult(name = "isPurchaseBill", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "getInputTransferToHallReport",
	resultSetMapping = "getInputTransferToHallReportResult",
	query = "SELECT "
		+ "itth.id AS id, "
		+ "itth.transfer_date AS transferDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND hm.name_prefer_lang IS NOT NULL AND hm.name_prefer_lang != '' THEN hm.name_prefer_lang "
		+ "WHEN :langType = 2 AND hm.name_supportive_lang IS NOT NULL AND hm.name_supportive_lang != '' THEN hm.name_supportive_lang "
		+ "ELSE hm.name_default_lang "
		+ "END AS hallName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterial, "
		+ "itthrm.weight, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS measurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND itthrm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND itthrm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM input_transfer_to_hall itth "
		+ "INNER JOIN input_transfer_to_hall_raw_material itthrm ON itthrm.fk_input_transfer_to_hall_id = itth.id "
		+ "INNER JOIN hall_master hm ON hm.id = itth.fk_hall_id "
		+ "INNER JOIN raw_material rm ON rm.id = itthrm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = itthrm.fk_measurement_id "
		+ "WHERE itth.id = :inputTransferToHallId "
		+ "ORDER BY itthrm.id; "
)

@SqlResultSetMapping(
	name = "getInputTransferToHallReportResult",
	classes = @ConstructorResult(
		targetClass = IndividualRecordInputTransferToHallReportDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "transferDate", type = LocalDate.class),
			@ColumnResult(name = "hallName", type = String.class),
			@ColumnResult(name = "rawMaterial", type = String.class),
			@ColumnResult(name = "weight", type = Double.class),
			@ColumnResult(name = "measurement", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getRawMaterialReturnToHallReport",
	resultSetMapping = "getInputTransferToHallReportResult",
	query = "SELECT "
		+ "rmrth.id AS id, "
		+ "rmrth.return_date AS transferDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND hm.name_prefer_lang IS NOT NULL AND hm.name_prefer_lang != '' THEN hm.name_prefer_lang "
		+ "WHEN :langType = 2 AND hm.name_supportive_lang IS NOT NULL AND hm.name_supportive_lang != '' THEN hm.name_supportive_lang "
		+ "ELSE hm.name_default_lang "
		+ "END AS hallName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterial, "
		+ "rmrthd.weight, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS measurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND rmrthd.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND rmrthd.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM raw_material_return_to_hall rmrth "
		+ "INNER JOIN raw_material_return_to_hall_details rmrthd ON rmrthd.fk_raw_material_return_to_hall_id = rmrth.id  "
		+ "INNER JOIN hall_master hm ON hm.id = rmrth.fk_hall_id  "
		+ "INNER JOIN raw_material rm ON rm.id = rmrthd.fk_raw_material_id  "
		+ "INNER JOIN measurement m ON m.id = rmrthd.fk_measurement_id  "
		+ "WHERE rmrth.id = :rawMaterialReturnToHallId"
)

@Entity
public class IndividualRecordReportNativeQuery extends AuditIdModelOnly {
}