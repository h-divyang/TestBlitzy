package com.catering.dao.menu_allocation_type;

import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.MenuAllocationDtoForNativeQuery;
import com.catering.dto.tenant.request.MenuAllocationTypeForNativeQuery;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Represents the native query mappings for menu allocation and preparation.
 * This class is used to define and map SQL queries related to order menu preparations
 * and menu allocation types, ensuring the result sets are mapped to the appropriate DTOs.
 * 
 * <p>It extends {@link AuditIdModelOnly} which likely handles audit-related fields.</p>
 */
@NamedNativeQuery(
	name = "findOrderMenuPreparationMenuItemByOrderId",
	resultSetMapping = "findOrderMenuPreparationMenuItemByOrderIdResult",
	query = "SELECT "
		+ "ompmi.id AS id, "
		+ "ompmi.order_type AS orderType, "
		+ "ompmi.order_date AS orderDate "
		+ "FROM customer_order_details cod "
		+ "JOIN order_function of2 on of2.fk_customer_order_details_id = cod.id "
		+ "JOIN order_menu_preparation omp on omp.fk_order_function_id = of2.id "
		+ "JOIN order_menu_preparation_menu_item ompmi on ompmi.fk_menu_preparation_id = omp.id "
		+ "JOIN order_menu_allocation_type omat on omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "WHERE cod.id = :orderId "
		+ "GROUP BY ompmi.id"
)

@SqlResultSetMapping(
	name = "findOrderMenuPreparationMenuItemByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = MenuAllocationDtoForNativeQuery.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "orderType", type = Integer.class),
			@ColumnResult(name = "orderDate", type = LocalDateTime.class),
		}
	)
)

@NamedNativeQuery(
	name = "findMenuAllocationTypeByOrderId",
	resultSetMapping = "findMenuAllocationTypeByOrderIdResult",
	query = "SELECT "
		+ "omat.id AS id, "
		+ "omat.fk_order_menu_preparation_menu_item_id AS fkOrderMenuPreparationMenuItemId, "
		+ "omat.fk_contact_id AS fkContactId, "
		+ "omat.counter_no AS counterNo, "
		+ "omat.counter_price AS counterPrice, "
		+ "omat.helper_no AS helperNo, "
		+ "omat.helper_price AS helperPrice, "
		+ "omat.quantity AS quantity, "
		+ "omat.price AS price "
		+ "FROM customer_order_details cod "
		+ "JOIN order_function of2 on of2.fk_customer_order_details_id = cod.id "
		+ "JOIN order_menu_preparation omp on omp.fk_order_function_id = of2.id "
		+ "JOIN order_menu_preparation_menu_item ompmi on ompmi.fk_menu_preparation_id = omp.id "
		+ "JOIN order_menu_allocation_type omat on omat.fk_order_menu_preparation_menu_item_id = ompmi.id "
		+ "WHERE cod.id = :orderId"
	)

@SqlResultSetMapping(
	name = "findMenuAllocationTypeByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = MenuAllocationTypeForNativeQuery.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "fkOrderMenuPreparationMenuItemId", type = Long.class),
			@ColumnResult(name = "fkContactId", type = Long.class),
			@ColumnResult(name = "counterNo", type = Integer.class),
			@ColumnResult(name = "counterPrice", type = Double.class),
			@ColumnResult(name = "helperNo", type = Integer.class),
			@ColumnResult(name = "helperPrice", type = Double.class),
			@ColumnResult(name = "quantity", type = Double.class),
			@ColumnResult(name = "price", type = Double.class)
		}
	)
)

@Entity
public class MenuAllocationNativeQuery extends AuditIdModelOnly {}
