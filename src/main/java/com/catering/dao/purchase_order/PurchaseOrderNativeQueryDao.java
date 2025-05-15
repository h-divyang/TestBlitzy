package com.catering.dao.purchase_order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.PurchaseOrderContactDto;
import com.catering.dto.tenant.request.PurchaseOrderContactSupplierDataDto;
import com.catering.dto.tenant.request.PurchaseOrderRawMaterialDropDownDto;

/**
 * Repository interface for executing native queries related to purchase orders.
 * Provides methods for retrieving contact lists, raw material lists, and purchase order calculations.
 *
 * @author Krushali Talaviya
 * @since 2024-05-31
 */
public interface PurchaseOrderNativeQueryDao extends JpaRepository<PurchaseOrderNativeQuery, Long> {

	/**
	 * Retrieves a list of contacts for purchase orders.
	 *
	 * @return A list of PurchaseOrderContactDto objects representing the contacts.
	 */
	@Query(name = "purchaseOrderContactDropDown", nativeQuery = true)
	List<PurchaseOrderContactDto> getContactList();

	/**
	 * Retrieves a list of raw materials for purchase orders.
	 *
	 * @return A list of PurchaseOrderRawMaterialDropDownDto objects representing the raw materials.
	 */
	@Query(name = "purchaseOrderRawMaterialDropDown", nativeQuery = true)
	List<PurchaseOrderRawMaterialDropDownDto> getRawMaterialList();

	/**
	 * Retrieves the calculation field for a purchase order based on its ID.
	 *
	 * @param id The ID of the purchase order.
	 * @return A PurchaseOrderCalculationFieldDto object representing the calculation field.
	 */
	@Query(name = "purchaseOrderListCalculation", nativeQuery = true)
	CommonCalculationFieldDto getPurchaseListCalculation(Long id);

	/**
	 * Retrieves the list of supplier details for a given item ID.
	 * 
	 * @param id the ID of the item
	 * @return a list of PurchaseOrderContactSupplierDataDto objects containing the supplier details
	 */
	@Query(name = "purchaseOrderContactSupplier", nativeQuery = true)
	List<PurchaseOrderContactSupplierDataDto> getContactSupplierForItem(Long id);

}