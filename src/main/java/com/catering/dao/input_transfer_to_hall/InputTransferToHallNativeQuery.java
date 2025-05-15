package com.catering.dao.input_transfer_to_hall;

import java.time.LocalDate;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.InputTransferToHallCalculationDto;
import com.catering.dto.tenant.request.InputTransferToHallRawMaterialDropDownDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderDto;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity representing the native queries for input transfer to hall operations.
 * This class contains native SQL queries used for various operations related to input transfers
 * for orders, raw materials, and calculations involving order events.
 *
 * <p> The following native queries are mapped within this entity:
 * <ul>
 * <li>{@link #upcomingOrdersForInputTransferToHall}: Fetches upcoming orders for input transfer to hall, with customer and event details.</li>
 * <li>{@link #findInputTransferToHallRawMaterialByOrderId}: Retrieves raw material details for a given order ID.</li>
 * <li>{@link #getInputTransferToHallCalculation}: Fetches the calculated weights and measurement details for input transfer to hall.</li>
 * <li>{@link #getRawMaterial}: Fetches raw material details including measurement information for active materials.</li>
 * </ul>
 * 
 * <p> This entity serves as a container for native queries to interact with underlying database tables
 * related to customer orders, raw materials, and measurements, providing custom SQL queries for business logic operations.
 */
@NamedNativeQuery(
	name = "upcomingOrdersForInputTransferToHall",
	resultSetMapping = "upcomingOrdersForInputTransferToHallResult",
	query = "SELECT "
		+ "cod.id, "
		+ "cod.event_main_date AS eventMainDate, "
		+ "c.name_default_lang AS customerNameDefaultLang, "
		+ "c.name_prefer_lang AS customerNamePreferLang, "
		+ "c.name_supportive_lang AS customerNameSupportiveLang, "
		+ "et.name_default_lang AS eventTypeNameDefaultLang, "
		+ "et.name_prefer_lang AS eventTypeNamePreferLang, "
		+ "et.name_supportive_lang AS eventTypeNameSupportiveLang "
		+ "FROM customer_order_details cod "
		+ "INNER JOIN contact c ON c.id = cod.fk_contact_customer_id "
		+ "INNER JOIN event_type et ON et.id = cod.fk_event_type_id "
		+ "WHERE cod.id NOT IN( "
		+ "SELECT _cod.id FROM customer_order_details _cod "
		+ "INNER JOIN input_transfer_to_hall ith ON "
		+ "_cod.id = ith.fk_customer_order_details_id "
		+ "AND (:orderId IS NULL "
		+ "OR ith.fk_customer_order_details_id != :orderId)) "
		+ "AND event_main_date >= CURRENT_DATE "
		+ "AND EXISTS ( "
		+ "SELECT 1 "
		+ "FROM ( "
		+ "SELECT rm.id AS rawMaterialId "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function ofu ON ofu.id = omp.fk_order_function_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id ELSE rma.fk_raw_material_id END "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE ofu.fk_customer_order_details_id = cod.id "
		+ "AND oni.id IS NULL "
		+ "AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) "
		+ "AND rm.is_general_fix_raw_material = false "
		+ "GROUP BY rm.id "
		+ "UNION "
		+ "SELECT ogfrm.fk_raw_material_id AS rawMaterialId "
		+ "FROM order_general_fix_raw_material ogfrm "
		+ "INNER JOIN order_function ofn ON ogfrm.fk_order_function_id = ofn.id "
		+ "WHERE ogfrm.qty > 0 "
		+ "AND ofn.id = (SELECT id "
		+ "FROM order_function "
		+ "WHERE fk_customer_order_details_id = cod.id "
		+ "GROUP BY fk_function_type_id "
		+ "ORDER BY MAX(person) DESC LIMIT 1) "
		+ ") AS raw_materials "
		+ ") "
		+ "ORDER BY event_main_date;"
)
@SqlResultSetMapping(
	name = "upcomingOrdersForInputTransferToHallResult",
	classes = @ConstructorResult(
		targetClass = InputTransferToHallUpcomingOrderDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "eventMainDate", type = LocalDate.class),
			@ColumnResult(name = "customerNameDefaultLang", type = String.class),
			@ColumnResult(name = "customerNamePreferLang", type = String.class),
			@ColumnResult(name = "customerNameSupportiveLang", type = String.class),
			@ColumnResult(name = "eventTypeNameDefaultLang", type = String.class),
			@ColumnResult(name = "eventTypeNamePreferLang", type = String.class),
			@ColumnResult(name = "eventTypeNameSupportiveLang", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "findInputTransferToHallRawMaterialByOrderId",
	resultSetMapping = "findInputTransferToHallRawMaterialByOrderIdResult",
	query = "SELECT * FROM ((SELECT "
		+ "rm.id AS rawMaterialId, "
		+ "adjustQuantity(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS qty, "
		+ "adjustQuantityUnit(SUM(getSmallestMeasurementValue(rma.final_qty, finalM.id)), getSmallestMeasurementId(finalM.id), IF(:isAdjustQuantity, 1, 0)) AS qtyMeasurementId "
		+ "FROM raw_material_allocation rma "
		+ "INNER JOIN order_menu_preparation_menu_item ompmi ON ompmi.id = rma.fk_menu_preparation_menu_item_id "
		+ "INNER JOIN order_menu_preparation omp ON omp.id = ompmi.fk_menu_preparation_id "
		+ "INNER JOIN order_function ofu ON ofu.id = omp.fk_order_function_id "
		+ "LEFT JOIN menu_item_raw_material mirm ON mirm.id = rma.fk_menu_item_raw_material_id "
		+ "LEFT JOIN raw_material rm ON rm.id = CASE WHEN mirm.id IS NOT NULL THEN mirm.fk_raw_material_id ELSE rma.fk_raw_material_id END "
		+ "LEFT JOIN measurement finalM ON finalM.id = rma.fk_final_measurement_id "
		+ "LEFT JOIN order_no_items oni ON oni.fk_order_menu_preparation_id = omp.id AND oni.fk_raw_material_id = rm.id "
		+ "LEFT JOIN raw_material_category rmc ON rmc.id = rm.fk_raw_material_category_id "
		+ "WHERE ofu.fk_customer_order_details_id = :orderId AND rm.is_active = TRUE AND oni.id IS NULL AND NOT (ompmi.order_type = 2 AND rmc.fk_raw_material_category_type_id = 1) AND rm.is_general_fix_raw_material = false "
		+ "GROUP BY rm.id) "
		+ "UNION "
		+ "(SELECT "
		+ "ogfrm.fk_raw_material_id AS rawMaterialId, "
		+ "ogfrm.qty AS qty, "
		+ "ogfrm.fk_measurement_id AS qtyMeasurementId "
		+ "FROM order_general_fix_raw_material ogfrm "
		+ "INNER JOIN order_function ofn ON ogfrm.fk_order_function_id = ofn.id "
		+ "INNER JOIN raw_material rm ON rm.id = ogfrm.fk_raw_material_id "
		+ "WHERE ogfrm.qty > 0 AND rm.is_active = TRUE "
		+ "AND ofn.id = (SELECT id "
		+ "FROM order_function "
		+ "WHERE fk_customer_order_details_id = :orderId "
		+ "GROUP BY fk_function_type_id "
		+ "ORDER BY MAX(person) DESC LIMIT 1) "
		+ "GROUP BY ofn.id, rm.id)) AS t "
		+ "ORDER BY t.rawMaterialId"
)
@SqlResultSetMapping(
	name = "findInputTransferToHallRawMaterialByOrderIdResult",
	classes = @ConstructorResult(
		targetClass = InputTransferToHallUpcomingOrderRawMaterial.class,
		columns = {
			@ColumnResult(name = "rawMaterialId", type = Long.class),
			@ColumnResult(name = "qty", type = Double.class),
			@ColumnResult(name = "qtyMeasurementId", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "getInputTransferToHallCalculation",
	resultSetMapping = "getInputTransferToHallCalculationResult",
	query = "SELECT "
		+ "itth.fk_customer_order_details_id AS customerOrderId, "
		+ "fk_input_transfer_to_hall_id AS inputTransferToHallId, "
		+ "GROUP_CONCAT(CONCAT(FORMAT(weight, IF(me.id IN(1,3) AND me.decimal_limit_qty = -1, IF(weight % 1 != 0, 3, 0), me.decimal_limit_qty)), ' ', me.symbol_default_lang) SEPARATOR ', ') AS nameDefaultLang, "
		+ "GROUP_CONCAT(CONCAT(FORMAT(weight, IF(me.id IN(1,3) AND me.decimal_limit_qty = -1, IF(weight % 1 != 0, 3, 0), me.decimal_limit_qty)), ' ', me.symbol_prefer_lang) SEPARATOR ', ') AS namePreferLang, "
		+ "GROUP_CONCAT(CONCAT(FORMAT(weight, IF(me.id IN(1,3) AND me.decimal_limit_qty = -1, IF(weight % 1 != 0, 3, 0), me.decimal_limit_qty)), ' ', me.symbol_supportive_lang) SEPARATOR ', ') AS nameSupportiveLang "
		+ "FROM ( "
		+ "SELECT "
		+ "t.fk_input_transfer_to_hall_id, "
		+ "CASE "
		+ "WHEN SUM(t.total_weight) < 1 AND t.unit IN(1,3) THEN SUM(t.total_weight) * 1000 "
		+ "ELSE SUM(t.total_weight) "
		+ "END AS weight, "
		+ "CASE "
		+ "WHEN SUM(t.total_weight) < 1 AND t.unit IN(1,3) THEN t.unit + 1 "
		+ "ELSE t.unit "
		+ "END AS unit "
		+ "FROM "
		+ "(SELECT "
		+ "fk_input_transfer_to_hall_id, "
		+ "CASE "
		+ "WHEN fk_measurement_id = 2 THEN SUM(weight) / 1000 "
		+ "WHEN fk_measurement_id = 4 THEN SUM(weight) / 1000 "
		+ "WHEN m.is_base_unit = 0 THEN SUM(weight) * m.base_unit_equivalent "
		+ "ELSE SUM(weight) "
		+ "END AS total_weight, "
		+ "CASE "
		+ "WHEN fk_measurement_id = 2 THEN 1 "
		+ "WHEN fk_measurement_id = 4 THEN 3 "
		+ "WHEN m.is_base_unit = 0 THEN m.base_unit_id "
		+ "ELSE (SELECT id FROM measurement WHERE id = itthrm.fk_measurement_id) "
		+ "END AS unit "
		+ "FROM input_transfer_to_hall_raw_material itthrm "
		+ "LEFT JOIN measurement m ON m.id = itthrm.fk_measurement_id "
		+ "WHERE "
		+ "fk_input_transfer_to_hall_id = :id "
		+ "GROUP BY "
		+ "fk_input_transfer_to_hall_id, fk_measurement_id) AS t "
		+ "GROUP BY t.unit "
		+ ") AS sub "
		+ "LEFT JOIN measurement me ON me.id = unit "
		+ "LEFT JOIN input_transfer_to_hall itth ON itth.id = fk_input_transfer_to_hall_id "
		+ "GROUP BY "
		+ "fk_input_transfer_to_hall_id;"
)
@SqlResultSetMapping(
	name = "getInputTransferToHallCalculationResult",
	classes = @ConstructorResult(
		targetClass = InputTransferToHallCalculationDto.class,
		columns = {
			@ColumnResult(name = "customerOrderId", type = Long.class),
			@ColumnResult(name = "inputTransferToHallId", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "getRawMaterial",
	resultSetMapping = "getRawMaterialResult",
	query = "SELECT "
		+ "rm.id AS id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "rm.name_prefer_lang AS namePreferLang, "
		+ "rm.name_supportive_lang AS nameSupportiveLang, "
		+ "rm.fk_measurement_id AS measurementId "
		+ "FROM raw_material rm "
		+ "WHERE rm.is_active = TRUE;"
)
@SqlResultSetMapping(
	name = "getRawMaterialResult",
	classes = @ConstructorResult(
		targetClass = InputTransferToHallRawMaterialDropDownDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "measurementId", type = Long.class)
		}
	)
)

@Entity
public class InputTransferToHallNativeQuery extends AuditIdModelOnly {
}