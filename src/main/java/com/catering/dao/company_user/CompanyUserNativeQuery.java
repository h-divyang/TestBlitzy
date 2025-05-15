package com.catering.dao.company_user;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.CompanyUserDetailsForReportDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing a named native query result for retrieving company admin details.
 * The named native query combines data from the company_user table to construct a {@link CompanyUserDetailsForReportDto}.
 * This class extends {@link AuditIdModelOnly} to inherit common auditing fields.
 *
 * <p>
 * The native query selects and concatenates first and last names based on the specified language type.
 * The result is mapped to a {@code CompanyUserDetailsForReportDto} using a SQL result set mapping.
 * </p>
 *
 * @see NamedNativeQuery
 * @see SqlResultSetMapping
 * @see ConstructorResult
 * @see CompanyUserDetailsForReportDto
 * @see AuditIdModelOnly
 * 
 * @author Krushali Talaviya
 * @since 22 January 2024
 */
@NamedNativeQuery(
	name = "findCompanyAdminDetails",
	resultSetMapping = "findCompanyAdminResult",
	query = "SELECT "
		+ "CONCAT_WS(' ', "
		+ "CASE "
		+ "WHEN :langType = 1 AND cu.first_name_prefer_lang IS NOT NULL AND cu.first_name_prefer_lang != '' THEN cu.first_name_prefer_lang "
		+ "WHEN :langType = 2 AND cu.first_name_supportive_lang IS NOT NULL AND cu.first_name_supportive_lang != '' THEN cu.first_name_supportive_lang "
		+ "ELSE cu.first_name_default_lang END, "
		+ "CASE "
		+ "WHEN :langType = 1 AND cu.last_name_prefer_lang IS NOT NULL AND cu.last_name_prefer_lang != '' THEN cu.last_name_prefer_lang "
		+ "WHEN :langType = 2 AND cu.last_name_supportive_lang IS NOT NULL AND cu.last_name_supportive_lang != '' THEN cu.last_name_supportive_lang "
		+ "ELSE cu.last_name_default_lang END "
		+ ") AS companyUserName, "
		+ "cu.mobile_number AS companyUserNumber "
		+ "FROM company_user cu "
		+ "WHERE cu.id = 1"
)

@NamedNativeQuery(
	name = "findCompanyAdminDetailsInEnglish",
	resultSetMapping = "findCompanyAdminResult",
	query = "SELECT "
		+ "IFNULL(CONCAT_WS(' ', cu.first_name_prefer_lang, cu.last_name_prefer_lang), ' ') AS companyUserName, "
		+ "cu.mobile_number AS companyUserNumber "
		+ "FROM company_user cu "
		+ "WHERE cu.id = 1"
)

@SqlResultSetMapping(
	name = "findCompanyAdminResult",
	classes = @ConstructorResult(
		targetClass = CompanyUserDetailsForReportDto.class,
		columns = {
			@ColumnResult(name = "companyUserName", type = String.class),
			@ColumnResult(name = "companyUserNumber", type = String.class)
		}
	)
)
@Entity
public class CompanyUserNativeQuery extends AuditIdModelOnly {
}