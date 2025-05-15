package com.catering.dao.accounting_reports.stock_report;

import java.time.LocalDateTime;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import com.catering.dto.tenant.request.DateWiseReportDropDownCommonDto;
import com.catering.dto.tenant.request.DateWiseStockReportDto;
import com.catering.dto.tenant.request.DateWiseStockSummaryReportDto;
import com.catering.dto.tenant.request.StockLedgerRawMaterialDropDownDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing a stock report in the database.
 * This class extends {@link AuditIdModelOnly}, which provides basic audit fields 
 * such as created and modified timestamps and user information.
 * The purpose of this class is to serve as a data model for stock report-related queries.
 */
@NamedNativeQuery (
	name = "getRawMaterialForStockLedgerDropDown",
	resultSetMapping = "getDropDownDataResultStockLedgerOnly",
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
		+ "END AS nameSupportiveLang, "
		+ "sh.transfer_date AS transferDate "
		+ "FROM raw_material rm "
		+ "LEFT JOIN ( "
		+ "SELECT sh.fk_raw_material_id, MIN(sh.transfer_date) AS min_transaction_date "
		+ "FROM stock_history sh "
		+ "GROUP BY sh.fk_raw_material_id "
		+ ") sh_min ON rm.id = sh_min.fk_raw_material_id "
		+ "LEFT JOIN stock_history sh ON rm.id = sh.fk_raw_material_id AND sh.transfer_date = sh_min.min_transaction_date;"
)

@SqlResultSetMapping(
	name = "getDropDownDataResultStockLedgerOnly",
	classes = @ConstructorResult(
		targetClass = StockLedgerRawMaterialDropDownDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "transferDate", type = String.class)
		}
	)
)

@SqlResultSetMapping(
	name = "getDropDownDataResultStockLedger",
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
	name = "getDateWiseStockLedgerReport",
	resultSetMapping = "getDateWiseStockReportResult",
	query = "WITH PastBalanceCalculation AS ( "
		+ "SELECT "
		+ ":startDate AS transferDate, "
		+ "NULL AS voucherNo, "
		+ "'OPB' AS voucherType, "
		+ "0 AS contactName, "
		+ "0 AS issue, "
		+ "0 AS receive, "
		+ "COALESCE(m.id, (SELECT rm1.fk_measurement_id FROM raw_material rm1 WHERE rm1.id = :rawMaterialId)) AS subUnit, "
		+ "m.decimal_limit_qty AS subDecimalLimitQty, "
		+ "COALESCE(SUM( "
		+ "CASE "
		+ "WHEN sh.transfer_type = 1 AND m.is_base_unit = 0 THEN sh.quantity * m.base_unit_equivalent "
		+ "WHEN sh.transfer_type = 1 AND m.is_base_unit = 1 THEN sh.quantity "
		+ "WHEN sh.transfer_type = 0 AND m.is_base_unit = 0 THEN -(sh.quantity * m.base_unit_equivalent) "
		+ "WHEN sh.transfer_type = 0 AND m.is_base_unit = 1 THEN -sh.quantity "
		+ "ELSE 0 "
		+ "END "
		+ "), 0) AS balance, "
		+ "COALESCE(CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.id FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.id "
		+ "END, (SELECT rm1.fk_measurement_id FROM raw_material rm1 WHERE rm1.id = :rawMaterialId)) AS balanceUnit, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.decimal_limit_qty FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM stock_history sh "
		+ "LEFT JOIN contact c ON c.id = sh.fk_contact_id "
		+ "LEFT JOIN measurement m ON m.id = sh.fk_measurement_id "
		+ "WHERE sh.fk_raw_material_id = :rawMaterialId AND (sh.transfer_date < :startDate) "
		+ "), "
		+ "DateRangeTransaction AS ( "
		+ "SELECT "
		+ "sh.transfer_date AS transferDate, "
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
		+ "END AS contactName, "
		+ "CASE "
		+ "WHEN sh.transfer_type = 0 THEN sh.quantity "
		+ "ELSE 0 "
		+ "END AS issue, "
		+ "CASE "
		+ "WHEN sh.transfer_type = 1 THEN sh.quantity "
		+ "ELSE 0 "
		+ "END AS receive, "
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
		+ ") OVER (ORDER BY sh.transfer_date,sh.id) AS balance, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.id FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.id "
		+ "END AS balanceUnit, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.decimal_limit_qty FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM stock_history sh "
		+ "LEFT JOIN contact c ON c.id = sh.fk_contact_id "
		+ "LEFT JOIN hall_master hm ON hm.id = sh.fk_contact_id "
		+ "LEFT JOIN measurement m ON m.id = sh.fk_measurement_id "
		+ "WHERE sh.fk_raw_material_id = :rawMaterialId AND sh.transfer_date >= :startDate AND sh.transfer_date <= :endDate "
		+ "), "
		+ "ClosingBalance AS ( "
		+ "SELECT "
		+ ":endDate AS transferDate, "
		+ "NULL AS voucherNo, "
		+ "'CLB' AS voucherType, "
		+ "0 AS contactName, "
		+ "0 AS issue, "
		+ "0 AS receive, "
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
		+ ")AS balance, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.id FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.id "
		+ "END AS balanceUnit, "
		+ "CASE "
		+ "WHEN m.is_base_unit = 0 THEN (SELECT m1.decimal_limit_qty FROM measurement m1 WHERE m1.id = m.base_unit_id) "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty "
		+ "FROM stock_history sh "
		+ "LEFT JOIN contact c ON c.id = sh.fk_contact_id "
		+ "LEFT JOIN measurement m ON m.id = sh.fk_measurement_id "
		+ "WHERE sh.fk_raw_material_id = :rawMaterialId AND sh.transfer_date <= :endDate "
		+ ") "
		+ "SELECT "
		+ "transferDate, "
		+ "voucherNo, "
		+ "voucherType, "
		+ "contactName, "
		+ "issue, "
		+ "receive, "
		+ "subUnit, "
		+ "subDecimalLimitQty, "
		+ "balance, "
		+ "balanceUnit, "
		+ "decimalLimitQty "
		+ "FROM ( "
		+ "SELECT "
		+ "pbc.transferDate, "
		+ "pbc.voucherNo, "
		+ "pbc.voucherType, "
		+ "pbc.contactName, "
		+ "CASE "
		+ "WHEN pbc.balance > -1 AND pbc.balance < 0 AND pbc.balance != 0 THEN ABS(pbc.balance / m1.base_unit_equivalent) "
		+ "WHEN pbc.balance <= -1 THEN ABS(pbc.balance) "
		+ "ELSE 0 "
		+ "END AS issue, "
		+ "CASE "
		+ "WHEN pbc.balance > 0 AND pbc.balance < 1 AND pbc.balance != 0 THEN pbc.balance / m1.base_unit_equivalent "
		+ "WHEN pbc.balance > 0 AND pbc.balance >= 1 THEN pbc.balance "
		+ "ELSE 0 "
		+ "END AS receive, "
		+ "CASE "
		+ "WHEN pbc.balance > -1 AND pbc.balance < 1 AND pbc.balance != 0 THEN "
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
		+ "pbc.subDecimalLimitQty, "
		+ "CASE "
		+ "WHEN pbc.balance > -1 AND pbc.balance < 1 AND pbc.balance != 0 THEN (pbc.balance / m1.base_unit_equivalent) "
		+ "ELSE pbc.balance "
		+ "END AS balance, "
		+ "CASE "
		+ "WHEN pbc.balance > -1 AND pbc.balance < 1 AND pbc.balance != 0 THEN "
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
		+ "WHEN pbc.balance > -1 AND pbc.balance < 1 AND pbc.balance != 0 THEN m1.decimal_limit_qty "
		+ "ELSE pbc.decimalLimitQty "
		+ "END AS decimalLimitQty "
		+ "FROM PastBalanceCalculation pbc "
		+ "LEFT JOIN measurement m ON m.id = pbc.balanceUnit "
		+ "LEFT JOIN measurement m1 ON m1.base_unit_id = pbc.balanceUnit "
		+ "UNION ALL "
		+ "SELECT "
		+ "drt.transferDate, "
		+ "drt.voucherNo, "
		+ "drt.voucherType, "
		+ "drt.contactName, "
		+ "drt.issue AS issue, "
		+ "drt.receive AS receive, "
		+ "CASE "
		+ "WHEN :langType = 1 AND subm.symbol_prefer_lang IS NOT NULL AND subm.symbol_prefer_lang != '' THEN subm.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND subm.symbol_supportive_lang IS NOT NULL AND subm.symbol_supportive_lang != '' THEN subm.symbol_supportive_lang "
		+ "ELSE subm.symbol_default_lang "
		+ "END AS subUnit, "
		+ "drt.subDecimalLimitQty, "
		+ "CASE "
		+ "WHEN (pbc.balance + drt.balance) > -1 AND (pbc.balance + drt.balance) < 1 AND (pbc.balance + drt.balance) != 0 THEN (pbc.balance + drt.balance) / m1.base_unit_equivalent "
		+ "ELSE (pbc.balance + drt.balance) "
		+ "END AS balance, "
		+ "CASE "
		+ "WHEN (pbc.balance + drt.balance) > -1 AND (pbc.balance + drt.balance) < 1 AND (pbc.balance + drt.balance) != 0 THEN "
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
		+ "WHEN (pbc.balance + drt.balance) > -1 AND (pbc.balance + drt.balance) < 1 AND (pbc.balance + drt.balance) != 0 THEN m1.decimal_limit_qty "
		+ "WHEN drt.balanceUnit IN (1,3) AND drt.decimalLimitQty = -1 AND (pbc.balance + drt.balance) % 1 != 0 THEN 3 "
		+ "WHEN drt.balanceUnit IN (1,3) AND drt.decimalLimitQty = -1 AND (pbc.balance + drt.balance) % 1 = 0 THEN 0 "
		+ "ELSE drt.decimalLimitQty "
		+ "END AS decimalLimitQty "
		+ "FROM DateRangeTransaction drt "
		+ "LEFT JOIN PastBalanceCalculation pbc ON 1=1 "
		+ "LEFT JOIN measurement m ON m.id = drt.balanceUnit "
		+ "LEFT JOIN measurement m1 ON m1.base_unit_id = drt.balanceUnit "
		+ "LEFT JOIN measurement subm ON subm.id = drt.subUnit "
		+ "UNION ALL "
		+ "SELECT "
		+ "cb.transferDate, "
		+ "cb.voucherNo, "
		+ "cb.voucherType, "
		+ "cb.contactName, "
		+ "cb.issue, "
		+ "cb.receive, "
		+ "cb.subUnit, "
		+ "cb.subDecimalLimitQty, "
		+ "CASE "
		+ "WHEN cb.balance > -1 AND cb.balance < 1 AND cb.balance != 0 THEN (cb.balance / m1.base_unit_equivalent) "
		+ "ELSE cb.balance "
		+ "END AS balance, "
		+ "CASE "
		+ "WHEN cb.balance > -1 AND cb.balance < 1 AND cb.balance != 0 THEN "
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
		+ "WHEN cb.balance > -1 AND cb.balance < 1 AND cb.balance != 0 THEN m1.decimal_limit_qty "
		+ "WHEN cb.balanceUnit IN(1,3) AND cb.decimalLimitQty = -1 AND cb.balance % 1 != 0 THEN 3 "
		+ "WHEN cb.balanceUnit IN(1,3) AND cb.decimalLimitQty = -1 AND cb.balance % 1 = 0 THEN 0 "
		+ "ELSE cb.decimalLimitQty "
		+ "END AS decimalLimitQty "
		+ "FROM ClosingBalance cb "
		+ "LEFT JOIN measurement m ON m.id = cb.balanceUnit "
		+ "LEFT JOIN measurement m1 ON m1.base_unit_id = cb.balanceUnit "
		+ ") AS CombinedUnions "
		+ "ORDER BY transferDate;"
)

@SqlResultSetMapping(
	name = "getDateWiseStockReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseStockReportDto.class,
		columns = {
			@ColumnResult(name = "transferDate", type = LocalDateTime.class),
			@ColumnResult(name = "voucherNo", type = Long.class),
			@ColumnResult(name = "voucherType", type = String.class),
			@ColumnResult(name = "contactName", type = String.class),
			@ColumnResult(name = "issue", type = Double.class),
			@ColumnResult(name = "receive", type = Double.class),
			@ColumnResult(name = "subUnit", type = String.class),
			@ColumnResult(name = "subDecimalLimitQty", type = Integer.class),
			@ColumnResult(name = "balance", type = Double.class),
			@ColumnResult(name = "balanceUnit", type = String.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class)
		}
	)
)

@NamedNativeQuery(
	name = "getRawMaterialNameForStockLedgerReport",
	resultSetMapping = "getRawMaterialNameForStockReport",
	query = "SELECT DISTINCT "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName "
		+ "FROM stock_history sh "
		+ "INNER JOIN raw_material rm ON rm.id = sh.fk_raw_material_id "
		+ "WHERE sh.fk_raw_material_id = :rawMaterialId "
)

@SqlResultSetMapping(
	name = "getRawMaterialNameForStockReport",
	classes = @ConstructorResult(
		targetClass = DateWiseStockReportDto.class,
		columns = {
			@ColumnResult(name = "rawMaterialName", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getRawMaterialCategoryForStockSummaryDropDown",
	resultSetMapping = "getDropDownDataResultStockLedger",
	query = "SELECT DISTINCT "
		+ "rm.fk_raw_material_category_id AS id, "
		+ "rmc.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM total_stock ts "
		+ "LEFT JOIN raw_material rm ON rm.id = ts.fk_raw_material_id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "ORDER BY id "
)

@NamedNativeQuery(
	name = "getRawMaterialForStockSummaryDropDown",
	resultSetMapping = "getDropDownDataResultStockLedger",
	query = "SELECT DISTINCT "
		+ "rm.id AS id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "CASE "
		+ "WHEN rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS namePreferLang, "
		+ "CASE "
		+ "WHEN rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS nameSupportiveLang "
		+ "FROM total_stock ts "
		+ "LEFT JOIN raw_material rm ON rm.id = ts.fk_raw_material_id "
		+ "WHERE (0 = :rawMaterialCategoryId OR rm.fk_raw_material_category_id = :rawMaterialCategoryId) "
		+ "ORDER BY id "
)

@NamedNativeQuery(
	name = "getStockSummaryReport",
	resultSetMapping = "stockSummaryReportResult",
	query = "SELECT "
		+ "rmc.id AS rawMaterialCategoryId, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rmc.name_prefer_lang IS NOT NULL AND rmc.name_prefer_lang != '' THEN rmc.name_prefer_lang "
		+ "WHEN :langType = 2 AND rmc.name_supportive_lang IS NOT NULL AND rmc.name_supportive_lang != '' THEN rmc.name_supportive_lang "
		+ "ELSE rmc.name_default_lang "
		+ "END AS rawMaterialCategoryName, "
		+ "CASE "
		+ "WHEN :langType = 1 AND rm.name_prefer_lang IS NOT NULL AND rm.name_prefer_lang != '' THEN rm.name_prefer_lang "
		+ "WHEN :langType = 2 AND rm.name_supportive_lang IS NOT NULL AND rm.name_supportive_lang != '' THEN rm.name_supportive_lang "
		+ "ELSE rm.name_default_lang "
		+ "END AS rawMaterialName, "
		+ "ts.quantity AS stock, "
		+ "CASE "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND ts.quantity % 1 != 0 THEN 3 "
		+ "WHEN m.id IN(1,3) AND m.decimal_limit_qty = -1 AND ts.quantity % 1 = 0 THEN 0 "
		+ "ELSE m.decimal_limit_qty "
		+ "END AS decimalLimitQty, "
		+ "CASE "
		+ "WHEN :langType = 1 AND m.symbol_prefer_lang IS NOT NULL AND m.symbol_prefer_lang != '' THEN m.symbol_prefer_lang "
		+ "WHEN :langType = 2 AND m.symbol_supportive_lang IS NOT NULL AND m.symbol_supportive_lang != '' THEN m.symbol_supportive_lang "
		+ "ELSE m.symbol_default_lang "
		+ "END AS unit, "
		+ "CASE "
		+ "WHEN rm.supplier_rate IS NOT NULL AND rm.supplier_rate != '' THEN rm.supplier_rate "
		+ "ELSE 0 "
		+ "END AS rate, "
		+ "(SELECT cs.decimal_limit_for_currency FROM company_setting cs) AS decimalLimitForCurrency "
		+ "FROM total_stock ts "
		+ "INNER JOIN raw_material rm ON rm.id = ts.fk_raw_material_id "
		+ "INNER JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "INNER JOIN measurement m ON m.id = ts.fk_measurement_id "
		+ "WHERE (0 = :rawMaterialCategoryId OR rm.fk_raw_material_category_id = :rawMaterialCategoryId) AND (0 = :rawMaterialId OR ts.fk_raw_material_id = :rawMaterialId) "
		+ "ORDER BY rmc.priority, rmc.id, rm.priority, rm.id;"
)

@SqlResultSetMapping(
	name = "stockSummaryReportResult",
	classes = @ConstructorResult(
		targetClass = DateWiseStockSummaryReportDto.class,
		columns = {
			@ColumnResult(name = "rawMaterialCategoryId", type = Long.class),
			@ColumnResult(name = "rawMaterialCategoryName", type = String.class),
			@ColumnResult(name = "rawMaterialName", type = String.class),
			@ColumnResult(name = "stock", type = Double.class),
			@ColumnResult(name = "decimalLimitQty", type = Integer.class),
			@ColumnResult(name = "unit", type = String.class),
			@ColumnResult(name = "rate", type = Double.class),
			@ColumnResult(name = "decimalLimitForCurrency", type = Integer.class)
		}
	)
)

@Entity
public class StockReportNativeQuery extends AuditIdModelOnly {
}