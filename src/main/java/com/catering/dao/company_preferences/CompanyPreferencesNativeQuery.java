package com.catering.dao.company_preferences;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.CompanyDetailsForReportDto;
import com.catering.dto.tenant.request.CompanyPreferencesForReportDto;
import com.catering.dto.tenant.request.TermsAndConditionsReportResultDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity representing the company preferences in the database, which includes various details 
 * such as company name, address, email, mobile number, GST number, and other information 
 * with support for different language preferences.
 * 
 * This class uses native SQL queries to fetch company preferences from the database 
 * based on a specified language type and maps the results to DTOs (Data Transfer Objects) 
 * for reporting purposes. The queries handle language-specific fields for various company attributes.
 * 
 * The class includes multiple `@NamedNativeQuery` annotations to retrieve company details 
 * in the desired language and the terms and conditions. These queries use SQL result set mappings 
 * to map the results to the corresponding DTOs.
 */
@NamedNativeQuery(
	name = "find",
	resultSetMapping = "findResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND cp.name_prefer_lang IS NOT NULL AND cp.name_prefer_lang != '' THEN cp.name_prefer_lang "
		+ "WHEN :langType = 2 AND cp.name_supportive_lang IS NOT NULL AND cp.name_supportive_lang != '' THEN cp.name_supportive_lang "
		+ "ELSE cp.name_default_lang "
		+ "END AS name, "
		+ "CASE "
		+ "WHEN :langType = 1 AND cp.address_prefer_lang IS NOT NULL AND cp.address_prefer_lang != '' THEN cp.address_prefer_lang "
		+ "WHEN :langType = 2 AND cp.address_supportive_lang  IS NOT NULL AND cp.address_supportive_lang != '' THEN cp.address_supportive_lang "
		+ "ELSE cp.address_default_lang "
		+ "END AS address, "
		+ "cp.email_id AS email, "
		+ "cp.mobile_number AS mobileNumber,"
		+ "cp.gst_number AS gstNo, "
		+ "CASE "
		+ "WHEN :langType = 1 AND cp.invoice_address_prefer_lang IS NOT NULL AND cp.invoice_address_prefer_lang != '' THEN cp.invoice_address_prefer_lang "
		+ "WHEN :langType = 2 AND cp.invoice_address_supportive_lang  IS NOT NULL AND cp.invoice_address_supportive_lang != '' THEN cp.invoice_address_supportive_lang "
		+ "ELSE cp.invoice_address_default_lang "
		+ "END AS invoiceAddress, "
		+ "cp.office_number AS officeNumber, "
		+ "cp.website AS website "
		+ "FROM company_preferences cp "
)

@SqlResultSetMapping(
	name = "findResult",
	classes = @ConstructorResult(
		targetClass = CompanyPreferencesForReportDto.class,
		columns = {
			@ColumnResult(name = "name", type = String.class),
			@ColumnResult(name = "address", type = String.class),
			@ColumnResult(name = "email", type = String.class),
			@ColumnResult(name = "mobileNumber", type = String.class),
			@ColumnResult(name = "gstNo", type = String.class),
			@ColumnResult(name = "invoiceAddress", type = String.class),
			@ColumnResult(name = "officeNumber", type = String.class),
			@ColumnResult(name = "website", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "findCompanyDetailsInEnglish",
	resultSetMapping = "findCompanyDetailsInEnglishResult",
	query = "SELECT "
		+ "IFNULL(cp.name_prefer_lang, '') AS companyName, "
		+ "IFNULL(cp.address_prefer_lang, '') AS companyAddress "
		+ "FROM company_preferences cp"
)

@SqlResultSetMapping(
	name = "findCompanyDetailsInEnglishResult",
	classes = @ConstructorResult(
		targetClass = CompanyDetailsForReportDto.class,
		columns = {
			@ColumnResult(name = "companyName", type = String.class),
			@ColumnResult(name = "companyAddress", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "findTermsAndConditions",
	resultSetMapping = "findTermsAndConditionsResult",
	query = "SELECT "
		+ "CASE "
		+ "WHEN :langType = 1 AND tac.content_prefer_lang IS NOT NULL AND tac.content_prefer_lang != '' THEN tac.content_prefer_lang "
		+ "WHEN :langType = 2 AND tac.content_supportive_lang IS NOT NULL AND tac.content_supportive_lang != '' THEN tac.content_supportive_lang "
		+ "ELSE tac.content_default_lang "
		+ "END AS termsAndConditions "
		+ "FROM terms_and_conditions tac"
)

@SqlResultSetMapping(
	name = "findTermsAndConditionsResult",
	classes = @ConstructorResult(
		targetClass = TermsAndConditionsReportResultDto.class,
		columns = {
			@ColumnResult(name = "termsAndConditions", type = String.class),
		}
	)
)

@Entity
public class CompanyPreferencesNativeQuery extends AuditIdModelOnly {}