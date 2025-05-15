package com.catering.dao.cash_payment_receipt;

import java.time.LocalDate;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.CashBankPaymentReceiptCommonResultListDto;
import com.catering.dto.tenant.request.PaymentContactCustomDto;
import com.catering.dto.tenant.request.VoucherNumberDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * A set of native SQL queries that fetches contact details related to different order types, 
 * such as labour allocation, purchase bills, invoices, debit notes, etc.
 * Each query returns a list of contacts along with their default, preferred, and supportive language names, 
 * mobile number, and current balance. The queries also compute remaining amounts based on payments made.
 */
@NamedNativeQuery(
	name = "fetchContactOfLabourAllocation",
	resultSetMapping = "paymentContactResultSet",
	query = "SELECT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM contact c "
		+ "JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+ "JOIN order_labour_distribution old ON c.id = old.fk_contact_id "
		+ "GROUP BY c.id;"
)

@NamedNativeQuery(
	name = "fetchContactOfOrderByOrderType",
	resultSetMapping = "paymentContactResultSet",
	query = "SELECT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM contact c "
		+ "JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+ "JOIN order_menu_allocation_type omat ON omat.fk_contact_id = c.id "
		+ "JOIN order_menu_preparation_menu_item ompmi ON omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "WHERE ompmi.order_type = :orderTypeId "
		+ "GROUP BY c.id;"
)

@NamedNativeQuery(
	name = "fetchContactOfPurchaseBill",
	resultSetMapping = "paymentContactResultSet",
	query = "SELECT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM contact c "
		+ "JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+ "JOIN purchase_bill pb ON c.id = pb.fk_contact_id "
		+ "GROUP BY c.id;"
)

@NamedNativeQuery(
	name = "fetchContactOfInvoice",
	resultSetMapping = "paymentContactResultSet",
	query = "SELECT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM contact c "
		+ "JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+ "JOIN customer_order_details cod ON c.id = cod.fk_contact_customer_id "
		+ "JOIN order_invoice oi ON oi.fk_customer_order_details_id = cod.id "
		+ "GROUP BY c.id;"
)

@NamedNativeQuery(
	name = "fetchContactOfDebitNote",
	resultSetMapping = "paymentContactResultSet",
	query = "SELECT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM contact c "
		+ "JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+ "JOIN debit_note dn ON c.id = dn.fk_contact_id "
		+ "GROUP BY c.id;"
)

@NamedNativeQuery(
	name = "fetchPaymentContactList",
	resultSetMapping = "paymentContactResultSet",
	query = "SELECT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM contact c "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "INNER JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc on cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE cc.is_non_updatable = 0 "
		+ "GROUP BY c.id;"
)

@SqlResultSetMapping(
	name = "paymentContactResultSet",
	classes = @ConstructorResult(
		targetClass = PaymentContactCustomDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "currentBalance", type = Double.class)
		}
	)
)

@NamedNativeQuery(
	name = "fetchVoucherNumberOfPurchaseBill",
	resultSetMapping = "voucherNumberResultSet",
	query = "SELECT "
		+ "pb.id AS orderId, "
		+ "CASE "
		+ "WHEN COALESCE(SUM(pbrm.total_amount), 0) - (COALESCE(SUM(DISTINCT cash_total.totalAmount), 0) + COALESCE(SUM(DISTINCT bank_total.totalAmount), 0)) < 0 "
		+ "THEN 0 "
		+ "ELSE COALESCE(SUM(pbrm.total_amount), 0) - (COALESCE(SUM(DISTINCT cash_total.totalAmount), 0) + COALESCE(SUM(DISTINCT bank_total.totalAmount), 0)) "
		+ "END AS remainAmount, "
		+ "c.id AS id, "
		+ "c.name_default_lang AS nameDefaultLang, " 
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM purchase_bill pb "
		+ "JOIN purchase_bill_raw_material pbrm ON pb.id = pbrm.fk_purchase_bill_id "
		+ "JOIN contact c ON pb.fk_contact_id = c.id "
		+ "JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+"LEFT JOIN ( "
		+"SELECT "
		+"cprd.voucher_number, " 
		+"cprd.fk_contact_id, "
		+ "SUM(cprd.amount) AS totalAmount "
		+ "FROM cash_payment_receipt_details cprd "
		+ "WHERE cprd.voucher_type = 1 "
		+ "GROUP BY cprd.voucher_number, cprd.fk_contact_id "
		+ ") AS cash_total "
		+ "ON pb.id = cash_total.voucher_number AND pb.fk_contact_id = cash_total.fk_contact_id "
		+ "LEFT JOIN ( "
		+ "SELECT "
		+ "bprd.voucher_number, "
		+ "bprd.fk_contact_id, "
		+ "SUM(bprd.amount) AS totalAmount "
		+ "FROM bank_payment_receipt_details bprd "
		+ "WHERE bprd.voucher_type = 1 "
		+ "GROUP BY bprd.voucher_number, bprd.fk_contact_id "
		+ ") AS bank_total "
		+ "ON pb.id = bank_total.voucher_number AND pb.fk_contact_id = bank_total.fk_contact_id "
		+ "GROUP BY pb.id, pb.fk_contact_id;"
)

@NamedNativeQuery(
	name = "fetchVoucherNumberOfOrderByOrderType",
	resultSetMapping = "voucherNumberResultSet",
	query = "SELECT "
		+ "cod.id AS orderId, "
		+ "CASE "
		+ "WHEN COALESCE(SUM(omat.total), 0) - (COALESCE(SUM(DISTINCT cash_total.totalAmount), 0) + COALESCE(SUM(DISTINCT bank_total.totalAmount), 0)) < 0 "
		+ "THEN 0 "
		+ "ELSE COALESCE(SUM(omat.total), 0) - (COALESCE(SUM(DISTINCT cash_total.totalAmount), 0) + COALESCE(SUM(DISTINCT bank_total.totalAmount), 0)) "
		+ "END AS remainAmount, "
		+ "c.id AS id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM order_menu_allocation_type omat "
		+ "JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = omat.fk_order_menu_preparation_menu_item_id "
		+ "JOIN order_menu_preparation omp ON ompmi.fk_menu_preparation_id = omp.id "
		+ "JOIN order_function off ON omp.fk_order_function_id = off.id "
		+ "JOIN customer_order_details cod ON off.fk_customer_order_details_id = cod.id "
		+ "JOIN contact c ON omat.fk_contact_id = c.id "
		+ "JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+"LEFT JOIN ( "
		+"SELECT "
		+"cprd.voucher_number, " 
		+"cprd.fk_contact_id, "
		+ "SUM(cprd.amount) AS totalAmount "
		+ "FROM cash_payment_receipt_details cprd "
		+ "WHERE cprd.voucher_type = :voucherTypeId "
		+ "GROUP BY cprd.voucher_number, cprd.fk_contact_id "
		+ ") AS cash_total "
		+ "ON cod.id = cash_total.voucher_number AND cash_total.fk_contact_id = omat.fk_contact_id "
		+ "LEFT JOIN ( "
		+ "SELECT  "
		+ "bprd.voucher_number,  "
		+ "bprd.fk_contact_id,  "
		+ "SUM(bprd.amount) AS totalAmount "
		+ "FROM bank_payment_receipt_details bprd "
		+ "WHERE bprd.voucher_type = :voucherTypeId "
		+ "GROUP BY bprd.voucher_number, bprd.fk_contact_id "
		+ ") AS bank_total "
		+ "ON cod.id = bank_total.voucher_number AND bank_total.fk_contact_id = omat.fk_contact_id "
		+ "WHERE ompmi.order_type = :orderTypeId "
		+ "GROUP BY cod.id, omat.fk_contact_id;"
)

@NamedNativeQuery(
	name = "fetchVoucherNumberOfLabourAllocation",
	resultSetMapping = "voucherNumberResultSet",
	query = "SELECT "
		+ "cod.id AS orderId, "
		+ "CASE "
		+ "WHEN COALESCE(SUM(old.labour_price * old.quantity), 0) - (COALESCE(SUM(DISTINCT cash_total.totalAmount), 0) + COALESCE(SUM(DISTINCT bank_total.totalAmount), 0)) < 0 "
		+ "THEN 0 "
		+ "ELSE COALESCE(SUM(old.labour_price * old.quantity), 0) - (COALESCE(SUM(DISTINCT cash_total.totalAmount), 0) + COALESCE(SUM(DISTINCT bank_total.totalAmount), 0)) "
		+ "END AS remainAmount, "
		+ "c.id AS id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM order_labour_distribution old "
		+ "JOIN order_function off ON off.id = old.fk_order_function_id "
		+ "JOIN customer_order_details cod ON cod.id = off.fk_customer_order_details_id "
		+ "JOIN contact c ON old.fk_contact_id = c.id "
		+ "JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+"LEFT JOIN ( "
		+"SELECT "
		+"cprd.voucher_number, " 
		+"cprd.fk_contact_id, "
		+ "SUM(cprd.amount) AS totalAmount "
		+ "FROM cash_payment_receipt_details cprd "
		+ "WHERE cprd.voucher_type = 4 "
		+ "GROUP BY cprd.voucher_number, cprd.fk_contact_id "
		+ ") AS cash_total "
		+ "ON cod.id = cash_total.voucher_number AND cash_total.fk_contact_id = old.fk_contact_id "
		+ "LEFT JOIN ( "
		+ "SELECT  "
		+ "bprd.voucher_number,  "
		+ "bprd.fk_contact_id,  "
		+ "SUM(bprd.amount) AS totalAmount "
		+ "FROM bank_payment_receipt_details bprd "
		+ "WHERE bprd.voucher_type = 4 "
		+ "GROUP BY bprd.voucher_number, bprd.fk_contact_id "
		+ ") AS bank_total "
		+ "ON cod.id = bank_total.voucher_number AND bank_total.fk_contact_id = old.fk_contact_id "
		+ "GROUP BY cod.id, old.fk_contact_id;"
)

@NamedNativeQuery(
	name = "fetchVoucherNumberOfDebitNote",
	resultSetMapping = "voucherNumberResultSet",
	query = "SELECT "
		+ "dn.id AS orderId, "
		+ "CASE "
		+ "WHEN COALESCE(SUM(dnrm.total_amount), 0) - (COALESCE(SUM(DISTINCT cash_total.totalAmount), 0) + COALESCE(SUM(DISTINCT bank_total.totalAmount), 0)) < 0 "
		+ "THEN 0 "
		+ "ELSE COALESCE(SUM(dnrm.total_amount), 0) - (COALESCE(SUM(DISTINCT cash_total.totalAmount), 0) + COALESCE(SUM(DISTINCT bank_total.totalAmount), 0)) "
		+ "END AS remainAmount, "
		+ "c.id AS id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM debit_note dn "
		+ "JOIN debit_note_raw_material dnrm ON dnrm.fk_debit_note_id = dn.id "
		+ "JOIN contact c ON dn.fk_contact_id = c.id "
		+ "JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+"LEFT JOIN ( "
		+"SELECT "
		+"cprd.voucher_number, " 
		+"cprd.fk_contact_id, "
		+ "SUM(cprd.amount) AS totalAmount "
		+ "FROM cash_payment_receipt_details cprd "
		+ "WHERE cprd.voucher_type = 5 "
		+ "GROUP BY cprd.voucher_number, cprd.fk_contact_id "
		+ ") AS cash_total "
		+ "ON dn.id = cash_total.voucher_number AND cash_total.fk_contact_id = dn.fk_contact_id "
		+ "LEFT JOIN ( "
		+ "SELECT  "
		+ "bprd.voucher_number,  "
		+ "bprd.fk_contact_id,  "
		+ "SUM(bprd.amount) AS totalAmount "
		+ "FROM bank_payment_receipt_details bprd "
		+ "WHERE bprd.voucher_type = 5 "
		+ "GROUP BY bprd.voucher_number, bprd.fk_contact_id "
		+ ") AS bank_total "
		+ "ON dn.id = bank_total.voucher_number AND bank_total.fk_contact_id = dn.fk_contact_id "
		+ "GROUP BY dn.id, dn.fk_contact_id;"
)

@NamedNativeQuery(
	name = "fetchVoucherNumberOfInvoice",
	resultSetMapping = "voucherNumberResultSet",
	query = "SELECT "
		+ "cod.id AS orderId, "
		+ "CASE "
		+ "WHEN (SELECT fk_subscription_id FROM company_preferences) = 2 THEN COALESCE(oi.grand_total, 0) - COALESCE(oi.advance_payment, 0) "
		+ "ELSE "
		+ "CASE "
		+ "WHEN COALESCE(COALESCE(oi.grand_total, 0) - COALESCE(oi.advance_payment, 0), 0) - (COALESCE(SUM(DISTINCT cash_total.totalAmount), 0) + COALESCE(SUM(DISTINCT bank_total.totalAmount), 0)) < 0 "
		+ "THEN 0 "
		+ "ELSE COALESCE(COALESCE(oi.grand_total, 0) - COALESCE(oi.advance_payment, 0), 0) - (COALESCE(SUM(DISTINCT cash_total.totalAmount), 0) + COALESCE(SUM(DISTINCT bank_total.totalAmount), 0)) "
		+ "END "
		+ "END AS remainAmount, "
		+ "c.id AS id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.mobile_number AS mobileNumber, "
		+ "ab.balance AS currentBalance "
		+ "FROM order_invoice oi "
		+ "JOIN customer_order_details cod ON cod.id = oi.fk_customer_order_details_id "
		+ "JOIN contact c ON cod.fk_contact_customer_id = c.id "
		+ "JOIN account_balance ab ON ab.fk_contact_id = c.id "
		+"LEFT JOIN ( "
		+"SELECT "
		+"cprd.voucher_number, " 
		+"cprd.fk_contact_id, "
		+ "SUM(cprd.amount) AS totalAmount "
		+ "FROM cash_payment_receipt_details cprd "
		+ "WHERE cprd.voucher_type = 6 "
		+ "GROUP BY cprd.voucher_number, cprd.fk_contact_id "
		+ ") AS cash_total "
		+ "ON oi.id = cash_total.voucher_number AND cash_total.fk_contact_id = cod.fk_contact_customer_id "
		+ "LEFT JOIN ( "
		+ "SELECT  "
		+ "bprd.voucher_number,  "
		+ "bprd.fk_contact_id,  "
		+ "SUM(bprd.amount) AS totalAmount "
		+ "FROM bank_payment_receipt_details bprd "
		+ "WHERE bprd.voucher_type = 6 "
		+ "GROUP BY bprd.voucher_number, bprd.fk_contact_id "
		+ ") AS bank_total "
		+ "ON oi.id = bank_total.voucher_number AND bank_total.fk_contact_id = cod.fk_contact_customer_id "
		+ "GROUP BY oi.id, cod.fk_contact_customer_id;"
)

@NamedNativeQuery(
	name = "fetchVoucherNumberOfAdvancePayment",
	resultSetMapping = "voucherNumberofAdvancePaymentResultSet",
	query = "SELECT "
			+ "cod.id AS orderId, "
			+ "0 AS remainAmount, "
			+ "cod.event_main_date AS eventDate, "
			+ "c.id AS id, "
			+ "c.name_default_lang AS nameDefaultLang, "
			+ "c.name_prefer_lang AS namePreferLang, "
			+ "c.name_supportive_lang AS nameSupportiveLang, "
			+ "c.mobile_number AS mobileNumber, "
			+ "ab.balance AS currentBalance "
			+ "FROM customer_order_details cod "
			+ "LEFT JOIN contact c ON cod.fk_contact_customer_id = c.id "
			+ "LEFT JOIN account_balance ab ON ab.fk_contact_id = c.id "
			+ "LEFT JOIN order_invoice oi ON cod.id = oi.fk_customer_order_details_id "
			+ "ORDER BY cod.id"
)

@SqlResultSetMapping(
	name = "voucherNumberofAdvancePaymentResultSet",
	classes = @ConstructorResult(
		targetClass = VoucherNumberDto.class,
		columns = {
			@ColumnResult(name = "orderId", type = Long.class),
			@ColumnResult(name = "remainAmount", type = Double.class),
			@ColumnResult(name = "eventDate", type = LocalDate.class),
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "currentBalance", type = Double.class)
		}
	)
)

@SqlResultSetMapping(
	name = "voucherNumberResultSet",
	classes = @ConstructorResult(
		targetClass = VoucherNumberDto.class,
		columns = {
			@ColumnResult(name = "orderId", type = Long.class),
			@ColumnResult(name = "remainAmount", type = Double.class),
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "currentBalance", type = Double.class)
		}
	)
)

@NamedNativeQuery(
	name = "getSuppilerContactOfCashPaymentReceiptDetails",
	resultSetMapping = "suppilerContactOfCashPaymentReceiptDetailsResultSet",
	query = "SELECT "
		+ "IF(COUNT(DISTINCT cprd.fk_contact_id) > 1, NULL, c.name_default_lang) AS nameDefaultLang, "
		+ "IF(COUNT(DISTINCT cprd.fk_contact_id) > 1, NULL, c.name_prefer_lang) AS namePreferLang, "
		+ "IF(COUNT(DISTINCT cprd.fk_contact_id) > 1, NULL, c.name_supportive_lang) AS nameSupportiveLang, "
		+ "IFNULL(SUM(cprd.amount), 0) AS totalAmount "
		+ "FROM cash_payment_receipt_details cprd "
		+ "INNER JOIN cash_payment_receipt cpr ON cpr.id = cprd.fk_cash_payment_receipt_id "
		+ "INNER JOIN contact c ON c.id = cprd.fk_contact_id "
		+ "WHERE cpr.id = :cashPaymentReceiptId"
)

@SqlResultSetMapping(
	name = "suppilerContactOfCashPaymentReceiptDetailsResultSet",
	classes = @ConstructorResult(
		targetClass = CashBankPaymentReceiptCommonResultListDto.class,
		columns = {
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "totalAmount", type = Double.class)
		}
	)
)

@Entity
public class CashPaymentReceiptNativeQuery extends AuditIdModelOnly {
}