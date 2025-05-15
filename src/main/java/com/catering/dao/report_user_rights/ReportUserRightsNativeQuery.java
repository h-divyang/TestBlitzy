package com.catering.dao.report_user_rights;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.audit.OnlyIdDto;
import com.catering.dto.tenant.GetReportUserRightsReportCategoryDto;
import com.catering.dto.tenant.GetReportUserRightsReportNamesDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing the native queries for report user rights in the system.
 * This class does not contain fields or data mappings but serves as a base entity for 
 * defining native queries with specific result set mappings. It extends {@link AuditIdModelOnly},
 * which provides audit-related fields (such as created/modified timestamps and user tracking).
 * 
 * The following native queries are defined:
 * <ul>
 * <li>{@code getReportCategory}: Retrieves report category data including the category ID, category name, and the user ID for the current user.</li>
 * <li>{@code getReportNamesWithRights}: Fetches the report names along with the associated report rights, including whether the user has 'view' permission.</li>
 * <li>{@code getRightsOfReportName}: Retrieves the report rights IDs for reports where the user has 'view' permission.</li>
 * </ul>
 * 
 * @since 2025-01-13
 * @author Krushali Talaviya
 */
@NamedNativeQuery(
	name = "getReportCategory",
	resultSetMapping = "getReportCategoryResult",
	query = "SELECT rc.id, rc.report_category_name AS name, :userId AS userId FROM report_category rc;"
)
@SqlResultSetMapping(
	name = "getReportCategoryResult",
	classes = @ConstructorResult(
		targetClass = GetReportUserRightsReportCategoryDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "name", type = String.class),
			@ColumnResult(name = "userId", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "getReportNamesWithRights",
	resultSetMapping = "getReportNamesWithRightsResult",
	query = "SELECT "
		+ "rm.id, "
		+ "rm.report_name AS name, "
		+ "rur.id AS reportRightsId, "
		+ "IF(rur.can_view IS NULL, FALSE, rur.can_view) AS canView "
		+ "FROM report_master rm "
		+ "LEFT JOIN report_user_rights rur ON rur.fk_report_master_id = rm.id AND rur.fk_user_id = :userId "
		+ "WHERE (rur.fk_user_id = :userId OR rur.fk_user_id IS NULL) AND rm.fk_report_category_id = :reportCategoryId "
		+ "ORDER BY rm.id;"
)
@SqlResultSetMapping(
	name = "getReportNamesWithRightsResult",
	classes = @ConstructorResult(
		targetClass = GetReportUserRightsReportNamesDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "name", type = String.class),
			@ColumnResult(name = "reportRightsId", type = Long.class),
			@ColumnResult(name = "canView", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "getRightsOfReportName",
	resultSetMapping = "getRightsOfReportNameResult",
	query = "SELECT fk_report_master_id AS id FROM report_user_rights WHERE fk_user_id = :userId AND can_view = 1;"
)
@SqlResultSetMapping(
	name = "getRightsOfReportNameResult",
	classes = @ConstructorResult(
		targetClass = OnlyIdDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class)
		}
	)
)
@Entity
public class ReportUserRightsNativeQuery extends AuditIdModelOnly {
}