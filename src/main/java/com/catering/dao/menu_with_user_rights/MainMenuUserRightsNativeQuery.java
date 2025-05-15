package com.catering.dao.menu_with_user_rights;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.MenuWithUserRightsDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Represents a data access object for managing Menu With UserRight native queries.
 * This class provides methods to retrieve user rights information for menus.
 * It utilizes native SQL queries for optimized database interactions.
 *
 * @author Krushali Talaviya
 * @since 2023-08-28
 */
@NamedNativeQuery(
	name = "findMainMenuDataWithUserRightsData",
	resultSetMapping = "findMainMenuDataWithUserRightsDataResult",
	query = "SELECT * FROM ( "
		+ "(SELECT "
		+ "mh.id AS id, "
		+ "mh.name AS title, "
		+ "TRUE AS groupTitle, "
		+ "'' `path`, "
		+ "'' AS icon, "
		+ "'' AS iconType, "
		+ "'' AS clazz, "
		+ "NULL AS mainMenuId, "
		+ "NULL AS canAdd, "
		+ "NULL AS canEdit, "
		+ "NULL AS canDelete, "
		+ "NULL AS canView, "
		+ "NULL AS canPrint, "
		+ "NULL AS apiRightsName, "
		+ "concat(mh.`sequence`, '.', mh.id) AS temp "
		+ "FROM menu_header mh "
		+ "LEFT JOIN main_menu mm ON mm.fk_menu_header_id = mh.id "
		+ "LEFT JOIN user_rights ur ON ur.fk_main_menu_id = mm.id "
		+ "WHERE ur.fk_user_id = :userId AND ur.can_view IS TRUE "
		+ "GROUP BY mh.id ORDER BY mh.`sequence`) "
		+ "UNION "
		+ "(SELECT "
		+ "mm.id AS id, "
		+ "mm.name AS title, "
		+ "FALSE AS groupTitle, "
		+ "mm.`path`, "
		+ "mm.icon AS icon, "
		+ "'' AS iconType, "
		+ "IF(mm.`path` IS NULL, 'menu-toggle', NULL) AS clazz, "
		+ "mm.id AS mainMenuId, "
		+ "ur.can_add AS canAdd, "
		+ "ur.can_edit AS canEdit, "
		+ "ur.can_delete AS canDelete, "
		+ "ur.can_view AS canView, "
		+ "ur.can_print AS canPrint, "
		+ "mm.api_rights_name AS apiRightsName, "
		+ "concat(mh.`sequence`, '.', mm.fk_menu_header_id, '.', mm.`sequence`, mm.id) AS temp "
		+ "FROM main_menu mm "
		+ "LEFT JOIN menu_header mh ON mh.id = mm.fk_menu_header_id "
		+ "LEFT JOIN user_rights ur ON ur.fk_main_menu_id = mm.id "
		+ "WHERE ur.fk_user_id = :userId AND (NOT :isOnlySidebar OR (mm.is_sidebar_menu = 1 AND :isOnlySidebar)) AND ur.can_view IS TRUE GROUP BY mm.id) "
		+ ") AS t ORDER BY t.temp"
)

@SqlResultSetMapping(
	name = "findMainMenuDataWithUserRightsDataResult",
	classes = @ConstructorResult(
		targetClass = MenuWithUserRightsDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "title", type = String.class),
			@ColumnResult(name = "groupTitle", type = Boolean.class),
			@ColumnResult(name = "path", type = String.class),
			@ColumnResult(name = "icon", type = String.class),
			@ColumnResult(name = "iconType", type = String.class),
			@ColumnResult(name = "clazz", type = String.class),
			@ColumnResult(name = "mainMenuId", type = Long.class),
			@ColumnResult(name = "canAdd", type = Boolean.class),
			@ColumnResult(name = "canEdit", type = Boolean.class),
			@ColumnResult(name = "canDelete", type = Boolean.class),
			@ColumnResult(name = "canView", type = Boolean.class),
			@ColumnResult(name = "canPrint", type = Boolean.class),
			@ColumnResult(name = "temp", type = String.class),
			@ColumnResult(name = "apiRightsName", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "findSubMenuDataWithUserRightsData",
	resultSetMapping = "findSubMenuDataWithUserRightsDataResult",
	query = "SELECT "
		+ "sm.id AS id, "
		+ "sm.name AS title, "
		+ "FALSE AS groupTitle, "
		+ "sm.`path`, "
		+ "'' AS icon, "
		+ "'' AS iconType, "
		+ "'ml-menu' AS clazz, "
		+ "sm.fk_main_menu_id AS mainMenuId, "
		+ "ur.can_add AS canAdd, "
		+ "ur.can_edit AS canEdit, "
		+ "ur.can_delete AS canDelete, "
		+ "ur.can_view AS canView, "
		+ "ur.can_print AS canPrint, "
		+ "sm.api_rights_name AS apiRightsName, "
		+ "NULL AS temp "
		+ "FROM sub_menu sm "
		+ "LEFT JOIN user_rights ur ON ur.fk_sub_menu_id = sm.id "
		+ "WHERE ur.fk_user_id = :userId AND ur.fk_main_menu_id = :mainMenuId AND ur.can_view IS TRUE "
		+ "ORDER BY sm.`sequence`"
)

@SqlResultSetMapping(
	name = "findSubMenuDataWithUserRightsDataResult",
	classes = @ConstructorResult(
		targetClass = MenuWithUserRightsDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "title", type = String.class),
			@ColumnResult(name = "groupTitle", type = Boolean.class),
			@ColumnResult(name = "path", type = String.class),
			@ColumnResult(name = "icon", type = String.class),
			@ColumnResult(name = "iconType", type = String.class),
			@ColumnResult(name = "clazz", type = String.class),
			@ColumnResult(name = "mainMenuId", type = Long.class),
			@ColumnResult(name = "canAdd", type = Boolean.class),
			@ColumnResult(name = "canEdit", type = Boolean.class),
			@ColumnResult(name = "canDelete", type = Boolean.class),
			@ColumnResult(name = "canView", type = Boolean.class),
			@ColumnResult(name = "canPrint", type = Boolean.class),
			@ColumnResult(name = "temp", type = String.class),
			@ColumnResult(name = "apiRightsName", type = String.class)
		}
	)
)

@Entity
public class MainMenuUserRightsNativeQuery extends AuditIdModelOnly {
}