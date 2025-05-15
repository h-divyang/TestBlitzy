package com.catering.dao.purchase_order;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.PurchaseOrderContactDto;
import com.catering.dto.tenant.request.PurchaseOrderContactSupplierDataDto;
import com.catering.dto.tenant.request.PurchaseOrderRawMaterialDropDownDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link PurchaseOrderNativeQueryService} interface.
 * Executes native queries related to purchase orders.
 *
 * @since 2024-05-31
 * @author Krushali Talaviya
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PurchaseOrderNativeQueryServiceImpl implements PurchaseOrderNativeQueryService {

	/**
	 * The DAO interface for performing native SQL queries related to Purchase Orders.
	 * 
	 * This field represents the data access object (DAO) used to execute native queries for fetching
	 * purchase order-related data such as contacts, raw materials, pricing calculations, and supplier details.
	 * It leverages the `RawMaterialAllocationNativeQueryDao` interface and other custom methods defined to 
	 * interact with the database.
	 * 
	 * The DAO is injected into the service layer and is used for executing the queries defined by 
	 * the {@link NamedNativeQuery} annotations in the `PurchaseOrderNativeQuery` class.
	 */
	PurchaseOrderNativeQueryDao purchaseOrderNativeQueryDao;

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public List<PurchaseOrderContactDto> getContactList() {
		return purchaseOrderNativeQueryDao.getContactList();
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public List<PurchaseOrderRawMaterialDropDownDto> getRawMaterialList() {
		return purchaseOrderNativeQueryDao.getRawMaterialList();
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public CommonCalculationFieldDto getPurchaseListCalculation(Long id) {
		return purchaseOrderNativeQueryDao.getPurchaseListCalculation(id);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public List<PurchaseOrderContactSupplierDataDto> getContactSupplierForItem(Long id) {
		return purchaseOrderNativeQueryDao.getContactSupplierForItem(id);
	}

}