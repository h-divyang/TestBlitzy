package com.catering.dao.accounting_reports.account_report;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import com.catering.dto.tenant.request.AccountBankBookReportDto;
import com.catering.dto.tenant.request.AccountCashBookReportDto;
import com.catering.dto.tenant.request.AccountCollectionReportDto;
import com.catering.dto.tenant.request.AccountDailyActivityReportDto;
import com.catering.dto.tenant.request.AccountGroupSummaryReportDto;
import com.catering.dto.tenant.request.DateWiseGeneralLedgerReportDto;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.GeneralLedgerContactDropDownDto;
import com.catering.dto.tenant.request.GstSalesPurchaseReportCommonDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing the result of a native query for account-related reports.
 * This class is used to map the result set of a native SQL query to Java objects.
 * The fields in this class are mapped to columns in the SQL result set and represent various
 * financial details such as CGST, SGST, IGST, total amount, and the decimal limit for currency formatting.
 * 
 * The annotations map the result of the query to the properties of this entity, where each 
 * result column corresponds to a field in the entity. This allows the report data to be accessed 
 * easily as a structured Java object.
 */
@NamedNativeQuery(
	name = "getContactCategoryForDropDown",
	resultSetMapping = "getDropDownDataResultSet",
	query = "SELECT DISTINCT "
		+ "cc.id AS id, "
		+ "cc.name_default_lang AS nameDefaultLang, "
		+ "cc.name_prefer_lang AS namePreferLang, "
		+ "cc.name_supportive_lang AS nameSupportiveLang "
		+ "FROM account_balance ab "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = ab.fk_contact_id "
		+ "INNER JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "ORDER BY cc.priority ASC, id "
)

@NamedNativeQuery(
	name = "getContactForDropDown",
	resultSetMapping = "getDropDownDataResultSet",
	query = "SELECT DISTINCT "
		+ "ab.fk_contact_id AS id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang "
		+ "FROM account_balance ab "
		+ "INNER JOIN contact c ON c.id = ab.fk_contact_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "WHERE(0= :contactCategoryId or contactCategories.fk_contact_category_id= :contactCategoryId) "
		+ "ORDER BY id "
)

@SqlResultSetMapping(
	name = "getDropDownDataResultSet",
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
	name = "getCollectionReport",
	resultSetMapping = "getCollectionReportResult",
	query = "SELECT DISTINCT "
		+ "c.id AS contactId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS name, "
		+ "ab.credit, "
		+ "ab.debit, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitCurrency "
		+ "FROM account_balance ab "
		+ "INNER JOIN contact c ON c.id = ab.fk_contact_id "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "INNER JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE (0 = :contactCategoryId OR contactCategories.fk_contact_category_id = :contactCategoryId) "
		+ "AND (0 = :contactId OR c.id = :contactId) "
		+ "ORDER BY cc.is_non_updatable DESC;"
)

@SqlResultSetMapping(
	name = "getCollectionReportResult",
	classes = @ConstructorResult(
		targetClass = AccountCollectionReportDto.class,
		columns = {
			@ColumnResult(name = "contactId", type = Long.class),
			@ColumnResult(name = "name", type = String.class),
			@ColumnResult(name = "credit", type = Double.class),
			@ColumnResult(name = "debit", type = Double.class),
			@ColumnResult(name = "decimalLimitCurrency", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getGeneralLedgerSuppilerContactForDropDown",
	resultSetMapping = "getGeneralLedgerSuppilerContactForDropDownResult",
	query = "SELECT DISTINCT "
		+ "c.id AS id, "
		+ "c.name_default_lang AS nameDefaultlang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.lock_date AS lockDate "
		+ "FROM account_history ah "
		+ "LEFT JOIN contact c ON ah.fk_contact_from_id = c.id "
		+ "WHERE c.is_active = TRUE"
)

@SqlResultSetMapping(
	name = "getGeneralLedgerSuppilerContactForDropDownResult",
	classes = @ConstructorResult(
		targetClass = GeneralLedgerContactDropDownDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "lockDate", type = LocalDate.class)
		}
	)
)

@NamedNativeQuery(
	name = "getSuppilerContactNameForGeneralLedgerReport",
	resultSetMapping = "contactNameForGeneralLedgerReportResult",
	query = "SELECT DISTINCT "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName "
		+ "FROM contact c "
		+ "WHERE c.id = :supplierCategoryId "
)

@SqlResultSetMapping(
	name = "contactNameForGeneralLedgerReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseGeneralLedgerReportDto.class,
		columns = {
			@ColumnResult(name = "contactName", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getGeneralLedgerReportData",
	resultSetMapping = "getGeneralLedgerReportResult",
	query = "WITH PastBalanceTransaction AS ( "
		+ "SELECT "
		+ "c.id AS contactId, "
		+ "1 AS seqNo, "
		+ "0 AS id, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ ":startDate AS transactionDate, "
		+ "NULL AS voucherNumber, "
		+ "'OPB' AS voucherType, "
		+ "0 AS debit, "
		+ "0 AS credit, "
		+ "COALESCE(SUM( "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN ah.amount "
		+ "WHEN ah.transaction_type = 1 THEN -ah.amount "
		+ "ELSE 0 "
		+ "END "
		+ "), 0) AS balance, "
		+ "NULL AS remark "
		+ "FROM account_history ah "
		+ "LEFT JOIN contact c ON ah.fk_contact_from_id = c.id "
		+ "WHERE (0 =:supplierCategoryId OR ah.fk_contact_from_id = :supplierCategoryId) AND (DATE(ah.transaction_date) < :startDate) "
		+ "GROUP BY ah.fk_contact_from_id "
		+ "ORDER BY c.id "
		+ "), "
		+ "DateWiseTransaction AS ( "
		+ "SELECT "
		+ "c.id AS contactId, "
		+ "2 AS seqNo, "
		+ "ah.id AS id, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "DATE(ah.transaction_date) AS transactionDate, "
		+ "CASE "
		+ "WHEN ah.voucher_type = 'CP' OR ah.voucher_type = 'CR' THEN cprd.fk_cash_payment_receipt_id "
		+ "WHEN ah.voucher_type = 'BP' OR ah.voucher_type = 'BR' THEN bprd.fk_bank_payment_receipt_id "
		+ "ELSE ah.voucher_number "
		+ "END AS voucherNumber, "
		+ "ah.voucher_type AS voucherType, "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN ah.amount "
		+ "ELSE 0 "
		+ "END AS debit, "
		+ "CASE "
		+ "WHEN ah.transaction_type = 1 THEN ah.amount "
		+ "ELSE 0 "
		+ "END AS credit, "
		+ "SUM( "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN ah.amount "
		+ "WHEN ah.transaction_type = 1 THEN -ah.amount "
		+ "ELSE 0 "
		+ "END "
		+ ") OVER (PARTITION BY ah.fk_contact_from_id ORDER BY DATE(ah.transaction_date), ah.id) AS balance, "
		+ "ah.remark AS remark "
		+ "FROM account_history ah "
		+ "LEFT JOIN bank_payment_receipt_details bprd ON bprd.id = ah.voucher_number "
		+ "LEFT JOIN cash_payment_receipt_details cprd ON cprd.id = ah.voucher_number "
		+ "LEFT JOIN contact c ON ah.fk_contact_from_id = c.id "
		+ "WHERE ah.amount != 0 AND (0 =:supplierCategoryId OR ah.fk_contact_from_id = :supplierCategoryId) AND ((DATE(ah.transaction_date) BETWEEN :startDate AND :endDate) OR :startDate IS NULL OR :endDate IS NULL) "
		+ "ORDER BY c.id,transactionDate "
		+ "), "
		+ "ClosingBalance AS ( "
		+ "SELECT "
		+ "c.id AS contactId, "
		+ "3 AS seqNo, "
		+ "0 AS id, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "NULL AS transactionDate, "
		+ "0 AS voucherNumber, "
		+ "'CLB' AS voucherType, "
		+ "0 AS debit, "
		+ "0 AS credit, "
		+ "COALESCE(SUM( "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN ah.amount "
		+ "WHEN ah.transaction_type = 1 THEN -ah.amount "
		+ "ELSE 0 "
		+ "END "
		+ "), 0) AS balance, "
		+ "NULL AS remark "
		+ "FROM account_history ah "
		+ "LEFT JOIN contact c ON ah.fk_contact_from_id = c.id "
		+ "WHERE (0 =:supplierCategoryId OR ah.fk_contact_from_id = :supplierCategoryId) AND (DATE(ah.transaction_date) <= :endDate OR :endDate IS NULL) "
		+ "GROUP BY ah.fk_contact_from_id "
		+ "ORDER BY c.id "
		+ ") "
		+ "SELECT "
		+ "contactId, "
		+ "contactName, "
		+ "transactionDate, "
		+ "voucherNumber, "
		+ "voucherType, "
		+ "debit, "
		+ "credit, "
		+ "balance, "
		+ "remark, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM ( "
		+ "SELECT "
		+ "pbt.contactId, "
		+ "pbt.seqNo, "
		+ "pbt.id, "
		+ "pbt.contactName, "
		+ "pbt.transactionDate, "
		+ "pbt.voucherNumber, "
		+ "pbt.voucherType, "
		+ "CASE "
		+ "WHEN pbt.balance >= 0 THEN pbt.balance "
		+ "ELSE 0 "
		+ "END AS debit, "
		+ "CASE "
		+ "WHEN pbt.balance < 0 THEN ABS(pbt.balance) "
		+ "ELSE 0 "
		+ "END AS credit, "
		+ "pbt.balance, "
		+ "pbt.remark "
		+ "FROM PastBalanceTransaction pbt "
		+ "UNION ALL "
		+ "SELECT "
		+ "dwt.contactId, "
		+ "dwt.seqNo, "
		+ "dwt.id, "
		+ "dwt.contactName, "
		+ "dwt.transactionDate, "
		+ "dwt.voucherNumber, "
		+ "dwt.voucherType, "
		+ "dwt.debit, "
		+ "dwt.credit, "
		+ "COALESCE(pbt.balance, 0) + dwt.balance AS balance, "
		+ "dwt.remark "
		+ "FROM DateWiseTransaction dwt "
		+ "LEFT JOIN PastBalanceTransaction pbt ON pbt.contactId = dwt.contactId "
		+ "UNION ALL "
		+ "SELECT "
		+ "cb.contactId, "
		+ "cb.seqNo, "
		+ "cb.id, "
		+ "cb.contactName, "
		+ "cb.transactionDate, "
		+ "cb.voucherNumber, "
		+ "cb.voucherType, "
		+ "cb.debit, "
		+ "cb.credit, "
		+ "IF(cb.balance = 0, ABS(cb.balance), cb.balance), "
		+ "cb.remark "
		+ "FROM ClosingBalance cb "
		+ ") AS CombinedUnions "
		+ "ORDER BY contactId, seqNo, transactionDate, id"
)

@SqlResultSetMapping(
	name = "getGeneralLedgerReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseGeneralLedgerReportDto.class,
		columns = {
			@ColumnResult(name = "contactId", type = Long.class),
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "transactionDate", type = LocalDateTime.class),
			@ColumnResult(name = "voucherNumber", type = Long.class),
			@ColumnResult(name = "voucherType", type = String.class),
			@ColumnResult(name = "debit", type = Double.class),
			@ColumnResult(name = "credit", type = Double.class),
			@ColumnResult(name = "balance", type = Double.class),
			@ColumnResult(name = "remark", type = String.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getGroupSummaryReport",
	resultSetMapping = "getGroupSummaryReportResult",
	query = "SELECT DISTINCT "
		+ "c.id AS contactId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS name, "
		+ "ab.opening_balance AS openingBalance, "
		+ "ab.credit AS credit, "
		+ "ab.debit AS debit, "
		+ "ab.balance AS balance, "
		+ "SUM(ab.opening_balance) OVER () AS totalOpeningBalance, "
		+ "SUM(ab.credit) OVER () AS totalCredit, "
		+ "SUM(ab.debit) OVER () AS totalDebit, "
		+ "SUM(ab.balance) OVER () AS totalBalance, "
		+ "cs.decimal_limit_for_currency AS decimalLimitCurrency "
		+ "FROM account_balance ab "
		+ "LEFT JOIN contact c ON c.id = ab.fk_contact_id "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "JOIN company_setting cs "
		+ "WHERE IF(0 = :contactCategoryId, TRUE, cc.id = :contactCategoryId) "
		+ "ORDER BY cc.is_non_updatable DESC;"
)

@SqlResultSetMapping(
	name = "getGroupSummaryReportResult",
	classes = @ConstructorResult(
		targetClass = AccountGroupSummaryReportDto.class,
		columns = {
			@ColumnResult(name = "contactId", type = Long.class),
			@ColumnResult(name = "name", type = String.class),
			@ColumnResult(name = "openingBalance", type = Double.class),
			@ColumnResult(name = "credit", type = Double.class),
			@ColumnResult(name = "debit", type = Double.class),
			@ColumnResult(name = "balance", type = Double.class),
			@ColumnResult(name = "totalOpeningBalance", type = Double.class),
			@ColumnResult(name = "totalCredit", type = Double.class),
			@ColumnResult(name = "totalDebit", type = Double.class),
			@ColumnResult(name = "totalBalance", type = Double.class),
			@ColumnResult(name = "decimalLimitCurrency", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getCashBookReport",
	resultSetMapping = "getCashBookReportResult",
	query = "WITH PastBalanceCalculation AS ( "
		+ "SELECT "
		+ "COALESCE(SUM(CASE "
		+ "WHEN ah.transaction_type = 0 THEN -(ah.amount) "
		+ "ELSE ah.amount "
		+ "END), 0) AS pastBalance "
		+ "FROM account_history ah "
		+ "WHERE (ah.fk_contact_from_id IN (SELECT id FROM contact c1 WHERE c1.is_cash = TRUE AND c1.is_active = TRUE)) AND (DATE(ah.transaction_date) < :startDate) "
		+ ") "
		+ "SELECT  "
		+ "t.pastBalance, "
		+ "t.transactionDate, "
		+ "t.voucherNumber, "
		+ "t.voucherType, "
		+ "t.description, "
		+ "t.debit, "
		+ "t.credit, "
		+ "CASE  "
		+ "WHEN t.seqNo > 1 THEN LAG(t.closingBalance) OVER (ORDER BY t.transactionDate) "
		+ "ELSE NULL  "
		+ "END AS openingBalance, "
		+ "t.closingBalance, "
		+ "t.decimalLimitForCurrency "
		+ "FROM ( "
		+ "SELECT "
		+ "DENSE_RANK() OVER(ORDER BY DATE(ah.transaction_date)) AS seqNo, "
		+ "pbc.pastBalance AS pastBalance, "
		+ "DATE(ah.transaction_date) AS transactionDate, "
		+ "CASE "
		+ "WHEN ah.voucher_type = 'CP' OR ah.voucher_type = 'CR' THEN cprd.fk_cash_payment_receipt_id "
		+ "ELSE ah.voucher_number "
		+ "END AS voucherNumber, "
		+ "ah.voucher_type AS voucherType, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS description, "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN ah.amount "
		+ "ELSE NULL "
		+ "END AS debit, "
		+ "CASE "
		+ "WHEN ah.transaction_type = 1 THEN ah.amount "
		+ "ELSE NULL "
		+ "END AS credit, "
		+ "pbc.pastBalance + SUM( "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN -(ah.amount) "
		+ "WHEN ah.transaction_type = 1 THEN ah.amount "
		+ "ELSE 0 "
		+ "END "
		+ ") OVER (ORDER BY DATE(ah.transaction_date)) AS closingBalance, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM account_history ah "
		+ "LEFT JOIN contact c ON c.id = ah.fk_contact_to_id "
		+ "LEFT JOIN PastBalanceCalculation pbc ON 1=1 "
		+ "LEFT JOIN cash_payment_receipt_details cprd ON cprd.id = ah.voucher_number "
		+ "WHERE ah.amount != 0 AND (ah.fk_contact_from_id IN (SELECT id FROM contact c1 WHERE c1.is_cash = TRUE AND c1.is_active = TRUE)) AND (DATE(ah.transaction_date) BETWEEN :startDate AND :endDate) "
		+ "ORDER BY transactionDate ) AS t;"
)

@SqlResultSetMapping(
	name = "getCashBookReportResult",
	classes = @ConstructorResult(
		targetClass = AccountCashBookReportDto.class,
		columns = {
			@ColumnResult(name = "pastBalance", type = Double.class),
			@ColumnResult(name = "transactionDate", type = LocalDateTime.class),
			@ColumnResult(name = "voucherNumber", type = Long.class),
			@ColumnResult(name = "voucherType", type = String.class),
			@ColumnResult(name = "description", type = String.class),
			@ColumnResult(name = "debit", type = Double.class),
			@ColumnResult(name = "credit", type = Double.class),
			@ColumnResult(name = "openingBalance", type = Double.class),
			@ColumnResult(name = "closingBalance", type = Double.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getBankBookBankContactForDropDown",
	resultSetMapping = "getGeneralLedgerSuppilerContactForDropDownResult",
	query = "SELECT DISTINCT "
		+ "c.id AS id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.lock_date AS lockDate "
		+ "FROM account_history ah "
		+ "LEFT JOIN contact c ON c.id = ah.fk_contact_from_id "
		+ "WHERE ah.fk_contact_from_id IN ( "
		+ "SELECT "
		+ "c.id "
		+ "FROM contact c "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE cc.is_non_updatable = TRUE AND c.is_cash = 2 AND c.is_active = TRUE) "
)

@NamedNativeQuery(
	name = "getBankBookReportData",
	resultSetMapping = "getBankBookReportResult",
	query = "WITH PastBalanceCalculation AS ( "
		+ "SELECT "
		+ "COALESCE(SUM( "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN -(ah.amount) "
		+ "ELSE ah.amount "
		+ "END), 0) AS pastBalance "
		+ "FROM account_history ah "
		+ "WHERE (ah.fk_contact_from_id = :bankContactId) "
		+ "AND (DATE(ah.transaction_date) < :startDate) "
		+ ") "
		+ "SELECT "
		+ "t.pastBalance, "
		+ "t.transactionDate, "
		+ "t.voucherNumber, "
		+ "t.voucherType, "
		+ "t.description, "
		+ "t.debit, "
		+ "t.credit, "
		+ "t.paymentMode, "
		+ "t.paymentNumber, "
		+ "t.paymentDate, "
		+ "CASE "
		+ "WHEN t.seqNo > 1 THEN LAG(t.closingBalance) OVER (ORDER BY t.transactionDate) "
		+ "ELSE NULL "
		+ "END AS openingBalance, "
		+ "t.closingBalance, "
		+ "t.decimalLimitForCurrency "
		+ "FROM ( "
		+ "SELECT "
		+ "DENSE_RANK() OVER (ORDER BY DATE(ah.transaction_date)) AS seqNo, "
		+ "pbc.pastBalance AS pastBalance, "
		+ "DATE(ah.transaction_date) AS transactionDate, "
		+ "bpr.id AS voucherNumber, "
		+ "ah.voucher_type AS voucherType, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS description, "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN ah.amount "
		+ "ELSE NULL "
		+ "END AS debit, "
		+ "CASE "
		+ "WHEN ah.transaction_type = 1 THEN ah.amount "
		+ "ELSE NULL "
		+ "END AS credit, "
		+ "bprd.payment_mode AS paymentMode, "
		+ "bprd.cheque_transaction_number AS paymentNumber, "
		+ "bprd.cheque_transaction_date AS paymentDate, "
		+ "pbc.pastBalance + SUM( "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN -(ah.amount) "
		+ "WHEN ah.transaction_type = 1 THEN ah.amount "
		+ "ELSE 0 "
		+ "END "
		+ ") OVER (ORDER BY DATE(ah.transaction_date)) AS closingBalance, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM account_history ah "
		+ "LEFT JOIN contact c ON c.id = ah.fk_contact_to_id "
		+ "LEFT JOIN PastBalanceCalculation pbc ON 1=1 "
		+ "LEFT JOIN bank_payment_receipt_details bprd ON bprd.id = ah.voucher_number AND bprd.fk_contact_id = ah.fk_contact_to_id "
		+ "LEFT JOIN bank_payment_receipt bpr ON bpr.id = bprd.fk_bank_payment_receipt_id "
		+ "WHERE ah.amount != 0 AND ah.fk_contact_from_id = :bankContactId "
		+ "AND (DATE(ah.transaction_date) BETWEEN :startDate AND :endDate) "
		+ "ORDER BY transactionDate ) AS t "
)

@SqlResultSetMapping(
	name = "getBankBookReportResult",
	classes = @ConstructorResult(
		targetClass = AccountBankBookReportDto.class,
		columns = {
			@ColumnResult(name = "pastBalance", type = Double.class),
			@ColumnResult(name = "transactionDate", type = LocalDateTime.class),
			@ColumnResult(name = "voucherNumber", type = Long.class),
			@ColumnResult(name = "voucherType", type = String.class),
			@ColumnResult(name = "description", type = String.class),
			@ColumnResult(name = "debit", type = Double.class),
			@ColumnResult(name = "credit", type = Double.class),
			@ColumnResult(name = "paymentMode", type = Integer.class),
			@ColumnResult(name = "paymentNumber", type = Long.class),
			@ColumnResult(name = "paymentDate", type = LocalDateTime.class),
			@ColumnResult(name = "openingBalance", type = Double.class),
			@ColumnResult(name = "closingBalance", type = Double.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getDailyActivityStockDetailsReport",
	resultSetMapping = "getDailyActivityStockReportResult",
	query = "WITH PastBalanceCalculation AS ( "
		+ "SELECT DISTINCT "
		+ "rm.id AS materialId, "
		+ "1 AS seqNo, "
		+ "0 AS id, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS contactName, "
		+ ":startDate AS transactionDate, "
		+ "0 AS voucherNo, "
		+ "'OPB' AS voucherType, "
		+ "0 AS accountName, "
		+ "0 AS jama, "
		+ "0 AS udhar, "
		+ "0 AS subUnit, "
		+ "0 AS subDecimalLimitQty, "
		+ "COALESCE(SUM( "
		+ "CASE "
		+ "WHEN sh.transfer_type = 1 AND m.is_base_unit = 0 THEN sh.quantity * m.base_unit_equivalent "
		+ "WHEN sh.transfer_type = 1 AND m.is_base_unit = 1 THEN sh.quantity "
		+ "WHEN sh.transfer_type = 0 AND m.is_base_unit = 0 THEN -(sh.quantity * m.base_unit_equivalent) "
		+ "WHEN sh.transfer_type = 0 AND m.is_base_unit = 1 THEN -sh.quantity "
		+ "ELSE 0 "
		+ "END "
		+ ")OVER (PARTITION BY sh.fk_raw_material_id), 0) AS balance, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.id FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.id "
		+ "END AS balanceUnit, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.decimal_limit_qty FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM stock_history sh "
		+ "LEFT JOIN raw_material rm ON rm.id = sh.fk_raw_material_id "
		+ "LEFT JOIN measurement m ON m.id = sh.fk_measurement_id "
		+ "WHERE sh.transfer_date < :startDate AND sh.quantity != 0 AND rm.is_active = TRUE "
		+ "ORDER BY rm.id "
		+ "), "
		+ "DateWiseTransaction AS ( "
		+ "SELECT "
		+ "rm.id AS materialId, "
		+ "2 AS seqNo, "
		+ "sh.id AS id, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS contactName, "
		+ "sh.transfer_date AS transactionDate, "
		+ "sh.voucher_number AS voucherNo, "
		+ "sh.voucher_type AS voucherType, "
		+ "CASE "
		+ "WHEN sh.voucher_type != 'IHT' AND sh.voucher_type != 'RRH' THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND hm.name_prefer_lang IS NOT NULL AND hm.name_prefer_lang != '' THEN hm.name_prefer_lang "
		+ "WHEN :langType = 2 AND hm.name_supportive_lang IS NOT NULL AND hm.name_supportive_lang != '' THEN hm.name_supportive_lang "
		+ "ELSE hm.name_default_lang "
		+ "END "
		+ "END AS accountName, "
		+ "CASE "
		+ "WHEN sh.transfer_type = 0 THEN sh.quantity "
		+ "ELSE 0 "
		+ "END AS jama, "
		+ "CASE "
		+ "WHEN sh.transfer_type = 1 THEN sh.quantity "
		+ "ELSE 0 "
		+ "END AS udhar, "
		+ "m.id AS subUnit, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND sh.quantity % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND sh.quantity % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS subDecimalLimitQty, "
		+ "SUM( "
		+ "CASE "
		+ "WHEN sh.transfer_type = 1 AND m.is_base_unit = 0 THEN sh.quantity * m.base_unit_equivalent "
		+ "WHEN sh.transfer_type = 1 AND m.is_base_unit = 1 THEN sh.quantity "
		+ "WHEN sh.transfer_type = 0 AND m.is_base_unit = 0 THEN -(sh.quantity * m.base_unit_equivalent) "
		+ "WHEN sh.transfer_type = 0 AND m.is_base_unit = 1 THEN -sh.quantity "
		+ "ELSE 0 "
		+ "END "
		+ ") OVER (PARTITION BY sh.fk_raw_material_id ORDER BY sh.transfer_date,sh.id) AS balance, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.id FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.id "
		+ "END AS balanceUnit, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.decimal_limit_qty FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM stock_history sh "
		+ "LEFT JOIN raw_material rm ON rm.id = sh.fk_raw_material_id "
		+ "LEFT JOIN hall_master hm ON hm.id = sh.fk_contact_id "
		+ "LEFT JOIN measurement m ON m.id = sh.fk_measurement_id "
		+ "LEFT JOIN contact c ON c.id = sh.fk_contact_id "
		+ "WHERE sh.transfer_date BETWEEN :startDate AND :endDate AND sh.quantity != 0 AND rm.is_active = TRUE "
		+ "ORDER BY rm.id "
		+ "), "
		+ "ClosingBalance AS ( "
		+ "SELECT DISTINCT "
		+ "rm.id AS materialId, "
		+ "3 AS seqNo, "
		+ "0 AS id, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS contactName, "
		+ "NULL AS transactionDate, "
		+ "0 AS voucherNo, "
		+ "'CLB' AS voucherType, "
		+ "0 AS accountName, "
		+ "0 AS jama, "
		+ "0 AS udhar, "
		+ "0 AS subUnit, "
		+ "0 AS subDecimalLimitQty, "
		+ "SUM( "
		+ "CASE "
		+ "WHEN sh.transfer_type = 1 AND m.is_base_unit = 0 THEN sh.quantity * m.base_unit_equivalent "
		+ "WHEN sh.transfer_type = 1 AND m.is_base_unit = 1 THEN sh.quantity "
		+ "WHEN sh.transfer_type = 0 AND m.is_base_unit = 0 THEN -(sh.quantity * m.base_unit_equivalent) "
		+ "WHEN sh.transfer_type = 0 AND m.is_base_unit = 1 THEN -sh.quantity "
		+ "ELSE 0 "
		+ "END "
		+ ")OVER (PARTITION BY sh.fk_raw_material_id) AS balance, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.id FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.id "
		+ "END AS balanceUnit, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.decimal_limit_qty FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM stock_history sh "
		+ "LEFT JOIN raw_material rm ON rm.id = sh.fk_raw_material_id "
		+ "LEFT JOIN measurement m ON m.id = sh.fk_measurement_id "
		+ "WHERE sh.transfer_date <= :endDate AND sh.quantity != 0 AND rm.is_active = TRUE "
		+ "ORDER BY rm.id "
		+ ") "
		+ "SELECT "
		+ "materialId, "
		+ "contactName, "
		+ "transactionDate, "
		+ "voucherNo, "
		+ "voucherType, "
		+ "accountName, "
		+ "jama, "
		+ "udhar, "
		+ "subUnit, "
		+ "subDecimalLimitQty, "
		+ "balance, "
		+ "balanceUnit, "
		+ "decimalLimitQty "
		+ "FROM( "
		+ "SELECT "
		+ "pbc.materialId, "
		+ "pbc.seqNo, "
		+ "pbc.id, "
		+ "pbc.contactName, "
		+ "pbc.transactionDate, "
		+ "pbc.voucherNo, "
		+ "pbc.voucherType, "
		+ "pbc.accountName, "
		+ "CASE "
		+ "WHEN pbc.balance > -1 AND pbc.balance < 0 THEN ABS(pbc.balance / m1.base_unit_equivalent) "
		+ "WHEN pbc.balance <= -1 THEN ABS(pbc.balance) "
		+ "ELSE 0 "
		+ "END AS jama, "
		+ "CASE "
		+ "WHEN pbc.balance > 0 AND pbc.balance < 1 THEN pbc.balance / m1.base_unit_equivalent "
		+ "WHEN pbc.balance > 0 AND pbc.balance >= 1 THEN pbc.balance "
		+ "ELSE 0 "
		+ "END AS udhar, "
		+ "CASE "
		+ "WHEN pbc.balance > -1 AND pbc.balance < 1 THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND m1.symbol_prefer_lang IS NOT NULL AND m1.symbol_prefer_lang != '' THEN m1.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m1.symbol_supportive_lang IS NOT NULL AND m1.symbol_supportive_lang != '' THEN m1.symbol_supportive_lang "
		+ "ELSE m1.symbol_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END "
		+ "END AS subUnit, "
		+ "CASE "
		+ "WHEN pbc.balance > -1 AND pbc.balance < 1 THEN m1.decimal_limit_qty "
		+ "ELSE pbc.decimalLimitQty "
		+ "END AS subDecimalLimitQty, "
		+ "CASE "
		+ "WHEN pbc.balance > -1 AND pbc.balance < 1 THEN (pbc.balance / m1.base_unit_equivalent) "
		+ "ELSE pbc.balance "
		+ "END AS balance, "
		+ "CASE "
		+ "WHEN pbc.balance > -1 AND pbc.balance < 1 THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND m1.symbol_prefer_lang IS NOT NULL AND m1.symbol_prefer_lang != '' THEN m1.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m1.symbol_supportive_lang IS NOT NULL AND m1.symbol_supportive_lang != '' THEN m1.symbol_supportive_lang "
		+ "ELSE m1.symbol_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END "
		+ "END AS balanceUnit, "
		+ "CASE "
		+ "WHEN pbc.balance > -1 AND pbc.balance < 1 THEN m1.decimal_limit_qty "
		+ "ELSE pbc.decimalLimitQty "
		+ "END AS decimalLimitQty "
		+ "FROM PastBalanceCalculation pbc "
		+ "LEFT JOIN measurement m ON m.id = pbc.balanceUnit "
		+ "LEFT JOIN measurement m1 ON m1.base_unit_id = pbc.balanceUnit "
		+ "WHERE pbc.materialId IN (SELECT materialId FROM DateWiseTransaction) "
		+ "UNION ALL "
		+ "SELECT "
		+ "dwt.materialId, "
		+ "dwt.seqNo, "
		+ "dwt.id, "
		+ "dwt.contactName, "
		+ "dwt.transactionDate, "
		+ "dwt.voucherNo, "
		+ "dwt.voucherType, "
		+ "dwt.accountName, "
		+ "dwt.jama, "
		+ "dwt.udhar, "
		+ "CASE "
		+ "WHEN :langType = 1 AND subm.symbol_prefer_lang IS NOT NULL AND subm.symbol_prefer_lang != '' THEN subm.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND subm.symbol_supportive_lang IS NOT NULL AND subm.symbol_supportive_lang != '' THEN subm.symbol_supportive_lang "
		+ "ELSE subm.symbol_default_lang "
		+ "END AS subUnit, "
		+ "dwt.subDecimalLimitQty, "
		+ "CASE "
		+ "WHEN pbc.balance IS NOT NULL AND pbc.balance != '' THEN "
		+ "CASE "
		+ "WHEN (pbc.balance + dwt.balance) > -1 AND (pbc.balance + dwt.balance) < 1 THEN (pbc.balance + dwt.balance) / m1.base_unit_equivalent "
		+ "ELSE (pbc.balance + dwt.balance) "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN dwt.balance > -1 AND dwt.balance < 1 THEN (dwt.balance) / m1.base_unit_equivalent "
		+ "ELSE dwt.balance "
		+ "END "
		+ "END AS balance, "
		+ "CASE "
		+ "WHEN pbc.balance IS NOT NULL AND (pbc.balance + dwt.balance) > -1 AND (pbc.balance + dwt.balance) < 1 THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND m1.symbol_prefer_lang IS NOT NULL AND m1.symbol_prefer_lang != '' THEN m1.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m1.symbol_supportive_lang IS NOT NULL AND m1.symbol_supportive_lang != '' THEN m1.symbol_supportive_lang "
		+ "ELSE m1.symbol_default_lang "
		+ "END "
		+ "WHEN pbc.balance IS NULL AND dwt.balance > -1 AND dwt.balance < 1 THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND m1.symbol_prefer_lang IS NOT NULL AND m1.symbol_prefer_lang != '' THEN m1.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m1.symbol_supportive_lang IS NOT NULL AND m1.symbol_supportive_lang != '' THEN m1.symbol_supportive_lang "
		+ "ELSE m1.symbol_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END "
		+ "END AS balanceUnit, "
		+ "CASE "
		+ "WHEN (IFNULL(pbc.balance, 0) + dwt.balance) > -1 AND (IFNULL(pbc.balance, 0) + dwt.balance) < 1 THEN m1.decimal_limit_qty "
		+ "WHEN dwt.balanceUnit IN (1,3) AND dwt.decimalLimitQty = -1 AND (IFNULL(pbc.balance, 0) + dwt.balance) % 1 != 0 THEN 3 "
		+ "WHEN dwt.balanceUnit IN (1,3) AND dwt.decimalLimitQty = -1 AND (IFNULL(pbc.balance, 0) + dwt.balance) % 1 = 0 THEN 0 "
		+ "ELSE dwt.decimalLimitQty "
		+ "END AS decimalLimitQty "
		+ "FROM DateWiseTransaction dwt "
		+ "LEFT JOIN PastBalanceCalculation pbc ON pbc.materialId = dwt.materialId "
		+ "LEFT JOIN measurement m ON m.id = dwt.balanceUnit "
		+ "LEFT JOIN measurement m1 ON m1.base_unit_id = dwt.balanceUnit "
		+ "LEFT JOIN measurement subm ON subm.id = dwt.subUnit "
		+ "UNION ALL "
		+ "SELECT "
		+ "cb.materialId, "
		+ "cb.seqNo, "
		+ "cb.id, "
		+ "cb.contactName, "
		+ "cb.transactionDate, "
		+ "cb.voucherNo, "
		+ "cb.voucherType, "
		+ "cb.accountName, "
		+ "cb.jama, "
		+ "cb.udhar, "
		+ "cb.subUnit, "
		+ "cb.subDecimalLimitQty, "
		+ "CASE "
		+ "WHEN cb.balance > -1 AND cb.balance < 1 THEN (cb.balance / m1.base_unit_equivalent) "
		+ "ELSE cb.balance "
		+ "END AS balance, "
		+ "CASE "
		+ "WHEN cb.balance > -1 AND cb.balance < 1 THEN "
		+ "CASE "
		+ "WHEN :langType = 1 AND m1.symbol_prefer_lang IS NOT NULL AND m1.symbol_prefer_lang != '' THEN m1.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m1.symbol_supportive_lang IS NOT NULL AND m1.symbol_supportive_lang != '' THEN m1.symbol_supportive_lang "
		+ "ELSE m1.symbol_default_lang "
		+ "END "
		+ "ELSE "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END "
		+ "END AS balanceUnit, "
		+ "CASE "
		+ "WHEN cb.balance > -1 AND cb.balance < 1 THEN m1.decimal_limit_qty "
		+ "WHEN cb.balanceUnit IN(1,3) AND cb.decimalLimitQty = -1 AND cb.balance % 1 != 0 THEN 3 "
		+ "WHEN cb.balanceUnit IN(1,3) AND cb.decimalLimitQty = -1 AND cb.balance % 1 = 0 THEN 0 "
		+ "ELSE cb.decimalLimitQty "
		+ "END AS decimalLimitQty "
		+ "FROM ClosingBalance cb "
		+ "LEFT JOIN measurement m ON m.id = cb.balanceUnit "
		+ "LEFT JOIN measurement m1 ON m1.base_unit_id = cb.balanceUnit "
		+ "WHERE cb.materialId IN (SELECT materialId FROM DateWiseTransaction) "
		+ ") AS CombinedUnions "
		+ "ORDER BY materialId,seqNo,transactionDate,id;"
)

@SqlResultSetMapping(
	name = "getDailyActivityStockReportResult",
	classes = @ConstructorResult(
		targetClass = AccountDailyActivityReportDto.class,
		columns = {
			@ColumnResult(name = "materialId", type = Long.class),
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "transactionDate", type = LocalDateTime.class),
			@ColumnResult(name = "voucherNo", type = Long.class),
			@ColumnResult(name = "voucherType", type = String.class),
			@ColumnResult(name = "accountName", type = String.class),
			@ColumnResult(name = "jama", type = Double.class),
			@ColumnResult(name = "udhar", type = Double.class),
			@ColumnResult(name = "subUnit", type = String.class),
			@ColumnResult(name = "subDecimalLimitQty", type = Integer.class),
			@ColumnResult(name = "balance", type = Double.class),
			@ColumnResult(name = "balanceUnit", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getDailyActivityAccountDetailsReport",
	resultSetMapping = "getDailyActivityAccountReportResult",
	query = "WITH PastBalanceCalculation AS ( "
		+ "SELECT DISTINCT "
		+ "c.id AS contactId, "
		+ "1 AS seqNo, "
		+ "0 AS id, "
		+ "c.is_cash AS cashId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ ":startDate AS transactionDate, "
		+ "0 AS voucherNo, "
		+ "'OPB' AS voucherType, "
		+ "0 AS accountName, "
		+ "0 AS jama, "
		+ "0 AS udhar, "
		+ "COALESCE(SUM( "
		+ "CASE "
		+ "WHEN ah.transaction_type = 1 THEN -ah.amount "
		+ "ELSE ah.amount "
		+ "END "
		+ ")OVER (PARTITION BY ah.fk_contact_from_id), 0) AS balance, "
		+ "NULL AS remark, "
		+ "0 AS decimalLimitForCurrency "
		+ "FROM account_history ah "
		+ "LEFT JOIN contact c ON c.id = ah.fk_contact_from_id "
		+ "WHERE DATE(ah.transaction_date) < :startDate AND (ah.fk_contact_from_id IN "
		+ "(SELECT "
		+ "c.id "
		+ "FROM contact c "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE cc.is_non_updatable = TRUE AND c.is_active = TRUE "
		+ ")) "
		+ "ORDER BY c.id "
		+ "), "
		+ "DateWiseTransaction AS ( "
		+ "SELECT "
		+ "c.id AS contactId, "
		+ "2 AS seqNo, "
		+ "ah.id AS id, "
		+ "c.is_cash AS cashId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "DATE(ah.transaction_date) AS transactionDate, "
		+ "CASE "
		+ "WHEN ah.voucher_type = 'CP' OR ah.voucher_type = 'CR' THEN cprd.fk_cash_payment_receipt_id "
		+ "WHEN ah.voucher_type = 'BP' OR ah.voucher_type = 'BR' THEN bprd.fk_bank_payment_receipt_id "
		+ "ELSE ah.voucher_number "
		+ "END AS voucherNo, "
		+ "ah.voucher_type AS voucherType, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c1.name_prefer_lang IS NOT NULL AND c1.name_prefer_lang != '' THEN c1.name_prefer_lang "
		+ "WHEN :langType = 2 AND c1.name_supportive_lang IS NOT NULL AND c1.name_supportive_lang != '' THEN c1.name_supportive_lang "
		+ "ELSE c1.name_default_lang "
		+ "END AS accountName, "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN ah.amount "
		+ "ELSE 0 "
		+ "END AS jama, "
		+ "CASE "
		+ "WHEN ah.transaction_type = 1 THEN ah.amount "
		+ "ELSE 0 "
		+ "END AS udhar, "
		+ "SUM( "
		+ "CASE "
		+ "WHEN ah.transaction_type = 0 THEN ah.amount "
		+ "WHEN ah.transaction_type = 1 THEN -ah.amount "
		+ "ELSE 0 "
		+ "END "
		+ ") OVER (PARTITION BY ah.fk_contact_from_id ORDER BY DATE(ah.transaction_date), ah.id) AS balance, "
		+ "ah.remark AS remark, "
		+ "0 AS decimalLimitForCurrency "
		+ "FROM account_history ah "
		+ "LEFT JOIN contact c ON c.id = ah.fk_contact_from_id "
		+ "LEFT JOIN contact c1 ON c1.id = ah.fk_contact_to_id "
		+ "LEFT JOIN bank_payment_receipt_details bprd ON bprd.id = ah.voucher_number "
		+ "LEFT JOIN cash_payment_receipt_details cprd ON cprd.id = ah.voucher_number "
		+ "WHERE (ah.amount != 0) AND (DATE(ah.transaction_date) BETWEEN :startDate AND :endDate) AND (ah.fk_contact_from_id IN "
		+ "(SELECT "
		+ "c.id "
		+ "FROM contact c "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE cc.is_non_updatable = TRUE AND c.is_active = TRUE "
		+ ")) "
		+ "ORDER BY c.id,transactionDate "
		+ "), "
		+ "ClosingBalance AS ( "
		+ "SELECT DISTINCT "
		+ "c.id AS contactId, "
		+ "3 AS seqNo, "
		+ "0 AS id, "
		+ "c.is_cash AS cashId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "NULL AS transactionDate, "
		+ "0 AS voucherNo, "
		+ "'CLB' AS voucherType, "
		+ "0 AS accountName, "
		+ "0 AS jama, "
		+ "0 AS udhar, "
		+ "COALESCE(SUM( "
		+ "CASE "
		+ "WHEN ah.transaction_type = 1 THEN -ah.amount "
		+ "ELSE ah.amount "
		+ "END "
		+ ")OVER (PARTITION BY ah.fk_contact_from_id), 0) AS balance, "
		+ "NULL AS remark, "
		+ "0 AS decimalLimitForCurrency "
		+ "FROM account_history ah "
		+ "LEFT JOIN contact c ON c.id = ah.fk_contact_from_id "
		+ "WHERE DATE(ah.transaction_date) <= :endDate AND (ah.fk_contact_from_id IN "
		+ "(SELECT "
		+ "c.id "
		+ "FROM contact c "
		+ "LEFT JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "LEFT JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE cc.is_non_updatable = TRUE AND c.is_active = TRUE "
		+ ")) "
		+ "ORDER BY c.id "
		+ ") "
		+ "SELECT "
		+ "contactId, "
		+ "contactName, "
		+ "transactionDate, "
		+ "voucherNo, "
		+ "voucherType, "
		+ "accountName, "
		+ "jama, "
		+ "udhar, "
		+ "balance, "
		+ "remark, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM( "
		+ "SELECT "
		+ "pbc.contactId, "
		+ "pbc.seqNo, "
		+ "pbc.id, "
		+ "pbc.cashId, "
		+ "pbc.contactName, "
		+ "pbc.transactionDate, "
		+ "pbc.voucherNo, "
		+ "pbc.voucherType, "
		+ "pbc.accountName, "
		+ "CASE "
		+ "WHEN pbc.balance >= 0 THEN pbc.balance "
		+ "ELSE 0 "
		+ "END AS jama, "
		+ "CASE "
		+ "WHEN pbc.balance < 0 THEN ABS(pbc.balance) "
		+ "ELSE 0 "
		+ "END AS udhar, "
		+ "pbc.balance, "
		+ "pbc.remark, "
		+ "pbc.decimalLimitForCurrency "
		+ "FROM PastBalanceCalculation pbc "
		+ "WHERE pbc.contactId IN (SELECT contactId FROM DateWiseTransaction) "
		+ "UNION ALL "
		+ "SELECT "
		+ "dwt.contactId, "
		+ "dwt.seqNo, "
		+ "dwt.id, "
		+ "dwt.cashId, "
		+ "dwt.contactName, "
		+ "dwt.transactionDate, "
		+ "dwt.voucherNo, "
		+ "dwt.voucherType, "
		+ "dwt.accountName, "
		+ "dwt.jama, "
		+ "dwt.udhar, "
		+ "COALESCE((pbc.balance), 0) + dwt.balance AS balance, "
		+ "dwt.remark, "
		+ "dwt.decimalLimitForCurrency "
		+ "FROM DateWiseTransaction dwt "
		+ "LEFT JOIN PastBalanceCalculation pbc ON pbc.contactId = dwt.contactId "
		+ "UNION ALL "
		+ "SELECT * FROM ClosingBalance cb "
		+ "WHERE cb.contactId IN (SELECT contactId FROM DateWiseTransaction) "
		+ ") AS CombinedUnions "
		+ "ORDER BY cashId DESC,contactId,seqNo,transactionDate,id"
)

@SqlResultSetMapping(
	name = "getDailyActivityAccountReportResult",
	classes = @ConstructorResult(
		targetClass = AccountDailyActivityReportDto.class,
		columns = {
			@ColumnResult(name = "contactId", type = Long.class),
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "transactionDate", type = LocalDateTime.class),
			@ColumnResult(name = "voucherNo", type = Long.class),
			@ColumnResult(name = "voucherType", type = String.class),
			@ColumnResult(name = "accountName", type = String.class),
			@ColumnResult(name = "jama", type = Double.class),
			@ColumnResult(name = "udhar", type = Double.class),
			@ColumnResult(name = "balance", type = Double.class),
			@ColumnResult(name = "remark", type = String.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class)	
		}
	)
)

@NamedNativeQuery(
	name = "getGstSalesReportData",
	resultSetMapping = "getgstSalesReportResult",
	query = "SELECT "
		+ "oi.bill_date AS billDate, "
		+ "oi.id AS billNo, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "c.gst_number AS gstNumber, "
		+ "SUM(ROUND((IFNULL(oif.person,0) * IFNULL(oif.rate, 0)) + (IFNULL(oif.extra, 0) * IFNULL(oif.rate, 0)), 2)) AS netTotal, "
		+ "CASE  "
		+ "WHEN c.gst_number IS NOT NULL AND cp.gst_number IS NOT NULL AND LEFT(cp.gst_number, 2) != LEFT(c.gst_number, 2) THEN 0 "
		+ "ELSE tm.cgst "
		+ "END AS cgstRate, "
		+ "CASE  "
		+ "WHEN c.gst_number IS NOT NULL AND cp.gst_number IS NOT NULL AND LEFT(cp.gst_number, 2) != LEFT(c.gst_number, 2) THEN 0 "
		+ "ELSE tm.sgst "
		+ "END AS sgstRate, "
		+ "CASE  "
		+ "WHEN c.gst_number IS NOT NULL AND cp.gst_number IS NOT NULL AND LEFT(cp.gst_number, 2) != LEFT(c.gst_number, 2) THEN tm.igst "
		+ "ELSE 0 "
		+ "END AS igstRate, "
		+ "CASE "
		+ "WHEN c.gst_number IS NOT NULL AND cp.gst_number IS NOT NULL AND LEFT(cp.gst_number, 2) != LEFT(c.gst_number, 2) THEN 0 "
		+ "ELSE ((tm.cgst * SUM(ROUND((IFNULL(oif.person,0) * IFNULL(oif.rate, 0)) + (IFNULL(oif.extra, 0) * IFNULL(oif.rate, 0)), 2))) / 100) "
		+ "END AS cgstPrice, "
		+ "CASE "
		+ "WHEN c.gst_number IS NOT NULL AND cp.gst_number IS NOT NULL AND LEFT(cp.gst_number, 2) != LEFT(c.gst_number, 2) THEN 0 "
		+ "ELSE ((tm.sgst * SUM(ROUND((IFNULL(oif.person,0) * IFNULL(oif.rate, 0)) + (IFNULL(oif.extra, 0) * IFNULL(oif.rate, 0)), 2))) / 100) "
		+ "END AS sgstPrice, "
		+ "CASE "
		+ "WHEN c.gst_number IS NOT NULL AND cp.gst_number IS NOT NULL AND LEFT(cp.gst_number, 2) != LEFT(c.gst_number, 2) THEN ((tm.igst * SUM(ROUND((IFNULL(oif.person,0) * IFNULL(oif.rate, 0)) + (IFNULL(oif.extra, 0) * IFNULL(oif.rate, 0)), 2))) / 100) "
		+ "ELSE 0 "
		+ "END AS igstPrice, "
		+ "oi.grand_total AS total,"
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM order_invoice oi "
		+ "LEFT JOIN order_invoice_function oif ON oi.id = oif.fk_order_invoice_id "
		+ "LEFT JOIN customer_order_details cod ON oi.fk_customer_order_details_id = cod.id "
		+ "LEFT JOIN contact c ON cod.fk_contact_customer_id = c.id "
		+ "LEFT JOIN tax_master tm ON oi.fk_tax_master_id = tm.id "
		+ "JOIN company_preferences cp "
		+ "WHERE oi.fk_tax_master_id IS NOT NULL AND (oi.bill_date >= :startDate OR :startDate IS NULL) AND (oi.bill_date <= :endDate OR :endDate IS NULL) "
		+ "GROUP BY oi.id "
		+ "ORDER BY oi.bill_date;"
)

@SqlResultSetMapping(
	name = "getgstSalesReportResult",
	classes = @ConstructorResult(
		targetClass = GstSalesPurchaseReportCommonDto.class,
		columns = {
			@ColumnResult(name = "billDate", type = LocalDateTime.class),
			@ColumnResult(name = "billNo", type = Long.class),
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "gstNumber", type = String.class),
			@ColumnResult(name = "netTotal", type = Double.class),
			@ColumnResult(name = "cgstRate", type = Double.class),
			@ColumnResult(name = "sgstRate", type = Double.class),
			@ColumnResult(name = "igstRate", type = Double.class),
			@ColumnResult(name = "cgstPrice", type = Double.class),
			@ColumnResult(name = "sgstPrice", type = Double.class),
			@ColumnResult(name = "igstPrice", type = Double.class),
			@ColumnResult(name = "total", type = Double.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getGstPurchaseReportData",
	resultSetMapping = "getgstPurchaseReportResult",
	query = "SELECT "
		+ "pb.bill_date AS billDate, "
		+ "pb.id AS billNo, "
		+ "CASE "
		+ "WHEN :langType = 1 AND c.name_prefer_lang IS NOT NULL AND c.name_prefer_lang != '' THEN c.name_prefer_lang "
		+ "WHEN :langType = 2 AND c.name_supportive_lang IS NOT NULL AND c.name_supportive_lang != '' THEN c.name_supportive_lang "
		+ "ELSE c.name_default_lang "
		+ "END AS contactName, "
		+ "ROUND(SUM(pbrm.weight * pbrm.price),2) AS netTotal, "
		+ "tax_record.cgstPrice AS cgstprice, "
		+ "tax_record.sgstPrice AS sgstprice, "
		+ "tax_record.igstPrice AS igstprice, "
		+ "SUM(pbrm.weight * pbrm.price) + tax_record.cgstPrice + tax_record.sgstPrice + tax_record.igstPrice AS total, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM purchase_bill pb "
		+ "LEFT JOIN contact c ON pb.fk_contact_id = c.id "
		+ "LEFT JOIN purchase_bill_raw_material pbrm ON pb.id = pbrm.fk_purchase_bill_id "
		+ "LEFT JOIN ( "
		+ "SELECT "
		+ "pbrm.fk_purchase_bill_id AS id, "
		+ "pbrm.fk_tax_master_id AS taxId, "
		+ "CASE "
		+ "WHEN c.gst_number IS NOT NULL AND cp.gst_number IS NOT NULL AND LEFT(c.gst_number, 2) != LEFT(cp.gst_number, 2) THEN 0 "
		+ "ELSE ROUND(SUM((pbrm.weight * pbrm.price * tm.cgst) / 100), 2) "
		+ "END AS cgstPrice, "
		+ "CASE "
		+ "WHEN c.gst_number IS NOT NULL AND cp.gst_number IS NOT NULL AND LEFT(c.gst_number, 2) != LEFT(cp.gst_number, 2) THEN 0 "
		+ "ELSE ROUND(SUM((pbrm.weight * pbrm.price * tm.sgst) / 100), 2) "
		+ "END AS sgstPrice, "
		+ "CASE "
		+ "WHEN c.gst_number IS NOT NULL AND cp.gst_number IS NOT NULL AND LEFT(c.gst_number, 2) != LEFT(cp.gst_number, 2) THEN ROUND(SUM((pbrm.weight * pbrm.price * tm.igst) / 100), 2) "
		+ "ELSE 0 "
		+ "END AS igstPrice "
		+ "FROM purchase_bill_raw_material pbrm "
		+ "LEFT JOIN purchase_bill pb ON pbrm.fk_purchase_bill_id = pb.id "
		+ "LEFT JOIN contact c ON pb.fk_contact_id = c.id  "
		+ "LEFT JOIN tax_master tm ON pbrm.fk_tax_master_id = tm.id "
		+ "JOIN company_preferences cp  "
		+ "WHERE pbrm.fk_tax_master_id IS NOT NULL AND (pb.bill_date >= :startDate OR :startDate IS NULL) AND (pb.bill_date <= :endDate OR :endDate IS NULL) "
		+ "GROUP BY pb.id "
		+ ") AS tax_record ON pb.id = tax_record.id "
		+ "LEFT JOIN tax_master tm ON tax_record.taxId = tm.id "
		+ "JOIN company_preferences cp  "
		+ "WHERE pb.id IN ( "
		+ "SELECT DISTINCT "
		+ "pbrm.fk_purchase_bill_id AS id "
		+ "FROM purchase_bill_raw_material pbrm "
		+ "WHERE pbrm.fk_tax_master_id IS NOT NULL "
		+ ") AND (pb.bill_date >= :startDate OR :startDate IS NULL) AND (pb.bill_date <= :endDate OR :endDate IS NULL) "
		+ "GROUP BY pb.id;"
)

@SqlResultSetMapping(
	name = "getgstPurchaseReportResult",
	classes = @ConstructorResult(
		targetClass = GstSalesPurchaseReportCommonDto.class,
		columns = {
			@ColumnResult(name = "billDate", type = LocalDateTime.class),
			@ColumnResult(name = "billNo", type = Long.class),
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "netTotal", type = Double.class),
			@ColumnResult(name = "cgstPrice", type = Double.class),
			@ColumnResult(name = "sgstPrice", type = Double.class),
			@ColumnResult(name = "igstPrice", type = Double.class),
			@ColumnResult(name = "total", type = Double.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class)
		}
	)
)

@Entity
public class AccountReportNativeQuery extends AuditIdModelOnly {
}