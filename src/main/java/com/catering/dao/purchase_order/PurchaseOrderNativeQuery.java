package com.catering.dao.purchase_order;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.PurchaseOrderContactDto;
import com.catering.dto.tenant.request.PurchaseOrderContactSupplierDataDto;
import com.catering.dto.tenant.request.PurchaseOrderRawMaterialDropDownDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Represents the Purchase Order entity for performing native SQL queries related to purchase orders and their
 * associated raw materials, contacts, and pricing.
 * 
 * This class is annotated with various native queries using the {@link NamedNativeQuery} annotation to fetch
 * data related to purchase orders, raw materials, contacts, and suppliers. Each query is mapped to a result class
 * through the {@link SqlResultSetMapping} annotation, and the results are returned as DTOs with the necessary fields.
 */
@NamedNativeQuery(
	name = "purchaseOrderContactDropDown",
	resultSetMapping = "purchaseOrderContactDropDownResult",
	query = "SELECT DISTINCT "
		+ "c.id, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "c.gst_number AS gstNumber "
		+ "FROM contact c "
		+ "INNER JOIN contact_categories contactCategories ON contactCategories.fk_contact_id = c.id "
		+ "INNER JOIN contact_category cc ON cc.id = contactCategories.fk_contact_category_id "
		+ "WHERE cc.fk_contact_category_type_id = 3 AND c.is_active = TRUE AND c.is_cash = 0 "
		+ "ORDER BY c.id"
)
@SqlResultSetMapping(
	name = "purchaseOrderContactDropDownResult",
	classes = @ConstructorResult(
		targetClass = PurchaseOrderContactDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "gstNumber", type = String.class)
		}
	)
)

@NamedNativeQuery(
	name = "purchaseOrderRawMaterialDropDown",
	resultSetMapping = "purchaseOrderRawMaterialDropDownResult",
	query = "SELECT "
		+ "rm.id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "rm.name_prefer_lang AS namePreferLang, "
		+ "rm.name_supportive_lang AS nameSupportiveLang, "
		+ "rm.hsn_code AS hsnCode, "
		+ "rm.fk_tax_master_id AS taxMasterId, "
		+ "rm.fk_measurement_id AS measurementId, "
		+ "rm.supplier_rate AS supplierRate "
		+ "FROM raw_material rm "
		+ "WHERE rm.is_active = TRUE "
		+ "ORDER BY rm.priority, rm.id"
)
@SqlResultSetMapping(
	name = "purchaseOrderRawMaterialDropDownResult",
	classes = @ConstructorResult(
		targetClass = PurchaseOrderRawMaterialDropDownDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "hsnCode", type = String.class),
			@ColumnResult(name = "taxMasterId", type = Long.class),
			@ColumnResult(name = "measurementId", type = Long.class),
			@ColumnResult(name = "supplierRate", type = Double.class)
		}
	)
)

@NamedNativeQuery(
	name = "purchaseOrderListCalculation",
	resultSetMapping = "purchaseOrderListCalculationResult",
	query = "SELECT po.id, "
		+ "IFNULL (SUM(porm.weight * porm.price), 0) AS amount, "
		+ "IFNULL (SUM(CASE "
		+ "WHEN cp.gst_number IS NOT NULL AND c.gst_number IS NOT NULL AND SUBSTRING(cp.gst_number, 1, 2) != SUBSTRING(c.gst_number, 1, 2) "
		+ "THEN (porm.weight * porm.price * tm.igst) / 100 "
		+ "ELSE (porm.weight * porm.price * tm.sgst) / 100 + (porm.weight * porm.price * tm.cgst) / 100 "
		+ "END), 0) AS taxableAmount, "
		+ "IFNULL (SUM(porm.total_amount), 0) AS totalAmount "
		+ "FROM purchase_order po "
		+ "INNER JOIN purchase_order_raw_material porm ON porm.fk_purchase_order_id = po.id "
		+ "INNER JOIN contact c ON c.id = po.fk_contact_id "
		+ "LEFT JOIN tax_master tm ON tm.id = porm.fk_tax_master_id "
		+ "JOIN company_preferences cp "
		+ "WHERE po.id = :id"
)
@SqlResultSetMapping(
	name = "purchaseOrderListCalculationResult",
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
	name = "purchaseOrderContactSupplier",
	resultSetMapping = "purchaseOrderContactSupplierResult",
	query = "SELECT "
		+ "po.fk_contact_id AS contactId, "
		+ "porm.price AS itemPrice "
		+ "FROM purchase_order po "
		+ "INNER JOIN purchase_order_raw_material porm ON porm.fk_purchase_order_id = po.id "
		+ "INNER JOIN raw_material rm ON rm.id = porm.fk_raw_material_id "
		+ "WHERE rm.id = :id AND porm.id = ( "
		+ "SELECT MAX(_porm.id) "
		+ "FROM purchase_order_raw_material _porm "
		+ "INNER JOIN purchase_order _po ON _porm.fk_purchase_order_id = _po.id "
		+ "WHERE _porm.fk_purchase_order_id = po.id "
		+ "AND _po.fk_contact_id = po.fk_contact_id "
		+ "AND _porm.fk_raw_material_id = porm.fk_raw_material_id);"
)
@SqlResultSetMapping(
	name = "purchaseOrderContactSupplierResult",
	classes = @ConstructorResult(
		targetClass = PurchaseOrderContactSupplierDataDto.class,
		columns = {
			@ColumnResult(name = "contactId", type = Long.class),
			@ColumnResult(name = "itemPrice", type = Double.class)
		}
	)
)
@Entity
public class PurchaseOrderNativeQuery extends AuditIdModelOnly {
}