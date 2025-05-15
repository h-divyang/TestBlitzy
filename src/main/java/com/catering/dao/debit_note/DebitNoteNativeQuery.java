package com.catering.dao.debit_note;

import java.time.LocalDate;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.DebitNotePurchaseBillDropDownDto;
import com.catering.dto.tenant.request.DebitNoteRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.model.audit.AuditIdModelOnly;

/**
 * Entity class for managing native queries related to debit notes and associated entities.
 * 
 * <p>This class contains {@code @NamedNativeQuery} and {@code @SqlResultSetMapping} annotations
 * to define and map results for various native SQL queries. These queries include operations
 * related to purchase bills, raw materials, and debit note calculations.
 */
@NamedNativeQuery(
	name = "debitNotePurchaseBillDropDown",
	resultSetMapping = "debitNotePurchaseBillDropDownResult",
	query = "SELECT "
		+ "pb.id, "
		+ "pb.fk_contact_id AS purchaseBillContactId, "
		+ "pb.bill_date AS billDate, "
		+ "c.name_default_lang AS nameDefaultLang, "
		+ "c.name_prefer_lang AS namePreferLang, "
		+ "c.name_supportive_lang AS nameSupportiveLang, "
		+ "SUM(pbrm.total_amount) AS totalAmount "
		+ "FROM purchase_bill pb "
		+ "LEFT JOIN purchase_bill_raw_material pbrm ON pbrm.fk_purchase_bill_id = pb.id "
		+ "LEFT JOIN contact c ON c.id = pb.fk_contact_id "
		+ "WHERE pb.id NOT IN(SELECT fk_purchase_bill_id FROM debit_note WHERE fk_purchase_bill_id IS NOT NULL) OR pb.id IN (:id) "
		+ "GROUP BY pb.id"
)
@SqlResultSetMapping(
	name = "debitNotePurchaseBillDropDownResult",
	classes = @ConstructorResult(
		targetClass = DebitNotePurchaseBillDropDownDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "purchaseBillContactId", type = Long.class),
			@ColumnResult(name = "billDate", type = LocalDate.class),
			@ColumnResult(name = "totalAmount", type = Double.class)
		}
	)
)

@NamedNativeQuery(
	name = "debitNotePurchaseBillRawMaterial",
	resultSetMapping = "debitNotePurchaseBillRawMaterialResult",
	query = "SELECT "
		+ "pbrm.fk_raw_material_id AS rawMaterialId, "
		+ "'' AS hsnCode, "
		+ "pbrm.weight, "
		+ "pbrm.fk_measurement_id AS measurementId, "
		+ "pbrm.price, "
		+ "pbrm.fk_tax_master_id AS taxMasterId, "
		+ "pbrm.total_amount AS totalAmount "
		+ "FROM purchase_bill_raw_material pbrm "
		+ "WHERE pbrm.fk_purchase_bill_id = :purchaseBillId"
)
@SqlResultSetMapping(
	name = "debitNotePurchaseBillRawMaterialResult",
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
	name = "debitNodeRawMaterialDropDown",
	resultSetMapping = "debitNodeRawMaterialDropDownResult",
	query = "SELECT "
		+ "rm.id, "
		+ "rm.name_default_lang AS nameDefaultLang, "
		+ "rm.name_prefer_lang AS namePreferLang, "
		+ "rm.name_supportive_lang AS nameSupportiveLang, "
		+ "rm.fk_tax_master_id AS taxMasterId, "
		+ "rm.fk_measurement_id AS measurementId "
		+ "FROM raw_material rm "
		+ "WHERE rm.is_active = TRUE "
		+ "ORDER BY rm.priority, rm.id"
)
@SqlResultSetMapping(
	name = "debitNodeRawMaterialDropDownResult",
	classes = @ConstructorResult(
		targetClass = DebitNoteRawMaterialDropDownDto.class,
		columns = {
			@ColumnResult(name = "id", type = Long.class),
			@ColumnResult(name = "nameDefaultLang", type = String.class),
			@ColumnResult(name = "namePreferLang", type = String.class),
			@ColumnResult(name = "nameSupportiveLang", type = String.class),
			@ColumnResult(name = "taxMasterId", type = Long.class),
			@ColumnResult(name = "measurementId", type = Long.class)
		}
	)
)

@NamedNativeQuery(
	name = "debitNoteListCalculation",
	resultSetMapping = "debitNoteListCalculationResult",
	query = "SELECT dn.id, "
		+ "IFNULL (SUM(dnrm.weight * dnrm.price) , 0) AS amount, "
		+ "IFNULL (SUM(CASE "
		+ "WHEN cp.gst_number IS NOT NULL AND c.gst_number IS NOT NULL AND SUBSTRING(cp.gst_number, 1, 2) != SUBSTRING(c.gst_number, 1, 2) "
		+ "THEN (dnrm.weight * dnrm.price * tm.igst) / 100 "
		+ "ELSE (dnrm.weight * dnrm.price * tm.sgst) / 100 + (dnrm.weight * dnrm.price * tm.cgst) / 100 "
		+ "END), 0) AS taxableAmount, "
		+ "IFNULL (SUM(dnrm.total_amount), 0) AS totalAmount "
		+ "FROM debit_note dn "
		+ "INNER JOIN debit_note_raw_material dnrm ON dnrm.fk_debit_note_id = dn.id "
		+ "INNER JOIN contact c ON c.id = dn.fk_contact_id "
		+ "LEFT JOIN tax_master tm ON tm.id = dnrm.fk_tax_master_id "
		+ "JOIN company_preferences cp "
		+ "WHERE dn.id = :id"
)
@SqlResultSetMapping(
	name = "debitNoteListCalculationResult",
	classes = @ConstructorResult(
		targetClass = CommonCalculationFieldDto.class,
		columns = {
			@ColumnResult(name = "amount", type = Double.class),
			@ColumnResult(name = "taxableAmount", type = Double.class),
			@ColumnResult(name = "totalAmount", type = Double.class)
		}
	)
)
@Entity
public class DebitNoteNativeQuery extends AuditIdModelOnly {
}