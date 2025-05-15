package com.catering.dao.purchase_bill;

import java.util.List;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.PurchaseBillOrderDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialSupplierDto;

/**
 * Service interface for performing operations related to Purchase Bill operations.
 * It abstracts the interactions with the data layer (e.g., DAO layer) to fetch information
 * about purchase orders, raw materials, calculations, and suppliers.
 */
public interface PurchaseBillNativeQueryService {

	/**
	 * Fetches a list of PurchaseBillOrderDropDownDto objects based on the provided ID.
	 *
	 * @param id the ID of the purchase order to filter the results.
	 * @return a list of {@link PurchaseBillOrderDropDownDto} containing purchase order details.
	 */
	List<PurchaseBillOrderDropDownDto> getPurchaseBillOrderDropDown(Long id);

	/**
	 * Fetches a list of PurchaseBillOrderRawMaterialDto objects based on the purchase order ID.
	 *
	 * @param purchaseOrderId the ID of the purchase order to filter the results.
	 * @return a list of {@link PurchaseBillOrderRawMaterialDto} containing raw material details for the purchase order.
	 */
	List<PurchaseBillOrderRawMaterialDto> getPurchaseBillOrderRawMaterial(Long purchaseOrderId);

	/**
	 * Fetches the purchase bill calculation details for a specific purchase bill ID.
	 *
	 * @param id the ID of the purchase bill to calculate amounts.
	 * @return a {@link CommonCalculationFieldDto} containing calculated amounts (amount, taxable amount, total amount).
	 */
	CommonCalculationFieldDto getPurchaseBillListCalculation(Long id);

	/**
	 * Fetches a list of PurchaseBillRawMaterialSupplierDto objects based on the raw material ID.
	 *
	 * @param id the ID of the raw material to filter the results.
	 * @return a list of {@link PurchaseBillRawMaterialSupplierDto} containing supplier details for the raw material.
	 */
	List<PurchaseBillRawMaterialSupplierDto> getPurchaseBillRawMaterialSupplier(Long id);

	/**
	 * Fetches a list of PurchaseBillRawMaterialDropDownDto objects for the dropdown selection.
	 *
	 * @return a list of {@link PurchaseBillRawMaterialDropDownDto} containing raw material details.
	 */
	List<PurchaseBillRawMaterialDropDownDto> getPurchaseBillRawMaterial();

}