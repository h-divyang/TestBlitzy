package com.catering.dao.setting;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.SettingMenuWithUserRightsDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * This class represents the query model for Setting Menu.
 * It extends the AuditIdModelOnly class to inherit basic auditing fields.
 *
 * @author Krushali Talaviya
 * @since 2023-12-04
 */
@NamedNativeQuery(
	name = "getSettingMenuWithUserRights",
	resultSetMapping = "getSettingMenuWithUserRightsResult",
	query = "SELECT "
		+ "mm.id AS id, "
		+ "mm.name AS spanTagName, "
		+ "mm.`path`, "
		+ "mm.icon AS icon, "
		+ "'' AS labelClass, "
		+ "'' AS parameterTagName, "
		+ "'' AS parameterTagId, "
		+ "mm.id AS mainMenuId, "
		+ "ur.can_add AS canAdd, "
		+ "ur.can_edit AS canEdit, "
		+ "ur.can_delete AS canDelete, "
		+ "ur.can_view AS canView, "
		+ "ur.can_print AS canPrint "
		+ "FROM main_menu mm "
		+ "LEFT JOIN user_rights ur ON ur.fk_main_menu_id = mm.id "
		+ "WHERE ur.fk_user_id = :userId AND mm.is_sidebar_menu = 0 AND ur.can_view IS TRUE GROUP BY mm.id"
)

@NamedNativeQuery(
	name = "getSettingSubMenuWithUserRights",
	resultSetMapping = "getSettingMenuWithUserRightsResult",
	query = "SELECT "
		+ "sm.id AS id, "
		+ "sm.name AS spanTagName, "
		+ "'' AS `path`, "
		+ "CASE "
		+ " WHEN sm.name = 'SETTINGS.COMPANY-PROFILE.COMPANY-PROFILE' THEN 'manage_accounts' "
		+ " WHEN sm.name = 'SETTINGS.UTILITY.UTILITY' THEN 'admin_panel_settings' "
		+ " WHEN sm.name = 'SETTINGS.SUBSCRIPTION.SUBSCRIPTION' THEN 'stars' "
		+ " WHEN sm.name = 'SETTINGS.PAYMENT-HISTORY.PAYMENT-HISTORY' THEN 'monetization_on' "
		+ " WHEN sm.name = 'SETTINGS.ABOUT-US.ABOUT-US' THEN 'info' "
		+ "END AS icon, "
		+ "CASE "
		+ " WHEN sm.name = 'SETTINGS.COMPANY-PROFILE.COMPANY-PROFILE' THEN 'company' "
		+ " WHEN sm.name = 'SETTINGS.UTILITY.UTILITY' THEN 'security' "
		+ " WHEN sm.name = 'SETTINGS.SUBSCRIPTION.SUBSCRIPTION' THEN 'subscription' "
		+ " WHEN sm.name = 'SETTINGS.PAYMENT-HISTORY.PAYMENT-HISTORY' THEN 'payment-history' "
		+ " WHEN sm.name = 'SETTINGS.ABOUT-US.ABOUT-US' THEN 'about-us' "
		+ "END AS labelClass, "
		+ "CASE "
		+ " WHEN sm.name = 'SETTINGS.COMPANY-PROFILE.COMPANY-PROFILE' THEN 'SETTINGS.COMPANY-PROFILE.MANAGE-COMPANY-PROFILE' "
		+ " WHEN sm.name = 'SETTINGS.UTILITY.UTILITY' THEN 'SETTINGS.UTILITY.MANAGE-UTILITY-SERVICE' "
		+ " WHEN sm.name = 'SETTINGS.SUBSCRIPTION.SUBSCRIPTION' THEN 'SETTINGS.SUBSCRIPTION.CURRENT-SUBSCRIPTION' "
		+ " WHEN sm.name = 'SETTINGS.PAYMENT-HISTORY.PAYMENT-HISTORY' THEN 'SETTINGS.PAYMENT-HISTORY.PAYMENT-HISTORY-AND-INVOICE' "
		+ " WHEN sm.name = 'SETTINGS.ABOUT-US.ABOUT-US' THEN 'SETTINGS.ABOUT-US.ABOUT-US' "
		+ "END AS parameterTagName, "
		+ "CASE"
		+ " WHEN sm.name = 'SETTINGS.COMPANY-PROFILE.COMPANY-PROFILE' THEN 'company_p' "
		+ " WHEN sm.name = 'SETTINGS.UTILITY.UTILITY' THEN 'security_p' "
		+ " WHEN sm.name = 'SETTINGS.SUBSCRIPTION.SUBSCRIPTION' THEN 'subscription_p' "
		+ " WHEN sm.name = 'SETTINGS.PAYMENT-HISTORY.PAYMENT-HISTORY' THEN 'payment_history_p' "
		+ " WHEN sm.name = 'SETTINGS.ABOUT-US.ABOUT-US' THEN 'about_us_p' "
		+ "END AS parameterTagId, "
		+ "sm.fk_main_menu_id AS mainMenuId, "
		+ "ur.can_add AS canAdd, "
		+ "ur.can_edit AS canEdit, "
		+ "ur.can_delete AS canDelete, "
		+ "ur.can_view AS canView, "
		+ "ur.can_print AS canPrint "
		+ "FROM sub_menu sm "
		+ "LEFT JOIN user_rights ur ON ur.fk_sub_menu_id = sm.id "
		+ "WHERE ur.fk_user_id = :userId AND ur.fk_main_menu_id = :mainMenuId AND ur.can_view IS TRUE "
		+ "ORDER BY sm.`sequence`"
)

@SqlResultSetMapping(
	name = "getSettingMenuWithUserRightsResult",
	classes = @ConstructorResult(
		targetClass = SettingMenuWithUserRightsDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "path", type = String.class),
			@ColumnResult(name = "spanTagName", type = String.class),
			@ColumnResult(name = "icon", type = String.class),
			@ColumnResult(name = "labelClass", type = String.class),
			@ColumnResult(name = "parameterTagName", type = String.class),
			@ColumnResult(name = "parameterTagId", type = String.class),
			@ColumnResult(name = "mainMenuId", type = Long.class),
			@ColumnResult(name = "canAdd", type = Boolean.class),
			@ColumnResult(name = "canEdit", type = Boolean.class),
			@ColumnResult(name = "canDelete", type = Boolean.class),
			@ColumnResult(name = "canView", type = Boolean.class),
			@ColumnResult(name = "canPrint", type = Boolean.class)
		}
	)
)

@Entity
public class SettingNativeQuery extends AuditIdModelOnly {
}