package com.catering.dao.purchase_bill;

import java.time.LocalDate;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.PurchaseBillOrderDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialSupplierDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Represents the Purchase Bill entity for performing native SQL queries related to purchase bills and their
 * associated raw materials, contacts, and pricing calculations.
 * 
 * This class is annotated with several native queries using the {@link NamedNativeQuery} annotation to fetch
 * data related to purchase bills, raw materials, suppliers, and their amounts. The results are mapped to DTOs 
 * with the necessary fields, which are returned to the caller.
 */
@NamedNativeQuery(
	name = "purchaseBillOrderDropDown",
	resultSetMapping = "purchaseBillOrderDropDownResult",
	query = "SELECT "
		+ "po.id, "
		+ "po.fk_contact_id AS purchaseOrderContactId, "
		+ "po.purchase_date as purchaseDate, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "SUM(porm.total_amount) AS totalAmount "
		+ "FROM purchase_order po "
		+ "LEFT JOIN purchase_order_raw_material porm ON porm.fk_purchase_order_id = po.id "
		+ "LEFT JOIN contact c ON c.id = po.fk_contact_id "
		+ "WHERE po.id NOT IN(SELECT fk_purchase_order_id FROM purchase_bill WHERE fk_purchase_order_id IS NOT NULL) OR po.id IN (:id) "
		+ "GROUP BY po.id"
)
@SqlResultSetMapping(
	name = "purchaseBillOrderDropDownResult",
	classes = @ConstructorResult(
		targetClass = PurchaseBillOrderDropDownDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "purchaseOrderContactId", type = Long.class),
			@ColumnResult(name = "purchaseDate", type = LocalDate.class),
			@ColumnResult(name = "totalAmount", type = Double.class)
		}
	)
)

@NamedNativeQuery(
	name = "purchaseBillOrderRawMaterial",
	resultSetMapping = "purchaseBillOrderRawMaterialResult",
	query = "SELECT "
		+ "porm.fk_raw_material_id AS rawMaterialId, "
		+ "porm.hsn_code AS hsnCode, "
		+ "porm.weight, "
		+ "porm.fk_measurement_id AS measurementId, "
		+ "porm.price, "
		+ "porm.fk_tax_master_id AS taxMasterId, "
		+ "porm.total_amount AS totalAmount "
		+ "FROM purchase_order_raw_material porm "
		+ "WHERE porm.fk_purchase_order_id = :purchaseOrderId"
)
@SqlResultSetMapping(
	name = "purchaseBillOrderRawMaterialResult",
	classes = @ConstructorResult(
		targetClass = PurchaseBillOrderRawMaterialDto.class,
		columns = {
			@ColumnResult(name = "rawMaterialId", type = Long.class),
			@ColumnResult(name = "hsnCode", type = String.class),
			@ColumnResult(name = "weight", type = Double.class),
			@ColumnResult(name = "measurementId", type = Long.class),
			@ColumnResult(name = "price", type = Double.class),
			@ColumnResult(name = "taxMasterId", type = Long.class),
			@ColumnResult(name = "totalAmount", type = Double.class)
		}
	)
)

@NamedNativeQuery(
	name = "purchaseBillListCalculation",
	resultSetMapping = "purchaseBillListCalculationResult",
	query = "SELECT pb.id, "
		+ "IFNULL (SUM(pbrm.weight * pbrm.price) , 0) AS amount, "
		+ "IFNULL (SUM(CASE "
		+ "WHEN cp.gst_number IS NOT NULL AND c.gst_number IS NOT NULL AND SUBSTRING(cp.gst_number, 1, 2) != SUBSTRING(c.gst_number, 1, 2) "
		+ "THEN (pbrm.weight * pbrm.price * tm.igst) / 100 "
		+ "ELSE (pbrm.weight * pbrm.price * tm.sgst) / 100 + (pbrm.weight * pbrm.price * tm.cgst) / 100 "
		+ "END), 0) AS taxableAmount, "
		+ "IFNULL (SUM(pbrm.total_amount), 0) AS totalAmount "
		+ "FROM purchase_bill pb "
		+ "INNER JOIN purchase_bill_raw_material pbrm ON pbrm.fk_purchase_bill_id = pb.id "
		+ "INNER JOIN contact c ON c.id = pb.fk_contact_id "
		+ "LEFT JOIN tax_master tm ON tm.id = pbrm.fk_tax_master_id "
		+ "JOIN company_preferences cp "
		+ "WHERE pb.id = :id"
)
@SqlResultSetMapping(
	name = "purchaseBillListCalculationResult",
	classes = @ConstructorResult(
		targetClass = CommonCalculationFieldDto.class,
		columns = {
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "taxableAmount", type = Double.class),
			@ColumnResult(name = "totalAmount", type = Double.class)
		}
	)
)

@NamedNativeQuery(
	name = "purchaseBillRawMaterialDropDown",
	resultSetMapping = "purchaseBillRawMaterialDropDownResult",
	query = "SELECT "
		+ "rm.id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "rm.name_prefer_lang AS namePreferLang, "
		+ "rm.name_supportive_lang AS nameSupportiveLang, "
		+ "rm.hsn_code AS hsnCode, "
		+ "rm.fk_tax_master_id AS taxMasterId, "
		+ "rm.fk_measurement_id AS measurementId "
		+ "FROM raw_material rm "
		+ "WHERE rm.is_active = TRUE "
		+ "ORDER BY rm.priority, rm.id"
)
@SqlResultSetMapping(
	name = "purchaseBillRawMaterialDropDownResult",
	classes = @ConstructorResult(
		targetClass = PurchaseBillRawMaterialDropDownDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "hsnCode", type = String.class),
			@ColumnResult(name = "taxMasterId", type = Long.class),
			@ColumnResult(name = "measurementId", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "purchaseBillRawMaterialSupplier",
	resultSetMapping = "purchaseBillRawMaterialSupplierResult",
	query = "SELECT "
		+ "pb.fk_contact_id AS contactId, "
		+ "pbrm.price AS itemPrice, "
		+ "pbrm.fk_tax_master_id AS taxMasterId  "
		+ "FROM purchase_bill pb "
		+ "INNER JOIN purchase_bill_raw_material pbrm ON pbrm.fk_purchase_bill_id = pb.id "
		+ "INNER JOIN raw_material rm ON rm.id = pbrm.fk_raw_material_id "
		+ "WHERE rm.id = :id AND pbrm.id = ( "
		+ "SELECT MAX(_pbrm.id) "
		+ "FROM purchase_bill_raw_material _pbrm "
		+ "INNER JOIN purchase_bill _pb ON _pbrm.fk_purchase_bill_id = _pb.id "
		+ "WHERE _pbrm.fk_purchase_bill_id = pb.id "
		+ "AND _pb.fk_contact_id = pb.fk_contact_id "
		+ "AND _pbrm.fk_raw_material_id = pbrm.fk_raw_material_id)"
)
@SqlResultSetMapping(
	name = "purchaseBillRawMaterialSupplierResult",
	classes = @ConstructorResult(
		targetClass = PurchaseBillRawMaterialSupplierDto.class,
		columns = {
			@ColumnResult(name = "contactId", type = Long.class),
			@ColumnResult(name = "itemPrice", type = Double.class),
			@ColumnResult(name = "taxMasterId", type = Long.class),
		}
	)
)
@Entity
public class PurchaseBillNativeQuery extends AuditIdModelOnly {
}