package com.catering.dao.bank_payment_receipt;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import com.catering.dto.tenant.request.CashBankPaymentReceiptCommonResultListDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class for representing the result of a native query executed on the `bank_payment_receipt_details` table.
 * This class maps the result of the query that fetches supplier contact information and total amount 
 * for a specific bank payment receipt.
 * 
 * The class uses the named native query {@link BankPaymentReceiptNativeQuery#getSuppilerContactOfBankPaymentReceiptDetails}
 * to retrieve the necessary data from the database.
 */
@NamedNativeQuery(
	name = "getSuppilerContactOfBankPaymentReceiptDetails",
	resultSetMapping = "suppilerContactOfBankPaymentReceiptDetailsResult",
	query = "SELECT "
		+ "IF(COUNT(DISTINCT bprd.fk_contact_id) > 1, NULL, c.name_default_lang) AS nameDefaultLang, "
		+ "IF(COUNT(DISTINCT bprd.fk_contact_id) > 1, NULL, c.name_prefer_lang) AS namePreferLang, "
		+ "IF(COUNT(DISTINCT bprd.fk_contact_id) > 1, NULL, c.name_supportive_lang) AS nameSupportiveLang,"
		+ "IFNULL(SUM(bprd.amount), 0) AS totalAmount  "
		+ "FROM bank_payment_receipt_details bprd "
		+ "INNER JOIN bank_payment_receipt bpr ON bpr.id = bprd.fk_bank_payment_receipt_id "
		+ "INNER JOIN contact c ON c.id = bprd.fk_contact_id "
		+ "WHERE bpr.id = :bankPaymentReceiptId"
)

@SqlResultSetMapping(
	name = "suppilerContactOfBankPaymentReceiptDetailsResult",
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
public class BankPaymentReceiptNativeQuery extends AuditIdModelOnly {
}