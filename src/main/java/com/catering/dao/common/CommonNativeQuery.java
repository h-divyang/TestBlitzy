package com.catering.dao.common;

import java.time.LocalDate;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.common.VoucherPaymentHistoryDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity representing a custom native SQL query for retrieving voucher payment history.
 * The query combines payment details from both bank and cash payment receipt tables, 
 * based on the voucher type and voucher number, and orders the results by transaction date.
 *
 * The data retrieved includes:
 * - Payment ID
 * - Amount
 * - Transaction date
 * - Payment type (indicating whether it's a bank or cash payment)
 *
 * The result is mapped to the {@link VoucherPaymentHistoryDto} object.
 */
@NamedNativeQuery(
	name = "readVoucherPaymentHistory",
	resultSetMapping = "voucherPaymentHistoryResult",
	query = "( "
			+ "SELECT "
			+ "bprd.id, "
			+ "bprd.amount, "
			+ "bpr.transaction_date as transactionDate, "
			+ "'1' AS paymentType "
			+ "FROM "
			+ "bank_payment_receipt_details bprd "
			+ "INNER JOIN bank_payment_receipt bpr ON bprd.fk_bank_payment_receipt_id = bpr.id "
			+ "INNER JOIN order_invoice oi ON oi.id = bprd.voucher_number "
			+ "WHERE bprd.voucher_type = :voucherType "
			+ "AND oi.fk_customer_order_details_id = :voucherNumber "
			+ ") "
			+ "UNION ALL "
			+ "( "
			+ "SELECT "
			+ "cprd.id, "
			+ "cprd.amount, "
			+ "cpr.transaction_date as transactionDate, "
			+ "'0' AS paymentType "
			+ "FROM "
			+ "cash_payment_receipt_details cprd "
			+ "INNER JOIN cash_payment_receipt cpr ON cprd.fk_cash_payment_receipt_id = cpr.id "
			+ "INNER JOIN order_invoice oi ON oi.id = cprd.voucher_number "
			+ "WHERE cprd.voucher_type = :voucherType "
			+ "AND oi.fk_customer_order_details_id = :voucherNumber "
			+ ") "
			+ "ORDER BY transactionDate DESC"
)

@SqlResultSetMapping(
	name = "voucherPaymentHistoryResult",
	classes = @ConstructorResult(
		targetClass = VoucherPaymentHistoryDto.class,
		columns = {
				@ColumnResult(name = "id", type = Long.class),
				@ColumnResult(name = "amount", type = Double.class),
				@ColumnResult(name = "transactionDate", type = LocalDate.class),
				@ColumnResult(name = "paymentType", type = Integer.class)
		}
	)
)
@Entity
public class CommonNativeQuery extends AuditIdModelOnly {
}