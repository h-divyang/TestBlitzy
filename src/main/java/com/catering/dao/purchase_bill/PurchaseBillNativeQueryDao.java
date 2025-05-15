package com.catering.dao.purchase_bill;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.PurchaseBillOrderDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialSupplierDto;

/**
 * DAO interface for performing native SQL queries related to Purchase Bill operations.
 * This interface uses {@link JpaRepository} to interact with the database and retrieve information
 * related to purchase orders, raw materials, calculations, and suppliers.
 */
public interface PurchaseBillNativeQueryDao extends JpaRepository<PurchaseBillNativeQuery, Long> {

	/**
	 * Fetches a list of PurchaseBillOrderDropDownDto objects based on the provided ID.
	 * This uses the 'purchaseBillOrderDropDown' named native query.
	 *
	 * @param id the ID of the purchase order to filter the results.
	 * @return a list of {@link PurchaseBillOrderDropDownDto} containing purchase order details.
	 */
	@Query(name = "purchaseBillOrderDropDown", nativeQuery = true)
	List<PurchaseBillOrderDropDownDto> getPurchaseBillOrderDropDown(Long id);

	/**
	 * Fetches a list of PurchaseBillOrderRawMaterialDto objects based on the purchase order ID.
	 * This uses the 'purchaseBillOrderRawMaterial' named native query.
	 *
	 * @param purchaseOrderId the ID of the purchase order to filter the results.
	 * @return a list of {@link PurchaseBillOrderRawMaterialDto} containing raw material details for the purchase order.
	 */
	@Query(name = "purchaseBillOrderRawMaterial", nativeQuery = true)
	List<PurchaseBillOrderRawMaterialDto> getPurchaseBillOrderRawMaterial(Long purchaseOrderId);

	/**
	 * Fetches the purchase bill calculation details for a specific purchase bill ID.
	 * This uses the 'purchaseBillListCalculation' named native query.
	 *
	 * @param id the ID of the purchase bill to calculate amounts.
	 * @return a {@link CommonCalculationFieldDto} containing calculated amounts (amount, taxable amount, total amount).
	 */
	@Query(name = "purchaseBillListCalculation", nativeQuery = true)
	CommonCalculationFieldDto getPurchaseBillListCalculation(Long id);

	/**
	 * Fetches a list of PurchaseBillRawMaterialSupplierDto objects based on the raw material ID.
	 * This uses the 'purchaseBillRawMaterialSupplier' named native query.
	 *
	 * @param id the ID of the raw material to filter the results.
	 * @return a list of {@link PurchaseBillRawMaterialSupplierDto} containing supplier details for the raw material.
	 */
	@Query(name = "purchaseBillRawMaterialSupplier", nativeQuery = true)
	List<PurchaseBillRawMaterialSupplierDto> getPurchaseBillRawMaterialSupplier(Long id);

	/**
	 * Fetches a list of PurchaseBillRawMaterialDropDownDto objects for the dropdown selection.
	 * This uses the 'purchaseBillRawMaterialDropDown' named native query.
	 *
	 * @return a list of {@link PurchaseBillRawMaterialDropDownDto} containing raw material details.
	 */
	@Query(name = "purchaseBillRawMaterialDropDown", nativeQuery = true)
	List<PurchaseBillRawMaterialDropDownDto> getPurchaseBillRawMaterial();

}