package com.catering.dao.journal_voucher;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.JournalVoucherCalculationListDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing journal voucher-related data for native queries.
 * Extends {@link AuditIdModelOnly} to include auditing information for entity lifecycle events.
 * Includes a native query {@code JournalVoucherCalculation} to calculate the total Credit (CR) and 
 * Debit (DR) amounts for a specific journal voucher.
 */
@NamedNativeQuery(
	name = "JournalVoucherCalculation",
	resultSetMapping = "JournalVoucherCalculationListResult",
	query = "SELECT "
		+ "(SELECT SUM(jvd.amount) FROM journal_voucher_details jvd WHERE jvd.fk_journal_voucher_id = :id AND jvd.transaction_type = 1) AS totalCR, "
		+ "(SELECT SUM(jvd.amount) FROM journal_voucher_details jvd WHERE jvd.fk_journal_voucher_id = :id AND jvd.transaction_type = 0) AS totalDR;"
)
@SqlResultSetMapping(
	name = "JournalVoucherCalculationListResult",
	classes = @ConstructorResult(
		targetClass = JournalVoucherCalculationListDto.class,
		columns = {
			@ColumnResult(name = "totalCr", type = Double.class),
			@ColumnResult(name = "totalDr", type = Double.class)
		}
	)
)
@Entity
public class JournalVoucherNativeQuery extends AuditIdModelOnly {
}