package com.catering.dao.purchase_bill;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.PurchaseBillOrderDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillRawMaterialSupplierDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the PurchaseBillNativeQueryService interface.
 * Provides business logic to fetch and manipulate purchase bill data.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PurchaseBillNativeQueryServiceImpl implements PurchaseBillNativeQueryService {

	/**
	 * Data access object (DAO) to interact with the database for purchase bill-related queries.
	 * This is injected through constructor injection using the @RequiredArgsConstructor annotation.
	 */
	PurchaseBillNativeQueryDao purchaseBillNativeQueryDao;

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public List<PurchaseBillOrderDropDownDto> getPurchaseBillOrderDropDown(Long id) {
		return purchaseBillNativeQueryDao.getPurchaseBillOrderDropDown(id);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public List<PurchaseBillOrderRawMaterialDto> getPurchaseBillOrderRawMaterial(Long purchaseOrderId) {
		return purchaseBillNativeQueryDao.getPurchaseBillOrderRawMaterial(purchaseOrderId);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public CommonCalculationFieldDto getPurchaseBillListCalculation(Long id) {
		return purchaseBillNativeQueryDao.getPurchaseBillListCalculation(id);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public List<PurchaseBillRawMaterialSupplierDto> getPurchaseBillRawMaterialSupplier(Long id) {
		return purchaseBillNativeQueryDao.getPurchaseBillRawMaterialSupplier(id);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public List<PurchaseBillRawMaterialDropDownDto> getPurchaseBillRawMaterial() {
		return purchaseBillNativeQueryDao.getPurchaseBillRawMaterial();
	}

}