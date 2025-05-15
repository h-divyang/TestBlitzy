package com.catering.dao.raw_material_return_to_hall;

import java.time.LocalDate;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import com.catering.dto.tenant.request.InputTransferToHallUpcomingOrderRawMaterial;
import com.catering.dto.tenant.request.RawMaterialReturnToHallCalculationDto;
import com.catering.dto.tenant.request.RawMaterialReturnToHallInputTransferToHallDropDownDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class representing raw material return to hall operations.
 * This class utilizes named native queries to retrieve and calculate data 
 * related to raw material returns, input transfers to halls, and associated 
 * raw material details from the database.
 *
 * <p>This class is annotated with native SQL queries that interact with multiple 
 * database tables such as <code>input_transfer_to_hall</code>, <code>hall_master</code>, 
 * <code>raw_material_return_to_hall</code>, <code>measurement</code>, etc. The queries 
 * handle various operations like fetching data for dropdowns, calculating material weights, 
 * and returning material details with unit conversions.</p>
 */
@NamedNativeQuery(
	name = "getRawMaterialReturnToHallInputTransferToHallDropDownData",
	resultSetMapping = "getRawMaterialReturnToHallInputTransferToHallDropDownDataResult",
	query = "SELECT "
		+ "itth.id, "
		+ "itth.fk_hall_id AS hallId, "
		+ "itth.transfer_date AS transferDate, "
		+ "hm.name_default_lang AS nameDefaultLang, "
		+ "hm.name_prefer_lang AS namePreferLang, "
		+ "hm.name_supportive_lang AS nameSupportiveLang "
		+ "FROM input_transfer_to_hall itth "
		+ "LEFT JOIN hall_master hm ON hm.id = itth.fk_hall_id "
		+ "WHERE itth.id NOT IN (SELECT fk_input_transfer_to_hall_id FROM raw_material_return_to_hall rmrth WHERE fk_input_transfer_to_hall_id IS NOT NULL) OR itth.id IN (:inputTransferToHallId) "
		+ "GROUP BY itth.id "
)
@SqlResultSetMapping(
	name = "getRawMaterialReturnToHallInputTransferToHallDropDownDataResult",
	classes = @ConstructorResult(
		targetClass = RawMaterialReturnToHallInputTransferToHallDropDownDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "hallId", type = Long.class),
			@ColumnResult(name = "transferDate", type = LocalDate.class)
		}
	)
)

@NamedNativeQuery(
	name = "findRawMaterialByInputTransferToHallId",
	resultSetMapping = "findRawMaterialByInputTransferToHallIdResult",
	query = "SELECT "
		+ "itthrm.fk_raw_material_id AS rawMaterialId, "
		+ "itthrm.weight AS qty, "
		+ "itthrm.fk_measurement_id AS qtyMeasurementId "
		+ "FROM input_transfer_to_hall itth "
		+ "LEFT JOIN input_transfer_to_hall_raw_material itthrm ON itthrm.fk_input_transfer_to_hall_id = itth.id "
		+ "WHERE itth.id = :inputTransferToHallId "
		+ "ORDER BY rawMaterialId"
)
@SqlResultSetMapping(
	name = "findRawMaterialByInputTransferToHallIdResult",
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
	name = "getRawMaterialReturnToHallCalculation",
	resultSetMapping = "getRawMaterialReturnToHallCalculationResult",
	query = "SELECT "
		+ "rmrth.fk_input_transfer_to_hall_id AS inputTransferToHallId, "
		+ "fk_raw_material_return_to_hall_id AS rawMaterialReturnToHallId, "
		+ "GROUP_CONCAT(CONCAT(FORMAT(weight, IF(me.id IN(1,3) AND me.decimal_limit_qty = -1, IF(weight % 1 != 0, 3, 0), me.decimal_limit_qty)), ' ', me.symbol_default_lang) SEPARATOR ', ') AS nameDefaultLang, "
		+ "GROUP_CONCAT(CONCAT(FORMAT(weight, IF(me.id IN(1,3) AND me.decimal_limit_qty = -1, IF(weight % 1 != 0, 3, 0), me.decimal_limit_qty)), ' ', me.symbol_prefer_lang) SEPARATOR ', ') AS namePreferLang, "
		+ "GROUP_CONCAT(CONCAT(FORMAT(weight, IF(me.id IN(1,3) AND me.decimal_limit_qty = -1, IF(weight % 1 != 0, 3, 0), me.decimal_limit_qty)), ' ', me.symbol_supportive_lang) SEPARATOR ', ') AS nameSupportiveLang "
		+ "FROM ( "
		+ "SELECT "
		+ "t.fk_raw_material_return_to_hall_id, "
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
		+ "fk_raw_material_return_to_hall_id, "
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
		+ "ELSE (SELECT id FROM measurement WHERE id = rmrthd.fk_measurement_id) "
		+ "END AS unit "
		+ "FROM raw_material_return_to_hall_details rmrthd "
		+ "LEFT JOIN measurement m ON m.id = rmrthd.fk_measurement_id "
		+ "WHERE "
		+ "fk_raw_material_return_to_hall_id = :id "
		+ "GROUP BY "
		+ "fk_raw_material_return_to_hall_id, fk_measurement_id) AS t "
		+ "LEFT JOIN measurement m1 ON m1.id = t.unit "
		+ "GROUP BY t.unit "
		+ ") AS sub "
		+ "LEFT JOIN measurement me ON me.id = unit "
		+ "LEFT JOIN raw_material_return_to_hall rmrth ON rmrth.id = fk_raw_material_return_to_hall_id "
		+ "GROUP BY "
		+ "fk_raw_material_return_to_hall_id;"
)
@SqlResultSetMapping(
	name = "getRawMaterialReturnToHallCalculationResult",
	classes = @ConstructorResult(
		targetClass = RawMaterialReturnToHallCalculationDto.class,
		columns = {
			@ColumnResult(name = "inputTransferToHallId", type = Long.class),
			@ColumnResult(name = "rawMaterialReturnToHallId", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class)
		}
	)
)
@Entity
public class RawMaterialReturnToHallNativeQuery extends AuditIdModelOnly {
}