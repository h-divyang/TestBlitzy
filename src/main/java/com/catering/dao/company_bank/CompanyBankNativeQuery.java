package com.catering.dao.company_bank;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.CompanyBankForReportDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Represents a database entity for querying company bank details through native SQL.
 * This entity is used to map the results of a native query that fetches bank account details 
 * of the company, such as account name, account number, branch name, and IFSC code.
 * The results are returned as a {@link CompanyBankForReportDto} object.
 * 
 * The entity uses a native SQL query defined by the {@link NamedNativeQuery} annotation to 
 * retrieve data from the `company_bank` table and map the results to a Data Transfer Object (DTO).
 * 
 * The results are mapped to the {@link CompanyBankForReportDto} using the {@link SqlResultSetMapping}
 * annotation, which ensures that the selected columns from the database correspond to the properties of 
 * the DTO.
 * 
 * The entity extends {@link AuditIdModelOnly}, which includes audit information such as ID tracking 
 * but does not contain the full set of fields in the `company_bank` table.
 */
@NamedNativeQuery(
	name = "findBankDetails",
	resultSetMapping = "findBankResult",
	query = "SELECT "
		+ "cb.account_name AS accountName, "
		+ "cb.account_number AS accountNumber, "
		+ "cb.branch_name as branchName, "
		+ "cb.ifsc_code  as ifscCode "
		+ "FROM company_bank cb;"
)
@SqlResultSetMapping(
	name = "findBankResult",
	classes = @ConstructorResult(
		targetClass = CompanyBankForReportDto.class,
		columns = {
			@ColumnResult(name = "accountName", type = String.class),
			@ColumnResult(name = "accountNumber", type = String.class),
			@ColumnResult(name = "branchName", type = String.class),
			@ColumnResult(name = "ifscCode", type = String.class),
		}
	)
)
@Entity
public class CompanyBankNativeQuery extends AuditIdModelOnly {
}