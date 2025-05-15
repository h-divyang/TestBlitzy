package com.catering.dao.accounting_reports.transaction_report;

import java.time.LocalDateTime;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import com.catering.dto.tenant.request.DateWisePurchaseOrderReportDto;
import com.catering.dto.tenant.request.DateWisePurchaseOrderWithoutRawMaterialDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.DateWiseBankPaymentReportDto;
import com.catering.dto.tenant.request.DateWiseCashPaymentReportDto;
import com.catering.dto.tenant.request.DateWiseInputTransferHallReportDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity representing a transaction report in the system.
 * This class extends {@link AuditIdModelOnly} and is used for querying transaction data 
 * related to various reports in the database.
 * It doesn't contain any additional fields or methods, but serves as a marker class 
 * for the corresponding table in the database.
 * 
 * @see AuditIdModelOnly for audit fields like creation and modification information.
 */
@NamedNativeQuery(
	name = "getSuppilerContactForCashPaymnetReceiptDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT  "
		+ "c.id AS id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM cash_payment_receipt cpr  "
		+ "INNER JOIN cash_payment_receipt_details cprd ON cprd.fk_cash_payment_receipt_id = cpr.id  "
		+ "INNER JOIN contact c ON c.id = cprd.fk_contact_id  "
		+ "WHERE (cpr.transaction_date >= :startDate OR :startDate IS NULL) AND (cpr.transaction_date <= :endDate OR :endDate IS NULL) AND c.is_active = TRUE AND cpr.transaction_type = :transactionTypeId "
)

@NamedNativeQuery(
	name = "getDateWiseCashPaymentReceiptReport",
	resultSetMapping = "getCashPaymentReportResult",
	query = "SELECT "
		+ "cpr.id AS refNo, "
		+ "COALESCE(cpr.transaction_date, NULL) AS transactionDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS accountName, "
		+ "cprd.amount AS amount, "
		+ "cprd.remark AS particulars, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency, "
		+ "cpr.transaction_type AS transactionType "
		+ "FROM cash_payment_receipt cpr "
		+ "INNER JOIN cash_payment_receipt_details cprd ON cprd.fk_cash_payment_receipt_id = cpr.id "
		+ "INNER JOIN contact c ON c.id = cprd.fk_contact_id "
		+ "WHERE (cpr.transaction_date >= :startDate OR :startDate IS NULL) AND (cpr.transaction_date <= :endDate OR :endDate IS NULL) AND (0 = :supplierCategoryId OR cprd.fk_contact_id = :supplierCategoryId) AND (cpr.transaction_type = :transactionTypeId) "
		+ "ORDER BY  "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN accountName "
		+ "WHEN :orderById = 2 THEN transactionDate "
		+ "WHEN :orderById = 3 THEN refNo "
		+ "END, "
		+ "CASE "
		+ "WHEN :orderById = 4 THEN amount "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 5 THEN amount "
		+ "END DESC, "
		+ "refNo "
)

@SqlResultSetMapping(
	name = "getCashPaymentReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseCashPaymentReportDto.class,
		columns = {
			@ColumnResult(name = "refNo", type = Long.class),
			@ColumnResult(name = "transactionDate", type = LocalDateTime.class),
			@ColumnResult(name = "accountName", type = String.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "particulars", type = String.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class),
			@ColumnResult(name = "transactionType", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getSuppilerContactForBankPaymnetReceiptDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "c.id AS id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM bank_payment_receipt bpr "
		+ "INNER JOIN bank_payment_receipt_details bprd ON bprd.fk_bank_payment_receipt_id = bpr.id "
		+ "INNER JOIN contact c ON c.id = bprd.fk_contact_id "
		+ "WHERE (bpr.transaction_date >= :startDate OR :startDate IS NULL) AND (bpr.transaction_date <= :endDate OR :endDate IS NULL) AND (c.is_active = TRUE) AND (bpr.is_active = TRUE) AND bpr.transaction_type = :transactionTypeId "
)

@NamedNativeQuery(
	name = "getDateWiseBankPaymentReceiptReport",
	resultSetMapping = "getBankPaymentReportResult",
	query = "SELECT "
		+ "bpr.fk_contact_id AS contactId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c1.name_prefer_lang IS NOT NULL AND c1.name_prefer_lang != '' THEN c1.name_prefer_lang "
		+ "WHEN :langType = 2 AND c1.name_supportive_lang IS NOT NULL AND c1.name_supportive_lang != '' THEN c1.name_supportive_lang "
		+ "ELSE c1.name_default_lang "
		+ "END AS bankName, "
		+ "bpr.id AS refno, "
		+ "bpr.transaction_date AS transactionDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS accountName, "
		+ "bprd.amount AS amount, "
		+ "bprd.cheque_transaction_number AS cheqNo, "
		+ "bprd.cheque_transaction_date AS cheqDate, "
		+ "bprd.remark AS particulars, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency, "
		+ "bpr.transaction_type AS transactionType "
		+ "FROM bank_payment_receipt bpr "
		+ "INNER JOIN bank_payment_receipt_details bprd ON bprd.fk_bank_payment_receipt_id = bpr.id "
		+ "INNER JOIN contact c ON c.id = bprd.fk_contact_id "
		+ "INNER JOIN contact c1 ON c1.id = bpr.fk_contact_id "
		+ "WHERE (bpr.transaction_date >= :startDate OR :startDate IS NULL) AND (bpr.transaction_date <= :endDate OR :endDate IS NULL) AND (0 = :supplierCategoryId OR bprd.fk_contact_id = :supplierCategoryId) AND (bpr.transaction_type = :transactionTypeId) "
		+ "ORDER BY "
		+ "bpr.fk_contact_id,transactionDate, "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN accountName "
		+ "WHEN :orderById = 2 THEN transactionDate "
		+ "END, "
		+ "CASE "
		+ "WHEN :orderById = 3 THEN refNo "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 4 THEN amount "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 5 THEN amount "
		+ "END DESC, "
		+ "refNo"
)

@SqlResultSetMapping(
	name = "getBankPaymentReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseBankPaymentReportDto.class,
		columns = {
			@ColumnResult(name = "contactId", type = Long.class),
			@ColumnResult(name = "bankName", type = String.class),
			@ColumnResult(name = "refNo", type = Long.class),
			@ColumnResult(name = "transactionDate", type = LocalDateTime.class),
			@ColumnResult(name = "accountName", type = String.class),
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "cheqNo", type = String.class),
			@ColumnResult(name = "cheqDate", type = LocalDateTime.class),
			@ColumnResult(name = "particulars", type = String.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class),
			@ColumnResult(name = "transactionType", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getSuppilerContactForPurchaseOrderDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "c.id , "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM purchase_order po "
		+ "INNER JOIN contact c ON c.id = po.fk_contact_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "INNER JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE (cc.fk_contact_category_type_id = :contactCategoryTypeId) AND (c.is_active = TRUE) "
		+ "AND (po.purchase_date >= :startDate OR :startDate IS NULL) AND (po.purchase_date <= :endDate OR :endDate IS NULL) "
		+ "ORDER BY po.id "
)

@NamedNativeQuery(
	name = "getRawMaterialForPurchaseOrderDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "rm.id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM purchase_order_raw_material porm "
		+ "LEFT JOIN raw_material rm ON rm.id = porm.fk_raw_material_id "
		+ "LEFT JOIN purchase_order po ON po.id = porm.fk_purchase_order_id "
		+ "LEFT JOIN contact c ON c.id = po.fk_contact_id "
		+ "WHERE (0 = :supplierCategoryId OR po.fk_contact_id = :supplierCategoryId) AND (c.is_active = TRUE)"
		+ "AND (po.purchase_date >= :startDate OR :startDate IS NULL) AND (po.purchase_date <= :endDate OR :endDate IS NULL) "
)

@SqlResultSetMapping(
	name = "getDropDownDataResultPurchaseOrder",
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
	name = "getDateWisePurchaseOrderReportWithitems",
	resultSetMapping = "getReportWithItemsResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "po.id AS purchaseOrderId, "
		+ "po.purchase_date AS purchaseOrderDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "porm.weight AS finalQty, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS finalQuantityMeasurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND porm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND porm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "porm.price AS rate, "
		+ "porm.weight * porm.price AS amt, "
		+ "porm.total_amount AS netAmt, "
		+ "SUM(porm.weight * porm.price) OVER (PARTITION BY po.id) AS suppilerContactSubTotal, "
		+ "SUM(porm.total_amount) OVER (PARTITION BY po.id) AS suppilerContactTotal, "
		+ "(SELECT "
		+ "cs.decimal_limit_for_currency "
		+ "FROM company_setting cs "
		+ ") AS decimalLimitForCurrency "
		+ "FROM purchase_order po "
		+ "INNER JOIN contact c ON c.id = po.fk_contact_id "
		+ "INNER JOIN purchase_order_raw_material porm ON porm.fk_purchase_order_id = po.id "
		+ "INNER JOIN raw_material rm ON rm.id = porm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = porm.fk_measurement_id "
		+ "WHERE (po.purchase_date >= :startDate OR :startDate IS NULL) AND (po.purchase_date <= :endDate OR :endDate IS NULL) "
		+ "AND (0 = :supplierContactId OR po.fk_contact_id = :supplierContactId) "
		+ "AND (0 = :rawMaterialId OR porm.fk_raw_material_id = :rawMaterialId) "
		+ "ORDER BY "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN c.name_default_lang "
		+ "WHEN :orderById = 2 THEN po.purchase_date "
		+ "END, "
		+ "CASE "
		+ "WHEN :orderById = 3 THEN po.id "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 4 THEN suppilerContactTotal "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 5 THEN suppilerContactTotal "
		+ "END DESC, "
		+ "po.id; "
)

@SqlResultSetMapping(
	name = "getReportWithItemsResult",
	classes = @ConstructorResult (
		targetClass = DateWisePurchaseOrderReportDto.class,
		columns = {
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "purchaseOrderId", type = Long.class),
			@ColumnResult(name = "purchaseOrderDate", type = LocalDateTime.class),
			@ColumnResult(name = "rawMaterialName", type = String.class),
			@ColumnResult(name = "finalQty", type = Double.class),
			@ColumnResult(name = "finalQuantityMeasurement", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "amt", type = Double.class),
			@ColumnResult(name = "netAmt", type = Double.class),
			@ColumnResult(name = "suppilerContactSubTotal", type = Double.class),
			@ColumnResult(name = "suppilerContactTotal", type = Double.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getDateWisePurchaseOrderReportWithoutitems",
	resultSetMapping = "getReportWithoutItemsResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2  AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "po.id AS purchaseOrderId, "
		+ "po.purchase_date AS purchaseOrderDate, "
		+ "SUM(porm.weight * porm.price) AS suppilerContactSubTotal, "
		+ "SUM(porm.total_amount) AS suppilerContactTotal, "
		+ "(SELECT "
		+ "cs.decimal_limit_for_currency "
		+ "FROM company_setting cs "
		+ ") AS decimalLimitForCurrency "
		+ "FROM purchase_order po "
		+ "INNER JOIN contact c ON c.id = po.fk_contact_id "
		+ "INNER JOIN purchase_order_raw_material porm ON porm.fk_purchase_order_id = po.id "
		+ "WHERE (po.purchase_date >= :startDate OR :startDate IS NULL) AND (po.purchase_date <= :endDate OR :endDate IS NULL) "
		+ "AND (0 = :supplierContactId OR po.fk_contact_id = :supplierContactId) "
		+ "AND (0 = :rawMaterialId OR porm.fk_raw_material_id = :rawMaterialId) "
		+ "GROUP BY po.id "
		+ "ORDER BY "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN c.name_default_lang "
		+ "WHEN :orderById = 2 THEN po.purchase_date "
		+ "END, "
		+ "CASE "
		+ "WHEN :orderById = 3 THEN po.id "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 4 THEN suppilerContactTotal "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 5 THEN suppilerContactTotal "
		+ "END DESC, "
		+ "po.id; "
)

@SqlResultSetMapping(
	name = "getReportWithoutItemsResult",
	classes = @ConstructorResult(
		targetClass = DateWisePurchaseOrderWithoutRawMaterialDto.class,
		columns = {
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "purchaseOrderId", type = Long.class),
			@ColumnResult(name = "purchaseOrderDate", type = LocalDateTime.class),
			@ColumnResult(name = "suppilerContactSubTotal", type = Double.class),
			@ColumnResult(name = "suppilerContactTotal", type = Double.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getSuppilerContactForPurchaseBillDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM purchase_bill pb "
		+ "INNER JOIN contact c ON c.id = pb.fk_contact_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "INNER JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE (cc.fk_contact_category_type_id = :contactCategoryTypeId) AND (c.is_active = TRUE)"
		+ "AND (pb.bill_date >= :startDate OR :startDate IS NULL) AND (pb.bill_date <= :endDate OR :endDate IS NULL) "
		+ "ORDER BY pb.id "
)

@NamedNativeQuery(
	name = "getRawMaterialForPurchaseBillDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "rm.id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM purchase_bill_raw_material pbrm "
		+ "LEFT JOIN raw_material rm ON rm.id = pbrm.fk_raw_material_id "
		+ "LEFT JOIN purchase_bill pb ON pb.id = pbrm.fk_purchase_bill_id "
		+ "LEFT JOIN contact c ON c.id = pb.fk_contact_id "
		+ "WHERE (0 = :supplierCategoryId OR pb.fk_contact_id = :supplierCategoryId) AND (c.is_active = TRUE)"
		+ "AND (pb.bill_date >= :startDate OR :startDate IS NULL) AND (pb.bill_date <= :endDate OR :endDate IS NULL) "
)

@NamedNativeQuery(
	name = "getDateWisePurchaseBillReportWithitems",
	resultSetMapping = "getReportWithItemsResult",
	query = "SELECT "
		+ "CASE " 
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "pb.id AS purchaseOrderId, "
		+ "pb.bill_date AS purchaseOrderDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "pbrm.weight AS finalQty, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS finalQuantityMeasurement, "
		+ "m.symbol_default_lang AS finalQuantityMeasurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND pbrm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND pbrm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "pbrm.price AS rate, "
		+ "pbrm.weight * pbrm.price AS amt, "
		+ "pbrm.total_amount AS netAmt, "
		+ "SUM(pbrm.weight * pbrm.price) OVER (PARTITION BY pb.id) AS suppilerContactSubTotal, "
		+ "pb.grand_total AS suppilerContactTotal, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM purchase_bill pb "
		+ "INNER JOIN contact c ON c.id = pb.fk_contact_id "
		+ "INNER JOIN purchase_bill_raw_material pbrm ON pbrm.fk_purchase_bill_id = pb.id "
		+ "INNER JOIN raw_material rm ON rm.id = pbrm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = pbrm.fk_measurement_id "
		+ "WHERE (pb.bill_date >= :startDate OR :startDate IS NULL) AND (pb.bill_date <= :endDate OR :endDate IS NULL) "
		+ "AND (0 = :supplierContactId OR pb.fk_contact_id = :supplierContactId) "
		+ "AND (0 = :rawMaterialId OR pbrm.fk_raw_material_id = :rawMaterialId) "
		+ "ORDER BY "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN c.name_default_lang "
		+ "WHEN :orderById = 2 THEN pb.bill_date "
		+ "WHEN :orderById = 3 THEN pb.id "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 4 THEN suppilerContactTotal "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 5 THEN suppilerContactTotal "
		+ "END DESC, "
		+ "pb.id; "
)

@NamedNativeQuery(
	name = "getDateWisePurchaseBillReportWithoutitems",
	resultSetMapping = "getReportWithoutItemsResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "pb.id AS purchaseOrderId, "
		+ "pb.bill_date AS purchaseOrderDate, "
		+ "SUM(pbrm.weight * pbrm.price) AS suppilerContactSubTotal, "
		+ "pb.grand_total AS suppilerContactTotal, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM purchase_bill pb "
		+ "INNER JOIN contact c ON c.id = pb.fk_contact_id "
		+ "INNER JOIN purchase_bill_raw_material pbrm ON pbrm.fk_purchase_bill_id = pb.id "
		+ "WHERE (pb.bill_date >= :startDate OR :startDate IS NULL) AND (pb.bill_date <= :endDate OR :endDate IS NULL) "
		+ "AND (0 = :supplierContactId OR pb.fk_contact_id = :supplierContactId) "
		+ "AND (0 = :rawMaterialId OR pbrm.fk_raw_material_id = :rawMaterialId) "
		+ "GROUP BY pb.id "
		+ "ORDER BY "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN c.name_default_lang "
		+ "WHEN :orderById = 2 THEN pb.bill_date "
		+ "WHEN :orderById = 3 THEN pb.id "
		+ "END, "
		+ "CASE "
		+ "WHEN :orderById = 4 THEN suppilerContactTotal "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 5 THEN suppilerContactTotal "
		+ "END DESC, "
		+ "pb.id; "
)

@NamedNativeQuery(
	name = "getInputTransferToHallDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "hm.id, "
		+ "hm.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN hm.name_prefer_lang IS NOT NULL AND hm.name_prefer_lang != '' THEN hm.name_prefer_lang "
		+ "ELSE hm.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN hm.name_supportive_lang IS NOT NULL AND hm.name_supportive_lang != '' THEN hm.name_supportive_lang "
		+ "ELSE hm.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM input_transfer_to_hall itth "
		+ "INNER JOIN hall_master hm ON hm.id = itth.fk_hall_id "
		+ "WHERE (hm.is_active = TRUE) AND (itth.transfer_date >= :startDate OR :startDate IS NULL) AND (itth.transfer_date <= :endDate OR :endDate IS NULL) "
		+ "ORDER BY itth.id "
)

@NamedNativeQuery(
	name = "getRawMaterialForInputTransferDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "rm.id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM input_transfer_to_hall_raw_material itthrm "
		+ "LEFT JOIN raw_material rm ON rm.id = itthrm.fk_raw_material_id "
		+ "LEFT JOIN input_transfer_to_hall itth ON itth.id = itthrm.fk_input_transfer_to_hall_id "
		+ "LEFT JOIN hall_master hm ON hm.id = itth.fk_hall_id "
		+ "WHERE (0 = :hallId OR itth.fk_hall_id = :hallId) AND (hm.is_active = true) "
		+ "AND (itth.transfer_date >= :startDate OR :startDate IS NULL) AND (itth.transfer_date <= :endDate OR :endDate IS NULL) "
)

@NamedNativeQuery(
	name = "getDateWiseInputTransferHallReportWithitems",
	resultSetMapping = "getInputTransferHallReportWithItemsResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND hm.name_prefer_lang IS NOT NULL AND hm.name_prefer_lang != '' THEN hm.name_prefer_lang "
		+ "WHEN :langType = 2 AND hm.name_supportive_lang IS NOT NULL AND hm.name_supportive_lang != '' THEN hm.name_supportive_lang "
		+ "ELSE hm.name_default_lang "
		+ "END AS hallName, "
		+ "itth.id AS inputTransferHallId, "
		+ "itth.transfer_date AS transferDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "itthrm.weight AS finalQty, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS finalQuantityMeasurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND itthrm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND itthrm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM input_transfer_to_hall itth "
		+ "INNER JOIN hall_master hm ON hm.id = itth.fk_hall_id "
		+ "INNER JOIN input_transfer_to_hall_raw_material itthrm ON itthrm.fk_input_transfer_to_hall_id = itth.id "
		+ "INNER JOIN raw_material rm ON rm.id = itthrm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = itthrm.fk_measurement_id "
		+ "WHERE (itth.transfer_date >= :startDate OR :startDate IS NULL) AND (itth.transfer_date <= :endDate OR :endDate IS NULL) "
		+ "AND (0 = :hallId OR itth.fk_hall_id = :hallId) "
		+ "AND (0 = :rawMaterialId OR itthrm.fk_raw_material_id = :rawMaterialId) "
		+ "ORDER BY "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN hm.name_default_lang "
		+ "WHEN :orderById = 2 THEN itth.transfer_date "
		+ "WHEN :orderById = 3 THEN itth.id "
		+ "END ASC, "
		+ "itth.id; "
)

@SqlResultSetMapping(
	name = "getInputTransferHallReportWithItemsResult",
	classes = @ConstructorResult (
		targetClass = DateWiseInputTransferHallReportDto.class,
		columns = {
			@ColumnResult(name = "hallName", type = String.class),
			@ColumnResult(name = "inputTransferHallId", type = Long.class),
			@ColumnResult(name = "transferDate", type = LocalDateTime.class),
			@ColumnResult(name = "rawMaterialName", type = String.class),
			@ColumnResult(name = "finalQty", type = Double.class),
			@ColumnResult(name = "finalQuantityMeasurement", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
		}
	)
)

@NamedNativeQuery(
	name = "getDateWiseInputTransferHallReportWithoutitems",
	resultSetMapping = "getInputTransferHallReportWithoutItemsResult",
	query="SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND hm.name_prefer_lang IS NOT NULL AND hm.name_prefer_lang != '' THEN hm.name_prefer_lang "
		+ "WHEN :langType = 2 AND hm.name_supportive_lang IS NOT NULL AND hm.name_supportive_lang != '' THEN hm.name_supportive_lang "
		+ "ELSE hm.name_default_lang "
		+ "END AS hallName, "
		+ "_itth.id AS inputTransferHallId, "
		+ "_itth.transfer_date AS transferDate, "
		+ "(SELECT "
		+ "GROUP_CONCAT( "
		+ "CONCAT( "
		+ "FORMAT(weight, IF(me.id IN(1,3) AND me.decimal_limit_qty = -1, IF(weight % 1 != 0, 3, 0), me.decimal_limit_qty)), "
		+ "' ', "
		+ "CASE "
		+ "WHEN :langType = 1 AND me.symbol_prefer_lang IS NOT NULL AND me.symbol_prefer_lang != '' THEN me.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND me.symbol_supportive_lang IS NOT NULL AND me.symbol_supportive_lang != '' THEN me.symbol_supportive_lang "
		+ "ELSE me.symbol_default_lang "
		+ "END "
		+ ") SEPARATOR ', ' "
		+ ") AS nameDefaultLang "
		+ "FROM ( "
		+ "SELECT "
		+ "t.fk_input_transfer_to_hall_id, "
		+ "CASE "
		+ "WHEN SUM(t.total_weight) < 1 AND t.unit IN(1,3) THEN SUM(t.total_weight) * 1000 "
		+ "ELSE SUM(t.total_weight) "
		+ "END AS weight, "
		+ "CASE "
		+ "WHEN SUM(t.total_weight) < 1 AND t.unit IN(1,3) THEN t.unit + 1 "
		+ "ELSE t.unit "
		+ "END AS unit "
		+ "FROM "
		+ "(SELECT "
		+ "fk_input_transfer_to_hall_id, "
		+ "CASE "
		+ "WHEN fk_measurement_id = 2 THEN SUM(weight) / 1000 "
		+ "WHEN fk_measurement_id = 4 THEN SUM(weight) / 1000 "
		+ "WHEN m.is_base_unit = 0 THEN SUM(weight) * m.base_unit_equivalent "
		+ "ELSE SUM(weight) "
		+ "END AS total_weight, "
		+ "CASE "
		+ "WHEN fk_measurement_id = 2 THEN 1 "
		+ "WHEN fk_measurement_id = 4 THEN 3 "
		+ "WHEN m.is_base_unit = 0 THEN m.base_unit_id "
		+ "ELSE (SELECT id FROM measurement WHERE id = itthrm.fk_measurement_id) "
		+ "END AS unit "
		+ "FROM input_transfer_to_hall_raw_material itthrm "
		+ "INNER JOIN input_transfer_to_hall itth ON itth.id = itthrm.fk_input_transfer_to_hall_id "
		+ "LEFT JOIN measurement m ON m.id = itthrm.fk_measurement_id "
		+ "WHERE itth.id = _itth.id AND (0 = :rawMaterialId OR itthrm.fk_raw_material_id = :rawMaterialId) "
		+ "GROUP BY "
		+ "fk_input_transfer_to_hall_id, fk_measurement_id) AS t "
		+ "WHERE total_weight != 0 "
		+ "GROUP BY t.fk_input_transfer_to_hall_id, t.unit "
		+ ") AS concatedStringForTotalQty "
		+ "LEFT JOIN measurement me ON me.id = unit "
		+ "GROUP BY fk_input_transfer_to_hall_id) AS totalQty "
		+ "FROM input_transfer_to_hall _itth "
		+ "INNER JOIN hall_master hm ON hm.id = _itth.fk_hall_id "
		+ "WHERE (_itth.transfer_date >= :startDate OR :startDate IS NULL) AND (_itth.transfer_date <= :endDate OR :endDate IS NULL) "
		+ "AND (0 = :hallId OR _itth.fk_hall_id = :hallId) "
		+ "ORDER BY "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN hallName "
		+ "WHEN :orderById = 2 THEN transferDate "
		+ "WHEN :orderById = 3 THEN inputTransferHallId "
		+ "END ASC, "
		+ "_itth.id "
)

@SqlResultSetMapping(
	name = "getInputTransferHallReportWithoutItemsResult",
	classes = @ConstructorResult (
		targetClass = DateWiseInputTransferHallReportDto.class,
		columns = {
			@ColumnResult(name = "hallName", type = String.class),
			@ColumnResult(name = "inputTransferHallId", type = Long.class),
			@ColumnResult(name = "transferDate", type = LocalDateTime.class),
			@ColumnResult(name = "totalQty", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getSuppilerContactForDebitNoteDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM debit_note dn "
		+ "INNER JOIN contact c ON c.id = dn.fk_contact_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "INNER JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE (cc.fk_contact_category_type_id = :contactCategoryTypeId) AND c.is_active = TRUE "
		+ "AND (dn.bill_date >= :startDate OR :startDate IS NULL) AND (dn.bill_date <= :endDate OR :endDate IS NULL) "
		+ "ORDER BY dn.id; "
)

@NamedNativeQuery(
	name = "getRawMaterialForDebitNoteDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "rm.id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM debit_note_raw_material dnrm "
		+ "LEFT JOIN raw_material rm ON rm.id = dnrm.fk_raw_material_id "
		+ "LEFT JOIN debit_note dn ON dn.id = dnrm.fk_debit_note_id "
		+ "LEFT JOIN contact c ON c.id = dn.fk_contact_id "
		+ "WHERE (0 = :supplierCategoryId OR dn.fk_contact_id = :supplierCategoryId) AND (c.is_active = TRUE) AND (dn.bill_date >= :startDate OR :startDate IS NULL) AND (dn.bill_date <= :endDate OR :endDate IS NULL); "
)

@NamedNativeQuery(
	name = "getDateWiseDebitNoteReportWithitems",
	resultSetMapping = "getReportWithItemsResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "dn.id AS purchaseOrderId, "
		+ "dn.bill_date AS purchaseOrderDate, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "dnrm.weight AS finalQty, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS finalQuantityMeasurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND dnrm.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND dnrm.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "dnrm.price AS rate, "
		+ "dnrm.weight * dnrm.price AS amt, "
		+ "dnrm.total_amount AS netAmt, "
		+ "SUM(dnrm.weight * dnrm.price) OVER (PARTITION BY dn.id) AS suppilerContactSubTotal, "
		+ "SUM(dnrm.total_amount) OVER (PARTITION BY dn.id) AS suppilerContactTotal, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM debit_note dn "
		+ "INNER JOIN contact c ON c.id = dn.fk_contact_id "
		+ "INNER JOIN debit_note_raw_material dnrm ON dnrm.fk_debit_note_id = dn.id "
		+ "INNER JOIN raw_material rm ON rm.id = dnrm.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = dnrm.fk_measurement_id "
		+ "WHERE (dn.bill_date >= :startDate OR :startDate IS NULL) AND (dn.bill_date <= :endDate OR :endDate IS NULL) AND (0 = :supplierContactId OR dn.fk_contact_id = :supplierContactId) AND (0 = :rawMaterialId OR dnrm.fk_raw_material_id = :rawMaterialId) "
		+ "ORDER BY "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN c.name_default_lang "
		+ "WHEN :orderById = 2 THEN dn.bill_date "
		+ "WHEN :orderById = 3 THEN dn.id "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 4 THEN suppilerContactTotal "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 5 THEN suppilerContactTotal "
		+ "END DESC, "
		+ "dn.id; "
)

@NamedNativeQuery(
	name = "getDateWiseDebitNoteReportWithoutitems",
	resultSetMapping = "getReportWithoutItemsResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "dn.id AS purchaseOrderId, "
		+ "dn.bill_date AS purchaseOrderDate, "
		+ "SUM(dnrm.weight * dnrm.price) AS suppilerContactSubTotal, "
		+ "SUM(dnrm.total_amount) AS suppilerContactTotal, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM debit_note dn "
		+ "INNER JOIN contact c ON c.id = dn.fk_contact_id "
		+ "INNER JOIN debit_note_raw_material dnrm ON dnrm.fk_debit_note_id = dn.id "
		+ "WHERE (dn.bill_date >= :startDate OR :startDate IS NULL) AND (dn.bill_date <= :endDate OR :endDate IS NULL) "
		+ "AND (0 = :supplierContactId OR dn.fk_contact_id = :supplierContactId) "
		+ "AND (0 = :rawMaterialId OR dnrm.fk_raw_material_id = :rawMaterialId) "
		+ "GROUP BY dn.id "
		+ "ORDER BY "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN c.name_default_lang "
		+ "WHEN :orderById = 2 THEN dn.bill_date "
		+ "WHEN :orderById = 3 THEN dn.id "
		+ "END, "
		+ "CASE "
		+ "WHEN :orderById = 4 THEN suppilerContactTotal "
		+ "END ASC, "
		+ "CASE "
		+ "WHEN :orderById = 5 THEN suppilerContactTotal "
		+ "END DESC, "
		+ "dn.id;"
)

@NamedNativeQuery(
	name = "getRawMaterialReturnToHallDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "hm.id, "
		+ "hm.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN hm.name_prefer_lang IS NOT NULL AND hm.name_prefer_lang != '' THEN hm.name_prefer_lang "
		+ "ELSE hm.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN hm.name_supportive_lang IS NOT NULL AND hm.name_supportive_lang != '' THEN hm.name_supportive_lang "
		+ "ELSE hm.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM raw_material_return_to_hall rmrth  "
		+ "INNER JOIN hall_master hm ON hm.id = rmrth.fk_hall_id "
		+ "WHERE (hm.is_active = TRUE) AND (rmrth.return_date >= :startDate OR :startDate IS NULL) AND (rmrth.return_date <= :endDate OR :endDate IS NULL) "
		+ "ORDER BY hm.id;"
)

@NamedNativeQuery(
	name = "getRawMaterialForRawMaterialReturnToHallDropDown",
	resultSetMapping = "getDropDownDataResultPurchaseOrder",
	query = "SELECT DISTINCT "
		+ "rm.id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "CASE  "
		+ "WHEN rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang  "
		+ "ELSE rm.name_default_lang  "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang  "
		+ "ELSE rm.name_default_lang  "
		+ "END AS nameSupportiveLang "
		+ "FROM raw_material_return_to_hall_details rmrthd  "
		+ "INNER JOIN raw_material rm ON rm.id = rmrthd.fk_raw_material_id  "
		+ "INNER JOIN raw_material_return_to_hall rmrth ON rmrth.id = rmrthd.fk_raw_material_return_to_hall_id  "
		+ "INNER JOIN hall_master hm ON hm.id = rmrth.fk_hall_id  "
		+ "WHERE (0 = :hallId OR rmrth.fk_hall_id = :hallId) AND (hm.is_active = TRUE) "
		+ "AND (rmrth.return_date >= :startDate OR :startDate IS NULL) AND (rmrth.return_date <= :endDate OR :endDate IS NULL)"
)

@NamedNativeQuery(
	name = "getDateWiseRawMaterialReturnHallReportWithitems",
	resultSetMapping = "getInputTransferHallReportWithItemsResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND hm.name_prefer_lang IS NOT NULL AND hm.name_prefer_lang != '' THEN hm.name_prefer_lang  "
		+ "WHEN :langType = 2 AND hm.name_supportive_lang IS NOT NULL AND hm.name_supportive_lang != '' THEN hm.name_supportive_lang "
		+ "ELSE hm.name_default_lang  "
		+ "END AS hallName, "
		+ "rmrth.id AS inputTransferHallId, "
		+ "rmrth.return_date AS transferDate, "
		+ "CASE  "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "rmrthd.weight AS finalQty, "
		+ "CASE  "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS finalQuantityMeasurement, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND rmrthd.weight % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND rmrthd.weight % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM raw_material_return_to_hall rmrth  "
		+ "INNER JOIN hall_master hm ON hm.id = rmrth.fk_hall_id  "
		+ "INNER JOIN raw_material_return_to_hall_details rmrthd ON rmrthd.fk_raw_material_return_to_hall_id = rmrth.id "
		+ "INNER JOIN raw_material rm ON rm.id = rmrthd.fk_raw_material_id "
		+ "INNER JOIN measurement m ON m.id = rmrthd.fk_measurement_id "
		+ "WHERE (rmrth.return_date >= :startDate OR :startDate IS NULL) AND (rmrth.return_date <= :endDate OR :endDate IS NULL) "
		+ "AND (0 = :hallId OR rmrth.fk_hall_id = :hallId) "
		+ "AND (0 = :rawMaterialId OR rmrthd.fk_raw_material_id = :rawMaterialId) "
		+ "ORDER BY "
		+ "CASE  "
		+ "WHEN :orderById = 1 THEN hm.name_default_lang  "
		+ "WHEN :orderById = 2 THEN rmrth.return_date  "
		+ "WHEN :orderById = 3 THEN rmrth.id "
		+ "END ASC, "
		+ "rmrth.id;"
)

@NamedNativeQuery(
	name = "getDateWiseRawMaterialReturnHallReportWithoutitems",
	resultSetMapping = "getInputTransferHallReportWithoutItemsResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND hm.name_prefer_lang IS NOT NULL AND hm.name_prefer_lang != '' THEN hm.name_prefer_lang "
		+ "WHEN :langType = 2 AND hm.name_supportive_lang IS NOT NULL AND hm.name_supportive_lang != '' THEN hm.name_supportive_lang "
		+ "ELSE hm.name_default_lang "
		+ "END AS hallName, "
		+ "_rmrth.id AS inputTransferHallId, "
		+ "_rmrth.return_date AS transferDate, "
		+ "(SELECT "
		+ "GROUP_CONCAT( "
		+ "CONCAT( "
		+ "FORMAT(weight, IF(me.id IN(1,3) AND me.decimal_limit_qty = -1, IF(weight % 1 != 0, 3, 0), me.decimal_limit_qty)), "
		+ "' ', "
		+ "CASE "
		+ "WHEN :langType = 1 AND me.symbol_prefer_lang IS NOT NULL AND me.symbol_prefer_lang != '' THEN me.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND me.symbol_supportive_lang IS NOT NULL AND me.symbol_supportive_lang != '' THEN me.symbol_supportive_lang "
		+ "ELSE me.symbol_default_lang "
		+ "END "
		+ ") SEPARATOR ', ' "
		+ ") AS nameDefaultLang "
		+ "FROM ( "
		+ "SELECT "
		+ "t.fk_raw_material_return_to_hall_id, "
		+ "CASE "
		+ "WHEN SUM(t.total_weight) < 1 AND t.unit IN(1,3) THEN SUM(t.total_weight) * 1000 "
		+ "ELSE SUM(t.total_weight) "
		+ "END AS weight, "
		+ "CASE "
		+ "WHEN SUM(t.total_weight) < 1 AND t.unit IN(1,3) THEN t.unit + 1 "
		+ "ELSE t.unit "
		+ "END AS unit "
		+ "FROM "
		+ "(SELECT "
		+ "fk_raw_material_return_to_hall_id, "
		+ "CASE "
		+ "WHEN fk_measurement_id = 2 THEN SUM(weight) / 1000 "
		+ "WHEN fk_measurement_id = 4 THEN SUM(weight) / 1000 "
		+ "WHEN m.is_base_unit = 0 THEN SUM(weight) * m.base_unit_equivalent "
		+ "ELSE SUM(weight) "
		+ "END AS total_weight, "
		+ "CASE "
		+ "WHEN fk_measurement_id = 2 THEN 1 "
		+ "WHEN fk_measurement_id = 4 THEN 3 "
		+ "WHEN m.is_base_unit = 0 THEN m.base_unit_id "
		+ "ELSE (SELECT id FROM measurement WHERE id = rmrthd.fk_measurement_id) "
		+ "END AS unit "
		+ "FROM raw_material_return_to_hall_details rmrthd  "
		+ "INNER JOIN raw_material_return_to_hall rmrth ON rmrth.id = rmrthd.fk_raw_material_return_to_hall_id "
		+ "LEFT JOIN measurement m ON m.id = rmrthd.fk_measurement_id "
		+ "WHERE rmrth.id = _rmrth.id AND (0 = :rawMaterialId OR rmrthd.fk_raw_material_id = :rawMaterialId) "
		+ "GROUP BY "
		+ "fk_raw_material_return_to_hall_id, fk_measurement_id) AS t "
		+ "WHERE total_weight != 0 "
		+ "GROUP BY t.fk_raw_material_return_to_hall_id, t.unit "
		+ ") AS concatedStringForTotalQty "
		+ "LEFT JOIN measurement me ON me.id = unit "
		+ "GROUP BY fk_raw_material_return_to_hall_id) AS totalQty "
		+ "FROM raw_material_return_to_hall _rmrth "
		+ "INNER JOIN hall_master hm ON hm.id = _rmrth.fk_hall_id "
		+ "WHERE (_rmrth.return_date >= :startDate OR :startDate IS NULL) AND (_rmrth.return_date <= :endDate OR :endDate IS NULL) "
		+ "AND (0 = :hallId OR _rmrth.fk_hall_id = :hallId) "
		+ "ORDER BY "
		+ "CASE "
		+ "WHEN :orderById = 1 THEN hallName "
		+ "WHEN :orderById = 2 THEN transferDate "
		+ "WHEN :orderById = 3 THEN inputTransferHallId "
		+ "END ASC, "
		+ "_rmrth.id"
)

@Entity
public class TransactionReportNativeQuery extends AuditIdModelOnly {
}