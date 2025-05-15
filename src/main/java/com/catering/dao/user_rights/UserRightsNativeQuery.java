package com.catering.dao.user_rights;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.UserRightsMainMenuSubMenuDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Represents a data access object for managing user rights using named native queries.
 * This class provides methods to retrieve user rights information for main menus and submenus.
 * It utilizes native SQL queries for optimized database interactions.
 *
 * @author Krushali Talaviya
 * @since August 2023
 */
@NamedNativeQuery(
	name = "findMainMenuWithUserRights",
	resultSetMapping = "findMainMenuWithUserRightsResult",
	query = "SELECT DISTINCT "
		+ "IF(ur.fk_sub_menu_id IS NULL, ur.id, NULL) AS id, "
		+ "IFNULL(ur.fk_user_id, :userId) AS userId, "
		+ "mm.sequence AS sequence, "
		+ "mm.id AS mainMenuId, "
		+ "mm.name AS name, "
		+ "ur.fk_sub_menu_id = NULL AS subMenuId, "
		+ "IF(ur.fk_sub_menu_id IS NULL, ur.can_add, NULL) AS canAdd, "
		+ "IF(ur.fk_sub_menu_id IS NULL, ur.can_edit, NULL) AS canEdit, "
		+ "IF(ur.fk_sub_menu_id IS NULL, ur.can_delete, NULL) AS canDelete, "
		+ "IF(ur.fk_sub_menu_id IS NULL, ur.can_view, NULL) AS canView, "
		+ "IF(ur.fk_sub_menu_id IS NULL, ur.can_print, NULL) AS canPrint, "
		+ "ur.is_active AS isActive "
		+ "FROM main_menu mm "
		+ "LEFT JOIN user_rights ur ON ur.fk_main_menu_id = mm.id "
		+ "WHERE ur.fk_user_id = :userId OR ur.fk_user_id IS NULL "
		+ "ORDER BY mm.id"
)

@SqlResultSetMapping(
	name = "findMainMenuWithUserRightsResult",
	classes = @ConstructorResult(
		targetClass = UserRightsMainMenuSubMenuDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "userId", type = Long.class),
			@ColumnResult(name = "sequence", type = Long.class),
			@ColumnResult(name = "mainMenuId", type = Long.class),
			@ColumnResult(name = "name", type = String.class),
			@ColumnResult(name = "subMenuId", type = Long.class),
			@ColumnResult(name = "canAdd", type = Boolean.class),
			@ColumnResult(name = "canEdit", type = Boolean.class),
			@ColumnResult(name = "canDelete", type = Boolean.class),
			@ColumnResult(name = "canView", type = Boolean.class),
			@ColumnResult(name = "canPrint", type = Boolean.class),
			@ColumnResult(name = "isActive", type = Boolean.class)
		}
	)
)

@NamedNativeQuery(
	name = "findSubMenuWithUserRights",
	resultSetMapping = "findSubMenuWithUserRightsResult",
	query = "SELECT "
		+ "ur.id AS id, "
		+ "IFNULL(ur.fk_user_id, :userId) AS userId, "
		+ "sm.sequence AS sequence, "
		+ "sm.fk_main_menu_id AS mainMenuId, "
		+ "sm.name AS name, "
		+ "sm.id AS subMenuId, "
		+ "IF(ur.can_add IS NULL, false, ur.can_add) AS canAdd, "
		+ "IF(ur.can_edit IS NULL, false, ur.can_edit) AS canEdit, "
		+ "IF(ur.can_delete IS NULL, false, ur.can_delete) AS canDelete, "
		+ "IF(ur.can_view IS NULL, false, ur.can_view) AS canView, "
		+ "IF(ur.can_print IS NULL, false, ur.can_print) AS canPrint, "
		+ "IF(ur.is_active IS NULL, false, ur.is_active) AS isActive "
		+ "FROM sub_menu sm "
		+ "LEFT JOIN main_menu mm ON mm.id = sm.fk_main_menu_id "
		+ "LEFT JOIN user_rights ur ON ur.fk_sub_menu_id = sm.id "
		+ "WHERE (ur.fk_user_id = :userId OR ur.fk_user_id IS NULL) AND sm.fk_main_menu_id = :mainMenuId "
		+ "ORDER BY sequence"
)

@SqlResultSetMapping(
	name = "findSubMenuWithUserRightsResult",
	classes = @ConstructorResult(
		targetClass = UserRightsMainMenuSubMenuDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "userId", type = Long.class),
			@ColumnResult(name = "sequence", type = Long.class),
			@ColumnResult(name = "mainMenuId", type = Long.class),
			@ColumnResult(name = "name", type = String.class),
			@ColumnResult(name = "subMenuId", type = Long.class),
			@ColumnResult(name = "canAdd", type = Boolean.class),
			@ColumnResult(name = "canEdit", type = Boolean.class),
			@ColumnResult(name = "canDelete", type = Boolean.class),
			@ColumnResult(name = "canView", type = Boolean.class),
			@ColumnResult(name = "canPrint", type = Boolean.class),
			@ColumnResult(name = "isActive", type = Boolean.class)
		}
	)
)
@Entity
public class UserRightsNativeQuery extends AuditIdModelOnly {
}